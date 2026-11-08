package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.Lcg
import demo.examples.shared.toolbox

private val scatterLargeCategory = Category(Groups.SCATTER, "examples/scatter/ScatterLarge.kt")

val scatterLargeEntry: CatalogEntry =
    scatterLargeCategory.chart("scatter-large", "大规模散点图", "Large Scatter") {
            val random = Lcg(20260428)
            val dataA = largeScatterData(random, 0.0)
            val dataB = largeScatterData(random, 10.0)
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "${formatThousands(dataA.size + dataB.size)} Points"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> {})
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            left = StringOrDouble("center")
                            feature = unsafeJso<ToolboxFeature> { dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> {} }
                        },
                    )
                legend = one(unsafeJso<LegendOption> { orient = Orients.vertical; right = StringOrDouble(10.0) })
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> {})
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside },
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.slider },
                    )
                animation = false
                series = arrayOf<SeriesOption>(largeScatterSeries("A", dataA), largeScatterSeries("B", dataB))
            }
        }
