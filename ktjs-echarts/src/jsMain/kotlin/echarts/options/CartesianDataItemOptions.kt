package echarts.options

import kotlin.js.JsName
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * Data items of the cartesian series (line, bar, scatter, effect scatter, candlestick, boxplot,
 * heatmap, lines and parallel).
 *
 * Upstream declares one `*DataItemOption` interface per series and accepts
 * `(DataValue | DataItemOption)[]` as the `data`; the unions below carry the same shape, so a value,
 * a row of values and an object all stay representable.
 *
 * Every item interface extends [OptionDataItemObject] so that it can also be passed to [items] and
 * read back through [SeriesOption.data]. When the upstream item narrows `value` to a type that is
 * wider than [OptionDataItemObject.value] (a row of values, or a value that may itself be an
 * array), the item exposes it as `typedValue`, mapped to the same JavaScript property by `@JsName`;
 * see `TYPE-MAPPING.md` §12.
 */

/**
 * One data item of a line series.
 *
 * Upstream `LineDataItemOption`.
 */
external interface LineDataItemOption : SymbolOptionMixin, OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `LineDataValue`, i.e. `OptionDataValue | OptionDataValue[]`; the inherited [value]
     * stays the generic single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Style of the point. */
    var itemStyle: ItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?

    /** Label at the end of the line. */
    var endLabel: LabelOption?
}

/**
 * One entry of `LineSeriesOption.typedData`.
 *
 * Upstream `LineDataValue | LineDataItemOption`.
 */
external interface LineDataItem

/** Wraps a single value. */
fun LineDataItem(value: OptionDataValue): LineDataItem = value.unsafeCast<LineDataItem>()

/** Wraps a row of values, one per dimension. */
fun LineDataItem(values: Array<OptionDataValue?>): LineDataItem = values.unsafeCast<LineDataItem>()

/** Wraps a data item object. */
fun LineDataItem(value: LineDataItemOption): LineDataItem = value.unsafeCast<LineDataItem>()

/**
 * One data item of a bar series.
 *
 * Upstream `BarDataItemOption`; its `itemStyle` is the upstream `BarItemStyleOption`, whose extra
 * `borderRadius` member [ItemStyleOption] already declares.
 */
external interface BarDataItemOption : OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `BarDataValue`, i.e. `OptionDataValue | OptionDataValue[]`; the inherited [value]
     * stays the generic single value. A bar on a time axis carries its `[time, value]` pair this way.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Cursor shown when hovering the bar. */
    var cursor: String?

    /** Style of the bar. */
    var itemStyle: ItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `BarSeriesOption.typedData`.
 *
 * Upstream `BarDataItemOption | OptionDataValue | OptionDataValue[]`.
 */
external interface BarDataItem

/** Wraps a single value. */
fun BarDataItem(value: OptionDataValue): BarDataItem = value.unsafeCast<BarDataItem>()

/** Wraps a row of values, for example an `[x, y]` pair. */
fun BarDataItem(values: Array<OptionDataValue?>): BarDataItem = values.unsafeCast<BarDataItem>()

/** Wraps a data item object. */
fun BarDataItem(value: BarDataItemOption): BarDataItem = value.unsafeCast<BarDataItem>()

/**
 * One data item of a scatter series.
 *
 * Upstream `ScatterDataItemOption`.
 */
