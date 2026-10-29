package echartsgl

import echarts.AxisNameLocations
import echarts.AxisTypes
import echarts.ECharts
import echarts.options.AxisMinMax
import echarts.options.DoubleOrDoubleArray
import echarts.options.OptionDataValue
import echarts.options.one
import echartsgl.options.Axis3DOption
import echartsgl.options.Bar3DDataItem
import echartsgl.options.Bar3DSeriesOption
import echartsgl.options.EChartsGlOption
import echartsgl.options.Grid3DOption
import echartsgl.options.bar3DSeries
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import web.html.HTMLCanvasElement

/**
 * Tests the `bar3D` series and the `grid3D` options it is drawn with.
 *
 * `echarts-gl` has no upstream unit suite for these types, so the cases cover the wrapper-specific
 * contract: the option is accepted, the fields survive the `getOption` round trip, and the WebGL
 * layer is created and painted.
 */
class Bar3DSeriesTest {
    @Test
    fun bar3DSeriesRendersAGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        chart.setOption(bar3DOption())
        return nextFrames(2).then {
            try {
                val option = chart.getOption().unsafeCast<EChartsGlOption>()
                assertEquals(EChartsGlSeriesTypes.bar3D, option.series!![0].type)

                // A bar3D series paints into the WebGL layer only, so the container holds exactly
                // the canvas of that layer; the zrender canvas layer stays empty.
                assertNull(container.querySelector(".ecgl-nowebgl"), "WebGL is not available")
                val canvases = container.querySelectorAll("canvas")
                assertEquals(1, canvases.length)
                val canvas = canvases.item(0).unsafeCast<HTMLCanvasElement>()
                assertEquals("400px", canvas.style.width)
                assertEquals("300px", canvas.style.height)
                assertTrue(canvas.width > 0 && canvas.height > 0, "${canvas.width}x${canvas.height}")
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    @Test
    fun grid3DOptionsRoundTrip() {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        try {
            chart.setOption(bar3DOption())

            val grid = chart.getOption().unsafeCast<EChartsGlOption>().grid3D!![0]
            assertEquals(100.0, grid.boxWidth)
            assertEquals(100.0, grid.boxHeight)
            assertEquals(100.0, grid.boxDepth)
            assertEquals(20.0, grid.viewControl!!.alpha)
            assertEquals(40.0, grid.viewControl!!.beta)
            assertEquals(200.0, grid.viewControl!!.distance)
            assertEquals(30.0, grid.light!!.main!!.alpha)
            assertEquals(0.4, grid.light!!.ambient!!.intensity)
            assertEquals(true, grid.postEffect!!.bloom!!.enable)
            assertEquals(0.1, grid.postEffect!!.bloom!!.intensity)
            assertEquals(BooleanOrAutos.auto, grid.temporalSuperSampling!!.enable)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun axis3DOptionsRoundTrip() {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsGlOption> {
                    grid3D = one(unsafeJso<Grid3DOption> {})
                    xAxis3D = one(
                        unsafeJso<Axis3DOption> {
                            type = AxisTypes.value
                            name = "x"
                            nameGap = 24.0
                            nameLocation = AxisNameLocations.end
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(10.0)
                            splitNumber = 4.0
                        },
                    )
                    yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
                    zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
                    series = one(bar3DSeries { typedData = bars(1.0, 2.0, 3.0) })
                },
            )

            val xAxis = chart.getOption().unsafeCast<EChartsGlOption>().xAxis3D!![0]
            assertEquals("x", xAxis.name)
            assertEquals(24.0, xAxis.nameGap)
            assertEquals(AxisNameLocations.end, xAxis.nameLocation)
            assertEquals(0.0, xAxis.min!!.unsafeCast<Double>())
            assertEquals(10.0, xAxis.max!!.unsafeCast<Double>())
            assertEquals(4.0, xAxis.splitNumber)

            val yAxis = chart.getOption().unsafeCast<EChartsGlOption>().yAxis3D!![0]
            assertEquals(AxisTypes.value, yAxis.type)
            assertEquals(0.0, yAxis.grid3DIndex)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun bar3DSeriesOptionsRoundTrip() {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsGlOption> {
                    grid3D = one(unsafeJso<Grid3DOption> {})
                    xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
                    yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
                    zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
                    series = one(
                        bar3DSeries {
                            bevelSize = 0.3
                            bevelSmoothness = 4.0
                            barSize = DoubleOrDoubleArray(arrayOf(0.2, 0.4))
                            minHeight = 2.0
                            onGridPlane = GridPlaneTypes.xz
                            shading = ShadingTypes.lambert
                            typedBlendMode = GlBlendModes.lighter
                            typedItemStyle = unsafeJso { opacity = 0.8 }
                            typedLabel = unsafeJso { show = true; distance = 4.0 }
                            typedEmphasis = unsafeJso { label = unsafeJso { show = false } }
                            typedData = bars(1.0, 2.0, 3.0, 2.0, 1.0, 4.0)
                        },
                    )
                },
            )

            val series = chart.getOption()
                .unsafeCast<EChartsGlOption>()
                .series!![0]
                .unsafeCast<Bar3DSeriesOption>()
            assertEquals(EChartsGlSeriesTypes.bar3D, series.type)
            assertEquals(0.3, series.bevelSize)
            assertEquals(4.0, series.bevelSmoothness)
            val barSize = series.barSize!!.unsafeCast<Array<Double>>()
            assertEquals(2, barSize.size)
            assertEquals(0.2, barSize[0])
            assertEquals(0.4, barSize[1])
            assertEquals(2.0, series.minHeight)
            assertEquals(GridPlaneTypes.xz, series.onGridPlane)
            assertEquals(ShadingTypes.lambert, series.shading)
            assertEquals(GlBlendModes.lighter, series.typedBlendMode)
            assertEquals(0.8, series.typedItemStyle!!.opacity)
            assertEquals(true, series.typedLabel!!.show)
            assertEquals(4.0, series.typedLabel!!.distance)
            assertEquals(false, series.typedEmphasis!!.label!!.show)
            assertEquals(2, series.typedData!!.size)
        } finally {
            destroyChart(chart, container)
        }
    }

    /**
     * Builds a `bar3D` option on a default grid: three value axes and one bar series.
     */
    private fun bar3DOption(): EChartsGlOption = unsafeJso {
        grid3D = one(
            unsafeJso<Grid3DOption> {
                boxWidth = 100.0
                boxHeight = 100.0
                boxDepth = 100.0
                viewControl = unsafeJso {
                    alpha = 20.0
                    beta = 40.0
                    distance = 200.0
                }
                light = unsafeJso {
                    main = unsafeJso { alpha = 30.0; beta = 40.0 }
                    ambient = unsafeJso { intensity = 0.4 }
                }
                postEffect = unsafeJso {
                    enable = false
                    bloom = unsafeJso { enable = true; intensity = 0.1 }
                }
                temporalSuperSampling = unsafeJso { enable = BooleanOrAutos.auto }
            },
        )
        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
        series = one(bar3DSeries { typedData = bars(1.0, 2.0, 3.0, 2.0, 1.0, 4.0, 3.0, 3.0, 2.0) })
    }

    /**
     * Builds `bar3D` data items from flat `x, y, z` triples.
     */
    private fun bars(vararg xyz: Double): Array<Bar3DDataItem?> {
        require(xyz.size % 3 == 0) { "A bar3D data item needs three values" }
        return Array(xyz.size / 3) { index ->
            Bar3DDataItem(
                arrayOf<OptionDataValue?>(
                    OptionDataValue(xyz[index * 3]),
                    OptionDataValue(xyz[index * 3 + 1]),
                    OptionDataValue(xyz[index * 3 + 2]),
                ),
            )
        }
    }
}
