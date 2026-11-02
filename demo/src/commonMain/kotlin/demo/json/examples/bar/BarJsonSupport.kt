package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.examples.dataset.row
import demo.json.examples.shared.fill
import demo.json.examples.shared.stroke

/** Days of the week, the category axis of most pages. */
internal val WEEK = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

/** Months of `multiple-y-axis` and `bar1`. */
internal val MONTHS =
    arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

/** `focus: 'series'` of the hovered series in the stacked and animated pages. */
internal fun focusSeries(): EmphasisOption = EmphasisOption().apply { focus = "series" }

/** An axis pointer of [pointerType]; the model has no builder for it. */
internal fun pointerOf(pointerType: String): AxisPointerOption =
    AxisPointerOption().apply { type = pointerType }

/** The axis tooltip with a shadow pointer shared by most bar pages. */
internal fun EChartsOption.shadowAxisTooltip() {
    tooltip {
        trigger = TooltipTrigger.Axis
        axisPointer = pointerOf("shadow")
    }
}

/** The `dataView` / `magicType` / `restore` / `saveAsImage` toolbox of several pages. */
internal fun EChartsOption.showToolbox(magicTypes: List<SeriesType>? = null) {
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
internal fun fillStyle(fillColor: String): ItemStyleOption =
    ItemStyleOption().apply { color = fillColor.asColor() }

/** A line style with only a stroke color. */
internal fun lineColor(strokeColor: String): LineStyleOption =
    LineStyleOption().apply { color = strokeColor.asColor() }

/** The transparent style that hides the placeholder series of the waterfall pages. */
internal fun invisibleStyle(): ItemStyleOption =
    ItemStyleOption().apply {
        borderColor = "transparent".asColor()
        color = "transparent".asColor()
    }

/** One bar series of the stacked pages: a `total` stack, visible labels and series focus. */
internal fun SeriesBuilder.stackedBar(seriesName: String, vararg row: Number) {
    bar {
        this.name = seriesName
        stack = "total"
        label { show = true }
        emphasis = focusSeries()
        values(*row)
    }
}

/** One series of the two polar stack pages, which share the stack group `a`. */
internal fun SeriesBuilder.polarStacked(seriesName: String, vararg row: Number) {
    bar {
        this.name = seriesName
        stack = "a"
        coordinateSystem = "polar"
        emphasis = focusSeries()
        values(*row)
    }
}

/** One `markPoint` entry of a built-in statistic. */
internal fun markPointStat(markName: String, statType: String): MarkPointData =
    MarkPointData().apply {
        this.name = markName
        this.type = statType
    }

/** One `markPoint` entry pinned to an explicit category index and value. */
internal fun markPointAt(
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
internal fun averageMarkLine(): MarkLineDataItem =
    MarkLineDataItem.of(
        MarkLineData().apply {
            name = "Avg"
            type = "average"
        },
    )

/** A waterfall row; `null` is the `"-"` hole ECharts reads as an empty slot of the stack. */
internal fun waterfallRow(vararg values: Double?): List<DataValue> =
    values.map { value -> if (value == null) textValue("-") else valueOf(value) }

/** The channels stacked by `bar-stack-normalization`, in legend order. */
internal val AD_CHANNELS =
    arrayOf("Direct", "Mail Ad", "Affiliate Ad", "Video Ad", "Search Engine")

/** `[channel][day]` counts of `bar-stack-normalization`; the table holds no negative value. */
internal val AD_COUNTS =
    arrayOf(
        doubleArrayOf(100.0, 302.0, 301.0, 334.0, 390.0, 330.0, 320.0),
        doubleArrayOf(320.0, 132.0, 101.0, 134.0, 90.0, 230.0, 210.0),
        doubleArrayOf(220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0),
        doubleArrayOf(150.0, 212.0, 201.0, 154.0, 190.0, 330.0, 410.0),
        doubleArrayOf(820.0, 832.0, 901.0, 934.0, 1290.0, 1330.0, 1320.0),
    )

/** The total of every day, the denominator of the normalization. */
internal val AD_TOTALS = DoubleArray(WEEK.size) { day -> AD_COUNTS.sumOf { it[day] } }

/**
 * One series of `bar-stack-normalization`.
 *
 * Upstream divides by the daily total and rounds the *label* to `Math.round(value * 1000) / 10`
 * percent. A template cannot round, so the shares are pre-scaled to percent and rounded here, and
 * the label is `{c}%`; the bars keep the heights of the original.
 */
internal fun SeriesBuilder.normalizedChannel(index: Int) {
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
internal val REAL_ESTATE_CITIES =
    arrayOf(
        "北京", "上海", "深圳", "广州", "苏州", "杭州", "南京", "福州", "青岛", "济南",
        "长春", "大连", "温州", "郑州", "武汉", "成都", "东莞", "沈阳", "烟台",
    )

/** `[lowest, highest, average]` monthly rent per city of `bar-polar-real-estate`. */
internal val REAL_ESTATE_ROWS =
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
internal const val REAL_ESTATE_BAR_HEIGHT = 50.0

/** One stacked bar of `bar-polar-real-estate`; the transparent bases lift the visible bands. */
internal fun SeriesBuilder.polarBar(
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
internal val STACK_RADIUS_ROWS: Array<Array<Double?>> =
    arrayOf(
        arrayOf(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0),
        arrayOf(10.0, 46.0, 64.0, null, 0.0, null, 0.0),
        arrayOf(30.0, null, 0.0, 20.0, 10.0, null, 0.0),
        arrayOf(30.0, null, 0.0, 20.0, 10.0, null, 0.0),
        arrayOf(10.0, 20.0, 150.0, 0.0, null, 50.0, 10.0),
    )

/** The stack group every series of `bar-stack-borderRadius` belongs to. */
internal val STACK_RADIUS_GROUPS = arrayOf("a", "a", "a", "b", "b")

/** The names of the series of `bar-stack-borderRadius`. */
internal val STACK_RADIUS_NAMES = arrayOf("a", "b", "c", "d", "e")

/**
 * For every stack of [STACK_RADIUS_GROUPS], the index of the last series that fills a day.
 *
 * Only non-zero numbers count, which reproduces the `data && data !== '-'` test of the original:
 * `0` is as empty as the `null` that stands for `"-"`.
 */
internal val STACK_RADIUS_ENDS: Map<String, IntArray> =
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
internal fun SeriesBuilder.roundedStack(index: Int) {
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
internal fun rotatedLabel(): LabelOption =
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
internal class LargeBarRandom {
    private var state = 20110101L

    /** Next value in `0..1`. */
    fun next(): Double {
        state = state * 1103515245L + 12345L
        return ((state ushr 16) and 0x7FFF).toDouble() / 0x8000
    }
}

/** The values of `bar-large`, following the upstream generator with a fixed random source. */
internal fun largeBarValues(count: Int): List<Double> {
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
internal fun largeBarCategories(count: Int): List<DataValue> =
    (0 until count).map { index ->
        val time =
            twoDigits(index / 3600) + ":" + twoDigits(index / 60 % 60) + ":" + twoDigits(index % 60)
        textValue("2011-01-01\n$time")
    }

/** Two-digit padding of the time parts of `bar-large`. */
internal fun twoDigits(value: Int): String = value.toString().padStart(2, '0')
