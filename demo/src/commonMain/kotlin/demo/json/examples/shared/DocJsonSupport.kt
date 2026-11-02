package demo.json.examples.shared

import demo.json.namedValues
import demo.json.points
import demo.json.triples
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import demo.json.examples.dataset.row
import demo.json.examples.doc.hidden

/** The five slices every `tutorial-styling-step*` page shares. */
internal val VISIT_SOURCE =
    namedValues(
        "视频广告" to 235,
        "联盟广告" to 274,
        "邮件营销" to 310,
        "直接访问" to 335,
        "搜索引擎" to 400,
    )

/** The seven slices of the `pie-label-*` pages. */
internal fun pieLabelSlices(longNames: Boolean): List<DataValue> {
    val names =
        if (longNames) {
            listOf(
                "Apple Numbers",
                "Strawberry Numbers",
                "Banana Numbers",
                "Orange Numbers",
                "Pear Numbers",
                "Pineapple Numbers",
                "Grape Numbers",
            )
        } else {
            listOf("Apples", "Strawberries", "Bananas", "Oranges", "Pears", "Pineapples", "Grapes")
        }
    val values = listOf(70, 68, 48, 40, 32, 27, 18)
    return names.zip(values) { name, value ->
        dataItem {
            this.name = name
            this.value = valueOf(value)
        }
    }
}

/** One `[date, AQI, PM2.5, PM10, CO, NO2, SO2, grade]` row of the AQI tables. */
internal fun aqiRow(
    day: Int,
    aqi: Int,
    pm25: Int,
    pm10: Int,
    co: Number,
    no2: Int,
    so2: Int,
    grade: String,
): DataValue =
    DataValue.Array(
        listOf(
            valueOf(day),
            valueOf(aqi),
            valueOf(pm25),
            valueOf(pm10),
            valueOf(co),
            valueOf(no2),
            valueOf(so2),
            textValue(grade),
        ),
    )

/** 北京, 31 days; shared by `parallel-all` and `scatter-visualMap-categories`. */
internal val AQI_BJ: List<DataValue> =
    listOf(
        aqiRow(1, 55, 9, 56, 0.46, 18, 6, "良"),
        aqiRow(2, 25, 11, 21, 0.65, 34, 9, "优"),
        aqiRow(3, 56, 7, 63, 0.3, 14, 5, "良"),
        aqiRow(4, 33, 7, 29, 0.33, 16, 6, "优"),
        aqiRow(5, 42, 24, 44, 0.76, 40, 16, "优"),
        aqiRow(6, 82, 58, 90, 1.77, 68, 33, "良"),
        aqiRow(7, 74, 49, 77, 1.46, 48, 27, "良"),
        aqiRow(8, 78, 55, 80, 1.29, 59, 29, "良"),
        // Upstream writes "-" for CO on this day; the shared table keeps the numeric value.
        aqiRow(9, 267, 216, 280, 4.8, 108, 64, "重度污染"),
        aqiRow(10, 185, 127, 216, 2.52, 61, 27, "中度污染"),
        aqiRow(11, 39, 19, 38, 0.57, 31, 15, "优"),
        aqiRow(12, 41, 11, 40, 0.43, 21, 7, "优"),
        aqiRow(13, 64, 38, 74, 1.04, 46, 22, "良"),
        aqiRow(14, 108, 79, 120, 1.7, 75, 41, "轻度污染"),
        aqiRow(15, 108, 63, 116, 1.48, 44, 26, "轻度污染"),
        aqiRow(16, 33, 6, 29, 0.34, 13, 5, "优"),
        aqiRow(17, 94, 66, 110, 1.54, 62, 31, "良"),
        aqiRow(18, 186, 142, 192, 3.88, 93, 79, "中度污染"),
        aqiRow(19, 57, 31, 54, 0.96, 32, 14, "良"),
        aqiRow(20, 22, 8, 17, 0.48, 23, 10, "优"),
        aqiRow(21, 39, 15, 36, 0.61, 29, 13, "优"),
        aqiRow(22, 94, 69, 114, 2.08, 73, 39, "良"),
        aqiRow(23, 99, 73, 110, 2.43, 76, 48, "良"),
        aqiRow(24, 31, 12, 30, 0.5, 32, 16, "优"),
        aqiRow(25, 42, 27, 43, 1, 53, 22, "优"),
        aqiRow(26, 154, 117, 157, 3.05, 92, 58, "中度污染"),
        aqiRow(27, 234, 185, 230, 4.09, 123, 69, "重度污染"),
        aqiRow(28, 160, 120, 186, 2.77, 91, 50, "中度污染"),
        aqiRow(29, 134, 96, 165, 2.76, 83, 41, "轻度污染"),
        aqiRow(30, 52, 24, 60, 1.03, 50, 21, "良"),
        aqiRow(31, 46, 5, 49, 0.28, 10, 6, "优"),
    )

