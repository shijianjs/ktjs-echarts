package demo.examples

import demo.CatalogEntry
import demo.Group
import demo.bi
import demo.ui.DEFAULT_CHART_HEIGHT
import demo.ui.PageMeta
import demo.ui.PageView
import demo.ui.chart
import demo.ui.example
import demo.ui.interactiveChart
import demo.ui.officialExampleUrl
import demo.ui.page
import echarts.EChartsInstance
import echarts.options.EChartsOption

/**
 * Factory of catalog entries for one category file.
 *
 * The sidebar group and the source path are bound once per file, so each example only declares its
 * id, its official titles (copied from the upstream manifest) and its chart. The official example id
 * is also the route id, which keeps a ported example one click away from the live original.
 */
class Category(private val group: Group, private val sourcePath: String) {
    /** An example rendered from a static option. */
    fun chart(
        id: String,
        titleZh: String,
        titleEn: String,
        summaryZh: String = "",
        summaryEn: String = "",
        height: String = DEFAULT_CHART_HEIGHT,
        option: () -> EChartsOption,
    ): CatalogEntry =
        entry(id, titleZh, titleEn, summaryZh, summaryEn) { view ->
            view.body.append(example(null, view.track(chart(option, height)).root))
        }

    /** An example that drives the chart itself: animation, timeline, user interaction, ... */
    fun interactive(
        id: String,
        titleZh: String,
        titleEn: String,
        summaryZh: String = "",
        summaryEn: String = "",
        height: String = DEFAULT_CHART_HEIGHT,
        setup: (EChartsInstance) -> Unit,
    ): CatalogEntry =
        entry(id, titleZh, titleEn, summaryZh, summaryEn) { view ->
            view.body.append(example(null, view.track(interactiveChart(height, setup)).root))
        }

    private fun entry(
        id: String,
        titleZh: String,
        titleEn: String,
        summaryZh: String,
        summaryEn: String,
        build: (PageView) -> Unit,
    ): CatalogEntry {
        val meta =
            PageMeta(
                id = id,
                title = bi(titleZh, titleEn),
                summary =
                    if (summaryZh.isNotEmpty() || summaryEn.isNotEmpty()) {
                        bi(summaryZh, summaryEn)
                    } else {
                        bi(
                            "官方示例 $id 的 Kotlin/JS 移植，完全用 ktjs-echarts 的类型化 API 构建。",
                            "Kotlin/JS port of the official $id example, built entirely with the typed " +
                                "ktjs-echarts API.",
                        )
                    },
                officialUrl = officialExampleUrl(id),
                sourcePath = sourcePath,
            )
        return CatalogEntry(id, group, meta) { pageMeta ->
            val view = page(pageMeta)
            build(view)
            view
        }
    }
}
