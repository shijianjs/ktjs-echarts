package demo.examples.lines

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
import demo.examples.gauge.center

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
internal fun appendNyChunks(
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
internal fun nyLinesOption(): EChartsOption =
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
