package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.json.JsonObject

/**
 * Root of an ECharts option.
 *
 * Every property is optional and maps to the same-named key of the object passed to
 * `setOption`. Components that ECharts accepts as a single object or an array are modeled as
 * lists; a one-element list is equivalent to the single object.
 *
 * Serialize with [toJsonString] or [EChartsJson.encode].
 */
@Serializable
public class EChartsOption {
    /** Whether all series animations are enabled. */
    public var animation: Boolean? = null

    /** Element count above which animations are disabled. */
    public var animationThreshold: Double? = null

    /** Initial animation duration in milliseconds. */
    public var animationDuration: Double? = null

    /** Initial animation easing name. */
    public var animationEasing: String? = null

    /** Initial animation delay in milliseconds. */
    public var animationDelay: Double? = null

    /** Update animation duration in milliseconds. */
    public var animationDurationUpdate: Double? = null

    /** Update animation easing name. */
    public var animationEasingUpdate: String? = null

    /** Update animation delay in milliseconds. */
    public var animationDelayUpdate: Double? = null

    /** Chart background color. */
    public var backgroundColor: EChartsColor? = null

    /** Series color palette. */
    public var color: List<EChartsColor>? = null

    /** Default text style of every component. */
    public var textStyle: TextStyleOption? = null

    /** Dark mode: `true`, `false` or `"auto"`. */
    public var darkMode: BooleanOrString? = null

    /** Whether time values are interpreted as UTC. */
    public var useUTC: Boolean? = null

    /** Element count above which hover layers are not created. */
    public var hoverLayerThreshold: Double? = null

    /** Blend mode used when rendering the chart. */
    public var blendMode: String? = null

    /** Titles. */
    public var title: List<TitleOption>? = null

    /** Tooltips. */
    public var tooltip: List<TooltipOption>? = null

    /** Axis pointers shared by tooltips and axes. */
    public var axisPointer: AxisPointerOption? = null

    /** Legends. */
    public var legend: List<LegendOption>? = null

    /** Cartesian grids. */
    public var grid: List<GridOption>? = null

    /** Horizontal value axes. */
    public var xAxis: List<AxisOption>? = null

    /** Vertical value axes. */
    public var yAxis: List<AxisOption>? = null

    /** Polar coordinate systems. */
    public var polar: List<PolarOption>? = null

    /** Radius axes of polar coordinate systems. */
    public var radiusAxis: List<AxisOption>? = null

    /** Angle axes of polar coordinate systems. */
    public var angleAxis: List<AxisOption>? = null

    /** Radar coordinate systems. */
    public var radar: List<RadarOption>? = null

    /** Single axes. */
    public var singleAxis: List<AxisOption>? = null

    /** Parallel coordinate systems. */
    public var parallel: List<ParallelOption>? = null

    /** Axes of parallel coordinate systems. */
    public var parallelAxis: List<AxisOption>? = null

    /** Calendars. */
    public var calendar: List<CalendarOption>? = null

    /** Geographic coordinate systems. */
    public var geo: List<GeoOption>? = null

    /** Data zoom components. */
    public var dataZoom: List<DataZoomOption>? = null

    /** Visual map components. */
    public var visualMap: List<VisualMapOption>? = null

    /** Toolboxes. */
    public var toolbox: List<ToolboxOption>? = null

    /** Box selection settings. */
    public var brush: BrushOption? = null

    /** Datasets shared by series. */
    public var dataset: List<DatasetOption>? = null

    /** Timeline component. */
    public var timeline: TimelineOption? = null

    /** Free-floating graphic elements. */
    public var graphic: GraphicOption? = null

    /** Accessibility settings. */
    public var aria: AriaOption? = null

    /** Series. */
    public var series: List<SeriesOption>? = null

    /** Snapshots used with [timeline]. */
    public var options: List<EChartsOption>? = null

    /** Base option applied to every snapshot of a timeline. */
    public var baseOption: EChartsOption? = null

    /** Responsive overrides selected by media queries. */
    public var media: List<MediaOption>? = null

    /**
     * Extra top-level keys merged into the serialized object.
     *
     * This is the escape hatch for options not modeled yet; [toJsonElement] gives the same result
     * for nested keys.
     */
    @Transient
    public var extra: JsonObject? = null
}

/** One entry of [EChartsOption.media]. */
@Serializable
public class MediaOption {
    /** Media query, for example `{"maxWidth": 500}`. */
    public var query: JsonObject? = null

    /** Option applied when the query matches. */
    public var option: EChartsOption? = null
}
