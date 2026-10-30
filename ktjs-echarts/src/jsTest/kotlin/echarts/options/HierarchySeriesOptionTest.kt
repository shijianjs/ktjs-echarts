package echarts.options

import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Covers the hierarchy-series fields in `HierarchySeriesOptions.kt`: the `tree`, `treemap` and
 * `sunburst` series options, their node data items and the nested `visual`, `state`, `breadcrumb`
 * sub-options. Each field is set through [unsafeJso] and read back so the typed property keeps
 * mapping to the exact JavaScript key; a final `setOption` feeds real trees of all three series to
 * prove ECharts accepts them.
 */
class HierarchySeriesOptionTest {

    @Test
    fun treeSeriesFieldsRoundTrip() {
        val series = unsafeJso<TreeSeriesOption> {
            initialTreeDepth = 2.0
            expandAndCollapse = true
            scaleLimit = unsafeJso<ScaleLimitOption> {
                min = 0.5
                max = 3.0
            }
        }
        assertEquals(2.0, series.initialTreeDepth)
        assertEquals(true, series.expandAndCollapse)
        assertNotNull(series.scaleLimit)

        val leaf = unsafeJso<TreeSeriesNodeItemOption> {
            collapsed = true
        }
        assertEquals(true, leaf.collapsed)
    }

    @Test
    fun treemapSeriesFieldsRoundTrip() {
        val series = unsafeJso<TreemapSeriesOption> {
            squareRatio = 0.6
            leafDepth = 1.0
            drillDownIcon = "arrow"
            zoomToNodeRatio = 0.1
            nodeGap = 2.0
            nodeWidth = 3.0
            upperLabel = unsafeJso<LabelOption> {
                show = true
                color = "#222222"
            }
        }
        assertEquals(0.6, series.squareRatio)
        assertEquals(1.0, series.leafDepth)
        assertEquals("arrow", series.drillDownIcon)
        assertEquals(0.1, series.zoomToNodeRatio)
        assertEquals(2.0, series.nodeGap)
        assertEquals(3.0, series.nodeWidth)
        assertNotNull(series.upperLabel)

        val visual = unsafeJso<TreemapSeriesVisualOption> {
            visualDimension = StringOrDouble(0.0)
            visualMin = 10.0
            visualMax = 100.0
            visibleMin = 300.0
            childrenVisibleMin = 50.0
        }
        assertEquals(10.0, visual.visualMin)
        assertEquals(100.0, visual.visualMax)
        assertEquals(300.0, visual.visibleMin)
        assertEquals(50.0, visual.childrenVisibleMin)
        assertNotNull(visual.visualDimension)

        val breadcrumb = unsafeJso<TreemapBreadcrumbOption> {
            emptyItemWidth = 10.0
        }
        assertEquals(10.0, breadcrumb.emptyItemWidth)

        val state = unsafeJso<TreemapStateOption> {
            upperLabel = unsafeJso<LabelOption> {
                show = false
                color = "#555555"
            }
        }
        assertNotNull(state.upperLabel)
    }

    @Test
    fun sunburstSeriesFieldsRoundTrip() {
        val series = unsafeJso<SunburstSeriesOption> {
            minShowLabelAngle = 5.0
            stillShowZeroSum = false
            renderLabelForZeroData = true
            nodeGap = 1.0
            nodeWidth = 4.0
            percentPrecision = 2.0
        }
        assertEquals(5.0, series.minShowLabelAngle)
        assertEquals(false, series.stillShowZeroSum)
        assertEquals(true, series.renderLabelForZeroData)
        assertEquals(1.0, series.nodeGap)
        assertEquals(4.0, series.nodeWidth)
        assertEquals(2.0, series.percentPrecision)

        val node = unsafeJso<SunburstSeriesNodeItemOption> {
            collapsed = false
        }
        assertEquals(false, node.collapsed)
    }

    @Test
    fun hierarchySeriesAreAcceptedByECharts() {
        val tree = treeSeries {
            name = "tree-a"
            initialTreeDepth = 1.0
            typedData = items(
                unsafeJso<TreeSeriesNodeItemOption> {
                    name = OptionId("root")
                    value = OptionDataValue(3.0)
                    children = arrayOf(unsafeJso<TreeSeriesNodeItemOption> { name = OptionId("leaf") })
                },
            )
        }
        val treemap = treemapSeries {
            name = "treemap-a"
            squareRatio = 0.8
            typedData = items(
                unsafeJso<TreemapSeriesNodeItemOption> {
                    name = OptionId("root")
                    value = OptionDataValue(100.0)
                    children = arrayOf(unsafeJso<TreemapSeriesNodeItemOption> { name = OptionId("child") })
                },
            )
        }
        val sunburst = sunburstSeries {
            name = "sunburst-a"
            minShowLabelAngle = 3.0
            typedData = items(
                unsafeJso<SunburstSeriesNodeItemOption> {
                    name = OptionId("root")
                    value = OptionDataValue(10.0)
                    children = arrayOf(unsafeJso<SunburstSeriesNodeItemOption> { name = OptionId("child") })
                },
            )
        }

        val (chart, container) = createChart(width = 480, height = 360)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = arrayOf(tree, treemap, sunburst)
                },
            )
            assertEquals(3, chart.getOption().series?.size)
            assertEquals(1.0, chart.getOption().series?.get(0).unsafeCast<TreeSeriesOption>()?.initialTreeDepth)
            assertEquals(0.8, chart.getOption().series?.get(1).unsafeCast<TreemapSeriesOption>()?.squareRatio)
            assertEquals(3.0, chart.getOption().series?.get(2).unsafeCast<SunburstSeriesOption>()?.minShowLabelAngle)
            assertNotNull(chart.getWidth())
        } finally {
            destroyChart(chart, container)
        }
    }
}
