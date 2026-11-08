package demo.json.examples.funnel

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import kotlinx.serialization.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.examples.shared.FUNNEL_LABELS
import demo.json.examples.shared.funnelToolbox
import demo.json.examples.shared.quadrantFunnel

private val funnelMutipleJsonCategory = JsonCategory("json/examples/funnel/FunnelMutipleJson.kt")

// ---------------------------------------------------------------------------------------------
// Shared builders
// ---------------------------------------------------------------------------------------------

val funnelMutipleJsonExample: JsonExample =
    funnelMutipleJsonCategory.example("funnel-mutiple") {
            title {
                text = "Funnel"
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
                entries(*FUNNEL_LABELS)
            }
            series {
                quadrantFunnel("Funnel", "5%", "50%", labelled = false)
                quadrantFunnel("Pyramid", "5%", "5%", labelled = false)
                quadrantFunnel("Funnel", "55%", "5%", labelled = true)
                quadrantFunnel("Pyramid", "55%", "50%", labelled = true)
            }
        }
