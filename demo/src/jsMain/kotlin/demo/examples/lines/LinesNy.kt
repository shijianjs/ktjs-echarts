package demo.examples.lines

import demo.CatalogEntry
import demo.Groups
import demo.examples.Category
import demo.examples.gl.registerWorldMap
import demo.examples.shared.every

private val linesNyCategory = Category(Groups.LINES, "examples/lines/LinesNy.kt")

val linesNyEntry: CatalogEntry =
    linesNyCategory.interactive(
            "lines-ny",
            "使用线图绘制近100万纽约街道数据",
            "Use lines to draw 1 million New York streets",
            "分片读取纽约街道的二进制轨迹，逐块追加到大模式的多段线系列，橙色路网叠在世界地图上。",
            "Reads the street table chunk by chunk and appends every block to a large-mode polyline " +
                "series, so the network paints progressively over the map.",
        ) { chart ->
            registerWorldMap().then {
                chart.setOption(nyLinesOption())
                appendNyChunks(chart, 0)
            }
        }
