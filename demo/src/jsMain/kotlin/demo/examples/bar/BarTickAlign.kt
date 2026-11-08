package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val barTickAlignCategory = Category(Groups.BAR, "examples/bar/BarTickAlign.kt")

val barTickAlignEntry: CatalogEntry =
    barTickAlignCategory.chart("bar-tick-align", "坐标轴刻度与标签对齐", "Axis Align with Tick") {
            unsafeJso<EChartsOption> {
                tooltip = axisTooltip
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("3%")
                            right = StringOrDouble("4%")
                            bottom = StringOrDouble("3%")
                            containLabel = true
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*week)
                            axisTick = unsafeJso { alignWithLabel = true }
                        },
                    )
                yAxis = valueAxis
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Direct"
                            barWidth = StringOrDouble("60%")
                            typedData = numbers(10.0, 52.0, 200.0, 334.0, 390.0, 330.0, 220.0)
                        },
                    )
            }
        }
