package demo

import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs
import demo.examples.overview.OverviewPage
import demo.examples.bar.BarEntries
import demo.examples.pie.PieEntries
import demo.examples.line.LineEntries
import demo.examples.lines.LinesEntries
import demo.examples.candlestick.CandlestickEntries
import demo.examples.scatter.ScatterEntries
import demo.examples.boxplot.BoxplotEntries
import demo.examples.pictorialbar.PictorialbarEntries
import demo.examples.parallel.ParallelEntries
import demo.examples.heatmap.HeatmapEntries
import demo.examples.gauge.GaugeEntries
import demo.examples.geo.GeoEntries
import demo.examples.funnel.FunnelEntries
import demo.examples.radar.RadarEntries
import demo.examples.tree.TreeEntries
import demo.examples.treemap.TreemapEntries
import demo.examples.sunburst.SunburstEntries
import demo.examples.dataset.DatasetEntries
import demo.examples.calendar.CalendarEntries
import demo.examples.matrix.MatrixEntries
import demo.examples.custom.CustomEntries
import demo.examples.graphic.GraphicEntries
import demo.examples.graph.GraphEntries
import demo.examples.chord.ChordEntries
import demo.examples.sankey.SankeyEntries
import demo.examples.themeriver.ThemeriverEntries
import demo.examples.gl.GlEntries
import demo.examples.doc.DocEntries
import demo.examples.kmpjson.KmpJson

/**
 * One drawer group.
 *
 * [titleZh] / [titleEn] are the two language variants of the same title; [icon] is an Iconify name
 * drawn before it (the shell's own icon set, so the demo carries no SVG).
 */
class Group(val id: String, val titleZh: String, val titleEn: String, val icon: String? = null)

/**
 * The showcase's one registry: every ported example, its drawer group, and the route table.
 *
 * Each example class carries its own entry (`BarSimple.ENTRY`), because an entry is that example's
 * metadata — route id, group, bilingual copy, and the option it renders. Per-group objects order
 * those entries, and this object orders the groups.
 *
 * Every member here is `by lazy`: the catalog is assembled from 384 companions in 30 packages, and
 * Kotlin/JS initialises file-level properties in an order nobody controls. Reading them on first use
 * is what makes the collection independent of that order.
 */
object Catalog {
    /** Dokka output assembled by `pagesSite` next to the demo. */
    const val API_ROOT: String = "api/ktjs-echarts"

    /** Menu entry point of the API reference. */
    const val API_DOCS_URL: String = "$API_ROOT/index.html"

    /** Live gallery of the official examples. */
    const val OFFICIAL_INDEX: String = "https://echarts.apache.org/examples/zh/index.html"

    /** GitHub blob base of the Kotlin sources of this showcase. */
    const val REPO_SOURCE_ROOT: String =
        "https://github.com/shijianjs/ktjs-echarts/blob/main/demo/src/jsMain/kotlin/demo"

    /** GitHub blob base of the platform-independent demo sources. */
    const val REPO_COMMON_SOURCE_ROOT: String =
        "https://github.com/shijianjs/ktjs-echarts/blob/main/demo/src/commonMain/kotlin/demo"

    /** Every ported example, in drawer order. */
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(OverviewPage.ENTRY) + listOf(
            BarEntries.ENTRIES,
            PieEntries.ENTRIES,
            LineEntries.ENTRIES,
            LinesEntries.ENTRIES,
            CandlestickEntries.ENTRIES,
            ScatterEntries.ENTRIES,
            BoxplotEntries.ENTRIES,
            PictorialbarEntries.ENTRIES,
            ParallelEntries.ENTRIES,
            HeatmapEntries.ENTRIES,
            GaugeEntries.ENTRIES,
            GeoEntries.ENTRIES,
            FunnelEntries.ENTRIES,
            RadarEntries.ENTRIES,
            TreeEntries.ENTRIES,
            TreemapEntries.ENTRIES,
            SunburstEntries.ENTRIES,
            DatasetEntries.ENTRIES,
            CalendarEntries.ENTRIES,
            MatrixEntries.ENTRIES,
            CustomEntries.ENTRIES,
            GraphicEntries.ENTRIES,
            GraphEntries.ENTRIES,
            ChordEntries.ENTRIES,
            SankeyEntries.ENTRIES,
            ThemeriverEntries.ENTRIES,
            GlEntries.ENTRIES,
            DocEntries.ENTRIES,
            KmpJson.ENTRIES,
        ).flatten()
    }

    /** Groups that actually have entries, in navigation order. */
    val GROUPS: List<Group> by lazy { ENTRIES.map { it.group }.distinct() }

    private val BY_ID: Map<String, CatalogEntry> by lazy { ENTRIES.associateBy { it.id } }

    /** Looks up an entry by route id. */
    fun entry(id: String): CatalogEntry? = BY_ID[id]

    /** Entries of one drawer group, in catalog order. */
    fun entriesOf(group: Group): List<CatalogEntry> = ENTRIES.filter { it.group == group }
}

/** Live example on the official site, addressed by the id the demo uses as its route. */
fun officialExampleUrl(id: String): String = "https://echarts.apache.org/examples/zh/editor.html?c=$id"

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
