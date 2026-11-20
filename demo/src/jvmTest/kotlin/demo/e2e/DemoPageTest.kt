package demo.e2e

import com.microsoft.playwright.Page
import com.microsoft.playwright.junit.UsePlaywright
import com.microsoft.playwright.options.WaitUntilState
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junitpioneer.jupiter.RetryingTest

/**
 * Smoke checks for the gallery: the shared shell runs, the drawer drives the router, charts mount inside
 * the page, the KMP model pages show both builders, and the shell's own features (filter, language
 * switch, one scrollbar) still work.
 *
 * The shell lives in a shadow tree, so the demo's own nodes are selected through `#demo` and the page
 * element the shell mounts — never through the document root.
 */
@UsePlaywright(DemoOptions::class)
class DemoPageTest {
    @RetryingTest(maxAttempts = 2)
    fun `overview lists every group and every example`(page: Page) {
        openPage(page)

        assertTrue(page.title().contains("ktjs-echarts"), "title: ${page.title()}")
        assertTrue(page.locator("#demo img.layout__logo").isVisible(), "the shell serves the project logo")
        assertEquals("Example overview", page.locator("#demo :is(echarts-example-page, echarts-overview-page) h1").first().textContent())

        // One drawer row per group, one card per ported example.
        val groups = page.locator("#demo vaadin-side-nav-item").count()
        assertTrue(groups > 25, "drawer rows: $groups")
        val cards = page.locator("#demo :is(echarts-example-page, echarts-overview-page) a.example-card").count()
        assertTrue(cards > 300, "index cards: $cards")
    }

    @RetryingTest(maxAttempts = 2)
    fun `bar simple mounts a chart inside the page`(page: Page) {
        openPage(page)
        openRoute(page, "bar-simple")

        assertEquals("Basic Bar", page.locator("#demo :is(echarts-example-page, echarts-overview-page) h1").first().textContent())
        waitForChart(page)

        val box = chartBox(page)
        assertTrue(box.first > 400, "chart width: ${box.first}")
        assertTrue(box.second > 240, "chart height: ${box.second}")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the json switch feeds the same chart from the model output`(page: Page) {
        openPage(page)
        openRoute(page, "bar-simple")

        assertEquals(1, page.locator("#demo :is(echarts-example-page, echarts-overview-page) vaadin-switch").count())
        page.locator("#demo :is(echarts-example-page, echarts-overview-page) vaadin-switch").first().evaluate("(el) => { el.checked = true }")
        waitForChart(page)

        // Re-initialising leaves one canvas behind, and the JSON card stayed where it was.
        assertEquals(1, page.locator("#demo :is(echarts-example-page, echarts-overview-page) demo-chart canvas").count())
        assertEquals(2, page.locator("#demo :is(echarts-example-page, echarts-overview-page) .section__title").count())
    }

    @RetryingTest(maxAttempts = 2)
    fun `a kmp model page shows the builder, the json and the chart`(page: Page) {
        openPage(page)
        openRoute(page, "kmp-json/line-smooth")

        assertEquals("Smooth line", page.locator("#demo :is(echarts-example-page, echarts-overview-page) h1").first().textContent())
        assertEquals(
            listOf("Chart", "Kotlin builder", "JSON option"),
            page.locator("#demo :is(echarts-example-page, echarts-overview-page) .section__title").allInnerTexts(),
        )
        assertEquals(2, page.locator("#demo :is(echarts-example-page, echarts-overview-page) code.code").count())
        waitForChart(page)
    }

    @RetryingTest(maxAttempts = 2)
    fun `the index filter narrows the cards`(page: Page) {
        openPage(page)
        val all = page.locator("#demo :is(echarts-example-page, echarts-overview-page) a.example-card").count()

        page.locator("#demo :is(echarts-example-page, echarts-overview-page) vaadin-text-field").first().evaluate(
            "(el, value) => { el.value = value; el.dispatchEvent(new Event('input')) }",
            "gauge",
        )
        val visible = page.locator("#demo :is(echarts-example-page, echarts-overview-page) a.example-card:not([hidden])").count()
        assertTrue(visible < all, "visible $visible of $all")
        assertTrue(visible > 0, "the filter matched at least one example")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the language switch rewrites the page in place`(page: Page) {
        openPage(page)
        openRoute(page, "kmp-json/line-smooth")
        val jsonBefore = page.locator("#demo :is(echarts-example-page, echarts-overview-page) code.code").nth(1).textContent()

        switchLanguage(page, "zh-Hans")
        assertEquals("平滑折线图", page.locator("#demo :is(echarts-example-page, echarts-overview-page) h1").first().textContent())
        assertEquals(
            listOf("图表", "Kotlin 构造", "JSON 选项"),
            page.locator("#demo :is(echarts-example-page, echarts-overview-page) .section__title").allInnerTexts(),
        )

        // The serialized option is data: a language switch must not re-run the model.
        assertEquals(jsonBefore, page.locator("#demo :is(echarts-example-page, echarts-overview-page) code.code").nth(1).textContent())
    }

    @RetryingTest(maxAttempts = 2)
    fun `the page keeps a single scrollbar`(page: Page) {
        openPage(page)
        openRoute(page, "bar-simple")

        val horizontal = page.evaluate(
            "() => document.documentElement.scrollWidth - document.documentElement.clientWidth",
        ) as Number
        assertEquals(0, horizontal.toInt(), "no horizontal overflow")
    }

    @RetryingTest(maxAttempts = 2)
    fun `page logs no console errors or failed requests`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        val failedRequests = mutableListOf<String>()
        page.onConsoleMessage { message ->
            if (message.type() == "error") consoleErrors += message.text()
        }
        page.onRequestFailed { request ->
            failedRequests += "${request.url()} ${request.failure()}"
        }

        openPage(page)
        openRoute(page, "bar-simple")
        waitForChart(page)

        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
        assertTrue(failedRequests.isEmpty(), "failed requests: $failedRequests")
    }

