package demo.examples.heatmap

import demo.CatalogEntry

/**
 * Heatmap examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object HeatmapEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            HeatmapCartesian.ENTRY, HeatmapLarge.ENTRY, HeatmapLargePiecewise.ENTRY, HeatmapMap.ENTRY,
        )
    }
}
