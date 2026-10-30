package echarts.options

import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import zrender.ZRColor

/**
 * Covers the root-option, media-query, geo/radar and axis-pointer members of `EChartsOption.kt`.
 * Each field is set through [unsafeJso] and read back so the typed property keeps mapping to the
 * exact JavaScript key; unions are wrapped with their factory and unwrapped with [unsafeCast].
 */
class EChartsRootOptionTest {

    @Test
    fun mediaQueryAndRootOptionFieldsRoundTrip() {
        val media = unsafeJso<MediaQueryOption> {
            minHeight = 100.0
            maxHeight = 600.0
            maxAspectRatio = 1.5
        }
        assertEquals(100.0, media.minHeight)
        assertEquals(600.0, media.maxHeight)
        assertEquals(1.5, media.maxAspectRatio)

        val option = unsafeJso<EChartsOption> {
            useUTC = true
            darkMode = BooleanOrString("dark")
        }
        assertEquals(true, option.useUTC)
        assertEquals("dark", option.darkMode!!.unsafeCast<String>())
    }

    @Test
    fun geoAndRadarFieldsRoundTrip() {
        val geo = unsafeJso<GeoOption> {
            nameProperty = "properties.name"
        }
        assertEquals("properties.name", geo.nameProperty)

        val radar = unsafeJso<RadarOption> {
            axisName = unsafeJso<LabelOption> {
                show = true
                color = "#333333"
            }
            axisNameGap = 12.0
        }
        assertNotNull(radar.axisName)
        assertEquals(12.0, radar.axisNameGap)
    }

    @Test
    fun axisPointerOptionAndLinkFieldsRoundTrip() {
        val pointer = unsafeJso<AxisPointerOption> {
            triggerEmphasis = true
            snap = true
            triggerTooltip = false
            shadowStyle = unsafeJso<AreaStyleOption> { color = ZRColor("#eeeeee") }
            crossStyle = unsafeJso<LineStyleOption> {
                color = ZRColor("#ff0000")
                width = 1.0
            }
        }
        assertEquals(true, pointer.triggerEmphasis)
        assertEquals(true, pointer.snap)
        assertEquals(false, pointer.triggerTooltip)
        assertNotNull(pointer.shadowStyle)
        assertEquals(1.0, pointer.crossStyle!!.width)

        val link = unsafeJso<AxisPointerLink> {
            radiusAxisId = arrayOf("radius-0")
            angleAxisId = arrayOf("angle-0")
            radiusAxisName = StringOrStringArray("radius")
            angleAxisName = StringOrStringArray(arrayOf("angle"))
            singleAxisName = StringOrStringArray("single")
        }
        assertEquals(1, link.radiusAxisId?.size)
        assertEquals(1, link.angleAxisId?.size)
        assertEquals("radius", link.radiusAxisName!!.unsafeCast<String>())
        assertNotNull(link.angleAxisName)
        assertEquals("single", link.singleAxisName!!.unsafeCast<String>())
    }

    @Test
    fun matrixDimensionDividerLineStyleRoundTrip() {
        val dimension = unsafeJso<MatrixDimensionOption> {
            dividerLineStyle = unsafeJso<LineStyleOption> {
                color = ZRColor("#cccccc")
                width = 2.0
            }
        }
        assertEquals(2.0, dimension.dividerLineStyle!!.width)
    }
}
