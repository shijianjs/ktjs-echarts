package echarts.options

import kotlin.js.JsAny

/**
 * Pie series.
 */
external interface PieSeriesOption : SeriesOption {
    /** Coordinate system, always `"none"` for pie charts. */
    override var coordinateSystem: String?

    /** Radius of the pie, or `[inner, outer]` for a doughnut chart. */
    var radius: JsAny?

    /** Center of the pie, `[x, y]`. */
    var center: Array<StringOrDouble>?

    /** Rose type: `false`, `"radius"` or `"area"`. */
    var roseType: JsAny?

    /** Start angle in degrees. */
    var startAngle: Double?

    /** End angle in degrees. */
    var endAngle: Double?

    /** Minimum angle of a sector in degrees. */
    var minAngle: Double?

    /** Minimum angle for the label to be shown, in degrees. */
    var minShowLabelAngle: Double?

    /** Whether to avoid label overlaps. */
    var avoidLabelOverlap: Boolean?

    /** Whether to keep the sectors of zero values. */
    var stillShowZeroSum: Boolean?

    /** Precision of the percentage values. */
    var percentPrecision: Double?

    /** Selection mode: `false`, `"single"` or `"multiple"`. */
    var selectedMode: BooleanOrString?

    /** Offset of a selected sector in pixels. */
    var selectedOffset: Double?

    /** Whether the sectors are drawn clockwise. */
    var clockwise: Boolean?

    /** Padding angle between sectors, in degrees. */
    var padAngle: Double?

    /** Whether to show an empty circle when there is no data. */
    var showEmptyCircle: Boolean?

    /** Style of the empty circle. */
    var emptyCircleStyle: ItemStyleOption?

    /** Style of the labels. */
    override var label: LabelOption?

    /** Style of the label lines. */
    override var labelLine: LabelLineOption?
}

/**
 * Funnel series.
 */
external interface FunnelSeriesOption : SeriesOption {
    /** Minimum value of the funnel. */
    var min: Double?

    /** Maximum value of the funnel. */
    var max: Double?

    /** Minimum size of the funnel. */
    var minSize: StringOrDouble?

    /** Maximum size of the funnel. */
    var maxSize: StringOrDouble?

    /** Sort order: `"ascending"`, `"descending"` or `"none"`. */
    var sort: String?

    /** Gap between funnel items in pixels. */
    var gap: Double?

    /** Alignment of the funnel: `"left"`, `"center"` or `"right"`. */
    var funnelAlign: String?

    /** Style of the labels. */
    override var label: LabelOption?

    /** Style of the label lines. */
    override var labelLine: LabelLineOption?
}

/**
 * Gauge series.
 */
external interface GaugeSeriesOption : SeriesOption {
    /** Radius of the gauge, or `[inner, outer]`. */
    var radius: JsAny?

    /** Center of the gauge, `[x, y]`. */
    var center: Array<StringOrDouble>?

    /** Start angle in degrees. */
    var startAngle: Double?

    /** End angle in degrees. */
    var endAngle: Double?

    /** Minimum value. */
    var min: Double?

    /** Maximum value. */
    var max: Double?

    /** Number of split segments. */
    var splitNumber: Double?

    /** Configuration of the axis line. */
    var axisLine: GaugeAxisLineOption?

    /** Configuration of the progress arc. */
    var progress: GaugeProgressOption?

    /** Configuration of the pointer. */
    var pointer: GaugePointerOption?

    /** Configuration of the anchor of the pointer. */
    var anchor: GaugeAnchorOption?

    /** Configuration of the axis ticks. */
    var axisTick: GaugeAxisTickOption?

    /** Configuration of the split lines. */
    var splitLine: GaugeSplitLineOption?

    /** Configuration of the axis labels. */
    var axisLabel: LabelOption?

    /** Configuration of the title. */
    var title: LabelOption?

    /** Configuration of the detail text. */
    var detail: LabelOption?

    /** Precision of the displayed value. */
    var precision: Double?

    /** Whether the gauge is silent. */
    override var silent: Boolean?
}

/**
 * Axis line of a gauge series.
 */
external interface GaugeAxisLineOption : JsAny {
    /** Whether the axis line is shown. */
    var show: Boolean?

    /** Whether to round the line caps. */
    var roundCap: Boolean?

    /** Style of the axis line. */
    var lineStyle: JsAny?
}

/**
 * Progress arc of a gauge series.
 */
external interface GaugeProgressOption : JsAny {
    /** Whether the progress arc is shown. */
    var show: Boolean?

    /** Whether to overlap the progress with the axis line. */
    var overlap: Boolean?

    /** Width of the progress arc. */
    var width: Double?

    /** Whether the progress ends are rounded. */
    var roundCap: Boolean?

    /** Whether to clip the progress inside the axis line. */
    var clip: Boolean?

    /** Style of the progress arc. */
    var itemStyle: ItemStyleOption?
}

