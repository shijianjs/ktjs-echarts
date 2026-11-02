package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineSectionsJsonCategory = JsonCategory("json/examples/line/LineSectionsJson.kt")

val lineSectionsJsonExample: JsonExample =
    lineSectionsJsonCategory.example("line-sections") {
            title {
                text = "Distribution of Electricity"
                subtext = "Fake Data"
            }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(
                    "00:00", "01:15", "02:30", "03:45", "05:00", "06:15", "07:30", "08:45",
                    "10:00", "11:15", "12:30", "13:45", "15:00", "16:15", "17:30", "18:45",
                    "20:00", "21:15", "22:30", "23:45",
                )
            }
            yAxis {
                type = AxisType.Value
                axisLabel { formatter = "{value} W" }
                axisPointer = AxisPointerOption().apply { snap = true }
            }
            visualMap {
                show = false
                dimension = num(0)
                pieces =
                    listOf(
                        hourPiece(null, 6.0, "green"),
                        hourPiece(6.0, 8.0, "red"),
                        hourPiece(8.0, 14.0, "green"),
                        hourPiece(14.0, 17.0, "red"),
                        hourPiece(17.0, null, "green"),
                    )
            }
            series {
                line {
                    name = "Electricity"
                    smooth = BooleanOrNumber.of(true)
                    values(
                        300, 280, 250, 260, 270, 300, 550, 500, 400, 390,
                        380, 390, 400, 500, 600, 750, 800, 700, 600, 400,
                    )
                    markArea {
                        itemStyle =
                            ItemStyleOption().apply { color = "rgba(255, 173, 177, 0.4)".asColor() }
                        data =
                            listOf(
                                hourBand("Morning Peak", "07:30", "10:00"),
                                hourBand("Evening Peak", "17:30", "21:15"),
                            )
                    }
                }
            }
        }
