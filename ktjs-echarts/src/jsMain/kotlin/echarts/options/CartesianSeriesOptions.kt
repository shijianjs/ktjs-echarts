package echarts.options

import echarts.AnimationEasing
import echarts.SeriesType
import kotlin.js.JsAny
import zrender.ZRColor

/**
 * Common configuration of every series.
 *
 * Concrete series interfaces extend this one and add the options that are specific to the series
 * type. Use the factory functions ([lineSeries], [barSeries], ...) to create a series with its
 * `type` field already set.
 */
external interface SeriesOption {
    /** Series id, used by `getOption`, `dispatchAction` and `setOption` merges. */
    var id: String?

    /** Series name shown in the legend and the tooltip. */
    var name: String?

    /** Series type, set by the factory functions such as [lineSeries]. */
    var type: SeriesType?

    /** How the color is assigned: `"series"`, `"data"` or a custom dimension name. */
    var colorBy: String?

    /** Whether the series ignores mouse events. */
    var silent: Boolean?

    /** Whether the series animates. */
    var animation: Boolean?

    /** Number of elements above which animation is disabled. */
    var animationThreshold: Double?

    /** Duration of the initial animation in milliseconds. */
    var animationDuration: Double?

    /** Easing of the initial animation. */
    var animationEasing: AnimationEasing?

    /** Delay of the initial animation in milliseconds. */
    var animationDelay: Double?

    /** Duration of the update animation in milliseconds. */
    var animationDurationUpdate: Double?

    /** Easing of the update animation. */
    var animationEasingUpdate: AnimationEasing?

    /** Delay of the update animation in milliseconds. */
    var animationDelayUpdate: Double?

    /** Emphasis (hover) state. */
    var emphasis: EmphasisOption?

    /** Blur (non-focused) state. */
    var blur: BlurOption?

    /** Select state. */
    var select: SelectOption?

    /** Item style. */
    var itemStyle: ItemStyleOption?

    /** Line style. */
    var lineStyle: LineStyleOption?

    /** Area style. */
    var areaStyle: AreaStyleOption?

    /** Label style. */
    var label: LabelOption?

    /** Label line style. */
    var labelLine: LabelLineOption?

    /** Mark points. */
    var markPoint: MarkPointOption?

    /** Mark lines. */
    var markLine: MarkLineOption?

    /** Mark areas. */
    var markArea: MarkAreaOption?

    /** z-index of the series. */
    var z: Double?

    /** z-level of the series. */
    var zlevel: Double?

    /** Cursor shown when hovering the series. */
    var cursor: String?

    /** Series data: values, `[x, y]` tuples or data item objects. */
    var data: Array<OptionDataItem?>?

    /** Index of the dataset used as the data source. */
    var datasetIndex: Double?

    /** Id of the dataset used as the data source. */
    var datasetId: String?

    /** Mapping between data dimensions and data fields. */
    var encode: OptionEncode?

    /** Dimensions of the data. */
    var dimensions: Array<DimensionDefinitionLoose>?

    /** Tooltip configuration specific to the series. */
    var tooltip: TooltipOption?

    /** Universal transition animation between series types. */
    var universalTransition: BooleanOrUniversalTransitionOption?

    /** Number of elements rendered per frame during progressive rendering. */
    var progressive: Double?

    /** Number of elements above which progressive rendering is used. */
    var progressiveThreshold: Double?

    /** Chunking mode of the progressive rendering: `"sequential"` or `"mod"`. */
    var progressiveChunkMode: String?

    /** Coordinate system used by the series. */
    var coordinateSystem: String?

    /** Whether the series participates in the legend. */
    var legendHoverLink: Boolean?
}

/**
 * Line series.
 */
external interface LineSeriesOption : SeriesOption {
    /** Coordinate system: `"cartesian2d"` or `"polar"`. */
    override var coordinateSystem: String?

    /** Whether to clip the line to the coordinate system. */
    var clip: Boolean?

    /** Step shape: `false`, `"start"`, `"middle"` or `"end"`. */
    var step: BooleanOrString?

    /** Whether to smooth the line; `0..1` controls the strength. */
    var smooth: BooleanOrDouble?

    /** How smoothing handles monotonic data: `"x"`, `"y"` or `"none"`. */
    var smoothMonotone: String?

    /** Whether to connect points with null values. */
    var connectNulls: Boolean?

    /** Whether to show the symbols. */
    var showSymbol: Boolean?

    /** Whether to show all symbols when the data is large. */
    var showAllSymbol: BooleanOrString?

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

    /** Label at the end of the line. */
    var endLabel: LabelOption?

    /** Whether hovering the line triggers events. */
    var triggerLineEvent: Boolean?

    /** Whether hovering the line or the area triggers events: `true`, `"line"` or `"area"`. */
    var triggerEvent: BooleanOrString?

    /** Stack group name. */
    var stack: String?

    /** Downsampling method: `"lttb"`, `"average"`, `"min"`, `"max"`, `"minmax"` or `"sum"`. */
    var sampling: String?

    /** Data size above which [sampling] is applied. */
    var samplingThreshold: Double?

    /** How the data maps to the axes: `"column"` or `"row"`. */
    var seriesLayoutBy: String?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Index of the polar coordinate system. */
    var polarIndex: Double?

    /** Stack strategy: `"samesign"`, `"all"` or `"positive"`. */
    var stackStrategy: String?

    /** Area style; gives the line a filled area. */
    override var areaStyle: AreaStyleOption?

    /** Whether to show the line as a polar bar when on a polar coordinate system. */
    var showBackground: Boolean?
}

