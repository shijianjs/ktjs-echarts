package demo.json.examples.calendar

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.timedValue

private val calendarPieJsonCategory = JsonCategory("json/examples/calendar/CalendarPieJson.kt")

val calendarPieJsonExample: JsonExample =
    calendarPieJsonCategory.example("calendar-pie") {
            // The pies sit on the days of February 2017, one per day.
            val days = dailyValues("2017-02-01", "2017-03-01", 10000.0, 20170201L)
            tooltip { }
            legend {
                entries("Work", "Entertainment", "Sleep")
                bottom = num(20)
            }
            calendarIn("2017-02") {
                top = str("middle")
                left = str("center")
                orient = Orient.Vertical
                cellSize = cellSize(num(80), num(80))
                yearLabel =
                    CalendarLabelOption().apply {
                        show = false
                        fontSize = 30.0
                    }
                // The `firstDay` / `nameMap` day labels of the original have no model field.
                dayLabel = CalendarLabelOption().apply { margin = 20.0 }
                monthLabel = CalendarLabelOption().apply { show = false }
            }
            series {
                // The layer that carried the day numbers as a `label.formatter` callback.
                scatter {
                    id = "label"
                    coordinateSystem = "calendar"
                    symbolSize = NumberOrStringOrNumbers.of(0)
                    label {
                        show = true
                        position = "inside"
                        offset = listOf(num(-30), num(-30))
                        fontSize = 14.0
                    }
                    data = days.map { (date, value) -> timedValue(date, value) }
                }
                days.forEachIndexed { index, (date, _) -> add(dayPie(index, date)) }
            }
        }
