package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.fixed

private val docExampleScatterJitterAvoidOverlapCategory = Category(Groups.DOC, "examples/doc/DocExampleScatterJitterAvoidOverlap.kt")

val docExampleScatterJitterAvoidOverlapEntry: CatalogEntry =
    docExampleScatterJitterAvoidOverlapCategory.interactive("doc-example/scatter-jitter-avoidOverlap", "散点抖动（避免重叠）", "Scatter with Jittering") { chart ->
            // The jitter is expressed in pixels, so it depends on the width of the plot area.
            val plotWidth = chart.getWidth() - 80.0 - 50.0
            // The original randomizes the third value; a fixed sequence keeps the page reproducible.
            val values =
                Array(7 * 30) { index ->
                    val day = index / 30
                    val position = index % 30
                    doubleArrayOf(day.toDouble(), kotlin.math.tan(position.toDouble()) / 2 + 7, (index % 100).toDouble() / 100.0)
                }
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "Scatter with Jittering" })
                    grid =
                        one(
                            unsafeJso<GridOption> {
                                left = StringOrDouble(80.0)
                                right = StringOrDouble(50.0)
                            },
                        )
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.category
                                jitter = plotWidth / 7 * 0.8
                                jitterOverlap = false
                                data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                max = AxisMinMax(10.0)
                                min = AxisMinMax(0.0)
                            },
                        )
                    series =
                        one(
                            scatterSeries {
                                name = "Sleeping Hours"
                                colorBy = "data"
                                typedData = points(*values)
                            },
                        )
                },
            )
        }
