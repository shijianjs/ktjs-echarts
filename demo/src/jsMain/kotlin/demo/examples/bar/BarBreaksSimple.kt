package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRFontWeights
import demo.examples.Category
import demo.examples.gauge.center

private val barBreaksSimpleCategory = Category(Groups.BAR, "examples/bar/BarBreaksSimple.kt")

val barBreaksSimpleEntry: CatalogEntry =
    barBreaksSimpleCategory.chart("bar-breaks-simple", "断轴上的柱状图", "Bar Chart with Axis Breaks") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Bar Chart with Axis Breaks"
                            subtext = "Click the break area to expand it"
                            left = StringOrDouble("center")
                            textStyle = unsafeJso<TextStyleOption> { fontSize = StringOrDouble(20.0) }
                            subtextStyle =
                                unsafeJso<TextStyleOption> {
                                    color = "#175ce5"
                                    fontSize = StringOrDouble(15.0)
                                    fontWeight = ZRFontWeights.bold
                                }
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                legend = one(unsafeJso<LegendOption> {})
                grid = one(unsafeJso<GridOption> { top = StringOrDouble(120.0) })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            breaks = arrayOf(axisBreak(5000.0, 100000.0), axisBreak(105000.0, 3100000.0))
                            breakArea =
                                unsafeJso<AxisBreakAreaOption> {
                                    itemStyle = unsafeJso<ItemStyleOption> { opacity = 1.0 }
                                    zigzagZ = 200.0
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Data A"
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(1500.0, 2032.0, 2001.0, 3154.0, 2190.0, 4330.0, 2410.0)
                        },
                        barSeries {
                            name = "Data B"
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(1200.0, 1320.0, 1010.0, 1340.0, 900.0, 2300.0, 2100.0)
                        },
                        barSeries {
                            name = "Data C"
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(103200.0, 100320.0, 103010.0, 102340.0, 103900.0, 103300.0, 103200.0)
                        },
                        barSeries {
                            name = "Data D"
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(3106212.0, 3102118.0, 3102643.0, 3104631.0, 3106679.0, 3100130.0, 3107022.0)
                        },
                    )
            }
        }
