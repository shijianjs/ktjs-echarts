package echarts.options

import kotlin.js.JsAny

/**
 * The root ECharts option object accepted by `setOption`.
 *
 * Top-level fields configure components and global defaults; [series] holds the data series.
 * Every component field is an array because ECharts supports multiple instances of the same
 * component type; use [one] for the common single-instance case.
 *
 * This model covers the documented public options. For a fragment that is not modelled, pass a
 * JavaScript object through `setOption` with a `JsAny` value, or build extra fields with
 * [echartsOption] and assign them to the typed object.
 */
external interface EChartsOption : JsAny {
    /** Title components. */
    var title: Array<TitleOption>?

    /** Legend components. */
    var legend: Array<LegendOption>?

    /** Tooltip component. */
    var tooltip: TooltipOption?

    /** Grid components (cartesian coordinate systems). */
    var grid: Array<GridOption>?

    /** X axes. */
    var xAxis: Array<XAxisOption>?

    /** Y axes. */
    var yAxis: Array<YAxisOption>?

    /** Dataset components. */
    var dataset: Array<DatasetOption>?

    /** Data zoom components. */
    var dataZoom: Array<DataZoomOption>?

    /** Toolbox component. */
    var toolbox: ToolboxOption?

    /** Visual map components. */
    var visualMap: Array<VisualMapOption>?

    /** Geo components. */
    var geo: Array<GeoOption>?

    /** Polar coordinate systems. */
    var polar: Array<PolarOption>?

    /** Radar coordinate systems. */
    var radar: Array<RadarOption>?

    /** Angle axes of polar coordinate systems. */
    var angleAxis: Array<AngleAxisOption>?

    /** Radius axes of polar coordinate systems. */
    var radiusAxis: Array<RadiusAxisOption>?

    /** Single axes. */
    var singleAxis: Array<SingleAxisOption>?

    /** Parallel coordinate systems. */
    var parallel: Array<ParallelOption>?

    /** Axes of parallel coordinate systems. */
    var parallelAxis: Array<ParallelAxisOption>?

    /** Calendar coordinate systems. */
    var calendar: Array<CalendarOption>?

    /** Timeline component. */
    var timeline: TimelineOption?

    /** Graphic elements drawn without a coordinate system. */
    var graphic: Array<GraphicElement>?

    /** Accessibility settings. */
    var aria: AriaOption?

    /** Brush component. */
    var brush: BrushOption?

    /** Global axis pointer configuration. */
    var axisPointer: AxisPointerOption?

    /** Matrix coordinate systems. */
    var matrix: Array<MatrixOption>?

    /** Data series. */
    var series: Array<SeriesOption>?

    /** Color palette. */
    var color: Array<JsAny>?

    /** Background color of the whole chart. */
    var backgroundColor: JsAny?

    /** Global text style of the chart. */
    var textStyle: TextStyleOption?

    /** Whether animation is enabled globally. */
    var animation: Boolean?

    /** Number of elements above which animation is disabled. */
    var animationThreshold: Double?

    /** Duration of the initial animation in milliseconds. */
    var animationDuration: Double?

    /** Easing of the initial animation. */
    var animationEasing: String?

    /** Delay of the initial animation in milliseconds. */
    var animationDelay: Double?

    /** Duration of the update animation in milliseconds. */
    var animationDurationUpdate: Double?

    /** Easing of the update animation. */
    var animationEasingUpdate: String?

    /** Delay of the update animation in milliseconds. */
    var animationDelayUpdate: Double?

    /** Animation of state transitions (hover, select, ...). */
    var stateAnimation: StateAnimationOption?

    /** Whether to treat time values as UTC. */
    var useUTC: Boolean?

    /** Whether to use the dark theme defaults. */
    var darkMode: BooleanOrString?

    /** Number of elements above which a separate hover layer is used. */
    var hoverLayerThreshold: Double?

    /** Blend mode used by the renderer. */
    var blendMode: String?

    /** Progressive rendering threshold. */
    var progressiveThreshold: Double?

    /** Number of elements rendered per frame during progressive rendering. */
    var progressive: Double?
}

/**
 * Dataset component: provides data shared by series.
 */
