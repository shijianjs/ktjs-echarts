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
import kotlin.math.round
import zrender.ZRColor

/*
 * The official `map` pages whose data the repository already ships: the Hong Kong GeoJSON, the
 * `USA.json` GeoJSON with its special areas, the Iceland GeoJSON and the Sicily SVG.
 *
 * These pages need the chart instance (a loader, a registered map, a `selectchanged` listener or a
 * `setInterval`), so they use `interactive` instead of a static option.
 *
 * Changed on purpose:
 *
 * - `map-HK` and `map-usa` show the loader while their GeoJSON is fetched, as the originals do;
 *   `showLoading` / `hideLoading` are kept;
 * - `map-usa` keeps the third `registerMap` argument that moves Alaska, Hawaii and Puerto Rico next
 *   to the mainland;
 * - `map-bar-morph` sorts the same population table by value before it morphs it into bars;
 * - `map-iceland-pie` keeps the region-name center of the fourth pie (supported since 5.4.1, and
 *   ECharts 6 is above the cutoff of the original's version check) and replaces its `Math.random()`
 *   with [Lcg], so the pies are the same on every visit;
 * - the `itemStyle.color: undefined` of the Sicily page is dropped: leaving the field out is what
 *   `undefined` means to ECharts.
 *
 * `geo-svg-map` also keeps the two "buttons": a scatter series of two invisible points whose labels
 * are the route names, and a `selectchanged` listener that turns the selected button into
 * `geoSelect` + `showTip` on the matching SVG region.
 */

private val geoMaps = Category(Groups.GEO, "examples/GeoMaps.kt")

/** Registers a GeoJSON document as the map [name]. */
private fun registerGeojsonMap(name: String, json: kotlin.js.JsAny) {
    ECharts.registerMap(
        name,
        MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
    )
}

/** A string dictionary for the option fields typed as `Record<String, String>`. */
private fun stringRecordOf(vararg pairs: Pair<String, String>): js.objects.Record<String, String> =
    js(
        "(keys, values) => { const o = {}; for (let i = 0; i < keys.length; i++) { o[keys[i]] = values[i]; } return o; }",
    )(
        Array(pairs.size) { pairs[it].first },
        Array(pairs.size) { pairs[it].second },
    ).unsafeCast<js.objects.Record<String, String>>()

/** One `{ name, value }` entry of a map series. */
private fun mapData(name: String, value: Double): MapDataItem =
    MapDataItem(
        unsafeJso<MapDataItemOption> {
            this.name = OptionId(name)
            this.value = OptionDataValue(value)
        },
    )

// ---------------------------------------------------------------------------------------------
// geo-svg-map
// ---------------------------------------------------------------------------------------------

/** One selectable route of the Sicily SVG: borderless, with an always-visible description. */
private fun sicilyRoute(name: String, position: String, lines: List<String>): RegionOption =
    unsafeJso {
        this.name = name
        itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 0.0 }
        select =
            unsafeJso<RegionStateOption> {
                itemStyle =
                    unsafeJso<ItemStyleOption> {
                        color = ZRColor("#b5280d")
                        borderWidth = 0.0
                    }
            }
        tooltip =
            unsafeJso<TooltipOption> {
                this.position = TooltipPosition(position)
                alwaysShowContent = true
                enterable = true
                extraCssText = "user-select: text"
                formatter = TooltipFormatter(lines.joinToString("<br>"))
            }
    }

private val ROUTE_1 =
    listOf(
        "Route 1:",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
    )

private val ROUTE_2 =
    listOf(
        "Route 2:",
        "xxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxx",
    )

private val SICILY_INTRO =
    listOf(
        "This is the introduction:",
        "xxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxx",
    )

/** Names of the two route buttons of [sicilyOption], indexed by the data index of the scatter. */
private val SICILY_ROUTE_NAMES = arrayOf("route1", "route2")

