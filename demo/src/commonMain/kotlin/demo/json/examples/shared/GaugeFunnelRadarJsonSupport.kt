package demo.json.examples.shared

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*
import demo.json.examples.dataset.row
import demo.json.namedValues

/** A line style from the few properties these pages set. */
internal fun stroke(
    color: String? = null,
    width: Double? = null,
    opacity: Double? = null,
    type: String? = null,
    shadowColor: String? = null,
    shadowBlur: Double? = null,
    shadowOffsetX: Double? = null,
    shadowOffsetY: Double? = null,
): LineStyleOption =
    LineStyleOption().apply {
        color?.let { this.color = it.asColor() }
        this.width = width
        this.opacity = opacity
        this.type = type
        shadowColor?.let { this.shadowColor = it.asColor() }
        this.shadowBlur = shadowBlur
        this.shadowOffsetX = shadowOffsetX
        this.shadowOffsetY = shadowOffsetY
    }

/** An item style from the few properties these pages set. */
internal fun fill(
    color: String? = null,
    borderColor: String? = null,
    borderWidth: Double? = null,
    opacity: Double? = null,
    shadowColor: String? = null,
    shadowBlur: Double? = null,
    shadowOffsetX: Double? = null,
    shadowOffsetY: Double? = null,
): ItemStyleOption =
    ItemStyleOption().apply {
        color?.let { this.color = it.asColor() }
        borderColor?.let { this.borderColor = it.asColor() }
        this.borderWidth = borderWidth
        this.opacity = opacity
        shadowColor?.let { this.shadowColor = it.asColor() }
        this.shadowBlur = shadowBlur
        this.shadowOffsetX = shadowOffsetX
        this.shadowOffsetY = shadowOffsetY
    }

/**
 * The `axisLine` of a gauge.
 *
 * The color is deliberately absent: ECharts reads the gauge `axisLine.lineStyle.color` as a list of
 * `[percent, color]` pairs, so a single `EChartsColor` cannot be written there.
 */
internal fun gaugeAxisLine(show: Boolean? = null, width: Double? = null): AxisLineOption =
    AxisLineOption().apply {
        this.show = show
        if (width != null) lineStyle = stroke(width = width)
    }

/** The `axisTick` of a gauge; the model has no `distance` or `splitNumber`. */
internal fun gaugeAxisTick(
    show: Boolean? = null,
    length: Double? = null,
    lineStyle: LineStyleOption? = null,
): AxisTickOption =
    AxisTickOption().apply {
        this.show = show
        this.length = length
        this.lineStyle = lineStyle
    }

/** The `splitLine` of a gauge; the model has no `distance` or `length`. */
internal fun gaugeSplitLine(show: Boolean? = null, lineStyle: LineStyleOption? = null): SplitLineOption =
    SplitLineOption().apply {
        this.show = show
        this.lineStyle = lineStyle
    }

/** The `axisLabel` of a gauge; the model has no `distance`. */
internal fun gaugeAxisLabel(
    show: Boolean? = null,
    color: String? = null,
    fontSize: Double? = null,
    fontFamily: String? = null,
    fontWeight: NumberOrString? = null,
    formatter: String? = null,
): AxisLabelOption =
    AxisLabelOption().apply {
        this.show = show
        color?.let { this.color = it.asColor() }
        this.fontSize = fontSize
        this.fontFamily = fontFamily
        this.fontWeight = fontWeight
        this.formatter = formatter
    }

/** A gauge `title` or `detail` block; the model has no `offsetCenter` or `precision`. */
internal fun labelOf(
    show: Boolean? = null,
    formatter: String? = null,
    color: String? = null,
    fontSize: Double? = null,
    fontWeight: NumberOrString? = null,
    fontFamily: String? = null,
    lineHeight: Double? = null,
    width: NumberOrString? = null,
    height: NumberOrString? = null,
    borderRadius: Number? = null,
    backgroundColor: String? = null,
    borderColor: String? = null,
    borderWidth: Double? = null,
    padding: List<Number>? = null,
    valueAnimation: Boolean? = null,
    align: String? = null,
    rich: Map<String, TextStyleOption>? = null,
): LabelOption =
    LabelOption().apply {
        this.show = show
        this.formatter = formatter
        color?.let { this.color = it.asColor() }
        this.fontSize = fontSize
        this.fontWeight = fontWeight
        this.fontFamily = fontFamily
        this.lineHeight = lineHeight
        this.width = width
        this.height = height
        this.borderRadius = borderRadius?.let { NumberOrStringOrNumbers.of(it) }
        backgroundColor?.let { this.backgroundColor = it.asColor() }
        borderColor?.let { this.borderColor = it.asColor() }
        this.borderWidth = borderWidth
        this.padding = padding?.let { NumberOrStringOrNumbers.numbers(*it.toTypedArray()) }
        this.valueAnimation = valueAnimation
        this.align = align
        this.rich = rich
    }

