package echarts.options

import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertSame
import zrender.RectLike
import zrender.ZRColor

/**
 * Covers the opaque union factories and the remaining structural fields declared in
 * `CommonTypes.kt`. Each union factory is a no-op cast, so wrapping and reading the value back
 * with [unsafeCast] proves the Kotlin type keeps mapping to the exact JavaScript value. The
 * structural fields are set through [unsafeJso] and read back so their typed accessors stay bound
 * to the upstream keys.
 */
class CommonTypesUnionTest {

    @Test
    fun unionFactoriesWrapTheirValuesByIdentity() {
        val marker = TooltipMarker("marker")
        assertEquals("marker", marker.unsafeCast<String>())

        val rich = unsafeJso<RichTextTooltipMarker> {
            content = "rich"
        }
        assertEquals("rich", rich.content)
        assertSame(rich, TooltipMarker(rich).unsafeCast<RichTextTooltipMarker>())

        assertEquals(true, BooleanOrDoubleOrString(true).unsafeCast<Boolean>())
        assertEquals(2.0, BooleanOrDoubleOrString(2.0).unsafeCast<Double>())
        assertEquals("text", BooleanOrDoubleOrString("text").unsafeCast<String>())

        assertEquals(true, BooleanOrStringArray(true).unsafeCast<Boolean>())
        val flagList = arrayOf(StringOrDouble("a"))
        assertSame(flagList, BooleanOrStringArray(flagList).unsafeCast<Array<StringOrDouble>>())

        assertEquals(false, SelectionMode(false).unsafeCast<Boolean>())
        assertEquals("single", SelectionMode("single").unsafeCast<String>())

        val date = Date(0.0)
        assertSame(date, OptionDataValueDate(date).unsafeCast<Date>())
        assertEquals("2026-01-01", OptionDataValueDate("2026-01-01").unsafeCast<String>())
        assertEquals(10.0, OptionDataValueDate(10.0).unsafeCast<Double>())

        val color = ZRColor("#fff")
        assertSame(color, ZRColorOrArray(color).unsafeCast<ZRColor>())
        val colorList = arrayOf(ZRColor("#000"), ZRColor("#111"))
        assertSame(colorList, ZRColorOrArray(colorList).unsafeCast<Array<ZRColor>>())
    }

    @Test
    fun datasetSourceFactoriesWrapEachShape() {
        val original = arrayOf(OptionDataItem(OptionDataValue(1.0)), OptionDataItem(OptionDataValue("a")))
        val fromOriginal = datasetOriginal(original).unsafeCast<Array<OptionDataItem>>()
        assertEquals(2, fromOriginal.size)

        val objectRows = arrayOf(recordOf("value" to OptionDataValue(3.0)))
        val fromObjectRows = datasetObjectRows(objectRows).unsafeCast<Array<js.objects.Record<String, OptionDataValue>>>()
        assertEquals(1, fromObjectRows.size)

        val keyedColumns = recordOf("dim" to arrayOf(OptionDataValue(1.0), OptionDataValue(2.0)))
        val fromKeyedColumns = datasetKeyedColumns(keyedColumns).unsafeCast<js.objects.Record<String, Array<OptionDataValue>>>()
        assertNotNull(fromKeyedColumns["dim"])

        val typedArray = doubleArrayOf(1.0, 2.0, 3.0)
        val fromTyped = datasetTypedArray(typedArray).unsafeCast<DoubleArray>()
        assertEquals(3, fromTyped.size)
    }

    @Test
    fun structuralFieldsRoundTrip() {
        val dimension = unsafeJso<DimensionDefinition> {
            displayName = "Sales"
        }
        assertEquals("Sales", dimension.displayName)

        val encode = unsafeJso<OptionEncode> {
            itemId = OptionEncodeValue(DimensionLoose("0"))
        }
        assertNotNull(encode.itemId)

        val transform = unsafeJso<DataTransformOption> {
            type = "filter"
            print = true
        }
        assertEquals(true, transform.print)

        val radar = unsafeJso<SeriesOnRadarOptionMixin> {
            radarIndex = 1.0
            radarId = "radar-a"
        }
        assertEquals(1.0, radar.radarIndex)
        assertEquals("radar-a", radar.radarId)

        val piece = unsafeJso<VisualPiece> {
            colorLightness = DoubleOrDoubleArray(0.1)
            colorHue = DoubleOrDoubleArray(arrayOf(0.0, 120.0))
            gte = 2.0
            lte = 8.0
        }
        assertNotNull(piece.colorLightness)
        assertNotNull(piece.colorHue)
        assertEquals(2.0, piece.gte)
        assertEquals(8.0, piece.lte)

        val transition = unsafeJso<UniversalTransitionOption> {
            divideShape = "split"
            seriesKey = StringOrStringArray("series-key")
        }
        assertEquals("split", transition.divideShape)
        assertNotNull(transition.seriesKey)

        val rect = unsafeJso<RectLike> {
            x = 0.0
            y = 0.0
            width = 100.0
            height = 50.0
        }
        val layout = unsafeJso<CoordinateSystemDataLayout> {
            contentRect = rect
            matrixXYLocatorRange = arrayOf(arrayOf(1.0, 2.0))
        }
        assertSame(rect, layout.contentRect)
        assertEquals(1, layout.matrixXYLocatorRange?.size)
    }
}
