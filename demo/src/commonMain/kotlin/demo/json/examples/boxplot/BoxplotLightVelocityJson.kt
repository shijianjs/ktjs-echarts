package demo.json.examples.boxplot

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.MICHELSON_SUMMARIES
import demo.json.examples.shared.boxRows
import demo.json.examples.shared.boxplotTooltip
import demo.json.examples.shared.michelsonTitles
import demo.json.examples.shared.outlierPoints

private val boxplotLightVelocityJsonCategory = JsonCategory("json/examples/boxplot/BoxplotLightVelocityJson.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val boxplotLightVelocityJsonExample: JsonExample =
    // ------------------------------------------------------------------ box plot
        boxplotLightVelocityJsonCategory.example("boxplot-light-velocity") {
            val summaries = MICHELSON_SUMMARIES
            michelsonTitles(normalWeight = true)
            boxplotTooltip()
            grid {
                left = str("10%")
                right = str("10%")
                bottom = str("15%")
            }
            xAxis {
                type = AxisType.Category
                categories("expr 0", "expr 1", "expr 2", "expr 3", "expr 4")
                boundaryGap = BoundaryGap.of(true)
                nameGap = 30.0
                splitArea { show = false }
                splitLine { show = false }
            }
            yAxis {
                type = AxisType.Value
                name = "km/s minus 299,000"
                splitArea { show = true }
            }
            series {
                boxplot {
                    name = "boxplot"
                    data = boxRows(summaries)
                }
                scatter {
                    name = "outlier"
                    data = outlierPoints(summaries, swapped = false)
                }
            }
        }
