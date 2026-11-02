package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadFloat32
import echarts.options.one
import demo.examples.Category

private val scatterNebulaCategory = Category(Groups.SCATTER, "examples/scatter/ScatterNebula.kt")

val scatterNebulaEntry: CatalogEntry =
    scatterNebulaCategory.interactive(
            "scatter-nebula",
            "大规模星云散点图",
            "Scatter Nebula",
            "一百万点星云：8 MB 二进制浮点表直接作为 typed array 交给 large 模式的散点系列，四周各一个 dataZoom。",
            "A one-million-point nebula: an 8 MB binary float table handed to a large-mode scatter " +
                "series as a typed array, with a data zoom on each side.",
        ) { chart ->
            loadFloat32("data/fake-nebula.bin").then { points ->
                chart.setOption(nebulaOption(points))
            }
        }
