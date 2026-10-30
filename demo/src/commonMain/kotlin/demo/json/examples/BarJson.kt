package demo.json.examples

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*

/*
 * Bar examples carried over from `bar-*.ts` of apache/echarts-examples.
 *
 * The pages keep the official ids, so the Kotlin/JS port and the JSON port of the same chart share
 * one route. Skipped ids, and why:
 *
 * - `bar-breaks-simple`: the y axis is built from `breaks` / `breakArea`, which `AxisOption` does
 *   not model yet; without them the page is an ordinary four-series bar chart.
 * - `polar-endAngle`: the option is nothing but two angle axes with `startAngle` / `endAngle`,
 *   likewise missing from `AxisOption`.
 * - `bar-animation-delay`: the staggered `animationDelay` is a per-index callback; without it the
 *   page is an ordinary animated bar chart, which the JSON model cannot distinguish.
 *
 * `bar-polar-label-radial`, `bar-polar-label-tangential` and `polar-roundCap` drop only the
 * `startAngle` of their angle axis for the same reason; the label position and the round cap they
 * demonstrate stay intact.
 *
 * Callback-valued options become their static equivalents: the tooltips of `bar-waterfall`,
 * `bar-waterfall2` and `bar-polar-real-estate` keep the default content (a template cannot read
 * "the second series on the axis" or index into a data table), the `valueFormatter`s of
 * `mix-line-bar` are dropped, and `bar-stack-normalization` pre-scales and rounds its shares
 * because its label callback cannot be written as a template.
 */

private val bar = JsonCategory("json/examples/BarJson.kt")

/** Days of the week, the category axis of most pages. */
private val WEEK = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

/** Months of `multiple-y-axis` and `bar1`. */
private val MONTHS =
    arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

/** `focus: 'series'` of the hovered series in the stacked and animated pages. */
private fun focusSeries(): EmphasisOption = EmphasisOption().apply { focus = "series" }

/** An axis pointer of [pointerType]; the model has no builder for it. */
private fun pointerOf(pointerType: String): AxisPointerOption =
    AxisPointerOption().apply { type = pointerType }

/** The axis tooltip with a shadow pointer shared by most bar pages. */
private fun EChartsOption.shadowAxisTooltip() {
    tooltip {
        trigger = TooltipTrigger.Axis
        axisPointer = pointerOf("shadow")
    }
}

/** The `dataView` / `magicType` / `restore` / `saveAsImage` toolbox of several pages. */
private fun EChartsOption.showToolbox(magicTypes: List<SeriesType>? = null) {
    toolbox {
        feature =
            ToolboxFeatureOption().apply {
                dataView = ToolboxDataViewFeature().apply { readOnly = false }
                restore = ToolboxFeature()
                saveAsImage = ToolboxSaveAsImageFeature()
                if (magicTypes != null) magicType = ToolboxMagicTypeFeature().apply { type = magicTypes }
            }
    }
}

/** An item style with only a fill color. */
private fun fillStyle(fillColor: String): ItemStyleOption =
    ItemStyleOption().apply { color = fillColor.asColor() }

/** A line style with only a stroke color. */
private fun lineColor(strokeColor: String): LineStyleOption =
    LineStyleOption().apply { color = strokeColor.asColor() }

/** The transparent style that hides the placeholder series of the waterfall pages. */
private fun invisibleStyle(): ItemStyleOption =
    ItemStyleOption().apply {
        borderColor = "transparent".asColor()
        color = "transparent".asColor()
    }

/** One bar series of the stacked pages: a `total` stack, visible labels and series focus. */
private fun SeriesBuilder.stackedBar(seriesName: String, vararg row: Number) {
    bar {
        this.name = seriesName
        stack = "total"
        label { show = true }
        emphasis = focusSeries()
        values(*row)
    }
}

/** One series of the two polar stack pages, which share the stack group `a`. */
private fun SeriesBuilder.polarStacked(seriesName: String, vararg row: Number) {
    bar {
        this.name = seriesName
        stack = "a"
        coordinateSystem = "polar"
        emphasis = focusSeries()
        values(*row)
    }
}

