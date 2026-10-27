package echarts.options

import echarts.VisualMapType
import echarts.BrushMode
import echarts.BrushThrottleType
import echarts.BrushToolboxIconType
import echarts.BrushTypeUncertain
import echarts.DataZoomRangeMode
import echarts.GraphicAction
import echarts.GraphicBounding
import echarts.HorizontalAlign
import echarts.MatrixDimensionType
import echarts.AxisPointerStatus
import echarts.AxisPointerTriggerOn
import echarts.ParallelAxisExpandTriggerOn
import echarts.TimelineControlPosition
import echarts.RadarShape
import echarts.Orient
import echarts.NameMap
import echarts.ImageType
import echarts.FilterMode
import echarts.DataZoomType
import echarts.AxisPointerType
import echarts.AnimationEasing
import js.objects.Record
import kotlin.js.JsAny
import kotlin.js.JsName
import web.html.HTMLDivElement
import zrender.ZRColor
import zrender.ZRElementEvent
import zrender.ZRFontWeight
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/**
 * One responsive option of [EChartsOption.media].
 */
external interface MediaUnit {
    /** Condition under which [option] is applied; omitted for the default option. */
    var query: MediaQueryOption?

    /** Option applied when [query] matches. */
    var option: EChartsOption
}

/**
 * Container size condition of a [MediaUnit].
 */
external interface MediaQueryOption {
    /** Minimum container width, in pixels. */
    var minWidth: Double?

    /** Maximum container width, in pixels. */
    var maxWidth: Double?

    /** Minimum container height, in pixels. */
    var minHeight: Double?

    /** Maximum container height, in pixels. */
    var maxHeight: Double?

    /** Minimum ratio of the container width to its height. */
    var minAspectRatio: Double?

    /** Maximum ratio of the container width to its height. */
    var maxAspectRatio: Double?
}

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
external interface EChartsOption {
    /**
     * Base option of a timeline / media option.
     *
     * When the option is a timeline or a media option, the shared part goes here and the
     * per-state parts go into [options] (timeline) or [media].
     */
    var baseOption: EChartsOption?

    /** One option per timeline point; only used together with [baseOption]. */
    var options: Array<EChartsOption>?

    /** Responsive options selected by container size. */
    var media: Array<MediaUnit>?

    /** Title components. */
    var title: Array<TitleOption>?

    /** Legend components. */
    var legend: Array<LegendOption>?

    /** Tooltip components. */
    var tooltip: Array<TooltipOption>?

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

    /** Toolbox components. */
    var toolbox: Array<ToolboxOption>?

    /** Visual map components. */
    var visualMap: Array<VisualMapOption>?

    /** Thumbnail components: a miniature movable view of a coordinate system. */
    var thumbnail: Array<ThumbnailOption>?

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

    /** Graphic components or graphic elements drawn without a coordinate system. */
    var graphic: Array<GraphicComponentLooseOption>?

    /** Accessibility settings. */
    var aria: AriaOption?

    /** Brush components. */
    var brush: Array<BrushOption>?

    /** Global axis pointer configurations. */
    var axisPointer: Array<AxisPointerOption>?

    /** Matrix coordinate systems. */
    var matrix: Array<MatrixOption>?

    /** Data series. */
    var series: Array<SeriesOption>?

    /** Color palette. */
    var color: Array<ZRColor>?

    /** Color palette layers, one per `colorBy` layer group. */
    var colorLayer: Array<Array<ZRColor>>?

    /** Background color of the whole chart. */
    var backgroundColor: ZRColor?

    /** Global text style of the chart. */
    var textStyle: TextStyleOption?

    /** Whether animation is enabled globally. */
    var animation: Boolean?

    /** Number of elements above which animation is disabled. */
    var animationThreshold: Double?

    /** Duration of the initial animation in milliseconds, or a callback per element. */
    var animationDuration: AnimationDuration?

    /** Easing of the initial animation. */
    var animationEasing: AnimationEasing?

    /** Delay of the initial animation in milliseconds, or a callback per element. */
    var animationDelay: AnimationDelay?

    /** Duration of the update animation in milliseconds, or a callback per element. */
    var animationDurationUpdate: AnimationDuration?

    /** Easing of the update animation. */
    var animationEasingUpdate: AnimationEasing?

    /** Delay of the update animation in milliseconds, or a callback per element. */
    var animationDelayUpdate: AnimationDelay?

    /** Animation of state transitions (hover, select, ...). */
    var stateAnimation: StateAnimationOption?

    /** Whether to treat time values as UTC. */
    var useUTC: Boolean?

    /** Whether to use the dark theme defaults. */
    var darkMode: BooleanOrString?

    /** Number of elements above which a separate hover layer is used. */
    var hoverLayerThreshold: Double?

    /**
     * Restore the centring of roamable coordinate systems used before 6.0.
     *
     * Upstream `legacyViewCoordSysCenterBase`, a compatibility switch for the coordinate system
     * refactoring.
     */
    var legacyViewCoordSysCenterBase: Boolean?

    /**
     * Keep `min` / `max` from being inverted by an inverse axis.
     *
     * Upstream `legacyMinMaxDontInverseAxis`, a compatibility switch for the axis refactoring.
     */
    var legacyMinMaxDontInverseAxis: Boolean?

    /** Blend mode used by the renderer. */
    var blendMode: String?

    /** Progressive rendering threshold. */
    var progressiveThreshold: Double?

    /** Number of elements rendered per frame during progressive rendering. */
    var progressive: Double?
}

