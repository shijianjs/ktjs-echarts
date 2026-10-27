package echarts.util

import echarts.ECharts
import echarts.options.StringOrDouble
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Translated from `test/ut/spec/util/number.test.ts` of the upstream apache/echarts repository.
 *
 * Every case goes through the `echarts.number` namespace, which is the part of the upstream
 * `src/util/number` module that the published package re-exports. Upstream cases that only touch
 * non-exported helpers (`addSafe`, `getAcceptableTickPrecision`) are left out on purpose: they are
 * not reachable through the npm entry point, so they cannot be reached through this wrapper
 * either.
 */
class NumberTest {
    @Test
    fun linearMapAccuracyError() {
        // Should not be 17724.899999999998.
        assertEquals(17724.9, linearMap(100.0, arrayOf(0.0, 100.0), arrayOf(-15918.3, 17724.9), true))
        // Should not be 83.55999999999999.
        assertEquals(83.56, linearMap(100.0, arrayOf(0.0, 100.0), arrayOf(-62.83, 83.56), true))
    }

    @Test
    fun linearMapClamp() {
        // (1) normal order.
        val range = arrayOf(-15918.3, 17724.9)
        // bigger than max
        assertEquals(range[1], linearMap(100.1, arrayOf(0.0, 100.0), range, true))
        // smaller than min
        assertEquals(range[0], linearMap(-2.0, arrayOf(0.0, 100.0), range, true))
        // equals to max
        assertEquals(range[1], linearMap(100.0, arrayOf(0.0, 100.0), range, true))
        // equals to min
        assertEquals(range[0], linearMap(0.0, arrayOf(0.0, 100.0), range, true))

        // (2) inverse range
        val inverseRange = arrayOf(17724.9, -15918.3)
        assertEquals(inverseRange[1], linearMap(102.0, arrayOf(0.0, 100.0), inverseRange, true))
        assertEquals(inverseRange[0], linearMap(-0.001, arrayOf(0.0, 100.0), inverseRange, true))
        assertEquals(inverseRange[1], linearMap(100.0, arrayOf(0.0, 100.0), inverseRange, true))
        assertEquals(inverseRange[0], linearMap(0.0, arrayOf(0.0, 100.0), inverseRange, true))

        // (3) inverse domain
        assertEquals(range[0], linearMap(102.0, arrayOf(100.0, 0.0), range, true))
        assertEquals(range[1], linearMap(-0.001, arrayOf(100.0, 0.0), range, true))
        assertEquals(range[0], linearMap(100.0, arrayOf(100.0, 0.0), range, true))
        assertEquals(range[1], linearMap(0.0, arrayOf(100.0, 0.0), range, true))

        // (4) inverse domain, inverse range
        assertEquals(inverseRange[0], linearMap(100.1, arrayOf(100.0, 0.0), inverseRange, true))
        assertEquals(inverseRange[1], linearMap(-2.0, arrayOf(100.0, 0.0), inverseRange, true))
        assertEquals(inverseRange[0], linearMap(100.0, arrayOf(100.0, 0.0), inverseRange, true))
        assertEquals(inverseRange[1], linearMap(0.0, arrayOf(100.0, 0.0), inverseRange, true))
    }

