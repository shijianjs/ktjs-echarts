package echarts.options

import echarts.MarkerRelativeTo
import echarts.MarkerStatisticType
import kotlin.js.JsName
import kotlin.js.unsafeCast

/**
 * Symbol members shared by mark items, graph nodes, tree nodes and similar option objects.
 *
 * Upstream `SymbolOptionMixin<T>`: every member also accepts a callback over the data item.
 */
external interface SymbolOptionMixin {
    /** Symbol shape: a built-in name, a `"path://..."` value or a callback. */
    var symbol: SymbolShape?

    /** Symbol size, `[width, height]`, or a callback. */
    var symbolSize: SymbolSize?

    /** Symbol rotation in degrees, or a callback. */
    var symbolRotate: SymbolRotate?

    /** Whether to keep the symbol aspect ratio. */
    var symbolKeepAspect: Boolean?

    /** Symbol offset, `[x, y]`, or a callback. */
    var symbolOffset: SymbolOffset?
}

/**
 * Where a marker is placed.
 *
 * Upstream `MarkerPositionOption`; a marker is positioned by an explicit `x`/`y` pair, by a
 * coordinate on the series coordinate system, or by a statistic of the data.
 */
external interface MarkerPositionOption {
    /** Horizontal position, a pixel number or a percentage string. */
    var x: StringOrDouble?

    /** Vertical position, a pixel number or a percentage string. */
    var y: StringOrDouble?

    /** What [x] and [y] are relative to. */
    var relativeTo: MarkerRelativeTo?

    /** Coordinate on the coordinate system of the series. */
    var coord: Array<ScaleDataValueOrMarkerStatisticType>?

    /** Position on the x axis. */
    var xAxis: ScaleDataValue?

    /** Position on the y axis. */
    var yAxis: ScaleDataValue?

    /** Position on the radius axis. */
    var radiusAxis: ScaleDataValue?

    /** Position on the angle axis. */
    var angleAxis: ScaleDataValue?

    /** Statistic of the data used to place the marker. */
    var type: MarkerStatisticType?

    /** Index of the dimension [type] is computed from. */
    var valueIndex: Double?

    /** Name of the dimension [type] is computed from. */
    var valueDim: String?

    /** Value shown as the label of the marker. */
    var value: StringOrDouble?
}

/**
 * State options of one mark point.
 */
external interface MarkPointStateOption {
    /** Item style. */
    var itemStyle: ItemStyleOption?

    /** Label style. */
    var label: LabelOption?

    /** z-index of the mark point inside its z-level. */
    var z2: Double?
}

/**
 * One mark point of [MarkPointOption].
 */
external interface MarkPointDataItemOption : MarkPointStateOption, MarkerPositionOption, SymbolOptionMixin {
    /** Name of the mark point; required upstream. */
    var name: String
}

/**
 * State options of one mark line.
 */
external interface MarkLineStateOption {
    /** Line style. */
    var lineStyle: LineStyleOption?

    /** Style of the symbols at both ends of the line. */
    var itemStyle: ItemStyleOption?

    /** Style of the two end labels. */
    var label: MarkLineLabelOption?

    /** z-index of the mark line inside its z-level. */
    var z2: Double?
}

/**
 * Label of a mark line: upstream `SeriesLineLabelOption`, which extends `LineLabelOption`.
 *
 * Upstream widens `distance` to `number | number[]`, where the pair gives the horizontal and the
 * vertical distance to the line, and narrows `position` to the start / middle / end keywords
 * (`"start"`, `"insideMiddleTop"`, ...), which [LabelPosition] already wraps. Kotlin cannot override
 * the inherited `var distance`, so the array form is written through [typedDistance]; both
 * properties address the same `distance` field of the option object.
 */
external interface MarkLineLabelOption : LabelOption {
    /** [distance] as the upstream `number | number[]`; the same JavaScript property. */
    @JsName("distance")
    var typedDistance: DoubleOrDoubleArray?
}

/**
 * Members shared by the two shapes of a mark line definition.
 */
