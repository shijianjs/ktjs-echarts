package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AnimationEasings
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.js
import kotlin.math.round
import web.cssom.TextAlign
import zrender.ZRColor
import zrender.ZRFontWeight
import zrender.ZRFontWeights

/*
 * The gauge examples the official page builds its dial faces out of `path://` icons.
 *
 * Every path string is copied verbatim from the upstream file; the four needles and the numeral ring
 * are the whole visual identity of these pages, so they are inlined instead of dropped.
 */

private val gaugeLongTail = Category(Groups.GAUGE, "examples/GaugeLongTail.kt")

/** The tapered needle of `gauge-clock`, `gauge-multi-title` and the second dial of `gauge-barometer`. */
private const val NEEDLE_PATH =
    "path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z"

/** The wide needle of `gauge-speed` and `gauge-barometer`. */
private const val WIDE_NEEDLE_PATH =
    "path://M2090.36389,615.30999 L2090.36389,615.30999 C2091.48372,615.30999 2092.40383,616.194028 " +
        "2092.44859,617.312956 L2096.90698,728.755929 C2097.05155,732.369577 2094.2393,735.416212 " +
        "2090.62566,735.56078 C2090.53845,735.564269 2090.45117,735.566014 2090.36389,735.566014 " +
        "L2090.36389,735.566014 C2086.74736,735.566014 2083.81557,732.63423 2083.81557,729.017692 " +
        "C2083.81557,728.930412 2083.81732,728.84314 2083.82081,728.755929 L2088.2792,617.312956 " +
        "C2088.32396,616.194028 2089.24407,615.30999 2090.36389,615.30999 Z"

/** The hour numerals drawn as one path, the anchor icon of `gauge-clock`. */
private const val CLOCK_NUMERALS_PATH =
    "path://M532.8,70.8C532.8,70.8,532.8,70.8,532.8,70.8L532.8,70.8C532.7,70.8,532.8,70.8,532.8,70.8z " +
        "M456.1,49.6c-2.2-6.2-8.1-10.6-15-10.6h-37.5v10.6h37.5l0,0c2.9,0,5.3,2.4,5.3,5.3c0,2.9-2.4,5.3-5.3,5.3v0h-22.5" +
        "c-1.5,0.1-3,0.4-4.3,0.9c-4.5,1.6-8.1,5.2-9.7,9.8c-0.6,1.7-0.9,3.4-0.9,5.3v16h10.6v-16l0,0l0,0c0-2.7,2.1-5,4.7-5.3" +
        "h10.3l10.4,21.2h11.8l-10.4-21.2h0c6.9,0,12.8-4.4,15-10.6c0.6-1.7,0.9-3.5,0.9-5.3C457,53,456.7,51.2,456.1,49.6z " +
        "M388.9,92.1h11.3L381,39h-3.6h-11.3L346.8,92v0h11.3l3.9-10.7h7.3h7.7l3.9-10.6h-7.7h-7.3l7.7-21.2v0L388.9,92.1z " +
        "M301,38.9h-10.6v53.1H301V70.8h28.4l3.7-10.6H301V38.9zM333.2,38.9v10.6v10.7v31.9h10.6V38.9H333.2z " +
        "M249.5,81.4L249.5,81.4L249.5,81.4c-2.9,0-5.3-2.4-5.3-5.3h0V54.9h0l0,0c0-2.9,2.4-5.3,5.3-5.3l0,0l0,0h33.6l3.9-10.6" +
        "h-37.5c-1.9,0-3.6,0.3-5.3,0.9c-4.5,1.6-8.1,5.2-9.7,9.7c-0.6,1.7-0.9,3.5-0.9,5.3l0,0v21.3c0,1.9,0.3,3.6,0.9,5.3" +
        "c1.6,4.5,5.2,8.1,9.7,9.7c1.7,0.6,3.5,0.9,5.3,0.9h33.6l3.9-10.6H249.5z M176.8,38.9v10.6h49.6l3.9-10.6H176.8z " +
        "M192.7,81.4L192.7,81.4L192.7,81.4c-2.9,0-5.3-2.4-5.3-5.3l0,0v-5.3h38.9l3.9-10.6h-53.4v10.6v5.3l0,0c0,1.9,0.3,3.6,0.9,5.3" +
        "c1.6,4.5,5.2,8.1,9.7,9.7c1.7,0.6,3.4,0.9,5.3,0.9h23.4h10.2l3.9-10.6l0,0H192.7z M460.1,38.9v10.6h21.4v42.5h10.6V49.6" +
        "h17.5l3.8-10.6H460.1z M541.6,68.2c-0.2,0.1-0.4,0.3-0.7,0.4C541.1,68.4,541.4,68.3,541.6,68.2L541.6,68.2z " +
        "M554.3,60.2h-21.6v0l0,0c-2.9,0-5.3-2.4-5.3-5.3c0-2.9,2.4-5.3,5.3-5.3l0,0l0,0h33.6l3.8-10.6h-37.5l0,0" +
        "c-6.9,0-12.8,4.4-15,10.6c-0.6,1.7-0.9,3.5-0.9,5.3c0,1.9,0.3,3.7,0.9,5.3c2.2,6.2,8.1,10.6,15,10.6h21.6l0,0" +
        "c2.9,0,5.3,2.4,5.3,5.3c0,2.9-2.4,5.3-5.3,5.3l0,0h-37.5v10.6h37.5c6.9,0,12.8-4.4,15-10.6c0.6-1.7,0.9-3.5,0.9-5.3" +
        "c0-1.9-0.3-3.7-0.9-5.3C567.2,64.6,561.3,60.2,554.3,60.2z"

