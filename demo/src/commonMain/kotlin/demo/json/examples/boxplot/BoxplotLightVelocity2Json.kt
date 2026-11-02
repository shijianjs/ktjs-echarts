package demo.json.examples.boxplot

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.MICHELSON_SUMMARIES
import demo.json.examples.shared.boxRows
import demo.json.examples.shared.boxplotTooltip
import demo.json.examples.shared.michelsonTitles
import demo.json.examples.shared.outlierPoints

private val boxplotLightVelocity2JsonCategory = JsonCategory("json/examples/boxplot/BoxplotLightVelocity2Json.kt")

// ---------------------------------------------------------------------------------------------
// Dates and generated prices
// ---------------------------------------------------------------------------------------------

val boxplotLightVelocity2JsonExample: JsonExample =
    boxplotLightVelocity2JsonCategory.example("boxplot-light-velocity2") {
            val summaries = MICHELSON_SUMMARIES
            michelsonTitles(normalWeight = false)
            boxplotTooltip()
            grid {
                left = str("10%")
                right = str("10%")
                bottom = str("15%")
            }
            yAxis {
                type = AxisType.Category
                categories("0", "1", "2", "3", "4")
                boundaryGap = BoundaryGap.of(true)
                nameGap = 30.0
                splitArea { show = false }
                splitLine { show = false }
            }
            xAxis {
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
                    data = outlierPoints(summaries, swapped = true)
                }
            }
        }
