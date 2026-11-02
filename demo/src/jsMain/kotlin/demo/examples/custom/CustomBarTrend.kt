package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import echarts.DataZoomTypes
import echarts.TooltipTriggers
import echarts.VisualProps
import echarts.options.BarGridLayoutOptionForCustomSeries
import echarts.options.CustomPathOption
import echarts.options.DataZoomOption
import echarts.options.DimensionLoose
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LegendOption
import echarts.options.OptionDataItem
import echarts.options.OptionDataValue
import echarts.options.OptionEncodeValue
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.customSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import zrender.ZRColor
import zrender.ZRStyleProps
import demo.examples.Category
import demo.examples.scatter.point
import demo.examples.shared.legendNames
import demo.examples.shared.textOf

private val customBarTrendCategory = Category(Groups.CUSTOM, "examples/custom/CustomBarTrend.kt")

val customBarTrendEntry: CatalogEntry =
    customBarTrendCategory.chart("custom-bar-trend", "柱状图趋势", "Custom Bar Trend") {
            val random = sequence(20260614L)
            val yearCount = 7
            val categoryCount = 30
            val categories = List(categoryCount) { "category$it" }
            val years = List(yearCount) { (2010 + it).toString() }
            val legendItems = listOf("trend") + years
            val seriesValues = List(yearCount) { mutableListOf<Double>() }
            val customRows =
                List(categoryCount) { index ->
                    val first = kotlin.math.round(random() * 1000 * 100) / 100
                    val values = mutableListOf(first)
                    seriesValues[0] += first
                    for (j in 1 until yearCount) {
                        val previous = seriesValues[j - 1][index]
                        val value = kotlin.math.round(kotlin.math.max(0.0, previous + (random() - 0.5) * 200) * 100) / 100
                        seriesValues[j] += value
                        values += value
                    }
                    mutableListOf<OptionDataValue?>(OptionDataValue(index.toDouble())).also { list ->
                        values.forEach { list += OptionDataValue(it) }
                    }
                }
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                this.legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames(*legendItems.toTypedArray())
                            top = StringOrDouble(20.0)
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            start = 50.0
                            end = 70.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            start = 50.0
                            end = 70.0
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings(*categories.toTypedArray())
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        customSeries {
                            name = "trend"
                            itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 2.0 }
                            encode =
                                xyEncode(
                                    OptionEncodeValue(dim(0)),
                                    OptionEncodeValue(Array(yearCount) { dim(it + 1) }),
                                )
                            typedData = Array<OptionDataItem?>(customRows.size) { OptionDataItem(customRows[it].toTypedArray()) }
                            z = 100.0
                            renderItem = { params, api ->
                                val x = api.value(dim(0))
                                val indices = api.currentSeriesIndices()
                                val layout =
                                    api.barLayout(
                                        unsafeJso<BarGridLayoutOptionForCustomSeries> {
                                            barGap = StringOrDouble("30%")
                                            barCategoryGap = StringOrDouble("20%")
                                            count = indices.size.toDouble() - 1
                                        },
                                    )
                                val points = mutableListOf<Array<Double>>()
                                indices.forEachIndexed { index, seriesIndex ->
                                    if (seriesIndex != params.seriesIndex) {
                                        val point = api.coord(coord(x, api.value(DimensionLoose(seriesIndex))))
                                        point[0] += layout?.get(index - 1)?.offsetCenter ?: 0.0
                                        point[1] -= 20
                                        points += point
                                    }
                                }
                                unsafeJso<CustomPathOption> {
                                    type = "polyline"
                                    shape =
                                        unsafeJso {
                                            this.points = points.toTypedArray()
                                        }
                                    style = api.style(unsafeJso<ZRStyleProps> { stroke = ZRColor(textOf(api.visual(VisualProps.color))); fill = ZRColor("none") })
                                }
                            }
                        },
                        *seriesValues
                            .mapIndexed { index, values ->
                                barSeries {
                                    animation = false
                                    name = years[index]
                                    itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.5 }
                                    typedData = numbers(*values.toDoubleArray())
                                } as SeriesOption
                            }
                            .toTypedArray(),
                    )
            }
        }
