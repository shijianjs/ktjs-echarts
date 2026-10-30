package echarts.options

import echarts.AxisTypes
import echarts.createChart
import echarts.destroyChart
import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Covers the cartesian-series fields in `CartesianSeriesOptions.kt`: the shared `SeriesOption`
 * members, the line/bar specific styling, and the special fields of the scatter, effect-scatter,
 * candlestick, boxplot, heatmap, parallel and custom series plus the `rippleEffect`, `effect` and
 * `barGrid` sub-options. Each field is set through [unsafeJso] and read back so the typed property
 * keeps mapping to the exact JavaScript key; a real `setOption` proves ECharts accepts the tree.
 */
class CartesianSeriesOptionTest {

    @Test
    fun baseSeriesOptionSharedFieldsRoundTrip() {
        val series = unsafeJso<LineSeriesOption> {
            colorBy = "series"
            labelLine = unsafeJso<LabelLineOption> { show = true }
            datasetId = "my-dataset"
            universalTransition = BooleanOrUniversalTransitionOption(true)
            progressive = 4000.0
            progressiveThreshold = 3000.0
            legendHoverLink = false
            dataGroupId = OptionId("group-1")
            blendMode = "lighter"
            hoverLayerThreshold = 3000.0
        }
        assertEquals("series", series.colorBy)
        assertNotNull(series.labelLine)
        assertEquals("my-dataset", series.datasetId)
        assertEquals(true, series.universalTransition.unsafeCast<Boolean>())
        assertEquals(4000.0, series.progressive)
        assertEquals(3000.0, series.progressiveThreshold)
        assertEquals(false, series.legendHoverLink)
        assertEquals("group-1", series.dataGroupId.unsafeCast<String>())
        assertEquals("lighter", series.blendMode)
        assertEquals(3000.0, series.hoverLayerThreshold)
    }

    @Test
    fun lineAndBarSeriesFieldsRoundTrip() {
        val line = lineSeries {
            clip = false
            smoothMonotone = "x"
            connectNulls = true
            showAllSymbol = BooleanOrString(true)
            endLabel = unsafeJso<LabelOption> { show = true }
            triggerLineEvent = true
            sampling = "lttb"
            samplingThreshold = 500.0
            stackStrategy = "samesign"
            showBackground = true
        }
        assertEquals(false, line.clip)
        assertEquals("x", line.smoothMonotone)
        assertEquals(true, line.connectNulls)
        assertNotNull(line.showAllSymbol)
        assertNotNull(line.endLabel)
        assertEquals(true, line.triggerLineEvent)
        assertEquals("lttb", line.sampling)
        assertEquals(500.0, line.samplingThreshold)
        assertEquals("samesign", line.stackStrategy)
        assertEquals(true, line.showBackground)

        val bar = barSeries {
            stackStrategy = "all"
            stackOrder = "seriesDesc"
            barMaxWidth = StringOrDouble(30.0)
            barMinWidth = StringOrDouble("5")
            barCategoryGap = StringOrDouble("20%")
            barMinHeight = 1.0
            barMinAngle = 30.0
            showBackground = true
            realtimeSort = true
            sampling = "avg"
        }
        assertEquals("all", bar.stackStrategy)
        assertEquals("seriesDesc", bar.stackOrder)
        assertEquals(30.0, bar.barMaxWidth.unsafeCast<Double>())
        assertEquals("5", bar.barMinWidth.unsafeCast<String>())
        assertEquals("20%", bar.barCategoryGap.unsafeCast<String>())
        assertEquals(1.0, bar.barMinHeight)
        assertEquals(30.0, bar.barMinAngle)
        assertEquals(true, bar.showBackground)
        assertEquals(true, bar.realtimeSort)
        assertEquals("avg", bar.sampling)
    }

