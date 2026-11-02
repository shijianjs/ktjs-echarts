package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.lineSeries
import echarts.options.numbers
import js.objects.unsafeJso
import zrender.ZRLineTypes
import demo.examples.Category

private val lineStyleCategory = Category(Groups.LINE, "examples/line/LineStyle.kt")

val lineStyleEntry: CatalogEntry =
    lineStyleCategory.chart("line-style", "自定义折线图样式", "Line Style and Item Style") {
            unsafeJso<EChartsOption> {
                xAxis = categoryAxis(*week)
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            symbol = SymbolShape("triangle")
                            symbolSize = SymbolSize(20.0)
                            lineStyle = unsafeJso<LineStyleOption> { color = zrender.ZRColor("#5470C6"); width = 4.0; type = ZRLineTypes.dashed }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderWidth = 3.0
                                    borderColor = zrender.ZRColor("#EE6666")
                                    color = zrender.ZRColor("yellow")
                                }
                            typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0)
                        },
                    )
            }
        }
