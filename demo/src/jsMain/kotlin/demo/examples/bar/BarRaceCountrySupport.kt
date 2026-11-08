package demo.examples.bar

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.round
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRStyleProps
import demo.examples.chord.column
import demo.examples.matrix.dimension
import demo.examples.shared.axisLabel
import demo.examples.shared.every
import demo.examples.shared.numeric
import demo.examples.shared.recordOf
import demo.examples.shared.row
import demo.examples.shared.textAt
import demo.examples.shared.textOf
import demo.examples.shared.valueText

/** Milliseconds between two years of the race, the upstream `updateFrequency`. */
internal const val RACE_INTERVAL = 2000

/** Index of the year column of `life-expectancy-table.json`, the column the race steps through. */
internal const val YEAR_COLUMN = 4

/** Index of the country column, used as the category of the y axis. */
internal const val COUNTRY_COLUMN = 3

/** Index of the income column, drawn as the bar; the upstream `dimension`. */
internal const val INCOME_COLUMN = 0

/**
 * Which entry of the year list the race starts at.
 *
 * Index 10 is 1890, because the header row of the table counts as the first entry — see the note
 * above.
 */
internal const val START_INDEX = 10

/** Color of a country the table does not have an entry for, ECharts' own default blue. */
internal const val FALLBACK_COLOR = "#5470c6"

/** The upstream `countryColors`: the dominant color of each flag. */
internal val COUNTRY_COLORS: Map<String, String> =
    mapOf(
        "Australia" to "#00008b",
        "Canada" to "#f00",
        "China" to "#ffde00",
        "Cuba" to "#002a8f",
        "Finland" to "#003580",
        "France" to "#ed2939",
        "Germany" to "#000",
        "Iceland" to "#003897",
        "India" to "#f93",
        "Japan" to "#bc002d",
        "North Korea" to "#024fa2",
        "South Korea" to "#000",
        "New Zealand" to "#00247d",
        "Norway" to "#ef2b2d",
        "Poland" to "#dc143c",
        "Russia" to "#d52b1e",
        "Turkey" to "#e30a17",
        "United Kingdom" to "#00247d",
        "United States" to "#b22234",
    )

/** One record of `emoji-flags.json`; only these two fields of the six are read. */
internal external interface EmojiFlag {
    /** Country name, spelled the way the life-expectancy table spells its country column. */
    val name: String

    /** The flag of that country. */
    val emoji: String
}

/** The flag lookup of the upstream `getFlag`, as a map built once per page mount. */
internal fun flagsOf(json: JsAny): Map<String, String> =
    json.unsafeCast<Array<EmojiFlag>>().associate { flag -> flag.name to flag.emoji }

/** The distinct year column of [table], header row included. */
internal fun yearsOf(table: Array<Array<OptionDataValue>>): List<String> {
    val years = mutableListOf<String>()
    for (row in table) {
        val year = textOf(row[YEAR_COLUMN])
        if (years.isEmpty() || years.last() != year) {
            years.add(year)
        }
    }
    return years
}

/** The rows of [year] without the header row, the value of `dataset.source` and of the series data. */
internal fun rowsOfYear(table: Array<Array<OptionDataValue>>, year: String): Array<Array<OptionDataValue>> =
    table.drop(1).filter { row -> textOf(row[YEAR_COLUMN]) == year }.toTypedArray()

/** [rows] as series data: a tuple item per row, which is what a row-major table of dimensions is. */
internal fun raceData(rows: Array<Array<OptionDataValue>>): Array<BarDataItem?> =
    Array(rows.size) { index -> BarDataItem(rows[index].unsafeCast<Array<OptionDataValue?>>()) }