external interface DatasetOption : JsAny {
    /** Component id, used by `series.datasetId`. */
    var id: String?

    /** Index of this dataset, assigned by ECharts. */
    var index: Double?

    /** Raw data: an array of rows, or an object for keyed data. */
    var source: JsAny?

    /** Dimensions of the dataset. */
    var dimensions: Array<JsAny>?

    /** Whether the first row of [source] contains dimension names. */
    var sourceHeader: JsAny?

    /** Data transform pipeline, e.g. `{ type: "filter", config: { ... } }`. */
    var transform: JsAny?

    /** Index of the source dataset when this dataset is derived from another one. */
    var fromDatasetIndex: Double?

    /** Id of the source dataset when this dataset is derived from another one. */
    var fromDatasetId: String?

    /** Index of the transform result used as the source. */
    var fromTransformResult: Double?
}

/**
 * Data zoom component; covers both the slider and the inside (mouse wheel) variants.
 *
 * Set [type] to `"slider"` or `"inside"`.
 */
external interface DataZoomOption : JsAny {
    /** Component id. */
    var id: String?

    /** Whether the component is shown. */
    var show: Boolean?

    /** Zoom type: `"slider"` or `"inside"`. */
    var type: String?

    /** Background color. */
    var backgroundColor: JsAny?

    /** Border color. */
    var borderColor: JsAny?

    /** Fill color of the selected range. */
    var fillerColor: JsAny?

    /** Data background of the slider. */
    var dataBackground: JsAny?

    /** Selected data background of the slider. */
    var selectedDataBackground: JsAny?

    /** Icon of the two handles. */
    var handleIcon: String?

    /** Size of the handles. */
    var handleSize: DoubleOrDoubleArray?

    /** Style of the handles. */
    var handleStyle: ItemStyleOption?

    /** Style of the move handle. */
    var moveHandleStyle: ItemStyleOption?

    /** Size of the move handle. */
    var moveHandleSize: Double?

    /** Style of the text labels. */
    var textStyle: TextStyleOption?

    /** Formatter of the range labels. */
    var labelFormatter: LabelFormatter?

    /** Indices of the x axes controlled by this component. */
    var xAxisIndex: JsAny?

    /** Indices of the y axes controlled by this component. */
    var yAxisIndex: JsAny?

    /** Indices of the radius axes controlled by this component. */
    var radiusAxisIndex: JsAny?

    /** Indices of the angle axes controlled by this component. */
    var angleAxisIndex: JsAny?

    /** Indices of the single axes controlled by this component. */
    var singleAxisIndex: JsAny?

    /** Filter mode: `"filter"`, `"weakFilter"`, `"empty"` or `"none"`. */
    var filterMode: String?

    /** Start percentage of the window. */
    var start: Double?

    /** End percentage of the window. */
    var end: Double?

    /** Start value of the window. */
    var startValue: JsAny?

    /** End value of the window. */
    var endValue: JsAny?

    /** Minimum span percentage. */
    var minSpan: Double?

    /** Maximum span percentage. */
    var maxSpan: Double?

    /** Minimum span of the window. */
    var minValueSpan: JsAny?

    /** Maximum span of the window. */
    var maxValueSpan: JsAny?

    /** Orientation of the slider: `"horizontal"` or `"vertical"`. */
    var orient: String?

    /** Whether zooming is locked. */
    var zoomLock: Boolean?

    /** Throttle delay of wheel events in milliseconds. */
    var throttle: Double?

    /** Zoom range mode: `"value"` or `"percent"`. */
    var rangeMode: Array<String>?

    /** Whether brush selection is enabled. */
    var brushSelect: Boolean?

    /** Style of the brush selection. */
    var brushStyle: ItemStyleOption?

    /** Whether to show the detail text. */
    var showDetail: Boolean?

    /** Whether to show data shadows. */
    var showDataShadow: String?

    /** Zoom lock on the inside variant. */
    var preventDefaultMouseMove: Boolean?
}

/**
 * Toolbox component: a row of feature buttons.
 */
external interface ToolboxOption : JsAny {
    /** Component id. */
    var id: String?

    /** Whether the toolbox is shown. */
    var show: Boolean?

    /** Orientation: `"horizontal"` or `"vertical"`. */
    var orient: String?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the toolbox. */
    var width: StringOrDouble?

    /** Height of the toolbox. */
    var height: StringOrDouble?

    /** Distance between feature icons in pixels. */
    var itemGap: Double?

    /** Size of the feature icons. */
    var itemSize: Double?

    /** Whether to show the title of a feature on hover. */
    var showTitle: Boolean?

    /** Feature buttons. */
    var feature: ToolboxFeature?

    /** Icon style shared by all features. */
    var iconStyle: ItemStyleOption?

    /** Emphasis style of the feature icons. */
    var emphasis: EmphasisOption?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Tooltip shown when hovering a feature. */
    var tooltip: TooltipOption?
}

