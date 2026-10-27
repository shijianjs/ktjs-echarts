package echarts.util

import echarts.ECharts
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Translated from `test/ut/spec/util/time.test.ts` of the upstream apache/echarts repository.
 *
 * The upstream cases build the sample dates from `... UTC` ISO strings and format them with
 * `isUTC = true`. Here the same instants are built from local date parts and formatted with
 * `isUTC = false`, which is equivalent (the local parts are exactly the expected values) and keeps
 * the assertions independent of the machine time zone. The UTC branch of `format` and `roundTime`
 * is covered separately with epoch-based instants.
 */
class TimeTest {
    // Upstream: `new Date('2003-04-09 01:04:02.300 UTC')`.
    private val time = Date(2003, 3, 9, 1, 4, 2, 300)

    // Upstream: `new Date('2023-12-19 11:44:33.003 UTC')`.
    private val anotherTime = Date(2023, 11, 19, 11, 44, 33, 3)

    // Upstream: `new Date('2023-01-12 13:09:01.035 UTC')`.
    private val oneMoreTime = Date(2023, 0, 12, 13, 9, 1, 35)

    @Test
    fun shouldFormatYear() {
        assertEquals("2003", format(time, "{yyyy}"))
        assertEquals("03", format(time, "{yy}"))

        assertEquals("2023", format(anotherTime, "{yyyy}"))
        assertEquals("23", format(anotherTime, "{yy}"))
    }

    @Test
    fun shouldFormatQuarter() {
        assertEquals("2", format(time, "{Q}"))
        assertEquals("4", format(anotherTime, "{Q}"))
    }

    @Test
    fun shouldFormatMonth() {
        assertEquals("April", format(time, "{MMMM}"))
        assertEquals("Apr", format(time, "{MMM}"))
        assertEquals("04", format(time, "{MM}"))
        assertEquals("4", format(time, "{M}"))

        assertEquals("December", format(anotherTime, "{MMMM}"))
        assertEquals("Dec", format(anotherTime, "{MMM}"))
        assertEquals("12", format(anotherTime, "{MM}"))
        assertEquals("12", format(anotherTime, "{M}"))
    }

    @Test
    fun shouldFormatDay() {
        assertEquals("09", format(time, "{dd}"))
        assertEquals("9", format(time, "{d}"))

        assertEquals("19", format(anotherTime, "{dd}"))
        assertEquals("19", format(anotherTime, "{d}"))
    }

    @Test
    fun shouldFormatDayOfWeek() {
        assertEquals("Wednesday", format(time, "{eeee}"))
        assertEquals("Wed", format(time, "{ee}"))
        assertEquals("3", format(time, "{e}"))

        assertEquals("Tuesday", format(anotherTime, "{eeee}"))
        assertEquals("Tue", format(anotherTime, "{ee}"))
        assertEquals("2", format(anotherTime, "{e}"))
    }

    @Test
    fun shouldFormatHour() {
        assertEquals("01", format(time, "{HH}"))
        assertEquals("1", format(time, "{H}"))

        assertEquals("11", format(anotherTime, "{HH}"))
        assertEquals("11", format(anotherTime, "{H}"))
    }

    @Test
    fun shouldFormatHour12() {
        assertEquals("01", format(time, "{hh}"))
        assertEquals("1", format(time, "{h}"))

        assertEquals("11", format(anotherTime, "{hh}"))
        assertEquals("11", format(anotherTime, "{h}"))
    }

    @Test
    fun shouldFormatMinute() {
        assertEquals("04", format(time, "{mm}"))
        assertEquals("4", format(time, "{m}"))

        assertEquals("44", format(anotherTime, "{mm}"))
        assertEquals("44", format(anotherTime, "{m}"))
    }

    @Test
    fun shouldFormatSecond() {
        assertEquals("02", format(time, "{ss}"))
        assertEquals("2", format(time, "{s}"))

        assertEquals("33", format(anotherTime, "{ss}"))
        assertEquals("33", format(anotherTime, "{s}"))
    }

    @Test
    fun shouldFormatMillisecondWithThreeDigits() {
        assertEquals("300", format(time, "{SSS}"))
        assertEquals("003", format(anotherTime, "{SSS}"))
    }

    @Test
    fun shouldFormatMillisecondWithOneDigit() {
        assertEquals("300", format(time, "{S}"))
        assertEquals("3", format(anotherTime, "{S}"))
    }

    @Test
    fun shouldFormatMeridian() {
        assertEquals("am", format(time, "{a}"))
        assertEquals("am", format(anotherTime, "{a}"))
        assertEquals("pm", format(oneMoreTime, "{a}"))
    }

