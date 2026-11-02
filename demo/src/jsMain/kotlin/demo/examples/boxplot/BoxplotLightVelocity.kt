package demo.examples.boxplot

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.BooleanOrStringArray
import echarts.options.DatasetOption
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.SeriesOption
import echarts.options.SplitAreaOption
import echarts.options.SplitLineOption
import echarts.options.StringOrDouble
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.boxplotSeries
import echarts.options.one
import echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.dataset.transform
import demo.examples.shared.numberRows

private val boxplotLightVelocityCategory = Category(Groups.BOXPLOT, "examples/boxplot/BoxplotLightVelocity.kt")

val boxplotLightVelocityEntry: CatalogEntry =
    boxplotLightVelocityCategory.chart("boxplot-light-velocity", "基础盒须图", "Boxplot Light Velocity") {
            unsafeJso<EChartsOption> {
                title = michelsonTitles(normalWeight = true)
                dataset =
                    arrayOf(
                        unsafeJso<DatasetOption> { source = numberRows(michelson) },
                        unsafeJso<DatasetOption> { transform = arrayOf(boxplotTransform("expr {value}")) },
                        unsafeJso<DatasetOption> { fromDatasetIndex = 1.0; fromTransformResult = 1.0 },
                    )
                tooltip = boxplotTooltip()
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
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
                            splitArea = unsafeJso<SplitAreaOption> { show = false }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "km/s minus 299,000"
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        boxplotSeries { name = "boxplot"; datasetIndex = 1.0 },
                        scatterSeries { name = "outlier"; datasetIndex = 2.0 },
                    )
            }
        }
