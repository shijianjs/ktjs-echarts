package demo.json.examples.boxplot

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.MULTI_GROUPS
import demo.json.examples.shared.MULTI_SUMMARIES
import demo.json.examples.shared.boxRows
import demo.json.examples.shared.boxplotTooltip

private val boxplotMultiJsonCategory = JsonCategory("json/examples/boxplot/BoxplotMultiJson.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val boxplotMultiJsonExample: JsonExample =
    boxplotMultiJsonCategory.example("boxplot-multi") {
            val summaries = MULTI_SUMMARIES
            title {
                text = "Multiple Categories"
                left = str("center")
            }
            legend { top = str("10%") }
            boxplotTooltip()
            grid {
                left = str("10%")
                top = str("20%")
                right = str("10%")
                bottom = str("15%")
            }
            xAxis {
                type = AxisType.Category
                categories(*(0 until MULTI_GROUPS).map { it.toString() }.toTypedArray())
                boundaryGap = BoundaryGap.of(true)
                nameGap = 30.0
                splitArea { show = true }
                splitLine { show = false }
            }
            yAxis {
                type = AxisType.Value
                name = "Value"
                min = num(-400)
                max = num(600)
                splitArea { show = false }
            }
            dataZoom {
                type = DataZoomType.Inside
                start = 0.0
                end = 20.0
            }
            dataZoom {
                show = true
                type = DataZoomType.Slider
                top = str("90%")
                xAxisIndex = listOf(0)
                start = 0.0
                end = 20.0
            }
            series {
                summaries.forEachIndexed { index, table ->
                    boxplot {
                        name = "category$index"
                        data = boxRows(table)
                    }
                }
            }
        }
