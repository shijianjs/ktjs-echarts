package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.AxisNameLocations
import echarts.AxisTypes
import echarts.CustomSeriesRenderItemAPI
import echarts.ECharts
import echarts.Orients
import echarts.TooltipTriggers
import echarts.VisualMapTypes
import echarts.options.AreaStyleOption
import echarts.options.AxisLabelOption
import echarts.options.AxisLineOption
import echarts.options.AxisMinMax
import echarts.options.BooleanOrDouble
import echarts.options.BooleanOrString
import echarts.options.CallbackDataParams
import echarts.options.CoordinateSystemDataCoord
import echarts.options.CustomGroupOption
import echarts.options.CustomImageOption
import echarts.options.CustomPathOption
import echarts.options.CustomRootElementOption
import echarts.options.CustomSeriesFontOption
import echarts.options.CustomSeriesOption
import echarts.options.CustomTextOption
import echarts.options.DataZoomOption
import echarts.options.DimensionLoose
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.GridOption
import echarts.options.IndexOrIndexArray
import echarts.options.ItemStyleOption
import echarts.options.LineDataItem
import echarts.options.OptionDataItem
import echarts.options.OptionDataValue
import echarts.options.OptionEncode
import echarts.options.OptionEncodeValue
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.TitleOption
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.TooltipContent
import echarts.options.VisualMapOption
import echarts.options.VisualPiece
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.customSeries
import echarts.options.lineSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.unsafeCast
import kotlin.math.PI
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.StringOrImageLike
import zrender.ZRColor
import zrender.ZRLineTypes
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import zrender.gradientColorStop
import zrender.linearGradient

/*
 * `wind-barb` of the official example gallery.
 *
 * The page puts four series on one time axis: the wave height as an area line on the second axis,
 * the wind speed as a dotted line plus one rotated arrow per reading, and the daily forecast as a
 * weather icon with the temperature range on a hidden third axis. The arrow and the forecast icon
 * are custom series elements.
 *
 * Two differences against the original:
 *
 * - the time strings of `wind-barb-hobart.json` are turned into timestamps while loading, so the
 *   tooltip formatter and the `api.coord` of the forecast series see numbers; upstream adds a day to
 *   the raw date string and relies on the time axis reparsing the result.
 * - the weather icons are shipped under `data/img/weather/` (see `SOURCE.md`), the path the other
 *   rich-text pages already use.
 */

private val windBarb = Category(Groups.CUSTOM, "examples/WindBarb.kt")

/** One reading of the `data` table: `[time, windSpeed, direction, waveHeight]`. */
private external interface WindReading {
    /** ISO timestamp of the reading. */
    var time: String

    /** Wind speed in knots. */
    var windSpeed: Double

    /** Compass direction, e.g. `NNW`. */
    var R: String

    /** Wave height in metres. */
    var waveHeight: Double
}

/** One day of the `forecast` table. */
private external interface WindForecast {
    /** Local date of the forecast. */
    var localDate: String

    /** Icon key of the sky, e.g. `Cloudy`. */
    var skyIcon: String

    /** Lowest temperature of the day. */
    var minTemp: Double

    /** Highest temperature of the day. */
    var maxTemp: Double
}

/** Shape of `wind-barb-hobart.json`. */
private external interface WindBarbJson {
    /** Hourly readings. */
    var data: Array<WindReading>

    /** Daily forecast. */
    var forecast: Array<WindForecast>
}

/** The eight-point compass the `R` column uses, in the order of the upstream angle table. */
private val WIND_DIRECTIONS =
    arrayOf("W", "WSW", "SW", "SSW", "S", "SSE", "SE", "ESE", "E", "ENE", "NE", "NNE", "N", "NNW", "NW", "WNW")

/** Rotation of one arrow, in radians; the index of the direction in [WIND_DIRECTIONS] times `π/8`. */
private fun windAngle(direction: String): Double {
    val index = WIND_DIRECTIONS.indexOf(direction)
    return if (index < 0) 0.0 else PI / 8 * index
}

/** The columns of the readings, by the names the option encodes. */
private object WindDims {
    const val TIME = 0
    const val WIND_SPEED = 1
    const val DIRECTION = 2
    const val WAVE_HEIGHT = 3

