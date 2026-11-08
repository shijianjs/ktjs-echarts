package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.numberAt

private val docExampleScatterTutorialDataZoom1Category = Category(Groups.DOC, "examples/doc/DocExampleScatterTutorialDataZoom1.kt")

val docExampleScatterTutorialDataZoom1Entry: CatalogEntry =
    docExampleScatterTutorialDataZoom1Category.chart("doc-example/scatter-tutorial-dataZoom-1", "散点图与缩放（一）", "Scatter with dataZoom (1)") {
            val values =
                arrayOf(
                    doubleArrayOf(14.616, 7.241, 0.896),
                    doubleArrayOf(3.958, 5.701, 0.955),
                    doubleArrayOf(2.768, 8.971, 0.669),
                    doubleArrayOf(9.051, 9.710, 0.171),
                    doubleArrayOf(14.046, 4.182, 0.536),
                    doubleArrayOf(12.295, 1.429, 0.962),
                    doubleArrayOf(4.417, 8.167, 0.113),
                    doubleArrayOf(0.492, 4.771, 0.785),
                    doubleArrayOf(7.632, 2.605, 0.645),
                    doubleArrayOf(14.242, 5.042, 0.368),
                )
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
                    )
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            name = "scatter"
                            itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
                            symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) * 40) }
                            typedData = points(*values)
                        },
                    )
            }
        }
