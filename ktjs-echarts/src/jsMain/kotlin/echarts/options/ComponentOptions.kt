package echarts.options

import echarts.TooltipTriggerOn
import echarts.TooltipTrigger
import echarts.AxisNameLocation
import echarts.HorizontalAlign
import echarts.LegendPageButtonPosition
import echarts.OuterBoundsContain
import echarts.OuterBoundsMode
import echarts.TooltipOrder
import echarts.TooltipRenderMode
import echarts.Orient
import echarts.LegendType
import echarts.AxisType
import echarts.AxisPosition
import echarts.TitleTarget
import echarts.AnimationEasing
import js.objects.Record
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.canvas.CanvasLineCap
import web.canvas.CanvasLineJoin
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import web.html.HTMLElement
import zrender.TextOverflow
import zrender.ZRColor
import zrender.ZRLineType

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

    /** Target of [link]: `"self"` or `"blank"`. */
    var target: TitleTarget?

    /** Target of [sublink]: `"self"` or `"blank"`. */
    var subtarget: TitleTarget?

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
 * Style shared by a legend and by one entry of its data.
 *
 * Upstream splits this into `LegendStyleOption`; the members are the ones a legend item can
 * override for itself.
 */
external interface LegendStyleOption {
    /** Icon of the item, e.g. `"circle"`, `"rect"` or `"path://..."`. */
    var icon: String?

    /** Color of the item when it is not selected. */
    var inactiveColor: String?

    /** Border color of the item when it is not selected. */
    var inactiveBorderColor: String?

    /** Border width of the item when it is not selected, `"auto"` or `"inherit"`. */
    var inactiveBorderWidth: StringOrDouble?

    /** Formatter of the item name. */
    var formatter: LabelFormatter?

    /** Style of the item symbol. */
    var itemStyle: LegendItemStyleOption?

    /** Style of the item line. */
    var lineStyle: LegendLineStyleOption?

    /** Style of the item text. */
    var textStyle: LabelOption?

    /** Rotation of the item symbol in degrees, or `"inherit"`. */
    var symbolRotate: StringOrDouble?

    /** Whether the symbol keeps its aspect ratio. Deprecated upstream. */
    var symbolKeepAspect: Boolean?
}

/**
 * Item style of a legend item.
 *
 * Upstream widens every [ItemStyleOption] member with `"inherit"` and [borderWidth] also with
 * `"auto"`. Kotlin cannot widen `var` members of an inherited interface, so the members are
 * repeated here. Only [borderWidth] changes to a type that can hold the string forms; the other
 * numeric members still accept `"inherit"` upstream but are typed as numbers here.
 */
external interface LegendItemStyleOption : ShadowOption {
    /** Fill color. */
    var color: ZRColor?

    /** Border color. */
    var borderColor: ZRColor?

    /** Border width in pixels, `"auto"` or `"inherit"`. */
    var borderWidth: StringOrDouble?

    /** Border type: `"solid"`, `"dashed"`, `"dotted"` or a dash pattern. */
    var borderType: ZRLineType?

    /** Line cap of the border. */
    var borderCap: CanvasLineCap?

    /** Line join of the border. */
    var borderJoin: CanvasLineJoin?

    /** Dash offset of a dashed border. */
    var borderDashOffset: Double?

    /** Miter limit of the border. */
    var borderMiterLimit: Double?

    /** Border radius, or an array of radii. */
    var borderRadius: StringOrDoubleOrArray?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Decal pattern applied to the item, or `"none"`. */
    var decal: DecalSetting?

    /** Area fill color, used by map and geo items. */
    var areaColor: ZRColor?
}

/**
 * Line style of a legend item.
 *
 * Upstream widens every [LineStyleOption] member with `"inherit"` and [width] also with `"auto"`;
 * see [LegendItemStyleOption] for why the members are repeated.
 */
