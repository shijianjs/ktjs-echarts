package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val docExampleTutorialDynamicDataCategory = Category(Groups.DOC, "examples/doc/DocExampleTutorialDynamicData.kt")

val docExampleTutorialDynamicDataEntry: CatalogEntry =
    docExampleTutorialDynamicDataCategory.interactive(
            "doc-example/tutorial-dynamic-data",
            "动态数据",
            "Dynamic data",
        ) { chart, holder ->
            val series = DynamicSeries()
            chart.setOption(dynamicDataOption(series))
            holder.setInterval({
                series.add(true)
                chart.setOption(dynamicDataOption(series))
            }, 500)
        }
