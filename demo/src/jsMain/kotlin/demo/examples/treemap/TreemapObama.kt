package demo.examples.treemap

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.gauge.center

private val treemapObamaCategory = Category(Groups.TREEMAP, "examples/treemap/TreemapObama.kt")

val treemapObamaEntry: CatalogEntry =
    treemapObamaCategory.interactive("treemap-obama", "3.7 万亿美元支出构成", "How $3.7 Trillion is Spent") { chart ->
            loadJson("data/obama_budget_proposal_2012.json").then { json ->
                val tree = json.unsafeCast<Array<BudgetNode>>()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "How $3.7 Trillion is Spent"
                                    left = StringOrDouble("center")
                                },
                            )
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    formatter = TooltipFormatter("{b}: {c}")
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                treemapSeries {
                                    name = "Where \$3.7 Trillion Goes"
                                    top = StringOrDouble(40.0)
                                    bottom = StringOrDouble(10.0)
                                    left = StringOrDouble("center")
                                    width = StringOrDouble("90%")
                                    // The colour of a node follows its id, not its position.
                                    levels = budgetLevels()
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            formatter = LabelFormatter("{b}")
                                        }
                                    typedData = Array(tree.size) { index -> budgetNode(tree[index]) }
                                },
                            )
                    },
                )
                null
            }
        }
