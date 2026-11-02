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
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center

private val treemapDiskCategory = Category(Groups.TREEMAP, "examples/treemap/TreemapDisk.kt")

val treemapDiskEntry: CatalogEntry =
    treemapDiskCategory.interactive("treemap-disk", "磁盘占用", "Disk Usage") { chart ->
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
                                    itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { borderColor = ZRColor("#fff") }
                                    levels = diskLevels()
                                    typedData = json.unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                },
                            )
                    },
                )
                null
            }
        }
