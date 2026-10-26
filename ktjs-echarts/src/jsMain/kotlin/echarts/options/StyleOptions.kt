package echarts.options

import kotlin.js.JsAny
import kotlin.js.unsafeCast

/**
 * Shadow settings shared by text and shape styles.
 */
external interface ShadowOption : JsAny {
    /** Shadow blur radius in pixels. */
    var shadowBlur: Double?

    /** Shadow color. */
    var shadowColor: JsAny?

    /** Horizontal shadow offset in pixels. */
    var shadowOffsetX: Double?

    /** Vertical shadow offset in pixels. */
    var shadowOffsetY: Double?
}

/**
 * Common text style, used by `textStyle`, axis names, tooltip text and labels.
 */
external interface TextStyleOption : ShadowOption {
    /** Text color. */
    var color: JsAny?

    /** Font style: `"normal"`, `"italic"` or `"oblique"`. */
    var fontStyle: String?

    /** Font weight: `"normal"`, `"bold"`, `"bolder"`, `"lighter"` or `100`..`900`. */
    var fontWeight: JsAny?

    /** Font family. */
    var fontFamily: String?

    /** Font size in pixels. */
    var fontSize: Double?

    /** Line height in pixels. */
    var lineHeight: Double?

    /** Horizontal alignment: `"left"`, `"center"` or `"right"`. */
    var align: String?

    /** Vertical alignment: `"top"`, `"middle"` or `"bottom"`. */
    var verticalAlign: String?

    /** Background color of the text block. */
    var backgroundColor: JsAny?

    /** Border color of the text block. */
    var borderColor: JsAny?

    /** Border width of the text block in pixels. */
    var borderWidth: Double?

    /** Border type: `"solid"`, `"dashed"` or `"dotted"`. */
    var borderType: String?

    /** Border radius of the text block. */
    var borderRadius: DoubleOrDoubleArray?

    /** Padding of the text block. */
    var padding: DoubleOrDoubleArray?

    /** Block width; only effective when [overflow] is set. */
    var width: Double?

    /** Block height. */
    var height: Double?

    /** Overflow behaviour: `"truncate"`, `"break"` or `"breakAll"`. */
    var overflow: String?

    /** Ellipsis shown when [overflow] is `"truncate"`. */
    var ellipsis: String?

    /** Text stroke color. */
    var textBorderColor: JsAny?

    /** Text stroke width. */
    var textBorderWidth: Double?

    /** Text stroke type: `"solid"`, `"dashed"` or `"dotted"`. */
    var textBorderType: String?

    /** Text stroke dash offset. */
    var textBorderDashOffset: Double?

    /** Text shadow blur radius. */
    var textShadowBlur: Double?

    /** Text shadow color. */
    var textShadowColor: JsAny?

    /** Text shadow horizontal offset. */
    var textShadowOffsetX: Double?

    /** Text shadow vertical offset. */
    var textShadowOffsetY: Double?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Rich text styles keyed by the tags used in [LabelOption.formatter]. */
    var rich: JsAny?
}

/**
 * Item style shared by shapes such as bars, symbols and map areas.
 */
external interface ItemStyleOption : ShadowOption {
    /** Fill color. */
    var color: JsAny?

    /** Border color. */
    var borderColor: JsAny?

    /** Border width in pixels. */
    var borderWidth: Double?

    /** Border type: `"solid"`, `"dashed"` or `"dotted"`. */
    var borderType: String?

    /** Dash offset of a dashed border. */
    var borderDashOffset: Double?

    /** Border radius. */
    var borderRadius: DoubleOrDoubleArray?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Decal pattern applied to the item. */
    var decal: JsAny?

    /** Area fill color, used by map and geo items. */
    var areaColor: JsAny?
}

/**
 * Line style shared by line series, axis lines and borders.
 */
external interface LineStyleOption : ShadowOption {
    /** Line color. */
    var color: JsAny?

    /** Line width in pixels. */
    var width: Double?

    /** Line type: `"solid"`, `"dashed"`, `"dotted"` or a `[dash, gap]` array. */
    var type: JsAny?

    /** Dash offset. */
    var dashOffset: Double?

    /** Line cap: `"butt"`, `"round"` or `"square"`. */
    var cap: String?

    /** Line join: `"bevel"`, `"round"` or `"miter"`. */
    var join: String?

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
    var color: JsAny?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Fill origin: `"auto"`, `"start"` or `"end"`. */
    var origin: String?

    /** Decal pattern applied to the area. */
    var decal: JsAny?
}

/**
 * A label line, used by pie, funnel and graph series.
 */
external interface LabelLineOption : JsAny {
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
    var position: JsAny?

    /** Distance to the element in pixels. */
    var distance: Double?

    /** Rotation in degrees, `-90..90`. */
    var rotate: Double?

    /** Offset from the computed position, `[x, y]`. */
    var offset: Array<Double>?

    /** Formatter: a template string such as `"{b}: {c}"` or a callback. */
    var formatter: LabelFormatter?

    /** Whether the label should stay visible during the initial animation. */
    var valueAnimation: Boolean?

    /** Minimum margin between the label and the container, in pixels. */
    var minMargin: Double?
}

