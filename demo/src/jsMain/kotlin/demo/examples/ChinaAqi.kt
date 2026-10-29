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
import kotlin.math.round
import zrender.ZRColor

/*
 * The five pages that draw the same air-quality table on the China map: `scatter-map`,
 * `effectScatter-map`, `heatmap-map`, `geo-map-scatter` and `doc-example/scatter-visualMap-piecewise`.
 *
 * Upstream inlines the 190-city coordinate table and the matching pm2.5 column in every one of these
 * files; the tables are byte-identical, so they are vendored once as `geo-coord-map.json` and
 * `pm25-cities.json` and the Kotlin side only carries the `convertData` that joins them.
 *
 * Changed on purpose:
 *
 * - `geo-map-scatter` is the only page of the batch that also has a `map` series: its 34 province
 *   values are `Math.random()`, replaced with [Lcg], and its short province names go through the
 *   [chinaRegionNames] translation of `DocMaps.kt`, because `china.json` names the regions officially;
 * - the ECharts 4 `itemStyle.normal` / `label.emphasis` states are written in the current flat form;
 * - the `geo` of `geo-map-scatter` hides the label of `境界线`, the name `china.json` gives its eight
 *   boundary line features; the boundary of ECharts 4 had no such region, so upstream never had to;
 * - the legacy `title.x` of `scatter-map` is set as `left`, which is the same anchor since ECharts 5;
 * - the four air-quality pages all carry the upstream title `全国主要城市空气质量`, so the sidebar label of
 *   all but `scatter-map` names its series type to keep the five entries distinguishable.
 */

private val chinaAqiScatter = Category(Groups.SCATTER, "examples/ChinaAqi.kt")
private val chinaAqiHeatmap = Category(Groups.HEATMAP, "examples/ChinaAqi.kt")
private val chinaAqiGeo = Category(Groups.GEO, "examples/ChinaAqi.kt")
private val chinaAqiDoc = Category(Groups.DOC, "examples/ChinaAqi.kt")

/** One row of `pm25-cities.json`. */
private external interface Pm25Row {
    val name: String
    val value: Double
}

/** The coordinate table and the pm2.5 column that the air-quality pages share. */
private class ChinaAqiData(
    val coords: JsAny,
    val rows: Array<Pm25Row>,
)

/**
 * Loads `china.json` and registers it as the `china` map.
 *
 * `internal` because the two weibo check-in pages of `WeiboCheckin.kt` draw on the same map.
 */
internal fun registerChinaMap(): Promise<JsAny> =
    loadJson("data/china.json").then { china ->
        registerGeojsonMap("china", china)
        china
    }

/** The three requests every page of this batch starts with. */
private fun loadChinaAqi(): Promise<ChinaAqiData> =
    Promise.all(
        arrayOf(
            loadJson("data/geo-coord-map.json"),
            loadJson("data/pm25-cities.json"),
            registerChinaMap(),
        ),
    ).then { loaded -> ChinaAqiData(loaded[0], loaded[1].unsafeCast<Array<Pm25Row>>()) }

/** The `[lng, lat]` of [name] in the coordinate table, or null when upstream has no entry for it. */
private fun coordOf(coords: JsAny, name: String): Pair<Double, Double>? {
    val value: JsAny? = js("(coords, name) => coords[name]")(coords, name)
    if (value == null) return null
    return (js("(v) => v[0]")(value) as Double) to (js("(v) => v[1]")(value) as Double)
}

/**
 * Upstream `convertData`: the rows whose city has a coordinate become a `[lng, lat, pm2.5]` row.
 *
 * The name is kept next to the row because the two spellings of `convertData` differ: the scatter and
 * effect-scatter pages push `{ name, value }` objects, the heatmap and the documentation page push
 * the bare array.
 */
