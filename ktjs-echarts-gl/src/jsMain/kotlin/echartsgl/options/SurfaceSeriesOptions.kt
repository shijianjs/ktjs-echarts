package echartsgl.options

import echarts.options.LineStyleOption
import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOption
import echartsgl.CoordinateSystem3D
import kotlin.js.JsName
import kotlin.js.unsafeCast

/**
 * Callback of a surface equation.
 *
 * The `z` member of [SurfaceEquationOption] receives `x` and `y`; the `x` / `y` / `z` members of
 * [SurfaceParametricEquationOption] receive `u` and `v`.
 */
external interface SurfaceFunction

/** Wraps a callback of the surface equations. */
fun SurfaceFunction(callback: (Double, Double) -> Double): SurfaceFunction =
    callback.unsafeCast<SurfaceFunction>()

/**
 * Sampling range of one independent variable of a surface.
 */
external interface SurfaceEquationDimensionOption {
    /** Minimum value of the variable. */
    var min: Double?

    /** Maximum value of the variable. */
    var max: Double?

    /** Distance between two samples of the variable. */
    var step: Double?
}

/**
 * Function surface `z = f(x, y)`, used while [SurfaceSeriesOption.parametric] is `false`.
 *
 * When [SurfaceSeriesOption.data] is set the equation is ignored; when the equation is set the
 * surface is sampled from it.
 */
external interface SurfaceEquationOption {
    /** Range of `x`. */
    var x: SurfaceEquationDimensionOption?

    /** Range of `y`. */
    var y: SurfaceEquationDimensionOption?

    /** The dependent value `f(x, y)`. */
    var z: SurfaceFunction?
}

/**
 * Parametric surface `[x, y, z] = f(u, v)`, used while [SurfaceSeriesOption.parametric] is `true`.
 */
external interface SurfaceParametricEquationOption {
    /** Range of `u`. */
    var u: SurfaceEquationDimensionOption?

    /** Range of `v`. */
    var v: SurfaceEquationDimensionOption?

    /** The `x` coordinate `f(u, v)`. */
    var x: SurfaceFunction?

    /** The `y` coordinate `f(u, v)`. */
    var y: SurfaceFunction?

    /** The `z` coordinate `f(u, v)`. */
    var z: SurfaceFunction?
}

/**
 * Wireframe drawn on top of a [SurfaceSeriesOption].
 */
external interface SurfaceWireframeOption {
    /** Whether the wireframe is shown; shown by default. */
    var show: Boolean?

    /** Style of the wireframe lines. */
    var lineStyle: LineStyleOption?
}

/**
 * One data item of a [SurfaceSeriesOption].
 *
 * A surface is a grid of vertices stored as a flat array; an item holds one vertex, `[x, y, z]`
 * or `[x, y, z, u, v]` for a parametric surface.
 */
external interface SurfaceDataItemOption : OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the inherited [value] stays the generic single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Style of this vertex patch. */
    var itemStyle: GlItemStyleOption?
}

/**
 * One entry of [SurfaceSeriesOption.typedData].
 *
 * Upstream `OptionDataValue[] | SurfaceDataItemOption`.
 */
external interface SurfaceDataItem

/** Wraps a single value. */
fun SurfaceDataItem(value: OptionDataValue): SurfaceDataItem = value.unsafeCast<SurfaceDataItem>()

/** Wraps one vertex, for example an `[x, y, z]` triple. */
fun SurfaceDataItem(values: Array<OptionDataValue?>): SurfaceDataItem =
    values.unsafeCast<SurfaceDataItem>()

/** Wraps a data item object. */
fun SurfaceDataItem(value: SurfaceDataItemOption): SurfaceDataItem =
    value.unsafeCast<SurfaceDataItem>()

/**
 * Surface series, `series.type = "surface"`.
 *
 * Drawn in a [Grid3DOption] from either [data][SeriesOption.data] or an equation. The surface is
 * shaded with lambert lighting by default, so its wireframe is visible even without a `visualMap`.
 */
external interface SurfaceSeriesOption : SeriesOption, ShadingOption {
    /** Coordinate system: `"cartesian3D"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [Bar3DSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem3D?

    /** Index of the [Grid3DOption] the surface is drawn in. */
    var grid3DIndex: Double?

    /** Whether the surface is defined by [parametricEquation] instead of [equation]. */
    var parametric: Boolean?

    /** Wireframe drawn on top of the surface. */
    var wireframe: SurfaceWireframeOption?

    /** Function surface used when no data and no [parametricEquation] are set. */
    var equation: SurfaceEquationOption?

    /** Parametric surface used while [parametric] is `true` and no data is set. */
    var parametricEquation: SurfaceParametricEquationOption?

    /** Number of rows and columns of the vertex grid when the data is given as a flat array. */
    var dataShape: Array<Double>?

    /** Style of the surface. */
    @JsName("itemStyle")
    var typedItemStyle: GlItemStyleOption?

    /** Typed view of [SeriesOption.data] with the item type of a surface series. */
    @JsName("data")
    var typedData: Array<SurfaceDataItem?>?
}
