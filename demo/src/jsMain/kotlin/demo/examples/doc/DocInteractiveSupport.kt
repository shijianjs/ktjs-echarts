package demo.examples.doc

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.round
import io.github.shijianjs.ktjs.zrender.ElementTextConfig
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRShapeProps
import io.github.shijianjs.ktjs.zrender.ZRStyleProps
import demo.examples.candlestick.candle
import demo.examples.chord.column
import demo.examples.funnel.labels
import demo.examples.gauge.center
import demo.examples.matrix.names
import demo.examples.matrix.separator
import demo.examples.radar.indicator
import demo.examples.scatter.point
import demo.examples.shared.Lcg
import demo.examples.shared.axisLabel
import demo.examples.shared.every
import demo.examples.shared.legendNames
import demo.examples.shared.mixTimelineEstate
import demo.examples.shared.mixTimelineFinancial
import demo.examples.shared.mixTimelineGdp
import demo.examples.shared.mixTimelinePrimaryIndustry
import demo.examples.shared.mixTimelineProvinces
import demo.examples.shared.mixTimelineSecondaryIndustry
import demo.examples.shared.mixTimelineTertiaryIndustry
import demo.examples.shared.mixTimelineYears
import demo.examples.shared.numeric
import demo.examples.shared.pieItem
import demo.examples.shared.recordOf
import demo.examples.shared.row
import demo.examples.shared.textAt
import demo.examples.shared.textOf
import demo.examples.shared.thousands
import demo.examples.shared.toolbox
import demo.examples.sunburst.radius

/** One day in milliseconds, the step of the walking date of `tutorial-dynamic-data`. */
internal const val ONE_DAY = 24.0 * 3600.0 * 1000.0

/** The play icon of `timeline-dynamic-series`; the original also passes a base64 stop icon. */
internal const val PLAY_ICON =
    "path://M41.365908,29.4271388 L41.3664843,29.4265626 L26.3794329,19.1497136 L26.3747509,19.1541315 " +
        "C26.0642269,18.8592621 25.6429678,18.677793 25.1786824,18.677793 C24.2236284,18.677793 23.4494433,19.4443188 23.4494433,20.3905371 " +
        "C23.4494433,20.910214 23.4270417,21.9276946 23.4494433,21.9056292 L23.4494433,30.6673861 L23.4494433,39.8901629 " +
        "C23.4494433,39.8977982 23.4494433,40.4825908 23.4494433,40.9444991 C23.4494433,41.8901412 24.2236284,42.656691 25.1786824,42.656691 " +
        "C25.6447205,42.656691 26.0677564,42.4740454 26.3782564,42.1764869 L26.3794329,42.1770872 L41.3664843,31.9005503 L41.3659081,31.8996379 " +
        "C41.6917266,31.5882735 41.894997,31.1514078 41.894997,30.6670739 C41.894997,30.6658974 41.894997,30.6650091 41.894997,30.6635444 " +
        "C41.894997,30.6623679 41.894997,30.6609273 41.894997,30.6600389 C41.894997,30.175657 41.6917265,29.7384792 41.365908,29.4271388 Z"

/**
 * Data index of the `target` node in the depth-first flattened tree, where index 0 is the virtual
 * root: 0 root, 1 value 10, 2 value 4, 3 value 2, 4 `target`.
 */
internal const val ANCESTOR_TARGET_DATA_INDEX = 4.0

/** Same flattening as [ANCESTOR_TARGET_DATA_INDEX]: 0 root, 1 value 10, 2 `target`. */
internal const val DESCENDANT_TARGET_DATA_INDEX = 2.0

/**
 * The rolling window of `tutorial-dynamic-data`: one date string and one walking value per point.
 *
 * The first point has no date in the original page either (it starts with `[Math.random() * 150]`
 * and only the 99 following steps push a date), so the series is one point longer than the axis.
 */
internal class DynamicSeries {
    private val random = Lcg(20261028)
    private val dates = mutableListOf<String>()
    private val values = mutableListOf<Double>()
    private var time = Date(2014, 9, 3).getTime()

    init {
        values += random.next() * 150.0
        repeat(99) { add(false) }
    }

    /** Appends one day; with [shift] the oldest point is dropped so the window keeps its length. */
    fun add(shift: Boolean) {
        val now = Date(time)
        dates += "${now.getFullYear()}/${now.getMonth() + 1}/${now.getDate()}"
        values += (random.next() - 0.4) * 10.0 + values.last()
        if (shift) {
            dates.removeAt(0)
            values.removeAt(0)
        }
        time += ONE_DAY
    }

