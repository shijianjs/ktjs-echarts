package echarts

import kotlin.js.unsafeCast

/*
 * String-literal unions declared by the upstream TypeScript definitions.
 *
 * Kotlin cannot assign a JavaScript string to a member of an external interface without a compiler
 * plugin, so the values live in a plain object next to the type, the same way kotlin-wrappers
 * declares `web.html.HtmlTagName`. The constants compile to the bare string, so they are
 * interchangeable with the literals they replace:
 *
 * ```kotlin
 * xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
 * ```
 */

/**
 * Type of a cartesian, polar or single axis.
 */
sealed external interface AxisType

/** Wraps a value that comes from JavaScript. Prefer the constants in [AxisTypes]. */
inline fun AxisType(value: String): AxisType = value.unsafeCast<AxisType>()

/**
 * The documented values of [AxisType].
 */
object AxisTypes {
    /** Numeric axis. */
    inline val `value`: AxisType get() = AxisType("value")

    /** Category axis. */
    inline val category: AxisType get() = AxisType("category")

    /** Time axis. */
    inline val time: AxisType get() = AxisType("time")

    /** Logarithmic axis. */
    inline val log: AxisType get() = AxisType("log")

}

/**
 * Orientation of a layout that can run along either axis.
 */
sealed external interface Orient

/** Wraps a value that comes from JavaScript. Prefer the constants in [Orients]. */
inline fun Orient(value: String): Orient = value.unsafeCast<Orient>()

/**
 * The documented values of [Orient].
 */
object Orients {
    /** Left to right. */
    inline val horizontal: Orient get() = Orient("horizontal")

    /** Top to bottom. */
    inline val vertical: Orient get() = Orient("vertical")

}

/**
 * Whether a pie series is drawn as a rose chart, and how the sectors are sized.
 */
sealed external interface RoseType

/** Wraps a value that comes from JavaScript. Prefer the constants in [RoseTypes]. */
inline fun RoseType(value: String): RoseType = value.unsafeCast<RoseType>()

/**
 * The documented values of [RoseType].
 */
object RoseTypes {
    /** The radius of the sector encodes the value. */
    inline val radius: RoseType get() = RoseType("radius")

    /** The area of the sector encodes the value. */
    inline val area: RoseType get() = RoseType("area")

}

/**
 * What triggers a tooltip.
 */
sealed external interface TooltipTrigger

/** Wraps a value that comes from JavaScript. Prefer the constants in [TooltipTriggers]. */
inline fun TooltipTrigger(value: String): TooltipTrigger = value.unsafeCast<TooltipTrigger>()

/**
 * The documented values of [TooltipTrigger].
 */
object TooltipTriggers {
    /** A data item triggers the tooltip. */
    inline val item: TooltipTrigger get() = TooltipTrigger("item")

    /** An axis triggers the tooltip. */
    inline val axis: TooltipTrigger get() = TooltipTrigger("axis")

    /** The tooltip is never triggered automatically. */
    inline val none: TooltipTrigger get() = TooltipTrigger("none")

}

/**
 * Rendering mode of a legend.
 */
sealed external interface LegendType

/** Wraps a value that comes from JavaScript. Prefer the constants in [LegendTypes]. */
inline fun LegendType(value: String): LegendType = value.unsafeCast<LegendType>()

/**
 * The documented values of [LegendType].
 */
object LegendTypes {
    /** Plain legend; wrapping is controlled by the item gap and the container size. */
    inline val plain: LegendType get() = LegendType("plain")

    /** Scrollable legend. */
    inline val scroll: LegendType get() = LegendType("scroll")

}

/**
 * Variant of a data zoom component.
 */
sealed external interface DataZoomType

/** Wraps a value that comes from JavaScript. Prefer the constants in [DataZoomTypes]. */
inline fun DataZoomType(value: String): DataZoomType = value.unsafeCast<DataZoomType>()

/**
 * The documented values of [DataZoomType].
 */
object DataZoomTypes {
    /** Zooming with the mouse wheel and pinch gestures. */
    inline val inside: DataZoomType get() = DataZoomType("inside")

    /** Zooming with a slider. */
    inline val slider: DataZoomType get() = DataZoomType("slider")

}

/**
 * Variant of a visual map component.
 */
sealed external interface VisualMapType

