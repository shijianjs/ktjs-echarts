package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import web.cssom.TextAlign
import demo.examples.Category
import demo.examples.sunburst.radius

private val docExampleSunburstLabelAlignCategory = Category(Groups.DOC, "examples/doc/DocExampleSunburstLabelAlign.kt")

val docExampleSunburstLabelAlignEntry: CatalogEntry =
    docExampleSunburstLabelAlignCategory.chart("doc-example/sunburst-label-align", "旭日图标签对齐", "Sunburst label align") {
            val data =
                Array(8) {
                    docSunburstNode(
                        name = "left",
                        label = insideLabel(TextAlign.left),
                        children = arrayOf(docSunburstNode(name = "right", value = 1.0, label = insideLabel(TextAlign.right))),
                    )
                }
            unsafeJso<EChartsOption> {
                series =
                    one(
                        sunburstSeries {
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble("20%"), StringOrDouble("90%")))
                            // `sort: null` keeps the order of the data, which is what the original
                            // asks for; ECharts types it as a nullable sort order.
                            sort = null
                            emphasis = unsafeJso { focus = EmphasisFocuses.none }
                            typedData = dataItems(*data)
                            label = unsafeJso<SunburstLabelOption> { fontSize = StringOrDouble(14.0) }
                        },
                    )
            }
        }