/**
 * Base option shape accepted by `setOption`.
 *
 * Upstream declares `ECBasicOption` as the supertype of `EChartsOption`; every member is optional,
 * so the wrapper models both with [EChartsOption] and keeps this alias for callers that follow the
 * upstream name.
 */
typealias ECBasicOption = EChartsOption

/**
 * Dataset component: provides data shared by series.
 */
external interface DatasetOption {
    /** Component id, used by `series.datasetId`. */
    var id: String?

    /** Index of this dataset, assigned by ECharts. */
    var index: Double?

    /** Raw data: an array of rows, or an object for keyed data. */
    var source: DatasetSource?

    /** Dimensions of the dataset. */
    var dimensions: Array<DimensionDefinitionLoose>?

    /** Whether the first row of [source] contains dimension names. */
    var sourceHeader: OptionSourceHeader?

    /** Data transform pipeline, e.g. `{ type: "filter", config: { ... } }`. */
    var transform: PipedDataTransformOption?

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
external interface DataZoomOption : BoxLayoutOptionMixin {
    /** Component id. */
    var id: String?

    /** Whether the component is shown. */
    var show: Boolean?

    /** Variant of the data zoom component. */
    var type: DataZoomType?

    /** Background color. */
    var backgroundColor: ZRColor?

    /** Border color. */
    var borderColor: ZRColor?

    /** Fill color of the selected range. */
    var fillerColor: ZRColor?

    /** Data background of the slider. */
    var dataBackground: DataZoomDataBackgroundOption?

    /** Selected data background of the slider. */
    var selectedDataBackground: DataZoomDataBackgroundOption?

    /** Icon of the two handles. */
    var handleIcon: String?

    /** Size of the handles. */
    var handleSize: StringOrDouble?

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
    var xAxisIndex: IndexOrIndexArray?

    /** Indices of the y axes controlled by this component. */
    var yAxisIndex: IndexOrIndexArray?

    /** Indices of the radius axes controlled by this component. */
    var radiusAxisIndex: IndexOrIndexArray?

    /** Indices of the angle axes controlled by this component. */
    var angleAxisIndex: IndexOrIndexArray?

    /** Indices of the single axes controlled by this component. */
    var singleAxisIndex: IndexOrIndexArray?

    /** How the data outside the window is handled. */
    var filterMode: FilterMode?

    /** Start percentage of the window. */
    var start: Double?

    /** End percentage of the window. */
    var end: Double?

    /** Start value of the window. */
    var startValue: DataZoomValue?

    /** End value of the window. */
    var endValue: DataZoomValue?

    /** Minimum span percentage. */
    var minSpan: Double?

    /** Maximum span percentage. */
    var maxSpan: Double?

    /** Minimum span of the window. */
    var minValueSpan: Double?

    /** Maximum span of the window. */
    var maxValueSpan: Double?

    /** Orientation of the slider: `"horizontal"` or `"vertical"`. */
    var orient: Orient?

    /** Whether zooming is locked. */
    var zoomLock: Boolean?

    /** Throttle delay of wheel events in milliseconds. */
    var throttle: Double?

    /** How each bound of the zoom range is interpreted: `"value"` or `"percent"`. */
    var rangeMode: Array<DataZoomRangeMode>?

    /** Whether brush selection is enabled. */
    var brushSelect: Boolean?

    /** Style of the brush selection. */
    var brushStyle: ItemStyleOption?

    /** Whether to show the detail text. */
    var showDetail: Boolean?

    /** Whether to show the data shadow behind the slider: `"auto"` or a boolean. */
    var showDataShadow: BooleanOrString?

    /** Zoom lock on the inside variant. */
    var preventDefaultMouseMove: Boolean?
}

/**
 * Data background of a data zoom slider.
 */
external interface DataZoomDataBackgroundOption {
    /** Style of the lines of the data shadow. */
    var lineStyle: LineStyleOption?

    /** Style of the areas of the data shadow. */
    var areaStyle: AreaStyleOption?
}

/**
 * Thumbnail component: a miniature, movable view of a coordinate system.
 *
 * Thumbnail is implemented as a component so that one thumbnail can serve several series and a
 * geo coordinate system at the same time.
 */
external interface ThumbnailOption : BoxLayoutOptionMixin, ComponentOnCalendarOptionMixin, ComponentOnMatrixOptionMixin {
    /** Component id. */
    var id: String?

    /** Whether the thumbnail is shown. */
    var show: Boolean?

    /** Style of the thumbnail window. */
    var itemStyle: ItemStyleOption?

    /** Style of the window that marks the visible part of the main coordinate system. */
    var windowStyle: ItemStyleOption?

    /** Indices of the series drawn inside the thumbnail. */
    var seriesIndex: IndexOrIndexArray?

    /** Ids of the series drawn inside the thumbnail. */
    var seriesId: StringOrStringArray?
}

/**
 * Toolbox component: a row of feature buttons.
 */
external interface ToolboxOption {
    /** Component id. */
    var id: String?

    /** Whether the toolbox is shown. */
    var show: Boolean?

    /** Orientation: `"horizontal"` or `"vertical"`. */
    var orient: Orient?

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
    var iconStyle: ToolboxIconStyleOption?

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
external interface ToolboxFeature {
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
external interface ToolboxFeatureOption {
    /** Whether the feature is shown. */
    var show: Boolean?

    /** Title shown on hover, or one title per feature key. */
    var title: StringOrDictionary?

    /** Icon of the feature, or one icon per feature key. */
    var icon: StringOrDictionary?

    /** Style of the feature icon. */
    var iconStyle: ToolboxIconStyleOption?

    /** Emphasis style of the feature icon. */
    var emphasis: ToolboxEmphasisOption?

    /** Status of each icon key. */
    var iconStatus: Record<String, String>?

    /** Callback invoked when the feature is clicked. */
    var onclick: (() -> Unit)?
}

/**
 * Icon style of a toolbox feature: an item style plus text-specific fields.
 */
external interface ToolboxIconStyleOption : ItemStyleOption {
    /** Fill color of the feature text. */
    var textFill: ZRColor?

    /** Background color of the feature text. */
    var textBackgroundColor: ZRColor?

    /** Position of the feature text. */
    var textPosition: LabelPosition?

    /** Horizontal alignment of the feature text. */
    var textAlign: String?

    /** Border radius of the feature text. */
    var textBorderRadius: StringOrDoubleOrArray?

    /** Padding of the feature text. */
    var textPadding: DoubleOrDoubleArray?

    /** Font family of the feature text. */
    var textFontFamily: String?

    /** Font size of the feature text. */
    var textFontSize: StringOrDouble?

    /** Font weight of the feature text. */
    var textFontWeight: ZRFontWeight?

    /** Font style of the feature text. */
    var textFontStyle: String?
}

/**
 * Emphasis style of a toolbox feature.
 */
external interface ToolboxEmphasisOption {
    /** Icon style in the emphasis state. */
    var iconStyle: ToolboxIconStyleOption?
}

/**
 * `saveAsImage` toolbox feature.
 */
external interface SaveAsImageOption : ToolboxFeatureOption {
    /** File type of the saved image. */
    var type: ImageType?

    /** File name without extension. */
    var name: String?

    /** Background color of the saved image. */
    var backgroundColor: ZRColor?

    /** Whether to save all charts of the connected group. */
    var connectedBackgroundColor: ZRColor?

    /** Pixel ratio of the saved image; the chart's device pixel ratio when omitted. */
    var pixelRatio: Double?

    /** Components excluded from the saved image, e.g. `["toolbox"]`. */
    var excludeComponents: Array<String>?

    /** Languages available in the save dialog. */
    var lang: Array<String>?
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
    var backgroundColor: String?

    /** Text color of the data view. */
    var textareaColor: String?

    /** Border color of the data view. */
    var textareaBorderColor: String?

    /** Text color of the data view. */
    var textColor: String?

    /** Button color of the data view. */
    var buttonColor: String?

    /** Button text color of the data view. */
    var buttonTextColor: String?

    /** Converts the option to the content shown in the data view. */
    var optionToContent: ((option: EChartsOption) -> StringOrHTMLElement)?

    /** Parses the edited content back into an option. */
    var contentToOption: ((viewMain: HTMLDivElement, oldOption: EChartsOption) -> EChartsOption)?

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
    var option: Record<String, SeriesOption>?

    /** Series index the magic type applies to, keyed by type name. */
    var seriesIndex: Record<String, Double>?
}

/**
 * Visual map component; covers the continuous and piecewise variants.
 *
 * Set [type] to `"continuous"` or `"piecewise"`.
 */
external interface VisualMapOption {
    /** Component id. */
    var id: String?

    /** Whether the component is shown. */
    var show: Boolean?

    /** Variant of the visual map. */
    var type: VisualMapType?

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
    var orient: Orient?

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

    /** Alignment of the component: `"auto"`, `"left"` or `"right"`. */
    var align: HorizontalAlign?

    /** Text displayed next to the range. */
    var text: Array<String>?

    /** Gap between the text and the component, in pixels. */
    var textGap: Double?

    /** Style of the text. */
    var textStyle: LabelOption?

    /** Formatter of the range labels. */
    var formatter: LabelFormatter?

    /** Visual style for values inside the range. */
    var inRange: VisualOptionUnit?

    /** Visual style for values outside the range. */
    var outOfRange: VisualOptionUnit?

    /** Configuration of the controller. */
    var controller: VisualMapControllerOption?

    /** Visual style applied to the target element. */
    var target: VisualMapControllerOption?

    /** Series indices controlled by this component. */
    var seriesIndex: IndexOrIndexArray?

    /** Series targets with specific dimensions. */
    var seriesTargets: Array<VisualMapSeriesTarget>?

    /** Dimension of the data used for the mapping. */
    var dimension: Double?

    /** Whether hovering a value highlights the related data. */
    var hoverLink: Boolean?

    /** Number of split pieces of a piecewise visual map. */
    var splitNumber: Double?

    /** Pieces of a piecewise visual map. */
    var pieces: Array<VisualPiece>?

    /** Categories of a piecewise visual map. Upstream types this as `unknown`. */
    var categories: JsAny?

    /** How to select the piece style: `"auto"` or `"all"`. */
    var selectedMode: SelectionMode?

    /** Items per row or column. */
    var itemGap: Double?

    /** Background color of the component. */
    var backgroundColor: ZRColor?

    /** Border color of the component. */
    var borderColor: ZRColor?

    /** Border width of the component. */
    var borderWidth: Double?

    /** Color of the text content. */
    var contentColor: ZRColor?

    /** Color of the text of inactive items. */
    var inactiveColor: ZRColor?

    /** Color palette of the visual map. */
    var color: Array<String>?

    /** Padding of the component. */
    var padding: DoubleOrDoubleArray?

    /** Whether the component updates while the range is dragged. */
    var realtime: Boolean?

    /** Whether to show the labels. */
    var showLabel: Boolean?

    /** Range of the values displayed by the component, `[min, max]`. */
    var range: Array<Double>?
}

/**
 * Geo component: draws a map without needing a series.
 */
external interface GeoOption : BoxLayoutOptionMixin {
    /** Component id. */
    var id: String?

    /** Whether the component is shown. */
    var show: Boolean?

    /** Name of the registered map. */
    var map: String?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Center of the map, e.g. `[104.0, 35.0]`. */
    var center: Array<StringOrDouble>?

    /** Initial zoom level. */
    var zoom: Double?

    /** Zoom limits. */
    var scaleLimit: ScaleLimitOption?

    /** Mapping of the names in the map data to display names. */
    var nameMap: NameMap?

    /** Property of the GeoJSON feature used as the region name. */
    var nameProperty: String?

    /** Aspect ratio of the map. */
    var aspectScale: Double?

    /** Selection mode of the regions. */
    var selectedMode: SelectionMode?

    /** Selection state per region name. */
    var selectedMap: Record<String, Boolean>?

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
    var regions: Array<RegionOption>?

    /** Region layout: `"center"` for [layoutCenter]/[layoutSize]. */
    var layoutCenter: Array<StringOrDouble>?

    /** Size of the map when [layoutCenter] is used. */
    var layoutSize: StringOrDouble?

    /** Bounding coordinates of the map, `[[left, top], [right, bottom]]`. */
    var boundingCoords: Array<Array<Double>>?

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
 * Configuration of one region of a map or geo component.
 */
external interface RegionOption {
    /** Name of the region. */
    var name: String?

    /** Whether the region is selected. */
    var selected: Boolean?

    /** Whether the region ignores mouse events. */
    var silent: Boolean?

    /** Style of the region. */
    var itemStyle: ItemStyleOption?

    /** Label style of the region. */
    var label: LabelOption?

    /** Emphasis style of the region. */
    var emphasis: RegionStateOption?

    /** Blur style of the region. */
    var blur: RegionStateOption?

    /** Select style of the region. */
    var select: RegionStateOption?

    /** Tooltip specific to the region. */
    var tooltip: TooltipOption?
}

/**
 * State style of a map region.
 */
external interface RegionStateOption {
    /** Style of the region. */
    var itemStyle: ItemStyleOption?

    /** Label style of the region. */
    var label: LabelOption?
}

/**
 * Polar coordinate system.
 */
external interface PolarOption {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Center of the polar coordinate system. */
    var center: Array<StringOrDouble>?

    /** Radius of the polar coordinate system, or `[inner, outer]`. */
    var radius: StringOrDoubleOrArray?

    /** Index of the tooltip that describes this component. */
    var tooltip: TooltipOption?
}

/**
 * Radar coordinate system.
 */
external interface RadarOption {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Shape of the radar. */
    var shape: RadarShape?

    /** Center of the radar. */
    var center: Array<StringOrDouble>?

    /** Radius of the radar. */
    var radius: StringOrDouble?

    /** Start angle in degrees. */
    var startAngle: Double?

    /** Whether the radar is drawn clockwise. */
    var clockwise: Boolean?

    /** Indicator definitions, one per radar axis. */
    var indicator: Array<RadarIndicator>?

    /** Configuration of the axis names. */
    var axisName: LabelOption?

    /** Distance between the axis name and the axis line. */
    var axisNameGap: Double?

    /** Whether axes with a zero minimum are shared. */
    var scale: Boolean?

    /** Whether the radar emits mouse events. */
    var triggerEvent: Boolean?

    /** Gap at the ends of a category axis. */
    var boundaryGap: BooleanOrStringArray?

    /** Number of split areas. */
    var splitNumber: Double?

    /** Style of the axis lines. */
    var axisLine: AxisLineOption?

    /** Style of the axis ticks. */
    var axisTick: AxisTickOption?

    /** Style of the axis labels. */
    var axisLabel: AxisLabelOption?

    /** Style of the split lines. */
    var splitLine: SplitLineOption?

    /** Style of the split areas. */
    var splitArea: SplitAreaOption?
}

/**
 * One axis of a radar coordinate system.
 */
external interface RadarIndicator {
    /** Display name. */
    var name: String?

    /** Deprecated display name; use [name]. */
    var text: String?

    /** Maximum value. */
    var max: Double?

    /** Minimum value. */
    var min: Double?

    /** Fixed color of the indicator, overriding the series color. */
    var color: String?

    /** Type of the indicator axis: `"value"` or `"log"`. */
    var axisType: String?
}

/**
 * Calendar coordinate system.
 */
external interface CalendarOption {
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
    var orient: Orient?

    /** Date range, e.g. `"2017"` or `["2017-01-01", "2017-12-31"]`. */
    var range: CalendarRange?

    /** Size of a day cell in pixels or as a percentage. */
    var cellSize: StringOrDoubleOrArray?

    /** Configuration of the day labels. */
    var dayLabel: CalendarDayLabelOption?

    /** Configuration of the month labels. */
    var monthLabel: CalendarMonthLabelOption?

    /** Configuration of the year labels. */
    var yearLabel: CalendarYearLabelOption?

    /** Style of the day cells. */
    var itemStyle: ItemStyleOption?

    /** Style of the split lines. */
    var splitLine: SplitLineOption?
}

/**
 * Range of a calendar: one date or two dates bounding the range.
 */
external interface CalendarRange

/** Wraps one date. */
fun CalendarRange(value: DataZoomValue): CalendarRange = value.unsafeCast<CalendarRange>()

/** Wraps two dates. */
fun CalendarRange(values: Array<DataZoomValue>): CalendarRange = values.unsafeCast<CalendarRange>()

/**
 * Day labels of a calendar.
 */
external interface CalendarDayLabelOption : LabelOption {
    /** First day of the week, `0` for Sunday. */
    var firstDay: Double?

    /** Margin between the label and the axis line. */
    var margin: StringOrDouble?

    /** Names of the days, starting with Sunday. */
    var nameMap: StringOrStringArray?
}

/**
 * Month labels of a calendar.
 */
external interface CalendarMonthLabelOption : LabelOption {
    /** Margin between the label and the axis line. */
    var margin: Double?

    /** Names of the months, starting with January. */
    var nameMap: StringOrStringArray?

    /** Formatter of the month labels. */
    override var formatter: LabelFormatter?
}

/**
 * Year labels of a calendar.
 */
external interface CalendarYearLabelOption : LabelOption {
    /** Margin between the label and the axis line. */
    var margin: Double?

    /** Formatter of the year labels. */
    override var formatter: LabelFormatter?
}

/**
 * Timeline component: switches between several options over time.
 */
external interface TimelineOption {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Whether the component is shown. */
    var show: Boolean?

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

    /** Position of the control buttons: `"left"` or `"right"`. */
    var controlPosition: TimelineControlPosition?

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
    var orient: Orient?

    /** Whether to invert the axis. */
    var inverse: Boolean?

    /** Symbol of the timeline points. */
    var symbol: String?

    /** Size of the timeline points. */
    var symbolSize: DoubleOrDoubleArray?

    /** Style of the axis line. */
    var lineStyle: TimelineLineStyleOption?

    /** Style of the axis labels. */
    var label: TimelineLabelOption?

    /** Style of the timeline points. */
    var itemStyle: ItemStyleOption?

    /** Style of the current timeline point. */
    var checkpointStyle: TimelineCheckpointStyle?

    /** Style of the play controls. */
    var controlStyle: TimelineControlStyle?

    /** Style of the progress bar. */
    var progress: TimelineProgressOption?

    /** Data of the timeline points. */
    var data: Array<OptionDataItem?>?

    /** Background color of the component. */
    var backgroundColor: ZRColor?

    /** Border color of the component. */
    var borderColor: String?

    /** Border width of the component. */
    var borderWidth: Double?

    /** Interval between two points during playback, in milliseconds. */
    var playInterval: Double?

    /** Whether the component updates in realtime. */
    var realtime: Boolean?

    /**
     * Components that are replaced instead of merged when the timeline switches to another option.
     *
     * When omitted, the option of the new point is merged into the current one (`normalMerge`).
     */
    var replaceMerge: StringOrStringArray?
}

/**
 * Line style of a timeline axis.
 */
external interface TimelineLineStyleOption : LineStyleOption {
    /** Whether the line is shown. */
    var show: Boolean?
}

/**
 * Label style of a timeline axis.
 */
external interface TimelineLabelOption : LabelOption {
    /** Whether the label is shown. */
    override var show: Boolean?

    /** Interval between two labels. */
    var interval: AxisInterval?
}

/**
 * Style of the current point of a timeline.
 */
external interface TimelineCheckpointStyle : ItemStyleOption, SymbolOptionMixin {
    /** Whether the point animates. */
    var animation: Boolean?

    /** Duration of the animation in milliseconds. */
    var animationDuration: Double?

    /** Easing of the animation. */
    var animationEasing: AnimationEasing?
}

/**
 * Style of the play controls of a timeline.
 */
external interface TimelineControlStyle : ItemStyleOption {
    /** Whether the controls are shown. */
    var show: Boolean?

    /** Whether the play button is shown. */
    var showPlayBtn: Boolean?

    /** Whether the previous button is shown. */
    var showPrevBtn: Boolean?

    /** Whether the next button is shown. */
    var showNextBtn: Boolean?

    /** Size of the buttons. */
    var itemSize: Double?

    /** Gap between the buttons. */
    var itemGap: Double?

    /** Position of the controls: `"left"`, `"right"`, `"top"` or `"bottom"`. */
    var position: TimelineControlPosition?

    /** Icon of the play button. */
    var playIcon: String?

    /** Icon of the stop button. */
    var stopIcon: String?

    /** Icon of the previous button. */
    var prevIcon: String?

    /** Icon of the next button. */
    var nextIcon: String?

    /** Size of the play button. */
    var playBtnSize: StringOrDouble?

    /** Size of the stop button. */
    var stopBtnSize: StringOrDouble?

    /** Size of the next button. */
    var nextBtnSize: StringOrDouble?

    /** Size of the previous button. */
    var prevBtnSize: StringOrDouble?
}

/**
 * Progress bar style of a timeline.
 */
external interface TimelineProgressOption {
    /** Style of the progress line. */
    var lineStyle: TimelineLineStyleOption?

    /** Style of the progress area. */
    var itemStyle: ItemStyleOption?

    /** Style of the progress label. */
    var label: TimelineLabelOption?
}

/**
 * A graphic element drawn directly on the canvas.
 */
external interface GraphicElement {
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

    /**
     * X coordinate of the element.
     *
     * The upstream element option inherits it from the zrender element props (`TextProps.x` and
     * friends), where it is a number; the graphic component also accepts a percentage string.
     */
    var x: StringOrDouble?

    /** Y coordinate of the element; see [x]. */
    var y: StringOrDouble?

    /** Width of a `"group"` element, in pixels. */
    var width: Double?

    /** Height of a `"group"` element, in pixels. */
    var height: Double?

    /** Bounding box used to position the element: `"raw"` or `"all"`. */
    var bounding: GraphicBounding?

    /**
     * How the element reacts when the option is merged: `"merge"`, `"replace"` or `"remove"`.
     *
     * Upstream property is `$action`.
     */
    @JsName("\$action")
    var action: GraphicAction?

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
    var shape: ZRShapeProps?

    /** Style of the element. */
    var style: ZRStyleProps?

    /** Position offset, `[x, y]`. */
    var position: Array<Double>?

    /** Scale, `[x, y]`. */
    var scale: Array<Double>?

    /** Rotation in radians, as in the zrender element props. */
    var rotation: Double?

    /** Origin of the rotation, `[x, y]`. */
    var origin: Array<Double>?

    /** Child elements of a `"group"` element. */
    var children: Array<GraphicElement>?

    /** Callback invoked when the element is clicked. */
    var onclick: ((event: ZRElementEvent) -> Unit)?
}

/**
 * Graphic component: a group of elements that is positioned and animated as a whole.
 *
 * Upstream `GraphicComponentOption`. Use [GraphicComponentLooseOption] to put it into
 * [EChartsOption.graphic].
 */
external interface GraphicComponentOption : AnimationOption {
    /** Component id. */
    var id: String?

    /** z-index of the component. */
    var z: Double?

    /** z-level of the component. */
    var zlevel: Double?

    /** Elements of the group. */
    var elements: Array<GraphicElement>?

    /**
     * How the group reacts when the option is merged: `"merge"`, `"replace"` or `"remove"`.
     *
     * Upstream property is `$action`.
     */
    @JsName("\$action")
    var action: GraphicAction?
}

/**
 * One entry of the `graphic` option: a [GraphicComponentOption] or a bare [GraphicElement].
 *
 * Upstream `GraphicComponentLooseOption`.
 */
external interface GraphicComponentLooseOption

/** Wraps a graphic component. */
fun GraphicComponentLooseOption(value: GraphicComponentOption): GraphicComponentLooseOption =
    value.unsafeCast<GraphicComponentLooseOption>()

/** Wraps a bare element. */
fun GraphicComponentLooseOption(value: GraphicElement): GraphicComponentLooseOption =
    value.unsafeCast<GraphicComponentLooseOption>()

/**
 * Accessibility settings.
 */
external interface AriaOption {
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
external interface AriaDecalOption {
    /** Whether to show decal patterns. */
    var show: Boolean?

    /** Default decal configurations, or a single one. */
    var decals: DecalList?
}

/**
 * Screen-reader description of the chart.
 */
external interface AriaLabelOption {
    /** Whether the description is enabled. */
    var enabled: Boolean?

    /** Custom description; defaults to a generated summary. */
    var description: String?
}

/**
 * Brush component: selects a region and emits `brushSelected`.
 */
external interface BrushOption {
    /** Icon of the brush types shown in the toolbox: `"rect"`, `"polygon"`, `"lineX"`, `"lineY"`, `"keep"` or `"clear"`. */
    var toolbox: Array<BrushToolboxIconType>?

    /** Brush type: `"rect"`, `"polygon"`, `"lineX"`, `"lineY"`, `"auto"` or `false`. */
    var brushType: BrushTypeUncertain?

    /** Brush mode: `"single"` or `"multiple"`. */
    var brushMode: BrushMode?

    /** Whether the selection can be transformed. */
    var transformable: Boolean?

    /** Style of the brush shape. */
    var brushStyle: BrushStyleOption?

    /** Throttle type of the `brushselected` event: `"debounce"` or `"fixRate"`. */
    var throttleType: BrushThrottleType?

    /** Throttle delay in milliseconds. */
    var throttleDelay: Double?

    /** Whether the selection is removed on click. */
    var removeOnClick: Boolean?

    /** Selected indices per brush area. */
    var geoIndex: ModelFinderIndexQuery?

    /** Style of the elements inside the selection. */
    var inBrush: VisualOptionUnit?

    /** Style of the elements outside the selection. */
    var outOfBrush: VisualOptionUnit?

    /** Index of the x axes the brush applies to. */
    var xAxisIndex: ModelFinderIndexQuery?

    /** Index of the y axes the brush applies to. */
    var yAxisIndex: ModelFinderIndexQuery?

    /** Links the brush areas of several components. */
    var brushLink: ModelFinderIndexQuery?
}

/**
 * Style of a brush area.
 */
external interface BrushStyleOption {
    /** Border width of the brush area. */
    var borderWidth: Double?

    /** Fill color of the brush area. */
    var color: ZRColor?

    /** Border color of the brush area. */
    var borderColor: ZRColor?
}

/**
 * Axis pointer: the crosshair shown by tooltip and axis triggers.
 */
external interface AxisPointerOption {
    /** Whether the axis pointer is shown, or `"auto"`. */
    var show: BooleanOrString?

    /** Shape of the pointer. */
    var type: AxisPointerType?

    /** How the pointer is triggered: `"click"`, `"mousemove"`, `"none"` or `"mousemove|click"`. */
    var triggerOn: AxisPointerTriggerOn?

    /** Whether hovering highlights the related series. */
    var triggerEmphasis: Boolean?

    /** Current value of the pointer. */
    var value: OptionDataValue?

    /** `"show"` or `"hide"`; drives the pointer from `dispatchAction`. */
    var status: AxisPointerStatus?

    /** Whether the pointer snaps to data points. */
    var snap: Boolean?

    /** Whether to trigger the tooltip. */
    var triggerTooltip: Boolean?

    /** z-index of the pointer. */
    var z: Double?

    /** Style of the label. */
    var label: AxisPointerLabelOption?

    /** Handle of the pointer. */
    var handle: AxisPointerHandleOption?

    /** Axes linked to this pointer. */
    var link: Array<AxisPointerLink>?

    /** Style of the line pointer. */
    var lineStyle: LineStyleOption?

    /** Style of the shadow pointer. */
    var shadowStyle: AreaStyleOption?

    /** Style of the crosshair pointer. */
    var crossStyle: LineStyleOption?

    /** Animation of the pointer. */
    var animation: BooleanOrString?

    /** Duration of the pointer animation. */
    var animationDuration: Double?

    /** Easing of the pointer animation. */
    var animationEasing: AnimationEasing?
}

/**
 * Label of an axis pointer.
 */
external interface AxisPointerLabelOption : LabelOption {
    /** Precision of the displayed value, or `"auto"`. */
    override var precision: StringOrDouble?

    /** Margin between the label and the pointer. */
    var margin: Double?

    /** Formatter of the label. */
    override var formatter: LabelFormatter?
}

/**
 * Handle of an axis pointer.
 */
external interface AxisPointerHandleOption {
    /** Whether the handle is shown. */
    var show: Boolean?

    /** Icon of the handle. */
    var icon: String?

    /** Size of the handle. */
    var size: DoubleOrDoubleArray?

    /** Distance between the handle and the axis. */
    var margin: Double?

    /** Color of the handle. */
    var color: String?

    /** Throttle delay for mobile devices. */
    var throttle: Double?

    /** Shadow blur radius of the handle. */
    var shadowBlur: Double?

    /** Shadow color of the handle. */
    var shadowColor: String?

    /** Horizontal shadow offset of the handle. */
    var shadowOffsetX: Double?

    /** Vertical shadow offset of the handle. */
    var shadowOffsetY: Double?
}

/**
 * One linked axis group of an axis pointer.
 */
external interface AxisPointerLink {
    /** X axes of the link. */
    var xAxisIndex: ModelFinderIndexQuery?

    /** Y axes of the link. */
    var yAxisIndex: ModelFinderIndexQuery?

    /** Ids of the x axes of the link. */
    var xAxisId: Array<String>?

    /** Ids of the y axes of the link. */
    var yAxisId: Array<String>?

    /** Names of the x axes of the link. */
    var xAxisName: StringOrStringArray?

    /** Names of the y axes of the link. */
    var yAxisName: StringOrStringArray?

    /** Radius axes of the link. */
    var radiusAxisIndex: ModelFinderIndexQuery?

    /** Angle axes of the link. */
    var angleAxisIndex: ModelFinderIndexQuery?

    /** Radius axis ids of the link. */
    var radiusAxisId: Array<String>?

    /** Angle axis ids of the link. */
    var angleAxisId: Array<String>?

    /** Radius axis names of the link. */
    var radiusAxisName: StringOrStringArray?

    /** Angle axis names of the link. */
    var angleAxisName: StringOrStringArray?

    /** Single axes of the link. */
    var singleAxisIndex: ModelFinderIndexQuery?

    /** Single axis ids of the link. */
    var singleAxisId: Array<String>?

    /** Single axis names of the link. */
    var singleAxisName: StringOrStringArray?
}

/**
 * Matrix coordinate system introduced in ECharts 6.
 */
external interface MatrixOption {
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

    /** x dimension (header row) of the matrix. */
    var x: MatrixDimensionOption?

    /** y dimension (header column) of the matrix. */
    var y: MatrixDimensionOption?

    /** Body cells of the matrix. */
    var body: MatrixBodyOption?

    /** Top-left corner cells of the matrix. */
    var corner: MatrixCornerOption?

    /** Background style of the matrix. */
    var backgroundStyle: ItemStyleOption?

    /** z-order of the cell borders. */
    var borderZ2: Double?

    /** Tooltip of the matrix. */
    var tooltip: TooltipOption?

    /** Whether the matrix emits mouse events. */
    var triggerEvent: Boolean?
}

/**
 * One dimension of a matrix.
 */
external interface MatrixDimensionOption : MatrixCellStyleOption {
    /** Dimension type; only `"category"` is supported. */
    var type: MatrixDimensionType?

    /** Whether the dimension is shown. */
    var show: Boolean?

    /** Cells of the dimension; a plain string is a leaf cell. */
    var data: Array<MatrixDimensionCellLooseOption>?

    /** Length of the dimension, in pixels. */
    var length: Double?

    /** Size of the outermost level of the dimension; same as `levels[0].levelSize`. */
    var levelSize: StringOrDouble?

    /** Sizes of the nested levels, or `null` for a level with the default size. */
    var levels: Array<MatrixDimensionLevelOption?>?

    /** Style of the divider lines. */
    var dividerLineStyle: LineStyleOption?
}

/**
 * Style shared by the cells of a matrix: dimensions, the body and the corner.
 *
 * Upstream `MatrixCellStyleOption`.
 */
external interface MatrixCellStyleOption {
    /** Label style of the cells. */
    var label: LabelOption?

    /** Item style of the cells. */
    var itemStyle: ItemStyleOption?

    /** Cursor shown on hover. */
    var cursor: String?

    /** Whether the cells ignore mouse events. */
    var silent: Boolean?

    /** z-order of the cells. */
    var z2: Double?
}

/**
 * One cell of a matrix dimension.
 */
external interface MatrixDimensionCellOption : MatrixCellStyleOption {
    /** Value of the cell. */
    var value: String?

    /** Size of the cell. */
    var size: StringOrDouble?

    /** Nested child cells of a non-leaf cell. */
    var children: Array<MatrixDimensionCellOption>?
}

/**
 * A cell of a matrix dimension: either a cell object or the plain value of a leaf cell.
 *
 * Upstream `MatrixDimensionCellLooseOption`.
 */
external interface MatrixDimensionCellLooseOption

/** Wraps a cell object. */
fun MatrixDimensionCellLooseOption(value: MatrixDimensionCellOption): MatrixDimensionCellLooseOption =
    value.unsafeCast<MatrixDimensionCellLooseOption>()

/** Wraps the value of a leaf cell. */
fun MatrixDimensionCellLooseOption(value: String): MatrixDimensionCellLooseOption =
    value.unsafeCast<MatrixDimensionCellLooseOption>()

/**
 * Size of one nested level of a matrix dimension.
 */
external interface MatrixDimensionLevelOption {
    /** Length of the level. */
    var levelSize: StringOrDouble?
}

/**
 * Cells of the matrix body or corner.
 */
external interface MatrixBodyCornerOption : MatrixCellStyleOption {
    /** Cells with a specific coordinate. */
    var data: Array<MatrixBodyCornerCellOption>?
}

/** Body cells of a matrix. */
typealias MatrixBodyOption = MatrixBodyCornerOption

/** Corner cells of a matrix. */
typealias MatrixCornerOption = MatrixBodyCornerOption

/**
 * One cell of the matrix body or corner.
 */
external interface MatrixBodyCornerCellOption : MatrixCellStyleOption {
    /** Value of the cell. */
    var value: String?

    /**
     * Coordinates of the cell, one entry per dimension.
     *
     * Upstream `MatrixCoordRangeOption[]`: `["A", 2]` locates one cell, `[["A", "C"], 2]` a range
     * of columns, and `null` means the dimension is not relevant.
     */
    var coord: Array<MatrixCoordRangeOption?>?

    /** Whether the coordinates are clamped to the matrix bounds. */
    var coordClamp: Boolean?

    /** Whether the cell is merged with its neighbours. */
    var mergeCells: Boolean?
}

/**
 * One coordinate of a matrix cell: a value, a `[min, max]` range, or `null` for "not relevant".
 *
 * Upstream `MatrixCoordRangeOption`.
 */
external interface MatrixCoordRangeOption

/** Wraps a single coordinate value. */
fun MatrixCoordRangeOption(value: OptionDataValue): MatrixCoordRangeOption =
    value.unsafeCast<MatrixCoordRangeOption>()

/** Wraps a coordinate range. */
fun MatrixCoordRangeOption(values: Array<OptionDataValue?>): MatrixCoordRangeOption =
    values.unsafeCast<MatrixCoordRangeOption>()

/**
 * Parallel coordinate system.
 */
external interface ParallelOption {
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
    var layout: Orient?

    /** Whether the axes can be expanded. */
    var axisExpandable: Boolean?

    /** Index of the axis expanded at first. */
    var axisExpandCenter: Double?

    /** Number of axes shown when expanded. */
    var axisExpandCount: Double?

    /** Width of one axis when expanded. */
    var axisExpandWidth: Double?

    /** Event that expands the axes: `"click"` or `"mousemove"`. */
    var axisExpandTriggerOn: ParallelAxisExpandTriggerOn?

    /** Expansion speed, `0` means no animation. */
    var axisExpandRate: Double?

    /** Debounce delay of the expansion, in milliseconds. */
    var axisExpandDebounce: Double?

    /**
     * Area that triggers the expansion while the pointer moves, as `[left, right, middle]`
     * fractions of the parallel width.
     */
    var axisExpandSlideTriggerArea: Array<Double>?

    /** Range of the expanded window. */
    var axisExpandWindow: Array<Double>?

    /** Index of the parallel axes used by this coordinate system. */
    var parallelAxisDefault: ParallelAxisOption?
}
