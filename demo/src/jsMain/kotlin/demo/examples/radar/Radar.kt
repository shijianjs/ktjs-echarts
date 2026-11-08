package demo.examples.radar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.RadarOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.radarSeries
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.radarItem

private val radarCategory = Category(Groups.RADAR, "examples/radar/Radar.kt")

val radarEntry: CatalogEntry =
    radarCategory.chart("radar", "基础雷达图", "Basic Radar Chart") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Basic Radar Chart" })
                legend = one(unsafeJso<LegendOption> { data = legendNames("Allocated Budget", "Actual Spending") })
                radar =
                    one(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Sales", 6500.0),
                                    indicator("Administration", 16000.0),
                                    indicator("Information Technology", 30000.0),
                                    indicator("Customer Support", 38000.0),
                                    indicator("Development", 52000.0),
                                    indicator("Marketing", 25000.0),
                                )
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        radarSeries {
                            name = "Budget vs spending"
                            typedData =
                                dataItems(
                                    radarItem("Allocated Budget", 4200.0, 3000.0, 20000.0, 35000.0, 50000.0, 18000.0),
                                    radarItem("Actual Spending", 5000.0, 14000.0, 28000.0, 26000.0, 42000.0, 21000.0),
                                )
                        },
                    )
            }
        }
