package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.namedValue
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val pieLegendJsonCategory = JsonCategory("json/examples/pie/PieLegendJson.kt")

val pieLegendJsonExample: JsonExample =
    pieLegendJsonCategory.example("pie-legend") {
            val surnames = surnames(50)
            title {
                text = "同名数量统计"
                subtext = "纯属虚构"
                left = str("center")
            }
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c} ({d}%)"
            }
            legend {
                type = LegendType.Scroll
                orient = Orient.Vertical
                right = num(10)
                top = num(20)
                bottom = num(20)
                entries(*surnames.map { it.first }.toTypedArray())
            }
            series {
                pie {
                    name = "姓名"
                    radius = NumberOrStringOrNumbers.of("55%")
                    center = listOf(str("40%"), str("50%"))
                    data = surnames.map { (name, value) -> demo.json.namedValue(name, value) }
                    emphasis {
                        itemStyle {
                            shadowBlur = 10.0
                            shadowOffsetX = 0.0
                            shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                        }
                    }
                }
            }
        }