private fun sicilyOption(): EChartsOption =
    unsafeJso {
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    formatter =
                        TooltipFormatter { params ->
                            val name = params.unsafeCast<echarts.options.CallbackDataParams>().name
                            echarts.options.TooltipContent(
                                listOf(name + ":")
                                    .plus(List(3) { "xxxxxxxxxxxxxxxx" })
                                    .joinToString("<br>"),
                            )
                        }
                },
            )
        geo =
            one(
                unsafeJso<GeoOption> {
                    map = "sicily"
                    roam = BooleanOrString(true)
                    layoutCenter = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                    layoutSize = StringOrDouble("100%")
                    selectedMode = SelectionModes.single
                    tooltip =
                        unsafeJso<TooltipOption> {
                            show = true
                            confine = true
                            formatter = TooltipFormatter(SICILY_INTRO.joinToString("<br>"))
                        }
                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = false } }
                    select =
                        unsafeJso<SelectOption> {
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#b50205") }
                            label = unsafeJso<LabelOption> { show = false }
                        }
                    regions =
                        arrayOf(
                            sicilyRoute("route1", "right", ROUTE_1),
                            sicilyRoute("route2", "left", ROUTE_2),
                        )
                },
            )
        // The two invisible scatter points act as buttons; see the header comment.
        grid =
            one(
                unsafeJso {
                    top = StringOrDouble(10.0)
                    left = StringOrDouble("center")
                    width = StringOrDouble(80.0)
                    height = StringOrDouble(20.0)
                },
            )
        xAxis =
            one(
                unsafeJso {
                    axisLine = unsafeJso<echarts.options.AxisLineOption> { show = BooleanOrString(false) }
                    splitLine = unsafeJso<echarts.options.SplitLineOption> { show = false }
                    axisLabel = unsafeJso<echarts.options.AxisLabelOption> { show = false }
                    axisTick = unsafeJso<echarts.options.AxisTickOption> { show = BooleanOrString(false) }
                },
            )
        yAxis =
            one(
                unsafeJso {
                    axisLine = unsafeJso<echarts.options.AxisLineOption> { show = BooleanOrString(false) }
                    splitLine = unsafeJso<echarts.options.SplitLineOption> { show = false }
                    axisLabel = unsafeJso<echarts.options.AxisLabelOption> { show = false }
                    axisTick = unsafeJso<echarts.options.AxisTickOption> { show = BooleanOrString(false) }
                },
            )
        series =
            one(
                scatterSeries {
                    selectedMode = SelectionModes.single
                    symbolSize = SymbolSize(0.0)
                    tooltip = unsafeJso<TooltipOption> { show = false }
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            borderColor = "#999"
                            borderWidth = 1.0
                            borderRadius = echarts.options.DoubleOrDoubleArray(2.0)
                            backgroundColor = ZRColor("#fff")
                            padding = echarts.options.DoubleOrDoubleArray(arrayOf(3.0, 5.0))
                            fontSize = StringOrDouble(18.0)
                            opacity = 1.0
                            color = "#333"
                        }
                    encode =
                        unsafeJso<echarts.options.OptionEncode> {
                            label = echarts.options.OptionEncodeValue(echarts.options.DimensionLoose(2.0))
                        }
                    select =
                        unsafeJso<SelectOption> {
                            label =
                                unsafeJso<LabelOption> {
                                    color = "#fff"
                                    borderColor = "#555"
                                    backgroundColor = ZRColor("#555")
                                }
                        }
                    typedData =
                        dataItems(
                            ScatterDataItem(row(0.0, 0.0, "route1")),
                            ScatterDataItem(row(1.0, 0.0, "route2")),
                        )
                },
            )
    }

// ---------------------------------------------------------------------------------------------
// map-HK
// ---------------------------------------------------------------------------------------------

/** Population density of the 18 districts of Hong Kong (2011), person per square kilometre. */
private val HK_DENSITY =
    arrayOf(
        "中西区" to 20057.34,
        "湾仔" to 15477.48,
        "东区" to 31686.1,
        "南区" to 6992.6,
        "油尖旺" to 44045.49,
        "深水埗" to 40689.64,
        "九龙城" to 37659.78,
        "黄大仙" to 45180.97,
        "观塘" to 55204.26,
        "葵青" to 21900.9,
        "荃湾" to 4918.26,
        "屯门" to 5881.84,
        "元朗" to 4178.01,
        "北区" to 2227.92,
        "大埔" to 2180.98,
        "沙田" to 9172.94,
        "西贡" to 3368.0,
        "离岛" to 806.98,
    )

/** English region names of `HK.json` mapped to the Chinese names of [HK_DENSITY]. */
private fun hkNameMap(): js.objects.Record<String, String> =
    stringRecordOf(
        "Central and Western" to "中西区",
        "Eastern" to "东区",
        "Islands" to "离岛",
        "Kowloon City" to "九龙城",
        "Kwai Tsing" to "葵青",
        "Kwun Tong" to "观塘",
        "North" to "北区",
        "Sai Kung" to "西贡",
        "Sha Tin" to "沙田",
        "Sham Shui Po" to "深水埗",
        "Southern" to "南区",
        "Tai Po" to "大埔",
        "Tsuen Wan" to "荃湾",
        "Tuen Mun" to "屯门",
        "Wan Chai" to "湾仔",
        "Wong Tai Sin" to "黄大仙",
        "Yau Tsim Mong" to "油尖旺",
        "Yuen Long" to "元朗",
    )

