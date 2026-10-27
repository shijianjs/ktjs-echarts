package echarts.model

import echarts.Orients
import echarts.createChart
import echarts.destroyChart
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.MediaQueryOption
import echarts.options.MediaUnit
import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.PieSeriesOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.TitleOption
import echarts.options.items
import echarts.options.one
import echarts.options.pieSeries
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Coverage for the media-query form of `setOption` (`baseOption` plus `media`), which the plain
 * option tests do not exercise.
 *
 * Upstream mounts `baseOption` and then merges the option of every matched `media` unit on top of
 * it; the unit without a query is the fallback and a unit may set only part of a component.
 */
class MediaQueryOptionTest {
    @Test
    fun mediaUnitIsMergedOnTopOfTheBaseOption() {
        val (chart, container) = createChart(400, 300)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    baseOption =
                        unsafeJso {
                            title = one(unsafeJso<TitleOption> { text = "roses" })
                            legend = one(unsafeJso<LegendOption> { right = StringOrDouble("center") })
                            series =
                                one<SeriesOption>(
                                    pieSeries {
                                        name = "radius"
                                        typedData =
                                            items(
                                                unsafeJso<OptionDataItemObject> {
                                                    name = OptionId("rose1")
                                                    value = OptionDataValue(10.0)
                                                },
                                                unsafeJso<OptionDataItemObject> {
                                                    name = OptionId("rose2")
                                                    value = OptionDataValue(20.0)
                                                },
                                            )
                                    },
                                )
                        }
                    media =
                        arrayOf(
                            unsafeJso<MediaUnit> {
                                option =
                                    unsafeJso {
                                        series =
                                            one<SeriesOption>(
                                                pieSeries {
                                                    radius =
                                                        StringOrDoubleOrArray(
                                                            arrayOf(StringOrDouble(20.0), StringOrDouble("50%")),
                                                        )
                                                },
                                            )
                                    }
                            },
                            unsafeJso<MediaUnit> {
                                query = unsafeJso<MediaQueryOption> { minAspectRatio = 0.5 }
                                option =
                                    unsafeJso {
                                        legend = one(unsafeJso<LegendOption> { orient = Orients.vertical })
                                    }
                            },
                        )
                },
            )

            val series = assertNotNull(chart.getOption().series)[0].unsafeCast<PieSeriesOption>()
            assertEquals("radius", series.name)
            assertEquals(2, series.typedData!!.size, "the media unit must not replace the base data")
            assertTrue(chart.renderToCanvas().width > 0, "the media option should paint")
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * A `media` unit addresses the components of `baseOption` by index, so its series entries carry
     * no `type`: upstream writes `{ radius: [...], center: [...] }` there. A typed entry becomes an
     * extra series instead of an override.
     */
    @Test
    fun mediaSeriesEntriesWithoutATypeOverrideTheBaseSeries() {
        val (chart, container) = createChart(400, 300)
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    baseOption =
                        unsafeJso {
                            series =
                                arrayOf<SeriesOption>(
                                    pieSeries {
                                        name = "first"
                                        typedData =
                                            items(
                                                unsafeJso<OptionDataItemObject> {
                                                    name = OptionId("rose1")
                                                    value = OptionDataValue(10.0)
                                                },
                                            )
                                    },
                                    pieSeries {
                                        name = "second"
                                        typedData =
                                            items(
                                                unsafeJso<OptionDataItemObject> {
                                                    name = OptionId("rose1")
                                                    value = OptionDataValue(20.0)
                                                },
                                            )
                                    },
                                )
                        }
                    media =
                        arrayOf(
                            unsafeJso<MediaUnit> {
                                option =
                                    unsafeJso {
                                        series =
                                            arrayOf<SeriesOption>(
                                                pieSeries {
                                                    radius =
                                                        StringOrDoubleOrArray(
                                                            arrayOf(StringOrDouble(20.0), StringOrDouble("50%")),
                                                        )
                                                },
                                                pieSeries {
                                                    radius =
                                                        StringOrDoubleOrArray(
                                                            arrayOf(StringOrDouble(30.0), StringOrDouble("40%")),
                                                        )
                                                },
                                            )
                                    }
                            },
                        )
                },
            )

            val series = assertNotNull(chart.getOption().series)
            assertEquals(2, series.size, "the typed media entries must override, not add series")
            assertTrue(chart.renderToCanvas().width > 0, "the media option should paint")
        } finally {
            destroyChart(chart, container)
        }
    }
}
