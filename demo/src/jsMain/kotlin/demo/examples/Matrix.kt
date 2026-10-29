package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.CustomSeriesRenderItem
import echarts.Orients
import echarts.VisualMapTypes
import echarts.options.CoordinateSystemDataCoord
import echarts.options.CustomGroupOption
import echarts.options.CustomTextOption
import echarts.options.DatasetOption
import echarts.options.OptionDataValue
import echarts.options.OptionEncode
import echarts.options.OptionEncodeValue
import echarts.options.customSeries
import echarts.options.datasetArrayRows
import echarts.options.CustomPathOption
import echarts.options.CustomSeriesOption
import echarts.options.DimensionLoose
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.GraphDataItem
import echarts.options.GraphEdgeItemOption
import echarts.options.GraphicComponentLooseOption
import echarts.options.GraphicComponentOption
import echarts.options.GraphicElement
import echarts.options.HeatmapDataItem
import echarts.options.HeatmapSeriesOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.LabelOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.MatrixDimensionCellLooseOption
import echarts.options.MatrixDimensionCellOption
import echarts.options.MatrixDimensionOption
import echarts.options.MatrixOption
import echarts.options.OptionDataItem
import echarts.options.ScatterDataItem
import echarts.options.SymbolSize
import echarts.options.TextStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.StringOrStringArray
import echarts.options.TooltipOption
import echarts.options.TooltipValueFormatter
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.dataItems
import echarts.options.graphSeries
import echarts.options.heatmapSeries
import echarts.options.one
import echarts.options.pieSeries
import echarts.options.scatterSeries
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRFontWeights
import web.cssom.TextAlign
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * Matrix examples ported from `public/examples/ts/matrix-*.ts` of apache/echarts-examples.
 *
 * The matrix coordinate system is new in ECharts 6: `matrix.x` / `matrix.y` declare the header
 * cells, and every series names the cell it belongs to through the values of its data items.
 *
 * `Math.random()` of the originals is replaced by a fixed sequence (see [sequence]); the examples
 * therefore look the same on every visit and the end-to-end assertions stay stable.
 *
 * Not ported:
 *
 * - `matrix-graph` and `matrix-confusion` place their axis captions with `window.innerWidth`; the
 *   captions here sit at a percentage position instead;
 * - `matrix-sparkline` lays ~25 grids out on the matrix, which needs `grid.coord` and a
 *   `dataZoom` over all x axes — left for a later batch;
 * - `matrix-mbti` / `matrix-periodic-table` / `matrix-stock` / `matrix-mini-bar-*` combine the
 *   matrix with `body.data`, custom `renderItem` bars and `graphic` captions; the periodic table
 *   additionally needs a 118-row element table.
 *
 * `matrix-grid-layout` is ported in `MatrixGridLayout.kt`.
 */

private val matrix = Category(Groups.MATRIX, "examples/Matrix.kt")

/**
 * A reproducible replacement for `Math.random()`: a linear congruential sequence returning values
 * in `[0, 1)`.
 */
private fun sequence(seed: Long): () -> Double {
    var state = seed
    return {
        state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
        (state % 100000L).toDouble() / 100000.0
    }
}

/** One `matrix` component. */
internal fun matrixOption(block: MatrixOption.() -> Unit): Array<MatrixOption> = one(unsafeJso(block))

/** One dimension of the matrix: a header row or a header column. */
internal fun dimension(block: MatrixDimensionOption.() -> Unit): MatrixDimensionOption = unsafeJso(block)

/** A header cell, optionally with nested cells. */
internal fun cell(value: String, vararg kids: MatrixDimensionCellOption): MatrixDimensionCellOption =
    unsafeJso {
        this.value = value
        if (kids.isNotEmpty()) children = arrayOf(*kids)
    }

/** Header cells that are plain names. */
internal fun names(vararg values: String): Array<MatrixDimensionCellLooseOption> =
    Array(values.size) { MatrixDimensionCellLooseOption(values[it]) }

/** A header cell that has children of its own. */
internal fun branch(value: String, vararg kids: MatrixDimensionCellOption): MatrixDimensionCellLooseOption =
    MatrixDimensionCellLooseOption(cell(value, *kids))

/** `[x, y, value]` points of a matrix heatmap. */
private fun heatmapPoints(points: List<Triple<String, String, Double>>): Array<HeatmapDataItem?> =
    dataItems(*points.map { (x, y, value) -> HeatmapDataItem(row(x, y, value)) }.toTypedArray())

