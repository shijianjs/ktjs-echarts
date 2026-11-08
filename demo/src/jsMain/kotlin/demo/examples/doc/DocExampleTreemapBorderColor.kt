package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.treemap.diskLevels

private val docExampleTreemapBorderColorCategory = Category(Groups.DOC, "examples/doc/DocExampleTreemapBorderColor.kt")

val docExampleTreemapBorderColorEntry: CatalogEntry =
    docExampleTreemapBorderColorCategory.interactive(
            "doc-example/treemap-borderColor",
            "矩形树图的边框颜色",
            "Treemap border colour",
        ) { chart ->
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
                        // The original formats the folder path as HTML; the UI rules of this
                        // repository keep markup out of the sources, so the tooltip is plain text.
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
                                    itemStyle =
                                        unsafeJso<TreemapSeriesItemStyleOption> {
                                            borderColor = ZRColor("#fff")
                                        }
                                    levels = diskLevels()
                                    typedData = json.unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                },
                            )
                    },
                )
                null
            }
        }