/** Wraps a value that comes from JavaScript. Prefer the constants in [VisualMapTypes]. */
inline fun VisualMapType(value: String): VisualMapType = value.unsafeCast<VisualMapType>()

/**
 * The documented values of [VisualMapType].
 */
object VisualMapTypes {
    /** Continuous visual map. */
    inline val continuous: VisualMapType get() = VisualMapType("continuous")

    /** Piecewise visual map. */
    inline val piecewise: VisualMapType get() = VisualMapType("piecewise")

}

/**
 * How a data zoom component handles the data outside its window.
 */
sealed external interface FilterMode

/** Wraps a value that comes from JavaScript. Prefer the constants in [FilterModes]. */
inline fun FilterMode(value: String): FilterMode = value.unsafeCast<FilterMode>()

/**
 * The documented values of [FilterMode].
 */
object FilterModes {
    /** Filter the data outside the window. */
    inline val filter: FilterMode get() = FilterMode("filter")

    /** Keep the data but ignore it when the axis extent is computed. */
    inline val weakFilter: FilterMode get() = FilterMode("weakFilter")

    /** Render the data outside the window as empty. */
    inline val empty: FilterMode get() = FilterMode("empty")

    /** Do not filter or hide anything. */
    inline val none: FilterMode get() = FilterMode("none")

}

/**
 * What the emphasis state focuses on.
 */
sealed external interface EmphasisFocus

/** Wraps a value that comes from JavaScript. Prefer the constants in [EmphasisFocuses]. */
inline fun EmphasisFocus(value: String): EmphasisFocus = value.unsafeCast<EmphasisFocus>()

/**
 * The documented values of [EmphasisFocus].
 */
object EmphasisFocuses {
    /** No focus; the whole chart keeps its normal state. */
    inline val none: EmphasisFocus get() = EmphasisFocus("none")

    /** Only the hovered element is emphasized. */
    inline val self: EmphasisFocus get() = EmphasisFocus("self")

    /** The whole series of the hovered element is emphasized. */
    inline val series: EmphasisFocus get() = EmphasisFocus("series")

}

/**
 * Scope that is blurred while an element is emphasized.
 */
sealed external interface BlurScope

/** Wraps a value that comes from JavaScript. Prefer the constants in [BlurScopes]. */
inline fun BlurScope(value: String): BlurScope = value.unsafeCast<BlurScope>()

/**
 * The documented values of [BlurScope].
 */
object BlurScopes {
    /** Blur the other elements of the same coordinate system. */
    inline val coordinateSystem: BlurScope get() = BlurScope("coordinateSystem")

    /** Blur the other elements of the same series. */
    inline val series: BlurScope get() = BlurScope("series")

    /** Blur every other element of the chart. */
    inline val global: BlurScope get() = BlurScope("global")

}

/**
 * Shape of an axis pointer.
 */
sealed external interface AxisPointerType

/** Wraps a value that comes from JavaScript. Prefer the constants in [AxisPointerTypes]. */
inline fun AxisPointerType(value: String): AxisPointerType = value.unsafeCast<AxisPointerType>()

/**
 * The documented values of [AxisPointerType].
 */
object AxisPointerTypes {
    /** A line perpendicular to the axis. */
    inline val `line`: AxisPointerType get() = AxisPointerType("line")

    /** A translucent band covering the category of the pointer. */
    inline val shadow: AxisPointerType get() = AxisPointerType("shadow")

    /** Two lines, one per axis, meeting at the pointer. */
    inline val cross: AxisPointerType get() = AxisPointerType("cross")

    /** No axis pointer. */
    inline val none: AxisPointerType get() = AxisPointerType("none")

}

/**
 * Image format produced by `getDataURL`.
 */
sealed external interface ImageType

/** Wraps a value that comes from JavaScript. Prefer the constants in [ImageTypes]. */
inline fun ImageType(value: String): ImageType = value.unsafeCast<ImageType>()

/**
 * The documented values of [ImageType].
 */
object ImageTypes {
    /** PNG image. */
    inline val png: ImageType get() = ImageType("png")

    /** JPEG image. */
    inline val jpeg: ImageType get() = ImageType("jpeg")

    /** SVG image. */
    inline val svg: ImageType get() = ImageType("svg")

}

/**
 * Type of a data series, set by the series factory functions.
 */
