package demo.json.examples.parallel

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.AQI_BEIJING
import demo.json.examples.shared.AQI_GUANGZHOU
import demo.json.examples.shared.AQI_SHANGHAI
import demo.json.*

private val parallelAqiJsonCategory = JsonCategory("json/examples/parallel/ParallelAqiJson.kt")

val parallelAqiJsonExample: JsonExample =
    parallelAqiJsonCategory.example("parallel-aqi") {
            backgroundColor = "#333".asColor()
            legend {
                bottom = num(30)
                entries("Beijing", "Shanghai", "Guangzhou")
                itemGap = 20.0
                textStyle {
                    color = "#fff".asColor()
                    fontSize = 14.0
                }
            }
            tooltip {
                padding = NumberOrStringOrNumbers.of(10)
                backgroundColor = "#222".asColor()
                borderColor = "#777".asColor()
                borderWidth = 1.0
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
            visualMap {
                show = true
                min = 0.0
                max = 150.0
                dimension = num(2)
                inRange { color = colors("#50a3ba", "#eac736", "#d94e5d") }
            }
            parallel {
                left = str("5%")
                right = str("18%")
                bottom = num(100)
                parallelAxisDefault =
                    AxisOption().apply {
                        type = AxisType.Value
                        name = "AQI指数"
                        nameLocation = "end"
                        nameGap = 20.0
                        nameTextStyle {
                            color = "#fff".asColor()
                            fontSize = 12.0
                        }
                        axisLine {
                            lineStyle = LineStyleOption().apply { color = "#aaa".asColor() }
                        }
                        axisTick {
                            lineStyle = LineStyleOption().apply { color = "#777".asColor() }
                        }
                        splitLine { show = false }
                        axisLabel { color = "#fff".asColor() }
                    }
            }
            series {
                parallel {
                    name = "Beijing"
                    lineStyle {
                        width = 1.0
                        opacity = 0.5
                    }
                    data = AQI_BEIJING
                }
                parallel {
                    name = "Shanghai"
                    lineStyle {
                        width = 1.0
                        opacity = 0.5
                    }
                    data = AQI_SHANGHAI
                }
                parallel {
                    name = "Guangzhou"
                    lineStyle {
                        width = 1.0
                        opacity = 0.5
                    }
                    data = AQI_GUANGZHOU
                }
            }
        }
