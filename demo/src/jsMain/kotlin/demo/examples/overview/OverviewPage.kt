package demo.examples.overview

import demo.CatalogEntry
import demo.ExampleBody
import demo.Groups
import demo.catalog
import demo.catalogEntriesOf
import demo.catalogGroups
import demo.summaryFor
import demo.titleFor
import io.github.shijianjs.ktjs.demo.i18n.Binder
import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.i18n.Translations
import io.github.shijianjs.ktjs.demo.i18n.texts
import io.github.shijianjs.ktjs.demo.page.DemoPage
import io.github.shijianjs.ktjs.demo.page.Blocks.anchor
import io.github.shijianjs.ktjs.demo.page.Blocks.heading
import io.github.shijianjs.ktjs.demo.page.Blocks.icon
import io.github.shijianjs.ktjs.demo.page.Blocks.paragraph
import io.github.shijianjs.ktjs.demo.page.Blocks.row
import io.github.shijianjs.ktjs.demo.page.Grid
import io.github.shijianjs.ktjs.demo.page.SectionBlock
import io.github.shijianjs.ktjs.demo.page.Readout
import io.github.shijianjs.ktjs.dom.css.cssSheet
import io.github.shijianjs.ktjs.dom.css.dsl.attributeSelector
import io.github.shijianjs.ktjs.dom.css.dsl.child
import io.github.shijianjs.ktjs.dom.css.dsl.className
import io.github.shijianjs.ktjs.dom.css.dsl.compound
import io.github.shijianjs.ktjs.dom.css.dsl.invoke
import io.github.shijianjs.ktjs.dom.css.dsl.pseudo
import io.github.shijianjs.ktjs.dom.css.dsl.pseudoClass
import io.github.shijianjs.ktjs.dom.css.dsl.tag
import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.dom.webcomponent.CustomElementReferenceAdapter
import io.github.shijianjs.ktjs.vaadin.VaadinTagName
import io.github.shijianjs.ktjs.vaadin.card.CardVariants
import io.github.shijianjs.ktjs.vaadin.card.createCard
import io.github.shijianjs.ktjs.vaadin.textfield.TextFieldEvents
import io.github.shijianjs.ktjs.vaadin.textfield.createTextField
import io.github.shijianjs.ktjs.vaadin.themablemixin.addTheme
import web.cssom.ClassName
import web.cssom.Color
import web.cssom.CSSStyleSheet
import web.cssom.Display
import web.cssom.HtmlAttributes
import web.cssom.None
import web.cssom.number
import web.dom.Node
import web.events.Event
import web.events.addEventListener
import web.html.HtmlTagName

private val english: OverviewTexts = OverviewTexts(
    title = "Example overview",
    summary = "Every example of the official ECharts gallery, ported to Kotlin/JS one page at a time.",
    whatHeading = "What this is",
    whatBody = "Each page mirrors one example of the official gallery, and its route id is the official " +
        "example id (e.g. #/bar-simple), so the “Official example” link opens the very same chart on " +
        "echarts.apache.org. Options are written entirely in Kotlin: components through unsafeJso, series " +
        "through the generated factories, and values such as axis types from the generated constant objects.",
    indexHeading = "Example index",
    indexHint = "Filter by name, then open a card to visit its page.",
    findLabel = "Find an example",
    findPlaceholder = "e.g. bar, gauge, river",
    showing = { visible, total -> "Showing $visible of $total examples" },
)

private val chinese: OverviewTexts = OverviewTexts(
    title = "示例总览",
    summary = "把 ECharts 官方示例逐个移植成 Kotlin/JS：每个图表都由 ktjs-echarts 的类型化 API 构造。",
    whatHeading = "这是什么",
    whatBody = "每一页对应官方示例库里的一个例子，路由 id 与官方示例 id 一致（例如 #/bar-simple），" +
        "页面上的「官方示例」链接可打开同一张图的官方页面。所有选项都写成 Kotlin：组件用 unsafeJso 构造，" +
        "系列用生成的工厂，坐标轴类型等取值来自生成的常量对象。",
    indexHeading = "示例索引",
    indexHint = "输入名称过滤，点击卡片进入对应页面。",
    findLabel = "查找示例",
    findPlaceholder = "例如 bar、gauge、river",
    showing = { visible, total -> "显示 $visible / $total 个示例" },
)

/** The class the index cards are found by in tests; it carries no style of its own. */
private val EXAMPLE_CARD: ClassName = ClassName("example-card")

/** The condition the filter toggles, written as a selector instead of as text. */
private val NOT_HIDDEN = pseudo(pseudoClass("not"), attributeSelector(HtmlAttributes.hidden))

/**
 * Landing page: what the showcase covers and an index of every ported example.
 *
 * The index is filterable so the whole catalog stays reachable without scrolling; filtering only toggles
 * `hidden` on the cards that are already in the DOM, and a language switch only rewrites their copy.
 */
internal class OverviewPage : DemoPage() {
    private val binder = Binder(overviewTexts)

    private val counter = Readout.NAME { show() }

    private val search = createTextField { clearButtonVisible = true }

    private val cards = mutableListOf<Card>()

    private val groups = mutableListOf<GroupSection>()

    private var currentLang: Lang = Langs.EN

