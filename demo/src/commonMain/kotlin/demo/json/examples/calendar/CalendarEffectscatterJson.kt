package demo.json.examples.calendar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val calendarEffectscatterJsonCategory = JsonCategory("json/examples/calendar/CalendarEffectscatterJson.kt")

val calendarEffectscatterJsonExample: JsonExample =
    calendarEffectscatterJsonCategory.example("calendar-effectscatter") {
            val days = dailyValues("2016-01-01", "2017-01-01", 10000.0, 20160101L)
            val topDays = days.sortedByDescending { it.second }.take(12)
            backgroundColor = "#404a59".asColor()
            title {
                top = num(30)
                text = "Daily Step Count in 2016"
                subtext = "Fake Data"
                left = str("center")
                textStyle { color = "#fff".asColor() }
            }
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                top = str("30")
                left = str("100")
                entries("Steps", "Top 12")
                textStyle { color = "#fff".asColor() }
            }
            halfYearCalendar("2016-01-01", "2016-06-30", 120.0)
            halfYearCalendar("2016-07-01", "2016-12-31", 340.0)
            series {
                calendarSteps(days, null)
                calendarSteps(days, 1)
                calendarTopSteps(topDays, 1)
                calendarTopSteps(topDays, null)
            }
        }
