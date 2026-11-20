package demo.examples.lines

import demo.CatalogEntry

/**
 * Lines examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object LinesEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            LinesAirline.ENTRY, LinesNy.ENTRY,
        )
    }
}
