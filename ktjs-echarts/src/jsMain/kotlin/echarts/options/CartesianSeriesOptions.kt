package echarts.options

import echarts.SeriesType
import echarts.SeriesLayoutBy
import echarts.ProgressiveChunkMode
import echarts.CoordinateSystemUsage
import echarts.CustomSeriesRenderItem
import echarts.CartesianPolarCoordinateSystem
import echarts.CartesianCoordinateSystem
import echarts.HeatmapCoordinateSystem
import echarts.AnimationEasing
import js.objects.Record
import kotlin.js.JsAny
import kotlin.js.JsName
import zrender.ZRColor
import zrender.ZRFontWeight

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

    /**
     * Read-only view of the series data.
     *
     * The elements keep the generic [OptionDataItem] type, which is enough to read values and
     * `[x, y]` tuples but not to write the data back. Writing uses the `typedData` view that every
     * concrete series declares over the same JavaScript property, with the exact item type of that
     * series.
     */
    val data: Array<OptionDataItem?>?

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

    /** Chunking mode of the progressive rendering; `"mod"` splits the rest into equal chunks. */
    var progressiveChunkMode: ProgressiveChunkMode?

    /**
     * Coordinate system used by the series. Series that only accept a fixed set of coordinate
     * systems also expose a typed `typedCoordinateSystem` property that maps to this same
     * JavaScript property; see [LineSeriesOption.typedCoordinateSystem].
     */
    var coordinateSystem: String?

    /**
     * How the series uses [coordinateSystem]; `"data"` (the default) lays every data item out on it,
     * `"box"` takes only the bounding rect or the anchor point from it.
     */
    var coordinateSystemUsage: CoordinateSystemUsage?

    /**
     * Position of the series on the coordinate system named by [coordinateSystem] when
     * [coordinateSystemUsage] is `"box"`, e.g. one cell of a matrix.
     */
    var coord: CoordinateSystemDataCoord?

    /** Whether the series participates in the legend. */
    var legendHoverLink: Boolean?

    /** Group id of the data, used by the drill-down animation of hierarchy series. */
    var dataGroupId: OptionId?

    /** Blend mode of the elements of the series. */
    var blendMode: String?

    /** Layout of the labels of the series. */
    var labelLayout: LabelLayout?

    /** Animation of the state transitions of the series. */
    var stateAnimation: StateAnimationOption?

    /** Map of the selected data items, keyed by name or index, or `"all"`. */
    var selectedMap: SelectedMap?

    /** Selection mode of the data items of the series. */
    var selectedMode: SelectionMode?

    /** Number of elements above which a separate hover layer is used. */
    var hoverLayerThreshold: Double?
}

/**
 * Line series.
 */
