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
import demo.json.JsonExample
import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs
import demo.ui.ChartHolder
import io.github.shijianjs.ktjs.echarts.EChartsInstance
import io.github.shijianjs.ktjs.echarts.options.EChartsOption

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

/** Drawer groups, in navigation order. */
object Groups {
    val OVERVIEW = Group("overview", "概览", "Overview", "lucide:layout-grid")
    val BAR = Group("bar", "柱状图", "Bar", "lucide:bar-chart-3")
    val LINE = Group("line", "折线图", "Line", "lucide:chart-line")
    val PIE = Group("pie", "饼图", "Pie", "lucide:chart-pie")
    val SCATTER = Group("scatter", "散点图", "Scatter", "lucide:scatter-chart")
    val CANDLESTICK = Group("candlestick", "K 线图", "Candlestick", "lucide:candlestick-chart")
    val BOXPLOT = Group("boxplot", "箱线图", "Boxplot", "lucide:box")
    val PICTORIAL_BAR = Group("pictorial-bar", "象形柱图", "Pictorial bar", "lucide:bar-chart")
    val PARALLEL = Group("parallel", "平行坐标", "Parallel", "lucide:equal")
    val HEATMAP = Group("heatmap", "热力图", "Heatmap", "lucide:grid-3x3")
    val GAUGE = Group("gauge", "仪表盘", "Gauge", "lucide:gauge")
    val FUNNEL = Group("funnel", "漏斗图", "Funnel", "lucide:funnel")
    val RADAR = Group("radar", "雷达图", "Radar", "lucide:radar")
    val TREE = Group("tree", "树图", "Tree", "lucide:git-fork")
    val DATASET = Group("dataset", "数据集", "Dataset", "lucide:database")
    val SUNBURST = Group("sunburst", "旭日图", "Sunburst", "lucide:sun")
    val CALENDAR = Group("calendar", "日历图", "Calendar", "lucide:calendar")
    val MATRIX = Group("matrix", "矩阵坐标系", "Matrix", "lucide:table")
    val CUSTOM = Group("custom", "自定义系列", "Custom series", "lucide:wand-sparkles")

    /** Elements drawn by the `graphic` component itself, animated through keyframes. */
    val GRAPHIC = Group("graphic", "图形元素", "Graphic", "lucide:shapes")

    val TREEMAP = Group("treemap", "矩形树图", "Treemap", "lucide:layout-dashboard")
    val GRAPH = Group("graph", "关系图", "Graph", "lucide:share-2")
    val CHORD = Group("chord", "和弦图", "Chord", "lucide:circle-dot")
    val SANKEY = Group("sankey", "桑基图", "Sankey", "lucide:git-branch")
    val THEME_RIVER = Group("theme-river", "主题河流", "Theme river", "lucide:waves")

    /** Charts drawn on a registered map, the `map` / `geo` family of the manifest. */
    val GEO = Group("geo", "地图", "Geo", "lucide:map")
    val LINES = Group("lines", "路径图", "Lines", "lucide:route")

    /** WebGL charts and scenes of `echarts-gl`. */
    val GL = Group("gl", "3D 图表", "3D charts", "lucide:box")
    val DOC = Group("doc-example", "文档示例", "Doc examples", "lucide:book-open")

    /**
     * Pages built with the pure-KMP `kmp-echarts-json` model instead of the JS wrapper, showing the
     * model code next to the JSON it serializes to.
     */
    val JSON = Group("kmp-json", "KMP JSON 模型", "KMP JSON model", "lucide:braces")
}

/**
 * How a page gets its chart.
 *
 * The page builder is generic on purpose: every example differs in the option, not in the frame, so
 * the frame lives in [ExamplePage] and a `(id, title, option)` triple is all a family declaration
 * has to write.
 */
sealed interface ExampleBody {
    /**
     * A chart fed by a typed option.
     *
     * [json], when the example also has a `kmp-echarts-json` builder, is the same option serialized by
     * that model: the page shows it and can feed the very chart with it, which is what proves the two
     * builders agree.
     */
    class Option(
        val option: () -> EChartsOption,
        val height: String? = null,
        val json: JsonExample? = null,
    ) : ExampleBody


    /** A chart the example drives itself: animation, timeline, user interaction, ... */
    class Interactive(
        val setup: (EChartsInstance, ChartHolder) -> Unit,
        val height: String? = null,
    ) : ExampleBody

    /** A page built from the KMP model: the chart consumes the JSON, both sources are shown. */
    class Model(
        val json: String,
        val kotlinSource: String,
    ) : ExampleBody

    /** A page that builds its own body instead of the shared example frame (the overview). */
    data object Custom : ExampleBody

}

/**
 * One page of the gallery.
 *
 * [id] is the route id *and* the id the official gallery uses, so a ported example is one click away
 * from the live original. Titles and summaries are kept in both languages here and resolved when the
 * page is translated, which is what lets a language switch rewrite strings instead of rebuilding the
 * page.
 */
class CatalogEntry(
    val id: String,
    val group: Group,
    val titleZh: String,
    val titleEn: String,
    val summaryZh: String,
    val summaryEn: String,
    val body: ExampleBody,
    val sourcePath: String,
    val officialUrl: String? = officialExampleUrl(id),
    /** GitHub blob base of [sourcePath]; the KMP model lives in the `commonMain` tree. */
    val sourceRoot: String = REPO_SOURCE_ROOT,
)

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

