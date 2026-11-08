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
import demo.examples.shared.ZOOM_MIN_SPAN
import demo.examples.shared.axisFormatterWithDay
import demo.examples.shared.axisLabel
import demo.examples.shared.invisibleBreakArea
import demo.examples.shared.walkMultiDay
import demo.examples.shared.weakRichText
import demo.examples.themeriver.days

private val intradayBreaks1Category = Category(Groups.LINE, "examples/line/IntradayBreaks1.kt")

val intradayBreaks1Entry: CatalogEntry =
    intradayBreaks1Category.chart(
            "intraday-breaks-1",
            "断轴上的日内走势图",
            "Intraday Chart with Breaks",
            "四个交易日、逐分钟的价格折线，每晚收盘到次日开盘之间是一段被折叠的断轴，" +
                "断口上标着日期；价格在夜间断开，线不会连成一条。",
            "Four trading days of minute data with a folded break between the close and the next " +
                "open; the break carries the date and the line does not connect across the night.",
        ) {
            val data = walkMultiDay(Lcg(20240409))
            unsafeJso<EChartsOption> {
                useUTC = true
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Intraday Chart with Breaks (Multiple Days)"
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
                grid =
                    one(
                        unsafeJso<GridOption> {
                            outerBounds =
                                unsafeJso<BoxLayoutOption> {
                                    top = StringOrDouble("20%")
                                    bottom = StringOrDouble("30%")
                                }
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
                                    formatter = LabelFormatter(axisLabel(::axisFormatterWithDay))
                                    rich = weakRichText()
                                }
                            breaks = data.breaks
                            breakArea = invisibleBreakArea()
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
                            minValueSpan = ZOOM_MIN_SPAN
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            top = StringOrDouble("73%")
                            minValueSpan = ZOOM_MIN_SPAN
                        },
                    )
                series =
                    one(
                        lineSeries {
                            symbolSize = SymbolSize(0.0)
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData = data.points
                        },
                    )
            }
        }
