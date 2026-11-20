package demo.examples.graph

import demo.CatalogEntry

/**
 * Graph examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object GraphEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            GraphSimple.ENTRY, GraphForce2.ENTRY, Graph.ENTRY, GraphCircularLayout.ENTRY, GraphForce.ENTRY,
            GraphLabelOverlap.ENTRY, GraphNpm.ENTRY, GraphWebkitDep.ENTRY, GraphLifeExpectancy.ENTRY,
            GraphGrid.ENTRY, GraphForceDynamic.ENTRY,
        )
    }
}
