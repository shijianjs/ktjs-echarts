package echartsgl

import echarts.AxisTypes
import echarts.ECharts
import echarts.options.DoubleOrDoubleArray
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.OptionId
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.StringOrDouble
import echarts.options.one
import echartsgl.options.Axis3DOption
import echartsgl.options.EChartsGlOption
import echartsgl.options.GlobeOption
import echartsgl.options.Grid3DOption
import echartsgl.options.Line3DDataItem
import echartsgl.options.Line3DSeriesOption
import echartsgl.options.Lines3DDataItem
import echartsgl.options.Lines3DDataItemOption
import echartsgl.options.Lines3DSeriesOption
import echartsgl.options.RealisticMaterialOption
import echartsgl.options.Scatter3DDataItem
import echartsgl.options.Scatter3DSeriesOption
import echartsgl.options.SurfaceFunction
import echartsgl.options.SurfaceSeriesOption
import echartsgl.options.line3DSeries
import echartsgl.options.lines3DSeries
import echartsgl.options.scatter3DSeries
import echartsgl.options.surfaceSeries
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import web.html.HTMLCanvasElement
import web.html.HTMLElement
import zrender.ZRColor

/**
 * Tests the `line3D`, `scatter3D` and `surface` series.
 *
 * `echarts-gl` has no upstream unit suite for these types, so the cases cover the wrapper-specific
 * contract: the option is accepted, the fields survive the `getOption` round trip, and the WebGL
 * layer is created and painted.
 */