/** The pointed needle of `gauge-car`. */
private const val CAR_NEEDLE_PATH =
    "path://M-36.5,23.9L-41,4.4c-0.1-0.4-0.4-0.7-0.7-0.7c-0.5-0.1-1.1,0.2-1.2,0.7l-4.5,19.5c0,0.1,0,0.1,0,0.2v92.3" +
        "c0,0.6,0.4,1,1,1h9c0.6,0,1-0.4,1-1V24.1C-36.5,24-36.5,23.9-36.5,23.9z M-39.5,114.6h-5v-85h5V114.6z"

/** The fuel-pump icon of `gauge-car`. */
private const val CAR_FUEL_PATH =
    "path://M1.11979167,1.11111112 C1.11979167,0.497461393 1.61725306,0 2.23090279,0 L12.2309028,0 " +
        "C12.8445525,1.43824153e-08 13.3420139,0.497461403 13.3420139,1.11111112 L13.3420139,10 L15.5642361,10 " +
        "C16.7915356,10 17.7864583,10.9949228 17.7864583,12.2222222 L17.7864583,16.6666667 C17.7865523,17.28025 " +
        "18.2839861,17.7776077 18.8975694,17.7776077 C19.5111527,17.7776077 20.0085866,17.28025 20.0086805,16.6666667 " +
        "L20.0086805,8.88888888 L17.7864583,8.88888888 C17.1728086,8.88888888 16.6753472,8.3914275 16.6753472,7.77777779 " +
        "L16.6753472,3.79333333 L15.6197917,2.73777777 C15.1859413,2.30392741 15.1859413,1.60051702 15.6197917,1.16666667 " +
        "L15.6197917,1.16666667 C16.053642,0.732816318 16.7570524,0.732816318 17.1909028,1.16666667 L21.9053472,5.88111112 " +
        "C22.1140468,6.08922811 22.2312072,6.37193273 22.2309028,6.66666667 L22.2309028,16.6666667 C22.2309028,18.5076158 " +
        "20.7385186,20 18.8975695,20 C17.0566203,20 15.5642361,18.5076158 15.5642361,16.6666667 L15.5642361,12.2222222 " +
        "L13.3420139,12.2222222 L13.3420139,17.7777778 L13.3420139,17.7777778 C13.9556636,17.7777778 14.453125,18.2752392 " +
        "14.453125,18.8888889 L14.453125,18.8888889 C14.453125,19.5025386 13.9556636,20 13.3420139,20 L1.11979165,20 " +
        "C0.506141934,20 0.00868054688,19.5025386 0.00868054687,18.8888889 L0.00868054687,18.8888889 C0.00868054688,18.2752392 " +
        "0.506141934,17.7777778 1.11979165,17.7777778 L1.11979167,17.7777778 L1.11979167,1.11111112 Z M3.34201388,2.22222221 " +
        "L3.34201388,8.88888888 L11.1197917,8.88888888 L11.1197917,2.22222221 L3.34201388,2.22222221 Z"

/** The gear icon of `gauge-car`. */
private const val CAR_GEAR_PATH =
    "path://M-34.1-1.1L-34.1-1.1c0-0.3-0.3-0.6-0.6-0.6h-3.6v-1.5c0-0.5-0.2-0.9-0.6-1.1s-0.9-0.2-1.3,0" +
        "c-0.4,0.2-0.6,0.7-0.6,1.1V7.9c0,0,0,0.1,0,0.1c-0.8,0.5-1.2,1.5-0.9,2.5c0.3,0.9,1.1,1.6,2.1,1.6c1,0,1.8-0.6,2.1-1.5" +
        "c0.3-0.9,0-1.9-0.8-2.5V6.3h3.5c0.4,0,0.7-0.3,0.7-0.7l0,0c0-0.4-0.3-0.7-0.7-0.7h-3.5V2.9h3.5c0.4,0,0.7-0.3,0.7-0.7l0,0" +
        "c0-0.4-0.3-0.7-0.7-0.7h-3.5v-2.1h3.6C-34.4-0.5-34.1-0.8-34.1-1.1z M-44.9,11.6c-0.7,0-1.4-0.2-2-0.6" +
        "c-0.4-0.3-0.9-0.4-1.4-0.4c-0.4,0-0.9,0.2-1.2,0.4c-0.4,0.2-1.4-0.9-0.9-1.3c0.6-0.4,1.3-0.6,2-0.7c0.8,0,1.5,0.2,2.2,0.5" +
        "c0.4,0.3,0.9,0.4,1.3,0.4c0.6,0,1.1-0.2,1.5-0.6s1.6,0.7,0.9,1.3S-44,11.6-44.9,11.6L-44.9,11.6z M-34.3,11.6" +
        "c-0.7,0-1.4-0.3-2-0.7c-0.6-0.4,0.5-1.6,0.9-1.3s0.8,0.4,1.2,0.4c0.5,0,1-0.1,1.4-0.4c0.6-0.3,1.3-0.5,2-0.6h0c0.9,0,1.7,0.3,2.4,0.9" +
        "c0.7,0.5-0.5,1.6-0.9,1.3c-0.4-0.3-1-0.6-1.5-0.6h0c-0.5,0-0.9,0.2-1.3,0.4c-0.6,0.3-1.3,0.5-2,0.6H-34.3z M-33.5,16.3" +
        "c-0.7,0-1.4-0.3-1.9-0.8c-0.4-0.3-0.6-0.5-1-0.5c-0.4,0-0.7,0.2-1,0.4c-0.6,0.5-1.3,0.7-2,0.7c-0.7,0-1.4-0.3-1.9-0.8" +
        "c-0.2-0.3-0.6-0.4-0.9-0.4c-0.4,0-0.7,0.1-1.1,0.5c-0.6,0.5-1.3,0.7-2.1,0.7c-0.7-0.1-1.4-0.4-1.9-0.9c-0.4-0.3-0.6-0.5-1-0.5" +
        "c-0.3,0-0.6,0.2-0.9,0.4s-1.6-0.7-1.1-1.2c0.5-0.5,1.2-0.8,1.9-0.9c1-0.1,1.6,0.4,2.1,0.8c0.3,0.3,0.6,0.5,1,0.5" +
        "c0.4,0,0.6-0.1,1-0.4c0.6-0.5,1.4-0.8,2.1-0.8c0.7,0,1.4,0.3,1.9,0.8c0.2,0.2,0.6,0.4,0.9,0.4c0.4,0,0.6-0.1,1-0.4" +
        "c0.6-0.5,1.3-0.7,2-0.7c0.8,0,1.5,0.3,2,0.9c0.4,0.3,0.6,0.4,0.9,0.4c0.3,0,0.7-0.2,1.1-0.5c0.5-0.4,1.2-0.9,2.3-0.8" +
        "c0.7,0,1.4,0.3,1.9,0.7c0.5,0.4-0.7,1.5-1,1.3s-0.6-0.4-1-0.4c-0.4,0-0.7,0.2-1.2,0.5C-32,15.9-32.7,16.2-33.5,16.3L-33.5,16.3z"

