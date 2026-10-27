package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.math.PI
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * The `doc-example/` pages that drive the chart after the first paint: timers, `dispatchAction`,
 * `showLoading` and the timeline component.
 *
 * The first two batches of the folder live in `DocExamples.kt` (static pages) and `DocMore.kt`
 * (pictorial bar tutorials, zooming scatter pages, text and animation pages). These pages need the
 * chart instance or the option tree instead, so they use `interactive` and the `ChartHolder` timers,
 * which are cleared when the page is left.
 *
 * Changed on purpose:
 *
 * - the `Math.random()` of the dynamic line is replaced by [Lcg]; the sequence changes on every tick
 *   but is reproducible;
 * - the ECharts 4 `itemStyle.normal` / `label.normal` wrappers are dropped (ECharts 6 has no
 *   `normal` state), and the same fields are set directly;
 * - `graphic-bounding` keeps the rotation in radians, which is what zrender reads;
 * - the stop icon of `timeline-dynamic-series` is an inline base64 image upstream, which this site
 *   does not ship, so the timeline keeps the default stop icon and only the `path://` play icon is
 *   copied;
 * - `sunburst-highlight-*` set a legacy `highlight` / `downplay` state and a `silent` root flag,
 *   neither of which ECharts 6 reads (the states are `emphasis` / `blur` / `select`); the ports keep
 *   the two `emphasis` blocks the original sets (the first one, with `focus`, is overwritten by the
 *   second in the original object literal) and then dispatch the modern `highlight` action.
 *   `sunburstHighlight` / `targetNodeId` is deprecated and, as of ECharts 6.1.0, applies no state at
 *   all — the official page stays all yellow — so the ports resolve the target's flattened data
 *   index themselves instead.
 */

private val docInteractive = Category(Groups.DOC, "examples/DocInteractive.kt")

/** One day in milliseconds, the step of the walking date of `tutorial-dynamic-data`. */
private const val ONE_DAY = 24.0 * 3600.0 * 1000.0

/** The play icon of `timeline-dynamic-series`; the original also passes a base64 stop icon. */
private const val PLAY_ICON =
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
private const val ANCESTOR_TARGET_DATA_INDEX = 4.0

/** Same flattening as [ANCESTOR_TARGET_DATA_INDEX]: 0 root, 1 value 10, 2 `target`. */
private const val DESCENDANT_TARGET_DATA_INDEX = 2.0

/**
 * The rolling window of `tutorial-dynamic-data`: one date string and one walking value per point.
 *
 * The first point has no date in the original page either (it starts with `[Math.random() * 150]`
 * and only the 99 following steps push a date), so the series is one point longer than the axis.
 */
