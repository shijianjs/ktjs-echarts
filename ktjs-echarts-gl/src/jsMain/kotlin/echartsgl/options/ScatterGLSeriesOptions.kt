package echartsgl.options

import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOnCartesianOptionMixin
import echarts.options.SeriesOnGeoOptionMixin
import echarts.options.SeriesOnPolarOptionMixin
import echarts.options.SeriesOnSingleOptionMixin
import echarts.options.SeriesOption
import echarts.options.SymbolOptionMixin
import echartsgl.CoordinateSystem2D
import echartsgl.GlBlendMode
import js.array.ArrayLike
import kotlin.js.JsAny
import kotlin.js.JsName
import kotlin.js.unsafeCast

/**
 * One data item of a [ScatterGLSeriesOption].
 *
 * The upstream form of an item is an array: `[x, y]` in a `cartesian2d` / `polar` / `singleAxis`
 * coordinate system and `[lng, lat, value]` in a `geo` coordinate system. Extra values can be mapped
 * by a `visualMap` component.
 */
external interface ScatterGLDataItemOption : SymbolOptionMixin, OptionDataItemObject {
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
 * One entry of [ScatterGLSeriesOption.typedData].
 *
 * Upstream `OptionDataValue[] | ScatterGLDataItemOption`.
 */
external interface ScatterGLDataItem

/** Wraps a single value. */
fun ScatterGLDataItem(value: OptionDataValue): ScatterGLDataItem = value.unsafeCast<ScatterGLDataItem>()

/** Wraps a row of values, for example an `[x, y]` pair. */
fun ScatterGLDataItem(values: Array<OptionDataValue?>): ScatterGLDataItem =
    values.unsafeCast<ScatterGLDataItem>()

/** Wraps a data item object. */
fun ScatterGLDataItem(value: ScatterGLDataItemOption): ScatterGLDataItem =
    value.unsafeCast<ScatterGLDataItem>()

/**
 * WebGL scatter series, `series.type = "scatterGL"`.
 *
 * Used in the same way as a 2D [scatter][echarts.options.ScatterSeriesOption], but drawn on the GPU,
 * which is what makes hundred-thousand-point series practical. Every point is a symbol of
 * [symbol]/[symbolSize]; [itemStyle] and a `visualMap` component color them.
 *
 * The `*Index` members select the coordinate system component the series belongs to; only the ones
 * matching [typedCoordinateSystem] are read.
 */
external interface ScatterGLSeriesOption :
    SeriesOption,
    SeriesOnCartesianOptionMixin,
    SeriesOnPolarOptionMixin,
    SeriesOnSingleOptionMixin,
    SeriesOnGeoOptionMixin,
    SymbolOptionMixin
{
    /** Coordinate system: `"cartesian2d"`, `"polar"`, `"geo"` or `"singleAxis"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [Bar3DSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem2D?

    /**
     * How much the symbol grows while the coordinate system zooms in; `0` keeps the size fixed.
     *
     * The option is applied by a roam transform, so it only has an effect in coordinate systems
     * that can be zoomed, such as `geo`.
     */
    var zoomScale: Double?

    /**
     * Whether the symbols are drawn in the large mode, which skips the per-item symbol info.
     *
     * Upstream reads the option from the model and defaults it to `false`, but does not document it.
     */
    var large: Boolean?

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

    /** Post-processing effects of the layer the series is drawn on. */
    var postEffect: PostEffectOption?

    /** Typed view of [SeriesOption.data] with the item type of a WebGL scatter series. */
    @JsName("data")
    var typedData: Array<ScatterGLDataItem?>?

    /**
     * Flat view of the data: consecutive `[x0, y0, x1, y1, ...]` rows, or `[lng, lat]` pairs on a
     * `geo` coordinate system.
     *
     * This is the branch the multi-million-point pages pass as a typed array read from a binary
     * file, and it needs `dimensions`. Same JavaScript property as [typedData].
     */
    @JsName("data")
    var typedFlatData: ArrayLike<JsAny>?
}
