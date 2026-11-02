package demo.json.examples.scatter

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonPrimitive
import demo.json.examples.shared.SINGLE_AXIS_DAYS
import demo.json.examples.shared.SINGLE_AXIS_HOURS
import demo.json.examples.shared.singleAxisPoints
import demo.json.*

private val scatterSingleAxisJsonCategory = JsonCategory("json/examples/scatter/ScatterSingleAxisJson.kt")

val scatterSingleAxisJsonExample: JsonExample =
    scatterSingleAxisJsonCategory.example("scatter-single-axis") {
            tooltip { position = JsonPrimitive("top") }
            val seriesOptions = mutableListOf<SeriesOption>()
            for (index in SINGLE_AXIS_DAYS.indices) {
                title {
                    textVerticalAlign = "middle"
                    top = str("${((index + 0.5) * 100) / 7}%")
                    text = SINGLE_AXIS_DAYS[index]
                }
                singleAxis {
                    left = num(150)
                    type = AxisType.Category
                    boundaryGap = BoundaryGap.of(false)
                    categories(*SINGLE_AXIS_HOURS)
                    top = str("${(index * 100.0) / 7.0 + 5.0}%")
                    height = str("${100.0 / 7.0 - 10.0}%")
                    axisLabel { interval = num(2) }
                }
                seriesOptions +=
                    SeriesOption(SeriesType.Scatter).apply {
                        singleAxisIndex = index
                        coordinateSystem = "singleAxis"
                        data = singleAxisPoints(index)
                    }
            }
            series = seriesOptions
        }
