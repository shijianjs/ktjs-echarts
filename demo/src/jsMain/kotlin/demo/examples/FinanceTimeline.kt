package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast

/*
 * `mix-timeline-finance` from `public/examples/ts/mix-timeline-finance.js` of apache/echarts-examples.
 *
 * The same ten-year provincial table as `doc-example/mix-timeline-all` (see `MixTimelineData.kt`)
 * drives six bars and a pie of the three industries. The two pages differ in the parts the option
 * builds, not in the numbers: here the legend hides the three GDP series by default, the grid has
 * its own tooltip whose label drops the forced line break of the province name, and the axis needs
 * no fixed maximum.
 *
 * The upstream `calculable: true` is the pre-DataZoom switch for value-axis zoom and no longer
 * exists in the ECharts 6 typings, so it is not carried over.
 */

private val financeTimeline = Category(Groups.BAR, "examples/FinanceTimeline.kt")

val financeTimelineEntries: List<CatalogEntry> =
    listOf(
        financeTimeline.chart(
            "mix-timeline-finance",
            "2002全国宏观经济指标",
            "Finance Indices 2002",
            summaryZh = "时间轴逐年播放十个年份，六根省级柱与一张三产业占比饼图同步换数据；图例默认隐藏 GDP、金融、房地产三项。",
            summaryEn = "A timeline plays ten years of provincial bars plus a pie of the three industries; the legend starts with GDP, finance and real estate switched off.",
        ) {
            unsafeJso {
                baseOption = financeBaseOption()
                options = Array(mixTimelineYears.size) { index -> financeYearOption(index) }
            }
        },
    )

/** The option every timeline point starts from; only the titles and the series data change. */
private fun financeBaseOption(): EChartsOption =
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
private fun financeYearOption(index: Int): EChartsOption =
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
private fun financeTimelineYearLabel(): LabelFormatterCallback =
    js("(value) => String(new Date(value).getFullYear())") as LabelFormatterCallback
