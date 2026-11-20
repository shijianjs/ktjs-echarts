package demo.examples.parallel

import demo.CatalogEntry

/**
 * Parallel examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object ParallelEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            ParallelSimple.ENTRY, ParallelAqi.ENTRY, ParallelNutrients.ENTRY,
        )
    }
}