    @Test
    fun linearMapNoClamp() {
        val range = arrayOf(-15918.3, 17724.9)
        assertEquals(17758.543199999996, linearMap(100.1, arrayOf(0.0, 100.0), range, false))
        assertEquals(-16591.164, linearMap(-2.0, arrayOf(0.0, 100.0), range, false))
        assertEquals(17724.9, linearMap(100.0, arrayOf(0.0, 100.0), range, false))
        assertEquals(-15918.3, linearMap(0.0, arrayOf(0.0, 100.0), range, false))

        val inverseRange = arrayOf(17724.9, -15918.3)
        assertEquals(-16591.163999999997, linearMap(102.0, arrayOf(0.0, 100.0), inverseRange, false))
        assertEquals(17725.236432, linearMap(-0.001, arrayOf(0.0, 100.0), inverseRange, false))
        assertEquals(-15918.3, linearMap(100.0, arrayOf(0.0, 100.0), inverseRange, false))
        assertEquals(17724.9, linearMap(0.0, arrayOf(0.0, 100.0), inverseRange, false))

        assertEquals(-16591.164, linearMap(102.0, arrayOf(100.0, 0.0), range, false))
        assertEquals(17725.236432, linearMap(-0.001, arrayOf(100.0, 0.0), range, false))
        assertEquals(-15918.3, linearMap(100.0, arrayOf(100.0, 0.0), range, false))
        assertEquals(17724.9, linearMap(0.0, arrayOf(100.0, 0.0), range, false))

        assertEquals(17758.5432, linearMap(100.1, arrayOf(100.0, 0.0), inverseRange, false))
        assertEquals(-16591.163999999997, linearMap(-2.0, arrayOf(100.0, 0.0), inverseRange, false))
        assertEquals(17724.9, linearMap(100.0, arrayOf(100.0, 0.0), inverseRange, false))
        assertEquals(-15918.3, linearMap(0.0, arrayOf(100.0, 0.0), inverseRange, false))
    }

    @Test
    fun linearMapNormal() {
        for (clamp in booleanArrayOf(true, false)) {
            assertEquals(488.4, linearMap(40.0, arrayOf(0.0, 100.0), arrayOf(444.0, 555.0), clamp))
            assertEquals(510.6, linearMap(40.0, arrayOf(0.0, 100.0), arrayOf(555.0, 444.0), clamp))
            assertEquals(488.4, linearMap(40.0, arrayOf(100.0, 0.0), arrayOf(555.0, 444.0), clamp))
            assertEquals(510.6, linearMap(40.0, arrayOf(100.0, 0.0), arrayOf(444.0, 555.0), clamp))
        }
    }

    @Test
    fun linearMapZeroInterval() {
        for (clamp in booleanArrayOf(true, false)) {
            // zero domain interval -> half of range.
            assertEquals(
                499.5,
                linearMap(40.0, arrayOf(1212222223.2323232, 1212222223.2323232), arrayOf(444.0, 555.0), clamp),
            )
            // zero range interval
            assertEquals(
                1221212.1221372238,
                linearMap(40.0, arrayOf(0.0, 100.0), arrayOf(1221212.1221372238, 1221212.1221372238), clamp),
            )
            // zero domain interval and range interval
            assertEquals(
                1221212.1221372238,
                linearMap(
                    40.0,
                    arrayOf(43.55454545, 43.55454545),
                    arrayOf(1221212.1221372238, 1221212.1221372238),
                    clamp,
                ),
            )
        }
    }

