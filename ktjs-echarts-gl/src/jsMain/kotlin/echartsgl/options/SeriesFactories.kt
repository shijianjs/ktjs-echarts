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
