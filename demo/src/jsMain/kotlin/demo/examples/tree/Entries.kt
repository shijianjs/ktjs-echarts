package demo.examples.tree

import demo.CatalogEntry

/**
 * Tree examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object TreeEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            TreeBasic.ENTRY, TreeLegend.ENTRY, TreePolyline.ENTRY, TreeRadial.ENTRY, TreeVertical.ENTRY,
            TreeOrientBottomTop.ENTRY, TreeOrientRightLeft.ENTRY,
        )
    }
}
