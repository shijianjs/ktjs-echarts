package demo.json.examples.sankey

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.ITEM_STYLE_LINKS
import demo.json.examples.shared.ITEM_STYLE_NODES
import demo.json.examples.shared.coloredNode
import demo.json.*

private val sankeyItemstyleJsonCategory = JsonCategory("json/examples/sankey/SankeyItemstyleJson.kt")

val sankeyItemstyleJsonExample: JsonExample =
    sankeyItemstyleJsonCategory.example("sankey-itemstyle") {
            backgroundColor = "#fff".asColor()
            title {
                subtext = "Data From lisachristina1234 on GitHub"
                left = str("center")
            }
            tooltip { trigger = TooltipTrigger.Item }
            series {
                sankey {
                    left = num(50)
                    top = num(20)
                    right = num(150)
                    bottom = num(25)
                    data = ITEM_STYLE_NODES.map { (name, hex) -> coloredNode(name, hex) }
                    links = ITEM_STYLE_LINKS
                    lineStyle { color = "source".asColor() }
                    itemStyle {
                        color = "#1f77b4".asColor()
                        borderColor = "#1f77b4".asColor()
                    }
                    label {
                        color = "rgba(0,0,0,0.7)".asColor()
                        fontFamily = "Arial"
                        fontSize = 10.0
                    }
                }
            }
        }
