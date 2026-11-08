package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.matrix.dimension

private val datasetLinkCategory = Category(Groups.DATASET, "examples/dataset/DatasetLink.kt")

val datasetLinkEntry: CatalogEntry =
    datasetLinkCategory.interactive(
            "dataset-link",
            "联动和共享数据集",
            "Share Dataset",
            summaryZh = "四条折线与一张饼图共用同一个 dataset；axisPointer 扫到哪一年，饼图就换成那一年的维度。",
            summaryEn = "Four lines and one pie share a single dataset; the axis pointer swaps the encoded dimension of the pie.",
        ) { chart ->
            chart.setOption(linkedDatasetOption())
            chart.on("updateAxisPointer") { event ->
                val dimension = linkedDimension(event)
                if (dimension >= 0) {
                    chart.setOption(linkedPieDimensionOption(dimension))
                }
            }
        }
