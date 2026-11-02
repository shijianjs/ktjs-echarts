package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.sunburst.radius

private val docExampleTutorialStylingStep1Category = Category(Groups.DOC, "examples/doc/DocExampleTutorialStylingStep1.kt")

val docExampleTutorialStylingStep1Entry: CatalogEntry =
    docExampleTutorialStylingStep1Category.chart("doc-example/tutorial-styling-step1", "教程：南丁格尔玫瑰图", "Tutorial: rose pie") {
            unsafeJso<EChartsOption> {
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                        },
                    )
            }
        }
