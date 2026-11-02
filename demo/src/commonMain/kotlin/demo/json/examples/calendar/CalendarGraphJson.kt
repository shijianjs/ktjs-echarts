package demo.json.examples.calendar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val calendarGraphJsonCategory = JsonCategory("json/examples/calendar/CalendarGraphJson.kt")

val calendarGraphJsonExample: JsonExample =
    calendarGraphJsonCategory.example("calendar-graph") {
            // The seven days of the graph, and the chain that links them.
            val days =
                listOf(
                    "2017-02-01" to 260.0,
                    "2017-02-04" to 200.0,
                    "2017-02-09" to 279.0,
                    "2017-02-13" to 847.0,
                    "2017-02-18" to 241.0,
                    "2017-02-23" to 411.0,
                    "2017-03-14" to 985.0,
                )
            tooltip { }
            calendarBetween("2017-02", "2017-03-31") {
                top = str("middle")
                left = str("center")
                orient = Orient.Vertical
                cellSize = cellSize(40)
                yearLabel =
                    CalendarLabelOption().apply {
                        margin = 50.0
                        fontSize = 30.0
                    }
                // `dayLabel: { firstDay: 1, nameMap: 'cn' }` has no model field; see the file header.
                monthLabel =
                    CalendarLabelOption().apply {
                        margin = 15.0
                        fontSize = 20.0
                        color = "#999".asColor()
                    }
            }
            piecewiseVisualMap {
                min = 0.0
                max = 1000.0
                left = str("center")
                bottom = num(20)
                seriesIndex = listOf(1)
                inRange { color = listOf("#5291FF".asColor(), "#C7DBFF".asColor()) }
            }
            series {
                graph {
                    coordinateSystem = "calendar"
                    edgeSymbol = listOf("none", "arrow")
                    links = graphLinks(days.size)
                    symbolSize = NumberOrStringOrNumbers.of(15)
                    calendarIndex = 0
                    itemStyle =
                        ItemStyleOption().apply {
                            color = "yellow".asColor()
                            shadowBlur = 9.0
                            shadowOffsetX = 1.5
                            shadowOffsetY = 3.0
                            shadowColor = "#555".asColor()
                        }
                    lineStyle =
                        LineStyleOption().apply {
                            color = "#D10E00".asColor()
                            width = 1.0
                            opacity = 1.0
                        }
                    data = graphNodes(days)
                    z = 20.0
                }
                calendarHeatmap(yearValues("2017", 1000.0, 20170401L))
            }
        }