/** 广州, 31 days. */
internal val AQI_GZ: List<DataValue> =
    listOf(
        aqiRow(1, 26, 37, 27, 1.163, 27, 13, "优"),
        aqiRow(2, 85, 62, 71, 1.195, 60, 8, "良"),
        aqiRow(3, 78, 38, 74, 1.363, 37, 7, "良"),
        aqiRow(4, 21, 21, 36, 0.634, 40, 9, "优"),
        aqiRow(5, 41, 42, 46, 0.915, 81, 13, "优"),
        aqiRow(6, 56, 52, 69, 1.067, 92, 16, "良"),
        aqiRow(7, 64, 30, 28, 0.924, 51, 2, "良"),
        aqiRow(8, 55, 48, 74, 1.236, 75, 26, "良"),
        aqiRow(9, 76, 85, 113, 1.237, 114, 27, "良"),
        aqiRow(10, 91, 81, 104, 1.041, 56, 40, "良"),
        aqiRow(11, 84, 39, 60, 0.964, 25, 11, "良"),
        aqiRow(12, 64, 51, 101, 0.862, 58, 23, "良"),
        aqiRow(13, 70, 69, 120, 1.198, 65, 36, "良"),
        aqiRow(14, 77, 105, 178, 2.549, 64, 16, "良"),
        aqiRow(15, 109, 68, 87, 0.996, 74, 29, "轻度污染"),
        aqiRow(16, 73, 68, 97, 0.905, 51, 34, "良"),
        aqiRow(17, 54, 27, 47, 0.592, 53, 12, "良"),
        aqiRow(18, 51, 61, 97, 0.811, 65, 19, "良"),
        aqiRow(19, 91, 71, 121, 1.374, 43, 18, "良"),
        aqiRow(20, 73, 102, 182, 2.787, 44, 19, "良"),
        aqiRow(21, 73, 50, 76, 0.717, 31, 20, "良"),
        aqiRow(22, 84, 94, 140, 2.238, 68, 18, "良"),
        aqiRow(23, 93, 77, 104, 1.165, 53, 7, "良"),
        aqiRow(24, 99, 130, 227, 3.97, 55, 15, "良"),
        aqiRow(25, 146, 84, 139, 1.094, 40, 17, "轻度污染"),
        aqiRow(26, 113, 108, 137, 1.481, 48, 15, "轻度污染"),
        aqiRow(27, 81, 48, 62, 1.619, 26, 3, "良"),
        aqiRow(28, 56, 48, 68, 1.336, 37, 9, "良"),
        aqiRow(29, 82, 92, 174, 3.29, 0, 13, "良"),
        aqiRow(30, 106, 116, 188, 3.628, 101, 16, "轻度污染"),
        aqiRow(31, 118, 50, 0, 1.383, 76, 11, "轻度污染"),
    )

