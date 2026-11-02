package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category

private val calendarLunarCategory = Category(Groups.CALENDAR, "examples/calendar/CalendarLunar.kt")

val calendarLunarEntry: CatalogEntry =
    calendarLunarCategory.interactive("calendar-lunar", "农历日历图", "Calendar Lunar") { chart ->
            loadJson("data/calendar-lunar.json").then { json ->
                chart.setOption(lunarOption(json.unsafeCast<Array<Array<String?>>>()))
            }
        }
