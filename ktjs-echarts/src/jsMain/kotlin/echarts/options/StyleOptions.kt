package echarts.options

import echarts.AnimationEasing
import echarts.BlurScope
import echarts.EmphasisFocus
import echarts.MoveOverlap
import js.objects.Record
import kotlin.js.unsafeCast
import web.canvas.CanvasLineCap
import web.canvas.CanvasLineJoin
import web.cssom.FontStyle
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.RectLike
import zrender.TextOverflow
import zrender.ZRColor
import zrender.ZRFontWeight
import zrender.ZRLineType

/**
 * Shadow settings shared by text and shape styles.
 */
external interface ShadowOption {
    /** Shadow blur radius in pixels. */
    var shadowBlur: Double?

    /** Shadow color. */
    var shadowColor: ZRColor?

    /** Horizontal shadow offset in pixels. */
    var shadowOffsetX: Double?

    /** Vertical shadow offset in pixels. */
    var shadowOffsetY: Double?
}

/**
 * Common text style, used by `textStyle`, axis names, tooltip text and labels.
 */
external interface TextStyleOption : ShadowOption {
    /** Text color; `"inherit"` reuses the parent color. */
    var color: String?

    /** Font style, a CSS `font-style` keyword. */
    var fontStyle: FontStyle?

    /** Font weight: `"normal"`, `"bold"`, `"bolder"`, `"lighter"` or `100`..`900`. */
    var fontWeight: ZRFontWeight?

    /** Font family. */
    var fontFamily: String?

    /** Font size in pixels or as a string. */
    var fontSize: StringOrDouble?

    /** Line height in pixels. */
    var lineHeight: Double?

    /** Horizontal alignment of the text. */
    var align: TextAlign?

    /** Vertical alignment of the text. */
    var verticalAlign: VerticalAlign?

    /** Background color of the text block, or an image pattern. */
    var backgroundColor: ZRColor?

    /** Border color of the text block. */
    var borderColor: String?

    /** Border width of the text block in pixels. */
    var borderWidth: Double?

    /** Border type: `"solid"`, `"dashed"`, `"dotted"` or a dash pattern. */
    var borderType: ZRLineType?

    /** Border radius of the text block. */
    var borderRadius: DoubleOrDoubleArray?

    /** Padding of the text block. */
    var padding: DoubleOrDoubleArray?

    /** Block width; only effective when [overflow] is set. */
    var width: StringOrDouble?

    /** Block height. */
    var height: Double?

    /** Overflow behaviour of the text. */
    var overflow: TextOverflow?

    /** Ellipsis shown when [overflow] is `"truncate"`. */
    var ellipsis: String?

    /** Text stroke color. */
    var textBorderColor: String?

    /** Text stroke width. */
    var textBorderWidth: Double?

    /** Text stroke type: `"solid"`, `"dashed"`, `"dotted"` or a dash pattern. */
    var textBorderType: ZRLineType?

    /** Text stroke dash offset. */
    var textBorderDashOffset: Double?

    /** Text shadow blur radius. */
    var textShadowBlur: Double?

    /** Text shadow color. */
    var textShadowColor: String?

    /** Text shadow horizontal offset. */
    var textShadowOffsetX: Double?

    /** Text shadow vertical offset. */
    var textShadowOffsetY: Double?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Rich text styles keyed by the tags used in [LabelOption.formatter]. */
    var rich: Record<String, TextStyleOption>?
}

/**
 * Item style shared by shapes such as bars, symbols and map areas.
 */
