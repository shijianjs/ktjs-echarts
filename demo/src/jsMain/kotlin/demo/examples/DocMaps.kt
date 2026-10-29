package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.round

/*
 * The five map tutorials of the official documentation: `doc-example/map-example`, the two
 * `map-visualMap-*-text` Hong Kong pages, `map-visualMap-continuous` and `map-visualMap-pieces`.
 *
 * Changed on purpose:
 *
 * - the originals declare `mapType`, which ECharts 5 dropped: `MapSeries.getMapType` reads `map`
 *   today, so the ports use that key;
 * - the originals also rely on the gallery having pre-registered the `world` / `china` maps. No
 *   build does that any more, so each page fetches its GeoJSON and registers it, exactly like the
 *   `map-HK` and `map-usa` pages of `GeoMaps.kt` already do;
 * - `map-visualMap-continuous` keeps its 177 country values inline upstream; the port reads the
 *   vendored `world-population.json`, which holds the same table, and translates the country names to
 *   the abbreviations `world.json` uses — the same [regionName] table the GL globe pages apply;
 * - `map-example` keeps the short province names of the original and translates them into the
 *   official names of the vendored boundary (see [chinaRegionName]);
 * - the `Math.random()` of `map-example` and `map-visualMap-pieces` is replaced with [Lcg], so both
 *   pages draw the same values on every visit;
 * - the tooltip of `map-visualMap-continuous` groups its value into thousands. The original splits the
 *   stringified value and always appends `'.' + value[1]`, which prints `...undefined` for a whole
 *   number; [grouped] appends the fraction only when there is one;
 * - the ECharts 4 `label.normal` / `itemStyle.normal.label` states are set directly, and the dead
 *   `toolbox.feature.mark` key is left out.
 */

private val docMaps = Category(Groups.DOC, "examples/DocMaps.kt")

/** `1359821.465` -> `"1,359,821.465"`, the thousands grouping of the original's tooltip. */
private fun grouped(value: Double): String {
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
 * `internal` because `geo-map-scatter` of [ChinaAqi] lists the same 34 provinces.
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
private fun chinaRegionName(name: String): String = chinaRegionNames.getValue(name)

/** The provinces of the three series of `doc-example/map-example`, in the order of the original. */
private val IPHONE_PROVINCES =
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
private fun chinaSalesSeries(
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

private fun chinaSalesOption(): EChartsOption {
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
private fun worldMapData(population: JsAny): Array<MapDataItem?> {
    val items = population.unsafeCast<Array<WorldPopulationItem>>()
    return dataItems(
        *Array(items.size) { index -> mapData(regionName(items[index].name), items[index].value) },
    )
}

private fun worldPopulationOption(population: JsAny): EChartsOption =
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
private const val HK_WIKI_LINK =
    "http://zh.wikipedia.org/wiki/%E9%A6%99%E6%B8%AF%E8%A1%8C%E6%94%BF%E5%8D%80%E5%8A%83#cite_note-12"

/** The two Hong Kong pages differ only in the variant of their visual map. */
private fun hkDensityOption(type: VisualMapType): EChartsOption =
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

private external interface UsaFeatureProperties {
    val name: String
}

private external interface UsaFeature {
    val properties: UsaFeatureProperties
}

private external interface UsaGeoJson {
    val features: Array<UsaFeature>
}

/** One `{ min, max, label, color }` piece of the visual map of `map-visualMap-pieces`. */
private fun visualPiece(
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
private fun usaRegionNames(usa: JsAny): Array<String> =
    usa.unsafeCast<UsaGeoJson>().features.map { it.properties.name }.toTypedArray()

private fun usaOrderOption(names: Array<String>): EChartsOption {
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

val docMapsEntries: List<CatalogEntry> =
    listOf(
        docMaps.interactive(
            "doc-example/map-example",
            "中国地图（iphone 销量）",
            "China Map (iphone Sales)",
            "三个地图系列叠加在同一张中国地图上，图例切换、视觉映射按销量从高到低着色。",
            "Three map series stacked on one China map: the legend switches between them and a " +
                "visual map colors the provinces by the sales figures.",
        ) { chart ->
            chart.showLoading(null)
            loadJson("data/china.json").then { china ->
                chart.hideLoading()
                registerGeojsonMap("china", china)
                chart.setOption(chinaSalesOption())
                null
            }
        },
        docMaps.interactive(
            "doc-example/map-visualMap-continuous",
            "世界人口连续型视觉映射",
            "World Population with a Continuous Visual Map",
            "177 个国家按 2010 年人口落在连续色带上，色带两端可拖拽筛选。",
            "The 2010 population of 177 countries mapped onto a continuous color band whose handles " +
                "filter the range.",
        ) { chart ->
            registerWorldMap().then {
                loadJson("data/world-population.json").then { population ->
                    chart.setOption(worldPopulationOption(population))
                    null
                }
            }
        },
        docMaps.interactive(
            "doc-example/map-visualMap-continuous-text",
            "香港人口密度（连续型视觉映射）",
            "Hong Kong Density with a Continuous Visual Map",
            "香港 18 区人口密度用连续色带映射，`nameMap` 把英文区名换成中文。",
            "The density of the 18 districts of Hong Kong on a continuous color band, with `nameMap` " +
                "translating the English region names.",
        ) { chart ->
            chart.showLoading(null)
            loadJson("data/HK.json").then { hk ->
                chart.hideLoading()
                registerGeojsonMap("HK", hk)
                chart.setOption(hkDensityOption(VisualMapTypes.continuous))
                null
            }
        },
        docMaps.interactive(
            "doc-example/map-visualMap-piecewise-text",
            "香港人口密度（分段型视觉映射）",
            "Hong Kong Density with a Piecewise Visual Map",
            "与连续型一页同样的数据，视觉映射改成分段型：按 `min`/`max` 自动切成若干段。",
            "The same data as the continuous page, mapped by a piecewise visual map that splits the " +
                "`min`/`max` range into pieces.",
        ) { chart ->
            chart.showLoading(null)
            loadJson("data/HK.json").then { hk ->
                chart.hideLoading()
                registerGeojsonMap("HK", hk)
                chart.setOption(hkDensityOption(VisualMapTypes.piecewise))
                null
            }
        },
        docMaps.interactive(
            "doc-example/map-visualMap-pieces",
            "美国订单分段视觉映射",
            "USA Orders with Piecewise Visual Map",
            "虚构订单量按自定义分段着色，两段带自己的标签与颜色，区间外用灰色；标签按区域宽度缩放并隐藏重叠。",
            "Fake order volumes colored by custom pieces — two of them carry their own label and " +
                "color, everything else falls out of range — with labels scaled to the region width.",
        ) { chart ->
            chart.showLoading(null)
            loadJson("data/USA.json").then { usa ->
                chart.hideLoading()
                ECharts.registerMap(
                    "USA",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = usa.unsafeCast<GeoJSONSourceInput>() }),
                    usaSpecialAreas(),
                )
                chart.setOption(usaOrderOption(usaRegionNames(usa)))
                null
            }
        },
    )
