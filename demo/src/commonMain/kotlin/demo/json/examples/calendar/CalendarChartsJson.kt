package demo.json.examples.calendar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.timedValue

private val calendarChartsJsonCategory = JsonCategory("json/examples/calendar/CalendarChartsJson.kt")

val calendarChartsJsonExample: JsonExample =
    calendarChartsJsonCategory.example("calendar-charts") {
            val graphDays =
                listOf(
                    "2017-02-01" to 260.0,
                    "2017-02-04" to 200.0,
                    "2017-02-09" to 279.0,
                    "2017-02-13" to 847.0,
                    "2017-02-18" to 241.0,
                    "2017-02-23" to 411.0,
                    "2017-02-27" to 985.0,
                )
            tooltip { position = JsonPrimitive("top") }
            visualMap {
                min = 0.0
                max = 1000.0
                calculable = true
                seriesIndex = listOf(2, 3, 4)
                orient = Orient.Horizontal
                left = str("55%")
                bottom = num(20)
            }
            visualMap {
                min = 0.0
                max = 1000.0
                inRange {
                    color = listOf("grey".asColor())
                    opacity = listOf(0.0, 0.3)
                }
                controller =
                    buildJsonObject {
                        put("inRange", visualRangeJson(VisualMapRange().apply { opacity = listOf(0.3, 0.6) }))
                        put("outOfRange", visualRangeJson(VisualMapRange().apply { color = listOf("#ccc".asColor()) }))
                    }
                seriesIndex = listOf(1)
                orient = Orient.Horizontal
                left = str("10%")
                bottom = num(20)
            }
            calendarIn("2017-02") {
                orient = Orient.Vertical
                yearLabel = CalendarLabelOption().apply { margin = 40.0 }
                monthLabel = CalendarLabelOption().apply { margin = 20.0 }
                // The `dayLabel: { firstDay: 1, nameMap: 'cn' }` of the original has no model field.
                cellSize = cellSize(40)
            }
            calendarIn("2017-01") {
                orient = Orient.Vertical
                yearLabel = CalendarLabelOption().apply { margin = 40.0 }
                monthLabel = CalendarLabelOption().apply { margin = 20.0 }
                cellSize = cellSize(40)
                left = num(460)
            }
            calendarIn("2017-03") {
                orient = Orient.Vertical
                yearLabel = CalendarLabelOption().apply { margin = 40.0 }
                monthLabel = CalendarLabelOption().apply { margin = 20.0 }
                cellSize = cellSize(40)
                top = num(350)
            }
            calendarIn("2017-04") {
                orient = Orient.Vertical
                yearLabel = CalendarLabelOption().apply { margin = 40.0 }
                // The English `nameMap` day labels of the original have no model field.
                monthLabel = CalendarLabelOption().apply { margin = 20.0 }
                cellSize = cellSize(40)
                top = num(350)
                left = num(460)
            }
            series {
                graph {
                    coordinateSystem = "calendar"
                    edgeSymbol = listOf("none", "arrow")
                    links = graphLinks(graphDays.size)
                    symbolSize = NumberOrStringOrNumbers.of(10)
                    calendarIndex = 0
                    data = graphNodes(graphDays)
                }
                calendarHeatmap(yearValues("2017", 1000.0, 20170115L))
                effectScatter {
                    coordinateSystem = "calendar"
                    calendarIndex = 1
                    symbolSize = NumberOrStringOrNumbers.of(25)
                    data =
                        dailyValues("2017-01-01", "2018-01-01", 1000.0, 20170501L)
                            .map { (date, value) -> timedValue(date, value) }
                }
                scatter {
                    coordinateSystem = "calendar"
                    calendarIndex = 2
                    symbolSize = NumberOrStringOrNumbers.of(20)
                    data =
                        dailyValues("2017-03-01", "2018-01-01", 1000.0, 20170601L)
                            .map { (date, value) -> timedValue(date, value) }
                }
                calendarHeatmap(yearValues("2017", 1000.0, 20170701L), 3)
            }
        }
