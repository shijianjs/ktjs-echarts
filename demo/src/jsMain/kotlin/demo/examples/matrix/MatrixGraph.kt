package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.GraphDataItem
import echarts.options.GraphEdgeItemOption
import echarts.options.GraphicComponentLooseOption
import echarts.options.GraphicComponentOption
import echarts.options.LabelFormatter
import echarts.options.LabelOption
import echarts.options.LineStyleOption
import echarts.options.SymbolSize
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.dataItems
import echarts.options.graphSeries
import echarts.options.one
import js.objects.unsafeJso
import zrender.ZRFontWeights
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.row
import demo.examples.shared.textAt

private val matrixGraphCategory = Category(Groups.MATRIX, "examples/matrix/MatrixGraph.kt")

val matrixGraphEntry: CatalogEntry =
    matrixGraphCategory.chart("matrix-graph", "矩阵布局下的关系图", "Graph Chart in Matrix") {
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
        }
