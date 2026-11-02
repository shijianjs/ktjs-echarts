package demo.examples.graphic

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import kotlin.js.unsafeCast
import web.dom.document
import web.canvas.CanvasRenderingContext2D
import web.canvas.ID
import web.html.HtmlTagName
import zrender.ImageLike
import zrender.ImagePatternObject
import zrender.PatternObject
import zrender.ZRColor
import zrender.ZRStyleProps
import demo.examples.funnel.labels
import demo.examples.gauge.center
import demo.examples.gl.square
import demo.examples.matrix.names
import demo.examples.shared.every
import demo.examples.shared.pieItem
import demo.examples.shared.toolbox

/** Watermark text of `watermark`, drawn into the tile once and repeated by the pattern. */
internal const val WATERMARK_TEXT = "ECHARTS"

/** Edge of the square tile of the watermark pattern, in pixels. */
internal const val WATERMARK_TILE = 100.0

/** Alpha of the watermark text; the original writes `0.08`. */
internal const val WATERMARK_ALPHA = 0.08

/** Build requests of the official site, the first bar block of `watermark`. */
internal val BUILDER_CHARTS =
    arrayOf(
        "map" to 3237.0,
        "lines" to 2164.0,
        "bar" to 7561.0,
        "line" to 7778.0,
        "pie" to 7355.0,
        "scatter" to 2405.0,
        "candlestick" to 1842.0,
        "radar" to 2090.0,
        "heatmap" to 1762.0,
        "treemap" to 1593.0,
        "graph" to 2060.0,
        "boxplot" to 1537.0,
        "parallel" to 1908.0,
        "gauge" to 2107.0,
        "funnel" to 1692.0,
        "sankey" to 1568.0,
    )

/** Component requests of the official site, the second bar block of `watermark`. */
internal val BUILDER_COMPONENTS =
    arrayOf(
        "geo" to 2788.0,
        "title" to 9575.0,
        "legend" to 9400.0,
        "tooltip" to 9466.0,
        "grid" to 9266.0,
        "markPoint" to 3419.0,
        "markLine" to 2984.0,
        "timeline" to 2739.0,
        "dataZoom" to 2744.0,
        "visualMap" to 2466.0,
        "toolbox" to 3034.0,
        "polar" to 1945.0,
    )

/** `builderJson.all`, the denominator of both bar blocks. */
internal const val BUILDER_ALL = 10887.0

/** Downloads of the built bundles, the upper pie of `watermark`. */
internal val DOWNLOAD_COUNTS =
    arrayOf(
        "echarts.min" to 17365.0,
        "echarts.simple.min" to 4079.0,
        "echarts.common.min" to 6929.0,
        "echarts" to 14890.0,
    )

/** Downloads of the themes, the lower pie of `watermark`. */
internal val THEME_COUNTS =
    arrayOf(
        "dark" to 1594.0,
        "infographic" to 925.0,
        "shine" to 1608.0,
        "roma" to 721.0,
        "macarons" to 2179.0,
        "vintage" to 1982.0,
    )

/** Sum of the values of [entries]. */
internal fun totalOf(entries: Array<Pair<String, Double>>): Double = entries.sumOf { it.second }

/** The names of [entries], the category labels of the bar blocks. */
internal fun namesOf(entries: Array<Pair<String, Double>>): Array<String> =
    Array(entries.size) { index -> entries[index].first }

/** The values of [entries], the visible bars of the bar blocks. */
internal fun valuesOf(entries: Array<Pair<String, Double>>): DoubleArray =
    DoubleArray(entries.size) { index -> entries[index].second }

/** What is left of [BUILDER_ALL] after every entry; the grey background bar of `watermark`. */
internal fun restOf(entries: Array<Pair<String, Double>>): DoubleArray =
    DoubleArray(entries.size) { index -> BUILDER_ALL - entries[index].second }

/** The pie items of [entries]. */
internal fun countItems(entries: Array<Pair<String, Double>>): Array<PieDataItem?> =
    Array(entries.size) { index -> pieItem(entries[index].first, entries[index].second) }

/**
 * The tiled watermark: `ECHARTS` written diagonally into a 100px canvas, used as the background.
 *
 * Upstream builds the tile with the canvas API and hands the element to the pattern; the wrapper's
 * [ImagePatternObject] takes the same element through [ImageLike].
 */
internal fun watermarkBackground(): ZRColor {
    val canvas = document.createElement(HtmlTagName.canvas)
    canvas.width = WATERMARK_TILE.toInt()
    canvas.height = WATERMARK_TILE.toInt()
    val context =
        checkNotNull(canvas.getContext(CanvasRenderingContext2D.ID)) {
            "the watermark needs a 2d context"
        }
    // The companion members of the generated unions are not reachable through Kotlin's import
    // machinery here, so the two canvas keywords are written as plain strings.
    context.textAlign = "center".unsafeCast<web.canvas.CanvasTextAlign>()
    context.textBaseline = "middle".unsafeCast<web.canvas.CanvasTextBaseline>()
    context.globalAlpha = WATERMARK_ALPHA
    context.font = "20px Microsoft Yahei"
    context.translate(WATERMARK_TILE / 2, WATERMARK_TILE / 2)
    context.rotate(-kotlin.math.PI / 4)
    context.fillText(WATERMARK_TEXT, 0.0, 0.0)
    return ZRColor(
        PatternObject(
            unsafeJso<ImagePatternObject> {
                image = ImageLike(canvas)
                repeat = "repeat"
            },
        ),
    )
}