    /** The dates of the axis. */
    fun dates(): Array<OptionDataItem?> = strings(*dates.toTypedArray())

    /** The values of the line. */
    fun values(): Array<LineDataItem?> = numbers<LineDataItem>(*values.toDoubleArray())
}

/** The option of `tutorial-dynamic-data`, shared by the first paint and the updates. */
internal fun dynamicDataOption(window: DynamicSeries): EChartsOption =
    unsafeJso {
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    boundaryGap = BooleanOrStringArray(false)
                    data = window.dates()
                },
            )
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble("50%")))
                    type = AxisTypes.value
                },
            )
        series =
            arrayOf<SeriesOption>(
                lineSeries {
                    name = "成交"
                    smooth = BooleanOrDouble(true)
                    symbol = SymbolShape("none")
                    stack = "a"
                    areaStyle = unsafeJso<AreaStyleOption> {}
                    typedData = window.values()
                },
            )
    }

/** One node of the two `sunburst-highlight-*` trees. */
internal fun highlightNode(
    value: Double? = null,
    name: String? = null,
    children: Array<SunburstSeriesNodeItemOption>? = null,
): SunburstSeriesNodeItemOption =
    unsafeJso {
        if (name != null) this.name = OptionId(name)
        if (value != null) this.value = OptionDataValue(value)
        if (children != null) this.children = children
    }

/** The tree of `sunburst-highlight-ancestor`; the target sits three levels deep. */
internal fun ancestorTree(): Array<SunburstSeriesNodeItemOption?> =
    arrayOf(
        highlightNode(
            value = 10.0,
            children =
                arrayOf(
                    highlightNode(
                        value = 4.0,
                        children =
                            arrayOf(
                                highlightNode(
                                    value = 2.0,
                                    children = arrayOf(highlightNode(value = 1.0, name = "target")),
                                ),
                                highlightNode(value = 1.0),
                                highlightNode(value = 0.5),
                            ),
                    ),
                    highlightNode(value = 2.0),
                ),
        ),
        highlightNode(
            value = 4.0,
            children = arrayOf(highlightNode(children = arrayOf(highlightNode(value = 2.0)))),
        ),
    )

/** The tree of `sunburst-highlight-descendant`; the target sits one level below the root. */
internal fun descendantTree(): Array<SunburstSeriesNodeItemOption?> =
    arrayOf(
        highlightNode(
            value = 10.0,
            children =
                arrayOf(
                    highlightNode(
                        value = 4.0,
                        name = "target",
                        children =
                            arrayOf(
                                highlightNode(value = 2.0, children = arrayOf(highlightNode(value = 1.0))),
                                highlightNode(value = 1.0),
                                highlightNode(value = 0.5),
                            ),
                    ),
                    highlightNode(value = 2.0),
                ),
        ),
        highlightNode(
            value = 4.0,
            children = arrayOf(highlightNode(children = arrayOf(highlightNode(value = 2.0)))),
        ),
    )

/** The option of the two highlight pages; only [focus] and the tree differ. */
internal fun highlightOption(
    focus: EmphasisFocus,
    data: Array<SunburstSeriesNodeItemOption?>,
): EChartsOption =
    unsafeJso {
        series =
            arrayOf<SeriesOption>(
                sunburstSeries {
                    radius =
                        StringOrDoubleOrArray(
                            arrayOf(StringOrDouble("15%"), StringOrDouble("95%")),
                        )
                    center = arrayOf(StringOrDouble("50%"), StringOrDouble("60%"))
                    sort = null
                    emphasis =
                        unsafeJso<EmphasisOption> {
                            this.focus = focus
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("red") }
                        }
                    typedLabel =
                        unsafeJso<SunburstLabelOption> {
                            // The original asks for `rotate: 'none'`; ECharts 6 keeps the label
                            // horizontal for any non-keyword / non-number value, which is what 0
                            // degrees does here.
                            typedRotate = LabelRotate(0.0)
                            color = "#fff"
                        }
                    levels = arrayOf()
                    itemStyle =
                        unsafeJso<ItemStyleOption> {
                            color = ZRColor("yellow")
                            borderWidth = 2.0
                        }
                    typedData = data
                },
            )
    }

