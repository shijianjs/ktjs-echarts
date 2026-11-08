package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.AXIS_LABEL_POINTS
import demo.json.*

private val docExampleAxisLabelAlignMinMaxJsonCategory = JsonCategory("json/examples/doc/DocExampleAxisLabelAlignMinMaxJson.kt")

val docExampleAxisLabelAlignMinMaxJsonExample: JsonExample =
    docExampleAxisLabelAlignMinMaxJsonCategory.example("doc-example/axis-label-align-min-max") {
            xAxis {
                axisLabel {
                    formatter = "label of {value}"
                    showMinLabel = true
                    showMaxLabel = true
                }
            }
            yAxis { }
            series {
                line { data = AXIS_LABEL_POINTS }
            }
            dataZoom {
                type = DataZoomType.Slider
                start = 20.0
                end = 42.0
            }
        }
