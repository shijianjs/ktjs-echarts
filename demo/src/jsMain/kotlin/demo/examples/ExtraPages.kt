package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.floor
import zrender.ZRColor
import kotlin.math.round

/*
 * Pages that did not fit the family files they belong to: the confidence band of the line family and
 * the animated two-axis table of the bar family.
 *
 * `confidence-band` loads `confidence-band.json` from `demo/src/jsMain/resources/data/` (see its
 * `SOURCE.md`) instead of `$.get`, and derives the stacked lower band on the client exactly like the
 * original: the data is shifted by the floor of its smallest lower bound so that every band stays
 * positive, and the percentages in the labels are computed against that base.
 *
 * `dynamic-data` keeps the 2.1 second refresh of the original, but the two random walks are fed by
 * [Lcg] so that a reload always starts from the same picture. The chart-scoped timer comes from
 * `ChartHolder`, which clears it when the page is left.
 */

private val confidenceBand = Category(Groups.LINE, "examples/ExtraPages.kt")

private val dynamicDataPage = Category(Groups.BAR, "examples/ExtraPages.kt")

/** Shape of `confidence-band.json`: one record per day, the band around `value`. */
private external interface ConfidenceBandItem {
    /** Lower bound. */
    val l: Double

    /** Upper bound. */
    val u: Double

    /** The measured value. */
    val value: Double

    /** ISO date, used as the category of the x axis. */
    val date: String
}

/** The days a `dynamic-data` window holds. */
private const val DYNAMIC_WINDOW = 10

/** One clock label of `dynamic-data`: `hh:mm:ss` of the current time. */
private fun clockLabel(date: Date): String {
    fun pad(value: Int): String = value.toString().padStart(2, '0')
    return pad(date.getHours()) + ":" + pad(date.getMinutes()) + ":" + pad(date.getSeconds())
}

/** `value` rounded to one decimal, the precision of the order series upstream. */
private fun round1(value: Double): Double = round(value * 10) / 10

/** `(value - base) * 100` with one digit after the point, as the tooltip of the band prints it. */
private fun percent(value: Double): String {
    val tenths = round(value * 1000)
    return (tenths / 10).toString() + "%"
}

