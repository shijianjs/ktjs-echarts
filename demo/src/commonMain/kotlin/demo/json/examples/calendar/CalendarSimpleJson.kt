package demo.json.examples.calendar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val calendarSimpleJsonCategory = JsonCategory("json/examples/calendar/CalendarSimpleJson.kt")

val calendarSimpleJsonExample: JsonExample =
    calendarSimpleJsonCategory.example("calendar-simple") {
            visualMap {
                show = false
                min = 0.0
                max = 10000.0
            }
            calendarIn("2017")
            series {
                calendarHeatmap(yearValues("2017", 10000.0, 20170401L))
            }
        }
