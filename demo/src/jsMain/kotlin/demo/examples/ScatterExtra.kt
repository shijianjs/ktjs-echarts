package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * Scatter examples of the second round: the large-data pages, the axis-less colour page of
 * `masterPainterColorChoice.json`, and the single-axis punch card.
 *
 * Changed on purpose:
 *
 * - `scatter-large` generates its million points with [Lcg] instead of `Math.random()`, so the
 *   picture of the page is the same on every reload;
 * - the regression pages of the original (`scatter-linear-regression`,
 *   `scatter-exponential-regression`, `scatter-logarithmic-regression`,
 *   `scatter-polynomial-regression`) are not ported: they need `echarts.registerTransform(
 *   ecStat.transform.regression)`, a transform of the separate `echarts-stat` package;
 * - `scatter-painter-choice` loads `masterPainterColorChoice.json` from
 *   `demo/src/jsMain/resources/data/` (see its `SOURCE.md`) instead of `$.get`; the tooltip and the
 *   hue axis are the ones of the original.
 */

private val scatterExtra = Category(Groups.SCATTER, "examples/ScatterExtra.kt")

/** `scatter-large` plots one million points; the count is the original `5e5` per series. */
private const val LARGE_SCATTER_POINTS = 500_000

/**
 * The `[x, y]` table of `scatter-large`.
 *
 * The original writes into a `Float32Array` and adds a random jitter to the sine curve; the
 * generate-then-read order is the same here, so the values match the upstream shape while staying
 * reproducible.
 */
private fun largeScatterData(random: Lcg, offset: Double): Array<ScatterDataItem?> {
    val x = DoubleArray(LARGE_SCATTER_POINTS)
    val y = DoubleArray(LARGE_SCATTER_POINTS)
    for (index in 0 until LARGE_SCATTER_POINTS) {
        val xValue = random.next() * 10
        x[index] = xValue
        y[index] = kotlin.math.sin(xValue) - xValue * (if (LARGE_SCATTER_POINTS % 2 == 0) -0.1 else 0.1) * random.next() + offset / 10
    }
    val points = Array(LARGE_SCATTER_POINTS) { index ->
        OptionDataItem(Array(2) { column -> OptionDataValue(if (column == 0) x[index] else y[index]) })
    }
    return points.unsafeCast<Array<ScatterDataItem?>>()
}

/**
 * Shape of `masterPainterColorChoice.json`: the painters as column arrays plus their colours.
 *
 * `x` holds the years as **strings** (the plotly export writes `"1007"`), which the original
 * converts with the unary `+` operator; a Kotlin `Double` cast would reject them.
 */
private external interface PainterColorData {
    val x: Array<JsAny>
    val y: Array<JsAny>
    val marker: PainterColorMarker
}

/** The per-point symbol size and fill colour of [PainterColorData]. */
private external interface PainterColorMarker {
    val size: Array<Double>
    val sizeref: Double
    val color: Array<String>
}

/**
 * `scatter-large` of the original draws the two series in one chart: `A` on the plain sine and `B`
 * lifted by `10 / 10` of the offset, both in large mode.
 */
private fun largeScatterSeries(name: String, data: Array<ScatterDataItem?>): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        typedData = data
        dimensions = arrayOf(DimensionDefinitionLoose("x"), DimensionDefinitionLoose("y"))
        symbolSize = SymbolSize(3.0)
        itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.4 }
        large = true
    }

/** The hours of `scatter-single-axis`. */
private val SINGLE_AXIS_HOURS =
    arrayOf("12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a",
        "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p")

/** The days of `scatter-single-axis`, top to bottom. */
private val SINGLE_AXIS_DAYS =
    arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/**
 * The punch card of `scatter-single-axis`: for every day, the commits per hour. `rows[i][h]` is the
 * value the original pushes as `[hour, commits]` into the series of day `i`.
 */
private val SINGLE_AXIS_VALUES: Array<DoubleArray> =
    arrayOf(
        doubleArrayOf(5.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 4.0, 1.0, 1.0, 3.0, 4.0, 6.0, 4.0, 4.0, 3.0, 3.0, 2.0, 5.0),
        doubleArrayOf(7.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 2.0, 2.0, 6.0, 9.0, 11.0, 6.0, 7.0, 8.0, 12.0, 5.0, 5.0, 7.0, 2.0),
        doubleArrayOf(1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 2.0, 1.0, 9.0, 8.0, 10.0, 6.0, 5.0, 5.0, 5.0, 7.0, 4.0, 2.0, 4.0),
        doubleArrayOf(7.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 5.0, 4.0, 7.0, 14.0, 13.0, 12.0, 9.0, 5.0, 5.0, 10.0, 6.0, 4.0, 4.0, 1.0),
        doubleArrayOf(1.0, 3.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 4.0, 4.0, 2.0, 4.0, 4.0, 14.0, 12.0, 1.0, 8.0, 5.0, 3.0, 7.0, 3.0, 0.0),
        doubleArrayOf(2.0, 1.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 4.0, 1.0, 5.0, 10.0, 5.0, 7.0, 11.0, 6.0, 0.0, 5.0, 3.0, 4.0, 2.0, 0.0),
        doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 1.0, 3.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 2.0, 6.0),
    )

