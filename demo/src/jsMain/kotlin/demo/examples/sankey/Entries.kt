package demo.examples.sankey

import demo.CatalogEntry

/**
 * Sankey examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object SankeyEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            SankeySimple.ENTRY, SankeyVertical.ENTRY, SankeyEnergy.ENTRY, SankeyLevels.ENTRY,
            SankeyNodeAlignLeft.ENTRY, SankeyNodeAlignRight.ENTRY, SankeyItemstyle.ENTRY,
        )
    }
}
