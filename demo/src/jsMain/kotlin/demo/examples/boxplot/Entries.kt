package demo.examples.boxplot

import demo.CatalogEntry

/**
 * Boxplot examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object BoxplotEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            BoxplotLightVelocity.ENTRY, BoxplotLightVelocity2.ENTRY, BoxplotMulti.ENTRY,
        )
    }
}
