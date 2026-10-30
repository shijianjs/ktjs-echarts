package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.Serializable

/**
 * Text style shared by labels, axis labels, titles, legends and tooltips.
 *
 * Every property maps to the same-named ECharts text style property; unset properties keep the
 * chart defaults.
 */
@Serializable
public open class TextStyleOption {
    /** Text color. */
    public var color: EChartsColor? = null

    /** `"normal"`, `"italic"` or `"oblique"`. */
    public var fontStyle: String? = null

    /** Font weight, either a number (`400`) or a keyword (`"bold"`). */
    public var fontWeight: NumberOrString? = null

    /** Font family list, for example `"sans-serif"`. */
    public var fontFamily: String? = null

    /** Font size in pixels. */
    public var fontSize: Double? = null

    /** Line height in pixels. */
    public var lineHeight: Double? = null

    /** Text block width. */
    public var width: NumberOrString? = null

    /** Text block height. */
    public var height: NumberOrString? = null

    /** Horizontal alignment: `"left"`, `"center"` or `"right"`. */
    public var align: String? = null

    /** Vertical alignment: `"top"`, `"middle"` or `"bottom"`. */
    public var verticalAlign: String? = null

    /** Background color behind the text. */
    public var backgroundColor: EChartsColor? = null

    /** Border color of the text box. */
    public var borderColor: EChartsColor? = null

    /** Border width of the text box. */
    public var borderWidth: Double? = null

    /** Border line type, for example `"solid"` or `"dashed"`. */
    public var borderType: String? = null

    /** Border dash offset. */
    public var borderDashOffset: Double? = null

    /** Border corner radius, a number or an array of radii. */
    public var borderRadius: NumberOrStringOrNumbers? = null

    /** Padding inside the text box, a number or `[top, right, bottom, left]`. */
    public var padding: NumberOrStringOrNumbers? = null

    /** Shadow blur radius. */
    public var shadowBlur: Double? = null

    /** Shadow color. */
    public var shadowColor: EChartsColor? = null

    /** Horizontal shadow offset. */
    public var shadowOffsetX: Double? = null

    /** Vertical shadow offset. */
    public var shadowOffsetY: Double? = null

    /** Text stroke color. */
    public var textBorderColor: EChartsColor? = null

    /** Text stroke width. */
    public var textBorderWidth: Double? = null

    /** Text stroke line type. */
    public var textBorderType: String? = null

    /** Text stroke dash offset. */
    public var textBorderDashOffset: Double? = null

    /** Text shadow blur radius. */
    public var textShadowBlur: Double? = null

    /** Text shadow color. */
    public var textShadowColor: EChartsColor? = null

    /** Horizontal text shadow offset. */
    public var textShadowOffsetX: Double? = null

    /** Vertical text shadow offset. */
    public var textShadowOffsetY: Double? = null

    /** How text longer than [width] is handled: `"truncate"`, `"break"` or `"breakAll"`. */
    public var overflow: String? = null

    /** Text appended when [overflow] is `"truncate"`. */
    public var ellipsis: String? = null

    /** Rich text styles keyed by the `{styleName|text}` names used in a formatter. */
    public var rich: Map<String, TextStyleOption>? = null
}

/** Axis, series and data item label. Extends every [TextStyleOption] property. */
@Serializable
public open class LabelOption : TextStyleOption() {
    /** Whether the label is shown. */
    public var show: Boolean? = null

    /** Label position: `"top"`, `"inside"`, `"center"`, `"outside"` and similar. */
    public var position: String? = null

    /** Distance from the element along [position]. */
    public var distance: Double? = null

    /** Label rotation in degrees. */
    public var rotate: Double? = null

    /** Label offset as `[x, y]`. */
    public var offset: List<NumberOrString>? = null

    /** String template or formatter keyword supported by the series. */
    public var formatter: String? = null

    /** Whether the formatted value animates between updates. */
    public var valueAnimation: Boolean? = null