/** One `markPoint` entry of a built-in statistic. */
private fun markPointStat(markName: String, statType: String): MarkPointData =
    MarkPointData().apply {
        this.name = markName
        this.type = statType
    }

/** One `markPoint` entry pinned to an explicit category index and value. */
private fun markPointAt(
    markName: String,
    pointValue: Double,
    categoryIndex: Int,
    axisValue: Double,
): MarkPointData =
    MarkPointData().apply {
        this.name = markName
        this.value = valueOf(pointValue)
        this.xAxis = num(categoryIndex)
        this.yAxis = num(axisValue)
    }

/** The `{type: 'average', name: 'Avg'}` mark line of `bar1`. */
private fun averageMarkLine(): MarkLineDataItem =
    MarkLineDataItem.of(
        MarkLineData().apply {
            name = "Avg"
            type = "average"
        },
    )

/** A waterfall row; `null` is the `"-"` hole ECharts reads as an empty slot of the stack. */
private fun waterfallRow(vararg values: Double?): List<DataValue> =
    values.map { value -> if (value == null) textValue("-") else valueOf(value) }

/** The channels stacked by `bar-stack-normalization`, in legend order. */
private val AD_CHANNELS =
    arrayOf("Direct", "Mail Ad", "Affiliate Ad", "Video Ad", "Search Engine")

/** `[channel][day]` counts of `bar-stack-normalization`; the table holds no negative value. */
private val AD_COUNTS =
    arrayOf(
        doubleArrayOf(100.0, 302.0, 301.0, 334.0, 390.0, 330.0, 320.0),
        doubleArrayOf(320.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0),
        doubleArrayOf(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0),
        doubleArrayOf(150.0, 212.0, 201.0, 154.0, 190.0, 330.0, 410.0),
        doubleArrayOf(820.0, 832.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0),
    )

/** The total of every day, the denominator of the normalization. */
private val AD_TOTALS = DoubleArray(WEEK.size) { day -> AD_COUNTS.sumOf { it[day] } }

/**
 * One series of `bar-stack-normalization`.
 *
 * Upstream divides by the daily total and rounds the *label* to `Math.round(value * 1000) / 10`
 * percent. A template cannot round, so the shares are pre-scaled to percent and rounded here, and
 * the label is `{c}%`; the bars keep the heights of the original.
 */
private fun SeriesBuilder.normalizedChannel(index: Int) {
    bar {
        name = AD_CHANNELS[index]
        stack = "total"
        barWidth = str("60%")
        label {
            show = true
            formatter = "{c}%"
        }
        values(
            *Array(WEEK.size) { day ->
                val total = AD_TOTALS[day]
                if (total <= 0.0) 0.0 else kotlin.math.round(AD_COUNTS[index][day] / total * 1000) / 10
            },
        )
    }
}

/** The cities of `bar-polar-real-estate`; they are also the categories of its angle axis. */
private val REAL_ESTATE_CITIES =
    arrayOf(
        "北京", "上海", "深圳", "广州", "苏州", "杭州", "南京", "福州", "青岛", "济南",
        "长春", "大连", "温州", "郑州", "武汉", "成都", "东莞", "沈阳", "烟台",
    )

/** `[lowest, highest, average]` monthly rent per city of `bar-polar-real-estate`. */
private val REAL_ESTATE_ROWS =
    arrayOf(
        doubleArrayOf(5000.0, 10000.0, 6785.71),
        doubleArrayOf(4000.0, 10000.0, 6825.0),
        doubleArrayOf(3000.0, 6500.0, 4463.33),
        doubleArrayOf(2500.0, 5600.0, 3793.83),
        doubleArrayOf(2000.0, 4000.0, 3060.0),
        doubleArrayOf(2000.0, 4000.0, 3222.33),
        doubleArrayOf(2500.0, 4000.0, 3133.33),
        doubleArrayOf(1800.0, 4000.0, 3100.0),
        doubleArrayOf(2000.0, 3500.0, 2750.0),
        doubleArrayOf(2000.0, 3000.0, 2500.0),
        doubleArrayOf(1800.0, 3000.0, 2433.33),
        doubleArrayOf(2000.0, 2700.0, 2375.0),
        doubleArrayOf(1500.0, 2800.0, 2150.0),
        doubleArrayOf(1500.0, 2300.0, 2100.0),
        doubleArrayOf(1600.0, 3500.0, 2057.14),
        doubleArrayOf(1500.0, 2600.0, 2037.5),
        doubleArrayOf(1500.0, 2417.54, 1905.85),
        doubleArrayOf(1500.0, 2000.0, 1775.0),
        doubleArrayOf(1500.0, 1800.0, 1650.0),
    )

