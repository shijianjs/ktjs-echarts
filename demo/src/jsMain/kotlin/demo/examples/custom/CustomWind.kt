package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.sqrt
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.gauge.center
import demo.examples.matrix.dimension
import demo.examples.shared.row

private val customWindCategory = Category(Groups.CUSTOM, "examples/custom/CustomWind.kt")

val customWindEntry: CatalogEntry =
    customWindCategory.interactive("custom-wind", "风场（自定义系列）", "Wind Vectors") { chart ->
            loadJson("data/world.json").then { world ->
                ECharts.registerMap(
                    "world",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = world.unsafeCast<GeoJSONSourceInput>() }),
                )
                loadJson("data/winds.json").then { json ->
                val wind = json.unsafeCast<WindData>()
                val nx = wind.nx.toInt()
                val ny = wind.ny.toInt()
                var maxMag = 0.0
                var minMag = Double.MAX_VALUE
                val data = Array(nx * ny) { index ->
                    val vx = wind.data[index][0]
                    val vy = wind.data[index][1]
                    val magnitude = sqrt(vx * vx + vy * vy)
                    maxMag = kotlin.math.max(magnitude, maxMag)
                    minMag = kotlin.math.min(magnitude, minMag)
                    val column = index % nx
                    val row = index / nx
                    doubleArrayOf(
                        column.toDouble() / nx * 360 - 180,
                        row.toDouble() / ny * 180 - 90,
                        vx,
                        vy,
                        magnitude,
                    )
                }
                shuffleFixed(data, 20260604)
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        backgroundColor = ZRColor("#333")
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    left = StringOrDouble("center")
                                    min = minMag
                                    max = maxMag
                                    dimension = 4.0
                                    calculable = true
                                    orient = Orient("horizontal")
                                    textStyle = unsafeJso<LabelOption> { color = "#fff" }
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            color =
                                                StringOrStringArray(
                                                    arrayOf(
                                                        "#313695", "#4575b4", "#74add1", "#abd9e9", "#e0f3f8",
                                                        "#ffffbf", "#fee090", "#fdae61", "#f46d43", "#d73027", "#a50026",
                                                    ),
                                                )
                                        }
                                },
                            )
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "world"
                                    silent = true
                                    roam = BooleanOrString(true)
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            areaColor = ZRColor("#323c48")
                                            borderColor = ZRColor("#111")
                                        }
                                },
                            )
                        series =
                            one(
                                customSeries {
                                    coordinateSystem = "geo"
                                    progressive = 2000.0
                                    encode =
                                        unsafeJso<OptionEncode> {
                                            this.x = OptionEncodeValue(DimensionLoose(0.0))
                                            this.y = OptionEncodeValue(DimensionLoose(0.0))
                                        }
                                    typedData =
                                        dataItems(
                                            *data.map { row ->
                                                OptionDataItem(
                                                    Array(5) { dimension -> OptionDataValue(row[dimension]) },
                                                )
                                            }.toTypedArray(),
                                        )
                                    renderItem = { _, api ->
                                        val x = api.value(DimensionLoose(0.0)).unsafeCast<Double>()
                                        val y = api.value(DimensionLoose(1.0)).unsafeCast<Double>()
                                        val dx = api.value(DimensionLoose(2.0)).unsafeCast<Double>()
                                        val dy = api.value(DimensionLoose(3.0)).unsafeCast<Double>()
                                        val start =
                                            api.coord(
                                                CoordinateSystemDataCoord(
                                                    arrayOf<OptionDataValue?>(
                                                        OptionDataValue(kotlin.math.max(x - dx / 5, -180.0)),
                                                        OptionDataValue(kotlin.math.max(y - dy / 5, -90.0)),
                                                    ),
                                                ),
                                            )
                                        val end =
                                            api.coord(
                                                CoordinateSystemDataCoord(
                                                    arrayOf<OptionDataValue?>(
                                                        OptionDataValue(kotlin.math.min(x + dx / 5, 180.0)),
                                                        OptionDataValue(kotlin.math.min(y + dy / 5, 90.0)),
                                                    ),
                                                ),
                                            )
                                        unsafeJso<CustomPathOption> {
                                            type = "line"
                                            shape =
                                                unsafeJso<ZRShapeProps> {
                                                    x1 = start[0]
                                                    y1 = start[1]
                                                    x2 = end[0]
                                                    y2 = end[1]
                                                }
                                            style =
                                                unsafeJso<ZRStyleProps> {
                                                    lineWidth = 0.5
                                                    stroke = api.visual(VisualProps.color).unsafeCast<ZRColor>()
                                                }
                                        }
                                    }
                                },
                            )
                    },
                )
                null
                }
            }
        }
