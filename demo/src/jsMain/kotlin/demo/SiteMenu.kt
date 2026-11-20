package demo

import demo.examples.overview.OverviewPage
import io.github.shijianjs.ktjs.demo.nav.DemoMenu
import io.github.shijianjs.ktjs.demo.nav.Entry
import io.github.shijianjs.ktjs.demo.nav.Group
import io.github.shijianjs.ktjs.dom.element.invoke

/** The Kotlin toolchain this bundle is compiled with; shown next to the title. */
const val SITE_VERSION: String = "Kotlin/JS 2.4.21"

/** The documentation site. */
const val DOCS_URL: String = "https://shijianjs.github.io/ktjs-echarts/"

/** Dokka reference assembled by pagesSite next to the demo. */
const val API_URL: String = "https://shijianjs.github.io/ktjs-echarts/api/"

/** Source repository, linked from the navbar's link menu. */
const val SOURCE_URL: String = "https://github.com/shijianjs/ktjs-echarts"

/**
 * One shell group per catalog group.
 *
 * The drawer compares groups by identity (`entriesOf` filters with `==`), so every entry of a group has
 * to point at the *same* instance — hence the map instead of a `Group(...)` per entry.
 */
private val groups: Map<String, Group> =
    catalogGroups.associate { group -> group.id to Group(group.id, group.icon) }

/**
 * The site: one entry per page, and one page per ported example.
 *
 * Both the drawer and the router are derived from [catalog], so adding an example means adding its
 * `<family>Entries` list — the navigation, the breadcrumbs and the index page follow without a second
 * declaration to keep in sync.
 */
fun siteMenu(): DemoMenu =
    DemoMenu(
        catalog.map { entry ->
            Entry(entry.id, groups.getValue(entry.group.id)) {
                // The overview draws its own index; every other route is the shared example frame.
                if (entry.body == ExampleBody.Custom) OverviewPage.NAME() else ExamplePage.NAME { show(entry) }
            }
        },
    )
