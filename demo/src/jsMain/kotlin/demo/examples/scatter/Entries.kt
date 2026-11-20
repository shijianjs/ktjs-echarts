package demo.examples.scatter

import demo.CatalogEntry

/**
 * Scatter examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object ScatterEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            ScatterSimple.ENTRY, BubbleGradient.ENTRY, ScatterJitter.ENTRY, ScatterPolarPunchCard.ENTRY,
            ScatterLarge.ENTRY, ScatterPainterChoice.ENTRY, ScatterSingleAxis.ENTRY, ScatterAqiColor.ENTRY,
            ScatterLifeExpectancyTimeline.ENTRY, ScatterNutrients.ENTRY, ScatterNutrientsMatrix.ENTRY,
            ScatterPunchCard.ENTRY, ScatterStreamVisual.ENTRY, ScatterNebula.ENTRY,
            ScatterSymbolMorph.ENTRY, ScatterAggregateBar.ENTRY, ScatterLabelAlignTop.ENTRY,
            ScatterLabelAlignRight.ENTRY, ScatterEffect.ENTRY, ScatterAnscombeQuartet.ENTRY,
            ScatterMatrix.ENTRY, ScatterWeight.ENTRY, ScatterWorldPopulation.ENTRY, ScatterMap.ENTRY,
            EffectScatterMap.ENTRY, ScatterWeibo.ENTRY,
        )
    }
}
