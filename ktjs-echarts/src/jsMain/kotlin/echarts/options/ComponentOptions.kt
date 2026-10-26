package echarts.options

import echarts.TooltipTriggerOn
import echarts.TooltipTrigger
import echarts.Orient
import echarts.LegendType
import echarts.AxisType
import echarts.AxisPosition
import echarts.AnimationEasing
import js.objects.Record
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.TextOverflow
import zrender.ZRColor

/**
 * Title component.
 */
external interface TitleOption {
    /** Component id. */
    var id: String?

    /** Whether the title is shown. */
    var show: Boolean?

    /** Main title text; `\n` starts a new line. */
    var text: String?

    /** Subtitle text. */
    var subtext: String?

    /** Hyperlink of the main title. */
    var link: String?

    /** Hyperlink of the subtitle. */
    var sublink: String?

    /** Target of [link] and [sublink]: `"self"` or `"blank"`. */
    var target: String?

    /** Style of the main title. */
    var textStyle: TextStyleOption?

    /** Style of the subtitle. */
    var subtextStyle: TextStyleOption?

    /** Horizontal alignment: `"auto"`, `"left"`, `"right"` or `"center"`. */
    var textAlign: String?

    /** Vertical alignment: `"auto"`, `"top"`, `"bottom"` or `"middle"`. */
    var textVerticalAlign: String?

    /** Whether the title emits mouse events. */
    var triggerEvent: Boolean?

    /** Padding of the title block. */
    var padding: DoubleOrDoubleArray?

    /** Gap between the title and the subtitle in pixels. */
    var itemGap: Double?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Background color of the title block. */
    var backgroundColor: ZRColor?

    /** Border color of the title block. */
    var borderColor: ZRColor?

    /** Border width of the title block. */
    var borderWidth: Double?

    /** Border radius of the title block. */
    var borderRadius: DoubleOrDoubleArray?

    /** Shadow blur radius of the title block. */
    var shadowBlur: Double?

    /** Shadow color of the title block. */
    var shadowColor: String?

    /** Horizontal shadow offset of the title block. */
    var shadowOffsetX: Double?

    /** Vertical shadow offset of the title block. */
    var shadowOffsetY: Double?
}

/**
 * Legend component.
 */
external interface LegendOption {
    /** Component id. */
    var id: String?

    /** Whether the legend is shown. */
    var show: Boolean?

    /** Rendering mode of the legend. */
    var type: LegendType?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the component. */
    var width: StringOrDouble?

    /** Height of the component. */
    var height: StringOrDouble?

    /** Orientation: `"horizontal"` or `"vertical"`. */
    var orient: Orient?

    /** Alignment of the items: `"auto"`, `"left"` or `"right"`. */
    var align: String?

    /** Padding of the component. */
    var padding: DoubleOrDoubleArray?

    /** Gap between items in pixels. */
    var itemGap: Double?

    /** Width of a legend item. */
    var itemWidth: Double?

    /** Height of a legend item. */
    var itemHeight: Double?

    /** Style of the legend symbol. */
    var itemStyle: ItemStyleOption?

    /** Style of the legend line. */
    var lineStyle: LineStyleOption?

    /** Rotation of the legend symbols in degrees, or `"inherit"`. */
    var symbolRotate: StringOrDouble?

    /** Formatter of the legend labels. */
    var formatter: LabelFormatter?

    /** Selection mode of the legend items. */
    var selectedMode: SelectionMode?

    /** Color of unselected items. */
    var inactiveColor: String?

    /** Border color of unselected items. */
    var inactiveBorderColor: String?

    /** Border width of unselected items, or `"auto"`. */
    var inactiveBorderWidth: StringOrDouble?

    /** Which legend items are selected, keyed by name. */
    var selected: Record<String, Boolean>?

    /** Style of the legend text. */
    var textStyle: TextStyleOption?

    /** Icon of the legend items, e.g. `"circle"`, `"rect"` or `"path://..."`. */
    var icon: String?

    /** Data of the legend items; defaults to the series names. */
    var data: Array<LegendDataItemOption>?

    /** Background color of the component. */
    var backgroundColor: ZRColor?

    /** Border color of the component. */
    var borderColor: ZRColor?

    /** Border width of the component. */
    var borderWidth: Double?

    /** Border radius of the component. */
    var borderRadius: DoubleOrDoubleArray?

    /** Shadow blur radius of the component. */
    var shadowBlur: Double?

    /** Shadow color of the component. */
    var shadowColor: String?

    /** Horizontal shadow offset of the component. */
    var shadowOffsetX: Double?

    /** Vertical shadow offset of the component. */
    var shadowOffsetY: Double?

    /** Icon of the scroll buttons. */
    var pageIconColor: String?

    /** Color of disabled scroll buttons. */
    var pageIconInactiveColor: String?

    /** Size of the scroll buttons. */
    var pageIconSize: Double?

    /** Style of the scroll button text. */
    var pageTextStyle: TextStyleOption?

    /** Formatter of the page text. */
    var pageFormatter: LabelFormatter?

    /** Index of the first item shown in the scroll page. */
    var scrollDataIndex: Double?

    /** Whether the legend animates when the selection changes. */
    var animation: Boolean?

    /** Duration of the legend animation. */
    var animationDuration: Double?

    /** Easing of the legend animation. */
    var animationEasing: AnimationEasing?

    /** Duration of the legend update animation. */
    var animationDurationUpdate: Double?

    /** Easing of the legend update animation. */
    var animationEasingUpdate: AnimationEasing?
}

