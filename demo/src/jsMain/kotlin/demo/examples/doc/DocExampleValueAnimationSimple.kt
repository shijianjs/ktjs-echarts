package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.math.round
import demo.examples.Category
import demo.examples.shared.Lcg
import demo.examples.shared.axisLabel

private val docExampleValueAnimationSimpleCategory = Category(Groups.DOC, "examples/doc/DocExampleValueAnimationSimple.kt")

val docExampleValueAnimationSimpleEntry: CatalogEntry =
    docExampleValueAnimationSimpleCategory.interactive(
            "doc-example/value-animation-simple",
            "标签数值动画",
            "Value animation",
        ) { chart, holder ->
            val random = Lcg(20261028)
            val yMax = 1000.0
            val updateDuration = 3000
            fun nextValue(): Double = round(random.next() * yMax)

            fun bar(name: String, animate: Boolean, value: Double): BarSeriesOption =
                barSeries {
                    this.name = name
                    typedData = numbers(value)
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            valueAnimation = animate
                            position = LabelPosition("top")
                            fontSize = StringOrDouble(40.0)
                        }
                    barWidth = StringOrDouble(20.0)
                    animationDurationUpdate = AnimationDuration(updateDuration * 0.7)
                    animationEasingUpdate = AnimationEasings.linear
                }

            val initial = nextValue()
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                data = strings("a")
                                axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            },
                        )
                    yAxis = one(unsafeJso<YAxisOption> { max = AxisMinMax(yMax) })
                    legend = one(unsafeJso<LegendOption> { bottom = StringOrDouble(10.0) })
                    series =
                        arrayOf<SeriesOption>(
                            bar("valueAnimation: true", true, initial).apply {
                                barGap = StringOrDouble("1000%")
                                barCategoryGap = StringOrDouble(200.0)
                            },
                            bar("valueAnimation: false", false, initial),
                        )
                },
            )
            holder.setInterval(
                {
                    val value = nextValue()
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    barSeries { typedData = numbers(value) },
                                    barSeries { typedData = numbers(value) },
                                )
                        },
                    )
                },
                updateDuration,
            )
        }
