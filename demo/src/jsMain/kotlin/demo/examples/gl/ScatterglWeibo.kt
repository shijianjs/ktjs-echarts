package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.loadWeibo
import demo.examples.shared.weiboGlOption

private val scatterglWeiboCategory = Category(Groups.GL, "examples/gl/ScatterglWeibo.kt")

val scatterglWeiboEntry: CatalogEntry =
    scatterglWeiboCategory.interactive(
            "scattergl-weibo",
            "微博签到数据点亮中国 - WebGL",
            "Sign in of weibo (WebGL)",
            "同一份签到表交给 `scatterGL` 在 GPU 上画，漫游地图时依然流畅；GL 渲染器不读 itemStyle 的阴影。",
            "The same table drawn by `scatterGL` on the GPU, so roaming stays smooth; the GL renderer " +
                "does not read the shadow keys of `itemStyle`.",
        ) { chart ->
            chart.showLoading(null)
            loadWeibo().then { table ->
                if (!chart.isDisposed()) {
                    chart.hideLoading()
                    chart.setOption(weiboGlOption(table))
                }
                null
            }
        }