/** Emphasizes the `target` node of the current tree, as the original page tries to do. */
internal fun highlightTarget(
    chart: EChartsInstance,
    dataIndex: Double,
) {
    chart.dispatchAction(
        unsafeJso<HighlightPayload> {
            type = "highlight"
            seriesIndex = 0.0
            this.dataIndex = dataIndex
        },
    )
}

/** One rounded rectangle with a line of text, used twice by `graphic-bounding`. */
internal fun boundingGroup(
    id: String,
    raw: Boolean,
    left: StringOrDouble,
    top: StringOrDouble?,
    bottom: StringOrDouble?,
    text: String,
): GraphicElement =
    unsafeJso {
        type = "group"
        this.id = id
        this.left = left
        this.top = top
        this.bottom = bottom
        if (raw) bounding = GraphicBoundings.raw
        children =
            arrayOf(
                unsafeJso<GraphicElement> {
                    type = "rect"
                    z = 100.0
                    this.left = StringOrDouble("center")
                    this.top = StringOrDouble("center")
                    shape =
                        unsafeJso<ZRShapeProps> {
                            width = 330.0
                            height = 100.0
                        }
                    style =
                        unsafeJso<ZRStyleProps> {
                            fill = ZRColor("#fff")
                            stroke = ZRColor("#999")
                            lineWidth = 2.0
                            shadowBlur = 8.0
                            shadowOffsetX = 3.0
                            shadowOffsetY = 3.0
                            shadowColor = "rgba(0,0,0,0.3)"
                        }
                },
                unsafeJso<GraphicElement> {
                    type = "text"
                    z = 100.0
                    this.top = StringOrDouble("middle")
                    this.left = StringOrDouble("center")
                    style =
                        unsafeJso<ZRStyleProps> {
                            this.text = text
                            font = "20px \"STHeiti\", sans-serif"
                        }
                },
            )
    }

/** One series of `timeline-dynamic-series`. */
internal fun gdpBar(name: String, values: DoubleArray): BarSeriesOption =
    barSeries {
        this.name = name
        typedData = numbers(*values)
    }

/**
 * The `baseOption` of `doc-example/mix-timeline-all`.
 *
 * It declares the timeline axis, the axes, the legend and the seven series *names*; the data of every
 * series arrives through one option per year in [mixTimelineYearOption], merged by index.
 */
internal fun mixTimelineBaseOption(): EChartsOption =
    unsafeJso {
        timeline =
            unsafeJso<TimelineOption> {
                axisType = "category"
                autoPlay = true
                playInterval = 1000.0
                data = mixTimelinePoints()
                label =
                    unsafeJso<TimelineLabelOption> {
                        // The points are dates, the labels are years.
                        formatter = LabelFormatter(mixTimelineYearLabel())
                    }
            }
        title = one(unsafeJso<TitleOption> { subtext = "数据来自国家统计局" })
        tooltip = one(unsafeJso<TooltipOption> {})
        legend =
            one(
                unsafeJso<LegendOption> {
                    left = StringOrDouble("right")
                    top = StringOrDouble(10.0)
                    data = legendNames("第一产业", "第二产业", "第三产业")
                    selected =
                        recordOf(
                            "GDP" to (false.unsafeCast<JsAny>()),
                            "金融" to (false.unsafeCast<JsAny>()),
                            "房地产" to (false.unsafeCast<JsAny>()),
                        ).unsafeCast<Record<String, Boolean>?>()
                },
            )
        grid =
            one(
                unsafeJso<GridOption> {
                    top = StringOrDouble(80.0)
                    bottom = StringOrDouble(100.0)
                },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    // 31 provinces do not fit side by side; the labels are printed in two rows.
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
                    max = AxisMinMax(30000.0)
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
                unsafeJso<PieSeriesOption> {
                    name = "GDP占比"
                    center = arrayOf(StringOrDouble("75%"), StringOrDouble("35%"))
                    radius = StringOrDoubleOrArray("28%")
                },
            )
    }

/**
 * The [index]th timeline point of [mixTimelineYears] as its own option: the title carries the year
 * and every series carries only its data, so the declaration in the base option is reused.
 */
internal fun mixTimelineYearOption(index: Int): EChartsOption =
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
                unsafeJso<PieSeriesOption> {
                    typedData =
                        arrayOf(
                            pieItem("第一产业", mixTimelinePrimaryIndustry[index].sum()),
                            pieItem("第二产业", mixTimelineSecondaryIndustry[index].sum()),
                            pieItem("第三产业", mixTimelineTertiaryIndustry[index].sum()),
                        )
                },
            )
    }

