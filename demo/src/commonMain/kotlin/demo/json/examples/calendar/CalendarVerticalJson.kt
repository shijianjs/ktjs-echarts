package demo.json.examples.calendar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonPrimitive
import demo.json.JsonCategory
import demo.json.JsonExample

private val calendarVerticalJsonCategory = JsonCategory("json/examples/calendar/CalendarVerticalJson.kt")

val calendarVerticalJsonExample: JsonExample =
    calendarVerticalJsonCategory.example("calendar-vertical") {
            tooltip { position = JsonPrimitive("top") }
            visualMap {
                min = 0.0
                max = 1000.0
                calculable = true
                orient = Orient.Vertical
                left = str("670")
                top = str("center")
            }
            calendarIn("2015") { orient = Orient.Vertical }
            calendarIn("2016") {
                left = num(300)
                orient = Orient.Vertical
            }
            calendarIn("2017") {
                left = num(520)
                cellSize = cellSize(num(20), str("auto"))
                bottom = num(10)
                orient = Orient.Vertical
                dayLabel { margin = 5.0 }
            }
            series {
                calendarHeatmap(yearValues("2015", 1000.0, 20150101L), 0)
                calendarHeatmap(yearValues("2016", 1000.0, 20160101L), 1)
                calendarHeatmap(yearValues("2017", 1000.0, 20170101L), 2)
            }
        }
