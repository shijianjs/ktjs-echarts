package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/** Kind of a cartesian, polar, single or parallel axis. */
@Serializable
public enum class AxisType {
    /** Continuous numeric axis. */
    @SerialName("value")
    Value,

    /** Discrete category axis. */
    @SerialName("category")
    Category,

    /** Time axis; data values are epoch milliseconds or parseable date strings. */
    @SerialName("time")
    Time,

    /** Logarithmic numeric axis. */
    @SerialName("log")
    Log,
}

/**
 * An axis of a cartesian, polar, single or parallel coordinate system.
 *
 * The same class covers `xAxis`, `yAxis`, `radiusAxis`, `angleAxis`, `singleAxis` and
 * `parallelAxis`; which properties apply depends on the coordinate system.
 */
@Serializable
public class AxisOption {
    /** Component id used by [EChartsOption] cross references. */
    public var id: String? = null

    /** Whether the axis is shown. */
    public var show: Boolean? = null

    /** Index of the grid this axis belongs to. */
    public var gridIndex: Int? = null

    /** Index of the polar coordinate system this axis belongs to. */
    public var polarIndex: Int? = null

    /** Index of the parallel coordinate system this axis belongs to. */
    public var parallelIndex: Int? = null

    /** Side of the grid the axis sits on: `"top"`, `"bottom"`, `"left"` or `"right"`. */
    public var position: String? = null

    /** Offset from its default position, in pixels. */
    public var offset: Double? = null

    /** Axis kind; `"category"` for category data. */
    public var type: AxisType? = null

    /** Axis name. */
    public var name: String? = null

    /** Name position: `"start"`, `"middle"`, `"end"` or a value on the axis. */
    public var nameLocation: String? = null

    /** Name text style. */
    public var nameTextStyle: TextStyleOption? = null

    /** Gap between the name and the axis line. */
    public var nameGap: Double? = null

    /** Name rotation in degrees. */
    public var nameRotate: Double? = null

    /** Truncation of long names. */
    public var nameTruncate: TextTruncateOption? = null

    /** Minimum value; `"dataMin"` uses the smallest data value. */
    public var min: NumberOrString? = null

    /** Maximum value; `"dataMax"` uses the largest data value. */
    public var max: NumberOrString? = null

    /** Minimum interval between ticks. */
    public var minInterval: Double? = null

    /** Maximum interval between ticks. */
    public var maxInterval: Double? = null

    /** Fixed interval between ticks. */
    public var interval: Double? = null

    /** Number of segments the axis is split into. */
    public var splitNumber: Int? = null

    /** Whether the axis range is scaled to the data instead of starting at zero. */
    public var scale: Boolean? = null

    /** Base of a `log` axis. */
    public var logBase: Double? = null

    /** Whether the value axis leaves a gap at both ends. */
    public var boundaryGap: BoundaryGap? = null

    /** Whether the axis direction is reversed. */
    public var inverse: Boolean? = null

    /** Start angle of a polar angle axis, in degrees. */
    public var startAngle: Double? = null

    /** End angle of a polar angle axis, in degrees. */
    public var endAngle: Double? = null

    /** Distance to the left edge, used by a `singleAxis`. */
    public var left: NumberOrString? = null

    /** Distance to the top edge, used by a `singleAxis`. */
    public var top: NumberOrString? = null

    /** Distance to the right edge, used by a `singleAxis`. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge, used by a `singleAxis`. */
    public var bottom: NumberOrString? = null

    /** Width of a `singleAxis`. */
    public var width: NumberOrString? = null

    /** Height of a `singleAxis`. */
    public var height: NumberOrString? = null

    /** Axis line style and arrow symbols. */
    public var axisLine: AxisLineOption? = null

    /** Tick marks. */
    public var axisTick: AxisTickOption? = null

    /** Tick labels. */
    public var axisLabel: AxisLabelOption? = null

    /** Grid lines perpendicular to the axis. */
    public var splitLine: SplitLineOption? = null

    /** Alternating background bands between split lines. */
    public var splitArea: SplitAreaOption? = null

    /** Secondary split lines drawn between the main ones. */
    public var minorSplitLine: SplitLineOption? = null

    /** Secondary ticks drawn between the main ones. */
    public var minorTick: AxisTickOption? = null

    /** Pointer shown for this axis on hover. */
    public var axisPointer: AxisPointerOption? = null

    /** Category values of a `category` axis. */
    public var data: List<DataValue>? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Whether the axis ignores mouse events. */
    public var silent: Boolean? = null

    /** Whether clicking the axis triggers the `click` event. */
    public var triggerEvent: Boolean? = null

    /** Whether axis animations are enabled. */
    public var animation: Boolean? = null

    /** Animation duration in milliseconds. */
    public var animationDuration: Double? = null

    /** Animation easing name. */
    public var animationEasing: String? = null
}

/** Text truncation settings for names too long for the available space. */
@Serializable
public class TextTruncateOption {
    /** Maximum width in pixels. */
    public var maxWidth: Double? = null

    /** Text appended when the name is truncated. */
    public var ellipsis: String? = null
}