// ---------------------------------------------------------------------------------------------
// map-usa / map-bar-morph
// ---------------------------------------------------------------------------------------------

/** Population estimates of the US states and Puerto Rico (2012), people. */
private val USA_POPULATION =
    arrayOf(
        "Alabama" to 4822023.0,
        "Alaska" to 731449.0,
        "Arizona" to 6553255.0,
        "Arkansas" to 2949131.0,
        "California" to 38041430.0,
        "Colorado" to 5187582.0,
        "Connecticut" to 3590347.0,
        "Delaware" to 917092.0,
        "District of Columbia" to 632323.0,
        "Florida" to 19317568.0,
        "Georgia" to 9919945.0,
        "Hawaii" to 1392313.0,
        "Idaho" to 1595728.0,
        "Illinois" to 12875255.0,
        "Indiana" to 6537334.0,
        "Iowa" to 3074186.0,
        "Kansas" to 2885905.0,
        "Kentucky" to 4380415.0,
        "Louisiana" to 4601893.0,
        "Maine" to 1329192.0,
        "Maryland" to 5884563.0,
        "Massachusetts" to 6646144.0,
        "Michigan" to 9883360.0,
        "Minnesota" to 5379139.0,
        "Mississippi" to 2984926.0,
        "Missouri" to 6021988.0,
        "Montana" to 1005141.0,
        "Nebraska" to 1855525.0,
        "Nevada" to 2758931.0,
        "New Hampshire" to 1320718.0,
        "New Jersey" to 8864590.0,
        "New Mexico" to 2085538.0,
        "New York" to 19570261.0,
        "North Carolina" to 9752073.0,
        "North Dakota" to 699628.0,
        "Ohio" to 11544225.0,
        "Oklahoma" to 3814820.0,
        "Oregon" to 3899353.0,
        "Pennsylvania" to 12763536.0,
        "Rhode Island" to 1050292.0,
        "South Carolina" to 4723723.0,
        "South Dakota" to 833354.0,
        "Tennessee" to 6456243.0,
        "Texas" to 26059203.0,
        "Utah" to 2855287.0,
        "Vermont" to 626011.0,
        "Virginia" to 8185867.0,
        "Washington" to 6897012.0,
        "West Virginia" to 1855413.0,
        "Wisconsin" to 5726398.0,
        "Wyoming" to 576412.0,
        "Puerto Rico" to 3667084.0,
    )

/** Special areas of `USA.json`: the three regions that sit outside the mainland. */
private fun usaSpecialAreas(): GeoSpecialAreas =
    recordOf(
        "Alaska" to
            unsafeJso<GeoSpecialArea> {
                left = -131.0
                top = 25.0
                width = 15.0
            },
        "Hawaii" to
            unsafeJso<GeoSpecialArea> {
                left = -110.0
                top = 28.0
                width = 5.0
            },
        "Puerto Rico" to
            unsafeJso<GeoSpecialArea> {
                left = -76.0
                top = 26.0
                width = 2.0
            },
    ).unsafeCast<GeoSpecialAreas>()

/** The eleven-step color ramp of the US population pages. */
private val USA_COLORS =
    arrayOf(
        "#313695",
        "#4575b4",
        "#74add1",
        "#abd9e9",
        "#e0f3f8",
        "#ffffbf",
        "#fee090",
        "#fdae61",
        "#f46d43",
        "#d73027",
        "#a50026",
    )

private fun usaVisualMap(): VisualMapOption =
    unsafeJso {
        left = StringOrDouble("right")
        min = 500000.0
        max = 38000000.0
        inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(USA_COLORS) }
        text = arrayOf("High", "Low")
        calculable = true
    }

private fun usaMapOption(): EChartsOption =
    unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "USA Population Estimates (2012)"
                    subtext = "Data from www.census.gov"
                    sublink = "http://www.census.gov/popest/data/datasets.html"
                    left = StringOrDouble("right")
                },
            )
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.item
                    showDelay = 0.0
                    transitionDuration = 0.2
                },
            )
        visualMap = one(usaVisualMap())
        toolbox =
            toolbox(
                dataView = true,
                restore = true,
                saveAsImage = true,
                left = StringOrDouble("left"),
                top = StringOrDouble("top"),
            )
        series =
            one(
                mapSeries {
                    name = "USA PopEstimates"
                    roam = BooleanOrString(true)
                    map = "USA"
                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = true } }
                    typedData = dataItems(*USA_POPULATION.map { mapData(it.first, it.second) }.toTypedArray())
                },
            )
    }

