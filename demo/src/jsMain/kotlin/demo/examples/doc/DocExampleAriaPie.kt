package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.pieItem

private val docExampleAriaPieCategory = Category(Groups.DOC, "examples/doc/DocExampleAriaPie.kt")

val docExampleAriaPieEntry: CatalogEntry =
    docExampleAriaPieCategory.chart("doc-example/aria-pie", "无障碍饼图", "Aria pie") {
            unsafeJso<EChartsOption> {
                aria = unsafeJso<AriaOption> { enabled = true }
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "某站点用户访问来源"
                            left = StringOrDouble("center")
                        },
                    )
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            typedData =
                                dataItems(
                                    pieItem("直接访问", 335.0),
                                    pieItem("邮件营销", 310.0),
                                    pieItem("联盟广告", 234.0),
                                    pieItem("视频广告", 135.0),
                                    pieItem("搜索引擎", 1548.0),
                                )
                        },
                    )
            }
        }
