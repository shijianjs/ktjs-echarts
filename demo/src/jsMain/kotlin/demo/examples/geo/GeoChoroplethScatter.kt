package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.cos
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.matrix.dimension

private val geoChoroplethScatterCategory = Category(Groups.GEO, "examples/geo/GeoChoroplethScatter.kt")

val geoChoroplethScatterEntry: CatalogEntry =
    geoChoroplethScatterCategory.interactive("geo-choropleth-scatter", "地理坐标系上的等值区划图和散点图", "Geo Choropleth and Scatter") { chart ->
            loadJson("data/iceland.geo.json").then { json ->
                ECharts.registerMap(
                    "iceland",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "iceland"
                                    roam = BooleanOrString(true)
                                    // The map is laid out for the latitude of Iceland.
                                    aspectScale = cos(65 * PI / 180)
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            color = "#555"
                                        }
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        visualMap =
                            arrayOf(
                                unsafeJso<VisualMapOption> {
                                    orient = Orient("horizontal")
                                    calculable = true
                                    right = StringOrDouble(0.0)
                                    bottom = StringOrDouble(0.0)
                                    seriesIndex = IndexOrIndexArray(0.0)
                                    min = 0.0
                                    max = 1e5
                                    dimension = 2.0
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            symbolSize = DoubleOrDoubleArray(arrayOf(5.0, 30.0))
                                        }
                                },
                                unsafeJso<VisualMapOption> {
                                    orient = Orient("horizontal")
                                    calculable = true
                                    left = StringOrDouble(0.0)
                                    bottom = StringOrDouble(0.0)
                                    seriesIndex = IndexOrIndexArray(1.0)
                                    min = 0.0
                                    max = 1e3
                                    dimension = 0.0
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            color = StringOrStringArray(arrayOf("#deebf7", "#3182bd"))
                                        }
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                scatterSeries {
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    encode =
                                        unsafeJso<OptionEncode> {
                                            tooltip = OptionEncodeValue(DimensionLoose(2.0))
                                            label = OptionEncodeValue(DimensionLoose(2.0))
                                        }
                                    typedData = icelandScatter()
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            color = ZRColor("#66c2a5")
                                            borderWidth = 1.0
                                            borderColor = ZRColor("#3c7865")
                                        }
                                },
                                mapSeries {
                                    // Sharing the `geo` component makes this a choropleth instead of
                                    // a second map of its own.
                                    geoIndex = 0.0
                                    map = ""
                                    typedData =
                                        dataItems(
                                            region("Austurland", 423.0),
                                            region("Suðurland", 256.0),
                                            region("Norðurland vestra", 489.0),
                                            region("Norðurland eystra", 51.0),
                                        )
                                },
                            )
                    },
                )
                null
            }
        }
