package demo

import demo.examples.overview.overviewEntries
import demo.examples.bar.barEntries
import demo.examples.pie.pieEntries
import demo.examples.line.lineEntries
import demo.examples.lines.linesEntries
import demo.examples.candlestick.candlestickEntries
import demo.examples.scatter.scatterEntries
import demo.examples.boxplot.boxplotEntries
import demo.examples.pictorialbar.pictorialbarEntries
import demo.examples.parallel.parallelEntries
import demo.examples.heatmap.heatmapEntries
import demo.examples.gauge.gaugeEntries
import demo.examples.geo.geoEntries
import demo.examples.funnel.funnelEntries
import demo.examples.radar.radarEntries
import demo.examples.tree.treeEntries
import demo.examples.treemap.treemapEntries
import demo.examples.sunburst.sunburstEntries
import demo.examples.dataset.datasetEntries
import demo.examples.calendar.calendarEntries
import demo.examples.matrix.matrixEntries
import demo.examples.custom.customEntries
import demo.examples.graphic.graphicEntries
import demo.examples.graph.graphEntries
import demo.examples.chord.chordEntries
import demo.examples.sankey.sankeyEntries
import demo.examples.themeriver.themeriverEntries
import demo.examples.gl.glEntries
import demo.examples.doc.docEntries
import demo.examples.kmpjson.kmpJsonEntries
import demo.ui.PageMeta
import demo.ui.PageView
import demo.ui.officialExampleUrl

/** Dokka output assembled by `pagesSite` next to the demo. */
const val API_ROOT = "api/ktjs-echarts"

/** Top-bar entry point of the API reference. */
const val API_DOCS_URL = "$API_ROOT/index.html"

/** A sidebar group. [id] is stable across languages; [label] is resolved for display. */
class Group(val id: String, private val zh: String, private val en: String) {
    val label: String
        get() = tr(zh, en)
}

/** Sidebar groups, in navigation order. */
object Groups {
    val OVERVIEW = Group("overview", "概览", "Overview")
    val BAR = Group("bar", "柱状图", "Bar")
    val LINE = Group("line", "折线图", "Line")
    val PIE = Group("pie", "饼图", "Pie")
    val SCATTER = Group("scatter", "散点图", "Scatter")
    val CANDLESTICK = Group("candlestick", "K 线图", "Candlestick")
    val BOXPLOT = Group("boxplot", "箱线图", "Boxplot")
    val PICTORIAL_BAR = Group("pictorial-bar", "象形柱图", "Pictorial bar")
    val PARALLEL = Group("parallel", "平行坐标", "Parallel")
    val HEATMAP = Group("heatmap", "热力图", "Heatmap")
    val GAUGE = Group("gauge", "仪表盘", "Gauge")
    val FUNNEL = Group("funnel", "漏斗图", "Funnel")
    val RADAR = Group("radar", "雷达图", "Radar")
    val TREE = Group("tree", "树图", "Tree")
    val DATASET = Group("dataset", "数据集", "Dataset")
    val SUNBURST = Group("sunburst", "旭日图", "Sunburst")
    val CALENDAR = Group("calendar", "日历图", "Calendar")
    val MATRIX = Group("matrix", "矩阵坐标系", "Matrix")
    val CUSTOM = Group("custom", "自定义系列", "Custom series")

    /** Elements drawn by the `graphic` component itself, animated through keyframes. */
    val GRAPHIC = Group("graphic", "图形元素", "Graphic")

    val TREEMAP = Group("treemap", "矩形树图", "Treemap")
    val GRAPH = Group("graph", "关系图", "Graph")
    val CHORD = Group("chord", "和弦图", "Chord")
    val SANKEY = Group("sankey", "桑基图", "Sankey")
    val THEME_RIVER = Group("theme-river", "主题河流", "Theme river")

    /** Path charts drawn on a map, the `lines` family of the manifest. */
    val LINES = Group("lines", "路径图", "Lines")

    /** Charts drawn on a registered map, the `map` / `geo` family of the manifest. */
    val GEO = Group("geo", "地图", "Geo")

    /** WebGL charts and scenes of `echarts-gl`, the `gl/` directory of the manifest. */
    val GL = Group("gl", "3D 图表", "3D charts")

    /** The short examples of the official documentation, `doc-example/` entries of the manifest. */
    val DOC = Group("doc-example", "文档示例", "Doc examples")

    /**
     * Pages built with the pure-KMP `kmp-echarts-json` model instead of the JS wrapper.
     *
     * They are not ports of official examples: each page shows the model code and the JSON it
     * serializes to, and the chart itself is fed that parsed JSON.
     */
    val JSON = Group("kmp-json", "KMP JSON 模型", "KMP JSON model")
}

/**
 * One entry of the gallery: the route id, the sidebar group it belongs to, the page metadata and the
 * builder that produces the page DOM.
 */
class CatalogEntry(
    val id: String,
    val group: Group,
    val meta: PageMeta,
    /** Whether the top-bar code-source switch applies to this page. */
    val switchableSource: Boolean = false,
    private val builder: (PageMeta) -> PageView,
) {
    /** Builds a fresh instance of the page. The shell caches one per route. */
    fun create(): PageView = builder(meta)
}


/**
 * Every ported example, one page each, grouped by the examples' family.
 *
 * Each family directory owns one `<family>Entries` list and every page is its own file there, so a
 * new example only touches the family it belongs to. The order of the list below is the order of the
 * sidebar groups; within a group the declaration order of the family's `Entries.kt` is kept.
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

/** Sidebar groups that actually have entries, in navigation order. */
val catalogGroups: List<Group> = catalog.map { it.group }.distinct()

/** Looks up an entry by route id. */
fun catalogEntry(id: String): CatalogEntry? = catalogById[id]

/** Entries of one sidebar group, in declaration order. */
fun catalogEntriesOf(group: Group): List<CatalogEntry> = catalog.filter { it.group == group }

/** Official example page of [id]; every ported example keeps the id of the official example. */
fun officialUrlOf(id: String): String = officialExampleUrl(id)