/**
 * Buttons of the toolbox.
 */
external interface ToolboxFeature : JsAny {
    /** Save the chart as an image. */
    var saveAsImage: SaveAsImageOption?

    /** Restore the chart to the initial option. */
    var restore: ToolboxFeatureOption?

    /** Show the data in a table. */
    var dataView: DataViewOption?

    /** Data zoom buttons. */
    var dataZoom: ToolboxFeatureOption?

    /** Switch between chart types, e.g. line and bar. */
    var magicType: MagicTypeOption?

    /** Brush selection. */
    var brush: ToolboxFeatureOption?
}

/**
 * Common configuration of a toolbox feature.
 */
external interface ToolboxFeatureOption : JsAny {
    /** Whether the feature is shown. */
    var show: Boolean?

    /** Title shown on hover. */
    var title: String?

    /** Icon of the feature; a built-in name or an image URL. */
    var icon: JsAny?

    /** Style of the feature icon. */
    var iconStyle: ItemStyleOption?

    /** Emphasis style of the feature icon. */
    var emphasis: EmphasisOption?

    /** Callback invoked when the feature is clicked. */
    var onclick: JsAny?
}

/**
 * `saveAsImage` toolbox feature.
 */
external interface SaveAsImageOption : ToolboxFeatureOption {
    /** File type: `"png"` or `"jpeg"`. */
    var type: String?

    /** File name without extension. */
    var name: String?

    /** Background color of the saved image. */
    var backgroundColor: JsAny?

    /** Whether to save all charts of the connected group. */
    var connectedBackgroundColor: JsAny?

    /** Pixel ratio of the saved image; the chart's device pixel ratio when omitted. */
    var pixelRatio: Double?

    /** Components excluded from the saved image, e.g. `["toolbox"]`. */
    var excludeComponents: Array<String>?
}

/**
 * `dataView` toolbox feature.
 */
external interface DataViewOption : ToolboxFeatureOption {
    /** Whether the data view is read-only. */
    var readOnly: Boolean?

    /** Languages available in the data view. */
    var lang: Array<String>?

    /** Background color of the data view. */
    var backgroundColor: JsAny?

    /** Text color of the data view. */
    var textareaColor: JsAny?

    /** Border color of the data view. */
    var textareaBorderColor: JsAny?

    /** Text color of the data view. */
    var textColor: JsAny?

    /** Button color of the data view. */
    var buttonColor: JsAny?

    /** Button text color of the data view. */
    var buttonTextColor: JsAny?

    /** Formatter of the displayed data. */
    var optionToContent: JsAny?

    /** Parser of the edited data. */
    var contentToOption: JsAny?

    /** Callback invoked when the data view is refreshed. */
    var refreshRate: Double?
}

/**
 * `magicType` toolbox feature.
 */
external interface MagicTypeOption : ToolboxFeatureOption {
    /** Chart types that can be switched to, e.g. `["line", "bar", "stack"]`. */
    var type: Array<String>?

    /** Option fragments applied for each chart type, keyed by type name. */
    var option: JsAny?

    /** Series index the magic type applies to. */
    var seriesIndex: JsAny?
}

/**
 * Visual map component; covers the continuous and piecewise variants.
 *
 * Set [type] to `"continuous"` or `"piecewise"`.
 */
