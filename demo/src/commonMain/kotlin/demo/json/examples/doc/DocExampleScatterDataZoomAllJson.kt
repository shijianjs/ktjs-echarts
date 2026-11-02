package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.scatterCloud
import demo.json.*

private val docExampleScatterDataZoomAllJsonCategory = JsonCategory("json/examples/doc/DocExampleScatterDataZoomAllJson.kt")

val docExampleScatterDataZoomAllJsonExample: JsonExample =
    docExampleScatterDataZoomAllJsonCategory.example("doc-example/scatter-dataZoom-all") {
            animation = false
            legend { entries("scatter", "scatter2", "scatter3") }
            tooltip { }
            xAxis {
                type = AxisType.Value
                min = str("dataMin")
                max = str("dataMax")
                splitLine { show = true }
            }
            yAxis {
                type = AxisType.Value
                min = str("dataMin")
                max = str("dataMax")
                splitLine { show = true }
            }
            dataZoom {
                type = DataZoomType.Slider
                show = true
                xAxisIndex = listOf(0)
                start = 1.0
                end = 35.0
            }
            dataZoom {
                type = DataZoomType.Slider
                show = true
                yAxisIndex = listOf(0)
                left = str("93%")
                start = 29.0
                end = 36.0
            }
            dataZoom {
                type = DataZoomType.Inside
                xAxisIndex = listOf(0)
                start = 1.0
                end = 35.0
            }
            dataZoom {
                type = DataZoomType.Inside
                yAxisIndex = listOf(0)
                start = 29.0
                end = 36.0
            }
            series {
                scatter {
                    name = "scatter"
                    itemStyle { opacity = 0.8 }
                    data = scatterCloud(15.0, 20261024L)
                }
                scatter {
                    name = "scatter2"
                    itemStyle { opacity = 0.8 }
                    data = scatterCloud(10.0, 20261025L)
                }
                scatter {
                    name = "scatter3"
                    itemStyle { opacity = 0.8 }
                    data = scatterCloud(15.0, 20261026L)
                }
            }
        }