class GlSeriesTest {
    @Test
    fun line3DSeriesRendersAGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(Line3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                axes()
                grid3D = one(unsafeJso<Grid3DOption> {})
                series =
                    one(
                        line3DSeries {
                            lineStyle = unsafeJso { width = 4.0 }
                            typedCoordinateSystem = CoordinateSystems3D.cartesian3D
                            typedData =
                                arrayOf<Line3DDataItem?>(
                                    Line3DDataItem(row(0.0, 0.0, 0.0)),
                                    Line3DDataItem(row(1.0, 2.0, 3.0)),
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
                assertEquals(EChartsGlSeriesTypes.line3D, series.type)
                assertEquals(4.0, series.lineStyle!!.width)
                assertEquals(CoordinateSystems3D.cartesian3D, series.typedCoordinateSystem)
                assertEquals(3, series.typedData!!.size)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * A `lines3D` series is drawn on the globe; the trail effect is enabled and the style is read
     * back from `getOption`.
     */
    @Test
    fun lines3DSeriesOnGlobeRendersAGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(Lines3DChart, GlobeComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                globe = one(unsafeJso<GlobeOption> {})
                series =
                    one(
                        lines3DSeries {
                            typedCoordinateSystem = CoordinateSystems3D.globe
                            typedBlendMode = GlBlendModes.lighter
                            effect = unsafeJso { show = true; trailWidth = 2.0; trailLength = 0.2 }
                            typedLineStyle =
                                unsafeJso {
                                    width = 0.0
                                    color = GlColor(ZRColor("rgb(50, 50, 150)"))
                                    opacity = 0.2
                                }
                            typedData =
                                arrayOf<Lines3DDataItem?>(
                                    Lines3DDataItem(arrayOf(arrayOf(0.0, 0.0), arrayOf(10.0, 20.0))),
                                    Lines3DDataItem(
                                        unsafeJso<Lines3DDataItemOption> {
                                            name = OptionId("route")
                                            typedValue = OptionDataValueOrArray(3.0)
                                            coords = arrayOf(arrayOf(-74.0, 40.0), arrayOf(2.0, 48.0))
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
                    .unsafeCast<Lines3DSeriesOption>()
                assertEquals(EChartsGlSeriesTypes.lines3D, series.type)
                assertEquals(CoordinateSystems3D.globe, series.typedCoordinateSystem)
                assertEquals(GlBlendModes.lighter, series.typedBlendMode)
                assertEquals(true, series.effect!!.show)
                assertEquals(2.0, series.effect!!.trailWidth)
                assertEquals(0.0, series.typedLineStyle!!.width)
                assertEquals(0.2, series.typedLineStyle!!.opacity)
                assertEquals(2, series.typedData!!.size)
                val item = series.typedData!![1]!!
                    .unsafeCast<Lines3DDataItemOption>()
                assertEquals(OptionId("route"), item.name)
                assertEquals(2, item.coords!!.size)
                assertEquals(1.0, item.lineStyle!!.width)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    @Test
    fun scatter3DSeriesRendersAGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(Scatter3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                axes()
                grid3D = one(unsafeJso<Grid3DOption> {})
                series =
                    one(
                        scatter3DSeries {
                            symbol = SymbolShape("circle")
                            symbolSize = SymbolSize(14.0)
                            typedBlendMode = GlBlendModes.lighter
                            typedItemStyle = unsafeJso { opacity = 0.8; borderWidth = 1.0 }
                            typedLabel = unsafeJso { show = true; fontSize = StringOrDouble(20.0) }
                            typedEmphasis = unsafeJso { label = unsafeJso { color = "#900" } }
                            typedData =
                                arrayOf<Scatter3DDataItem?>(
                                    Scatter3DDataItem(row(0.0, 0.0, 1.0)),
                                    Scatter3DDataItem(row(1.0, 2.0, 3.0)),
                                )
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val option = chart.getOption().unsafeCast<EChartsGlOption>()
                val series = option.series!![0].unsafeCast<Scatter3DSeriesOption>()
                assertEquals(EChartsGlSeriesTypes.scatter3D, series.type)
                assertEquals(SymbolShape("circle"), series.symbol)
                assertEquals(SymbolSize(14.0), series.symbolSize)
                assertEquals(GlBlendModes.lighter, series.typedBlendMode)
                assertEquals(0.8, series.typedItemStyle!!.opacity)
                assertEquals(1.0, series.typedItemStyle!!.borderWidth)
                assertEquals(true, series.typedLabel!!.show)
                assertEquals(StringOrDouble(20.0), series.typedLabel!!.fontSize)
                assertEquals("#900", series.typedEmphasis!!.label!!.color)
                assertEquals(2, series.typedData!!.size)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    @Test
    fun surfaceSeriesEquationRendersAGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(SurfaceChart, Grid3DComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                axes()
                grid3D = one(unsafeJso<Grid3DOption> {})
                series =
                    one(
                        surfaceSeries {
                            waveSurface()
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
                assertEquals(EChartsGlSeriesTypes.surface, series.type)
                assertEquals(0.05, series.equation!!.x!!.step)
                assertEquals(-3.0, series.equation!!.y!!.min)
                assertEquals(false, series.wireframe!!.show)
                assertEquals(ShadingTypes.color, series.shading)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    @Test
    fun surfaceParametricEquationRoundTrip() {
        ECharts.use(arrayOf(SurfaceChart, Grid3DComponent))
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsGlOption> {
                    axes()
                    grid3D = one(unsafeJso<Grid3DOption> {})
                    series =
                        one(
                            surfaceSeries {
                                parametric = true
                                parametricEquation =
                                    unsafeJso {
                                        u = unsafeJso { min = -PI; max = PI; step = PI / 20 }
                                        v = unsafeJso { min = 0.0; max = PI; step = PI / 20 }
                                        x = SurfaceFunction { u, v -> sin(v) * sin(u) }
                                        y = SurfaceFunction { u, v -> sin(v) * cos(u) }
                                        z = SurfaceFunction { _, v -> cos(v) }
                                    }
                                dataShape = arrayOf(41.0, 21.0)
                                typedItemStyle = unsafeJso { opacity = 0.6 }
                            },
                        )
                },
            )

            val series = chart.getOption()
                .unsafeCast<EChartsGlOption>()
                .series!![0]
                .unsafeCast<SurfaceSeriesOption>()
            assertEquals(true, series.parametric)
            assertEquals(PI / 20, series.parametricEquation!!.u!!.step)
            assertEquals(0.0, series.parametricEquation!!.v!!.min)
            assertEquals(2, series.dataShape!!.size)
            assertEquals(41.0, series.dataShape!![0])
            assertEquals(0.6, series.typedItemStyle!!.opacity)
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * The material textures of a realistic surface are tiled and offset by a single factor or by a
     * pair of factors; upstream reads `textureTiling` / `textureOffset` as `number | number[]`
     * (`graphicGL.setMaterialFromModel`), and the leather surface of the gallery tiles its roughness
     * and normal maps per axis.
     */
    @Test
    fun surfaceRealisticMaterialKeepsItsTextureTiling() {
        ECharts.use(arrayOf(SurfaceChart, Grid3DComponent))
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsGlOption> {
                    axes()
                    grid3D = one(unsafeJso<Grid3DOption> {})
                    series =
                        one(
                            surfaceSeries {
                                waveSurface()
                                shading = ShadingTypes.realistic
                                realisticMaterial =
                                    unsafeJso<RealisticMaterialOption> {
                                        roughness = NumberOrTexture(0.4)
                                        metalness = NumberOrTexture("data/img/gl/leather/metalness.jpg")
                                        normalTexture = TextureSource("data/img/gl/leather/normal.jpg")
                                        textureTiling = DoubleOrDoubleArray(arrayOf(4.0, 2.0))
                                        textureOffset = DoubleOrDoubleArray(0.5)
                                    }
                            },
                        )
                },
            )

            val series = chart.getOption()
                .unsafeCast<EChartsGlOption>()
                .series!![0]
                .unsafeCast<SurfaceSeriesOption>()
            val material = series.realisticMaterial!!
            assertEquals(ShadingTypes.realistic, series.shading)
            assertEquals(0.5, material.textureOffset!!.unsafeCast<Double>())
            val tiling = material.textureTiling!!.unsafeCast<Array<Double>>()
            assertEquals(2, tiling.size)
            assertEquals(4.0, tiling[0])
            assertEquals(2.0, tiling[1])
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * Asserts that the WebGL layer was created and painted into the only canvas of the container.
     */
    private fun assertWebGlCanvas(container: HTMLElement) {
        assertNull(container.querySelector(".ecgl-nowebgl"), "WebGL is not available")
        val canvases = container.querySelectorAll("canvas")
        assertEquals(1, canvases.length)
        val canvas = canvases.item(0).unsafeCast<HTMLCanvasElement>()
        assertTrue(canvas.width > 0 && canvas.height > 0, "${canvas.width}x${canvas.height}")
    }
}

/**
 * Adds the three value axes a `cartesian3D` series needs.
 */
private fun EChartsGlOption.axes() {
    xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
    zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
}

/**
 * A wave surface defined by a function instead of data.
 */
private fun SurfaceSeriesOption.waveSurface() {
    wireframe = unsafeJso { show = false }
    shading = ShadingTypes.color
    equation =
        unsafeJso {
            x = unsafeJso { min = -3.0; max = 3.0; step = 0.05 }
            y = unsafeJso { min = -3.0; max = 3.0; step = 0.05 }
            z = SurfaceFunction { x, y -> sin(x * x + y * y) * x / PI }
        }
}

/**
 * Builds an `x, y, z` row.
 */
private fun row(vararg values: Double): Array<OptionDataValue?> =
    Array(values.size) { index -> OptionDataValue(values[index]) }
