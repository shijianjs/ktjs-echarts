package echarts

/**
 * Names of the events emitted by ECharts, for use with [EChartsInstance.on] and
 * [EChartsInstance.off].
 *
 * The list mirrors the "events" section of the ECharts documentation. Component events are also
 * described by their payload types, for example [EChartsInstance.onSelectChanged].
 */
object EChartsEvents {
    /** Mouse click on a chart element. */
    const val Click: String = "click"

    /** Mouse double click. */
    const val DoubleClick: String = "dblclick"

    /** Mouse button pressed down. */
    const val MouseDown: String = "mousedown"

    /** Mouse moved. */
    const val MouseMove: String = "mousemove"

    /** Mouse button released. */
    const val MouseUp: String = "mouseup"

    /** Mouse entered a chart element. */
    const val MouseOver: String = "mouseover"

    /** Mouse left a chart element. */
    const val MouseOut: String = "mouseout"

    /** Mouse left the chart canvas. */
    const val GlobalOut: String = "globalout"

    /** Context menu requested. */
    const val ContextMenu: String = "contextmenu"

    /** A series element is highlighted. */
    const val Highlight: String = "highlight"

    /** A series element is downplayed. */
    const val Downplay: String = "downplay"

    /** The selection changed, see [EChartsInstance.onSelectChanged]. */
    const val SelectChanged: String = "selectchanged"

    /** A legend item was selected. */
    const val LegendSelectChanged: String = "legendselectchanged"

    /** A legend item was selected. */
    const val LegendSelected: String = "legendselected"

    /** A legend item was unselected. */
    const val LegendUnselected: String = "legendunselected"

    /** All legend items were selected. */
    const val LegendSelectAll: String = "legendselectall"

    /** The legend selection was inverted. */
    const val LegendInverseSelect: String = "legendinverseselect"

    /** The legend was scrolled. */
    const val LegendScroll: String = "legendscroll"

    /** The data zoom window changed. */
    const val DataZoom: String = "datazoom"

    /** The data range of a data zoom component was selected. */
    const val DataRangeSelected: String = "datarangeselected"

    /** The timeline point changed. */
    const val TimelineChanged: String = "timelinechanged"

    /** The timeline playback state changed. */
    const val TimelinePlayChanged: String = "timelineplaychanged"

    /** The chart was restored to its initial option. */
    const val Restore: String = "restore"

    /** The data view was edited. */
    const val DataViewChanged: String = "dataviewchanged"

    /** The chart type was changed by the magic type feature. */
    const val MagicTypeChanged: String = "magictypechanged"

    /** A geo region was selected. */
    const val GeoSelectChanged: String = "geoselectchanged"

    /** A geo region was selected. */
    const val GeoSelected: String = "geoselected"

    /** A geo region was unselected. */
    const val GeoUnselected: String = "geounselected"

    /** The geo coordinate system was panned or zoomed. */
    const val GeoRoam: String = "georoam"

    /** The brush selection changed. */
    const val BrushSelected: String = "brushselected"

    /** A brush selection ended. */
    const val BrushEnd: String = "brushEnd"

    /** A brush area was created or cleared. */
    const val Brush: String = "brush"

    /** A frame was rendered. */
    const val Rendered: String = "rendered"

    /** The rendering finished. */
    const val Finished: String = "finished"

    /** An axis break was expanded or collapsed, see [EChartsInstance.onAxisBreakChanged]. */
    const val AxisBreakChanged: String = "axisbreakchanged"

    /** The axis pointer was triggered. */
    const val AxisTrigger: String = "axistrigger"

    /** A parallel axis area was selected. */
    const val AxisAreaSelected: String = "axisareaselected"

    /** The dimensions of a series changed. */
    const val DimensionNamesChanged: String = "dimensionnameschanged"
}

/**
 * Names of the actions that can be dispatched with [EChartsInstance.dispatchAction].
 */
object EChartsActions {
    /** Highlights a data item. */
    const val Highlight: String = "highlight"

    /** Downplays a data item. */
    const val Downplay: String = "downplay"

    /** Selects a data item. */
    const val Select: String = "select"

    /** Unselects a data item. */
    const val Unselect: String = "unselect"

    /** Toggles the selection of a data item. */
    const val ToggleSelected: String = "toggleSelect"

    /** Shows the tooltip of a data item. */
    const val ShowTip: String = "showTip"

    /** Hides the tooltip. */
    const val HideTip: String = "hideTip"

    /** Zooms or pans a data zoom component. */
    const val DataZoom: String = "dataZoom"

    /** Restores the chart to its initial option. */
    const val Restore: String = "restore"

    /** Changes the cursor style used by the brush component. */
    const val TakeGlobalCursor: String = "takeGlobalCursor"

    /** Updates the axis pointer. */
    const val UpdateAxisPointer: String = "updateAxisPointer"

    /** Applies a brush selection. */
    const val Brush: String = "brush"

    /** Selects geo regions. */
    const val GeoSelect: String = "geoSelect"

    /** Unselects geo regions. */
    const val GeoUnSelect: String = "geoUnSelect"

    /** Pans or zooms a geo component. */
    const val GeoRoam: String = "geoRoam"

    /** Expands or collapses a tree node. */
    const val TreeExpandAndCollapse: String = "treeExpandAndCollapse"

    /** Changes the current timeline point. */
    const val TimelineChange: String = "timelineChange"

    /** Changes the timeline playback state. */
    const val TimelinePlayChange: String = "timelinePlayChange"

    /** Changes the chart type of a series. */
    const val MagicTypeChange: String = "magicTypeChange"

    /** Expands an axis break. */
    const val ExpandAxisBreak: String = "expandAxisBreak"

    /** Collapses an axis break. */
    const val CollapseAxisBreak: String = "collapseAxisBreak"

    /** Toggles an axis break. */
    const val ToggleAxisBreak: String = "toggleAxisBreak"
}
