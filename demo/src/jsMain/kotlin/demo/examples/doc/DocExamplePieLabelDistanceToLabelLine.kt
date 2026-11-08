package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category

private val docExamplePieLabelDistanceToLabelLineCategory = Category(Groups.DOC, "examples/doc/DocExamplePieLabelDistanceToLabelLine.kt")

val docExamplePieLabelDistanceToLabelLineEntry: CatalogEntry =
    docExamplePieLabelDistanceToLabelLineCategory.chart("doc-example/pie-label-distanceToLabelLine", "饼图标签与引导线距离", "Pie label distance to label line") {
            unsafeJso<EChartsOption> {
                title = pieLabelTitles("distanceToLabelLine: 5", "distanceToLabelLine: 20")
                series =
                    pieLabelPair(
                        pieLabelSlices(true),
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.labelLine
                            distanceToLabelLine = 5.0
                        },
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.labelLine
                            distanceToLabelLine = 20.0
                        },
                    )
            }
        }