private fun geoCoordRows(
    data: ChinaAqiData,
    rows: List<Pair<String, Double>> = data.rows.map { it.name to it.value },
): List<Pair<String, Array<OptionDataValue?>>> =
    rows.mapNotNull { (name, value) ->
        coordOf(data.coords, name)?.let { (lng, lat) -> name to row(lng, lat, value) }
    }

/** The scatter items of [geoCoordRows]: `{ name, value: [lng, lat, pm2.5] }`. */
private fun namedScatterData(converted: List<Pair<String, Array<OptionDataValue?>>>) =
    dataItems(
        *converted
            .map { (name, values) ->
                ScatterDataItem(
                    unsafeJso<ScatterDataItemOption> {
                        this.name = OptionId(name)
                        typedValue = OptionDataValueOrArray(values)
                    },
                )
            }
            .toTypedArray(),
    )

/** The title every air-quality page shows, with the upstream PM25.in attribution. */
private fun aqiTitle(text: String): Array<TitleOption> =
    one(
        unsafeJso<TitleOption> {
            this.text = text
            subtext = "data from PM25.in"
            sublink = "http://www.pm25.in"
            left = StringOrDouble("center")
            textStyle = unsafeJso<TextStyleOption> { color = "#fff" }
        },
    )

/** The dark legend the four air-quality pages share. */
private fun darkLegend(name: String): LegendOption =
    unsafeJso {
        orient = Orients.vertical
        top = StringOrDouble("bottom")
        left = StringOrDouble("right")
        data = legendNames(name)
        textStyle = unsafeJso<LabelOption> { color = "#fff" }
    }

/**
 * The dark China geo of the air-quality pages: a flat dark fill with a thin border.
 *
 * The four upstream pages differ only in `roam` and in whether the hidden label is written at the top
 * level or under `emphasis` — both are the default, so this is their union. `internal` because the two
 * weibo check-in pages of `WeiboCheckin.kt` style their geo the same way.
 */
internal fun darkChinaGeo(roam: Boolean): GeoOption =
    unsafeJso {
        map = "china"
        if (roam) this.roam = BooleanOrString(true)
        label = unsafeJso<LabelOption> { show = false }
        itemStyle =
            unsafeJso<ItemStyleOption> {
                areaColor = ZRColor("#323c48")
                borderColor = ZRColor("#111")
            }
        emphasis =
            unsafeJso<EmphasisOption> {
                label = unsafeJso<LabelOption> { show = false }
                itemStyle = unsafeJso<ItemStyleOption> { areaColor = ZRColor("#2a333d") }
            }
    }

// ---------------------------------------------------------------------------------------------
// scatter-map
// ---------------------------------------------------------------------------------------------

private fun scatterMapOption(data: ChinaAqiData): EChartsOption =
    unsafeJso {
        backgroundColor = ZRColor("#404a59")
        title = aqiTitle("全国主要城市空气质量")
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.item
                    formatter =
                        TooltipFormatter { params ->
                            val item = params.unsafeCast<CallbackDataParams>()
                            TooltipContent(item.name + " : " + numberArray(item.value)[2])
                        }
                },
            )
        legend = one(darkLegend("pm2.5"))
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    min = 0.0
                    max = 200.0
                    calculable = true
                    inRange =
                        unsafeJso<VisualOptionUnit> {
                            color = StringOrStringArray(arrayOf("#50a3ba", "#eac736", "#d94e5d"))
                        }
                    textStyle = unsafeJso<LabelOption> { color = "#fff" }
                },
            )
        geo = one(darkChinaGeo(roam = false))
        series =
            one(
                scatterSeries {
                    name = "pm2.5"
                    coordinateSystem = "geo"
                    typedData = namedScatterData(geoCoordRows(data))
                    symbolSize = SymbolSize(12.0)
                    emphasis =
                        unsafeJso<EmphasisOption> {
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderColor = ZRColor("#fff")
                                    borderWidth = 1.0
                                }
                        }
                },
            )
    }