/** A bar series of the base option: a name, no data. */
internal fun mixTimelineBar(name: String): SeriesOption = barSeries { this.name = name }

/**
 * A bar series of one year: the values of [values] as named items, which is what upstream's
 * `dataFormatter` produces so a tooltip and the labels know the province.
 */
internal fun mixTimelineBarData(values: DoubleArray): SeriesOption =
    unsafeJso<BarSeriesOption> {
        typedData =
            Array(values.size) { index ->
                BarDataItem(
                    unsafeJso<BarDataItemOption> {
                        name = OptionId(mixTimelineProvinces[index])
                        value = OptionDataValue(values[index])
                    },
                )
            }
    }

/** The ten points of the timeline axis; the third and the last are diamonds with their own tooltip. */
internal fun mixTimelinePoints(): Array<OptionDataItem?> =
    Array(mixTimelineYears.size) { index ->
        val value = "${mixTimelineYears[index]}-01-01"
        when (index) {
            3 -> OptionDataItem(mixTimelineDiamond(value, 16.0, TooltipFormatter("{b} GDP达到一个高度")))
            mixTimelineYears.lastIndex ->
                OptionDataItem(
                    mixTimelineDiamond(
                        value,
                        18.0,
                        TooltipFormatter { params ->
                            val name = params.unsafeCast<CallbackDataParams>().name
                            TooltipContent("${name}GDP达到又一个高度")
                        },
                    ),
                )
            else -> OptionDataItem(OptionDataValue(value))
        }
    }

/** One highlighted point of the timeline axis. */
internal fun mixTimelineDiamond(
    value: String,
    size: Double,
    tooltip: TooltipFormatter,
): TimelineDataItemOption =
    unsafeJso {
        this.value = OptionDataValue(value)
        symbol = SymbolShape("diamond")
        symbolSize = SymbolSize(size)
        this.tooltip = BooleanOrTooltipOption(unsafeJso<TooltipOption> { formatter = tooltip })
    }

/** The categories of the x axis, with a forced line break in front of every second province. */
internal fun mixTimelineAxisCategories(): Array<String> =
    Array(mixTimelineProvinces.size) { index ->
        val name = mixTimelineProvinces[index]
        if (index % 2 == 1) "\n$name" else name
    }

/**
 * The timeline label formatter: `(value) => new Date(value).getFullYear()`.
 *
 * A value axis hands the label formatter the raw axis value rather than the params object of a
 * series, and [LabelFormatter] is typed with [CallbackDataParams] for the series case, so the year
 * is read from the date string in JavaScript the way upstream reads it.
 */
internal fun mixTimelineYearLabel(): LabelFormatterCallback =
    js("(value) => String(new Date(value).getFullYear())") as LabelFormatterCallback

/** Shape of `data/obama_budget_proposal_2012.list.json`. */
internal external interface ObamaBudgetJson {
    val names: Array<String>
    val budget2011List: Array<Double>
    val budget2012List: Array<Double>
}

/**
 * Option of `doc-example/bar-dataZoom-filterMode`.
 *
 * The vertical slider is the point of the page: with `filterMode: 'empty'` a bar that stays inside
 * the horizontal window but falls outside the vertical one is drawn empty instead of being dropped,
 * which keeps the category axis intact.
 */
