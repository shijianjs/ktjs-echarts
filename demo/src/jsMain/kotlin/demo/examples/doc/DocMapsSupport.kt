package demo.examples.doc

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.round
import demo.examples.gauge.center
import demo.examples.geo.HK_DENSITY
import demo.examples.geo.hkNameMap
import demo.examples.geo.mapData
import demo.examples.geo.region
import demo.examples.gl.WorldPopulationItem
import demo.examples.gl.regionName
import demo.examples.matrix.names
import demo.examples.shared.Lcg
import demo.examples.shared.legendNames
import demo.examples.shared.numeric
import demo.examples.shared.thousands
import demo.examples.shared.toolbox

/** `1359821.465` -> `"1,359,821.465"`, the thousands grouping of the original's tooltip. */
internal fun grouped(value: Double): String {
    val parts = value.toString().split('.')
    val whole = parts[0].reversed().chunked(3).joinToString(",").reversed()
    return if (parts.size > 1) "$whole.${parts[1]}" else whole
}

// ---------------------------------------------------------------------------------------------
// map-example
// ---------------------------------------------------------------------------------------------

/**
 * The official names of the 34 provinces of `china.json`, keyed by the short names the original uses.
 *
 * The vendored boundary names its features `北京市` / `内蒙古自治区` / `台湾省` because it comes from the
 * modern DataV-derived GeoJSON; the original page was written against the ECharts 4 `china.js`, whose
 * features carried the short names. ECharts matches data to region by exact name, so the port keeps
 * the names of the original and translates them here.
 *
 * `internal` because `geo-map-scatter` of `ChinaAqi.kt` lists the same 34 provinces.
 */
internal val chinaRegionNames =
    mapOf(
        "北京" to "北京市",
        "天津" to "天津市",
        "上海" to "上海市",
        "重庆" to "重庆市",
        "河北" to "河北省",
        "河南" to "河南省",
        "云南" to "云南省",
        "辽宁" to "辽宁省",
        "黑龙江" to "黑龙江省",
        "湖南" to "湖南省",
        "安徽" to "安徽省",
        "山东" to "山东省",
        "新疆" to "新疆维吾尔自治区",
        "江苏" to "江苏省",
        "浙江" to "浙江省",
        "江西" to "江西省",
        "湖北" to "湖北省",
        "广西" to "广西壮族自治区",
        "甘肃" to "甘肃省",
        "山西" to "山西省",
        "内蒙古" to "内蒙古自治区",
        "陕西" to "陕西省",
        "吉林" to "吉林省",
        "福建" to "福建省",
        "贵州" to "贵州省",
        "广东" to "广东省",
        "青海" to "青海省",
        "西藏" to "西藏自治区",
        "四川" to "四川省",
        "宁夏" to "宁夏回族自治区",
        "海南" to "海南省",
        "台湾" to "台湾省",
        "香港" to "香港特别行政区",
        "澳门" to "澳门特别行政区",
    )

/** The region of `china.json` that the short name [name] of the original refers to. */
internal fun chinaRegionName(name: String): String = chinaRegionNames.getValue(name)

/** The provinces of the three series of `doc-example/map-example`, in the order of the original. */
internal val IPHONE_PROVINCES =
    mapOf(
        "iphone3" to
            arrayOf(
                "北京", "天津", "上海", "重庆", "河北", "河南", "云南", "辽宁", "黑龙江", "湖南",
                "安徽", "山东", "新疆", "江苏", "浙江", "江西", "湖北", "广西", "甘肃", "山西",
                "内蒙古", "陕西", "吉林", "福建", "贵州", "广东", "青海", "西藏", "四川", "宁夏",
                "海南", "台湾", "香港", "澳门",
            ),
        "iphone4" to
            arrayOf(
                "北京", "天津", "上海", "重庆", "河北", "安徽", "新疆", "浙江", "江西", "山西",
                "内蒙古", "吉林", "福建", "广东", "西藏", "四川", "宁夏", "香港", "澳门",
            ),
        "iphone5" to arrayOf("北京", "天津", "上海", "广东", "台湾", "香港", "澳门"),
    )

/** One of the three series of `doc-example/map-example`: `{ name, value: round(random() * 1000) }`. */
internal fun chinaSalesSeries(
    name: String,
    random: Lcg,
    roam: BooleanOrString? = null,
): MapSeriesOption =
    mapSeries {
        this.name = name
        map = "china"
        if (roam != null) this.roam = roam
        label = unsafeJso<LabelOption> { show = false }
        emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = true } }
        typedData =
            dataItems(
                *IPHONE_PROVINCES.getValue(name)
                    .map { mapData(chinaRegionName(it), round(random.next() * 1000)) }
                    .toTypedArray(),
            )
    }

internal fun chinaSalesOption(): EChartsOption {
    val random = Lcg(20261030)
    return unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "iphone销量"
                    subtext = "纯属虚构"
                    left = StringOrDouble("center")
                },
            )
        tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
        legend =
            one(
                unsafeJso<LegendOption> {
                    orient = Orients.vertical
                    left = StringOrDouble("left")
                    data = legendNames("iphone3", "iphone4", "iphone5")
                },
            )
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    min = 0.0
                    max = 2500.0
                    left = StringOrDouble("left")
                    top = StringOrDouble("bottom")
                    text = arrayOf("高", "低")
                    calculable = true
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
        series =
            arrayOf<SeriesOption>(
                chinaSalesSeries("iphone3", random, roam = BooleanOrString(false)),
                chinaSalesSeries("iphone4", random),
                chinaSalesSeries("iphone5", random),
            )
    }
}