/** The `progress` bar of a gauge; the model has no `overlap` or `clip`. */
internal fun gaugeProgress(
    show: Boolean? = null,
    width: Double? = null,
    roundCap: Boolean? = null,
    color: String? = null,
): GaugeProgressOption =
    GaugeProgressOption().apply {
        this.show = show
        this.width = width
        this.roundCap = roundCap
        if (color != null) itemStyle = fill(color = color)
    }

/** The `itemStyle` of a `pointer` or `anchor` JSON block. */
internal fun pointerStyle(
    color: String? = null,
    borderColor: String? = null,
    borderWidth: Double? = null,
    shadowColor: String? = null,
    shadowBlur: Double? = null,
    shadowOffsetX: Double? = null,
    shadowOffsetY: Double? = null,
): JsonObject =
    buildJsonObject {
        color?.let { put("color", it) }
        borderColor?.let { put("borderColor", it) }
        borderWidth?.let { put("borderWidth", it) }
        shadowColor?.let { put("shadowColor", it) }
        shadowBlur?.let { put("shadowBlur", it) }
        shadowOffsetX?.let { put("shadowOffsetX", it) }
        shadowOffsetY?.let { put("shadowOffsetY", it) }
    }

/** A gauge `pointer` block; `offsetCenter` is `[x, y]` of percentages. */
internal fun gaugePointer(
    show: Boolean? = null,
    icon: String? = null,
    width: Double? = null,
    length: String? = null,
    offsetCenter: Pair<String, String>? = null,
    itemStyle: JsonObject? = null,
    keepAspect: Boolean? = null,
): JsonElement =
    buildJsonObject {
        show?.let { put("show", it) }
        icon?.let { put("icon", it) }
        width?.let { put("width", it) }
        length?.let { put("length", it) }
        keepAspect?.let { put("keepAspect", it) }
        offsetCenter?.let { (x, y) ->
            put("offsetCenter", buildJsonArray { add(x); add(y) })
        }
        itemStyle?.let { put("itemStyle", it) }
    }

/** A gauge `anchor` block. */
internal fun gaugeAnchor(
    show: Boolean? = null,
    showAbove: Boolean? = null,
    size: Double? = null,
    icon: String? = null,
    keepAspect: Boolean? = null,
    offsetCenter: Pair<String, String>? = null,
    itemStyle: JsonObject? = null,
): JsonElement =
    buildJsonObject {
        show?.let { put("show", it) }
        showAbove?.let { put("showAbove", it) }
        size?.let { put("size", it) }
        icon?.let { put("icon", it) }
        keepAspect?.let { put("keepAspect", it) }
        offsetCenter?.let { (x, y) ->
            put("offsetCenter", buildJsonArray { add(x); add(y) })
        }
        itemStyle?.let { put("itemStyle", it) }
    }

/** The `{a} <br/>{b} : {c}%` tooltip of the gauge pages. */
internal fun EChartsOption.gaugeTooltip() {
    tooltip { formatter = "{a} <br/>{b} : {c}%" }
}

// ---------------------------------------------------------------------------------------------
// Gauge dial faces
// ---------------------------------------------------------------------------------------------

/** The wide needle of `gauge-speed`, copied verbatim from the upstream page. */
internal const val WIDE_NEEDLE_PATH =
    "path://M2090.36389,615.30999 L2090.36389,615.30999 C2091.48372,615.30999 2092.40383,616.194028 " +
        "2092.44859,617.312956 L2096.90698,728.755929 C2097.05155,732.369577 2094.2393,735.416212 " +
        "2090.62566,735.56078 C2090.53845,735.564269 2090.45117,735.566014 2090.36389,735.566014 " +
        "L2090.36389,735.566014 C2086.74736,735.566014 2083.81557,732.63423 2083.81557,729.017692 " +
        "C2083.81557,728.930412 2083.81732,728.84314 2083.82081,728.755929 L2088.2792,617.312956 " +
        "C2088.32396,616.194028 2089.24407,615.30999 2090.36389,615.30999 Z"

