package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.examples.Category
import demo.examples.shared.every

private val scatterSymbolMorphCategory = Category(Groups.SCATTER, "examples/scatter/ScatterSymbolMorph.kt")

val scatterSymbolMorphEntry: CatalogEntry =
    scatterSymbolMorphCategory.interactive(
            "scatter-symbol-morph",
            "散点图变形动画",
            "Symbol Shape Morph",
            "同样的 10×10 个点每 700 毫秒换一种形状，`universalTransition` 把方块补形成心形、笑脸、" +
                "馅饼和飞机；后面的选项只写 `symbol`，其余字段靠 `setOption` 合并。",
            "The same hundred points change shape every 700 milliseconds; `universalTransition` morphs " +
                "the squares into hearts, smiles, pies and a plane, and every later option only " +
                "carries the new `symbol` while `setOption` merges the rest.",
        ) { chart, holder ->
            var optionIndex = 0
            chart.setOption(MORPH_OPTIONS[optionIndex])
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                optionIndex = (optionIndex + 1) % MORPH_OPTIONS.size
                chart.setOption(MORPH_OPTIONS[optionIndex])
            }, MORPH_INTERVAL_MILLIS)
        }
