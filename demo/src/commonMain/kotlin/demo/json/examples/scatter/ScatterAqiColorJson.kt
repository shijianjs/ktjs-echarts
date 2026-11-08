package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val scatterAqiColorJsonCategory = JsonCategory("json/examples/scatter/ScatterAqiColorJson.kt")

val scatterAqiColorJsonExample: JsonExample =
    scatterAqiColorJsonCategory.example("scatter-aqi-color") {
            color = colors("#dd4444", "#fec42c", "#80F1BE")
            legend {
                top = num(10)
                entries("北京", "上海", "广州")
                textStyle { fontSize = 16.0 }
            }
            grid {
                left = str("10%")
                right = num(150)
                top = str("18%")
                bottom = str("10%")
            }
            tooltip { backgroundColor = "rgba(255,255,255,0.7)".asColor() }
            xAxis {
                type = AxisType.Value
                name = "日期"
                nameGap = 16.0
                nameTextStyle { fontSize = 16.0 }
                max = num(31)
                splitLine { show = false }
            }
            yAxis {
                type = AxisType.Value
                name = "AQI指数"
                nameLocation = "end"
                nameGap = 20.0
                nameTextStyle { fontSize = 16.0 }
                splitLine { show = false }
            }
            visualMap {
                left = str("right")
                top = str("10%")
                dimension = num(2)
                min = 0.0
                max = 250.0
                itemWidth = 30.0
                itemHeight = 120.0
                calculable = true
                text = listOf("圆形大小：PM2.5")
                textGap = 30.0
                inRange { symbolSize = listOf(10.0, 70.0) }
                outOfRange {
                    symbolSize = listOf(10.0, 70.0)
                    color = listOf("rgba(255,255,255,0.4)".asColor())
                }
            }
            visualMap {
                left = str("right")
                bottom = str("5%")
                dimension = num(6)
                min = 0.0
                max = 50.0
                itemHeight = 120.0
                text = listOf("明暗：二氧化硫")
                textGap = 30.0
                inRange { colorLightness = listOf(0.9, 0.5) }
                outOfRange { color = listOf("rgba(255,255,255,0.4)".asColor()) }
            }
            series {
                scatter {
                    name = "北京"
                    data = aqiItems(AQI_BEIJING)
                    itemStyle =
                        ItemStyleOption().apply {
                            opacity = 0.8
                            shadowBlur = 10.0
                            shadowOffsetX = 0.0
                            shadowOffsetY = 0.0
                            shadowColor = "rgba(0,0,0,0.3)".asColor()
                        }
                }
                scatter {
                    name = "上海"
                    data = aqiItems(AQI_SHANGHAI)
                    itemStyle =
                        ItemStyleOption().apply {
                            opacity = 0.8
                            shadowBlur = 10.0
                            shadowOffsetX = 0.0
                            shadowOffsetY = 0.0
                            shadowColor = "rgba(0,0,0,0.3)".asColor()
                        }
                }
                scatter {
                    name = "广州"
                    data = aqiItems(AQI_GUANGZHOU)
                    itemStyle =
                        ItemStyleOption().apply {
                            opacity = 0.8
                            shadowBlur = 10.0
                            shadowOffsetX = 0.0
                            shadowOffsetY = 0.0
                            shadowColor = "rgba(0,0,0,0.3)".asColor()
                        }
                }
            }
        }
