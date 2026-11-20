package demo.examples.funnel

import demo.CatalogEntry

/**
 * Funnel examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object FunnelEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            Funnel.ENTRY, FunnelAlign.ENTRY, FunnelCustomize.ENTRY, FunnelMutiple.ENTRY,
        )
    }
}
