package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category

private val docExamplePieLabelBleedMarginCategory = Category(Groups.DOC, "examples/doc/DocExamplePieLabelBleedMargin.kt")

val docExamplePieLabelBleedMarginEntry: CatalogEntry =
    docExamplePieLabelBleedMarginCategory.chart("doc-example/pie-label-bleedMargin", "饼图标签出血边距", "Pie label bleed margin") {
            unsafeJso<EChartsOption> {
                title = pieLabelTitles("bleedMargin: 50", "bleedMargin: 10")
                series =
                    pieLabelPair(
                        pieLabelSlices(true),
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.labelLine
                            bleedMargin = 50.0
                        },
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.labelLine
                            bleedMargin = 10.0
                        },
                    )
            }
        }
