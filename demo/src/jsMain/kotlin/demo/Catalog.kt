package demo

import demo.examples.bar.barEntries
import demo.examples.boxplot.boxplotEntries
import demo.examples.calendar.calendarEntries
import demo.examples.candlestick.candlestickEntries
import demo.examples.chord.chordEntries
import demo.examples.custom.customEntries
import demo.examples.dataset.datasetEntries
import demo.examples.doc.docEntries
import demo.examples.funnel.funnelEntries
import demo.examples.gauge.gaugeEntries
import demo.examples.geo.geoEntries
import demo.examples.gl.glEntries
import demo.examples.graph.graphEntries
import demo.examples.graphic.graphicEntries
import demo.examples.heatmap.heatmapEntries
import demo.examples.kmpjson.kmpJsonEntries
import demo.examples.line.lineEntries
import demo.examples.lines.linesEntries
import demo.examples.matrix.matrixEntries
import demo.examples.overview.overviewEntries
import demo.examples.parallel.parallelEntries
import demo.examples.pictorialbar.pictorialbarEntries
import demo.examples.pie.pieEntries
import demo.examples.radar.radarEntries
import demo.examples.sankey.sankeyEntries
import demo.examples.scatter.scatterEntries
import demo.examples.sunburst.sunburstEntries
import demo.examples.themeriver.themeriverEntries
import demo.examples.tree.treeEntries
import demo.examples.treemap.treemapEntries
import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs

/** Dokka output assembled by `pagesSite` next to the demo. */
const val API_ROOT = "api/ktjs-echarts"

/** Menu entry point of the API reference. */
const val API_DOCS_URL = "$API_ROOT/index.html"

/** Live gallery of the official examples. */
const val OFFICIAL_INDEX = "https://echarts.apache.org/examples/zh/index.html"

/** GitHub blob base of the Kotlin sources of this showcase. */
const val REPO_SOURCE_ROOT = "https://github.com/shijianjs/ktjs-echarts/blob/main/demo/src/jsMain/kotlin/demo"

/** GitHub blob base of the platform-independent demo sources. */
const val REPO_COMMON_SOURCE_ROOT =
    "https://github.com/shijianjs/ktjs-echarts/blob/main/demo/src/commonMain/kotlin/demo"

/** Live example on the official site, addressed by the id the demo uses as its route. */
fun officialExampleUrl(id: String): String = "https://echarts.apache.org/examples/zh/editor.html?c=$id"

/**
 * One drawer group.
 *
 * [titleZh] / [titleEn] are the two language variants of the same title; [icon] is an Iconify name
 * drawn before it (the shell's own icon set, so the demo carries no SVG).
 */
class Group(val id: String, val titleZh: String, val titleEn: String, val icon: String? = null)

/**
 * How a page gets its chart.
 *
 * The page builder is generic on purpose: every example differs in the option, not in the frame, so
 * the frame lives in [ExamplePage] and a `(id, title, option)` triple is all a family declaration
 * has to write.
 */

/**
 * One page of the gallery.
 *
 * [id] is the route id *and* the id the official gallery uses, so a ported example is one click away
 * from the live original. Titles and summaries are kept in both languages here and resolved when the
 * page is translated, which is what lets a language switch rewrite strings instead of rebuilding the
 * page.
 */

/**
 * Every ported example, one page each, grouped by the examples' family.
 *
 * Each family directory owns one `<family>Entries` list and every page is its own file there, so a new
 * example only touches the family it belongs to. The order of the list below is the order of the
 * drawer groups; within a group the declaration order of the family's `Entries.kt` is kept.
 */
val catalog: List<CatalogEntry> =
    listOf(
        overviewEntries,
        barEntries,
        pieEntries,
        lineEntries,
        linesEntries,
        candlestickEntries,
        scatterEntries,
        boxplotEntries,
        pictorialbarEntries,
        parallelEntries,
        heatmapEntries,
        gaugeEntries,
        geoEntries,
        funnelEntries,
        radarEntries,
        treeEntries,
        treemapEntries,
        sunburstEntries,
        datasetEntries,
        calendarEntries,
        matrixEntries,
        customEntries,
        graphicEntries,
        graphEntries,
        chordEntries,
        sankeyEntries,
        themeriverEntries,
        glEntries,
        docEntries,
        kmpJsonEntries,
    ).flatten()

private val catalogById: Map<String, CatalogEntry> = catalog.associateBy { it.id }

/** Groups that actually have entries, in navigation order. */
val catalogGroups: List<Group> = catalog.map { it.group }.distinct()

/** Looks up an entry by route id. */
fun catalogEntry(id: String): CatalogEntry? = catalogById[id]

/** Entries of one drawer group, in catalog order. */
fun catalogEntriesOf(group: Group): List<CatalogEntry> = catalog.filter { it.group == group }

/**
 * The title of a group in [lang].
 *
 * Titles are stored as two strings rather than resolved at declaration time: the ids the drawer and the
 * router use are language-independent, and a language switch is a rewrite of these two fields, never a
 * rebuild of the navigation.
 */
fun Group.titleFor(lang: Lang): String = if (lang == Langs.ZH_HANS) titleZh else titleEn

/** The title of a page in [lang]. */
fun CatalogEntry.titleFor(lang: Lang): String = if (lang == Langs.ZH_HANS) titleZh else titleEn

/** The summary of a page in [lang]. */
fun CatalogEntry.summaryFor(lang: Lang): String = if (lang == Langs.ZH_HANS) summaryZh else summaryEn
