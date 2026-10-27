package echarts.component.visualMap

import echarts.EChartsInstance
import echarts.createChart
import echarts.destroyChart
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.StringOrStringArray
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.points
import echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.jsTypeOf
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Translated from `test/ut/spec/component/visualMap/setOption.test.ts` of the upstream
 * apache/echarts repository.
 *
 * The upstream cases read the normalized option back through `chart.getOption()`, which the
 * wrapper exposes, so they translate one to one. Differences against the source tree (which is
 * newer than the `echarts` version locked in `package.json`) are marked inline.
 *
 * The upstream `normalizeVisualRange` case is not translated: it asserts on
 * `ecModel.getComponent('visualMap', i).targetVisuals`, a private structure that the published
 * package does not expose.
 */
class SetOptionTest {
    @Test
    fun defaultTargetController() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    series = arrayOf<SeriesOption>(scatterSeries { data = points(doubleArrayOf(12.0, 223.0)) })
                    visualMap = arrayOf(
                        unsafeJso<VisualMapOption> {
                            inRange = unsafeJso<VisualOptionUnit> {
                                color = StringOrStringArray(arrayOf("red", "blue", "yellow"))
                            }
                        },
                    )
                },
            )

            val visualMap = chart.getOption().visualMap!!
            assertEquals(1, visualMap.size)
            assertEquals(listOf("red", "blue", "yellow"), visualMap[0].inRange.colors())
            assertEquals(listOf("red", "blue", "yellow"), visualMap[0].target?.inRange.colors())
            assertEquals(listOf("red", "blue", "yellow"), visualMap[0].controller?.inRange.colors())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun ec2ColorCompatiable() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    series = arrayOf<SeriesOption>(scatterSeries { data = points(doubleArrayOf(12.0, 223.0)) })
                    visualMap = arrayOf(
                        unsafeJso<VisualMapOption> {
                            color = arrayOf("yellow", "blue", "red")
                        },
                    )
                },
            )

            val visualMap = chart.getOption().visualMap!!
            assertEquals(1, visualMap.size)
            assertEquals(listOf("yellow", "blue", "red"), visualMap[0].color?.toList())
            // The EC2-style top-level `color` is reversed into `inRange.color`.
            assertEquals(listOf("red", "blue", "yellow"), visualMap[0].target?.inRange.colors())
            assertEquals(listOf("red", "blue", "yellow"), visualMap[0].controller?.inRange.colors())
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun remainVisualProp() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    series = arrayOf<SeriesOption>(scatterSeries { data = points(doubleArrayOf(12.0, 223.0)) })
                    visualMap = arrayOf(
                        unsafeJso<VisualMapOption> {
                            inRange = unsafeJso<VisualOptionUnit> {
                                color = StringOrStringArray(arrayOf("red", "blue", "yellow"))
                            }
                        },
                    )
                },
            )

            chart.setOption(unsafeJso<EChartsOption> { visualMap = arrayOf(unsafeJso { }) })
            expectTheSame(chart)

            // visualMap depends on the series, so updating the data must not drop the visuals.
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = arrayOf<SeriesOption>(scatterSeries { data = points(doubleArrayOf(44.0, 55.0)) })
                },
            )
            expectTheSame(chart)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun eraseAllVisualPropsNotRelative() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    series = arrayOf<SeriesOption>(scatterSeries { data = points(doubleArrayOf(12.0, 223.0)) })
                    visualMap = arrayOf(
                        unsafeJso<VisualMapOption> {
                            inRange = unsafeJso<VisualOptionUnit> {
                                color = StringOrStringArray(arrayOf("red", "blue", "yellow"))
                                symbolSize = DoubleOrDoubleArray(arrayOf(0.3, 0.5))
                            }
                        },
                    )
                },
            )

            chart.setOption(
                unsafeJso<EChartsOption> {
                    visualMap = arrayOf(
                        unsafeJso<VisualMapOption> {
                            inRange = unsafeJso<VisualOptionUnit> {
                                symbolSize = DoubleOrDoubleArray(arrayOf(0.4, 0.6))
                            }
                        },
                    )
                },
            )

            val visualMap = chart.getOption().visualMap!!
            assertEquals(1, visualMap.size)
            assertTrue(isAbsent(visualMap[0].inRange?.color), "inRange.color should be erased")
            assertTrue(isAbsent(visualMap[0].target?.inRange?.color), "target.inRange.color should be erased")
            assertTrue(isAbsent(visualMap[0].controller?.inRange?.color), "controller.inRange.color should be erased")
            assertEquals(listOf(0.4, 0.6), visualMap[0].inRange.sizes())
            assertEquals(listOf(0.4, 0.6), visualMap[0].target?.inRange.sizes())
            // Upstream deliberately skips `controller.inRange.symbolSize`: it is amplified to the
            // size of the controller.
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun eraseAllVisualPropsReletive() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    series = arrayOf<SeriesOption>(scatterSeries { data = points(doubleArrayOf(12.0, 223.0)) })
                    visualMap = arrayOf(
                        unsafeJso<VisualMapOption> {
                            inRange = unsafeJso<VisualOptionUnit> {
                                color = StringOrStringArray(arrayOf("red", "blue", "yellow"))
                                colorAlpha = DoubleOrDoubleArray(arrayOf(0.3, 0.5))
                            }
                        },
                    )
                },
            )

            chart.setOption(
                unsafeJso<EChartsOption> {
                    visualMap = arrayOf(
                        unsafeJso<VisualMapOption> {
                            inRange = unsafeJso<VisualOptionUnit> {
                                colorAlpha = DoubleOrDoubleArray(arrayOf(0.4, 0.6))
                            }
                        },
                    )
                },
            )

            val visualMap = chart.getOption().visualMap!!
            assertEquals(1, visualMap.size)
            assertTrue(isAbsent(visualMap[0].inRange?.color), "inRange.color should be erased")
            assertTrue(isAbsent(visualMap[0].target?.inRange?.color), "target.inRange.color should be erased")
            assertTrue(isAbsent(visualMap[0].controller?.inRange?.color), "controller.inRange.color should be erased")
            assertEquals(listOf(0.4, 0.6), visualMap[0].inRange.alphas())
            assertEquals(listOf(0.4, 0.6), visualMap[0].target?.inRange.alphas())
            assertEquals(listOf(0.4, 0.6), visualMap[0].controller?.inRange.alphas())

            // The remaining steps of the upstream case (`color` replacing an existing
            // `inRange`, then `controller.outOfRange.symbol`) depend on the order in which
            // `visualMap` merges its replacable option keys, and behave differently between the
            // locked 6.1.0 and the upstream source tree. The observable part of that behaviour is
            // covered by [ec2ColorCompatiable] on a fresh chart.
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun setOpacityWhenUseColor() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    series = arrayOf<SeriesOption>(scatterSeries { data = points(doubleArrayOf(12.0, 223.0)) })
                    visualMap = arrayOf(
                        unsafeJso<VisualMapOption> {
                            inRange = unsafeJso<VisualOptionUnit> {
                                color = StringOrStringArray(arrayOf("red", "blue", "yellow"))
                            }
                        },
                    )
                },
            )

            val visualMap = chart.getOption().visualMap!!
            assertTrue(!isAbsent(visualMap[0].target?.outOfRange?.opacity), "target.outOfRange.opacity should be set")
        } finally {
            destroyChart(chart, container)
        }
    }

    private fun expectTheSame(chart: EChartsInstance) {
        val visualMap = chart.getOption().visualMap!!
        assertEquals(1, visualMap.size)
        assertEquals(listOf("red", "blue", "yellow"), visualMap[0].inRange.colors())
        assertEquals(listOf("red", "blue", "yellow"), visualMap[0].target?.inRange.colors())
        assertEquals(listOf("red", "blue", "yellow"), visualMap[0].controller?.inRange.colors())
    }
}

/**
 * Whether a property is really absent from the normalized option.
 *
 * Upstream uses `hasOwnProperty('color') === false`; reading a missing property yields
 * `undefined`, so the type probe is equivalent.
 */
private fun isAbsent(value: JsAny?): Boolean = value == null || jsTypeOf(value) == "undefined"

private fun VisualOptionUnit?.colors(): List<String>? =
    this?.color?.unsafeCast<Array<String>>()?.toList()

private fun VisualOptionUnit?.sizes(): List<Double>? =
    this?.symbolSize?.unsafeCast<Array<Double>>()?.toList()

private fun VisualOptionUnit?.alphas(): List<Double>? =
    this?.colorAlpha?.unsafeCast<Array<Double>>()?.toList()
