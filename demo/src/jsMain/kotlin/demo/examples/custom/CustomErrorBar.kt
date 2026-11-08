package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.DataZoomTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.DataZoomOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataItem
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.OptionEncodeValue
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
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
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.numberArray
import demo.examples.shared.row

private val customErrorBarCategory = Category(Groups.CUSTOM, "examples/custom/CustomErrorBar.kt")

val customErrorBarEntry: CatalogEntry =
    customErrorBarCategory.chart("custom-error-bar", "误差棒图", "Error Bar Chart") {
            val random = sequence(20260612L)
            val categories = List(100) { "category$it" }
            val barValues = mutableListOf<Double>()
            val errorValues =
                List(100) { index ->
                    val value = random() * 1000
                    barValues += kotlin.math.round(value * 100) / 100
                    row(
                        index.toDouble(),
                        kotlin.math.round(kotlin.math.max(0.0, value - random() * 100)),
                        kotlin.math.round(value + random() * 80),
                    )
                }
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                title = one(unsafeJso<TitleOption> { text = "Error bar chart" })
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("bar", "error")
                            top = StringOrDouble(20.0)
                            right = StringOrDouble(30.0)
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
                            data =
                                strings(*categories.toTypedArray())
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "bar"
                            typedData =
                                numbers(*barValues.toDoubleArray())
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#77bef7") }
                        },
                        customSeries {
                            name = "error"
                            itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 1.5 }
                            encode = xyEncode(OptionEncodeValue(dim(0)), OptionEncodeValue(arrayOf(dim(1), dim(2))))
                            typedData = Array<OptionDataItem?>(errorValues.size) { OptionDataItem(errorValues[it]) }
                            z = 100.0
                            renderItem = { _, api ->
                                val x = api.value(dim(0))
                                val high = api.coord(coord(x, api.value(dim(1))))
                                val low = api.coord(coord(x, api.value(dim(2))))
                                val halfWidth = numberArray(api.size(extent(OptionDataValue(1.0), OptionDataValue(0.0))))[0] * 0.1
                                val style = api.style()
                                groupElement(
                                    lineElement(style) {
                                        x1 = high[0] - halfWidth
                                        y1 = high[1]
                                        x2 = high[0] + halfWidth
                                        y2 = high[1]
                                    },
                                    lineElement(style) {
                                        x1 = high[0]
                                        y1 = high[1]
                                        x2 = low[0]
                                        y2 = low[1]
                                    },
                                    lineElement(style) {
                                        x1 = low[0] - halfWidth
                                        y1 = low[1]
                                        x2 = low[0] + halfWidth
                                        y2 = low[1]
                                    },
                                )
                            }
                        },
                    )
            }
        }
