package demo.examples.gl

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
import demo.examples.gauge.center
import demo.examples.lines.NY_CHUNK_COUNT
import demo.examples.lines.nyChunk
import demo.examples.scatter.point

/** Number of `gps_*.bin` chunks the site ships; upstream splits the table into 230. */
internal const val GPS_CHUNK_COUNT = 20

/** GPS points per `gps_*.bin` chunk; upstream cuts the table into 50,000-point chunks. */
internal const val GPS_POINTS_PER_CHUNK = 50_000

/** The world map, centred on New York at the zoom the street pages use. */
internal fun nyGeoOption(): GeoOption =
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
internal fun appendNyChunksToGl(
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
internal fun appendGpsChunks(
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
internal fun nyLinesGlOption(): EChartsGlOption =
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
internal fun gpsScatterGlOption(): EChartsGlOption =
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