/** Thickness of the average band of `bar-polar-real-estate`, in data units. */
private const val REAL_ESTATE_BAR_HEIGHT = 50.0

/** One stacked bar of `bar-polar-real-estate`; the transparent bases lift the visible bands. */
private fun SeriesBuilder.polarBar(
    seriesName: String?,
    row: List<Double>,
    transparent: Boolean,
    group: String,
    gap: String? = null,
    layer: Double? = null,
) {
    bar {
        this.name = seriesName
        stack = group
        coordinateSystem = "polar"
        z = layer
        data = row.map { value -> valueOf(value) }
        if (transparent) {
            silent = true
            itemStyle = fillStyle("transparent")
        }
        if (gap != null) barGap = gap
    }
}

/** Raw values of `bar-stack-borderRadius`, where `null` is the hole the original leaves. */
private val STACK_RADIUS_ROWS: Array<Array<Double?>> =
    arrayOf(
        arrayOf(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0),
        arrayOf(10.0, 46.0, 64.0, null, 0.0, null, 0.0),
        arrayOf(30.0, null, 0.0, 20.0, 10.0, null, 0.0),
        arrayOf(30.0, null, 0.0, 20.0, 10.0, null, 0.0),
        arrayOf(10.0, 20.0, 150.0, 0.0, null, 50.0, 10.0),
    )

/** The stack group every series of `bar-stack-borderRadius` belongs to. */
private val STACK_RADIUS_GROUPS = arrayOf("a", "a", "a", "b", "b")

/** The names of the series of `bar-stack-borderRadius`. */
private val STACK_RADIUS_NAMES = arrayOf("a", "b", "c", "d", "e")

/**
 * For every stack of [STACK_RADIUS_GROUPS], the index of the last series that fills a day.
 *
 * Only non-zero numbers count, which reproduces the `data && data !== '-'` test of the original:
 * `0` is as empty as the `null` that stands for `"-"`.
 */
private val STACK_RADIUS_ENDS: Map<String, IntArray> =
    run {
        val ends = LinkedHashMap<String, IntArray>()
        for (day in WEEK.indices) {
            for (index in STACK_RADIUS_NAMES.indices) {
                val end = ends.getOrPut(STACK_RADIUS_GROUPS[index]) { IntArray(WEEK.size) { -1 } }
                val value = STACK_RADIUS_ROWS[index][day]
                if (value != null && value != 0.0) end[day] = index
            }
        }
        ends
    }

/** One series of `bar-stack-borderRadius`, with the corner radius of every single bar. */
private fun SeriesBuilder.roundedStack(index: Int) {
    val ends = STACK_RADIUS_ENDS.getValue(STACK_RADIUS_GROUPS[index])
    bar {
        name = STACK_RADIUS_NAMES[index]
        stack = STACK_RADIUS_GROUPS[index]
        data =
            STACK_RADIUS_ROWS[index].mapIndexed { day, raw ->
                val radius = if (ends[day] == index) 20.0 else 0.0
                dataItem {
                    value = if (raw == null) textValue("-") else valueOf(raw)
                    itemStyle {
                        borderRadius =
                            NumberOrStringOrNumbers.of(num(radius), num(radius), num(0), num(0))
                    }
                }
            }
    }
}

/** The labels of `bar-label-rotation`: 16px, rotated a quarter turn inside the bar. */
private fun rotatedLabel(): LabelOption =
    LabelOption().apply {
        show = true
        position = "insideBottom"
        distance = 15.0
        align = "left"
        verticalAlign = "middle"
        rotate = 90.0
        formatter = "{c}  {name|{a}}"
        fontSize = 16.0
        rich = mapOf("name" to TextStyleOption())
    }

