package demo

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
