package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import web.cssom.TextAlign
import demo.examples.Category
import demo.examples.funnel.labels
import demo.examples.scatter.point
import demo.examples.shared.axisLabel
import demo.examples.shared.fixed

private val docExampleAxisLabelAlignMinMaxCategory = Category(Groups.DOC, "examples/doc/DocExampleAxisLabelAlignMinMax.kt")

val docExampleAxisLabelAlignMinMaxEntry: CatalogEntry =
    docExampleAxisLabelAlignMinMaxCategory.chart("doc-example/axis-label-align-min-max", "轴首尾标签对齐", "Axis min/max label alignment") {
            // The original fills the series with `Math.random()`; a fixed sequence keeps the page
            // reproducible, the point of the example is the alignment of the two end labels.
            val values = DoubleArray(200) { index -> (index * 37 % 100).toDouble() }
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    formatter = LabelFormatter("label of {value}")
                                    showMinLabel = true
                                    showMaxLabel = true
                                    alignMinLabel = TextAlign.left
                                    alignMaxLabel = TextAlign.right
                                }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    one(
                        lineSeries {
                            typedData = numbers(*values)
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            start = 20.0
                            end = 42.0
                        },
                    )
            }
        }
