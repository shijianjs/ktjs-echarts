package demo.examples.radar

import demo.CatalogEntry

/**
 * Radar examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object RadarEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            Radar.ENTRY, RadarAqi.ENTRY, RadarCustom.ENTRY, Radar2.ENTRY, RadarMultiple.ENTRY,
        )
    }
}
