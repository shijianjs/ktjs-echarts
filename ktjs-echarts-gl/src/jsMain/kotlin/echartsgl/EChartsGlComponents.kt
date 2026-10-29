package echartsgl

import echarts.EChartsExtensionInstaller

/*
 * Component installers exported by the `echarts-gl/components` entry.
 *
 * Pass them to `ECharts.use` before `ECharts.init`, the same way the chart installers are used.
 * Reading one of the values also loads the `echarts-gl` core module, which registers the hook that
 * draws the WebGL layers.
 */

/**
 * Installs the 3D cartesian coordinate system, `grid3D` with `xAxis3D` / `yAxis3D` / `zAxis3D`.
 *
 * Use it together with the 3D cartesian series: [Bar3DChart], [Line3DChart], [Scatter3DChart] and
 * [SurfaceChart].
 */
val Grid3DComponent: EChartsExtensionInstaller get() = withEChartsGl(grid3DComponentInstaller)

/**
 * Installs the 3D geographic coordinate system, `geo3D`.
 *
 * Use it together with any series whose `coordinateSystem` is `"geo3D"`; the map has to be
 * registered with `ECharts.registerMap` first, exactly like the 2D `geo` component.
 */
val Geo3DComponent: EChartsExtensionInstaller get() = withEChartsGl(geo3DComponentInstaller)

/**
 * Installs the globe component, `globe`.
 *
 * A 3D earth on which series with `coordinateSystem: "globe"` are drawn.
 */
val GlobeComponent: EChartsExtensionInstaller get() = withEChartsGl(globeComponentInstaller)

/**
 * Installs the Mapbox GL coordinate system, `mapbox3D`.
 *
 * The page has to load the Mapbox GL library itself; `echarts-gl` only renders into its canvas.
 */
val Mapbox3DComponent: EChartsExtensionInstaller get() = withEChartsGl(mapbox3DComponentInstaller)

/**
 * Installs the maptalks coordinate system, `maptalks3D`.
 *
 * The page has to load the maptalks library itself; `echarts-gl` only renders into its canvas.
 */
val Maptalks3DComponent: EChartsExtensionInstaller get() = withEChartsGl(maptalks3DComponentInstaller)