/** The pointed needle of `gauge-car`. */
internal const val CAR_NEEDLE_PATH =
    "path://M-36.5,23.9L-41,4.4c-0.1-0.4-0.4-0.7-0.7-0.7c-0.5-0.1-1.1,0.2-1.2,0.7l-4.5,19.5c0,0.1,0,0.1,0,0.2v92.3" +
        "c0,0.6,0.4,1,1,1h9c0.6,0,1-0.4,1-1V24.1C-36.5,24-36.5,23.9-36.5,23.9z M-39.5,114.6h-5v-85h5V114.6z"

/** The tapered needle of `gauge-car`, copied from the upstream page. */
internal const val NEEDLE_PATH =
    "path://M2.9,0.7L2.9,0.7c1.4,0,2.6,1.2,2.6,2.6v115c0,1.4-1.2,2.6-2.6,2.6l0,0c-1.4,0-2.6-1.2-2.6-2.6V3.3C0.3,1.9,1.4,0.7,2.9,0.7z"

/** The fuel-pump icon of `gauge-car`. */
internal const val CAR_FUEL_PATH =
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
internal const val CAR_GEAR_PATH =
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

/** An Arial numeral style of `gauge-car`. */
internal fun carText(
    size: Double,
    weight: Double,
    align: String? = null,
    lineHeight: Double? = null,
    padding: List<Number>? = null,
): TextStyleOption =
    TextStyleOption().apply {
        fontSize = size
        fontWeight = num(weight)
        fontFamily = "Arial"
        color = "#fff".asColor()
        this.align = align
        this.lineHeight = lineHeight
        this.padding = padding?.let { NumberOrStringOrNumbers.numbers(*it.toTypedArray()) }
    }

/** The readout styles of the middle speed display of `gauge-car`. */
internal val carSpeedRich: Map<String, TextStyleOption> =
    mapOf(
        "a" to carText(60.0, 800.0, align = "center", padding = listOf<Number>(0, 5, 0, 0)),
        "b" to carText(14.0, 800.0, padding = listOf<Number>(0, 0, 20, 0)),
    )

/** The readout styles of the trip summary of `gauge-car`. */
internal val carTripRich: Map<String, TextStyleOption> =
    mapOf(
        "a" to carText(14.0, 800.0, align = "left", lineHeight = 22.0),
        "b" to carText(12.0, 600.0, align = "left", lineHeight = 22.0),
    )

/** The readout styles of the odometer of `gauge-car`. */
internal val carOdometerRich: Map<String, TextStyleOption> =
    mapOf(
        "a" to carText(15.0, 800.0),
        "b" to carText(12.0, 600.0),
    )

/** The red glowing needle style of `gauge-car`. */
internal val carNeedleStyle: JsonObject =
    pointerStyle(
        color = "#f00",
        shadowColor = "rgba(255, 0, 0)",
        shadowBlur = 5.0,
        shadowOffsetY = 2.0,
    )

/** The `{value|…}{unit|km/h}` readout of `gauge-speed`. */
internal val speedRich: Map<String, TextStyleOption> =
    mapOf(
        "value" to
            TextStyleOption().apply {
                fontSize = 50.0
                fontWeight = str("bolder")
                color = "#777".asColor()
            },
        "unit" to
            TextStyleOption().apply {
                fontSize = 20.0
                color = "#999".asColor()
                padding = NumberOrStringOrNumbers.of(num(0), num(0), num(-20), num(10))
            },
    )

/** `gauge-car`, gauge 0: the outer speed ring. */
internal fun SeriesBuilder.carSpeedDial() {
    gauge {
        name = "gauge 0"
        min = -200.0
        max = 250.0
        startAngle = -30.0
        endAngle = -315.0
        splitNumber = 9
        radius = NumberOrStringOrNumbers.of("35%")
        center = listOf(str("21%"), str("55%"))
        splitLine = gaugeSplitLine(show = false)
        axisTick = gaugeAxisTick(show = false)
        axisLabel = gaugeAxisLabel(show = false)
        pointer = gaugePointer(show = false)
        detail = labelOf(show = false)
        title = labelOf(fontSize = 12.0, fontWeight = num(800), fontFamily = "Arial", color = "#fff")
        progress = gaugeProgress(show = true, width = 3.0, color = "#fff")
        data = namedValues("km/h" to 250)
    }
}

