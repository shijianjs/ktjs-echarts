package echartsgl.options

import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOption
import echarts.options.SymbolOptionMixin
import echartsgl.CoordinateSystem3D
import echartsgl.GlBlendMode
import kotlin.js.JsName
import kotlin.js.unsafeCast

/**
 * One data item of a [Scatter3DSeriesOption].
 *
 * The upstream form of an item is an array: `[x, y, z]` in a `cartesian3D` coordinate system,
 * `[lng, lat, value]` in `geo3D` and `globe`. Extra values can be mapped by a `visualMap`
 * component.
 */
external interface Scatter3DDataItemOption : SymbolOptionMixin, OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the inherited [value] stays the generic single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Style of the symbol of this item. */
    var itemStyle: GlItemStyleOption?

    /** Label of this item. */
    var label: GlLabelOption?

    /** Emphasis state of this item. */
    var emphasis: GlEmphasisOption?
}

/**
 * One entry of [Scatter3DSeriesOption.typedData].
 *
 * Upstream `OptionDataValue[] | Scatter3DDataItemOption`.
 */
external interface Scatter3DDataItem

/** Wraps a single value. */
fun Scatter3DDataItem(value: OptionDataValue): Scatter3DDataItem = value.unsafeCast<Scatter3DDataItem>()

/** Wraps a row of values, for example an `[x, y, z]` triple. */
fun Scatter3DDataItem(values: Array<OptionDataValue?>): Scatter3DDataItem =
    values.unsafeCast<Scatter3DDataItem>()

/** Wraps a data item object. */
fun Scatter3DDataItem(value: Scatter3DDataItemOption): Scatter3DDataItem =
    value.unsafeCast<Scatter3DDataItem>()

/**
 * 3D scatter series, `series.type = "scatter3D"`.
 *
 * Drawn in a [Grid3DOption], or on a `geo3D` / `globe` component. Symbols are sized by
 * [symbolSize] and colored by [itemStyle] or a `visualMap` component.
 */
external interface Scatter3DSeriesOption : SeriesOption, SymbolOptionMixin {
    /** Coordinate system: `"cartesian3D"`, `"geo3D"` or `"globe"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [Bar3DSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem3D?

    /** Index of the [Grid3DOption] the series is drawn in. */
    var grid3DIndex: Double?

    /** Index of the `geo3D` component the series is drawn in. */
    var geo3DIndex: Double?

    /** Index of the `globe` component the series is drawn in. */
    var globeIndex: Double?

    /** Blend mode of the symbols: `"source-over"` or `"lighter"`. */
    @JsName("blendMode")
    var typedBlendMode: GlBlendMode?

    /** Style of the symbols. */
    @JsName("itemStyle")
    var typedItemStyle: GlItemStyleOption?

    /** Label of the symbols. */
    @JsName("label")
    var typedLabel: GlLabelOption?

    /** Emphasis state of the series. */
    @JsName("emphasis")
    var typedEmphasis: GlEmphasisOption?

    /** Typed view of [SeriesOption.data] with the item type of a 3D scatter series. */
    @JsName("data")
    var typedData: Array<Scatter3DDataItem?>?
}
