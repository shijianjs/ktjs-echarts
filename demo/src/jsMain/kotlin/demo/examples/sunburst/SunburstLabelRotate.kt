package demo.examples.sunburst

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.LabelFormatterCallback
import echarts.options.LabelRotates
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SunburstSeriesLevelOption
import echarts.options.sunburstSeries
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category
import demo.examples.matrix.names

private val sunburstLabelRotateCategory = Category(Groups.SUNBURST, "examples/sunburst/SunburstLabelRotate.kt")

val sunburstLabelRotateEntry: CatalogEntry =
    sunburstLabelRotateCategory.chart("sunburst-label-rotate", "旭日图标签旋转", "Sunburst Label Rotate") {
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
        }
