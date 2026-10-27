package echarts

import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.VisualValue
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.math.abs
import kotlin.test.assertEquals
import kotlin.test.assertTrue
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

/**
 * Registers a map made of a single rectangular region.
 *
 * This mirrors the `testGeoJson1` / `testGeoJson2` fixtures of the upstream
 * `test/ut/spec/api/containPixel.test.ts` and `test/ut/spec/api/converter.test.ts`: one feature
 * named `"Afghanistan"` whose polygon is the given rectangle. The ring is left unclosed on
 * purpose, exactly like upstream.
 */
internal fun registerRectangleMap(
    mapName: String,
    left: Double,
    top: Double,
    right: Double,
    bottom: Double,
) {
    val ring = arrayOf(
        arrayOf(left, top),
        arrayOf(right, top),
        arrayOf(right, bottom),
        arrayOf(left, bottom),
    )
    val geoJson = unsafeJso<GeoJSON> {
        type = "FeatureCollection"
        features = arrayOf(
            unsafeJso<GeoJSONFeature> {
                type = "Feature"
                properties = unsafeJso<GeoJSONProperties> { this.name = "Afghanistan" }
                geometry = unsafeJso<GeoJSONGeometry> {
                    type = "Polygon"
                    coordinates = arrayOf(ring).unsafeCast<JsAny>()
                }
            },
        )
    }
    ECharts.registerMap(mapName, MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = GeoJSONSourceInput(geoJson) }))
}

/**
 * Builds a [ModelFinder] query object.
 */
internal fun finder(block: ModelFinderObject.() -> Unit): ModelFinder =
    ModelFinder(unsafeJso<ModelFinderObject> { block() })

/**
 * Wraps an `[x, y]` coordinate for `convertToPixel` / `convertFromPixel`.
 */
internal fun coordinate(x: Double, y: Double): OptionDataValueOrArray =
    OptionDataValueOrArray(arrayOf(OptionDataValue(x), OptionDataValue(y)))

/**
 * Reads a coordinate result as a list, so that assertions can use `assertEquals`.
 */
internal fun DoubleOrDoubleArray.asList(): List<Double> = unsafeCast<Array<Double>>().toList()

/**
 * Reads a single-value result.
 */
internal fun DoubleOrDoubleArray.asScalar(): Double = unsafeCast<Double>()

/**
 * Compares two coordinates with the tolerance the upstream converter test uses.
 */
internal fun assertPointEquals(expected: List<Double>, actual: List<Double>, tolerance: Double = 1e-3) {
    assertEquals(expected.size, actual.size, "coordinate size")
    for (index in expected.indices) {
        assertTrue(
            abs(expected[index] - actual[index]) < tolerance,
            "component $index: expected ${expected[index]}, actual ${actual[index]}",
        )
    }
}

internal fun assertScalarEquals(expected: Double, actual: Double, tolerance: Double = 1e-3) {
    assertTrue(abs(expected - actual) < tolerance, "expected $expected, actual $actual")
}

/**
 * Reads a `getVisual` result as text.
 *
 * `VisualValue` is a union of every visual type; the tests that need the text form (colors,
 * symbols) use this to avoid repeating the cast.
 */
internal fun VisualValue.asText(): String = unsafeCast<String>()
