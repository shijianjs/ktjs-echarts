package demo.examples.bar

import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
import demo.examples.doc.ObamaBudgetJson
import demo.examples.doc.budgetAxisLabel
import demo.examples.funnel.labels
import demo.examples.matrix.names
import demo.examples.scatter.point
import demo.examples.shared.Lcg
import demo.examples.shared.axisLabel
import demo.examples.shared.every
import demo.examples.shared.fixed
import demo.examples.shared.legendNames
import demo.examples.shared.recordOf
import demo.examples.shared.row
import demo.examples.shared.toolbox

/** The rotated labels shared by the four series of `bar-label-rotation`. */
internal fun rotatedLabels(): LabelOption = unsafeJso {
    show = true
    position = LabelPosition("insideBottom")
    distance = 15.0
    align = TextAlign.left
    verticalAlign = VerticalAlign.middle
    rotate = 90.0
    formatter = LabelFormatter("{c}  {name|{a}}")
    fontSize = StringOrDouble(16.0)
    rich = recordOf("name" to unsafeJso<TextStyleOption> {}).unsafeCast<Record<String, TextStyleOption>>()
}

/** The categories of `bar-gradient`, one character per bar. */
internal val gradientAxis =
    arrayOf("点", "击", "柱", "子", "或", "者", "两", "指", "在", "触", "屏", "上", "滑", "动", "能", "够", "自", "动", "缩", "放")

/** The values of `bar-gradient`. */
internal val gradientValues =
    doubleArrayOf(
        220.0, 182.0, 191.0, 234.0, 290.0, 330.0, 310.0, 123.0, 442.0, 321.0,
        90.0, 149.0, 210.0, 122.0, 133.0, 334.0, 198.0, 123.0, 125.0, 220.0,
    )

/** `dataZoom` action with a value window; upstream types extra payload keys as `any`. */
internal external interface DataZoomAction : Payload {
    var startValue: OptionDataValue?
    var endValue: OptionDataValue?
}

/** The `[category, value]` table of `bar-large`. */
internal class LargeBarData(val categories: Array<OptionDataItem?>, val values: Array<BarDataItem?>) {
    val count: Int
        get() = values.size
}

/**
 * Generates the data of `bar-large`, following the original generator with a fixed random source.
 *
 * The original stores the values as two-decimal strings; numbers render the same and cost less.
 */
internal fun largeBarData(count: Int): LargeBarData {
    val random = Lcg(2011)
    val categories = arrayOfNulls<OptionDataItem>(count)
    val values = arrayOfNulls<BarDataItem>(count)
    var baseValue = random.next() * 1000
    var smallBaseValue = 0.0
    val start = Date(2011, 0, 1).getTime()
    for (index in 0 until count) {
        smallBaseValue =
            if (index % 30 == 0) random.next() * 700 else smallBaseValue + random.next() * 500 - 250
        baseValue += random.next() * 20 - 10
        val value = kotlin.math.max(0.0, kotlin.math.round(baseValue + smallBaseValue) + 3000)
        val time = Date(start + index * 1000.0)
        categories[index] =
            OptionDataItem(OptionDataValue(ECharts.format.formatTime("yyyy-MM-dd\nhh:mm:ss", time, false)))
        values[index] = BarDataItem(OptionDataValue(value))
    }
    return LargeBarData(categories.unsafeCast<Array<OptionDataItem?>>(), values.unsafeCast<Array<BarDataItem?>>())
}

/** One break of the y axis of `bar-breaks-simple`. */
internal fun axisBreak(start: Double, end: Double): AxisBreakOption =
    unsafeJso {
        this.start = OptionDataValue(start)
        this.end = OptionDataValue(end)
        gap = StringOrDouble("1.5%")
    }

/** The four categories of `bar-polar-label-radial` / `bar-polar-label-tangential`. */
internal fun polarLabelCategories(): Array<OptionDataItem?> = strings("a", "b", "c", "d")

/**
 * The label of the polar bars of `bar-polar-label-radial` / `bar-polar-label-tangential`.
 *
 * Upstream comments the position as a teaching point: `"middle"`, or `"start"`, `"insideStart"`,
 * `"end"`, `"insideEnd"`.
 */
internal fun polarBarLabel(): LabelOption =
    unsafeJso {
        show = true
        position = LabelPosition("middle")
        formatter = LabelFormatter("{b}: {c}")
    }