/** `offsetCenter` given as `[0, percent]`, the form these dials use. */
private fun gaugeCenter(percent: String): Array<StringOrDouble> =
    arrayOf(StringOrDouble(0.0), StringOrDouble(percent))

/** A `[x, y]` offset where only the horizontal part is a percentage. */
private fun gaugeOffset(x: String, y: String): Array<StringOrDouble> = arrayOf(StringOrDouble(x), StringOrDouble(y))

/** `{ value: <value> }`, optionally named, of a gauge series. */
private fun dialItem(value: Double, name: String? = null): GaugeDataItem =
    GaugeDataItem(
        unsafeJso<GaugeDataItemOption> {
            if (name != null) this.name = OptionId(name)
            this.value = OptionDataValue(value)
        },
    )

/** `value === 0 ? '' : value + ''`, the hour labels of `gauge-clock`. */
private val skipZeroLabel =
    js("(value) => value === 0 ? '' : value + ''") as LabelFormatterCallback

/** `{value|<value>}{unit|km/h}`, the speed readout of `gauge-speed`. */
private val speedReadout =
    js("(value) => '{value|' + value.toFixed(0) + '}{unit|km/h}'") as LabelFormatterCallback

/** A `rich` block from named text styles, the object form of `{a|…}{b|…}` markup. */
private fun richText(vararg styles: Pair<String, TextStyleOption>): Record<String, TextStyleOption> =
    recordOf(*Array(styles.size) { styles[it].first to styles[it].second.unsafeCast<JsAny>() })
        .unsafeCast<Record<String, TextStyleOption>>()

/** The `{value|…}{unit|km/h}` pair of `gauge-speed`. */
private val speedRich =
    richText(
        "value" to
            unsafeJso<TextStyleOption> {
                fontSize = StringOrDouble(50.0)
                fontWeight = ZRFontWeights.bolder
                color = "#777"
            },
        "unit" to
            unsafeJso<TextStyleOption> {
                fontSize = StringOrDouble(20.0)
                color = "#999"
                padding = DoubleOrDoubleArray(arrayOf(0.0, 0.0, -20.0, 10.0))
            },
    )

/** The Arial numerals every dial of `gauge-car` prints. */
private fun carLabel(build: GaugeTitleOption.() -> Unit = {}): GaugeTitleOption =
    unsafeJso<GaugeTitleOption> {
        fontWeight = ZRFontWeight(800.0)
        fontFamily = "Arial"
        color = "#fff"
        build()
    }

/** The same typography for the axis labels of `gauge-car`. */
private fun carAxisLabel(
    distance: Double,
    fontSize: Double,
    formatter: LabelFormatterCallback? = null,
): GaugeAxisLabelOption =
    unsafeJso<GaugeAxisLabelOption> {
        this.distance = distance
        this.fontSize = StringOrDouble(fontSize)
        fontWeight = ZRFontWeight(800.0)
        fontFamily = "Arial"
        color = "#fff"
        if (formatter != null) this.formatter = LabelFormatter(formatter)
    }

/** A `#fff` tick/split line of `gauge-car`, `[color, width]`. */
private fun carTickStyle(width: Double): LineStyleOption =
    unsafeJso<LineStyleOption> { color = ZRColor("#fff"); this.width = width }

/** One Arial numeral style of `gauge-car`. */
private fun carText(
    fontSize: Double,
    fontWeight: Double,
    align: TextAlign? = null,
    lineHeight: Double? = null,
    padding: DoubleOrDoubleArray? = null,
): TextStyleOption =
    unsafeJso<TextStyleOption> {
        this.fontSize = StringOrDouble(fontSize)
        this.fontWeight = ZRFontWeight(fontWeight)
        fontFamily = "Arial"
        color = "#fff"
        if (align != null) this.align = align
        if (lineHeight != null) this.lineHeight = lineHeight
        if (padding != null) this.padding = padding
    }

/** The readout styles of the middle speed display of `gauge-car`. */
private val carSpeedRich =
    richText(
        "a" to carText(60.0, 800.0, TextAlign.center, padding = DoubleOrDoubleArray(arrayOf(0.0, 5.0, 0.0, 0.0))),
        "b" to carText(14.0, 800.0, padding = DoubleOrDoubleArray(arrayOf(0.0, 0.0, 20.0, 0.0))),
    )

