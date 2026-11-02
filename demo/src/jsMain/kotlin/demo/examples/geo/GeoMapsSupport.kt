package demo.examples.geo

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.round
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.gl.square
import demo.examples.matrix.names
import demo.examples.shared.Lcg
import demo.examples.shared.axisLabel
import demo.examples.shared.pieItem
import demo.examples.shared.recordOf
import demo.examples.shared.row
import demo.examples.shared.toolbox
import demo.examples.sunburst.radius

/** Registers a GeoJSON document as the map [name]. */
internal fun registerGeojsonMap(name: String, json: kotlin.js.JsAny) {
    ECharts.registerMap(
        name,
        MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = json.unsafeCast<GeoJSONSourceInput>() }),
    )
}

/** A string dictionary for the option fields typed as `Record<String, String>`. */
internal fun stringRecordOf(vararg pairs: Pair<String, String>): js.objects.Record<String, String> =
    js(
        "(keys, values) => { const o = {}; for (let i = 0; i < keys.length; i++) { o[keys[i]] = values[i]; } return o; }",
    )(
        Array(pairs.size) { pairs[it].first },
        Array(pairs.size) { pairs[it].second },
    ).unsafeCast<js.objects.Record<String, String>>()

/** One `{ name, value }` entry of a map series. */
internal fun mapData(name: String, value: Double): MapDataItem =
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
internal fun sicilyRoute(name: String, position: String, lines: List<String>): RegionOption =
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

internal val ROUTE_1 =
    listOf(
        "Route 1:",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
        "xxxxxxxxxxxxxxxxxxxxxxxxxxx",
    )

internal val ROUTE_2 =
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

internal val SICILY_INTRO =
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
internal val SICILY_ROUTE_NAMES = arrayOf("route1", "route2")

internal fun sicilyOption(): EChartsOption =
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
internal val HK_DENSITY =
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
internal fun hkNameMap(): js.objects.Record<String, String> =
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
internal val USA_POPULATION =
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
internal fun usaSpecialAreas(): GeoSpecialAreas =
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
internal val USA_COLORS =
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

internal fun usaVisualMap(): VisualMapOption =
    unsafeJso {
        left = StringOrDouble("right")
        min = 500000.0
        max = 38000000.0
        inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(USA_COLORS) }
        text = arrayOf("High", "Low")
        calculable = true
    }

internal fun usaMapOption(): EChartsOption =
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
internal fun morphMapOption(sorted: Array<Pair<String, Double>>): EChartsOption =
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
internal fun morphBarOption(sorted: Array<Pair<String, Double>>): EChartsOption =
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
internal fun icelandPie(center: Array<StringOrDouble>, radius: Double, values: List<Pair<String, Double>>): PieSeriesOption =
    icelandPie(radius, values) { this.center = center }

/** One pie of `map-iceland-pie`, placed at the geo region [centerName]. */
internal fun icelandPie(centerName: String, radius: Double, values: List<Pair<String, Double>>): PieSeriesOption =
    icelandPie(radius, values) { typedCenter = StringOrDouble(centerName) }

internal fun icelandPie(
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
internal fun pieValues(random: Lcg): List<Pair<String, Double>> =
    listOf("A", "B", "C", "D").map { "Category $it" to round(random.next() * 100) }

internal fun icelandOption(): EChartsOption {
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
