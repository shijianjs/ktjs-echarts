package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.DataZoomTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.VisualProps
import io.github.shijianjs.ktjs.echarts.options.BarGridLayoutOptionForCustomSeries
import io.github.shijianjs.ktjs.echarts.options.CustomPathOption
import io.github.shijianjs.ktjs.echarts.options.DataZoomOption
import io.github.shijianjs.ktjs.echarts.options.DimensionLoose
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataItem
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.OptionEncodeValue
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.customSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRStyleProps
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