/** The readout styles of the trip summary of `gauge-car`. */
private val carTripRich =
    richText(
        "a" to carText(14.0, 800.0, TextAlign.left, 22.0),
        "b" to carText(12.0, 600.0, TextAlign.left, 22.0),
    )

/** The readout styles of the odometer of `gauge-car`. */
private val carOdometerRich = richText("a" to carText(15.0, 800.0), "b" to carText(12.0, 600.0))

val gaugeLongTailEntries: List<CatalogEntry> =
    listOf(
        gaugeLongTail.chart("gauge-speed", "速度仪表盘", "Speed Gauge") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        gaugeSeries {
                            startAngle = 180.0
                            endAngle = 0.0
                            min = 0.0
                            max = 240.0
                            splitNumber = 12.0
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#58D9F9")
                                    shadowColor = ZRColor("rgba(0,138,255,0.45)")
                                    shadowBlur = 10.0
                                    shadowOffsetX = 2.0
                                    shadowOffsetY = 2.0
                                }
                            progress = unsafeJso<GaugeProgressOption> { show = true; roundCap = true; width = 18.0 }
                            pointer =
                                unsafeJso<GaugePointerOption> {
                                    icon = WIDE_NEEDLE_PATH
                                    length = StringOrDouble("75%")
                                    width = 16.0
                                    offsetCenter = gaugeCenter("5%")
                                }
                            axisLine =
                                unsafeJso<GaugeAxisLineOption> {
                                    roundCap = true
                                    lineStyle = unsafeJso<GaugeAxisLineStyleOption> { width = 18.0 }
                                }
                            axisTick =
                                unsafeJso<GaugeAxisTickOption> {
                                    splitNumber = 2.0
                                    lineStyle = unsafeJso<LineStyleOption> { width = 2.0; color = ZRColor("#999") }
                                }
                            splitLine =
                                unsafeJso<GaugeSplitLineOption> {
                                    length = 12.0
                                    lineStyle = unsafeJso<LineStyleOption> { width = 3.0; color = ZRColor("#999") }
                                }
                            axisLabel =
                                unsafeJso<GaugeAxisLabelOption> {
                                    distance = 30.0
                                    color = "#999"
                                    fontSize = StringOrDouble(20.0)
                                }
                            title = unsafeJso<GaugeTitleOption> { show = false }
                            detail =
                                unsafeJso<GaugeDetailOption> {
                                    backgroundColor = ZRColor("#fff")
                                    borderColor = "#999"
                                    borderWidth = 2.0
                                    width = StringOrDouble("60%")
                                    lineHeight = 40.0
                                    height = 40.0
                                    borderRadius = DoubleOrDoubleArray(8.0)
                                    offsetCenter = gaugeCenter("35%")
                                    valueAnimation = true
                                    formatter = LabelFormatter(speedReadout)
                                    rich = speedRich
                                }
                            typedData = arrayOf(dialItem(100.0))
                        },
                    )
            }
        },
        gaugeLongTail.interactive("gauge-clock", "时钟仪表盘", "Clock", height = "40rem") { chart, holder ->
            chart.setOption(clockOption(0.0, 0.0, 0.0, false))
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                val now = Date()
                val second = now.getSeconds().toDouble()
                val minute = now.getMinutes().toDouble() + second / 60.0
                val hour = (now.getHours() % 12).toDouble() + minute / 60.0
                chart.setOption(clockOption(hour, minute, second, true))
            }, 1000)
        },
        gaugeLongTail.interactive("gauge-barometer", "气压表", "Gauge Barometer") { chart, holder ->
            chart.setOption(barometerOption(58.46))
            val random = Lcg(31)
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        series =
                            arrayOf<SeriesOption>(
                                gaugeSeries {
                                    typedData = arrayOf(dialItem(round(random.next() * 100 * 100) / 100, "PLP"))
                                },
                            )
                    },
                )
            }, 2000)
        },
        gaugeLongTail.interactive("gauge-multi-title", "多标题仪表盘", "Multi Title Gauge") { chart, holder ->
            val values = doubleArrayOf(20.0, 40.0, 60.0)
            chart.setOption(multiTitleOption(values))
            val random = Lcg(17)
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                for (index in values.indices) {
                    values[index] = round(random.next() * 100 * 100) / 100
                }
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        series = arrayOf<SeriesOption>(gaugeSeries { typedData = multiTitleItems(values) })
                    },
                )
            }, 2000)
        },
        gaugeLongTail.chart("gauge-car", "汽车仪表盘", "Gauge Car", height = "40rem") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#000")
                tooltip =
                    one(
                        unsafeJso<TooltipOption> { formatter = TooltipFormatter("{a} <br/>{b} : {c}%") },
                    )
                toolbox = toolbox(restore = true, saveAsImage = true)
                series =
                    arrayOf<SeriesOption>(
                        carSpeedDial(),
                        carPositionDial(),
                        carRevDial(),
                        carSpeedReadout(),
                        carTripHeader(),
                        carFuelDial(),
                        carGearDial(),
                    )
            }
        },
    )

/**
 * The three overlapping dials of `gauge-clock`.
 *
 * [animate] marks the update calls: upstream skips the needle animation whenever a hand sits at zero,
 * which the per-series `animation` flag below reproduces. The `option.animationDurationUpdate = 300`
 * the upstream file writes inside its interval never reaches `setOption` — the call only merges a
 * literal series array — so the port drops it.
 */