sealed external interface SeriesType

/** Wraps a value that comes from JavaScript. Prefer the constants in [SeriesTypes]. */
inline fun SeriesType(value: String): SeriesType = value.unsafeCast<SeriesType>()

/**
 * The documented values of [SeriesType].
 */
object SeriesTypes {
    /** Line chart. */
    inline val `line`: SeriesType get() = SeriesType("line")

    /** Bar chart. */
    inline val bar: SeriesType get() = SeriesType("bar")

    /** Pie chart. */
    inline val pie: SeriesType get() = SeriesType("pie")

    /** Scatter chart. */
    inline val scatter: SeriesType get() = SeriesType("scatter")

    /** Scatter chart with ripple effects. */
    inline val effectScatter: SeriesType get() = SeriesType("effectScatter")

    /** Radar chart. */
    inline val radar: SeriesType get() = SeriesType("radar")

    /** Map chart. */
    inline val `map`: SeriesType get() = SeriesType("map")

    /** Tree chart. */
    inline val tree: SeriesType get() = SeriesType("tree")

    /** Treemap chart. */
    inline val treemap: SeriesType get() = SeriesType("treemap")

    /** Sunburst chart. */
    inline val sunburst: SeriesType get() = SeriesType("sunburst")

    /** Graph chart. */
    inline val graph: SeriesType get() = SeriesType("graph")

    /** Chord chart. */
    inline val chord: SeriesType get() = SeriesType("chord")

    /** Funnel chart. */
    inline val funnel: SeriesType get() = SeriesType("funnel")

    /** Gauge chart. */
    inline val gauge: SeriesType get() = SeriesType("gauge")

    /** Pictorial bar chart. */
    inline val pictorialBar: SeriesType get() = SeriesType("pictorialBar")

    /** Theme river chart. */
    inline val themeRiver: SeriesType get() = SeriesType("themeRiver")

    /** Custom series. */
    inline val custom: SeriesType get() = SeriesType("custom")

    /** Boxplot chart. */
    inline val boxplot: SeriesType get() = SeriesType("boxplot")

    /** Candlestick chart. */
    inline val candlestick: SeriesType get() = SeriesType("candlestick")

    /** Heatmap chart. */
    inline val heatmap: SeriesType get() = SeriesType("heatmap")

    /** Parallel coordinates chart. */
    inline val parallel: SeriesType get() = SeriesType("parallel")

    /** Lines chart. */
    inline val lines: SeriesType get() = SeriesType("lines")

    /** Sankey diagram. */
    inline val sankey: SeriesType get() = SeriesType("sankey")

}

/**
 * Name of a built-in animation easing, or a custom easing function.
 *
 * Upstream `AnimationEasing` is the union of the built-in names and an easing function; wrap a
 * function with `AnimationEasing { k -> ... }`.
 */
sealed external interface AnimationEasing

/** Wraps a built-in easing name. Prefer the constants in [AnimationEasings]. */
inline fun AnimationEasing(value: String): AnimationEasing = value.unsafeCast<AnimationEasing>()

/** Wraps a custom easing function that maps `0..1` to `0..1`. */
fun AnimationEasing(value: (Double) -> Double): AnimationEasing = value.unsafeCast<AnimationEasing>()

/**
 * The built-in values of [AnimationEasing].
 */