    /** Alignment target for `"outside"` labels: `"labelLine"` or `"edge"`. */
    public var alignTo: String? = null

    /** Margin before the label edge when [alignTo] is `"edge"`. */
    public var bleedMargin: Double? = null
}

/** Style of the label connector line used by pie, funnel and similar series. */
@Serializable
public class LabelLineOption {
    /** Whether the line is shown. */
    public var show: Boolean? = null

    /** Whether the line is drawn above the shape. */
    public var showAbove: Boolean? = null

    /** Length of the first segment. */
    public var length: Double? = null

    /** Length of the second segment. */
    public var length2: NumberOrString? = null

    /** Smoothness of the polyline, `0..1`. */
    public var smooth: NumberOrStringOrNumbers? = null

    /** Minimum turning angle in degrees. */
    public var minTurnAngle: Double? = null

    /** Line style. */
    public var lineStyle: LineStyleOption? = null
}

/** Fill style of a symbol, bar, pie slice or area. */
@Serializable
public open class ItemStyleOption {
    /** Fill color. */
    public var color: EChartsColor? = null

    /** Border color. */
    public var borderColor: EChartsColor? = null

    /** Border width. */
    public var borderWidth: Double? = null

    /** Border line type. */
    public var borderType: String? = null

    /** Border dash offset. */
    public var borderDashOffset: Double? = null

    /** Border radius, a number or an array of radii. */
    public var borderRadius: NumberOrStringOrNumbers? = null

    /** Fill opacity, `0..1`. */
    public var opacity: Double? = null

    /** Shadow blur radius. */
    public var shadowBlur: Double? = null

    /** Shadow color. */
    public var shadowColor: EChartsColor? = null

    /** Horizontal shadow offset. */
    public var shadowOffsetX: Double? = null

    /** Vertical shadow offset. */
    public var shadowOffsetY: Double? = null

    /** Decal pattern, used by accessibility and by `aria.decal`. */
    public var decal: DecalOption? = null
}

/** Stroke style of a line, axis or edge. */
@Serializable
public open class LineStyleOption {
    /** Stroke color. */
    public var color: EChartsColor? = null

    /** Stroke width. */
    public var width: Double? = null

    /** Stroke opacity, `0..1`. */
    public var opacity: Double? = null

    /** Line type: `"solid"`, `"dashed"`, `"dotted"` or a dash array string. */
    public var type: String? = null

    /** Line cap style. */
    public var cap: String? = null

    /** Line join style. */
    public var join: String? = null

    /** Dash offset. */
    public var dashOffset: Double? = null

    /** Miter limit for `"miter"` joins. */
    public var miterLimit: Double? = null

    /** Shadow blur radius. */
    public var shadowBlur: Double? = null

    /** Shadow color. */
    public var shadowColor: EChartsColor? = null

    /** Horizontal shadow offset. */
    public var shadowOffsetX: Double? = null

    /** Vertical shadow offset. */
    public var shadowOffsetY: Double? = null
}

/** Fill style of the area below a line series. */
@Serializable
public class AreaStyleOption {
    /** Fill color. */
    public var color: EChartsColor? = null

    /** Fill opacity, `0..1`. */
    public var opacity: Double? = null

    /** Fill origin: `"auto"`, `"start"`, `"end"` or a number. */
    public var origin: NumberOrString? = null

    /** Shadow blur radius. */
    public var shadowBlur: Double? = null

    /** Shadow color. */
    public var shadowColor: EChartsColor? = null

    /** Horizontal shadow offset. */
    public var shadowOffsetX: Double? = null

    /** Vertical shadow offset. */
    public var shadowOffsetY: Double? = null
}

/** The highlighted state of an element. */
@Serializable
public class EmphasisOption {
    /** Disables the highlighted state when `true`. */
    public var disabled: Boolean? = null

    /** Scope of the highlight: `"none"`, `"self"` or `"series"`. */
    public var focus: String? = null

