package echartsgl.options

import echartsgl.EChartsGlSeriesTypes
import js.objects.unsafeJso

/**
 * Creates a [Bar3DSeriesOption] with `type = "bar3D"`.
 */
inline fun bar3DSeries(block: Bar3DSeriesOption.() -> Unit = {}): Bar3DSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.bar3D
        block()
    }

/**
 * Creates a [Line3DSeriesOption] with `type = "line3D"`.
 */
inline fun line3DSeries(block: Line3DSeriesOption.() -> Unit = {}): Line3DSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.line3D
        block()
    }

/**
 * Creates a [Scatter3DSeriesOption] with `type = "scatter3D"`.
 */
inline fun scatter3DSeries(block: Scatter3DSeriesOption.() -> Unit = {}): Scatter3DSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.scatter3D
        block()
    }

/**
 * Creates a [Lines3DSeriesOption] with `type = "lines3D"`.
 */
inline fun lines3DSeries(block: Lines3DSeriesOption.() -> Unit = {}): Lines3DSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.lines3D
        block()
    }

/**
 * Creates a [SurfaceSeriesOption] with `type = "surface"`.
 */
inline fun surfaceSeries(block: SurfaceSeriesOption.() -> Unit = {}): SurfaceSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.surface
        block()
    }

/**
 * Creates a [Polygons3DSeriesOption] with `type = "polygons3D"`.
 */
inline fun polygons3DSeries(block: Polygons3DSeriesOption.() -> Unit = {}): Polygons3DSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.polygons3D
        block()
    }

/**
 * Creates a [Map3DSeriesOption] with `type = "map3D"`.
 */
inline fun map3DSeries(block: Map3DSeriesOption.() -> Unit = {}): Map3DSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.map3D
        block()
    }

/**
 * Creates a [ScatterGLSeriesOption] with `type = "scatterGL"`.
 */
inline fun scatterGLSeries(block: ScatterGLSeriesOption.() -> Unit = {}): ScatterGLSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.scatterGL
        block()
    }

/**
 * Creates a [GraphGLSeriesOption] with `type = "graphGL"`.
 */
inline fun graphGLSeries(block: GraphGLSeriesOption.() -> Unit = {}): GraphGLSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.graphGL
        block()
    }

/**
 * Creates a [FlowGLSeriesOption] with `type = "flowGL"`.
 */
inline fun flowGLSeries(block: FlowGLSeriesOption.() -> Unit = {}): FlowGLSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.flowGL
        block()
    }

/**
 * Creates a [LinesGLSeriesOption] with `type = "linesGL"`.
 */
inline fun linesGLSeries(block: LinesGLSeriesOption.() -> Unit = {}): LinesGLSeriesOption =
    unsafeJso {
        type = EChartsGlSeriesTypes.linesGL
        block()
    }
