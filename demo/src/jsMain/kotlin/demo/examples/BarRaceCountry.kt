package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.unsafeCast
import kotlin.math.round
import zrender.ZRColor
import zrender.ZRStyleProps

/*
 * `bar-race-country`: the income race of the bar family.
 *
 * The page reads two files. `life-expectancy-table.json` is the table several other pages already
 * use, and `emoji-flags.json` is the data of the `emoji-flags` npm package, which upstream loads
 * from a CDN and this site vendors (see the `SOURCE.md` of the data folder). Every year of the race
 * filters the rows of that year out of the table, so the year column is what drives both the timer
 * schedule and the `dataset`.
 *
 * The year list keeps the quirk of the original: upstream walks the whole table, header row
 * included, so the first entry of its `years` is the column name `Year` and its `startIndex` of 10
 * therefore lands on 1890. Dropping the header here would silently start the race a decade earlier.
 */

private val barRaceCountry = Category(Groups.BAR, "examples/BarRaceCountry.kt")

/** Milliseconds between two years of the race, the upstream `updateFrequency`. */
private const val RACE_INTERVAL = 2000

/** Index of the year column of `life-expectancy-table.json`, the column the race steps through. */
private const val YEAR_COLUMN = 4

/** Index of the country column, used as the category of the y axis. */
private const val COUNTRY_COLUMN = 3

/** Index of the income column, drawn as the bar; the upstream `dimension`. */
private const val INCOME_COLUMN = 0

/**
 * Which entry of the year list the race starts at.
 *
 * Index 10 is 1890, because the header row of the table counts as the first entry — see the note
 * above.
 */
private const val START_INDEX = 10

/** Color of a country the table does not have an entry for, ECharts' own default blue. */
private const val FALLBACK_COLOR = "#5470c6"

/** The upstream `countryColors`: the dominant color of each flag. */
private val COUNTRY_COLORS: Map<String, String> =
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
private external interface EmojiFlag {
    /** Country name, spelled the way the life-expectancy table spells its country column. */
    val name: String

    /** The flag of that country. */
    val emoji: String
}

/** The flag lookup of the upstream `getFlag`, as a map built once per page mount. */
private fun flagsOf(json: JsAny): Map<String, String> =
    json.unsafeCast<Array<EmojiFlag>>().associate { flag -> flag.name to flag.emoji }

/** The distinct year column of [table], header row included. */
private fun yearsOf(table: Array<Array<OptionDataValue>>): List<String> {
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
private fun rowsOfYear(table: Array<Array<OptionDataValue>>, year: String): Array<Array<OptionDataValue>> =
    table.drop(1).filter { row -> textOf(row[YEAR_COLUMN]) == year }.toTypedArray()

/** [rows] as series data: a tuple item per row, which is what a row-major table of dimensions is. */
private fun raceData(rows: Array<Array<OptionDataValue>>): Array<BarDataItem?> =
    Array(rows.size) { index -> BarDataItem(rows[index].unsafeCast<Array<OptionDataValue?>>()) }

/** The year drawn behind the bars by the `graphic` component. */
private fun yearElement(year: String): GraphicComponentLooseOption =
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
private fun countryLabel(country: String, flags: Map<String, String>): String =
    "$country{flag|${flags[country].orEmpty()}}"

/** The option of the race at [startYear]; the timers only replace the data and the year caption. */
private fun raceOption(
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
private fun setYear(chart: EChartsInstance, year: String, rows: Array<Array<OptionDataValue>>) {
    chart.setOption(
        unsafeJso<EChartsOption> {
            series = arrayOf<SeriesOption>(barSeries { typedData = raceData(rows) })
            graphic = arrayOf(yearElement(year))
        },
    )
}

val barRaceCountryEntries: List<CatalogEntry> =
    listOf(
        barRaceCountry.interactive(
            "bar-race-country",
            "动态排序柱状图 - 人均收入",
            "Bar Race - Country Income",
            "19 个国家的人均收入从 1890 年起每 2 秒换一次（1950 年前一跳十年，之后逐年），" +
                "`realtimeSort` 让 y 轴跟着重排，国家名后面用 rich 拼上 emoji 国旗，柱色取该国旗的主色；" +
                "`graphic` 在右下角写出当前年份。",
            "The income of 19 countries advances from 1890 every two seconds, a decade at a time until " +
                "1950 and a year at a time after that: `realtimeSort` reorders the y axis, a rich style " +
                "appends each flag to its country name, the bars take the dominant color of those flags, " +
                "and a `graphic` caption prints the year.",
        ) { chart, holder ->
            Promise.all(
                arrayOf(
                    loadJson("data/life-expectancy-table.json"),
                    loadJson("data/emoji-flags.json"),
                ),
            ).then { loaded ->
                if (!chart.isDisposed()) {
                    val table = loaded[0].unsafeCast<Array<Array<OptionDataValue>>>()
                    val flags = flagsOf(loaded[1])
                    val years = yearsOf(table)
                    val startYear = years[START_INDEX]
                    chart.setOption(raceOption(rowsOfYear(table, startYear), startYear, flags))
                    years.drop(START_INDEX + 1).forEachIndexed { step, year ->
                        holder.setTimeout(
                            {
                                if (!chart.isDisposed()) {
                                    setYear(chart, year, rowsOfYear(table, year))
                                }
                            },
                            step * RACE_INTERVAL,
                        )
                    }
                }
                null
            }
        },
    )