/** 上海, 31 days. */
internal val AQI_SH: List<DataValue> =
    listOf(
        aqiRow(1, 91, 45, 125, 0.82, 34, 23, "良"),
        aqiRow(2, 65, 27, 78, 0.86, 45, 29, "良"),
        aqiRow(3, 83, 60, 84, 1.09, 73, 27, "良"),
        aqiRow(4, 109, 81, 121, 1.28, 68, 51, "轻度污染"),
        aqiRow(5, 106, 77, 114, 1.07, 55, 51, "轻度污染"),
        aqiRow(6, 109, 81, 121, 1.28, 68, 51, "轻度污染"),
        aqiRow(7, 106, 77, 114, 1.07, 55, 51, "轻度污染"),
        aqiRow(8, 89, 65, 78, 0.86, 51, 26, "良"),
        aqiRow(9, 53, 33, 47, 0.64, 50, 17, "良"),
        aqiRow(10, 80, 55, 80, 1.01, 75, 24, "良"),
        aqiRow(11, 117, 81, 124, 1.03, 45, 24, "轻度污染"),
        aqiRow(12, 99, 71, 142, 1.1, 62, 42, "良"),
        aqiRow(13, 95, 69, 130, 1.28, 74, 50, "良"),
        aqiRow(14, 116, 87, 131, 1.47, 84, 40, "轻度污染"),
        aqiRow(15, 108, 80, 121, 1.3, 85, 37, "轻度污染"),
        aqiRow(16, 134, 83, 167, 1.16, 57, 43, "轻度污染"),
        aqiRow(17, 79, 43, 107, 1.05, 59, 37, "良"),
        aqiRow(18, 71, 46, 89, 0.86, 64, 25, "良"),
        aqiRow(19, 97, 71, 113, 1.17, 88, 31, "良"),
        aqiRow(20, 84, 57, 91, 0.85, 55, 31, "良"),
        aqiRow(21, 87, 63, 101, 0.9, 56, 41, "良"),
        aqiRow(22, 104, 77, 119, 1.09, 73, 48, "轻度污染"),
        aqiRow(23, 87, 62, 100, 1, 72, 28, "良"),
        aqiRow(24, 168, 128, 172, 1.49, 97, 56, "中度污染"),
        aqiRow(25, 65, 45, 51, 0.74, 39, 17, "良"),
        aqiRow(26, 39, 24, 38, 0.61, 47, 17, "优"),
        aqiRow(27, 39, 24, 39, 0.59, 50, 19, "优"),
        aqiRow(28, 93, 68, 96, 1.05, 79, 29, "良"),
        aqiRow(29, 188, 143, 197, 1.66, 99, 51, "中度污染"),
        aqiRow(30, 174, 131, 174, 1.55, 108, 50, "中度污染"),
        aqiRow(31, 187, 143, 201, 1.39, 89, 53, "中度污染"),
    )

/** The ten `[x, y, size]` rows of the three scatter tutorial pages. */
internal val SCATTER_TUTORIAL: List<DataValue> =
    triples(
        Triple(14.616, 7.241, 0.896),
        Triple(3.958, 5.701, 0.955),
        Triple(2.768, 8.971, 0.669),
        Triple(9.051, 9.710, 0.171),
        Triple(14.046, 4.182, 0.536),
        Triple(12.295, 1.429, 0.962),
        Triple(4.417, 8.167, 0.113),
        Triple(0.492, 4.771, 0.785),
        Triple(7.632, 2.605, 0.645),
        Triple(14.242, 5.042, 0.368),
    )

/** The `[i, random]` points of `axis-label-align-min-max`, with a fixed sequence instead of `Math.random()`. */
internal val AXIS_LABEL_POINTS: List<DataValue> =
    List(200) { index -> point(index, (index * 37) % 100) }

/**
 * 500 `[x, y, size]` points of `scatter-dataZoom-all`.
 *
 * Upstream fills the rows with `Math.random()`; a linear congruential sequence keeps the page
 * reproducible while producing the same cloud shape.
 */
