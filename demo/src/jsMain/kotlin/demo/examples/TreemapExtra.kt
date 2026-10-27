package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * Treemap examples that read their tree from a dataset instead of an inline table.
 *
 * The data lives in `demo/src/jsMain/resources/data/` (see its `SOURCE.md`): `disk.tree.json` is a
 * 750 KB tree of a scanned disk, which is why it is shipped as a static file rather than as Kotlin
 * source. Both pages therefore mount the chart first and apply the option once the fetch settles.
 *
 * The tooltip of `treemap-disk` shows the folder path built from `treePathInfo`; that formatter
 * returns HTML, which the project's UI rules keep out of the sources, so the port shows the name and
 * the size instead.
 */

private val treemapExtra = Category(Groups.TREEMAP, "examples/TreemapExtra.kt")

/** The `levels` of `treemap-disk`. */
private fun diskLevels(): Array<TreemapSeriesLevelOption> =
    arrayOf(
        unsafeJso {
            itemStyle =
                unsafeJso<TreemapSeriesItemStyleOption> {
                    borderWidth = 0.0
                    gapWidth = 5.0
                }
        },
        unsafeJso {
            itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { gapWidth = 1.0 }
        },
        unsafeJso {
            colorSaturation = DoubleArrayOrNone(arrayOf(0.35, 0.5))
            itemStyle =
                unsafeJso<TreemapSeriesItemStyleOption> {
                    gapWidth = 1.0
                    borderColorSaturation = 0.6
                }
        },
    )

val treemapExtraEntries: List<CatalogEntry> =
    listOf(
        treemapExtra.interactive("treemap-disk", "磁盘占用", "Disk Usage") { chart ->
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
        },
    )
