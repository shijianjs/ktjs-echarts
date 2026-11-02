package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import echarts.options.CustomPathOption
import echarts.options.DimensionDefinitionLoose
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.OptionDataItem
import echarts.options.OptionDataValue
import echarts.options.OptionEncodeValue
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.customSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.numberArray
import demo.examples.shared.numeric
import demo.examples.shared.row
import demo.examples.shared.styledItem

private val customProfitCategory = Category(Groups.CUSTOM, "examples/custom/CustomProfit.kt")

val customProfitEntry: CatalogEntry =
    customProfitCategory.chart("custom-profit", "利润分布直方图", "Profit") {
            val colors = listOf("#4f81bd", "#c0504d", "#9bbb59", "#604a7b", "#948a54", "#e46c0b")
            val bars =
                listOf(
                    row(10.0, 16.0, 3.0, "A"),
                    row(16.0, 18.0, 15.0, "B"),
                    row(18.0, 26.0, 12.0, "C"),
                    row(26.0, 32.0, 22.0, "D"),
                    row(32.0, 56.0, 7.0, "E"),
                    row(56.0, 62.0, 17.0, "F"),
                )
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Profit"; left = StringOrDouble("center") })
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis = one(unsafeJso<XAxisOption> { scale = true })
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        customSeries {
                            typedData =
                                Array<OptionDataItem?>(bars.size) { index ->
                                    styledItem(bars[index], colors[index])
                                }
                            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("top") }
                            dimensions =
                                arrayOf(
                                    DimensionDefinitionLoose("from"),
                                    DimensionDefinitionLoose("to"),
                                    DimensionDefinitionLoose("profit"),
                                )
                            encode =
                                unsafeJso {
                                    this.x = OptionEncodeValue(arrayOf(dim(0), dim(1)))
                                    this.y = OptionEncodeValue(dim(2))
                                    tooltip = OptionEncodeValue(arrayOf(dim(0), dim(1), dim(2)))
                                    itemName = OptionEncodeValue(dim(3))
                                }
                            renderItem = { _, api ->
                                val from = api.value(dim(0))
                                val to = api.value(dim(1))
                                val profit = api.value(dim(2))
                                val start = api.coord(coord(from, profit))
                                val size =
                                    numberArray(
                                        api.size(
                                            extent(
                                                OptionDataValue(numeric(to) - numeric(from)),
                                                profit,
                                            ),
                                        ),
                                    )
                                unsafeJso<CustomPathOption> {
                                    type = "rect"
                                    shape =
                                        unsafeJso {
                                            x = start[0]
                                            y = start[1]
                                            width = size[0]
                                            height = size[1]
                                        }
                                    style = api.style()
                                }
                            }
                        },
                    )
            }
        }