external interface LegendLineStyleOption : ShadowOption {
    /** Line color. */
    var color: ZRColor?

    /** Line width in pixels, `"auto"` or `"inherit"`. */
    var width: StringOrDouble?

    /** Line type: `"solid"`, `"dashed"`, `"dotted"`, a number or a `[dash, gap]` array. */
    var type: ZRLineType?

    /** Dash offset. */
    var dashOffset: Double?

    /** Line cap of the line. */
    var cap: CanvasLineCap?

    /** Line join of the line. */
    var join: CanvasLineJoin?

    /** Miter limit for `join = "miter"`. */
    var miterLimit: Double?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Curvature of the line. */
    var curveness: Double?

    /** Color of the line when the item is not selected. */
    var inactiveColor: ZRColor?

    /** Width of the line when the item is not selected. */
    var inactiveWidth: Double?
}

/**
 * Legend component.
 */
external interface LegendOption : LegendStyleOption {
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
    var align: HorizontalAlign?

    /** Position of the page buttons of a scrollable legend: `"start"` or `"end"`. */
    var pageButtonPosition: LegendPageButtonPosition?

    /** Gap between two page buttons, in pixels. */
    var pageButtonItemGap: Double?

    /** Gap between the page buttons and the legend items, in pixels. */
    var pageButtonGap: Double?

    /** Images of the page buttons, keyed by orientation. */
    var pageIcons: LegendPageIconsOption?

    /** Padding of the component. */
    var padding: DoubleOrDoubleArray?

    /** Gap between items in pixels. */
    var itemGap: Double?

    /** Width of a legend item. */
    var itemWidth: Double?

    /** Height of a legend item. */
    var itemHeight: Double?

    /** Selection mode of the legend items. */
    var selectedMode: SelectionMode?

    /** Which legend items are selected, keyed by name. */
    var selected: Record<String, Boolean>?

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
    var pageTextStyle: LabelOption?

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
 * Images of the page buttons of a scrollable legend, keyed by orientation.
 */
external interface LegendPageIconsOption {
    /** Images used when the legend scrolls horizontally. */
    var horizontal: Array<String>?

    /** Images used when the legend scrolls vertically. */
    var vertical: Array<String>?
}

/**
 * One entry of [LegendOption.data].
 */
external interface LegendDataItemOption : LegendStyleOption {
    /** Name of the series this item controls. */
    var name: String?

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

    /** Render mode: `"auto"`, `"html"` or `"richText"`. */
    var renderMode: TooltipRenderMode?

    /** Whether the tooltip is confined to the chart container. */
    var confine: Boolean?

    /** Horizontal alignment of the tooltip relative to the pointer. */
    var align: HorizontalAlign?

    /** Vertical alignment of the tooltip relative to the pointer. */
    var verticalAlign: VerticalAlign?

    /** Whether showing and hiding the tooltip is animated. */
    var displayTransition: Boolean?

    /** Whether the tooltip is appended to the document body. */
    var appendToBody: Boolean?

    /** Element or selector the tooltip element is appended to; only used by the HTML renderer. */
    var appendTo: TooltipAppendTo?

    /** Class name added to the HTML tooltip. */
    var className: String?

    /** Border color used when several series are shown and a series has no own color. */
    var defaultBorderColor: String?

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

    /** Border radius of the HTML tooltip in pixels. */
    var borderRadius: Double?

    /** Blur of the tooltip shadow. */
    var shadowBlur: Double?

    /** Color of the tooltip shadow. */
    var shadowColor: String?

    /** Horizontal offset of the tooltip shadow. */
    var shadowOffsetX: Double?

    /** Vertical offset of the tooltip shadow. */
    var shadowOffsetY: Double?

    /** Padding of the tooltip. */
    var padding: DoubleOrDoubleArray?

    /** Style of the tooltip text. */
    var textStyle: TextStyleOption?

    /** Extra CSS applied to the HTML tooltip. */
    var extraCssText: String?

    /** Order of the tooltip content: `"seriesAsc"`, `"seriesDesc"`, `"valueAsc"` or `"valueDesc"`. */
    var order: TooltipOrder?
}

/**
 * Target the HTML tooltip element is appended to: a selector, an element or a callback that
 * receives the chart container.
 */
external interface TooltipAppendTo

/** Wraps a CSS selector. */
fun TooltipAppendTo(selector: String): TooltipAppendTo = selector.unsafeCast<TooltipAppendTo>()

/** Wraps an element. */
fun TooltipAppendTo(element: HTMLElement): TooltipAppendTo = element.unsafeCast<TooltipAppendTo>()

/** Wraps a callback that chooses the element from the chart container. */
fun TooltipAppendTo(callback: (chartContainer: HTMLElement) -> HTMLElement?): TooltipAppendTo =
    callback.unsafeCast<TooltipAppendTo>()

/**
 * Tooltip formatter: a template string or a callback.
 */
external interface TooltipFormatter

/** Wraps a template string such as `"{b}: {c}"`. */
fun TooltipFormatter(template: String): TooltipFormatter = template.unsafeCast<TooltipFormatter>()

/**
 * Wraps a callback that receives the formatter parameters and returns HTML, an element or elements.
 *
 * The parameters are a single object when [TooltipOption.trigger] is `"item"` and an array when it
 * is `"axis"`; use the [TopLevelFormatterParams] factories to read the matching branch.
 */
fun TooltipFormatter(callback: TooltipFormatterCallback): TooltipFormatter =
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

    /** Coordinate system the grid is laid out on, e.g. `"matrix"` or `"calendar"`. */
    var coordinateSystem: String?

    /**
     * Cells of that coordinate system the grid is laid out on; see
     * [CoordinateSystemDataCoord][echarts.options.CoordinateSystemDataCoord].
     */
    var coord: CoordinateSystemDataCoord?

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
    var outerBoundsMode: OuterBoundsMode?

    /** Outer bounds of the grid. */
    var outerBounds: BoxLayoutOption?

    /** What the outer bounds contain: `"all"`, `"axisLabel"` or `"auto"`. */
    var outerBoundsContain: OuterBoundsContain?

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

    /** Id of the grid this axis belongs to, takes precedence over [gridIndex]. */
    var gridId: String?

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

    /** Location of [name]: `"start"`, `"middle"`, `"center"` or `"end"`. */
    var nameLocation: AxisNameLocation?

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

    /** Number of elements above which animation is disabled. */
    var animationThreshold: Double?

    /** Duration of the axis animation in milliseconds, or a callback per element. */
    var animationDuration: AnimationDuration?

    /** Easing of the axis animation. */
    var animationEasing: AnimationEasing?

    /** Delay of the axis animation in milliseconds, or a callback per element. */
    var animationDelay: AnimationDelay?

    /** Duration of the update animation in milliseconds, or a callback per element. */
    var animationDurationUpdate: AnimationDuration?

    /** Easing of the update animation. */
    var animationEasingUpdate: AnimationEasing?

    /** Delay of the update animation in milliseconds, or a callback per element. */
    var animationDelayUpdate: AnimationDelay?

    /** z-index of the axis. */
    var z: Double?

    /** z-level of the axis. */
    var zlevel: Double?

    /** Whether the axis shows a tooltip; the axis itself only supports `show`. */
    var tooltip: AxisTooltipOption?

    /** Value the axis extent is expanded to, usually the base of a bar or area series. */
    var startValue: Double?

    /** Magnitude of the random offset applied to symbols on a category axis. */
    var jitter: Double?

    /** Whether symbols that would overlap share the same jitter offset. */
    var jitterOverlap: Boolean?

    /** Extra distance kept between jittered symbols. */
    var jitterMargin: Double?

    /** Breaks drawn on the axis, see [AxisBreakOption]. */
    var breaks: Array<AxisBreakOption>?

    /** Style of the break areas. */
    var breakArea: AxisBreakAreaOption?

    /** Layout of the labels next to the breaks. */
    var breakLabelLayout: AxisBreakLabelLayoutOption?

    /** Whether the axis shape (for example the zigzag of a break) is drawn. */
    var containShape: Boolean?

    /** Data minimum included in the axis extent, in addition to the data extent. */
    var dataMin: OptionDataValue?

    /** Data maximum included in the axis extent, in addition to the data extent. */
    var dataMax: OptionDataValue?

    /** Whether duplicated category values are removed. */
    var deduplication: Boolean?

    /** Order of the categories of a category axis. */
    var categorySortInfo: OrdinalSortInfo?

    /** Data of a category axis: raw values or [CategoryAxisDataItem] objects. */
    var data: Array<OptionDataItem?>?
}

