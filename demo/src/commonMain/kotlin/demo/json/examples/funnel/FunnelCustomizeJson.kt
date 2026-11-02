package demo.json.examples.funnel

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.FUNNEL_LABELS
import demo.json.examples.shared.fill
import demo.json.examples.shared.funnelToolbox
import demo.json.examples.shared.stages

private val funnelCustomizeJsonCategory = JsonCategory("json/examples/funnel/FunnelCustomizeJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val funnelCustomizeJsonExample: JsonExample =
    funnelCustomizeJsonCategory.example("funnel-customize") {
            title { text = "Funnel" }
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c}%"
            }
            funnelToolbox()
            legend { entries(*FUNNEL_LABELS) }
            series {
                funnel {
                    name = "Expected"
                    left = str("10%")
                    width = str("80%")
                    label { formatter = "{b}Expected" }
                    labelLine { show = false }
                    itemStyle = fill(opacity = 0.7)
                    emphasis {
                        label {
                            position = "inside"
                            formatter = "{b}Expected: {c}%"
                        }
                    }
                    data = stages(60, 40, 20, 80, 100)
                }
                funnel {
                    name = "Actual"
                    left = str("10%")
                    width = str("80%")
                    maxSize = str("80%")
                    label {
                        position = "inside"
                        formatter = "{c}%"
                        color = "#fff".asColor()
                    }
                    itemStyle =
                        fill(
                            opacity = 0.5,
                            borderColor = "#fff",
                            borderWidth = 2.0,
                        )
                    emphasis {
                        label {
                            position = "inside"
                            formatter = "{b}Actual: {c}%"
                        }
                    }
                    z = 100.0
                    data = stages(30, 10, 5, 50, 80)
                }
            }
        }
