package demo.examples.treemap

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center

private val treemapVisualCategory = Category(Groups.TREEMAP, "examples/treemap/TreemapVisual.kt")

val treemapVisualEntry: CatalogEntry =
    treemapVisualCategory.interactive("treemap-visual", "映射为渐变色", "Gradient Mapping") { chart ->
            loadJson("data/obama_budget_proposal_2012.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    left = StringOrDouble("center")
                                    text = "Gradient Mapping"
                                    subtext = "Growth > 0: green; Growth < 0: red; Growth = 0: grey"
                                },
                            )
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    formatter = TooltipFormatter(::budgetTooltip)
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                treemapSeries {
                                    name = "ALL"
                                    top = StringOrDouble(80.0)
                                    typedData =
                                        budgetNodes(json.unsafeCast<Array<VisualBudgetNode>>())
                                            .unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            formatter = LabelFormatter("{b}")
                                        }
                                    itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { borderColor = ZRColor("black") }
                                    visualMin = VISUAL_MIN
                                    visualMax = VISUAL_MAX
                                    visualDimension = StringOrDouble(3.0)
                                    levels = budgetVisualLevels()
                                },
                            )
                    },
                )
                null
            }
        }
