package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.sunburst.radius

private val docExampleTutorialStylingStep0Category = Category(Groups.DOC, "examples/doc/DocExampleTutorialStylingStep0.kt")

val docExampleTutorialStylingStep0Entry: CatalogEntry =
    docExampleTutorialStylingStep0Category.chart("doc-example/tutorial-styling-step0", "教程：朴素饼图", "Tutorial: plain pie") {
            unsafeJso<EChartsOption> {
                series = one(pieSeries { name = "访问来源"; radius = StringOrDoubleOrArray("55%"); typedData = visitSource() })
            }
        }
