package demo.json.examples.shared

import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.dataset.row

/** One named radar row: `{ name, value: [...] }`. */
internal fun radarItemRadarScatterExtra(name: String, vararg values: Number): DataItem =
    DataItem().apply {
        this.name = name
        value = DataValue.Array(values.map { valueOf(it) })
    }

/** `indicator: [{ name, max }, ...]`. */
internal fun indicators(vararg entries: Pair<String, Number>): List<RadarIndicator> =
    entries.map { (name, max) ->
        RadarIndicator().apply {
            this.name = name
            this.max = max.toDouble()
        }
    }

/** The hours of `scatter-single-axis`. */
internal val SINGLE_AXIS_HOURS =
    arrayOf(
        "12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a",
        "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p",
    )

/** The days of `scatter-single-axis`, top to bottom. */
internal val SINGLE_AXIS_DAYS =
    arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/** The commits per hour of `scatter-single-axis`; `rows[day][hour]`, as in the Kotlin/JS port. */
internal val SINGLE_AXIS_VALUES: Array<DoubleArray> =
    arrayOf(
        doubleArrayOf(5.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0, 4.0, 1.0, 1.0, 3.0, 4.0, 6.0, 4.0, 4.0, 3.0, 3.0, 2.0, 5.0),
        doubleArrayOf(7.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 2.0, 2.0, 6.0, 9.0, 11.0, 6.0, 7.0, 8.0, 12.0, 5.0, 5.0, 7.0, 2.0),
        doubleArrayOf(1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 2.0, 1.0, 9.0, 8.0, 10.0, 6.0, 5.0, 5.0, 5.0, 7.0, 4.0, 2.0, 4.0),
        doubleArrayOf(7.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 5.0, 4.0, 7.0, 14.0, 13.0, 12.0, 9.0, 5.0, 5.0, 10.0, 6.0, 4.0, 4.0, 1.0),
        doubleArrayOf(1.0, 3.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 4.0, 4.0, 2.0, 4.0, 4.0, 14.0, 12.0, 1.0, 8.0, 5.0, 3.0, 7.0, 3.0, 0.0),
        doubleArrayOf(2.0, 1.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 4.0, 1.0, 5.0, 10.0, 5.0, 7.0, 11.0, 6.0, 0.0, 5.0, 3.0, 4.0, 2.0, 0.0),
        doubleArrayOf(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 2.0, 1.0, 3.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 2.0, 6.0),
    )

/** One `[hour, commits]` item of a day; the symbol size is the value the callback returned. */
internal fun singleAxisPoints(day: Int): List<DataValue> =
    SINGLE_AXIS_HOURS.indices.map { hour ->
        val commits = SINGLE_AXIS_VALUES[day][hour]
        DataItem().apply {
            value = DataValue.Array(listOf(valueOf(hour), valueOf(commits)))
            symbolSize = NumberOrStringOrNumbers.of(commits * 4.0)
        }.asDataValue()
    }
