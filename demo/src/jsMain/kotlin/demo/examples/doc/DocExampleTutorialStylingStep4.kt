package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.sunburst.radius

private val docExampleTutorialStylingStep4Category = Category(Groups.DOC, "examples/doc/DocExampleTutorialStylingStep4.kt")

val docExampleTutorialStylingStep4Entry: CatalogEntry =
    docExampleTutorialStylingStep4Category.chart("doc-example/tutorial-styling-step4", "教程：统一颜色", "Tutorial: single colour") {
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
                                    color = ZRColor("#c23531")
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        }