/** The map half of `map-bar-morph`; the series id links it to the bar option. */
private fun morphMapOption(sorted: Array<Pair<String, Double>>): EChartsOption =
    unsafeJso {
        visualMap = one(usaVisualMap())
        series =
            one(
                mapSeries {
                    id = "population"
                    roam = BooleanOrString(true)
                    map = "USA"
                    animationDurationUpdate = echarts.options.AnimationDuration(1000.0)
                    universalTransition = echarts.options.BooleanOrUniversalTransitionOption(true)
                    typedData = dataItems(*sorted.map { mapData(it.first, it.second) }.toTypedArray())
                },
            )
    }

/** The bar half of `map-bar-morph`. */
private fun morphBarOption(sorted: Array<Pair<String, Double>>): EChartsOption =
    unsafeJso {
        xAxis = one(unsafeJso { type = echarts.AxisTypes.value })
        yAxis =
            one(
                unsafeJso {
                    type = echarts.AxisTypes.category
                    axisLabel = unsafeJso<echarts.options.AxisLabelOption> { rotate = 30.0 }
                    data = Array<OptionDataItem?>(sorted.size) { OptionDataItem(OptionDataValue(sorted[it].first)) }
                },
            )
        animationDurationUpdate = echarts.options.AnimationDuration(1000.0)
        series =
            one(
                echarts.options.barSeries {
                    id = "population"
                    universalTransition = echarts.options.BooleanOrUniversalTransitionOption(true)
                    typedData = dataItems(*sorted.map { echarts.options.BarDataItem(OptionDataValue(it.second)) }.toTypedArray())
                },
            )
    }

// ---------------------------------------------------------------------------------------------
// map-iceland-pie
// ---------------------------------------------------------------------------------------------

/** One pie of `map-iceland-pie`, placed at the `[lng, lat]` [center] with [radius] pixels. */
private fun icelandPie(center: Array<StringOrDouble>, radius: Double, values: List<Pair<String, Double>>): PieSeriesOption =
    icelandPie(radius, values) { this.center = center }

/** One pie of `map-iceland-pie`, placed at the geo region [centerName]. */
private fun icelandPie(centerName: String, radius: Double, values: List<Pair<String, Double>>): PieSeriesOption =
    icelandPie(radius, values) { typedCenter = StringOrDouble(centerName) }

private fun icelandPie(
    radius: Double,
    values: List<Pair<String, Double>>,
    place: PieSeriesOption.() -> Unit,
): PieSeriesOption =
    pieSeries {
        coordinateSystem = "geo"
        tooltip = unsafeJso<TooltipOption> { formatter = TooltipFormatter("{b}: {c} ({d}%)") }
        label = unsafeJso<LabelOption> { show = false }
        labelLine = unsafeJso<LabelLineOption> { show = false }
        animationDuration = AnimationDuration(0.0)
        this.radius = StringOrDoubleOrArray(radius)
        place()
        typedData = dataItems(*values.map { pieItem(it.first, it.second) }.toTypedArray())
    }

/** Four categories of random values, one set per pie. */
private fun pieValues(random: Lcg): List<Pair<String, Double>> =
    listOf("A", "B", "C", "D").map { "Category $it" to round(random.next() * 100) }

private fun icelandOption(): EChartsOption {
    val random = Lcg(20260703)
    return unsafeJso {
        geo =
            one(
                unsafeJso<GeoOption> {
                    map = "iceland"
                    roam = BooleanOrString(true)
                    // The map is laid out for the latitude of Iceland.
                    aspectScale = cos(65 * PI / 180)
                    itemStyle = unsafeJso<ItemStyleOption> { areaColor = ZRColor("#e7e8ea") }
                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = false } }
                },
            )
        tooltip = one(unsafeJso<TooltipOption> {})
        legend = one(unsafeJso<LegendOption> {})
        series =
            arrayOf<SeriesOption>(
                icelandPie(
                    arrayOf(StringOrDouble(-19.007740346534653), StringOrDouble(64.1780281585128)),
                    45.0,
                    pieValues(random),
                ),
                icelandPie(
                    arrayOf(StringOrDouble(-17.204666089108912), StringOrDouble(65.44804833928391)),
                    25.0,
                    pieValues(random),
                ),
                icelandPie(
                    arrayOf(StringOrDouble(-15.264995297029705), StringOrDouble(64.8592208009264)),
                    30.0,
                    pieValues(random),
                ),
                // Since 5.4.1 the center also accepts the name of a geo region.
                icelandPie(arrayOf(StringOrDouble("Vestfirðir")), 30.0, pieValues(random)),
            )
    }
}

