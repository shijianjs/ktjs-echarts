package demo.examples.boxplot

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.AxisMinMax
import io.github.shijianjs.ktjs.echarts.options.BooleanOrStringArray
import io.github.shijianjs.ktjs.echarts.options.DataZoomOption
import io.github.shijianjs.ktjs.echarts.options.DatasetOption
import io.github.shijianjs.ktjs.echarts.DataZoomTypes
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.IndexOrIndexArray
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitAreaOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.boxplotSeries
import io.github.shijianjs.ktjs.echarts.options.one
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
