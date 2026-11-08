package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/** A calendar coordinate system used by `series.type = "heatmap"` and scatter. */
@Serializable
public class CalendarOption {
    /** Component id used by `calendarIndex` cross references. */
    public var id: String? = null

    /** Visible range: `"2017"`, `"2017-01"` or `["2017-01-01", "2017-12-31"]`. */
    public var range: JsonElement? = null

    /** Layout direction of the calendar cells. */
    public var orient: Orient? = null

    /** Size of one day cell, a number or `[width, height]`. */
    public var cellSize: NumberOrStringOrNumbers? = null

    /** Width of the whole calendar. */
    public var width: NumberOrString? = null

    /** Height of the whole calendar. */
    public var height: NumberOrString? = null

    /** Distance to the top edge. */
    public var top: NumberOrString? = null

    /** Distance to the left edge. */
    public var left: NumberOrString? = null

    /** Distance to the right edge. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge. */
    public var bottom: NumberOrString? = null

    /** Day label. */
    public var dayLabel: CalendarLabelOption? = null

    /** Month label. */
    public var monthLabel: CalendarLabelOption? = null

    /** Year label. */
    public var yearLabel: CalendarLabelOption? = null

    /** Cell style. */
    public var itemStyle: ItemStyleOption? = null

    /** Separator lines. */
    public var splitLine: CalendarSplitLineOption? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Whether the calendar ignores mouse events. */
    public var silent: Boolean? = null
}

/** Day, month or year label of a [CalendarOption]. */
@Serializable
public class CalendarLabelOption : TextStyleOption() {
    /** Whether the label is shown. */
    public var show: Boolean? = null

    /** Position of the label: `"start"`, `"end"` or `"middle"`. */
    public var position: String? = null

    /** Gap between the label and the calendar. */
    public var margin: Double? = null
}

/** Separator lines of a [CalendarOption]. */
@Serializable
public class CalendarSplitLineOption {
    /** Whether the lines are shown. */
    public var show: Boolean? = null

    /** Line style. */
    public var lineStyle: LineStyleOption? = null
}
