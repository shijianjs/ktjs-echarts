package demo.json.examples.funnel

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.FUNNEL_LABELS
import demo.json.examples.shared.fill
import demo.json.examples.shared.funnelToolbox
import demo.json.examples.shared.stages
import demo.json.examples.shared.stroke

private val funnelJsonCategory = JsonCategory("json/examples/funnel/FunnelJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val funnelJsonExample: JsonExample =
    // ------------------------------------------------------------------------------ funnels
        funnelJsonCategory.example("funnel") {
            title { text = "Funnel" }
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c}%"
            }
            funnelToolbox()
            legend { entries(*FUNNEL_LABELS) }
            series {
                funnel {
                    name = "Funnel"
                    left = str("10%")
                    top = num(60)
                    bottom = num(60)
                    width = str("80%")
                    min = 0.0
                    max = 100.0
                    minSize = str("0%")
                    maxSize = str("100%")
                    sort = BooleanOrString.of("descending")
                    gap = 2.0
                    label {
                        show = true
                        position = "inside"
                    }
                    labelLine {
                        length = 10.0
                        lineStyle = stroke(width = 1.0, type = "solid")
                    }
                    itemStyle = fill(borderColor = "#fff", borderWidth = 1.0)
                    emphasis { label { fontSize = 20.0 } }
                    data = stages(60, 40, 20, 80, 100)
                }
            }
        }
