package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import js.objects.unsafeJso
import demo.examples.Category

private val lineFunctionCategory = Category(Groups.LINE, "examples/line/LineFunction.kt")

val lineFunctionEntry: CatalogEntry =
    lineFunctionCategory.chart("line-function", "函数绘图", "Function Plot") {
            unsafeJso<EChartsOption> {
                animation = false
                grid =
                    one(
                        unsafeJso<GridOption> {
                            top = StringOrDouble(40.0)
                            left = StringOrDouble(50.0)
                            right = StringOrDouble(40.0)
                            bottom = StringOrDouble(50.0)
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            name = "x"
                            minorTick = unsafeJso<AxisTickOption> { show = BooleanOrString(true) }
                            minorSplitLine = unsafeJso<SplitLineOption> { show = true }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            name = "y"
                            min = AxisMinMax(-100.0)
                            max = AxisMinMax(100.0)
                            minorTick = unsafeJso<AxisTickOption> { show = BooleanOrString(true) }
                            minorSplitLine = unsafeJso<SplitLineOption> { show = true }
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            show = true
                            type = DataZoomTypes.inside
                            filterMode = FilterModes.none
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0))
                            startValue = DataZoomValue(-20.0)
                            endValue = DataZoomValue(20.0)
                        },
                        unsafeJso<DataZoomOption> {
                            show = true
                            type = DataZoomTypes.inside
                            filterMode = FilterModes.none
                            yAxisIndex = IndexOrIndexArray(arrayOf(0.0))
                            startValue = DataZoomValue(-20.0)
                            endValue = DataZoomValue(20.0)
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            showSymbol = false
                            clip = true
                            typedData = functionPlot()
                        },
                    )
            }
        }
