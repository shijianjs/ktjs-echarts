package demo.examples.dataset

import demo.CatalogEntry

/**
 * Dataset examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object DatasetEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            DatasetSimple0.ENTRY, DatasetSimple1.ENTRY, DatasetDefault.ENTRY, DatasetEncode0.ENTRY,
            DatasetSeriesLayoutBy.ENTRY, DataTransformSortBar.ENTRY, DataTransformMultiplePie.ENTRY,
            DatasetEncode1.ENTRY, DataTransformFilter.ENTRY, DatasetLink.ENTRY,
        )
    }
}