external interface VisualMapOption : JsAny {
    /** Component id. */
    var id: String?

    /** Whether the component is shown. */
    var show: Boolean?

    /** Type: `"continuous"` or `"piecewise"`. */
    var type: String?

    /** Minimum value of the mapping range. */
    var min: Double?

    /** Maximum value of the mapping range. */
    var max: Double?

    /** Value the visual map is calculated from; use `min`/`max` together with `minOpen`/`maxOpen`. */
    var minOpen: Boolean?

    /** Whether the maximum value is excluded from the range. */
    var maxOpen: Boolean?

    /** Whether to show handles to adjust the range. */
    var calculable: Boolean?

    /** Orientation: `"vertical"` or `"horizontal"`. */
    var orient: String?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the component. */
    var itemWidth: Double?

    /** Height of the component. */
    var itemHeight: Double?

    /** Whether to invert the range. */
    var inverse: Boolean?

    /** Precision of displayed values. */
    var precision: Double?

    /** Whether to align the component to the right. */
    var align: String?

    /** Text displayed next to the range. */
    var text: Array<String>?

    /** Gap between the text and the component, in pixels. */
    var textGap: Double?

    /** Style of the text. */
    var textStyle: TextStyleOption?

    /** Formatter of the range labels. */
    var formatter: LabelFormatter?

    /** Visual style for values inside the range. */
    var inRange: JsAny?

    /** Visual style for values outside the range. */
    var outOfRange: JsAny?

    /** Configuration of the controller. */
    var controller: JsAny?

    /** Series indices controlled by this component. */
    var seriesIndex: JsAny?

    /** Dimension of the data used for the mapping. */
    var dimension: String?

    /** Whether hovering a value highlights the related data. */
    var hoverLink: Boolean?

    /** Number of split pieces of a piecewise visual map. */
    var splitNumber: Double?

    /** Pieces of a piecewise visual map. */
    var pieces: Array<JsAny>?

    /** Categories of a piecewise visual map. */
    var categories: Array<String>?

    /** How to select the piece style: `"auto"` or `"all"`. */
    var selectedMode: String?

    /** Items per row or column. */
    var itemGap: Double?

    /** Background color of the component. */
    var backgroundColor: JsAny?

    /** Border color of the component. */
    var borderColor: JsAny?

    /** Border width of the component. */
    var borderWidth: Double?

    /** Whether to show the labels. */
    var showLabel: Boolean?

    /** Range of the values displayed by the component, `[min, max]`. */
    var range: Array<Double>?
}

/**
 * Geo component: draws a map without needing a series.
 */
external interface GeoOption : JsAny {
    /** Component id. */
    var id: String?

    /** Whether the component is shown. */
    var show: Boolean?

    /** Name of the registered map. */
    var map: String?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Center of the map, e.g. `[104.0, 35.0]`. */
    var center: Array<JsAny>?

    /** Initial zoom level. */
    var zoom: Double?

    /** Zoom limits. */
    var scaleLimit: JsAny?

    /** Mapping of the names in the map data to display names. */
    var nameMap: JsAny?

    /** Selection mode: `false`, `"single"` or `"multiple"`. */
    var selectedMode: BooleanOrString?

    /** Style of the labels. */
    var label: LabelOption?

    /** Style of the map regions. */
    var itemStyle: ItemStyleOption?

    /** Emphasis style of the map regions. */
    var emphasis: EmphasisOption?

    /** Blur style of the map regions. */
    var blur: BlurOption?

    /** Select style of the map regions. */
    var select: SelectOption?

    /** Per-region configuration. */
    var regions: Array<JsAny>?

    /** Region layout: `"center"` for [layoutCenter]/[layoutSize]. */
    var layoutCenter: Array<String>?

    /** Size of the map when [layoutCenter] is used. */
    var layoutSize: StringOrDouble?

    /** Bounding coordinates of the map, `[[left, top], [right, bottom]]`. */
    var boundingCoords: JsAny?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Whether the map is silent. */
    var silent: Boolean?

    /** Tooltip of the component. */
    var tooltip: TooltipOption?
}

/**
 * Polar coordinate system.
 */
external interface PolarOption : JsAny {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Center of the polar coordinate system. */
    var center: Array<StringOrDouble>?

    /** Radius of the polar coordinate system, or `[inner, outer]`. */
    var radius: JsAny?

    /** Index of the tooltip that describes this component. */
    var tooltip: TooltipOption?
}

/**
 * Radar coordinate system.
 */
external interface RadarOption : JsAny {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Shape of the radar: `"polygon"` or `"circle"`. */
    var shape: String?

    /** Center of the radar. */
    var center: Array<StringOrDouble>?

    /** Radius of the radar. */
    var radius: StringOrDouble?

    /** Start angle in degrees. */
    var startAngle: Double?

    /** Indicator definitions, one per radar axis. */
    var indicator: Array<RadarIndicator>?

    /** Configuration of the axis names. */
    var axisName: LabelOption?

    /** Number of split areas. */
    var splitNumber: Double?

    /** Style of the axis lines. */
    var axisLine: LineStyleOption?

    /** Style of the split lines. */
    var splitLine: LineStyleOption?

    /** Style of the split areas. */
    var splitArea: AreaStyleOption?

    /** Style of the symbols on the axis lines. */
    var axisTick: JsAny?
}

