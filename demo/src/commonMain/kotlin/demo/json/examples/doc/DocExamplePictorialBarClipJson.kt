package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.clipBackground
import demo.json.examples.shared.clippedSymbol
import demo.json.*

private val docExamplePictorialBarClipJsonCategory = JsonCategory("json/examples/doc/DocExamplePictorialBarClipJson.kt")

val docExamplePictorialBarClipJsonExample: JsonExample =
    docExamplePictorialBarClipJsonCategory.example("doc-example/pictorialBar-clip") {
            color = colors("#bb0004", "#ccc")
            xAxis {
                categories(
                    "symbolClip: true\nsymbolRepeat: false",
                    "symbolClip: true\nsymbolRepeat: true",
                    "symbolClip: false\nsymbolRepeat: true",
                )
                axisTick { show = false }
                axisLabel { interval = num(0) }
            }
            yAxis {
                max = num(80)
                splitLine { show = false }
            }
            series {
                clippedSymbol(0, clipped = true, repeat = null, size = null)
                clippedSymbol(
                    1,
                    clipped = true,
                    repeat = true,
                    size = NumberOrStringOrNumbers.of(str("50%"), str("80%")),
                )
                clippedSymbol(
                    2,
                    clipped = false,
                    repeat = true,
                    size = NumberOrStringOrNumbers.of(str("50%"), str("80%")),
                )
                clipBackground(0, null)
                clipBackground(1, NumberOrStringOrNumbers.of(str("50%"), str("80%")))
                clipBackground(2, NumberOrStringOrNumbers.of(str("50%"), str("80%")))
            }
        }