    /** Scope of the blur applied to the rest: `"coordinateSystem"`, `"series"` or `"global"`. */
    public var blurScope: String? = null

    /** Whether the symbol scales up; a boolean or a scale factor. */
    public var scale: BooleanOrNumber? = null

    /** Item style in the highlighted state. */
    public var itemStyle: ItemStyleOption? = null

    /** Label in the highlighted state. */
    public var label: LabelOption? = null

    /** Line style in the highlighted state. */
    public var lineStyle: LineStyleOption? = null

    /** Area style in the highlighted state. */
    public var areaStyle: AreaStyleOption? = null

    /** End label in the highlighted state. */
    public var endLabel: LabelOption? = null
}

/** `markPoint` marker. */
@Serializable
public class MarkPointOption {
    /** Symbol name. */
    public var symbol: String? = null

    /** Symbol size, a number or `[width, height]`. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Symbol rotation in degrees. */
    public var symbolRotate: Double? = null

    /** Symbol offset as `[x, y]`. */
    public var symbolOffset: List<NumberOrString>? = null

    /** Whether the marker ignores mouse events. */
    public var silent: Boolean? = null

    /** Marker label. */
    public var label: LabelOption? = null

    /** Marker style. */
    public var itemStyle: ItemStyleOption? = null

    /** Marker emphasis state. */
    public var emphasis: EmphasisOption? = null

    /** Markers to draw. */
    public var data: List<MarkPointData>? = null
}

/** One `markPoint` entry. */
@Serializable
public class MarkPointData {
    /** Display name. */
    public var name: String? = null

    /** Built-in type: `"min"`, `"max"`, `"average"`. */
    public var type: String? = null

    /** Explicit value. */
    public var value: DataValue? = null

    /** Coordinate on the series' coordinate system. */
    public var coord: List<NumberOrString>? = null

    /** X position in pixels or as a percentage. */
    public var x: NumberOrString? = null

    /** Y position in pixels or as a percentage. */
    public var y: NumberOrString? = null

    /** Symbol name for this marker. */
    public var symbol: String? = null

    /** Symbol size for this marker. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Symbol rotation for this marker. */
    public var symbolRotate: Double? = null

    /** Symbol offset for this marker as `[x, y]`. */
    public var symbolOffset: List<NumberOrString>? = null

    /** Style for this marker only. */
    public var itemStyle: ItemStyleOption? = null

    /** Label for this marker only. */
    public var label: LabelOption? = null
}

/** `markLine` marker. */
@Serializable
public class MarkLineOption {
    /** Whether the marker ignores mouse events. */
    public var silent: Boolean? = null

    /** Symbol at both ends, as `[start, end]`; `"none"` hides an end. */
    public var symbol: List<String>? = null

    /** Symbol size, a number or `[width, height]`. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Precision of the displayed value. */
    public var precision: Int? = null

    /** Marker label. */
    public var label: LabelOption? = null

    /** Marker line style. */
    public var lineStyle: LineStyleOption? = null

    /** Marker emphasis state. */
    public var emphasis: EmphasisOption? = null

    /** Lines to draw. */
    public var data: List<MarkLineData>? = null
}

/** One `markLine` entry. */
@Serializable
public class MarkLineData {
    /** Display name. */
    public var name: String? = null

    /** Built-in type: `"min"`, `"max"`, `"average"`. */
    public var type: String? = null

    /** X value of the line when it is vertical. */
    public var xAxis: NumberOrString? = null

    /** Y value of the line when it is horizontal. */
    public var yAxis: NumberOrString? = null

    /** Two coordinates defining the line. */
    public var coord: List<List<NumberOrString>>? = null

    /** Index of the data dimension the value refers to. */
    public var valueIndex: Int? = null

    /** Name of the data dimension the value refers to. */
    public var valueDim: String? = null

    /** Line style for this entry only. */
    public var lineStyle: LineStyleOption? = null

    /** Label for this entry only. */
    public var label: LabelOption? = null
}