/**
 * One entry of [LegendOption.data].
 */
external interface LegendDataItemOption {
    /** Name of the series this item controls. */
    var name: String?

    /** Icon of the item. */
    var icon: String?

    /** Style of the item symbol. */
    var itemStyle: ItemStyleOption?

    /** Style of the item line. */
    var lineStyle: LineStyleOption?

    /** Rotation of the item symbol in degrees, or `"inherit"`. */
    var symbolRotate: StringOrDouble?

    /** Style of the item text. */
    var textStyle: TextStyleOption?

    /** Color of the item when it is not selected. */
    var inactiveColor: String?

    /** Border color of the item when it is not selected. */
    var inactiveBorderColor: String?

    /** Border width of the item when it is not selected, or `"auto"`. */
    var inactiveBorderWidth: StringOrDouble?

    /** Tooltip specific to this item. Upstream types it as `unknown`. */
    var tooltip: JsAny?
}

/**
 * Tooltip component.
 */
external interface TooltipOption {
    /** Component id. */
    var id: String?

    /** Whether the tooltip is shown. */
    var show: Boolean?

    /** Trigger type: `"item"`, `"axis"` or `"none"`. */
    var trigger: TooltipTrigger?

    /** Axis pointer configuration when [trigger] is `"axis"`. */
    var axisPointer: AxisPointerOption?

    /** Whether to show the content box. */
    var showContent: Boolean?

    /** Whether the tooltip is always visible. */
    var alwaysShowContent: Boolean?

    /** Event that triggers the tooltip. */
    var triggerOn: TooltipTriggerOn?

    /** Delay before showing the tooltip, in milliseconds. */
    var showDelay: Double?

    /** Delay before hiding the tooltip, in milliseconds. */
    var hideDelay: Double?

    /** Whether the mouse can enter the tooltip. */
    var enterable: Boolean?

    /** Render mode: `"html"` or `"richText"`. */
    var renderMode: String?

    /** Whether the tooltip is confined to the chart container. */
    var confine: Boolean?

    /** Whether the tooltip is appended to the document body. */
    var appendToBody: Boolean?

    /** Class name added to the HTML tooltip. */
    var className: String?

    /** Duration of the position transition in milliseconds. */
    var transitionDuration: Double?

    /** Position of the tooltip, absolute or relative to the mouse. */
    var position: TooltipPosition?

    /** Formatter of the tooltip content. */
    var formatter: TooltipFormatter?

    /** Formatter of a single value inside the default tooltip content. */
    var valueFormatter: TooltipValueFormatter?

    /** Background color of the tooltip. */
    var backgroundColor: ZRColor?

    /** Border color of the tooltip. */
    var borderColor: ZRColor?

    /** Border width of the tooltip. */
    var borderWidth: Double?

    /** Padding of the tooltip. */
    var padding: DoubleOrDoubleArray?

    /** Style of the tooltip text. */
    var textStyle: TextStyleOption?

    /** Extra CSS applied to the HTML tooltip. */
    var extraCssText: String?

    /** Order of the tooltip content: `"seriesAsc"`, `"seriesDesc"`, `"valueAsc"` or `"valueDesc"`. */
    var order: String?
}