/**
 * One axis of a radar coordinate system.
 */
external interface RadarIndicator : JsAny {
    /** Display name. */
    var name: String?

    /** Maximum value. */
    var max: Double?

    /** Minimum value. */
    var min: Double?

    /** Fixed color of the indicator, overriding the series color. */
    var color: JsAny?

    /** Axis-specific label style. */
    var axisName: LabelOption?
}

/**
 * Calendar coordinate system.
 */
external interface CalendarOption : JsAny {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the component. */
    var width: StringOrDouble?

    /** Height of the component. */
    var height: StringOrDouble?

    /** Orientation: `"horizontal"` or `"vertical"`. */
    var orient: String?

    /** Date range, e.g. `"2017"` or `["2017-01-01", "2017-12-31"]`. */
    var range: JsAny?

    /** Size of a day cell in pixels or as a percentage. */
    var cellSize: DoubleOrDoubleArray?

    /** Configuration of the day labels. */
    var dayLabel: JsAny?

    /** Configuration of the month labels. */
    var monthLabel: JsAny?

    /** Configuration of the year labels. */
    var yearLabel: JsAny?

    /** Style of the day cells. */
    var itemStyle: ItemStyleOption?

    /** Style of the split lines. */
    var splitLine: LineStyleOption?
}

/**
 * Timeline component: switches between several options over time.
 */
external interface TimelineOption : JsAny {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Type of the timeline axis: `"value"` or `"category"`. */
    var axisType: String?

    /** Index of the currently selected timeline point. */
    var currentIndex: Double?

    /** Whether to play the timeline automatically. */
    var autoPlay: Boolean?

    /** Whether to restart from the beginning when the end is reached. */
    var rewind: Boolean?

    /** Whether to loop. */
    var loop: Boolean?

    /** Playback speed in milliseconds per point. */
    var playbackSpeed: Double?

    /** Position of the control buttons. */
    var controlPosition: String?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Padding of the component. */
    var padding: DoubleOrDoubleArray?

    /** Orientation: `"horizontal"` or `"vertical"`. */
    var orient: String?

    /** Whether to invert the axis. */
    var inverse: Boolean?

    /** Symbol of the timeline points. */
    var symbol: String?

    /** Size of the timeline points. */
    var symbolSize: DoubleOrDoubleArray?

    /** Style of the axis line. */
    var lineStyle: LineStyleOption?

    /** Style of the axis labels. */
    var label: LabelOption?

    /** Style of the timeline points. */
    var itemStyle: ItemStyleOption?

    /** Style of the current timeline point. */
    var checkpointStyle: ItemStyleOption?

    /** Style of the play controls. */
    var controlStyle: ItemStyleOption?

    /** Style of the progress bar. */
    var progress: JsAny?

    /** Data of the timeline points. */
    var data: Array<JsAny>?
}

/**
 * A graphic element drawn directly on the canvas.
 */
external interface GraphicElement : JsAny {
    /** Element type, e.g. `"group"`, `"text"`, `"rect"`, `"circle"` or `"image"`. */
    var type: String?

    /** Element id, used by `graphic` actions. */
    var id: String?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Bounding box used to position the element. */
    var bounding: String?

    /** z-index of the element. */
    var z: Double?

    /** z-level of the element. */
    var zlevel: Double?

    /** Whether the element ignores mouse events. */
    var silent: Boolean?

    /** Whether the element is invisible. */
    var invisible: Boolean?

    /** Whether the element is ignored by the layout. */
    var ignore: Boolean?

    /** Whether the element can be dragged. */
    var draggable: Boolean?

    /** Cursor shown on hover. */
    var cursor: String?

    /** Shape definition of the element. */
    var shape: JsAny?

    /** Style of the element. */
    var style: JsAny?

    /** Position offset, `[x, y]`. */
    var position: Array<Double>?

    /** Scale, `[x, y]`. */
    var scale: Array<Double>?

    /** Rotation in degrees. */
    var rotation: Double?

    /** Origin of the rotation, `[x, y]`. */
    var origin: Array<Double>?

    /** Child elements of a `"group"` element. */
    var children: Array<GraphicElement>?

    /** Callback invoked when the element is clicked. */
    var onclick: JsAny?
}

