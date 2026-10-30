package echartsgl

import echarts.ECharts
import echarts.options.StringOrDouble
import echarts.options.one
import echartsgl.options.EChartsGlOption
import echartsgl.options.Geo3DOption
import echartsgl.options.GlobeOption
import echartsgl.options.Grid3DOption
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import web.html.HTMLElement

/**
 * Tests where a GL component is painted.
 *
 * `zlevel` picks the WebGL layer: `echarts-gl.js` keeps one `LayerGL` per zlevel and inserts it into
 * the zrender painter, so two components on the same zlevel share a canvas and two on different
 * zlevels get one each. `left` / `top` / `width` / `height` are the box layout read by
 * `getBoxLayoutParams()` in `grid3DCreator`, `geo3DCreator` and `globeCreator`, which fit the
 * coordinate system into that part of the canvas.
 */
class ComponentLayoutTest {
    /**
     * Two `geo3D` components share one canvas on the same zlevel and need two on different ones.
     */
    @Test
    fun distinctZlevelsGetTheirOwnWebGlLayer(): Promise<Unit> {
        ECharts.use(arrayOf(Geo3DComponent))
        registerSquareMap()
        val (shared, sharedContainer) = createChart()
        shared.setOption(twoGeo3D(2.0, 2.0))
        val (split, splitContainer) = createChart()
        split.setOption(twoGeo3D(3.0, 4.0))
        return nextFrames(2).then {
            try {
                val sharedCanvases = canvasCount(sharedContainer)
                assertEquals(
                    sharedCanvases + 1,
                    canvasCount(splitContainer),
                    "one more zlevel is one more layer",
                )
                val geo3D = split.getOption().unsafeCast<EChartsGlOption>().geo3D!!
                assertEquals(3.0, geo3D[0].zlevel)
                assertEquals(4.0, geo3D[1].zlevel)
            } finally {
                destroyChart(shared, sharedContainer)
                destroyChart(split, splitContainer)
            }
        }
    }

    /**
     * The box layout of the three components that have a coordinate system of their own. They share
     * one zlevel here, so the single WebGL layer of the chart is also asserted.
     */
    @Test
    fun boxLayoutFieldsRoundTripOnEachComponent(): Promise<Unit> {
        ECharts.use(arrayOf(Grid3DComponent, Geo3DComponent, GlobeComponent))
        registerSquareMap()
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                cartesian3DAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            left = StringOrDouble(1.0)
                            top = StringOrDouble(2.0)
                            width = StringOrDouble(3.0)
                            height = StringOrDouble(4.0)
                        },
                    )
                geo3D =
                    one(
                        unsafeJso<Geo3DOption> {
                            map = SQUARE_MAP
                            silent = true
                            left = StringOrDouble(10.0)
                            top = StringOrDouble(20.0)
                            width = StringOrDouble(30.0)
                            height = StringOrDouble(40.0)
                        },
                    )
                globe =
                    one(
                        unsafeJso<GlobeOption> {
                            silent = true
                            left = StringOrDouble("5%")
                            top = StringOrDouble("10%")
                            width = StringOrDouble("50%")
                            height = StringOrDouble("60%")
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val option = chart.getOption().unsafeCast<EChartsGlOption>()
                val grid3D = option.grid3D!![0]
                assertEquals(StringOrDouble(1.0), grid3D.left)
                assertEquals(StringOrDouble(2.0), grid3D.top)
                assertEquals(StringOrDouble(3.0), grid3D.width)
                assertEquals(StringOrDouble(4.0), grid3D.height)
                val geo3D = option.geo3D!![0]
                assertEquals(StringOrDouble(20.0), geo3D.top)
                assertEquals(StringOrDouble(30.0), geo3D.width)
                val globe = option.globe!![0]
                assertEquals(StringOrDouble("10%"), globe.top)
                assertEquals(StringOrDouble("60%"), globe.height)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * Builds a chart with two `geo3D` components on the given zlevels.
     */
    private fun twoGeo3D(
        firstZLevel: Double,
        secondZLevel: Double,
    ): EChartsGlOption = unsafeJso {
        geo3D = arrayOf(
            unsafeJso<Geo3DOption> {
                map = SQUARE_MAP
                silent = true
                zlevel = firstZLevel
            },
            unsafeJso<Geo3DOption> {
                map = SQUARE_MAP
                silent = true
                zlevel = secondZLevel
            },
        )
    }

    /**
     * Number of canvases the chart painted into [container], which is one per zlevel it uses.
     */
    private fun canvasCount(container: HTMLElement): Int =
        container.querySelectorAll("canvas").length
}