/** Axis label. Extends every [TextStyleOption] property. */
@Serializable
public class AxisLabelOption : TextStyleOption() {
    /** Whether labels are shown. */
    public var show: Boolean? = null

    /** Interval between labels; `"auto"` or a number. */
    public var interval: NumberOrString? = null

    /** Whether labels are drawn inside the axis. */
    public var inside: Boolean? = null

    /** Rotation in degrees. */
    public var rotate: Double? = null

    /** Distance between the label and the axis line. */
    public var margin: Double? = null

    /** Distance between the label and the axis line, used by gauge series. */
    public var distance: Double? = null

    /** String template; `{value}` is replaced with the tick value. */
    public var formatter: String? = null

    /** Whether the first label is shown. */
    public var showMinLabel: Boolean? = null

    /** Whether the last label is shown. */
    public var showMaxLabel: Boolean? = null

    /** Whether overlapping labels are hidden. */
    public var hideOverlap: Boolean? = null
}

/** Axis line, including the optional arrow at the end. */
@Serializable
public class AxisLineOption {
    /** Whether the line is shown. */
    public var show: Boolean? = null

    /** Whether the zero line of a value axis is emphasized. */
    public var onZero: Boolean? = null

    /** Index of the axis used for the zero line when [onZero] is enabled. */
    public var onZeroAxisIndex: Int? = null

    /** Arrow at the start: `"none"`, `"circle"`, `"rect"`, `"roundRect"`, `"triangle"`, `"diamond"`, `"pin"`, `"arrow"`. */
    public var symbol: String? = null

    /** Arrow size, a number or `[width, height]`. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Arrow offset as `[x, y]`. */
    public var symbolOffset: List<NumberOrString>? = null

    /** Whether the ends of a gauge axis line are rounded. */
    public var roundCap: Boolean? = null

    /** Line style. */
    public var lineStyle: LineStyleOption? = null
}

/** Axis tick marks. */
@Serializable
public class AxisTickOption {
    /** Whether ticks are shown. */
    public var show: Boolean? = null

    /** Whether ticks are aligned with labels on a category axis. */
    public var alignWithLabel: Boolean? = null

    /** Interval between ticks; `"auto"` or a number. */
    public var interval: NumberOrString? = null

    /** Whether ticks are drawn inside the axis. */
    public var inside: Boolean? = null

    /** Tick length in pixels. */
    public var length: Double? = null

    /** Distance between the tick and the axis line, used by gauge series. */
    public var distance: Double? = null

    /** Tick style. */
    public var lineStyle: LineStyleOption? = null
}

/** Grid lines perpendicular to an axis. */
@Serializable
public class SplitLineOption {
    /** Whether split lines are shown. */
    public var show: Boolean? = null

    /** Interval between split lines; `"auto"` or a number. */
    public var interval: NumberOrString? = null

    /** Length of a gauge split line in pixels. */
    public var length: Double? = null

    /** Distance between a gauge split line and the axis line. */
    public var distance: Double? = null

    /** Line style. */
    public var lineStyle: LineStyleOption? = null
}

/** Alternating bands between split lines. */
@Serializable
public class SplitAreaOption {
    /** Whether the bands are shown. */
    public var show: Boolean? = null

    /** Interval between bands; `"auto"` or a number. */
    public var interval: NumberOrString? = null

    /** Band style. */
    public var areaStyle: AreaStyleOption? = null
}

/**
 * Pointer drawn on an axis when hovering, usually configured on `tooltip.axisPointer` or on an
 * axis. `type` is `"line"`, `"shadow"`, `"cross"` or `"none"`.
 */
@Serializable
public class AxisPointerOption {
    /** Whether the pointer is shown. */
    public var show: Boolean? = null

    /** Pointer type: `"line"`, `"shadow"`, `"cross"` or `"none"`. */
    public var type: String? = null

    /** Axis the pointer is attached to: `"x"`, `"y"`, `"radius"` or `"angle"`. */
    public var axis: String? = null

    /** Whether the pointer snaps to the nearest data point. */
    public var snap: Boolean? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Pointer label. */
    public var label: AxisPointerLabelOption? = null

    /** Line style of a `"line"` or `"cross"` pointer. */
    public var lineStyle: LineStyleOption? = null

    /** Shadow style of a `"shadow"` pointer. */
    public var shadowStyle: AreaStyleOption? = null

    /** Handle of a draggable pointer; the shape is a render callback and cannot be expressed as JSON. */
    public var handle: JsonElement? = null

    /** Links pointers of several axes; the link shape is a callback and cannot be expressed as JSON. */
    public var link: JsonElement? = null

    /** Whether the pointer triggers the tooltip. */
    public var triggerTooltip: Boolean? = null

    /** Event that triggers the pointer: `"mousemove"`, `"click"` or `"mousemove|click"`. */
    public var triggerOn: String? = null

    /** Whether pointer animations are enabled. */
    public var animation: Boolean? = null

    /** Update animation duration in milliseconds. */
    public var animationDurationUpdate: Double? = null

    /** Update animation easing name. */
    public var animationEasingUpdate: String? = null
}

/** Label of an [AxisPointerOption]. */
@Serializable
public class AxisPointerLabelOption : LabelOption()
