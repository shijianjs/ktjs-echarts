package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.legendNames

private val polarRoundCapCategory = Category(Groups.BAR, "examples/bar/PolarRoundCap.kt")

val polarRoundCapEntry: CatalogEntry =
    polarRoundCapCategory.chart("polar-roundCap", "圆角极坐标柱状图", "Rounded Bar on Polar") {
            unsafeJso<EChartsOption> {
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            max = AxisMinMax(2.0)
                            startAngle = 30.0
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            type = AxisTypes.category
                            data = strings("v", "w", "x", "y", "z")
                            z = 10.0
                        },
                    )
                polar = one(unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Without Round Cap"
                            coordinateSystem = "polar"
                            typedData = numbers(4.0, 3.0, 2.0, 1.0, 0.0)
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderColor = ZRColor("red")
                                    opacity = 0.8
                                    borderWidth = 1.0
                                }
                        },
                        barSeries {
                            name = "With Round Cap"
                            coordinateSystem = "polar"
                            roundCap = true
                            typedData = numbers(4.0, 3.0, 2.0, 1.0, 0.0)
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderColor = ZRColor("green")
                                    opacity = 0.8
                                    borderWidth = 1.0
                                }
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            show = true
                            data = legendNames("Without Round Cap", "With Round Cap")
                        },
                    )
            }
        }