/** The values of the polar bars of `bar-polar-label-radial` / `bar-polar-label-tangential`. */
internal fun polarLabelValues(): Array<BarDataItem?> = numbers(2.0, 1.2, 2.4, 3.6)

/** One series of `bar-polar-stack` / `bar-polar-stack-radial`, which share the `stack` group `a`. */
internal fun polarStackSeries(name: String, values: Array<BarDataItem?>): BarSeriesOption =
    barSeries {
        this.name = name
        stack = "a"
        coordinateSystem = "polar"
        emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
        typedData = values
    }

/** The cities of `bar-polar-real-estate`; they are also the categories of its angle axis. */
internal val realEstateCities =
    arrayOf(
        "北京", "上海", "深圳", "广州", "苏州", "杭州", "南京", "福州", "青岛", "济南",
        "长春", "大连", "温州", "郑州", "武汉", "成都", "东莞", "沈阳", "烟台",
    )

/** `[lowest, highest, average]` monthly rent per city of `bar-polar-real-estate`. */
internal val realEstateRows =
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

/**
 * One polar bar of `bar-polar-real-estate`.
 *
 * The chart draws two stacked bands: a transparent base up to the lower value, then the visible
 * segment, so the two series of a band share one `stack` group. [value] picks the number out of the
 * `[lowest, highest, average]` row of one city.
 */
internal fun realEstateSeries(
    name: String?,
    value: (DoubleArray) -> Double,
    transparent: Boolean,
    stack: String,
    barGap: String? = null,
    z: Double? = null,
): BarSeriesOption =
    barSeries {
        this.name = name
        this.stack = stack
        coordinateSystem = "polar"
        this.z = z
        typedData = numbers(*realEstateRows.map(value).toDoubleArray())
        if (transparent) {
            silent = true
            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("transparent") }
        }
        if (barGap != null) this.barGap = StringOrDouble(barGap)
    }

/** Rounds to the two decimals the original reaches through `+(value).toFixed(2)`. */
internal fun roundToCents(value: Double): Double = kotlin.math.round(value * 100) / 100

/**
 * `brushselected` payload of `bar-brush`.
 *
 * Upstream declares the event content as `any` and only documents `batch[].selected[].dataIndex`, so
 * the parts this page reads are described here and the event is cast to it.
 */
internal external interface BrushSelectedEvent {
    val batch: Array<BrushSelectedBatch>?
}

/** One brush area of [BrushSelectedEvent]. */
internal external interface BrushSelectedBatch {
    val selected: Array<BrushSelectedSeries>?
}

/** Indices of the data items a brush area covers, per series. */
internal external interface BrushSelectedSeries {
    val dataIndex: Array<Double>?
}

/** The emphasis style the four stacked series share. */
internal fun brushEmphasis(): EmphasisOption =
    unsafeJso {
        itemStyle = unsafeJso<ItemStyleOption> { shadowBlur = 10.0; shadowColor = ZRColor("rgba(0,0,0,0.3)") }
    }

/** The option of `bar-brush`: two stacks of two series over ten classes. */
internal fun barBrushOption(
    classes: Array<String>,
    first: DoubleArray,
    second: DoubleArray,
    third: DoubleArray,
    fourth: DoubleArray,
): EChartsOption =
    unsafeJso {
        legend = one(unsafeJso<LegendOption> { data = legendNames("bar", "bar2", "bar3", "bar4"); left = StringOrDouble("10%") })
        brush =
            one(
                unsafeJso<BrushOption> {
                    toolbox =
                        arrayOf(
                            BrushToolboxIconTypes.rect,
                            BrushToolboxIconTypes.polygon,
                            BrushToolboxIconTypes.lineX,
                            BrushToolboxIconTypes.lineY,
                            BrushToolboxIconTypes.keep,
                            BrushToolboxIconTypes.clear,
                        )
                    xAxisIndex = ModelFinderIndexQuery(0.0)
                },
            )
        toolbox =
            one(
                unsafeJso<ToolboxOption> {
                    feature =
                        unsafeJso<ToolboxFeature> {
                            magicType = unsafeJso<MagicTypeOption> { type = arrayOf("stack") }
                            dataView = unsafeJso<DataViewOption> {}
                        }
                },
            )
        tooltip = one(unsafeJso<TooltipOption> {})
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    data = strings(*classes)
                    name = "X Axis"
                    axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(true) }
                    splitLine = unsafeJso<SplitLineOption> { show = false }
                    splitArea = unsafeJso<SplitAreaOption> { show = false }
                },
            )
        yAxis = one(unsafeJso<YAxisOption> {})
        grid = one(unsafeJso<GridOption> { bottom = StringOrDouble(100.0) })
        series =
            arrayOf<SeriesOption>(
                barSeries {
                    name = "bar"
                    stack = "one"
                    emphasis = brushEmphasis()
                    typedData = numbers(*first)
                },
                barSeries {
                    name = "bar2"
                    stack = "one"
                    emphasis = brushEmphasis()
                    typedData = numbers(*second)
                },
                barSeries {
                    name = "bar3"
                    stack = "two"
                    emphasis = brushEmphasis()
                    typedData = numbers(*third)
                },
                barSeries {
                    name = "bar4"
                    stack = "two"
                    emphasis = brushEmphasis()
                    typedData = numbers(*fourth)
                },
            )
    }