/**
 * Tooltip formatter: a template string or a callback.
 */
external interface TooltipFormatter

/** Wraps a template string such as `"{b}: {c}"`. */
fun TooltipFormatter(template: String): TooltipFormatter = template.unsafeCast<TooltipFormatter>()

/** Wraps a callback that receives the item parameters and returns HTML, an element or elements. */
fun TooltipFormatter(callback: (Array<CallbackDataParams>) -> TooltipContent): TooltipFormatter =
    callback.unsafeCast<TooltipFormatter>()

/**
 * Tooltip value formatter: a template string or a callback.
 */
external interface TooltipValueFormatter

/** Wraps a template string. */
fun TooltipValueFormatter(template: String): TooltipValueFormatter = template.unsafeCast<TooltipValueFormatter>()

/** Wraps a callback that receives the raw value and its data index. */
fun TooltipValueFormatter(
    callback: (value: OptionDataValueOrArray, dataIndex: Double) -> String,
): TooltipValueFormatter = callback.unsafeCast<TooltipValueFormatter>()

/**
 * Grid component: the cartesian coordinate system of x and y axes.
 */
external interface GridOption {
    /** Component id. */
    var id: String?

    /** Whether the grid is shown. */
    var show: Boolean?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the grid. */
    var width: StringOrDouble?

    /** Height of the grid. */
    var height: StringOrDouble?

    /** Whether the grid expands to contain the axis labels. */
    var containLabel: Boolean?

    /** Background color of the grid. */
    var backgroundColor: ZRColor?

    /** Border color of the grid. */
    var borderColor: ZRColor?

    /** Border width of the grid. */
    var borderWidth: Double?

    /** Shadow blur radius of the grid. */
    var shadowBlur: Double?

    /** Shadow color of the grid. */
    var shadowColor: String?

    /** Horizontal shadow offset of the grid. */
    var shadowOffsetX: Double?

    /** Vertical shadow offset of the grid. */
    var shadowOffsetY: Double?

    /** Mode of the outer bounds: `"auto"`, `"same"` or `"none"`. */
    var outerBoundsMode: String?

    /** Outer bounds of the grid. */
    var outerBounds: BoxLayoutOption?

    /** What the outer bounds contain: `"all"`, `"axisLabel"` or `"auto"`. */
    var outerBoundsContain: String?

    /** Minimum width of the grid when the outer bounds shrink it. */
    var outerBoundsClampWidth: StringOrDouble?

    /** Minimum height of the grid when the outer bounds shrink it. */
    var outerBoundsClampHeight: StringOrDouble?

    /** Tooltip configuration specific to the grid. */
    var tooltip: TooltipOption?
}

/**
 * Common configuration of a cartesian, polar, single or parallel axis.
 *
 * Concrete axis options ([XAxisOption], [YAxisOption], ...) add the fields that are specific to
 * their coordinate system.
 */
