package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.custom.coord
import demo.examples.matrix.cell
import demo.examples.matrix.dimension
import demo.examples.matrix.matrixOption
import demo.examples.matrix.names

private val docExampleMatrixGraphBoxCategory = Category(Groups.DOC, "examples/doc/DocExampleMatrixGraphBox.kt")

val docExampleMatrixGraphBoxEntry: CatalogEntry =
    docExampleMatrixGraphBoxCategory.chart(
            "doc-example/matrix-graph-box",
            "矩阵坐标系上的关系图（box 用法）",
            "Graph on a matrix (box usage)",
            "关系图把 `coordinateSystemUsage` 设为 `\"box\"`、`coord` 设为一个单元格，节点与连线就画在" +
                "该单元格的盒子里，坐标即盒子的局部坐标。",
            "A graph placed on one matrix cell: with `coordinateSystemUsage: \"box\"` and `coord` naming " +
                "the cell, the nodes and links are drawn inside the box of that cell.",
        ) {
            unsafeJso<EChartsOption> {
                matrix =
                    matrixOption {
                        x = dimension { levelSize = StringOrDouble(50.0); data = names("a", "b", "c", "d") }
                        y = dimension { levelSize = StringOrDouble(50.0); data = names("x", "y", "z") }
                    }
                series =
                    arrayOf<SeriesOption>(
                        graphSeries {
                            coordinateSystem = "matrix"
                            coordinateSystemUsage = CoordinateSystemUsages.box
                            coord = CoordinateSystemDataCoord(arrayOf(OptionDataValue(2.0), OptionDataValue(1.0)))
                            edgeSymbol = StringOrStringArray(arrayOf("none", "arrow"))
                            symbolSize = SymbolSize(15.0)
                            links =
                                arrayOf(
                                    graphEdge(1.0, 0.0),
                                    graphEdge(2.0, 0.0),
                                    graphEdge(3.0, 0.0),
                                    graphEdge(4.0, 3.0),
                                    graphEdge(4.0, 2.0),
                                    graphEdge(5.0, 1.0),
                                    graphEdge(6.0, 3.0),
                                )
                            typedData =
                                arrayOf(
                                    matrixNode("a", 110.0, 100.0, 134.0),
                                    matrixNode("b", 270.0, 10.0, 421.0),
                                    matrixNode("c", 330.0, 100.0, 152.0),
                                    matrixNode("d", 401.0, 500.0, 216.0),
                                    matrixNode("e", 530.0, 200.0, 155.0),
                                    matrixNode("f", 610.0, 140.0, 212.0),
                                    matrixNode("g", 780.0, 70.0, 161.0),
                                )
                            lineStyle =
                                unsafeJso<LineStyleOption> {
                                    color = ZRColor("#9af")
                                    width = 2.0
                                    opacity = 1.0
                                }
                        },
                    )
            }
        }
