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
import zrender.ZRLineTypes

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

/** The seven pre-sold seats of the flight seatmap, coloured red and non-interactive. */
private fun takenRegions(): Array<RegionOption> {
    val names = arrayOf("26E", "26D", "26C", "25D", "23C", "21A", "20F")
    return Array(names.size) { index ->
        unsafeJso<RegionOption> {
            name = names[index]
            silent = true
            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#bf0e08") }
        }
    }
}
/** One animated route of the SVG traffic example; `coords` is `[x, y]` along the airport plan. */
private val TRAFFIC_A =
    arrayOf(
        arrayOf(50.875133928571415, 242.66287667410717),
        arrayOf(62.03696428571425, 264.482421875),
        arrayOf(72.63357421874997, 273.62779017857144),
        arrayOf(92.78291852678569, 285.869140625),
        arrayOf(113.43637834821425, 287.21854073660717),
        arrayOf(141.44788783482142, 288.92947823660717),
        arrayOf(191.71686104910714, 289.5507114955357),
        arrayOf(198.3060072544643, 294.0673828125),
        arrayOf(204.99699497767858, 304.60288783482144),
        arrayOf(210.79177734375003, 316.7373046875),
        arrayOf(212.45179408482142, 329.3656529017857),
        arrayOf(210.8885267857143, 443.3925083705358),
        arrayOf(215.35936941964286, 453.00634765625),
        arrayOf(224.38761997767858, 452.15087890625),
        arrayOf(265.71490792410714, 452.20179966517856),
        arrayOf(493.3408844866072, 453.77525111607144),
        arrayOf(572.8892940848216, 448.77992466517856),
        arrayOf(608.9513755580358, 448.43366350446433),
        arrayOf(619.99099609375, 450.8778599330358),
        arrayOf(624.2479715401787, 456.2194475446429),
        arrayOf(628.1434095982145, 463.9899553571429),
        arrayOf(629.8492550223216, 476.0276227678571),
        arrayOf(631.2750362723216, 535.7322126116071),
        arrayOf(624.6757059151787, 546.6496233258929),
        arrayOf(617.1801702008929, 552.6480887276786),
        arrayOf(603.7269056919645, 554.5066964285714),
        arrayOf(588.0178515625, 557.5517578125),
        arrayOf(529.4386104910716, 556.2991071428571),
        arrayOf(422.1994921875001, 551.38525390625),
        arrayOf(291.66921875, 552.5767996651786),
        arrayOf(219.4279380580357, 547.4949079241071),
        arrayOf(209.53912667410714, 541.5931919642858),
        arrayOf(206.70793247767858, 526.1947544642858),
        arrayOf(206.70793247767858, 507.4049944196429),
        arrayOf(206.12234375000003, 468.7663225446429),
        arrayOf(204.48778738839286, 459.44782366071433),
        arrayOf(197.56256417410714, 452.8943219866071),
        arrayOf(170.31995814732142, 456.27546037946433),
        arrayOf(1.8078906249999704, 460.5935407366071),
    )

private val TRAFFIC_B =
    arrayOf(
        arrayOf(779.4595368303574, 287.98744419642856),
        arrayOf(689.07009765625, 291.0477818080357),
        arrayOf(301.83300223214286, 290.49783761160717),
        arrayOf(229.31165736607142, 291.73011997767856),
        arrayOf(220.73660156250003, 297.4077845982143),
        arrayOf(214.74832031250003, 308.52378627232144),
        arrayOf(213.82156250000003, 421.35400390625),
        arrayOf(213.19523716517858, 443.0564313616071),
        arrayOf(222.31005301339286, 455.95465959821433),
        arrayOf(271.71846540178575, 454.37611607142856),
        arrayOf(359.64843191964286, 455.9393833705358),
        arrayOf(580.2524358258929, 448.11286272321433),
        arrayOf(627.7156752232145, 460.7463030133929),
        arrayOf(632.3290959821429, 536.6386021205358),
        arrayOf(628.9123130580358, 548.4776785714286),
        arrayOf(612.5667494419645, 556.8235909598214),
        arrayOf(543.7167912946429, 555.4741908482143),
        arrayOf(429.1756361607143, 551.9402901785714),
        arrayOf(293.42089285714286, 551.2172154017858),
        arrayOf(226.20039899553575, 556.0699637276786),
        arrayOf(215.49176339285714, 562.7253069196429),
        arrayOf(213.21051339285714, 591.6024693080358),
        arrayOf(212.00878348214286, 625.6735491071429),
        arrayOf(197.43017020089286, 645.0743582589286),
        arrayOf(187.41405691964286, 647.0857282366071),
        arrayOf(101.79589285714286, 649.0207170758929),
        arrayOf(69.96023437499997, 650.1613420758929),
        arrayOf(56.48150948660714, 656.8268694196429),
        arrayOf(51.11446149553569, 665.2542550223214),
    )

