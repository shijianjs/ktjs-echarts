package echartsgl.options

import echarts.options.EChartsOption

/**
 * An option that also contains the `echarts-gl` components.
 *
 * `echarts-gl` registers its components under new top-level keys, which [EChartsOption] does not
 * declare. The members here are the ones implemented by this wrapper; the rest of the option keeps
 * working because `EChartsGlOption` is an [EChartsOption].
 *
 * Pass the option to `EChartsInstance.setOption`, which accepts [EChartsOption]:
 *
 * ```kotlin
 * chart.setOption(
 *     unsafeJso<EChartsGlOption> {
 *         xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
 *         yAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
 *         zAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.value })
 *         grid3D = one(unsafeJso<Grid3DOption> { boxWidth = 100.0 })
 *         series = one(bar3DSeries { data = ... })
 *     },
 * )
 * ```
 *
 * The functions of the installed 3D charts are only available after the matching installers have
 * been passed to `ECharts.use`; see [Bar3DChart][echartsgl.Bar3DChart] and
 * [Grid3DComponent][echartsgl.Grid3DComponent].
 */
external interface EChartsGlOption : EChartsOption {
    /** 3D cartesian coordinate system components, created by [Grid3DComponent][echartsgl.Grid3DComponent]. */
    var grid3D: Array<Grid3DOption>?

    /** x axis of a [grid3D][grid3D]. */
    var xAxis3D: Array<Axis3DOption>?

    /** y axis of a [grid3D][grid3D]. */
    var yAxis3D: Array<Axis3DOption>?

    /** z axis of a [grid3D][grid3D]. */
    var zAxis3D: Array<Axis3DOption>?

    /** 3D geographic coordinate systems, created by [Geo3DComponent][echartsgl.Geo3DComponent]. */
    var geo3D: Array<Geo3DOption>?

    /** Globes, created by [GlobeComponent][echartsgl.GlobeComponent]. */
    var globe: Array<GlobeOption>?

    /** Mapbox GL maps, created by [Mapbox3DComponent][echartsgl.Mapbox3DComponent]. */
    var mapbox3D: Array<Mapbox3DOption>?

    /** maptalks maps, created by [Maptalks3DComponent][echartsgl.Maptalks3DComponent]. */
    var maptalks3D: Array<Maptalks3DOption>?
}
