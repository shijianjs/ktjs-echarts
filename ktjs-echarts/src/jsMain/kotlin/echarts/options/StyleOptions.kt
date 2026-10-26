package echarts.options

import echarts.AnimationEasing
import echarts.BlurScope
import echarts.EmphasisFocus
import js.objects.Record
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.canvas.CanvasLineCap
import web.canvas.CanvasLineJoin
import web.cssom.FontStyle
import web.cssom.TextAlign
import web.cssom.VerticalAlign
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
}

/**
 * Mark point, line and area styling.
 */
external interface MarkOption {
    /** Whether the mark is silent (ignores mouse events). */
    var silent: Boolean?

    /** Label style. */
    var label: LabelOption?

    /** Item style. */
    var itemStyle: ItemStyleOption?

    /** Line style. */
    var lineStyle: LineStyleOption?

    /** Whether to show the marker. */
    var show: Boolean?

    /** Symbol used by points and line ends. */
    var symbol: StringOrStringArray?

    /** Symbol size. */
    var symbolSize: DoubleOrDoubleArray?

    /** Symbol rotation in degrees. */
    var symbolRotate: DoubleOrDoubleArray?

    /** Whether to keep the symbol upright. */
    var symbolKeepAspect: Boolean?

    /** Symbol offset. */
    var symbolOffset: SymbolOffset?

    /** Precision of displayed values. */
    var precision: Double?

    /** Data of the mark. Upstream types this as `unknown[]`. */
    var data: Array<JsAny>?
}

/**
 * Mark point configuration; adds the point-specific display options.
 */
external interface MarkPointOption : MarkOption {
    /** Animation used when the mark point appears. */
    var animation: Boolean?

    /** Animation duration in milliseconds. */
    var animationDuration: Double?

    /** Animation easing. */
    var animationEasing: AnimationEasing?
}

/**
 * Mark line configuration.
 */
external interface MarkLineOption : MarkOption {
    /** Whether the mark line is silent. */
    override var silent: Boolean?

    /** Precision of displayed values. */
    override var precision: Double?

    /** Whether to display the two end labels. */
    override var label: LabelOption?
}

/**
 * Mark area configuration.
 */
external interface MarkAreaOption : MarkOption {
    /** Whether the mark area is silent. */
    override var silent: Boolean?
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
