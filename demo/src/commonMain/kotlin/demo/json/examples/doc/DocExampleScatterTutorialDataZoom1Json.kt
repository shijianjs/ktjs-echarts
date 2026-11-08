package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.SCATTER_TUTORIAL
import demo.json.*

private val docExampleScatterTutorialDataZoom1JsonCategory = JsonCategory("json/examples/doc/DocExampleScatterTutorialDataZoom1Json.kt")

val docExampleScatterTutorialDataZoom1JsonExample: JsonExample =
    docExampleScatterTutorialDataZoom1JsonCategory.example("doc-example/scatter-tutorial-dataZoom-1") {
            xAxis { type = AxisType.Value }
            yAxis { type = AxisType.Value }
            dataZoom {
                type = DataZoomType.Slider
                start = 1.0
                end = 35.0
            }
            series {
                scatter {
                    name = "scatter"
                    itemStyle { opacity = 0.8 }
                    data = SCATTER_TUTORIAL
                }
            }
        }
