package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.AQI_BJ
import demo.json.examples.shared.AQI_GZ
import demo.json.examples.shared.AQI_SH
import demo.json.*

private val docExampleParallelAllJsonCategory = JsonCategory("json/examples/doc/DocExampleParallelAllJson.kt")

val docExampleParallelAllJsonExample: JsonExample =
    docExampleParallelAllJsonCategory.example("doc-example/parallel-all") {
            color = colors("#c23531", "#91c7ae", "#dd8668")
            legend {
                top = num(10)
                itemGap = 20.0
                entries("北京", "上海", "广州")
            }
            parallelAxis {
                name = "日期"
                inverse = true
                max = num(31)
                nameLocation = "start"
            }
            parallelAxis { name = "AQI" }
            parallelAxis { name = "PM2.5" }
            parallelAxis { name = "PM10" }
            parallelAxis { name = " CO" }
            parallelAxis { name = "NO2" }
            parallelAxis { name = "SO2" }
            parallelAxis {
                name = "等级"
                type = AxisType.Category
                categories("优", "良", "轻度污染", "中度污染", "重度污染", "严重污染")
            }
            parallel {
                left = str("5%")
                right = str("13%")
                bottom = str("10%")
                top = str("20%")
                parallelAxisDefault =
                    AxisOption().apply {
                        type = AxisType.Value
                        name = "AQI指数"
                        nameLocation = "end"
                        nameGap = 20.0
                        nameTextStyle { fontSize = 12.0 }
                    }
            }
            series {
                parallel {
                    name = "北京"
                    lineStyle {
                        width = 1.0
                        opacity = 0.5
                    }
                    data = AQI_BJ
                }
                parallel {
                    name = "上海"
                    lineStyle {
                        width = 1.0
                        opacity = 0.5
                    }
                    data = AQI_SH
                }
                parallel {
                    name = "广州"
                    lineStyle {
                        width = 1.0
                        opacity = 0.5
                    }
                    data = AQI_GZ
                }
            }
        }