// ---------------------------------------------------------------------------------------------
// effectScatter-map
// ---------------------------------------------------------------------------------------------

/** The symbol size upstream derives from the pm2.5 dimension of a row. */
private val pm25SymbolSize: SymbolSize =
    SymbolSize { value, _ -> DoubleOrDoubleArray(numberArray(value)[2] / 10) }

private fun effectScatterMapOption(data: ChinaAqiData): EChartsOption {
    val converted = geoCoordRows(data)
    val topSix =
        data.rows.sortedByDescending { it.value }.take(6).map { it.name to it.value }
    return unsafeJso {
        backgroundColor = ZRColor("#404a59")
        title = aqiTitle("全国主要城市空气质量")
        tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
        legend = one(darkLegend("pm2.5"))
        geo = one(darkChinaGeo(roam = true))
        series =
            arrayOf<SeriesOption>(
                scatterSeries {
                    name = "pm2.5"
                    coordinateSystem = "geo"
                    typedData = namedScatterData(converted)
                    encode = unsafeJso<OptionEncode> { value = OptionEncodeValue(DimensionLoose(2.0)) }
                    symbolSize = pm25SymbolSize
                    label =
                        unsafeJso<LabelOption> {
                            formatter = LabelFormatter("{b}")
                            position = LabelPosition("right")
                            show = false
                        }
                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddb926") }
                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = true } }
                },
                effectScatterSeries {
                    name = "Top 5"
                    coordinateSystem = "geo"
                    typedData =
                        dataItems(
                            *geoCoordRows(data, topSix)
                                .map { (name, values) ->
                                    EffectScatterDataItem(
                                        unsafeJso<EffectScatterDataItemOption> {
                                            this.name = OptionId(name)
                                            typedValue = OptionDataValueOrArray(values)
                                        },
                                    )
                                }
                                .toTypedArray(),
                        )
                    encode = unsafeJso<OptionEncode> { value = OptionEncodeValue(DimensionLoose(2.0)) }
                    symbolSize = pm25SymbolSize
                    showEffectOn = "render"
                    rippleEffect = unsafeJso<RippleEffectOption> { brushType = "stroke" }
                    emphasis = unsafeJso<EmphasisOption> { scale = BooleanOrDouble(true) }
                    label =
                        unsafeJso<LabelOption> {
                            formatter = LabelFormatter("{b}")
                            position = LabelPosition("right")
                            show = true
                        }
                    itemStyle =
                        unsafeJso<ItemStyleOption> {
                            color = ZRColor("#f4e925")
                            shadowBlur = 10.0
                            shadowColor = ZRColor("#333")
                        }
                    zlevel = 1.0
                },
            )
    }
}

// ---------------------------------------------------------------------------------------------
// heatmap-map
// ---------------------------------------------------------------------------------------------

private fun heatmapMapOption(data: ChinaAqiData): EChartsOption =
    unsafeJso {
        backgroundColor = ZRColor("#404a59")
        title = aqiTitle("全国主要城市空气质量")
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    min = 0.0
                    max = 500.0
                    splitNumber = 5.0
                    inRange =
                        unsafeJso<VisualOptionUnit> {
                            color = StringOrStringArray(arrayOf("#50a3ba", "#eac736", "#d94e5d"))
                        }
                    textStyle = unsafeJso<LabelOption> { color = "#fff" }
                },
            )
        geo = one(darkChinaGeo(roam = true))
        series =
            one(
                heatmapSeries {
                    name = "AQI"
                    coordinateSystem = "geo"
                    typedData = dataItems(*geoCoordRows(data).map { HeatmapDataItem(it.second) }.toTypedArray())
                },
            )
    }

// ---------------------------------------------------------------------------------------------
// doc-example/scatter-visualMap-piecewise
// ---------------------------------------------------------------------------------------------

