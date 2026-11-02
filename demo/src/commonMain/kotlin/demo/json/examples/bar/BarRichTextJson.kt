package demo.json.examples.bar

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.shared.weatherAxisLabel
import demo.json.examples.shared.weatherMarkPoint
import demo.json.*

private val barRichTextJsonCategory = JsonCategory("json/examples/bar/BarRichTextJson.kt")

val barRichTextJsonExample: JsonExample =
    barRichTextJsonCategory.example("bar-rich-text") {
            title { text = "Weather Statistics" }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = AxisPointerOption().apply { type = "shadow" }
            }
            legend { entries("City Alpha", "City Beta", "City Gamma") }
            grid { left = num(100) }
            toolbox {
                show = true
                feature =
                    ToolboxFeatureOption().apply {
                        saveAsImage = ToolboxSaveAsImageFeature().apply { show = true }
                    }
            }
            xAxis {
                type = AxisType.Value
                name = "Days"
                axisLabel { formatter = "{value}" }
            }
            yAxis {
                type = AxisType.Category
                inverse = true
                categories("Sunny", "Cloudy", "Showers")
                axisLabel = weatherAxisLabel()
            }
            series {
                bar {
                    name = "City Alpha"
                    values(165, 170, 30)
                    label { show = true }
                    markPoint = weatherMarkPoint()
                }
                bar {
                    name = "City Beta"
                    values(150, 105, 110)
                    label { show = true }
                }
                bar {
                    name = "City Gamma"
                    values(220, 82, 63)
                    label { show = true }
                }
            }
        }
