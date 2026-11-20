package demo.examples.pictorialbar

import demo.CatalogEntry

/**
 * Pictorial bar examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object PictorialbarEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            PictorialBarDotted.ENTRY, PictorialBarVehicle.ENTRY, PictorialBarBodyFill.ENTRY,
            PictorialBarVelocity.ENTRY, PictorialBarBarTransition.ENTRY,
        )
    }
}
