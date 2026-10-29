package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast

/*
 * `scatter-punchCard` from `public/examples/ts/scatter-punchCard.ts` of apache/echarts-examples.
 *
 * A GitHub punch card: the 24 hours of the day across, the seven weekdays down, and the bubble area
 * showing how many commits landed in that hour. Upstream first stores the table as
 * `[day, hour, count]` and then maps it to `[hour, day, count]`; the rows below are that mapped
 * table, so the axis order reads the same way as the original option.
 */

private val scatterPunchCard = Category(Groups.SCATTER, "examples/ScatterPunchCard.kt")

/** The 24 hour labels of the x axis, from midnight to 11 pm. */
private val PUNCH_HOURS =
    arrayOf(
        "12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a",
        "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p",
    )

/** The seven weekday labels of the y axis, Saturday first. */
private val PUNCH_DAYS =
    arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/** The 168 rows `[hour, day, commits]` of the table. */
private val PUNCH_ROWS: Array<ScatterDataItem?> =
    dataItems(
        ScatterDataItem(doubleRow(0.0, 0.0, 5.0)), ScatterDataItem(doubleRow(1.0, 0.0, 1.0)), ScatterDataItem(doubleRow(2.0, 0.0, 0.0)), ScatterDataItem(doubleRow(3.0, 0.0, 0.0)),
        ScatterDataItem(doubleRow(4.0, 0.0, 0.0)), ScatterDataItem(doubleRow(5.0, 0.0, 0.0)), ScatterDataItem(doubleRow(6.0, 0.0, 0.0)), ScatterDataItem(doubleRow(7.0, 0.0, 0.0)),
        ScatterDataItem(doubleRow(8.0, 0.0, 0.0)), ScatterDataItem(doubleRow(9.0, 0.0, 0.0)), ScatterDataItem(doubleRow(10.0, 0.0, 0.0)), ScatterDataItem(doubleRow(11.0, 0.0, 2.0)),
        ScatterDataItem(doubleRow(12.0, 0.0, 4.0)), ScatterDataItem(doubleRow(13.0, 0.0, 1.0)), ScatterDataItem(doubleRow(14.0, 0.0, 1.0)), ScatterDataItem(doubleRow(15.0, 0.0, 3.0)),
        ScatterDataItem(doubleRow(16.0, 0.0, 4.0)), ScatterDataItem(doubleRow(17.0, 0.0, 6.0)), ScatterDataItem(doubleRow(18.0, 0.0, 4.0)), ScatterDataItem(doubleRow(19.0, 0.0, 4.0)),
        ScatterDataItem(doubleRow(20.0, 0.0, 3.0)), ScatterDataItem(doubleRow(21.0, 0.0, 3.0)), ScatterDataItem(doubleRow(22.0, 0.0, 2.0)), ScatterDataItem(doubleRow(23.0, 0.0, 5.0)),
        ScatterDataItem(doubleRow(0.0, 1.0, 7.0)), ScatterDataItem(doubleRow(1.0, 1.0, 0.0)), ScatterDataItem(doubleRow(2.0, 1.0, 0.0)), ScatterDataItem(doubleRow(3.0, 1.0, 0.0)),
        ScatterDataItem(doubleRow(4.0, 1.0, 0.0)), ScatterDataItem(doubleRow(5.0, 1.0, 0.0)), ScatterDataItem(doubleRow(6.0, 1.0, 0.0)), ScatterDataItem(doubleRow(7.0, 1.0, 0.0)),
        ScatterDataItem(doubleRow(8.0, 1.0, 0.0)), ScatterDataItem(doubleRow(9.0, 1.0, 0.0)), ScatterDataItem(doubleRow(10.0, 1.0, 5.0)), ScatterDataItem(doubleRow(11.0, 1.0, 2.0)),
        ScatterDataItem(doubleRow(12.0, 1.0, 2.0)), ScatterDataItem(doubleRow(13.0, 1.0, 6.0)), ScatterDataItem(doubleRow(14.0, 1.0, 9.0)), ScatterDataItem(doubleRow(15.0, 1.0, 11.0)),
        ScatterDataItem(doubleRow(16.0, 1.0, 6.0)), ScatterDataItem(doubleRow(17.0, 1.0, 7.0)), ScatterDataItem(doubleRow(18.0, 1.0, 8.0)), ScatterDataItem(doubleRow(19.0, 1.0, 12.0)),
        ScatterDataItem(doubleRow(20.0, 1.0, 5.0)), ScatterDataItem(doubleRow(21.0, 1.0, 5.0)), ScatterDataItem(doubleRow(22.0, 1.0, 7.0)), ScatterDataItem(doubleRow(23.0, 1.0, 2.0)),
        ScatterDataItem(doubleRow(0.0, 2.0, 1.0)), ScatterDataItem(doubleRow(1.0, 2.0, 1.0)), ScatterDataItem(doubleRow(2.0, 2.0, 0.0)), ScatterDataItem(doubleRow(3.0, 2.0, 0.0)),
        ScatterDataItem(doubleRow(4.0, 2.0, 0.0)), ScatterDataItem(doubleRow(5.0, 2.0, 0.0)), ScatterDataItem(doubleRow(6.0, 2.0, 0.0)), ScatterDataItem(doubleRow(7.0, 2.0, 0.0)),
        ScatterDataItem(doubleRow(8.0, 2.0, 0.0)), ScatterDataItem(doubleRow(9.0, 2.0, 0.0)), ScatterDataItem(doubleRow(10.0, 2.0, 3.0)), ScatterDataItem(doubleRow(11.0, 2.0, 2.0)),
        ScatterDataItem(doubleRow(12.0, 2.0, 1.0)), ScatterDataItem(doubleRow(13.0, 2.0, 9.0)), ScatterDataItem(doubleRow(14.0, 2.0, 8.0)), ScatterDataItem(doubleRow(15.0, 2.0, 10.0)),
        ScatterDataItem(doubleRow(16.0, 2.0, 6.0)), ScatterDataItem(doubleRow(17.0, 2.0, 5.0)), ScatterDataItem(doubleRow(18.0, 2.0, 5.0)), ScatterDataItem(doubleRow(19.0, 2.0, 5.0)),
        ScatterDataItem(doubleRow(20.0, 2.0, 7.0)), ScatterDataItem(doubleRow(21.0, 2.0, 4.0)), ScatterDataItem(doubleRow(22.0, 2.0, 2.0)), ScatterDataItem(doubleRow(23.0, 2.0, 4.0)),
        ScatterDataItem(doubleRow(0.0, 3.0, 7.0)), ScatterDataItem(doubleRow(1.0, 3.0, 3.0)), ScatterDataItem(doubleRow(2.0, 3.0, 0.0)), ScatterDataItem(doubleRow(3.0, 3.0, 0.0)),
        ScatterDataItem(doubleRow(4.0, 3.0, 0.0)), ScatterDataItem(doubleRow(5.0, 3.0, 0.0)), ScatterDataItem(doubleRow(6.0, 3.0, 0.0)), ScatterDataItem(doubleRow(7.0, 3.0, 0.0)),
        ScatterDataItem(doubleRow(8.0, 3.0, 1.0)), ScatterDataItem(doubleRow(9.0, 3.0, 0.0)), ScatterDataItem(doubleRow(10.0, 3.0, 5.0)), ScatterDataItem(doubleRow(11.0, 3.0, 4.0)),
        ScatterDataItem(doubleRow(12.0, 3.0, 7.0)), ScatterDataItem(doubleRow(13.0, 3.0, 14.0)), ScatterDataItem(doubleRow(14.0, 3.0, 13.0)), ScatterDataItem(doubleRow(15.0, 3.0, 12.0)),
        ScatterDataItem(doubleRow(16.0, 3.0, 9.0)), ScatterDataItem(doubleRow(17.0, 3.0, 5.0)), ScatterDataItem(doubleRow(18.0, 3.0, 5.0)), ScatterDataItem(doubleRow(19.0, 3.0, 10.0)),
        ScatterDataItem(doubleRow(20.0, 3.0, 6.0)), ScatterDataItem(doubleRow(21.0, 3.0, 4.0)), ScatterDataItem(doubleRow(22.0, 3.0, 4.0)), ScatterDataItem(doubleRow(23.0, 3.0, 1.0)),
        ScatterDataItem(doubleRow(0.0, 4.0, 1.0)), ScatterDataItem(doubleRow(1.0, 4.0, 3.0)), ScatterDataItem(doubleRow(2.0, 4.0, 0.0)), ScatterDataItem(doubleRow(3.0, 4.0, 0.0)),
        ScatterDataItem(doubleRow(4.0, 4.0, 0.0)), ScatterDataItem(doubleRow(5.0, 4.0, 1.0)), ScatterDataItem(doubleRow(6.0, 4.0, 0.0)), ScatterDataItem(doubleRow(7.0, 4.0, 0.0)),
        ScatterDataItem(doubleRow(8.0, 4.0, 0.0)), ScatterDataItem(doubleRow(9.0, 4.0, 2.0)), ScatterDataItem(doubleRow(10.0, 4.0, 4.0)), ScatterDataItem(doubleRow(11.0, 4.0, 4.0)),
        ScatterDataItem(doubleRow(12.0, 4.0, 2.0)), ScatterDataItem(doubleRow(13.0, 4.0, 4.0)), ScatterDataItem(doubleRow(14.0, 4.0, 4.0)), ScatterDataItem(doubleRow(15.0, 4.0, 14.0)),
        ScatterDataItem(doubleRow(16.0, 4.0, 12.0)), ScatterDataItem(doubleRow(17.0, 4.0, 1.0)), ScatterDataItem(doubleRow(18.0, 4.0, 8.0)), ScatterDataItem(doubleRow(19.0, 4.0, 5.0)),
        ScatterDataItem(doubleRow(20.0, 4.0, 3.0)), ScatterDataItem(doubleRow(21.0, 4.0, 7.0)), ScatterDataItem(doubleRow(22.0, 4.0, 3.0)), ScatterDataItem(doubleRow(23.0, 4.0, 0.0)),
        ScatterDataItem(doubleRow(0.0, 5.0, 2.0)), ScatterDataItem(doubleRow(1.0, 5.0, 1.0)), ScatterDataItem(doubleRow(2.0, 5.0, 0.0)), ScatterDataItem(doubleRow(3.0, 5.0, 3.0)),
        ScatterDataItem(doubleRow(4.0, 5.0, 0.0)), ScatterDataItem(doubleRow(5.0, 5.0, 0.0)), ScatterDataItem(doubleRow(6.0, 5.0, 0.0)), ScatterDataItem(doubleRow(7.0, 5.0, 0.0)),
        ScatterDataItem(doubleRow(8.0, 5.0, 2.0)), ScatterDataItem(doubleRow(9.0, 5.0, 0.0)), ScatterDataItem(doubleRow(10.0, 5.0, 4.0)), ScatterDataItem(doubleRow(11.0, 5.0, 1.0)),
        ScatterDataItem(doubleRow(12.0, 5.0, 5.0)), ScatterDataItem(doubleRow(13.0, 5.0, 10.0)), ScatterDataItem(doubleRow(14.0, 5.0, 5.0)), ScatterDataItem(doubleRow(15.0, 5.0, 7.0)),
        ScatterDataItem(doubleRow(16.0, 5.0, 11.0)), ScatterDataItem(doubleRow(17.0, 5.0, 6.0)), ScatterDataItem(doubleRow(18.0, 5.0, 0.0)), ScatterDataItem(doubleRow(19.0, 5.0, 5.0)),
        ScatterDataItem(doubleRow(20.0, 5.0, 3.0)), ScatterDataItem(doubleRow(21.0, 5.0, 4.0)), ScatterDataItem(doubleRow(22.0, 5.0, 2.0)), ScatterDataItem(doubleRow(23.0, 5.0, 0.0)),
        ScatterDataItem(doubleRow(0.0, 6.0, 1.0)), ScatterDataItem(doubleRow(1.0, 6.0, 0.0)), ScatterDataItem(doubleRow(2.0, 6.0, 0.0)), ScatterDataItem(doubleRow(3.0, 6.0, 0.0)),
        ScatterDataItem(doubleRow(4.0, 6.0, 0.0)), ScatterDataItem(doubleRow(5.0, 6.0, 0.0)), ScatterDataItem(doubleRow(6.0, 6.0, 0.0)), ScatterDataItem(doubleRow(7.0, 6.0, 0.0)),
        ScatterDataItem(doubleRow(8.0, 6.0, 0.0)), ScatterDataItem(doubleRow(9.0, 6.0, 0.0)), ScatterDataItem(doubleRow(10.0, 6.0, 1.0)), ScatterDataItem(doubleRow(11.0, 6.0, 0.0)),
        ScatterDataItem(doubleRow(12.0, 6.0, 2.0)), ScatterDataItem(doubleRow(13.0, 6.0, 1.0)), ScatterDataItem(doubleRow(14.0, 6.0, 3.0)), ScatterDataItem(doubleRow(15.0, 6.0, 4.0)),
        ScatterDataItem(doubleRow(16.0, 6.0, 0.0)), ScatterDataItem(doubleRow(17.0, 6.0, 0.0)), ScatterDataItem(doubleRow(18.0, 6.0, 0.0)), ScatterDataItem(doubleRow(19.0, 6.0, 0.0)),
        ScatterDataItem(doubleRow(20.0, 6.0, 1.0)), ScatterDataItem(doubleRow(21.0, 6.0, 2.0)), ScatterDataItem(doubleRow(22.0, 6.0, 2.0)), ScatterDataItem(doubleRow(23.0, 6.0, 6.0)),
    )

