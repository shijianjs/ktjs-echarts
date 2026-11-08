package demo.examples.boxplot

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.BooleanOrStringArray
import io.github.shijianjs.ktjs.echarts.options.DatasetOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitAreaOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.boxplotSeries
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.scatterSeries
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
