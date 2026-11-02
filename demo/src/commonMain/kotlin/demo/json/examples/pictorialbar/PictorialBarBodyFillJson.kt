package demo.json.examples.pictorialbar

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.BODY_A
import demo.json.examples.shared.BODY_B
import demo.json.examples.shared.BODY_MAX
import demo.json.examples.shared.BODY_PATHS
import demo.json.examples.shared.bodyBars
import demo.json.examples.shared.fill
import demo.json.*

private val pictorialBarBodyFillJsonCategory = JsonCategory("json/examples/pictorialbar/PictorialBarBodyFillJson.kt")

val pictorialBarBodyFillJsonExample: JsonExample =
    pictorialBarBodyFillJsonCategory.example("pictorialBar-body-fill") {
            tooltip {}
            legend {
                entries("typeA", "typeB")
                selectedMode = BooleanOrString.of("single")
            }
            xAxis {
                categories("a", "b", "c", "d", "e")
                axisTick { show = false }
                axisLine { show = false }
                axisLabel { show = false }
            }
            yAxis {
                max = num(BODY_MAX)
                offset = 20.0
                splitLine { show = false }
            }
            grid {
                top = str("center")
                height = num(230)
            }
            series {
                pictorialBar { bodyBars("typeA", BODY_A) }
                pictorialBar { bodyBars("typeB", BODY_B) }
                pictorialBar {
                    name = "full"
                    symbolBoundingData = NumberOrNumbers.of(BODY_MAX)
                    animationDuration = 0.0
                    itemStyle { color = "#ccc".asColor() }
                    data =
                        BODY_PATHS.map { path ->
                            dataItem {
                                value = valueOf(1)
                                symbol = path
                            }
                        }
                }
            }
        }
