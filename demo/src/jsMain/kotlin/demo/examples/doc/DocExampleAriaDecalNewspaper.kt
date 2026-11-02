package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRLineTypes
import demo.examples.Category
import demo.examples.shared.axisLabel

private val docExampleAriaDecalNewspaperCategory = Category(Groups.DOC, "examples/doc/DocExampleAriaDecalNewspaper.kt")

val docExampleAriaDecalNewspaperEntry: CatalogEntry =
    docExampleAriaDecalNewspaperCategory.chart("doc-example/aria-decal-newspaper", "无障碍花纹（报纸风格）", "Aria decal (newspaper)") {
            val blackBorder = unsafeJso<ItemStyleOption> {
                borderColor = ZRColor("black")
                borderWidth = 2.0
            }
            unsafeJso<EChartsOption> {
                color =
                    arrayOf(
                        ZRColor("white"),
                        ZRColor("black"),
                        ZRColor("white"),
                        ZRColor("white"),
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings("Q1", "Q2", "Q3", "Q4")
                            axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisLabel = unsafeJso<AxisLabelOption> { color = "black" }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisLabel = unsafeJso<AxisLabelOption> { color = "black" }
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    lineStyle =
                                        unsafeJso<SplitLineStyleOption> {
                                            color = ZRColor("black")
                                            type = ZRLineTypes.dashed
                                        }
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Apples"
                            typedData = numbers(108.0, 26.0, 39.0, 24.0)
                            itemStyle = blackBorder
                        },
                        barSeries {
                            name = "Oranges"
                            typedData = numbers(23.0, 40.0, 60.0, 70.0)
                            itemStyle = blackBorder
                        },
                        barSeries {
                            name = "Bananas"
                            typedData = numbers(129.0, 40.0, 40.0, 50.0)
                            itemStyle = blackBorder
                        },
                        barSeries {
                            name = "Pears"
                            typedData = numbers(40.0, 60.0, 50.0, 89.0)
                            itemStyle = blackBorder
                        },
                    )
                aria =
                    unsafeJso<AriaOption> {
                        decal =
                            unsafeJso<AriaDecalOption> {
                                show = true
                                decals =
                                    DecalList(
                                        arrayOf(
                                            unsafeJso<DecalObject> {
                                                dashArrayX = DecalDashArrayX(0.0)
                                                dashArrayY = DoubleOrDoubleArray(0.0)
                                                color = "black"
                                            },
                                            unsafeJso<DecalObject> {
                                                dashArrayX = DecalDashArrayX(0.0)
                                                dashArrayY = DoubleOrDoubleArray(0.0)
                                                color = "black"
                                            },
                                            unsafeJso<DecalObject> {
                                                dashArrayX =
                                                    DecalDashArrayX(
                                                        arrayOf(DoubleOrDoubleArray(1.0), DoubleOrDoubleArray(0.0)),
                                                    )
                                                dashArrayY = DoubleOrDoubleArray(arrayOf(2.0, 5.0))
                                                symbolSize = 1.0
                                                rotation = kotlin.math.PI / 6
                                                color = "black"
                                            },
                                            unsafeJso<DecalObject> {
                                                symbol = StringOrStringArray("circle")
                                                dashArrayX =
                                                    DecalDashArrayX(
                                                        arrayOf(
                                                            DoubleOrDoubleArray(arrayOf(8.0, 8.0)),
                                                            DoubleOrDoubleArray(arrayOf(0.0, 8.0, 8.0, 0.0)),
                                                        ),
                                                    )
                                                dashArrayY = DoubleOrDoubleArray(arrayOf(6.0, 0.0))
                                                symbolSize = 0.8
                                                color = "black"
                                            },
                                        ),
                                    )
                            }
                    }
            }
        }
