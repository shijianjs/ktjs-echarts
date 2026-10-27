package demo.ui

import demo.Theme
import echarts.ECharts
import echarts.EChartsInstance
import web.html.HTMLElement
import web.html.HtmlTagName

/** Dark theme registered by ECharts itself; used when the showcase runs in dark mode. */
private const val DARK_THEME = "dark"

/** Default height of an example chart. */
const val DEFAULT_CHART_HEIGHT = "32rem"

/**
 * One chart of an example page.
 *
 * The holder owns the container `<div>` and one ECharts instance. The instance is created when the
 * page becomes visible and disposed when it is replaced: a chart cannot measure itself while it is
 * detached, and keeping several hundred charts alive after a tour of the gallery would only waste
 * memory. The container node itself is never replaced, so the rest of the page stays untouched.
 */
class ChartHolder(
    private val height: String = DEFAULT_CHART_HEIGHT,
    private val setup: (EChartsInstance, ChartHolder) -> Unit,
) {
    /** Container of the chart; created once and never replaced. */
    val root: HTMLElement = element(HtmlTagName.div, "chart").apply { style.height = height }

    private var instance: EChartsInstance? = null

    /** Timer ids created by [setInterval]/[setTimeout]; cleared on [unmount]. */
    private val timers = mutableListOf<Int>()

    /** Creates the chart instance and applies the example option. Idempotent. */
    fun mount() {
        if (instance != null) return
        val chart = ECharts.init(root, if (Theme.isDark) DARK_THEME else null, null)
        instance = chart
        setup(chart, this)
    }

    /**
     * Registers an interval timer owned by this chart. The timer is cleared automatically when the
     * chart is disposed, so animation examples built on `setInterval` never leak after navigation.
     */
    fun setInterval(handler: () -> Unit, delayMillis: Int): Int =
        (kotlinx.browser.window.setInterval(handler, delayMillis) as Int).also { timers.add(it) }

    /**
     * Registers a one-shot timer owned by this chart; cleared on [unmount] if it has not fired yet.
     */
    fun setTimeout(handler: () -> Unit, delayMillis: Int): Int =
        (kotlinx.browser.window.setTimeout(handler, delayMillis) as Int).also { timers.add(it) }

    /** Disposes the chart instance; the container stays in the DOM. */
    fun unmount() {
        timers.forEach { kotlinx.browser.window.clearInterval(it); kotlinx.browser.window.clearTimeout(it) }
        timers.clear()
        instance?.dispose()
        instance = null
    }

    /** Forwards a container resize to the chart. */
    fun resize() {
        instance?.resize()
    }
}

/**
 * Creates a chart holder that applies the option returned by [option].
 *
 * The option is built lazily, on mount, so data generation happens once per visit and the page can
 * be rebuilt (language or theme switch) without paying for it at build time.
 */
fun chart(option: () -> echarts.options.EChartsOption, height: String = DEFAULT_CHART_HEIGHT): ChartHolder =
    ChartHolder(height) { chart, _ -> chart.setOption(option()) }

/** Creates a chart holder for an example that drives the chart directly (animation, timeline, ...). */
fun interactiveChart(
    height: String = DEFAULT_CHART_HEIGHT,
    setup: (EChartsInstance, ChartHolder) -> Unit,
): ChartHolder = ChartHolder(height, setup)