    @Test
    fun parseDate() {
        // Invalid Date
        for (invalid in listOf<JsAny?>(null, undefined(), string("asdf"), number(Double.NaN), string("-"), string("20120304"))) {
            assertTrue(parseDate(invalid).getTime().isNaN(), "expected an invalid date")
        }

        // Input instance of Date or timestamp
        assertEquals(1330819200000.0, parseDate(Date(1330819200000.0)).getTime())
        assertEquals(1330819200000.0, parseDate(number(1330819200000.0)).getTime())
        assertEquals(1330819200000.0, parseDate(number(1330819199999.99)).getTime())
        assertEquals(1330819200000.0, parseDate(number(1330819200000.01)).getTime())

        // ISO string
        assertEquals(Date(2012, 2, 1).getTime(), parseDate(string("2012-03")).getTime())
        assertEquals(Date(2012, 2, 4).getTime(), parseDate(string("2012-03-04")).getTime())
        assertEquals(Date(2012, 2, 4, 5).getTime(), parseDate(string("2012-03-04 05")).getTime())
        assertEquals(Date(2012, 2, 4, 5).getTime(), parseDate(string("2012-03-04T05")).getTime())
        assertEquals(Date(2012, 2, 4, 5, 6).getTime(), parseDate(string("2012-03-04 05:06")).getTime())
        assertEquals(Date(2012, 2, 4, 5, 6).getTime(), parseDate(string("2012-03-04T05:06")).getTime())
        assertEquals(Date(2012, 2, 4, 5, 6, 7).getTime(), parseDate(string("2012-03-04 05:06:07")).getTime())
        assertEquals(Date(2012, 2, 4, 5, 6, 7).getTime(), parseDate(string("2012-03-04T05:06:07")).getTime())
        assertEquals(Date(2012, 2, 4, 5, 6, 7, 123).getTime(), parseDate(string("2012-03-04T05:06:07.123")).getTime())
        assertEquals(Date(2012, 2, 4, 5, 6, 7, 123).getTime(), parseDate(string("2012-03-04T05:06:07,123")).getTime())
        // TODO upstream: `new Date('2012-03-04T05:06:07.12')` is the same as `'...07.120'`, not `'...07.012'`.
        assertEquals(Date(2012, 2, 4, 5, 6, 7, 12).getTime(), parseDate(string("2012-03-04T05:06:07.12")).getTime())
        assertEquals(Date(2012, 2, 4, 5, 6, 7, 1).getTime(), parseDate(string("2012-03-04T05:06:07.1")).getTime())
        assertEquals(1330837567123.0, parseDate(string("2012-03-04T05:06:07,123Z")).getTime())
        assertEquals(1330808767123.0, parseDate(string("2012-03-04T05:06:07.123+0800")).getTime())
        assertEquals(1330808767123.0, parseDate(string("2012-03-04T05:06:07.123+08:00")).getTime())
        assertEquals(1330862767123.0, parseDate(string("2012-03-04T05:06:07.123-0700")).getTime())
        assertEquals(1330862767123.0, parseDate(string("2012-03-04T05:06:07.123-07:00")).getTime())
        assertEquals(1330862767123.0, parseDate(string("2012-03-04T5:6:7.123-07:00")).getTime())
        assertEquals(1330837567123.0, parseDate(string("2012-03-04T05:06:07.123000Z")).getTime())

        // Other string
        assertEquals(Date(2012, 0, 1).getTime(), parseDate(string("2012")).getTime())
        assertEquals(Date(2012, 2, 1).getTime(), parseDate(string("2012/03")).getTime())
        assertEquals(Date(2012, 2, 4).getTime(), parseDate(string("2012/03/04")).getTime())
        assertEquals(Date(2012, 2, 4).getTime(), parseDate(string("2012-3-4")).getTime())
        assertEquals(Date(2012, 2, 1).getTime(), parseDate(string("2012/3")).getTime())
        assertEquals(Date(2012, 2, 4).getTime(), parseDate(string("2012/3/4")).getTime())
        assertEquals(Date(2012, 2, 4, 2, 5).getTime(), parseDate(string("2012/3/4 2:05")).getTime())
        assertEquals(Date(2012, 2, 4, 2, 5).getTime(), parseDate(string("2012/03/04 2:05")).getTime())
        assertEquals(Date(2012, 2, 4, 2, 5, 8).getTime(), parseDate(string("2012/3/4 2:05:08")).getTime())
        assertEquals(Date(2012, 2, 4, 2, 5, 8).getTime(), parseDate(string("2012/03/04 2:05:08")).getTime())
        assertEquals(Date(2012, 2, 4, 2, 5, 8, 123).getTime(), parseDate(string("2012/3/4 2:05:08.123")).getTime())
        assertEquals(Date(2012, 2, 4, 2, 5, 8, 123).getTime(), parseDate(string("2012/03/04 2:05:08.123")).getTime())
    }