/** `gauge-car`, gauge 1: the small position dial with its own needle. */
internal fun SeriesBuilder.carPositionDial() {
    gauge {
        name = "gauge 1"
        min = 0.0
        max = 250.0
        startAngle = -140.0
        endAngle = -305.0
        splitNumber = 5
        radius = NumberOrStringOrNumbers.of("35%")
        center = listOf(str("21%"), str("55%"))
        splitLine = gaugeSplitLine(lineStyle = stroke(color = "#fff", width = 4.0))
        axisTick = gaugeAxisTick(length = 8.0, lineStyle = stroke(color = "#fff", width = 2.0))
        axisLabel =
            gaugeAxisLabel(fontSize = 18.0, fontFamily = "Arial", fontWeight = num(800), color = "#fff")
        pointer =
            gaugePointer(
                icon = CAR_NEEDLE_PATH,
                width = 5.0,
                length = "40%",
                offsetCenter = "0%" to "-58%",
                itemStyle = carNeedleStyle,
            )
        title = labelOf(fontSize = 14.0, fontWeight = num(800), fontFamily = "Arial", color = "#fff")
        detail = labelOf(show = false)
        data = namedValues("当前位置：\n \n 中科路" to 0)
    }
}

/** `gauge-car`, gauge 2: the central tachometer. */
internal fun SeriesBuilder.carRevDial() {
    gauge {
        name = "gauge 2"
        min = 0.0
        max = 8.0
        z = 10.0
        startAngle = 210.0
        endAngle = -30.0
        splitNumber = 8
        radius = NumberOrStringOrNumbers.of("50%")
        center = listOf(str("50%"), str("50%"))
        axisLine = gaugeAxisLine(show = true, width = 0.0)
        splitLine =
            gaugeSplitLine(
                lineStyle =
                    stroke(
                        color = "auto",
                        width = 4.0,
                        shadowColor = "rgba(255, 255, 255, 0.5)",
                        shadowBlur = 15.0,
                        shadowOffsetY = -10.0,
                    ),
            )
        axisTick =
            gaugeAxisTick(
                length = 8.0,
                lineStyle =
                    stroke(
                        color = "auto",
                        width = 2.0,
                        shadowColor = "rgba(255, 255, 255)",
                        shadowBlur = 10.0,
                        shadowOffsetY = -10.0,
                    ),
            )
        axisLabel =
            gaugeAxisLabel(fontSize = 35.0, fontFamily = "Arial", fontWeight = num(800), color = "#fff")
        pointer =
            gaugePointer(
                icon = CAR_NEEDLE_PATH,
                width = 10.0,
                length = "75%",
                offsetCenter = "0%" to "-10%",
                itemStyle = carNeedleStyle,
            )
        title = labelOf(fontSize = 12.0, fontWeight = num(800), fontFamily = "Arial", color = "#fff")
        detail = labelOf(show = false)
        data = namedValues("1/min x 1000" to 0.6)
    }
}

/** `gauge-car`, gauge 3: the digital speed readout in the middle. */
internal fun SeriesBuilder.carSpeedReadout() {
    gauge {
        name = "gauge 3"
        min = 0.0
        max = 8.0
        z = 10.0
        splitNumber = 8
        radius = NumberOrStringOrNumbers.of("50%")
        axisLine = gaugeAxisLine(width = 14.0)
        splitLine = gaugeSplitLine(show = false)
        axisTick = gaugeAxisTick(show = false)
        axisLabel = gaugeAxisLabel(show = false)
        pointer = gaugePointer(show = false)
        title = labelOf(show = false)
        detail = labelOf(formatter = "{a|{value}}{b|km/h}", rich = carSpeedRich)
        data = namedValues("" to 0)
    }
}

/** `gauge-car`, gauge 4: the trip summary ring. */
internal fun SeriesBuilder.carTripHeader() {
    gauge {
        name = "gauge 4"
        min = 0.0
        max = 8.0
        startAngle = 135.0
        endAngle = -150.0
        splitNumber = 8
        radius = NumberOrStringOrNumbers.of("35%")
        center = listOf(str("79%"), str("55%"))
        splitLine = gaugeSplitLine(show = false)
        axisTick = gaugeAxisTick(show = false)
        axisLabel = gaugeAxisLabel(show = false)
        pointer = gaugePointer(show = false)
        detail =
            labelOf(
                formatter =
                    "{a|                  00:00}\n" +
                        "{a|行驶时间       0:00}{b| h}\n" +
                        "{a|行驶距离        0.0}{b| km}\n" +
                        "{a|平均耗能        ---}{b| 1/100km}\n" +
                        "{a|平均速度        ---}{b| km/h}",
                rich = carTripRich,
            )
        progress = gaugeProgress(show = true, width = 3.0, color = "#fff")
        data = namedValues("" to 250)
    }
}

