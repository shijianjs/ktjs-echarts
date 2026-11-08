package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.walkSymbol
import demo.json.*

private val docExamplePictorialBarPositionJsonCategory = JsonCategory("json/examples/doc/DocExamplePictorialBarPositionJson.kt")

val docExamplePictorialBarPositionJsonExample: JsonExample =
    docExamplePictorialBarPositionJsonCategory.example("doc-example/pictorialBar-position") {
            color = colors("#bb0004", "#FFD48A")
            legend { entries("pictorial element", "reference bar") }
            xAxis {
                categories(
                    "symbolPosition: \"start\"\nsymbolOffset: [0, 0]",
                    "symbolPosition: \"end\"\nsymbolOffset: [0, 0]",
                    "symbolPosition: \"center\"\nsymbolOffset: [0, 0]",
                    "symbolPosition: \"end\"\nsymbolOffset: ['-100%', -120]",
                )
                axisTick { show = false }
                axisLabel { interval = num(0) }
            }
            yAxis {
                max = num(80)
                splitLine { show = false }
            }
            series {
                walkSymbol(0, "start")
                walkSymbol(1, "end")
                walkSymbol(2, "center")
                walkSymbol(3, "end", listOf(num(-80), str("-100%")))
                bar {
                    name = "reference bar"
                    barGap = "-100%"
                    values(60, 60, 60, 60)
                    itemStyle { color = "#FFD48A".asColor() }
                }
            }
        }