    init {
        val what = SectionBlock.NAME { show() }
        binder.bind(what.heading) { it.whatHeading }
        what.body.append(binder.bind(paragraph()) { it.whatBody })

        val index = SectionBlock.NAME { show() }
        binder.bind(index.heading) { it.indexHeading }
        binder.bind(index.hint) { it.indexHint }
        index.body.append(row(search, counter.root), *buildIndex().toTypedArray())

        addSheet(INDEX_SHEET)
        body.append(what.root, index.root)

        val onInput: (Event) -> Unit = { filter() }
        search.addEventListener(TextFieldEvents.input, onInput)
    }

    private fun buildIndex(): List<Node> {
        val blocks = mutableListOf<Node>()
        catalogGroups.filter { group -> group != Groups.OVERVIEW }.forEach { group ->
            // The group's mark, the same the drawer draws: the heading and the drawer row are two
            // views of one section, so they carry one icon.
            val groupLabel = HtmlTagName.span { textContent = group.titleFor(currentLang) }
            val groupTitle = row(icon(group.icon ?: ""), heading(3).apply { append(groupLabel) })
            val list = Grid.NAME { show(240) }
            val groupCards = catalogEntriesOf(group).map { entry ->
                val card = card(entry)
                list.append(card.link)
                cards += card
                card
            }
            blocks += groupTitle
            blocks += list
            groups += GroupSection(groupTitle, groupLabel, groupCards)
        }
        return blocks
    }

    /**
     * One example entry: a plain `vaadin-card` inside the link that makes the entry navigable.
     *
     * The card is left exactly as the component draws itself — its own title slot, padding and typography
     * — and the link wraps the whole card, so the entire surface is clickable and keyboard / new-tab
     * behaviour comes from the anchor rather than from a click handler.
     */
    private fun card(entry: CatalogEntry): Card {
        val summary = HtmlTagName.div().apply { textContent = entry.summaryFor(currentLang) }
        val node = createCard {
            addTheme(CardVariants.elevated)
            cardTitle = entry.titleFor(currentLang)
            titleHeadingLevel = 4.0
            append(summary)
        }
        val link = HtmlTagName.a {
            href = "#/${entry.id}"
            classList.add(EXAMPLE_CARD)
            dataset["route"] = entry.id
            append(node)
        }
        return Card(entry, node, link, summary)
    }

    private fun filter() {
        val needle = search.value.trim().lowercase()
        cards.forEach { card ->
            val matches = needle.isEmpty() ||
                card.entry.titleFor(currentLang).lowercase().contains(needle) ||
                card.entry.id.contains(needle)
            card.link.toggleAttribute("hidden", !matches)
        }
        groups.forEach { section ->
            section.title.toggleAttribute("hidden", section.cards.none { !it.link.hasAttribute("hidden") })
        }
        val visible = cards.count { !it.link.hasAttribute("hidden") }
        counter.set(overviewTexts[currentLang].showing(visible, cards.size))
    }

    override fun applyTranslation(lang: Lang) {
        val current = overviewTexts[lang]
        this.currentLang = lang
        setTitle(current.title)
        setSummary(current.summary)
        binder.apply(lang)

        search.label = current.findLabel
        search.placeholder = current.findPlaceholder

        // Every card's copy comes from the catalog, not from this page's table: one place per page name.
        cards.forEach { card ->
            card.node.cardTitle = card.entry.titleFor(currentLang)
            card.summary.textContent = card.entry.summaryFor(currentLang)
        }
        groups.forEach { section -> section.label.textContent = section.cards.first().entry.group.titleFor(currentLang) }
        filter()
    }

    companion object : CustomElementReferenceAdapter<OverviewPage>(OverviewPage::class.js, "echarts-overview-page") {
        private val overviewTexts: Translations<OverviewTexts> = texts(
            Langs.EN to english,
            Langs.ZH_HANS to chinese,
        )
        /**
         * What the index needs from the wrapper: an `<a>` around a card must stop painting text the way a link
         * paints it, and must pass the row's height through to the card inside it.
         */
        private val INDEX_SHEET: CSSStyleSheet = cssSheet {
            compound(className(EXAMPLE_CARD), NOT_HIDDEN).invoke {
                display = Display.flex
            }

            className(EXAMPLE_CARD).child(tag(VaadinTagName.card)).invoke {
                flexGrow = number(1.0)
            }

            className(EXAMPLE_CARD).invoke {
                color = Color("inherit")
                textDecorationLine = None.none
            }
        }
    }
}

/** The catalog's overview entry: this page builds its body itself. */
val overviewEntries: List<CatalogEntry> =
    listOf(
        CatalogEntry(
            id = "overview",
            group = Groups.OVERVIEW,
            titleZh = "示例总览",
            titleEn = "Example overview",
            summaryZh = "把 ECharts 官方示例逐个移植成 Kotlin/JS：每个图表都由 ktjs-echarts 的类型化 API 构造。",
            summaryEn = "The official ECharts examples ported to Kotlin/JS one by one: every chart is built " +
                "with the typed ktjs-echarts API.",
            body = ExampleBody.Custom,
            sourcePath = "examples/overview/OverviewPage.kt",
            officialUrl = null,
        ),
    )
