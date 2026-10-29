package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * `scatter-aqi-color` from `public/examples/ts/scatter-aqi-color.ts` of apache/echarts-examples.
 *
 * Air quality of three cities in one scatter: PM2.5 drives the symbol size, sulphur dioxide the
 * lightness, and the grade of every day is appended to the row so the tooltip can print it. The
 * upstream file carries the three tables itself, so they live in `ScatterAqiData.kt` unchanged.
 */

private val scatterAqi = Category(Groups.SCATTER, "examples/ScatterAqi.kt")

/** The three series names, in the order of the legend. */
private val SCATTER_AQI_CITIES = arrayOf("北京", "上海", "广州")

/** The colour of every series, in the order of [SCATTER_AQI_CITIES]. */
private val SCATTER_AQI_COLORS = arrayOf("#dd4444", "#fec42c", "#80F1BE")

/** Rows of one city as scatter items; upstream hands the raw 8-element rows to the series. */
private fun aqiScatterData(rows: Array<Array<OptionDataValue?>>): Array<ScatterDataItem?> =
    Array(rows.size) { index -> ScatterDataItem(rows[index]) }

/** The tooltip body of the page: the header repeats the city, the day and that day's grade. */
private fun aqiTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val item = params.unsafeCast<CallbackDataParams>()
            val value = item.value
            TooltipContent(
                "<div style=\"border-bottom: 1px solid rgba(255,255,255,.3); font-size: 18px;" +
                    "padding-bottom: 7px;margin-bottom: 7px\">" +
                    item.seriesName + " " + textAt(value, 0) + "日：" + textAt(value, 7) +
                    "</div>" +
                    "AQI指数：" + textAt(value, 1) + "<br>" +
                    "PM2.5：" + textAt(value, 2) + "<br>" +
                    "PM10：" + textAt(value, 3) + "<br>" +
                    "一氧化碳（CO）：" + textAt(value, 4) + "<br>" +
                    "二氧化氮（NO2）：" + textAt(value, 5) + "<br>" +
                    "二氧化硫（SO2）：" + textAt(value, 6) + "<br>",
            )
        },
    )

/** The symbol style all three series share: a soft drop shadow behind the bubbles. */
private fun aqiItemStyle(): ItemStyleOption =
    unsafeJso {
        opacity = 0.8
        shadowBlur = 10.0
        shadowOffsetX = 0.0
        shadowOffsetY = 0.0
        shadowColor = ZRColor("rgba(0,0,0,0.3)")
    }

/** A value axis of the page; both axes hide their split lines. */
private fun aqiValueAxis(name: String, end: Boolean): YAxisOption =
    unsafeJso {
        type = AxisTypes.value
        this.name = name
        if (end) nameLocation = AxisNameLocations.end
        nameGap = if (end) 20.0 else 16.0
        nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0) }
        splitLine = unsafeJso<SplitLineOption> { show = false }
    }

/** One of the two visual maps: symbol size by PM2.5, lightness by sulphur dioxide. */
private fun aqiVisualMap(pm25: Boolean): VisualMapOption =
    unsafeJso {
        if (pm25) {
            left = StringOrDouble("right")
            top = StringOrDouble("10%")
            dimension = 2.0
            min = 0.0
            max = 250.0
            itemWidth = 30.0
            itemHeight = 120.0
            calculable = true
            precision = 0.1
            text = arrayOf("圆形大小：PM2.5")
            inRange = unsafeJso<VisualOptionUnit> { symbolSize = DoubleOrDoubleArray(arrayOf(10.0, 70.0)) }
            outOfRange =
                unsafeJso<VisualOptionUnit> {
                    symbolSize = DoubleOrDoubleArray(arrayOf(10.0, 70.0))
                    color = StringOrStringArray(arrayOf("rgba(255,255,255,0.4)"))
                }
        } else {
            left = StringOrDouble("right")
            bottom = StringOrDouble("5%")
            dimension = 6.0
            min = 0.0
            max = 50.0
            itemHeight = 120.0
            text = arrayOf("明暗：二氧化硫")
            inRange = unsafeJso<VisualOptionUnit> { colorLightness = DoubleOrDoubleArray(arrayOf(0.9, 0.5)) }
            outOfRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(arrayOf("rgba(255,255,255,0.4)")) }
        }
        textGap = 30.0
        controller =
            unsafeJso<VisualMapControllerOption> {
                inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(arrayOf("#c23531")) }
                outOfRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(arrayOf("#999")) }
            }
    }

/** One scatter series of the page. */
private fun aqiScatterSeries(name: String, rows: Array<Array<OptionDataValue?>>): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        itemStyle = aqiItemStyle()
        typedData = aqiScatterData(rows)
    }

val scatterAqiEntries: List<CatalogEntry> =
    listOf(
        scatterAqi.chart(
            "scatter-aqi-color",
            "AQI 气泡图",
            "Scatter Aqi Color",
            "北京、上海、广州三地各 31 天的空气质量；点的大小按 PM2.5 映射，明暗按二氧化硫，" +
                "两个 visualMap 都带可拖动的控制器。",
            "Thirty-one days of air quality in three cities: PM2.5 sets the bubble size and sulphur " +
                "dioxide the lightness, both with draggable visual-map controllers.",
        ) {
            unsafeJso<EChartsOption> {
                color = Array(SCATTER_AQI_COLORS.size) { ZRColor(SCATTER_AQI_COLORS[it]) }
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            top = StringOrDouble(10.0)
                            data = legendNames(*SCATTER_AQI_CITIES)
                            textStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0) }
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble(150.0)
                            top = StringOrDouble("18%")
                            bottom = StringOrDouble("10%")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            backgroundColor = ZRColor("rgba(255,255,255,0.7)")
                            formatter = aqiTooltip()
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            name = "日期"
                            nameGap = 16.0
                            nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0) }
                            max = AxisMinMax(31.0)
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                yAxis = one(aqiValueAxis("AQI指数", end = true))
                visualMap = arrayOf(aqiVisualMap(pm25 = true), aqiVisualMap(pm25 = false))
                series =
                    arrayOf<SeriesOption>(
                        aqiScatterSeries("北京", SCATTER_AQI_BEIJING),
                        aqiScatterSeries("上海", SCATTER_AQI_SHANGHAI),
                        aqiScatterSeries("广州", SCATTER_AQI_GUANGZHOU),
                    )
            }
        },
    )
