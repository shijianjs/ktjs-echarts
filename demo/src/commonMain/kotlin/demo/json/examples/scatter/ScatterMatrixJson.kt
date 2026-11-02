package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val scatterMatrixJsonCategory = JsonCategory("json/examples/scatter/ScatterMatrixJson.kt")

val scatterMatrixJsonExample: JsonExample =
    scatterMatrixJsonCategory.example("scatter-matrix") {
            val cells = matrixCells()
            animation = false
            tooltip { trigger = TooltipTrigger.Item }
            parallelAxis =
                MATRIX_AXES.map { axisName ->
                    AxisOption().apply {
                        type = AxisType.Value
                        name = axisName
                        nameLocation = "end"
                        nameGap = 20.0
                        splitNumber = 3
                        nameTextStyle = TextStyleOption().apply { fontSize = 14.0 }
                        axisLine =
                            AxisLineOption().apply {
                                lineStyle = LineStyleOption().apply { color = "#555".asColor() }
                            }
                        axisTick =
                            AxisTickOption().apply {
                                lineStyle = LineStyleOption().apply { color = "#555".asColor() }
                            }
                        splitLine { show = false }
                        axisLabel { color = "#555".asColor() }
                    }
                } +
                    AxisOption().apply {
                        type = AxisType.Category
                        name = "等级"
                        categories(*MATRIX_GRADES)
                    }
            parallel {
                bottom = str("5%")
                left = str("2%")
                height = str("30%")
                width = str("55%")
                parallelAxisDefault =
                    AxisOption().apply {
                        type = AxisType.Value
                        name = "AQI指数"
                        nameLocation = "end"
                        nameGap = 20.0
                        splitNumber = 3
                        nameTextStyle = TextStyleOption().apply { fontSize = 14.0 }
                        axisLine =
                            AxisLineOption().apply {
                                lineStyle = LineStyleOption().apply { color = "#555".asColor() }
                            }
                        axisTick =
                            AxisTickOption().apply {
                                lineStyle = LineStyleOption().apply { color = "#555".asColor() }
                            }
                        splitLine { show = false }
                        axisLabel { color = "#555".asColor() }
                    }
            }
            grid = cells.grids
            xAxis = cells.xAxes
            yAxis = cells.yAxes
            series =
                listOf(
                    SeriesOption(SeriesType.Parallel).apply {
                        name = "parallel"
                        smooth = BooleanOrNumber.of(true)
                        lineStyle = LineStyleOption().apply {
                            width = 1.0
                            opacity = 0.3
                        }
                        data = parallelItems()
                    },
                ) + cells.series
        }
