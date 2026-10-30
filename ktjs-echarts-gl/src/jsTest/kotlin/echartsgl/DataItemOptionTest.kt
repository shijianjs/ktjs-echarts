package echartsgl

import echarts.ECharts
import echarts.options.LabelFormatter
import echarts.options.OptionDataValueOrArray
import echarts.options.OptionId
import echarts.options.StringOrDouble
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.one
import echartsgl.options.Bar3DDataItem
import echartsgl.options.Bar3DDataItemOption
import echartsgl.options.Bar3DSeriesOption
import echartsgl.options.EChartsGlOption
import echartsgl.options.Grid3DOption
import echartsgl.options.Line3DDataItem
import echartsgl.options.Line3DDataItemOption
import echartsgl.options.Line3DSeriesOption
import echartsgl.options.Scatter3DDataItem
import echartsgl.options.Scatter3DDataItemOption
import echartsgl.options.Scatter3DSeriesOption
import echartsgl.options.SurfaceDataItem
import echartsgl.options.SurfaceDataItemOption
import echartsgl.options.SurfaceSeriesOption
import echartsgl.options.bar3DSeries
import echartsgl.options.line3DSeries
import echartsgl.options.scatter3DSeries
import echartsgl.options.surfaceSeries
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import zrender.ZRColor

/**
 * Tests the object form of the data items of `bar3D`, `scatter3D`, `line3D` and `surface`.
 *
 * Every one of these series also takes a plain row of values, and those forms are covered by the
 * series tests; the cases here pass data item objects, whose extra fields — a per-item style, label
 * and emphasis state — are what a `visualMap` cannot express.
 */