/**
 * Pointer of a gauge series.
 */
external interface GaugePointerOption : JsAny {
    /** Whether the pointer is shown. */
    var show: Boolean?

    /** Icon of the pointer. */
    var icon: JsAny?

    /** Length of the pointer. */
    var length: StringOrDouble?

    /** Width of the pointer. */
    var width: Double?

    /** Whether the pointer keeps its aspect ratio. */
    var keepAspect: Boolean?

    /** Offset of the pointer, `[x, y]`. */
    var offsetCenter: Array<StringOrDouble>?

    /** Style of the pointer. */
    var itemStyle: ItemStyleOption?
}

/**
 * Anchor of a gauge pointer.
 */
external interface GaugeAnchorOption : JsAny {
    /** Whether the anchor is shown. */
    var show: Boolean?

    /** Whether to show the anchor when the value is above zero. */
    var showAbove: Boolean?

    /** Size of the anchor. */
    var size: Double?

    /** Icon of the anchor. */
    var icon: JsAny?

    /** Offset of the anchor, `[x, y]`. */
    var offsetCenter: Array<StringOrDouble>?

    /** Whether the anchor keeps its aspect ratio. */
    var keepAspect: Boolean?

    /** Style of the anchor. */
    var itemStyle: ItemStyleOption?
}

/**
 * Axis ticks of a gauge series.
 */
external interface GaugeAxisTickOption : JsAny {
    /** Whether the ticks are shown. */
    var show: Boolean?

    /** Distance from the axis line in pixels. */
    var distance: Double?

    /** Length of the ticks in pixels. */
    var length: Double?

    /** Style of the ticks. */
    var lineStyle: LineStyleOption?

    /** Interval between two ticks. */
    var splitNumber: Double?
}

/**
 * Split lines of a gauge series.
 */
external interface GaugeSplitLineOption : JsAny {
    /** Whether the split lines are shown. */
    var show: Boolean?

    /** Length of the split lines in pixels. */
    var length: Double?

    /** Distance from the axis line in pixels. */
    var distance: Double?

    /** Style of the split lines. */
    var lineStyle: LineStyleOption?
}

/**
 * Radar series.
 */
external interface RadarSeriesOption : SeriesOption {
    /** Index of the radar coordinate system. */
    var radarIndex: Double?

    /** Symbol shape. */
    var symbol: String?

    /** Symbol size. */
    var symbolSize: DoubleOrDoubleArray?

    /** Symbol rotation in degrees. */
    var symbolRotate: Double?

    /** Whether to keep the symbol aspect ratio. */
    var symbolKeepAspect: Boolean?

    /** Symbol offset, `[x, y]`. */
    var symbolOffset: Array<JsAny>?

    /** How the data maps to the radar axes. */
    var seriesLayoutBy: String?
}

/**
 * Map series.
 */
external interface MapSeriesOption : SeriesOption {
    /** Name of the registered map. */
    var map: String?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Center of the map. */
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
    override var label: LabelOption?

    /** Per-region configuration. */
    var regions: Array<JsAny>?

    /** Index of the geo coordinate system, when the map is drawn by geo. */
    var geoIndex: Double?

    /** How multiple values are combined: `"sum"` or `"average"`. */
    var mapValueCalculation: String?

    /** Whether to show the legend symbol. */
    var showLegendSymbol: Boolean?
}

/**
 * Graph series: a node-link diagram.
 */
external interface GraphSeriesOption : SeriesOption {
    /** Layout: `"none"`, `"circular"` or `"force"`. */
    var layout: String?

    /** Whether the layout animates. */
    var layoutAnimation: Boolean?

    /** Configuration of the force layout. */
    var force: JsAny?

    /** Configuration of the circular layout. */
    var circular: JsAny?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Center of the graph. */
    var center: Array<JsAny>?

    /** Initial zoom level. */
    var zoom: Double?

    /** Zoom limits. */
    var scaleLimit: JsAny?

    /** Whether the nodes can be dragged. */
    var draggable: Boolean?

    /** Symbol shape of the nodes. */
    var symbol: String?

    /** Symbol size of the nodes. */
    var symbolSize: DoubleOrDoubleArray?

    /** Symbol rotation in degrees. */
    var symbolRotate: Double?

    /** Whether to keep the symbol aspect ratio. */
    var symbolKeepAspect: Boolean?

    /** Symbol offset, `[x, y]`. */
    var symbolOffset: Array<JsAny>?

    /** Symbol of the edges. */
    var edgeSymbol: Array<String>?

    /** Symbol size of the edges. */
    var edgeSymbolSize: DoubleOrDoubleArray?

    /** Label of the edges. */
    var edgeLabel: LabelOption?

    /** Categories of the nodes. */
    var categories: Array<JsAny>?

    /** Edges of the graph. */
    var links: Array<JsAny>?

    /** Whether the edges are directed. */
    var directed: Boolean?

    /** Curvature of the edges. */
    var autoCurveness: BooleanOrDouble?

    /** Shape of the edges: `"line"` or `"curve"`. */
    var edgeShape: String?

    /** Position of the fork in a `"curve"` edge. */
    var edgeForkPosition: String?

    /** Scale ratio of the nodes. */
    var nodeScaleRatio: Double?

    /** Whether to highlight the adjacent nodes on hover. */
    var focusNodeAdjacency: Boolean?

    /** Selection mode: `false`, `"single"` or `"multiple"`. */
    var selectedMode: BooleanOrString?
}