    @Test
    fun reformIntervalsBasic() {
        // all
        assertEquals(
            listOf(
                listOf(Double.NEGATIVE_INFINITY, -70.0, 0.0, 0.0),
                listOf(-70.0, -26.0, 1.0, 1.0),
                listOf(-26.0, 18.0, 0.0, 1.0),
                listOf(18.0, 62.0, 0.0, 1.0),
                listOf(62.0, 150.0, 0.0, 1.0),
                listOf(150.0, Double.POSITIVE_INFINITY, 0.0, 0.0),
            ),
            reformIntervals(
                interval(Double.NEGATIVE_INFINITY, -70.0, 0.0, 0.0),
                interval(18.0, 62.0, 1.0, 1.0),
                interval(-70.0, -26.0, 1.0, 1.0),
                interval(-26.0, 18.0, 1.0, 1.0),
                interval(62.0, 150.0, 1.0, 1.0),
                interval(106.0, 150.0, 1.0, 1.0),
                interval(150.0, Double.POSITIVE_INFINITY, 0.0, 0.0),
            ),
        )

        // remove overlap
        assertEquals(
            listOf(listOf(18.0, 62.0, 1.0, 1.0), listOf(62.0, 150.0, 0.0, 1.0)),
            reformIntervals(interval(18.0, 62.0, 1.0, 1.0), interval(50.0, 150.0, 1.0, 1.0)),
        )

        // remove overlap on edge
        assertEquals(
            listOf(listOf(18.0, 62.0, 1.0, 1.0), listOf(62.0, 150.0, 0.0, 1.0)),
            reformIntervals(interval(18.0, 62.0, 1.0, 1.0), interval(62.0, 150.0, 1.0, 1.0)),
        )

        // remove included interval
        assertEquals(
            listOf(listOf(18.0, 62.0, 1.0, 1.0)),
            reformIntervals(
                interval(30.0, 40.0, 1.0, 1.0),
                interval(42.0, 54.0, 1.0, 1.0),
                interval(45.0, 60.0, 1.0, 1.0),
                interval(18.0, 62.0, 1.0, 1.0),
            ),
        )

        // remove edge
        assertEquals(
            listOf(listOf(18.0, 62.0, 1.0, 1.0)),
            reformIntervals(interval(18.0, 62.0, 1.0, 1.0), interval(30.0, 62.0, 1.0, 1.0)),
        )
    }

    @Test
    fun getPrecisionBasic() {
        assertPrecisionCases(::getPrecision)
    }

    @Test
    fun getPrecisionSafeBasic() {
        assertPrecisionCases(::getPrecisionSafe)
    }

    @Test
    fun getPrecisionEqualsGetPrecisionSafeOnRandomNumbers() {
        repeat(500) {
            val number = randomExponentialNumber()
            assertEquals(getPrecision(number), getPrecisionSafe(number), "precision mismatch for $number")
        }
    }

    @Test
    fun getPercentWithPrecisionBasic() {
        assertEquals(51.0, percent(arrayOf(50.5, 49.5), 0.0, 0.0))
        assertEquals(49.0, percent(arrayOf(50.5, 49.5), 1.0, 0.0))

        assertEquals(12.3, percent(arrayOf(12.34, 34.56, 53.1), 0.0, 1.0))
        assertEquals(34.6, percent(arrayOf(12.34, 34.56, 53.1), 1.0, 1.0))
        assertEquals(53.1, percent(arrayOf(12.34, 34.56, 53.1), 2.0, 1.0))

        assertEquals(17.0, percent(arrayOf(1.678, 4.783, 2.664, 0.875), 0.0, 0.0))
        assertEquals(48.0, percent(arrayOf(1.678, 4.783, 2.664, 0.875), 1.0, 0.0))
        assertEquals(26.0, percent(arrayOf(1.678, 4.783, 2.664, 0.875), 2.0, 0.0))
        assertEquals(9.0, percent(arrayOf(1.678, 4.783, 2.664, 0.875), 3.0, 0.0))
    }

    @Test
    fun getPercentWithPrecisionIgnoresNonNumericEntries() {
        val withDash = looseDoubleArray(number(1.678), number(4.783), number(2.664), number(0.875), string("-"))
        assertEquals(17.0, percent(withDash, 0.0, 0.0))
        assertEquals(48.0, percent(withDash, 1.0, 0.0))
        assertEquals(26.0, percent(withDash, 2.0, 0.0))
        assertEquals(9.0, percent(withDash, 3.0, 0.0))
        assertEquals(0.0, percent(withDash, 4.0, 0.0))

        val allEmpty = looseDoubleArray(
            number(0.0),
            undefined(),
            string("-"),
            null,
            number(Double.NaN),
        )
        for (index in 0 until 5) {
            assertEquals(0.0, percent(allEmpty, index.toDouble(), 0.0))
        }
    }

