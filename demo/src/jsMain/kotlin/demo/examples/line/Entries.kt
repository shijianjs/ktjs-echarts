package demo.examples.line

import demo.CatalogEntry

/**
 * Line examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object LineEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            ConfidenceBand.ENTRY, LineSimple.ENTRY, LineSmooth.ENTRY, AreaBasic.ENTRY, LineStack.ENTRY,
            AreaStack.ENTRY, LineStep.ENTRY, LineStyle.ENTRY, LineLog.ENTRY, LineYCategory.ENTRY,
            LineInCartesianCoordinateSystem.ENTRY, GridMultiple.ENTRY, AreaRainfall.ENTRY, LinePolar.ENTRY,
            LinePolar2.ENTRY, LineFunction.ENTRY, LineAqi.ENTRY, LineTooltipTouch.ENTRY,
            LineMarkline.ENTRY, LineSections.ENTRY, LineMarker.ENTRY, LineRace.ENTRY,
            AreaStackGradient.ENTRY, AreaPieces.ENTRY, AreaTimeAxis.ENTRY, LineGradient.ENTRY,
            MultipleXAxis.ENTRY, DynamicData2.ENTRY, LineEasing.ENTRY, BumpChart.ENTRY, LineGraphic.ENTRY,
            IntradayBreaks1.ENTRY, IntradayBreaks2.ENTRY, AreaSimple.ENTRY,
        )
    }
}