private val TRAFFIC_C =
    arrayOf(
        arrayOf(2.5920703124999704, 450.66908482142856),
        arrayOf(204.0651450892857, 453.13364955357144),
        arrayOf(378.72844029017864, 453.13874162946433),
        arrayOf(551.1817745535716, 456.1532505580358),
        arrayOf(578.3734598214287, 456.91196986607144),
        arrayOf(601.2317885044645, 458.9895368303571),
        arrayOf(614.1503850446429, 462.1669921875),
        arrayOf(618.99294921875, 479.68882533482144),
        arrayOf(620.0826534598216, 513.5969587053571),
        arrayOf(615.6932840401787, 528.7306082589286),
        arrayOf(608.4829045758929, 533.2625558035714),
        arrayOf(592.7127455357145, 534.9582170758929),
        arrayOf(583.09890625, 527.5492466517858),
        arrayOf(578.6535239955358, 516.4077845982143),
        arrayOf(578.6535239955358, 498.36146763392856),
        arrayOf(577.9966462053571, 477.0613141741071),
        arrayOf(575.3691350446429, 469.1940569196429),
        arrayOf(569.0753292410716, 462.63037109375),
        arrayOf(553.9518638392858, 460.6444614955358),
        arrayOf(298.10051060267864, 465.61432756696433),
        arrayOf(193.49908761160714, 460.1759905133929),
        arrayOf(116.40505859374997, 465.78236607142856),
        arrayOf(3.5137360491071092, 463.47565569196433),
    )

/** Builds one `lines` data item: a coloured, constant-speed effect over the given polyline. */
private fun trafficRoute(effectColor: String, speed: Double, coords: Array<Array<Double>>): LinesDataItemOption =
    unsafeJso {
        effect = unsafeJso<LinesEffectOption> { color = ZRColor(effectColor); constantSpeed = speed }
        this.coords =
            coords.map { point -> arrayOf(point[0], point[1]) }.toTypedArray()
    }
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

private fun routeNode(name: String, lng: Double, lat: Double): GraphDataItem =
    GraphDataItem(
        unsafeJso<GraphNodeItemOption> {
            this.name = name
            this.value = OptionDataValueOrArray(arrayOf(OptionDataValue(lng), OptionDataValue(lat)))
        },
    )

