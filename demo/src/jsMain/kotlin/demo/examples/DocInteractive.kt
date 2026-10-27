package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.math.round
import zrender.ElementTextConfig
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
 * - `bar-dataZoom-filterMode` and `candlestick-axisPointer` ask for their dataset with `$.get` on the
 *   official site; both files are mirrored under `resources/data/` and loaded with `fetch`, so the
 *   `showLoading` / `hideLoading` pair of the originals is kept. `candlestick-axisPointer` also calls
 *   `splitData`, which mutates the rows (`splice`), so the columns are read by index here.
 * - the tooltip of `candlestick-axisPointer` is an HTML block upstream, with an `<hr>` between the
 *   date and the prices; the rows are kept and the `<hr>` dropped.
 * - `grid-outerBounds` is driven by a floating control panel upstream. This site has no panel, so the
 *   port runs with the defaults of that panel, and the two tutorial rectangles are repainted from the
 *   container size through the holder's `onResize` instead of the panel's `onChange` callback.
 * - `mix-timeline-all` keeps the legacy `calculable: true` out: ECharts 6 removed the flag, the data
 *   zoom it once implied is not part of the page, and the macro-economic table is inlined in
 *   `MixTimelineData.kt` instead of being fetched, because the site ships no `ROOT_PATH`.
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
                                            dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> {}
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
        docInteractive.interactive(
            "doc-example/bar-dataZoom-filterMode",
            "数据缩放过滤模式（filterMode）",
            "Bar with a vertical data zoom and filterMode",
        ) { chart, _ ->
            chart.showLoading(null)
            loadJson("data/obama_budget_proposal_2012.list.json").then { json ->
                chart.hideLoading()
                chart.setOption(budgetOption(json.unsafeCast<ObamaBudgetJson>()))
            }
        },
        docInteractive.interactive(
            "doc-example/candlestick-axisPointer",
            "K 线与联动的 axisPointer",
            "Candlestick with a linked axis pointer",
        ) { chart, _ ->
            chart.showLoading(null)
            loadJson("data/stock-DJI.json").then { json ->
                chart.hideLoading()
                chart.setOption(stockOption(json.unsafeCast<Array<Array<JsAny>>>()), true)
            }
        },
        docInteractive.interactive(
            "doc-example/grid-outerBounds",
            "网格外边界（grid.outerBounds）",
            "Grid outerBounds and its indicator rectangles",
        ) { chart, holder ->
            // The two tutorial rectangles are pixel-sized, so they follow the container.
            fun repaint() {
                chart.setOption(outerBoundsOption(chart.getWidth(), chart.getHeight()))
            }
            repaint()
            holder.onResize(::repaint)
        },
        docInteractive.chart(
            "doc-example/mix-timeline-all",
            "时间轴：全国宏观经济指标",
            "Timeline of the national macro-economic indicators",
        ) {
            unsafeJso {
                baseOption = mixTimelineBaseOption()
                options = Array(mixTimelineYears.size) { index -> mixTimelineYearOption(index) }
            }
        },
    )

/**
 * The `baseOption` of `doc-example/mix-timeline-all`.
 *
 * It declares the timeline axis, the axes, the legend and the seven series *names*; the data of every
 * series arrives through one option per year in [mixTimelineYearOption], merged by index.
 */
private fun mixTimelineBaseOption(): EChartsOption =
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
private fun mixTimelineYearOption(index: Int): EChartsOption =
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
private fun mixTimelineBar(name: String): SeriesOption = barSeries { this.name = name }

/**
 * A bar series of one year: the values of [values] as named items, which is what upstream's
 * `dataFormatter` produces so a tooltip and the labels know the province.
 */