external interface AxisOption {
    /** Component id. */
    var id: String?

    /** Whether the axis is shown. */
    var show: Boolean?

    /** Index of the grid this axis belongs to. */
    var gridIndex: Double?

    /** Whether ticks of multiple axes are aligned. */
    var alignTicks: Boolean?

    /** Position of the axis relative to the grid. */
    var position: AxisPosition?

    /** Offset of the axis line in pixels. */
    var offset: Double?

    /** Type of the axis. */
    var type: AxisType?

    /** Name displayed next to the axis. */
    var name: String?

    /** Location of [name]: `"start"`, `"middle"` or `"end"`. */
    var nameLocation: String?

    /** Style of [name]. */
    var nameTextStyle: LabelOption?

    /** Truncation of [name]. */
    var nameTruncate: NameTruncateOption?

    /** Distance between [name] and the axis line in pixels. */
    var nameGap: Double?

    /** Rotation of [name] in degrees. */
    var nameRotate: Double?

    /** Whether to move [name] to avoid overlapping the axis labels. */
    var nameMoveOverlap: BooleanOrString?

    /** Whether to invert the axis. */
    var inverse: Boolean?

    /** Whether there is a gap at the ends of a category axis. */
    var boundaryGap: BooleanOrStringArray?

    /** Minimum value of a value axis. */
    var min: AxisMinMax?

    /** Maximum value of a value axis. */
    var max: AxisMinMax?

    /** Whether the scale is computed from the data extents instead of `0`. */
    var scale: Boolean?

    /** Number of split segments. */
    var splitNumber: Double?

    /** Minimum distance between two ticks. */
    var minInterval: Double?

    /** Maximum distance between two ticks. */
    var maxInterval: Double?

    /** Forced distance between two ticks. */
    var interval: AxisInterval?

    /** Base of a log axis. */
    var logBase: Double?

    /** Whether the axis is silent (ignores mouse events). */
    var silent: Boolean?

    /** Whether the axis emits mouse events. */
    var triggerEvent: Boolean?

    /** Configuration of the axis line. */
    var axisLine: AxisLineOption?

    /** Configuration of the axis ticks. */
    var axisTick: AxisTickOption?

    /** Configuration of the axis labels. */
    var axisLabel: AxisLabelOption?

    /** Configuration of the split lines. */
    var splitLine: SplitLineOption?

    /** Configuration of the split areas. */
    var splitArea: SplitAreaOption?

    /** Configuration of the minor ticks. */
    var minorTick: AxisTickOption?

    /** Configuration of the minor split lines. */
    var minorSplitLine: SplitLineOption?

    /** Axis pointer configuration for this axis. */
    var axisPointer: AxisPointerOption?

    /** Whether the axis animates. */
    var animation: Boolean?

    /** Duration of the axis animation. */
    var animationDuration: Double?

    /** Easing of the axis animation. */
    var animationEasing: AnimationEasing?

    /** z-index of the axis. */
    var z: Double?

    /** z-level of the axis. */
    var zlevel: Double?

    /** Data of a category axis: raw values or [CategoryAxisDataItem] objects. */
    var data: Array<OptionDataItem?>?
}

/**
 * One entry of a category axis [AxisOption.data], with an optional text style.
 */
external interface CategoryAxisDataItem {
    /** Value of the category. */
    var value: OptionDataValue?

    /** Style of the axis label of this category. */
    var textStyle: TextStyleOption?
}

/**
 * Truncation of an axis name.
 */
external interface NameTruncateOption {
    /** Maximum width before truncation. */
    var maxWidth: Double?

    /** Text appended when the name is truncated. */
    var ellipsis: String?

    /** Placeholder shown while the name is not available. */
    var placeholder: String?
}

/**
 * X axis of a cartesian coordinate system.
 */
external interface XAxisOption : AxisOption

/**
 * Y axis of a cartesian coordinate system.
 */
external interface YAxisOption : AxisOption

/**
 * Angle axis of a polar coordinate system.
 */
external interface AngleAxisOption : AxisOption {
    /** Index of the polar coordinate system this axis belongs to. */
    var polarIndex: Double?
}

/**
 * Radius axis of a polar coordinate system.
 */
external interface RadiusAxisOption : AxisOption {
    /** Index of the polar coordinate system this axis belongs to. */
    var polarIndex: Double?
}

/**
 * Single axis, used by the `themeRiver` series.
 */
