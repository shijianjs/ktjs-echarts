package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.AQI_BJ
import demo.json.examples.shared.AQI_GZ
import demo.json.examples.shared.AQI_SH
import demo.json.*

private val docExampleScatterVisualMapCategoriesJsonCategory = JsonCategory("json/examples/doc/DocExampleScatterVisualMapCategoriesJson.kt")

val docExampleScatterVisualMapCategoriesJsonExample: JsonExample =
    docExampleScatterVisualMapCategoriesJsonCategory.example("doc-example/scatter-visualMap-categories") {
            color = colors("#dd4444", "#fec42c", "#80F1BE")
            legend {
                top = num(10)
                entries("北京", "上海", "广州")
            }
            grid {
                left = str("10%")
                right = num(200)
                top = str("15%")
                bottom = str("10%")
            }
            tooltip {
                padding = NumberOrStringOrNumbers.of(10)
                borderColor = "#777".asColor()
                borderWidth = 1.0
            }
            xAxis {
                type = AxisType.Value
                name = "日期"
                nameGap = 16.0
                nameTextStyle { fontSize = 14.0 }
                max = num(31)
                splitLine { show = false }
                axisLabel { formatter = "{value}" }
            }
            yAxis {
                type = AxisType.Value
                name = "AQI指数"
                nameLocation = "end"
                nameGap = 20.0
                nameTextStyle { fontSize = 20.0 }
                splitLine { show = false }
            }
            series {
                scatter {
                    name = "北京"
                    data = AQI_BJ
                    itemStyle {
                        opacity = 0.8
                        shadowBlur = 10.0
                        shadowOffsetX = 0.0
                        shadowOffsetY = 0.0
                        shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                    }
                }
                scatter {
                    name = "上海"
                    data = AQI_SH
                    itemStyle {
                        opacity = 0.8
                        shadowBlur = 10.0
                        shadowOffsetX = 0.0
                        shadowOffsetY = 0.0
                        shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                    }
                }
                scatter {
                    name = "广州"
                    data = AQI_GZ
                    itemStyle {
                        opacity = 0.8
                        shadowBlur = 10.0
                        shadowOffsetX = 0.0
                        shadowOffsetY = 0.0
                        shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                    }
                }
            }
        }