internal fun budgetOption(budget: ObamaBudgetJson): EChartsOption =
    unsafeJso {
        tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
        legend =
            one(
                unsafeJso<LegendOption> {
                    top = StringOrDouble(5.0)
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
        dataZoom = budgetDataZoom()
        series =
            arrayOf<SeriesOption>(
                barSeries { name = "Budget 2011"; typedData = numbers(*budget.budget2011List.toDoubleArray()) },
                barSeries { name = "Budget 2012"; typedData = numbers(*budget.budget2012List.toDoubleArray()) },
            )
    }

/** Labels of the budget axis, printed in thousands with the upstream thousands separator. */
internal fun budgetAxisLabel(): AxisLabelOption =
    unsafeJso {
        // A value axis hands the formatter the raw scale value, not the item parameters of a series.
        formatter =
            LabelFormatter(
                { params ->
                    val value = numeric(params.unsafeCast<JsAny?>())
                    if (value.isFinite()) {
                        ECharts.format.addCommas(StringOrDouble(value / 1000.0))
                    } else {
                        ""
                    }
                },
            )
    }

/** The horizontal slider and wheel plus the vertical slider that shows `filterMode: 'empty'`. */
internal fun budgetDataZoom(): Array<DataZoomOption> =
    arrayOf(
        unsafeJso<DataZoomOption> {
            type = DataZoomTypes.slider
            show = true
            start = 94.0
            end = 100.0
            handleSize = StringOrDouble(8.0)
        },
        unsafeJso {
            type = DataZoomTypes.inside
            start = 94.0
            end = 100.0
        },
        unsafeJso {
            type = DataZoomTypes.slider
            show = true
            yAxisIndex = IndexOrIndexArray(0.0)
            filterMode = FilterModes.empty
            width = StringOrDouble(12.0)
            height = StringOrDouble("70%")
            handleSize = StringOrDouble(8.0)
            showDataShadow = BooleanOrString(false)
            left = StringOrDouble("93%")
        },
    )

/**
 * Params of an axis-pointer label formatter.
 *
 * Upstream `AxisPointerLabelFormatterParams`; the wrapper types every label formatter with
 * [CallbackDataParams] because that is the shape shared by series labels, so the axis-pointer
 * specific fields are narrowed here.
 */
internal external interface AxisPointerLabelParams {
    val value: JsAny?
    val seriesData: Array<AxisPointerSeriesData>?
}

/** One series under the axis pointer, as read by [AxisPointerLabelParams.seriesData]. */
internal external interface AxisPointerSeriesData {
    val value: JsAny?
}

/** Columns of a candle after the date of a `data/stock-DJI.json` row. */
internal const val STOCK_CANDLE_COLUMNS = 5

/** Column of the volume in a row of `data/stock-DJI.json`. */
internal const val STOCK_VOLUME_COLUMN = 5

/** Column of the closing price, the input of the moving averages. */
internal const val STOCK_CLOSE_COLUMN = 2

/** Option of `doc-example/candlestick-axisPointer`, built once the quotes have loaded. */
internal fun stockOption(rows: Array<Array<JsAny>>): EChartsOption =
    unsafeJso {
        animation = false
        legend =
            one(
                unsafeJso<LegendOption> {
                    bottom = StringOrDouble(10.0)
                    left = StringOrDouble("center")
                    data = legendNames("Dow-Jones index", "MA5", "MA10", "MA20", "MA30")
                },
            )
        tooltip = one(stockTooltip())
        axisPointer = one(stockAxisPointer())
        toolbox = one(stockToolbox())
        brush = one(stockBrush())
        grid =
            arrayOf(
                unsafeJso<GridOption> {
                    left = StringOrDouble("10%")
                    right = StringOrDouble("8%")
                    height = StringOrDouble("50%")
                },
                unsafeJso {
                    left = StringOrDouble("10%")
                    right = StringOrDouble("8%")
                    bottom = StringOrDouble("20%")
                    height = StringOrDouble("15%")
                },
            )
        xAxis = stockAxes(stockDates(rows))
        yAxis = stockValueAxes()
        dataZoom = stockDataZoom()
        series = stockSeries(rows)
    }

/** Axis tooltip with a cross pointer, pinned to the far side of the pointer. */
internal fun stockTooltip(): TooltipOption =
    unsafeJso {
        trigger = TooltipTriggers.axis
        axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
        backgroundColor = ZRColor("rgba(255, 255, 255, 0.8)")
        position =
            TooltipPosition(
                { point, _, _, _, size ->
                    val box =
                        unsafeJso<TooltipBoxLayoutOption> {
                            top = StringOrDouble(10.0)
                            if (point[0] < size.viewSize[0] / 2.0) {
                                right = StringOrDouble(30.0)
                            } else {
                                left = StringOrDouble(30.0)
                            }
                        }
                    TooltipPosition(box)
                },
            )
        extraCssText = "width: 170px"
    }

/** Links the pointers of the price and the volume grid. */
internal fun stockAxisPointer(): AxisPointerOption =
    unsafeJso {
        link = one(unsafeJso<AxisPointerLink> { xAxisIndex = ModelFinderIndexQuery("all") })
        label = unsafeJso<AxisPointerLabelOption> { backgroundColor = ZRColor("#777") }
    }

/** Zoom and brush buttons; the brush offers only the horizontal line and clearing. */
internal fun stockToolbox(): ToolboxOption =
    unsafeJso {
        feature =
            unsafeJso<ToolboxFeature> {
                dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> { yAxisIndex = ModelFinderIndexQuery(false) }
                brush =
                    unsafeJso<ToolboxBrushFeatureOption> {
                        type = arrayOf(BrushToolboxIconTypes.lineX, BrushToolboxIconTypes.clear)
                    }
            }
    }

/** Brushing one grid applies to both, and what falls outside keeps only a tenth of its color. */
internal fun stockBrush(): BrushOption =
    unsafeJso {
        xAxisIndex = ModelFinderIndexQuery("all")
        brushLink = ModelFinderIndexQuery("all")
        outOfBrush = unsafeJso<VisualOptionUnit> { colorAlpha = DoubleOrDoubleArray(0.1) }
    }

/** The two category axes: the price grid and, below it, the volume grid. */
internal fun stockAxes(dates: Array<OptionDataItem?>): Array<XAxisOption> =
    arrayOf(
        unsafeJso<XAxisOption> {
            type = AxisTypes.category
            data = dates
            scale = true
            boundaryGap = BooleanOrStringArray(false)
            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
            splitLine = unsafeJso<SplitLineOption> { show = false }
            splitNumber = 20.0
            min = AxisMinMax("dataMin")
            max = AxisMinMax("dataMax")
            axisPointer = unsafeJso<AxisPointerOption> { z = 100.0 }
        },
        unsafeJso {
            type = AxisTypes.category
            gridIndex = 1.0
            data = dates
            scale = true
            boundaryGap = BooleanOrStringArray(false)
            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
            splitLine = unsafeJso<SplitLineOption> { show = false }
            axisLabel = unsafeJso<AxisLabelOption> { show = false }
            splitNumber = 20.0
            min = AxisMinMax("dataMin")
            max = AxisMinMax("dataMax")
            axisPointer =
                unsafeJso {
                    label = unsafeJso<AxisPointerLabelOption> { formatter = stockPointerLabel() }
                }
        },
    )

/**
 * Label of the volume axis pointer: the date and, under it, the price the pointer sits on.
 *
 * Upstream returns `params.value` plus `echarts.format.addCommas(seriesData[0].value)` when a series
 * value exists; `seriesData` is empty on the y pointer, so no value is appended there.
 */
internal fun stockPointerLabel(): LabelFormatter =
    LabelFormatter(
        { params ->
            val pointer = params.unsafeCast<AxisPointerLabelParams>()
            val date = textOf(pointer.value)
            val price = pointer.seriesData?.firstOrNull()?.value
            if (price == null) {
                date
            } else {
                date + "\n" + ECharts.format.addCommas(StringOrDouble(numeric(price)))
            }
        },
    )

/** The price axis, with the alternating split areas of the original. */
internal fun stockValueAxes(): Array<YAxisOption> =
    arrayOf(
        unsafeJso<YAxisOption> {
            scale = true
            splitArea = unsafeJso<SplitAreaOption> { show = true }
        },
        unsafeJso {
            scale = true
            gridIndex = 1.0
            splitNumber = 2.0
            axisLabel = unsafeJso<AxisLabelOption> { show = false }
            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
            splitLine = unsafeJso<SplitLineOption> { show = false }
        },
    )

/** The two zoom windows, both bound to the two category axes. */
internal fun stockDataZoom(): Array<DataZoomOption> =
    arrayOf(
        unsafeJso<DataZoomOption> {
            type = DataZoomTypes.inside
            xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
            start = 98.0
            end = 100.0
        },
        unsafeJso {
            show = true
            xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
            type = DataZoomTypes.slider
            top = StringOrDouble("85%")
            start = 98.0
            end = 100.0
        },
    )

/** Candles, the four moving averages and the volume bars of the second grid. */
internal fun stockSeries(rows: Array<Array<JsAny>>): Array<SeriesOption> {
    val closes = DoubleArray(rows.size) { numeric(rows[it][STOCK_CLOSE_COLUMN]) }
    return arrayOf(
        candlestickSeries {
            name = "Dow-Jones index"
            typedData = stockCandles(rows)
            itemStyle =
                unsafeJso<CandlestickItemStyleOption> {
                    color = ZRColor("#06B800")
                    color0 = ZRColor("#FA0000")
                }
            tooltip = unsafeJso<TooltipOption> { formatter = stockCandleTooltip() }
        },
        *stockAverageLines(closes),
        barSeries {
            name = "Volumn"
            xAxisIndex = 1.0
            yAxisIndex = 1.0
            typedData = numbers(*DoubleArray(rows.size) { numeric(rows[it][STOCK_VOLUME_COLUMN]) })
        },
    )
}

/**
 * Series tooltip of the candles.
 *
 * The original returns an HTML block with an `<hr>`; this site keeps tooltip content plain, so the
 * rows are separated by `<br/>` only.
 */
internal fun stockCandleTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val item = params.unsafeCast<Array<CallbackDataParams>>()[0]
            val candle = item.data.unsafeCast<JsAny?>()
            TooltipContent(
                listOf(
                    "Date: " + item.name,
                    "Open: " + textAt(candle, 0),
                    "Close: " + textAt(candle, 1),
                    "Lowest: " + textAt(candle, 2),
                    "Highest: " + textAt(candle, 3),
                ).joinToString("<br/>"),
            )
        },
    )