/**
 * Tree series.
 */
external interface TreeSeriesOption : SeriesOption {
    /** Layout: `"orthogonal"` or `"radial"`. */
    var layout: String?

    /** Orientation of an orthogonal tree: `"LR"`, `"RL"`, `"TB"` or `"BT"`. */
    var orient: String?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the tree. */
    var width: StringOrDouble?

    /** Height of the tree. */
    var height: StringOrDouble?

    /** Symbol shape of the tree nodes. */
    var symbol: String?

    /** Symbol size of the tree nodes. */
    var symbolSize: DoubleOrDoubleArray?

    /** Symbol rotation in degrees. */
    var symbolRotate: Double?

    /** Whether to keep the symbol aspect ratio. */
    var symbolKeepAspect: Boolean?

    /** Symbol offset, `[x, y]`. */
    var symbolOffset: Array<JsAny>?

    /** Depth of the tree that is expanded initially. */
    var initialTreeDepth: Double?

    /** Label layout of the nodes. */
    var labelLayout: JsAny?

    /** Whether to show the leaves. */
    var leaves: JsAny?

    /** Whether to expand or collapse a node on click. */
    var expandAndCollapse: Boolean?

    /** Duration of the layout animation. */
    override var animationDuration: Double?

    /** Easing of the layout animation. */
    override var animationEasing: String?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Center of the tree. */
    var center: Array<JsAny>?

    /** Initial zoom level. */
    var zoom: Double?

    /** Zoom limits. */
    var scaleLimit: JsAny?

    /** Shape of the edges: `"curve"`, `"polyline"` or `"line"`. */
    var edgeShape: String?

    /** Position of the fork in a `"curve"` edge. */
    var edgeForkPosition: String?
}

/**
 * Treemap series.
 */
external interface TreemapSeriesOption : SeriesOption {
    /** Ratio between the width and the height of the tiles. */
    var squareRatio: Double?

    /** Depth of the tree that can be drilled down. */
    var leafDepth: Double?

    /** Icon of the drill-down button. */
    var drillDownIcon: String?

    /** Whether clicking a node drills down. */
    var nodeClick: BooleanOrString?

    /** Ratio of the zoom after a drill-down. */
    var zoomToNodeRatio: Double?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Gap between nodes in pixels. */
    var nodeGap: Double?

    /** Width of the nodes. */
    var nodeWidth: Double?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the treemap. */
    var width: StringOrDouble?

    /** Height of the treemap. */
    var height: StringOrDouble?

    /** Breadcrumb navigation. */
    var breadcrumb: JsAny?

    /** Style of the labels. */
    override var label: LabelOption?

    /** Style of the labels of parent nodes. */
    var upperLabel: LabelOption?

    /** Per-level configuration. */
    var levels: Array<JsAny>?

    /** Dimension used for the color mapping. */
    var visualDimension: Double?

    /** Minimum value of the color mapping. */
    var visualMin: Double?

    /** Maximum value of the color mapping. */
    var visualMax: Double?

    /** How the color is mapped: `"value"`, `"index"` or `"id"`. */
    var colorMappingBy: String?

    /** Minimum area for a node to be visible. */
    var visibleMin: Double?

    /** Minimum area for the children of a node to be visible. */
    var childrenVisibleMin: Double?

    /** Saturation of the color mapping. */
    var colorSaturation: JsAny?

    /** Alpha of the color mapping. */
    var colorAlpha: JsAny?
}

/**
 * Sunburst series.
 */