object AnimationEasings {
    /** `linear`. */
    inline val linear: AnimationEasing get() = AnimationEasing("linear")
    /** `quadraticIn`. */
    inline val quadraticIn: AnimationEasing get() = AnimationEasing("quadraticIn")
    /** `quadraticOut`. */
    inline val quadraticOut: AnimationEasing get() = AnimationEasing("quadraticOut")
    /** `quadraticInOut`. */
    inline val quadraticInOut: AnimationEasing get() = AnimationEasing("quadraticInOut")
    /** `cubicIn`. */
    inline val cubicIn: AnimationEasing get() = AnimationEasing("cubicIn")
    /** `cubicOut`. */
    inline val cubicOut: AnimationEasing get() = AnimationEasing("cubicOut")
    /** `cubicInOut`. */
    inline val cubicInOut: AnimationEasing get() = AnimationEasing("cubicInOut")
    /** `quarticIn`. */
    inline val quarticIn: AnimationEasing get() = AnimationEasing("quarticIn")
    /** `quarticOut`. */
    inline val quarticOut: AnimationEasing get() = AnimationEasing("quarticOut")
    /** `quarticInOut`. */
    inline val quarticInOut: AnimationEasing get() = AnimationEasing("quarticInOut")
    /** `quinticIn`. */
    inline val quinticIn: AnimationEasing get() = AnimationEasing("quinticIn")
    /** `quinticOut`. */
    inline val quinticOut: AnimationEasing get() = AnimationEasing("quinticOut")
    /** `quinticInOut`. */
    inline val quinticInOut: AnimationEasing get() = AnimationEasing("quinticInOut")
    /** `sinusoidalIn`. */
    inline val sinusoidalIn: AnimationEasing get() = AnimationEasing("sinusoidalIn")
    /** `sinusoidalOut`. */
    inline val sinusoidalOut: AnimationEasing get() = AnimationEasing("sinusoidalOut")
    /** `sinusoidalInOut`. */
    inline val sinusoidalInOut: AnimationEasing get() = AnimationEasing("sinusoidalInOut")
    /** `exponentialIn`. */
    inline val exponentialIn: AnimationEasing get() = AnimationEasing("exponentialIn")
    /** `exponentialOut`. */
    inline val exponentialOut: AnimationEasing get() = AnimationEasing("exponentialOut")
    /** `exponentialInOut`. */
    inline val exponentialInOut: AnimationEasing get() = AnimationEasing("exponentialInOut")
    /** `circularIn`. */
    inline val circularIn: AnimationEasing get() = AnimationEasing("circularIn")
    /** `circularOut`. */
    inline val circularOut: AnimationEasing get() = AnimationEasing("circularOut")
    /** `circularInOut`. */
    inline val circularInOut: AnimationEasing get() = AnimationEasing("circularInOut")
    /** `elasticIn`. */
    inline val elasticIn: AnimationEasing get() = AnimationEasing("elasticIn")
    /** `elasticOut`. */
    inline val elasticOut: AnimationEasing get() = AnimationEasing("elasticOut")
    /** `elasticInOut`. */
    inline val elasticInOut: AnimationEasing get() = AnimationEasing("elasticInOut")
    /** `backIn`. */
    inline val backIn: AnimationEasing get() = AnimationEasing("backIn")
    /** `backOut`. */
    inline val backOut: AnimationEasing get() = AnimationEasing("backOut")
    /** `backInOut`. */
    inline val backInOut: AnimationEasing get() = AnimationEasing("backInOut")
    /** `bounceIn`. */
    inline val bounceIn: AnimationEasing get() = AnimationEasing("bounceIn")
    /** `bounceOut`. */
    inline val bounceOut: AnimationEasing get() = AnimationEasing("bounceOut")
    /** `bounceInOut`. */
    inline val bounceInOut: AnimationEasing get() = AnimationEasing("bounceInOut")
}

/**
 * Event that triggers a tooltip.
 *
 * Upstream `'mousemove' | 'click' | 'none' | 'mousewheel' | 'mousemove|click|mousewheel'`; the
 * combined value is available as [TooltipTriggerOns.mouseMoveClickWheel].
 */
sealed external interface TooltipTriggerOn

/** Wraps a value that comes from JavaScript. Prefer the constants in [TooltipTriggerOns]. */
inline fun TooltipTriggerOn(value: String): TooltipTriggerOn = value.unsafeCast<TooltipTriggerOn>()

/**
 * The documented values of [TooltipTriggerOn].
 */
object TooltipTriggerOns {
    /** Trigger while the pointer moves. */
    inline val mouseMove: TooltipTriggerOn get() = TooltipTriggerOn("mousemove")

    /** Trigger on a click. */
    inline val click: TooltipTriggerOn get() = TooltipTriggerOn("click")

    /** Never trigger. */
    inline val none: TooltipTriggerOn get() = TooltipTriggerOn("none")

    /** Trigger while the mouse wheel scrolls. */
    inline val mouseWheel: TooltipTriggerOn get() = TooltipTriggerOn("mousewheel")

    /** Trigger on a pointer move, a click or a wheel scroll. */
    inline val mouseMoveClickWheel: TooltipTriggerOn get() =
        TooltipTriggerOn("mousemove|click|mousewheel")

}

