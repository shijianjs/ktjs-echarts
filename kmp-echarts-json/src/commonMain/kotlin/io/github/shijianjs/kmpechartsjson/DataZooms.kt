package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Kind of a `dataZoom` component. */
@Serializable
public enum class DataZoomType {
    /** Zoom by mouse wheel or pinch inside the chart. */
    @SerialName("inside")
    Inside,

    /** Zoom with a draggable slider. */
    @SerialName("slider")
    Slider,

    /** Zoom by box selection. */
    @SerialName("select")
    Select,
}

/** Two-tone background drawn behind a data zoom slider. */
@Serializable
public class DataBackgroundOption {
    /** Line style of the preview series. */
    public var lineStyle: LineStyleOption? = null

    /** Area style of the preview series. */
    public var areaStyle: AreaStyleOption? = null
}

/**
 * `dataZoom` component.
 *
 * A data zoom selects a range on one or more axes. Which properties apply depends on [type];
 * `inside` ignores the slider-specific properties.
 */
@Serializable
public class DataZoomOption {
    /** Zoom component kind. */
    public var type: DataZoomType? = null

    /** Component id used by cross references. */
    public var id: String? = null

    /** Whether the component is shown. */
    public var show: Boolean? = null

    /** Background color of the slider. */
    public var backgroundColor: EChartsColor? = null

    /** Background of the preview series. */
    public var dataBackground: DataBackgroundOption? = null

    /** Background of the selected part of the preview series. */
    public var selectedDataBackground: DataBackgroundOption? = null

    /** Color of the selected area. */
    public var fillerColor: EChartsColor? = null

    /** Border color of the slider. */
    public var borderColor: EChartsColor? = null

    /** Icon of the two handles, as an image URL or a path. */
    public var handleIcon: String? = null

    /** Size of a handle, a number or `[width, height]`. */
    public var handleSize: NumberOrStringOrNumbers? = null

    /** Handle style. */
    public var handleStyle: ItemStyleOption? = null

    /** Icon of the move handle in the middle of the slider. */
    public var moveHandleIcon: String? = null

    /** Size of the move handle. */
    public var moveHandleSize: Double? = null

    /** Move handle style. */
    public var moveHandleStyle: ItemStyleOption? = null

    /** Number of decimals of the range label, a number or `"auto"`. */
    public var labelPrecision: NumberOrString? = null

    /** String template of the range label. */
    public var labelFormatter: String? = null

    /** Whether the range label is shown while dragging. */
    public var showDetail: Boolean? = null

    /** Whether the preview series is drawn as a shadow; a boolean or `"auto"`. */
    public var showDataShadow: BooleanOrString? = null

    /** Whether the chart updates while dragging. */
    public var realtime: Boolean? = null

    /** Whether zooming is locked. */
    public var zoomLock: Boolean? = null

    /** Text style of the range label. */
    public var textStyle: TextStyleOption? = null

    /** Whether box selection is enabled. */
    public var brushSelect: Boolean? = null

    /** Box selection style. */
    public var brushStyle: ItemStyleOption? = null

    /** Layout direction. */
    public var orient: Orient? = null

    /** Indices of the x axes controlled by this component. */
    public var xAxisIndex: List<Int>? = null

    /** Indices of the y axes controlled by this component. */
    public var yAxisIndex: List<Int>? = null

    /** Indices of the radius axes controlled by this component. */
    public var radiusAxisIndex: List<Int>? = null

    /** Indices of the angle axes controlled by this component. */
    public var angleAxisIndex: List<Int>? = null

    /** How other components are filtered: `"filter"`, `"weakFilter"`, `"empty"` or `"none"`. */
    public var filterMode: String? = null

    /** Start of the selected range, `0..100`. */
    public var start: Double? = null

    /** End of the selected range, `0..100`. */
    public var end: Double? = null

    /** Start value on the axis instead of [start] percentage. */
    public var startValue: NumberOrString? = null

    /** End value on the axis instead of [end] percentage. */
    public var endValue: NumberOrString? = null

    /** Minimum span kept when zooming in, in value units or percent. */
    public var minValueSpan: NumberOrString? = null

    /** Maximum span allowed when zooming out, in value units or percent. */
    public var maxValueSpan: NumberOrString? = null

    /** Throttle of the update rate, in milliseconds. */
    public var throttle: Double? = null

    /** Mouse wheel behavior: `true`, `false`, `"shift"`, `"ctrl"` or `"alt"`. */
    public var zoomOnMouseWheel: BooleanOrString? = null

    /** Mouse drag behavior: `true`, `false`, `"shift"`, `"ctrl"` or `"alt"`. */
    public var moveOnMouseMove: BooleanOrString? = null

    /** Mouse wheel translation behavior: `true`, `false`, `"shift"`, `"ctrl"` or `"alt"`. */
    public var moveOnMouseWheel: BooleanOrString? = null

    /** Whether the default mouse-move behavior is prevented. */
    public var preventDefaultMouseMove: Boolean? = null

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

    /** Slider width. */
    public var width: NumberOrString? = null

    /** Slider height. */
    public var height: NumberOrString? = null

    /** Emphasis state of the handles. */
    public var emphasis: EmphasisOption? = null
}
