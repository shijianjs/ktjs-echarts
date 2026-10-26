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
