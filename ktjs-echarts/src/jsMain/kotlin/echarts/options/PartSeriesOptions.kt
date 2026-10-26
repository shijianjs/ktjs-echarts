package echarts.options

import echarts.RoseType
import js.objects.Record
import kotlin.js.unsafeCast
import web.canvas.CanvasLineCap
import web.canvas.CanvasLineJoin
import zrender.ZRColor
import zrender.ZRLineType

/*
 * Series that are neither cartesian, hierarchical nor relational: pie, funnel, gauge, radar, map,
 * theme river and pictorial bar.
 */

/**
 * Pie series.
 */
external interface PieSeriesOption : SeriesOption {
    /** Coordinate system, always `"none"` for pie charts. */
    override var coordinateSystem: String?

    /** Radius of the pie, or `[inner, outer]` for a doughnut chart. */
    var radius: StringOrDoubleOrArray?

    /** Center of the pie, `[x, y]`. */
    var center: Array<StringOrDouble>?

    /** Whether the sectors are drawn as roses: `"radius"` or `"area"`. */
    var roseType: RoseType?

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
    var radius: StringOrDouble?

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
 * A color stop of the axis line of a gauge: upstream declares the tuple `[offset, color]`, where
 * the offset is a ratio between `0` and `1`.
 *
 * Because the value is a JavaScript array, only the [gaugeColorStop] factory creates one.
 */
external interface GaugeColorStop

/**
 * Creates a `[offset, color]` color stop for [GaugeAxisLineStyleOption.color].
 *
 * @param offset position of the stop between `0` and `1`.
 * @param color CSS color at that position.
 */
fun gaugeColorStop(offset: Double, color: String): GaugeColorStop =
    arrayOf<Any>(offset, color).unsafeCast<GaugeColorStop>()

/**
 * Style of the axis line of a gauge.
 *
 * Upstream declares `Omit<LineStyleOption, 'color'> & { color?: GaugeColorStop[] }`, so the color
 * is a list of `[offset, color]` stops instead of a single color; the other members are the ones
 * of [LineStyleOption].
 */
external interface GaugeAxisLineStyleOption : ShadowOption {
    /** Color stops of the axis line. */
    var color: Array<GaugeColorStop>?

    /** Line width in pixels. */
    var width: Double?

    /** Line type: `"solid"`, `"dashed"`, `"dotted"`, a number or a `[dash, gap]` array. */
    var type: ZRLineType?

    /** Dash offset. */
    var dashOffset: Double?

    /** Line cap of the line. */
    var cap: CanvasLineCap?

    /** Line join of the line. */
    var join: CanvasLineJoin?

    /** Miter limit for `join = "miter"`. */
    var miterLimit: Double?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Curvature of the line. */
    var curveness: Double?
}

/**
 * Axis line of a gauge series.
 */
external interface GaugeAxisLineOption {
    /** Whether the axis line is shown. */
    var show: Boolean?

    /** Whether to round the line caps. */
    var roundCap: Boolean?

    /** Style of the axis line. */
    var lineStyle: GaugeAxisLineStyleOption?
}

/**
 * Progress arc of a gauge series.
 */
external interface GaugeProgressOption {
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
external interface GaugePointerOption {
    /** Whether the pointer is shown. */
    var show: Boolean?

    /** Icon of the pointer. */
    var icon: String?

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
external interface GaugeAnchorOption {
    /** Whether the anchor is shown. */
    var show: Boolean?

    /** Whether to show the anchor when the value is above zero. */
    var showAbove: Boolean?

    /** Size of the anchor. */
    var size: Double?

    /** Icon of the anchor. */
    var icon: String?

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
external interface GaugeAxisTickOption {
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
external interface GaugeSplitLineOption {
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
    var symbolOffset: SymbolOffset?

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
    var center: Array<StringOrDouble>?

    /** Initial zoom level. */
    var zoom: Double?

    /** Zoom limits. */
    var scaleLimit: ScaleLimitOption?

    /** Mapping of the names in the map data to display names. */
    var nameMap: Record<String, String>?

    /** Selection mode: `false`, `"single"` or `"multiple"`. */
    var selectedMode: BooleanOrString?

    /** Style of the labels. */
    override var label: LabelOption?

    /** Per-region configuration. */
    var regions: Array<RegionOption>?

    /** Index of the geo coordinate system, when the map is drawn by geo. */
    var geoIndex: Double?

    /** How multiple values are combined: `"sum"` or `"average"`. */
    var mapValueCalculation: String?

    /** Whether to show the legend symbol. */
    var showLegendSymbol: Boolean?
}

/**
 * Theme river series.
 */
external interface ThemeRiverSeriesOption : SeriesOption {
    /** Color palette of the river bands. */
    var color: Array<ZRColor>?

    /** Coordinate system: `"singleAxis"`. */
    override var coordinateSystem: String?

    /** Whether the axis has gaps at both ends. */
    var boundaryGap: Array<StringOrDouble>?

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
    var symbolOffset: SymbolOffset?

    /** Symbol rotation in degrees. */
    var symbolRotate: DoubleOrDoubleArray?

    /** Whether to repeat the symbol along the bar. */
    var symbolRepeat: BooleanOrDoubleOrString?

    /** Direction of the repetition: `"start"` or `"end"`. */
    var symbolRepeatDirection: String?

    /** Margin between repeated symbols. */
    var symbolMargin: StringOrDoubleOrArray?

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
