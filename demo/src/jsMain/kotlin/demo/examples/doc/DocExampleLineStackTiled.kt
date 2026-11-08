package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val docExampleLineStackTiledCategory = Category(Groups.DOC, "examples/doc/DocExampleLineStackTiled.kt")

val docExampleLineStackTiledEntry: CatalogEntry =
    docExampleLineStackTiledCategory.chart("doc-example/line-stack-tiled", "折线图堆叠与平铺", "Stacked and tiled lines") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "某楼盘销售情况"
                            subtext = "纯属虚构"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend = one(unsafeJso<LegendOption> { data = legendNames("意向", "预购", "成交") })
                toolbox = toolbox(magicType = true, saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings("周一", "周二", "周三", "周四", "周五", "周六", "周日")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "成交"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(10.0, 12.0, 21.0, 54.0, 260.0, 830.0, 710.0)
                        },
                        lineSeries {
                            name = "预购"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(30.0, 182.0, 434.0, 791.0, 390.0, 30.0, 10.0)
                        },
                        lineSeries {
                            name = "意向"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(1320.0, 1132.0, 601.0, 234.0, 120.0, 90.0, 20.0)
                        },
                    )
            }
        }
