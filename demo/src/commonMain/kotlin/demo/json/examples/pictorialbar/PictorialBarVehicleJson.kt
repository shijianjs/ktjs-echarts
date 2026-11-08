package demo.json.examples.pictorialbar

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.shared.VEHICLE_2015
import demo.json.examples.shared.VEHICLE_2016
import demo.json.examples.shared.VEHICLE_NAMES
import demo.json.examples.shared.vehicleItems
import demo.json.examples.shared.vehicleLabel
import demo.json.*

private val pictorialBarVehicleJsonCategory = JsonCategory("json/examples/pictorialbar/PictorialBarVehicleJson.kt")

val pictorialBarVehicleJsonExample: JsonExample =
    pictorialBarVehicleJsonCategory.example("pictorialBar-vehicle") {
            title { text = "Vehicles in X City" }
            legend { entries("2015", "2016") }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = AxisPointerOption().apply { type = "shadow" }
            }
            grid {
                containLabel = true
                left = num(20)
            }
            yAxis {
                categories(*VEHICLE_NAMES)
                inverse = true
                axisLine { show = false }
                axisTick { show = false }
                axisLabel {
                    margin = 30.0
                    fontSize = 14.0
                }
                axisPointer =
                    AxisPointerOption().apply {
                        label = AxisPointerLabelOption().apply {
                            show = true
                            margin = num(30)
                        }
                    }
            }
            xAxis {
                splitLine { show = false }
                axisLabel { show = false }
                axisTick { show = false }
                axisLine { show = false }
            }
            series {
                pictorialBar {
                    name = "2015"
                    label = vehicleLabel()
                    symbolRepeat = BooleanOrNumberOrString.of(true)
                    symbolSize = NumberOrStringOrNumbers.of(str("80%"), str("60%"))
                    barCategoryGap = "40%"
                    data = vehicleItems(VEHICLE_2015)
                }
                pictorialBar {
                    name = "2016"
                    barGap = "10%"
                    label = vehicleLabel()
                    symbolRepeat = BooleanOrNumberOrString.of(true)
                    symbolSize = NumberOrStringOrNumbers.of(str("80%"), str("60%"))
                    data = vehicleItems(VEHICLE_2016)
                }
            }
        }
