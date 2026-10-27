package echarts.options

import echarts.AxisTypes
import echarts.LegendTypes
import echarts.SeriesTypes
import echarts.TooltipTriggers
import js.objects.unsafeJso
import kotlin.js.jsTypeOf
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import zrender.LinearGradientObject
import zrender.RadialGradientObject
import zrender.ZRColor
import zrender.gradientColorStop
import zrender.linearGradient
import zrender.radialGradient

/**
 * Tests for the option builders, the union wrappers, the gradient helpers and the series factories.
 *
 * These types are plain JavaScript objects, so the assertions read the values back through
 * `unsafeCast` the same way a JavaScript caller would.
 */
class OptionModelTest {
    @Test
    fun unsafeJsoCreatesAJavaScriptObjectAndAppliesTheBlock() {
        val option = unsafeJso<EChartsOption> {
            backgroundColor = ZRColor("#f5f5f5")
            animation = false
            animationDuration = AnimationDuration(1200.0)
            color = arrayOf(ZRColor("#111111"), ZRColor("#222222"))
        }

        assertEquals("object", jsTypeOf(option))
        assertEquals("#f5f5f5", option.backgroundColor.unsafeCast<String>())
        assertFalse(option.animation!!)
        assertEquals(1200.0, option.animationDuration!!.unsafeCast<Double>())
        assertEquals(2, option.color?.size)
        assertEquals("#222222", option.color!![1].unsafeCast<String>())
    }