internal fun scatterCloud(maxX: Double, seed: Long): List<DataValue> {
    var state = seed
    fun next(): Double {
        state = state * 1103515245L + 12345L
        return ((state ushr 16) and 0x7FFFL).toDouble() / 0x8000
    }
    fun round3(value: Double): Double = kotlin.math.round(value * 1000) / 1000
    return List(500) {
        DataValue.Array(
            listOf(
                valueOf(round3(next() * maxX)),
                valueOf(round3(next() * 10)),
                valueOf(round3(next())),
            ),
        )
    }
}

/** One value at [index] and nulls elsewhere, so a per-item option can be split into single-slot series. */
internal fun singleSlot(index: Int, size: Int, value: Number): List<DataValue> =
    List(size) { if (it == index) valueOf(value) else DataValue.Null }

/** The hidden `-1..1` axes the text examples and `title-block` sit on. */
internal fun EChartsOption.hiddenTextAxes(
    minY: Number = -1,
    maxY: Number = 1,
    inverseY: Boolean = false,
) {
    xAxis {
        axisLabel { show = false }
        axisLine { show = false }
        splitLine { show = false }
        axisTick { show = false }
        min = num(-1)
        max = num(1)
    }
    yAxis {
        axisLabel { show = false }
        axisLine { show = false }
        splitLine { show = false }
        axisTick { show = false }
        min = num(minY)
        max = num(maxY)
        if (inverseY) inverse = true
    }
}

/** The pie of the six `tutorial-styling-step*` pages, accumulating one styling step at a time. */
internal fun EChartsOption.tutorialStyling(step: Int) {
    if (step >= 3) backgroundColor = "#2c343c".asColor()
    if (step == 5) {
        visualMap {
            show = false
            min = 80.0
            max = 600.0
            inRange { colorLightness = listOf(0.0, 1.0) }
        }
    }
    series {
        pie {
            name = "访问来源"
            radius = NumberOrStringOrNumbers.of("55%")
            if (step >= 1) roseType = BooleanOrString.of("area")
            if (step >= 2) {
                itemStyle {
                    shadowBlur = 200.0
                    shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                }
            }
            if (step >= 3) {
                label { color = "rgba(255, 255, 255, 0.3)".asColor() }
                labelLine {
                    lineStyle =
                        LineStyleOption().apply { color = "rgba(255, 255, 255, 0.3)".asColor() }
                }
            }
            if (step >= 4) itemStyle { color = "#c23531".asColor() }
            data = VISIT_SOURCE
        }
    }
}

/** `xAxis` + `yAxis` of `aria-decal-simple`. */
internal fun EChartsOption.decalAxes() {
    xAxis { categories("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun") }
    yAxis { type = AxisType.Value }
}

/** A decal pattern of `aria.decal`; the lists may nest, so they are JSON arrays. */
internal fun jsonInts(vararg values: Int): JsonArray =
    buildJsonArray { values.forEach { add(JsonPrimitive(it)) } }

/** A decal dash pattern of nested arrays, for example `[[8, 8], [0, 8, 8, 0]]`. */
internal fun jsonIntArrays(vararg values: List<Int>): JsonArray =
    buildJsonArray { values.forEach { add(jsonInts(*it.toIntArray())) } }

/** One `aria.decal.decals` entry, with the keys the decal gallery uses. */
internal fun decalOf(
    color: String? = null,
    symbol: String? = null,
    dashArrayX: JsonElement? = null,
    dashArrayY: JsonElement? = null,
    symbolSize: Double? = null,
    rotation: Double? = null,
): JsonElement =
    buildJsonObject {
        color?.let { put("color", it) }
        symbol?.let { put("symbol", it) }
        dashArrayX?.let { put("dashArrayX", it) }
        dashArrayY?.let { put("dashArrayY", it) }
        symbolSize?.let { put("symbolSize", it) }
        rotation?.let { put("rotation", it) }
    }

/** The decal color of `aria-decal`. */
internal const val DECAL_COLOR = "rgba(0, 0, 0, 0.8)"

/** Number of patterns in the `aria-decal` list; one pie slice consumes one decal. */
internal const val DECAL_COUNT = 11