/**
 * The title the original merges in after every brush: one line per series with its selected indices.
 *
 * The original also sets `width: 100`, which is not a title option of ECharts 6 and has no effect.
 */
internal fun brushSelectionTitle(event: BrushSelectedEvent): EChartsOption {
    val selected = event.batch?.firstOrNull()?.selected
    val lines = mutableListOf<String>()
    selected?.forEachIndexed { index, series ->
        lines += "[Series $index] " + (series.dataIndex?.joinToString(", ") ?: "")
    }
    return unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    backgroundColor = ZRColor("#333")
                    text = "SELECTED DATA INDICES: \n" + lines.joinToString("\n")
                    bottom = StringOrDouble(0.0)
                    right = StringOrDouble("10%")
                    textStyle = unsafeJso<TextStyleOption> { fontSize = StringOrDouble(12.0); color = "#fff" }
                },
            )
    }
}

// ---------------------------------------------------------------------------------------------
// mix-zoom-on-value
// ---------------------------------------------------------------------------------------------

/**
 * The option of `mix-zoom-on-value`; [budget] is the shared Obama budget table.
 *
 * The upstream `calculable: true` is the pre-DataZoom switch for value-axis zoom and no longer
 * exists in the ECharts 6 typings, so the port keeps the three data zooms, which is what the
 * option actually did.
 */
internal fun mixZoomOnValueOption(budget: ObamaBudgetJson): EChartsOption =
    unsafeJso {
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.axis
                    axisPointer =
                        unsafeJso<AxisPointerOption> {
                            type = AxisPointerTypes.shadow
                            label = unsafeJso<AxisPointerLabelOption> { show = true }
                        }
                },
            )
        toolbox =
            one(
                unsafeJso<ToolboxOption> {
                    show = true
                    feature =
                        unsafeJso<ToolboxFeature> {
                            dataView = unsafeJso<DataViewOption> { readOnly = false }
                            magicType = unsafeJso<MagicTypeOption> { type = arrayOf("line", "bar") }
                            restore = unsafeJso<ToolboxFeatureOption> {}
                            saveAsImage = unsafeJso<SaveAsImageOption> {}
                        }
                },
            )
        legend =
            one(
                unsafeJso<LegendOption> {
                    data = legendNames("Growth", "Budget 2011", "Budget 2012")
                    itemGap = 5.0
                },
            )
        grid =
            one(
                unsafeJso<GridOption> {
                    top = StringOrDouble("12%")
                    left = StringOrDouble("1%")
                    right = StringOrDouble("10%")
                    containLabel = true
                },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    data = strings(*budget.names)
                },
            )
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    type = AxisTypes.value
                    name = "Budget (million USD)"
                    axisLabel = budgetAxisLabel()
                },
            )
        dataZoom =
            arrayOf(
                unsafeJso<DataZoomOption> {
                    show = true
                    start = 94.0
                    end = 100.0
                },
                unsafeJso<DataZoomOption> {
                    type = DataZoomTypes.inside
                    start = 94.0
                    end = 100.0
                },
                unsafeJso<DataZoomOption> {
                    show = true
                    yAxisIndex = IndexOrIndexArray(0.0)
                    filterMode = FilterModes.empty
                    width = StringOrDouble(30.0)
                    height = StringOrDouble("80%")
                    showDataShadow = BooleanOrString(false)
                    left = StringOrDouble("93%")
                },
            )
        series =
            arrayOf<SeriesOption>(
                barSeries { name = "Budget 2011"; typedData = numbers(*budget.budget2011List.toDoubleArray()) },
                barSeries { name = "Budget 2012"; typedData = numbers(*budget.budget2012List.toDoubleArray()) },
            )
    }
