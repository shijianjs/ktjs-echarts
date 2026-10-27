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
    private val setup: (EChartsInstance) -> Unit,
) {
    /** Container of the chart; created once and never replaced. */
    val root: HTMLElement = element(HtmlTagName.div, "chart").apply { style.height = height }

    private var instance: EChartsInstance? = null

    /** Creates the chart instance and applies the example option. Idempotent. */
    fun mount() {
        if (instance != null) return
        val chart = ECharts.init(root, if (Theme.isDark) DARK_THEME else null, null)
        instance = chart
        setup(chart)
    }

    /** Disposes the chart instance; the container stays in the DOM. */
    fun unmount() {
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
    ChartHolder(height) { chart -> chart.setOption(option()) }

/** Creates a chart holder for an example that drives the chart directly (animation, timeline, ...). */
fun interactiveChart(
    height: String = DEFAULT_CHART_HEIGHT,
    setup: (EChartsInstance) -> Unit,
): ChartHolder = ChartHolder(height, setup)
