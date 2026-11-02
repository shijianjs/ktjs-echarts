package demo.examples.scatter

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.sqrt
import web.cssom.FontWeight
import zrender.ZRFontWeight
import demo.examples.gauge.center
import demo.examples.gl.square
import demo.examples.matrix.dimension
import demo.examples.shared.axisLabel
import demo.examples.shared.numberAt
import demo.examples.shared.numeric
import demo.examples.shared.row
import demo.examples.shared.textAt
import demo.examples.shared.textOf

/** Shape of `life-expectancy.json`: the years, the countries and one table per year. */
internal external interface LifeTimelineTable {
    val counties: Array<String>
    val timeline: Array<JsAny>
    val series: Array<Array<Array<JsAny>>>
}

/** The two sizes of `symbolSize`: the square root of the population, scaled by 80. */
internal fun lifeTimelineSymbolSize(): SymbolSize =
    SymbolSize { value, _ ->
        DoubleOrDoubleArray((sqrt(numberAt(value, 2) / 5e8) + 0.1) * 80)
    }

/** One row `[income, life expectancy, population, country]` of a year. */
internal fun lifeTimelineItem(row: Array<JsAny>): ScatterDataItem =
    ScatterDataItem(
        row(
            numberAt(row, 0),
            numberAt(row, 1),
            numberAt(row, 2),
            textAt(row, 3),
        ),
    )

/** The items of one year of [LifeTimelineTable.series]. */
internal fun lifeTimelineItems(rows: Array<Array<JsAny>>): Array<ScatterDataItem?> =
    Array(rows.size) { index -> lifeTimelineItem(rows[index]) }

/** The tooltip of the page: the four schema entries of the upstream file. */
internal fun lifeTimelineTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val value = params.unsafeCast<CallbackDataParams>().value
            TooltipContent(
                "国家：" + textAt(value, 3) + "<br>" +
                    "人均寿命：" + textAt(value, 1) + "岁<br>" +
                    "人均收入：" + textAt(value, 0) + "美元<br>" +
                    "总人口：" + textAt(value, 2) + "<br>",
            )
        },
    )

/** The ten country colours, repeated twice, exactly as the upstream concatenation produces them. */
internal fun lifeTimelineColors(): Array<String> {
    val colors =
        arrayOf(
            "#51689b", "#ce5c5c", "#fbc357", "#8fbf8f", "#659d84",
            "#fb8e6a", "#c77288", "#786090", "#91c4c5", "#6890ba",
        )
    return colors + colors
}

/** One scatter series of the timeline, named after its year. */
internal fun lifeTimelineSeries(name: String, rows: Array<Array<JsAny>>): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
        typedData = lifeTimelineItems(rows)
        symbolSize = lifeTimelineSymbolSize()
    }

/** The option of the whole page: the shared base option plus one option per year. */
internal fun lifeTimelineOption(table: LifeTimelineTable): EChartsOption {
    val years = Array(table.timeline.size) { index -> textOf(table.timeline[index]) }
    val firstYear = years.firstOrNull().orEmpty()
    return unsafeJso {
        baseOption =
            unsafeJso<EChartsOption> {
                timeline =
                    unsafeJso<TimelineOption> {
                        axisType = "category"
                        orient = Orients.vertical
                        autoPlay = true
                        inverse = true
                        playInterval = 1000.0
                        left = null
                        right = StringOrDouble(0.0)
                        top = StringOrDouble(20.0)
                        bottom = StringOrDouble(20.0)
                        width = StringOrDouble(55.0)
                        height = null
                        symbol = "none"
                        checkpointStyle = unsafeJso<TimelineCheckpointStyle> { borderWidth = 2.0 }
                        controlStyle =
                            unsafeJso<TimelineControlStyle> {
                                showNextBtn = false
                                showPrevBtn = false
                            }
                        data = numbers(*DoubleArray(table.timeline.size) { numeric(table.timeline[it]) })
                    }
                title =
                    arrayOf(
                        unsafeJso<TitleOption> {
                            text = firstYear
                            textAlign = "center"
                            left = StringOrDouble("63%")
                            top = StringOrDouble("55%")
                            textStyle = unsafeJso<TextStyleOption> { fontSize = StringOrDouble(100.0) }
                        },
                        unsafeJso<TitleOption> {
                            text = "各国人均寿命与GDP关系演变"
                            left = StringOrDouble("center")
                            top = StringOrDouble(10.0)
                            textStyle =
                                unsafeJso<TextStyleOption> {
                                    fontWeight = ZRFontWeight(FontWeight.normal)
                                    fontSize = StringOrDouble(20.0)
                                }
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            padding = DoubleOrDoubleArray(5.0)
                            borderWidth = 1.0
                            formatter = lifeTimelineTooltip()
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            top = StringOrDouble(100.0)
                            containLabel = true
                            left = StringOrDouble(30.0)
                            right = StringOrDouble(110.0)
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.log
                            name = "人均收入"
                            max = AxisMinMax(100000.0)
                            min = AxisMinMax(300.0)
                            nameGap = 25.0
                            nameLocation = AxisNameLocations.middle
                            nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(18.0) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} $") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "平均寿命"
                            max = AxisMinMax(100.0)
                            nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(18.0) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} 岁") }
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            dimension = 3.0
                            categories = strings<OptionDataItem>(*table.counties).unsafeCast<JsAny>()
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color = StringOrStringArray(lifeTimelineColors())
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lifeTimelineSeries(firstYear, table.series.firstOrNull() ?: emptyArray()),
                    )
                animationDurationUpdate = AnimationDuration(1000.0)
                animationEasingUpdate = AnimationEasings.quinticInOut
            }
        options =
            Array(table.timeline.size) { index ->
                unsafeJso<EChartsOption> {
                    title =
                        one(
                            unsafeJso<TitleOption> {
                                show = true
                                text = years[index]
                            },
                        )
                    series = arrayOf<SeriesOption>(lifeTimelineSeries(years[index], table.series[index]))
                }
            }
    }
}
