package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.Promise
import zrender.ZRColor

/*
 * The `geo-lines` page: three groups of ten migration routes, each drawn three times over — a lines
 * series with a trail effect, the same lines with an arrow end and an airplane flying along them, and
 * an effect-scatter series marking the destinations.
 *
 * Upstream inlines the 114-city `geoCoordMap` of this page. It is a different table from the 190-city
 * one of the air-quality pages: every name is shared, but no coordinate is, because those were rounded
 * to two decimals while these keep four. Hence `data/geo-lines-coord-map.json`, and the same
 * [coordOf] lookup the air-quality pages use.
 *
 * Changed on purpose:
 *
 * - the ECharts 4 `lineStyle.normal` of the first series of each group is written in the flat form;
 * - `china.json` carries eight line features named `境界线`; the page shows region labels on emphasis,
 *   so their label is turned off in `regions` — the ECharts 4 boundary had no such features;
 * - the three route tables and the single airplane glyph are written in Kotlin, since they are a few
 *   dozen literals rather than a data file.
 */

private val geoLines = Category(Groups.GEO, "examples/GeoLines.kt")

/** One origin of the migration: its color and the ten destinations with the value upstream gives them. */
private class MigrationGroup(val origin: String, val color: String, val destinations: List<Pair<String, Double>>)

private val migrationGroups =
    arrayOf(
        MigrationGroup(
            "北京",
            "#a6c84c",
            listOf(
                "上海" to 95.0,
                "广州" to 90.0,
                "大连" to 80.0,
                "南宁" to 70.0,
                "南昌" to 60.0,
                "拉萨" to 50.0,
                "长春" to 40.0,
                "包头" to 30.0,
                "重庆" to 20.0,
                "常州" to 10.0,
            ),
        ),
        MigrationGroup(
            "上海",
            "#ffa022",
            listOf(
                "包头" to 95.0,
                "昆明" to 90.0,
                "广州" to 80.0,
                "郑州" to 70.0,
                "长春" to 60.0,
                "重庆" to 50.0,
                "长沙" to 40.0,
                "北京" to 30.0,
                "丹东" to 20.0,
                "大连" to 10.0,
            ),
        ),
        MigrationGroup(
            "广州",
            "#46bee9",
            listOf(
                "福州" to 95.0,
                "太原" to 90.0,
                "长春" to 80.0,
                "重庆" to 70.0,
                "西安" to 60.0,
                "成都" to 50.0,
                "常州" to 40.0,
                "北京" to 30.0,
                "北海" to 20.0,
                "海口" to 10.0,
            ),
        ),
    )

/** Loads the page's own coordinate table, with the China map registered underneath. */
private fun loadGeoLines(): Promise<JsAny> =
    Promise.all(arrayOf(registerChinaMap(), loadJson("data/geo-lines-coord-map.json"))).then { loaded ->
        loaded[1]
    }

/**
 * Upstream `convertData`: one `{ fromName, toName, coords }` row per route whose ends are both in the
 * coordinate table. All 33 cities of the three groups are in the table, so nothing is dropped.
 */
private fun migrationLines(table: JsAny, group: MigrationGroup): Array<LinesDataItemOption?> {
    val from = coordOf(table, group.origin)
    return dataItems(
        *group.destinations
            .mapNotNull { (destination, _) ->
                val to = coordOf(table, destination) ?: return@mapNotNull null
                if (from == null) return@mapNotNull null
                unsafeJso<LinesDataItemOption> {
                    fromName = group.origin
                    toName = destination
                    coords = arrayOf(arrayOf(from.first, from.second), arrayOf(to.first, to.second))
                }
            }
            .toTypedArray(),
    )
}

/** The destinations as `{ name, value: [lng, lat, value] }` ripple points, sized by their value. */
private fun migrationPoints(table: JsAny, group: MigrationGroup) =
    dataItems(
        *group.destinations
            .mapNotNull { (destination, value) ->
                val coord = coordOf(table, destination) ?: return@mapNotNull null
                EffectScatterDataItem(
                    unsafeJso<EffectScatterDataItemOption> {
                        name = OptionId(destination)
                        typedValue = OptionDataValueOrArray(row(coord.first, coord.second, value))
                    },
                )
            }
            .toTypedArray(),
    )