    @Test
    fun quantityExponentBasic() {
        assertEquals(0.0, ECharts.number.quantityExponent(1.0))
        assertEquals(0.0, ECharts.number.quantityExponent(9.0))
        assertEquals(1.0, ECharts.number.quantityExponent(12.0))
        assertEquals(2.0, ECharts.number.quantityExponent(123.0))
        assertEquals(3.0, ECharts.number.quantityExponent(1234.0))
        assertEquals(3.0, ECharts.number.quantityExponent(1234.5678))
        assertEquals(1.0, ECharts.number.quantityExponent(10.0))
        assertEquals(3.0, ECharts.number.quantityExponent(1000.0))
        assertEquals(4.0, ECharts.number.quantityExponent(10000.0))
    }

    @Test
    fun quantityExponentDecimals() {
        assertEquals(-1.0, ECharts.number.quantityExponent(0.1))
        assertEquals(-3.0, ECharts.number.quantityExponent(0.001))
        assertEquals(-3.0, ECharts.number.quantityExponent(0.00123))
    }

    @Test
    fun quantityExponentLargeNumber() {
        assertEquals(100.0, ECharts.number.quantityExponent(3.14e100))
        assertEquals(-100.0, ECharts.number.quantityExponent(3.14e-100))
    }

    @Test
    fun quantityExponentZero() {
        assertEquals(0.0, ECharts.number.quantityExponent(0.0))
    }

    @Test
    fun quantityBasic() {
        assertEquals(1.0, ECharts.number.quantity(1.0))
        assertEquals(1.0, ECharts.number.quantity(9.0))
        assertEquals(10.0, ECharts.number.quantity(12.0))
        assertEquals(100.0, ECharts.number.quantity(123.0))
        assertEquals(1000.0, ECharts.number.quantity(1234.0))
        assertEquals(1000.0, ECharts.number.quantity(1234.5678))
        assertEquals(10.0, ECharts.number.quantity(10.0))
        assertEquals(1000.0, ECharts.number.quantity(1000.0))
        assertEquals(10000.0, ECharts.number.quantity(10000.0))
    }

    @Test
    fun quantityDecimals() {
        assertEquals(0.1, ECharts.number.quantity(0.2))
        assertEquals(0.001, ECharts.number.quantity(0.002))
        assertEquals(0.001, ECharts.number.quantity(0.00123))
    }

    @Test
    fun quantityZero() {
        assertEquals(1.0, ECharts.number.quantity(0.0))
    }

    @Test
    fun niceExtreme() {
        // Should not be 0.30000000000000004
        assertEquals(0.3, nice(0.3869394696651766, true))
        assertEquals(0.5, nice(0.3869394696651766, null))
        assertEquals(0.00003, nice(0.00003869394696651766, true))
        assertEquals(0.00005, nice(0.00003869394696651766, false))
        assertEquals(10.0, nice(13.0, true))
        assertEquals(20.0, nice(13.0, null))
        assertEquals(3e21, nice(3900000000000000000021.0, true))
        assertEquals(5e21, nice(3900000000000000000021.0, null))
        assertEquals(5e-18, nice(0.00000000000000000656939, true))
        assertEquals(1e-17, nice(0.00000000000000000656939, null))
        assertEquals(0.1, nice(0.10000000000000000656939, true))
        assertEquals(0.2, nice(0.10000000000000000656939, null))
    }

