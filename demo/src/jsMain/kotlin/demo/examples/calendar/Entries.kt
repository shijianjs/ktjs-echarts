package demo.examples.calendar

import demo.CatalogEntry

/**
 * Calendar examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object CalendarEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            CalendarSimple.ENTRY, CalendarHeatmap.ENTRY, CalendarVertical.ENTRY, CalendarHorizontal.ENTRY,
            CalendarGraph.ENTRY, CalendarPie.ENTRY, CalendarCharts.ENTRY, CalendarEffectscatter.ENTRY,
            CalendarLunar.ENTRY,
        )
    }
}
