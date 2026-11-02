package demo.examples.radar

import demo.CatalogEntry
import demo.Groups
import echarts.LegendTypes
import echarts.TooltipTriggers
import echarts.options.AreaStyleOption
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.RadarOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolShape
import echarts.options.StringOrStringArray
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.dataItems
import echarts.options.one
import echarts.options.radarSeries
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.radarItem

private val radar2Category = Category(Groups.RADAR, "examples/radar/Radar2.kt")

val radar2Entry: CatalogEntry =
    radar2Category.chart("radar2", "浏览器占比变化", "Proportion of Browsers") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Proportion of Browsers"
                            subtext = "Fake Data"
                            top = StringOrDouble(10.0)
                            left = StringOrDouble(10.0)
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            type = LegendTypes.scroll
                            bottom = StringOrDouble(10.0)
                            data = legendNames(*Array(28) { index -> "${index + 2001}" })
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            top = StringOrDouble("middle")
                            right = StringOrDouble(10.0)
                            inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(arrayOf("red", "yellow")) }
                            calculable = true
                        },
                    )
                radar =
                    one(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("IE8-", 400.0),
                                    indicator("IE9+", 400.0),
                                    indicator("Safari", 400.0),
                                    indicator("Firefox", 400.0),
                                    indicator("Chrome", 400.0),
                                )
                        },
                    )
                series =
                    Array<SeriesOption>(28) { index ->
                        val year = index + 1
                        radarSeries {
                            symbol = SymbolShape("none")
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0 }
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    areaStyle = unsafeJso<AreaStyleOption> { color = ZRColor("rgba(0,250,0,0.3)") }
                                }
                            typedData =
                                dataItems(
                                    radarItem(
                                        "${year + 2000}",
                                        (40 - year) * 10.0,
                                        (38 - year) * 4.0 + 60.0,
                                        year * 5.0 + 10.0,
                                        year * 9.0,
                                        year * year / 2.0,
                                    ),
                                )
                        }
                    }
            }
        }
