package demo.examples.bar

import demo.CatalogEntry

/**
 * Bar examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object BarEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            BarSimple.ENTRY, BarTickAlign.ENTRY, BarBackground.ENTRY, BarDataColor.ENTRY,
            BarYCategory.ENTRY, BarNegative.ENTRY, BarNegative2.ENTRY, BarStack.ENTRY,
            BarYCategoryStack.ENTRY, BarWaterfall.ENTRY, MixLineBar.ENTRY, MultipleYAxis.ENTRY,
            BarAnimationDelay.ENTRY, BarLabelRotation.ENTRY, BarGradient.ENTRY, BarLarge.ENTRY,
            BarBreaksSimple.ENTRY, BarBrush.ENTRY, BarPolarLabelRadial.ENTRY,
            BarPolarLabelTangential.ENTRY, BarPolarStack.ENTRY, BarPolarStackRadial.ENTRY,
            BarPolarRealEstate.ENTRY, PolarRoundCap.ENTRY, PolarEndAngle.ENTRY, MixZoomOnValue.ENTRY,
            Bar1.ENTRY, BarStackNormalization.ENTRY, BarStackNormalizationAndVariation.ENTRY,
            BarStackBorderRadius.ENTRY, BarWaterfall2.ENTRY, BarMultiDrilldown.ENTRY, BarRaceCountry.ENTRY,
            MixTimelineFinance.ENTRY, BarRichText.ENTRY, DynamicData.ENTRY,
        )
    }
}
