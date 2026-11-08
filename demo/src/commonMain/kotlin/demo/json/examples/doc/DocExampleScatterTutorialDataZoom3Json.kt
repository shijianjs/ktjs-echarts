package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.SCATTER_TUTORIAL
import demo.json.*

private val docExampleScatterTutorialDataZoom3JsonCategory = JsonCategory("json/examples/doc/DocExampleScatterTutorialDataZoom3Json.kt")

val docExampleScatterTutorialDataZoom3JsonExample: JsonExample =
    docExampleScatterTutorialDataZoom3JsonCategory.example("doc-example/scatter-tutorial-dataZoom-3") {
            xAxis { type = AxisType.Value }
            yAxis { type = AxisType.Value }
            dataZoom {
                type = DataZoomType.Slider
                xAxisIndex = listOf(0)
                start = 10.0
                end = 60.0
            }
            dataZoom {
                type = DataZoomType.Inside
                xAxisIndex = listOf(0)
                start = 10.0
                end = 60.0
            }
            dataZoom {
                type = DataZoomType.Slider
                yAxisIndex = listOf(0)
                start = 30.0
                end = 80.0
            }
            dataZoom {
                type = DataZoomType.Inside
                yAxisIndex = listOf(0)
                start = 30.0
                end = 80.0
            }
            series {
                scatter {
                    name = "scatter"
                    itemStyle { opacity = 0.8 }
                    data = SCATTER_TUTORIAL
                }
            }
        }
