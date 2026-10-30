package echarts.options

import echarts.AxisPointerType
import echarts.AxisTypes
import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import web.canvas.CanvasLineCap
import web.canvas.CanvasLineJoin
import zrender.ZRColor

/**
 * Covers the component-level styling and behaviour fields on `Title`, `Legend`, `Tooltip` and the
 * coordinate `Axis`. The fields are set through [unsafeJso] builders and read back so the typed
 * properties keep mapping to the exact upstream JavaScript keys; a real `setOption` proves ECharts
 * itself accepts the whole tree together.
 */
class ComponentTitleLegendTooltipAxisOptionTest {

    @Test
    fun titleAndLegendComponentFieldsRoundTrip() {
        val title = unsafeJso<TitleOption> {
            text = "Chart"
            subtext = "Sub"
            sublink = "https://example.test/docs"
            textVerticalAlign = "top"
            subtextStyle = unsafeJso<TextStyleOption> {
                color = "#444"
            }
        }
        assertEquals("https://example.test/docs", title.sublink)
        assertEquals("top", title.textVerticalAlign)
        assertNotNull(title.subtextStyle)
        assertEquals("#444", title.subtextStyle?.color)

        val itemStyle = unsafeJso<LegendItemStyleOption> {
            borderCap = "round".unsafeCast<CanvasLineCap>()
            borderJoin = "bevel".unsafeCast<CanvasLineJoin>()
            borderDashOffset = 2.0
            borderMiterLimit = 6.0
            areaColor = ZRColor("#eee")
        }
        assertEquals("round", itemStyle.borderCap.unsafeCast<String>())
        assertEquals("bevel", itemStyle.borderJoin.unsafeCast<String>())
        assertEquals(2.0, itemStyle.borderDashOffset)
        assertEquals(6.0, itemStyle.borderMiterLimit)
        assertNotNull(itemStyle.areaColor)

        val lineStyle = unsafeJso<LegendLineStyleOption> {
            dashOffset = 1.0
            cap = "butt".unsafeCast<CanvasLineCap>()
            join = "miter".unsafeCast<CanvasLineJoin>()
        }
        assertEquals(1.0, lineStyle.dashOffset)
        assertEquals("butt", lineStyle.cap.unsafeCast<String>())
        assertEquals("miter", lineStyle.join.unsafeCast<String>())

        val legend = unsafeJso<LegendOption> {
            inactiveBorderColor = "#999"
            pageIconColor = "#000"
            pageIconInactiveColor = "#bbb"
            pageIconSize = 14.0
            pageTextStyle = unsafeJso<LabelOption> { color = "#333" }
            pageFormatter = LabelFormatter("{current}/{total}")
            scrollDataIndex = 2.0
            this.itemStyle = itemStyle
            this.lineStyle = lineStyle
        }
        assertEquals("#999", legend.inactiveBorderColor)
        assertEquals("#000", legend.pageIconColor)
        assertEquals("#bbb", legend.pageIconInactiveColor)
        assertEquals(14.0, legend.pageIconSize)
        assertNotNull(legend.pageTextStyle)
        assertNotNull(legend.pageFormatter)
        assertEquals(2.0, legend.scrollDataIndex)

        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    this.title = one(title)
                    this.legend = one(legend)
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series = one(lineSeries { typedData = numbers(1.0, 2.0) })
                },
            )
            assertEquals(1, chart.getOption().series?.size)
            assertTrue(chart.getWidth() > 0.0)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun tooltipAndAxisComponentFieldsRoundTrip() {
        val tooltip = unsafeJso<TooltipOption> {
            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerType("cross") }
            showContent = true
            alwaysShowContent = false
            showDelay = 100.0
            hideDelay = 200.0
            enterable = true
            confine = true
            className = "tip"
            transitionDuration = 0.4
            extraCssText = "box-shadow: 0 0 4px #000;"
        }
        assertNotNull(tooltip.axisPointer)
        assertEquals(true, tooltip.showContent)
        assertEquals(false, tooltip.alwaysShowContent)
        assertEquals(100.0, tooltip.showDelay)
        assertEquals(200.0, tooltip.hideDelay)
        assertEquals(true, tooltip.enterable)
        assertEquals(true, tooltip.confine)
        assertEquals("tip", tooltip.className)
        assertEquals(0.4, tooltip.transitionDuration)
        assertEquals("box-shadow: 0 0 4px #000;", tooltip.extraCssText)

        val axis = unsafeJso<AxisOption> {
            type = AxisTypes.value
            alignTicks = true
            nameTextStyle = unsafeJso<LabelOption> { color = "#222" }
            nameTruncate = unsafeJso<NameTruncateOption> { placeholder = "…" }
            nameGap = 20.0
            nameRotate = 45.0
            nameMoveOverlap = BooleanOrString(true)
            minInterval = 1.0
            maxInterval = 100.0
            logBase = 10.0
            axisTick = unsafeJso<AxisTickOption> { alignWithLabel = true }
            minorTick = unsafeJso<AxisTickOption>()
            splitArea = unsafeJso<SplitAreaOption>()
            minorSplitLine = unsafeJso<SplitLineOption> { showMinLine = true; showMaxLine = false }
            splitLine = unsafeJso<SplitLineOption> { showMinLine = false; showMaxLine = true }
            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerType("shadow") }
            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString("0"); onZeroAxisIndex = 1.0 }
        }
        assertEquals(true, axis.alignTicks)
        assertNotNull(axis.nameTextStyle)
        assertEquals("…", axis.nameTruncate?.placeholder)
        assertEquals(20.0, axis.nameGap)
        assertEquals(45.0, axis.nameRotate)
        assertNotNull(axis.nameMoveOverlap)
        assertEquals(1.0, axis.minInterval)
        assertEquals(100.0, axis.maxInterval)
        assertEquals(10.0, axis.logBase)
        assertEquals(true, axis.axisTick?.alignWithLabel)
        assertNotNull(axis.minorTick)
        assertNotNull(axis.splitArea)
        assertEquals(true, axis.minorSplitLine?.showMinLine)
        assertNotNull(axis.axisPointer)
        assertNotNull(axis.axisLine)

        val breakArea = unsafeJso<AxisBreakAreaOption> {
            zigzagMinSpan = 0.2
            zigzagMaxSpan = 0.8
            zigzagZ = 3.0
        }
        assertEquals(0.2, breakArea.zigzagMinSpan)
        assertEquals(0.8, breakArea.zigzagMaxSpan)
        assertEquals(3.0, breakArea.zigzagZ)
    }
}
