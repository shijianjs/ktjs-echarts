package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadFloat32
import demo.ui.loadInt32
import echarts.AppendDataParams
import echarts.EChartsInstance
import echarts.options.BooleanOrString
import echarts.options.DimensionDefinitionLoose
import echarts.options.GeoOption
import echarts.options.ItemStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.TitleOption
import echarts.options.one
import echartsgl.CoordinateSystems2D
import echartsgl.GlBlendModes
import echartsgl.GlColor
import echartsgl.options.EChartsGlOption
import echartsgl.options.LinesGLLineStyleOption
import echartsgl.options.linesGLSeries
import echartsgl.options.scatterGLSeries
import js.buffer.ArrayBuffer
import js.objects.unsafeJso
import js.typedarrays.Float32Array
import js.typedarrays.Float64Array
import js.typedarrays.Int32Array
import zrender.ZRColor

/*
 * The two WebGL big-data pages of the `gl/` directory of apache/echarts-examples.
 *
 * Both read the same kind of binary chunk tables as the 2D pages and append them with `appendData`,
 * but draw them on the GPU: `linesGL-ny` reuses the New York street chunks of `LinesNy.kt` and
 * `scatterGL-gps` reads the bulk GPS points of OpenStreetMap. Because the series live on the GL
 * layer, the coordinates are handed over as flat typed arrays, the branch upstream declares as
 * `ArrayLike<number>`; per-point objects would turn a million points into a million allocations.
 */

private val glBigData = Category(Groups.GL, "examples/GlBigData.kt")

/** Number of `gps_*.bin` chunks the site ships; upstream splits the table into 230. */
private const val GPS_CHUNK_COUNT = 20

/** GPS points per `gps_*.bin` chunk; upstream cuts the table into 50,000-point chunks. */
private const val GPS_POINTS_PER_CHUNK = 50_000

/** The world map, centred on New York at the zoom the street pages use. */
private fun nyGeoOption(): GeoOption =
    unsafeJso {
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
    }

/** Appends the street chunk [index] and then the ones after it, stopping when the chart is gone. */
private fun appendNyChunksToGl(
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
        appendNyChunksToGl(chart, index + 1)
    }
}

/**
 * Converts one `gps_*.bin` chunk to the flat `[lng, lat, ...]` table of a `scatterGL` series.
 *
 * The file stores `[lat, lng]` pairs as 32-bit integers scaled by 1e7, so the two halves are swapped
 * and divided here; the output keeps the element count of the chunk because the swap is in place.
 */
internal fun gpsChunk(raw: Int32Array<ArrayBuffer>): Float32Array<ArrayBuffer> {
    val data = Float32Array<ArrayBuffer>(raw.length)
    var index = 0
    while (index < raw.length) {
        data[index] = (raw[index + 1] / 1e7).toFloat()
        data[index + 1] = (raw[index] / 1e7).toFloat()
        index += 2
    }
    return data
}

/** Appends the GPS chunk [index] and then the ones after it, stopping when the chart is gone. */
private fun appendGpsChunks(
    chart: EChartsInstance,
    index: Int,
) {
    if (index >= GPS_CHUNK_COUNT || chart.isDisposed()) return
    loadInt32("data/gps/gps_$index.bin").then { raw ->
        chart.appendData(
            unsafeJso<AppendDataParams> {
                seriesIndex = 0.0
                typedFlatData = gpsChunk(raw)
            },
        )
        appendGpsChunks(chart, index + 1)
    }
}

/** `linesGL` over the New York street chunks: the WebGL twin of `lines-ny`. */
private fun nyLinesGlOption(): EChartsGlOption =
    unsafeJso {
        progressive = 20000.0
        backgroundColor = ZRColor("#111")
        geo = one(nyGeoOption())
        series =
            one<SeriesOption>(
                linesGLSeries {
                    coordinateSystem = "geo"
                    typedCoordinateSystem = CoordinateSystems2D.geo
                    typedBlendMode = GlBlendModes.lighter
                    dimensions = arrayOf(DimensionDefinitionLoose("value"))
                    typedFlatData = Float64Array<ArrayBuffer>(0)
                    polyline = true
                    typedLineStyle =
                        unsafeJso<LinesGLLineStyleOption> {
                            color = GlColor(ZRColor("orange"))
                            opacity = 0.3
                        }
                },
            )
    }

/**
 * `scatterGL` over the GPS chunks.
 *
 * Upstream titles the page after its 10 million points; the site ships 20 of the 230 chunks, so the
 * title shows the 1,000,000 points that are actually drawn. The `geo` component only provides the
 * coordinates here, which is why it is silent and keeps its own hover styles.
 */
private fun gpsScatterGlOption(): EChartsGlOption =
    unsafeJso {
        backgroundColor = ZRColor("#000")
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "${GPS_CHUNK_COUNT * GPS_POINTS_PER_CHUNK} GPS Points"
                    left = StringOrDouble("center")
                    textStyle = unsafeJso { color = "#fff" }
                },
            )
        geo =
            one(
                unsafeJso<GeoOption> {
                    map = "world"
                    roam = BooleanOrString(true)
                    silent = true
                    itemStyle =
                        unsafeJso<ItemStyleOption> {
                            color = ZRColor("#323c48")
                            borderColor = ZRColor("#111")
                        }
                },
            )
        series =
            one<SeriesOption>(
                scatterGLSeries {
                    name = "弱"
                    progressive = 1e6
                    coordinateSystem = "geo"
                    typedCoordinateSystem = CoordinateSystems2D.geo
                    symbolSize = SymbolSize(1.0)
                    zoomScale = 0.002
                    typedBlendMode = GlBlendModes.lighter
                    large = true
                    typedItemStyle =
                        unsafeJso {
                            color = GlColor(ZRColor("rgb(20, 15, 2)"))
                        }
                    postEffect = unsafeJso { enable = true }
                    silent = true
                    dimensions =
                        arrayOf(
                            DimensionDefinitionLoose("lng"),
                            DimensionDefinitionLoose("lat"),
                        )
                    typedFlatData = Float32Array<ArrayBuffer>(0)
                },
            )
    }

/** The two WebGL big-data pages, `linesGL-ny` and `scatterGL-gps`. */
val glBigDataEntries: List<CatalogEntry> =
    listOf(
        glBigData.interactive(
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
        },
        glBigData.interactive(
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
        },
    )
