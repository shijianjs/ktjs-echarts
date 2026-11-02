package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.shared.every

private val scatterStreamVisualCategory = Category(Groups.SCATTER, "examples/scatter/ScatterStreamVisual.kt")

val scatterStreamVisualEntry: CatalogEntry =
    scatterStreamVisualCategory.interactive(
            "scatter-stream-visual",
            "流式渲染与视觉映射",
            "Visual interaction with stream",
            "一万六千套房的面积与总价铺成散点，连续的 visualMap 按总价把点从金色渐变到蓝色。",
            "Sixteen thousand houses plotted by area against price, with a continuous visual map " +
                "shading every point from gold to blue by its price.",
        ) { chart ->
            loadJson("data/house-price-area2.json").then { json ->
                chart.setOption(streamOption(priceItems(json.unsafeCast<Array<Array<Double>>>())))
            }
        }
