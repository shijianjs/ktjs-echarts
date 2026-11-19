package demo.examples

import demo.CatalogEntry
import demo.ExampleBody
import demo.Group
import demo.json.jsonExamples
import demo.ui.ChartHolder
import io.github.shijianjs.ktjs.echarts.EChartsInstance
import io.github.shijianjs.ktjs.echarts.options.EChartsOption

/**
 * Factory of catalog entries for one family.
 *
 * The drawer group and the source path are bound once per file, so each example only declares its id,
 * its official titles (copied from the upstream manifest) and its chart. The official example id is
 * also the route id, which keeps a ported example one click away from the live original.
 *
 * Pages that also have a `kmp-echarts-json` builder in [jsonExamples] get the JSON card: it shows the
 * option the model serializes to, and a switch feeds the same chart with it — the two builders
 * describing one chart is the point of the KMP model.
 */
class Category(private val group: Group, private val sourcePath: String) {
    /** An example rendered from a static option. */
    fun chart(
        id: String,
        titleZh: String,
        titleEn: String,
        summaryZh: String = "",
        summaryEn: String = "",
        height: String? = null,
        option: () -> EChartsOption,
    ): CatalogEntry =
        entry(id, titleZh, titleEn, summaryZh, summaryEn) {
            ExampleBody.Option(option = option, height = height, json = jsonExamples[id])
        }

    /** An example that drives the chart itself: animation, timeline, user interaction, ... */
    fun interactive(
        id: String,
        titleZh: String,
        titleEn: String,
        summaryZh: String = "",
        summaryEn: String = "",
        height: String? = null,
        setup: (EChartsInstance) -> Unit,
    ): CatalogEntry =
        interactive(id, titleZh, titleEn, summaryZh, summaryEn, height) { chart, _ -> setup(chart) }

    /**
     * An example that needs a chart-scoped timer: the [setup] receives the [ChartHolder] so it can call
     * [ChartHolder.setInterval] / [ChartHolder.setTimeout], cleared automatically when the page leaves
     * the document.
     */
    fun interactive(
        id: String,
        titleZh: String,
        titleEn: String,
        summaryZh: String = "",
        summaryEn: String = "",
        height: String? = null,
        setup: (EChartsInstance, ChartHolder) -> Unit,
    ): CatalogEntry =
        entry(id, titleZh, titleEn, summaryZh, summaryEn) {
            ExampleBody.Interactive(setup = setup, height = height)
        }

    private fun entry(
        id: String,
        titleZh: String,
        titleEn: String,
        summaryZh: String,
        summaryEn: String,
        body: () -> ExampleBody,
    ): CatalogEntry =
        CatalogEntry(
            id = id,
            group = group,
            titleZh = titleZh,
            titleEn = titleEn,
            summaryZh =
                summaryZh.ifEmpty {
                    "官方示例 $id 的 Kotlin/JS 移植，完全用 ktjs-echarts 的类型化 API 构建。"
                },
            summaryEn =
                summaryEn.ifEmpty {
                    "Kotlin/JS port of the official $id example, built entirely with the typed " +
                        "ktjs-echarts API."
                },
            body = body(),
            sourcePath = sourcePath,
        )
}
