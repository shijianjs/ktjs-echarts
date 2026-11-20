package demo.examples.geo

import demo.CatalogEntry

/**
 * Geo examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object GeoEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            GeoChoroplethScatter.ENTRY, GeoSvgScatterSimple.ENTRY, GeoBeefCuts.ENTRY, GeoGraph.ENTRY,
            GeoSvgLines.ENTRY, GeoSeatmapFlight.ENTRY, GeoOrgan.ENTRY, GeoSvgTraffic.ENTRY,
            GeoSvgMap.ENTRY, MapHK.ENTRY, MapUsa.ENTRY, MapBarMorph.ENTRY, MapIcelandPie.ENTRY,
            GeoMapScatter.ENTRY, GeoLines.ENTRY,
        )
    }
}
