package demo.examples.custom

import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.DataZoomTypes
import io.github.shijianjs.ktjs.echarts.VisualProps
import io.github.shijianjs.ktjs.echarts.CustomSeriesRenderItemAPI
import io.github.shijianjs.ktjs.echarts.CustomSeriesRenderItemParams
import io.github.shijianjs.ktjs.echarts.options.BooleanOrStringArray
import io.github.shijianjs.ktjs.echarts.options.CoordinateSystemDataCoord
import io.github.shijianjs.ktjs.echarts.options.CustomPathOption
import io.github.shijianjs.ktjs.echarts.options.DataZoomOption
import io.github.shijianjs.ktjs.echarts.options.DimensionLoose
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.LabelFormatter
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.OptionDataValueOrArray
import io.github.shijianjs.ktjs.echarts.options.OptionEncodeValue
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.customSeries
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.points
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRShapeProps
import io.github.shijianjs.ktjs.zrender.ZRStyleProps
import demo.examples.gauge.center
import demo.examples.scatter.point
import demo.examples.shared.legendNames
import demo.examples.shared.numberArray
import demo.examples.shared.row

/** The monthly sales table upstream inlines: the year, then one value per month. */
internal val CYCLE_TABLE =
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

internal val CYCLE_MONTHS =
    arrayOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

/**
 * The table turned on its side: `[month, one value per year...]`.
 *
 * This is what the trend series consumes, so `renderItem` can read the years of one month out of a
 * single data item.
 */
internal val CYCLE_BY_MONTH: Array<DoubleArray> =
    Array(CYCLE_MONTHS.size) { month ->
        DoubleArray(CYCLE_TABLE.size + 1) { position ->
            if (position == 0) month.toDouble() else CYCLE_TABLE[position - 1][month + 1]
        }
    }

/** `[month, mean of the years]`, the second series of the page. */
internal val CYCLE_AVERAGES: Array<DoubleArray> =
    CYCLE_BY_MONTH.map { row ->
        val total = row.drop(1).sum()
        doubleArrayOf(row[0], total / (row.size - 1))
    }.toTypedArray()

/** `api.size([0, 0])[0]`, the width of one category band in pixels. */
internal fun bandWidth(api: CustomSeriesRenderItemAPI): Double =
    numberArray(api.size(OptionDataValueOrArray(arrayOf(OptionDataValue(0.0), OptionDataValue(0.0)))))[0]

internal fun strokedStyle(api: CustomSeriesRenderItemAPI): ZRStyleProps =
    api.style(
        unsafeJso<ZRStyleProps> {
            // Upstream clears the fill so only the outline of each trend line is drawn.
            fill = null
            stroke = api.visual(VisualProps.color).unsafeCast<ZRColor>()
            lineWidth = 2.0
        },
    )

/** One trend line per year, shifted inside the band of the month. */
internal fun cycleTrendItem(
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
internal fun cycleAverageItem(
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
internal fun cyclePlotOption(): EChartsOption =
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