    @Test
    fun scatterCandlestickHeatmapAndOthersRoundTrip() {
        val scatter = scatterSeries {
            sampling = "min"
            clip = true
        }
        assertEquals("min", scatter.sampling)
        assertEquals(true, scatter.clip)

        val ripple = unsafeJso<RippleEffectOption> {
            period = 3.0
        }
        assertEquals(3.0, ripple.period)

        val effect = effectScatterSeries {
            effectType = "ripple"
            showEffectOn = "render"
            clip = false
            rippleEffect = ripple
        }
        assertEquals("ripple", effect.effectType)
        assertEquals("render", effect.showEffectOn)
        assertEquals(false, effect.clip)
        assertNotNull(effect.rippleEffect)

        val candle = unsafeJso<CandlestickSeriesOption> {
            barMaxWidth = StringOrDouble(20.0)
            barMinWidth = StringOrDouble(2.0)
            barCategoryGap = "30%"
            clip = true
        }
        assertEquals(20.0, candle.barMaxWidth.unsafeCast<Double>())
        assertEquals(2.0, candle.barMinWidth.unsafeCast<Double>())
        assertEquals("30%", candle.barCategoryGap)
        assertEquals(true, candle.clip)

        val box = unsafeJso<BoxplotSeriesOption> {
            boxWidth = DoubleOrDoubleArray(40.0)
            clip = true
        }
        assertNotNull(box.boxWidth)
        assertEquals(true, box.clip)

        val heat = unsafeJso<HeatmapSeriesOption> {
            blurSize = 5.0
            minOpacity = 0.2
            maxOpacity = 0.9
            clip = false
        }
        assertEquals(5.0, heat.blurSize)
        assertEquals(0.2, heat.minOpacity)
        assertEquals(0.9, heat.maxOpacity)
        assertEquals(false, heat.clip)

        val parallel = unsafeJso<ParallelSeriesOption> {
            inactiveOpacity = 0.05
            activeOpacity = 0.8
        }
        assertEquals(0.05, parallel.inactiveOpacity)
        assertEquals(0.8, parallel.activeOpacity)

        val custom = unsafeJso<CustomSeriesOption> {
            clip = true
            itemPayload = recordOf<String, JsAny>("kind" to "cell".unsafeCast<JsAny>())
        }
        assertEquals(true, custom.clip)
        assertNotNull(custom.itemPayload)

        val linesEffect = unsafeJso<LinesEffectOption> {
            period = 4.0
            trailLength = 0.2
            constantSpeed = 60.0
        }
        assertEquals(4.0, linesEffect.period)
        assertEquals(0.2, linesEffect.trailLength)
        assertEquals(60.0, linesEffect.constantSpeed)

        val barGrid = unsafeJso<BarGridLayoutOptionForCustomSeries> {
            barMaxWidth = StringOrDouble(25.0)
            barMinWidth = StringOrDouble(3.0)
            barCategoryGap = StringOrDouble("15%")
        }
        assertEquals(25.0, barGrid.barMaxWidth.unsafeCast<Double>())
        assertEquals(3.0, barGrid.barMinWidth.unsafeCast<Double>())
        assertEquals("15%", barGrid.barCategoryGap.unsafeCast<String>())
    }

    @Test
    fun seriesOptionsAreAcceptedByECharts() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = arrayOf(
                        lineSeries { sampling = "lttb"; showBackground = true; typedData = numbers(1.0, 2.0) },
                        barSeries { barMaxWidth = StringOrDouble(18.0); stackOrder = "seriesDesc"; typedData = numbers(3.0, 4.0) },
                        scatterSeries { sampling = "min"; typedData = numbers(5.0, 6.0) },
                        effectScatterSeries {
                            effectType = "ripple"
                            rippleEffect = unsafeJso<RippleEffectOption> { period = 2.0 }
                            typedData = numbers(7.0, 8.0)
                        },
                    )
                },
            )
            assertEquals(4, chart.getOption().series?.size)
        } finally {
            destroyChart(chart, container)
        }
    }
}