external interface MarkLineDataItemOptionBase : MarkLineStateOption {
    /** Name of the mark line. */
    var name: String?
}

/**
 * A mark line defined on one axis, e.g. `{ yAxis: "average" }`.
 *
 * Upstream declares the symbol members here instead of inheriting `SymbolOptionMixin`, so `symbol`
 * also accepts a `[startSymbol, endSymbol]` pair and `symbolSize` / `symbolRotate` also accept one
 * value per end.
 */
external interface MarkLine1DDataItemOption : MarkLineDataItemOptionBase {
    /** Position on the x axis. */
    var xAxis: StringOrDouble?

    /** Position on the y axis. */
    var yAxis: StringOrDouble?

    /** Statistic of the data used when no axis position is set. */
    var type: MarkerStatisticType?

    /** Index of the dimension [type] is computed from. */
    var valueIndex: Double?

    /** Name of the dimension [type] is computed from. */
    var valueDim: String?

    /** Symbol of both ends, or one per end. */
    var symbol: StringOrStringArray?

    /** Symbol size of both ends, or one per end. */
    var symbolSize: DoubleOrDoubleArray?

    /** Symbol rotation of both ends, or one per end. */
    var symbolRotate: DoubleOrDoubleArray?

    /** Symbol offset of both ends. */
    var symbolOffset: SymbolOffset?
}

/**
 * One end of a mark line defined by two points.
 */
external interface MarkLine2DDataItemDimOption : MarkLineDataItemOptionBase, SymbolOptionMixin, MarkerPositionOption

/**
 * One entry of `MarkLineOption.data`: a single definition or a pair of them.
 *
 * Create a pair with a JavaScript array of two [MarkLine2DDataItemDimOption] values:
 *
 * ```kotlin
 * data = arrayOf(MarkLineDataItem(arrayOf(unsafeJso { coord = ... }, unsafeJso { coord = ... })))
 * ```
 */
external interface MarkLineDataItem

/** Wraps a definition on one axis. */
fun MarkLineDataItem(value: MarkLine1DDataItemOption): MarkLineDataItem =
    value.unsafeCast<MarkLineDataItem>()

/** Wraps a `[from, to]` pair. */
fun MarkLineDataItem(values: Array<MarkLine2DDataItemDimOption>): MarkLineDataItem =
    values.unsafeCast<MarkLineDataItem>()

/**
 * State options of one mark area.
 */
external interface MarkAreaStateOption {
    /** Item style. */
    var itemStyle: ItemStyleOption?

    /** Label style. */
    var label: LabelOption?

    /** z-index of the mark area inside its z-level. */
    var z2: Double?
}

/**
 * Members shared by the two shapes of a mark area definition.
 */
external interface MarkAreaDataItemOptionBase : MarkAreaStateOption {
    /** Name of the mark area. */
    var name: String?
}

/**
 * A mark area defined on one axis, e.g. `{ yAxis: 10 }`.
 */
external interface MarkArea1DDataItemOption : MarkAreaDataItemOptionBase {
    /** Position on the x axis. */
    var xAxis: Double?

    /** Position on the y axis. */
    var yAxis: Double?

    /** Statistic of the data used when no axis position is set. */
    var type: MarkerStatisticType?

    /** Index of the dimension [type] is computed from. */
    var valueIndex: Double?

    /** Name of the dimension [type] is computed from. */
    var valueDim: String?
}

/**
 * One corner of a mark area defined by two points.
 */
external interface MarkArea2DDataItemDimOption : MarkAreaDataItemOptionBase, MarkerPositionOption

/**
 * One entry of `MarkAreaOption.data`: a single definition or a pair of them.
 */
external interface MarkAreaDataItem

/** Wraps a definition on one axis. */
fun MarkAreaDataItem(value: MarkArea1DDataItemOption): MarkAreaDataItem =
    value.unsafeCast<MarkAreaDataItem>()

/** Wraps a `[from, to]` pair. */
fun MarkAreaDataItem(values: Array<MarkArea2DDataItemDimOption>): MarkAreaDataItem =
    values.unsafeCast<MarkAreaDataItem>()
