package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category

private val docExamplePieLabelMarginCategory = Category(Groups.DOC, "examples/doc/DocExamplePieLabelMargin.kt")

val docExamplePieLabelMarginEntry: CatalogEntry =
    docExamplePieLabelMarginCategory.chart("doc-example/pie-label-margin", "饼图标签外边距", "Pie label margin") {
            unsafeJso<EChartsOption> {
                title = pieLabelTitles("margin: \"25%\"", "margin: 10")
                series =
                    pieLabelPair(
                        pieLabelSlices(false),
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.edge
                            margin = StringOrDouble("25%")
                        },
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.edge
                            margin = StringOrDouble(10.0)
                        },
                    )
            }
        }
