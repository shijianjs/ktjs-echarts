package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.Category

private val docExampleCandlestickAxisPointerCategory = Category(Groups.DOC, "examples/doc/DocExampleCandlestickAxisPointer.kt")

val docExampleCandlestickAxisPointerEntry: CatalogEntry =
    docExampleCandlestickAxisPointerCategory.interactive(
            "doc-example/candlestick-axisPointer",
            "K 线与联动的 axisPointer",
            "Candlestick with a linked axis pointer",
        ) { chart, _ ->
            chart.showLoading(null)
            loadJson("data/stock-DJI.json").then { json ->
                chart.hideLoading()
                chart.setOption(stockOption(json.unsafeCast<Array<Array<JsAny>>>()), true)
            }
        }
