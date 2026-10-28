package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.DataZoomTypes
import echarts.VisualProps
import echarts.CustomSeriesRenderItemAPI
import echarts.CustomSeriesRenderItemParams
import echarts.options.BooleanOrStringArray
import echarts.options.CoordinateSystemDataCoord
import echarts.options.CustomPathOption
import echarts.options.DataZoomOption
import echarts.options.DimensionLoose
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.LabelFormatter
import echarts.options.LegendOption
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.OptionEncodeValue
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.customSeries
import echarts.options.one
import echarts.options.points
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * Custom series pages that build their geometry in `renderItem`.
 *
 * `cycle-plot` draws one small trend line per month: the x axis is the month, and every year of the
 * table becomes a polyline shifted inside the category band, so the years of one month are read
 * side by side. The average series is the horizontal rule in the middle of each band.
 *
 * Both `renderItem`s set `transition: ['shape']`, which upstream uses to morph the lines when the data
 * changes; the option here is set once, so the property only has to survive the merge.
 */

private val customLongTail = Category(Groups.CUSTOM, "examples/CustomLongTail.kt")

/** The monthly sales table upstream inlines: the year, then one value per month. */
private val CYCLE_TABLE =
    arrayOf(
        doubleArrayOf(2002.0, 14.0, 21.0, 25.0, 21.0, 26.0, 32.0, 27.0, 20.0, 10.0, 11.0, 5.0, 5.0),
        doubleArrayOf(2003.0, 18.0, 24.0, 28.0, 24.0, 33.0, 37.0, 30.0, 25.0, 13.0, 14.0, 6.0, 6.0),
        doubleArrayOf(2004.0, 22.0, 31.0, 36.0, 28.0, 37.0, 43.0, 35.0, 30.0, 13.0, 13.0, 7.0, 7.0),
        doubleArrayOf(2005.0, 25.0, 32.0, 38.0, 34.0, 39.0, 48.0, 38.0, 29.0, 14.0, 14.0, 8.0, 8.0),
        doubleArrayOf(2006.0, 29.0, 38.0, 47.0, 33.0, 44.0, 57.0, 41.0, 39.0, 16.0, 16.0, 9.0, 8.0),
        doubleArrayOf(2007.0, 29.0, 35.0, 49.0, 34.0, 43.0, 57.0, 41.0, 37.0, 20.0, 17.0, 9.0, 10.0),
        doubleArrayOf(2008.0, 22.0, 32.0, 37.0, 30.0, 35.0, 44.0, 38.0, 31.0, 16.0, 17.0, 8.0, 7.0),
        doubleArrayOf(2009.0, 25.0, 34.0, 41.0, 33.0, 39.0, 47.0, 44.0, 32.0, 17.0, 17.0, 9.0, 8.0),
        doubleArrayOf(2010.0, 26.0, 35.0, 46.0, 40.0, 47.0, 61.0, 47.0, 41.0, 20.0, 18.0, 9.0, 10.0),
        doubleArrayOf(2011.0, 29.0, 39.0, 55.0, 38.0, 55.0, 67.0, 53.0, 41.0, 19.0, 20.0, 11.0, 11.0),
        doubleArrayOf(2012.0, 38.0, 48.0, 60.0, 49.0, 57.0, 79.0, 62.0, 54.0, 26.0, 26.0, 13.0, 11.0),
    )

private val CYCLE_MONTHS =
    arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

/**
 * The table turned on its side: `[month, one value per year...]`.
 *
 * This is what the trend series consumes, so `renderItem` can read the years of one month out of a
 * single data item.
 */
private val CYCLE_BY_MONTH: Array<DoubleArray> =
    Array(CYCLE_MONTHS.size) { month ->
        DoubleArray(CYCLE_TABLE.size + 1) { position ->
            if (position == 0) month.toDouble() else CYCLE_TABLE[position - 1][month + 1]
        }
    }

/** `[month, mean of the years]`, the second series of the page. */
private val CYCLE_AVERAGES: Array<DoubleArray> =
    CYCLE_BY_MONTH.map { row ->
        val total = row.drop(1).sum()
        doubleArrayOf(row[0], total / (row.size - 1))
    }.toTypedArray()

/** `api.size([0, 0])[0]`, the width of one category band in pixels. */
private fun bandWidth(api: CustomSeriesRenderItemAPI): Double =
    numberArray(api.size(OptionDataValueOrArray(arrayOf(OptionDataValue(0.0), OptionDataValue(0.0)))))[0]

private fun strokedStyle(api: CustomSeriesRenderItemAPI): ZRStyleProps =
    api.style(
        unsafeJso<ZRStyleProps> {
            // Upstream clears the fill so only the outline of each trend line is drawn.
            fill = null
            stroke = api.visual(VisualProps.color).unsafeCast<ZRColor>()
            lineWidth = 2.0
        },
    )