/** `gauge-car`, gauge 5: the fuel ring with the pump icon. */
internal fun SeriesBuilder.carFuelDial() {
    gauge {
        name = "gauge 5"
        min = 0.0
        max = 1.0
        startAngle = 125.0
        endAngle = 55.0
        splitNumber = 2
        radius = NumberOrStringOrNumbers.of("34%")
        center = listOf(str("79%"), str("55.3%"))
        axisLine = gaugeAxisLine(width = 9.0)
        splitLine = gaugeSplitLine(lineStyle = stroke(color = "#fff", width = 4.0))
        axisTick = gaugeAxisTick(length = 10.0, lineStyle = stroke(color = "#fff", width = 2.0))
        axisLabel =
            gaugeAxisLabel(fontSize = 18.0, fontFamily = "Arial", fontWeight = num(800), color = "#fff")
        progress = gaugeProgress(show = true, width = 5.0, color = "#fff")
        anchor =
            gaugeAnchor(
                show = true,
                size = 18.0,
                icon = CAR_FUEL_PATH,
                offsetCenter = "-22%" to "-57%",
            )
        pointer = gaugePointer(show = false)
        detail = labelOf(formatter = "{a|831}{b| km}", rich = carOdometerRich)
        data = namedValues("" to 0.85)
    }
}

/** `gauge-car`, gauge 6: the reverse-graded temperature dial. */
internal fun SeriesBuilder.carGearDial() {
    gauge {
        name = "gauge 6"
        min = -120.0
        max = -60.0
        startAngle = 230.0
        endAngle = 310.0
        clockwise = false
        splitNumber = 2
        radius = NumberOrStringOrNumbers.of("35%")
        center = listOf(str("79%"), str("55%"))
        splitLine = gaugeSplitLine(lineStyle = stroke(color = "#fff", width = 4.0))
        axisTick = gaugeAxisTick(length = 8.0, lineStyle = stroke(color = "#fff", width = 2.0))
        axisLabel =
            gaugeAxisLabel(fontSize = 18.0, fontFamily = "Arial", fontWeight = num(800), color = "#fff")
        anchor =
            gaugeAnchor(
                show = true,
                size = 20.0,
                icon = CAR_GEAR_PATH,
                offsetCenter = "0%" to "55%",
            )
        pointer =
            gaugePointer(
                icon = NEEDLE_PATH,
                width = 15.0,
                length = "4",
                offsetCenter = "0%" to "-90%",
                itemStyle = pointerStyle(color = "#f00"),
            )
        detail = labelOf(show = false)
        data = namedValues("" to -120)
    }
}

// ---------------------------------------------------------------------------------------------
// Funnel and radar helpers
// ---------------------------------------------------------------------------------------------

/** The five stages of the funnel pages, in the order the originals declare them. */
internal val FUNNEL_LABELS = arrayOf("Show", "Click", "Visit", "Inquiry", "Order")

/** The `{ name, value }` rows of the funnel pages. */
internal fun stages(vararg values: Number): List<DataValue> =
    namedValues(*Array(values.size) { index -> FUNNEL_LABELS[index] to values[index] })

/** The four compared products of `funnel-align`. */
internal fun compareStages(): List<DataValue> =
    namedValues(
        "Prod C" to 60,
        "Prod D" to 30,
        "Prod E" to 10,
        "Prod B" to 80,
        "Prod A" to 100,
    )

/** The `dataView` / `restore` / `saveAsImage` toolbox of the funnel pages. */
internal fun EChartsOption.funnelToolbox(orient: Orient? = null, top: NumberOrString? = null) {
    toolbox {
        this.orient = orient
        this.top = top
        feature =
            ToolboxFeatureOption().apply {
                dataView = ToolboxDataViewFeature().apply { readOnly = false }
                restore = ToolboxFeature()
                saveAsImage = ToolboxSaveAsImageFeature()
            }
    }
}

