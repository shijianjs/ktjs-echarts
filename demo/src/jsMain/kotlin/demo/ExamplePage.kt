package demo

import demo.ui.ChartElement
import demo.ui.chart
import demo.ui.interactiveChart
import demo.ui.jsonChart
import demo.ui.jsonInput
import demo.ui.optionInput
import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.nav.Page
import io.github.shijianjs.ktjs.demo.page.SectionBlock
import io.github.shijianjs.ktjs.demo.page.anchor
import io.github.shijianjs.ktjs.demo.page.codeText
import io.github.shijianjs.ktjs.demo.page.exampleBox
import io.github.shijianjs.ktjs.demo.page.newPage
import io.github.shijianjs.ktjs.demo.page.sectionBlock
import io.github.shijianjs.ktjs.vaadin.switch.SwitchElement
import io.github.shijianjs.ktjs.vaadin.switch.SwitchEvents
import io.github.shijianjs.ktjs.vaadin.switch.createSwitch
import web.events.Event
import web.events.addEventListener
import web.html.HTMLAnchorElement
import web.html.HTMLElement

/**
 * One page of the gallery: the frame every ported example shares.
 *
 * The examples differ in their chart, not in what a page has to say about it — so the frame (chart card,
 * the JSON the KMP model serializes to, the links to the official original and to the sources) is written
 * once here, and a family declaration only supplies an option. This is the kit's [Page] contract; the
 * page element is what the router mounts, and [applyTranslation] is the only place strings change.
 */
class ExamplePage(private val entry: CatalogEntry) : Page {
    private val page = newPage()

    override val node: HTMLElement get() = page

    private val chartSection: SectionBlock = sectionBlock()

    private var modelSection: SectionBlock? = null

    private var jsonSection: SectionBlock? = null

    private var chartNode: ChartElement? = null

    /** Decides what the chart consumes; only present when the entry has a KMP JSON builder. */
    private var jsonSwitch: SwitchElement? = null

    /** Link to the official example; absent for pages the official gallery does not have. */
    private val officialLink: HTMLAnchorElement? = entry.officialUrl?.let { pageLink(it) }

    private val sourceLink: HTMLAnchorElement =
        pageLink("${entry.sourceRoot}/blob/${entry.sourcePath}")

    /** One link of the page's link row, opened in a new tab. */
    private fun pageLink(url: String): HTMLAnchorElement =
        anchor("", url, external = true).also { page.addLink(it) }

    init {
        when (val body = entry.body) {
            is ExampleBody.Option -> {
                val node = chart(body.option, body.height)
                chartNode = node
                chartSection.body.append(exampleBox(null, node))
                body.json?.let { json -> jsonSection = buildJsonSection(json.json) }
            }

            is ExampleBody.Interactive -> {
                val node = interactiveChart(body.height, body.setup)
                chartNode = node
                chartSection.body.append(exampleBox(null, node))
            }

            is ExampleBody.Model -> {
                val node = jsonChart(body.json)
                chartNode = node
                chartSection.body.append(exampleBox(null, node))
                modelSection = sectionBlock().also {
                    it.body.append(exampleBox(null, codeText(body.kotlinSource)))
                }
                jsonSection = sectionBlock().also {
                    it.body.append(exampleBox(null, codeText(body.json)))
                }
            }

            is ExampleBody.Custom -> Unit
        }

        page.body.append(chartSection.root)
        modelSection?.let { page.body.append(it.root) }
        jsonSection?.let { page.body.append(it.root) }
    }

    /**
     * The JSON card: the switch that decides what the chart consumes, and the JSON itself.
     *
     * Switching does not rebuild the page — the chart element re-initialises with the other input, which
     * is the same code path a first mount takes.
     */
    private fun buildJsonSection(json: String): SectionBlock {
        val section = sectionBlock()
        val demoSwitch = createSwitch()
        val onToggle: (Event) -> Unit = {
            val node = chartNode
            val body = entry.body as? ExampleBody.Option
            val builder = body?.json
            if (node != null && body != null && builder != null) {
                node.reload(if (demoSwitch.checked) jsonInput(builder.json) else optionInput(body.option))
            }
        }
        demoSwitch.addEventListener(SwitchEvents.checkedChanged, onToggle)
        jsonSwitch = demoSwitch
        section.body.append(exampleBox(null, demoSwitch, codeText(json)))
        return section
    }

    override fun applyTranslation(lang: Lang) {
        val zh = lang == Langs.ZH_HANS
        page.setTitle(if (zh) entry.titleZh else entry.titleEn)
        page.setSummary(if (zh) entry.summaryZh else entry.summaryEn)

        chartSection.setText(if (zh) "图表" else "Chart")
        modelSection?.setText(if (zh) "Kotlin 构造" else "Kotlin builder")
        jsonSection?.setText(
            if (zh) "JSON 选项" else "JSON option",
            if (zh) {
                "kmp-echarts-json 模型序列化出的配置；打开开关即用它驱动上面的图表。"
            } else {
                "The option the kmp-echarts-json model serializes to; flip the switch to feed the chart " +
                    "with it."
            },
        )

        jsonSwitch?.label = if (zh) "用这份 JSON 驱动图表" else "Feed the chart with this JSON"

        officialLink?.textContent = if (zh) "官方示例" else "Official example"
        sourceLink.textContent = if (zh) "Kotlin 源码" else "Kotlin source"
    }
}

/** Page of an entry: the shared frame, or the body the entry builds itself. */
internal fun pageFor(entry: CatalogEntry): Page =
    (entry.body as? ExampleBody.Custom)?.create?.invoke() ?: ExamplePage(entry)
