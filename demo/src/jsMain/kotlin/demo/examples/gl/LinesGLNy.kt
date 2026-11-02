package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.examples.Category

private val linesGLNyCategory = Category(Groups.GL, "examples/gl/LinesGLNy.kt")

val linesGLNyEntry: CatalogEntry =
    linesGLNyCategory.interactive(
            "linesGL-ny",
            "LinesGL - 实时交互的纽约街道可视化",
            "Use linesGL to draw 1 million New York streets",
            "与 lines-ny 同样的分片街道数据，改由 WebGL 绘制，缩放平移时依然流畅。",
            "The same street chunks as `lines-ny`, drawn on the GPU so that roaming the map stays " +
                "responsive.",
        ) { chart ->
            registerWorldMap().then {
                chart.setOption(nyLinesGlOption())
                appendNyChunksToGl(chart, 0)
            }
        }
