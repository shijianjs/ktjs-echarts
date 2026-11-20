package demo.examples.gauge

import demo.CatalogEntry

/**
 * Gauge examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object GaugeEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            Gauge.ENTRY, GaugeSimple.ENTRY, GaugeProgress.ENTRY, GaugeGrade.ENTRY, GaugeRing.ENTRY,
            GaugeStage.ENTRY, GaugeTemperature.ENTRY, GaugeSpeed.ENTRY, GaugeClock.ENTRY,
            GaugeBarometer.ENTRY, GaugeMultiTitle.ENTRY, GaugeCar.ENTRY,
        )
    }
}