val geoMapsEntries: List<CatalogEntry> =
    listOf(
        geoMaps.interactive("geo-svg-map", "地图（SVG）", "GEO SVG Map") { chart ->
            loadText("data/Sicily_prehellenic_topographic_map.svg").then { svg ->
                ECharts.registerMap(
                    "sicily",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(sicilyOption())
                chart.onSelectChanged { event: SelectChangedEvent ->
                    if (event.selected.isEmpty()) {
                        chart.dispatchAction(unsafeJso<Payload> { type = "hideTip" })
                        chart.dispatchAction(unsafeJso<Payload> { type = "geoSelect"; geoIndex = 0.0 })
                    } else {
                        val buttonIndex = event.selected[0].dataIndex[0].toInt()
                        val name = SICILY_ROUTE_NAMES[buttonIndex]
                        chart.dispatchAction(
                            unsafeJso<Payload> {
                                type = "geoSelect"
                                geoIndex = 0.0
                                this.name = name
                            },
                        )
                        chart.dispatchAction(
                            unsafeJso<Payload> {
                                type = "showTip"
                                geoIndex = 0.0
                                this.name = name
                            },
                        )
                    }
                }
                null
            }
        },
        geoMaps.interactive("map-HK", "香港18区人口密度（2011）", "Population Density of HongKong (2011)") { chart ->
            chart.showLoading(null)
            loadJson("data/HK.json").then { json ->
                chart.hideLoading()
                registerGeojsonMap("HK", json)
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Population Density of Hong Kong （2011）"
                                    subtext = "Data from Wikipedia"
                                    sublink =
                                        "http://zh.wikipedia.org/wiki/%E9%A6%99%E6%B8%AF%E8%A1%8C%E6%94%BF%E5%8D%80%E5%8A%83" +
                                            "#cite_note-12"
                                },
                            )
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    trigger = TooltipTriggers.item
                                    formatter = TooltipFormatter("{b}<br/>{c} (p / km2)")
                                },
                            )
                        toolbox =
                            toolbox(
                                dataView = true,
                                restore = true,
                                saveAsImage = true,
                                orient = Orients.vertical,
                                left = StringOrDouble("right"),
                                top = StringOrDouble("center"),
                            )
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    min = 800.0
                                    max = 50000.0
                                    text = arrayOf("High", "Low")
                                    realtime = false
                                    calculable = true
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            color = StringOrStringArray(arrayOf("lightskyblue", "yellow", "orangered"))
                                        }
                                },
                            )
                        series =
                            one(
                                mapSeries {
                                    name = "香港18区人口密度"
                                    map = "HK"
                                    label = unsafeJso<LabelOption> { show = true }
                                    nameMap = hkNameMap()
                                    typedData = dataItems(*HK_DENSITY.map { mapData(it.first, it.second) }.toTypedArray())
                                },
                            )
                    },
                )
                null
            }
        },
        geoMaps.interactive("map-usa", "2012 年美国人口统计", "USA Population Estimates (2012)") { chart ->
            chart.showLoading(null)
            loadJson("data/USA.json").then { json ->
                chart.hideLoading()
                ECharts.registerMap(
                    "USA",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
                    usaSpecialAreas(),
                )
                chart.setOption(usaMapOption())
                null
            }
        },
        geoMaps.interactive("map-bar-morph", "地图柱状图变形动画", "Morphing between Map and Bar") { chart, holder ->
            chart.showLoading(null)
            loadJson("data/USA.json").then { json ->
                chart.hideLoading()
                ECharts.registerMap(
                    "USA",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
                    usaSpecialAreas(),
                )
                val sorted = USA_POPULATION.sortedBy { it.second }.toTypedArray()
                val mapOption = morphMapOption(sorted)
                val barOption = morphBarOption(sorted)
                var current = mapOption
                chart.setOption(mapOption)
                holder.setInterval(
                    {
                        current = if (current === mapOption) barOption else mapOption
                        chart.setOption(current, true)
                    },
                    2000,
                )
                null
            }
        },
        geoMaps.interactive("map-iceland-pie", "在地图上显示饼图", "Pie Charts on GEO Map") { chart ->
            chart.showLoading(null)
            loadJson("data/iceland.geo.json").then { json ->
                chart.hideLoading()
                registerGeojsonMap("iceland", json)
                chart.setOption(icelandOption())
                null
            }
        },
    )
