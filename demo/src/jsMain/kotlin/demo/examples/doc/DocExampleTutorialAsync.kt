package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.legendNames

private val docExampleTutorialAsyncCategory = Category(Groups.DOC, "examples/doc/DocExampleTutorialAsync.kt")

val docExampleTutorialAsyncEntry: CatalogEntry =
    docExampleTutorialAsyncCategory.interactive(
            "doc-example/tutorial-async",
            "异步数据加载（不设 loading）",
            "Asynchronous data loading",
        ) { chart, holder ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "异步数据加载示例" })
                    tooltip = one(unsafeJso<TooltipOption> {})
                    legend = one(unsafeJso<LegendOption> { data = legendNames("销量") })
                    xAxis = one(unsafeJso<XAxisOption> { data = strings() })
                    yAxis = one(unsafeJso<YAxisOption> {})
                    series = arrayOf<SeriesOption>(barSeries { name = "销量"; typedData = numbers() })
                },
            )
            // The original resolves a `setTimeout(1000)` callback, then merges only the two parts the
            // response carries; the series is found by name, exactly like the tutorial explains.
            holder.setTimeout({
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        xAxis = one(unsafeJso<XAxisOption> { data = strings("衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子") })
                        series =
                            arrayOf<SeriesOption>(
                                barSeries {
                                    name = "销量"
                                    typedData = numbers(5.0, 20.0, 36.0, 10.0, 10.0, 20.0)
                                },
                            )
                    },
                )
            }, 1000)
        }
