package demo.json.examples.funnel

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.compareFunnel
import demo.json.examples.shared.funnelToolbox

private val funnelAlignJsonCategory = JsonCategory("json/examples/funnel/FunnelAlignJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val funnelAlignJsonExample: JsonExample =
    funnelAlignJsonCategory.example("funnel-align") {
            title {
                text = "Funnel Compare"
                subtext = "Fake Data"
                left = str("left")
                top = str("bottom")
            }
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c}%"
            }
            funnelToolbox(orient = Orient.Vertical, top = str("center"))
            legend {
                orient = Orient.Vertical
                left = str("left")
                entries("Prod A", "Prod B", "Prod C", "Prod D", "Prod E")
            }
            series {
                compareFunnel("Funnel", "5%", "50%", "right", ascending = false)
                compareFunnel("Pyramid", "5%", "5%", "right", ascending = true)
                compareFunnel("Funnel", "55%", "5%", "left", ascending = false)
                compareFunnel("Pyramid", "55%", "50%", "left", ascending = true)
            }
        }
