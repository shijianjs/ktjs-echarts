package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.sunburst.radius

internal class DocExampleTutorialStylingStep0 {
    companion object {
        private val CATEGORY = Category(Groups.DOC, "examples/doc/DocExampleTutorialStylingStep0.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart("doc-example/tutorial-styling-step0", "教程：朴素饼图", "Tutorial: plain pie") {
                    unsafeJso<EChartsOption> {
                        series = one(pieSeries { name = "访问来源"; radius = StringOrDoubleOrArray("55%"); typedData = visitSource() })
                    }
                }
    }
}
