package demo

import demo.examples.useEChartsGl
import web.dom.document

/**
 * Entry point of the example gallery.
 *
 * The site is a single-page application: the shell is built once and the router swaps the page
 * inside it on every fragment change. Nothing is assembled from markup strings — every node is
 * created with `document.createElement` and configured through typed properties.
 */
fun main() {
    // Pages resolve their copy when they are built, and charts pick their theme at mount time, so
    // both have to be settled before the first page is created.
    LanguageState.init()
    Theme.applyStored()

    // The 3D charts of `echarts-gl` are not part of the `echarts` entry; `ECharts.use` registers
    // them for every chart instance, so it has to run before the first page mounts.
    useEChartsGl()

    val shell = AppShell()
    document.body.appendChild(shell.root)

    Router(shell::show).start()
}
