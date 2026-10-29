package echartsgl.options

import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOnCartesianOptionMixin
import echarts.options.SeriesOnGeoOptionMixin
import echarts.options.SeriesOption
import echartsgl.CoordinateSystem2D
import echartsgl.GlBlendMode
import echartsgl.GlColor
import kotlin.js.JsName
import kotlin.js.unsafeCast

/**
 * Style of one line of a [LinesGLSeriesOption].
 */
external interface LinesGLLineStyleOption {
    /** Color of the line. See [GlColor][echartsgl.GlColor] for the HDR array form. */
    var color: GlColor?

    /** Width of the line in pixels. */
    var width: Double?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /**
     * Curvature of a line between two coordinates; `0` draws a straight line.
     *
     * Upstream reads the option from the model but does not document it.
     */
    var curveness: Double?
}

/**
 * Object form of a [LinesGLSeriesOption] data item.
 *
 * The array form of an item is the list of coordinates itself, for example
 * `[[120, 66], [122, 67]]`; use this object when the line needs a name, a value for a `visualMap`
 * component or its own style.
 */
external interface LinesGLDataItemOption : OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the inherited [value] stays the generic single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /**
     * Coordinates of the line: two `[lng, lat]` pairs (or `[x, y]` pairs) as a curve, or more pairs
     * when [LinesGLSeriesOption.polyline] is set.
     */
    var coords: Array<Array<Double>>?

    /** Style of this line. */
    var lineStyle: LinesGLLineStyleOption?
}

/**
 * One entry of [LinesGLSeriesOption.typedData].
 *
 * Upstream `number[][] | LinesGLDataItemOption`.
 */
external interface LinesGLDataItem

/** Wraps the coordinates of a line, for example `[[120, 66], [122, 67]]`. */
fun LinesGLDataItem(coords: Array<Array<Double>>): LinesGLDataItem = coords.unsafeCast<LinesGLDataItem>()

/** Wraps a data item object. */
fun LinesGLDataItem(value: LinesGLDataItemOption): LinesGLDataItem =
    value.unsafeCast<LinesGLDataItem>()

/**
 * WebGL lines series, `series.type = "linesGL"`.
 *
 * Upstream `lines3D` drawn in a 2D coordinate system instead of a 3D one; it is meant for large
 * numbers of lines, such as every street of a city, and has no trail effect.
 */
external interface LinesGLSeriesOption :
    SeriesOption,
    SeriesOnCartesianOptionMixin,
    SeriesOnGeoOptionMixin
{
    /** Coordinate system: `"cartesian2d"` or `"geo"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [Bar3DSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem2D?

    /**
     * Whether an item is a polyline through all of its coordinates.
     *
     * When it is `false` every item is one curve between its first two coordinates.
     */
    var polyline: Boolean?

    /** Blend mode of the lines: `"source-over"` or `"lighter"`. */
    @JsName("blendMode")
    var typedBlendMode: GlBlendMode?

    /** Style of the lines. */
    @JsName("lineStyle")
    var typedLineStyle: LinesGLLineStyleOption?

    /** Post-processing effects of the layer the series is drawn on. */
    var postEffect: PostEffectOption?

    /** Typed view of [SeriesOption.data] with the item type of a WebGL lines series. */
    @JsName("data")
    var typedData: Array<LinesGLDataItem?>?
}
