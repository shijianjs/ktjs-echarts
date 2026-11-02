package demo.json.examples.pie

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val pieRoseTypeJsonCategory = JsonCategory("json/examples/pie/PieRoseTypeJson.kt")

val pieRoseTypeJsonExample: JsonExample =
    pieRoseTypeJsonCategory.example("pie-roseType") {
            title {
                text = "Nightingale Chart"
                subtext = "Fake Data"
                left = str("center")
            }
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c} ({d}%)"
            }
            legend {
                entries("rose1", "rose2", "rose3", "rose4", "rose5", "rose6", "rose7", "rose8")
            }
            series {
                pie {
                    name = "Radius Mode"
                    radius = NumberOrStringOrNumbers.of(num(20), num(140))
                    center = listOf(str("25%"), str("50%"))
                    roseType = io.github.shijianjs.kmpechartsjson.BooleanOrString.of("radius")
                    itemStyle { borderRadius = NumberOrStringOrNumbers.of(5) }
                    label { show = false }
                    emphasis { label { show = true } }
                    data = roseData(40, 33, 28, 22, 20, 15, 12, 10)
                }
                pie {
                    name = "Area Mode"
                    radius = NumberOrStringOrNumbers.of(num(20), num(140))
                    center = listOf(str("75%"), str("50%"))
                    roseType = io.github.shijianjs.kmpechartsjson.BooleanOrString.of("area")
                    itemStyle { borderRadius = NumberOrStringOrNumbers.of(5) }
                    data = roseData(30, 28, 26, 24, 22, 20, 18, 16)
                }
            }
        }
