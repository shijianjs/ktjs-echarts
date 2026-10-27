package demo.e2e

import com.microsoft.playwright.Page
import com.microsoft.playwright.junit.UsePlaywright
import com.microsoft.playwright.options.WaitUntilState
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
     * Walks a sample of the catalog (every sixth example) and asserts that each route renders a
     * sized canvas, which catches a page that throws while building its option.
     */
    @Test
    fun `a sample of the catalog renders without errors`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        openPage(page)

        val routes =
            (page.locator(".example-card").evaluateAll("(nodes) => nodes.map((node) => node.dataset.route)") as List<*>)
                .filterIsInstance<String>()
                .filterIndexed { index, _ -> index % 6 == 0 }
        assertTrue(routes.size >= 8, "the sample covers the catalog, got ${routes.size} routes")

        routes.forEach { route -> assertChartRendered(page, route) }
        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
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
        page.waitForFunction("() => document.querySelector('.chart canvas') !== null")
        val canvas = page.locator(".chart canvas").first()
        val box = canvas.boundingBox()
        assertTrue(box != null && box.width > 0 && box.height > 0, "chart canvas box: $box")
    }

    private fun openPage(page: Page, hash: String = "") {
        page.navigate("/$hash", Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE))
    }
}