/** One quadrant of `funnel-align`. */
internal fun SeriesBuilder.compareFunnel(
    seriesName: String,
    left: String,
    top: String,
    align: String,
    ascending: Boolean,
) {
    funnel {
        name = seriesName
        width = str("40%")
        height = str("45%")
        this.left = str(left)
        this.top = str(top)
        funnelAlign = align
        if (ascending) sort = BooleanOrString.of("ascending")
        data = compareStages()
    }
}

/** One quadrant of `funnel-mutiple`; the right-hand ones put the label outside. */
internal fun SeriesBuilder.quadrantFunnel(
    seriesName: String,
    left: String,
    top: String,
    labelled: Boolean,
) {
    funnel {
        name = seriesName
        width = str("40%")
        height = str("45%")
        this.left = str(left)
        this.top = str(top)
        if (seriesName == "Pyramid") sort = BooleanOrString.of("ascending")
        if (labelled) label { position = "left" }
        data = stages(60, 30, 10, 80, 100)
    }
}

/** One indicator of a radar coordinate system. */
internal fun indicator(name: String, max: Double? = null): RadarIndicator =
    RadarIndicator().apply {
        this.name = name
        this.max = max
    }

/** An unnamed radar row: one value per indicator. */
internal fun radarRow(vararg values: Number): DataValue =
    DataValue.Array(values.map { valueOf(it) })

/** A named radar data item. */
internal fun radarItem(itemName: String, vararg values: Number): DataValue =
    dataItem {
        name = itemName
        value = radarRow(*values)
    }

/** The AQI tables of `radar-aqi`; each row is `[AQI, PM2.5, PM10, CO, NO2, SO2, day]`. */
internal val BEIJING_AQI =
    listOf(
        doubleArrayOf(55.0, 9.0, 56.0, 0.46, 18.0, 6.0, 1.0),
        doubleArrayOf(25.0, 11.0, 21.0, 0.65, 34.0, 9.0, 2.0),
        doubleArrayOf(56.0, 7.0, 63.0, 0.3, 14.0, 5.0, 3.0),
        doubleArrayOf(33.0, 7.0, 29.0, 0.33, 16.0, 6.0, 4.0),
        doubleArrayOf(42.0, 24.0, 44.0, 0.76, 40.0, 16.0, 5.0),
        doubleArrayOf(82.0, 58.0, 90.0, 1.77, 68.0, 33.0, 6.0),
        doubleArrayOf(74.0, 49.0, 77.0, 1.46, 48.0, 27.0, 7.0),
        doubleArrayOf(78.0, 55.0, 80.0, 1.29, 59.0, 29.0, 8.0),
        doubleArrayOf(267.0, 216.0, 280.0, 4.8, 108.0, 64.0, 9.0),
        doubleArrayOf(185.0, 127.0, 216.0, 2.52, 61.0, 27.0, 10.0),
        doubleArrayOf(39.0, 19.0, 38.0, 0.57, 31.0, 15.0, 11.0),
        doubleArrayOf(41.0, 11.0, 40.0, 0.43, 21.0, 7.0, 12.0),
        doubleArrayOf(64.0, 38.0, 74.0, 1.04, 46.0, 22.0, 13.0),
        doubleArrayOf(108.0, 79.0, 120.0, 1.7, 75.0, 41.0, 14.0),
        doubleArrayOf(108.0, 63.0, 116.0, 1.48, 44.0, 26.0, 15.0),
        doubleArrayOf(33.0, 6.0, 29.0, 0.34, 13.0, 5.0, 16.0),
        doubleArrayOf(94.0, 66.0, 110.0, 1.54, 62.0, 31.0, 17.0),
        doubleArrayOf(186.0, 142.0, 192.0, 3.88, 93.0, 79.0, 18.0),
        doubleArrayOf(57.0, 31.0, 54.0, 0.96, 32.0, 14.0, 19.0),
        doubleArrayOf(22.0, 8.0, 17.0, 0.48, 23.0, 10.0, 20.0),
        doubleArrayOf(39.0, 15.0, 36.0, 0.61, 29.0, 13.0, 21.0),
        doubleArrayOf(94.0, 69.0, 114.0, 2.08, 73.0, 39.0, 22.0),
        doubleArrayOf(99.0, 73.0, 110.0, 2.43, 76.0, 48.0, 23.0),
        doubleArrayOf(31.0, 12.0, 30.0, 0.5, 32.0, 16.0, 24.0),
        doubleArrayOf(42.0, 27.0, 43.0, 1.0, 53.0, 22.0, 25.0),
        doubleArrayOf(154.0, 117.0, 157.0, 3.05, 92.0, 58.0, 26.0),
        doubleArrayOf(234.0, 185.0, 230.0, 4.09, 123.0, 69.0, 27.0),
        doubleArrayOf(160.0, 120.0, 186.0, 2.77, 91.0, 50.0, 28.0),
        doubleArrayOf(134.0, 96.0, 165.0, 2.76, 83.0, 41.0, 29.0),
        doubleArrayOf(52.0, 24.0, 60.0, 1.03, 50.0, 21.0, 30.0),
        doubleArrayOf(46.0, 5.0, 49.0, 0.28, 10.0, 6.0, 31.0),
    )

