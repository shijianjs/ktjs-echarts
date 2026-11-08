package demo.json.examples.calendar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val calendarHeatmapJsonCategory = JsonCategory("json/examples/calendar/CalendarHeatmapJson.kt")

val calendarHeatmapJsonExample: JsonExample =
    calendarHeatmapJsonCategory.example("calendar-heatmap") {
            title {
                top = num(30)
                left = str("center")
                text = "Daily Step Count"
            }
            tooltip { }
            piecewiseVisualMap {
                min = 0.0
                max = 10000.0
                left = str("center")
                top = num(65)
            }
            calendarIn("2016") {
                top = num(120)
                left = num(30)
                right = num(30)
                cellSize = cellSize(str("auto"), num(13))
                itemStyle = ItemStyleOption().apply { borderWidth = 0.5 }
                yearLabel = CalendarLabelOption().apply { show = false }
            }
            series {
                calendarHeatmap(yearValues("2016", 10000.0, 20160101L))
            }
        }
