package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.bi
import demo.catalog
import demo.catalogEntriesOf
import demo.catalogGroups
import demo.ui.PageMeta
import demo.ui.PageView
import demo.ui.Readout
import demo.ui.anchor
import demo.ui.div
import demo.ui.grid
import demo.ui.heading
import demo.ui.isHidden
import demo.ui.listen
import demo.ui.page
import demo.ui.pageSection
import demo.ui.paragraph
import demo.ui.setHidden
import demo.ui.span
import demo.ui.withClass
import demo.tr
import web.cssom.ClassName
import web.dom.document
import web.events.Event
import web.events.EventType
import web.events.addEventListener
import web.html.HTMLInputElement
import web.html.InputType
import web.html.search
import web.html.HTMLElement
import web.html.HtmlTagName

private val INPUT: EventType<Event> = EventType("input")

val overviewEntries: List<CatalogEntry> =
    listOf(
        CatalogEntry(
            id = "overview",
            group = Groups.OVERVIEW,
            meta =
                PageMeta(
                    id = "overview",
                    title = bi("示例总览", "Example overview"),
                    summary =
                        bi(
                            "把 ECharts 官方示例逐个移植成 Kotlin/JS：每个图表都由 ktjs-echarts 的类型化 API 构造。",
                            "The official ECharts examples ported to Kotlin/JS one by one: every chart is built " +
                                "with the typed ktjs-echarts API.",
                        ),
                    sourcePath = "examples/Overview.kt",
                ),
            builder = ::buildOverviewPage,
        ),
    )

/**
 * Landing page: what the showcase covers plus an index of every ported example.
 *
 * The index is filterable so the whole catalog stays reachable without scrolling; filtering only
 * toggles a CSS class on the cards that are already in the DOM.
 */
private fun buildOverviewPage(meta: PageMeta): PageView {
    val view = page(meta)

    val intro =
        pageSection(
            tr("这是什么", "What this is"),
            tr(
                "每一页对应 ECharts 官方示例库里的一个例子，路由 id 与官方示例 id 一致（例如 #/bar-simple）。" +
                    "页面上的「官方示例」链接可以直接打开官方站点上的同一张图。",
                "Every page mirrors one example of the official ECharts gallery, and the route id is the " +
                    "official example id (e.g. #/bar-simple). The “Official example” link opens the very same " +
                    "chart on the official site.",
            ),
        )
    intro.body.append(
        paragraph(
            tr(
                "所有选项都写成 Kotlin：组件用 unsafeJso 构造，系列用 lineSeries / barSeries 等工厂，" +
                    "坐标轴类型、触发方式等取值来自包装生成的常量对象。",
                "All options are written in Kotlin: components are built with unsafeJso, series use the " +
                    "generated factories (lineSeries, barSeries, …), and values such as axis types or trigger " +
                    "modes come from the generated constant objects.",
            ),
        ),
    )
    view.body.append(intro.root)

    val index = pageSection(tr("示例索引", "Example index"), tr("输入名称过滤，点击卡片进入对应页面。", "Filter by name, then open a card to visit its page."))

    val counter = Readout(tr("共 ${catalog.size} 个示例", "${catalog.size} examples"))
    val search = document.createElement(HtmlTagName.input)
    search.type = InputType.search
    search.placeholder = tr("例如 bar、gauge、race", "e.g. bar, gauge, race")
    search.className = ClassName("index__search")
    search.ariaLabel = tr("查找示例", "Find an example")

    val cards = mutableListOf<Pair<CatalogEntry, HTMLElement>>()
    val groupBlocks = mutableListOf<Pair<HTMLElement, MutableList<HTMLElement>>>()

    catalogGroups.filter { it != Groups.OVERVIEW }.forEach { group ->
        val groupHeading = heading(3, group.label, "index__group-title")
        val groupGrid = grid().withClass("index__grid")
        val groupCards = mutableListOf<HTMLElement>()
        catalogEntriesOf(group).forEach { entry ->
            val card =
                anchor("", "#/${entry.id}", "example-card").apply {
                    dataset["route"] = entry.id
                }
            card.append(
                span(entry.meta.title, "example-card__title"),
                paragraph(entry.id, "example-card__id"),
            )
            groupGrid.append(card)
            groupCards += card
            cards += entry to card
        }
        index.body.append(groupHeading, groupGrid)
        groupBlocks += groupHeading to groupCards
    }

    index.body.prepend(div("index__toolbar", search, counter.root))
    view.body.append(index.root)

    search.listen(INPUT) {
        val needle = search.value.trim().lowercase()
        cards.forEach { (entry, card) ->
            val matches =
                needle.isEmpty() || entry.meta.title.lowercase().contains(needle) ||
                    entry.id.contains(needle)
            card.setHidden(!matches)
        }
        groupBlocks.forEach { (groupHeading, groupCards) ->
            groupHeading.setHidden(groupCards.all { it.isHidden() })
        }
        val visible = cards.count { !it.second.isHidden() }
        counter.set(tr("显示 ", "Showing "), "$visible", tr(" / ${catalog.size} 个示例", " of ${catalog.size} examples"))
    }

    return view
}
