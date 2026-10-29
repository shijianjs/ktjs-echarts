package demo.e2e

import com.microsoft.playwright.Page
import com.microsoft.playwright.junit.UsePlaywright
import com.microsoft.playwright.options.BoundingBox
import com.microsoft.playwright.options.WaitUntilState
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junitpioneer.jupiter.RetryingTest

/**
 * End-to-end checks for the example gallery.
 *
 * The suite drives the published bundle the way a visitor does: it walks the sidebar, switches the
 * language and the theme, filters the index, and asserts that the visited pages render their ECharts
 * canvas without a console error or a failed request.
 */
@UsePlaywright(DemoOptions::class)
class DemoPageTest {
    @RetryingTest(maxAttempts = 2)
    fun `overview lists every example and applies its stylesheet`(page: Page) {
        openPage(page)

        assertTrue(page.title().contains("ktjs-echarts"), "title: ${page.title()}")
        assertTrue(page.locator(".shell__nav").isVisible, "the sidebar is visible")

        val cards = page.locator(".example-card").count()
        assertTrue(cards > 50, "the index lists the ported examples, got $cards cards")

        val display = page.locator(".page").first().evaluate("(node) => getComputedStyle(node).display") as String
        assertTrue(display != "contents", "the stylesheet applies to .page (display = $display)")
    }

    @RetryingTest(maxAttempts = 2)
    fun `sidebar navigation renders the chart of the chosen example`(page: Page) {
        openPage(page)
        openFromSidebar(page, "bar", "bar-simple")

        val breadcrumb = page.locator(".topbar__breadcrumb").textContent()!!
        assertTrue(breadcrumb.contains("基础柱状图"), "breadcrumb: $breadcrumb")
        assertChartRendered(page, "bar-simple")
    }

