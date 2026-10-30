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
 * One data item of a [Line3DSeriesOption].
 *
 * A 3D line is a single polyline through all of its items, so an item is one point: `[x, y, z]`
 * in the `cartesian3D` coordinate system.
 */
external interface Line3DDataItemOption : OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the inherited [value] stays the generic single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /**
     * Color and opacity of this point of the polyline.
     *
     * Named `lineStyle`, not `itemStyle`: `Line3DSeries` sets `visualStyleAccessPath = 'lineStyle'`,
     * so echarts reads the style of a data item from this key.
     */
    var lineStyle: LineStyleOption?
}

/**
 * One entry of [Line3DSeriesOption.typedData].
 *
 * Upstream `OptionDataValue[] | Line3DDataItemOption`.
 */
external interface Line3DDataItem

/** Wraps a single value. */
fun Line3DDataItem(value: OptionDataValue): Line3DDataItem = value.unsafeCast<Line3DDataItem>()

/** Wraps a point, for example an `[x, y, z]` triple. */
fun Line3DDataItem(values: Array<OptionDataValue?>): Line3DDataItem = values.unsafeCast<Line3DDataItem>()

/** Wraps a data item object. */
fun Line3DDataItem(value: Line3DDataItemOption): Line3DDataItem = value.unsafeCast<Line3DDataItem>()

/**
 * 3D line series, `series.type = "line3D"`.
 *
 * Drawn in a [Grid3DOption]: the items of the series are the points of one polyline. The width of
 * the line comes from [lineStyle], the color of each point may come from a data item or from a
 * `visualMap` component.
 */
external interface Line3DSeriesOption : SeriesOption {
    /** Coordinate system: `"cartesian3D"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [Bar3DSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem3D?

    /** Index of the [Grid3DOption] the series is drawn in. */
    var grid3DIndex: Double?

    /** Typed view of [SeriesOption.data] with the item type of a 3D line series. */
    @JsName("data")
    var typedData: Array<Line3DDataItem?>?
}