/** A sunburst node with an optional per-node color and children. */
internal fun sunburstNode(
    name: String,
    value: Number? = null,
    color: String? = null,
    vararg children: DataItem,
): DataItem {
    val node = DataItem()
    node.name = name
    node.value = value?.let(::valueOf)
    if (color != null) {
        node.itemStyle = ItemStyleOption().apply { this.color = color.asColor() }
    }
    if (children.isNotEmpty()) node.children = children.toList()
    return node
}

/** A sunburst node whose label sits inside with [align], used by `sunburst-label-align`. */
internal fun alignedSunburstNode(
    name: String,
    align: String,
    value: Number? = null,
    children: List<DataItem> = emptyList(),
): DataItem {
    val node = DataItem()
    node.name = name
    node.value = value?.let(::valueOf)
    val textAlign = align
    node.label =
        LabelOption().apply {
            this.align = textAlign
            position = "inside"
        }
    if (children.isNotEmpty()) node.children = children
    return node
}

/** The `rich` table shared by the three text blocks of `title-block`; image cells are omitted. */
internal fun titleBlockRich(): Map<String, TextStyleOption> =
    mapOf(
        "titleBg" to
            textStyleOf {
                backgroundColor = "#000".asColor()
                height = num(30)
                borderRadius = NumberOrStringOrNumbers.of(num(5), num(5), num(0), num(0))
                padding = NumberOrStringOrNumbers.of(num(0), num(10), num(0), num(10))
                width = str("100%")
                color = "#eee".asColor()
            },
        "tc" to
            textStyleOf {
                align = "center"
                color = "#eee".asColor()
            },
        "hr" to
            textStyleOf {
                borderColor = "#777".asColor()
                width = str("100%")
                borderWidth = 0.5
                height = num(0)
            },
    )

/** One text block of `title-block`; the icon cells of the formatter were dropped with the images. */
internal fun textBlock(y: Number, text: String, alignTitle: Boolean): DataValue =
    dataItem {
        value = point(0, y)
        label {
            formatter = text
            if (alignTitle) rich = mapOf("titleBg" to textStyleOf { align = "right" })
        }
    }

/** The two-slice box of a `pie-label-*` page, placed in the left or right half. */
internal fun SeriesBuilder.labelPie(
    data: List<DataValue>,
    left: NumberOrString,
    right: NumberOrString,
    align: String = "labelLine",
    margin: NumberOrString? = null,
    bleedMargin: Double? = null,
) {
    val slices = data
    val leftEdge = left
    val rightEdge = right
    val alignTarget = align
    val labelMargin = margin
    val labelBleedMargin = bleedMargin
    pie {
        radius = NumberOrStringOrNumbers.of("25%")
        center = listOf(str("50%"), str("50%"))
        this.data = slices
        animation = false
        label {
            position = "outer"
            this.alignTo = alignTarget
            labelMargin?.let { this.margin = it }
            labelBleedMargin?.let { this.bleedMargin = it }
        }
        this.left = leftEdge
        this.right = rightEdge
        top = num(0)
        bottom = num(0)
    }
}

/** One swatch of `pictorialBar-position`: a single slot with its own `symbolPosition`. */
internal fun SeriesBuilder.walkSymbol(
    index: Int,
    position: String,
    offset: List<NumberOrString>? = null,
) {
    pictorialBar {
        name = "pictorial element"
        symbol = "roundRect"
        symbolSize = NumberOrStringOrNumbers.of(num(50), num(120))
        z = 10.0
        symbolPosition = position
        offset?.let { symbolOffset = it }
        itemStyle { color = "#bb0004".asColor() }
        data = singleSlot(index, 4, 60)
    }
}

/** One swatch of `pictorialBar-repeat`. */
internal fun SeriesBuilder.repeatedSymbol(index: Int, size: NumberOrStringOrNumbers) {
    pictorialBar {
        name = "pictorialBar"
        symbol = "circle"
        symbolSize = size
        data = singleSlot(index, 2, 29000)
    }
}