private fun mixTimelineBarData(values: DoubleArray): SeriesOption =
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
private fun mixTimelinePoints(): Array<OptionDataItem?> =
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
private fun mixTimelineDiamond(
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
private fun mixTimelineAxisCategories(): Array<String> =
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
private fun mixTimelineYearLabel(): LabelFormatterCallback =
    js("(value) => String(new Date(value).getFullYear())") as LabelFormatterCallback

/** Shape of `data/obama_budget_proposal_2012.list.json`. */
private external interface ObamaBudgetJson {
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
private fun budgetOption(budget: ObamaBudgetJson): EChartsOption =
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
private fun budgetAxisLabel(): AxisLabelOption =
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
private fun budgetDataZoom(): Array<DataZoomOption> =
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
private external interface AxisPointerLabelParams {
    val value: JsAny?
    val seriesData: Array<AxisPointerSeriesData>?
}

/** One series under the axis pointer, as read by [AxisPointerLabelParams.seriesData]. */
private external interface AxisPointerSeriesData {
    val value: JsAny?
}

/** Columns of a candle after the date of a `data/stock-DJI.json` row. */
private const val STOCK_CANDLE_COLUMNS = 5

/** Column of the volume in a row of `data/stock-DJI.json`. */
private const val STOCK_VOLUME_COLUMN = 5

/** Column of the closing price, the input of the moving averages. */
private const val STOCK_CLOSE_COLUMN = 2

/** Option of `doc-example/candlestick-axisPointer`, built once the quotes have loaded. */
private fun stockOption(rows: Array<Array<JsAny>>): EChartsOption =
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
private fun stockTooltip(): TooltipOption =
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
private fun stockAxisPointer(): AxisPointerOption =
    unsafeJso {
        link = one(unsafeJso<AxisPointerLink> { xAxisIndex = ModelFinderIndexQuery("all") })
        label = unsafeJso<AxisPointerLabelOption> { backgroundColor = ZRColor("#777") }
    }

/** Zoom and brush buttons; the brush offers only the horizontal line and clearing. */
private fun stockToolbox(): ToolboxOption =
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
private fun stockBrush(): BrushOption =
    unsafeJso {
        xAxisIndex = ModelFinderIndexQuery("all")
        brushLink = ModelFinderIndexQuery("all")
        outOfBrush = unsafeJso<VisualOptionUnit> { colorAlpha = DoubleOrDoubleArray(0.1) }
    }

/** The two category axes: the price grid and, below it, the volume grid. */
private fun stockAxes(dates: Array<OptionDataItem?>): Array<XAxisOption> =
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
private fun stockPointerLabel(): LabelFormatter =
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
private fun stockValueAxes(): Array<YAxisOption> =
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
private fun stockDataZoom(): Array<DataZoomOption> =
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
private fun stockSeries(rows: Array<Array<JsAny>>): Array<SeriesOption> {
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
private fun stockCandleTooltip(): TooltipFormatter =
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
private fun stockDates(rows: Array<Array<JsAny>>): Array<OptionDataItem?> =
    strings(*Array(rows.size) { textOf(rows[it][0]) })

/** The five columns that follow the date, in the order ECharts expects a candle. */
private fun stockCandles(rows: Array<Array<JsAny>>): Array<CandlestickDataItem?> =
    Array(rows.size) { row ->
        CandlestickDataItem(
            Array(STOCK_CANDLE_COLUMNS) { column -> OptionDataValue(numeric(rows[row][column + 1])) },
        )
    }

/** Moving averages of the closing prices, in the order of the legend. */
private fun stockAverageLines(closes: DoubleArray): Array<SeriesOption> =
    arrayOf<SeriesOption>(
        stockAverageLine("MA5", movingAverage(5, closes)),
        stockAverageLine("MA10", movingAverage(10, closes)),
        stockAverageLine("MA20", movingAverage(20, closes)),
        stockAverageLine("MA30", movingAverage(30, closes)),
    )

private fun stockAverageLine(seriesName: String, data: Array<LineDataItem?>): SeriesOption =
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
private fun movingAverage(dayCount: Int, closes: DoubleArray): Array<LineDataItem?> =
    Array(closes.size) { index ->
        if (index < dayCount) {
            LineDataItem(OptionDataValue("-"))
        } else {
            val sum = (0 until dayCount).sumOf { offset -> closes[index - offset] }
            LineDataItem(OptionDataValue(round(sum / dayCount * 1000.0) / 1000.0))
        }
    }

/** `grid.left` of the control panel of `doc-example/grid-outerBounds`, kept by this port. */
private const val OUTER_BOUNDS_GRID_LEFT = 70.0

/** `grid.top` of that panel. */
private const val OUTER_BOUNDS_GRID_TOP = 50.0

/** `grid.right` of that panel. */
private const val OUTER_BOUNDS_GRID_RIGHT = 260.0

/** `grid.bottom` of that panel. */
private const val OUTER_BOUNDS_GRID_BOTTOM = 45.0

/** The jitter of the `medium_data` set: five points, `y += random * 1000`. */
private val OUTER_BOUNDS_JITTER = doubleArrayOf(0.79, 0.03, 0.25, 0.68, 0.33)

/** Name of an axis, spelled the way the original builds it (`xAxis_0name`). */
private fun outerBoundsAxisName(
    axis: String,
    index: Int,
): String = "${axis}Axis_${index}name"

/** `[x, y]` points of the medium data set: `100000 + i * 10000`, with the jitter added to `y`. */
private fun outerBoundsData(): Array<LineDataItem?> =
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
private fun outerBoundsOption(
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
private fun outerBoundsGrid(): GridOption =
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
private fun outerBoundsIndicators(
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
private fun indicatorRectangle(
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
