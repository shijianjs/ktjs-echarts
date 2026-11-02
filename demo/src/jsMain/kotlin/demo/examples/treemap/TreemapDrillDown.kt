package demo.examples.treemap

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.ECharts
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.Category

private val treemapDrillDownCategory = Category(Groups.TREEMAP, "examples/treemap/TreemapDrillDown.kt")

val treemapDrillDownEntry: CatalogEntry =
    treemapDrillDownCategory.interactive("treemap-drill-down", "ECharts 配置项查询分布", "ECharts Option Query") { chart ->
            loadJson("data/ec-option-doc-statistics-201604.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "ECharts Options"
                                    subtext = "2016/04"
                                    // The original passes the name of the drill-down field here.
                                    left = StringOrDouble("leafDepth")
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        series =
                            arrayOf<SeriesOption>(
                                treemapSeries {
                                    name = "option"
                                    visibleMin = 300.0
                                    leafDepth = 2.0
                                    typedData = statisticNodes(json).unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                    levels =
                                        arrayOf(
                                            statisticLevel(4.0, 4.0, "#555"),
                                            statisticLevel(2.0, 2.0, saturation = arrayOf(0.3, 0.6), borderColorSaturation = 0.7),
                                            statisticLevel(1.0, saturation = arrayOf(0.3, 0.5), borderColorSaturation = 0.6),
                                            statisticLevel(0.0, saturation = arrayOf(0.3, 0.5)),
                                        )
                                },
                            )
                    },
                )
                null
            }
        }