external interface SingleAxisOption : AxisOption {
    /** Orientation: `"horizontal"` or `"vertical"`. */
    var orient: Orient?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the axis. */
    var width: StringOrDouble?

    /** Height of the axis. */
    var height: StringOrDouble?
}

/**
 * One axis of a parallel coordinate system.
 */
external interface ParallelAxisOption : AxisOption {
    /** Index of the parallel coordinate system this axis belongs to. */
    var parallelIndex: Double?

    /** Whether the axis can be dragged to reorder. */
    var realtime: Boolean?

    /** Dimensions of the data shown by this axis. */
    var dim: IndexOrIndexArray?

    /** Style of the area selected by the axis brush. */
    var areaSelectStyle: AreaSelectStyleOption?
}

/**
 * Style of the area selected by a parallel axis.
 */
external interface AreaSelectStyleOption {
    /** Width of the selected band. */
    var width: Double?

    /** Fill color of the selected band. */
    var color: ZRColor?

    /** Border color of the selected band. */
    var borderColor: ZRColor?

    /** Border width of the selected band. */
    var borderWidth: Double?

    /** Opacity of the selected band. */
    var opacity: Double?
}

/**
 * Axis line configuration.
 */
external interface AxisLineOption {
    /** Whether the axis line is shown, or `"auto"`. */
    var show: BooleanOrString?

    /** Whether the axis line is on the zero value, or `"auto"`. */
    var onZero: BooleanOrString?

    /** Index of the axis the zero line is aligned with. */
    var onZeroAxisIndex: Double?

    /** Symbol of the axis line ends. */
    var symbol: StringOrStringArray?

    /** Size of the axis line symbols. */
    var symbolSize: Array<Double>?

    /** Offset of the axis line symbols. */
    var symbolOffset: SymbolOffset?

    /** Style of the axis line. */
    var lineStyle: LineStyleOption?
}

/**
 * Axis tick configuration.
 */
external interface AxisTickOption {
    /** Whether the ticks are shown, or `"auto"`. */
    var show: BooleanOrString?

    /** Whether the ticks point inside the grid. */
    var inside: Boolean?

    /** Length of the ticks in pixels. */
    var length: Double?

    /** Style of the ticks. */
    var lineStyle: LineStyleOption?

    /** Interval between two ticks. */
    var interval: AxisInterval?

    /** Whether ticks align with the labels. */
    var alignWithLabel: Boolean?
}

/**
 * Axis label configuration.
 */
external interface AxisLabelOption : LabelOption {
    /** Whether the labels are shown. */
    override var show: Boolean?

    /** Interval between two labels, or a callback deciding which labels to show. */
    var interval: AxisInterval?

    /** Whether the labels are drawn inside the grid. */
    var inside: Boolean?

    /** Distance between a label and the axis line in pixels. */
    var margin: Double?

    /** Whether to hide labels that overlap. */
    var hideOverlap: Boolean?

    /** Whether to show the label of the minimum tick. */
    var showMinLabel: Boolean?

    /** Whether to show the label of the maximum tick. */
    var showMaxLabel: Boolean?

    /** Formatter of the axis labels. */
    override var formatter: LabelFormatter?

    /** Number of lines kept when the label overflows. */
    override var overflow: TextOverflow?
}

/**
 * Split line configuration.
 */
external interface SplitLineOption {
    /** Whether the split lines are shown. */
    var show: Boolean?

    /** Interval between two split lines. */
    var interval: AxisInterval?

    /** Style of the split lines. */
    var lineStyle: LineStyleOption?

    /** Whether the split line at the minimum value is shown. */
    var showMinLine: Boolean?

    /** Whether the split line at the maximum value is shown. */
    var showMaxLine: Boolean?
}

/**
 * Split area configuration.
 */
external interface SplitAreaOption {
    /** Whether the split areas are shown. */
    var show: Boolean?

    /** Interval between two split areas. */
    var interval: AxisInterval?

    /** Style of the split areas. */
    var areaStyle: AreaStyleOption?
}
