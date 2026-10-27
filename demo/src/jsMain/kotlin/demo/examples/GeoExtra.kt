package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import demo.ui.loadText
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

private fun beefCut(name: String, value: Double): MapDataItem =
    MapDataItem(
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
        geoExamples.interactive("geo-svg-scatter-simple", "散点图（SVG 地图）", "GEO SVG Scatter") { chart ->
            loadText("data/Map_of_Iceland.svg").then { svg ->
                ECharts.registerMap(
                    "iceland_svg",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    tooltip = unsafeJso<TooltipOption> { show = true }
                                    map = "iceland_svg"
                                    roam = BooleanOrString(true)
                                },
                            )
                        series =
                            one(
                                effectScatterSeries {
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    // The third dimension drives the symbol size.
                                    symbolSize =
                                        SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) / 100 * 15 + 5) }
                                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#b02a02") }
                                    encode =
                                        unsafeJso<OptionEncode> {
                                            tooltip = OptionEncodeValue(DimensionLoose(2.0))
                                        }
                                    typedData =
                                        dataItems(
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(488.2358421078053),
                                                    OptionDataValue(459.70913833075736),
                                                    OptionDataValue(100.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(770.3415644319939),
                                                    OptionDataValue(757.9672194986475),
                                                    OptionDataValue(30.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(1180.0329284196291),
                                                    OptionDataValue(743.6141808346214),
                                                    OptionDataValue(80.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(894.03790632245),
                                                    OptionDataValue(1188.1985153835008),
                                                    OptionDataValue(61.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(1372.98925630313),
                                                    OptionDataValue(477.3839988649537),
                                                    OptionDataValue(70.0),
                                                ),
                                            ),
                                            ScatterDataItem(
                                                arrayOf<OptionDataValue?>(
                                                    OptionDataValue(1378.62251255796),
                                                    OptionDataValue(935.6708486282843),
                                                    OptionDataValue(81.0),
                                                ),
                                            ),
                                        ).unsafeCast<Array<EffectScatterDataItem?>>()
                                },
                            )
                    },
                )
                null
            }

        geoExamples.interactive("geo-beef-cuts", "庖丁解牛", "GEO Beef Cuts") { chart ->
            loadText("data/Beef_cuts_France.svg").then { svg ->
                ECharts.registerMap(
                    "Beef_cuts_France",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    left = StringOrDouble("center")
                                    bottom = StringOrDouble("10%")
                                    min = 5.0
                                    max = 100.0
                                    orient = Orient("horizontal")
                                    text = arrayOf("", "Price")
                                    realtime = true
                                    calculable = true
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            color = StringOrStringArray(arrayOf("#dbac00", "#db6e00", "#cf0000"))
                                        }
                                },
                            )
                        series =
                            one(
                                mapSeries {
                                    name = "French Beef Cuts"
                                    map = "Beef_cuts_France"
                                    roam = BooleanOrString(true)
                                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = false } }
                                    selectedMode = SelectionMode(false)
                                    typedData =
                                        dataItems(
                                            beefCut("Queue", 15.0),
                                            beefCut("Langue", 35.0),
                                            beefCut("Plat de joue", 15.0),
                                            beefCut("Gros bout de poitrine", 25.0),
                                            beefCut("Jumeau à pot-au-feu", 45.0),
                                            beefCut("Onglet", 85.0),
                                            beefCut("Plat de tranche", 25.0),
                                            beefCut("Araignée", 15.0),
                                            beefCut("Gîte à la noix", 55.0),
                                            beefCut("Bavette d'aloyau", 25.0),
                                            beefCut("Tende de tranche", 65.0),
                                            beefCut("Rond de gîte", 45.0),
                                            beefCut("Bavettede de flanchet", 85.0),
                                            beefCut("Flanchet", 35.0),
                                            beefCut("Hampe", 75.0),
                                            beefCut("Plat de côtes", 65.0),
                                            beefCut("Tendron Milieu de poitrine", 65.0),
                                            beefCut("Macreuse à pot-au-feu", 85.0),
                                            beefCut("Rumsteck", 75.0),
                                            beefCut("Faux-filet", 65.0),
                                            beefCut("Côtes Entrecôtes", 55.0),
                                            beefCut("Basses côtes", 45.0),
                                            beefCut("Collier", 85.0),
                                            beefCut("Jumeau à biftek", 15.0),
                                            beefCut("Paleron", 65.0),
                                            beefCut("Macreuse à bifteck", 45.0),
                                            beefCut("Gîte", 85.0),
                                            beefCut("Aiguillette baronne", 65.0),
                                            beefCut("Filet", 95.0),
                                        )
                                },
                            )
                    },
                )
                null
                }
            }
        },
    )
