package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.SourceMode
import demo.SourceModeState
import demo.bi
import demo.json.ModelExample
import demo.json.kmpJsonExamples
import demo.tr
import demo.ui.PageMeta
import demo.ui.PageView
import demo.ui.REPO_COMMON_SOURCE_ROOT
import demo.ui.element
import demo.ui.example
import demo.ui.interactiveChart
import demo.ui.page
import demo.ui.pageSection
import kotlin.js.JsAny
import kotlin.js.js
import web.html.HtmlTagName

/**
 * Presentation of the pure-KMP `kmp-echarts-json` pages.
 *
 * The copy, the model builder and the serialized JSON all live in
 * `commonMain/kotlin/demo/json/ModelExamples.kt`; this file only turns each [ModelExample] into a
 * page: it parses [ModelExample.json] into the object `setOption` accepts, and the code panel shows
 * either the Kotlin builder or that JSON depending on the top-bar source switch.
 */
val kmpJsonEntries: List<CatalogEntry> = kmpJsonExamples.map(::modelEntry)

private fun modelEntry(example: ModelExample): CatalogEntry {
    val meta =
        PageMeta(
            id = example.id,
            title = bi(example.titleZh, example.titleEn),
            summary = bi(example.summaryZh, example.summaryEn),
            officialUrl = null,
            sourcePath = "json/ModelExamples.kt",
            sourceRoot = REPO_COMMON_SOURCE_ROOT,
        )
    return CatalogEntry(example.id, Groups.JSON, meta, switchableSource = true) { pageMeta ->
        buildModelPage(pageMeta, example)
    }
}

private fun buildModelPage(meta: PageMeta, example: ModelExample): PageView {
    val view = page(meta)

    val chartSection =
        pageSection(
            tr("图表", "Chart"),
            tr(
                "图表接收的是模型序列化出的 JSON：解析后交给 setOption，与从服务端取回一份配置等价。",
                "The chart receives the JSON serialized by the model: it is parsed and passed to setOption, " +
                    "which is the same as loading an option from a server.",
            ),
        )
    chartSection.body.append(
        example(null, view.track(interactiveChart { chart, _ -> chart.setOption(parseOption(example.json)) }).root),
    )
    view.body.append(chartSection.root)

    val codeSection =
        pageSection(
            tr("代码", "Code"),
            tr(
                "顶部按钮在 Kotlin 构造代码与 ECharts 消费的 JSON 之间切换。",
                "The top-bar switch toggles between the Kotlin builder and the JSON ECharts consumes.",
            ),
        )
    codeSection.body.append(
        element(HtmlTagName.pre, "code-block").apply {
            textContent = if (SourceModeState.current == SourceMode.Json) example.json else example.kotlinSource
        },
    )
    view.body.append(codeSection.root)

    return view
}

/** Parses the model output into the plain option object `setOption` accepts. */
private fun parseOption(json: String): JsAny = js("(text) => JSON.parse(text)")(json)
