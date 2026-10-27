package echarts.options

import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import zrender.ZRColor
import zrender.ZRLineTypes

/**
 * Tests for the legend-specific style types.
 *
 * Upstream `LegendStyleOption` is shared by `LegendOption` and `DataItem`; `itemStyle` and
 * `lineStyle` use `LegendItemStyleOption` / `LegendLineStyleOption`, which add the `"auto"` and
 * `"inherit"` string forms to the generic [`ItemStyleOption`] / [`LineStyleOption`].
 */
class LegendOptionTest {
    @Test
    fun legendAndItsDataShareTheSameStyleProperties() {
        val component: LegendStyleOption = unsafeJso<LegendOption> {
            icon = "circle"
            inactiveColor = "#ccc"
            inactiveBorderWidth = StringOrDouble("auto")
            symbolRotate = StringOrDouble("inherit")
            symbolKeepAspect = true
        }
        val dataItem: LegendStyleOption = unsafeJso<LegendDataItemOption> {
            name = "series"
            icon = "rect"
            inactiveBorderWidth = StringOrDouble(2.0)
        }

        assertEquals("circle", component.icon)
        assertEquals("auto", component.inactiveBorderWidth!!.unsafeCast<String>())
        assertEquals("inherit", component.symbolRotate!!.unsafeCast<String>())
        assertTrue(component.symbolKeepAspect!!)
        assertEquals("series", (dataItem as LegendDataItemOption).name)
        assertEquals("rect", dataItem.icon)
        assertEquals(2.0, dataItem.inactiveBorderWidth!!.unsafeCast<Double>())
    }

    @Test
    fun legendStylesAcceptTheInheritedStringForms() {
        val itemStyle = unsafeJso<LegendItemStyleOption> {
            color = ZRColor("#5470c6")
            borderWidth = StringOrDouble("auto")
            borderColor = ZRColor("inherit")
            borderType = ZRLineTypes.dashed
            borderRadius = StringOrDoubleOrArray("50%")
        }
        val lineStyle = unsafeJso<LegendLineStyleOption> {
            width = StringOrDouble("inherit")
            inactiveColor = ZRColor("#999")
            inactiveWidth = 2.0
        }

        assertEquals("auto", itemStyle.borderWidth!!.unsafeCast<String>())
        assertEquals("inherit", itemStyle.borderColor!!.unsafeCast<String>())
        assertEquals("dashed", itemStyle.borderType!!.unsafeCast<String>())
        assertEquals("50%", itemStyle.borderRadius!!.unsafeCast<String>())
        assertEquals("inherit", lineStyle.width!!.unsafeCast<String>())
        assertEquals(2.0, lineStyle.inactiveWidth)
    }

    /**
     * `getOption()` keeps the legend data styles, so the narrowed types must survive a real
     * `setOption` round trip as well.
     */    @Test
    fun chartKeepsTheLegendDataStyles() {
        val option = unsafeJso<EChartsOption> {
            legend = one(
                unsafeJso<LegendOption> {
                    data = arrayOf(
                        unsafeJso<LegendDataItemOption> {
                            name = "series"
                            itemStyle = unsafeJso<LegendItemStyleOption> {
                                borderWidth = StringOrDouble("inherit")
                            }
                            lineStyle = unsafeJso<LegendLineStyleOption> {
                                width = StringOrDouble("auto")
                                inactiveColor = ZRColor("#999")
                            }
                        },
                    )
                },
            )
            series = one(
                unsafeJso<LineSeriesOption> {
                    name = "series"
                    data = numbers(1.0, 2.0)
                },
            )
        }

        val (chart, container) = createChart()
        try {
            chart.setOption(option)
            val legend = chart.getOption().legend!![0]
            val item = legend.data!![0]
            assertEquals("inherit", item.itemStyle!!.borderWidth!!.unsafeCast<String>())
            assertEquals("auto", item.lineStyle!!.width!!.unsafeCast<String>())
            assertEquals("#999", item.lineStyle!!.inactiveColor!!.unsafeCast<String>())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun scrollableLegendPageOptionsAreTyped() {
        val option = unsafeJso<EChartsOption> {
            legend = one(
                unsafeJso<LegendOption> {
                    type = echarts.LegendTypes.scroll
                    pageButtonPosition = echarts.LegendPageButtonPositions.end
                    pageButtonItemGap = 5.0
                    pageButtonGap = 10.0
                    pageIcons = unsafeJso<LegendPageIconsOption> {
                        horizontal = arrayOf("path://M0,0L10,10", "path://M0,0L10,10", "path://M0,0L10,10", "path://M0,0L10,10")
                    }
                },
            )
            series = one(unsafeJso<LineSeriesOption> { data = numbers(1.0, 2.0) })
        }

        val (chart, container) = createChart()
        try {
            chart.setOption(option)
            val legend = chart.getOption().legend!![0]
            assertEquals("scroll", legend.type!!.unsafeCast<String>())
            assertEquals(echarts.LegendPageButtonPositions.end, legend.pageButtonPosition)
            assertEquals(5.0, legend.pageButtonItemGap)
            assertEquals(10.0, legend.pageButtonGap)
            assertEquals(4, legend.pageIcons!!.horizontal!!.size)
        } finally {
            destroyChart(chart, container)
        }
    }
}