/**
 * Accessibility settings.
 */
external interface AriaOption : JsAny {
    /** Whether screen-reader support is enabled. */
    var enabled: Boolean?

    /** Decal patterns that make series distinguishable without color. */
    var decal: AriaDecalOption?

    /** Text description of the chart for screen readers. */
    var label: AriaLabelOption?
}

/**
 * Decal settings of the accessibility module.
 */
external interface AriaDecalOption : JsAny {
    /** Whether to show decal patterns. */
    var show: Boolean?

    /** Default decal settings. */
    var decal: DecalOption?
}

/**
 * Screen-reader description of the chart.
 */
external interface AriaLabelOption : JsAny {
    /** Whether the description is enabled. */
    var enabled: Boolean?

    /** Custom description; defaults to a generated summary. */
    var description: String?
}

/**
 * Brush component: selects a region and emits `brushSelected`.
 */
external interface BrushOption : JsAny {
    /** Button configuration for the toolbox. */
    var toolbox: Array<String>?

    /** Brush type: `"rect"`, `"polygon"`, `"lineX"`, `"lineY"`, `"keep"` or `"clear"`. */
    var brushType: String?

    /** Brush mode: `"single"` or `"multiple"`. */
    var brushMode: String?

    /** Whether the selection can be transformed. */
    var transformable: Boolean?

    /** Style of the brush shape. */
    var brushStyle: ItemStyleOption?

    /** Throttle type of the `brushselected` event: `"debounce"` or `"fixRate"`. */
    var throttleType: String?

    /** Throttle delay in milliseconds. */
    var throttleDelay: Double?

    /** Whether the selection is removed on click. */
    var removeOnClick: Boolean?

    /** Selected indices per brush area. */
    var geoIndex: JsAny?

    /** Style of the elements inside the selection. */
    var inBrush: JsAny?

    /** Style of the elements outside the selection. */
    var outOfBrush: JsAny?

    /** Index of the x axes the brush applies to. */
    var xAxisIndex: JsAny?

    /** Index of the y axes the brush applies to. */
    var yAxisIndex: JsAny?
}

/**
 * Axis pointer: the crosshair shown by tooltip and axis triggers.
 */
external interface AxisPointerOption : JsAny {
    /** Whether the axis pointer is shown. */
    var show: Boolean?

    /** Type: `"line"`, `"shadow"`, `"none"`. */
    var type: String?

    /** Whether the pointer snaps to data points. */
    var snap: Boolean?

    /** Whether to trigger the tooltip. */
    var triggerTooltip: Boolean?

    /** z-index of the pointer. */
    var z: Double?

    /** Style of the label. */
    var label: JsAny?

    /** Style of the line pointer. */
    var lineStyle: LineStyleOption?

    /** Style of the shadow pointer. */
    var shadowStyle: AreaStyleOption?

    /** Style of the crosshair pointer. */
    var crossStyle: LineStyleOption?

    /** Animation of the pointer. */
    var animation: Boolean?

    /** Duration of the pointer animation. */
    var animationDuration: Double?

    /** Easing of the pointer animation. */
    var animationEasing: String?
}

/**
 * Matrix coordinate system introduced in ECharts 6.
 */
external interface MatrixOption : JsAny {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the component. */
    var width: StringOrDouble?

    /** Height of the component. */
    var height: StringOrDouble?

    /** Border width of the matrix cells. */
    var borderWidth: Double?

    /** Border color of the matrix cells. */
    var borderColor: JsAny?

    /** Background color of the matrix cells. */
    var backgroundColor: JsAny?

    /** Corner radius of the matrix cells. */
    var borderRadius: Double?

    /** Matrix body configuration. */
    var matrix: JsAny?

    /** Label style of the matrix cells. */
    var label: LabelOption?
}

/**
 * Parallel coordinate system.
 */
external interface ParallelOption : JsAny {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the component. */
    var width: StringOrDouble?

    /** Height of the component. */
    var height: StringOrDouble?

    /** Orientation: `"horizontal"` or `"vertical"`. */
    var layout: String?

    /** Index of the parallel axes used by this coordinate system. */
    var parallelAxisDefault: JsAny?
}
