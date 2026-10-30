package echartsgl

import echarts.AnimationEasings
import echarts.ECharts
import echarts.options.DoubleOrDoubleArray
import echarts.options.one
import echartsgl.options.EChartsGlOption
import echartsgl.options.Geo3DOption
import echartsgl.options.Grid3DOption
import echartsgl.options.ViewControlOption
import echartsgl.options.bar3DSeries
import js.objects.unsafeJso
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests the camera of a 3D component, `viewControl`.
 *
 * One `geo3D` scene carries every member: the component is the cheapest of the four owners of a
 * camera to bring up, and `ViewControlOption` is the very same interface for `grid3D`, `geo3D`,
 * `globe` and `map3D`, so a round trip through one of them covers the declaration.
 *
 * `componentViewControlMixin` writes `alpha`, `beta`, `distance` and `center` back into the model
 * option when the control reports an update, which `Geo3DView` does on every render. The camera is
 * initialised from those very options and nothing moves it in these cases, so the write-back is the
 * identity and `getOption` still returns what went in.
 */
class ViewControlTest {
    /**
     * Every member of the camera control survives `setOption` and `getOption`.
     */
    @Test
    fun viewControlOptionsRoundTrip(): Promise<Unit> {
        ECharts.use(arrayOf(Geo3DComponent))
        registerSquareMap()
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                geo3D =
                    one(
                        unsafeJso<Geo3DOption> {
                            map = SQUARE_MAP
                            silent = true
                            viewControl = unsafeJso { fullCamera() }
                        },
                    )
            },
        )
        return nextFrames(2).then {
            try {
                val view = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .geo3D!![0]
                    .viewControl!!
                assertEquals(ProjectionTypes.orthographic, view.projection)
                assertEquals(true, view.autoRotate)
                assertEquals(AutoRotateDirections.ccw, view.autoRotateDirection)
                assertEquals(6.0, view.autoRotateSpeed)
                assertEquals(1.5, view.autoRotateAfterStill)
                assertEquals(0.4, view.damping)
                assertEquals(
                    2.5,
                    view.rotateSensitivity!!.unsafeCast<Array<Double>>()[1],
                )
                assertEquals(1.2, view.zoomSensitivity)
                assertEquals(
                    0.5,
                    view.panSensitivity!!.unsafeCast<Array<Double>>()[0],
                )
                assertEquals(MouseButtons.right, view.panMouseButton)
                assertEquals(MouseButtons.middle, view.rotateMouseButton)
                assertEquals(120.0, view.distance)
                assertEquals(60.0, view.minDistance)
                assertEquals(300.0, view.maxDistance)
                assertEquals(90.0, view.orthographicSize)
                assertEquals(300.0, view.maxOrthographicSize)
                assertEquals(30.0, view.minOrthographicSize)
                assertEquals(1.0, view.center!![0])
                assertEquals(2.0, view.center!![1])
                assertEquals(3.0, view.center!![2])
                // Only a globe reads this one; here it has to survive the option merge.
                assertEquals(10.0, view.targetCoord!![0])
                assertEquals(-30.0, view.minAlpha)
                assertEquals(60.0, view.maxAlpha)
                assertEquals(-70.0, view.minBeta)
                assertEquals(70.0, view.maxBeta)
                assertEquals(true, view.animation)
                assertEquals(300.0, view.animationDurationUpdate)
                assertEquals(AnimationEasings.elasticOut, view.animationEasingUpdate)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * `grid3D` overrides the camera defaults of `componentViewControlMixin` (`alpha = 20`,
     * `beta = 40`, `distance = 200`, `minDistance = 40`, `maxDistance = 400`), so a component that
     * shares [ViewControlOption] still gets its own values, and the ones it does not override stay
     * at the mixin default.
     */
    @Test
    fun grid3DCameraKeepsItsOwnDefaults(): Promise<Unit> {
        ECharts.use(arrayOf(Bar3DChart, Grid3DComponent))
        val (chart, container) = createChart()
        chart.setOption(
            unsafeJso<EChartsGlOption> {
                cartesian3DAxes()
                grid3D =
                    one(
                        unsafeJso<Grid3DOption> {
                            boxHeight = 20.0
                            viewControl = unsafeJso {
                                projection = ProjectionTypes.orthographic
                                autoRotateAfterStill = 0.0
                            }
                        },
                    )
                series = one(
                    bar3DSeries { typedData = bar3DItems(0.0, 0.0, 1.0, 1.0, 1.0, 2.0) },
                )
            },
        )
        return nextFrames(2).then {
            try {
                val view = chart.getOption()
                    .unsafeCast<EChartsGlOption>()
                    .grid3D!![0]
                    .viewControl!!
                assertEquals(ProjectionTypes.orthographic, view.projection)
                assertEquals(0.0, view.autoRotateAfterStill)
                // The defaults of the grid, not those of the mixin.
                assertEquals(20.0, view.alpha)
                assertEquals(40.0, view.beta)
                assertEquals(200.0, view.distance)
                assertEquals(40.0, view.minDistance)
                assertEquals(400.0, view.maxDistance)
                // The mixin default, which the grid leaves alone.
                assertEquals(150.0, view.orthographicSize)
                assertWebGlCanvas(container)
            } finally {
                destroyChart(chart, container)
            }
        }
    }

    /**
     * Fills every member of [ViewControlOption] except [ViewControlOption.alpha] and
     * [ViewControlOption.beta], which are left to the component so that [grid3DCameraKeepsItsOwnDefaults]
     * can tell the mixin default from the grid one.
     */
    private fun ViewControlOption.fullCamera() {
        projection = ProjectionTypes.orthographic
        autoRotate = true
        autoRotateDirection = AutoRotateDirections.ccw
        autoRotateSpeed = 6.0
        autoRotateAfterStill = 1.5
        damping = 0.4
        rotateSensitivity = DoubleOrDoubleArray(arrayOf(1.0, 2.5))
        zoomSensitivity = 1.2
        panSensitivity = DoubleOrDoubleArray(arrayOf(0.5, 0.75))
        panMouseButton = MouseButtons.right
        rotateMouseButton = MouseButtons.middle
        distance = 120.0
        minDistance = 60.0
        maxDistance = 300.0
        orthographicSize = 90.0
        maxOrthographicSize = 300.0
        minOrthographicSize = 30.0
        center = arrayOf(1.0, 2.0, 3.0)
        targetCoord = arrayOf(10.0, 20.0)
        minAlpha = -30.0
        maxAlpha = 60.0
        minBeta = -70.0
        maxBeta = 70.0
        animation = true
        animationDurationUpdate = 300.0
        animationEasingUpdate = AnimationEasings.elasticOut
    }
}
