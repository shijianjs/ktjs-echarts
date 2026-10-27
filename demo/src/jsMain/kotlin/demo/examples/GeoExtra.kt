package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.cos
import zrender.ZRColor

/*
 * Geo examples driven by a GeoJSON file of the official site.
 *
 * `geo-choropleth-scatter` registers `iceland.geo.json` as the map `iceland`, then shares one `geo`
 * component between a scatter series (population centres) and a map series (a choropleth of the
 * regions). The file lives in `demo/src/jsMain/resources/data/` (see its `SOURCE.md`).
 *
 * The `controller.inRange.color` of the first visual map is not ported: it only colours the slider
 * handle, which the demo's theme does not use.
 */

private val geoExamples = Category(Groups.GEO, "examples/GeoExtra.kt")

/** The eight population centres of the example: `[lng, lat, population]`. */
private fun icelandScatter(): Array<ScatterDataItem?> =
    dataItems(
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-21.9348415), OptionDataValue(64.1334671), OptionDataValue(14523.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-19.028531), OptionDataValue(63.710241), OptionDataValue(45126.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-17.089925), OptionDataValue(65.37887072), OptionDataValue(12345.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-19.15936), OptionDataValue(65.6218101), OptionDataValue(56789.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-19.849175), OptionDataValue(65.7287035), OptionDataValue(67890.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-23.18326), OptionDataValue(65.582939), OptionDataValue(89012.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-14.9515), OptionDataValue(64.475135), OptionDataValue(34567.0))),
        ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(-20.88389), OptionDataValue(63.85321), OptionDataValue(45678.0))),
    )

/** One region of the choropleth. */
private fun region(name: String, value: Double): MapDataItem = MapDataItem(
    unsafeJso<MapDataItemOption> {
    this.name = OptionId(name)
    this.value = OptionDataValue(value)
    },
)

val geoExtraEntries: List<CatalogEntry> =
    listOf(
        geoExamples.interactive("geo-choropleth-scatter", "地理坐标系上的等值区划图和散点图", "Geo Choropleth and Scatter") { chart ->
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
        },
    )
