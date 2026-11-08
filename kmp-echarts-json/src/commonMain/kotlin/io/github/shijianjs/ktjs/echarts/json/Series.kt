package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/**
 * One `series` entry.
 *
 * ECharts models every series type as a separate TypeScript union member, but they all share one
 * JSON object with a `type` discriminator and mostly disjoint optional properties. This class
 * follows that shape: [type] is required, every other property is optional, and properties that
 * belong to another series type are simply ignored by ECharts. The DSL's per-type builders select
 * the matching [SeriesType] for you.
 */
@Serializable
public class SeriesOption(
    /** Discriminator: see [SeriesType] for the bundled values. */
    public var type: SeriesType,
) {
    /** Component id used by cross references. */
    public var id: String? = null

    /** Series name shown in the legend and tooltip. */
    public var name: String? = null

    /** Whether colors follow the data (`"data"`) or the series (`"series"`). */
    public var colorBy: String? = null

    /** Whether hovering a legend entry highlights this series. */
    public var legendHoverLink: Boolean? = null

    /** Coordinate system: `"cartesian2d"`, `"polar"`, `"geo"`, `"calendar"`, `"singleAxis"`, `"parallel"` or `"none"`. */
    public var coordinateSystem: String? = null

    /** Index of the x axis. */
    public var xAxisIndex: Int? = null

    /** Index of the y axis. */
    public var yAxisIndex: Int? = null

    /** Index of the polar coordinate system. */
    public var polarIndex: Int? = null

    /** Index of the radius axis. */
    public var radiusAxisIndex: Int? = null

    /** Index of the angle axis. */
    public var angleAxisIndex: Int? = null

    /** Index of the radar coordinate system. */
    public var radarIndex: Int? = null

    /** Index of the geo coordinate system. */
    public var geoIndex: Int? = null

    /** Index of the calendar coordinate system. */
    public var calendarIndex: Int? = null

    /** Index of the parallel coordinate system. */
    public var parallelIndex: Int? = null

    /** Index of the single axis. */
    public var singleAxisIndex: Int? = null

    /** Index of the dataset that provides the data. */
    public var datasetIndex: Int? = null

    /** Id of the dataset that provides the data. */
    public var datasetId: String? = null

    /** Series data. */
    public var data: List<DataValue>? = null

    /** Dimension names or definitions; only needed for datasets or custom series. */
    public var dimensions: List<JsonElement>? = null

    /** Mapping from visual channels to data dimensions, for example `{"x": 0, "y": 1}`. */
    public var encode: JsonObject? = null

    /** Whether dataset rows or columns are series: `"row"` or `"column"`. */
    public var seriesLayoutBy: String? = null

    /** Links of a `graph` or `sankey` series. */
    public var links: List<DataValue>? = null

    /** Extra nodes of a `graph` series; the data itself is also a node list. */
    public var nodes: List<DataValue>? = null

    /** Categories of a `graph` series. */
    public var categories: List<DataItem>? = null

    /** Hierarchy levels of `tree`, `treemap`, `sunburst` or `sankey`. */
    public var levels: List<JsonElement>? = null

    /** Data group used to animate drill-down changes. */
    public var dataGroupId: String? = null

    // Layout and shape -----------------------------------------------------

    /** Layout algorithm: `"force"`, `"circular"`, `"none"`, `"orthogonal"` or `"radial"`. */
    public var layout: String? = null

    /** Center of a pie, gauge or radar as `[x, y]`. */
    public var center: List<NumberOrString>? = null

    /** Radius as a number, a percentage string or `[inner, outer]`. */
    public var radius: NumberOrStringOrNumbers? = null

    /** Start angle of a pie or gauge, in degrees. */
    public var startAngle: Double? = null

    /** End angle of a pie; `"auto"` is not representable, omit it for the default. */
    public var endAngle: Double? = null

    /** Whether a pie or gauge runs clockwise. */
    public var clockwise: Boolean? = null

    /** Rose type of a pie: `false`, `"radius"` or `"area"`. */
    public var roseType: BooleanOrString? = null

    /** Minimum angle of a pie slice, in degrees. */
    public var minAngle: Double? = null

    /** Gap between pie slices, in degrees. */
    public var padAngle: Double? = null

    /** Minimum angle below which labels are hidden. */
    public var minShowLabelAngle: Double? = null

    /** Whether pie labels avoid overlapping each other. */
    public var avoidLabelOverlap: Boolean? = null

    /** Whether a pie still shows a zero-value slice with the minimum angle. */
    public var stillShowZeroSum: Boolean? = null

    /** Number of decimals of pie percentages. */
    public var percentPrecision: Int? = null

    /** Selection mode of a pie, map or graph: `true`, `false`, `"single"` or `"multiple"`. */
    public var selectedMode: BooleanOrString? = null

    /** Offset of a selected pie slice. */
    public var selectedOffset: Double? = null

    /** Orientation of a tree or sankey series. */
    public var orient: Orient? = null

    /** Distance to the top edge of a tree. */
    public var top: NumberOrString? = null

    /** Distance to the left edge of a tree. */
    public var left: NumberOrString? = null

    /** Distance to the right edge of a tree. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge of a tree. */
    public var bottom: NumberOrString? = null

    /** Width of a tree. */
    public var width: NumberOrString? = null

    /** Height of a tree. */
    public var height: NumberOrString? = null

    // Line, bar and symbol styling ----------------------------------------

    /** Smoothness of a line; `true`, `false` or a factor between 0 and 1. */
    public var smooth: BooleanOrNumber? = null

    /** How smoothed lines stay monotonic: `"none"` or `"x"`. */
    public var smoothMonotonicity: String? = null

    /** Step line: `true`, `"start"`, `"middle"` or `"end"`. */
    public var step: BooleanOrString? = null

    /** Whether null values are connected instead of leaving a gap. */
    public var connectNulls: Boolean? = null

    /** Whether the series is clipped to the coordinate system. */
    public var clip: Boolean? = null

    /** Downsampling method: `"lttb"`, `"average"`, `"min"`, `"max"`, `"minmax"` or `"sum"`. */
    public var sampling: String? = null

    /** Stack group; series in the same group share a baseline. */
    public var stack: String? = null

    /** Stack strategy: `"samesign"`, `"all"` or `"positive"`. */
    public var stackStrategy: String? = null

    /** Whether symbols are shown. */
    public var showSymbol: Boolean? = null

    /** Whether every symbol is shown; `"auto"` hides symbols that would overlap. */
    public var showAllSymbol: BooleanOrString? = null

    /** Symbol name, for example `"circle"`, `"rect"`, `"emptyCircle"` or an image URL via `"image://"`. */
    public var symbol: String? = null

    /** Symbol size, a number or `[width, height]`. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Symbol rotation in degrees. */
    public var symbolRotate: Double? = null

    /** Whether the symbol keeps its aspect ratio. */
    public var symbolKeepAspect: Boolean? = null

    /** Symbol offset as `[x, y]`. */
    public var symbolOffset: List<NumberOrString>? = null

    /** Line style. */
    public var lineStyle: LineStyleOption? = null

    /** Area fill below a line series. */
    public var areaStyle: AreaStyleOption? = null

    /** End label of a line series. */
    public var endLabel: LabelOption? = null

    /** Bar width, a number or a percentage string. */
    public var barWidth: NumberOrString? = null

    /** Maximum bar width. */
    public var barMaxWidth: NumberOrString? = null

    /** Minimum bar width. */
    public var barMinWidth: NumberOrString? = null

    /** Gap between bars of one category, as a percentage. */
    public var barGap: String? = null

    /** Gap between categories, as a percentage or a fixed size. */
    public var barCategoryGap: String? = null

    /** Minimum bar height in pixels. */
    public var barMinHeight: Double? = null

    /** Whether bar ends are rounded. */
    public var roundCap: Boolean? = null

    /** Whether the bar background is shown. */
    public var showBackground: Boolean? = null

    /** Background style of a bar series. */
    public var backgroundStyle: ItemStyleOption? = null

    /** Whether bars re-sort while their values change. */
    public var realtimeSort: Boolean? = null

    /** Position of a pictorial bar symbol: `"start"`, `"end"` or `"middle"`. */
    public var symbolPosition: String? = null

    /** Pictorial bar repetition: a boolean, a count or the `"fixed"` keyword. */
    public var symbolRepeat: BooleanOrNumberOrString? = null

    /** Repetition direction: `"start"` or `"end"`. */
    public var symbolRepeatDirection: String? = null

    /** Margin between repeated symbols, a number or a percentage. */
    public var symbolMargin: NumberOrString? = null

    /** Whether the series shape clips the repeated symbols. */
    public var symbolClip: Boolean? = null

    /** Data value the symbols are sized against, one bound or a `[lower, upper]` pair. */
    public var symbolBoundingData: NumberOrNumbers? = null

    /** Size of one tile of the symbol pattern. */
    public var symbolPatternSize: Double? = null

    // Scatter and effect ---------------------------------------------------

    /** Whether large data sets use an optimized path. */
    public var large: Boolean? = null

    /** Data size above which the large path is used. */
    public var largeThreshold: Double? = null

    /** Progressive rendering chunk size; `false` disables it. */
    public var progressive: BooleanOrNumber? = null

    /** Data size above which progressive rendering starts. */
    public var progressiveThreshold: Double? = null

    /** Progressive chunk mode: `"mod"` or `"sequential"`. */
    public var progressiveChunkMode: String? = null

    /** Ripple animation of an `effectScatter` series. */
    public var rippleEffect: RippleEffectOption? = null

    /** When the ripple shows: `"render"` or `"emphasis"`. */
    public var showEffectOn: String? = null

    /** Whether emphasis enlarges the symbol. */
    public var hoverAnimation: Boolean? = null

    // Graph ----------------------------------------------------------------

    /** Pan and zoom behavior: `true`, `false`, `"scale"` or `"move"`. */
    public var roam: BooleanOrString? = null

    /** Whether nodes can be dragged. */
    public var draggable: Boolean? = null

    /** Node size relative to the value range. */
    public var nodeScaleRatio: Double? = null

    /** Symbols at both ends of an edge, as `[start, end]`. */
    public var edgeSymbol: List<String>? = null

    /** Size of the edge symbols. */
    public var edgeSymbolSize: NumberOrStringOrNumbers? = null

    /** Edge label. */
    public var edgeLabel: LabelOption? = null

    /** Force layout parameters. */
    public var force: ForceOption? = null

    /** Circular layout parameters; `{"rotateLabel": true}` is the usual value. */
    public var circular: JsonElement? = null

    /** Action on node click: `false`, `"zoomToNode"` or `"link"`. */
    public var nodeClick: BooleanOrString? = null

    /** Curve factor of graph edges: a boolean or a factor. */
    public var autoCurveness: BooleanOrNumber? = null

    /** Edge shape: `"line"` or `"curve"`. */
    public var edgeShape: String? = null

    // Tree and hierarchy ---------------------------------------------------

    /** Initial depth of an expanded tree; `-1` expands everything. */
    public var initialTreeDepth: Int? = null

    /** Whether nodes expand and collapse on click. */
    public var expandAndCollapse: Boolean? = null

    /** Label of tree leaf nodes. */
    public var leaves: LabelOption? = null

    /** Depth at which a treemap becomes non-interactive. */
    public var leafDepth: Int? = null

    /** Minimum area of a visible treemap node. */
    public var visibleMin: Double? = null

    /** Minimum area of visible treemap children. */
    public var childrenVisibleMin: Double? = null

    /** Aspect ratio of treemap tiles. */
    public var squareRatio: Double? = null

    /** Zoom ratio applied when a treemap node is clicked. */
    public var zoomToNodeRatio: Double? = null

    /** Label of treemap nodes above the leaves. */
    public var upperLabel: LabelOption? = null

    /** Breadcrumb navigation; a boolean or an options object. */
    public var breadcrumb: JsonElement? = null

    /** Sorting of treemap, sunburst and funnel items: a boolean or `"asc"` / `"desc"`. */
    public var sort: BooleanOrString? = null

    // Sankey and gauge -----------------------------------------------------

    /** Width of a sankey node. */
    public var nodeWidth: Double? = null

    /** Gap between sankey nodes. */
    public var nodeGap: Double? = null

    /** Sankey node alignment: `"justify"`, `"left"` or `"right"`. */
    public var nodeAlign: String? = null

    /** Number of iterations of the sankey layout. */
    public var layoutIterations: Int? = null

    /** Gauge minimum value. */
    public var min: Double? = null

    /** Gauge or funnel maximum value. */
    public var max: Double? = null

    /** Number of gauge segments. */
    public var splitNumber: Int? = null

    /** Gauge axis line. */
    public var axisLine: AxisLineOption? = null

    /** Gauge axis tick marks. */
    public var axisTick: AxisTickOption? = null

    /** Gauge split lines. */
    public var splitLine: SplitLineOption? = null

    /** Gauge axis labels. */
    public var axisLabel: AxisLabelOption? = null

    /** Gauge pointer; the shape is a render callback and cannot be expressed as JSON. */
    public var pointer: JsonElement? = null

    /** Gauge anchor; the shape is a render callback and cannot be expressed as JSON. */
    public var anchor: JsonElement? = null

    /** Gauge title. */
    public var title: LabelOption? = null

    /** Gauge value detail. */
    public var detail: LabelOption? = null

    /** Gauge progress bar. */
    public var progress: GaugeProgressOption? = null

    /** Funnel minimum size, a number or a percentage string. */
    public var minSize: NumberOrString? = null

    /** Funnel maximum size, a number or a percentage string. */
    public var maxSize: NumberOrString? = null

    /** Gap between funnel items. */
    public var gap: Double? = null

    /** Funnel alignment: `"left"`, `"right"` or `"center"`. */
    public var funnelAlign: String? = null

    // Heatmap, candlestick, boxplot ---------------------------------------

    /** Size of a heatmap point in pixels. */
    public var pointSize: Double? = null

    /** Blur size of a heatmap point. */
    public var blurSize: Double? = null

    /** Minimum opacity of heatmap values. */
    public var minOpacity: Double? = null

    /** Maximum opacity of heatmap values. */
    public var maxOpacity: Double? = null

    /** Width of a candlestick or boxplot item, a number or `[width]`. */
    public var boxWidth: NumberOrStringOrNumbers? = null

    /** Gap between boxplot items. */
    public var boxGap: NumberOrString? = null

    // Map ------------------------------------------------------------------

    /** Name of the map registered with `echarts.registerMap`. */
    public var map: String? = null

    /** Map from source map names to display names. */
    public var nameMap: Map<String, String>? = null

    /** Minimum and maximum zoom. */
    public var scaleLimit: ScaleLimit? = null

    /** Per-region overrides. */
    public var regions: List<GeoRegion>? = null

    /** Aspect ratio applied to the map. */
    public var aspectScale: Double? = null

    /** Bounding box as `[[west, south], [east, north]]`. */
    public var boundingCoords: List<List<Double>>? = null

    /** Layout center as `[x, y]` percentages. */
    public var layoutCenter: List<NumberOrString>? = null

    /** Layout size used together with [layoutCenter]. */
    public var layoutSize: NumberOrString? = null

    // Lines ----------------------------------------------------------------

    /** Whether a `lines` series is drawn as a polyline. */
    public var polyline: Boolean? = null

    /** Moving effect of a `lines` series. */
    public var effect: LinesEffectOption? = null

    // Shared states and animation -----------------------------------------

    /** Item style. */
    public var itemStyle: ItemStyleOption? = null

    /** Label. */
    public var label: LabelOption? = null

    /** Connector line of a pie or funnel label. */
    public var labelLine: LabelLineOption? = null

    /** Label layout options; the callback form cannot be expressed as JSON. */
    public var labelLayout: JsonElement? = null

    /** Emphasis state. */
    public var emphasis: EmphasisOption? = null

    /** Blur state, used together with `emphasis.focus`. */
    public var blur: EmphasisOption? = null

    /** Select state. */
    public var select: EmphasisOption? = null

    /** `markPoint` markers. */
    public var markPoint: MarkPointOption? = null

    /** `markLine` markers. */
    public var markLine: MarkLineOption? = null

    /** `markArea` markers. */
    public var markArea: MarkAreaOption? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Whether the series ignores mouse events. */
    public var silent: Boolean? = null

    /** Cursor shown over the series. */
    public var cursor: String? = null

    /** Blend mode of the series. */
    public var blendMode: String? = null

    /** Tooltip limited to this series; overrides the top-level tooltip. */
    public var tooltip: TooltipOption? = null

    /** Universal transition between chart types; the callback form cannot be expressed as JSON. */
    public var universalTransition: JsonElement? = null

    /** State transition animation. */
    public var stateAnimation: JsonElement? = null

    /** Whether series animations are enabled. */
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
}

/** Progress bar of a gauge series. */
@Serializable
public class GaugeProgressOption {
    /** Whether the progress bar is shown. */
    public var show: Boolean? = null

    /** Width of the progress bar. */
    public var width: Double? = null

    /** Whether the ends of the progress bar are rounded. */
    public var roundCap: Boolean? = null

    /** Whether progress bars of several data items may overlap instead of being clipped. */
    public var overlap: Boolean? = null

    /** Whether the progress bar is clipped to the axis line. */
    public var clip: Boolean? = null

    /** Progress bar style. */
    public var itemStyle: ItemStyleOption? = null
}

/** Moving effect of a `lines` series. */
@Serializable
public class LinesEffectOption {
    /** Whether the effect is shown. */
    public var show: Boolean? = null

    /** Animation period in seconds. */
    public var period: Double? = null

    /** Trail length, `0..1`. */
    public var trailLength: Double? = null

    /** Symbol of the moving point. */
    public var symbol: String? = null

    /** Size of the moving symbol. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Color of the moving point. */
    public var color: EChartsColor? = null

    /** Whether the point moves at a constant speed instead of over a fixed period. */
    public var constantSpeed: Double? = null
}
