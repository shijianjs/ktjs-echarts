package demo.examples.bar

import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.doc.mixTimelineAxisCategories
import demo.examples.doc.mixTimelineBar
import demo.examples.doc.mixTimelineBarData
import demo.examples.doc.mixTimelinePoints
import demo.examples.funnel.labels
import demo.examples.gauge.center
import demo.examples.matrix.names
import demo.examples.scatter.point
import demo.examples.shared.axisLabel
import demo.examples.shared.every
import demo.examples.shared.legendNames
import demo.examples.shared.mixTimelineEstate
import demo.examples.shared.mixTimelineFinancial
import demo.examples.shared.mixTimelineGdp
import demo.examples.shared.mixTimelinePrimaryIndustry
import demo.examples.shared.mixTimelineSecondaryIndustry
import demo.examples.shared.mixTimelineTertiaryIndustry
import demo.examples.shared.mixTimelineYears
import demo.examples.shared.pieItem
import demo.examples.shared.recordOf
import demo.examples.shared.valueText
import demo.examples.sunburst.radius

/** The option every timeline point starts from; only the titles and the series data change. */
internal fun financeBaseOption(): EChartsOption =
    unsafeJso {
        timeline =
            unsafeJso<TimelineOption> {
                axisType = "category"
                autoPlay = true
                playInterval = 1000.0
                data = mixTimelinePoints()
                label = unsafeJso<TimelineLabelOption> { formatter = LabelFormatter(financeTimelineYearLabel()) }
            }
        title = one(unsafeJso<TitleOption> { subtext = "数据来自国家统计局" })
        tooltip = one(unsafeJso<TooltipOption> {})
        legend =
            one(
                unsafeJso<LegendOption> {
                    left = StringOrDouble("right")
                    data = legendNames("第一产业", "第二产业", "第三产业", "GDP", "金融", "房地产")
                    selected =
                        recordOf(
                            "GDP" to false.unsafeCast<JsAny>(),
                            "金融" to false.unsafeCast<JsAny>(),
                            "房地产" to false.unsafeCast<JsAny>(),
                        ).unsafeCast<Record<String, Boolean>?>()
                },
            )
        grid =
            one(
                unsafeJso<GridOption> {
                    top = StringOrDouble(80.0)
                    bottom = StringOrDouble(100.0)
                    // The grid-level tooltip drops the line break of the province names.
                    tooltip =
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    type = AxisPointerTypes.shadow
                                    label =
                                        unsafeJso<AxisPointerLabelOption> {
                                            show = true
                                            formatter = LabelFormatter { params -> valueText(params.value).replace("\n", "") }
                                        }
                                }
                        }
                },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    axisLabel = unsafeJso<AxisLabelOption> { interval = AxisInterval(0.0) }
                    data = strings(*mixTimelineAxisCategories())
                    splitLine = unsafeJso<SplitLineOption> { show = false }
                },
            )
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    type = AxisTypes.value
                    name = "GDP（亿元）"
                },
            )
        series =
            arrayOf<SeriesOption>(
                mixTimelineBar("GDP"),
                mixTimelineBar("金融"),
                mixTimelineBar("房地产"),
                mixTimelineBar("第一产业"),
                mixTimelineBar("第二产业"),
                mixTimelineBar("第三产业"),
                pieSeries {
                    name = "GDP占比"
                    center = arrayOf(StringOrDouble("75%"), StringOrDouble("35%"))
                    radius = StringOrDoubleOrArray("28%")
                    z = 100.0
                },
            )
    }

/** One timeline point: the year in the title, the seven series' data, nothing else. */
internal fun financeYearOption(index: Int): EChartsOption =
    unsafeJso {
        title = one(unsafeJso<TitleOption> { text = "${mixTimelineYears[index]}全国宏观经济指标" })
        series =
            arrayOf<SeriesOption>(
                mixTimelineBarData(mixTimelineGdp[index]),
                mixTimelineBarData(mixTimelineFinancial[index]),
                mixTimelineBarData(mixTimelineEstate[index]),
                mixTimelineBarData(mixTimelinePrimaryIndustry[index]),
                mixTimelineBarData(mixTimelineSecondaryIndustry[index]),
                mixTimelineBarData(mixTimelineTertiaryIndustry[index]),
                pieSeries {
                    typedData =
                        arrayOf(
                            pieItem("第一产业", mixTimelinePrimaryIndustry[index].sum()),
                            pieItem("第二产业", mixTimelineSecondaryIndustry[index].sum()),
                            pieItem("第三产业", mixTimelineTertiaryIndustry[index].sum()),
                        )
                },
            )
    }

/** The timeline labels: `(value) => new Date(value).getFullYear()`, as in upstream. */
internal fun financeTimelineYearLabel(): LabelFormatterCallback =
    js("(value) => String(new Date(value).getFullYear())") as LabelFormatterCallback