    const val MIN_TEMP = 3
    const val MAX_TEMP = 4
}

/** Side of the square the arrow is drawn in. */
private const val ARROW_SIZE = 18.0

/** Side of the square the weather icon is drawn in. */
private const val WEATHER_ICON_SIZE = 45.0

/** One day in milliseconds. */
private const val ONE_DAY = 3600.0 * 24 * 1000

/** Icon of the `skyIcon` key; the files are the 128 px weather icons of the upstream site. */
private fun weatherIcon(skyIcon: String): String = "data/img/weather/" + skyIcon.lowercase() + "_128.png"

/** A reading as a `[time, windSpeed, direction, waveHeight]` row on the time axis. */
private fun readingRow(reading: WindReading): LineDataItem =
    LineDataItem(row(Date(reading.time).getTime(), reading.windSpeed, reading.R, reading.waveHeight))

/** A forecast day as a `[time, 0, icon, minTemp, maxTemp]` row of the hidden third axis. */
private fun forecastRow(forecast: WindForecast): LineDataItem =
    LineDataItem(
        row(Date(forecast.localDate).getTime(), 0.0, weatherIcon(forecast.skyIcon), forecast.minTemp, forecast.maxTemp),
    )

/** The readings of [json] as time-axis rows. */
private fun readingRows(json: WindBarbJson): Array<LineDataItem?> =
    Array(json.data.size) { index -> readingRow(json.data[index]) }

/** The forecast days of [json] as time-axis rows. */
private fun forecastRows(json: WindBarbJson): Array<LineDataItem?> =
    Array(json.forecast.size) { index -> forecastRow(json.forecast[index]) }

/** `api.value(dimension)` as a number. */
private fun customNumber(api: CustomSeriesRenderItemAPI, dimension: Int): Double =
    numeric(api.value(DimensionLoose(dimension.toDouble())))

/** `api.value(dimension)` as text. */
private fun customText(api: CustomSeriesRenderItemAPI, dimension: Int): String =
    textOf(api.value(DimensionLoose(dimension.toDouble())))

/**
 * One arrow of the wind-speed series: a path rotated to the direction of the reading and centred on
 * its `[time, windSpeed]` point.
 */
private fun windArrowItem(api: CustomSeriesRenderItemAPI): CustomRootElementOption? {
    val point =
        api.coord(
            CoordinateSystemDataCoord(
                arrayOf(
                    OptionDataValue(customNumber(api, WindDims.TIME)),
                    OptionDataValue(customNumber(api, WindDims.WIND_SPEED)),
                ),
            ),
        )
    val angle = windAngle(customText(api, WindDims.DIRECTION))
    return unsafeJso<CustomPathOption> {
        type = "path"
        shape =
            unsafeJso<ZRShapeProps> {
                pathData = "M31 16l-15-15v9h-26v12h26v9z"
                x = -ARROW_SIZE / 2
                y = -ARROW_SIZE / 2
                width = ARROW_SIZE
                height = ARROW_SIZE
            }
        rotation = angle
        x = point[0]
        y = point[1]
        style =
            api.style(
                unsafeJso<ZRStyleProps> {
                    stroke = ZRColor("#555")
                    lineWidth = 1.0
                },
            )
    }
}

/**
 * One forecast day: a group with the weather icon and the temperature range above it.
 *
 * The group ignores the value of the third axis; the icon is centred on the middle of the day and
 * parked at a fixed height, the same `y` values as the original.
 */
private fun weatherForecastItem(api: CustomSeriesRenderItemAPI): CustomRootElementOption? {
    val point =
        api.coord(
            CoordinateSystemDataCoord(
                arrayOf(
                    OptionDataValue(customNumber(api, WindDims.TIME) + ONE_DAY / 2),
                    OptionDataValue(0.0),
                ),
            ),
        )
    return unsafeJso<CustomGroupOption> {
        type = "group"
        children =
            arrayOf(
                unsafeJso<CustomImageOption> {
                    type = "image"
                    style =
                        unsafeJso<ZRStyleProps> {
                            image = StringOrImageLike(customText(api, WindDims.DIRECTION))
                            x = -WEATHER_ICON_SIZE / 2
                            y = -WEATHER_ICON_SIZE / 2
                            width = StringOrDouble(WEATHER_ICON_SIZE)
                            height = StringOrDouble(WEATHER_ICON_SIZE)
                        }
                    x = point[0]
                    y = 110.0
                },
                unsafeJso<CustomTextOption> {
                    type = "text"
                    style =
                        unsafeJso<ZRStyleProps> {
                            text =
                                fixed(customNumber(api, WindDims.MIN_TEMP), 0) + " - " +
                                    fixed(customNumber(api, WindDims.MAX_TEMP), 0) + "°"
                            font = api.font(unsafeJso<CustomSeriesFontOption> { fontSize = StringOrDouble(14.0) })
                            align = TextAlign.center
                            verticalAlign = VerticalAlign.bottom
                        }
                    x = point[0]
                    y = 80.0
                },
            )
    }
}

