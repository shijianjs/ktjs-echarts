package demo

import demo.ui.PageView
import demo.ui.anchor
import demo.ui.button
import demo.ui.div
import demo.ui.element
import demo.ui.listen
import demo.ui.span
import demo.ui.withClass
import web.cssom.ClassName
import web.dom.document
import web.events.Event
import web.events.EventType
import web.html.HTMLButtonElement
import web.html.HTMLElement
import web.html.HtmlTagName
import web.location.location
import web.window.window

/** `click` as a typed event. */
private val CLICK: EventType<Event> = EventType("click")

/** `resize` as a typed event. */
private val RESIZE: EventType<Event> = EventType("resize")

/** One collapsible sidebar group: the header button, the list it controls and the current state. */
private class NavGroup(
    val group: Group,
    val toggle: HTMLButtonElement,
    val list: HTMLElement,
    val label: HTMLElement,
    var expanded: Boolean,
)

/**
 * Application shell: a fixed sidebar built once from [catalog], a top bar with the breadcrumb, the
 * language switch, the theme switch and the links, and the content area that hosts the current page.
 *
 * The shell never rebuilds structure. Navigation swaps the single child of [contentHost] and keeps
 * the built pages cached, so a page keeps its DOM when the user walks the gallery and comes back.
 * Charts are different from the rest of a page — they measure their container, so the shell mounts
 * them when the page enters the view and disposes them when it leaves.
 *
 * A language or theme switch is different from navigation: both affect how a page is built (copy and
 * chart theme), so the cached pages are dropped and the current route is built again.
 */
class AppShell {
    private val contentHost = div("content__host")

    private val breadcrumb = span("", "topbar__breadcrumb")

    private val navItems = mutableMapOf<String, HTMLElement>()

    private val navGroups = mutableMapOf<String, NavGroup>()

    private val pages = mutableMapOf<String, PageView>()

    private val lightButton: HTMLButtonElement = button()

    private val darkButton: HTMLButtonElement = button()

    private val zhButton: HTMLButtonElement = button(Language.ZH.label)

    private val enButton: HTMLButtonElement = button(Language.EN.label)

    private val menuButton: HTMLButtonElement = button().withClass("topbar__menu")

    private val apiLink = anchor("", API_DOCS_URL, "topbar__link")

    private val officialLink = anchor("", OFFICIAL_SITE, "topbar__link", external = true)

    private val nav: HTMLElement = buildNav()

    private val main: HTMLElement = buildMain()

    /** Root element; the shell owns it for the whole lifetime of the page. */
    val root: HTMLElement = div("shell", nav, main)

    private var shownRoute: String? = null

    private var currentRoute: String? = null

    private var shownPage: PageView? = null

    init {
        lightButton.listen(CLICK) { switchTheme(false) }
        darkButton.listen(CLICK) { switchTheme(true) }
        zhButton.listen(CLICK) { LanguageState.set(Language.ZH) }
        enButton.listen(CLICK) { LanguageState.set(Language.EN) }
        menuButton.listen(CLICK) { nav.classList.toggle(ClassName("shell__nav--open")) }
        window.listen(RESIZE) { shownPage?.resize() }
        LanguageState.onChange { rebuild() }
        syncChrome()
        syncThemeButtons()
        syncLanguageButtons()
    }

    /** Renders [route], falling back to the overview when the fragment names an unknown page. */
    fun show(route: String) {
        val entry = catalogEntry(route) ?: catalogEntry(Router.DEFAULT_ROUTE) ?: return
        currentRoute = entry.id

        if (shownRoute != entry.id) {
            shownRoute = entry.id
            navItems.forEach { (id, item) ->
                val active = id == entry.id
                item.classList.toggle(ClassName("nav__current"), active)
                item.ariaCurrent = if (active) "page" else null
            }
            setGroupExpanded(entry.group.id, true)
            breadcrumb.textContent = "${entry.group.label} / ${entry.meta.title}"
            document.title = "${entry.meta.title} · ktjs-echarts"
        }

        val page = pages.getOrPut(entry.id) { entry.create() }
        if (contentHost.firstElementChild !== page.root) {
            if (shownPage !== null && shownPage !== page) shownPage?.detach()
            contentHost.replaceChildren(page.root)
            page.attach()
            shownPage = page
            window.scrollTo(0.0, 0.0)
        }
        nav.classList.remove(ClassName("shell__nav--open"))
    }