// ---------------------------------------------------------------------------------------------
// map-visualMap-continuous
// ---------------------------------------------------------------------------------------------

/** The `data` of the world map: the vendored table, named the way `world.json` spells it. */
internal fun worldMapData(population: JsAny): Array<MapDataItem?> {
    val items = population.unsafeCast<Array<WorldPopulationItem>>()
    return dataItems(
        *Array(items.size) { index -> mapData(regionName(items[index].name), items[index].value) },
    )
}

internal fun worldPopulationOption(population: JsAny): EChartsOption =
    unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "World Population (2010)"
                    subtext = "from United Nations, Total population, both sexes combined, as of 1 July (thousands)"
                    sublink = "http://esa.un.org/wpp/Excel-Data/population.htm"
                    left = StringOrDouble("center")
                    top = StringOrDouble("top")
                },
            )
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.item
                    formatter =
                        TooltipFormatter { params ->
                            val item = params.unsafeCast<CallbackDataParams>()
                            TooltipContent(
                                item.seriesName.orEmpty() + "<br/>" + item.name + " : " + grouped(numeric(item.value)),
                            )
                        }
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
                    type = VisualMapTypes.continuous
                    min = 0.0
                    max = 1000000.0
                    text = arrayOf("High", "Low")
                    realtime = false
                    calculable = true
                    // The legacy palette key: `VisualMapModel` reverses it into `inRange.color`.
                    color = arrayOf("orangered", "yellow", "lightskyblue")
                },
            )
        series =
            one(
                mapSeries {
                    name = "World Population (2010)"
                    map = "world"
                    roam = BooleanOrString(true)
                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = true } }
                    typedData = worldMapData(population)
                },
            )
    }

// ---------------------------------------------------------------------------------------------
// map-visualMap-continuous-text / map-visualMap-piecewise-text
// ---------------------------------------------------------------------------------------------

/** The Wikipedia link of the Hong Kong density pages. */
internal const val HK_WIKI_LINK =
    "http://zh.wikipedia.org/wiki/%E9%A6%99%E6%B8%AF%E8%A1%8C%E6%94%BF%E5%8D%80%E5%8A%83#cite_note-12"

/** The two Hong Kong pages differ only in the variant of their visual map. */
internal fun hkDensityOption(type: VisualMapType): EChartsOption =
    unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "香港18区人口密度 （2011）"
                    subtext = "人口密度数据来自Wikipedia"
                    sublink = HK_WIKI_LINK
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
                    this.type = type
                    min = 800.0
                    max = 50000.0
                    text = arrayOf("High", "Low")
                    realtime = false
                    calculable = true
                    color = arrayOf("orangered", "yellow", "lightskyblue")
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
    }

// ---------------------------------------------------------------------------------------------
// map-visualMap-pieces
// ---------------------------------------------------------------------------------------------

internal external interface UsaFeatureProperties {
    val name: String
}

internal external interface UsaFeature {
    val properties: UsaFeatureProperties
}

internal external interface UsaGeoJson {
    val features: Array<UsaFeature>
}

/** One `{ min, max, label, color }` piece of the visual map of `map-visualMap-pieces`. */
internal fun visualPiece(
    min: Double? = null,
    max: Double? = null,
    label: String? = null,
    color: String? = null,
): VisualPiece =
    unsafeJso {
        this.min = min
        this.max = max
        this.label = label
        if (color != null) this.color = StringOrStringArray(color)
    }

/** The region names upstream reads from `usaJson.features` with `echarts.util.map`. */
internal fun usaRegionNames(usa: JsAny): Array<String> =
    usa.unsafeCast<UsaGeoJson>().features.map { it.properties.name }.toTypedArray()

internal fun usaOrderOption(names: Array<String>): EChartsOption {
    val random = Lcg(20261031)
    val layout: LabelLayoutCallback =
        { params ->
            unsafeJso<LabelLayoutOption> {
                hideOverlap = true
                fontSize = maxOf((params.rect?.width ?: 0.0) / 10, 8.0)
            }
        }
    return unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "Order"
                    subtext = "Fake Data"
                    left = StringOrDouble("center")
                },
            )
        tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    type = VisualMapTypes.piecewise
                    pieces =
                        arrayOf(
                            visualPiece(min = 1500.0),
                            visualPiece(min = 900.0, max = 1500.0),
                            visualPiece(min = 310.0, max = 1000.0),
                            visualPiece(min = 200.0, max = 300.0, label = "200 - 300 (Custom Color)", color = "lightgreen"),
                            visualPiece(min = 10.0, max = 200.0, label = "10 to 200 (Custom Label)"),
                        )
                    outOfRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray("#eee") }
                },
            )
        series =
            one(
                mapSeries {
                    name = "Order"
                    map = "USA"
                    roam = BooleanOrString(true)
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            color = "inherit"
                        }
                    labelLayout = LabelLayout(layout)
                    typedData =
                        dataItems(
                            *Array(names.size) { index ->
                                mapData(names[index], round(random.next() * random.next() * 1500))
                            },
                        )
                },
            )
    }
}
