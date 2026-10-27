package echarts.options

import kotlin.js.JsName
import kotlin.js.unsafeCast

/*
 * Data items of the remaining series: pie, funnel, gauge, radar, map, theme river and pictorial bar.
 *
 * The same rules as in `CartesianDataItemOptions.kt` apply: one item interface per series plus a
 * union over the raw shapes upstream accepts, and `@JsName("value") typedValue` whenever upstream
 * narrows `value` to a wider type than [OptionDataItemObject.value].
 */

/**
 * Rotation of a pie label.
 *
 * Upstream `number | boolean | 'radial' | 'tangential' | 'tangential-noflip'`; prefer the constants
 * in [PieLabelRotates].
 */
external interface PieLabelRotate

/** Wraps a rotation in degrees. */
fun PieLabelRotate(value: Double): PieLabelRotate = value.unsafeCast<PieLabelRotate>()

/** Wraps the boolean form, which rotates the label with the sector. */
fun PieLabelRotate(value: Boolean): PieLabelRotate = value.unsafeCast<PieLabelRotate>()

/** Wraps a keyword such as `"radial"`; prefer the constants in [PieLabelRotates]. */
fun PieLabelRotate(value: String): PieLabelRotate = value.unsafeCast<PieLabelRotate>()

/** The keywords accepted by [PieLabelRotate]. */
object PieLabelRotates {
    /** The label is laid out along the radius. */
    inline val radial: PieLabelRotate get() = PieLabelRotate("radial")

    /** The label is laid out along the tangent of the sector. */
    inline val tangential: PieLabelRotate get() = PieLabelRotate("tangential")

    /** Like [tangential], but the label text is never flipped. */
    inline val tangentialNoflip: PieLabelRotate get() = PieLabelRotate("tangential-noflip")
}

/**
 * What a pie label is aligned to.
 *
 * Upstream `'none' | 'labelLine' | 'edge'`; prefer the constants in [PieLabelAlignTos].
 */
sealed external interface PieLabelAlignTo

/** Wraps a keyword; prefer the constants in [PieLabelAlignTos]. */
inline fun PieLabelAlignTo(value: String): PieLabelAlignTo = value.unsafeCast<PieLabelAlignTo>()

/** The keywords accepted by [PieLabelAlignTo]. */
object PieLabelAlignTos {
    /** No alignment. */
    inline val none: PieLabelAlignTo get() = PieLabelAlignTo("none")

    /** The label is aligned to the end of the label line. */
    inline val labelLine: PieLabelAlignTo get() = PieLabelAlignTo("labelLine")

    /** The label is aligned to the edge of the container. */
    inline val edge: PieLabelAlignTo get() = PieLabelAlignTo("edge")
}

/**
 * Style of a pie item; upstream `PieItemStyleOption`, whose only extra member, `borderRadius`, is
 * already part of [ItemStyleOption].
 */
typealias PieItemStyleOption = ItemStyleOption

/**
 * Style of a pie label.
 *
 * Upstream `PieLabelOption`, i.e. [LabelOption] without `rotate` and `position` plus the
 * pie-specific members; the inherited members stay available, including the generic
 * [LabelOption.rotate] and [LabelOption.position].
 */
external interface PieLabelOption : LabelOption {
    /**
     * Typed view of [LabelOption.rotate]; upstream additionally allows the `"radial"` /
     * `"tangential"` keywords and a boolean.
     */
    @JsName("rotate")
    var typedRotate: PieLabelRotate?

    /** What the label is aligned to. */
    var alignTo: PieLabelAlignTo?

    /** Distance to the edge of the container. */
    var edgeDistance: StringOrDouble?

    /** Upstream deprecated alias of [edgeDistance]. */
    var margin: StringOrDouble?

    /** Margin that keeps the label from bleeding into other sectors. */
    var bleedMargin: Double?

    /** Distance between the label and the label line. */
    var distanceToLabelLine: Double?
}

/**
 * Style of a pie label line; upstream `PieLabelLineOption`, whose `maxSurfaceAngle` member is
 * already part of [LabelLineOption].
 */
typealias PieLabelLineOption = LabelLineOption

/**
 * One data item of a pie series.
 *
 * Upstream `PieDataItemOption`.
 */
external interface PieDataItemOption : OptionDataItemObject, DataItemStateOption {
    /** Cursor shown when hovering the sector. */
    var cursor: String?

    /** Style of the sector. */
    var itemStyle: PieItemStyleOption?

    /** Style of the label. */
    var label: PieLabelOption?

    /** Style of the label line. */
    var labelLine: PieLabelLineOption?
}

/**
 * One entry of `PieSeriesOption.typedData`.
 *
 * Upstream `OptionDataValueNumeric | OptionDataValueNumeric[] | PieDataItemOption`.
 */
external interface PieDataItem

/** Wraps a single numeric value. */
fun PieDataItem(value: OptionDataValueNumeric): PieDataItem = value.unsafeCast<PieDataItem>()

/** Wraps a row of numeric values. */
fun PieDataItem(values: Array<OptionDataValueNumeric?>): PieDataItem = values.unsafeCast<PieDataItem>()

/** Wraps a data item object. */
fun PieDataItem(value: PieDataItemOption): PieDataItem = value.unsafeCast<PieDataItem>()

/**
 * Style of a funnel item.
 *
 * Upstream `FunnelDataItemOption['itemStyle']`, i.e. [ItemStyleOption] plus the size of the band.
 */
