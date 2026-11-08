package demo.examples

import demo.CatalogEntry
import demo.Group
import demo.SourceMode
import demo.SourceModeState
import demo.bi
import demo.json.JsonExample
import demo.json.jsonExamples
import demo.tr
import demo.ui.PageMeta
import demo.ui.PageView
import demo.ui.ChartHolder
import demo.ui.chart
import demo.ui.element
import demo.ui.example
import demo.ui.interactiveChart
import demo.ui.officialExampleUrl
import demo.ui.page
import demo.ui.pageSection
import io.github.shijianjs.ktjs.echarts.EChartsInstance
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import web.html.HtmlTagName

/**
 * Factory of catalog entries for one category file.
 *
 * The sidebar group and the source path are bound once per file, so each example only declares its
 * id, its official titles (copied from the upstream manifest) and its chart. The official example id
 * is also the route id, which keeps a ported example one click away from the live original.
 *
 * Pages that also have a `kmp-echarts-json` builder in [jsonExamples] get the top-bar Kotlin/JSON
 * switch: in JSON mode the chart is fed the JSON the model serializes to, the source link points to
 * the commonMain builder, and the JSON itself is shown below the chart.
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
        entry(id, titleZh, titleEn, summaryZh, summaryEn) { view, json ->
            if (json != null && SourceModeState.current == SourceMode.Json) {
                view.body.append(example(null, view.track(jsonChart(json, height)).root))
                view.body.append(jsonSection(json))
            } else {
                view.body.append(example(null, view.track(chart(option, height)).root))
            }
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
        entry(id, titleZh, titleEn, summaryZh, summaryEn) { view, _ ->
            view.body.append(example(null, view.track(interactiveChart(height) { chart, _ -> setup(chart) }).root))
        }

    /**
     * An example that needs a chart-scoped timer: the [setup] receives the [ChartHolder] so it can call
     * [ChartHolder.setInterval]/[ChartHolder.setTimeout], cleared automatically on navigation.
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
        entry(id, titleZh, titleEn, summaryZh, summaryEn) { view, _ ->
            view.body.append(example(null, view.track(interactiveChart(height, setup)).root))
        }

    private fun entry(
        id: String,
        titleZh: String,
        titleEn: String,
        summaryZh: String,
        summaryEn: String,
        build: (PageView, JsonExample?) -> Unit,
    ): CatalogEntry {
        val json = jsonExamples[id]
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
                jsonSourcePath = json?.sourcePath,
            )
        return CatalogEntry(id, group, meta, switchableSource = json != null) { pageMeta ->
            val view = page(pageMeta)
            build(view, json)
            view
        }
    }

    /** Chart fed the JSON serialized by the pure-KMP model. */
    private fun jsonChart(json: JsonExample, height: String?): ChartHolder =
        interactiveChart(height) { chart, _ -> chart.setOption(parseJsonOption(json.json)) }

    /** Pretty-printed model output, shown only in JSON mode. */
    private fun jsonSection(json: JsonExample): web.html.HTMLElement {
        val text = json.json
        val section =
            pageSection(
                tr("JSON 选项", "JSON option"),
                tr(
                    "kmp-echarts-json 模型序列化出的配置，图表接收的就是这份 JSON。",
                    "The option serialized by the kmp-echarts-json model; the chart consumes this JSON.",
                ),
            )
        section.body.append(element(HtmlTagName.pre, "code-block").apply { textContent = text })
        return section.root
    }
}
