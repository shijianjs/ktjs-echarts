package demo.examples.pie

import demo.CatalogEntry
import demo.Groups
import echarts.AnimationEasings
import echarts.AnimationTypes
import echarts.RoseTypes
import echarts.options.AnimationDelay
import echarts.options.BooleanOrDouble
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.ItemStyleOption
import echarts.options.LabelLineOption
import echarts.options.LabelOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.TextStyleOption
import echarts.options.TitleOption
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.dataItems
import echarts.options.one
import echarts.options.pieSeries
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.fixed
import demo.examples.shared.pieItem
import demo.examples.sunburst.radius

private val pieCustomCategory = Category(Groups.PIE, "examples/pie/PieCustom.kt")

val pieCustomEntry: CatalogEntry =
    pieCustomCategory.chart("pie-custom", "饼图自定义样式", "Customized Pie") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Customized Pie"
                            left = StringOrDouble("center")
                            top = StringOrDouble(20.0)
                            textStyle = unsafeJso<TextStyleOption> { color = "#ccc" }
                        },
                    )
                tooltip = tooltipItem
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            min = 80.0
                            max = 600.0
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    colorLightness = DoubleOrDoubleArray(arrayOf(0.0, 1.0))
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        pieSeries {
                            name = "Access From"
                            radius = StringOrDoubleOrArray("55%")
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                            // The original sorts the items by value before rendering.
                            typedData =
                                dataItems(
                                    pieItem("Video Ads", 235.0),
                                    pieItem("Union Ads", 274.0),
                                    pieItem("Email", 310.0),
                                    pieItem("Direct", 335.0),
                                    pieItem("Search Engine", 400.0),
                                )
                            roseType = RoseTypes.radius
                            label = unsafeJso<LabelOption> { color = "rgba(255, 255, 255, 0.3)" }
                            labelLine =
                                unsafeJso<LabelLineOption> {
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("rgba(255, 255, 255, 0.3)")
                                        }
                                    smooth = BooleanOrDouble(0.2)
                                    length = 10.0
                                    length2 = 20.0
                                }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#c23531")
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                            animationType = AnimationTypes.scale
                            animationEasing = AnimationEasings.elasticOut
                            // The original uses Math.random() per item; a fixed sequence keeps the
                            // page reproducible.
                            animationDelay = AnimationDelay { index, _ -> index * 17.0 % 200.0 }
                        },
                    )
            }
        }