/** `[x, y, value]` points of a matrix scatter series. */
private fun scatterPoints(points: List<Triple<String, String, Double>>): Array<ScatterDataItem?> =
    dataItems(*points.map { (x, y, value) -> ScatterDataItem(row(x, y, value)) }.toTypedArray())

/** A continuous visual map that colors the value of dimension 2. */
private fun valueVisualMap(min: Double, max: Double, block: VisualMapOption.() -> Unit = {}): Array<VisualMapOption> =
    one(
        unsafeJso {
            type = VisualMapTypes.continuous
            this.min = min
            this.max = max
            dimension = 2.0
            calculable = true
            orient = Orients.horizontal
            top = StringOrDouble(5.0)
            left = StringOrDouble("center")
            block()
        },
    )

/** Label formatter of the correlation matrices: two decimals of the value of dimension 2. */
private fun twoDecimals(): LabelFormatter =
    LabelFormatter { params -> fixed(numberAt(params.value, 2), 2) }

val matrixEntries: List<CatalogEntry> =
    listOf(
        matrix.chart("matrix-simple", "简单的矩阵图", "Simple Matrix") {
            val x =
                dimension {
                    data =
                        arrayOf(
                            branch(
                                "A",
                                cell("A1"),
                                cell("A2"),
                                cell("A3", cell("A31"), cell("A32")),
                            ),
                        )
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        this.x = x
                        y = dimension { data = names("U", "V") }
                        top = StringOrDouble(150.0)
                        bottom = StringOrDouble(150.0)
                    }
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.continuous
                            min = 0.0
                            max = 80.0
                            top = StringOrDouble("middle")
                            dimension = 2.0
                            calculable = true
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        heatmapSeries {
                            coordinateSystem = "matrix"
                            typedData =
                                heatmapPoints(
                                    listOf(
                                        Triple("A1", "U", 10.0),
                                        Triple("A1", "V", 20.0),
                                        Triple("A2", "U", 30.0),
                                        Triple("A2", "V", 40.0),
                                        Triple("A31", "U", 50.0),
                                        Triple("A3", "V", 60.0),
                                    ),
                                )
                            label = unsafeJso<LabelOption> { show = true }
                        },
                    )
            }
        },
        matrix.chart("matrix-pie", "矩阵布局下的饼图", "Pie Charts in Matrix") {
            // 9 grades (two schools) by 6 classes; every cell holds the male/female split of a class.
            val random = sequence(20260601L)
            val pies =
                (0 until 9).flatMap { column ->
                    (0 until 6).map { rowIndex ->
                        pieSeries {
                            coordinateSystem = "matrix"
                            center =
                                arrayOf(
                                    StringOrDouble("Grade ${column + 1}"),
                                    StringOrDouble("Class ${rowIndex + 1}"),
                                )
                            radius = StringOrDoubleOrArray(18.0)
                            typedData =
                                dataItems(
                                    pieItem("Male", kotlin.math.round(random() * 10) + 10),
                                    pieItem("Female", kotlin.math.round(random() * 10) + 10),
                                )
                            label = unsafeJso<LabelOption> { show = false }
                            emphasis = unsafeJso<EmphasisOption> { label = unsafeJso { show = false } }
                        }
                    }
                }
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { show = true; bottom = StringOrDouble(40.0) })
                this.matrix =
                    matrixOption {
                        x =
                            dimension {
                                data =
                                    arrayOf(
                                        branch("Primary School", *(1..5).map { cell("Grade $it") }.toTypedArray()),
                                        branch("High School", *(6..9).map { cell("Grade $it") }.toTypedArray()),
                                    )
                            }
                        y = dimension { data = names(*(1..6).map { "Class $it" }.toTypedArray()) }
                        top = StringOrDouble(80.0)
                        bottom = StringOrDouble(80.0)
                    }
                series = pies.toTypedArray<SeriesOption>()
                tooltip = one(unsafeJso<TooltipOption> { show = true })
            }
        },
        matrix.chart("matrix-correlation-heatmap", "相关矩阵（热力图）", "Correlation Matrix (Heatmap)") {
            val random = sequence(20260602L)
            val points =
                (1..8).flatMap { x ->
                    (1..8).filter { y -> x >= y }.map { y ->
                        Triple("X$x", "Y$y", if (x == y) 1.0 else random() * 2 - 1)
                    }
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        x = dimension { data = names(*(1..8).map { "X$it" }.toTypedArray()) }
                        y = dimension { data = names(*(1..8).map { "Y$it" }.toTypedArray()) }
                        top = StringOrDouble(80.0)
                    }
                visualMap = valueVisualMap(-1.0, 1.0)
                series =
                    arrayOf<SeriesOption>(
                        heatmapSeries {
                            coordinateSystem = "matrix"
                            typedData = heatmapPoints(points)
                            label = unsafeJso<LabelOption> { show = true; formatter = twoDecimals() }
                        },
                    )
            }
        },
        matrix.chart("matrix-correlation-scatter", "相关矩阵（散点图）", "Correlation Matrix (Scatter)") {
            val random = sequence(20260603L)
            val points =
                (1..10).flatMap { x ->
                    (1..6).map { y -> Triple("X$x", "Y$y", random() * 2 - 1) }
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        x = dimension { data = names(*(1..10).map { "X$it" }.toTypedArray()) }
                        y = dimension { data = names(*(1..6).map { "Y$it" }.toTypedArray()) }
                        top = StringOrDouble(80.0)
                    }
                visualMap =
                    valueVisualMap(-1.0, 1.0) {
                        inRange =
                            unsafeJso<VisualOptionUnit> {
                                color =
                                    StringOrStringArray(
                                        arrayOf(
                                            "#313695", "#4575b4", "#74add1", "#abd9e9", "#e0f3f8",
                                            "#ffffbf", "#fee090", "#fdae61", "#f46d43", "#d73027", "#a50026",
                                        ),
                                    )
                                symbolSize = DoubleOrDoubleArray(arrayOf(15.0, 40.0))
                            }
                    }
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            coordinateSystem = "matrix"
                            typedData = scatterPoints(points)
                            itemStyle = unsafeJso<ItemStyleOption> { opacity = 1.0 }
                            label = unsafeJso<LabelOption> { show = true; formatter = twoDecimals() }
                        },
                    )
            }
        },
        matrix.chart("matrix-covariance", "协方差矩阵", "Covariance Matrix", height = "620px") {
            // The matrix header is two levels deep: five groups of five columns / rows, and the
            // 25x25 data below is addressed by the leaf cell names ("1" .. "25"). A flat header of
            // five cells leaves the heatmap without a cell for every point, which renders empty.
            fun grouped(prefix: String): Array<MatrixDimensionCellLooseOption> =
                Array(5) { group ->
                    branch(
                        "$prefix${group + 1}",
                        *Array(5) { index -> cell((group * 5 + index + 1).toString()) },
                    )
                }
            // The 25x25 grid is symmetric: the upper triangle is generated and mirrored below it.
            val random = sequence(20260604L)
            val values = HashMap<String, Double>()
            for (i in 1..25) {
                for (j in i..25) {
                    var base = if (i == j) 100.0 else 20.0
                    val iGroup = kotlin.math.ceil(i / 5.0)
                    val jGroup = kotlin.math.ceil(j / 5.0)
                    base += (3 - kotlin.math.abs(iGroup - jGroup)) * 35
                    if (i % 5 == j % 5) base += 20
                    if (random() > 0.9) base += random() * 40
                    values["${i}_${j}"] = (random() * 0.5 + 0.5) * base
                }
            }
            val points =
                (1..25).flatMap { i ->
                    (1..25).map { j ->
                        val value = if (i > j) values["${j}_${i}"]!! else values["${i}_${j}"]!!
                        Triple("$i", "$j", value)
                    }
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        x =
                            dimension {
                                data = grouped("X")
                                show = false
                            }
                        y =
                            dimension {
                                data = grouped("Y")
                                show = false
                            }
                        width = StringOrDouble(500.0)
                        height = StringOrDouble(500.0)
                        left = StringOrDouble("center")
                    }
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            show = true
                            valueFormatter =
                                TooltipValueFormatter { value, _ -> kotlin.math.round(numeric(value)).toInt().toString() }
                        },
                    )
                visualMap =
                    valueVisualMap(15.0, 120.0) {
                        inRange =
                            unsafeJso<VisualOptionUnit> {
                                color =
                                    StringOrStringArray(
                                        arrayOf(
                                            "#313695", "#4575b4", "#74add1", "#abd9e9", "#e0f3f8",
                                            "#ffffbf", "#fee090", "#fdae61", "#f46d43", "#d73027", "#a50026",
                                        ),
                                    )
                            }
                    }
                series =
                    arrayOf<SeriesOption>(
                        heatmapSeries {
                            coordinateSystem = "matrix"
                            typedData = heatmapPoints(points)
                        },
                    )
            }
        },
        matrix.chart("matrix-graph", "矩阵布局下的关系图", "Graph Chart in Matrix") {
            val links =
                listOf(1 to 0, 2 to 0, 3 to 0, 4 to 3, 4 to 2, 5 to 1, 6 to 3)
                    .map { (source, target) ->
                        unsafeJso<GraphEdgeItemOption> {
                            this.source = StringOrDouble(source.toDouble())
                            this.target = StringOrDouble(target.toDouble())
                        }
                    }
                    .toTypedArray()
            val courses =
                listOf(
                    listOf("Programming", "1st Year", "Intro to Computer Science"),
                    listOf("Data Analysis", "2nd Year", "Intro to Data Analysis"),
                    listOf("Algorithms", "2nd Year", "Intro to Algorithms"),
                    listOf("Programming", "2nd Year", "Advanced Programming"),
                    listOf("Algorithms", "4th Year", "Data Structures\nand Algorithms"),
                    listOf("Data Analysis", "3rd Year", "Statistics for Data Analysis"),
                    listOf("Programming", "3rd Year", "Software Development"),
                )
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<echarts.options.TitleOption> { text = "Course Prerequisites" })
                this.matrix =
                    matrixOption {
                        x = dimension { data = names("Data Analysis", "Programming", "Algorithms") }
                        y = dimension { data = names("1st Year", "2nd Year", "3rd Year", "4th Year") }
                        left = StringOrDouble(80.0)
                        right = StringOrDouble(80.0)
                        top = StringOrDouble(150.0)
                        bottom = StringOrDouble(150.0)
                    }
                series =
                    arrayOf<SeriesOption>(
                        graphSeries {
                            coordinateSystem = "matrix"
                            edgeSymbol = StringOrStringArray(arrayOf("none", "arrow"))
                            symbolSize = echarts.options.SymbolSize(15.0)
                            this.links = links
                            typedData =
                                dataItems(
                                    *courses.map { row(it[0], it[1], 1.0, it[2]) }
                                        .map { GraphDataItem(it) }
                                        .toTypedArray(),
                                )
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    formatter = LabelFormatter { params -> textAt(params.data, 3) }
                                    color = "#555"
                                    borderWidth = 0.0
                                    fontSize = StringOrDouble(15.0)
                                    fontWeight = ZRFontWeights.bold
                                    offset = arrayOf(0.0, -15.0)
                                    verticalAlign = web.cssom.VerticalAlign.bottom
                                }
                            lineStyle =
                                unsafeJso<LineStyleOption> {
                                    color = ZRColor("#9af")
                                    width = 2.0
                                    opacity = 1.0
                                }
                        },
                    )
                graphic =
                    arrayOf(
                        GraphicComponentLooseOption(
                            unsafeJso<GraphicComponentOption> {
                                elements =
                                    arrayOf(
                                        textElement("Course Categories", StringOrDouble("45%"), StringOrDouble(120.0), 0.0),
                                        textElement("Course Categories", StringOrDouble("8%"), StringOrDouble("55%"), 90.0),
                                    )
                            },
                        ),
                    )
            }
        },
        matrix.chart("matrix-confusion", "混淆矩阵", "Confusion Matrix") {
            val points =
                listOf(
                    Triple("Positive", "Positive", 10.0),
                    Triple("Positive", "Negative", 2.0),
                    Triple("Negative", "Positive", 3.0),
                    Triple("Negative", "Negative", 5.0),
                )
            val custom =
                unsafeJso<CustomSeriesOption> {
                    // Without the type ECharts drops the series and the cells stay empty.
                    type = echarts.SeriesTypes.custom
                    coordinateSystem = "matrix"
                    clip = false
                    typedData =
                        Array<OptionDataItem?>(points.size) { index ->
                            val (x, y, value) = points[index]
                            OptionDataItem(row(x, y, value))
                        }
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            formatter =
                                LabelFormatter { params ->
                                    val x = textAt(params.value, 0)
                                    val y = textAt(params.value, 1)
                                    val prefix = if (x == y) "True " else "False "
                                    "{name|$prefix$y}\n{value|${numberAt(params.value, 2).toInt()}}"
                                }
                            rich =
                                recordOf(
                                    "name" to
                                        unsafeJso<TextStyleOption> {
                                            color = "#fff"
                                            backgroundColor = ZRColor("#999")
                                            textBorderColor = "#333"
                                            padding = DoubleOrDoubleArray(5.0)
                                            fontSize = StringOrDouble(18.0)
                                        },
                                    "value" to
                                        unsafeJso<TextStyleOption> {
                                            color = "#444"
                                            textBorderWidth = 0.0
                                            padding = DoubleOrDoubleArray(5.0)
                                            fontSize = StringOrDouble(16.0)
                                            align = TextAlign.center
                                        },
                                ).unsafeCast<Record<String, TextStyleOption>>()
                        }
                    renderItem = { _, api ->
                        val x = api.value(DimensionLoose(0.0))
                        val y = api.value(DimensionLoose(1.0))
                        val rect = api.layout(CoordinateSystemDataCoord(arrayOf(x, y))).rect
                        unsafeJso<CustomPathOption> {
                            type = "rect"
                            shape =
                                unsafeJso<ZRShapeProps> {
                                    this.x = rect?.x
                                    this.y = rect?.y
                                    width = rect?.width
                                    height = rect?.height
                                }
                            style =
                                api.style(
                                    unsafeJso<ZRStyleProps> {
                                        fill = ZRColor(if (textOf(x) == textOf(y)) "#8f8" else "#f88")
                                    },
                                )
                        }
                    }
                }
            unsafeJso<EChartsOption> {
                this.matrix =
                    matrixOption {
                        x =
                            dimension {
                                data = names("Positive", "Negative")
                                label = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0); color = "#555" }
                            }
                        y =
                            dimension {
                                data = names("Positive", "Negative")
                                label = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0); color = "#555" }
                            }
                        top = StringOrDouble(80.0)
                        width = StringOrDouble(600.0)
                        left = StringOrDouble("center")
                    }
                series = arrayOf<SeriesOption>(custom)
                graphic =
                    arrayOf(
                        GraphicComponentLooseOption(
                            unsafeJso<GraphicComponentOption> {
                                elements =
                                    arrayOf(
                                        textElement("True Class", StringOrDouble("70%"), StringOrDouble(20.0), 0.0),
                                        textElement(
                                            "Predicted Class",
                                            StringOrDouble("10%"),
                                            StringOrDouble("50%"),
                                            90.0,
                                        ),
                                    )
                            },
                        ),
                    )
            }
        },
        matrix.chart(
            "matrix-mini-bar-data-collection",
            "矩阵坐标系表头数据自动收集（以微型条形图为例）",
            "Matrix Header Data Collection (Mini Bar)",
        ) {
            unsafeJso<EChartsOption> {
                dataset =
                    one(
                        unsafeJso<DatasetOption> {
                            source =
                                datasetArrayRows(
                                    arrayOf(
                                        row("2021-02-01", "amount", 1212.0, "file", 2321.0, "Q", 1412.0),
                                        row("2021-02-02", "amount", 7181.0, "file", 2114.0, "Q", 1402.0),
                                        row("2021-02-03", "amount", 2763.0, "file", 4212.0, "Q", 8172.0),
                                        row("2021-02-04", "amount", 6122.0, "file", 2942.0, "Q", 6121.0),
                                        row("2021-02-05", "amount", 4221.0, "file", 3411.0, "Q", 1987.0),
                                        row("2021-02-06", "amount", 7221.0, "file", 5121.0, "Q", 1303.0),
                                        row("2021-02-07", "amount", 5121.0, "file", 4121.0, "Q", 1819.0),
                                        row("2021-02-08", "amount", 6121.0, "file", 3121.0, "Q", 2303.0),
                                        row("2021-02-09", "amount", 7121.0, "file", 2121.0, "Q", 3303.0),
                                        row("2021-02-10", "amount", 8121.0, "file", 1121.0, "Q", 4303.0),
                                    ).unsafeCast<Array<Array<OptionDataValue>>>(),
                                )
                        },
                    )
                this.matrix =
                    matrixOption {
                        // `x` / `y` data are collected from the dataset dimensions named by each
                        // series' `encode`; only the layout of the header is set here.
                        x =
                            dimension {
                                levelSize = StringOrDouble(50.0)
                                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#f0f8ff") }
                                label = unsafeJso<LabelOption> { fontWeight = ZRFontWeights.bold }
                            }
                    }
                series =
                    arrayOf<SeriesOption>(
                        miniBarSeries(1, 0, 2),
                        miniBarSeries(3, 0, 4),
                        miniBarSeries(5, 0, 6),
                    )
            }
        },
    )

