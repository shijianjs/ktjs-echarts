package echarts.options

import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import zrender.ZRLineType

/**
 * Covers the text-style, label and label-line members of `StyleOptions.kt`. Fields are set through
 * [unsafeJso] and read back so the typed property keeps mapping to the exact JavaScript key;
 * [LabelRotate] has no dedicated host field (its `rotate` is a plain number) so both factory
 * overloads are exercised and unwrapped directly.
 */
class StyleOptionTest {

    @Test
    fun textStyleBorderFieldsRoundTrip() {
        val style = unsafeJso<TextStyleOption> {
            textBorderColor = "#333333"
            textBorderWidth = 2.0
            textBorderType = ZRLineType("dashed")
            textBorderDashOffset = 4.0
        }
        assertEquals("#333333", style.textBorderColor)
        assertEquals(2.0, style.textBorderWidth)
        assertEquals("dashed", style.textBorderType!!.unsafeCast<String>())
        assertEquals(4.0, style.textBorderDashOffset)
    }

    @Test
    fun labelAndLabelLineFieldsRoundTrip() {
        val line = unsafeJso<LabelLineOption> {
            length2 = 16.0
            minTurnAngle = 30.0
            maxSurfaceAngle = 120.0
        }
        assertEquals(16.0, line.length2)
        assertEquals(30.0, line.minTurnAngle)
        assertEquals(120.0, line.maxSurfaceAngle)

        val label = unsafeJso<LabelOption> {
            valueAnimation = true
            minMargin = 5.0
            textMargin = DoubleOrDoubleArray(8.0)
        }
        assertEquals(true, label.valueAnimation)
        assertEquals(5.0, label.minMargin)
        assertEquals(8.0, label.textMargin!!.unsafeCast<Double>())
    }

    @Test
    fun labelRotateFactoriesWrapTheirValues() {
        assertEquals(45.0, LabelRotate(45.0).unsafeCast<Double>())
        assertEquals("radial", LabelRotate("radial").unsafeCast<String>())
    }
}
