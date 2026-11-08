package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LabelFormatter
import io.github.shijianjs.ktjs.echarts.options.LabelLayout
import io.github.shijianjs.ktjs.echarts.options.LabelLayoutOption
import io.github.shijianjs.ktjs.echarts.options.LabelLineOption
import io.github.shijianjs.ktjs.echarts.options.PieLabelAlignTos
import io.github.shijianjs.ktjs.echarts.options.PieLabelOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.TextStyleOption
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRFontWeights
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.recordOf
import demo.examples.sunburst.radius

private val pieLabelLineAdjustCategory = Category(Groups.PIE, "examples/pie/PieLabelLineAdjust.kt")

val pieLabelLineAdjustEntry: CatalogEntry =
    pieLabelLineAdjustCategory.interactive("pie-labelLine-adjust", "饼图引导线调整", "Label Line Adjust") { chart ->
            val shelves = bookShelves()
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title =
                        one(
                            unsafeJso<TitleOption> {
                                text = "阅读书籍分布"
                                left = StringOrDouble("center")
                                textStyle =
                                    unsafeJso<TextStyleOption> {
                                        color = "#999"
                                        fontWeight = ZRFontWeights.normal
                                        fontSize = StringOrDouble(14.0)
                                    }
                            },
                        )
                    series =
                        Array<SeriesOption>(shelves.size) { index ->
                            pieSeries {
                                radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(20.0), StringOrDouble(60.0)))
                                top = StringOrDouble(index * 33.3)
                                height = StringOrDouble("33.33%")
                                left = StringOrDouble("center")
                                width = StringOrDouble(400.0)
                                itemStyle =
                                    unsafeJso<ItemStyleOption> {
                                        borderColor = ZRColor("#fff")
                                        borderWidth = 1.0
                                    }
                                typedLabel =
                                    unsafeJso<PieLabelOption> {
                                        alignTo = PieLabelAlignTos.edge
                                        formatter = LabelFormatter("{name|{b}}\n{time|{c} 小时}")
                                        minMargin = 5.0
                                        edgeDistance = StringOrDouble(10.0)
                                        lineHeight = 15.0
                                        rich =
                                            recordOf(
                                                "time" to
                                                    unsafeJso<TextStyleOption> {
                                                        fontSize = StringOrDouble(10.0)
                                                        color = "#999"
                                                    },
                                            ).unsafeCast<Record<String, TextStyleOption>>()
                                    }
                                labelLine =
                                    unsafeJso<LabelLineOption> {
                                        length = 15.0
                                        length2 = 0.0
                                        maxSurfaceAngle = 80.0
                                    }
                                // The original trims the last segment of the leader line to the edge of
                                // the label, which needs the canvas width of the live chart.
                                labelLayout =
                                    LabelLayout { params ->
                                        val rect = params.labelRect
                                        val points = params.labelLinePoints
                                        if (rect != null && points != null && points.size > 2) {
                                            val isLeft = rect.x < chart.getWidth() / 2
                                            points[2][0] = if (isLeft) rect.x else rect.x + rect.width
                                        }
                                        unsafeJso<LabelLayoutOption> { labelLinePoints = points }
                                    }
                                typedData = shelves[index]
                            }
                        }
                },
            )
        }
