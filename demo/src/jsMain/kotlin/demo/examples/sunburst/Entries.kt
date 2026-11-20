package demo.examples.sunburst

import demo.CatalogEntry

/**
 * Sunburst examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object SunburstEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            SunburstSimple.ENTRY, SunburstBorderRadius.ENTRY, SunburstLabelRotate.ENTRY,
            SunburstMonochrome.ENTRY, SunburstVisualMap.ENTRY, SunburstDrink.ENTRY, SunburstBook.ENTRY,
        )
    }
}
