package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.Serializable

/** Chart title, including an optional subtitle. */
@Serializable
public class TitleOption {
    /** Component id used by cross references. */
    public var id: String? = null

    /** Whether the title is shown. */
    public var show: Boolean? = null

    /** Main title text. */
    public var text: String? = null

    /** Subtitle text. */
    public var subtext: String? = null

    /** URL opened when the title is clicked. */
    public var link: String? = null

    /** URL opened when the subtitle is clicked. */
    public var sublink: String? = null

    /** Link target: `"self"` or `"blank"`. */
    public var target: String? = null

    /** Horizontal alignment of the whole title block. */
    public var textAlign: String? = null

    /** Vertical alignment of the whole title block. */
    public var textVerticalAlign: String? = null

    /** Whether clicking the title triggers a `click` event. */
    public var triggerEvent: Boolean? = null

    /** Padding around the title block, a number or `[top, right, bottom, left]`. */
    public var padding: NumberOrStringOrNumbers? = null

    /** Gap between the title and the subtitle. */
    public var itemGap: Double? = null

    /** Main title text style. */
    public var textStyle: TextStyleOption? = null

    /** Subtitle text style. */
    public var subtextStyle: TextStyleOption? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Distance to the left edge. */
    public var left: NumberOrString? = null

    /** Distance to the top edge. */
    public var top: NumberOrString? = null

    /** Distance to the right edge. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge. */
    public var bottom: NumberOrString? = null

    /** Block width. */
    public var width: NumberOrString? = null

    /** Block height. */
    public var height: NumberOrString? = null

    /** Background color. */
    public var backgroundColor: EChartsColor? = null

    /** Border color. */
    public var borderColor: EChartsColor? = null

    /** Border width. */
    public var borderWidth: Double? = null

    /** Border radius, a number or an array of radii. */
    public var borderRadius: NumberOrStringOrNumbers? = null

    /** Shadow blur radius. */
    public var shadowBlur: Double? = null

    /** Shadow color. */
    public var shadowColor: EChartsColor? = null

    /** Horizontal shadow offset. */
    public var shadowOffsetX: Double? = null

    /** Vertical shadow offset. */
    public var shadowOffsetY: Double? = null
}