    @RetryingTest(maxAttempts = 2)
    fun `a deep link renders the example and its official link`(page: Page) {
        openPage(page, "#/gauge-grade")

        val heading = page.locator("h1").textContent()!!
        assertTrue(heading.contains("等级仪表盘"), "heading: $heading")
        val official = page.locator(".page__links a").first().getAttribute("href")!!
        assertTrue(official.contains("editor.html?c=gauge-grade"), "official link: $official")
        assertChartRendered(page, "gauge-grade")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the index filter hides the cards that do not match`(page: Page) {
        openPage(page)

        val total = page.locator(".example-card").count()
        page.locator(".index__search").fill("gauge")
        page.waitForFunction("() => document.querySelectorAll('.example-card:not(.is-hidden)').length < 50")

        val visible = page.locator(".example-card:not(.is-hidden)").count()
        assertTrue(visible in 1 until total, "filtering keeps $visible of $total cards")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the language switch rebuilds the page in the other language`(page: Page) {
        openPage(page, "#/pie-simple")

        val chineseHeading = page.locator("h1").textContent()!!
        page.locator(".lang-switch button").nth(1).click()
        page.waitForFunction("() => document.documentElement.lang === 'en'")

        val englishHeading = page.locator("h1").textContent()!!
        assertTrue(
            englishHeading != chineseHeading && englishHeading.contains("Referer"),
            "heading is re-resolved: $englishHeading",
        )
        assertChartRendered(page, "pie-simple")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the theme switch re-creates the chart in dark mode`(page: Page) {
        openPage(page, "#/line-simple")

        page.locator(".theme-switch button").nth(1).click()
        page.waitForFunction("() => document.documentElement.getAttribute('data-theme') === 'dark'")

        assertChartRendered(page, "line-simple")
    }

    /**
     * The two theme buttons share one highlighted style, so exactly one of them may carry `is-on`,
     * and its label has to stay readable against the accent of the current theme.
     */
    @RetryingTest(maxAttempts = 2)
    fun `exactly one theme button is highlighted and stays readable`(page: Page) {
        openPage(page)
        page.evaluate("() => { localStorage.clear(); location.hash = '#/bar-simple' }")

        assertEquals(1, page.locator(".theme-switch button.is-on").count(), "one highlighted theme button")

        page.locator(".theme-switch button").nth(1).click()
        page.waitForFunction("() => document.documentElement.getAttribute('data-theme') === 'dark'")
        assertEquals(1, page.locator(".theme-switch button.is-on").count(), "one highlighted theme button after switching")
        assertTrue(
            page.locator(".theme-switch button.is-on").textContent()!!.contains("暗色"),
            "the dark button is the highlighted one",
        )
        assertTrue(contrast(page, ".theme-switch button.is-on") >= 4.5, "the highlighted label contrasts with its background")

        page.locator(".theme-switch button").nth(0).click()
        page.waitForFunction("() => document.documentElement.getAttribute('data-theme') !== 'dark'")
        assertEquals(1, page.locator(".theme-switch button.is-on").count(), "one highlighted theme button after switching back")
        assertTrue(
            page.locator(".theme-switch button.is-on").textContent()!!.contains("亮色"),
            "the light button is the highlighted one",
        )
        assertTrue(contrast(page, ".theme-switch button.is-on") >= 4.5, "the highlighted label contrasts with its background")
    }

    /** Computes the WCAG contrast ratio between the text and the background of [selector]. */
    private fun contrast(page: Page, selector: String): Double =
        page.locator(selector).first().evaluate(
            """
            (node) => {
                const parse = (value) => {
                    const parts = value.match(/[\d.]+/g).map(Number);
                    return parts.length >= 3 ? parts.slice(0, 3) : [0, 0, 0];
                };
                const channel = (value) => {
                    const c = value / 255;
                    return c <= 0.03928 ? c / 12.92 : Math.pow((c + 0.055) / 1.055, 2.4);
                };
                const luminance = (rgb) => 0.2126 * channel(rgb[0]) + 0.7152 * channel(rgb[1]) + 0.0722 * channel(rgb[2]);
                const style = getComputedStyle(node);
                const foreground = luminance(parse(style.color));
                const background = luminance(parse(style.backgroundColor));
                const lighter = Math.max(foreground, background);
                const darker = Math.min(foreground, background);
                return (lighter + 0.05) / (darker + 0.05);
            }
            """,
        ) as Double

    @RetryingTest(maxAttempts = 2)
    fun `the gallery logs no console errors or failed requests`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        val failedRequests = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        page.onRequestFailed { request -> failedRequests += "${request.url()} ${request.failure()}" }

        openPage(page)
        openFromSidebar(page, "boxplot", "boxplot-light-velocity")
        assertChartRendered(page, "boxplot-light-velocity")
        openFromSidebar(page, "sankey", "sankey-vertical")
        assertChartRendered(page, "sankey-vertical")
        openFromSidebar(page, "heatmap", "heatmap-cartesian")
        assertChartRendered(page, "heatmap-cartesian")

        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
        assertTrue(failedRequests.isEmpty(), "failed requests: $failedRequests")
    }

    /**
     * Walks the whole catalog and asserts that each route renders a sized canvas, which catches a
     * page that throws while building its option. Every route is visited: a sample missed a page
     * whose option builder indexed past the end of a table.
     */
    @Test
    fun `every example of the catalog renders without errors`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        openPage(page)

        val routes =
            (page.locator(".example-card").evaluateAll("(nodes) => nodes.map((node) => node.dataset.route)") as List<*>)
                .filterIsInstance<String>()
        assertTrue(routes.size >= 100, "the catalog should ship every example, got ${routes.size} routes")

        routes.forEach { route -> assertChartRendered(page, route) }
        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
    }

    /**
     * Asserts that the newest coordinate systems really paint: a canvas that exists but stays
     * transparent would pass [assertChartRendered] on its own.
     *
     * `matrix-covariance` guards the nested matrix header (a flat header renders nothing) and
     * `matrix-confusion` the custom series `type` — without it the cells stay empty and the labels
     * pile up in the corner.
     */
    @RetryingTest(maxAttempts = 2)
    fun `the matrix and custom series pages draw content`(page: Page) {
        openPage(page, "#/matrix-simple")
        assertChartRendered(page, "matrix-simple")
        waitForPainted(page, "matrix-simple")

        val painted =
            listOf(
                "custom-bar-trend",
                "custom-wind",
                "custom-hexbin",
                "custom-profile",
                "geo-svg-custom-effect",
                "custom-polar-heatmap",
                "matrix-covariance",
                "matrix-confusion",
                "matrix-sparkline",
                "pie-nest",
                // Applied after a fetch, so it also proves the static datasets are served.
                "treemap-disk",
                "treemap-obama",
            )
        for (route in painted) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page, route)
            waitForPainted(page, route)
        }
    }

    /**
     * The `echarts-gl` pages of `Gl.kt`, `GlScatter.kt`, `GlSurfaceMaterial.kt` and `GlGeo.kt` paint
     * into a WebGL layer, which is a canvas of its own next to the (empty) zrender canvas layer. A
     * surface with a broken equation or a missing `visualMap` dimension only logs an error and stays
     * blank, which is what the console assertion catches; the `scatter3D` pages fetch their rows
     * first, so a `dataset` whose `encode.z` never arrived would leave the layer empty without an
     * error of its own.
     *
     * The leather and image surfaces wait for an image before they set any option, so those layers
     * are only expected to be there once the `load` handler ran. The `geo3D` pages wait for
     * `data/world.json` and the globe pages for their textures, which the same wait covers.
     *
     * A missing core module of `echarts-gl` (its `sideEffects` list does not cover the nested
     * installers, so a bundler may drop the module that registers the post-update hook) leaves the
     * option accepted and the page blank; a browser without WebGL replaces the layer with an
     * `.ecgl-nowebgl` placeholder. Both are asserted against here.
     */
    @RetryingTest(maxAttempts = 2)
    fun `the 3d pages paint through the webgl layer`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        openPage(page)

        val routes =
            listOf(
                "bar3d-punch-card",
                "transparent-bar3d",
                "bar3d-dataset",
                "line3d-orthographic",
                "scatter3d",
                "scatter3d-scatter",
                "scatter3D-dataset",
                "simple-surface",
                "sphere-parametric-surface",
                "surface-wave",
                "metal-surface",
                "surface-breather",
                "surface-mollusc-shell",
                "surface-golden-rose",
                "surface-theme-roses",
                "parametric-surface-rose",
                "surface-leather",
                "image-surface-sushuang",
                "geo3d",
                "geo3d-with-different-height",
                "globe-echarts-gl-hello-world",
                "globe-atmosphere",
                "globe-displacement",
                "globe-moon",
                "globe-layers",
                "scatter3d-globe-population",
                "globe-with-echarts-surface",
            )
        for (route in routes) {
            assertChartRendered(page, route)
            page.waitForFunction(
                """
                (value) => {
                    const canvas = document.querySelector('.page[data-route="' + value + '"] .chart canvas');
                    if (canvas === null) {
                        return false;
                    }
                    return canvas.getContext('webgl2') !== null || canvas.getContext('webgl') !== null;
                }
                """,
                route,
            )
            assertEquals(0, page.locator(".ecgl-nowebgl").count(), "WebGL is not available on $route")
        }
        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
    }

    /**
     * The `doc-example` pages of `DocInteractive.kt` paint on their own: a timer replaces the data,
     * `showLoading` covers the canvas for three seconds, the sunburst pages dispatch an action after
     * the first frame, and `bar-dataZoom-filterMode` / `candlestick-axisPointer` set the option only
     * once their data file has been fetched. Sampling once right after the route switch would miss all
     * of that, so each page is left running long enough for at least one tick of its timer; an
     * exception in a timer callback is what the console assertion catches. `mix-timeline-all` is
     * checked one step further: its timeline autoplays, so a frame sampled after the play interval
     * must differ from the first one, which is what proves the per-year options reached the chart.
     * `tutorial-async` starts with empty arrays, so its check counts content instead of comparing
     * frames: the deferred option has to put noticeably more pixels on the canvas.
     */
    @Test
    fun `the interactive doc examples paint and run their timers`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        openPage(page)
        val routes =
            listOf(
                "doc-example/barGrid-barGap",
                "doc-example/tutorial-dynamic-data",
                "doc-example/tutorial-loading",
                "doc-example/sunburst-highlight-ancestor",
                "doc-example/sunburst-highlight-descendant",
                "doc-example/graphic-bounding",
                "doc-example/timeline-dynamic-series",
                "doc-example/pictorialBar-clip",
                "doc-example/bar-dataZoom-filterMode",
                "doc-example/candlestick-axisPointer",
                "doc-example/grid-outerBounds",
                "doc-example/mix-timeline-all",
                "doc-example/tutorial-async",
            )
        for (route in routes) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page, route)
            waitForPainted(page, route)
            if (route == "doc-example/tutorial-loading") {
                // The loading effect occupies the canvas until the faked request resolves.
                page.waitForTimeout(3300.0)
                assertChartRendered(page, route)
            }
            if (route == "doc-example/mix-timeline-all") {
                // The timeline autoplays one year per second, so a year must reach the canvas.
                val before = paintedFrame(page)
                page.waitForTimeout(1600.0)
                val after = paintedFrame(page)
                assertTrue(before != after, "the timeline of $route did not switch to the next year")
            }
            if (route == "doc-example/tutorial-async") {
                // The page starts with empty arrays, so the axis labels and the bars only arrive with
                // the asynchronous option; the canvas must therefore hold noticeably more content.
                val before = paintedPixels(page)
                page.waitForTimeout(1800.0)
                val after = paintedPixels(page)
                assertTrue(after > before * 1.2, "$route stayed empty: $before painted pixels before, $after after")
            }
        }
        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
    }

    /**
     * The dial pages of `GaugeLongTail.kt` draw their whole identity from `path://` needles, so a
     * missing icon leaves a canvas that only holds the ring: the static pages are checked for painted
     * pixels. The three animated ones replace their values from a timer, so a frame sampled after one
     * tick of that timer must differ from the first one; an exception inside the callback is what the
     * console assertion catches.
     */
    @RetryingTest(maxAttempts = 2)
    fun `the gauge long tail pages draw and move their needles`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        openPage(page)
        for (route in listOf("gauge-speed", "gauge-car")) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page, route)
            waitForPainted(page, route)
        }
        for (route in listOf("gauge-clock", "gauge-barometer", "gauge-multi-title")) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page, route)
            waitForPainted(page, route)
            val before = paintedFrame(page)
            page.waitForTimeout(2400.0)
            val after = paintedFrame(page)
            assertTrue(before != after, "$route did not repaint after its timer tick")
        }
        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
    }

    /**
     * The generated-data pages of `LineLongTail.kt`. `area-time-axis` plots twenty thousand points and
     * `line-easing` thirty grids of their own: a page that fails to build its arrays leaves an empty
     * canvas, so those are checked for painted pixels. `dynamic-data2` shifts its window once a
     * second, so a frame sampled after one tick must differ from the first one; an exception inside
     * the timer callback is what the console assertion catches.
     */
    @RetryingTest(maxAttempts = 2)
    fun `the line long tail pages draw and move their window`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        openPage(page)
        for (route in listOf("area-time-axis", "line-easing", "area-pieces", "doc-example/matrix-graph-box")) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page, route)
            waitForPainted(page, route)
        }
        val route = "dynamic-data2"
        page.evaluate("(value) => { location.hash = '#/' + value }", route)
        assertChartRendered(page, route)
        waitForPainted(page, route)
        val before = paintedFrame(page)
        page.waitForTimeout(2400.0)
        val after = paintedFrame(page)
        assertTrue(before != after, "$route did not repaint after its timer tick")
        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
    }

    /**
     * The computed-stack pages of `BarLongTail.kt`, the cartesian graph of `GraphExtra.kt` and the two
     * lines pages of `LineLongTail.kt` that draw their shape out of a table.
     *
     * `bar-stack-normalization` and `bar-stack-borderRadius` build their bars out of the same array of
     * counts, so a mistake in that arithmetic shows up as an empty or half-empty canvas;
     * `bar-waterfall2` hides its placeholder series with a transparent style, which leaves the two
     * visible segments as the only content. The variation page is compared against the plain
     * normalization page: it draws the same bars plus the translucent polygons that connect them, so
     * it has to hold strictly more painted pixels — a graphic layer that never got built would leave
     * the two counts equal.
     */
    @RetryingTest(maxAttempts = 2)
    fun `the bar long tail pages draw their computed stacks`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        openPage(page)
        for (
            route in
                listOf(
                    "bar1",
                    "bar-stack-borderRadius",
                    "bar-waterfall2",
                    "graph-grid",
                    "bump-chart",
                    "line-graphic",
                )
        ) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page, route)
            waitForPainted(page, route)
        }
        page.evaluate("(value) => { location.hash = '#/' + value }", "bar-stack-normalization")
        assertChartRendered(page, "bar-stack-normalization")
        waitForPainted(page, "bar-stack-normalization")
        val plainBars = paintedPixels(page)
        page.evaluate("(value) => { location.hash = '#/' + value }", "bar-stack-normalization-and-variation")
        assertChartRendered(page, "bar-stack-normalization-and-variation")
        waitForPainted(page, "bar-stack-normalization-and-variation")
        val withPolygons = paintedPixels(page)
        assertTrue(
            withPolygons > plainBars,
            "the polygons of bar-stack-normalization-and-variation are missing: " +
                "$plainBars painted pixels without them, $withPolygons with them",
        )
        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
    }

    /**
     * The keyframe-driven `graphic` pages of `GraphicAnimations.kt`, the sampled perlin fields of
     * `HeatmapLongTail.kt`, the `cycle-plot` polylines and the two pages that only reach their final
     * shape over time.
     *
     * The stroke page starts from `lineDash: [0, 200]`, so nothing is drawn until the animation reaches
     * its `lineDash: [200, 0]` keyframe — a missing `keyframeAnimation` would therefore leave the canvas
     * empty, which is what the painted-pixel wait catches. The stroke and wave pages loop, so a frame
     * sampled one second later must differ. `graph-force-dynamic` adds a node every 200 ms, so its
     * canvas can only grow; `dataset-link` rewrites the pie through an `updateAxisPointer` listener that
     * never fires on its own, so it is checked as a static page.
     */
    @RetryingTest(maxAttempts = 2)
    fun `the graphic keyframe pages animate and the dynamic page grows`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        openPage(page)
        for (
            route in
                listOf(
                    "heatmap-large",
                    "heatmap-large-piecewise",
                    "cycle-plot",
                    "dataset-link",
                    "graphic-loading",
                    "graphic-stroke-animation",
                    "graphic-wave-animation",
                    "graph-force-dynamic",
                )
        ) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page, route)
            waitForPainted(page, route)
        }
        for (route in listOf("graphic-stroke-animation", "graphic-wave-animation")) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page, route)
            waitForPainted(page, route)
            val before = paintedFrame(page)
            page.waitForTimeout(1500.0)
            val after = paintedFrame(page)
            assertTrue(before != after, "$route did not repaint its keyframe animation")
        }
        page.evaluate("(value) => { location.hash = '#/' + value }", "graph-force-dynamic")
        assertChartRendered(page, "graph-force-dynamic")
        waitForPainted(page, "graph-force-dynamic")
        val first = paintedPixels(page)
        page.waitForTimeout(2500.0)
        val later = paintedPixels(page)
        assertTrue(
            later > first,
            "graph-force-dynamic stopped adding nodes: $first painted pixels at first, $later after 2.5s",
        )
        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
    }

    /**
     * The pages of the breaks / generated-data batch: the two intraday charts of ECharts 6, the
     * twenty-thousand point area page and the two hundred thousand candle page.
     *
     * The intraday pages build their own minute series, so a wrong walk would either loop forever
     * (a break that never advances the clock) or leave an empty canvas; the candle page turns two
     * hundred thousand rows into a `dataset`, which the painted-pixel wait proves reached the
     * chart. `scatter-matrix` is the one page here whose grid positions are computed: a percentage
     * that slipped back to pixels would stack every cell on the top-left corner, so it is checked
     * for painted pixels as well.
     */
    @RetryingTest(maxAttempts = 2)
    fun `the breaks and generated pages draw content`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        openPage(page)
        for (
            route in
                listOf(
                    "intraday-breaks-1",
                    "intraday-breaks-2",
                    "area-simple",
                    "candlestick-large",
                    "scatter-matrix",
                    "parallel-aqi",
                    "watermark",
                    "scatter-anscombe-quartet",
                )
        ) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page, route)
            waitForPainted(page, route)
        }
        // `bar-race` grows its bars from a timer, so a frame after one tick must differ.
        page.evaluate("(value) => { location.hash = '#/' + value }", "bar-race")
        assertChartRendered(page, "bar-race")
        waitForPainted(page, "bar-race")
        val before = paintedFrame(page)
        page.waitForTimeout(3600.0)
        val after = paintedFrame(page)
        assertTrue(before != after, "bar-race did not repaint after its timer tick")
        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
    }

    /**
     * Waits until the chart canvas of [route] holds a painted pixel.
     *
     * The pages that fetch their data set the option later, so a single sample would race with the
     * request; the loop samples the alpha channel until something shows up. The canvas is looked up
     * inside the page of [route] for the same reason [assertChartRendered] does — the previous page is
     * still mounted while the router has not reacted to the hash yet.
     */
    private fun waitForPainted(page: Page, route: String) {
        try {
            page.waitForFunction(
                """
                () => {
                    const canvas = document.querySelector(".page[data-route='$route'] .chart canvas");
                    if (!canvas) {
                        return false;
                    }
                    const context = canvas.getContext('2d');
                    if (!context) {
                        return false;
                    }
                    const data = context.getImageData(0, 0, canvas.width, canvas.height).data;
                    for (let index = 3; index < data.length; index += 4 * 97) {
                        if (data[index] !== 0) {
                            return true;
                        }
                    }
                    return false;
                }
                """,
                null,
                Page.WaitForFunctionOptions().setTimeout(15000.0),
            )
        } catch (error: com.microsoft.playwright.TimeoutError) {
            throw AssertionError("route $route did not paint any pixel", error)
        }
    }

    /**
     * Fingerprint of what the chart canvas currently holds.
     *
     * Only the pixels that carry content contribute, so a page that repaints with different data
     * changes the value while an idle page does not.
     *
     * Playwright hands a JS integer back in the narrowest box that fits, so the sampling helpers read
     * it through [Number] instead of casting to [Double].
     */
    private fun paintedFrame(page: Page): Double =
        (
            page.evaluate(
                """
                () => {
                    const canvas = document.querySelector('.chart canvas');
                    const context = canvas.getContext('2d');
                    const data = context.getImageData(0, 0, canvas.width, canvas.height).data;
                    let hash = 0;
                    let painted = 0;
                    for (let index = 3; index < data.length; index += 4 * 7) {
                        if (data[index] !== 0) {
                            painted++;
                            hash = (hash * 31 + data[index - 3] + data[index - 2] * 7 + data[index - 1] * 13) % 1000000007;
                        }
                    }
                    return painted * 1000000007 + hash;
                }
                """,
            ) as Number
                ).toDouble()

    /**
     * How much of the chart canvas currently carries content.
     *
     * A page that starts from empty arrays can only grow, so the count tells whether a deferred option
     * ever reached the chart, which a frame comparison cannot prove while other animation runs.
     */
    private fun paintedPixels(page: Page): Double =
        (
            page.evaluate(
                """
                () => {
                    const canvas = document.querySelector('.chart canvas');
                    const context = canvas.getContext('2d');
                    const data = context.getImageData(0, 0, canvas.width, canvas.height).data;
                    let painted = 0;
                    for (let index = 3; index < data.length; index += 4 * 7) {
                        if (data[index] !== 0) {
                            painted++;
                        }
                    }
                    return painted;
                }
                """,
            ) as Number
                ).toDouble()

    /** Opens [route] through the sidebar, the way a visitor reaches it. */
    private fun openFromSidebar(page: Page, group: String, route: String) {
        // Groups start collapsed except the active one, so expand the group before clicking.
        page.locator(".nav__group-title[data-group='$group']").click()
        val link = page.locator(".nav__link[data-route='$route']")
        link.waitFor()
        link.click()
        page.waitForFunction("(route) => location.hash === '#/' + route", route)
        page.waitForFunction("() => document.querySelector('.chart canvas') !== null")
    }

    /**
     * Waits until the current page holds a laid-out ECharts canvas.
     *
     * A chart instance is created on mount and measures its container, so a rendered canvas with a
     * non-zero box is exactly the signal that the option survived the mount.
     *
     * With a [route] the canvas is looked up inside the page of that route: the shell swaps pages from
     * the `hashchange` handler, which the browser dispatches only after the hash already changed, so an
     * unscoped wait can pass on the page that is still mounted. Reading the box through the locator
     * re-resolves the selector, so a null box means the swap landed in between and the check is retried
     * until the box of the page that stayed is measured.
     */
    private fun assertChartRendered(page: Page, route: String? = null) {
        if (route != null) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
        }
        val selector = if (route == null) ".chart canvas" else ".page[data-route='$route'] .chart canvas"
        var lastBox: BoundingBox? = null
        repeat(3) {
            try {
                page.waitForFunction(
                    """
                    (scope) => {
                        const canvas = document.querySelector(scope);
                        if (canvas === null) {
                            return false;
                        }
                        const box = canvas.getBoundingClientRect();
                        return box.width > 0 && box.height > 0;
                    }
                    """,
                    selector,
                )
            } catch (error: com.microsoft.playwright.TimeoutError) {
                throw AssertionError("route $route did not mount a laid-out chart canvas", error)
            }
            val box = page.locator(selector).first().boundingBox()
            if (box != null && box.width > 0 && box.height > 0) {
                return
            }
            lastBox = box
        }
        throw AssertionError("chart canvas box of $route stayed $lastBox after three samples")
    }

    private fun openPage(page: Page, hash: String = "") {
        page.navigate("/$hash", Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE))
    }
}
