package echartsgl

import echarts.AxisTypes
import echarts.ECharts
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.OptionId
import echarts.options.StringOrDouble
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.one
import echartsgl.options.EChartsGlOption
import echartsgl.options.FlowGLDataItem
import echartsgl.options.GraphGLCategoryOption
import echartsgl.options.GraphGLForceAtlas2Option
import echartsgl.options.GraphGLLinkOption
import echartsgl.options.GraphGLNodeOption
import echartsgl.options.LinesGLDataItem
import echartsgl.options.LinesGLDataItemOption
import echartsgl.options.ScatterGLDataItem
import echartsgl.options.ScatterGLDataItemOption
import echartsgl.options.flowGLSeries
import echartsgl.options.graphGLSeries
import echartsgl.options.linesGLSeries
import echartsgl.options.scatterGLSeries
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import web.dom.document
import web.gl.ID
import web.gl.WebGLRenderingContext
import web.html.HTMLCanvasElement
import web.html.HTMLElement
import web.html.HtmlTagName
import zrender.ZRColor

/**
 * Tests the 2D series that `echarts-gl` draws with WebGL: `scatterGL`, `linesGL`, `flowGL` and
 * `graphGL`.
 *
 * `echarts-gl` has no upstream unit suite for these types, so the cases cover the wrapper-specific
 * contract: the option is accepted, the fields survive the `getOption` round trip, and the WebGL
 * layer is created and painted.
 */
