package demo.examples.matrix

import demo.CatalogEntry

/**
 * Matrix examples, in navigation order.
 *
 * The entries are the example classes' own companions; this list only orders them, lazily —
 * the classes sit in this same package, so nothing here has to be imported or initialised
 * ahead of anything else.
 */
internal object MatrixEntries {
    val ENTRIES: List<CatalogEntry> by lazy {
        listOf(
            MatrixSimple.ENTRY, MatrixPie.ENTRY, MatrixCorrelationHeatmap.ENTRY,
            MatrixCorrelationScatter.ENTRY, MatrixCovariance.ENTRY, MatrixGraph.ENTRY,
            MatrixConfusion.ENTRY, MatrixMiniBarDataCollection.ENTRY, MatrixSparkline.ENTRY,
            MatrixMiniBarGeo.ENTRY, MatrixGridLayout.ENTRY, MatrixMbti.ENTRY, MatrixPeriodicTable.ENTRY,
            MatrixStock.ENTRY,
        )
    }
}