private fun clockOption(
    hour: Double,
    minute: Double,
    second: Double,
    animate: Boolean,
): EChartsOption =
    unsafeJso<EChartsOption> {
        series =
            arrayOf<SeriesOption>(
                gaugeSeries {
                    name = "hour"
                    startAngle = 90.0
                    endAngle = -270.0
                    min = 0.0
                    max = 12.0
                    splitNumber = 12.0
                    clockwise = true
                    this.animation = !animate || hour != 0.0
                    axisLine =
                        unsafeJso<GaugeAxisLineOption> {
                            lineStyle =
                                unsafeJso<GaugeAxisLineStyleOption> {
                                    width = 15.0
                                    color = arrayOf(gaugeColorStop(1.0, "rgba(0,0,0,0.7)"))
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                    shadowBlur = 15.0
                                }
                        }
                    splitLine =
                        unsafeJso<GaugeSplitLineOption> {
                            lineStyle =
                                unsafeJso<LineStyleOption> {
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.3)")
                                    shadowBlur = 3.0
                                    shadowOffsetX = 1.0
                                    shadowOffsetY = 2.0
                                }
                        }
                    axisLabel =
                        unsafeJso<GaugeAxisLabelOption> {
                            fontSize = StringOrDouble(50.0)
                            distance = 25.0
                            formatter = LabelFormatter(skipZeroLabel)
                        }
                    anchor =
                        unsafeJso<GaugeAnchorOption> {
                            show = true
                            icon = CLOCK_NUMERALS_PATH
                            showAbove = false
                            offsetCenter = gaugeCenter("-35%")
                            size = 120.0
                            keepAspect = true
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#707177") }
                        }
                    pointer = clockNeedle(12.0, "55%")
                    detail = unsafeJso<GaugeDetailOption> { show = false }
                    title = unsafeJso<GaugeTitleOption> { offsetCenter = gaugeCenter("30%") }
                    typedData = arrayOf(dialItem(hour))
                },
                gaugeSeries {
                    name = "minute"
                    startAngle = 90.0
                    endAngle = -270.0
                    min = 0.0
                    max = 60.0
                    clockwise = true
                    this.animation = !animate || minute != 0.0
                    axisLine = unsafeJso<GaugeAxisLineOption> { show = false }
                    splitLine = unsafeJso<GaugeSplitLineOption> { show = false }
                    axisTick = unsafeJso<GaugeAxisTickOption> { show = false }
                    axisLabel = unsafeJso<GaugeAxisLabelOption> { show = false }
                    pointer = clockNeedle(8.0, "70%")
                    anchor =
                        unsafeJso<GaugeAnchorOption> {
                            show = true
                            size = 20.0
                            showAbove = false
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderWidth = 15.0
                                    borderColor = ZRColor("#C0911F")
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.3)")
                                    shadowBlur = 8.0
                                    shadowOffsetX = 2.0
                                    shadowOffsetY = 4.0
                                }
                        }
                    detail = unsafeJso<GaugeDetailOption> { show = false }
                    title = unsafeJso<GaugeTitleOption> { offsetCenter = gaugeOffset("0%", "-40%") }
                    typedData = arrayOf(dialItem(minute))
                },
                gaugeSeries {
                    name = "second"
                    startAngle = 90.0
                    endAngle = -270.0
                    min = 0.0
                    max = 60.0
                    clockwise = true
                    animationEasingUpdate = AnimationEasings.bounceOut
                    this.animation = !animate || second != 0.0
                    axisLine = unsafeJso<GaugeAxisLineOption> { show = false }
                    splitLine = unsafeJso<GaugeSplitLineOption> { show = false }
                    axisTick = unsafeJso<GaugeAxisTickOption> { show = false }
                    axisLabel = unsafeJso<GaugeAxisLabelOption> { show = false }
                    pointer = clockNeedle(4.0, "85%")
                    anchor =
                        unsafeJso<GaugeAnchorOption> {
                            show = true
                            size = 15.0
                            showAbove = true
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#C0911F")
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.3)")
                                    shadowBlur = 8.0
                                    shadowOffsetX = 2.0
                                    shadowOffsetY = 4.0
                                }
                        }
                    detail = unsafeJso<GaugeDetailOption> { show = false }
                    title = unsafeJso<GaugeTitleOption> { offsetCenter = gaugeOffset("0%", "-40%") }
                    typedData = arrayOf(dialItem(second))
                },
            )
    }

/** One gold needle of `gauge-clock`. */
private fun clockNeedle(
    width: Double,
    length: String,
): GaugePointerOption =
    unsafeJso<GaugePointerOption> {
        icon = NEEDLE_PATH
        this.width = width
        this.length = StringOrDouble(length)
        offsetCenter = gaugeCenter("8%")
        itemStyle =
            unsafeJso<ItemStyleOption> {
                color = ZRColor("#C0911F")
                shadowColor = ZRColor("rgba(0, 0, 0, 0.3)")
                shadowBlur = 8.0
                shadowOffsetX = 2.0
                shadowOffsetY = 4.0
            }
    }

