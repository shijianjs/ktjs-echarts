package demo

import demo.examples.overview.OverviewPage
import io.github.shijianjs.ktjs.demo.nav.DemoMenu
import io.github.shijianjs.ktjs.demo.nav.Entry
import io.github.shijianjs.ktjs.demo.nav.Group
import io.github.shijianjs.ktjs.dom.element.invoke
import demo.Catalog

/** The Kotlin toolchain this bundle is compiled with; shown next to the title. */
const val SITE_VERSION: String = "Kotlin/JS 2.4.21"

/** The documentation site. */
const val DOCS_URL: String = "https://shijianjs.github.io/ktjs-echarts/"

/** Dokka reference assembled by pagesSite next to the demo. */
const val API_URL: String = "https://shijianjs.github.io/ktjs-echarts/api/"

/** Source repository, linked from the navbar's link menu. */
const val SOURCE_URL: String = "https://github.com/shijianjs/ktjs-echarts"

/**
 * The site: one entry per page, and one page per ported example.
 *
 * Both the drawer and the router are derived from [Catalog.ENTRIES], so adding an example means adding
 * its entry to its family's group object — the navigation, the breadcrumbs and the index page follow
 * without a second declaration to keep in sync.
 *
 * The groups are built inside the call, from the catalog, because the drawer compares groups by identity
 * (`entriesOf` filters with `==`): every entry of a group has to point at the *same* instance.
 */
fun siteMenu(): DemoMenu {
    val groups: Map<String, Group> = Catalog.GROUPS.associate { group -> group.id to Group(group.id, group.icon) }
    return DemoMenu(
        Catalog.ENTRIES.map { entry ->
            Entry(entry.id, groups.getValue(entry.group.id)) {
                // The overview draws its own index; every other route is the shared example frame.
                if (entry.body == ExampleBody.Custom) OverviewPage.NAME() else ExamplePage.NAME { show(entry) }
            }
        },
    )
}