external interface SunburstSeriesOption : SeriesOption {
    /** Center of the sunburst. */
    var center: Array<StringOrDouble>?

    /** Radius of the sunburst, or `[inner, outer]`. */
    var radius: JsAny?

    /** Sort order of the sectors: `"desc"`, `"asc"` or `null`. */
    var sort: JsAny?

    /** Start angle in degrees. */
    var startAngle: Double?

    /** End angle in degrees. */
    var endAngle: Double?

    /** Minimum angle of a sector in degrees. */
    var minAngle: Double?

    /** Minimum angle for the label to be shown, in degrees. */
    var minShowLabelAngle: Double?

    /** Whether clicking a node drills down. */
    var nodeClick: BooleanOrString?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Gap between nodes in pixels. */
    var nodeGap: Double?

    /** Width of the nodes. */
    var nodeWidth: Double?

    /** Per-level configuration. */
    var levels: Array<JsAny>?

    /** Precision of the percentage values. */
    var percentPrecision: Double?
}

/**
 * Sankey series.
 */
external interface SankeySeriesOption : SeriesOption {
    /** Width of the nodes. */
    var nodeWidth: Double?

    /** Gap between nodes in pixels. */
    var nodeGap: Double?

    /** Alignment of the nodes: `"justify"`, `"left"` or `"right"`. */
    var nodeAlign: String?

    /** Number of layout iterations. */
    var layoutIterations: Double?

    /** Orientation: `"horizontal"` or `"vertical"`. */
    var orient: String?

    /** Whether the nodes can be dragged. */
    var draggable: Boolean?

    /** Label of the edges. */
    var edgeLabel: LabelOption?

    /** Per-level configuration. */
    var levels: Array<JsAny>?

    /** Whether to highlight the adjacent nodes on hover. */
    var focusNodeAdjacency: Boolean?

    /** Edges of the graph. */
    var links: Array<JsAny>?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?
}

/**
 * Chord series introduced in ECharts 6.
 */
external interface ChordSeriesOption : SeriesOption {
    /** Width of the nodes. */
    var nodeWidth: Double?

    /** Gap between nodes in pixels. */
    var nodeGap: Double?

    /** Alignment of the nodes: `"justify"`, `"left"` or `"right"`. */
    var nodeAlign: String?

    /** Number of layout iterations. */
    var layoutIterations: Double?

    /** Orientation: `"horizontal"` or `"vertical"`. */
    var orient: String?

    /** Whether the nodes can be dragged. */
    var draggable: Boolean?

    /** Center of the chord diagram. */
    var center: Array<StringOrDouble>?

    /** Radius of the chord diagram. */
    var radius: StringOrDouble?

    /** Start angle in degrees. */
    var startAngle: Double?

    /** End angle in degrees. */
    var endAngle: Double?

    /** Minimum angle of a sector in degrees. */
    var minAngle: Double?

    /** Padding angle between sectors, in degrees. */
    var padAngle: Double?

    /** Whether the sectors are drawn clockwise. */
    var clockwise: Boolean?

    /** Label of the edges. */
    var edgeLabel: LabelOption?

    /** Edges of the graph. */
    var links: Array<JsAny>?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?
}

/**
 * Theme river series.
 */
external interface ThemeRiverSeriesOption : SeriesOption {
    /** Color palette of the river bands. */
    var color: Array<JsAny>?

    /** Coordinate system: `"singleAxis"`. */
    override var coordinateSystem: String?

    /** Whether the axis has gaps at both ends. */
    var boundaryGap: Array<String>?

    /** Index of the single axis. */
    var singleAxisIndex: Double?

    /** Whether to show the tooltip. */
    override var tooltip: TooltipOption?
}

/**
 * Pictorial bar series.
 */
external interface PictorialBarSeriesOption : SeriesOption {
    /** Symbol shape of the bars. */
    var symbol: String?

    /** Symbol size. */
    var symbolSize: DoubleOrDoubleArray?

    /** Position of the symbol: `"start"`, `"end"` or `"middle"`. */
    var symbolPosition: String?

    /** Symbol offset, `[x, y]`. */
    var symbolOffset: Array<JsAny>?

    /** Symbol rotation in degrees. */
    var symbolRotate: Double?

    /** Whether to repeat the symbol along the bar. */
    var symbolRepeat: JsAny?

    /** Direction of the repetition: `"start"` or `"end"`. */
    var symbolRepeatDirection: String?

    /** Margin between repeated symbols. */
    var symbolMargin: JsAny?

    /** Whether to clip the symbols to the bar shape. */
    var symbolClip: Boolean?

    /** Bounding value used to size the symbols. */
    var symbolBoundingData: Double?

    /** Size of the symbol pattern. */
    var symbolPatternSize: Double?

    /** Gap between bars of different series. */
    var barGap: String?

    /** Gap between bar categories. */
    var barCategoryGap: String?

    /** Width of the bars. */
    var barWidth: StringOrDouble?

    /** Maximum width of the bars. */
    var barMaxWidth: StringOrDouble?

    /** Minimum width of the bars. */
    var barMinWidth: StringOrDouble?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Whether to clip the bars to the coordinate system. */
    var clip: Boolean?
}