/** The outer dial and the empty inner ring of `gauge-barometer`. */
private fun barometerOption(value: Double): EChartsOption =
    unsafeJso<EChartsOption> {
        series =
            arrayOf<SeriesOption>(
                gaugeSeries {
                    min = 0.0
                    max = 100.0
                    splitNumber = 10.0
                    radius = StringOrDouble("80%")
                    axisLine =
                        unsafeJso<GaugeAxisLineOption> {
                            lineStyle =
                                unsafeJso<GaugeAxisLineStyleOption> {
                                    color = arrayOf(gaugeColorStop(1.0, "#f00"))
                                    width = 3.0
                                }
                        }
                    splitLine =
                        unsafeJso<GaugeSplitLineOption> {
                            distance = -18.0
                            length = 18.0
                            lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#f00") }
                        }
                    axisTick =
                        unsafeJso<GaugeAxisTickOption> {
                            distance = -12.0
                            length = 10.0
                            lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#f00") }
                        }
                    axisLabel =
                        unsafeJso<GaugeAxisLabelOption> {
                            distance = -50.0
                            color = "#f00"
                            fontSize = StringOrDouble(25.0)
                        }
                    anchor =
                        unsafeJso<GaugeAnchorOption> {
                            show = true
                            size = 20.0
                            itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("#000"); borderWidth = 2.0 }
                        }
                    pointer =
                        unsafeJso<GaugePointerOption> {
                            offsetCenter = gaugeCenter("10%")
                            icon = WIDE_NEEDLE_PATH
                            length = StringOrDouble("115%")
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#000") }
                        }
                    detail = unsafeJso<GaugeDetailOption> { valueAnimation = true; precision = StringOrDouble(1.0) }
                    title = unsafeJso<GaugeTitleOption> { offsetCenter = gaugeCenter("-50%") }
                    typedData = arrayOf(dialItem(value, "PLP"))
                },
                gaugeSeries {
                    min = 0.0
                    max = 60.0
                    splitNumber = 6.0
                    axisLine =
                        unsafeJso<GaugeAxisLineOption> {
                            lineStyle =
                                unsafeJso<GaugeAxisLineStyleOption> {
                                    color = arrayOf(gaugeColorStop(1.0, "#000"))
                                    width = 3.0
                                }
                        }
                    splitLine =
                        unsafeJso<GaugeSplitLineOption> {
                            distance = -3.0
                            length = 18.0
                            lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#000") }
                        }
                    axisTick =
                        unsafeJso<GaugeAxisTickOption> {
                            distance = 0.0
                            length = 10.0
                            lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#000") }
                        }
                    axisLabel =
                        unsafeJso<GaugeAxisLabelOption> {
                            distance = 10.0
                            fontSize = StringOrDouble(25.0)
                            color = "#000"
                        }
                    pointer = unsafeJso<GaugePointerOption> { show = false }
                    title = unsafeJso<GaugeTitleOption> { show = false }
                    anchor =
                        unsafeJso<GaugeAnchorOption> {
                            show = true
                            size = 14.0
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#000") }
                        }
                },
            )
    }

/** The three offset titles of `gauge-multi-title`. */
private fun multiTitleItems(values: DoubleArray): Array<GaugeDataItem?> =
    arrayOf(
        multiTitleItem("Good", values[0], "-40%"),
        multiTitleItem("Better", values[1], "0%"),
        multiTitleItem("Perfect", values[2], "40%"),
    )

private fun multiTitleItem(
    name: String,
    value: Double,
    horizontal: String,
): GaugeDataItem =
    GaugeDataItem(
        unsafeJso<GaugeDataItemOption> {
            this.name = OptionId(name)
            this.value = OptionDataValue(value)
            title = unsafeJso<GaugeTitleOption> { offsetCenter = gaugeOffset(horizontal, "80%") }
            detail = unsafeJso<GaugeDetailOption> { offsetCenter = gaugeOffset(horizontal, "95%") }
        },
    )

private fun multiTitleOption(values: DoubleArray): EChartsOption =
    unsafeJso<EChartsOption> {
        series =
            arrayOf<SeriesOption>(
                gaugeSeries {
                    anchor =
                        unsafeJso<GaugeAnchorOption> {
                            show = true
                            showAbove = true
                            size = 18.0
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#FAC858") }
                        }
                    pointer =
                        unsafeJso<GaugePointerOption> {
                            icon = NEEDLE_PATH
                            width = 8.0
                            length = StringOrDouble("80%")
                            offsetCenter = gaugeCenter("8%")
                        }
                    progress = unsafeJso<GaugeProgressOption> { show = true; overlap = true; roundCap = true }
                    axisLine = unsafeJso<GaugeAxisLineOption> { roundCap = true }
                    typedData = multiTitleItems(values)
                    title = unsafeJso<GaugeTitleOption> { fontSize = StringOrDouble(14.0) }
                    detail =
                        unsafeJso<GaugeDetailOption> {
                            width = StringOrDouble(40.0)
                            height = 14.0
                            fontSize = StringOrDouble(14.0)
                            color = "#fff"
                            backgroundColor = ZRColor("inherit")
                            borderRadius = DoubleOrDoubleArray(3.0)
                            formatter = LabelFormatter("{value}%")
                        }
                },
            )
    }

/** `gauge-car`, gauge 0: the outer speed ring. */
private fun carSpeedDial(): SeriesOption =
    gaugeSeries {
        name = "gauge 0"
        min = -200.0
        max = 250.0
        startAngle = -30.0
        endAngle = -315.0
        splitNumber = 9.0
        radius = StringOrDouble("35%")
        center = arrayOf(StringOrDouble("21%"), StringOrDouble("55%"))
        axisLine =
            unsafeJso<GaugeAxisLineOption> {
                lineStyle = unsafeJso<GaugeAxisLineStyleOption> { color = arrayOf(gaugeColorStop(1.0, "#AE96A6")) }
            }
        splitLine = unsafeJso<GaugeSplitLineOption> { show = false }
        axisTick = unsafeJso<GaugeAxisTickOption> { show = false }
        axisLabel = unsafeJso<GaugeAxisLabelOption> { show = false }
        anchor = unsafeJso<GaugeAnchorOption> {}
        pointer = unsafeJso<GaugePointerOption> { show = false }
        detail = unsafeJso<GaugeDetailOption> { show = false }
        title = carLabel { fontSize = StringOrDouble(12.0); offsetCenter = gaugeCenter("-60%") }
        progress =
            unsafeJso<GaugeProgressOption> {
                show = true
                width = 3.0
                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#fff") }
            }
        typedData = arrayOf(dialItem(250.0, "km/h"))
    }

