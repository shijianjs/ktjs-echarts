package demo.examples.doc

import demo.CatalogEntry

/**
 * doc examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object DocEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            DocExamplePolarAnticlockwise.ENTRY, DocExamplePolarStartAngle.ENTRY,
            DocExamplePolarEndAngle.ENTRY, DocExampleGettingStarted.ENTRY, DocExampleAriaPie.ENTRY,
            DocExampleSunburstColor.ENTRY, DocExampleSunburstLabelAlign.ENTRY,
            DocExampleLineStackTiled.ENTRY, DocExampleAxisLabelAlignMinMax.ENTRY,
            DocExampleTutorialStylingStep0.ENTRY, DocExampleTutorialStylingStep1.ENTRY,
            DocExampleTutorialStylingStep2.ENTRY, DocExampleTutorialStylingStep3.ENTRY,
            DocExampleTutorialStylingStep4.ENTRY, DocExampleTutorialStylingStep5.ENTRY,
            DocExampleAriaDecalSimple.ENTRY, DocExampleAriaDecalNewspaper.ENTRY,
            DocExampleTextBlockFragment.ENTRY, DocExampleTitleBlock.ENTRY, DocExampleTextOptions.ENTRY,
            DocExampleDatasetEncodeSimple0.ENTRY, DocExampleScatterTutorialDataZoom1.ENTRY,
            DocExamplePieLabelMargin.ENTRY, DocExamplePieLabelDistanceToLabelLine.ENTRY,
            DocExamplePieLabelBleedMargin.ENTRY, DocExampleRadar.ENTRY,
            DocExampleScatterJitterAvoidOverlap.ENTRY, DocExampleParallelAll.ENTRY,
            DocExampleDataTransformMultipleSortBar.ENTRY, DocExampleMatrixGraphBox.ENTRY,
            DocExampleGeoSvgNamedBasic.ENTRY, DocExampleGeoSvgLabelBasic.ENTRY,
            DocExampleGeoSvgLayoutBasic.ENTRY, DocExamplePictorialBarRepeat.ENTRY,
            DocExamplePictorialBarPosition.ENTRY, DocExamplePictorialBarSymbolSize.ENTRY,
            DocExamplePictorialBarSymbolBoundingDataArray.ENTRY, DocExamplePictorialBarClip.ENTRY,
            DocExampleScatterTutorialDataZoom2.ENTRY, DocExampleScatterTutorialDataZoom3.ENTRY,
            DocExampleScatterDataZoomAll.ENTRY, DocExampleSunburstSimple.ENTRY,
            DocExampleTextFregmentAlign.ENTRY, DocExampleLabelPosition.ENTRY,
            DocExampleCustomTransitionSimple.ENTRY, DocExampleValueAnimationSimple.ENTRY,
            DocExampleTreemapBorderColor.ENTRY, DocExamplePieMedia.ENTRY, DocExampleAriaDecal.ENTRY,
            DocExampleBarGridBarGap.ENTRY, DocExampleTutorialDynamicData.ENTRY,
            DocExampleTutorialLoading.ENTRY, DocExampleSunburstHighlightAncestor.ENTRY,
            DocExampleSunburstHighlightDescendant.ENTRY, DocExampleGraphicBounding.ENTRY,
            DocExampleTimelineDynamicSeries.ENTRY, DocExampleBarDataZoomFilterMode.ENTRY,
            DocExampleCandlestickAxisPointer.ENTRY, DocExampleGridOuterBounds.ENTRY,
            DocExampleMixTimelineAll.ENTRY, DocExampleTutorialAsync.ENTRY,
            DocExampleScatterVisualMapCategories.ENTRY, DocExampleMapExample.ENTRY,
            DocExampleMapVisualMapContinuous.ENTRY, DocExampleMapVisualMapContinuousText.ENTRY,
            DocExampleMapVisualMapPiecewiseText.ENTRY, DocExampleMapVisualMapPieces.ENTRY,
            DocExampleScatterVisualMapPiecewise.ENTRY,
        )
    }
}
