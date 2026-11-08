package demo.examples.treemap

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center

private val treemapShowParentCategory = Category(Groups.TREEMAP, "examples/treemap/TreemapShowParent.kt")

val treemapShowParentEntry: CatalogEntry =
    treemapShowParentCategory.interactive("treemap-show-parent", "显示父层级标签", "Show Parent Labels") { chart ->
            loadJson("data/disk.tree.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Disk Usage"
                                    left = StringOrDouble("center")
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> { formatter = TooltipFormatter("{b}: {c} KB") })
                        series =
                            arrayOf<SeriesOption>(
                                treemapSeries {
                                    name = "Disk Usage"
                                    visibleMin = 300.0
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            formatter = LabelFormatter("{b}")
                                        }
                                    upperLabel =
                                        unsafeJso<TreemapSeriesLabelOption> {
                                            show = true
                                            height = 30.0
                                        }
                                    itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { borderColor = ZRColor("#fff") }
                                    levels = parentLevels()
                                    typedData = json.unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                },
                            )
                    },
                )
                null
            }
        }
