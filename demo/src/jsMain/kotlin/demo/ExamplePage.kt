package demo

import demo.ui.ChartElement
import demo.ui.chart
import demo.ui.interactiveChart
import demo.ui.jsonChart
import demo.ui.jsonInput
import demo.ui.optionInput
import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.page.DemoPage
import io.github.shijianjs.ktjs.demo.page.SectionBlock
import io.github.shijianjs.ktjs.demo.page.Blocks.anchor
import io.github.shijianjs.ktjs.demo.page.Blocks.codeText
import io.github.shijianjs.ktjs.demo.page.Blocks.exampleBox
import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.dom.webcomponent.CustomElementReferenceAdapter
import io.github.shijianjs.ktjs.vaadin.switch.SwitchElement
import io.github.shijianjs.ktjs.vaadin.switch.SwitchEvents
import io.github.shijianjs.ktjs.vaadin.switch.createSwitch
import web.events.Event
import web.events.addEventListener
import web.html.HTMLAnchorElement

/**
 * One page of the gallery: the frame every ported example shares.
 *
 * The examples differ in their chart, not in what a page has to say about it — so the frame (chart card,
 * the JSON the KMP model serializes to, the links to the official original and to the sources) is written
 * once here, and a family declaration only supplies an option.
 *
 * The page is a custom element like every other page of the site, and the catalog entry it shows is what
 * the router hands it: [show] is the element's one property, the same way a component takes its props.
 * [applyTranslation] is the only place strings change.
 */
internal class ExamplePage : DemoPage() {
    private val chartSection: SectionBlock = SectionBlock.NAME { show() }

    private var modelSection: SectionBlock? = null

    private var jsonSection: SectionBlock? = null

    private var chartNode: ChartElement? = null

    /** Decides what the chart consumes; only present when the entry has a KMP JSON builder. */
    private var jsonSwitch: SwitchElement? = null

    /** Link to the official example; absent for pages the official gallery does not have. */
    private var officialLink: HTMLAnchorElement? = null

    private var sourceLink: HTMLAnchorElement? = null

    private var entry: CatalogEntry? = null

    /** Builds the frame around the entry's chart. Called once, right after the element is created. */
    fun show(entry: CatalogEntry) {
        this.entry = entry
        officialLink = entry.officialUrl?.let { pageLink(it) }
        sourceLink = pageLink("${entry.sourceRoot}/blob/${entry.sourcePath}")

        when (val example = entry.body) {
            is ExampleBody.Option -> {
                val node = chart(example.option, example.height)
                chartNode = node
                chartSection.body.append(exampleBox(null, node))
                example.json?.let { json -> jsonSection = buildJsonSection(json.json) }
            }

            is ExampleBody.Interactive -> {
                val node = interactiveChart(example.height, example.setup)
                chartNode = node
                chartSection.body.append(exampleBox(null, node))
            }

            is ExampleBody.Model -> {
                val node = jsonChart(example.json)
                chartNode = node
                chartSection.body.append(exampleBox(null, node))
                modelSection = SectionBlock.NAME { show() }.also {
                    it.body.append(exampleBox(null, codeText(example.kotlinSource)))
                }
                jsonSection = SectionBlock.NAME { show() }.also {
                    it.body.append(exampleBox(null, codeText(example.json)))
                }
            }

            is ExampleBody.Custom -> Unit
        }

        body.append(chartSection.root)
        modelSection?.let { body.append(it.root) }
        jsonSection?.let { body.append(it.root) }
    }

    /** One link of the page's link row, opened in a new tab. */
    private fun pageLink(url: String): HTMLAnchorElement =
        anchor("", url, external = true).also { addLink(it) }

    /**
     * The JSON card: the switch that decides what the chart consumes, and the JSON itself.
     *
     * Switching does not rebuild the page — the chart element re-initialises with the other input, which
     * is the same code path a first mount takes.
     */
    private fun buildJsonSection(json: String): SectionBlock {
        val section = SectionBlock.NAME { show() }
        val demoSwitch = createSwitch()
        val onToggle: (Event) -> Unit = {
            val node = chartNode
            val example = entry?.body as? ExampleBody.Option
            val builder = example?.json
            if (node != null && example != null && builder != null) {
                node.reload(if (demoSwitch.checked) jsonInput(builder.json) else optionInput(example.option))
            }
        }
        demoSwitch.addEventListener(SwitchEvents.checkedChanged, onToggle)
        jsonSwitch = demoSwitch
        section.body.append(exampleBox(null, demoSwitch, codeText(json)))
        return section
    }

    override fun applyTranslation(lang: Lang) {
        val current = entry ?: return
        val zh = lang == Langs.ZH_HANS
        setTitle(if (zh) current.titleZh else current.titleEn)
        setSummary(if (zh) current.summaryZh else current.summaryEn)

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
        sourceLink?.textContent = if (zh) "Kotlin 源码" else "Kotlin source"
    }

    companion object : CustomElementReferenceAdapter<ExamplePage>(ExamplePage::class.js, "echarts-example-page")
}

/** Creates a defined `<echarts-example-page>` showing [entry]. */
internal fun newExamplePage(entry: CatalogEntry): ExamplePage {
    // The companion registers the tag in its `init`; reading `NAME` upgrades the element the page already spells.
    ExamplePage.NAME
    return ExamplePage.NAME().also { it.show(entry) }
}
