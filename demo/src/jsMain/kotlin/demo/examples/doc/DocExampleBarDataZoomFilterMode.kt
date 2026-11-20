package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.Category

internal class DocExampleBarDataZoomFilterMode {
    companion object {
        private val CATEGORY = Category(Groups.DOC, "examples/doc/DocExampleBarDataZoomFilterMode.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive(
                    "doc-example/bar-dataZoom-filterMode",
                    "数据缩放过滤模式（filterMode）",
                    "Bar with a vertical data zoom and filterMode",
                ) { chart, _ ->
                    chart.showLoading(null)
                    loadJson("data/obama_budget_proposal_2012.list.json").then { json ->
                        chart.hideLoading()
                        chart.setOption(budgetOption(json.unsafeCast<ObamaBudgetJson>()))
                    }
                }
    }
}