    @Test
    fun shouldFormatMeridianInUppercase() {
        assertEquals("AM", format(time, "{A}"))
        assertEquals("AM", format(anotherTime, "{A}"))
        assertEquals("PM", format(oneMoreTime, "{A}"))
    }

    @Test
    fun shouldFormatMultiplePlaceholdersInOneTemplate() {
        assertEquals(
            "2003-04-09 01:04:02",
            format(time, "{yyyy}-{MM}-{dd} {HH}:{mm}:{ss}"),
        )
    }

    @Test
    fun shouldFormatInUtcWhenRequested() {
        // Epoch millis are time-zone independent, so the UTC branch is checked on its own.
        val epoch = Date(0.0)
        assertEquals("1970-01-01 00:00:00", ECharts.time.format(epoch, "{yyyy}-{MM}-{dd} {HH}:{mm}:{ss}", true, EN))
        assertEquals("1970-01-01", ECharts.time.format(epoch, "{yyyy}-{MM}-{dd}", true, EN))
    }

    @Test
    fun roundTimeUtc() {
        assertEquals("1970-01-01T00:00:00.000Z", ECharts.time.roundTime(Date(0.0), "year", true).toISOString())

        // '1986-10-06T03:25:45.678Z'
        val time1 = 3600.0 * 1000 * 24 * 6122 + 12345678
        assertEquals("1986-01-01T00:00:00.000Z", ECharts.time.roundTime(Date(time1), "year", true).toISOString())
        assertEquals("1986-10-01T00:00:00.000Z", ECharts.time.roundTime(Date(time1), "month", true).toISOString())
        assertEquals("1986-10-06T00:00:00.000Z", ECharts.time.roundTime(Date(time1), "day", true).toISOString())
        assertEquals("1986-10-06T03:00:00.000Z", ECharts.time.roundTime(Date(time1), "hour", true).toISOString())
        assertEquals("1986-10-06T03:25:00.000Z", ECharts.time.roundTime(Date(time1), "minute", true).toISOString())
        assertEquals("1986-10-06T03:25:45.000Z", ECharts.time.roundTime(Date(time1), "second", true).toISOString())
        assertEquals("1986-10-06T03:25:45.678Z", ECharts.time.roundTime(Date(time1), "millisecond", true).toISOString())
    }

    @Test
    fun roundTimeLocale() {
        // Upstream derives the instant from the local time-zone offset; using local date parts is
        // the same thing without depending on the offset value.
        val time1 = Date(1986, 9, 6, 11, 25, 45, 678)

        assertEquals(Date(1986, 0, 1).getTime(), ECharts.time.roundTime(Date(time1.getTime()), "year", false).getTime())
        assertEquals(Date(1986, 9, 1).getTime(), ECharts.time.roundTime(Date(time1.getTime()), "month", false).getTime())
        assertEquals(Date(1986, 9, 6).getTime(), ECharts.time.roundTime(Date(time1.getTime()), "day", false).getTime())
        assertEquals(
            Date(1986, 9, 6, 11).getTime(),
            ECharts.time.roundTime(Date(time1.getTime()), "hour", false).getTime(),
        )
        assertEquals(
            Date(1986, 9, 6, 11, 25).getTime(),
            ECharts.time.roundTime(Date(time1.getTime()), "minute", false).getTime(),
        )
        assertEquals(
            Date(1986, 9, 6, 11, 25, 45).getTime(),
            ECharts.time.roundTime(Date(time1.getTime()), "second", false).getTime(),
        )
        assertEquals(
            Date(1986, 9, 6, 11, 25, 45, 678).getTime(),
            ECharts.time.roundTime(Date(time1.getTime()), "millisecond", false).getTime(),
        )
    }

    @Test
    fun parseIsAnAliasOfParseDate() {
        // `echarts.time.parse` re-exports `parseDate`; both must accept the same inputs.
        assertEquals(1330819200000.0, ECharts.time.parse("2012-03-04T00:00:00Z").getTime())
        assertTrue(ECharts.time.parse(null).getTime().isNaN())
        assertTrue(ECharts.time.parse("asdf").getTime().isNaN())
    }
}

/**
 * `echarts.time.format` resolves `{MMMM}` / `{eeee}` through the ECharts locale model, which
 * defaults to the browser language. Upstream runs in jsdom (`en-US`); pin `"EN"` so that the
 * expectations do not depend on the machine locale.
 */
private val EN: JsAny = "EN".unsafeCast<JsAny>()

private fun format(date: Date, template: String): String = ECharts.time.format(date, template, false, EN)
