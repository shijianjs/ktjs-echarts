package demo.examples.scatter

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.matrix.dimension
import demo.examples.matrix.names
import demo.examples.shared.every
import demo.examples.shared.textAt

/** The three series names, in the order of the legend. */
internal val SCATTER_AQI_CITIES = arrayOf("北京", "上海", "广州")

/** The colour of every series, in the order of [SCATTER_AQI_CITIES]. */
internal val SCATTER_AQI_COLORS = arrayOf("#dd4444", "#fec42c", "#80F1BE")

/** Rows of one city as scatter items; upstream hands the raw 8-element rows to the series. */
internal fun aqiScatterData(rows: Array<Array<OptionDataValue?>>): Array<ScatterDataItem?> =
    Array(rows.size) { index -> ScatterDataItem(rows[index]) }

/** The tooltip body of the page: the header repeats the city, the day and that day's grade. */
internal fun aqiTooltip(): TooltipFormatter =
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
internal fun aqiItemStyle(): ItemStyleOption =
    unsafeJso {
        opacity = 0.8
        shadowBlur = 10.0
        shadowOffsetX = 0.0
        shadowOffsetY = 0.0
        shadowColor = ZRColor("rgba(0,0,0,0.3)")
    }

/** A value axis of the page; both axes hide their split lines. */
internal fun aqiValueAxis(name: String, end: Boolean): YAxisOption =
    unsafeJso {
        type = AxisTypes.value
        this.name = name
        if (end) nameLocation = AxisNameLocations.end
        nameGap = if (end) 20.0 else 16.0
        nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0) }
        splitLine = unsafeJso<SplitLineOption> { show = false }
    }

/** One of the two visual maps: symbol size by PM2.5, lightness by sulphur dioxide. */
internal fun aqiVisualMap(pm25: Boolean): VisualMapOption =
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
internal fun aqiScatterSeries(name: String, rows: Array<Array<OptionDataValue?>>): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        itemStyle = aqiItemStyle()
        typedData = aqiScatterData(rows)
    }
