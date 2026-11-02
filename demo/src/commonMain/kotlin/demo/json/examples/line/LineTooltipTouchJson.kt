package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineTooltipTouchJsonCategory = JsonCategory("json/examples/line/LineTooltipTouchJson.kt")

val lineTooltipTouchJsonExample: JsonExample =
    lineTooltipTouchJsonCategory.example("line-tooltip-touch") {
            title {
                left = str("center")
                text = "Tootip and dataZoom on Mobile Device"
            }
            legend {
                top = str("bottom")
                entries("Intention")
            }
            // The original pins the tooltip to a fixed height through a `position` callback.
            tooltip { triggerOn = "none" }
            toolbox {
                left = str("center")
                itemSize = 25.0
                top = num(55)
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        restore = ToolboxFeature()
                    }
            }
            xAxis {
                type = AxisType.Time
                splitLine { show = false }
                axisPointer =
                    AxisPointerOption().apply {
                        snap = true
                        lineStyle =
                            LineStyleOption().apply {
                                color = "#7581BD".asColor()
                                width = 2.0
                            }
                        label =
                            AxisPointerLabelOption().apply {
                                show = true
                                backgroundColor = "#7581BD".asColor()
                            }
                    }
            }
            yAxis {
                type = AxisType.Value
                z = 10.0
                axisTick { inside = true }
                splitLine { show = false }
                axisLabel {
                    inside = true
                    formatter = "{value}\n"
                }
            }
            grid {
                top = num(110)
                left = num(15)
                right = num(15)
                height = num(160)
            }
            dataZoom {
                type = DataZoomType.Inside
                throttle = 50.0
            }
            series {
                line {
                    name = "Fake Data"
                    smooth = BooleanOrNumber.of(true)
                    symbol = "circle"
                    symbolSize = NumberOrStringOrNumbers.of(5)
                    sampling = "average"
                    stack = "a"
                    itemStyle { color = "#0770FF".asColor() }
                    areaStyle {
                        color = verticalGradient("rgba(58,77,233,0.8)", "rgba(58,77,233,0.3)")
                    }
                    data = touchWalk(11, 300.0)
                }
                line {
                    name = "Fake Data"
                    smooth = BooleanOrNumber.of(true)
                    symbol = "circle"
                    symbolSize = NumberOrStringOrNumbers.of(5)
                    sampling = "average"
                    stack = "a"
                    itemStyle { color = "#F2597F".asColor() }
                    areaStyle {
                        color = verticalGradient("rgba(213,72,120,0.8)", "rgba(213,72,120,0.3)")
                    }
                    data = touchWalk(29, 50.0)
                }
            }
        }
