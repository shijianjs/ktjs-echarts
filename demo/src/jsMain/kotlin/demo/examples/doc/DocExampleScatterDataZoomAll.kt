package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.Lcg
import demo.examples.shared.legendNames

private val docExampleScatterDataZoomAllCategory = Category(Groups.DOC, "examples/doc/DocExampleScatterDataZoomAll.kt")

val docExampleScatterDataZoomAllEntry: CatalogEntry =
    docExampleScatterDataZoomAllCategory.chart(
            "doc-example/scatter-dataZoom-all",
            "散点图：双轴缩放",
            "Scatter with dataZoom on both axes",
        ) {
            val random = Lcg(20261028)
            fun data(xMax: Double): Array<DoubleArray> =
                Array(500) {
                    doubleArrayOf(
                        round3(random.next() * xMax),
                        round3(random.next() * 10.0),
                        round3(random.next()),
                    )
                }
            val data1 = data(15.0)
            val data2 = data(10.0)
            val data3 = data(15.0)
            unsafeJso<EChartsOption> {
                animation = false
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("scatter", "scatter2", "scatter3")
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                            splitLine = unsafeJso<SplitLineOption> { show = true }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                            splitLine = unsafeJso<SplitLineOption> { show = true }
                        },
                    )
                dataZoom =
                    arrayOf(
                        zoomWindow(
                            DataZoomTypes.slider,
                            1.0,
                            35.0,
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0)),
                        ),
                        zoomWindow(
                            DataZoomTypes.slider,
                            29.0,
                            36.0,
                            yAxisIndex = IndexOrIndexArray(arrayOf(0.0)),
                            left = StringOrDouble("93%"),
                        ),
                        zoomWindow(
                            DataZoomTypes.inside,
                            1.0,
                            35.0,
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0)),
                        ),
                        zoomWindow(
                            DataZoomTypes.inside,
                            29.0,
                            36.0,
                            yAxisIndex = IndexOrIndexArray(arrayOf(0.0)),
                        ),
                    )
                series =
                    arrayOf<SeriesOption>(
                        zoomScatter("scatter", data1),
                        zoomScatter("scatter2", data2),
                        zoomScatter("scatter3", data3),
                    )
            }
        }