/** One trend line per year, shifted inside the band of the month. */
private fun cycleTrendItem(
    params: CustomSeriesRenderItemParams,
    api: CustomSeriesRenderItemAPI,
): CustomPathOption {
    val categoryIndex = api.value(DimensionLoose(0.0))
    val unitBandWidth = bandWidth(api) * 0.85 / (CYCLE_TABLE.size - 1)
    val trendPoints =
        Array(CYCLE_TABLE.size) { year ->
            val value = api.value(DimensionLoose((year + 1).toDouble()))
            val point =
                api.coord(
                    CoordinateSystemDataCoord(arrayOf<OptionDataValue?>(categoryIndex, value)),
                )
            arrayOf(point[0] + unitBandWidth * (year - CYCLE_TABLE.size / 2.0), point[1])
        }
    return unsafeJso<CustomPathOption> {
        type = "polyline"
        transition = StringOrStringArray(arrayOf("shape"))
        shape = unsafeJso<ZRShapeProps> { points = trendPoints }
        style = strokedStyle(api)
    }
}

/** The average rule of one month. */
private fun cycleAverageItem(
    params: CustomSeriesRenderItemParams,
    api: CustomSeriesRenderItemAPI,
): CustomPathOption {
    val bandWidthPx = bandWidth(api) * 0.85
    val point =
        api.coord(
            CoordinateSystemDataCoord(
                arrayOf<OptionDataValue?>(api.value(DimensionLoose(0.0)), api.value(DimensionLoose(1.0))),
            ),
        )
    return unsafeJso<CustomPathOption> {
        type = "line"
        transition = StringOrStringArray(arrayOf("shape"))
        shape =
            unsafeJso<ZRShapeProps> {
                x1 = point[0] - bandWidthPx / 2
                x2 = point[0] + bandWidthPx / 2
                y1 = point[1]
                y2 = point[1]
            }
        style = strokedStyle(api)
    }
}

/** The cycle plot option: months on x, one polyline per year inside each band. */
private fun cyclePlotOption(): EChartsOption =
    unsafeJso<EChartsOption> {
        tooltip = one(unsafeJso<TooltipOption> {})
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "Sales Trends by Year within Each Month"
                    subtext = "Sample of Cycle Plot"
                    left = StringOrDouble("center")
                },
            )
        legend =
            one(
                unsafeJso<LegendOption> {
                    top = StringOrDouble(70.0)
                    data = legendNames("Trend by year (2002 - 2012)", "Average")
                },
            )
        dataZoom =
            arrayOf(
                unsafeJso<DataZoomOption> {
                    type = DataZoomTypes.slider
                    labelFormatter = LabelFormatter("")
                },
                unsafeJso<DataZoomOption> { type = DataZoomTypes.inside },
            )
        grid =
            one(
                unsafeJso<GridOption> {
                    bottom = StringOrDouble(70.0)
                    top = StringOrDouble(120.0)
                },
            )
        xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings(*CYCLE_MONTHS) })
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    type = AxisTypes.value
                    boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.0), StringOrDouble("20%")))
                },
            )
        series =
            arrayOf<SeriesOption>(
                customSeries {
                    name = "Average"
                    renderItem = { itemParams, api -> cycleAverageItem(itemParams, api) }
                    encode =
                        unsafeJso {
                            x = OptionEncodeValue(DimensionLoose(0.0))
                            y = OptionEncodeValue(DimensionLoose(1.0))
                        }
                    typedData = points(*CYCLE_AVERAGES)
                },
                customSeries {
                    name = "Trend by year (2002 - 2012)"
                    renderItem = { itemParams, api -> cycleTrendItem(itemParams, api) }
                    encode =
                        unsafeJso {
                            x = OptionEncodeValue(DimensionLoose(0.0))
                            y =
                                OptionEncodeValue(
                                    Array(CYCLE_TABLE.size) { year -> DimensionLoose((year + 1).toDouble()) },
                                )
                        }
                    typedData = points(*CYCLE_BY_MONTH)
                },
            )
    }

/** Entries of this file, in sidebar order. */
val customLongTailEntries: List<CatalogEntry> =
    listOf(
        customLongTail.chart(
            "cycle-plot",
            "周期图",
            "Cycle Plot",
            summaryZh = "x 轴是月份，每年在各自的月份带里画一条折线，用来比较同一个月在不同年份的走势。",
            summaryEn = "Months on the x axis with one trend line per year inside each band, so a month can be compared across years.",
        ) {
            cyclePlotOption()
        },
    )
