package demo.examples.graphic

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.shared.Lcg

private val barRaceCategory = Category(Groups.GRAPHIC, "examples/graphic/BarRace.kt")

val barRaceEntry: CatalogEntry =
    barRaceCategory.interactive(
            "bar-race",
            "动态排序柱状图",
            "Bar Race",
            "每三秒给五根柱子加一截，`realtimeSort` 让 y 轴跟着重排，只显示最大的三根；" +
                "随机源换成种子固定的 Lcg，每次访问跑出同一场比赛。",
            "Every three seconds the five bars grow and `realtimeSort` reorders them, showing only " +
                "the three largest; a seeded Lcg keeps the race reproducible.",
        ) { chart, holder ->
            val random = Lcg(20160613)
            val values = DoubleArray(RACE_SIZE) { kotlin.math.round(random.next() * 200) }
            chart.setOption(raceOption(values))
            val run = {
                if (!chart.isDisposed()) {
                    for (index in values.indices) {
                        values[index] +=
                            if (random.next() > 0.9) {
                                kotlin.math.round(random.next() * 2000)
                            } else {
                                kotlin.math.round(random.next() * 200)
                            }
                    }
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    barSeries {
                                        type = io.github.shijianjs.ktjs.echarts.SeriesTypes.bar
                                        typedData = numbers(*values)
                                    },
                                )
                        },
                    )
                }
            }
            holder.setTimeout(run, 0)
            holder.setInterval(run, 3000)
        }