/**
 * One break of an axis: the range between [start] and [end] is compressed when the break is not
 * expanded.
 */
external interface AxisBreakOption : AxisBreakIdentifier {
    /** Size of the gap that replaces the range: a pixel number or a percentage string. */
    var gap: StringOrDouble?

    /** Whether the break starts expanded. */
    var isExpanded: Boolean?
}

/**
 * The range of a break, as used by the `*AxisBreak` action payloads.
 *
 * Upstream `AxisBreakOptionIdentifierInAxis`, which is `Pick<AxisBreakOption, "start" | "end">`.
 */
external interface AxisBreakIdentifier {
    /** Start of the compressed range. */
    var start: OptionDataValue

    /** End of the compressed range. */
    var end: OptionDataValue
}

/**
 * Style of the areas drawn over the breaks of an axis.
 */
external interface AxisBreakAreaOption {
    /** Whether the areas are shown. */
    var show: Boolean?

    /** Style of the areas. */
    var itemStyle: ItemStyleOption?

    /** Amplitude of the zigzag edges in pixels. */
    var zigzagAmplitude: Double?

    /** Minimum span of one zigzag segment in pixels. */
    var zigzagMinSpan: Double?

    /** Maximum span of one zigzag segment in pixels. */
    var zigzagMaxSpan: Double?

    /** z-index of the zigzag. */
    var zigzagZ: Double?

    /** Whether clicking the area expands or collapses the break. */
    var expandOnClick: Boolean?
}

