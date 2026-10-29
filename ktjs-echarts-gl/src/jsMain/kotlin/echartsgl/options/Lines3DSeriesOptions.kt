package echartsgl.options

import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOption
import echartsgl.CoordinateSystem3D
import echartsgl.GlBlendMode
import echartsgl.GlColor
import kotlin.js.JsName
import kotlin.js.unsafeCast

/**
 * Style of one 3D line.
 *
 * The 2D [echarts.options.LineStyleOption] only accepts a rendered color; `echarts-gl` parses the
 * color with its own `parseColor`, which also takes the HDR `[r, g, b(, a)]` array form, so this
 * style uses [GlColor].
 */
external interface Lines3DLineStyleOption {
    /** Color of the line. */
    var color: GlColor?

    /** Width of the line in pixels. */
    var width: Double?

    /** Opacity, `0..1`. */
    var opacity: Double?
}

/**
 * Moving trail drawn on top of the lines.
 *
 * The trail is a second mesh whose head travels along every line; the amount of the line that stays
 * lit behind the head is [trailLength].
 */
external interface Lines3DEffectOption {
    /** Whether the trail is drawn; it is off by default. */
    var show: Boolean?

    /**
     * Duration of one period, in seconds.
     *
     * Ignored when [constantSpeed] is set.
     */
    var period: Double?

    /**
     * Speed of the trail head in 3D units per period; when set, [period] is ignored and all lines
     * move at the same speed whatever their length.
     */
    var constantSpeed: Double?

    /** Width of the trail in pixels. */
    var trailWidth: Double?

    /** Length of the trail as a fraction of the line, `0..1`. */
    var trailLength: Double?

    /** Color of the trail; the line color is used when it is not set. */
    var trailColor: GlColor?

    /** Opacity of the trail; the opacity of the line is used when it is not set. */
    var trailOpacity: Double?

    /**
     * Intensity of the moving spot at the head of the trail.
     *
     * Upstream reads this option from the model but does not document it.
     */
    var spotIntensity: Double?
}

/**
 * Object form of a [Lines3DSeriesOption] data item.
 *
 * The array form of an item is the list of coordinates itself, for example
 * `[[120, 66], [122, 67]]`; use this object when the line needs a name, a value for a `visualMap`
 * component or its own style.
 */
external interface Lines3DDataItemOption : OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the inherited [value] stays the generic single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /**
     * Coordinates of the line: two `[lng, lat]` pairs (or `[x, y, z]` triples) as a curve, or more
     * pairs when [Lines3DSeriesOption.polyline] is set.
     */
    var coords: Array<Array<Double>>?

    /** Style of this line. */
    var lineStyle: Lines3DLineStyleOption?
}

/**
 * One entry of [Lines3DSeriesOption.typedData].
 *
 * Upstream `number[][] | Lines3DDataItemOption`.
 */
external interface Lines3DDataItem

/** Wraps the coordinates of a line, for example `[[120, 66], [122, 67]]`. */
fun Lines3DDataItem(coords: Array<Array<Double>>): Lines3DDataItem =
    coords.unsafeCast<Lines3DDataItem>()

/** Wraps a data item object. */
fun Lines3DDataItem(value: Lines3DDataItemOption): Lines3DDataItem =
    value.unsafeCast<Lines3DDataItem>()

/**
 * 3D lines series, `series.type = "lines3D"`.
 *
 * Every item of the series is one line between two geographic coordinates, drawn as a curve above
 * the surface of the `geo3D` component or `globe` it belongs to. Set [Lines3DEffectOption.show] to
 * animate a trail along the lines.
 */
external interface Lines3DSeriesOption : SeriesOption {
    /** Coordinate system: `"geo3D"` or `"globe"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [Bar3DSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem3D?

    /** Index of the `geo3D` component the series is drawn on. */
    var geo3DIndex: Double?

    /** Index of the `globe` component the series is drawn on. */
    var globeIndex: Double?

    /**
     * Whether an item is a polyline through all of its coordinates.
     *
     * When it is `false` every item is one curve between its first two coordinates, which is what a
     * flight route needs. Set it to `true` to draw GPS tracks.
     */
    var polyline: Boolean?

    /** Blend mode of the lines and trails: `"source-over"` or `"lighter"`. */
    @JsName("blendMode")
    var typedBlendMode: GlBlendMode?

    /** Moving trail of the lines. */
    var effect: Lines3DEffectOption?

    /** Style of the lines. */
    @JsName("lineStyle")
    var typedLineStyle: Lines3DLineStyleOption?

    /** Typed view of [SeriesOption.data] with the item type of a 3D lines series. */
    @JsName("data")
    var typedData: Array<Lines3DDataItem?>?
}
