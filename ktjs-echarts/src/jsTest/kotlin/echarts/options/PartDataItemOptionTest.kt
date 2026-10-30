package echarts.options

import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

/**
 * Covers the union factories and `pie` label members of `PartDataItemOptions.kt`. Every opaque
 * union is a no-op cast, so wrapping a value and reading it back with [unsafeCast] proves the
 * Kotlin type keeps mapping to the exact JavaScript value; the pie-label fields are set through
 * [unsafeJso] and read back.
 */
class PartDataItemOptionTest {

    @Test
    fun pieLabelUnionsAndFieldsRoundTrip() {
        assertEquals(30.0, PieLabelRotate(30.0).unsafeCast<Double>())
        assertEquals(true, PieLabelRotate(true).unsafeCast<Boolean>())
        assertEquals("radial", PieLabelRotate("radial").unsafeCast<String>())
        assertEquals("edge", PieLabelAlignTo("edge").unsafeCast<String>())

        val label = unsafeJso<PieLabelOption> {
            alignTo = PieLabelAlignTo("labelLine")
            edgeDistance = StringOrDouble("12%")
            bleedMargin = 12.0
            distanceToLabelLine = 2.5
        }
        assertEquals("labelLine", label.alignTo!!.unsafeCast<String>())
        assertEquals("12%", label.edgeDistance!!.unsafeCast<String>())
        assertEquals(12.0, label.bleedMargin)
        assertEquals(2.5, label.distanceToLabelLine)
    }

    @Test
    fun gaugeDataItemUnionsWrapNumericAndObject() {
        val numeric = GaugeDataItem(OptionDataValueNumeric(30.0))
        assertEquals(30.0, numeric.unsafeCast<Double>())

        val objectItem = unsafeJso<GaugeDataItemOption> {
            value = OptionDataValue(75.0)
        }
        assertSame(objectItem, GaugeDataItem(objectItem).unsafeCast<GaugeDataItemOption>())
    }

    @Test
    fun radarAndPictorialBarDataItemUnionsWrapTheirShapes() {
        val radarArray = arrayOf<OptionDataValue?>(OptionDataValue(1.0), OptionDataValue(2.0))
        assertEquals(2, RadarSeriesDataItem(radarArray).unsafeCast<Array<OptionDataValue?>>().size)

        val radarObject = unsafeJso<RadarSeriesDataItemOption> { name = OptionId("series") }
        assertSame(radarObject, RadarSeriesDataItem(radarObject).unsafeCast<RadarSeriesDataItemOption>())

        assertEquals(5.0, PictorialBarDataItem(OptionDataValue(5.0)).unsafeCast<Double>())

        val symbolArray = arrayOf<OptionDataValue?>(OptionDataValue(3.0))
        assertEquals(1, PictorialBarDataItem(symbolArray).unsafeCast<Array<OptionDataValue?>>().size)

        val symbolObject = unsafeJso<PictorialBarDataItemOption> {
            z = 4.0
            symbol = "circle"
        }
        assertSame(symbolObject, PictorialBarDataItem(symbolObject).unsafeCast<PictorialBarDataItemOption>())
        assertEquals(4.0, symbolObject.z)
    }
}