/**
 * Position of a cartesian or single axis inside its grid.
 */
sealed external interface AxisPosition

/** Wraps a value that comes from JavaScript. Prefer the constants in [AxisPositions]. */
inline fun AxisPosition(value: String): AxisPosition = value.unsafeCast<AxisPosition>()

/**
 * The documented values of [AxisPosition].
 */
object AxisPositions {
    /** Top side of the grid; for the x axis. */
    inline val top: AxisPosition get() = AxisPosition("top")

    /** Bottom side of the grid; for the x axis. */
    inline val bottom: AxisPosition get() = AxisPosition("bottom")

    /** Left side of the grid; for the y axis. */
    inline val left: AxisPosition get() = AxisPosition("left")

    /** Right side of the grid; for the y axis. */
    inline val right: AxisPosition get() = AxisPosition("right")

}

/**
 * Location of an axis name relative to the axis line.
 */
sealed external interface AxisNameLocation

/** Wraps a value that comes from JavaScript. Prefer the constants in [AxisNameLocations]. */
inline fun AxisNameLocation(value: String): AxisNameLocation = value.unsafeCast<AxisNameLocation>()

/**
 * The documented values of [AxisNameLocation].
 */
object AxisNameLocations {
    /** At the start of the axis line. */
    inline val start: AxisNameLocation get() = AxisNameLocation("start")

    /** In the middle of the axis line. */
    inline val middle: AxisNameLocation get() = AxisNameLocation("middle")

    /** In the middle of the axis line; kept for compatibility with the upstream union. */
    inline val center: AxisNameLocation get() = AxisNameLocation("center")

    /** At the end of the axis line. */
    inline val end: AxisNameLocation get() = AxisNameLocation("end")
}

/**
 * Window a title link is opened in.
 */
sealed external interface TitleTarget

/** Wraps a value that comes from JavaScript. Prefer the constants in [TitleTargets]. */
inline fun TitleTarget(value: String): TitleTarget = value.unsafeCast<TitleTarget>()

/**
 * The documented values of [TitleTarget].
 */
object TitleTargets {
    /** The current window. */
    inline val self: TitleTarget get() = TitleTarget("self")

    /** A new window. */
    inline val blank: TitleTarget get() = TitleTarget("blank")
}

/**
 * Horizontal alignment of the legend items.
 */
sealed external interface HorizontalAlign

/** Wraps a value that comes from JavaScript. Prefer the constants in [HorizontalAligns]. */
inline fun HorizontalAlign(value: String): HorizontalAlign = value.unsafeCast<HorizontalAlign>()

/**
 * The documented values of [HorizontalAlign].
 */
object HorizontalAligns {
    /** Aligned automatically with the legend position. */
    inline val auto: HorizontalAlign get() = HorizontalAlign("auto")

    /** Aligned to the left. */
    inline val left: HorizontalAlign get() = HorizontalAlign("left")

    /** Aligned to the right. */
    inline val right: HorizontalAlign get() = HorizontalAlign("right")
}

/**
 * Position of the page buttons of a scrollable legend.
 */
sealed external interface LegendPageButtonPosition

/** Wraps a value that comes from JavaScript. Prefer the constants in [LegendPageButtonPositions]. */
inline fun LegendPageButtonPosition(value: String): LegendPageButtonPosition =
    value.unsafeCast<LegendPageButtonPosition>()

/**
 * The documented values of [LegendPageButtonPosition].
 */
object LegendPageButtonPositions {
    /** Buttons at the start of the legend. */
    inline val start: LegendPageButtonPosition get() = LegendPageButtonPosition("start")

    /** Buttons at the end of the legend. */
    inline val end: LegendPageButtonPosition get() = LegendPageButtonPosition("end")
}

/**
 * Position of the play controls of a timeline.
 */
sealed external interface TimelineControlPosition

/** Wraps a value that comes from JavaScript. Prefer the constants in [TimelineControlPositions]. */
inline fun TimelineControlPosition(value: String): TimelineControlPosition =
    value.unsafeCast<TimelineControlPosition>()

/**
 * The documented values of [TimelineControlPosition].
 */
object TimelineControlPositions {
    /** Left of the timeline axis. */
    inline val left: TimelineControlPosition get() = TimelineControlPosition("left")

