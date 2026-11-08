package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SymbolShape
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRLineTypes
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
                            lineStyle = unsafeJso<LineStyleOption> { color = io.github.shijianjs.ktjs.zrender.ZRColor("#5470C6"); width = 4.0; type = ZRLineTypes.dashed }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderWidth = 3.0
                                    borderColor = io.github.shijianjs.ktjs.zrender.ZRColor("#EE6666")
                                    color = io.github.shijianjs.ktjs.zrender.ZRColor("yellow")
                                }
                            typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0)
                        },
                    )
            }
        }