private fun routeEdge(source: String, target: String): GraphEdgeItemOption =
    unsafeJso {
        this.source = StringOrDouble(source)
        this.target = StringOrDouble(target)
    }

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
        geoExamples.interactive("geo-graph", "地理坐标系上的关系图", "Geo Graph") { chart ->
            loadJson("data/ch.geo.json").then { json ->
                ECharts.registerMap(
                    "ch",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title = one(unsafeJso<TitleOption> { text = "Travel Routes" })
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "ch"
                                    roam = BooleanOrString(true)
                                    // The map is laid out for the latitude of Switzerland.
                                    aspectScale = cos(47 * PI / 180)
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            textBorderColor = "#fff"
                                            textBorderWidth = 2.0
                                        }
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        series =
                            one(
                                graphSeries {
                                    coordinateSystem = "geo"
                                    typedData =
                                        dataItems(
                                            routeNode("a", 7.667821250000001, 46.791734269956265),
                                            routeNode("b", 7.404848750000001, 46.516308805996054),
                                            routeNode("c", 7.376673125000001, 46.24628858538375),
                                            routeNode("d", 8.015320625000001, 46.39460918238572),
                                            routeNode("e", 8.616400625, 46.7020608630855),
                                            routeNode("f", 8.869981250000002, 46.37539345234199),
                                            routeNode("g", 9.546196250000001, 46.58676648282309),
                                            routeNode("h", 9.311399375, 47.182454114178896),
                                            routeNode("i", 9.085994375000002, 47.55395822835779),
                                            routeNode("j", 8.653968125000002, 47.47709530818285),
                                            routeNode("k", 8.203158125000002, 47.44506909144329),
                                        )
                                    edges =
                                        arrayOf(
                                            routeEdge("a", "b"),
                                            routeEdge("b", "c"),
                                            routeEdge("c", "d"),
                                            routeEdge("d", "e"),
                                            routeEdge("e", "f"),
                                            routeEdge("f", "g"),
                                            routeEdge("g", "h"),
                                            routeEdge("h", "i"),
                                            routeEdge("i", "j"),
                                            routeEdge("j", "k"),
                                        )
                                    edgeSymbol = StringOrStringArray(arrayOf("none", "arrow"))
                                    edgeSymbolSize = DoubleOrDoubleArray(arrayOf(5.0))
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("#718adbff")
                                            opacity = 1.0
                                        }
                                },
                            )
                    },
                )
                null
                }
            }
        },
        geoExamples.interactive("geo-svg-lines", "路径图（SVG 地图）", "GEO SVG Lines") { chart ->
            loadText("data/MacOdrum-LV5-floorplan-web.svg").then { svg ->
                ECharts.registerMap(
                    "MacOdrum-LV5-floorplan-web",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Visit Route"
                                    left = StringOrDouble("center")
                                    bottom = StringOrDouble(10.0)
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "MacOdrum-LV5-floorplan-web"
                                    roam = BooleanOrString(true)
                                    emphasis =
                                        unsafeJso<EmphasisOption> {
                                            itemStyle = unsafeJso<ItemStyleOption> {}
                                            label = unsafeJso<LabelOption> { show = false }
                                        }
                                },
                            )
                        series =
                            one(
                                linesSeries {
                                    name = "Route"
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = false } }
                                    polyline = true
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("#c46e54")
                                            width = 5.0
                                            opacity = 1.0
                                            type = ZRLineTypes.dotted
                                        }
                                    typedData =
                                        dataItems(
                                            unsafeJso<LinesDataItemOption> {
                                                coords =
                                                    arrayOf(
                                                        arrayOf(110.6189462165178, 456.64349563895087),
                                                        arrayOf(124.10988522879458, 450.8570048730469),
                                                        arrayOf(123.9272226116071, 389.9520693708147),
                                                        arrayOf(61.58708083147317, 386.87942320312504),
                                                        arrayOf(61.58708083147317, 72.8954315876116),
                                                        arrayOf(258.29514854771196, 72.8954315876116),
                                                        arrayOf(260.75457021484374, 336.8559607533482),
                                                        arrayOf(280.5277985253906, 410.2406672084263),
                                                        arrayOf(275.948185765904, 528.0254369698661),
                                                        arrayOf(111.06907909458701, 552.795792593471),
                                                        arrayOf(118.87138231445309, 701.365737015904),
                                                        arrayOf(221.36468155133926, 758.7870354617745),
                                                        arrayOf(307.86195445452006, 742.164737297712),
                                                        arrayOf(366.8489324762834, 560.9895157073103),
                                                        arrayOf(492.8750778390066, 560.9895157073103),
                                                        arrayOf(492.8750778390066, 827.9639780566406),
                                                        arrayOf(294.9255269587053, 827.9639780566406),
                                                        arrayOf(282.79803391043527, 868.2476088113839),
                                                    )
                                            },
                                        )
                                },
                            )
                    },
                )
                null
            }
        },
        geoExamples.interactive("geo-seatmap-flight", "航班选座（SVG）", "Flight Seatmap") { chart ->
            loadText("data/flight-seats.svg").then { svg ->
                ECharts.registerMap(
                    "flight-seats",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "flight-seats"
                                    roam = BooleanOrString(true)
                                    selectedMode = SelectionMode("multiple")
                                    layoutCenter = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                                    layoutSize = StringOrDouble("95%")
                                    tooltip = unsafeJso<TooltipOption> { show = true }
                                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#fff") }
                                    regions = takenRegions()
                                },
                            )
                    },
                )
                null
            }
        },
        geoExamples.interactive("geo-organ", "内脏数据（SVG）", "Organ Data") { chart ->
            loadText("data/Veins_Medical_Diagram_clip_art.svg").then { svg ->
                ECharts.registerMap(
                    "organ_diagram",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    left = StringOrDouble(10.0)
                                    right = StringOrDouble("50%")
                                    map = "organ_diagram"
                                    selectedMode = SelectionMode("multiple")
                                    emphasis =
                                        unsafeJso<EmphasisOption> {
                                            focus = "self".unsafeCast<EmphasisFocus>()
                                            itemStyle = unsafeJso<ItemStyleOption> {}
                                            label =
                                                unsafeJso<LabelOption> {
                                                    position = LabelPosition("bottom")
                                                    distance = 0.0
                                                    textBorderColor = "#fff"
                                                    textBorderWidth = 2.0
                                                }
                                        }
                                },
                            )
                        grid =
                            one(
                                unsafeJso<GridOption> {
                                    left = StringOrDouble("60%")
                                    top = StringOrDouble("20%")
                                    bottom = StringOrDouble("20%")
                                },
                            )
                        xAxis = one(unsafeJso<XAxisOption> {})
                        yAxis =
                            one(
                                unsafeJso<YAxisOption> {
                                    data =
                                        strings(
                                            "heart",
                                            "large-intestine",
                                            "small-intestine",
                                            "spleen",
                                            "kidney",
                                            "lung",
                                            "liver",
                                        )
                                },
                            )
                        series =
                            one(
                                barSeries {
                                    typedData =
                                        numbers(
                                            121.0, 321.0, 141.0, 52.0, 198.0, 289.0, 139.0,
                                        )
                                },
                            )
                    },
                )
                null
            }
        },
        geoExamples.interactive("geo-svg-traffic", "交通（SVG）", "SVG Traffic") { chart ->
            loadText("data/ksia-ext-plan-min.svg").then { svg ->
                ECharts.registerMap(
                    "ksia-ext-plan",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "ksia-ext-plan"
                                    roam = BooleanOrString(true)
                                    layoutCenter = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                                    layoutSize = StringOrDouble("100%")
                                },
                            )
                        series =
                            one(
                                linesSeries {
                                    name = "Route"
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    polyline = true
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#c46e54"); width = 0.0 }
                                    effect =
                                        unsafeJso<LinesEffectOption> {
                                            show = true
                                            period = 8.0
                                            color = ZRColor("#a10000")
                                            trailLength = 0.0
                                            symbolSize = 12.0
                                            symbol =
                                                "path://M87.1667 3.8333L80.5.5h-60l-6.6667 3.3333L.5 70.5v130l10 10h80l10 -10v-130zM15.5 190.5l15 -20h40l15 20zm75 -65l-15 5v35l15 15zm-80 0l15 5v35l-15 15zm65 0l15 -5v-40l-15 20zm-50 0l-15 -5v-40l15 20zm 65,-55 -15,25 c -15,-5 -35,-5 -50,0 l -15,-25 c 25,-15 55,-15 80,0 z"
                                        }
                                    z = 100.0
                                    typedData =
                                        dataItems(
                                            trafficRoute("#a10000", 100.0, TRAFFIC_A),
                                            trafficRoute("#00067d", 80.0, TRAFFIC_B),
                                            trafficRoute("#997405", 60.0, TRAFFIC_C),
                                        )
                                },
                            )
                    },
                )
                null
            }
        },
    )