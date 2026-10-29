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
