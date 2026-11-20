package demo.examples.treemap

import demo.CatalogEntry

/**
 * Treemap examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object TreemapEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            TreemapSimple.ENTRY, TreemapDrillDown.ENTRY, TreemapShowParent.ENTRY,
            TreemapSunburstTransition.ENTRY, TreemapVisual.ENTRY, TreemapDisk.ENTRY, TreemapObama.ENTRY,
        )
    }
}