/** Dates of the axis, the first column of every row. */
internal fun stockDates(rows: Array<Array<JsAny>>): Array<OptionDataItem?> =
    strings(*Array(rows.size) { textOf(rows[it][0]) })

/** The five columns that follow the date, in the order ECharts expects a candle. */
internal fun stockCandles(rows: Array<Array<JsAny>>): Array<CandlestickDataItem?> =
    Array(rows.size) { row ->
        CandlestickDataItem(
            Array(STOCK_CANDLE_COLUMNS) { column -> OptionDataValue(numeric(rows[row][column + 1])) },
        )
    }

/** Moving averages of the closing prices, in the order of the legend. */
internal fun stockAverageLines(closes: DoubleArray): Array<SeriesOption> =
    arrayOf<SeriesOption>(
        stockAverageLine("MA5", movingAverage(5, closes)),
        stockAverageLine("MA10", movingAverage(10, closes)),
        stockAverageLine("MA20", movingAverage(20, closes)),
        stockAverageLine("MA30", movingAverage(30, closes)),
    )

internal fun stockAverageLine(seriesName: String, data: Array<LineDataItem?>): SeriesOption =
    lineSeries {
        name = seriesName
        smooth = BooleanOrDouble(true)
        lineStyle = unsafeJso<LineStyleOption> { opacity = 0.5 }
        typedData = data
    }