internal val GUANGZHOU_AQI =
    listOf(
        doubleArrayOf(26.0, 37.0, 27.0, 1.163, 27.0, 13.0, 1.0),
        doubleArrayOf(85.0, 62.0, 71.0, 1.195, 60.0, 8.0, 2.0),
        doubleArrayOf(78.0, 38.0, 74.0, 1.363, 37.0, 7.0, 3.0),
        doubleArrayOf(21.0, 21.0, 36.0, 0.634, 40.0, 9.0, 4.0),
        doubleArrayOf(41.0, 42.0, 46.0, 0.915, 81.0, 13.0, 5.0),
        doubleArrayOf(56.0, 52.0, 69.0, 1.067, 92.0, 16.0, 6.0),
        doubleArrayOf(64.0, 30.0, 28.0, 0.924, 51.0, 2.0, 7.0),
        doubleArrayOf(55.0, 48.0, 74.0, 1.236, 75.0, 26.0, 8.0),
        doubleArrayOf(76.0, 85.0, 113.0, 1.237, 114.0, 27.0, 9.0),
        doubleArrayOf(91.0, 81.0, 104.0, 1.041, 56.0, 40.0, 10.0),
        doubleArrayOf(84.0, 39.0, 60.0, 0.964, 25.0, 11.0, 11.0),
        doubleArrayOf(64.0, 51.0, 101.0, 0.862, 58.0, 23.0, 12.0),
        doubleArrayOf(70.0, 69.0, 120.0, 1.198, 65.0, 36.0, 13.0),
        doubleArrayOf(77.0, 105.0, 178.0, 2.549, 64.0, 16.0, 14.0),
        doubleArrayOf(109.0, 68.0, 87.0, 0.996, 74.0, 29.0, 15.0),
        doubleArrayOf(73.0, 68.0, 97.0, 0.905, 51.0, 34.0, 16.0),
        doubleArrayOf(54.0, 27.0, 47.0, 0.592, 53.0, 12.0, 17.0),
        doubleArrayOf(51.0, 61.0, 97.0, 0.811, 65.0, 19.0, 18.0),
        doubleArrayOf(91.0, 71.0, 121.0, 1.374, 43.0, 18.0, 19.0),
        doubleArrayOf(73.0, 102.0, 182.0, 2.787, 44.0, 19.0, 20.0),
        doubleArrayOf(73.0, 50.0, 76.0, 0.717, 31.0, 20.0, 21.0),
        doubleArrayOf(84.0, 94.0, 140.0, 2.238, 68.0, 18.0, 22.0),
        doubleArrayOf(93.0, 77.0, 104.0, 1.165, 53.0, 7.0, 23.0),
        doubleArrayOf(99.0, 130.0, 227.0, 3.97, 55.0, 15.0, 24.0),
        doubleArrayOf(146.0, 84.0, 139.0, 1.094, 40.0, 17.0, 25.0),
        doubleArrayOf(113.0, 108.0, 137.0, 1.481, 48.0, 15.0, 26.0),
        doubleArrayOf(81.0, 48.0, 62.0, 1.619, 26.0, 3.0, 27.0),
        doubleArrayOf(56.0, 48.0, 68.0, 1.336, 37.0, 9.0, 28.0),
        doubleArrayOf(82.0, 92.0, 174.0, 3.29, 0.0, 13.0, 29.0),
        doubleArrayOf(106.0, 116.0, 188.0, 3.628, 101.0, 16.0, 30.0),
        doubleArrayOf(118.0, 50.0, 0.0, 1.383, 76.0, 11.0, 31.0),
    )