private class DynamicSeries {
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
private fun dynamicDataOption(window: DynamicSeries): EChartsOption =
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
private fun highlightNode(
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
private fun ancestorTree(): Array<SunburstSeriesNodeItemOption?> =
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
private fun descendantTree(): Array<SunburstSeriesNodeItemOption?> =
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
private fun highlightOption(
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
private fun highlightTarget(
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
private fun boundingGroup(
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
private fun gdpBar(name: String, values: DoubleArray): BarSeriesOption =
    barSeries {
        this.name = name
        typedData = numbers(*values)
    }

val docInteractiveEntries: List<CatalogEntry> =
    listOf(
        docInteractive.interactive(
            "doc-example/barGrid-barGap",
            "柱状图：同轴柱子的间距",
            "Bar grid: barGap",
        ) { chart, holder ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    animationDurationUpdate = AnimationDuration(1200.0)
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                data = strings("a", "b", "c", "d")
                                axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                                axisLabel =
                                    unsafeJso<AxisLabelOption> {
                                        formatter = LabelFormatter("barGap: '-100%'")
                                    }
                            },
                        )
                    yAxis = one(unsafeJso<YAxisOption> { splitLine = unsafeJso<SplitLineOption> { show = false } })
                    series =
                        arrayOf<SeriesOption>(
                            barSeries {
                                // The original nests `color` under `itemStyle.normal`.
                                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddd") }
                                silent = true
                                barWidth = StringOrDouble(40.0)
                                // A negative gap overlaps the two series.
                                barGap = StringOrDouble("-100%")
                                typedData = numbers(60.0, 60.0, 60.0, 60.0)
                            },
                            barSeries {
                                barWidth = StringOrDouble(40.0)
                                z = 10.0
                                typedData = numbers(45.0, 60.0, 13.0, 25.0)
                            },
                        )
                },
            )
            val barGaps = arrayOf("30%", "-100%")
            var loopIndex = 0
            holder.setInterval({
                val barGap = barGaps[loopIndex]
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            formatter = LabelFormatter("barGap: '$barGap'")
                                        }
                                },
                            )
                        // A one-item array merges into the first series.
                        series = arrayOf<SeriesOption>(barSeries { this.barGap = StringOrDouble(barGap) })
                    },
                )
                loopIndex = (loopIndex + 1) % barGaps.size
            }, 2000)
        },
        docInteractive.interactive(
            "doc-example/tutorial-dynamic-data",
            "动态数据",
            "Dynamic data",
        ) { chart, holder ->
            val series = DynamicSeries()
            chart.setOption(dynamicDataOption(series))
            holder.setInterval({
                series.add(true)
                chart.setOption(dynamicDataOption(series))
            }, 500)
        },
        docInteractive.interactive(
            "doc-example/tutorial-loading",
            "异步数据加载",
            "Asynchronous data loading",
        ) { chart, holder ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "异步数据加载示例" })
                    tooltip = one(unsafeJso<TooltipOption> {})
                    legend = one(unsafeJso<LegendOption> { data = legendNames("销量") })
                    xAxis = one(unsafeJso<XAxisOption> { data = strings() })
                    yAxis = one(unsafeJso<YAxisOption> {})
                    series = arrayOf<SeriesOption>(barSeries { name = "销量"; typedData = numbers() })
                },
            )
            chart.showLoading(null)
            // The original fakes a three-second request with `setTimeout`.
            holder.setTimeout({
                chart.hideLoading()
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    data = strings("衬衫", "羊毛衫", "雪纺衫", "裤子", "高跟鞋", "袜子")
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                barSeries {
                                    name = "销量"
                                    typedData = numbers(5.0, 20.0, 36.0, 10.0, 10.0, 20.0)
                                },
                            )
                    },
                )
            }, 3000)
        },
        docInteractive.interactive(
            "doc-example/sunburst-highlight-ancestor",
            "旭日图：高亮祖先节点",
            "Sunburst: highlight the ancestors",
        ) { chart, holder ->
            chart.setOption(highlightOption(EmphasisFocuses.ancestor, ancestorTree()))
            holder.setTimeout({ highlightTarget(chart, ANCESTOR_TARGET_DATA_INDEX) }, 0)
        },
        docInteractive.interactive(
            "doc-example/sunburst-highlight-descendant",
            "旭日图：高亮子孙节点",
            "Sunburst: highlight the descendants",
        ) { chart, holder ->
            chart.setOption(highlightOption(EmphasisFocuses.descendant, descendantTree()))
            holder.setTimeout({ highlightTarget(chart, DESCENDANT_TARGET_DATA_INDEX) }, 0)
        },
        docInteractive.interactive(
            "doc-example/graphic-bounding",
            "graphic 组件的包围盒",
            "Graphic: bounding",
        ) { chart, holder ->
            val rawText =
                "这个文本框的 bounding 为 \"raw\"\n" +
                    "表示定位时仅仅取 group 自己的\n" +
                    "未经过 transform 的包围盒。"
            val allText =
                "这个文本框的 bounding 为 \"all\"\n" +
                    "表示定位时取整个 group 的\n" +
                    "经过 transform 后的包围盒。"
            chart.setOption(
                unsafeJso<EChartsOption> {
                    graphic =
                        arrayOf(
                            GraphicComponentLooseOption(
                                boundingGroup(
                                    id = "textGroup1",
                                    raw = true,
                                    left = StringOrDouble(30.0),
                                    top = StringOrDouble(30.0),
                                    bottom = null,
                                    text = rawText,
                                ),
                            ),
                            GraphicComponentLooseOption(
                                boundingGroup(
                                    id = "textGroup2",
                                    raw = false,
                                    left = StringOrDouble(0.0),
                                    top = null,
                                    bottom = StringOrDouble(0.0),
                                    text = allText,
                                ),
                            ),
                        )
                },
            )
            var rotation = 0.0
            holder.setInterval({
                rotation = (rotation + PI / 300.0) % (PI * 2.0)
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        // Elements are merged by id, so only the rotation is updated.
                        graphic =
                            arrayOf(
                                GraphicComponentLooseOption(
                                    unsafeJso<GraphicElement> {
                                        id = "textGroup1"
                                        this.rotation = rotation
                                    },
                                ),
                                GraphicComponentLooseOption(
                                    unsafeJso<GraphicElement> {
                                        id = "textGroup2"
                                        this.rotation = rotation
                                    },
                                ),
                            )
                    },
                )
            }, 20)
        },
        docInteractive.chart(
            "doc-example/timeline-dynamic-series",
            "时间轴：切换系列数量",
            "Timeline: dynamic series",
        ) {
            unsafeJso<EChartsOption> {
                baseOption =
                    unsafeJso {
                        timeline =
                            unsafeJso<TimelineOption> {
                                axisType = "category"
                                autoPlay = false
                                playInterval = 1000.0
                                controlStyle = unsafeJso<TimelineControlStyle> { playIcon = PLAY_ICON }
                                // Without it a point with fewer series would merge into the previous
                                // one instead of dropping the extra series.
                                replaceMerge = StringOrStringArray("series")
                                data = strings("2 series", "3 series", "1 series")
                            }
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    trigger = TooltipTriggers.axis
                                    axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                                },
                            )
                        legend = one(unsafeJso<LegendOption> {})
                        grid =
                            one(
                                unsafeJso<GridOption> {
                                    top = StringOrDouble(80.0)
                                    bottom = StringOrDouble(100.0)
                                },
                            )
                        toolbox =
                            one(
                                unsafeJso<ToolboxOption> {
                                    left = StringOrDouble("center")
                                    top = StringOrDouble(30.0)
                                    feature =
                                        unsafeJso<ToolboxFeature> {
                                            dataZoom = unsafeJso<ToolboxFeatureOption> {}
                                        }
                                },
                            )
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.category
                                    data = strings("CityB", "CityT", "CityH", "CityS")
                                    splitLine = unsafeJso<SplitLineOption> { show = false }
                                },
                            )
                        yAxis =
                            one(
                                unsafeJso<YAxisOption> {
                                    type = AxisTypes.value
                                    name = "GDP"
                                },
                            )
                        series = arrayOf()
                    }
                options =
                    arrayOf(
                        unsafeJso {
                            series =
                                arrayOf<SeriesOption>(
                                    gdpBar("a", doubleArrayOf(12.0, 33.0, 44.0, 55.0)),
                                    gdpBar("b", doubleArrayOf(55.0, 66.0, 77.0, 88.0)),
                                )
                        },
                        unsafeJso {
                            series =
                                arrayOf<SeriesOption>(
                                    gdpBar("a", doubleArrayOf(22.0, 33.0, 44.0, 55.0)),
                                    gdpBar("b", doubleArrayOf(55.0, 66.0, 77.0, 88.0)),
                                    gdpBar("c", doubleArrayOf(55.0, 66.0, 77.0, 88.0)),
                                )
                        },
                        unsafeJso {
                            series =
                                arrayOf<SeriesOption>(
                                    gdpBar("b", doubleArrayOf(55.0, 66.0, 77.0, 88.0)),
                                )
                        },
                    )
            }
        },
    )
