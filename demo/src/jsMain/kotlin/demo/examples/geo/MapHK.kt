package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.toolbox

private val mapHKCategory = Category(Groups.GEO, "examples/geo/MapHK.kt")

val mapHKEntry: CatalogEntry =
    mapHKCategory.interactive("map-HK", "香港18区人口密度（2011）", "Population Density of HongKong (2011)") { chart ->
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
        }