/** Fixed random source of `bar-large`, standing in for the `Math.random()` generator upstream. */
private class LargeBarRandom {
    private var state = 20110101L

    /** Next value in `0..1`. */
    fun next(): Double {
        state = state * 1103515245L + 12345L
        return ((state ushr 16) and 0x7FFF).toDouble() / 0x8000
    }
}

/** The values of `bar-large`, following the upstream generator with a fixed random source. */
private fun largeBarValues(count: Int): List<Double> {
    val random = LargeBarRandom()
    var baseValue = random.next() * 1000
    var smallBaseValue = 0.0
    return (0 until count).map { index ->
        smallBaseValue =
            if (index % 30 == 0) random.next() * 700 else smallBaseValue + random.next() * 500 - 250
        baseValue += random.next() * 20 - 10
        kotlin.math.max(0.0, kotlin.math.round(baseValue + smallBaseValue) + 3000)
    }
}

/** The category labels of `bar-large`: one second per bar, starting at 2011-01-01 00:00:00. */
private fun largeBarCategories(count: Int): List<DataValue> =
    (0 until count).map { index ->
        val time =
            twoDigits(index / 3600) + ":" + twoDigits(index / 60 % 60) + ":" + twoDigits(index % 60)
        textValue("2011-01-01\n$time")
    }

/** Two-digit padding of the time parts of `bar-large`. */
private fun twoDigits(value: Int): String = value.toString().padStart(2, '0')

