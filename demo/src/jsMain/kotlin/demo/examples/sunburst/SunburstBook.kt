package demo.examples.sunburst

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LabelPosition
import echarts.options.LabelRotates
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SunburstSeriesLevelOption
import echarts.options.SunburstSort
import echarts.options.sunburstSeries
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center

private val sunburstBookCategory = Category(Groups.SUNBURST, "examples/sunburst/SunburstBook.kt")

val sunburstBookEntry: CatalogEntry =
    sunburstBookCategory.chart("sunburst-book", "书籍分布", "Book Records") {
            val background = "#2E2733"
            val colors = arrayOf("#FFAE57", "#FF7853", "#EA5151", "#CC3F57", "#9A2555")
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor(background)
                color = Array(colors.size) { index -> ZRColor(colors[index]) }
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = bookData()
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("48%"))
                            sort =
                                SunburstSort { a, b ->
                                    if (a.depth == 1.0) b.getValue() - a.getValue() else a.dataIndex - b.dataIndex
                                }
                            typedLabel = unsafeJso { typedRotate = LabelRotates.radial; color = background }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderColor = ZRColor(background)
                                    borderWidth = 2.0
                                }
                            levels =
                                arrayOf(
                                    unsafeJso(),
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble(0.0)
                                        r = StringOrDouble(40.0)
                                        label = unsafeJso { rotate = 0.0 }
                                    },
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble(40.0)
                                        r = StringOrDouble(105.0)
                                    },
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble(115.0)
                                        r = StringOrDouble(140.0)
                                        itemStyle =
                                            unsafeJso<ItemStyleOption> {
                                                shadowBlur = 2.0
                                                shadowColor = ZRColor(colors[2])
                                                color = ZRColor("transparent")
                                            }
                                        label =
                                            unsafeJso {
                                                typedRotate = LabelRotates.tangential
                                                fontSize = StringOrDouble(10.0)
                                                color = colors[0]
                                            }
                                    },
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble(140.0)
                                        r = StringOrDouble(145.0)
                                        itemStyle =
                                            unsafeJso<ItemStyleOption> {
                                                shadowBlur = 80.0
                                                shadowColor = ZRColor(colors[0])
                                            }
                                        label =
                                            unsafeJso {
                                                position = LabelPosition("outside")
                                                textShadowBlur = 5.0
                                                textShadowColor = "#333"
                                            }
                                    },
                                )
                        },
                    )
            }
        }