private fun docScatterPiecewiseOption(data: ChinaAqiData): EChartsOption =
    unsafeJso {
        backgroundColor = ZRColor("#404a59")
        title = aqiTitle("全国主要城市空气质量")
        tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
        legend = one(darkLegend("pm2.5"))
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    min = 0.0
                    max = 300.0
                    splitNumber = 5.0
                    // The legacy palette key: `VisualMapModel` reverses it into `inRange.color`.
                    color = arrayOf("#d94e5d", "#eac736", "#50a3ba")
                    textStyle = unsafeJso<LabelOption> { color = "#fff" }
                },
            )
        geo = one(darkChinaGeo(roam = false))
        series =
            one(
                scatterSeries {
                    name = "pm2.5"
                    coordinateSystem = "geo"
                    typedData = dataItems(*geoCoordRows(data).map { ScatterDataItem(it.second) }.toTypedArray())
                },
            )
    }

// ---------------------------------------------------------------------------------------------
// geo-map-scatter
// ---------------------------------------------------------------------------------------------

/** The airplane silhouette of `geo-map-scatter`, drawn instead of a dot for the 27 cities. */
private const val AQI_PLANE_PATH =
    "path://M1705.06,1318.313v-89.254l-319.9-221.799l0.073-208.063c0.521-84.662-26.629-121.796" +
        "-63.961-121.491c-37.332-0.305-64.482,36.829-63.961,121.491l0.073,208.063l-319.9,221.799v89.254" +
        "l330.343-157.288l12.238,241.308l-134.449,92.931l0.531,42.034l175.125-42.917l175.125,42.917" +
        "l0.531-42.034l-134.449-92.931l12.238-241.308L1705.06,1318.313z"

/** The 27 cities of `geo-map-scatter`: a subset of the shared table, with upstream's own values. */
private val GEO_MAP_SCATTER_CITIES: List<Pair<String, Double>> =
    listOf(
        "海门" to 9.0, "鄂尔多斯" to 12.0, "招远" to 12.0, "舟山" to 12.0, "齐齐哈尔" to 14.0,
        "盐城" to 15.0, "赤峰" to 16.0, "青岛" to 18.0, "乳山" to 18.0, "金昌" to 19.0,
        "泉州" to 21.0, "南通" to 23.0, "拉萨" to 24.0, "云浮" to 24.0, "上海" to 25.0,
        "攀枝花" to 25.0, "承德" to 25.0, "汕尾" to 26.0, "丹东" to 27.0, "瓦房店" to 30.0,
        "延安" to 38.0, "咸阳" to 43.0, "南昌" to 54.0, "柳州" to 54.0, "三亚" to 54.0,
        "泸州" to 57.0, "克拉玛依" to 72.0,
    )

private fun geoMapScatterOption(data: ChinaAqiData): EChartsOption {
    val random = Lcg(20261101)
    return unsafeJso {
        tooltip = one(unsafeJso<TooltipOption> {})
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    min = 0.0
                    max = 1500.0
                    left = StringOrDouble("left")
                    top = StringOrDouble("bottom")
                    text = arrayOf("High", "Low")
                    seriesIndex = IndexOrIndexArray(arrayOf(1.0))
                    inRange =
                        unsafeJso<VisualOptionUnit> {
                            color = StringOrStringArray(arrayOf("#e0ffff", "#006edd"))
                        }
                    calculable = true
                },
            )
        geo =
            one(
                unsafeJso<GeoOption> {
                    map = "china"
                    roam = BooleanOrString(true)
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            color = "rgba(0,0,0,0.4)"
                        }
                    itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("rgba(0, 0, 0, 0.2)") }
                    // `china.json` carries eight line features named `境界线`; without this override the
                    // page prints their name on top of the map, which the ECharts 4 boundary did not have.
                    regions =
                        arrayOf(
                            unsafeJso<RegionOption> {
                                name = "境界线"
                                label = unsafeJso<LabelOption> { show = false }
                            },
                        )
                    emphasis =
                        unsafeJso<EmphasisOption> {
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    areaColor = null
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 0.0
                                    shadowBlur = 20.0
                                    borderWidth = 0.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        }
                },
            )
        series =
            arrayOf<SeriesOption>(
                scatterSeries {
                    coordinateSystem = "geo"
                    typedData =
                        namedScatterData(geoCoordRows(data, GEO_MAP_SCATTER_CITIES))
                    symbolSize = SymbolSize(20.0)
                    symbol = SymbolShape(AQI_PLANE_PATH)
                    symbolRotate = SymbolRotate(35.0)
                    label =
                        unsafeJso<LabelOption> {
                            formatter = LabelFormatter("{b}")
                            position = LabelPosition("right")
                            show = false
                        }
                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#F06C00") }
                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = true } }
                },
                mapSeries {
                    name = "categoryA"
                    geoIndex = 0.0
                    typedData =
                        dataItems(
                            *chinaRegionNames.values.map { mapData(it, round(random.next() * 1000)) }
                                .toTypedArray(),
                        )
                },
            )
    }
}