    @Test
    fun numeric() {
        assertNumeric(123.0, 123.0, true)
        assertNumeric("123", 123.0, true)
        assertNumeric(-123.0, -123.0, true)
        assertNumeric("555", 555.0, true)
        assertNumeric("555.6", 555.6, true)
        assertNumeric("0555.6", 555.6, true)
        assertNumeric("-555.6", -555.6, true)
        assertNumeric(" 555 ", 555.0, true)
        assertNumeric(" -555 ", -555.0, true)
        assertNumeric(1e3, 1000.0, true)
        assertNumeric(-1e3, -1000.0, true)
        assertNumeric("1e3", 1000.0, true)
        assertNumeric("-1e3", -1000.0, true)
        assertNumeric(" \r \n 555 \t ", 555.0, true)
        assertNumeric(" \r \n -555.6 \t ", -555.6, true)
        assertNumeric(number(Double.POSITIVE_INFINITY), Double.POSITIVE_INFINITY, true)
        assertNumeric(number(Double.NEGATIVE_INFINITY), Double.NEGATIVE_INFINITY, true)
        assertNumeric("Infinity", Double.POSITIVE_INFINITY, true)
        assertNumeric("-Infinity", Double.NEGATIVE_INFINITY, true)

        assertNumeric(number(Double.NaN), Double.NaN, false)
        assertNumeric(number(-Double.NaN), Double.NaN, false)
        assertNumeric("NaN", Double.NaN, false)
        assertNumeric("-NaN", Double.NaN, false)
        assertNumeric(" NaN ", Double.NaN, false)
        assertNumeric(true, Double.NaN, false)
        assertNumeric(false, Double.NaN, false)
        assertNumeric(undefined(), Double.NaN, false)
        assertNumeric(null, Double.NaN, false)
        assertNumeric(Date(2012, 5, 12), Double.NaN, false)
        assertNumeric(emptyArray<JsAny>(), Double.NaN, false)
        assertNumeric(emptyObject(), Double.NaN, false)
        assertNumeric(regexOne(), Double.NaN, false)
        assertNumeric(regexZero(), Double.NaN, false)
        assertNumeric("555a", Double.NaN, false)
        assertNumeric("- 555", Double.NaN, false)
        assertNumeric("0. 5", Double.NaN, false)
        assertNumeric("0 .5", Double.NaN, false)
        assertNumeric("0x11", Double.NaN, false)
        assertNumeric("", Double.NaN, false)
        assertNumeric("\n", Double.NaN, false)
        assertNumeric("\n\r", Double.NaN, false)
        assertNumeric("\t", Double.NaN, false)
        assertNumeric(12288.toChar().toString(), Double.NaN, false)
        assertNumeric(functionValue(), Double.NaN, false)
    }

    @Test
    fun roundAndAscAreReachable() {
        // `round` and `asc` are re-exported next to the helpers above but are not covered by the
        // upstream unit test, so they are checked here.
        assertEquals(1.23, ECharts.number.round(StringOrDouble(1.2345), 2.0))
        // `roundLegacy` falls back to a precision of 10 when the argument is omitted, so 1.5 stays 1.5.
        assertEquals(1.5, ECharts.number.round(StringOrDouble(1.5), null))

        val ascending = ECharts.number.asc(arrayOf(3.0, 1.0, 2.0))
        assertEquals(listOf(1.0, 2.0, 3.0), ascending.toList())
    }

    @Test
    fun pixelPrecisionAndRadiansAreReachable() {
        // Also exported from `echarts.number` but absent from the upstream unit test.
        assertEquals(0.0, ECharts.number.getPixelPrecision(arrayOf(0.0, 1000.0), arrayOf(0.0, 100.0)))
        assertEquals(0.0, ECharts.number.remRadian(kotlin.math.PI * 2))
        assertEquals(1.0, ECharts.number.remRadian(kotlin.math.PI * 2 + 1))
        assertTrue(ECharts.number.isRadianAroundZero(0.0))
        assertFalse(ECharts.number.isRadianAroundZero(1.0))
    }

    @Test
    fun maxSafeIntegerMatchesTheJavaScriptLimit() {
        assertEquals(9007199254740991.0, ECharts.number.MAX_SAFE_INTEGER)
    }

    private fun assertPrecisionCases(fn: (Double) -> Double) {
        assertEquals(0.0, fn(10.0))
        assertEquals(0.0, fn(1.0))
        assertEquals(0.0, fn(0.0))
        assertEquals(0.0, fn(1e29))
        assertEquals(1.0, fn(0.1))
        assertEquals(1.0, fn(0.100))
        assertEquals(4.0, fn(0.0032))
        assertEquals(13.0, fn(0.0000000000034))
        assertEquals(0.0, fn(1e100))
        assertEquals(0.0, fn(3.456e100))
        assertEquals(103.0, fn(3.456e-100))
        assertEquals(11.0, fn(3.4e-10))
        assertEquals(1.0, fn(3.4))
        assertEquals(0.0, fn(3.0))
        assertEquals(1.0, fn(0.3))
        assertEquals(1.0, fn(34.5))
        assertEquals(3.0, fn(0.345))
        assertEquals(0.0, fn(345.0))
        assertEquals(1.0, fn(45.6))
        assertEquals(5.0, fn(0.00456))
        assertEquals(0.0, fn(40.0))
        assertEquals(3.0, fn(0.004))
    }

