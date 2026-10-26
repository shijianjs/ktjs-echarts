package echarts

import echarts.options.EChartsOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.Promise
import web.animations.requestAnimationFrame
import web.dom.document
import web.html.HTMLElement

/**
 * Creates a sized container attached to the document.
 */
internal fun createContainer(width: Int = 400, height: Int = 300): HTMLElement {
    val container = document.createElement("div")
    container.style.width = "${width}px"
    container.style.height = "${height}px"
    document.body.appendChild(container)
    return container
}

/**
 * Creates a chart on a freshly created container and returns both.
 */
internal fun createChart(
    width: Int = 400,
    height: Int = 300,
    theme: String? = null,
    opts: EChartsInitOpts? = null,
): Pair<EChartsInstance, HTMLElement> {
    val container = createContainer(width, height)
    val chart = if (theme == null) {
        ECharts.init(container, null, opts)
    } else {
        ECharts.init(container, theme, opts)
    }
    return chart to container
}

/**
 * Disposes the chart and removes its container.
 */
internal fun destroyChart(chart: EChartsInstance, container: HTMLElement) {
    if (!chart.isDisposed()) chart.dispose()
    container.remove()
}

/**
 * Builds an option with a default category/value grid and a line series.
 *
 * ECharts requires explicit axes for cartesian series; this keeps the tests focused on the API
 * under test instead of repeating the grid boilerplate.
 */
internal fun cartesianLineOption(
    vararg values: Double,
    block: EChartsOption.() -> Unit = {},
): EChartsOption = unsafeJso<EChartsOption> {
    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
    series = one(lineSeries { data = numbers(*values) })
    block()
}

/**
 * Resolves after the next animation frame, so that pending rendering has completed.
 */
internal fun nextFrame(): Promise<Unit> = Promise { resolve, _ ->
    requestAnimationFrame { resolve(Unit) }
}
