package demo.examples.radar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.AreaStyleOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.RadarOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.radarSeries
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames
import demo.examples.shared.radarItem
import demo.examples.sunburst.radius

private val radarMultipleCategory = Category(Groups.RADAR, "examples/radar/RadarMultiple.kt")

val radarMultipleEntry: CatalogEntry =
    radarMultipleCategory.chart("radar-multiple", "多雷达图", "Multiple Radar") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Multiple Radar" })
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            left = StringOrDouble("center")
                            data =
                                legendNames(
                                    "A Software",
                                    "A Phone",
                                    "Another Phone",
                                    "Precipitation",
                                    "Evaporation",
                                )
                        },
                    )
                radar =
                    arrayOf(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Brand", 100.0),
                                    indicator("Content", 100.0),
                                    indicator("Usability", 100.0),
                                    indicator("Function", 100.0),
                                )
                            center = arrayOf(StringOrDouble("25%"), StringOrDouble("40%"))
                            radius = StringOrDouble(80.0)
                        },
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Look", 100.0),
                                    indicator("Photo", 100.0),
                                    indicator("System", 100.0),
                                    indicator("Performance", 100.0),
                                    indicator("Screen", 100.0),
                                )
                            radius = StringOrDouble(80.0)
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("60%"))
                        },
                        unsafeJso<RadarOption> {
                            indicator = Array(12) { index -> indicator("${index + 1}月", 100.0) }
                            center = arrayOf(StringOrDouble("75%"), StringOrDouble("40%"))
                            radius = StringOrDouble(80.0)
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        radarSeries {
                            tooltip = unsafeJso<TooltipOption> { trigger = TooltipTriggers.item }
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData = dataItems(radarItem("A Software", 60.0, 73.0, 85.0, 40.0))
                        },
                        radarSeries {
                            radarIndex = 1.0
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData =
                                dataItems(
                                    radarItem("A Phone", 85.0, 90.0, 90.0, 95.0, 95.0),
                                    radarItem("Another Phone", 95.0, 80.0, 95.0, 90.0, 93.0),
                                )
                        },
                        radarSeries {
                            radarIndex = 2.0
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData =
                                dataItems(
                                    radarItem(
                                        "Precipitation",
                                        2.6,
                                        5.9,
                                        9.0,
                                        26.4,
                                        28.7,
                                        70.7,
                                        75.6,
                                        82.2,
                                        48.7,
                                        18.8,
                                        6.0,
                                        2.3,
                                    ),
                                    radarItem("Evaporation", 2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 35.6, 62.2, 32.6, 20.0, 6.4, 3.3),
                                )
                        },
                    )
            }
        }