/** `gauge-car`, gauge 1: the small position dial with its own needle. */
private fun carPositionDial(): SeriesOption =
    gaugeSeries {
        name = "gauge 1"
        min = 0.0
        max = 250.0
        startAngle = -140.0
        endAngle = -305.0
        splitNumber = 5.0
        radius = StringOrDouble("35%")
        center = arrayOf(StringOrDouble("21%"), StringOrDouble("55%"))
        axisLine =
            unsafeJso<GaugeAxisLineOption> {
                lineStyle = unsafeJso<GaugeAxisLineStyleOption> { color = arrayOf(gaugeColorStop(1.0, "#AE96A6")) }
            }
        splitLine =
            unsafeJso<GaugeSplitLineOption> {
                distance = -7.0
                length = 12.0
                lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#fff"); width = 4.0 }
            }
        axisTick =
            unsafeJso<GaugeAxisTickOption> {
                distance = -8.0
                length = 8.0
                lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#fff"); width = 2.0 }
            }
        axisLabel = carAxisLabel(14.0, 18.0)
        anchor = unsafeJso<GaugeAnchorOption> {}
        pointer =
            unsafeJso<GaugePointerOption> {
                icon = CAR_NEEDLE_PATH
                width = 5.0
                length = StringOrDouble("40%")
                offsetCenter = gaugeCenter("-58%")
                itemStyle = carNeedleStyle()
            }
        title = carLabel { fontSize = StringOrDouble(14.0); offsetCenter = arrayOf(StringOrDouble(0.0), StringOrDouble(0.0)) }
        detail = unsafeJso<GaugeDetailOption> { show = false }
        typedData = arrayOf(dialItem(0.0, "当前位置：\n \n 中科路"))
    }

/** `gauge-car`, gauge 2: the central tachometer. */
private fun carRevDial(): SeriesOption =
    gaugeSeries {
        name = "gauge 2"
        min = 0.0
        max = 8.0
        z = 10.0
        startAngle = 210.0
        endAngle = -30.0
        splitNumber = 8.0
        radius = StringOrDouble("50%")
        center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
        axisLine =
            unsafeJso<GaugeAxisLineOption> {
                show = true
                lineStyle =
                    unsafeJso<GaugeAxisLineStyleOption> {
                        width = 0.0
                        color =
                            arrayOf(
                                gaugeColorStop(0.825, "#fff"),
                                gaugeColorStop(1.0, "#f00"),
                            )
                    }
            }
        splitLine =
            unsafeJso<GaugeSplitLineOption> {
                distance = 20.0
                length = 15.0
                lineStyle =
                    unsafeJso<LineStyleOption> {
                        color = ZRColor("auto")
                        width = 4.0
                        shadowColor = ZRColor("rgba(255, 255, 255, 0.5)")
                        shadowBlur = 15.0
                        shadowOffsetY = -10.0
                    }
            }
        axisTick =
            unsafeJso<GaugeAxisTickOption> {
                distance = 20.0
                length = 8.0
                lineStyle =
                    unsafeJso<LineStyleOption> {
                        color = ZRColor("auto")
                        width = 2.0
                        shadowColor = ZRColor("rgba(255, 255, 255)")
                        shadowBlur = 10.0
                        shadowOffsetY = -10.0
                    }
            }
        axisLabel = carAxisLabel(10.0, 35.0)
        anchor = unsafeJso<GaugeAnchorOption> {}
        pointer =
            unsafeJso<GaugePointerOption> {
                icon = CAR_NEEDLE_PATH
                width = 10.0
                offsetCenter = gaugeCenter("-10%")
                length = StringOrDouble("75%")
                itemStyle = carNeedleStyle()
            }
        title = carLabel { fontSize = StringOrDouble(12.0); offsetCenter = gaugeCenter("-50%") }
        typedData = arrayOf(dialItem(0.6, "1/min x 1000"))
        detail = unsafeJso<GaugeDetailOption> { show = false }
    }

/** `gauge-car`, gauge 3: the digital speed readout in the middle. */
private fun carSpeedReadout(): SeriesOption =
    gaugeSeries {
        name = "gauge 3"
        min = 0.0
        max = 8.0
        z = 10.0
        splitNumber = 8.0
        radius = StringOrDouble("50%")
        axisLine =
            unsafeJso<GaugeAxisLineOption> {
                lineStyle =
                    unsafeJso<GaugeAxisLineStyleOption> {
                        width = 14.0
                        color = arrayOf(gaugeColorStop(1.0, "#000"))
                    }
            }
        splitLine = unsafeJso<GaugeSplitLineOption> { show = false }
        axisTick = unsafeJso<GaugeAxisTickOption> { show = false }
        axisLabel = unsafeJso<GaugeAxisLabelOption> { show = false }
        anchor = unsafeJso<GaugeAnchorOption> {}
        pointer = unsafeJso<GaugePointerOption> { show = false }
        title = unsafeJso<GaugeTitleOption> { show = false }
        detail =
            unsafeJso<GaugeDetailOption> {
                offsetCenter = gaugeOffset("25%", "50%")
                formatter = LabelFormatter("{a|{value}}{b|km/h}")
                rich = carSpeedRich
            }
        typedData = arrayOf(dialItem(0.0, ""))
    }