/**
 * Bar series.
 */
external interface BarSeriesOption : SeriesOption {
    /** Coordinate system: `"cartesian2d"` or `"polar"`. */
    override var coordinateSystem: String?

    /** Stack group name. */
    var stack: String?

    /** Stack strategy: `"samesign"`, `"all"` or `"positive"`. */
    var stackStrategy: String?

    /** Stack order: `"seriesAsc"` or `"seriesDesc"`. */
    var stackOrder: String?

    /** Width of the bars; a number, a percentage string, or `"auto"`. */
    var barWidth: StringOrDouble?

    /** Maximum width of the bars. */
    var barMaxWidth: StringOrDouble?

    /** Minimum width of the bars. */
    var barMinWidth: StringOrDouble?

    /** Gap between bars of different series. */
    var barGap: String?

    /** Gap between bar categories. */
    var barCategoryGap: String?

    /** Minimum height of a bar. */
    var barMinHeight: Double?

    /** Minimum angle of a bar on a polar coordinate system. */
    var barMinAngle: Double?

    /** Whether to show the background of the bars. */
    var showBackground: Boolean?

    /** Style of the bar background. */
    var backgroundStyle: ItemStyleOption?

    /** Whether the bar ends are rounded. */
    var roundCap: Boolean?

    /** Whether to sort the data in real time. */
    var realtimeSort: Boolean?

    /** How the data maps to the axes: `"column"` or `"row"`. */
    var seriesLayoutBy: String?

    /** Downsampling method. */
    var sampling: String?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Index of the polar coordinate system. */
    var polarIndex: Double?

    /** Whether to enable large-data mode. */
    var large: Boolean?

    /** Data size above which large-data mode is used. */
    var largeThreshold: Double?
}

/**
 * Scatter series.
 */
external interface ScatterSeriesOption : SeriesOption {
    /** Coordinate system: `"cartesian2d"`, `"polar"`, `"geo"`, `"singleAxis"`, `"calendar"`. */
    override var coordinateSystem: String?

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

    /** Whether to enable large-data mode. */
    var large: Boolean?

    /** Data size above which large-data mode is used. */
    var largeThreshold: Double?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Index of the polar coordinate system. */
    var polarIndex: Double?

    /** Index of the geo coordinate system. */
    var geoIndex: Double?

    /** Index of the single axis. */
    var singleAxisIndex: Double?

    /** Index of the calendar coordinate system. */
    var calendarIndex: Double?

    /** How the data maps to the axes. */
    var seriesLayoutBy: String?

    /** Downsampling method. */
    var sampling: String?

    /** Whether to clip the symbols to the coordinate system. */
    var clip: Boolean?
}

/**
 * Effect scatter series: a scatter series with a ripple animation.
 */
external interface EffectScatterSeriesOption : ScatterSeriesOption {
    /** When the ripple effect is shown: `"render"` or `"emphasis"`. */
    var showEffectOn: String?

    /** Ripple effect configuration. */
    var rippleEffect: RippleEffectOption?
}

/**
 * Configuration of the effect scatter ripple.
 */
external interface RippleEffectOption {
    /** Color of the ripple. */
    var color: ZRColor?

    /** Number of ripples. */
    var number: Double?

    /** Duration of one ripple in milliseconds. */
    var period: Double?

    /** Maximum scale of the ripple. */
    var scale: Double?

    /** Brush type of the ripple: `"stroke"` or `"fill"`. */
    var brushType: String?
}

/**
 * Candlestick series.
 */