    /** Right of the timeline axis. */
    inline val right: TimelineControlPosition get() = TimelineControlPosition("right")

    /** Above the timeline axis. */
    inline val top: TimelineControlPosition get() = TimelineControlPosition("top")

    /** Below the timeline axis. */
    inline val bottom: TimelineControlPosition get() = TimelineControlPosition("bottom")
}

/**
 * Behaviour of a label whose text is wider than the available space.
 */
sealed external interface LabelLineOverflow

/** Wraps a value that comes from JavaScript. Prefer the constants in [LabelLineOverflows]. */
inline fun LabelLineOverflow(value: String): LabelLineOverflow = value.unsafeCast<LabelLineOverflow>()

/**
 * The documented values of [LabelLineOverflow].
 */
object LabelLineOverflows {
    /** Truncate the text and append an ellipsis. */
    inline val truncate: LabelLineOverflow get() = LabelLineOverflow("truncate")
}

/**
 * Bounding box used when a graphic element is laid out inside its container.
 */
sealed external interface GraphicBounding

/** Wraps a value that comes from JavaScript. Prefer the constants in [GraphicBoundings]. */
inline fun GraphicBounding(value: String): GraphicBounding = value.unsafeCast<GraphicBounding>()

/**
 * The documented values of [GraphicBounding].
 */
object GraphicBoundings {
    /** Only the element itself, before the transform is applied. */
    inline val raw: GraphicBounding get() = GraphicBounding("raw")

    /** The element and all of its descendants. */
    inline val all: GraphicBounding get() = GraphicBounding("all")
}

/**
 * Shape drawn by the brush component.
 */
sealed external interface BrushType

/** Wraps a value that comes from JavaScript. Prefer the constants in [BrushTypes]. */
inline fun BrushType(value: String): BrushType = value.unsafeCast<BrushType>()

/**
 * The documented values of [BrushType].
 */
object BrushTypes {
    /** Polygon selection. */
    inline val polygon: BrushType get() = BrushType("polygon")

    /** Rectangle selection. */
    inline val rect: BrushType get() = BrushType("rect")

    /** Vertical line selection. */
    inline val lineX: BrushType get() = BrushType("lineX")

    /** Horizontal line selection. */
    inline val lineY: BrushType get() = BrushType("lineY")
}

/**
 * Brush type that can also be disabled with `false` or decided automatically with `"auto"`.
 */
sealed external interface BrushTypeUncertain

/** Wraps a value that comes from JavaScript. Prefer the constants in [BrushTypeUncertains]. */
inline fun BrushTypeUncertain(value: String): BrushTypeUncertain = value.unsafeCast<BrushTypeUncertain>()

/** Wraps `false`, which disables the brush. */
inline fun BrushTypeUncertain(value: Boolean): BrushTypeUncertain = value.unsafeCast<BrushTypeUncertain>()

/**
 * The documented values of [BrushTypeUncertain] that are not already in [BrushTypes].
 */
object BrushTypeUncertains {
    /** The brush type is decided by the toolbox. */
    inline val auto: BrushTypeUncertain get() = BrushTypeUncertain("auto")
}

/**
 * How many brush areas can exist at the same time.
 */
sealed external interface BrushMode

/** Wraps a value that comes from JavaScript. Prefer the constants in [BrushModes]. */
inline fun BrushMode(value: String): BrushMode = value.unsafeCast<BrushMode>()

/**
 * The documented values of [BrushMode].
 */
object BrushModes {
    /** Only one brush area at a time. */
    inline val single: BrushMode get() = BrushMode("single")

    /** Several brush areas at the same time. */
    inline val multiple: BrushMode get() = BrushMode("multiple")
}

/**
 * Throttling strategy of the brush event handler.
 */
sealed external interface BrushThrottleType

/** Wraps a value that comes from JavaScript. Prefer the constants in [BrushThrottleTypes]. */
inline fun BrushThrottleType(value: String): BrushThrottleType = value.unsafeCast<BrushThrottleType>()

/**
 * The documented values of [BrushThrottleType].
 */
object BrushThrottleTypes {
    /** Emit at a fixed rate. */
    inline val fixRate: BrushThrottleType get() = BrushThrottleType("fixRate")

    /** Emit after the input settles. */
    inline val debounce: BrushThrottleType get() = BrushThrottleType("debounce")
}