/** `gauge-car`, gauge 4: the trip summary ring. */
private fun carTripHeader(): SeriesOption =
    gaugeSeries {
        name = "gauge 4"
        min = 0.0
        max = 8.0
        startAngle = 135.0
        endAngle = -150.0
        splitNumber = 8.0
        radius = StringOrDouble("35%")
        center = arrayOf(StringOrDouble("79%"), StringOrDouble("55%"))
        axisLine =
            unsafeJso<GaugeAxisLineOption> {
                lineStyle = unsafeJso<GaugeAxisLineStyleOption> { color = arrayOf(gaugeColorStop(1.0, "#AE96A6")) }
            }
        splitLine = unsafeJso<GaugeSplitLineOption> { show = false }
        axisTick = unsafeJso<GaugeAxisTickOption> { show = false }
        axisLabel = unsafeJso<GaugeAxisLabelOption> { show = false }
        anchor = unsafeJso<GaugeAnchorOption> {}
        pointer = unsafeJso<GaugePointerOption> { show = false }
        title = unsafeJso<GaugeTitleOption> {}
        detail =
            unsafeJso<GaugeDetailOption> {
                offsetCenter = gaugeOffset("-15%", "0%")
                formatter =
                    LabelFormatter(
                        "{a|                  00:00}\n" +
                            "{a|行驶时间       0:00}{b| h}\n" +
                            "{a|行驶距离        0.0}{b| km}\n" +
                            "{a|平均耗能        ---}{b| 1/100km}\n" +
                            "{a|平均速度        ---}{b| km/h}",
                    )
                rich = carTripRich
            }
        progress =
            unsafeJso<GaugeProgressOption> {
                show = true
                width = 3.0
                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#fff") }
            }
        typedData = arrayOf(dialItem(250.0, ""))
    }

/** `gauge-car`, gauge 5: the fuel ring with the pump icon. */
private fun carFuelDial(): SeriesOption =
    gaugeSeries {
        name = "gauge 5"
        min = 0.0
        max = 1.0
        startAngle = 125.0
        endAngle = 55.0
        splitNumber = 2.0
        radius = StringOrDouble("34%")
        center = arrayOf(StringOrDouble("79%"), StringOrDouble("55.3%"))
        axisLine =
            unsafeJso<GaugeAxisLineOption> {
                lineStyle =
                    unsafeJso<GaugeAxisLineStyleOption> {
                        width = 9.0
                        color =
                            arrayOf(
                                gaugeColorStop(0.15, "#f00"),
                                gaugeColorStop(1.0, "rgba(255, 0, 0, 0)"),
                            )
                    }
            }
        splitLine =
            unsafeJso<GaugeSplitLineOption> {
                distance = -14.0
                length = 16.0
                lineStyle = carTickStyle(4.0)
            }
        axisTick =
            unsafeJso<GaugeAxisTickOption> {
                distance = -14.0
                length = 10.0
                lineStyle = carTickStyle(2.0)
            }
        axisLabel =
            carAxisLabel(
                12.0,
                18.0,
                js("(value) => value === 0.5 ? '2/4' : value === 1 ? '4/4' : value + ''") as LabelFormatterCallback,
            )
        progress =
            unsafeJso<GaugeProgressOption> {
                show = true
                width = 5.0
                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#fff") }
            }
        anchor =
            unsafeJso<GaugeAnchorOption> {
                show = true
                itemStyle = unsafeJso<ItemStyleOption> {}
                offsetCenter = gaugeOffset("-22%", "-57%")
                size = 18.0
                icon = CAR_FUEL_PATH
            }
        pointer = unsafeJso<GaugePointerOption> { show = false }
        title = unsafeJso<GaugeTitleOption> {}
        detail =
            unsafeJso<GaugeDetailOption> {
                offsetCenter = gaugeOffset("10%", "-56%")
                formatter = LabelFormatter("{a|831}{b| km}")
                rich = carOdometerRich
            }
        typedData = arrayOf(dialItem(0.85, ""))
    }

/** `gauge-car`, gauge 6: the reverse-graded temperature dial. */
private fun carGearDial(): SeriesOption =
    gaugeSeries {
        name = "gauge 6"
        min = -120.0
        max = -60.0
        startAngle = 230.0
        endAngle = 310.0
        clockwise = false
        splitNumber = 2.0
        radius = StringOrDouble("35%")
        center = arrayOf(StringOrDouble("79%"), StringOrDouble("55%"))
        axisLine =
            unsafeJso<GaugeAxisLineOption> {
                lineStyle =
                    unsafeJso<GaugeAxisLineStyleOption> {
                        color =
                            arrayOf(
                                gaugeColorStop(1.0, "#AE96A6"),
                                gaugeColorStop(1.1, "#f00"),
                            )
                    }
            }
        splitLine =
            unsafeJso<GaugeSplitLineOption> {
                distance = -8.0
                length = 12.0
                lineStyle = carTickStyle(4.0)
            }
        axisTick =
            unsafeJso<GaugeAxisTickOption> {
                splitNumber = 3.0
                length = 8.0
                distance = -8.0
                lineStyle = carTickStyle(2.0)
            }
        axisLabel =
            carAxisLabel(
                14.0,
                18.0,
                js("(value) => -value + ''") as LabelFormatterCallback,
            )
        anchor =
            unsafeJso<GaugeAnchorOption> {
                show = true
                itemStyle = unsafeJso<ItemStyleOption> {}
                offsetCenter = gaugeCenter("55%")
                size = 20.0
                icon = CAR_GEAR_PATH
            }
        pointer =
            unsafeJso<GaugePointerOption> {
                icon = NEEDLE_PATH
                width = 15.0
                length = StringOrDouble("4")
                offsetCenter = gaugeCenter("-90%")
                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#f00") }
            }
        title = unsafeJso<GaugeTitleOption> {}
        detail = unsafeJso<GaugeDetailOption> { show = false }
        typedData = arrayOf(dialItem(-120.0, ""))
    }

/** The red glowing needle style of `gauge-car`. */
private fun carNeedleStyle(): ItemStyleOption =
    unsafeJso<ItemStyleOption> {
        color = ZRColor("#f00")
        shadowColor = ZRColor("rgba(255, 0, 0)")
        shadowBlur = 5.0
        shadowOffsetY = 2.0
    }