/**
 * Mean of the last [dayCount] closes, rounded to three decimals as the original does.
 *
 * The first [dayCount] points have no window yet; the original writes the string `"-"`, which
 * ECharts reads as a missing value.
 */
internal fun movingAverage(dayCount: Int, closes: DoubleArray): Array<LineDataItem?> =
    Array(closes.size) { index ->
        if (index < dayCount) {
            LineDataItem(OptionDataValue("-"))
        } else {
            val sum = (0 until dayCount).sumOf { offset -> closes[index - offset] }
            LineDataItem(OptionDataValue(round(sum / dayCount * 1000.0) / 1000.0))
        }
    }

/** `grid.left` of the control panel of `doc-example/grid-outerBounds`, kept by this port. */
internal const val OUTER_BOUNDS_GRID_LEFT = 70.0

/** `grid.top` of that panel. */
internal const val OUTER_BOUNDS_GRID_TOP = 50.0

/** `grid.right` of that panel. */
internal const val OUTER_BOUNDS_GRID_RIGHT = 260.0

/** `grid.bottom` of that panel. */
internal const val OUTER_BOUNDS_GRID_BOTTOM = 45.0

/** The jitter of the `medium_data` set: five points, `y += random * 1000`. */
internal val OUTER_BOUNDS_JITTER = doubleArrayOf(0.79, 0.03, 0.25, 0.68, 0.33)

/** Name of an axis, spelled the way the original builds it (`xAxis_0name`). */
internal fun outerBoundsAxisName(
    axis: String,
    index: Int,
): String = "${axis}Axis_${index}name"

/** `[x, y]` points of the medium data set: `100000 + i * 10000`, with the jitter added to `y`. */
internal fun outerBoundsData(): Array<LineDataItem?> =
    points<LineDataItem>(
        *Array(OUTER_BOUNDS_JITTER.size) { index ->
            val x = 100000.0 + index * 10000.0
            doubleArrayOf(x, x + OUTER_BOUNDS_JITTER[index] * 1000.0)
        },
    )

/**
 * Option of `doc-example/grid-outerBounds`.
 *
 * The original drives every box of the grid from a floating control panel; this port keeps the
 * defaults of that panel and only the two tutorial rectangles, which are rebuilt from the current
 * [width] and [height] so that they still hug the grid after a resize.
 */
