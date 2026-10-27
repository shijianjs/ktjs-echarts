package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.ln
import kotlin.math.round
import kotlin.math.sin
import kotlin.math.sqrt
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * `custom-hexbin`: a hexagonal binning of one player's shots, drawn as polygons on a basketball
 * court that is registered as a map.
 *
 * The two tables (`kawhi-leonard-16-17-regular.json` and `nba-court.json`) live in
 * `demo/src/jsMain/resources/data/`; the binning is a Kotlin port of the d3-hexbin code the example
 * embeds.
 */

private val customHexbin = Category(Groups.CUSTOM, "examples/CustomHexbin.kt")

/** Shape of the shot table: column names plus one row per shot. */
private external interface ShotData {
    val schema: Array<String>
    val data: Array<Array<JsAny>>
}

/** One polygon of the court outline. */
private external interface CourtShape {
    val type: String
    val points: Array<Array<Double>>
}

/** Shape of the court table: the border map plus the line segments. */
private external interface CourtData {
    val borderGeoJSON: JsAny
    val geometry: Array<CourtShape>
}

/** One hexagonal bin: its centre and the shots it holds. */
private class HexBin(val x: Double, val y: Double) {
    val points = mutableListOf<DoubleArray>()
}

/** Result of the binning: the largest bin and every bin. */
private class HexBinResult(val maxBinLen: Int, val bins: List<HexBin>)

/**
 * Port of the `hexBinStatistics` helper of the example (itself based on d3-hexbin).
 *
 * Each point is `[locX, locY, made]`; the returned bins carry their centre in the same coordinate
 * space as the input.
 */
private fun hexBinStatistics(points: List<DoubleArray>, r: Double): HexBinResult {
    val dx = r * 2 * sin(PI / 3)
    val dy = r * 1.5
    val binsById = mutableMapOf<String, HexBin>()
    val bins = mutableListOf<HexBin>()
    for (point in points) {
        val rawX = point[0]
        val rawY = point[1]
        if (rawX.isNaN() || rawY.isNaN()) continue
        var py = rawY / dy
        var pj = round(py).toInt()
        var px = rawX / dx - (pj and 1) / 2.0
        var pi = round(px).toInt()
        val py1 = py - pj
        if (kotlin.math.abs(py1) * 3 > 1) {
            val px1 = px - pi
            val pi2 = pi + if (px < pi) -1 else 1
            val pj2 = pj + if (py < pj) -1 else 1
            val px2 = px - pi2
            val py2 = py - pj2
            if (px1 * px1 + py1 * py1 > px2 * px2 + py2 * py2) {
                pi = (pi2 + if (pj and 1 != 0) 1 else -1) / 2
                pj = pj2
            }
        }
        val id = "$pi-$pj"
        val existing = binsById[id]
        if (existing != null) {
            existing.points += point
        } else {
            val bin = HexBin((pi + (pj and 1) / 2.0) * dx, pj * dy)
            bin.points += point
            binsById[id] = bin
            bins += bin
        }
    }
    return HexBinResult(bins.size, bins)
}

/** Reads one dimension of the current data item as a number. */
private fun CustomSeriesRenderItemAPI.numberValue(index: Int): Double =
    value(DimensionLoose(index.toDouble())).unsafeCast<Double>()

