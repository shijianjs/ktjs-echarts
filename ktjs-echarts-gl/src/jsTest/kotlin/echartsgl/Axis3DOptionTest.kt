package echartsgl

import echarts.AxisTypes
import echarts.ECharts
import echarts.options.AxisInterval
import echarts.options.BooleanOrStringArray
import echarts.options.LabelFormatter
import echarts.options.StringOrDouble
import echarts.options.ZRColorOrArray
import echarts.options.one
import echartsgl.options.Axis3DOption
import echartsgl.options.Axis3DPointerFormatter
import echartsgl.options.Axis3DPointerLabelOption
import echartsgl.options.EChartsGlOption
import echartsgl.options.Grid3DOption
import echartsgl.options.bar3DSeries
import js.objects.unsafeJso
import kotlin.js.jsTypeOf
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import zrender.ZRColor

/**
 * Tests the line, tick, label, split and pointer sub-options shared by `grid3D` and by the three
 * `xAxis3D` / `yAxis3D` / `zAxis3D` axes.
 *
 * `echarts-gl` has no upstream unit suite for these types, so the cases cover the wrapper-specific
 * contract: the nested option objects are accepted, the `@JsName` views such as `typedColor` reach
 * the key upstream reads, and the fields survive the `getOption` round trip.
 */