class DataItemOptionTest {
    /**
     * A `bar3D` item takes its own `itemStyle`, `label` and `emphasis`, which `Bar3DView` reads per
     * bar; the color may also be the HDR array that only `echarts-gl` accepts.
     */
    @Test
    fun bar3DObjectDataItemsRoundTrip(): Promise<Unit> {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                cartesian3DAxes()
                grid3D = one(unsafeJso<Grid3DOption> {})
                series = one(
                    bar3DSeries {
                        typedData = arrayOf<Bar3DDataItem?>(
                            Bar3DDataItem(
                                unsafeJso<Bar3DDataItemOption> {
                                    name = OptionId("alpha")
                                    typedValue = OptionDataValueOrArray(row(0.0, 0.0, 3.0))
                                    itemStyle = unsafeJso {
                                        color = GlColor(ZRColor("#ff0000"))
                                        opacity = 0.7
                                        borderColor = GlColor(ZRColor("#00ff00"))
                                        borderWidth = 2.0
                                    }
                                    label = unsafeJso {
                                        show = true
                                        formatter = LabelFormatter("{b}")
                                        fontSize = StringOrDouble(12.0)
                                    }
                                    emphasis = unsafeJso {
                                        itemStyle = unsafeJso { opacity = 1.0 }
                                        label = unsafeJso { color = "#0000ff" }
                                    }
                                },
                            ),
                            Bar3DDataItem(
                                unsafeJso<Bar3DDataItemOption> {
                                    typedValue = OptionDataValueOrArray(row(1.0, 1.0, 5.0))
                                    itemStyle = unsafeJso {
                                        color = GlColor(arrayOf(2.0, 0.0, 0.0))
                                    }
                                },
                            ),
                        )
                    },
                )
            },
        )
        return nextFrames(2).then {
            try {
                val series = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .series!![0]
                    .unsafeCast<Bar3DSeriesOption>()
                assertEquals(2, series.typedData!!.size)

                val styled = series.typedData!![0]!!.unsafeCast<Bar3DDataItemOption>()
                assertEquals(OptionId("alpha"), styled.name)
                assertEquals(3, styled.typedValue!!.unsafeCast<Array<Double>>().size)
                assertEquals("#ff0000", styled.itemStyle!!.color!!.unsafeCast<String>())
                assertEquals(0.7, styled.itemStyle!!.opacity)
                assertEquals(2.0, styled.itemStyle!!.borderWidth)
                assertEquals("{b}", styled.label!!.formatter!!.unsafeCast<String>())
                assertEquals(StringOrDouble(12.0), styled.label!!.fontSize)
                assertEquals(1.0, styled.emphasis!!.itemStyle!!.opacity)
                assertEquals("#0000ff", styled.emphasis!!.label!!.color)

                val hdr = series.typedData!![1]!!.unsafeCast<Bar3DDataItemOption>()
                assertEquals(
                    2.0,
                    hdr.itemStyle!!.color!!.unsafeCast<Array<Double>>()[0],
                    "an HDR color stays an array",
                )
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * A `scatter3D` item inherits [echarts.options.SymbolOptionMixin], so a single symbol can be a
     * different shape and size from the rest of the series.
     */
    @Test
    fun scatter3DObjectDataItemsRoundTrip(): Promise<Unit> {
        ECharts.use(arrayOf(Scatter3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                cartesian3DAxes()
                grid3D = one(unsafeJso<Grid3DOption> {})
                series = one(
                    scatter3DSeries {
                        symbol = SymbolShape("circle")
                        typedData = arrayOf<Scatter3DDataItem?>(
                            Scatter3DDataItem(row(0.0, 0.0, 1.0)),
                            Scatter3DDataItem(
                                unsafeJso<Scatter3DDataItemOption> {
                                    name = OptionId("big")
                                    typedValue = OptionDataValueOrArray(row(1.0, 2.0, 3.0))
                                    symbol = SymbolShape("diamond")
                                    symbolSize = SymbolSize(30.0)
                                    itemStyle = unsafeJso {
                                        color = GlColor(ZRColor("#123456"))
                                        opacity = 0.5
                                    }
                                    label = unsafeJso { show = true }
                                    emphasis = unsafeJso { disabled = true }
                                },
                            ),
                        )
                    },
                )
            },
        )
        return nextFrames(2).then {
            try {
                val series = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .series!![0]
                    .unsafeCast<Scatter3DSeriesOption>()
                assertEquals(SymbolShape("circle"), series.symbol)
                assertEquals(2, series.typedData!!.size)

                val item = series.typedData!![1]!!.unsafeCast<Scatter3DDataItemOption>()
                assertEquals(OptionId("big"), item.name)
                assertEquals(SymbolShape("diamond"), item.symbol)
                assertEquals(SymbolSize(30.0), item.symbolSize)
                assertEquals("#123456", item.itemStyle!!.color!!.unsafeCast<String>())
                assertEquals(true, item.label!!.show)
                assertEquals(true, item.emphasis!!.disabled)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * The style key of a `line3D` data item is `lineStyle`, not `itemStyle`: `Line3DSeries` sets
     * `visualStyleAccessPath = 'lineStyle'`, so echarts builds the per-item visual from that path and
     * `Line3DView` takes the color of every vertex from it.
     */
    @Test
    fun line3DObjectDataItemsRoundTrip(): Promise<Unit> {
        ECharts.use(arrayOf(Line3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                cartesian3DAxes()
                grid3D = one(unsafeJso<Grid3DOption> {})
                series = one(
                    line3DSeries {
                        lineStyle = unsafeJso { width = 3.0 }
                        typedData = arrayOf<Line3DDataItem?>(
                            Line3DDataItem(row(0.0, 0.0, 0.0)),
                            Line3DDataItem(
                                unsafeJso<Line3DDataItemOption> {
                                    typedValue = OptionDataValueOrArray(row(1.0, 2.0, 3.0))
                                    lineStyle = unsafeJso {
                                        color = ZRColor("#ff0000")
                                        opacity = 0.4
                                    }
                                },
                            ),
                            Line3DDataItem(row(2.0, 1.0, 4.0)),
                        )
                    },
                )
            },
        )
        return nextFrames(2).then {
            try {
                val series = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .series!![0]
                    .unsafeCast<Line3DSeriesOption>()
                assertEquals(3, series.typedData!!.size)
                assertEquals(3.0, series.lineStyle!!.width)

                val item = series.typedData!![1]!!.unsafeCast<Line3DDataItemOption>()
                assertEquals("#ff0000", item.lineStyle!!.color!!.unsafeCast<String>())
                assertEquals(0.4, item.lineStyle!!.opacity)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * A surface is also drawn from a vertex grid instead of an equation: [SurfaceSeriesOption.data]
     * holds one item per vertex and [SurfaceSeriesOption.dataShape] gives its rows and columns.
     *
     * `SurfaceSeries.getInitialData` only samples an equation when no data is set, so the two may be
     * given together and the data is what is drawn.
     */
    @Test
    fun surfaceDataGridRoundTrip(): Promise<Unit> {
        ECharts.use(arrayOf(SurfaceChart, Grid3DComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                cartesian3DAxes()
                grid3D = one(unsafeJso<Grid3DOption> {})
                series = one(
                    surfaceSeries {
                        shading = ShadingTypes.color
                        wireframe = unsafeJso { show = false }
                        dataShape = arrayOf(3.0, 3.0)
                        typedData = Array<SurfaceDataItem?>(9) { index ->
                            val x = index % 3 * 0.5 - 0.5
                            val y = index / 3 * 0.5 - 0.5
                            if (index == 4) {
                                SurfaceDataItem(
                                    unsafeJso<SurfaceDataItemOption> {
                                        typedValue = OptionDataValueOrArray(row(x, y, 1.0))
                                        itemStyle = unsafeJso {
                                            color = GlColor(ZRColor("#ff0000"))
                                            opacity = 0.6
                                        }
                                    },
                                )
                            } else {
                                SurfaceDataItem(row(x, y, 0.0))
                            }
                        }
                    },
                )
            },
        )
        return nextFrames(2).then {
            try {
                val series = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .series!![0]
                    .unsafeCast<SurfaceSeriesOption>()
                assertEquals(2, series.dataShape!!.size)
                assertEquals(3.0, series.dataShape!![0])
                assertEquals(9, series.typedData!!.size)

                val item = series.typedData!![4]!!.unsafeCast<SurfaceDataItemOption>()
                assertEquals("#ff0000", item.itemStyle!!.color!!.unsafeCast<String>())
                assertEquals(0.6, item.itemStyle!!.opacity)
                assertEquals(1.0, item.typedValue!!.unsafeCast<Array<Double>>()[2])
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }
}