/** The `[hour, commits]` points of one day of `scatter-single-axis`. */
private fun singleAxisPoints(day: Int): Array<ScatterDataItem?> =
    points(
        *Array(SINGLE_AXIS_HOURS.size) { hour ->
            doubleArrayOf(hour.toDouble(), SINGLE_AXIS_VALUES[day][hour])
        },
    )

val scatterExtraEntries: List<CatalogEntry> =
    listOf(
        scatterExtra.chart("scatter-large", "大规模散点图", "Large Scatter") {
            val random = Lcg(20260428)
            val dataA = largeScatterData(random, 0.0)
            val dataB = largeScatterData(random, 10.0)
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "${formatThousands(dataA.size + dataB.size)} Points"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> {})
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            left = StringOrDouble("center")
                            feature = unsafeJso<ToolboxFeature> { dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> {} }
                        },
                    )
                legend = one(unsafeJso<LegendOption> { orient = Orients.vertical; right = StringOrDouble(10.0) })
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> {})
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside },
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.slider },
                    )
                animation = false
                series = arrayOf<SeriesOption>(largeScatterSeries("A", dataA), largeScatterSeries("B", dataB))
            }
        },
        scatterExtra.interactive("scatter-painter-choice", "历代绘画大师的色彩运用", "Master Painter Color Choices Throughout History") { chart ->
            loadJson("data/masterPainterColorChoice.json").then { json ->
                val painters = json.unsafeCast<Array<PainterColorData>>()[0]
                val data: Array<ScatterDataItem?> = painterPoints(painters)
                val colorOf = ZRColor(painterColors(painters.marker.color))
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Master Painter Color Choices Throughout History"
                                    subtext = "Data From Plot.ly"
                                    left = StringOrDouble("right")
                                },
                            )
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.value
                                    splitLine = unsafeJso<SplitLineOption> { show = false }
                                    scale = true
                                    splitNumber = 5.0
                                    max = AxisMinMax("dataMax")
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            formatter =
                                                LabelFormatter(
                                                    js("(value) => value + 's'") as LabelFormatterCallback,
                                                )
                                        }
                                },
                            )
                        yAxis =
                            one(
                                unsafeJso<YAxisOption> {
                                    type = AxisTypes.value
                                    min = AxisMinMax(0.0)
                                    max = AxisMinMax(360.0)
                                    interval = AxisInterval(60.0)
                                    name = "Hue"
                                    splitLine = unsafeJso<SplitLineOption> { show = false }
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                scatterSeries {
                                    name = "scatter"
                                    symbolSize =
                                        SymbolSize { _, params ->
                                            val index = params.dataIndex
                                            DoubleOrDoubleArray(painters.marker.size[index] / painters.marker.sizeref)
                                        }
                                    itemStyle = unsafeJso<ItemStyleOption> { color = colorOf }
                                    typedData = data
                                },
                            )
                    },
                )
                null
            }
        },
        scatterExtra.chart("scatter-single-axis", "单轴散点图", "Scatter on Single Axis") {
            val titles =
                Array(SINGLE_AXIS_DAYS.size) { index ->
                    unsafeJso<TitleOption> {
                        text = SINGLE_AXIS_DAYS[index]
                        top = StringOrDouble(((index + 0.5) * 100) / 7)
                    }
                }
            val axes =
                Array(SINGLE_AXIS_DAYS.size) { index ->
                    unsafeJso<SingleAxisOption> {
                        left = StringOrDouble(150.0)
                        type = AxisTypes.category
                        boundaryGap = BooleanOrStringArray(false)
                        data = strings(*SINGLE_AXIS_HOURS)
                        top = StringOrDouble((index * 100.0) / 7.0 + 5.0)
                        height = StringOrDouble(100.0 / 7.0 - 10.0)
                        axisLabel = unsafeJso<AxisLabelOption> { interval = AxisInterval(2.0) }
                    }
                }
            val series =
                Array(SINGLE_AXIS_DAYS.size) { index ->
                    scatterSeries {
                        singleAxisIndex = index.toDouble()
                        coordinateSystem = "singleAxis"
                        typedData = singleAxisPoints(index)
                        symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 1) * 4.0) }
                    }
                }
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { position = TooltipPosition("top") })
                title = titles
                singleAxis = axes
                this.series = Array<SeriesOption>(series.size) { series[it] }
            }
        },
    )

/**
 * The `[year, hue]` points of `scatter-painter-choice`.
 *
 * The year column of the upstream table is a string, so both columns go through `Number(...)` the
 * way the unary `+` of the original does.
 */
private fun painterPoints(painters: PainterColorData): Array<ScatterDataItem?> {
    val rows =
        Array(painters.x.size) { index ->
            doubleArrayOf(toNumber(painters.x[index]), toNumber(painters.y[index]))
        }
    return points(*rows)
}

/** `Number(value)` of a JSON column entry. */
private fun toNumber(value: JsAny?): Double = js("(value) => Number(value)")(value) as Double

/**
 * The per-point colour callback of `scatter-painter-choice`.
 *
 * ECharts calls `itemStyle.color` with the params of the data item, so the colour table is bound to
 * the callback once instead of being looked up per call.
 */
private fun painterColors(table: Array<String>): (CallbackDataParams) -> String =
    js("(table) => function (params) { return table[params.dataIndex]; }")(table)
        as (CallbackDataParams) -> String

/** `12345` as `"12,345"`, the formatting `echarts.format.addCommas` applies to the title. */
private fun formatThousands(value: Int): String = value.toString().reversed().chunked(3).joinToString(",").reversed()
