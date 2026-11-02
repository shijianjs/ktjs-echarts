package demo.examples.boxplot

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.AxisMinMax
import echarts.options.BooleanOrStringArray
import echarts.options.DataZoomOption
import echarts.options.DatasetOption
import echarts.DataZoomTypes
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.IndexOrIndexArray
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.SplitAreaOption
import echarts.options.SplitLineOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.boxplotSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.dataset.transform
import demo.examples.gauge.center
import demo.examples.shared.numberRows

private val boxplotMultiCategory = Category(Groups.BOXPLOT, "examples/boxplot/BoxplotMulti.kt")

val boxplotMultiEntry: CatalogEntry =
    boxplotMultiCategory.chart("boxplot-multi", "多系列盒须图", "Multiple Categories") {
            val tables = generatedTables()
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Multiple Categories"
                            left = StringOrDouble("center")
                        },
                    )
                dataset =
                    arrayOf(
                        unsafeJso<DatasetOption> { source = numberRows(tables.subList(0, 18)) },
                        unsafeJso<DatasetOption> { source = numberRows(tables.subList(18, 36)) },
                        unsafeJso<DatasetOption> { source = numberRows(tables.subList(36, 54)) },
                        unsafeJso<DatasetOption> { fromDatasetIndex = 0.0; transform = arrayOf(boxplotTransform()) },
                        unsafeJso<DatasetOption> { fromDatasetIndex = 1.0; transform = arrayOf(boxplotTransform()) },
                        unsafeJso<DatasetOption> { fromDatasetIndex = 2.0; transform = arrayOf(boxplotTransform()) },
                    )
                legend = one(unsafeJso<LegendOption> { top = StringOrDouble("10%") })
                tooltip = boxplotTooltip()
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            top = StringOrDouble("20%")
                            right = StringOrDouble("10%")
                            bottom = StringOrDouble("15%")
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(true)
                            nameGap = 30.0
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "Value"
                            min = AxisMinMax(-400.0)
                            max = AxisMinMax(600.0)
                            splitArea = unsafeJso<SplitAreaOption> { show = false }
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside; start = 0.0; end = 20.0 },
                        unsafeJso<DataZoomOption> {
                            show = true
                            type = DataZoomTypes.slider
                            top = StringOrDouble("90%")
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0))
                            start = 0.0
                            end = 20.0
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        boxplotSeries { name = "category0"; datasetIndex = 3.0 },
                        boxplotSeries { name = "category1"; datasetIndex = 4.0 },
                        boxplotSeries { name = "category2"; datasetIndex = 5.0 },
                    )
            }
        }
