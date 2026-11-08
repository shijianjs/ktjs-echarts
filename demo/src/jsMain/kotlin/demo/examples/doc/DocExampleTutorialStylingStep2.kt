package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.sunburst.radius

private val docExampleTutorialStylingStep2Category = Category(Groups.DOC, "examples/doc/DocExampleTutorialStylingStep2.kt")

val docExampleTutorialStylingStep2Entry: CatalogEntry =
    docExampleTutorialStylingStep2Category.chart("doc-example/tutorial-styling-step2", "教程：阴影", "Tutorial: shadow") {
            unsafeJso<EChartsOption> {
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            // The original writes `itemStyle.normal`; ECharts 6 has no `normal`
                            // state any more, so the shadow is set on the style itself.
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        }