    /** Navigates to [route]; the router reacts to the resulting `hashchange`. */
    private fun open(route: String) {
        location.hash = Router.hashOf(route)
    }

    private fun switchTheme(dark: Boolean) {
        Theme.apply(dark)
        syncThemeButtons()
        rebuild()
    }

    /** Drops the cached pages and builds the current route again; used by language and theme. */
    private fun rebuild() {
        syncChrome()
        syncThemeButtons()
        syncLanguageButtons()
        navItems.forEach { (id, item) -> catalogEntry(id)?.let { item.textContent = it.meta.title } }
        navGroups.values.forEach { it.label.textContent = it.group.label }
        shownPage?.detach()
        shownPage = null
        pages.clear()
        shownRoute = null
        currentRoute?.let { show(it) }
    }

    private fun syncChrome() {
        lightButton.textContent = tr("亮色", "Light")
        darkButton.textContent = tr("暗色", "Dark")
        menuButton.textContent = tr("目录", "Catalog")
        menuButton.ariaLabel = tr("显示目录", "Show catalog")
        apiLink.textContent = tr("Kotlin API", "Kotlin API")
        officialLink.textContent = tr("ECharts 官网", "ECharts site")
    }

    private fun syncThemeButtons() {
        val dark = Theme.isDark
        // Toggle, not add: the two buttons share one highlighted style, so a switch has to clear
        // the previous pick or both of them stay highlighted.
        lightButton.classList.toggle(ClassName("is-on"), !dark)
        darkButton.classList.toggle(ClassName("is-on"), dark)
        lightButton.ariaPressed = if (dark) "false" else "true"
        darkButton.ariaPressed = if (dark) "true" else "false"
    }

    private fun syncLanguageButtons() {
        val chinese = LanguageState.current == Language.ZH
        zhButton.classList.toggle(ClassName("is-on"), chinese)
        enButton.classList.toggle(ClassName("is-on"), !chinese)
        zhButton.ariaPressed = if (chinese) "true" else "false"
        enButton.ariaPressed = if (chinese) "false" else "true"
    }

    private fun setGroupExpanded(groupId: String, expanded: Boolean) {
        val navGroup = navGroups[groupId] ?: return
        navGroup.expanded = expanded
        navGroup.toggle.ariaExpanded = if (expanded) "true" else "false"
        navGroup.list.classList.toggle(ClassName("is-hidden"), !expanded)
        navGroup.toggle.classList.toggle(ClassName("nav__group-title--collapsed"), !expanded)
    }

    private fun buildNav(): HTMLElement {
        val side = element(HtmlTagName.nav, "shell__nav")
        side.append(
            div(
                "nav__brand",
                span("ktjs-echarts", "nav__brand-name"),
                span(tr("Kotlin/JS", "Kotlin/JS"), "nav__brand-tag"),
            ),
        )

        catalogGroups.forEach { group ->
            val list = element(HtmlTagName.ul, "nav__list")
            catalogEntriesOf(group).forEach { entry ->
                val item = element(HtmlTagName.li, "nav__item")
                val link = button(entry.meta.title)
                link.withClass("nav__link")
                link.dataset["route"] = entry.id
                link.listen(CLICK) { open(entry.id) }
                item.append(link)
                navItems[entry.id] = link
                list.append(item)
            }

            val label = span(group.label)
            val toggle = button("", "nav__group-title")
            toggle.append(label, span("", "nav__group-chevron"))
            toggle.ariaExpanded = "false"
            toggle.dataset["group"] = group.id

            val navGroup = NavGroup(group, toggle, list, label, expanded = false)
            navGroups[group.id] = navGroup
            toggle.listen(CLICK) { setGroupExpanded(group.id, !navGroup.expanded) }
            setGroupExpanded(group.id, false)

            side.append(div("nav__group", toggle, list))
        }

        return side
    }

    private fun buildMain(): HTMLElement {
        val actions =
            div(
                "topbar__actions",
                apiLink,
                officialLink,
                div("lang-switch", zhButton, enButton),
                div("theme-switch", lightButton, darkButton),
            )

        val topbar = element(HtmlTagName.header, "topbar", menuButton, breadcrumb, actions)
        val content = element(HtmlTagName.main, "content", contentHost)
        return div("shell__main", topbar, content)
    }

    private companion object {
        const val OFFICIAL_SITE = "https://echarts.apache.org/examples/zh/index.html"
    }
}
