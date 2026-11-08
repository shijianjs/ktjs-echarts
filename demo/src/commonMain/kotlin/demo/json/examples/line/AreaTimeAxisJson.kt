package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val areaTimeAxisJsonCategory = JsonCategory("json/examples/line/AreaTimeAxisJson.kt")

val areaTimeAxisJsonExample: JsonExample =
    areaTimeAxisJsonCategory.example("area-time-axis") {
            // The title of the original is kept verbatim, including the misspelling.
            title {
                left = str("center")
                text = "Large Ara Chart"
            }
            tooltip { trigger = TooltipTrigger.Axis }
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        restore = ToolboxFeature()
                        saveAsImage = ToolboxSaveAsImageFeature()
                    }
            }
            xAxis {
                type = AxisType.Time
                boundaryGap = BoundaryGap.of(false)
            }
            yAxis {
                type = AxisType.Value
                boundaryGap = BoundaryGap.of(num(0), str("100%"))
            }
            dataZoom {
                type = DataZoomType.Inside
                start = 0.0
                end = 20.0
            }
            dataZoom {
                type = DataZoomType.Slider
                start = 0.0
                end = 20.0
            }
            series {
                line {
                    name = "Fake Data"
                    smooth = BooleanOrNumber.of(true)
                    symbol = "none"
                    areaStyle { }
                    data = timeAxisPoints()
                }
            }
        }