    @Test
    fun optionBuilderSupportsNestedComponents() {
        val option = unsafeJso<EChartsOption> {
            title = one(unsafeJso<TitleOption> { text = "Sales"; subtext = "2026" })
            tooltip = one(unsafeJso<TooltipOption> { show = true; trigger = TooltipTriggers.axis })
            legend = one(unsafeJso<LegendOption> { show = true; type = LegendTypes.scroll })
            grid = one(unsafeJso<GridOption> { left = StringOrDouble("5%"); containLabel = true })
            xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = numbers(1.0, 2.0, 3.0) })
            yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
            series = one(lineSeries { name = "orders"; typedData = numbers(3.0, 5.0, 8.0) })
        }

        assertEquals("Sales", option.title!![0].text)
        assertEquals("2026", option.title!![0].subtext)
        assertEquals(TooltipTriggers.axis, option.tooltip!![0].trigger)
        assertEquals(LegendTypes.scroll, option.legend!![0].type)
        assertTrue(option.grid!![0].containLabel!!)
        assertEquals(AxisTypes.category, option.xAxis!![0].type)
        assertEquals(3, option.xAxis!![0].data?.size)
        assertEquals(SeriesTypes.line, option.series!![0].type)
        assertEquals("orders", option.series!![0].name)
        assertEquals(3, option.series!![0].data?.size)
    }

    @Test
    fun oneWrapsASingleValueInAJavaScriptArray() {
        val title = unsafeJso<TitleOption> { text = "single" }
        val array = one(title)
        assertEquals(1, array.size)
        assertEquals("single", array[0].text)
    }

    @Test
    fun numbersCreatesARealJavaScriptArray() {
        val data: Array<OptionDataItem?> = numbers(1.0, 2.5, -3.0)
        assertEquals(3, data.size)
        assertEquals(1.0, data[0].unsafeCast<Double>())
        assertEquals(2.5, data[1].unsafeCast<Double>())
        assertEquals(-3.0, data[2].unsafeCast<Double>())
        assertEquals("object", jsTypeOf(data))
    }

    @Test
    fun pointsCreatesNestedJavaScriptArrays() {
        val data: Array<OptionDataItem?> =
            points(doubleArrayOf(1.0, 2.0), doubleArrayOf(3.0, 4.0))
        assertEquals(2, data.size)
        val first = data[0].unsafeCast<Array<Double>>()
        assertEquals(2, first.size)
        assertEquals(1.0, first[0])
        assertEquals(2.0, first[1])
    }

    @Test
    fun itemsWrapsExistingObjects() {
        val data: Array<OptionDataItem?> = items(
            unsafeJso<OptionDataItemObject> { name = OptionId("first") },
            unsafeJso<OptionDataItemObject> { name = OptionId("second") },
        )
        assertEquals(2, data.size)
        assertEquals("first", data[0].unsafeCast<OptionDataItemObject>().name!!.unsafeCast<String>())
    }

    @Test
    fun stringOrDoubleWrapsBothBranches() {
        assertEquals("40%", StringOrDouble("40%").unsafeCast<String>())
        assertEquals(40.0, StringOrDouble(40.0).unsafeCast<Double>())
    }

    @Test
    fun booleanOrStringWrapsBothBranches() {
        assertTrue(BooleanOrString(true).unsafeCast<Boolean>())
        assertEquals("multiple", BooleanOrString("multiple").unsafeCast<String>())
    }

    @Test
    fun booleanOrDoubleWrapsBothBranches() {
        assertFalse(BooleanOrDouble(false).unsafeCast<Boolean>())
        assertEquals(0.5, BooleanOrDouble(0.5).unsafeCast<Double>())
    }

    @Test
    fun doubleOrDoubleArrayWrapsBothBranches() {
        assertEquals(4.0, DoubleOrDoubleArray(4.0).unsafeCast<Double>())
        val array = DoubleOrDoubleArray(arrayOf(4.0, 8.0)).unsafeCast<Array<Double>>()
        assertEquals(2, array.size)
        assertEquals(8.0, array[1])
    }

    @Test
    fun stringOrStringArrayWrapsBothBranches() {
        assertEquals("#fff", StringOrStringArray("#fff").unsafeCast<String>())
        val array = StringOrStringArray(arrayOf("#fff", "#000")).unsafeCast<Array<String>>()
        assertEquals(2, array.size)
        assertEquals("#000", array[1])
    }

    @Test
    fun gradientColorStopCarriesOffsetAndColor() {
        val stop = gradientColorStop(0.25, "#abcdef")
        assertEquals(0.25, stop.offset)
        assertEquals("#abcdef", stop.color)
    }

    @Test
    fun linearGradientBuildsTheUpstreamShape() {
        val gradient = linearGradient(
            fromX = 0.0,
            fromY = 0.0,
            toX = 1.0,
            toY = 1.0,
            colorStops = arrayOf(gradientColorStop(0.0, "#fff"), gradientColorStop(1.0, "#000")),
            global = false,
        ).unsafeCast<LinearGradientObject>()

        assertEquals("linear", gradient.type)
        assertEquals(0.0, gradient.x)
        assertEquals(1.0, gradient.y2)
        assertFalse(gradient.global!!)
        assertEquals(2, gradient.colorStops?.size)
        assertEquals("#000", gradient.colorStops!![1].color)
    }

    @Test
    fun radialGradientBuildsTheUpstreamShape() {
        val gradient = radialGradient(
            centerX = 0.5,
            centerY = 0.5,
            radius = 0.4,
            colorStops = arrayOf(gradientColorStop(0.0, "#fff")),
            global = true,
        ).unsafeCast<RadialGradientObject>()

        assertEquals("radial", gradient.type)
        assertEquals(0.5, gradient.x)
        assertEquals(0.4, gradient.r)
        assertTrue(gradient.global!!)
    }

    @Test
    fun seriesFactoriesSetTheTypeAndKeepTheBlock() {
        assertEquals(SeriesTypes.line, lineSeries { name = "l" }.also { assertEquals("l", it.name) }.type)
        assertEquals(SeriesTypes.bar, barSeries().type)
        assertEquals(SeriesTypes.pie, pieSeries().type)
        assertEquals(SeriesTypes.scatter, scatterSeries().type)
        assertEquals(SeriesTypes.effectScatter, effectScatterSeries().type)
        assertEquals(SeriesTypes.radar, radarSeries().type)
        assertEquals(SeriesTypes.map, mapSeries().type)
        assertEquals(SeriesTypes.tree, treeSeries().type)
        assertEquals(SeriesTypes.treemap, treemapSeries().type)
        assertEquals(SeriesTypes.sunburst, sunburstSeries().type)
        assertEquals(SeriesTypes.graph, graphSeries().type)
        assertEquals(SeriesTypes.chord, chordSeries().type)
        assertEquals(SeriesTypes.funnel, funnelSeries().type)
        assertEquals(SeriesTypes.gauge, gaugeSeries().type)
        assertEquals(SeriesTypes.pictorialBar, pictorialBarSeries().type)
        assertEquals(SeriesTypes.themeRiver, themeRiverSeries().type)
        assertEquals(SeriesTypes.custom, customSeries().type)
        assertEquals(SeriesTypes.boxplot, boxplotSeries().type)
        assertEquals(SeriesTypes.candlestick, candlestickSeries().type)
        assertEquals(SeriesTypes.heatmap, heatmapSeries().type)
        assertEquals(SeriesTypes.parallel, parallelSeries().type)
        assertEquals(SeriesTypes.lines, linesSeries().type)
        assertEquals(SeriesTypes.sankey, sankeySeries().type)
    }

    @Test
    fun seriesFactoriesAcceptTypedNestedOptions() {
        val series = lineSeries {
            itemStyle = unsafeJso<ItemStyleOption> {
                color = linearGradient(
                    0.0,
                    0.0,
                    0.0,
                    1.0,
                    arrayOf(gradientColorStop(0.0, "#fff"), gradientColorStop(1.0, "#000")),
                )
            }
            label = unsafeJso<LabelOption> {
                show = true
                formatter = LabelFormatter("{b}: {c}")
            }
            smooth = BooleanOrDouble(0.3)
        }
        assertNotNull(series.itemStyle)
        assertEquals("object", jsTypeOf(series.itemStyle))
        assertTrue(series.label!!.show!!)
        assertEquals(0.3, series.smooth!!.unsafeCast<Double>())
    }

    @Test
    fun formatterUnionsAcceptTemplatesAndCallbacks() {
        val template = LabelFormatter("{a}")
        assertEquals("{a}", template.unsafeCast<String>())

        val callback = LabelFormatter { params -> params.name }
        assertEquals("function", jsTypeOf(callback))

        val tooltipTemplate = TooltipFormatter("{b}")
        assertEquals("{b}", tooltipTemplate.unsafeCast<String>())
        val tooltipCallback = TooltipFormatter { params ->
            TooltipContent(params.unsafeCast<Array<CallbackDataParams>>().size.toString())
        }
        assertEquals("function", jsTypeOf(tooltipCallback))
        val callTooltip = tooltipCallback.unsafeCast<(TopLevelFormatterParams) -> TooltipContent>()
        assertEquals(
            "0",
            callTooltip(TopLevelFormatterParams(emptyArray())).unsafeCast<String>(),
        )

        val valueFormatter = TooltipValueFormatter("{c}")
        assertEquals("{c}", valueFormatter.unsafeCast<String>())
        assertEquals("function", jsTypeOf(TooltipValueFormatter { value, _ -> value.toString() }))
    }
}
