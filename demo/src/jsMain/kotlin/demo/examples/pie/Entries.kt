package demo.examples.pie

import demo.CatalogEntry

/**
 * Pie examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object PieEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            PieRichText.ENTRY, PieSimple.ENTRY, PieDoughnut.ENTRY, PieHalfDonut.ENTRY,
            PieBorderRadius.ENTRY, PiePadAngle.ENTRY, PieRoseType.ENTRY, PieRoseTypeSimple.ENTRY,
            PieAlignTo.ENTRY, PieNest.ENTRY, PieCustom.ENTRY, PieHighlight.ENTRY, PieLabelLineAdjust.ENTRY,
            PieLegend.ENTRY,
        )
    }
}
