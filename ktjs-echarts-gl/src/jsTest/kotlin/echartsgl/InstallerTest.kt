package echartsgl

import echarts.ECharts
import kotlin.test.Test

/**
 * Tests the chart and component installers of `echarts-gl`.
 *
 * `ECharts.use` throws when a value is not a valid extension, so installing all of them verifies
 * the whole export list at once, including the modules that `echarts-gl` depends on implicitly.
 */
class InstallerTest {
    @Test
    fun everyChartInstallerIsExported() {
        ECharts.use(
            arrayOf(
                Bar3DChart,
                Line3DChart,
                Scatter3DChart,
                Lines3DChart,
                Polygons3DChart,
                SurfaceChart,
                Map3DChart,
                ScatterGLChart,
                GraphGLChart,
                FlowGLChart,
                LinesGLChart,
            ),
        )
    }

    @Test
    fun everyComponentInstallerIsExported() {
        ECharts.use(
            arrayOf(
                Grid3DComponent,
                Geo3DComponent,
                GlobeComponent,
                Mapbox3DComponent,
                Maptalks3DComponent,
            ),
        )
    }
}
