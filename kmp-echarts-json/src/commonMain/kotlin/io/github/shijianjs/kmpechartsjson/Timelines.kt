package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.Serializable

/**
 * `timeline` component, which switches the chart between snapshots.
 *
 * The snapshots themselves are set through [EChartsOption.baseOption] and
 * [EChartsOption.options].
 */
@Serializable
public class TimelineOption {
    /** Component id used by cross references. */
    public var id: String? = null

    /** Whether the timeline is shown. */
    public var show: Boolean? = null

    /** Timeline appearance: `"slider"` or `"scroll"`. */
    public var type: String? = null

    /** Axis kind of the timeline. */
    public var axisType: AxisType? = null

    /** Index of the initially selected snapshot. */
    public var currentIndex: Int? = null

    /** Whether snapshots play automatically. */
    public var autoPlay: Boolean? = null

    /** Whether playback restarts from the first snapshot. */
    public var rewind: Boolean? = null

    /** Whether playback loops. */
    public var loop: Boolean? = null

    /** Interval between snapshots during playback, in milliseconds. */
    public var playbackInterval: Double? = null

    /** Whether the chart updates while dragging. */
    public var realtime: Boolean? = null

    /** Position of the control buttons: `"left"` or `"right"`. */
    public var controlPosition: String? = null

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

    /** Width of the timeline. */
    public var width: NumberOrString? = null

    /** Height of the timeline. */
    public var height: NumberOrString? = null

    /** Padding, a number or `[top, right, bottom, left]`. */
    public var padding: NumberOrStringOrNumbers? = null

    /** Layout direction. */
    public var orient: Orient? = null

    /** Whether the axis is reversed. */
    public var inverse: Boolean? = null

    /** Symbol of a snapshot, for example `"circle"` or `"emptyCircle"`. */
    public var symbol: String? = null

    /** Symbol size. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Symbol rotation in degrees. */
    public var symbolRotate: Double? = null

    /** Whether the symbol keeps its aspect ratio. */
    public var symbolKeepAspect: Boolean? = null

    /** Symbol offset as `[x, y]`. */
    public var symbolOffset: List<NumberOrString>? = null

    /** Line style of the timeline axis. */
    public var lineStyle: LineStyleOption? = null

    /** Snapshot label. */
    public var label: LabelOption? = null

    /** Snapshot item style. */
    public var itemStyle: ItemStyleOption? = null

    /** Style of the current snapshot. */
    public var checkpointStyle: TimelineCheckpointStyle? = null

    /** Style of the control buttons. */
    public var controlStyle: TimelineControlStyle? = null

    /** Style of the progress part of the timeline. */
    public var progress: TimelineProgress? = null

    /** Snapshot labels or `{ value, tooltip }` objects. */
    public var data: List<DataValue>? = null

    /** Whether snapshot changes animate. */
    public var animation: Boolean? = null

    /** Animation duration in milliseconds. */
    public var animationDuration: Double? = null

    /** Animation easing name. */
    public var animationEasing: String? = null

    /** Update animation duration in milliseconds. */
    public var animationDurationUpdate: Double? = null

    /** Update animation easing name. */
    public var animationEasingUpdate: String? = null
}

/** Style of the current snapshot marker of a [TimelineOption]. */
@Serializable
public class TimelineCheckpointStyle {
    /** Symbol name. */
    public var symbol: String? = null

    /** Symbol size. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Marker color. */
    public var color: EChartsColor? = null

    /** Marker border color. */
    public var borderColor: EChartsColor? = null

    /** Marker border width. */
    public var borderWidth: Double? = null

    /** Whether the marker animates when the snapshot changes. */
    public var animation: Boolean? = null

    /** Animation duration in milliseconds. */
    public var animationDuration: Double? = null

    /** Animation easing name. */
    public var animationEasing: String? = null

    /** Marker label. */
    public var label: LabelOption? = null
}

/** Style of the play/pause and previous/next buttons of a [TimelineOption]. */
@Serializable
public class TimelineControlStyle {
    /** Whether the control buttons are shown. */
    public var show: Boolean? = null

    /** Whether the play button is shown. */
    public var showPlayBtn: Boolean? = null

    /** Whether the previous button is shown. */
    public var showPrevBtn: Boolean? = null

    /** Whether the next button is shown. */
    public var showNextBtn: Boolean? = null

    /** Size of a button. */
    public var itemSize: Double? = null

    /** Gap between buttons. */
    public var itemGap: Double? = null

    /** Button position: `"left"` or `"right"`. */
    public var position: String? = null

    /** Button color. */
    public var color: EChartsColor? = null

    /** Button border color. */
    public var borderColor: EChartsColor? = null

    /** Button border width. */
    public var borderWidth: Double? = null

    /** Button corner radius. */
    public var borderRadius: NumberOrStringOrNumbers? = null
}

/** Style of the elapsed part of a [TimelineOption]. */
@Serializable
public class TimelineProgress {
    /** Line style of the progress bar. */
    public var lineStyle: LineStyleOption? = null

    /** Item style of the progress bar. */
    public var itemStyle: ItemStyleOption? = null

    /** Progress label. */
    public var label: LabelOption? = null
}