internal fun outerBoundsOption(
    width: Double,
    height: Double,
): EChartsOption =
    unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "Grid outerBounds example"
                    left = StringOrDouble(5.0)
                    top = StringOrDouble(5.0)
                },
            )
        legend = one(unsafeJso<LegendOption> { top = StringOrDouble(3.0) })
        tooltip = one(unsafeJso<TooltipOption> {})
        graphic = one(GraphicComponentLooseOption(outerBoundsIndicators(width, height)))
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.value
                    name = outerBoundsAxisName("x", 0)
                    nameLocation = AxisNameLocations.center
                    splitLine = unsafeJso<SplitLineOption> { show = false }
                },
            )
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    type = AxisTypes.value
                    name = outerBoundsAxisName("y", 0)
                    nameLocation = AxisNameLocations.center
                    alignTicks = true
                    axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(true) }
                },
            )
        grid = one(outerBoundsGrid())
        series =
            arrayOf<SeriesOption>(
                lineSeries {
                    name = "series_0"
                    symbolSize = SymbolSize(10.0)
                    typedData = outerBoundsData()
                },
            )
    }

/** The grid of the page: the panel's box, an explicit `outerBounds` of `0` and `mode: 'auto'`. */
internal fun outerBoundsGrid(): GridOption =
    unsafeJso {
        left = StringOrDouble(OUTER_BOUNDS_GRID_LEFT)
        top = StringOrDouble(OUTER_BOUNDS_GRID_TOP)
        right = StringOrDouble(OUTER_BOUNDS_GRID_RIGHT)
        bottom = StringOrDouble(OUTER_BOUNDS_GRID_BOTTOM)
        outerBounds =
            unsafeJso<BoxLayoutOption> {
                left = StringOrDouble(0.0)
                top = StringOrDouble(0.0)
                right = StringOrDouble(0.0)
                bottom = StringOrDouble(0.0)
            }
        outerBoundsMode = OuterBoundsModes.auto
        containLabel = false
    }

/**
 * The two tutorial rectangles.
 *
 * With `outerBoundsMode: 'auto'` and `containLabel: false` the outer bounds are exactly the box set
 * in `grid.outerBounds`, which the panel keeps at `0` on every side; that is the second rectangle.
 */
internal fun outerBoundsIndicators(
    width: Double,
    height: Double,
): GraphicComponentOption =
    unsafeJso {
        elements =
            arrayOf(
                indicatorRectangle(
                    id = "grid_axis_aligned_rect_indicator",
                    left = OUTER_BOUNDS_GRID_LEFT,
                    top = OUTER_BOUNDS_GRID_TOP,
                    width = width - OUTER_BOUNDS_GRID_LEFT - OUTER_BOUNDS_GRID_RIGHT,
                    height = height - OUTER_BOUNDS_GRID_TOP - OUTER_BOUNDS_GRID_BOTTOM,
                    fillColor = "rgba(0,0,100,0.1)",
                    caption = "grid.t/r/b/l/w/h rect",
                ),
                indicatorRectangle(
                    id = "grid_outerBounds_indicator",
                    left = 0.0,
                    top = 0.0,
                    width = width,
                    height = height,
                    fillColor = "rgba(0,80,0,0.1)",
                    caption = "grid.outerBounds.t/r/b/l/w/h rect",
                ),
            )
    }

/** One indicator rectangle with its caption, drawn behind the chart and ignoring the pointer. */
internal fun indicatorRectangle(
    id: String,
    left: Double,
    top: Double,
    width: Double,
    height: Double,
    fillColor: String,
    caption: String,
): GraphicElement =
    unsafeJso {
        this.id = id
        type = "rect"
        shape = unsafeJso<ZRShapeProps> { x = left; y = top; this.width = width; this.height = height }
        style = unsafeJso<ZRStyleProps> { fill = ZRColor(fillColor) }
        silent = true
        textContent =
            unsafeJso<GraphicElement> {
                type = "text"
                style =
                    unsafeJso<ZRStyleProps> {
                        text = caption
                        stroke = ZRColor("#fff")
                        lineWidth = 2.0
                        fill = ZRColor("#555")
                        fontSize = StringOrDouble(10.0)
                    }
            }
        textConfig =
            unsafeJso<ElementTextConfig> {
                position = StringOrDoubleOrArray("insideTopRight")
                distance = 0.0
            }
    }
