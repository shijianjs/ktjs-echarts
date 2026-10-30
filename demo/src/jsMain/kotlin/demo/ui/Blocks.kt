package demo.ui

import demo.Bilingual
import demo.SourceMode
import demo.SourceModeState
import demo.tr
import web.dom.Node
import web.html.HTMLElement
import web.html.HtmlTagName

/** GitHub blob base of the Kotlin sources of the showcase, used by the "source" link. */
const val REPO_SOURCE_ROOT = "https://github.com/shijianjs/ktjs-echarts/blob/main/demo/src/jsMain/kotlin/demo"

/** GitHub blob base of the platform-independent demo sources of the showcase. */
const val REPO_COMMON_SOURCE_ROOT =
    "https://github.com/shijianjs/ktjs-echarts/blob/main/demo/src/commonMain/kotlin/demo"

/** Index of the official ECharts example gallery, used when a page has no dedicated example. */
const val OFFICIAL_INDEX = "https://echarts.apache.org/examples/zh/index.html"

/** Live example on the official site, addressed by the same id the demo uses. */
fun officialExampleUrl(id: String): String = "https://echarts.apache.org/examples/zh/editor.html?c=$id"

/** Links and identity of one example page. */
class PageMeta(
    val id: String,
    title: Bilingual,
    summary: Bilingual,
    /**
     * Live example on the official ECharts site.
     *
     * The official gallery is addressed by the example id, which is the same id used by the route of
     * this page.
     */
    val officialUrl: String? = null,
    /** Path of the Kotlin source of the page, relative to [sourceRoot]. */
    val sourcePath: String,
    /** GitHub blob base of [sourcePath]; defaults to the JS sources. */
    val sourceRoot: String = REPO_SOURCE_ROOT,
    /**
     * Path of the `kmp-echarts-json` builder of the page, relative to [REPO_COMMON_SOURCE_ROOT].
     *
     * Non-null when the page has a JSON counterpart: the source link then follows the top-bar code
     * source switch and points at the model builder instead of the JS wrapper example.
     */
    val jsonSourcePath: String? = null,
) {
    private val titleText = title
    private val summaryText = summary

    val title: String
        get() = titleText.resolve()

    val summary: String
        get() = summaryText.resolve()
}

/**
 * The built page: [root] is mounted by the shell, [body] receives the sections, and the [ChartHolder]s
 * registered with [track] are (re)initialised when the page enters and leaves the view.
 */
class PageView(val root: HTMLElement, val body: HTMLElement, val links: HTMLElement) {
    private val charts = mutableListOf<ChartHolder>()

    /** Registers [holder] so the shell can mount, unmount and resize it with the page. */
    fun track(holder: ChartHolder): ChartHolder {
        charts += holder
        return holder
    }

    /** Called by the shell after the page became visible. */
    fun attach() {
        charts.forEach { it.mount() }
    }

    /** Called by the shell right before the page is replaced. */
    fun detach() {
        charts.forEach { it.unmount() }
    }

    /** Called by the shell on window resize. */
    fun resize() {
        charts.forEach { it.resize() }
    }
}

/**
 * Builds the common frame of an example page: title, summary, the link to the live official example
 * and the link to the Kotlin source of the page itself.
 */
fun page(meta: PageMeta): PageView {
    val body = div("page__body")
    val links = div("page__links")

    val jsonMode = meta.jsonSourcePath != null && SourceModeState.current == SourceMode.Json
    val sourceLabel = if (jsonMode) tr("查看 JSON 源码", "JSON source") else tr("查看 Kotlin 源码", "Kotlin source")
    val sourcePath = if (jsonMode) meta.jsonSourcePath!! else meta.sourcePath
    val sourceRoot = if (jsonMode) REPO_COMMON_SOURCE_ROOT else meta.sourceRoot

    links.append(
        anchor(tr("官方示例", "Official example"), meta.officialUrl ?: OFFICIAL_INDEX, "page__link", external = true),
    )
    links.append(
        anchor(
            sourceLabel,
            "$sourceRoot/$sourcePath",
            "page__link",
            external = true,
        ),
    )

    val root = element(HtmlTagName.section, "page")
    // Marks which route this page belongs to: the shell replaces pages asynchronously, so a consumer
    // that waits for content needs to tell the incoming page from the one still mounted.
    root.setAttribute("data-route", meta.id)
    root.append(heading(1, meta.title, "page__title"), paragraph(meta.summary, "page__summary"), links, body)
    return PageView(root, body, links)
}

/** One titled block of a page; [body] receives the examples. */
class Section(val root: HTMLElement, val body: HTMLElement)

/** Creates a section with [title] and an optional [description]. */
fun pageSection(title: String, description: String = ""): Section {
    val body = div("section__body")
    val root = div("section")
    root.append(heading(2, title, "section__title"))
    if (description.isNotEmpty()) root.append(paragraph(description, "section__desc"))
    root.append(body)
    return Section(root, body)
}

/** A bordered area holding one chart, optionally captioned. */
fun example(caption: String? = null, vararg children: Node): HTMLElement {
    val box = div("example")
    if (caption != null) box.append(paragraph(caption, "example__caption"))
    box.append(*children)
    return box
}

/** Horizontal group of elements. */
fun row(vararg children: Node): HTMLElement = div("row", *children)

/** Vertical group. */
fun column(vararg children: Node): HTMLElement = div("col", *children)

/** Responsive grid of [children]. */
fun grid(vararg children: Node): HTMLElement = div("grid", *children)

/** Text that is updated in place, showing the current state of a widget. */
class Readout(initial: String) {
    val root: HTMLElement = paragraph(initial, "readout")

    fun set(text: String) {
        root.textContent = text
    }

    /** Renders `prefix value suffix`, with [value] emphasised. */
    fun set(prefix: String, value: String, suffix: String = "") {
        root.replaceChildren(span(prefix), strong(value), span(suffix))
    }
}
