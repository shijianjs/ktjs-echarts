package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.toolbox

private val barLargeCategory = Category(Groups.BAR, "examples/bar/BarLarge.kt")

val barLargeEntry: CatalogEntry =
    barLargeCategory.chart("bar-large", "大数据量柱图", "Large Scale Bar Chart") {
            val largeData = largeBarData(500_000)
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = ECharts.format.addCommas(StringOrDouble(largeData.count.toDouble())) + " Data"
                            left = StringOrDouble(10.0)
                        },
                    )
                // The original also configures the `dataZoom` toolbox button (`yAxisIndex: false`)
                // and the pixel ratio of `saveAsImage`; the port keeps the default buttons.
                toolbox = toolbox(dataZoom = true, saveAsImage = true)
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                grid = one(unsafeJso<GridOption> { bottom = StringOrDouble(90.0) })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside },
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.slider },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = largeData.categories
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            splitArea = unsafeJso<SplitAreaOption> { show = false }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { splitArea = unsafeJso<SplitAreaOption> { show = false } })
                series =
                    one(
                        barSeries {
                            typedData = largeData.values
                            large = true
                        },
                    )
            }
        }