/**
 * Loads the three files behind [option] and feeds them to [chart] once they are there.
 *
 * The boundary and both tables are fetched per navigation, so the chart can already be disposed when
 * the responses land; [EChartsInstance.isDisposed] is checked before touching it.
 */
private fun loadIntoChinaAqi(chart: EChartsInstance, option: (ChinaAqiData) -> EChartsOption) {
    chart.showLoading(null)
    loadChinaAqi().then { data ->
        if (!chart.isDisposed()) {
            chart.hideLoading()
            chart.setOption(option(data))
        }
        null
    }
}

val chinaAqiEntries: List<CatalogEntry> =
    listOf(
        chinaAqiScatter.interactive("scatter-map", "全国主要城市空气质量", "Air Quality") { chart ->
            loadIntoChinaAqi(chart, ::scatterMapOption)
        },
        chinaAqiScatter.interactive(
            "effectScatter-map",
            "全国主要城市空气质量 - 涟漪散点图",
            "Air Quality (effect scatter)",
            "同样的 190 个城市：普通散点按 pm2.5 定大小，前六名换成带涟漪的 `effectScatter`。",
            "The same 190 cities: the scatter sizes its symbols by pm2.5, and the six worst become " +
                "rippling effect-scatter symbols.",
        ) { chart ->
            loadIntoChinaAqi(chart, ::effectScatterMapOption)
        },
        chinaAqiHeatmap.interactive("heatmap-map", "全国主要城市空气质量 - 热力图", "Air Quality Heatmap") { chart ->
            loadIntoChinaAqi(chart, ::heatmapMapOption)
        },
        chinaAqiGeo.interactive(
            "geo-map-scatter",
            "map 与 scatter 共用一个 geo",
            "Map and scatter sharing a geo",
            "散点与地图系列都挂在同一个 `geo` 组件上：`series.geoIndex` 让地图复用坐标，视觉映射只作用于它。",
            "A scatter and a map series share one `geo` component: `series.geoIndex` makes the map " +
                "reuse the coordinate system, and the visual map targets only it.",
        ) { chart ->
            loadIntoChinaAqi(chart, ::geoMapScatterOption)
        },
        chinaAqiDoc.interactive(
            "doc-example/scatter-visualMap-piecewise",
            "全国主要城市空气质量（分段视觉映射）",
            "China Air Quality with a Piecewise Visual Map",
            "与 `scatter-map` 同一份数据，视觉映射改成 `splitNumber` 切分的色带，颜色用旧版 `color` 键。",
            "The same table as `scatter-map`, mapped by a visual map split into `splitNumber` bands " +
                "and colored through the legacy `color` key.",
        ) { chart ->
            loadIntoChinaAqi(chart, ::docScatterPiecewiseOption)
        },
    )