internal val SHANGHAI_AQI =
    listOf(
        doubleArrayOf(91.0, 45.0, 125.0, 0.82, 34.0, 23.0, 1.0),
        doubleArrayOf(65.0, 27.0, 78.0, 0.86, 45.0, 29.0, 2.0),
        doubleArrayOf(83.0, 60.0, 84.0, 1.09, 73.0, 27.0, 3.0),
        doubleArrayOf(109.0, 81.0, 121.0, 1.28, 68.0, 51.0, 4.0),
        doubleArrayOf(106.0, 77.0, 114.0, 1.07, 55.0, 51.0, 5.0),
        doubleArrayOf(109.0, 81.0, 121.0, 1.28, 68.0, 51.0, 6.0),
        doubleArrayOf(106.0, 77.0, 114.0, 1.07, 55.0, 51.0, 7.0),
        doubleArrayOf(89.0, 65.0, 78.0, 0.86, 51.0, 26.0, 8.0),
        doubleArrayOf(53.0, 33.0, 47.0, 0.64, 50.0, 17.0, 9.0),
        doubleArrayOf(80.0, 55.0, 80.0, 1.01, 75.0, 24.0, 10.0),
        doubleArrayOf(117.0, 81.0, 124.0, 1.03, 45.0, 24.0, 11.0),
        doubleArrayOf(99.0, 71.0, 142.0, 1.1, 62.0, 42.0, 12.0),
        doubleArrayOf(95.0, 69.0, 130.0, 1.28, 74.0, 50.0, 13.0),
        doubleArrayOf(116.0, 87.0, 131.0, 1.47, 84.0, 40.0, 14.0),
        doubleArrayOf(108.0, 80.0, 121.0, 1.3, 85.0, 37.0, 15.0),
        doubleArrayOf(134.0, 83.0, 167.0, 1.16, 57.0, 43.0, 16.0),
        doubleArrayOf(79.0, 43.0, 107.0, 1.05, 59.0, 37.0, 17.0),
        doubleArrayOf(71.0, 46.0, 89.0, 0.86, 64.0, 25.0, 18.0),
        doubleArrayOf(97.0, 71.0, 113.0, 1.17, 88.0, 31.0, 19.0),
        doubleArrayOf(84.0, 57.0, 91.0, 0.85, 55.0, 31.0, 20.0),
        doubleArrayOf(87.0, 63.0, 101.0, 0.9, 56.0, 41.0, 21.0),
        doubleArrayOf(104.0, 77.0, 119.0, 1.09, 73.0, 48.0, 22.0),
        doubleArrayOf(87.0, 62.0, 100.0, 1.0, 72.0, 28.0, 23.0),
        doubleArrayOf(168.0, 128.0, 172.0, 1.49, 97.0, 56.0, 24.0),
        doubleArrayOf(65.0, 45.0, 51.0, 0.74, 39.0, 17.0, 25.0),
        doubleArrayOf(39.0, 24.0, 38.0, 0.61, 47.0, 17.0, 26.0),
        doubleArrayOf(39.0, 24.0, 39.0, 0.59, 50.0, 19.0, 27.0),
        doubleArrayOf(93.0, 68.0, 96.0, 1.05, 79.0, 29.0, 28.0),
        doubleArrayOf(188.0, 143.0, 197.0, 1.66, 99.0, 51.0, 29.0),
        doubleArrayOf(174.0, 131.0, 174.0, 1.55, 108.0, 50.0, 30.0),
        doubleArrayOf(187.0, 143.0, 201.0, 1.39, 89.0, 53.0, 31.0),
    )

/** Maps an AQI table to radar rows. */
internal fun aqiRows(table: List<DoubleArray>): List<DataValue> =
    table.map { row -> radarRow(*row.toTypedArray()) }

/** One of the three city series of `radar-aqi`. */
internal fun SeriesBuilder.aqiSeries(
    seriesName: String,
    color: String,
    areaOpacity: Double,
    rows: List<DataValue>,
) {
    radar {
        name = seriesName
        symbol = "none"
        lineStyle = stroke(width = 1.0, opacity = 0.5)
        itemStyle = fill(color = color)
        areaStyle = AreaStyleOption().apply { opacity = areaOpacity }
        data = rows
    }
}

// ---------------------------------------------------------------------------------------------
// Pages
// ---------------------------------------------------------------------------------------------