class GlSeries2DTest {
    @Test
    fun scatterGLSeriesRendersAGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(ScatterGLChart))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                axes()
                series =
                    one(
                        scatterGLSeries {
                            typedCoordinateSystem = CoordinateSystems2D.cartesian2d
                            symbol = SymbolShape("circle")
                            symbolSize = SymbolSize(12.0)
                            zoomScale = 0.5
                            typedBlendMode = GlBlendModes.lighter
                            typedItemStyle = unsafeJso { opacity = 0.7 }
                            typedLabel = unsafeJso { show = true; fontSize = StringOrDouble(12.0) }
                            typedEmphasis = unsafeJso { itemStyle = unsafeJso { opacity = 1.0 } }
                            postEffect = unsafeJso { enable = true }
                            typedData =
                                arrayOf<ScatterGLDataItem?>(
                                    ScatterGLDataItem(row(1.0, 2.0)),
                                    ScatterGLDataItem(row(3.0, 4.0)),
                                    ScatterGLDataItem(
                                        unsafeJso<ScatterGLDataItemOption> {
                                            name = OptionId("single")
                                            typedValue = OptionDataValueOrArray(5.0)
                                            itemStyle = unsafeJso { opacity = 0.5 }
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
                    .unsafeCast<echartsgl.options.ScatterGLSeriesOption>()
                assertEquals(EChartsGlSeriesTypes.scatterGL, series.type)
                assertEquals(CoordinateSystems2D.cartesian2d, series.typedCoordinateSystem)
                assertEquals(SymbolShape("circle"), series.symbol)
                assertEquals(SymbolSize(12.0), series.symbolSize)
                assertEquals(0.5, series.zoomScale)
                assertEquals(GlBlendModes.lighter, series.typedBlendMode)
                assertEquals(0.7, series.typedItemStyle!!.opacity)
                assertEquals(true, series.typedLabel!!.show)
                assertEquals(1.0, series.typedEmphasis!!.itemStyle!!.opacity)
                assertEquals(true, series.postEffect!!.enable)
                assertEquals(3, series.typedData!!.size)
                val item = series.typedData!![2]!!.unsafeCast<ScatterGLDataItemOption>()
                assertEquals(OptionId("single"), item.name)
                assertEquals(0.5, item.itemStyle!!.opacity)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    @Test
    fun linesGLSeriesRendersAGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(LinesGLChart))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                axes()
                series =
                    one(
                        linesGLSeries {
                            typedCoordinateSystem = CoordinateSystems2D.cartesian2d
                            polyline = true
                            typedBlendMode = GlBlendModes.lighter
                            typedLineStyle =
                                unsafeJso {
                                    color = GlColor(ZRColor("#fff"))
                                    width = 2.0
                                    opacity = 0.5
                                    curveness = 0.2
                                }
                            typedData =
                                arrayOf<LinesGLDataItem?>(
                                    LinesGLDataItem(
                                        arrayOf(arrayOf(0.0, 0.0), arrayOf(1.0, 1.0), arrayOf(2.0, 0.5)),
                                    ),
                                    LinesGLDataItem(
                                        unsafeJso<LinesGLDataItemOption> {
                                            name = OptionId("route")
                                            coords = arrayOf(arrayOf(-1.0, 0.5), arrayOf(1.5, -0.5))
                                            lineStyle = unsafeJso { width = 1.0 }
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
                    .unsafeCast<echartsgl.options.LinesGLSeriesOption>()
                assertEquals(EChartsGlSeriesTypes.linesGL, series.type)
                assertEquals(CoordinateSystems2D.cartesian2d, series.typedCoordinateSystem)
                assertEquals(true, series.polyline)
                assertEquals(GlBlendModes.lighter, series.typedBlendMode)
                assertEquals(2.0, series.typedLineStyle!!.width)
                assertEquals(0.2, series.typedLineStyle!!.curveness)
                assertEquals(2, series.typedData!!.size)
                val item = series.typedData!![1]!!.unsafeCast<LinesGLDataItemOption>()
                assertEquals(2, item.coords!!.size)
                assertEquals(1.0, item.lineStyle!!.width)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * A `flowGL` series draws the traces of particles through a vector field; the field data is
     * `[x, y, vx, vy]` rows in a cartesian coordinate system.
     */
    @Test
    fun flowGLSeriesRendersAGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(FlowGLChart))
        val (chart, container) = createChart()
        val colorTexture = TextureSource(document.createElement(HtmlTagName.canvas))
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                axes()
                series =
                    one(
                        flowGLSeries {
                            typedCoordinateSystem = CoordinateSystems2D.cartesian2d
                            particleType = FlowGLParticleTypes.line
                            particleDensity = 64.0
                            particleSize = 2.0
                            particleSpeed = 1.5
                            particleTrail = 3.0
                            supersampling = 2.0
                            gridWidth = StringOrDouble(4.0)
                            gridHeight = StringOrDouble(4.0)
                            this.colorTexture = colorTexture
                            typedItemStyle = unsafeJso { color = GlColor(ZRColor("#0ff")); opacity = 0.6 }
                            typedData =
                                arrayOf<FlowGLDataItem?>(
                                    FlowGLDataItem(arrayOf(0.0, 0.0, 1.0, 1.0)),
                                    FlowGLDataItem(arrayOf(1.0, 0.0, 1.0, 1.0)),
                                    FlowGLDataItem(arrayOf(0.0, 1.0, 1.0, 1.0)),
                                    FlowGLDataItem(arrayOf(1.0, 1.0, 1.0, 1.0)),
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
                    .unsafeCast<echartsgl.options.FlowGLSeriesOption>()
                assertEquals(EChartsGlSeriesTypes.flowGL, series.type)
                assertEquals(CoordinateSystems2D.cartesian2d, series.typedCoordinateSystem)
                assertEquals(FlowGLParticleTypes.line, series.particleType)
                assertEquals(64.0, series.particleDensity)
                assertEquals(2.0, series.particleSize)
                assertEquals(1.5, series.particleSpeed)
                assertEquals(3.0, series.particleTrail)
                assertEquals(2.0, series.supersampling)
                assertEquals(StringOrDouble(4.0), series.gridWidth)
                assertEquals(StringOrDouble(4.0), series.gridHeight)
                assertEquals(0.6, series.typedItemStyle!!.opacity)
                assertEquals(4, series.typedData!!.size)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * The nodes of a `graphGL` series come from `data` (the alias `nodes` is covered by the field
     * type) and the layout runs on the CPU, so the test does not depend on float-texture support.
     */
    @Test
    fun graphGLSeriesRendersAGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(GraphGLChart))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                series =
                    one(
                        graphGLSeries {
                            layout = "forceAtlas2"
                            forceAtlas2 =
                                unsafeJso<GraphGLForceAtlas2Option> {
                                    GPU = false
                                    steps = 2.0
                                    maxSteps = 4.0
                                    gravity = 0.5
                                    barnesHutOptimize = false
                                }
                            focusNodeAdjacency = false
                            zoom = 1.5
                            offset = arrayOf(10.0, 20.0)
                            symbolSize = SymbolSize(8.0)
                            typedItemStyle = unsafeJso { opacity = 1.0 }
                            typedLineStyle =
                                unsafeJso {
                                    color = GlColor(ZRColor("#aaa"))
                                    width = 2.0
                                    opacity = 0.4
                                }
                            typedLabel = unsafeJso { show = true }
                            categories =
                                arrayOf(
                                    unsafeJso<GraphGLCategoryOption> {
                                        name = "group"
                                        itemStyle = unsafeJso { opacity = 0.9 }
                                    },
                                )
                            typedData =
                                arrayOf<GraphGLNodeOption?>(
                                    unsafeJso {
                                        name = OptionId("a")
                                        typedValue = OptionDataValueOrArray(1.0)
                                        category = StringOrDouble(0.0)
                                    },
                                    unsafeJso {
                                        name = OptionId("b")
                                        typedValue = OptionDataValueOrArray(2.0)
                                        x = 1.0
                                        y = 1.0
                                    },
                                )
                            links =
                                arrayOf<GraphGLLinkOption?>(
                                    unsafeJso {
                                        source = StringOrDouble(0.0)
                                        target = StringOrDouble(1.0)
                                        typedValue = OptionDataValueOrArray(2.0)
                                    },
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
                    .unsafeCast<echartsgl.options.GraphGLSeriesOption>()
                assertEquals(EChartsGlSeriesTypes.graphGL, series.type)
                assertEquals("forceAtlas2", series.layout)
                assertEquals(false, series.forceAtlas2!!.GPU)
                assertEquals(2.0, series.forceAtlas2!!.steps)
                assertEquals(4.0, series.forceAtlas2!!.maxSteps)
                assertEquals(0.5, series.forceAtlas2!!.gravity)
                assertEquals(false, series.forceAtlas2!!.barnesHutOptimize)
                assertEquals(false, series.focusNodeAdjacency)
                assertEquals(GraphGLFocusNodeAdjacencyOns.mouseover, series.focusNodeAdjacencyOn)
                assertEquals(1.5, series.zoom)
                assertEquals(2, series.offset!!.size)
                assertEquals(20.0, series.offset!![1])
                assertEquals(SymbolSize(8.0), series.symbolSize)
                assertEquals(0.4, series.typedLineStyle!!.opacity)
                assertEquals(true, series.typedLabel!!.show)
                assertEquals("group", series.categories!![0]!!.name)
                assertEquals(2, series.typedData!!.size)
                assertEquals(OptionId("a"), series.typedData!![0]!!.name)
                assertEquals(StringOrDouble(0.0), series.typedData!![0]!!.category)
                assertEquals(1, series.links!!.size)
                assertEquals(StringOrDouble(1.0), series.links!![0]!!.target)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * Asserts that the WebGL layer was created and painted into its own canvas.
     *
     * A chart with axes also has the 2D canvas of zrender, so the GL canvas is the one that has a
     * WebGL context.
     */
    private fun assertWebGlCanvas(container: HTMLElement) {
        assertNull(container.querySelector(".ecgl-nowebgl"), "WebGL is not available")
        val canvases = container.querySelectorAll("canvas")
        var glCanvases = 0
        for (index in 0 until canvases.length) {
            val canvas = canvases.item(index).unsafeCast<HTMLCanvasElement>()
            assertTrue(canvas.width > 0 && canvas.height > 0, "${canvas.width}x${canvas.height}")
            if (canvas.getContext(WebGLRenderingContext.ID) != null) {
                glCanvases++
            }
        }
        assertEquals(1, glCanvases)
    }
}

/**
 * Adds the two value axes a 2D GL series needs.
 */
private fun EChartsGlOption.axes() {
    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
}

/**
 * Builds an `x, y` row.
 */
private fun row(vararg values: Double): Array<OptionDataValue?> =
    Array(values.size) { index -> OptionDataValue(values[index]) }
