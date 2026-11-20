package demo.examples.custom

import demo.CatalogEntry

/**
 * Custom series examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object CustomEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            CustomProfit.ENTRY, CustomCartesianPolygon.ENTRY, CustomErrorBar.ENTRY,
            CustomErrorScatter.ENTRY, CustomBarTrend.ENTRY, CustomOhlc.ENTRY, CustomHexbin.ENTRY,
            CustomPolarHeatmap.ENTRY, CustomWind.ENTRY, GeoSvgCustomEffect.ENTRY, CustomProfile.ENTRY,
            CustomSpiralRace.ENTRY, CustomGauge.ENTRY, CyclePlot.ENTRY, CustomCalendarIcon.ENTRY,
            FlameGraph.ENTRY, WindBarb.ENTRY, PieParliamentTransition.ENTRY,
        )
    }
}
