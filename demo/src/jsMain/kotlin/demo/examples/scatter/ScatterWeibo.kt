package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.loadWeibo
import demo.examples.shared.weiboScatterOption

private val scatterWeiboCategory = Category(Groups.SCATTER, "examples/scatter/ScatterWeibo.kt")

val scatterWeiboEntry: CatalogEntry =
    scatterWeiboCategory.interactive(
            "scatter-weibo",
            "微博签到数据点亮中国",
            "Sign in of weibo",
            "11.8 万个签到点按强度分三组画在 `geo` 上，`large` 与扁平数据数组让页面撑得住这个量级。",
            "118k check-in points in three strength groups drawn on a `geo`; `large` plus flat data " +
                "arrays are what keep the page usable at that size.",
        ) { chart ->
            chart.showLoading(null)
            loadWeibo().then { table ->
                if (!chart.isDisposed()) {
                    chart.hideLoading()
                    chart.setOption(weiboScatterOption(table))
                }
                null
            }
        }