external interface FunnelItemStyleOption : ItemStyleOption {
    /** Width of the funnel band. */
    var width: StringOrDouble?

    /** Height of the funnel band. */
    var height: StringOrDouble?
}

/**
 * One data item of a funnel series.
 *
 * Upstream `FunnelDataItemOption`.
 */
external interface FunnelDataItemOption : OptionDataItemObject, DataItemStateOption {
    /** Style of the funnel band. */
    var itemStyle: FunnelItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?

    /** Style of the label line. */
    var labelLine: LabelLineOption?
}

/**
 * One entry of `FunnelSeriesOption.typedData`.
 *
 * Upstream `OptionDataValueNumeric | OptionDataValueNumeric[] | FunnelDataItemOption`.
 */
external interface FunnelDataItem

/** Wraps a single numeric value. */
fun FunnelDataItem(value: OptionDataValueNumeric): FunnelDataItem = value.unsafeCast<FunnelDataItem>()

/** Wraps a row of numeric values. */
fun FunnelDataItem(values: Array<OptionDataValueNumeric?>): FunnelDataItem =
    values.unsafeCast<FunnelDataItem>()

/** Wraps a data item object. */
fun FunnelDataItem(value: FunnelDataItemOption): FunnelDataItem = value.unsafeCast<FunnelDataItem>()

/**
 * One entry of `GaugeSeriesOption.typedData`.
 *
 * Upstream `OptionDataValueNumeric | GaugeDataItemOption`.
 */
external interface GaugeDataItem

/** Wraps a single numeric value. */
fun GaugeDataItem(value: OptionDataValueNumeric): GaugeDataItem = value.unsafeCast<GaugeDataItem>()

/** Wraps a data item object. */
fun GaugeDataItem(value: GaugeDataItemOption): GaugeDataItem = value.unsafeCast<GaugeDataItem>()

/**
 * One data item of a radar series.
 *
 * Upstream `RadarSeriesDataItemOption`.
 */
external interface RadarSeriesDataItemOption : SymbolOptionMixin, OptionDataItemObject, DataItemStateOption {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `RadarSeriesDataValue`, i.e. one value per radar axis; the inherited [value] stays
     * the generic single value.
     */
    @JsName("value")
    var typedValue: Array<OptionDataValue?>?

    /** Style of the line of this item. */
    var lineStyle: LineStyleOption?

    /** Style of the area of this item. */
    var areaStyle: AreaStyleOption?

    /** Style of the symbol. */
    var itemStyle: ItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `RadarSeriesOption.typedData`.
 *
 * Upstream `RadarSeriesDataItemOption | RadarSeriesDataValue`.
 */
external interface RadarSeriesDataItem

/** Wraps one value per radar axis. */
fun RadarSeriesDataItem(values: Array<OptionDataValue?>): RadarSeriesDataItem =
    values.unsafeCast<RadarSeriesDataItem>()

/** Wraps a data item object. */
fun RadarSeriesDataItem(value: RadarSeriesDataItemOption): RadarSeriesDataItem =
    value.unsafeCast<RadarSeriesDataItem>()

/**
 * One data item of a map series.
 *
 * Upstream `MapDataItemOption`; its `itemStyle` is the upstream `GeoItemStyleOption`, whose only
 * extra member, `areaColor`, is already part of [ItemStyleOption].
 */
external interface MapDataItemOption : OptionDataItemObject, DataItemStateOption {
    /** Cursor shown when hovering the region. */
    var cursor: String?

    /** Whether the region ignores mouse events. */
    var silent: Boolean?

    /** Style of the region. */
    var itemStyle: ItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `MapSeriesOption.typedData`.
 *
 * Upstream `OptionDataValueNumeric | OptionDataValueNumeric[] | MapDataItemOption`.
 */
external interface MapDataItem

/** Wraps a single numeric value. */
fun MapDataItem(value: OptionDataValueNumeric): MapDataItem = value.unsafeCast<MapDataItem>()

/** Wraps a row of numeric values. */
fun MapDataItem(values: Array<OptionDataValueNumeric?>): MapDataItem = values.unsafeCast<MapDataItem>()

/** Wraps a data item object. */
fun MapDataItem(value: MapDataItemOption): MapDataItem = value.unsafeCast<MapDataItem>()

/**
 * One data item of a pictorial bar series.
 *
 * Upstream `PictorialBarDataItemOption`.
 */
external interface PictorialBarDataItemOption : PictorialBarSeriesSymbolOption, OptionDataItemObject, DataItemStateOption {
    /** z-index of the item. */
    var z: Double?

    /** Cursor shown when hovering the item. */
    var cursor: String?

    /** Style of the symbol. */
    var itemStyle: ItemStyleOption?

    /** Style of the label. */
    var label: LabelOption?
}

/**
 * One entry of `PictorialBarSeriesOption.typedData`.
 *
 * Upstream `PictorialBarDataItemOption | OptionDataValue | OptionDataValue[]`.
 */
external interface PictorialBarDataItem

/** Wraps a single value. */
fun PictorialBarDataItem(value: OptionDataValue): PictorialBarDataItem =
    value.unsafeCast<PictorialBarDataItem>()

/** Wraps a row of values. */
fun PictorialBarDataItem(values: Array<OptionDataValue?>): PictorialBarDataItem =
    values.unsafeCast<PictorialBarDataItem>()

/** Wraps a data item object. */
fun PictorialBarDataItem(value: PictorialBarDataItemOption): PictorialBarDataItem =
    value.unsafeCast<PictorialBarDataItem>()
