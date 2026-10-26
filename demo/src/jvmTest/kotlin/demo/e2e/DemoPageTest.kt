package demo.e2e

import com.microsoft.playwright.Page
import com.microsoft.playwright.junit.UsePlaywright
import com.microsoft.playwright.options.WaitUntilState
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junitpioneer.jupiter.RetryingTest

/**
 * Smoke checks for the demo page: it must load the bundle, apply its stylesheet, render the ECharts
 * canvas, respond to the toolbar buttons and produce no console errors or failed requests.
 */
@UsePlaywright(DemoOptions::class)
class DemoPageTest {
    @RetryingTest(maxAttempts = 2)
    fun `page renders the heading and styles`(page: Page) {
        openPage(page)

        assertTrue(page.title().contains("ktjs-echarts"), "title: ${page.title()}")
        assertEquals("ktjs-echarts demo", page.locator("h1").textContent())

        val display = page.locator(".page").evaluate("(node) => getComputedStyle(node).display") as String
        assertTrue(display != "contents", "the stylesheet applies to .page (display = $display)")
    }

    @RetryingTest(maxAttempts = 2)
    fun `page mounts an ECharts canvas`(page: Page) {
        openPage(page)

        val canvas = page.locator("#chart canvas")
        canvas.waitFor()
        val box = canvas.boundingBox()
        assertTrue(box != null && box.width > 0 && box.height > 0, "canvas box: $box")
    }

    @RetryingTest(maxAttempts = 2)
    fun `toolbar actions update the chart and the status line`(page: Page) {
        openPage(page)

        page.locator("#randomize").click()
        assertTrue(
            page.locator("#status").textContent()!!.contains("Randomized"),
            "status after randomize: ${page.locator("#status").textContent()}",
        )

        page.locator("#append").click()
        assertTrue(
            page.locator("#status").textContent()!!.contains("7 months"),
            "status after append: ${page.locator("#status").textContent()}",
        )
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

        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
        assertTrue(failedRequests.isEmpty(), "failed requests: $failedRequests")
    }

    private fun openPage(page: Page) {
        page.navigate("/", Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE))
    }
}
