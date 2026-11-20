package demo.examples.graphic

import demo.CatalogEntry

/**
 * Graphic examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object GraphicEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            GraphicStrokeAnimation.ENTRY, GraphicLoading.ENTRY, GraphicWaveAnimation.ENTRY,
            Watermark.ENTRY, BarDrilldown.ENTRY, BarRace.ENTRY,
        )
    }
}