external interface ItemStyleOption : ShadowOption {
    /** Fill color, or a callback computing it from the data item. */
    var color: ZRColor?

    /** Border color. */
    var borderColor: ZRColor?

    /** Border width in pixels. */
    var borderWidth: Double?

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
 * Line style shared by line series, axis lines and borders.
 */
external interface LineStyleOption : ShadowOption {
    /** Line color. */
    var color: ZRColor?

    /** Line width in pixels. */
    var width: Double?

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

    /** Curvature of the line, used by line series and graph edges. */
    var curveness: Double?
}

/**
 * Area style shared by area charts and map regions.
 */
external interface AreaStyleOption : ShadowOption {
    /** Fill color. */
    var color: ZRColor?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Fill origin: `"auto"`, `"start"` or `"end"`. */
    var origin: String?

    /** Decal pattern applied to the area, or `"none"`. */
    var decal: DecalSetting?
}

/**
 * A label line, used by pie, funnel and graph series.
 */
external interface LabelLineOption {
    /** Whether to show the label line. */
    var show: Boolean?

    /** Length of the first segment in pixels. */
    var length: Double?

    /** Length of the second segment in pixels. */
    var length2: Double?

    /** Whether to smooth the line. */
    var smooth: BooleanOrDouble?

    /** Line style. */
    var lineStyle: LineStyleOption?

    /** Minimum turn angle of the line, in degrees. */
    var minTurnAngle: Double?

    /** Maximum surface angle of the line, in degrees. */
    var maxSurfaceAngle: Double?
}

/**
 * Series or component label.
 */
external interface LabelOption : TextStyleOption {
    /** Whether to show the label. */
    var show: Boolean?

    /**
     * Label position. The accepted values depend on the series: `"top"`, `"left"`, `"right"`,
     * `"bottom"`, `"inside"`, `"insideLeft"`, `"outside"`, `"center"`, `[x, y]`, ...
     */
    var position: LabelPosition?

    /** Distance to the element in pixels. */
    var distance: Double?

    /** Rotation in degrees, `-90..90`. */
    var rotate: Double?

    /** Offset from the computed position, `[x, y]`. */
    var offset: Array<Double>?

    /** Formatter: a template string such as `"{b}: {c}"` or a callback. */
    var formatter: LabelFormatter?

    /** Whether the label ignores mouse events. */
    var silent: Boolean?

    /** Whether the label should stay visible during the initial animation. */
    var valueAnimation: Boolean?

    /** Precision of the displayed number, or `"auto"`. */
    var precision: StringOrDouble?

    /** Minimum margin between the label and the container, in pixels. */
    var minMargin: Double?

    /** Space kept around the label to avoid overlapping. */
    var textMargin: DoubleOrDoubleArray?

    /** Overflow behaviour of a single line. */
    var lineOverflow: String?
}

/**
 * Label formatter: a template string or a callback.
 *
 * Create one with `LabelFormatter("{b}: {c}")` or `LabelFormatter { params -> ... }`.
 */
external interface LabelFormatter

/** Wraps a template string such as `"{b}: {c}"`. */
fun LabelFormatter(template: String): LabelFormatter = template.unsafeCast<LabelFormatter>()

/** Wraps a callback that receives the item parameters and returns the label text. */
fun LabelFormatter(callback: (CallbackDataParams) -> String): LabelFormatter = callback.unsafeCast<LabelFormatter>()

/**
 * Layout of the labels of a series, used by `labelLayout`.
 *
 * Every member is optional; the callback form receives [LabelLayoutCallbackParams] and returns the
 * layout of one label.
 */
external interface LabelLayoutOption {
    /** How overlapped labels are moved apart. */
    var moveOverlap: MoveOverlap?

    /** Whether overlapped labels are hidden after they were moved. */
    var hideOverlap: Boolean?

    /** Whether the label can be dragged with the mouse. */
    var draggable: Boolean?

    /** Absolute x position, a pixel number or a percentage string. */
    var x: StringOrDouble?

    /** Absolute y position, a pixel number or a percentage string. */
    var y: StringOrDouble?

    /** Offset on the x axis based on the original position. */
    var dx: Double?

    /** Offset on the y axis based on the original position. */
    var dy: Double?

    /** Rotation in degrees. */
    var rotate: Double?

    /** Horizontal alignment of the text. */
    var align: TextAlign?

    /** Vertical alignment of the text. */
    var verticalAlign: VerticalAlign?

    /** Width of the label box. */
    var width: Double?

    /** Height of the label box. */
    var height: Double?

    /** Font size in pixels. */
    var fontSize: Double?

    /** Points of the label line, `[[x, y], ...]`. */
    var labelLinePoints: Array<Array<Double>>?
}

/**
 * Parameters passed to a [LabelLayout] callback.
 */
external interface LabelLayoutCallbackParams {
    /** Index of the data the label represents, or `null` when it represents no data. */
    val dataIndex: Double?

    /** Type of the data: `"node"` or `"edge"`, used by graph and tree series. */
    val dataType: String?

    /** Index of the series. */
    val seriesIndex: Double

    /** Text of the label. */
    val text: String

    /** Horizontal alignment of the text. */
    val align: TextAlign?

    /** Vertical alignment of the text. */
    val verticalAlign: VerticalAlign?

    /** Rectangle of the element the label belongs to. */
    val rect: RectLike?

    /** Rectangle of the label itself. */
    val labelRect: RectLike?

    /** Points of the label line, `[[x, y], ...]`. */
    val labelLinePoints: Array<Array<Double>>?
}

/**
 * Callback that computes the layout of one label.
 */
typealias LabelLayoutCallback = (params: LabelLayoutCallbackParams) -> LabelLayoutOption

/**
 * Layout of the labels of a series: a fixed [LabelLayoutOption] or a callback.
 */
external interface LabelLayout

/** Wraps a fixed layout. */
fun LabelLayout(value: LabelLayoutOption): LabelLayout = value.unsafeCast<LabelLayout>()

/** Wraps a callback that computes the layout of every label. */
fun LabelLayout(value: LabelLayoutCallback): LabelLayout = value.unsafeCast<LabelLayout>()

/**
 * Parameters passed to most formatter callbacks.
 */
external interface CallbackDataParams {
    /** Component type, e.g. `"series"`. */
    val componentType: String

    /** Component subtype, e.g. `"line"`. */
    val componentSubType: String

    /** Index of the component inside the option. */
    val componentIndex: Int

    /** Series type, e.g. `"line"`. */
    val seriesType: String?

    /** Series index inside the option. */
    val seriesIndex: Int?

    /** Series id. */
    val seriesId: String?

    /** Series name. */
    val seriesName: String?

    /** Name of the data item. */
    val name: String

    /** Data index inside the series. */
    val dataIndex: Int

    /** Raw value of the data item. */
    val value: OptionDataItem

    /** Data item object. */
    val data: OptionDataItem

    /** Data type: `"node"` or `"edge"`, used by graph and tree series. */
    val dataType: String?

    /** Color assigned to the item. */
    val color: ZRColor?

    /** Border color assigned to the item. */
    val borderColor: String?

    /** Names of the data dimensions. */
    val dimensionNames: Array<String>?

    /** Index of the dimension that provided [value]. */
    val dimensionIndex: Double?

    /** Percent value, used by pie, funnel and gauge series. */
    val percent: Double?

    /** Tooltip marker markup for this item. */
    val marker: TooltipMarker?

    /** Display state of the element. */
    val status: String?

    /** Category (x) value, when the series is on an axis. */
    val axisValue: OptionDataValue?

    /** Label of the category (x) value. */
    val axisValueLabel: String?

    /** Type of the axis that provided [axisValue]. */
    val axisType: String?

    /** Whether the item is selected. */
    val selected: Boolean?

    /** Names of the variables available to a template formatter. */
    val `$vars`: Array<String>
}

/**
 * Emphasis (hover) state of a series or element.
 */
external interface EmphasisOption {
    /** Whether the emphasis state is disabled. */
    var disabled: Boolean?

    /** What the emphasis state focuses on. */
    var focus: EmphasisFocus?

    /** Scope that is blurred while [focus] is active. */
    var blurScope: BlurScope?

    /** Whether to scale symbols on hover. */
    var scale: BooleanOrDouble?

    /** Label style in the emphasis state. */
    var label: LabelOption?

    /** Label line style in the emphasis state. */
    var labelLine: LabelLineOption?

    /** Item style in the emphasis state. */
    var itemStyle: ItemStyleOption?

    /** Line style in the emphasis state. */
    var lineStyle: LineStyleOption?

    /** Area style in the emphasis state. */
    var areaStyle: AreaStyleOption?

    /** Label of the edges in the emphasis state, used by graph and chord series. */
    var edgeLabel: LabelOption?
}

/**
 * Blur (non-focused) state of a series or element.
 */
external interface BlurOption {
    /** Label style in the blur state. */
    var label: LabelOption?

    /** Label line style in the blur state. */
    var labelLine: LabelLineOption?

    /** Item style in the blur state. */
    var itemStyle: ItemStyleOption?

    /** Line style in the blur state. */
    var lineStyle: LineStyleOption?

    /** Area style in the blur state. */
    var areaStyle: AreaStyleOption?

    /** Label of the edges in the blur state, used by graph and chord series. */
    var edgeLabel: LabelOption?
}

/**
 * Select state of a series or element.
 */
external interface SelectOption {
    /** Whether selection is disabled. */
    var disabled: Boolean?

    /** Label style in the selected state. */
    var label: LabelOption?

    /** Item style in the selected state. */
    var itemStyle: ItemStyleOption?

    /** Line style in the selected state. */
    var lineStyle: LineStyleOption?

    /** Area style in the selected state. */
    var areaStyle: AreaStyleOption?

    /** Label of the edges in the selected state, used by graph and chord series. */
    var edgeLabel: LabelOption?
}

/**
 * Mark point, line and area styling.
 *
 * Upstream `MarkerOption` only carries [silent] and [tooltip] (plus the animation members); the
 * label, item and line styles come from the state option of each mark, and the symbol members from
 * [SymbolOptionMixin] on the marks that support them. `data` is declared on each concrete interface
 * because upstream narrows `MarkerOption.data` (`unknown[]`) to the item type of the mark.
 *
 * @see MarkPointOption
 * @see MarkLineOption
 * @see MarkAreaOption
 */
external interface MarkOption {
    /** Whether the mark is silent (ignores mouse events). */
    var silent: Boolean?

    /** Precision of displayed values. */
    var precision: Double?

    /** Whether the mark animates. */
    var animation: Boolean?

    /** Duration of the animation in milliseconds. */
    var animationDuration: Double?

    /** Easing of the animation. */
    var animationEasing: AnimationEasing?

    /**
     * Tooltip specific to the mark.
     *
     * Upstream `CommonTooltipOption<unknown> & { trigger?: 'item' | 'axis' | boolean | 'none' }`;
     * the boolean form of `trigger` is not modelled.
     */
    var tooltip: TooltipOption?
}

/**
 * Mark point configuration.
 */
external interface MarkPointOption : MarkOption, MarkPointStateOption, SymbolOptionMixin {
    /** Position and style of every mark point. */
    var data: Array<MarkPointDataItemOption>?
}

/**
 * Mark line configuration.
 *
 * Upstream declares the symbol members here instead of inheriting `SymbolOptionMixin`, so `symbol`
 * also accepts a `[startSymbol, endSymbol]` pair and `symbolSize` / `symbolRotate` also accept one
 * value per end.
 */
external interface MarkLineOption : MarkOption, MarkLineStateOption {
    /** Symbol of both ends, or one per end. */
    var symbol: StringOrStringArray?

    /** Symbol size of both ends, or one per end. */
    var symbolSize: DoubleOrDoubleArray?

    /** Symbol rotation of both ends, or one per end. */
    var symbolRotate: DoubleOrDoubleArray?

    /** Symbol offset of both ends. */
    var symbolOffset: SymbolOffset?

    /**
     * Position of every mark line: a single definition `{ xAxis: 0 }` or a pair of them
     * `[{ coord: [0, 0] }, { coord: [1, 1] }]`.
     */
    var data: Array<MarkLineDataItem?>?
}

/**
 * Mark area configuration.
 *
 * Upstream has no symbol members on a mark area, so the label and the item style come from
 * [MarkAreaStateOption].
 */
external interface MarkAreaOption : MarkOption, MarkAreaStateOption {
    /**
     * Position of every mark area: a single definition `{ xAxis: 0 }` or a pair of them
     * `[{ coord: [0, 0] }, { coord: [1, 1] }]`.
     */
    var data: Array<MarkAreaDataItem?>?
}

/**
 * Animation configuration shared by series and components.
 */
external interface AnimationOption {
    /** Whether animation is enabled. */
    var animation: Boolean?

    /** Threshold of the number of elements above which animation is disabled. */
    var animationThreshold: Double?

    /** Duration of the initial animation in milliseconds. */
    var animationDuration: Double?

    /** Duration of the update animation in milliseconds. */
    var animationDurationUpdate: Double?

    /** Easing of the initial animation. */
    var animationEasing: AnimationEasing?

    /** Easing of the update animation. */
    var animationEasingUpdate: AnimationEasing?

    /** Delay of the initial animation in milliseconds. */
    var animationDelay: Double?

    /** Delay of the update animation in milliseconds. */
    var animationDelayUpdate: Double?
}

/**
 * Animation configuration of state transitions (hover, select, ...).
 */
external interface StateAnimationOption {
    /** Duration of the state transition in milliseconds. */
    var duration: Double?

    /** Easing of the state transition. */
    var easing: String?

    /** Delay of the state transition in milliseconds. */
    var delay: Double?
}
