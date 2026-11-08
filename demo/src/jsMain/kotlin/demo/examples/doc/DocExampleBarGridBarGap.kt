package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel

private val docExampleBarGridBarGapCategory = Category(Groups.DOC, "examples/doc/DocExampleBarGridBarGap.kt")

val docExampleBarGridBarGapEntry: CatalogEntry =
    docExampleBarGridBarGapCategory.interactive(
            "doc-example/barGrid-barGap",
            "柱状图：同轴柱子的间距",
            "Bar grid: barGap",
        ) { chart, holder ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    animationDurationUpdate = AnimationDuration(1200.0)
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                data = strings("a", "b", "c", "d")
                                axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                                axisLabel =
                                    unsafeJso<AxisLabelOption> {
                                        formatter = LabelFormatter("barGap: '-100%'")
                                    }
                            },
                        )
                    yAxis = one(unsafeJso<YAxisOption> { splitLine = unsafeJso<SplitLineOption> { show = false } })
                    series =
                        arrayOf<SeriesOption>(
                            barSeries {
                                // The original nests `color` under `itemStyle.normal`.
                                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddd") }
                                silent = true
                                barWidth = StringOrDouble(40.0)
                                // A negative gap overlaps the two series.
                                barGap = StringOrDouble("-100%")
                                typedData = numbers(60.0, 60.0, 60.0, 60.0)
                            },
                            barSeries {
                                barWidth = StringOrDouble(40.0)
                                z = 10.0
                                typedData = numbers(45.0, 60.0, 13.0, 25.0)
                            },
                        )
                },
            )
            val barGaps = arrayOf("30%", "-100%")
            var loopIndex = 0
            holder.setInterval({
                val barGap = barGaps[loopIndex]
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            formatter = LabelFormatter("barGap: '$barGap'")
                                        }
                                },
                            )
                        // A one-item array merges into the first series.
                        series = arrayOf<SeriesOption>(barSeries { this.barGap = StringOrDouble(barGap) })
                    },
                )
                loopIndex = (loopIndex + 1) % barGaps.size
            }, 2000)
        }
