package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category

private val scatter3dScatterCategory = Category(Groups.GL, "examples/gl/Scatter3dScatter.kt")

val scatter3dScatterEntry: CatalogEntry =
    scatter3dScatterCategory.interactive(
            "scatter3d-scatter",
            "三维散点图和散点矩阵结合使用",
            "3D Scatter with Scatter Matrix",
            "左边一张三维散点图，右边四张二维散点图，同样是收入、预期寿命、人口三列的不同组合。",
            "A 3D scatter on the left and four 2D scatters on the right, each showing another pair of " +
                "the income, life expectancy and population columns.",
        ) { chart ->
            loadJson("data/life-expectancy-table.json").then { json ->
                val size = "57%"
                chart.setOption(
                    unsafeJso<EChartsGlOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        grid3D = one(unsafeJso<Grid3DOption> { width = StringOrDouble("50%") })
                        xAxis3D = one(unsafeJso<Axis3DOption> {})
                        yAxis3D = one(unsafeJso<Axis3DOption> {})
                        zAxis3D = one(unsafeJso<Axis3DOption> {})
                        grid =
                            arrayOf(
                                matrixGrid(left = "50%", width = "20%", bottom = size),
                                matrixGrid(left = "75%", width = "20%", bottom = size),
                                matrixGrid(left = "50%", width = "20%", top = size),
                                matrixGrid(left = "75%", width = "20%", top = size),
                            )
                        xAxis =
                            arrayOf(
                                panelXAxis(0.0, "Income"),
                                panelXAxis(1.0, "Country", category = true),
                                panelXAxis(2.0, "Income"),
                                panelXAxis(3.0, "Life Expectancy"),
                            )
                        yAxis =
                            arrayOf(
                                panelYAxis(0.0, "Life Expectancy"),
                                panelYAxis(1.0, "Income"),
                                panelYAxis(2.0, "Population"),
                                panelYAxis(3.0, "Population"),
                            )
                        dataset = one(lifeExpectancyDataset(json))
                        series =
                            arrayOf<SeriesOption>(
                                scatter3DSeries {
                                    symbolSize = SymbolSize(3.0)
                                    encode = encode3("Population", "Life Expectancy", "Income")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 0.0
                                    yAxisIndex = 0.0
                                    encode = encode2("Income", "Life Expectancy")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 1.0
                                    yAxisIndex = 1.0
                                    encode = encode2("Country", "Income")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 2.0
                                    yAxisIndex = 2.0
                                    encode = encode2("Income", "Population")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 3.0
                                    yAxisIndex = 3.0
                                    encode = encode2("Life Expectancy", "Population")
                                },
                            )
                    },
                )
                null
            }
        }