val extraPagesEntries: List<CatalogEntry> =
    listOf(
        confidenceBand.interactive("confidence-band", "置信带", "Confidence Band") { chart ->
            loadJson("data/confidence-band.json").then { json ->
                val table = json.unsafeCast<Array<ConfidenceBandItem>>()
                // The original shifts the whole table up so that the lowest bound sits at zero.
                val base = -floor(table.fold(Double.POSITIVE_INFINITY) { min, item -> minOf(min, item.l) })
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Confidence Band"
                                    subtext = "Example in MetricsGraphics.js"
                                    left = StringOrDouble("center")
                                },
                            )
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    trigger = TooltipTriggers.axis
                                    axisPointer =
                                        unsafeJso<AxisPointerOption> {
                                            type = AxisPointerTypes.cross
                                            animation = BooleanOrString(false)
                                            label =
                                                unsafeJso<AxisPointerLabelOption> {
                                                    backgroundColor = ZRColor("#ccc")
                                                    borderColor = "#aaa"
                                                    borderWidth = 1.0
                                                    shadowBlur = 0.0
                                                    shadowOffsetX = 0.0
                                                    shadowOffsetY = 0.0
                                                    color = "#222"
                                                }
                                        }
                                    formatter =
                                        TooltipFormatter(
                                            { params ->
                                                val items = params.unsafeCast<Array<CallbackDataParams>>()
                                                val last = items[2]
                                                TooltipContent(
                                                    textOf(last.name) + "<br />" +
                                                        percent(numberAt(last.value, 0) - base),
                                                )
                                            },
                                        )
                                },
                            )
                        grid =
                            one(
                                unsafeJso<GridOption> {
                                    left = StringOrDouble("3%")
                                    right = StringOrDouble("4%")
                                    bottom = StringOrDouble("3%")
                                    containLabel = true
                                },
                            )
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.category
                                    data = strings(*Array(table.size) { table[it].date })
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            formatter =
                                                LabelFormatter(
                                                    js(
                                                        "(value, index) => index === 0 ? value : " +
                                                            "(new Date(value).getMonth() + 1) + '-' + " +
                                                            "(new Date(value).getDate())",
                                                    ) as LabelFormatterCallback,
                                                )
                                        }
                                    boundaryGap = BooleanOrStringArray(false)
                                },
                            )
                        yAxis =
                            one(
                                unsafeJso<YAxisOption> {
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            formatter =
                                                LabelFormatter(
                                                    js("(offset) => function (value) {\n" +
                                                        "    return (value - offset) * 100 + '%';\n" +
                                                        "}")(base) as LabelFormatterCallback,
                                                )
                                        }
                                    axisPointer =
                                        unsafeJso<AxisPointerOption> {
                                            label =
                                                unsafeJso<AxisPointerLabelOption> {
                                                    formatter =
                                                        LabelFormatter(
                                                            js("(offset) => function (params) {\n" +
                                                                "    return ((params.value - offset) * 100).toFixed(1) + '%';\n" +
                                                                "}")(base) as LabelFormatterCallback,
                                                        )
                                                }
                                        }
                                    splitNumber = 3.0
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                lineSeries {
                                    name = "L"
                                    typedData = numbers(*DoubleArray(table.size) { table[it].l + base })
                                    lineStyle = unsafeJso<LineStyleOption> { opacity = 0.0 }
                                    stack = "confidence-band"
                                    symbol = SymbolShape("none")
                                },
                                lineSeries {
                                    name = "U"
                                    typedData = numbers(*DoubleArray(table.size) { table[it].u - table[it].l })
                                    lineStyle = unsafeJso<LineStyleOption> { opacity = 0.0 }
                                    areaStyle = unsafeJso<AreaStyleOption> { color = ZRColor("#ccc") }
                                    stack = "confidence-band"
                                    symbol = SymbolShape("none")
                                },
                                lineSeries {
                                    typedData = numbers(*DoubleArray(table.size) { table[it].value + base })
                                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#333") }
                                    showSymbol = false
                                },
                            )
                    },
                )
                null
            }
        },
        dynamicDataPage.interactive("dynamic-data", "动态数据", "Dynamic Data") { chart, holder ->
            val random = Lcg(20261028)
            val now = Date()
            val categories =
                Array(DYNAMIC_WINDOW) { index ->
                    clockLabel(Date(now.getTime() - (DYNAMIC_WINDOW - 1 - index) * 2000.0))
                }
            val categories2 = Array(DYNAMIC_WINDOW) { index -> (index + 1).toString() }
            val prices = DoubleArray(DYNAMIC_WINDOW) { round(random.next() * 1000) }
            val orders = DoubleArray(DYNAMIC_WINDOW) { round1(random.next() * 10 + 5) }
            var counter = 11

            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "Dynamic Data" })
                    tooltip =
                        one(
                            unsafeJso<TooltipOption> {
                                trigger = TooltipTriggers.axis
                                axisPointer =
                                    unsafeJso<AxisPointerOption> {
                                        type = AxisPointerTypes.cross
                                        label =
                                            unsafeJso<AxisPointerLabelOption> {
                                                backgroundColor = ZRColor("#283b56")
                                            }
                                    }
                            },
                        )
                    legend = one(unsafeJso<LegendOption> {})
                    toolbox =
                        one(
                            unsafeJso<ToolboxOption> {
                                show = true
                                feature =
                                    unsafeJso<ToolboxFeature> {
                                        dataView = unsafeJso<DataViewOption> { readOnly = false }
                                        restore = unsafeJso<ToolboxFeatureOption> {}
                                        saveAsImage = unsafeJso<SaveAsImageOption> {}
                                    }
                            },
                        )
                    dataZoom = one(unsafeJso<DataZoomOption> { show = false; start = 0.0; end = 100.0 })
                    xAxis =
                        arrayOf(
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.category
                                boundaryGap = BooleanOrStringArray(true)
                                data = strings(*categories)
                            },
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.category
                                boundaryGap = BooleanOrStringArray(true)
                                data = strings(*categories2)
                            },
                        )
                    yAxis =
                        arrayOf(
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                scale = true
                                name = "Price"
                                max = AxisMinMax(30.0)
                                min = AxisMinMax(0.0)
                                boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.2), StringOrDouble(0.2)))
                            },
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                scale = true
                                name = "Order"
                                max = AxisMinMax(1200.0)
                                min = AxisMinMax(0.0)
                                boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.2), StringOrDouble(0.2)))
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            barSeries {
                                name = "Dynamic Bar"
                                xAxisIndex = 1.0
                                yAxisIndex = 1.0
                                typedData = numbers(*prices)
                            },
                            lineSeries {
                                name = "Dynamic Line"
                                typedData = numbers(*orders)
                            },
                        )
                },
            )
            holder.setInterval({
                if (!chart.isDisposed()) {
                    categories.copyInto(categories, 0, 1)
                    categories[DYNAMIC_WINDOW - 1] = clockLabel(Date())
                    categories2.copyInto(categories2, 0, 1)
                    categories2[DYNAMIC_WINDOW - 1] = (counter++).toString()
                    prices.copyInto(prices, 0, 1)
                    prices[DYNAMIC_WINDOW - 1] = round(random.next() * 1000)
                    orders.copyInto(orders, 0, 1)
                    orders[DYNAMIC_WINDOW - 1] = round1(random.next() * 10 + 5)
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            xAxis =
                                arrayOf(
                                    unsafeJso<XAxisOption> { data = strings(*categories) },
                                    unsafeJso<XAxisOption> { data = strings(*categories2) },
                                )
                            series =
                                arrayOf<SeriesOption>(
                                    barSeries { typedData = numbers(*prices) },
                                    lineSeries { typedData = numbers(*orders) },
                                )
                        },
                    )
                }
            }, 2100)
        },
    )
