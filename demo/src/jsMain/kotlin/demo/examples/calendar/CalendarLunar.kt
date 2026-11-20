package demo.examples.calendar

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category

internal class CalendarLunar {
    companion object {
        private val CATEGORY = Category(Groups.CALENDAR, "examples/calendar/CalendarLunar.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive("calendar-lunar", "农历日历图", "Calendar Lunar") { chart ->
                    loadJson("data/calendar-lunar.json").then { json ->
                        chart.setOption(lunarOption(json.unsafeCast<Array<Array<String?>>>()))
                    }
                }
    }
}
