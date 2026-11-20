package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category

internal class DocExampleScatterTutorialDataZoom2 {
    companion object {
        private val CATEGORY = Category(Groups.DOC, "examples/doc/DocExampleScatterTutorialDataZoom2.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart(
                    "doc-example/scatter-tutorial-dataZoom-2",
                    "散点图与缩放（二）",
                    "Scatter with dataZoom (2)",
                ) {
                    unsafeJso<EChartsOption> {
                        xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                        yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                        dataZoom =
                            arrayOf(
                                unsafeJso<DataZoomOption> {
                                    type = DataZoomTypes.slider
                                    start = 1.0
                                    end = 35.0
                                },
                                unsafeJso<DataZoomOption> {
                                    type = DataZoomTypes.inside
                                    start = 1.0
                                    end = 35.0
                                },
                            )
                        series = arrayOf<SeriesOption>(tutorialScatter("scatter"))
                    }
                }
    }
}
