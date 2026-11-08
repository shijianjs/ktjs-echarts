package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val bar1Category = Category(Groups.BAR, "examples/bar/Bar1.kt")

val bar1Entry: CatalogEntry =
    bar1Category.chart("bar1", "某地区蒸发量和降水量", "Rainfall and Evaporation") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Rainfall vs Evaporation"
                            subtext = "Fake Data"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend = one(unsafeJso<LegendOption> { data = legendNames("Rainfall", "Evaporation") })
                toolbox = toolbox(dataView = true, magicType = true, restore = true, saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*RAINFALL_MONTHS)
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Rainfall"
                            typedData = numbers(*RAINFALL_VALUES)
                            markPoint =
                                unsafeJso<MarkPointOption> {
                                    data =
                                        arrayOf(
                                            unsafeJso<MarkPointDataItemOption> {
                                                name = "Max"
                                                type = MarkerStatisticTypes.max
                                            },
                                            unsafeJso<MarkPointDataItemOption> {
                                                name = "Min"
                                                type = MarkerStatisticTypes.min
                                            },
                                        )
                                }
                            markLine = averageMarkLine()
                        },
                        barSeries {
                            name = "Evaporation"
                            typedData = numbers(*EVAPORATION_VALUES)
                            markPoint =
                                unsafeJso<MarkPointOption> {
                                    data =
                                        arrayOf(
                                            unsafeJso<MarkPointDataItemOption> {
                                                name = "Max"
                                                value = StringOrDouble(182.2)
                                                xAxis = ScaleDataValue(7.0)
                                                yAxis = ScaleDataValue(183.0)
                                            },
                                            unsafeJso<MarkPointDataItemOption> {
                                                name = "Min"
                                                value = StringOrDouble(2.3)
                                                xAxis = ScaleDataValue(11.0)
                                                yAxis = ScaleDataValue(3.0)
                                            },
                                        )
                                }
                            markLine = averageMarkLine()
                        },
                    )
            }
        }
