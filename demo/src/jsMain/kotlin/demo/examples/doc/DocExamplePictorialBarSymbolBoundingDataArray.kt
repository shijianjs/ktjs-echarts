package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.fixed

private val docExamplePictorialBarSymbolBoundingDataArrayCategory = Category(Groups.DOC, "examples/doc/DocExamplePictorialBarSymbolBoundingDataArray.kt")

val docExamplePictorialBarSymbolBoundingDataArrayEntry: CatalogEntry =
    docExamplePictorialBarSymbolBoundingDataArrayCategory.chart(
            "doc-example/pictorialBar-symbolBoundingDataArray",
            "象形柱图：基准值数组",
            "Pictorial bar: symbolBoundingData array",
        ) {
            val categories = arrayOf("DTA", "Fin", "Tool", "VTG", "CAAT", "UYL", "TC")
            val values = doubleArrayOf(-12.0, -60.0, 34.0, 23.0, -21.0, 0.0, -25.0)
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble(120.0) })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            data = strings(*categories)
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#ccc") }
                                }
                            // The original nests `color` / `fontSize` under `axisLabel.textStyle`;
                            // ECharts 6 reads text styles from the label itself.
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    margin = 40.0
                                    color = "#999"
                                    fontSize = StringOrDouble(16.0)
                                }
                            splitLine = unsafeJso<SplitLineOption> { show = true }
                            position = AxisPositions.top
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        starSeries("bg", values) {
                            tooltip = unsafeJso<TooltipOption> { trigger = TooltipTriggers.none }
                            // The original uses `itemStyle.normal.color`.
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddd") }
                            silent = true
                            symbolRepeat = BooleanOrDoubleOrString("fixed")
                            symbolClip = false
                            symbolBoundingData = DoubleOrDoubleArray(40.0)
                            z = -1.0
                        },
                        starSeries("bg", values) {
                            tooltip = unsafeJso<TooltipOption> { trigger = TooltipTriggers.none }
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddd") }
                            silent = true
                            symbolRepeat = BooleanOrDoubleOrString("fixed")
                            symbolClip = false
                            symbolBoundingData = DoubleOrDoubleArray(-60.0)
                            z = -1.0
                        },
                        starSeries("data", values) {
                            symbolRepeat = BooleanOrDoubleOrString(true)
                            symbolClip = true
                            symbolBoundingData = DoubleOrDoubleArray(arrayOf(-60.0, 40.0))
                        },
                    )
            }
        }