/** One clipped symbol of `pictorialBar-clip`. */
internal fun SeriesBuilder.clippedSymbol(
    index: Int,
    clipped: Boolean,
    repeat: Boolean?,
    size: NumberOrStringOrNumbers?,
) {
    pictorialBar {
        name = "realValue"
        symbol = "roundRect"
        z = 10.0
        symbolBoundingData = NumberOrNumbers.of(80)
        symbolClip = clipped
        repeat?.let { symbolRepeat = BooleanOrNumberOrString.of(it) }
        size?.let { symbolSize = it }
        itemStyle { color = "#bb0004".asColor() }
        data = singleSlot(index, 3, 40)
    }
}

/** One background symbol of `pictorialBar-clip`. */
internal fun SeriesBuilder.clipBackground(
    index: Int,
    size: NumberOrStringOrNumbers?,
) {
    pictorialBar {
        name = "background"
        symbol = "roundRect"
        symbolBoundingData = NumberOrNumbers.of(80)
        size?.let { symbolSize = it }
        animationDuration = 0.0
        itemStyle { color = "#ccc".asColor() }
        label {
            show = true
            position = "outside"
            formatter = "value: {c}"
            color = "#bb0004".asColor()
        }
        data = singleSlot(index, 3, 40)
    }
}

/** The two pies of one `pie-media` media unit. */
internal fun SeriesBuilder.mediaPies(
    firstRadius: NumberOrStringOrNumbers,
    secondRadius: NumberOrStringOrNumbers,
    firstCenter: List<NumberOrString>,
    secondCenter: List<NumberOrString>,
) {
    pie {
        radius = firstRadius
        center = firstCenter
    }
    pie {
        radius = secondRadius
        center = secondCenter
    }
}

/** The labels of `bar-rich-text`; the weather icons were images and are dropped. */
internal fun weatherAxisLabel(): AxisLabelOption =
    AxisLabelOption().apply {
        formatter = "{value|{value}}"
        margin = 20.0
        rich =
            mapOf(
                "value" to
                    textStyleOf {
                        lineHeight = 30.0
                        align = "center"
                    },
                "Sunny" to
                    textStyleOf {
                        height = num(40)
                        align = "center"
                    },
                "Cloudy" to
                    textStyleOf {
                        height = num(40)
                        align = "center"
                    },
                "Showers" to
                    textStyleOf {
                        height = num(40)
                        align = "center"
                    },
            )
    }

/** The `markPoint` of `bar-rich-text`. */
internal fun weatherMarkPoint(): MarkPointOption =
    MarkPointOption().apply {
        symbolSize = NumberOrStringOrNumbers.of(1)
        symbolOffset = listOf(num(0), str("50%"))
        label =
            LabelOption().apply {
                formatter = "{a|{a}\n}{b|{b} }{c|{c}}"
                backgroundColor = "rgb(242,242,242)".asColor()
                borderColor = "#aaa".asColor()
                borderWidth = 1.0
                borderRadius = NumberOrStringOrNumbers.of(4)
                padding = NumberOrStringOrNumbers.of(num(4), num(10))
                lineHeight = 26.0
                position = "right"
                distance = 20.0
                rich =
                    mapOf(
                        "a" to
                            textStyleOf {
                                align = "center"
                                color = "#fff".asColor()
                                fontSize = 18.0
                                textShadowBlur = 2.0
                                textShadowColor = "#000".asColor()
                                textShadowOffsetX = 0.0
                                textShadowOffsetY = 1.0
                                textBorderColor = "#333".asColor()
                                textBorderWidth = 2.0
                            },
                        "b" to textStyleOf { color = "#333".asColor() },
                        "c" to
                            textStyleOf {
                                color = "#ff8811".asColor()
                                textBorderColor = "#000".asColor()
                                textBorderWidth = 1.0
                                fontSize = 22.0
                            },
                    )
            }
        data =
            listOf(
                MarkPointData().apply {
                    type = "max"
                    name = "max days: "
                },
                MarkPointData().apply {
                    type = "min"
                    name = "min days: "
                },
            )
    }
