package demo.e2e

import com.microsoft.playwright.Page
import com.microsoft.playwright.junit.UsePlaywright
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
        assertChartRendered(page)
    }

    @RetryingTest(maxAttempts = 2)
    fun `a deep link renders the example and its official link`(page: Page) {
        openPage(page, "#/gauge-grade")

        val heading = page.locator("h1").textContent()!!
        assertTrue(heading.contains("等级仪表盘"), "heading: $heading")
        val official = page.locator(".page__links a").first().getAttribute("href")!!
        assertTrue(official.contains("editor.html?c=gauge-grade"), "official link: $official")
        assertChartRendered(page)
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
        assertChartRendered(page)
    }

    @RetryingTest(maxAttempts = 2)
    fun `the theme switch re-creates the chart in dark mode`(page: Page) {
        openPage(page, "#/line-simple")

        page.locator(".theme-switch button").nth(1).click()
        page.waitForFunction("() => document.documentElement.getAttribute('data-theme') === 'dark'")

        assertChartRendered(page)
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
        assertChartRendered(page)
        openFromSidebar(page, "sankey", "sankey-vertical")
        assertChartRendered(page)
        openFromSidebar(page, "heatmap", "heatmap-cartesian")
        assertChartRendered(page)

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
        assertChartRendered(page)
        waitForPainted(page, "matrix-simple")

        val painted =
            listOf(
                "custom-bar-trend",
                "custom-polar-heatmap",
                "matrix-covariance",
                "matrix-confusion",
                "matrix-sparkline",
                "pie-nest",
                // Applied after a fetch, so it also proves the static datasets are served.
                "treemap-disk",
            )
        for (route in painted) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
            assertChartRendered(page)
            waitForPainted(page, route)
        }
    }

    /**
     * Waits until the chart canvas holds a painted pixel.
     *
     * The pages that fetch their data set the option later, so a single sample would race with the
     * request; the loop samples the alpha channel until something shows up.
     */
    private fun waitForPainted(page: Page, route: String) {
        try {
            page.waitForFunction(
                """
                () => {
                    const canvas = document.querySelector('.chart canvas');
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
     */
    private fun assertChartRendered(page: Page, route: String? = null) {
        if (route != null) {
            page.evaluate("(value) => { location.hash = '#/' + value }", route)
        }
        try {
            page.waitForFunction("() => document.querySelector('.chart canvas') !== null")
        } catch (error: com.microsoft.playwright.TimeoutError) {
            throw AssertionError("route $route did not mount a chart canvas", error)
        }
        val canvas = page.locator(".chart canvas").first()
        val box = canvas.boundingBox()
        assertTrue(box != null && box.width > 0 && box.height > 0, "chart canvas box of $route: $box")
    }

    private fun openPage(page: Page, hash: String = "") {
        page.navigate("/$hash", Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE))
    }
}