    private fun randomExponentialNumber(): Double {
        val p1 = kotlin.math.round(Random.nextDouble() * 100).toInt().toString()
        val p2 = kotlin.math.round(Random.nextDouble() * 10000).toInt().toString()
        val p3 = (kotlin.math.round(Random.nextDouble() * 20).toInt() - 100).toString()
        return "${p1}.${p2}e${p3}".toDouble()
    }

    private fun assertNumeric(rawValue: Double, expected: Double, expectedIsNumeric: Boolean) =
        assertNumeric(number(rawValue), expected, expectedIsNumeric)

    private fun assertNumeric(rawValue: String, expected: Double, expectedIsNumeric: Boolean) =
        assertNumeric(string(rawValue), expected, expectedIsNumeric)

    private fun assertNumeric(rawValue: Boolean, expected: Double, expectedIsNumeric: Boolean) =
        assertNumeric(bool(rawValue), expected, expectedIsNumeric)

    private fun assertNumeric(rawValue: JsAny?, expected: Double, expectedIsNumeric: Boolean) {
        assertEquals(expectedIsNumeric, ECharts.number.isNumeric(rawValue), "isNumeric($rawValue)")
        val converted = ECharts.number.numericToNumber(rawValue)
        if (expected.isNaN()) {
            assertTrue(converted.isNaN(), "numericToNumber($rawValue) should be NaN, was $converted")
        } else {
            assertEquals(expected, converted, "numericToNumber($rawValue)")
        }
    }
}

private fun linearMap(value: Double, domain: Array<Double>, range: Array<Double>, clamp: Boolean): Double =
    ECharts.number.linearMap(value, domain, range, clamp)

private fun parseDate(value: JsAny?): Date = ECharts.number.parseDate(value)

private fun getPrecision(value: Double): Double = ECharts.number.getPrecision(StringOrDouble(value))

private fun getPrecisionSafe(value: Double): Double = ECharts.number.getPrecisionSafe(StringOrDouble(value))

private fun percent(values: Array<Double>, index: Double, precision: Double): Double =
    ECharts.number.getPercentWithPrecision(values, index, precision)

private fun nice(value: Double, round: Boolean?): Double =
    ECharts.number.nice(value, if (round == null) null else bool(round))

/**
 * Kotlin primitives are not `JsAny` subtypes; these helpers perform the same cast a JavaScript
 * caller gets for free. The runtime value stays an unboxed JS primitive.
 */
private fun number(value: Double): JsAny = value.unsafeCast<JsAny>()

private fun string(value: String): JsAny = value.unsafeCast<JsAny>()

private fun bool(value: Boolean): JsAny = value.unsafeCast<JsAny>()

/** An interval descriptor as accepted by `echarts.number.reformIntervals`. */
private external interface ReformInterval {
    var interval: Array<Double>
    var close: Array<Double>
}

private fun interval(from: Double, to: Double, closeFrom: Double, closeTo: Double): JsAny =
    unsafeJso<ReformInterval> {
        interval = arrayOf(from, to)
        close = arrayOf(closeFrom, closeTo)
    }

private fun reformIntervals(vararg intervals: JsAny): List<List<Double>> =
    ECharts.number.reformIntervals(arrayOf(*intervals))
        .map { entry ->
            val typed = entry.unsafeCast<ReformInterval>()
            listOf(typed.interval[0], typed.interval[1], typed.close[0], typed.close[1])
        }

/**
 * Builds an array that mixes numbers with values ECharts treats as missing (`null`, `undefined`,
 * `"-"`, `NaN`). The upstream cases do the same with a TypeScript cast.
 */
private fun looseDoubleArray(vararg values: JsAny?): Array<Double> =
    arrayOf(*values).unsafeCast<Array<Double>>()

private fun undefined(): JsAny? = js("undefined")

private fun emptyObject(): JsAny = js("({})")

private fun regexOne(): JsAny = js("/1/")

private fun regexZero(): JsAny = js("/0/")

private fun functionValue(): JsAny = js("(function () {})")