/** One `[value, all - value]` stacked bar block of `watermark`. */
internal fun builderBars(
    entries: Array<Pair<String, Double>>,
    stack: String,
    gridIndex: Double?,
): Array<SeriesOption> {
    val visible =
        barSeries {
            this.stack = stack
            z = 3.0
            xAxisIndex = gridIndex
            yAxisIndex = gridIndex
            label =
                unsafeJso<LabelOption> {
                    position = LabelPosition("right")
                    show = true
                }
            typedData = numbers(*valuesOf(entries))
        }
    val background =
        barSeries {
            this.stack = stack
            silent = true
            xAxisIndex = gridIndex
            yAxisIndex = gridIndex
            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#eee") }
            typedData = numbers(*restOf(entries))
        }
    return arrayOf<SeriesOption>(visible, background)
}

/** One drilled-down level of `bar-drilldown`: its `groupId`, its categories and its values. */
internal class DrilldownLevel(
    val groupId: String,
    val names: Array<String>,
    val values: DoubleArray,
)

/** The drilling data of `bar-drilldown`, keyed by the `groupId` of the top level bars. */
internal val DRILLDOWN_LEVELS =
    arrayOf(
        DrilldownLevel(
            "animals",
            arrayOf("Cats", "Dogs", "Cows", "Sheep", "Pigs"),
            doubleArrayOf(4.0, 2.0, 1.0, 2.0, 1.0),
        ),
        DrilldownLevel("fruits", arrayOf("Apples", "Oranges"), doubleArrayOf(4.0, 2.0)),
        DrilldownLevel("cars", arrayOf("Toyota", "Opel", "Volkswagen"), doubleArrayOf(4.0, 2.0, 2.0)),
    )

/** The top level categories of `bar-drilldown`. */
internal val DRILLDOWN_TOP = arrayOf("Animals", "Fruits", "Cars")

/** The values of the top level bars of `bar-drilldown`. */
internal val DRILLDOWN_TOP_VALUES = doubleArrayOf(5.0, 2.0, 4.0)

/** The `universalTransition` both levels of `bar-drilldown` use. */
internal fun drilldownTransition(): BooleanOrUniversalTransitionOption =
    BooleanOrUniversalTransitionOption(
        unsafeJso<UniversalTransitionOption> {
            enabled = true
            divideShape = "clone"
        },
    )

/** The `sales` series of `bar-drilldown` for one level. */
internal fun drilldownSeries(groupId: String, values: DoubleArray): BarSeriesOption =
    barSeries {
        id = "sales"
        dataGroupId = OptionId(groupId)
        typedData = numbers(*values)
        universalTransition = drilldownTransition()
    }

/** The option of the first level of `bar-drilldown`. */
internal fun drilldownTopOption(): EChartsOption =
    unsafeJso<EChartsOption> {
        xAxis = one(unsafeJso<XAxisOption> { data = strings(*DRILLDOWN_TOP) })
        yAxis = one(unsafeJso<YAxisOption> {})
        animationDurationUpdate = AnimationDuration(500.0)
        series = arrayOf<SeriesOption>(drilldownSeries("", DRILLDOWN_TOP_VALUES))
    }

/** Axis labels that rotate 30 degrees and keep every category, the y axes of `watermark`. */
internal fun rotatedLabels(): AxisLabelOption =
    unsafeJso<AxisLabelOption> {
        interval = AxisInterval(0.0)
        rotate = 30.0
    }

/** The Back caption that the drilled-down level adds; clicking it returns to [drilldownTopOption]. */
internal fun backElement(onBack: () -> Unit): GraphicComponentLooseOption =
    GraphicComponentLooseOption(
        unsafeJso<GraphicComponentOption> {
            elements =
                arrayOf(
                    unsafeJso<GraphicElement> {
                        type = "text"
                        left = StringOrDouble("50")
                        top = StringOrDouble("20")
                        style =
                            unsafeJso<ZRStyleProps> {
                                text = "Back"
                                fontSize = StringOrDouble(18.0)
                            }
                        onclick = { onBack() }
                    },
                )
        },
    )

/** Number of bars of `bar-race`; the labels come from the same count. */
internal const val RACE_SIZE = 5

/** Label of the bar at [index]: `A`, `B`, ... like the original. */
internal fun raceLabel(index: Int): String = ('A' + index).toString()

/** Option of `bar-race` over [values]; the timer only replaces the series. */
internal fun raceOption(values: DoubleArray): EChartsOption =
    unsafeJso<EChartsOption> {
        xAxis = one(unsafeJso<XAxisOption> { max = AxisMinMax("dataMax") })
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    type = AxisTypes.category
                    data = strings(*Array(RACE_SIZE) { raceLabel(it) })
                    inverse = true
                    animationDuration = AnimationDuration(300.0)
                    animationDurationUpdate = AnimationDuration(300.0)
                    // Only the largest three bars are visible at a time.
                    max = AxisMinMax(2.0)
                },
            )
        series =
            arrayOf<SeriesOption>(
                barSeries {
                    realtimeSort = true
                    name = "X"
                    typedData = numbers(*values)
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            position = LabelPosition("right")
                            valueAnimation = true
                        }
                },
            )
        legend = one(unsafeJso<LegendOption> { show = true })
        animationDuration = AnimationDuration(0.0)
        animationDurationUpdate = AnimationDuration(3000.0)
        animationEasing = AnimationEasings.linear
        animationEasingUpdate = AnimationEasings.linear
    }