/** The tooltip of the axis trigger: the date, the speed, the direction and the wave height. */
private fun windBarbTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val item = params.unsafeCast<Array<CallbackDataParams>>()[0]
            val date = ECharts.format.formatTime("yyyy-MM-dd", numberAt(item.value, WindDims.TIME))
            val clock = ECharts.format.formatTime("hh:mm", numberAt(item.value, WindDims.TIME))
            TooltipContent(
                listOf(
                    "$date $clock",
                    "风速：" + numberAt(item.value, WindDims.WIND_SPEED),
                    "风向：" + textAt(item.value, WindDims.DIRECTION),
                    "浪高：" + numberAt(item.value, WindDims.WAVE_HEIGHT),
                ).joinToString("<br>"),
            )
        },
    )

/** The tooltip of one forecast icon. */
private fun weatherTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val item = params.unsafeCast<CallbackDataParams>()
            TooltipContent(
                ECharts.format.formatTime("yyyy-MM-dd", numberAt(item.value, WindDims.TIME)) + ": " +
                    numberAt(item.value, WindDims.MIN_TEMP) +
                    " - " +
                    numberAt(item.value, WindDims.MAX_TEMP) +
                    "°",
            )
        },
    )

/** One of the three pieces of the wind-speed visual map. */
private fun windPiece(label: String, color: String, gte: Double? = null, lt: Double? = null): VisualPiece =
    unsafeJso {
        this.label = label
        this.color = StringOrStringArray(color)
        if (gte != null) this.gte = gte
        if (lt != null) this.lt = lt
    }

