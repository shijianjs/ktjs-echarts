package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.SCATTER_TUTORIAL
import demo.json.*

private val docExampleScatterTutorialDataZoom2JsonCategory = JsonCategory("json/examples/doc/DocExampleScatterTutorialDataZoom2Json.kt")

val docExampleScatterTutorialDataZoom2JsonExample: JsonExample =
    docExampleScatterTutorialDataZoom2JsonCategory.example("doc-example/scatter-tutorial-dataZoom-2") {
            xAxis { type = AxisType.Value }
            yAxis { type = AxisType.Value }
            dataZoom {
                type = DataZoomType.Slider
                start = 1.0
                end = 35.0
            }
            dataZoom {
                type = DataZoomType.Inside
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