/**
 * Layout of the axis labels that are moved away from a break.
 */
external interface AxisBreakLabelLayoutOption {
    /** Whether labels are moved when they overlap a break: `true`, `false` or `"auto"`. */
    var moveOverlap: BooleanOrString?
}

/**
 * Tooltip configuration available on an axis; only [show] can be set per axis.
 */
external interface AxisTooltipOption {
    /** Whether the axis tooltip is shown. */
    var show: Boolean?
}

/**
 * Order of the categories of a category axis when `categorySortInfo` is used.
 */
external interface OrdinalSortInfo {
    /** Ordinal numbers of the categories. */
    var ordinalNumbers: Array<Double>
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

    /** Id of the polar coordinate system this axis belongs to. */
    var polarId: String?

    /** Angle the axis starts at, in degrees. */
    var startAngle: Double?

    /** Angle the axis ends at, in degrees. */
    var endAngle: Double?

    /** Whether the axis is laid out clockwise; the polar coordinate system is counter-clockwise. */
    var clockwise: Boolean?
}

/**
 * Radius axis of a polar coordinate system.
 */
external interface RadiusAxisOption : AxisOption {
    /** Index of the polar coordinate system this axis belongs to. */
    var polarIndex: Double?

    /** Id of the polar coordinate system this axis belongs to. */
    var polarId: String?
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

    /** Horizontal alignment of the first label; upstream `alignMinLabel`. */
    var alignMinLabel: TextAlign?

    /** Horizontal alignment of the last label; upstream `alignMaxLabel`. */
    var alignMaxLabel: TextAlign?

    /** Vertical alignment of the first label; upstream `verticalAlignMinLabel`. */
    var verticalAlignMinLabel: VerticalAlign?

    /** Vertical alignment of the last label; upstream `verticalAlignMaxLabel`. */
    var verticalAlignMaxLabel: VerticalAlign?

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
    var lineStyle: SplitLineStyleOption?

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
    var areaStyle: SplitAreaStyleOption?
}
