package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/** What triggers the tooltip. */
@Serializable
public enum class TooltipTrigger {
    /** Show the tooltip for the hovered item. */
    @SerialName("item")
    Item,

    /** Show the tooltip for the whole axis at the hovered position. */
    @SerialName("axis")
    Axis,

    /** Do not show a tooltip. */
    @SerialName("none")
    None,
}

/** How the tooltip is rendered. */
@Serializable
public enum class TooltipRenderMode {
    /** Render as an HTML element. */
    @SerialName("html")
    Html,

    /** Render on the chart canvas as rich text. */
    @SerialName("richText")
    RichText,
}

/**
 * Tooltip configuration.
 *
 * Callback-valued properties (`formatter`, `position`, `valueFormatter`) cannot be represented in
 * a JSON payload; the string forms are modeled here and everything else is applied after parsing.
 */
@Serializable
public class TooltipOption {
    /** Whether the tooltip is shown. */
    public var show: Boolean? = null

    /** What triggers the tooltip. */
    public var trigger: TooltipTrigger? = null

    /** Event that triggers the tooltip: `"mousemove"`, `"click"` or `"mousemove|click"`. */
    public var triggerOn: String? = null

    /** Whether the tooltip stays visible after the pointer leaves. */
    public var alwaysShowContent: Boolean? = null

    /** Delay before showing, in milliseconds. */
    public var showDelay: Double? = null

    /** Delay before hiding, in milliseconds. */
    public var hideDelay: Double? = null

    /** Whether the pointer can enter the tooltip. */
    public var enterable: Boolean? = null

    /** Render mode. */
    public var renderMode: TooltipRenderMode? = null

    /** Whether the tooltip is confined to the chart area. */
    public var confine: Boolean? = null

    /** Whether the HTML tooltip is appended to the document body instead of the chart container. */
    public var appendToBody: Boolean? = null

    /** Extra class name for the HTML tooltip. */
    public var className: String? = null

    /** Transition duration in milliseconds. */
    public var transitionDuration: Double? = null

    /**
     * Tooltip position: a keyword (``"inside"``), a point or a callback. Only the JSON forms are
     * modeled; a callback must be applied after parsing.
     */
    public var position: JsonElement? = null

    /** String template such as `"{b}: {c}"`. */
    public var formatter: String? = null

    /** Background color. */
    public var backgroundColor: EChartsColor? = null

    /** Border color. */
    public var borderColor: EChartsColor? = null

    /** Border width. */
    public var borderWidth: Double? = null

    /** Padding, a number or `[top, right, bottom, left]`. */
    public var padding: NumberOrStringOrNumbers? = null

    /** Text style. */
    public var textStyle: TextStyleOption? = null

    /** Extra CSS applied to the HTML tooltip. */
    public var extraCssText: String? = null

    /** Tooltip order when several series contribute to one tooltip. */
    public var order: Int? = null

    /** Axis pointer shown while the tooltip is visible. */
    public var axisPointer: AxisPointerOption? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Shadow blur radius. */
    public var shadowBlur: Double? = null

    /** Shadow color. */
    public var shadowColor: EChartsColor? = null

    /** Horizontal shadow offset. */
    public var shadowOffsetX: Double? = null

    /** Vertical shadow offset. */
    public var shadowOffsetY: Double? = null
}
