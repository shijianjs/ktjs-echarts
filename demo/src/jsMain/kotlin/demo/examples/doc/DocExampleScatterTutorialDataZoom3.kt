package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category

private val docExampleScatterTutorialDataZoom3Category = Category(Groups.DOC, "examples/doc/DocExampleScatterTutorialDataZoom3.kt")

val docExampleScatterTutorialDataZoom3Entry: CatalogEntry =
    docExampleScatterTutorialDataZoom3Category.chart(
            "doc-example/scatter-tutorial-dataZoom-3",
            "散点图与缩放（三）",
            "Scatter with dataZoom (3)",
        ) {
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            xAxisIndex = IndexOrIndexArray(0.0)
                            start = 10.0
                            end = 60.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            xAxisIndex = IndexOrIndexArray(0.0)
                            start = 10.0
                            end = 60.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            yAxisIndex = IndexOrIndexArray(0.0)
                            start = 30.0
                            end = 80.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            yAxisIndex = IndexOrIndexArray(0.0)
                            start = 30.0
                            end = 80.0
                        },
                    )
                series = arrayOf<SeriesOption>(tutorialScatter("scatter"))
            }
        }
