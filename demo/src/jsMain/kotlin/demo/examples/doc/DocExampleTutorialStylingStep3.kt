package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.sunburst.radius

private val docExampleTutorialStylingStep3Category = Category(Groups.DOC, "examples/doc/DocExampleTutorialStylingStep3.kt")

val docExampleTutorialStylingStep3Entry: CatalogEntry =
    docExampleTutorialStylingStep3Category.chart("doc-example/tutorial-styling-step3", "教程：暗色背景", "Tutorial: dark background") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
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
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        }