/**
 * Type of one dimension of a matrix coordinate system.
 */
sealed external interface MatrixDimensionType

/** Wraps a value that comes from JavaScript. Prefer the constants in [MatrixDimensionTypes]. */
inline fun MatrixDimensionType(value: String): MatrixDimensionType = value.unsafeCast<MatrixDimensionType>()

/**
 * The documented values of [MatrixDimensionType].
 */
object MatrixDimensionTypes {
    /** Category dimension; the only supported type. */
    inline val category: MatrixDimensionType get() = MatrixDimensionType("category")
}

/**
 * How one bound of a data zoom is interpreted.
 */
sealed external interface DataZoomRangeMode

/** Wraps a value that comes from JavaScript. Prefer the constants in [DataZoomRangeModes]. */
inline fun DataZoomRangeMode(value: String): DataZoomRangeMode = value.unsafeCast<DataZoomRangeMode>()

/**
 * The documented values of [DataZoomRangeMode].
 */
object DataZoomRangeModes {
    /** Bound expressed as a data value. */
    inline val value: DataZoomRangeMode get() = DataZoomRangeMode("value")

    /** Bound expressed as a percentage of the axis extent. */
    inline val percent: DataZoomRangeMode get() = DataZoomRangeMode("percent")
}

/**
 * Event that triggers an axis pointer.
 *
 * Upstream `'click' | 'mousemove' | 'none' | 'mousemove|click'`; the combined value is available as
 * [AxisPointerTriggerOns.mouseMoveClick].
 */
sealed external interface AxisPointerTriggerOn

/** Wraps a value that comes from JavaScript. Prefer the constants in [AxisPointerTriggerOns]. */
inline fun AxisPointerTriggerOn(value: String): AxisPointerTriggerOn =
    value.unsafeCast<AxisPointerTriggerOn>()

/**
 * The documented values of [AxisPointerTriggerOn].
 */
object AxisPointerTriggerOns {
    /** Trigger on a click. */
    inline val click: AxisPointerTriggerOn get() = AxisPointerTriggerOn("click")

    /** Trigger while the pointer moves. */
    inline val mouseMove: AxisPointerTriggerOn get() = AxisPointerTriggerOn("mousemove")

    /** Never trigger. */
    inline val none: AxisPointerTriggerOn get() = AxisPointerTriggerOn("none")

    /** Trigger on a pointer move or a click. */
    inline val mouseMoveClick: AxisPointerTriggerOn get() = AxisPointerTriggerOn("mousemove|click")
}

/**
 * Visibility of an axis pointer, driven by `dispatchAction`.
 */
sealed external interface AxisPointerStatus

/** Wraps a value that comes from JavaScript. Prefer the constants in [AxisPointerStatuses]. */
inline fun AxisPointerStatus(value: String): AxisPointerStatus = value.unsafeCast<AxisPointerStatus>()

/**
 * The documented values of [AxisPointerStatus].
 */
object AxisPointerStatuses {
    /** Show the pointer. */
    inline val show: AxisPointerStatus get() = AxisPointerStatus("show")

    /** Hide the pointer. */
    inline val hide: AxisPointerStatus get() = AxisPointerStatus("hide")
}

/**
 * Action applied to a graphic element when the option is merged.
 */
sealed external interface GraphicAction

/** Wraps a value that comes from JavaScript. Prefer the constants in [GraphicActions]. */
inline fun GraphicAction(value: String): GraphicAction = value.unsafeCast<GraphicAction>()

/**
 * The documented values of [GraphicAction].
 */
object GraphicActions {
    /** Merge the new members into the existing element. */
    inline val merge: GraphicAction get() = GraphicAction("merge")

    /** Replace the existing element with the new one. */
    inline val replace: GraphicAction get() = GraphicAction("replace")

    /** Remove the existing element. */
    inline val remove: GraphicAction get() = GraphicAction("remove")
}

/**
 * Icon of a brush type shown in the toolbox: a brush type, `"keep"` or `"clear"`.
 */
sealed external interface BrushToolboxIconType

/** Wraps a value that comes from JavaScript. Prefer the constants in [BrushToolboxIconTypes]. */
inline fun BrushToolboxIconType(value: String): BrushToolboxIconType =
    value.unsafeCast<BrushToolboxIconType>()

