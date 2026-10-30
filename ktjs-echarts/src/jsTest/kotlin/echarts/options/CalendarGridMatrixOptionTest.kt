package echarts.options

import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import zrender.ZRColor

/**
 * Tests for the `calendar`, `grid` outer-bounds and `matrix` structural options.
 *
 * These fields live in `EChartsOption.kt` (`calendar`, `calendar.dayLabel` / `monthLabel` /
 * `yearLabel`, `matrix`) and `ComponentOptions.kt` (`grid.outerBounds*`). Each case builds the option
 * through [unsafeJso] and reads every field back, so the typed property keeps mapping to the exact
 * JavaScript key and the union wrappers accept their forms.
 */
class CalendarGridMatrixOptionTest {
    @Test
    fun calendarCellSizeLabelAndSplitLineFieldsRoundTrip() {
        val dayLabel = unsafeJso<CalendarDayLabelOption> {
            show = true
            firstDay = 1.0
            margin = StringOrDouble(8.0)
            nameMap = StringOrStringArray(arrayOf("Sunday", "Monday", "Tuesday"))
        }
        val monthLabel = unsafeJso<CalendarMonthLabelOption> {
            show = true
            margin = 12.0
            nameMap = StringOrStringArray(arrayOf("Jan", "Feb", "Mar"))
        }
        val yearLabel = unsafeJso<CalendarYearLabelOption> {
            show = true
            margin = 30.0
        }
        val calendar = unsafeJso<CalendarOption> {
            range = CalendarRange(arrayOf(DataZoomValue("2017-01-01"), DataZoomValue("2017-12-31")))
            cellSize = StringOrDoubleOrArray(20.0)
            this.dayLabel = dayLabel
            this.monthLabel = monthLabel
            this.yearLabel = yearLabel
            splitLine = unsafeJso<SplitLineOption> { show = true }
        }

        assertEquals(20.0, calendar.cellSize!!.unsafeCast<Double>())
        assertEquals(1.0, assertNotNull(calendar.dayLabel).firstDay)
        assertEquals(8.0, calendar.dayLabel!!.margin!!.unsafeCast<Double>())
        assertEquals(3, calendar.dayLabel!!.nameMap!!.unsafeCast<Array<String>>().size)
        assertEquals(12.0, assertNotNull(calendar.monthLabel).margin)
        assertEquals("Jan", calendar.monthLabel!!.nameMap!!.unsafeCast<Array<String>>()[0])
        assertEquals(30.0, assertNotNull(calendar.yearLabel).margin)
        assertTrue(assertNotNull(calendar.splitLine).show!!)
    }

    @Test
    fun gridOuterBoundsFieldsRoundTrip() {
        val grid = unsafeJso<GridOption> {
            outerBounds = unsafeJso<BoxLayoutOption> {
                width = StringOrDouble(120.0)
                height = StringOrDouble(80.0)
            }
            outerBoundsClampWidth = StringOrDouble(10.0)
            outerBoundsClampHeight = StringOrDouble(12.0)
        }

        assertEquals(120.0, assertNotNull(grid.outerBounds).width!!.unsafeCast<Double>())
        assertEquals(80.0, grid.outerBounds!!.height!!.unsafeCast<Double>())
        assertEquals(10.0, grid.outerBoundsClampWidth!!.unsafeCast<Double>())
        assertEquals(12.0, grid.outerBoundsClampHeight!!.unsafeCast<Double>())
    }

    @Test
    fun matrixCornerBackgroundAndBorderZ2FieldsRoundTrip() {
        val matrix = unsafeJso<MatrixOption> {
            corner = unsafeJso<MatrixCornerOption> {
                data = emptyArray<MatrixBodyCornerCellOption>()
            }
            backgroundStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#eee") }
            borderZ2 = 1.0
        }

        assertNotNull(matrix.corner)
        assertEquals(0, matrix.corner!!.data!!.size)
        assertEquals("#eee", assertNotNull(matrix.backgroundStyle).color!!.unsafeCast<String>())
        assertEquals(1.0, matrix.borderZ2)
    }
}