/**
 * Label formatter: a template string or a callback.
 *
 * Create one with `LabelFormatter("{b}: {c}")` or `LabelFormatter { params -> ... }`.
 */
external interface LabelFormatter : JsAny

/** Wraps a template string such as `"{b}: {c}"`. */
fun LabelFormatter(template: String): LabelFormatter = template.unsafeCast<LabelFormatter>()

/** Wraps a callback that receives the item parameters and returns the label text. */
fun LabelFormatter(callback: (CallbackDataParams) -> String): LabelFormatter = callback.unsafeCast<LabelFormatter>()

/**
 * Parameters passed to most formatter callbacks.
 */
external interface CallbackDataParams : JsAny {
    /** Component type, e.g. `"series"`. */
    val componentType: String

    /** Series type, e.g. `"line"`. */
    val componentSubType: String

    /** Series index inside the option. */
    val seriesIndex: Int

    /** Data index inside the series. */
    val dataIndex: Int

    /** Data index inside the current data transform. */
    val dataIndexInside: Int

    /** Name of the data item. */
    val name: String

    /** Raw value of the data item. */
    val value: JsAny

    /** Color assigned to the item. */
    val color: JsAny

    /** Category (x) value, when the series is on an axis. */
    val axisValue: JsAny

    /** Label of the category (x) value. */
    val axisValueLabel: String

    /** Minimum value of the category axis, when applicable. */
    val axisType: String

    /** Series name. */
    val seriesName: String

    /** Data item object, when the item was given as an object. */
    val data: JsAny

    /** Percent value, used by pie, funnel and gauge series. */
    val percent: Double

    /** Whether the item is selected. */
    val selected: Boolean

    /** Marker markup for tooltips. */
    fun marker(): String
}

/**
 * Emphasis (hover) state of a series or element.
 */
external interface EmphasisOption : JsAny {
    /** Whether the emphasis state is disabled. */
    var disabled: Boolean?

    /** Focus mode: `"none"`, `"self"` or `"series"`. */
    var focus: String?

    /** Scope of [focus]: `"coordinateSystem"`, `"series"` or `"global"`. */
    var blurScope: String?

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
external interface BlurOption : JsAny {
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
external interface SelectOption : JsAny {
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
external interface MarkOption : JsAny {
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
    var symbol: String?

    /** Symbol size. */
    var symbolSize: DoubleOrDoubleArray?

    /** Symbol rotation in degrees. */
    var symbolRotate: Double?

    /** Whether to keep the symbol upright. */
    var symbolKeepAspect: Boolean?

    /** Symbol offset, `[x, y]`. */
    var symbolOffset: Array<JsAny>?

    /** Precision of displayed values. */
    var precision: Double?

    /** Data of the mark. */
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
    var animationEasing: String?
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
external interface AnimationOption : JsAny {
    /** Whether animation is enabled. */
    var animation: Boolean?

    /** Threshold of the number of elements above which animation is disabled. */
    var animationThreshold: Double?

    /** Duration of the initial animation in milliseconds. */
    var animationDuration: Double?

    /** Duration of the update animation in milliseconds. */
    var animationDurationUpdate: Double?

    /** Easing of the initial animation. */
    var animationEasing: String?

    /** Easing of the update animation. */
    var animationEasingUpdate: String?

    /** Delay of the initial animation in milliseconds. */
    var animationDelay: Double?

    /** Delay of the update animation in milliseconds. */
    var animationDelayUpdate: Double?
}

/**
 * Animation configuration of state transitions (hover, select, ...).
 */
external interface StateAnimationOption : JsAny {
    /** Duration of the state transition in milliseconds. */
    var duration: Double?

    /** Easing of the state transition. */
    var easing: String?

    /** Delay of the state transition in milliseconds. */
    var delay: Double?
}

/**
 * Decal pattern configuration used by `aria.decal.show` and item styles.
 */
external interface DecalOption : JsAny {
    /** Symbol of the pattern. */
    var symbol: String?

    /** Symbol size. */
    var symbolSize: Double?

    /** Dash spacing of the pattern. */
    var dashArrayX: DoubleOrDoubleArray?

    /** Dash spacing of the pattern. */
    var dashArrayY: DoubleOrDoubleArray?

    /** Rotation of the pattern in degrees. */
    var rotation: Double?

    /** Maximum tile width. */
    var maxTileWidth: Double?

    /** Maximum tile height. */
    var maxTileHeight: Double?

    /** Background color. */
    var backgroundColor: JsAny?

    /** Color of the pattern. */
    var color: JsAny?

    /** Background color of the pattern. */
    var decalColor: JsAny?

    /** Whether the pattern is dotted. */
    var dot: Boolean?
}