external interface CandlestickSeriesOption : SeriesOption {
    /** Coordinate system: `"cartesian2d"`. */
    override var coordinateSystem: String?

    /** Layout of the candles: `"horizontal"` or `"vertical"`. */
    var layout: String?

    /** Width of a candle. */
    var barWidth: StringOrDouble?

    /** Maximum width of a candle. */
    var barMaxWidth: StringOrDouble?

    /** Minimum width of a candle. */
    var barMinWidth: StringOrDouble?

    /** Gap between candles of different series. */
    var barGap: String?

    /** Gap between candle categories. */
    var barCategoryGap: String?

    /** Whether to enable large-data mode. */
    var large: Boolean?

    /** Data size above which large-data mode is used. */
    var largeThreshold: Double?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Whether to clip the candles to the coordinate system. */
    var clip: Boolean?
}

/**
 * Boxplot series.
 */
external interface BoxplotSeriesOption : SeriesOption {
    /** Coordinate system: `"cartesian2d"`. */
    override var coordinateSystem: String?

    /** Layout of the boxes: `"horizontal"` or `"vertical"`. */
    var layout: String?

    /** Width of a box. */
    var boxWidth: DoubleOrDoubleArray?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Whether to clip the boxes to the coordinate system. */
    var clip: Boolean?
}

/**
 * Heatmap series.
 */
external interface HeatmapSeriesOption : SeriesOption {
    /** Coordinate system: `"cartesian2d"`, `"calendar"`, `"geo"` or `"none"`. */
    override var coordinateSystem: String?

    /** Size of a heatmap point in pixels. */
    var pointSize: Double?

    /** Blur size of the heatmap. */
    var blurSize: Double?

    /** Minimum opacity of the heatmap. */
    var minOpacity: Double?

    /** Maximum opacity of the heatmap. */
    var maxOpacity: Double?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Index of the geo coordinate system. */
    var geoIndex: Double?

    /** Index of the calendar coordinate system. */
    var calendarIndex: Double?

    /** Whether to clip the heatmap to the coordinate system. */
    var clip: Boolean?
}

/**
 * Lines series: data lines on a coordinate system, for example flight routes.
 */
external interface LinesSeriesOption : SeriesOption {
    /** Coordinate system: `"cartesian2d"`, `"geo"` or `"none"`. */
    override var coordinateSystem: String?

    /** Whether the lines are drawn as polylines. */
    var polyline: Boolean?

    /** Moving effect of the lines. */
    var effect: LinesEffectOption?

    /** Whether to enable large-data mode. */
    var large: Boolean?

    /** Data size above which large-data mode is used. */
    var largeThreshold: Double?

    /** Symbol of the line ends. */
    var symbol: String?

    /** Symbol size of the line ends. */
    var symbolSize: DoubleOrDoubleArray?

    /** Index of the geo coordinate system. */
    var geoIndex: Double?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Index of the polar coordinate system. */
    var polarIndex: Double?
}

/**
 * Moving effect of a lines series.
 */
external interface LinesEffectOption {
    /** Whether the effect is shown. */
    var show: Boolean?

    /** Duration of one animation cycle in milliseconds. */
    var period: Double?

    /** Trail length of the moving symbol. */
    var trailLength: Double?

    /** Symbol of the moving effect. */
    var symbol: String?

    /** Size of the moving symbol. */
    var symbolSize: Double?

    /** Color of the moving symbol. */
    var color: ZRColor?

    /** Constant speed instead of a period per line. */
    var constantSpeed: Double?

    /** Whether the effect loops. */
    var loop: Boolean?
}

/**
 * Parallel series: one polyline per data item on a parallel coordinate system.
 */
external interface ParallelSeriesOption : SeriesOption {
    /** Coordinate system, always `"parallel"`. */
    override var coordinateSystem: String?

    /** Index of the parallel coordinate system. */
    var parallelIndex: Double?

    /** Opacity of the lines that are not highlighted. */
    var inactiveOpacity: Double?

    /** Opacity of the highlighted lines. */
    var activeOpacity: Double?

    /** Whether to smooth the lines. */
    var smooth: BooleanOrDouble?

    /** Whether to draw the lines as polylines. */
    var polyline: Boolean?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?
}

/**
 * Custom series: renders data with a user-provided render item function.
 */
external interface CustomSeriesOption : SeriesOption {
    /** Coordinate system: `"cartesian2d"`, `"polar"`, `"geo"`, `"singleAxis"`, `"calendar"`, `"none"`. */
    override var coordinateSystem: String?

    /** Function that returns the graphic elements of one data item. */
    var renderItem: JsAny?

    /** Whether to clip the elements to the coordinate system. */
    var clip: Boolean?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Index of the polar coordinate system. */
    var polarIndex: Double?

    /** Index of the geo coordinate system. */
    var geoIndex: Double?

    /** Index of the single axis. */
    var singleAxisIndex: Double?

    /** Index of the calendar coordinate system. */
    var calendarIndex: Double?
}
