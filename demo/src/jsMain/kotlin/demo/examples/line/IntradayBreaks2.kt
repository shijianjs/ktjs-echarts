package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.Lcg
import demo.examples.shared.allAxisIndices
import demo.examples.shared.axisFormatterWithBreak
import demo.examples.shared.axisLabel
import demo.examples.shared.walkSingleDay

private val intradayBreaks2Category = Category(Groups.LINE, "examples/line/IntradayBreaks2.kt")

val intradayBreaks2Entry: CatalogEntry =
    intradayBreaks2Category.chart(
            "intraday-breaks-2",
            "断轴上的日内走势图 (II)",
            "Intraday Chart with Breaks (II)",
            "单日逐分钟走势，午休时段折成一段零宽度的断轴；`breakLabelLayout.moveOverlap: false` " +
                "关掉标签自动避让，轴标签自己把「开盘/收盘」写成一个跨断口的标签。",
            "One trading day of minute data with the lunch break folded to zero width; " +
                "`breakLabelLayout.moveOverlap: false` turns off the automatic label avoidance so the " +
                "axis label writes the break itself.",
        ) {
            val data = walkSingleDay(Lcg(20240409))
            unsafeJso<EChartsOption> {
                useUTC = true
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Intraday Chart with Breaks (Single Day)"
                            left = StringOrDouble("center")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            show = true
                            trigger = TooltipTriggers.axis
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.time
                            interval = AxisInterval(1000.0 * 60 * 30)
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    showMinLabel = true
                                    showMaxLabel = true
                                    formatter = LabelFormatter(axisLabel(::axisFormatterWithBreak))
                                }
                            breakLabelLayout =
                                unsafeJso<AxisBreakLabelLayoutOption> { moveOverlap = BooleanOrString(false) }
                            breaks = data.breaks
                            breakArea =
                                unsafeJso<AxisBreakAreaOption> {
                                    expandOnClick = false
                                    zigzagAmplitude = 0.0
                                    zigzagZ = 200.0
                                }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            min = AxisMinMax("dataMin")
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            xAxisIndex = allAxisIndices()
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            xAxisIndex = allAxisIndices()
                        },
                    )
                series =
                    one(
                        lineSeries {
                            symbolSize = SymbolSize(0.0)
                            typedData = data.points
                        },
                    )
            }
        }