/**
 * The tooltip body: `<count> commits in <hour> of <day>`.
 *
 * The callback receives the parameters object, and its `value` is the `[hour, day, count]` row, so
 * the two labels are looked up by the numeric column of the row.
 */
private fun punchCardTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val value = params.unsafeCast<CallbackDataParams>().value
            TooltipContent(
                textAt(value, 2) + " commits in " +
                    PUNCH_HOURS[numberAt(value, 0).toInt()] + " of " +
                    PUNCH_DAYS[numberAt(value, 1).toInt()],
            )
        },
    )

private fun punchCardOption(): EChartsOption =
    unsafeJso {
        title = one(unsafeJso<TitleOption> { text = "Punch Card of Github" })
        legend =
            one(
                unsafeJso<LegendOption> {
                    data = legendNames("Punch Card")
                    left = StringOrDouble("right")
                },
            )
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    position = TooltipPosition("top")
                    formatter = punchCardTooltip()
                },
            )
        grid =
            one(
                unsafeJso<GridOption> {
                    left = StringOrDouble(2.0)
                    bottom = StringOrDouble(10.0)
                    right = StringOrDouble(10.0)
                    containLabel = true
                },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    data = strings<OptionDataItem>(*PUNCH_HOURS)
                    boundaryGap = BooleanOrStringArray(false)
                    splitLine = unsafeJso<SplitLineOption> { show = true }
                    axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                },
            )
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    type = AxisTypes.category
                    data = strings<OptionDataItem>(*PUNCH_DAYS)
                    axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                },
            )
        series =
            arrayOf<SeriesOption>(
                scatterSeries {
                    name = "Punch Card"
                    symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) * 2.0) }
                    typedData = PUNCH_ROWS
                    animationDelay = AnimationDelay { index, _ -> index * 5.0 }
                },
            )
    }

val scatterPunchCardEntries: List<CatalogEntry> =
    listOf(
        scatterPunchCard.chart(
            "scatter-punchCard",
            "GitHub 打卡气泡图",
            "Punch Card of Github",
            "一周七天 × 一天的 24 小时铺成网格，气泡大小就是那个小时的提交数；" +
                "`symbolSize` 读的是数据行的第三列，提示框把行号翻译成星期与小时。",
            "A week of days against the 24 hours of the day, the bubble area showing the commits of " +
                "that hour; `symbolSize` reads the third column of the row and the tooltip turns the " +
                "row numbers back into a weekday and an hour.",
        ) {
            punchCardOption()
        },
    )