/** The option of the page; [readings] and [forecast] are the two rows of the table. */
private fun windBarbOption(
    readings: Array<LineDataItem?>,
    forecast: Array<LineDataItem?>,
): EChartsOption =
    unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "天气 风向 风速 海浪 预报"
                    subtext = "示例数据源于 www.seabreeze.com.au"
                    left = StringOrDouble("center")
                },
            )
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.axis
                    formatter = windBarbTooltip()
                },
            )
        grid =
            one(
                unsafeJso<GridOption> {
                    top = StringOrDouble(160.0)
                    bottom = StringOrDouble(125.0)
                },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.time
                    maxInterval = ONE_DAY
                    splitLine = unsafeJso<SplitLineOption> { lineStyle = unsafeJso { color = ZRColor("#ddd") } }
                },
            )
        yAxis =
            arrayOf(
                unsafeJso<YAxisOption> {
                    name = "风速（节）"
                    nameLocation = AxisNameLocations.middle
                    nameGap = 35.0
                    axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso { color = ZRColor("#666") } }
                    splitLine = unsafeJso<SplitLineOption> { lineStyle = unsafeJso { color = ZRColor("#ddd") } }
                },
                unsafeJso<YAxisOption> {
                    name = "浪高（米）"
                    nameLocation = AxisNameLocations.middle
                    nameGap = 35.0
                    max = AxisMinMax(6.0)
                    axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso { color = ZRColor("#015DD5") } }
                    splitLine = unsafeJso<SplitLineOption> { show = false }
                },
                unsafeJso<YAxisOption> {
                    axisLine =
                        unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                    axisLabel = unsafeJso<AxisLabelOption> { show = false }
                    splitLine = unsafeJso<SplitLineOption> { show = false }
                },
            )
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    type = VisualMapTypes.piecewise
                    orient = Orients.horizontal
                    left = StringOrDouble("center")
                    bottom = StringOrDouble(10.0)
                    pieces =
                        arrayOf(
                            windPiece("大风（>=17节）", "#18BF12", gte = 17.0),
                            windPiece("中风（11  ~ 17 节）", "#f4e9a3", gte = 11.0, lt = 17.0),
                            windPiece("微风（小于 11 节）", "#D33C3E", lt = 11.0),
                        )
                    seriesIndex = IndexOrIndexArray(1.0)
                    dimension = 1.0
                },
            )
        dataZoom =
            arrayOf(
                unsafeJso<DataZoomOption> {
                    type = echarts.DataZoomTypes.inside
                    xAxisIndex = IndexOrIndexArray(0.0)
                    minSpan = 5.0
                },
                unsafeJso<DataZoomOption> {
                    type = echarts.DataZoomTypes.slider
                    xAxisIndex = IndexOrIndexArray(0.0)
                    minSpan = 5.0
                    bottom = StringOrDouble(50.0)
                },
            )
        series =
            arrayOf<SeriesOption>(
                lineSeries {
                    yAxisIndex = 1.0
                    showSymbol = false
                    emphasis =
                        unsafeJso<EmphasisOption> { scale = BooleanOrDouble(false) }
                    symbolSize = SymbolSize(10.0)
                    areaStyle =
                        unsafeJso<AreaStyleOption> {
                            color =
                                linearGradient(
                                    0.0,
                                    0.0,
                                    0.0,
                                    1.0,
                                    arrayOf(
                                        gradientColorStop(0.0, "rgba(88,160,253,1)"),
                                        gradientColorStop(0.5, "rgba(88,160,253,0.7)"),
                                        gradientColorStop(1.0, "rgba(88,160,253,0)"),
                                    ),
                                )
                        }
                    lineStyle = unsafeJso { color = ZRColor("rgba(88,160,253,1)") }
                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("rgba(88,160,253,1)") }
                    encode =
                        unsafeJso<OptionEncode> {
                            x = OptionEncodeValue(DimensionLoose(WindDims.TIME.toDouble()))
                            y = OptionEncodeValue(DimensionLoose(WindDims.WAVE_HEIGHT.toDouble()))
                        }
                    typedData = readings
                    z = 2.0
                },
                customSeries {
                    renderItem = { _, api -> windArrowItem(api) }
                    encode =
                        unsafeJso<OptionEncode> {
                            x = OptionEncodeValue(DimensionLoose(WindDims.TIME.toDouble()))
                            y = OptionEncodeValue(DimensionLoose(WindDims.WIND_SPEED.toDouble()))
                        }
                    typedData = readings.unsafeCast<Array<OptionDataItem?>>()
                    z = 10.0
                },
                lineSeries {
                    symbol = SymbolShape("none")
                    encode =
                        unsafeJso<OptionEncode> {
                            x = OptionEncodeValue(DimensionLoose(WindDims.TIME.toDouble()))
                            y = OptionEncodeValue(DimensionLoose(WindDims.WIND_SPEED.toDouble()))
                        }
                    lineStyle =
                        unsafeJso {
                            color = ZRColor("#aaa")
                            type = ZRLineTypes.dotted
                        }
                    typedData = readings
                    z = 1.0
                },
                customSeries {
                    renderItem = { _, api -> weatherForecastItem(api) }
                    typedData = forecast.unsafeCast<Array<OptionDataItem?>>()
                    tooltip =
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.item
                            formatter = weatherTooltip()
                        }
                    yAxisIndex = 2.0
                    z = 11.0
                },
            )
    }

val windBarbEntries: List<CatalogEntry> =
    listOf(
        windBarb.interactive(
            "wind-barb",
            "风向图",
            "Wind Barb",
            "同一根时间轴上叠四层：浪高是蓝色渐变面积，风速是虚线加每小时的转向箭头，" +
                "顶部是未来一周的天气图标与温度区间。",
            "Four layers on one time axis: wave height as a blue gradient area, wind speed as a " +
                "dotted line with one rotated arrow per hour, and the coming week's weather icons " +
                "with their temperature range on top.",
        ) { chart ->
            loadJson("data/wind-barb-hobart.json").then { json ->
                val table = json.unsafeCast<WindBarbJson>()
                chart.setOption(windBarbOption(readingRows(table), forecastRows(table)))
            }
        },
    )
