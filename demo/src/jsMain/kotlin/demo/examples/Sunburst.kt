package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.EmphasisFocuses
import echarts.VisualMapTypes
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.LabelFormatterCallback
import echarts.options.LabelPosition
import echarts.options.LabelRotates
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.StringOrStringArray
import echarts.options.SunburstLabelOption
import echarts.options.SunburstSeriesLevelOption
import echarts.options.SunburstSort
import echarts.options.TextStyleOption
import echarts.options.TitleOption
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.one
import echarts.options.sunburstSeries
import js.objects.unsafeJso
import kotlin.js.js
import web.cssom.TextAlign
import zrender.ZRColor

/*
 * Sunburst examples ported from `public/examples/ts/sunburst-*.ts` of apache/echarts-examples.
 *
 * The datasets are inlined in `SunburstData.kt`; every official sunburst example of the manifest is
 * ported here.
 *
 * Dropped on purpose: the top-level `silent: true` of `sunburst-label-rotate` is a parameter of
 * `setOption`, not an option field, so ECharts ignores it inside the option; the `downplay` state
 * of the last level of `sunburst-book` no longer exists in ECharts 6 (it was replaced by `blur`).
 */

private val sunburst = Category(Groups.SUNBURST, "examples/Sunburst.kt")

/** `label: { rotate: 'radial' }` of the examples that only rotate the labels. */
private fun radialLabel(): SunburstLabelOption = unsafeJso { typedRotate = LabelRotates.radial }

/** `emphasis: { focus: 'ancestor' }`, the emphasis state of the data-driven variants. */
private fun ancestorEmphasis(): EmphasisOption = unsafeJso { focus = EmphasisFocuses.ancestor }

/** Radius of a sunburst as an `[inner, outer]` pair. */
private fun radius(inner: StringOrDouble, outer: StringOrDouble): StringOrDoubleOrArray =
    StringOrDoubleOrArray(arrayOf(inner, outer))

val sunburstEntries: List<CatalogEntry> =
    listOf(
        sunburst.chart("sunburst-simple", "基础旭日图", "Basic Sunburst") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = familyData()
                            radius = radius(StringOrDouble(0.0), StringOrDouble("90%"))
                            typedLabel = radialLabel()
                        },
                    )
            }
        },
        sunburst.chart("sunburst-borderRadius", "圆角旭日图", "Sunburst with Rounded Corner") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = familyData()
                            radius = radius(StringOrDouble(60.0), StringOrDouble("90%"))
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderRadius = StringOrDoubleOrArray(7.0)
                                    borderWidth = 2.0
                                }
                            typedLabel = unsafeJso { show = false }
                        },
                    )
            }
        },
        sunburst.chart("sunburst-label-rotate", "旭日图标签旋转", "Sunburst Label Rotate") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = labelRotateData()
                            radius = radius(StringOrDouble("15%"), StringOrDouble("80%"))
                            // `sort: undefined` upstream: keep the order of the data.
                            emphasis = ancestorEmphasis()
                            typedLabel =
                                unsafeJso {
                                    color = "#000"
                                    textBorderColor = "#fff"
                                    textBorderWidth = 2.0
                                    // The original formatter names the rotation of the level the
                                    // sector sits on; the depth is the length of `treePathInfo`.
                                    formatter =
                                        LabelFormatter(
                                            js(
                                                "(param) => { const depth = param.treePathInfo.length;" +
                                                    " return depth === 2 ? 'radial' : depth === 3 ? 'tangential'" +
                                                    " : depth === 4 ? '0' : ''; }",
                                            ) as LabelFormatterCallback,
                                        )
                                }
                            levels =
                                arrayOf(
                                    unsafeJso(),
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#CD4949") }
                                        label = unsafeJso { typedRotate = LabelRotates.radial }
                                    },
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#F47251") }
                                        label = unsafeJso { typedRotate = LabelRotates.tangential }
                                    },
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#FFC75F") }
                                        label = unsafeJso { rotate = 0.0 }
                                    },
                                )
                        },
                    )
            }
        },
        sunburst.chart("sunburst-monochrome", "单色旭日图", "Monochrome Sunburst") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = monochromeData()
                            radius = radius(StringOrDouble("15%"), StringOrDouble("80%"))
                            emphasis = ancestorEmphasis()
                            typedLabel = radialLabel()
                            // `levels: []` upstream: the levels are derived from the data only.
                            levels = emptyArray()
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddd"); borderWidth = 2.0 }
                        },
                    )
            }
        },
        sunburst.chart("sunburst-visualMap", "旭日图使用视觉编码", "Sunburst VisualMap") {
            unsafeJso<EChartsOption> {
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.continuous
                            min = 0.0
                            max = 10.0
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color =
                                        StringOrStringArray(
                                            arrayOf("#2F93C8", "#AEC48F", "#FFDB5C", "#F98862"),
                                        )
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = visualMapData()
                            radius = radius(StringOrDouble(0.0), StringOrDouble("90%"))
                            typedLabel = radialLabel()
                        },
                    )
            }
        },
        sunburst.chart("sunburst-drink", "饮品风味分类", "Drink Flavors") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "WORLD COFFEE RESEARCH SENSORY LEXICON"
                            subtext = "Source: https://worldcoffeeresearch.org/work/sensory-lexicon/"
                            sublink = "https://worldcoffeeresearch.org/work/sensory-lexicon/"
                            textStyle =
                                unsafeJso<TextStyleOption> {
                                    fontSize = StringOrDouble(14.0)
                                    align = TextAlign.center
                                }
                            subtextStyle = unsafeJso<TextStyleOption> { align = TextAlign.center }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = drinkData()
                            radius = radius(StringOrDouble(0.0), StringOrDouble("95%"))
                            emphasis = ancestorEmphasis()
                            levels =
                                arrayOf(
                                    unsafeJso(),
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble("15%")
                                        r = StringOrDouble("35%")
                                        itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 2.0 }
                                        label = unsafeJso { typedRotate = LabelRotates.tangential }
                                    },
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble("35%")
                                        r = StringOrDouble("70%")
                                        label = unsafeJso { align = TextAlign.right }
                                    },
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble("70%")
                                        r = StringOrDouble("72%")
                                        label =
                                            unsafeJso {
                                                position = LabelPosition("outside")
                                                padding = DoubleOrDoubleArray(3.0)
                                                silent = false
                                            }
                                        itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 3.0 }
                                    },
                                )
                        },
                    )
            }
        },
        sunburst.chart("sunburst-book", "书籍分布", "Book Records") {
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
        },
    )