val customHexbinEntries: List<CatalogEntry> =
    listOf(
        customHexbin.interactive("custom-hexbin", "六边形分箱图（自定义系列）", "Hexagonal Binning") { chart ->
            val shots = loadJson("data/kawhi-leonard-16-17-regular.json")
            val court = loadJson("data/nba-court.json")
            Promise.all(arrayOf(shots, court)).then { results ->
                val shotData = results[0].unsafeCast<ShotData>()
                val courtData = results[1].unsafeCast<CourtData>()
                ECharts.registerMap(
                    "nbaCourt",
                    MapInput(
                        unsafeJso<GeoJSONMapInput> {
                            geoJSON = courtData.borderGeoJSON.unsafeCast<GeoJSONSourceInput>()
                        },
                    ),
                )
                val schema = shotData.schema
                val locX = schema.indexOf("loc_x")
                val locY = schema.indexOf("loc_y")
                val madeFlag = schema.indexOf("shot_made_flag")
                val points =
                    shotData.data.map { row ->
                        doubleArrayOf(
                            row[locX].unsafeCast<Double>(),
                            row[locY].unsafeCast<Double>(),
                            if (row[madeFlag].unsafeCast<String>() == "made") 1.0 else 0.0,
                        )
                    }
                val radiusInGeo = 1.0
                val result = hexBinStatistics(points, radiusInGeo)
                val maxBinLen = result.maxBinLen
                val bins =
                    result.bins.map { bin ->
                        var made = 0.0
                        bin.points.forEach { made += it[2] }
                        doubleArrayOf(
                            bin.x,
                            bin.y,
                            bin.points.size.toDouble(),
                            (made / bin.points.size * 100),
                        )
                    }
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        backgroundColor = ZRColor("#333")
                        animation = false
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Some Player"
                                    subtext = "Regular Season"
                                    backgroundColor = ZRColor("#333")
                                    top = StringOrDouble(10.0)
                                    left = StringOrDouble(10.0)
                                    textStyle = unsafeJso<LabelOption> { color = "#eee" }
                                },
                            )
                        legend = one(unsafeJso<LegendOption> { data = legendNames("bar", "error") })
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "nbaCourt"
                                    left = StringOrDouble(0.0)
                                    right = StringOrDouble(0.0)
                                    top = StringOrDouble(0.0)
                                    bottom = StringOrDouble(0.0)
                                    roam = BooleanOrString(true)
                                    silent = true
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            color = ZRColor("#333")
                                            borderWidth = 0.0
                                        }
                                },
                            )
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    type = VisualMapTypes.continuous
                                    orient = Orient("horizontal")
                                    right = StringOrDouble(30.0)
                                    top = StringOrDouble(40.0)
                                    min = 0.0
                                    max = 100.0
                                    dimension = 3.0
                                    calculable = true
                                    textStyle = unsafeJso<LabelOption> { color = "#eee" }
                                    formatter = LabelFormatter("{value} %")
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            color = StringOrStringArray(arrayOf("green", "yellow"))
                                        }
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                customSeries {
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    encode =
                                        unsafeJso<OptionEncode> {
                                            tooltip =
                                                OptionEncodeValue(
                                                    arrayOf<DimensionLoose>(DimensionLoose(2.0), DimensionLoose(3.0)),
                                                )
                                        }
                                    typedData =
                                        dataItems(
                                            *bins.map { bin ->
                                                OptionDataItem(
                                                    arrayOf<OptionDataValue?>(
                                                        OptionDataValue(bin[0]),
                                                        OptionDataValue(bin[1]),
                                                        OptionDataValue(bin[2]),
                                                        OptionDataValue(bin[3]),
                                                    ),
                                                )
                                            }.toTypedArray(),
                                        )
                                    renderItem = { _, api ->
                                        val center =
                                            api.coord(
                                                CoordinateSystemDataCoord(
                                                    arrayOf<OptionDataValue?>(api.value(DimensionLoose(0.0)), api.value(DimensionLoose(1.0))),
                                                ),
                                            )
                                        val maxViewRadius =
                                            numberArray(
                                                api.size(
                                                    OptionDataValueOrArray(
                                                        arrayOf(OptionDataValue(radiusInGeo), OptionDataValue(0.0)),
                                                    ),
                                                ),
                                            )[0]
                                        val minViewRadius = kotlin.math.min(maxViewRadius, 4.0)
                                        val extentMax = ln(sqrt(maxBinLen.toDouble()))
                                        val viewRadius =
                                            ECharts.number.linearMap(
                                                ln(sqrt(api.numberValue(2))),
                                                arrayOf(0.0, extentMax),
                                                arrayOf(minViewRadius, maxViewRadius),
                                            )
                                        var angle = PI / 6
                                        val points = Array(6) {
                                            val point =
                                                arrayOf(
                                                    center[0] + viewRadius * cos(angle),
                                                    center[1] + viewRadius * sin(angle),
                                                )
                                            angle += PI / 3
                                            point
                                        }
                                        var angleBg = PI / 6
                                        val background = Array(6) {
                                            val point =
                                                arrayOf(
                                                    center[0] + maxViewRadius * cos(angleBg),
                                                    center[1] + maxViewRadius * sin(angleBg),
                                                )
                                            angleBg += PI / 3
                                            point
                                        }
                                        unsafeJso<CustomGroupOption> {
                                            type = "group"
                                            children =
                                                arrayOf(
                                                    unsafeJso<CustomPathOption> {
                                                        type = "polygon"
                                                        shape = unsafeJso<ZRShapeProps> { this.points = points }
                                                        style =
                                                            unsafeJso<ZRStyleProps> {
                                                                stroke = ZRColor("#ccc")
                                                                fill = api.visual(VisualProps.color).unsafeCast<ZRColor>()
                                                                lineWidth = 1.0
                                                            }
                                                    },
                                                    unsafeJso<CustomPathOption> {
                                                        type = "polygon"
                                                        shape = unsafeJso<ZRShapeProps> { this.points = background }
                                                        style =
                                                            unsafeJso<ZRStyleProps> {
                                                                fill = ZRColor("rgba(0,0,0,0.5)")
                                                                lineWidth = 0.0
                                                            }
                                                        z2 = -19.0
                                                    },
                                                )
                                        }
                                    }
                                },
                                customSeries {
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    silent = true
                                    typedData = dataItems(OptionDataItem(OptionDataValue(0.0)))
                                    renderItem = { _, api ->
                                        unsafeJso<CustomGroupOption> {
                                            type = "group"
                                            children =
                                                Array(courtData.geometry.size) { index ->
                                                    val shape = courtData.geometry[index]
                                                    unsafeJso<CustomPathOption> {
                                                        type = shape.type
                                                        this.shape =
                                                            unsafeJso<ZRShapeProps> {
                                                                this.points =
                                                                    Array(shape.points.size) { pointIndex ->
                                                                        api.coord(
                                                                            CoordinateSystemDataCoord(
                                                                                arrayOf<OptionDataValue?>(
                                                                                    OptionDataValue(shape.points[pointIndex][0]),
                                                                                    OptionDataValue(shape.points[pointIndex][1]),
                                                                                ),
                                                                            ),
                                                                        )
                                                                    }
                                                            }
                                                        style =
                                                            unsafeJso<ZRStyleProps> {
                                                                stroke = ZRColor("#aaa")
                                                                lineWidth = 1.5
                                                            }
                                                    }
                                                }
                                        }
                                    }
                                },
                            )
                    },
                )
                null
            }
        },
    )
