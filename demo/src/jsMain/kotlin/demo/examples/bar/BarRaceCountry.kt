package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.shared.every

private val barRaceCountryCategory = Category(Groups.BAR, "examples/bar/BarRaceCountry.kt")

val barRaceCountryEntry: CatalogEntry =
    barRaceCountryCategory.interactive(
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
        }
