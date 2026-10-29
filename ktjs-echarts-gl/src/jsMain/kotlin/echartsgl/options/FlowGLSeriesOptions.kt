package echartsgl.options

import echarts.options.SeriesOnCartesianOptionMixin
import echarts.options.SeriesOnGeoOptionMixin
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echartsgl.CoordinateSystem2D
import echartsgl.FlowGLParticleType
import echartsgl.GlColor
import echartsgl.TextureSource
import kotlin.js.JsName
import kotlin.js.unsafeCast

/**
 * Style of the particle traces of a [FlowGLSeriesOption].
 *
 * Only the color and the opacity are drawn; the trace has no border.
 */
external interface FlowGLItemStyleOption {
    /** Color of the trace. See [GlColor][echartsgl.GlColor] for the HDR array form. */
    var color: GlColor?

    /** Opacity of the trace, `0..1`. */
    var opacity: Double?
}

/**
 * One entry of [FlowGLSeriesOption.typedData].
 *
 * Upstream `number[]`: the position of a vector followed by the vector itself — `[lng, lat, sLng,
 * sLat]` in a `geo` coordinate system, `[x, y, vx, vy]` in `cartesian2d`. The data does not have to
 * cover a regular grid; the vectors in between are interpolated.
 */
external interface FlowGLDataItem

/** Wraps one vector of the field, for example `[0, 0, 1, 1]`. */
fun FlowGLDataItem(values: Array<Double>): FlowGLDataItem = values.unsafeCast<FlowGLDataItem>()

/**
 * Vector-field series, `series.type = "flowGL"`.
 *
 * The field is drawn as the traces of particles that are pushed around by the vectors of
 * [typedData], which is how wind and ocean-current data is visualized. The particles live in a
 * floating-point texture sized by [gridWidth] and [gridHeight].
 */
external interface FlowGLSeriesOption :
    SeriesOption,
    SeriesOnCartesianOptionMixin,
    SeriesOnGeoOptionMixin
{
    /** Coordinate system: `"cartesian2d"`, `"geo"` or `"bmap"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [Bar3DSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem2D?

    /** Shape of the particles: `"point"` or `"line"`. */
    var particleType: FlowGLParticleType?

    /** Density of the particles; the particle count is its square. */
    var particleDensity: Double?

    /** Size of a particle in pixels, or the width of a line particle. */
    var particleSize: Double?

    /** Speed of the particles; a point particle becomes intermittent when it is too high. */
    var particleSpeed: Double?

    /** Length of the trace behind a particle. */
    var particleTrail: Double?

    /** Supersampling ratio of the layer; `4` is a visible quality increase at four times the cost. */
    var supersampling: Double?

    /**
     * Number of columns of the vector-field grid.
     *
     * `"auto"` derives it from the extent of the data and from the particle density.
     */
    var gridWidth: StringOrDouble?

    /** Number of rows of the vector-field grid; `"auto"` derives it like [gridWidth]. */
    var gridHeight: StringOrDouble?

    /**
     * Texture that colors a particle by the speed of the vector it sits on, instead of a flat
     * [FlowGLItemStyleOption.color].
     */
    var colorTexture: TextureSource?

    /** Color and opacity of the traces. */
    @JsName("itemStyle")
    var typedItemStyle: FlowGLItemStyleOption?

    /** Typed view of [SeriesOption.data] with the item type of a vector field. */
    @JsName("data")
    var typedData: Array<FlowGLDataItem?>?
}
