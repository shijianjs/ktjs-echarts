package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.repeatedSymbol
import demo.json.examples.shared.singleSlot
import demo.json.*

private val docExamplePictorialBarRepeatJsonCategory = JsonCategory("json/examples/doc/DocExamplePictorialBarRepeatJson.kt")

val docExamplePictorialBarRepeatJsonExample: JsonExample =
    docExamplePictorialBarRepeatJsonCategory.example("doc-example/pictorialBar-repeat") {
            legend { entries("pictorialBar", "bar") }
            xAxis { categories("symbolRepeat: true", "symbolRepeat: false") }
            yAxis { splitLine { show = false } }
            animationEasing = "elasticOut"
            series {
                pictorialBar {
                    name = "pictorialBar"
                    symbol = "circle"
                    symbolRepeat = BooleanOrNumberOrString.of(true)
                    symbolSize = NumberOrStringOrNumbers.of(num(50), num(50))
                    data = singleSlot(0, 2, 29000)
                }
                repeatedSymbol(1, NumberOrStringOrNumbers.of(num(50), str("100%")))
            }
        }
