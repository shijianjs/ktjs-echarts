package echartsgl.options

import echarts.AxisNameLocation
import echarts.AxisType
import echarts.options.AxisInterval
import echarts.options.AxisMinMax
import echarts.options.BooleanOrStringArray
import echarts.options.LabelFormatter
import echarts.options.LineStyleOption
import echarts.options.OptionDataItem
import echarts.options.SplitAreaStyleOption
import echarts.options.SplitLineStyleOption
import echarts.options.TextStyleOption
import kotlin.js.unsafeCast

/**
 * One of the `xAxis3D` / `yAxis3D` / `zAxis3D` axes of a [Grid3DOption].
 *
 * The three axes share one type: upstream creates them from the same model factory and only the
 * default name differs (`X`, `Y`, `Z`).
 */
external interface Axis3DOption {
    /** Axis id, used by `grid3DIndex`-free queries and `getOption` merges. */
    var id: String?

    /** Whether the axis is shown. */
    var show: Boolean?

    /** Index of the [grid3D][Grid3DOption] the axis belongs to. */
    var grid3DIndex: Double?

    /** Whether the axis is inverted. */
    var inverse: Boolean?

    /** Name of the axis, drawn next to the axis line. */
    var name: String?

    /** Position of [name] relative to the axis line. */
    var nameLocation: AxisNameLocation?

    /** Style of [name]. */
    var nameTextStyle: TextStyleOption?

    /** Gap between [name] and the axis line, in 3D space rather than screen pixels. */
    var nameGap: Double?

    /** Type of the axis. */
    var type: AxisType?

    /** Minimum value of the axis, or a keyword such as `"dataMin"`. */
    var min: AxisMinMax?

    /** Maximum value of the axis, or a keyword such as `"dataMax"`. */
    var max: AxisMinMax?

    /** Whether the axis extent no longer has to contain zero. Value axes only. */
    var scale: Boolean?

    /** Recommended number of segments of the axis. */
    var splitNumber: Double?

    /** Minimum gap between two split lines. */
    var minInterval: Double?

    /** Maximum gap between two split lines. */
    var maxInterval: Double?

    /** Forced interval between ticks. */
    var interval: AxisInterval?

    /** Base of a logarithmic axis. */
    var logBase: Double?

    /** Whether there is a gap at the ends of a category axis. */
    var boundaryGap: BooleanOrStringArray?

    /** Category data, or values that come from JavaScript. */
    var data: Array<OptionDataItem?>?

    /** Configuration of the axis pointer. */
    var axisPointer: Axis3DPointerOption?

    /** Configuration of the axis line. */
    var axisLine: Axis3DLineOption?

    /** Configuration of the axis ticks. */
    var axisTick: Axis3DTickOption?

    /** Configuration of the axis labels. */
    var axisLabel: Axis3DLabelOption?

    /** Configuration of the split lines. */
    var splitLine: Axis3DSplitLineOption?

    /** Configuration of the split areas. */
    var splitArea: Axis3DSplitAreaOption?
}

/**
 * Axis line of an [Axis3DOption] or of a [Grid3DOption].
 */
external interface Axis3DLineOption {
    /** Whether the axis line is shown. */
    var show: Boolean?

    /** Interval between two rendered segments; category axes only. */
    var interval: AxisInterval?

    /** Style of the axis line. */
    var lineStyle: LineStyleOption?
}

/**
 * Axis ticks of an [Axis3DOption] or of a [Grid3DOption].
 */
external interface Axis3DTickOption {
    /** Whether the ticks are shown. */
    var show: Boolean?

    /** Interval between two ticks; category axes only. */
    var interval: AxisInterval?

    /** Whether the ticks point into the grid. */
    var inside: Boolean?

    /** Length of a tick, in 3D space. */
    var length: Double?

    /** Style of the ticks. */
    var lineStyle: LineStyleOption?
}

/**
 * Tick labels of an [Axis3DOption] or of a [Grid3DOption].
 */
external interface Axis3DLabelOption {
    /** Whether the labels are shown. */
    var show: Boolean?

    /** Interval between two labels, or a callback deciding which labels to show. */
    var interval: AxisInterval?

    /** Distance between a label and the axis line, in 3D space. */
    var margin: Double?

    /** Rotation of the labels in degrees. */
    var rotate: Double?

    /** Whether the labels are drawn inside the grid. */
    var inside: Boolean?

    /** Formatter: a template string such as `"{value} kg"` or a callback. */
    var formatter: LabelFormatter?

    /** Text style of the labels. */
    var textStyle: TextStyleOption?
}

/**
 * Split lines of an [Axis3DOption] or of a [Grid3DOption].
 */
external interface Axis3DSplitLineOption {
    /** Whether the split lines are shown. */
    var show: Boolean?

    /** Interval between two split lines. */
    var interval: AxisInterval?

    /** Style of the split lines; `color` also accepts a palette cycled between the lines. */
    var lineStyle: SplitLineStyleOption?
}

/**
 * Split areas of an [Axis3DOption] or of a [Grid3DOption].
 */
external interface Axis3DSplitAreaOption {
    /** Whether the split areas are shown. */
    var show: Boolean?

    /** Interval between two split areas. */
    var interval: AxisInterval?

    /** Style of the split areas. */
    var areaStyle: SplitAreaStyleOption?
}

/**
 * Axis pointer of an [Axis3DOption] or of a [Grid3DOption].
 */
external interface Axis3DPointerOption {
    /** Whether the pointer is shown. */
    var show: Boolean?

    /** Style of the pointer line. */
    var lineStyle: LineStyleOption?

    /** Label of the pointer. */
    var label: Axis3DPointerLabelOption?
}

/**
 * Label of an [Axis3DPointerOption].
 */
external interface Axis3DPointerLabelOption {
    /** Whether the label is shown. Value axes show it, category axes do not. */
    var show: Boolean?

    /** Distance between the label and the axis, in 3D space. */
    var margin: Double?

    /** Formats the label from the value of the axis and of every axis. */
    var formatter: Axis3DPointerFormatter?

    /** Text style of the label. */
    var textStyle: TextStyleOption?
}

/**
 * Formats the label of an [Axis3DPointerOption].
 *
 * The first argument is the value of the current axis, the second one the values of all axes.
 */
external interface Axis3DPointerFormatter

/** Wraps a callback that formats the label. */
fun Axis3DPointerFormatter(callback: (value: Double, valueAll: Array<Double>) -> String): Axis3DPointerFormatter =
    callback.unsafeCast<Axis3DPointerFormatter>()