/** The dark China basemap of the page: region names appear on emphasis only. */
private fun migrationGeo(): GeoOption =
    unsafeJso {
        map = "china"
        roam = BooleanOrString(true)
        label = unsafeJso<LabelOption> { show = false }
        itemStyle =
            unsafeJso<ItemStyleOption> {
                areaColor = ZRColor("#323c48")
                borderColor = ZRColor("#404a59")
            }
        emphasis =
            unsafeJso<EmphasisOption> {
                label = unsafeJso<LabelOption> { show = true }
                itemStyle = unsafeJso<ItemStyleOption> { areaColor = ZRColor("#2a333d") }
            }
        regions =
            arrayOf(
                unsafeJso<RegionOption> {
                    name = "境界线"
                    label = unsafeJso<LabelOption> { show = false }
                    emphasis = unsafeJso<RegionStateOption> { label = unsafeJso<LabelOption> { show = false } }
                },
            )
    }

/** The nine series of the page: three per group, in the order the legend names them. */
private fun migrationSeries(table: JsAny): Array<SeriesOption> =
    migrationGroups
        .flatMap { group ->
            val name = "${group.origin} Top10"
            listOf(
                linesSeries {
                    this.name = name
                    zlevel = 1.0
                    effect =
                        unsafeJso<LinesEffectOption> {
                            show = true
                            period = 6.0
                            trailLength = 0.7
                            color = ZRColor("#fff")
                            symbolSize = 3.0
                        }
                    lineStyle =
                        unsafeJso<LineStyleOption> {
                            color = ZRColor(group.color)
                            width = 0.0
                            curveness = 0.2
                        }
                    typedData = migrationLines(table, group)
                },
                linesSeries {
                    this.name = name
                    zlevel = 2.0
                    symbol = StringOrStringArray(arrayOf("none", "arrow"))
                    symbolSize = DoubleOrDoubleArray(10.0)
                    effect =
                        unsafeJso<LinesEffectOption> {
                            show = true
                            period = 6.0
                            trailLength = 0.0
                            symbol = PLANE_SYMBOL_PATH
                            symbolSize = 15.0
                        }
                    lineStyle =
                        unsafeJso<LineStyleOption> {
                            color = ZRColor(group.color)
                            width = 1.0
                            opacity = 0.6
                            curveness = 0.2
                        }
                    typedData = migrationLines(table, group)
                },
                effectScatterSeries {
                    this.name = name
                    coordinateSystem = "geo"
                    zlevel = 2.0
                    rippleEffect = unsafeJso<RippleEffectOption> { brushType = "stroke" }
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            position = LabelPosition("right")
                            formatter = LabelFormatter("{b}")
                        }
                    symbolSize =
                        SymbolSize { value, _ ->
                            DoubleOrDoubleArray(numberArray(value)[2] / 8)
                        }
                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor(group.color) }
                    typedData = migrationPoints(table, group)
                },
            )
        }
        .toTypedArray()

private fun migrationOption(table: JsAny): EChartsOption =
    unsafeJso {
        backgroundColor = ZRColor("#404a59")
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "模拟迁徙"
                    subtext = "数据纯属虚构"
                    left = StringOrDouble("center")
                    textStyle = unsafeJso<TextStyleOption> { color = "#fff" }
                },
            )
        tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
        legend =
            one(
                unsafeJso<LegendOption> {
                    orient = Orients.vertical
                    top = StringOrDouble("bottom")
                    left = StringOrDouble("right")
                    data = legendNames("北京 Top10", "上海 Top10", "广州 Top10")
                    textStyle = unsafeJso<LabelOption> { color = "#fff" }
                    selectedMode = SelectionModes.single
                },
            )
        geo = one(migrationGeo())
        series = migrationSeries(table)
    }

/** The migration page: 114 cities, 30 routes and the airplane glyph of `geo-map-scatter`. */
val geoLinesEntries: List<CatalogEntry> =
    listOf(
        geoLines.interactive(
            "geo-lines",
            "模拟迁徙",
            "Migration",
            "三组迁徙航线各画三遍：拖尾的 `lines`、带箭头与飞机符号的 `lines`，以及目的地上的涟漪散点。",
            "Each of the three migration groups is drawn three times: trailing `lines`, the same routes " +
                "with an arrow end and an airplane flying them, and a ripple scatter on the destinations.",
        ) { chart ->
            chart.showLoading(null)
            loadGeoLines().then { table ->
                if (!chart.isDisposed()) {
                    chart.hideLoading()
                    chart.setOption(migrationOption(table))
                }
                null
            }
        },
    )