/**
 * The documented values of [BrushToolboxIconType].
 */
object BrushToolboxIconTypes {
    /** Polygon selection icon. */
    inline val polygon: BrushToolboxIconType get() = BrushToolboxIconType("polygon")

    /** Rectangle selection icon. */
    inline val rect: BrushToolboxIconType get() = BrushToolboxIconType("rect")

    /** Vertical line selection icon. */
    inline val lineX: BrushToolboxIconType get() = BrushToolboxIconType("lineX")

    /** Horizontal line selection icon. */
    inline val lineY: BrushToolboxIconType get() = BrushToolboxIconType("lineY")

    /** Keep the current selection. */
    inline val keep: BrushToolboxIconType get() = BrushToolboxIconType("keep")

    /** Clear the current selection. */
    inline val clear: BrushToolboxIconType get() = BrushToolboxIconType("clear")
}

/**
 * Bounds of a grid, expressed as percentages or as `"auto"`, `"same"` or `"none"`.
 */
sealed external interface OuterBoundsMode

/** Wraps a value that comes from JavaScript. Prefer the constants in [OuterBoundsModes]. */
inline fun OuterBoundsMode(value: String): OuterBoundsMode = value.unsafeCast<OuterBoundsMode>()

/**
 * The documented keyword values of [OuterBoundsMode].
 */
object OuterBoundsModes {
    /** Compute the bounds automatically from the axes and labels. */
    inline val auto: OuterBoundsMode get() = OuterBoundsMode("auto")

    /** Use the same bounds as `containLabel`. */
    inline val same: OuterBoundsMode get() = OuterBoundsMode("same")

    /** Do not add outer bounds. */
    inline val none: OuterBoundsMode get() = OuterBoundsMode("none")
}

/**
 * Elements included in the outer bounds of a grid.
 */
sealed external interface OuterBoundsContain

/** Wraps a value that comes from JavaScript. Prefer the constants in [OuterBoundsContains]. */
inline fun OuterBoundsContain(value: String): OuterBoundsContain =
    value.unsafeCast<OuterBoundsContain>()

/**
 * The documented values of [OuterBoundsContain].
 */
object OuterBoundsContains {
    /** Axes and labels. */
    inline val all: OuterBoundsContain get() = OuterBoundsContain("all")

    /** Axis labels only. */
    inline val axisLabel: OuterBoundsContain get() = OuterBoundsContain("axisLabel")

    /** Decide from the axis type. */
    inline val auto: OuterBoundsContain get() = OuterBoundsContain("auto")
}

/**
 * Order of the items inside a tooltip.
 */
sealed external interface TooltipOrder

/** Wraps a value that comes from JavaScript. Prefer the constants in [TooltipOrders]. */
inline fun TooltipOrder(value: String): TooltipOrder = value.unsafeCast<TooltipOrder>()

/**
 * The documented values of [TooltipOrder].
 */
object TooltipOrders {
    /** Series order, ascending. */
    inline val seriesAsc: TooltipOrder get() = TooltipOrder("seriesAsc")

    /** Series order, descending. */
    inline val seriesDesc: TooltipOrder get() = TooltipOrder("seriesDesc")

    /** Value order, ascending. */
    inline val valueAsc: TooltipOrder get() = TooltipOrder("valueAsc")

    /** Value order, descending. */
    inline val valueDesc: TooltipOrder get() = TooltipOrder("valueDesc")
}

/**
 * Event that expands the parallel axes.
 */
sealed external interface ParallelAxisExpandTriggerOn

/** Wraps a value that comes from JavaScript. Prefer the constants in [ParallelAxisExpandTriggerOns]. */
inline fun ParallelAxisExpandTriggerOn(value: String): ParallelAxisExpandTriggerOn =
    value.unsafeCast<ParallelAxisExpandTriggerOn>()

/**
 * The documented values of [ParallelAxisExpandTriggerOn].
 */
object ParallelAxisExpandTriggerOns {
    /** Expand on a click. */
    inline val click: ParallelAxisExpandTriggerOn get() = ParallelAxisExpandTriggerOn("click")

    /** Expand while the pointer moves. */
    inline val mouseMove: ParallelAxisExpandTriggerOn get() = ParallelAxisExpandTriggerOn("mousemove")
}