package echarts.options

import echarts.AxisTypes
import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import web.cssom.TextAlign

/**
 * Tests for the angle-axis fields of the polar coordinate system and the end-label alignment of an
 * axis.
 *
 * Upstream declares `startAngle`, `endAngle` and `clockwise` on `AngleAxisOption` (`shared.d.ts`)
 * and `alignMinLabel` / `alignMaxLabel` on `AxisBaseOption['axisLabel']`; the documentation
 * examples `polar-start-angle`, `polar-end-angle`, `polar-anticlockwise` and
 * `axis-label-align-min-max` are the reason they are part of the wrapper.
 */
class PolarAndAxisLabelOptionTest {
    @Test
    fun angleAxisCarriesItsAngles() {
        val axis = unsafeJso<AngleAxisOption> {
            type = AxisTypes.category
            polarId = "polar0"
            startAngle = 45.0
            endAngle = -180.0
            clockwise = false
            data = strings("S1", "S2", "S3")
        }

        assertEquals(45.0, axis.startAngle)
        assertEquals(-180.0, axis.endAngle)
        assertFalse(axis.clockwise!!)
        assertEquals("polar0", axis.polarId)
        assertEquals(3, axis.data!!.size)
    }

    @Test
    fun radiusAxisCarriesItsPolarId() {
        val axis = unsafeJso<RadiusAxisOption> {
            min = AxisMinMax(0.0)
            max = AxisMinMax(10.0)
            interval = AxisInterval(2.0)
            polarId = "polar0"
        }

        assertEquals("polar0", axis.polarId)
        assertEquals(0.0, axis.min!!.unsafeCast<Double>())
    }

    @Test
    fun axisLabelAlignsItsEndLabels() {
        val label = unsafeJso<AxisLabelOption> {
            formatter = LabelFormatter("label of {value}")
            showMinLabel = true
            showMaxLabel = true
            alignMinLabel = TextAlign.left
            alignMaxLabel = TextAlign.right
            verticalAlignMinLabel = web.cssom.VerticalAlign.bottom
            verticalAlignMaxLabel = web.cssom.VerticalAlign.top
        }

        assertEquals("left", label.alignMinLabel!!.unsafeCast<String>())
        assertEquals("right", label.alignMaxLabel!!.unsafeCast<String>())
        assertEquals("bottom", label.verticalAlignMinLabel!!.unsafeCast<String>())
        assertEquals("top", label.verticalAlignMaxLabel!!.unsafeCast<String>())
    }

    @Test
    fun aPolarChartKeepsTheAnglesAfterSetOption() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    polar = one(unsafeJso<PolarOption> {})
                    angleAxis =
                        one(
                            unsafeJso<AngleAxisOption> {
                                type = AxisTypes.category
                                startAngle = 45.0
                                endAngle = -180.0
                                clockwise = false
                                data = strings("S1", "S2", "S3")
                            },
                        )
                    radiusAxis = one(unsafeJso<RadiusAxisOption> {})
                    series =
                        one(
                            barSeries {
                                coordinateSystem = "polar"
                                typedData = numbers(1.0, 2.0, 3.0)
                            },
                        )
                },
            )

            val option = chart.getOption()
            assertEquals(45.0, option.angleAxis!![0].startAngle)
            assertEquals(-180.0, option.angleAxis!![0].endAngle)
            assertFalse(option.angleAxis!![0].clockwise!!)
            assertTrue(chart.getWidth() > 0)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun aCartesianChartKeepsTheAlignedEndLabels() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.category
                                data = strings("a", "b", "c")
                                axisLabel =
                                    unsafeJso<AxisLabelOption> {
                                        alignMinLabel = TextAlign.left
                                        alignMaxLabel = TextAlign.right
                                        showMinLabel = true
                                        showMaxLabel = true
                                    }
                            },
                        )
                    yAxis = one(unsafeJso<YAxisOption> {})
                    series = one(barSeries { typedData = numbers(1.0, 2.0, 3.0) })
                },
            )

            val label = chart.getOption().xAxis!![0].axisLabel!!
            assertEquals("left", label.alignMinLabel!!.unsafeCast<String>())
            assertEquals("right", label.alignMaxLabel!!.unsafeCast<String>())
            assertTrue(label.showMinLabel!!)
        } finally {
            destroyChart(chart, container)
        }
    }
}
