package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val scatterWeightCategory = Category(Groups.SCATTER, "examples/scatter/ScatterWeight.kt")

val scatterWeightEntry: CatalogEntry =
    scatterWeightCategory.chart(
            "scatter-weight",
            "男性女性身高体重分布",
            "Distribution of Height and Weight",
            "260 位女性与 247 位男性的身高体重散点，每个系列叠着自己的取值范围虚线框、" +
                "最大值/最小值标记和平均值线，右上角工具箱可切换缩放与框选。",
            "The height and weight of 260 women and 247 men; each series carries its own dashed " +
                "data-range box, its extreme points and an average line, and the toolbox on the " +
                "right switches zooming and brushing.",
        ) {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Male and female height and weight distribution"
                            subtext = "Data from: Heinz 2003"
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("3%")
                            right = StringOrDouble("7%")
                            bottom = StringOrDouble("7%")
                            containLabel = true
                        },
                    )
                tooltip = one(weightTooltip())
                toolbox = weightToolbox()
                brush = one(unsafeJso<BrushOption> {})
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("Female", "Male")
                            left = StringOrDouble("center")
                            bottom = StringOrDouble(10.0)
                        },
                    )
                xAxis = one(weightXAxis())
                yAxis = one(weightYAxis())
                series =
                    arrayOf(
                        weightSeries("Female", femaleHeightWeight, "AVG", 160.0),
                        weightSeries("Male", maleHeightWeight, "Average", 170.0),
                    )
            }
        }
