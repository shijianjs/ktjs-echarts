package demo

import demo.examples.barDrilldownEntries
import demo.examples.barEntries
import demo.examples.barExtraEntries
import demo.examples.barLongTailEntries
import demo.examples.boxplotEntries
import demo.examples.calendarEntries
import demo.examples.canvasGraphicsEntries
import demo.examples.candlestickEntries
import demo.examples.candlestickLongTailEntries
import demo.examples.chordEntries
import demo.examples.customEntries
import demo.examples.customHexbinEntries
import demo.examples.customExtraEntries
import demo.examples.customGaugeEntries
import demo.examples.customLongTailEntries
import demo.examples.customMoreEntries
import demo.examples.customTransitionEntries
import demo.examples.datasetEntries
import demo.examples.datasetExtraEntries
import demo.examples.docExampleEntries
import demo.examples.docInteractiveEntries
import demo.examples.docMoreEntries
import demo.examples.funnelEntries
import demo.examples.extraPagesEntries
import demo.examples.financeTimelineEntries
import demo.examples.gaugeEntries
import demo.examples.geoExtraEntries
import demo.examples.gaugeExtraEntries
import demo.examples.gaugeLongTailEntries
import demo.examples.glBarGeoEntries
import demo.examples.glEntries
import demo.examples.glGeoEntries
import demo.examples.glImageBarsEntries
import demo.examples.glLinesEntries
import demo.examples.glScatterEntries
import demo.examples.glSurfaceMaterialEntries
import demo.examples.graphicAnimationEntries
import demo.examples.graphDataEntries
import demo.examples.graphEntries
import demo.examples.graphExtraEntries
import demo.examples.heatmapEntries
import demo.examples.heatmapLongTailEntries
import demo.examples.generatedSeriesEntries
import demo.examples.intradayBreaksEntries
import demo.examples.lineEntries
import demo.examples.linesExtraEntries
import demo.examples.lineExtraEntries
import demo.examples.lineLongTailEntries
import demo.examples.matrixEntries
import demo.examples.matrixExtraEntries
import demo.examples.matrixLongTailEntries
import demo.examples.matrixStockEntries
import demo.examples.overviewEntries
import demo.examples.parallelAqiEntries
import demo.examples.parallelNutrientsEntries
import demo.examples.parallelEntries
import demo.examples.pictorialBarEntries
import demo.examples.pictorialBarLongTailEntries
import demo.examples.pieEntries
import demo.examples.radarEntries
import demo.examples.rainfallLineEntries
import demo.examples.richTextEntries
import demo.examples.sankeyEntries
import demo.examples.scatterEntries
import demo.examples.scatterAqiEntries
import demo.examples.scatterExtraEntries
import demo.examples.scatterLongTailEntries
import demo.examples.scatterLifeTimelineEntries
import demo.examples.scatterMatrixEntries
import demo.examples.scatterNutrientsEntries
import demo.examples.scatterWeightEntries
import demo.examples.scatterWorldEntries
import demo.examples.sunburstEntries
import demo.examples.themeRiverEntries
import demo.examples.treeEntries
import demo.examples.treemapDataEntries
import demo.examples.treemapEntries
import demo.examples.treemapExtraEntries
import demo.ui.PageMeta
import demo.ui.PageView
import demo.ui.officialExampleUrl

/**
 * Dokka output assembled by `pagesSite` next to the demo.
 *
 * The wrapper is one module, so the API reference is the module index rather than a page per type.
 */
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
}

/**
 * One entry of the gallery: the route id, the sidebar group it belongs to, the page metadata and the
 * builder that produces the page DOM.
 */
class CatalogEntry(
    val id: String,
    val group: Group,
    val meta: PageMeta,
    private val builder: (PageMeta) -> PageView,
) {
    /** Builds a fresh instance of the page. The shell caches one per route. */
    fun create(): PageView = builder(meta)
}

/**
 * Every ported example, one page each.
 *
 * The order of the lists below is the order of the sidebar groups; within a group the declaration
 * order of the category file is kept. `examples/Overview.kt` indexes the whole list, so a new entry
 * shows up there without a second registration.
 */
val catalog: List<CatalogEntry> =
    listOf(
        overviewEntries,
        barEntries,
        barExtraEntries,
        barLongTailEntries,
        barDrilldownEntries,
        financeTimelineEntries,
        richTextEntries,
        extraPagesEntries,
        lineEntries,
        rainfallLineEntries,
        linesExtraEntries,
        lineExtraEntries,
        lineLongTailEntries,
        intradayBreaksEntries,
        generatedSeriesEntries,
        pieEntries,
        scatterEntries,
        scatterExtraEntries,
        scatterAqiEntries,
        scatterLifeTimelineEntries,
        scatterNutrientsEntries,
        scatterLongTailEntries,
        scatterMatrixEntries,
        scatterWeightEntries,
        scatterWorldEntries,
        candlestickEntries,
        candlestickLongTailEntries,
        boxplotEntries,
        pictorialBarEntries,
        pictorialBarLongTailEntries,
        parallelEntries,
        parallelAqiEntries,
        parallelNutrientsEntries,
        heatmapEntries,
        heatmapLongTailEntries,
        gaugeEntries,
        geoExtraEntries,
        gaugeExtraEntries,
        gaugeLongTailEntries,
        funnelEntries,
        radarEntries,
        treeEntries,
        treemapEntries,
        treemapDataEntries,
        treemapExtraEntries,
        sunburstEntries,
        datasetEntries,
        datasetExtraEntries,
        calendarEntries,
        matrixEntries,
        matrixExtraEntries,
        matrixLongTailEntries,
        matrixStockEntries,
        customEntries,
        customHexbinEntries,
        customExtraEntries,
        customGaugeEntries,
        customLongTailEntries,
        customMoreEntries,
        customTransitionEntries,
        graphicAnimationEntries,
        canvasGraphicsEntries,
        graphEntries,
        graphDataEntries,
        graphExtraEntries,
        chordEntries,
        sankeyEntries,
        themeRiverEntries,
        glEntries,
        glBarGeoEntries,
        glGeoEntries,
        glImageBarsEntries,
        glLinesEntries,
        glScatterEntries,
        glSurfaceMaterialEntries,
        docExampleEntries,
        docMoreEntries,
        docInteractiveEntries,
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
