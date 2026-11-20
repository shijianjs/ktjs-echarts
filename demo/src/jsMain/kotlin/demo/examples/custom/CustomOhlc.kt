package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category

internal class CustomOhlc {
    companion object {
        private val CATEGORY = Category(Groups.CUSTOM, "examples/custom/CustomOhlc.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive("custom-ohlc", "OHLC 图（使用自定义系列）", "OHLC Chart") { chart ->
                    loadJson("data/stock-DJI.json").then { json ->
                        chart.setOption(ohlcOption(splitOhlc(json.unsafeCast<Array<Array<JsAny>>>())), true)
                        null
                    }
                }
    }
}
