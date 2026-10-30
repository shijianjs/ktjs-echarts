package demo.json.examples

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonPrimitive

/*
 * The two pages whose component selection lives on the root option: `radar-multiple` binds three
 * radar coordinate systems through `series[].radarIndex`, and `scatter-single-axis` lays seven
 * `singleAxis` components on one canvas.
 *
 * Both pages move a callback into data: the scatter's `symbolSize(dataItem)` becomes a per-item
 * `symbolSize`, which is the JSON form of the same value (`dataItem[1] * 4`).
 *
 * The page positions are percentages, as upstream writes them. The Kotlin/JS port passes the same
 * numbers without the `%` suffix, so the two modes of this route lay out differently until that port
 * is fixed; the JSON side follows the original.
 */

private val radarScatterExtra = JsonCategory("json/examples/RadarScatterExtraJson.kt")

/** One named radar row: `{ name, value: [...] }`. */
private fun radarItem(name: String, vararg values: Number): DataItem =
    DataItem().apply {
        this.name = name
        value = DataValue.Array(values.map { valueOf(it) })
    }

/** `indicator: [{ name, max }, ...]`. */
private fun indicators(vararg entries: Pair<String, Number>): List<RadarIndicator> =
    entries.map { (name, max) ->
        RadarIndicator().apply {
            this.name = name
            this.max = max.toDouble()
        }
    }

/** The hours of `scatter-single-axis`. */
private val SINGLE_AXIS_HOURS =
    arrayOf(
        "12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a",
        "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p",
    )

/** The days of `scatter-single-axis`, top to bottom. */
private val SINGLE_AXIS_DAYS =
    arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/** The commits per hour of `scatter-single-axis`; `rows[day][hour]`, as in the Kotlin/JS port. */
private val SINGLE_AXIS_VALUES: Array<DoubleArray> =
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
private fun singleAxisPoints(day: Int): List<DataValue> =
    SINGLE_AXIS_HOURS.indices.map { hour ->
        val commits = SINGLE_AXIS_VALUES[day][hour]
        DataItem().apply {
            value = DataValue.Array(listOf(valueOf(hour), valueOf(commits)))
            symbolSize = NumberOrStringOrNumbers.of(commits * 4.0)
        }.asDataValue()
    }

public val radarScatterExtraJsonExamples: List<JsonExample> =
    listOf(
        radarScatterExtra.example("radar-multiple") {
            title { text = "Multiple Radar" }
            tooltip { trigger = TooltipTrigger.Axis }
            legend {
                left = str("center")
                entries("A Software", "A Phone", "Another Phone", "Precipitation", "Evaporation")
            }
            radar {
                indicator =
                    indicators(
                        "Brand" to 100,
                        "Content" to 100,
                        "Usability" to 100,
                        "Function" to 100,
                    )
                center = strings("25%", "40%")
                radius = NumberOrStringOrNumbers.of(80)
            }
            radar {
                indicator =
                    indicators(
                        "Look" to 100,
                        "Photo" to 100,
                        "System" to 100,
                        "Performance" to 100,
                        "Screen" to 100,
                    )
                radius = NumberOrStringOrNumbers.of(80)
                center = strings("50%", "60%")
            }
            radar {
                indicator = indicators(*(1..12).map { "${it}月" to 100 }.toTypedArray())
                center = strings("75%", "40%")
                radius = NumberOrStringOrNumbers.of(80)
            }
            series {
                radar {
                    tooltip = TooltipOption().apply { trigger = TooltipTrigger.Item }
                    areaStyle = AreaStyleOption()
                    items(radarItem("A Software", 60, 73, 85, 40))
                }
                radar {
                    radarIndex = 1
                    areaStyle = AreaStyleOption()
                    items(
                        radarItem("A Phone", 85, 90, 90, 95, 95),
                        radarItem("Another Phone", 95, 80, 95, 90, 93),
                    )
                }
                radar {
                    radarIndex = 2
                    areaStyle = AreaStyleOption()
                    items(
                        radarItem("Precipitation", 2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 75.6, 82.2, 48.7, 18.8, 6.0, 2.3),
                        radarItem("Evaporation", 2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 35.6, 62.2, 32.6, 20.0, 6.4, 3.3),
                    )
                }
            }
        },
        radarScatterExtra.example("scatter-single-axis") {
            tooltip { position = JsonPrimitive("top") }
            val seriesOptions = mutableListOf<SeriesOption>()
            for (index in SINGLE_AXIS_DAYS.indices) {
                title {
                    textVerticalAlign = "middle"
                    top = str("${((index + 0.5) * 100) / 7}%")
                    text = SINGLE_AXIS_DAYS[index]
                }
                singleAxis {
                    left = num(150)
                    type = AxisType.Category
                    boundaryGap = BoundaryGap.of(false)
                    categories(*SINGLE_AXIS_HOURS)
                    top = str("${(index * 100.0) / 7.0 + 5.0}%")
                    height = str("${100.0 / 7.0 - 10.0}%")
                    axisLabel { interval = num(2) }
                }
                seriesOptions +=
                    SeriesOption(SeriesType.Scatter).apply {
                        singleAxisIndex = index
                        coordinateSystem = "singleAxis"
                        data = singleAxisPoints(index)
                    }
            }
            series = seriesOptions
        },
    )
