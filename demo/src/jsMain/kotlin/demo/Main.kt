package demo

import demo.examples.gl.useEChartsGl
import demo.ui.ChartElement
import io.github.shijianjs.ktjs.demo.i18n.LanguageState
import io.github.shijianjs.ktjs.demo.layout.DemoLayout
import io.github.shijianjs.ktjs.demo.layout.LayoutConfig
import io.github.shijianjs.ktjs.demo.style.Theme
import web.dom.ElementId
import web.dom.document
import io.github.shijianjs.ktjs.dom.element.invoke

/**
 * Entry point of the example gallery.
 *
 * The `<ktjs-demo-layout>` comes from index.html: the markup names it, this file defines the tags and
 * mounts the menu into the already-connected element. The site is a single-page application — the shell
 * is built once and the router swaps the page inside it on every fragment change.
 */
fun main() {
    // A page's copy is resolved when the page is built, so the language and the theme of this session
    // have to be settled before the first page is created.
    val language = LanguageState(storageKey = "ktjs-echarts-language")
    language.init()

    val theme = Theme()
    theme.applyStored()

    // The 3D charts of `echarts-gl` are not part of the `echarts` entry; `ECharts.use` registers them
    // for every chart instance, so it has to run before the first page mounts.
    useEChartsGl()

    // The companion registers the tag in its `init`; reading `NAME` upgrades the element the page already spells.
    ChartElement.NAME
    // The companion registers the tag in its `init`; reading `NAME` upgrades the element the page already spells.
    DemoLayout.NAME

    val layout = document.getElementById(ElementId("demo")) as? DemoLayout
        ?: error("index.html has no <ktjs-demo-layout id=\"demo\">")

    layout.mount(
        LayoutConfig(
            menu = siteMenu(),
            language = language,
            theme = theme,
            texts = siteTexts,
            siteVersion = Site.VERSION,
            docsUrl = Site.DOCS_URL,
            apiUrl = Site.API_URL,
            sourceUrl = Site.SOURCE_URL,
            logoSrc = "img/ktjs-echarts-logo.png",
        ),
    )
}