external interface ScatterDataItemOption : SymbolOptionMixin, OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue | OptionDataValue[]`; the inherited [value] stays the generic
     * single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Style of the symbol. */
    var itemStyle: ItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `ScatterSeriesOption.typedData`.
 *
 * Upstream `ScatterDataItemOption | OptionDataValue | OptionDataValue[]`.
 */
external interface ScatterDataItem

/** Wraps a single value. */
fun ScatterDataItem(value: OptionDataValue): ScatterDataItem = value.unsafeCast<ScatterDataItem>()

/** Wraps a row of values, for example an `[x, y]` pair. */
fun ScatterDataItem(values: Array<OptionDataValue?>): ScatterDataItem = values.unsafeCast<ScatterDataItem>()

/** Wraps a data item object. */
fun ScatterDataItem(value: ScatterDataItemOption): ScatterDataItem = value.unsafeCast<ScatterDataItem>()

/**
 * One data item of an effect scatter series.
 *
 * Upstream `EffectScatterDataItemOption`.
 */
external interface EffectScatterDataItemOption : SymbolOptionMixin, OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `ScatterDataValue`, i.e. `OptionDataValue | OptionDataValue[]`; the inherited [value]
     * stays the generic single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Ripple effect of this item. */
    var rippleEffect: RippleEffectOption?

    /** Style of the symbol. */
    var itemStyle: ItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `EffectScatterSeriesOption.typedData`.
 *
 * Upstream `EffectScatterDataItemOption | ScatterDataValue`.
 */
external interface EffectScatterDataItem

/** Wraps a single value. */
fun EffectScatterDataItem(value: OptionDataValue): EffectScatterDataItem = value.unsafeCast<EffectScatterDataItem>()

/** Wraps a row of values, for example an `[x, y]` pair. */
fun EffectScatterDataItem(values: Array<OptionDataValue?>): EffectScatterDataItem =
    values.unsafeCast<EffectScatterDataItem>()

/** Wraps a data item object. */
fun EffectScatterDataItem(value: EffectScatterDataItemOption): EffectScatterDataItem =
    value.unsafeCast<EffectScatterDataItem>()

/**
 * Style of a candlestick.
 *
 * Upstream `CandlestickItemStyleOption`: [ItemStyleOption] plus the colors of a falling candle.
 */
external interface CandlestickItemStyleOption : ItemStyleOption {
    /** Color of a falling (down) candle. */
    var color0: ZRColor?

    /** Border color of a falling candle. */
    var borderColor0: ZRColor?

    /** Border color used when open and close are equal. */
    var borderColorDoji: ZRColor?
}

/**
 * One data item of a candlestick series.
 *
 * Upstream `CandlestickDataItemOption`.
 */
external interface CandlestickDataItemOption : OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `CandlestickDataValue`, i.e. `[open, close, lowest, highest]`; the inherited [value]
     * stays the generic single value.
     */
    @JsName("value")
    var typedValue: Array<OptionDataValue?>?

    /** Cursor shown when hovering the candle. */
    var cursor: String?

    /** Style of the candle. */
    var itemStyle: CandlestickItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `CandlestickSeriesOption.typedData`.
 *
 * Upstream `CandlestickDataValue | CandlestickDataItemOption`.
 */
external interface CandlestickDataItem

/** Wraps a `[open, close, lowest, highest]` row. */
fun CandlestickDataItem(values: Array<OptionDataValue?>): CandlestickDataItem =
    values.unsafeCast<CandlestickDataItem>()

/** Wraps a data item object. */
fun CandlestickDataItem(value: CandlestickDataItemOption): CandlestickDataItem =
    value.unsafeCast<CandlestickDataItem>()

/**
 * One data item of a boxplot series.
 *
 * Upstream `BoxplotDataItemOption`.
 */
external interface BoxplotDataItemOption : OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `BoxplotDataValue`, i.e. `OptionDataValueNumeric[]`; the inherited [value] stays the
     * generic single value.
     */
    @JsName("value")
    var typedValue: Array<OptionDataValue?>?

    /** Style of the box. */
    var itemStyle: ItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `BoxplotSeriesOption.typedData`.
 *
 * Upstream `BoxplotDataValue | BoxplotDataItemOption`.
 */
external interface BoxplotDataItem

/** Wraps a `[min, Q1, median, Q3, max]` row. */
fun BoxplotDataItem(values: Array<OptionDataValue?>): BoxplotDataItem = values.unsafeCast<BoxplotDataItem>()

/** Wraps a data item object. */
fun BoxplotDataItem(value: BoxplotDataItemOption): BoxplotDataItem = value.unsafeCast<BoxplotDataItem>()

/**
 * One data item of a heatmap series.
 *
 * Upstream `HeatmapDataItemOption`.
 */
external interface HeatmapDataItemOption : OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `HeatmapDataValue`, i.e. `OptionDataValue[]`; the inherited [value] stays the generic
     * single value.
     */
    @JsName("value")
    var typedValue: Array<OptionDataValue?>?

    /** Style of the heat point; upstream adds `borderRadius`. */
    var itemStyle: ItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `HeatmapSeriesOption.typedData`.
 *
 * Upstream `HeatmapDataItemOption | HeatmapDataValue`.
 */
external interface HeatmapDataItem

/** Wraps a `[x, y, value]` row. */
fun HeatmapDataItem(values: Array<OptionDataValue?>): HeatmapDataItem = values.unsafeCast<HeatmapDataItem>()

/** Wraps a data item object. */
fun HeatmapDataItem(value: HeatmapDataItemOption): HeatmapDataItem = value.unsafeCast<HeatmapDataItem>()

/**
 * One data item of a lines series.
 *
 * Upstream `LinesDataItemOption`; the `data` of a lines series is an array of these objects (or a
 * flat typed array), so there is no union over the item type.
 */
external interface LinesDataItemOption : OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `LinesValue`, i.e. `OptionDataValue | OptionDataValue[]`; the inherited [value] stays
     * the generic single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Name of the source point, referenced by the data of the coordinate system. */
    var fromName: String?

    /** Name of the target point. */
    var toName: String?

    /** Symbol of the two line ends, one for both or one per end. */
    var symbol: StringOrStringArray?

    /** Size of the symbols at the two line ends. */
    var symbolSize: DoubleOrDoubleArray?

    /** Coordinates of the line, `[[x, y], ...]`. */
    var coords: Array<Array<Double>>?

    /** Moving effect of this line. */
    var effect: LinesEffectOption?

    /** Style of the line. */
    var lineStyle: LineStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One data item of a parallel series.
 *
 * Upstream `ParallelSeriesDataItemOption`.
 */
external interface ParallelSeriesDataItemOption : OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `ParallelSeriesDataValue`, i.e. one value per parallel axis; the inherited [value]
     * stays the generic single value.
     */
    @JsName("value")
    var typedValue: Array<OptionDataValue?>?

    /** Style of the line. */
    var lineStyle: LineStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `ParallelSeriesOption.typedData`.
 *
 * Upstream `ParallelSeriesDataValue | ParallelSeriesDataItemOption`.
 */
external interface ParallelSeriesDataItem

/** Wraps one value per parallel axis. */
fun ParallelSeriesDataItem(values: Array<OptionDataValue?>): ParallelSeriesDataItem =
    values.unsafeCast<ParallelSeriesDataItem>()

/** Wraps a data item object. */
fun ParallelSeriesDataItem(value: ParallelSeriesDataItemOption): ParallelSeriesDataItem =
    value.unsafeCast<ParallelSeriesDataItem>()
