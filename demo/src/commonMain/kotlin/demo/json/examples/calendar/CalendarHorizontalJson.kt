package demo.json.examples.calendar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonPrimitive
import demo.json.JsonCategory
import demo.json.JsonExample

private val calendarHorizontalJsonCategory = JsonCategory("json/examples/calendar/CalendarHorizontalJson.kt")

val calendarHorizontalJsonExample: JsonExample =
    calendarHorizontalJsonCategory.example("calendar-horizontal") {
            tooltip { position = JsonPrimitive("top") }
            visualMap {
                min = 0.0
                max = 1000.0
                calculable = true
                orient = Orient.Horizontal
                left = str("center")
                top = str("top")
            }
            calendarIn("2017") {
                cellSize = cellSize(str("auto"), num(20))
            }
            calendarIn("2016") {
                top = num(260)
                cellSize = cellSize(str("auto"), num(20))
            }
            calendarIn("2015") {
                top = num(450)
                cellSize = cellSize(str("auto"), num(20))
                right = num(5)
            }
            series {
                calendarHeatmap(yearValues("2017", 1000.0, 20170101L), 0)
                calendarHeatmap(yearValues("2016", 1000.0, 20160201L), 1)
                calendarHeatmap(yearValues("2015", 1000.0, 20150301L), 2)
            }
        }
