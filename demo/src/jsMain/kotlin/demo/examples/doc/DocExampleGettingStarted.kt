package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.legendNames

private val docExampleGettingStartedCategory = Category(Groups.DOC, "examples/doc/DocExampleGettingStarted.kt")

val docExampleGettingStartedEntry: CatalogEntry =
    docExampleGettingStartedCategory.chart("doc-example/getting-started", "快速上手示例", "Getting started") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "ECharts Getting Started Example" })
                tooltip = one(unsafeJso<TooltipOption> {})
                legend = one(unsafeJso<LegendOption> { data = legendNames("sales") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings("Shirts", "Cardigans", "Chiffons", "Pants", "Heels", "Socks")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    one(
                        barSeries {
                            name = "sales"
                            typedData = numbers(5.0, 20.0, 36.0, 10.0, 10.0, 20.0)
                        },
                    )
            }
        }
