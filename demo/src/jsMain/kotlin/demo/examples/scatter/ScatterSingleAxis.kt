package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.numberAt

private val scatterSingleAxisCategory = Category(Groups.SCATTER, "examples/scatter/ScatterSingleAxis.kt")

val scatterSingleAxisEntry: CatalogEntry =
    scatterSingleAxisCategory.chart("scatter-single-axis", "单轴散点图", "Scatter on Single Axis") {
            val titles =
                Array(SINGLE_AXIS_DAYS.size) { index ->
                    unsafeJso<TitleOption> {
                        text = SINGLE_AXIS_DAYS[index]
                        top = StringOrDouble(((index + 0.5) * 100) / 7)
                    }
                }
            val axes =
                Array(SINGLE_AXIS_DAYS.size) { index ->
                    unsafeJso<SingleAxisOption> {
                        left = StringOrDouble(150.0)
                        type = AxisTypes.category
                        boundaryGap = BooleanOrStringArray(false)
                        data = strings(*SINGLE_AXIS_HOURS)
                        top = StringOrDouble((index * 100.0) / 7.0 + 5.0)
                        height = StringOrDouble(100.0 / 7.0 - 10.0)
                        axisLabel = unsafeJso<AxisLabelOption> { interval = AxisInterval(2.0) }
                    }
                }
            val series =
                Array(SINGLE_AXIS_DAYS.size) { index ->
                    scatterSeries {
                        singleAxisIndex = index.toDouble()
                        coordinateSystem = "singleAxis"
                        typedData = singleAxisPoints(index)
                        symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 1) * 4.0) }
                    }
                }
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { position = TooltipPosition("top") })
                title = titles
                singleAxis = axes
                this.series = Array<SeriesOption>(series.size) { series[it] }
            }
        }
