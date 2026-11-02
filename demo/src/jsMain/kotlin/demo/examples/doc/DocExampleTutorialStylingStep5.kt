package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.sunburst.radius

private val docExampleTutorialStylingStep5Category = Category(Groups.DOC, "examples/doc/DocExampleTutorialStylingStep5.kt")

val docExampleTutorialStylingStep5Entry: CatalogEntry =
    docExampleTutorialStylingStep5Category.chart("doc-example/tutorial-styling-step5", "教程：按亮度着色", "Tutorial: lightness by value") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            min = 80.0
                            max = 600.0
                            inRange =
                                unsafeJso {
                                    colorLightness = DoubleOrDoubleArray(arrayOf(0.0, 1.0))
                                }
                        },
                    )
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            label = unsafeJso<LabelOption> { color = "rgba(255, 255, 255, 0.3)" }
                            labelLine = unsafeJso<PieLabelLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.3)") } }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#c23531")
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        }