/** One `custom` series of the mini-bar example, placed on the matrix cell of `encode.x` / `encode.y`. */
private fun miniBarSeries(xDim: Int, yDim: Int, valueDim: Int): CustomSeriesOption =
    customSeries {
        coordinateSystem = "matrix"
        encode =
            unsafeJso<OptionEncode> {
                x = OptionEncodeValue(DimensionLoose(xDim.toDouble()))
                y = OptionEncodeValue(DimensionLoose(yDim.toDouble()))
            }
        renderItem = miniBarRenderItem(xDim, yDim, valueDim)
    }

/** The value extent the mini bars are scaled to, as in the original example. */
private val miniBarExtent = doubleArrayOf(0.0, 10000.0)

/** Draws one mini bar plus its value next to the bar, inside the cell the layout returns. */
private fun miniBarRenderItem(xDim: Int, yDim: Int, valueDim: Int): CustomSeriesRenderItem =
    { _, api ->
        val xValue = api.value(DimensionLoose(xDim.toDouble()))
        val yValue = api.value(DimensionLoose(yDim.toDouble()))
        val labelValue = api.value(DimensionLoose(valueDim.toDouble())).unsafeCast<Double?>()
        val rect = api.layout(CoordinateSystemDataCoord(arrayOf(xValue, yValue))).rect
        if (rect == null || labelValue == null) {
            null
        } else {
            val rectY = rect.y!!
            val rectHeight = rect.height!!
            val rectWidth = rect.width!!
            val height = rectHeight * 0.2
            val barY = rectY + (rectHeight - height) / 4 * 3
            val barX = rect.x!! + rectWidth * 0.15
            val width = linearMap(labelValue, miniBarExtent, doubleArrayOf(0.0, rectWidth * 0.5))
            unsafeJso<CustomGroupOption> {
                type = "group"
                children =
                    arrayOf(
                        unsafeJso<CustomPathOption> {
                            type = "rect"
                            shape =
                                unsafeJso<ZRShapeProps> {
                                    x = barX
                                    y = barY
                                    this.width = width
                                    this.height = height
                                }
                            style = api.style(unsafeJso<ZRStyleProps> { fill = ZRColor("#0ca8df") })
                        },
                        unsafeJso<CustomTextOption> {
                            type = "text"
                            x = barX
                            y = rectY + rectHeight / 4 * 1.5
                            style =
                                unsafeJso<ZRStyleProps> {
                                    text = valueText(labelValue)
                                    fill = ZRColor("#333")
                                    align = web.cssom.TextAlign.left
                                    verticalAlign = web.cssom.VerticalAlign.middle
                                }
                        },
                    )
            }
        }
    }

/** Maps [value] from [domain] onto [range], the way the original `linearMap` helper does. */
private fun linearMap(value: Double, domain: DoubleArray, range: DoubleArray): Double {
    val subDomain = domain[1] - domain[0]
    val subRange = range[1] - range[0]
    return when {
        subDomain == 0.0 -> if (subRange == 0.0) range[0] else (range[0] + range[1]) / 2
        value == domain[0] -> range[0]
        value == domain[1] -> range[1]
        else -> (value - domain[0]) / subDomain * subRange + range[0]
    }
}

/**
 * A `text` element of a `graphic` component, optionally rotated in degrees.
 *
 * The position goes through `left` / `top` rather than `x` / `y`: the latter is the raw zrender
 * prop, which takes a number only, so a percentage string would land the element at the origin.
 */
private fun textElement(
    text: String,
    left: StringOrDouble,
    top: StringOrDouble,
    rotation: Double,
): echarts.options.GraphicElement =
    unsafeJso {
        type = "text"
        this.left = left
        this.top = top
        this.rotation = rotation
        style =
            unsafeJso {
                this.text = text
                fill = ZRColor("#333")
                font = "bold 24px serif"
                align = web.cssom.TextAlign.center
            }
    }