class Axis3DOptionTest {
    /**
     * `grid3D` carries the default of every axis setting, so all six sub-options round trip on the
     * component itself.
     */
    @Test
    fun grid3DAxisDefaultsRoundTrip() {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        try {
            chart.setOption(axisStyleScene(grid3D = { gridDefaults() }))

            val grid = chart.getOption().unsafeCast<EChartsGlOption>().grid3D!![0]
            assertEquals(true, grid.axisLine!!.show)
            assertEquals(2.0, grid.axisLine!!.lineStyle!!.width)
            assertEquals("#aaa", grid.axisLine!!.lineStyle!!.color!!.unsafeCast<String>())
            assertEquals(true, grid.axisTick!!.show)
            assertEquals(3.0, grid.axisTick!!.length)
            assertEquals(false, grid.axisTick!!.inside)
            assertEquals(2.0, grid.axisTick!!.interval!!.unsafeCast<Double>())
            assertEquals(6.0, grid.axisLabel!!.margin)
            assertEquals(20.0, grid.axisLabel!!.rotate)
            assertEquals("{value}", grid.axisLabel!!.formatter!!.unsafeCast<String>())
            assertEquals("auto", grid.axisLabel!!.interval!!.unsafeCast<String>())
            // ECharts flattens the deprecated `axisLabel.textStyle` onto the label itself and leaves
            // the nested object null, unlike `axisPointer.label.textStyle`, which it keeps.
            assertNull(grid.axisLabel!!.textStyle)
            assertEquals("#112233", grid.axisLabel!!.color)
            assertEquals(12.0, grid.axisLabel!!.fontSize!!.unsafeCast<Double>())
            assertEquals(true, grid.splitLine!!.show)
            // typedColor is the @JsName("color") view of a palette, so it must be on `color`.
            assertEquals("#ff0000", grid.splitLine!!.lineStyle!!.typedColor!!.unsafeCast<String>())
            assertEquals(true, grid.splitArea!!.show)
            assertEquals(2, grid.splitArea!!.areaStyle!!.typedColor!!.size)
            assertEquals(true, grid.axisPointer!!.show)
            assertEquals(1.5, grid.axisPointer!!.lineStyle!!.width)
            assertEquals(true, grid.axisPointer!!.label!!.show)
            assertEquals(8.0, grid.axisPointer!!.label!!.margin)
            assertEquals("#ffffff", grid.axisPointer!!.label!!.textStyle!!.color)
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * The same six sub-options exist on a single axis, where they override the `grid3D` default for
     * that axis only; its pointer label formatter takes two arguments, which the 2D `LabelFormatter`
     * union does not.
     */
    @Test
    fun axis3DSubOptionsRoundTrip() {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        try {
            chart.setOption(axisStyleScene(xAxis3D = { axisStyles() }))

            val axis = chart.getOption().unsafeCast<EChartsGlOption>().xAxis3D!![0]
            assertEquals(false, axis.axisLine!!.show)
            assertEquals(4.0, axis.axisLine!!.lineStyle!!.width)
            assertEquals(true, axis.axisTick!!.show)
            assertEquals(5.0, axis.axisTick!!.length)
            assertEquals(false, axis.axisLabel!!.inside)
            assertEquals(1.0, axis.axisLabel!!.margin)
            assertEquals(true, axis.splitLine!!.show)
            assertEquals(false, axis.splitArea!!.show)
            assertEquals(2, axis.splitArea!!.areaStyle!!.typedColor!!.size)
            assertEquals("#cccccc", axis.splitArea!!.areaStyle!!.typedColor!![1].unsafeCast<String>())
            assertEquals(true, axis.axisPointer!!.show)

            val format = axis.axisPointer!!.label!!.formatter
            assertEquals("function", jsTypeOf(format), "the callback reaches JavaScript as a function")
            assertEquals(
                "12/2",
                format!!.unsafeCast<(Double, Array<Double>) -> String>()(12.5, arrayOf(1.0, 2.0)),
            )
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * The axis-level fields that are not one of the six sub-options: identity, inversion, the name
     * style, and the scale limits an axis shares with the 2D axes.
     */
    @Test
    fun axis3DBasicOptionsRoundTrip() {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        try {
            chart.setOption(axisStyleScene(xAxis3D = { axisBasics() }))

            val axis = chart.getOption().unsafeCast<EChartsGlOption>().xAxis3D!![0]
            assertEquals("ktjs-x", axis.id)
            assertEquals(true, axis.inverse)
            assertEquals(true, axis.scale)
            assertEquals(0.5, axis.minInterval)
            assertEquals(2.0, axis.maxInterval)
            assertEquals(10.0, axis.logBase)
            assertEquals(true, axis.boundaryGap!!.unsafeCast<Boolean>())
            assertEquals("#aabbcc", axis.nameTextStyle!!.color)
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * [Axis3DPointerFormatter] wraps the callback instead of copying it, so `ViewGL` and the caller
     * hold the same function.
     */
    @Test
    fun axisPointerFormatterWrapsTheCallback() {
        val format = Axis3DPointerFormatter { value, valueAll -> "${value.toInt()}/${valueAll.size}" }
        assertEquals("function", jsTypeOf(format))
        assertEquals(
            "1/2",
            format.unsafeCast<(Double, Array<Double>) -> String>()(1.5, arrayOf(1.0, 2.0)),
        )

        val label = unsafeJso<Axis3DPointerLabelOption> { formatter = format }
        assertTrue(hasOwn(label, "formatter"), "the label keeps the documented key")
        assertSame(format, label.formatter)
    }

    /**
     * Fills the six `grid3D` axis defaults with the values [grid3DAxisDefaultsRoundTrip] asserts.
     */
    private fun Grid3DOption.gridDefaults() {
        axisLine = unsafeJso {
            show = true
            lineStyle = unsafeJso { width = 2.0; color = ZRColor("#aaa") }
        }
        axisTick = unsafeJso {
            show = true
            length = 3.0
            inside = false
            interval = AxisInterval(2.0)
        }
        axisLabel = unsafeJso {
            margin = 6.0
            rotate = 20.0
            interval = AxisInterval("auto")
            formatter = LabelFormatter("{value}")
            textStyle = unsafeJso { color = "#112233"; fontSize = StringOrDouble(12.0) }
        }
        splitLine = unsafeJso {
            show = true
            lineStyle = unsafeJso { typedColor = ZRColorOrArray(ZRColor("#ff0000")) }
        }
        splitArea = unsafeJso {
            show = true
            areaStyle = unsafeJso { typedColor = arrayOf(ZRColor("#00ff00"), ZRColor("#0000ff")) }
        }
        axisPointer = unsafeJso {
            show = true
            lineStyle = unsafeJso { width = 1.5 }
            label = unsafeJso { show = true; margin = 8.0; textStyle = unsafeJso { color = "#ffffff" } }
        }
    }

    /**
     * Fills the same six sub-options on one axis, with the pointer label formatted by a callback.
     */
    private fun Axis3DOption.axisStyles() {
        axisLine = unsafeJso { show = false; lineStyle = unsafeJso { width = 4.0 } }
        axisTick = unsafeJso { show = true; length = 5.0 }
        axisLabel = unsafeJso { show = true; inside = false; margin = 1.0 }
        splitLine = unsafeJso { show = true }
        splitArea = unsafeJso {
            show = false
            areaStyle = unsafeJso { typedColor = arrayOf(ZRColor("#eeeeee"), ZRColor("#cccccc")) }
        }
        axisPointer = unsafeJso {
            show = true
            label = unsafeJso {
                show = true
                formatter = Axis3DPointerFormatter { value, valueAll ->
                    "${value.toInt()}/${valueAll.size}"
                }
            }
        }
    }

    /**
     * Fills the axis-level fields that [axis3DBasicOptionsRoundTrip] reads back.
     */
    private fun Axis3DOption.axisBasics() {
        id = "ktjs-x"
        inverse = true
        scale = true
        minInterval = 0.5
        maxInterval = 2.0
        logBase = 10.0
        boundaryGap = BooleanOrStringArray(true)
        nameTextStyle = unsafeJso { color = "#aabbcc" }
    }

    /**
     * Builds a minimal `bar3D` scene: one grid, three value axes and one series, so both the grid and
     * the axes are created.
     */
    private fun axisStyleScene(
        grid3D: Grid3DOption.() -> Unit = {},
        xAxis3D: Axis3DOption.() -> Unit = {},
    ): EChartsGlOption = unsafeJso {
        this.grid3D = one(unsafeJso<Grid3DOption> { boxWidth = 100.0; grid3D() })
        this.xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value; xAxis3D() })
        this.yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        this.zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        series = one(
            bar3DSeries {
                typedData = bar3DItems(0.0, 0.0, 3.0, 1.0, 1.0, 5.0)
            },
        )
    }
}
