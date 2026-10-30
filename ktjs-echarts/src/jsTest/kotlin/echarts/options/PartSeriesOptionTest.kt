package echarts.options

import echarts.PieAnimationTypeUpdate
import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import zrender.ZRColor

/**
 * Covers the `pie`, `funnel`, `gauge`, `map` and pictorial-bar members of `PartSeriesOptions.kt`.
 * Each field is set through [unsafeJso] and read back so the typed property keeps mapping to the
 * exact JavaScript key; a final `setOption` feeds real `pie`, `funnel` and `gauge` series to prove
 * ECharts accepts them.
 */
class PartSeriesOptionTest {

    @Test
    fun pieSeriesFieldsRoundTrip() {
        val series = unsafeJso<PieSeriesOption> {
            avoidLabelOverlap = true
            selectedOffset = 10.0
            animationTypeUpdate = PieAnimationTypeUpdate("expansion")
            showEmptyCircle = true
            emptyCircleStyle = unsafeJso<ItemStyleOption> {
                color = ZRColor("#eeeeee")
            }
        }
        assertEquals(true, series.avoidLabelOverlap)
        assertEquals(10.0, series.selectedOffset)
        assertEquals("expansion", series.animationTypeUpdate!!.unsafeCast<String>())
        assertEquals(true, series.showEmptyCircle)
        assertNotNull(series.emptyCircleStyle)
    }

    @Test
    fun funnelAndGaugeFieldsRoundTrip() {
        val funnel = unsafeJso<FunnelSeriesOption> {
            minSize = StringOrDouble("0%")
            maxSize = StringOrDouble(200.0)
        }
        assertEquals("0%", funnel.minSize!!.unsafeCast<String>())
        assertEquals(200.0, funnel.maxSize!!.unsafeCast<Double>())

        val pointer = unsafeJso<GaugePointerOption> {
            keepAspect = true
        }
        assertEquals(true, pointer.keepAspect)

        val anchor = unsafeJso<GaugeAnchorOption> {
            showAbove = false
            keepAspect = true
        }
        assertEquals(false, anchor.showAbove)
        assertEquals(true, anchor.keepAspect)
    }

    @Test
    fun mapAndPictorialBarFieldsRoundTrip() {
        val map = unsafeJso<MapSeriesOption> {
            boundingCoords = arrayOf(arrayOf(116.0, 39.0), arrayOf(122.0, 41.0))
            mapValueCalculation = "sum"
            showLegendSymbol = true
        }
        assertEquals(2, map.boundingCoords?.size)
        assertEquals("sum", map.mapValueCalculation)
        assertEquals(true, map.showLegendSymbol)

        val symbol = unsafeJso<PictorialBarSeriesSymbolOption> {
            symbolRepeat = BooleanOrDoubleOrString("repeat")
            symbolClip = true
            symbolPatternSize = 400.0
        }
        assertEquals("repeat", symbol.symbolRepeat!!.unsafeCast<String>())
        assertEquals(true, symbol.symbolClip)
        assertEquals(400.0, symbol.symbolPatternSize)
    }

    @Test
    fun partSeriesAreAcceptedByECharts() {
        val pie = pieSeries {
            name = "pie-a"
            avoidLabelOverlap = true
            selectedOffset = 8.0
            animationTypeUpdate = PieAnimationTypeUpdate("transition")
            typedData = items(
                unsafeJso<OptionDataItemObject> { name = OptionId("x"); value = OptionDataValue(3.0) },
                unsafeJso<OptionDataItemObject> { name = OptionId("y"); value = OptionDataValue(5.0) },
            )
        }
        val funnel = funnelSeries {
            name = "funnel-a"
            minSize = StringOrDouble(20.0)
            maxSize = StringOrDouble(260.0)
            typedData = numbers(60.0, 40.0)
        }
        val gauge = gaugeSeries {
            name = "gauge-a"
            pointer = unsafeJso<GaugePointerOption> { keepAspect = true }
            anchor = unsafeJso<GaugeAnchorOption> { showAbove = true }
            typedData = numbers(30.0)
        }

        val (chart, container) = createChart(width = 480, height = 360)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = arrayOf(pie, funnel, gauge)
                },
            )
            assertEquals(3, chart.getOption().series?.size)
            assertNotNull(chart.getWidth())
        } finally {
            destroyChart(chart, container)
        }
    }
}
