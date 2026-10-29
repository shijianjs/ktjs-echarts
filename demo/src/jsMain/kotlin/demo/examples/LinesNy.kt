package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadFloat32
import echarts.AppendDataParams
import echarts.EChartsInstance
import echarts.options.BooleanOrString
import echarts.options.DimensionDefinitionLoose
import echarts.options.EChartsOption
import echarts.options.GeoOption
import echarts.options.ItemStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.linesSeries
import echarts.options.one
import js.buffer.ArrayBuffer
import js.objects.unsafeJso
import js.typedarrays.Float32Array
import js.typedarrays.Float64Array
import zrender.ZRColor

/*
 * `lines-ny` of `examples/ts/lines-ny.ts`: almost a million New York street polylines drawn as one
 * `lines` series on the world map.
 *
 * The dataset is a sequence of binary chunks, so the page never holds the whole table: it reads one
 * chunk at a time, converts it to the flat `[count, x0, y0, ...]` rows of a polyline series and hands
 * them to `appendData`, which is what keeps the million-point page interactive. The same chunks are
 * reused by the WebGL variant in `GlBigData.kt`.
 */

private val linesNy = Category(Groups.LINES, "examples/LinesNy.kt")

/**
 * Number of `links_ny_*.bin` chunks the site ships.
 *
 * Upstream splits the table into 32 chunks of about 1 MB each (23 MB in total); the site ships 8 of
 * them, which still carry 922,916 street segments. The split is a random one, not a spatial tiling,
 * so the eight cover the same bounding box as all 32 and the map looks the same, only sparser.
 * See `data/SOURCE.md`.
 */
internal const val NY_CHUNK_COUNT = 8

/**
 * Converts one `links_ny_*.bin` chunk to the flat data of a polyline `lines` series.
 *
 * A chunk starts with the `[offsetX, offsetY]` shared by its rows, followed by
 * `[count, x0, y0, x1, y1, ...]` polylines whose coordinates are local to that offset; the offset is
 * applied here because ECharts reads absolute coordinates. The two header floats are dropped, which
 * is why the output is `raw.length - 2` numbers long.
 */
internal fun nyChunk(raw: Float32Array<ArrayBuffer>): Float64Array<ArrayBuffer> {
    val data = Float64Array<ArrayBuffer>(raw.length - 2)
    val offsetX = raw[0].toDouble()
    val offsetY = raw[1].toDouble()
    var read = 2
    var write = 0
    while (read < raw.length) {
        val count = raw[read++].toInt()
        data[write++] = count.toDouble()
        for (segment in 0 until count) {
            data[write++] = raw[read++] + offsetX
            data[write++] = raw[read++] + offsetY
        }
    }
    return data
}

/** Appends the chunk [index] and then the ones after it, stopping when the chart is gone. */
private fun appendNyChunks(
    chart: EChartsInstance,
    index: Int,
) {
    if (index >= NY_CHUNK_COUNT || chart.isDisposed()) return
    loadFloat32("data/ny/links_ny_$index.bin").then { raw ->
        chart.appendData(
            unsafeJso<AppendDataParams> {
                seriesIndex = 0.0
                typedFlatData = nyChunk(raw)
            },
        )
        appendNyChunks(chart, index + 1)
    }
}

/**
 * The option of the page: an invisible, mouse-free world map zoomed onto New York and one large
 * polyline series that starts empty and grows chunk by chunk.
 */
private fun nyLinesOption(): EChartsOption =
    unsafeJso {
        progressive = 20000.0
        backgroundColor = ZRColor("#111")
        geo =
            one(
                unsafeJso<GeoOption> {
                    center =
                        arrayOf(
                            StringOrDouble(-74.04327099998152),
                            StringOrDouble(40.86737600240287),
                        )
                    zoom = 360.0
                    map = "world"
                    roam = BooleanOrString(true)
                    silent = true
                    itemStyle =
                        unsafeJso<ItemStyleOption> {
                            color = ZRColor("transparent")
                            borderColor = ZRColor("rgba(255,255,255,0.1)")
                            borderWidth = 1.0
                        }
                },
            )
        series =
            one<SeriesOption>(
                linesSeries {
                    coordinateSystem = "geo"
                    blendMode = "lighter"
                    dimensions = arrayOf(DimensionDefinitionLoose("value"))
                    typedFlatData = Float64Array<ArrayBuffer>(0)
                    polyline = true
                    large = true
                    lineStyle =
                        unsafeJso {
                            color = ZRColor("orange")
                            width = 0.5
                            opacity = 0.3
                        }
                },
            )
    }

val linesNyEntries: List<CatalogEntry> =
    listOf(
        linesNy.interactive(
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
        },
    )
