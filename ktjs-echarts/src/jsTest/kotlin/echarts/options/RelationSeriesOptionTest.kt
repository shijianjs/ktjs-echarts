package echarts.options

import echarts.GraphLayout
import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Covers the relation-series fields in `RelationSeriesOptions.kt`: the `graph` force and circular
 * layout sub-options, the graph series and edge data-item members, plus the `chord` and `sankey`
 * layout members. Each field is set through [unsafeJso] and read back so the typed property keeps
 * mapping to the exact JavaScript key; a final `setOption` feeds a real graph to prove ECharts
 * accepts it.
 */
class RelationSeriesOptionTest {

    @Test
    fun graphForceAndCircularOptionsRoundTrip() {
        val force = unsafeJso<GraphForceOption> {
            repulsion = DoubleOrDoubleArray(120.0)
            gravity = 0.25
            friction = 0.6
            edgeLength = DoubleOrDoubleArray(arrayOf(20.0, 80.0))
            layoutAnimation = false
        }
        assertEquals(120.0, force.repulsion!!.unsafeCast<Double>())
        assertEquals(0.25, force.gravity)
        assertEquals(0.6, force.friction)
        assertNotNull(force.edgeLength)
        assertEquals(false, force.layoutAnimation)

        val circular = unsafeJso<GraphCircularOption> {
            rotateLabel = true
        }
        assertEquals(true, circular.rotateLabel)
    }

    @Test
    fun graphSeriesAndEdgeFieldsRoundTrip() {
        val series = unsafeJso<GraphSeriesOption> {
            layoutAnimation = true
            edgeSymbolSize = DoubleOrDoubleArray(8.0)
            nodeScaleRatio = 0.4
            autoCurveness = BooleanOrDouble(0.3)
        }
        assertEquals(true, series.layoutAnimation)
        assertNotNull(series.edgeSymbolSize)
        assertEquals(0.4, series.nodeScaleRatio)
        assertEquals(0.3, series.autoCurveness!!.unsafeCast<Double>())

        val edge = unsafeJso<GraphEdgeItemOption> {
            ignoreForceLayout = true
        }
        assertEquals(true, edge.ignoreForceLayout)
    }

    @Test
    fun chordAndSankeyFieldsRoundTrip() {
        val chord = unsafeJso<ChordSeriesOption> {
            layoutIterations = 64.0
            padAngle = 0.02
        }
        assertEquals(64.0, chord.layoutIterations)
        assertEquals(0.02, chord.padAngle)

        val sankey = unsafeJso<SankeySeriesOption> {
            layoutIterations = 128.0
        }
        assertEquals(128.0, sankey.layoutIterations)

        val node = unsafeJso<SankeyNodeItemOption> {
            localX = 12.0
            localY = 30.0
        }
        assertEquals(12.0, node.localX)
        assertEquals(30.0, node.localY)
    }

    @Test
    fun graphSeriesIsAcceptedByECharts() {
        val graph = graphSeries {
            name = "graph-a"
            layout = GraphLayout("none")
            nodes = arrayOf(
                unsafeJso<GraphNodeItemOption> { name = "a" },
                unsafeJso<GraphNodeItemOption> { name = "b" },
            )
            links = arrayOf(
                unsafeJso<GraphEdgeItemOption> {
                    source = StringOrDouble("a")
                    target = StringOrDouble("b")
                    ignoreForceLayout = true
                },
            )
        }

        val (chart, container) = createChart(width = 480, height = 360)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = arrayOf(graph)
                },
            )
            assertEquals(1, chart.getOption().series?.size)
            assertNotNull(chart.getWidth())
        } finally {
            destroyChart(chart, container)
        }
    }
}
