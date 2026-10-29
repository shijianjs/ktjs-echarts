package echartsgl

import echarts.ECharts
import echarts.EChartsInitOpts
import echarts.EChartsInstance
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
    opts: EChartsInitOpts? = null,
): Pair<EChartsInstance, HTMLElement> {
    val container = createContainer(width, height)
    return ECharts.init(container, null, opts) to container
}

/**
 * Disposes the chart and removes its container.
 */
internal fun destroyChart(chart: EChartsInstance, container: HTMLElement) {
    if (!chart.isDisposed()) chart.dispose()
    container.remove()
}

/**
 * Resolves after the next animation frame, so that pending rendering has completed.
 */
internal fun nextFrame(): Promise<Unit> = Promise { resolve, _ ->
    requestAnimationFrame { resolve(Unit) }
}

/**
 * Resolves after [count] animation frames.
 *
 * The WebGL layer is created on the first post-update pass and painted on the following frame, so
 * assertions on its canvas need more than one frame.
 */
internal fun nextFrames(count: Int): Promise<Unit> =
    if (count <= 0) Promise.resolve(Unit) else nextFrame().then { nextFrames(count - 1) }