public val barJsonExamples: List<JsonExample> =
    listOf(
        // ---------------------------------------------------------------- basic bars
        bar.example("bar-simple") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                bar { values(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0) }
            }
        },
        bar.example("bar-tick-align") {
            shadowAxisTooltip()
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
                axisTick { alignWithLabel = true }
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Direct"
                    barWidth = str("60%")
                    values(10.0, 52.0, 200.0, 334.0, 390.0, 330.0, 220.0)
                }
            }
        },
        bar.example("bar-background") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    showBackground = true
                    backgroundStyle = fillStyle("rgba(180, 180, 180, 0.2)")
                    values(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0)
                }
            }
        },
        bar.example("bar-data-color") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    data =
                        listOf(
                            valueOf(120),
                            dataItem {
                                value = valueOf(200)
                                itemStyle { color = "#505372".asColor() }
                            },
                            valueOf(150),
                            valueOf(80),
                            valueOf(70),
                            valueOf(110),
                            valueOf(130),
                        )
                }
            }
        },
        bar.example("bar-negative") {
            shadowAxisTooltip()
            legend { entries("Profit", "Expenses", "Income") }
            xAxis { type = AxisType.Value }
            yAxis {
                type = AxisType.Category
                axisTick { show = false }
                categories(*WEEK)
            }
            series {
                bar {
                    name = "Profit"
                    label {
                        show = true
                        position = "inside"
                    }
                    emphasis = focusSeries()
                    values(200.0, 170.0, 240.0, 244.0, 200.0, 220.0, 210.0)
                }
                bar {
                    name = "Income"
                    stack = "Total"
                    label { show = true }
                    emphasis = focusSeries()
                    values(320.0, 302.0, 341.0, 374.0, 390.0, 450.0, 420.0)
                }
                bar {
                    name = "Expenses"
                    stack = "Total"
                    label {
                        show = true
                        position = "left"
                    }
                    emphasis = focusSeries()
                    values(-120.0, -132.0, -101.0, -134.0, -190.0, -230.0, -210.0)
                }
            }
        },
        bar.example("bar-negative2") {
            title { text = "Bar Chart with Negative Value" }
            shadowAxisTooltip()
            grid {
                top = num(80)
                bottom = num(30)
            }
            xAxis {
                type = AxisType.Value
                position = "top"
                splitLine { lineStyle = LineStyleOption().apply { type = "dashed" } }
            }
            yAxis {
                type = AxisType.Category
                axisLine { show = false }
                axisLabel { show = false }
                axisTick { show = false }
                splitLine { show = false }
                categories("ten", "nine", "eight", "seven", "six", "five", "four", "three", "two", "one")
            }
            val costs = doubleArrayOf(-0.07, -0.09, 0.2, 0.44, -0.23, 0.08, -0.17, 0.47, -0.36, 0.18)
            series {
                bar {
                    name = "Cost"
                    stack = "Total"
                    label {
                        show = true
                        formatter = "{b}"
                    }
                    data =
                        costs.map { cost ->
                            dataItem {
                                value = valueOf(cost)
                                if (cost < 0) label { position = "right" }
                            }
                        }
                }
            }
        },
        bar.example("bar-y-category") {
            title { text = "World Population" }
            shadowAxisTooltip()
            legend { }
            xAxis {
                type = AxisType.Value
                boundaryGap = BoundaryGap.of(num(0), num(0.01))
            }
            yAxis {
                type = AxisType.Category
                categories("Brazil", "Indonesia", "USA", "India", "China", "World")
            }
            series {
                bar {
                    name = "2011"
                    values(18203.0, 23489.0, 29034.0, 104970.0, 131744.0, 630230.0)
                }
                bar {
                    name = "2012"
                    values(19325.0, 23438.0, 31000.0, 121594.0, 134141.0, 681807.0)
                }
            }
        },
        bar.example("bar-y-category-stack") {
            shadowAxisTooltip()
            legend { }
            xAxis { type = AxisType.Value }
            yAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            series {
                stackedBar("Direct", 320.0, 302.0, 301.0, 334.0, 390.0, 330.0, 320.0)
                stackedBar("Mail Ad", 120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0)
                stackedBar("Affiliate Ad", 220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0)
                stackedBar("Video Ad", 150.0, 212.0, 201.0, 154.0, 190.0, 330.0, 410.0)
                stackedBar("Search Engine", 820.0, 832.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0)
            }
        },
        bar.example("bar-stack") {
            shadowAxisTooltip()
            legend { }
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            val searchEngine = doubleArrayOf(862.0, 1018.0, 964.0, 1026.0, 1679.0, 1600.0, 1570.0)
            series {
                bar {
                    name = "Direct"
                    emphasis = focusSeries()
                    values(320.0, 332.0, 301.0, 334.0, 390.0, 330.0, 320.0)
                }
                bar {
                    name = "Email"
                    stack = "Ad"
                    emphasis = focusSeries()
                    values(120.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0)
                }
                bar {
                    name = "Union Ads"
                    stack = "Ad"
                    emphasis = focusSeries()
                    values(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0)
                }
                bar {
                    name = "Video Ads"
                    stack = "Ad"
                    emphasis = focusSeries()
                    values(150.0, 232.0, 201.0, 154.0, 190.0, 330.0, 410.0)
                }
                bar {
                    name = "Search Engine"
                    emphasis = focusSeries()
                    values(*searchEngine.toTypedArray())
                    markLine {
                        lineStyle = LineStyleOption().apply { type = "dashed" }
                        data =
                            listOf(
                                MarkLineDataItem.of(
                                    MarkLineData().apply { type = "min" },
                                    MarkLineData().apply { type = "max" },
                                ),
                            )
                    }
                }
                bar {
                    name = "Baidu"
                    barWidth = num(5)
                    stack = "Search Engine"
                    emphasis = focusSeries()
                    values(620.0, 732.0, 701.0, 734.0, 1090.0, 1130.0, 1120.0)
                }
                bar {
                    name = "Google"
                    stack = "Search Engine"
                    emphasis = focusSeries()
                    values(120.0, 132.0, 101.0, 134.0, 290.0, 230.0, 220.0)
                }
                bar {
                    name = "Bing"
                    stack = "Search Engine"
                    emphasis = focusSeries()
                    values(60.0, 72.0, 71.0, 74.0, 190.0, 130.0, 110.0)
                }
                bar {
                    name = "Others"
                    stack = "Search Engine"
                    emphasis = focusSeries()
                    values(62.0, 82.0, 91.0, 84.0, 109.0, 110.0, 120.0)
                }
            }
        },
        // ------------------------------------------------------------ waterfalls
        bar.example("bar-waterfall") {
            title {
                text = "Waterfall Chart"
                subtext = "Living Expenses in Shenzhen"
            }
            shadowAxisTooltip()
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Category
                splitLine { show = false }
                categories("Total", "Rent", "Utilities", "Transportation", "Meals", "Other")
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Placeholder"
                    stack = "Total"
                    itemStyle = invisibleStyle()
                    emphasis { itemStyle = invisibleStyle() }
                    values(0.0, 1700.0, 1400.0, 1200.0, 300.0, 0.0)
                }
                bar {
                    name = "Life Cost"
                    stack = "Total"
                    label {
                        show = true
                        position = "inside"
                    }
                    values(2900.0, 1200.0, 300.0, 200.0, 900.0, 300.0)
                }
            }
        },
        bar.example("bar-waterfall2") {
            title { text = "Accumulated Waterfall Chart" }
            shadowAxisTooltip()
            legend { entries("Expenses", "Income") }
            grid {
                left = str("3%")
                right = str("4%")
                bottom = str("3%")
                containLabel = true
            }
            xAxis {
                type = AxisType.Category
                categories(*(1..11).map { "Nov $it" }.toTypedArray())
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Placeholder"
                    stack = "Total"
                    silent = true
                    itemStyle = invisibleStyle()
                    emphasis { itemStyle = invisibleStyle() }
                    values(0.0, 900.0, 1245.0, 1530.0, 1376.0, 1376.0, 1511.0, 1689.0, 1856.0, 1495.0, 1292.0)
                }
                bar {
                    name = "Income"
                    stack = "Total"
                    label {
                        show = true
                        position = "top"
                    }
                    data = waterfallRow(900.0, 345.0, 393.0, null, null, 135.0, 178.0, 286.0, null, null, null)
                }
                bar {
                    name = "Expenses"
                    stack = "Total"
                    label {
                        show = true
                        position = "bottom"
                    }
                    data = waterfallRow(null, null, null, 108.0, 154.0, null, null, null, 119.0, 361.0, 203.0)
                }
            }
        },
        // ---------------------------------------------------------- multiple axes
        bar.example("multiple-y-axis") {
            val axisColors = arrayOf("#5070dd", "#b6d634", "#505372")
            color = colors(*axisColors)
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            grid { right = str("20%") }
            showToolbox()
            legend { entries("Evaporation", "Precipitation", "Temperature") }
            xAxis {
                type = AxisType.Category
                axisTick { alignWithLabel = true }
                categories(*MONTHS)
            }
            // Upstream also sets `alignTicks: true`, which `AxisOption` does not model.
            yAxis {
                type = AxisType.Value
                name = "Evaporation"
                position = "right"
                axisLine {
                    show = true
                    lineStyle = lineColor(axisColors[0])
                }
                axisLabel { formatter = "{value} ml" }
            }
            yAxis {
                type = AxisType.Value
                name = "Precipitation"
                position = "right"
                offset = 80.0
                axisLine {
                    show = true
                    lineStyle = lineColor(axisColors[1])
                }
                axisLabel { formatter = "{value} ml" }
            }
            yAxis {
                type = AxisType.Value
                name = "温度"
                position = "left"
                axisLine {
                    show = true
                    lineStyle = lineColor(axisColors[2])
                }
                axisLabel { formatter = "{value} °C" }
            }
            series {
                bar {
                    name = "Evaporation"
                    values(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3)
                }
                bar {
                    name = "Precipitation"
                    yAxisIndex = 1
                    values(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                }
                line {
                    name = "Temperature"
                    yAxisIndex = 2
                    values(2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2)
                }
            }
        },
        bar.example("mix-line-bar") {
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = pointerOf("cross")
            }
            showToolbox(magicTypes = listOf(SeriesType.Line, SeriesType.Bar))
            legend { entries("Evaporation", "Precipitation", "Temperature") }
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
                axisPointer = pointerOf("shadow")
            }
            yAxis {
                type = AxisType.Value
                name = "Precipitation"
                min = num(0)
                max = num(250)
                interval = 50.0
                axisLabel { formatter = "{value} ml" }
            }
            yAxis {
                type = AxisType.Value
                name = "Temperature"
                min = num(0)
                max = num(25)
                interval = 5.0
                axisLabel { formatter = "{value} °C" }
            }
            series {
                bar {
                    name = "Evaporation"
                    values(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3)
                }
                bar {
                    name = "Precipitation"
                    values(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                }
                line {
                    name = "Temperature"
                    yAxisIndex = 1
                    values(2.0, 2.2, 3.3, 4.5, 6.3, 10.2, 20.3, 23.4, 23.0, 16.5, 12.0, 6.2)
                }
            }
        },
        // ------------------------------------------------------------- long tail
        bar.example("bar-label-rotation") {
            shadowAxisTooltip()
            legend { entries("Forest", "Steppe", "Desert", "Wetland") }
            xAxis {
                type = AxisType.Category
                axisTick { show = false }
                categories("2012", "2013", "2014", "2015", "2016")
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Forest"
                    barGap = "0"
                    label = rotatedLabel()
                    emphasis = focusSeries()
                    values(320.0, 332.0, 301.0, 334.0, 390.0)
                }
                bar {
                    name = "Steppe"
                    label = rotatedLabel()
                    emphasis = focusSeries()
                    values(220.0, 182.0, 191.0, 234.0, 290.0)
                }
                bar {
                    name = "Desert"
                    label = rotatedLabel()
                    emphasis = focusSeries()
                    values(150.0, 232.0, 201.0, 154.0, 190.0)
                }
                bar {
                    name = "Wetland"
                    label = rotatedLabel()
                    emphasis = focusSeries()
                    values(98.0, 77.0, 101.0, 99.0, 40.0)
                }
            }
            color = colors("#5470c6", "#91cc75", "#fac858", "#ee6666")
        },
        bar.example("bar-large") {
            val count = 300
            val seriesValues = largeBarValues(count)
            title {
                text = "$count Data"
                left = num(10)
            }
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        saveAsImage = ToolboxSaveAsImageFeature().apply { pixelRatio = 2.0 }
                    }
            }
            shadowAxisTooltip()
            grid { bottom = num(90) }
            dataZoom { type = DataZoomType.Inside }
            dataZoom { type = DataZoomType.Slider }
            xAxis {
                silent = false
                data = largeBarCategories(count)
                splitLine { show = false }
                splitArea { show = false }
            }
            yAxis { splitArea { show = false } }
            series {
                bar {
                    data = seriesValues.map { value -> valueOf(value) }
                    large = true
                }
            }
        },
        // --------------------------------------------------------------- polar
        bar.example("bar-polar-label-radial") {
            title { text = "Radial Polar Bar Label Position (middle)" }
            polar { radius = NumberOrStringOrNumbers.of(num(30), str("80%")) }
            radiusAxis { max = num(4) }
            angleAxis {
                type = AxisType.Category
                categories("a", "b", "c", "d")
            }
            tooltip { }
            series {
                bar {
                    coordinateSystem = "polar"
                    label {
                        show = true
                        position = "middle"
                        formatter = "{b}: {c}"
                    }
                    values(2.0, 1.2, 2.4, 3.6)
                }
            }
            animation = false
        },
        bar.example("bar-polar-label-tangential") {
            title { text = "Tangential Polar Bar Label Position (middle)" }
            polar { radius = NumberOrStringOrNumbers.of(num(30), str("80%")) }
            angleAxis { max = num(4) }
            radiusAxis {
                type = AxisType.Category
                categories("a", "b", "c", "d")
            }
            tooltip { }
            series {
                bar {
                    coordinateSystem = "polar"
                    label {
                        show = true
                        position = "middle"
                        formatter = "{b}: {c}"
                    }
                    values(2.0, 1.2, 2.4, 3.6)
                }
            }
        },
        bar.example("bar-polar-real-estate") {
            title {
                text = "How expensive is it to rent an apartment in China?"
                subtext = "Data from https://www.numbeo.com"
            }
            legend {
                show = true
                top = str("bottom")
                entries("Range", "Average")
            }
            grid { top = num(100) }
            angleAxis {
                type = AxisType.Category
                categories(*REAL_ESTATE_CITIES)
            }
            tooltip { show = true }
            radiusAxis { }
            polar { }
            series {
                polarBar(null, REAL_ESTATE_ROWS.map { it[0] }, transparent = true, group = "Min Max")
                polarBar("Range", REAL_ESTATE_ROWS.map { it[1] - it[0] }, transparent = false, group = "Min Max")
                polarBar(
                    null,
                    REAL_ESTATE_ROWS.map { it[2] - REAL_ESTATE_BAR_HEIGHT },
                    transparent = true,
                    group = "Average",
                    layer = 10.0,
                )
                polarBar(
                    "Average",
                    REAL_ESTATE_ROWS.map { REAL_ESTATE_BAR_HEIGHT * 2.0 },
                    transparent = false,
                    group = "Average",
                    gap = "-100%",
                    layer = 10.0,
                )
            }
        },
        bar.example("bar-polar-stack") {
            angleAxis { }
            radiusAxis {
                type = AxisType.Category
                z = 10.0
                categories("Mon", "Tue", "Wed", "Thu")
            }
            polar { }
            series {
                polarStacked("A", 1.0, 2.0, 3.0, 4.0)
                polarStacked("B", 2.0, 4.0, 6.0, 8.0)
                polarStacked("C", 1.0, 2.0, 3.0, 4.0)
            }
            legend {
                show = true
                entries("A", "B", "C")
            }
        },
        bar.example("bar-polar-stack-radial") {
            angleAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            radiusAxis { }
            polar { }
            series {
                polarStacked("A", 1.0, 2.0, 3.0, 4.0, 3.0, 5.0, 1.0)
                polarStacked("B", 2.0, 4.0, 6.0, 1.0, 3.0, 2.0, 1.0)
                polarStacked("C", 1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 5.0)
            }
            legend {
                show = true
                entries("A", "B", "C")
            }
        },
        bar.example("polar-roundCap") {
            angleAxis {
                max = num(2)
                splitLine { show = false }
            }
            radiusAxis {
                type = AxisType.Category
                z = 10.0
                categories("v", "w", "x", "y", "z")
            }
            polar { }
            series {
                bar {
                    name = "Without Round Cap"
                    coordinateSystem = "polar"
                    values(4.0, 3.0, 2.0, 1.0, 0.0)
                    itemStyle {
                        borderColor = "red".asColor()
                        opacity = 0.8
                        borderWidth = 1.0
                    }
                }
                bar {
                    name = "With Round Cap"
                    coordinateSystem = "polar"
                    roundCap = true
                    values(4.0, 3.0, 2.0, 1.0, 0.0)
                    itemStyle {
                        borderColor = "green".asColor()
                        opacity = 0.8
                        borderWidth = 1.0
                    }
                }
            }
            legend {
                show = true
                entries("Without Round Cap", "With Round Cap")
            }
        },
        // --------------------------------------------------------- stacked tails
        bar.example("bar-stack-borderRadius") {
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                roundedStack(0)
                roundedStack(1)
                roundedStack(2)
                roundedStack(3)
                roundedStack(4)
            }
        },
        bar.example("bar-stack-normalization") {
            legend { selectedMode = BooleanOrString.of(false) }
            xAxis {
                type = AxisType.Category
                categories(*WEEK)
            }
            yAxis { type = AxisType.Value }
            series {
                normalizedChannel(0)
                normalizedChannel(1)
                normalizedChannel(2)
                normalizedChannel(3)
                normalizedChannel(4)
            }
        },
        bar.example("bar1") {
            title {
                text = "Rainfall vs Evaporation"
                subtext = "Fake Data"
            }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { entries("Rainfall", "Evaporation") }
            showToolbox(magicTypes = listOf(SeriesType.Line, SeriesType.Bar))
            xAxis {
                type = AxisType.Category
                categories(*MONTHS)
            }
            yAxis { type = AxisType.Value }
            series {
                bar {
                    name = "Rainfall"
                    values(2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3)
                    markPoint {
                        data = listOf(markPointStat("Max", "max"), markPointStat("Min", "min"))
                    }
                    markLine { data = listOf(averageMarkLine()) }
                }
                bar {
                    name = "Evaporation"
                    values(2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3)
                    markPoint {
                        data =
                            listOf(
                                markPointAt("Max", 182.2, 7, 183.0),
                                markPointAt("Min", 2.3, 11, 3.0),
                            )
                    }
                    markLine { data = listOf(averageMarkLine()) }
                }
            }
        },
    )
