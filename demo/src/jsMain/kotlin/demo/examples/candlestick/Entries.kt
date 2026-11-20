package demo.examples.candlestick

import demo.CatalogEntry

/**
 * Candlestick examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object CandlestickEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            CandlestickLarge.ENTRY, CandlestickSimple.ENTRY, CandlestickTouch.ENTRY, CandlestickSh.ENTRY,
            CandlestickSh2015.ENTRY,
        )
    }
}
