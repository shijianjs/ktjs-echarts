package echarts.model

import echarts.createChart
import echarts.destroyChart
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.MediaUnit
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TimelineOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

/**
 * Translated from `test/ut/spec/model/timelineMediaOptions.test.ts` of the upstream
 * apache/echarts repository.
 *
 * Upstream reads the series data through `getECModel(chart).getComponent('series', i)`, which the
 * published package does not expose. The same values are read from `chart.getOption().series[0]
 * .data`, which reflects the currently selected timeline option — the public equivalent of the
 * upstream helper.
 */
class TimelineMediaOptionsTest {
    @Test
    fun parseMediaHasBaseOptionHasDefault() {
        val (chart, container) = createChart(10, 10)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    baseOption = mediaBaseOption()
                    // Illegal, should be ignored.
                    legend = one(unsafeJso<LegendOption> { left = StringOrDouble(999.0); right = StringOrDouble(123.0) })
                    media = arrayOf(
                        unsafeJso<MediaUnit> {
                            query = unsafeJso { maxWidth = 670.0; minWidth = 550.0 }
                            option = unsafeJso { legend = one(unsafeJso<LegendOption> { left = StringOrDouble(50.0) }) }
                        },
                        unsafeJso<MediaUnit> {
                            option = unsafeJso { legend = one(unsafeJso<LegendOption> { left = StringOrDouble(100.0) }) }
                        },
                    )
                },
            )

            assertEquals(100.0, legendLeft(chart))
            assertNotEquals(123.0, legendRight(chart))

            chart.resize(unsafeJso { width = StringOrDouble(600.0) })
            assertEquals(50.0, legendLeft(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun parseMediaNoBaseOptionHasDefault() {
        val (chart, container) = createChart(10, 10)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
                    yAxis = one(unsafeJso<YAxisOption> { })
                    legend = one(unsafeJso<LegendOption> { left = StringOrDouble(10.0) })
                    series = one(lineSeries { name = "a"; typedData = numbers(11.0) })
                    media = arrayOf(
                        unsafeJso<MediaUnit> {
                            query = unsafeJso { maxWidth = 670.0; minWidth = 550.0 }
                            option = unsafeJso { legend = one(unsafeJso<LegendOption> { left = StringOrDouble(50.0) }) }
                        },
                        unsafeJso<MediaUnit> {
                            option = unsafeJso { legend = one(unsafeJso<LegendOption> { left = StringOrDouble(100.0) }) }
                        },
                    )
                },
            )

            assertEquals(100.0, legendLeft(chart))

            chart.resize(unsafeJso { width = StringOrDouble(600.0) })
            assertEquals(50.0, legendLeft(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun parseMediaNoBaseOptionNoDefault() {
        val (chart, container) = createChart(10, 10)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
                    yAxis = one(unsafeJso<YAxisOption> { })
                    legend = one(unsafeJso<LegendOption> { left = StringOrDouble(10.0) })
                    series = one(lineSeries { name = "a"; typedData = numbers(11.0) })
                    media = arrayOf(
                        unsafeJso<MediaUnit> {
                            query = unsafeJso { maxWidth = 670.0; minWidth = 550.0 }
                            option = unsafeJso { legend = one(unsafeJso<LegendOption> { left = StringOrDouble(50.0) }) }
                        },
                    )
                },
            )

            assertEquals(10.0, legendLeft(chart))

            chart.resize(unsafeJso { width = StringOrDouble(600.0) })
            assertEquals(50.0, legendLeft(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun parseTimelineMediaHasBaseOption() {
        val (chart, container) = createChart(10, 10)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    baseOption = unsafeJso {
                        timeline = unsafeJso<TimelineOption> { axisType = "category" }
                        xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
                        yAxis = one(unsafeJso<YAxisOption> { })
                        legend = one(unsafeJso<LegendOption> { left = StringOrDouble(10.0) })
                        series = one(lineSeries { name = "a"; typedData = numbers(11.0) })
                    }
                    // Illegal, should be ignored.
                    legend = one(unsafeJso<LegendOption> { left = StringOrDouble(999.0); right = StringOrDouble(123.0) })
                    media = arrayOf(
                        unsafeJso<MediaUnit> {
                            query = unsafeJso { maxWidth = 670.0; minWidth = 550.0 }
                            option = unsafeJso { legend = one(unsafeJso<LegendOption> { left = StringOrDouble(50.0) }) }
                        },
                        unsafeJso<MediaUnit> {
                            option = unsafeJso { legend = one(unsafeJso<LegendOption> { left = StringOrDouble(100.0) }) }
                        },
                    )
                    options = arrayOf(
                        unsafeJso { series = one(lineSeries { typedData = numbers(88.0) }) },
                        unsafeJso { series = one(lineSeries { typedData = numbers(99.0) }) },
                    )
                },
            )

            assertEquals(100.0, legendLeft(chart))
            assertEquals(88.0, data0(chart))
            assertNotEquals(123.0, legendRight(chart))
            assertNotNull(timeline(chart))

            chart.resize(unsafeJso { width = StringOrDouble(600.0) })
            assertEquals(88.0, data0(chart))
            assertEquals(50.0, legendLeft(chart))

            chart.setOption(unsafeJso<EChartsOption> { timeline = unsafeJso<TimelineOption> { currentIndex = 1.0 } })
            assertEquals(99.0, data0(chart))
            assertEquals(50.0, legendLeft(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun parseTimelineMediaNoBaseOption() {
        val (chart, container) = createChart(10, 10)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    timeline = unsafeJso<TimelineOption> { axisType = "category" }
                    xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
                    yAxis = one(unsafeJso<YAxisOption> { })
                    legend = one(unsafeJso<LegendOption> { left = StringOrDouble(10.0) })
                    series = one(lineSeries { name = "a"; typedData = numbers(11.0) })
                    media = arrayOf(
                        unsafeJso<MediaUnit> {
                            query = unsafeJso { maxWidth = 670.0; minWidth = 550.0 }
                            option = unsafeJso { legend = one(unsafeJso<LegendOption> { left = StringOrDouble(50.0) }) }
                        },
                        unsafeJso<MediaUnit> {
                            option = unsafeJso { legend = one(unsafeJso<LegendOption> { left = StringOrDouble(100.0) }) }
                        },
                    )
                    options = arrayOf(
                        unsafeJso { series = one(lineSeries { typedData = numbers(88.0) }) },
                        unsafeJso { series = one(lineSeries { typedData = numbers(99.0) }) },
                    )
                },
            )

            assertEquals(100.0, legendLeft(chart))
            assertEquals(88.0, data0(chart))
            assertNotNull(timeline(chart))

            chart.resize(unsafeJso { width = StringOrDouble(600.0) })
            assertEquals(88.0, data0(chart))
            assertEquals(50.0, legendLeft(chart))

            chart.setOption(unsafeJso<EChartsOption> { timeline = unsafeJso<TimelineOption> { currentIndex = 1.0 } })
            assertEquals(99.0, data0(chart))
            assertEquals(50.0, legendLeft(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun parseTimelineHasBaseOption() {
        val (chart, container) = createChart(10, 10)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    baseOption = unsafeJso {
                        timeline = unsafeJso<TimelineOption> { axisType = "category" }
                        xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
                        yAxis = one(unsafeJso<YAxisOption> { })
                        legend = one(unsafeJso<LegendOption> { })
                        series = one(lineSeries { name = "a"; typedData = numbers(11.0) })
                    }
                    // Illegal, should be ignored.
                    legend = one(unsafeJso<LegendOption> { right = StringOrDouble(123.0) })
                    options = arrayOf(
                        unsafeJso { series = one(lineSeries { typedData = numbers(88.0) }) },
                        unsafeJso { series = one(lineSeries { typedData = numbers(99.0) }) },
                    )
                },
            )

            assertEquals(88.0, data0(chart))
            assertNotEquals(123.0, legendRight(chart))
            assertNotNull(timeline(chart))

            chart.setOption(unsafeJso<EChartsOption> { timeline = unsafeJso<TimelineOption> { currentIndex = 1.0 } })
            assertEquals(99.0, data0(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun parseTimelineHasBaseOptionCompat() {
        val (chart, container) = createChart(10, 10)
        try {
            // `timeline` outside `baseOption` is the legacy form and must still work.
            chart.setOption(
                unsafeJso<EChartsOption> {
                    timeline = unsafeJso<TimelineOption> { axisType = "category" }
                    baseOption = unsafeJso {
                        xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
                        yAxis = one(unsafeJso<YAxisOption> { })
                        legend = one(unsafeJso<LegendOption> { })
                        series = one(lineSeries { name = "a"; typedData = numbers(11.0) })
                    }
                    // Illegal, should be ignored.
                    legend = one(unsafeJso<LegendOption> { right = StringOrDouble(123.0) })
                    options = arrayOf(
                        unsafeJso { series = one(lineSeries { typedData = numbers(88.0) }) },
                        unsafeJso { series = one(lineSeries { typedData = numbers(99.0) }) },
                    )
                },
            )

            assertEquals(88.0, data0(chart))
            assertNotEquals(123.0, legendRight(chart))
            assertNotNull(timeline(chart))

            chart.setOption(unsafeJso<EChartsOption> { timeline = unsafeJso<TimelineOption> { currentIndex = 1.0 } })
            assertEquals(99.0, data0(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun parseTimelineNoBaseOption() {
        val (chart, container) = createChart(10, 10)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    timeline = unsafeJso<TimelineOption> { axisType = "category" }
                    xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
                    yAxis = one(unsafeJso<YAxisOption> { })
                    legend = one(unsafeJso<LegendOption> { })
                    series = one(lineSeries { name = "a"; typedData = numbers(11.0) })
                    options = arrayOf(
                        unsafeJso { series = one(lineSeries { typedData = numbers(88.0) }) },
                        unsafeJso { series = one(lineSeries { typedData = numbers(99.0) }) },
                    )
                },
            )

            assertEquals(88.0, data0(chart))
            assertNotEquals(123.0, legendRight(chart))
            assertNotNull(timeline(chart))

            chart.setOption(unsafeJso<EChartsOption> { timeline = unsafeJso<TimelineOption> { currentIndex = 1.0 } })
            assertEquals(99.0, data0(chart))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun timelineSetOptionOnceMoreBaseOption() {
        val (chart, container) = createChart(10, 10)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    baseOption = unsafeJso {
                        timeline = unsafeJso<TimelineOption> {
                            axisType = "category"
                            autoPlay = false
                            playInterval = 1000.0
                        }
                        xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
                        yAxis = one(unsafeJso<YAxisOption> { })
                    }
                    options = arrayOf(
                        unsafeJso {
                            series = arrayOf<SeriesOption>(
                                lineSeries { typedData = numbers(11.0) },
                                lineSeries { typedData = numbers(22.0) },
                            )
                        },
                        unsafeJso {
                            series = arrayOf<SeriesOption>(
                                lineSeries { typedData = numbers(111.0) },
                                lineSeries { typedData = numbers(222.0) },
                            )
                        },
                    )
                },
            )

            assertEquals(11.0, data0(chart))
            assertEquals(22.0, dataAt(chart, 1))

            chart.setOption(unsafeJso<EChartsOption> { xAxis = one(unsafeJso<XAxisOption> { data = numbers(2.0) }) })

            assertEquals(11.0, data0(chart))
            assertEquals(22.0, dataAt(chart, 1))

            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { data = numbers(3.0) })
                    timeline = unsafeJso<TimelineOption> { currentIndex = 1.0 }
                },
            )

            assertEquals(111.0, data0(chart))
            assertEquals(222.0, dataAt(chart, 1))
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun timelineSetOptionOnceMoreSubstitudeTimelineOptions() {
        val (chart, container) = createChart(10, 10)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    baseOption = unsafeJso {
                        timeline = unsafeJso<TimelineOption> {
                            axisType = "category"
                            autoPlay = false
                            playInterval = 1000.0
                            currentIndex = 2.0
                        }
                        xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
                        yAxis = one(unsafeJso<YAxisOption> { })
                    }
                    options = arrayOf(
                        unsafeJso {
                            series = arrayOf<SeriesOption>(
                                lineSeries { typedData = numbers(11.0) },
                                lineSeries { typedData = numbers(22.0) },
                            )
                        },
                        unsafeJso {
                            series = arrayOf<SeriesOption>(
                                lineSeries { typedData = numbers(111.0) },
                                lineSeries { typedData = numbers(222.0) },
                            )
                        },
                        unsafeJso {
                            series = arrayOf<SeriesOption>(
                                lineSeries { typedData = numbers(1111.0) },
                                lineSeries { typedData = numbers(2222.0) },
                            )
                        },
                    )
                },
            )

            assertEquals(1111.0, data0(chart))
            assertEquals(2222.0, dataAt(chart, 1))

            chart.setOption(
                unsafeJso<EChartsOption> {
                    baseOption = unsafeJso {
                        backgroundColor = zrender.ZRColor("#987654")
                        xAxis = one(unsafeJso<XAxisOption> { data = numbers(2.0) })
                    }
                    options = arrayOf(
                        unsafeJso {
                            series = arrayOf<SeriesOption>(
                                lineSeries { typedData = numbers(55.0) },
                                lineSeries { typedData = numbers(66.0) },
                            )
                        },
                        unsafeJso {
                            series = arrayOf<SeriesOption>(
                                lineSeries { typedData = numbers(555.0) },
                                lineSeries { typedData = numbers(666.0) },
                            )
                        },
                    )
                },
            )

            assertEquals("#987654", chart.getOption().backgroundColor.unsafeCast<String>())
            assertEquals(1111.0, data0(chart))
            assertEquals(2222.0, dataAt(chart, 1))

            chart.setOption(unsafeJso<EChartsOption> { timeline = unsafeJso<TimelineOption> { currentIndex = 0.0 } })

            assertEquals(55.0, data0(chart))
            assertEquals(66.0, dataAt(chart, 1))

            chart.setOption(unsafeJso<EChartsOption> { timeline = unsafeJso<TimelineOption> { currentIndex = 2.0 } })

            // no 1111 2222 any more, replaced totally by new timeline.
            assertEquals(55.0, data0(chart))
            assertEquals(66.0, dataAt(chart, 1))
        } finally {
            destroyChart(chart, container)
        }
    }
}

/** The `baseOption` shared by the media cases. */
private fun mediaBaseOption(): EChartsOption =
    unsafeJso {
        xAxis = one(unsafeJso<XAxisOption> { data = numbers(1.0) })
        yAxis = one(unsafeJso<YAxisOption> { })
        legend = one(unsafeJso<LegendOption> { left = StringOrDouble(10.0) })
        series = one(lineSeries { name = "a"; typedData = numbers(11.0) })
    }

private fun legendLeft(chart: echarts.EChartsInstance): Double =
    chart.getOption().legend!![0].left.unsafeCast<Double>()

private fun legendRight(chart: echarts.EChartsInstance): Double =
    chart.getOption().legend!![0].right.unsafeCast<Double>()

private fun timeline(chart: echarts.EChartsInstance): TimelineOption? = chart.getOption().timeline

private fun data0(chart: echarts.EChartsInstance): Double = dataAt(chart, 0)

private fun dataAt(chart: echarts.EChartsInstance, seriesIndex: Int): Double =
    chart.getOption().series!![seriesIndex].data!![0].unsafeCast<Double>()