external interface LineSeriesOption :
    SeriesOption,
    SymbolOptionMixin,
    SeriesOnCartesianOptionMixin,
    SeriesOnPolarOptionMixin
{
    /** Coordinate system: `"cartesian2d"` or `"polar"`. */
    override var coordinateSystem: String?

    /**
     * Typed view of [coordinateSystem]; the base interface keeps it as a string because Kotlin
     * cannot narrow an overridden `var`.
     */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CartesianPolarCoordinateSystem?

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

    /** How the data maps to the axes. */
    var seriesLayoutBy: SeriesLayoutBy?

    /** Stack strategy: `"samesign"`, `"all"` or `"positive"`. */
    var stackStrategy: String?

    /** Area style; gives the line a filled area. */
    override var areaStyle: AreaStyleOption?

    /** Whether to show the line as a polar bar when on a polar coordinate system. */
    var showBackground: Boolean?

    /**
     * Typed view of [SeriesOption.data] with the item type of a line series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<LineDataItem?>?
}

/**
 * Bar series.
 */
external interface BarSeriesOption : SeriesOption, SeriesOnCartesianOptionMixin, SeriesOnPolarOptionMixin {
    /** Coordinate system: `"cartesian2d"` or `"polar"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [LineSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CartesianPolarCoordinateSystem?

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

    /** Gap between bars of different series; a percentage string or an absolute pixel value. */
    var barGap: StringOrDouble?

    /** Gap between bar categories; a percentage string or an absolute pixel value. */
    var barCategoryGap: StringOrDouble?

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

    /** How the data maps to the axes. */
    var seriesLayoutBy: SeriesLayoutBy?

    /** Downsampling method. */
    var sampling: String?

    /** Whether to enable large-data mode. */
    var large: Boolean?

    /** Data size above which large-data mode is used. */
    var largeThreshold: Double?

    /**
     * Typed view of [SeriesOption.data] with the item type of a bar series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<BarDataItem?>?
}

/**
 * Scatter series.
 */
external interface ScatterSeriesOption :
    SeriesOption,
    SymbolOptionMixin,
    SeriesOnCartesianOptionMixin,
    SeriesOnPolarOptionMixin,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin,
    SeriesOnGeoOptionMixin,
    SeriesOnSingleOptionMixin
{
    /** Coordinate system: `"cartesian2d"`, `"polar"`, `"geo"`, `"singleAxis"`, `"calendar"`. */
    override var coordinateSystem: String?

    /** Whether to enable large-data mode. */
    var large: Boolean?

    /** Data size above which large-data mode is used. */
    var largeThreshold: Double?

    /** How the data maps to the axes. */
    var seriesLayoutBy: SeriesLayoutBy?

    /** Downsampling method. */
    var sampling: String?

    /** Whether to clip the symbols to the coordinate system. */
    var clip: Boolean?

    /**
     * Typed view of [SeriesOption.data] with the item type of a scatter series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<ScatterDataItem?>?
}

/**
 * Effect scatter series: a scatter series with a ripple animation.
 *
 * Upstream `EffectScatterSeriesOption` shares the coordinate-system mixins with
 * [ScatterSeriesOption] but is not a subtype of it, and the two series accept different data items,
 * so this interface does not extend [ScatterSeriesOption] either.
 */
external interface EffectScatterSeriesOption :
    SeriesOption,
    SymbolOptionMixin,
    SeriesOnCartesianOptionMixin,
    SeriesOnPolarOptionMixin,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin,
    SeriesOnGeoOptionMixin,
    SeriesOnSingleOptionMixin
{
    /** Coordinate system: `"cartesian2d"`, `"polar"`, `"geo"`, `"singleAxis"` or `"calendar"`. */
    override var coordinateSystem: String?

    /** Type of the effect, always `"ripple"`. */
    var effectType: String?

    /** When the ripple effect is shown: `"render"` or `"emphasis"`. */
    var showEffectOn: String?

    /** Whether to clip the symbols to the coordinate system. */
    var clip: Boolean?

    /** Ripple effect configuration. */
    var rippleEffect: RippleEffectOption?

    /**
     * Typed view of [SeriesOption.data] with the item type of an effect scatter series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<EffectScatterDataItem?>?
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
external interface CandlestickSeriesOption : SeriesOption, SeriesOnCartesianOptionMixin {
    /** Coordinate system: `"cartesian2d"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [LineSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CartesianCoordinateSystem?

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

    /** Whether to clip the candles to the coordinate system. */
    var clip: Boolean?

    /**
     * Typed view of [SeriesOption.data] with the item type of a candlestick series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<CandlestickDataItem?>?
}

/**
 * Boxplot series.
 */
external interface BoxplotSeriesOption : SeriesOption, SeriesOnCartesianOptionMixin {
    /** Coordinate system: `"cartesian2d"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [LineSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CartesianCoordinateSystem?

    /** Layout of the boxes: `"horizontal"` or `"vertical"`. */
    var layout: String?

    /** Width of a box. */
    var boxWidth: DoubleOrDoubleArray?

    /** Whether to clip the boxes to the coordinate system. */
    var clip: Boolean?

    /**
     * Typed view of [SeriesOption.data] with the item type of a boxplot series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<BoxplotDataItem?>?
}

/**
 * Heatmap series.
 */
external interface HeatmapSeriesOption :
    SeriesOption,
    SeriesOnCartesianOptionMixin,
    SeriesOnGeoOptionMixin,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin
{
    /** Coordinate system: `"cartesian2d"`, `"geo"`, `"calendar"` or `"matrix"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [LineSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: HeatmapCoordinateSystem?

    /** Size of a heatmap point in pixels. */
    var pointSize: Double?

    /** Blur size of the heatmap. */
    var blurSize: Double?

    /** Minimum opacity of the heatmap. */
    var minOpacity: Double?

    /** Maximum opacity of the heatmap. */
    var maxOpacity: Double?

    /** Whether to clip the heatmap to the coordinate system. */
    var clip: Boolean?

    /**
     * Typed view of [SeriesOption.data] with the item type of a heatmap series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<HeatmapDataItem?>?
}

/**
 * Lines series: data lines on a coordinate system, for example flight routes.
 */
external interface LinesSeriesOption :
    SeriesOption,
    SeriesOnCartesianOptionMixin,
    SeriesOnGeoOptionMixin,
    SeriesOnPolarOptionMixin,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin
{
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

    /** Symbol of the line ends, one for both ends or one per end. */
    var symbol: StringOrStringArray?

    /** Symbol size of the line ends. */
    var symbolSize: DoubleOrDoubleArray?

    /**
     * Typed view of [SeriesOption.data] with the item type of a lines series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<LinesDataItemOption?>?
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
external interface ParallelSeriesOption : SeriesOption, ComponentOnCalendarOptionMixin, ComponentOnMatrixOptionMixin {
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

    /**
     * Typed view of [SeriesOption.data] with the item type of a parallel series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<ParallelSeriesDataItem?>?
}

/**
 * Custom series: renders data with a user-provided render item function.
 */
external interface CustomSeriesOption :
    SeriesOption,
    SeriesOnCartesianOptionMixin,
    SeriesOnPolarOptionMixin,
    SeriesOnSingleOptionMixin,
    SeriesOnGeoOptionMixin,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin
{
    /** Coordinate system: `"cartesian2d"`, `"polar"`, `"geo"`, `"singleAxis"`, `"calendar"`, `"none"`. */
    override var coordinateSystem: String?

    /** Function that returns the graphic elements of one data item. */
    var renderItem: CustomSeriesRenderItem?

    /** Whether to clip the elements to the coordinate system. */
    var clip: Boolean?

    /** Payload attached to the series; readable from `renderItem` through `params.itemPayload`. */
    var itemPayload: Record<String, JsAny>?

    /**
     * Typed view of [SeriesOption.data] with the item type of a custom series.
     *
     * Upstream leaves the `data` of a custom series as `unknown[]` because the render item function
     * consumes it; the wrapper keeps the generic data item shape, so both this and
     * [SeriesOption.data] have the same type.
     */
    @JsName("data")
    var typedData: Array<OptionDataItem?>?
}

/**
 * Font members accepted by `CustomSeriesRenderItemAPI.font`.
 *
 * Upstream `Pick<TextCommonOption, 'fontStyle' | 'fontWeight' | 'fontSize' | 'fontFamily'>`.
 */
external interface CustomSeriesFontOption {
    /** Font style, a CSS `font-style` keyword. */
    var fontStyle: web.cssom.FontStyle?

    /** Font weight. */
    var fontWeight: ZRFontWeight?

    /** Font size in pixels or as a string. */
    var fontSize: StringOrDouble?

    /** Font family. */
    var fontFamily: String?
}

/**
 * Arguments of `CustomSeriesRenderItemAPI.barLayout`.
 */
external interface BarGridLayoutOptionForCustomSeries {
    /** Number of bars. */
    var count: Double

    /** Width of one bar. */
    var barWidth: StringOrDouble?

    /** Maximum width of one bar. */
    var barMaxWidth: StringOrDouble?

    /** Minimum width of one bar. */
    var barMinWidth: StringOrDouble?

    /** Gap between the bars of different series. */
    var barGap: StringOrDouble?

    /** Gap between the bar categories. */
    var barCategoryGap: StringOrDouble?
}

/**
 * One entry of a `CustomSeriesRenderItemAPI.barLayout` result.
 */
external interface BarGridLayoutResultItem {
    /** Width of the band of one category. */
    var bandWidth: Double

    /** Offset of the bar from the left of the band. */
    var offset: Double

    /** Width of the bar. */
    var width: Double

    /** Offset of the center of the bar from the left of the band. */
    var offsetCenter: Double
}

/**
 * Result of `CustomSeriesRenderItemAPI.barLayout`; `null`/`undefined` when the series is not on a
 * cartesian coordinate system.
 */
typealias BarGridLayoutResultForCustomSeries = Array<BarGridLayoutResultItem>