/** The year drawn behind the bars by the `graphic` component. */
internal fun yearElement(year: String): GraphicComponentLooseOption =
    GraphicComponentLooseOption(
        unsafeJso<GraphicComponentOption> {
            elements =
                arrayOf(
                    unsafeJso<GraphicElement> {
                        type = "text"
                        right = StringOrDouble(160.0)
                        bottom = StringOrDouble(60.0)
                        style =
                            unsafeJso<ZRStyleProps> {
                                text = year
                                font = "bolder 80px monospace"
                                fill = ZRColor("rgba(100, 100, 100, 0.25)")
                            }
                        z = 100.0
                    },
                )
        },
    )

/** The label of a country: its name followed by its flag, drawn with the `flag` rich style. */
internal fun countryLabel(country: String, flags: Map<String, String>): String =
    "$country{flag|${flags[country].orEmpty()}}"

/** The option of the race at [startYear]; the timers only replace the data and the year caption. */
internal fun raceOption(
    rows: Array<Array<OptionDataValue>>,
    startYear: String,
    flags: Map<String, String>,
): EChartsOption =
    unsafeJso<EChartsOption> {
        grid =
            one(
                unsafeJso<GridOption> {
                    top = StringOrDouble(10.0)
                    bottom = StringOrDouble(30.0)
                    left = StringOrDouble(150.0)
                    right = StringOrDouble(80.0)
                },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    max = AxisMinMax("dataMax")
                    axisLabel =
                        unsafeJso<AxisLabelOption> {
                            // A value axis hands the formatter the tick value itself, not a params object.
                            formatter = LabelFormatter { params -> valueText(round(numeric(params))) }
                        }
                },
            )
        dataset = one(unsafeJso<DatasetOption> { source = datasetArrayRows(rows) })
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    type = AxisTypes.category
                    inverse = true
                    max = AxisMinMax(10.0)
                    axisLabel =
                        unsafeJso<AxisLabelOption> {
                            show = true
                            fontSize = StringOrDouble(14.0)
                            // A category axis hands the formatter the category, the country name here.
                            formatter = LabelFormatter { params -> countryLabel(textOf(params), flags) }
                            rich =
                                recordOf(
                                    "flag" to
                                        unsafeJso<TextStyleOption> {
                                            fontSize = StringOrDouble(25.0)
                                            padding = DoubleOrDoubleArray(5.0)
                                        },
                                ).unsafeCast<Record<String, TextStyleOption>>()
                        }
                    animationDuration = AnimationDuration(300.0)
                    animationDurationUpdate = AnimationDuration(300.0)
                },
            )
        series =
            arrayOf<SeriesOption>(
                barSeries {
                    realtimeSort = true
                    seriesLayoutBy = SeriesLayoutBys.column
                    itemStyle =
                        unsafeJso<ItemStyleOption> {
                            color =
                                ZRColor { params ->
                                    COUNTRY_COLORS[textAt(params.value, COUNTRY_COLUMN)] ?: FALLBACK_COLOR
                                }
                        }
                    encode =
                        unsafeJso<OptionEncode> {
                            x = OptionEncodeValue(DimensionLoose(INCOME_COLUMN.toDouble()))
                            y = OptionEncodeValue(DimensionLoose(COUNTRY_COLUMN.toDouble()))
                        }
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            precision = StringOrDouble(1.0)
                            position = LabelPosition("right")
                            valueAnimation = true
                            fontFamily = "monospace"
                        }
                },
            )
        // The first frame is drawn without animation; every year then takes the whole interval.
        animationDuration = AnimationDuration(0.0)
        animationDurationUpdate = AnimationDuration(RACE_INTERVAL.toDouble())
        animationEasing = AnimationEasings.linear
        animationEasingUpdate = AnimationEasings.linear
        graphic = arrayOf(yearElement(startYear))
    }

/** The tick of one year: the rows of that year replace the series and the caption follows. */
internal fun setYear(chart: EChartsInstance, year: String, rows: Array<Array<OptionDataValue>>) {
    chart.setOption(
        unsafeJso<EChartsOption> {
            series = arrayOf<SeriesOption>(barSeries { typedData = raceData(rows) })
            graphic = arrayOf(yearElement(year))
        },
    )
}
