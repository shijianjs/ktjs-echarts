package echartsgl

import echarts.AxisTypes
import echarts.ECharts
import echarts.EChartsInitOpts
import echarts.EChartsInstance
import echarts.options.OptionDataValue
import echarts.options.one
import echartsgl.options.Axis3DOption
import echartsgl.options.Bar3DDataItem
import echartsgl.options.EChartsGlOption
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import web.animations.requestAnimationFrame
import web.dom.document
import web.html.HTMLCanvasElement
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

/**
 * Builds `bar3D` data items from flat `x, y, z` triples.
 */
internal fun bar3DItems(vararg xyz: Double): Array<Bar3DDataItem?> {
    require(xyz.size % 3 == 0) { "A bar3D data item needs three values" }
    return Array(xyz.size / 3) { index ->
        Bar3DDataItem(Array(3) { offset -> OptionDataValue(xyz[index * 3 + offset]) })
    }
}

/**
 * Whether [value] really owns [key].
 *
 * Reading a typed accessor proves only that `@JsName` is symmetric; this asks JavaScript itself, the
 * way upstream decides which spelling of an aliased option key wins.
 */
internal fun hasOwn(value: Any, key: String): Boolean =
    js("Object.prototype.hasOwnProperty").call(value, key).unsafeCast<Boolean>()

/**
 * Builds one data row, for example an `x, y, z` triple.
 */
internal fun row(vararg values: Double): Array<OptionDataValue?> =
    Array(values.size) { index -> OptionDataValue(values[index]) }

/**
 * Asserts that the WebGL layer was created and painted into the only canvas of the container.
 */
internal fun assertWebGlCanvas(container: HTMLElement) {
    assertNull(container.querySelector(".ecgl-nowebgl"), "WebGL is not available")
    val canvases = container.querySelectorAll("canvas")
    assertEquals(1, canvases.length)
    val canvas = canvases.item(0).unsafeCast<HTMLCanvasElement>()
    assertTrue(canvas.width > 0 && canvas.height > 0, "${canvas.width}x${canvas.height}")
}

/**
 * Adds the three value axes a `cartesian3D` series needs.
 */
internal fun EChartsGlOption.cartesian3DAxes() {
    xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
}