/** `markArea` marker. */
@Serializable
public class MarkAreaOption {
    /** Whether the marker ignores mouse events. */
    public var silent: Boolean? = null

    /** Area style. */
    public var itemStyle: ItemStyleOption? = null

    /** Area label. */
    public var label: LabelOption? = null

    /** Area emphasis state. */
    public var emphasis: EmphasisOption? = null

    /** Areas to draw. */
    public var data: List<MarkAreaData>? = null
}

/** One `markArea` entry; an area is defined by two corners. */
@Serializable
public class MarkAreaData {
    /** Display name. */
    public var name: String? = null

    /** Built-in type: `"min"`, `"max"`, `"average"`. */
    public var type: String? = null

    /** X value of one corner. */
    public var xAxis: NumberOrString? = null

    /** Y value of one corner. */
    public var yAxis: NumberOrString? = null

    /** Two corners as `[[x1, y1], [x2, y2]]`. */
    public var coord: List<List<NumberOrString>>? = null

    /** Index of the data dimension the value refers to. */
    public var valueIndex: Int? = null

    /** Name of the data dimension the value refers to. */
    public var valueDim: String? = null

    /** Style for this area only. */
    public var itemStyle: ItemStyleOption? = null

    /** Label for this area only. */
    public var label: LabelOption? = null
}

/** Ripple animation of `effectScatter` and `lines`. */
@Serializable
public class RippleEffectOption {
    /** Animation period in seconds. */
    public var period: Double? = null

    /** Maximum ripple scale. */
    public var scale: Double? = null

    /** Ripple shape: `"stroke"` or `"fill"`. */
    public var brushType: String? = null

    /** Number of ripples to render at once; `"all"` is not representable as JSON, use `-1`. */
    public var number: Double? = null

    /** Ripple color. */
    public var color: EChartsColor? = null
}

/** Layout parameters of `series.type = "graph"` with `layout = "force"`. */
@Serializable
public class ForceOption {
    /** Initial layout: `"random"` or `"circular"`. */
    public var initLayout: String? = null

    /** Node repulsion force, a number or `[min, max]`. */
    public var repulsion: NumberOrStringOrNumbers? = null

    /** Gravity toward the center. */
    public var gravity: Double? = null

    /** Edge length, a number or `[min, max]`. */
    public var edgeLength: NumberOrStringOrNumbers? = null

    /** Whether the layout animates while it settles. */
    public var layoutAnimation: Boolean? = null

    /** Friction coefficient. */
    public var friction: Double? = null
}

/** Minimum and maximum zoom of a `roam`-enabled component. */
@Serializable
public class ScaleLimit {
    /** Minimum zoom factor. */
    public var min: Double? = null

    /** Maximum zoom factor. */
    public var max: Double? = null
}

/** Decal pattern used by `aria.decal` and `itemStyle.decal`. */
@Serializable
public class DecalOption {
    /** Decal symbol: `"circle"`, `"rect"`, `"triangle"` and similar. */
    public var symbol: String? = null

    /** Symbol size. */
    public var symbolSize: Double? = null

    /** Whether the symbol keeps its aspect ratio. */
    public var symbolKeepAspect: Boolean? = null

    /** Symbol color. */
    public var color: EChartsColor? = null

    /** Background color behind the symbols. */
    public var backgroundColor: EChartsColor? = null

    /** Horizontal dash pattern: a number or an array. */
    public var dashArrayX: NumberOrStringOrNumbers? = null

    /** Vertical dash pattern: a number or an array. */
    public var dashArrayY: NumberOrStringOrNumbers? = null

    /** Rotation of the decal in radians. */
    public var rotation: Double? = null

    /** Maximum tile width. */
    public var maxTileWidth: Double? = null

    /** Maximum tile height. */
    public var maxTileHeight: Double? = null

    /** Shrinks the decal to dots when `true`. */
    public var dot: Boolean? = null
}