    private fun openPage(page: Page) {
        page.navigate("/", Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE))
        page.waitForSelector("#demo :is(echarts-example-page, echarts-overview-page) h1")
    }

    /** Clicks the drawer item for [route]; the router is also what the menu drives. */
    private fun openRoute(page: Page, route: String) {
        page.locator("#demo vaadin-side-nav-item[data-route='$route']").first().click()
        page.waitForSelector("#demo :is(echarts-example-page, echarts-overview-page) h1")
    }

    /** Opens the layout's language menu and picks [code]. */
    private fun switchLanguage(page: Page, code: String) {
        page.locator("#demo [data-menu='lang']").first().click()
        page.locator("#demo [data-action='lang:$code']").first().click()
        page.waitForFunction("(expected) => document.documentElement.lang === expected", code)
    }

    /**
     * Waits until the chart element has a canvas with a non-zero box.
     *
     * ECharts creates its canvas on init, which happens when the element connects, so this is the
     * signal that the page is mounted and measured.
     */
    private fun waitForChart(page: Page) {
        page.waitForFunction(
            """
            () => {
              const pageEl = document.querySelector('ktjs-demo-layout')?.shadowRoot?.querySelector(':is(echarts-example-page, echarts-overview-page)');
              const chart = pageEl?.shadowRoot?.querySelector('demo-chart');
              return Boolean(chart && chart.querySelector('canvas') && chart.clientHeight > 0);
            }
            """,
        )
    }

    private fun chartBox(page: Page): Pair<Int, Int> {
        val box = page.evaluate(
            """
            () => {
              const pageEl = document.querySelector('ktjs-demo-layout')?.shadowRoot?.querySelector(':is(echarts-example-page, echarts-overview-page)');
              const chart = pageEl?.shadowRoot?.querySelector('demo-chart');
              return chart ? [chart.clientWidth, chart.clientHeight] : [0, 0];
            }
            """,
        ) as List<*>
        return (box[0] as Number).toInt() to (box[1] as Number).toInt()
    }
}
