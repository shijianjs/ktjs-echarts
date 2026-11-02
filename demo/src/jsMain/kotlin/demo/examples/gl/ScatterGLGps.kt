package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import echarts.options.one
import demo.examples.Category
import demo.examples.scatter.point

private val scatterGLGpsCategory = Category(Groups.GL, "examples/gl/ScatterGLGps.kt")

val scatterGLGpsEntry: CatalogEntry =
    scatterGLGpsCategory.interactive(
            "scatterGL-gps",
            "1 千万 GPS 点可视化",
            "10 million Bulk GPS points",
            "分片读取 OpenStreetMap 的批量 GPS 轨迹并以 scatterGL 叠加在世界地图上；站内取前 20 片，" +
                "共 100 万点。",
            "Appends the bulk GPS point chunks of OpenStreetMap to a `scatterGL` series; the site " +
                "ships the first 20 of 230 chunks, one million points.",
        ) { chart ->
            registerWorldMap().then {
                chart.setOption(gpsScatterGlOption())
                appendGpsChunks(chart, 0)
            }
        }
