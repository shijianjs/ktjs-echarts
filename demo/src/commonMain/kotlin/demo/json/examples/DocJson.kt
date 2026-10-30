package demo.json.examples

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.namedValue
import demo.json.namedValues
import demo.json.nodeItem
import demo.json.nodes
import demo.json.points
import demo.json.triples
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

/**
 * The `doc-example/` pages of apache/echarts-examples, rewritten with the `kmp-echarts-json` DSL.
 *
 * Every id matches the official example id, including the `doc-example/` prefix; `bar-rich-text` and
 * `polar-endAngle` live outside that folder upstream and keep their plain ids.
 *
 * ## Data and shapes copied literally
 * - The tutorial pies, the AQI tables (`parallel-all`, `scatter-visualMap-categories`) and every small
 *   data set are the upstream literals.
 * - `Math.random()` is replaced by fixed sequences (`scatter-dataZoom-all`, `axis-label-align-min-max`),
 *   so the pages stay reproducible.
 * - ECharts 6 dropped the `normal` state, so `itemStyle.normal`, `label.normal` and `labelLine.normal`
 *   are flattened; `roseType: 'angle'` is written as `'area'`, which is what ECharts 6 maps it to.
 * - `formatter`, `symbolSize` and `itemStyle.color` callbacks are replaced by static strings or omitted.
 *
 * ## Model fields that do not exist (the matching option is omitted)
 * - `axisLabel.alignMinLabel` / `alignMaxLabel` (`axis-label-align-min-max`): both end labels use the
 *   default alignment.
 * - `angleAxis.clockwise` (`polar-anticlockwise`): the angle axis keeps its default direction.
 * - `parallelAxis.dim` (`parallel-all`): the axis order alone defines the dimensions; upstream `dim`
 *   equals the index, so the chart is unchanged.
 * - `label.distanceToLabelLine` (`pie-label-distanceToLabelLine`): both pies fall back to the default
 *   distance, so the two halves differ only by their layout box.
 * - `emphasis.labelLine` (`pie-media`): the emphasis state has no connector line, so the first pie
 *   keeps only its hidden label and the default emphasis connector.
 * - per-item `symbolPosition` / `symbolRepeat` / `symbolClip` / `animationDuration` (`pictorialBar-*`):
 *   those pages are rebuilt as one single-slot series per variant, with the property moved to series
 *   level; `symbolRepeat: 'fixed'` is carried by `BooleanOrNumberOrString`.
 * - `visualMap.categories` and `visualMap.selected` (`scatter-visualMap-categories`): the category
 *   visual map is omitted; the scatter keeps its palette, axes and layout.
 * - `radar.splitArea.areaStyle.color` is a single color in the model, so the four-band
 *   `#B8D3E4 … #72ACD1` array (`radar`) is omitted; `radar.indicator` uses `name` where upstream uses
 *   `text`.
 * - `sunburst.sort: null` cannot be expressed (`sort` is `BooleanOrString?`), so the page keeps the
 *   default `'desc'` sort (`sunburst-label-align`).
 * - `toolbox.feature.mark` (`pie-media`) and the legacy root `calculable` are not modeled.
 * - image-backed rich-text cells (`title-block` weather icons, `bar-rich-text` weather icons) and the
 *   long inline `path://` pictorial-bar silhouettes are assets, not fields: the rich cells keep only
 *   their colors/sizes, the pictorial bars use the built-in `roundRect` symbol.
 * - `aria-decal` paints its gallery with a custom `renderItem` callback, which JSON cannot carry: the
 *   option keeps the `aria.decal.decals` list (11 of the 12 patterns, the inline-SVG one is an asset)
 *   and paints it as a pie whose eleven slices pick one decal each, because a decal palette is
 *   consumed per data item. The custom grid of squares is not reproducible.
 *
 * `aria.decal.decals` and sunburst `levels` are typed `JsonElement` in the model, so nested decal dash
 * arrays and level styles are built as JSON objects instead of `extra` keys.
 */

private val doc = JsonCategory("json/examples/DocJson.kt")

/** The five slices every `tutorial-styling-step*` page shares. */
private val VISIT_SOURCE =
    namedValues(
        "视频广告" to 235,
        "联盟广告" to 274,
        "邮件营销" to 310,
        "直接访问" to 335,
        "搜索引擎" to 400,
    )

/** The seven slices of the `pie-label-*` pages. */
private fun pieLabelSlices(longNames: Boolean): List<DataValue> {
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
private fun aqiRow(
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
private val AQI_BJ: List<DataValue> =
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
private val AQI_GZ: List<DataValue> =
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
private val AQI_SH: List<DataValue> =
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
private val SCATTER_TUTORIAL: List<DataValue> =
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
private val AXIS_LABEL_POINTS: List<DataValue> =
    List(200) { index -> point(index, (index * 37) % 100) }

/**
 * 500 `[x, y, size]` points of `scatter-dataZoom-all`.
 *
 * Upstream fills the rows with `Math.random()`; a linear congruential sequence keeps the page
 * reproducible while producing the same cloud shape.
 */
private fun scatterCloud(maxX: Double, seed: Long): List<DataValue> {
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
private fun singleSlot(index: Int, size: Int, value: Number): List<DataValue> =
    List(size) { if (it == index) valueOf(value) else DataValue.Null }

/** The hidden `-1..1` axes the text examples and `title-block` sit on. */
private fun EChartsOption.hiddenTextAxes(
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
private fun EChartsOption.tutorialStyling(step: Int) {
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
private fun EChartsOption.decalAxes() {
    xAxis { categories("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun") }
    yAxis { type = AxisType.Value }
}

/** A decal pattern of `aria.decal`; the lists may nest, so they are JSON arrays. */
private fun jsonInts(vararg values: Int): JsonArray =
    buildJsonArray { values.forEach { add(JsonPrimitive(it)) } }

/** A decal dash pattern of nested arrays, for example `[[8, 8], [0, 8, 8, 0]]`. */
private fun jsonIntArrays(vararg values: List<Int>): JsonArray =
    buildJsonArray { values.forEach { add(jsonInts(*it.toIntArray())) } }

/** One `aria.decal.decals` entry, with the keys the decal gallery uses. */
private fun decalOf(
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
private const val DECAL_COLOR = "rgba(0, 0, 0, 0.8)"

/** Number of patterns in the `aria-decal` list; one pie slice consumes one decal. */
private const val DECAL_COUNT = 11

/** A sunburst node with an optional per-node color and children. */
private fun sunburstNode(
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
private fun alignedSunburstNode(
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
private fun titleBlockRich(): Map<String, TextStyleOption> =
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
private fun textBlock(y: Number, text: String, alignTitle: Boolean): DataValue =
    dataItem {
        value = point(0, y)
        label {
            formatter = text
            if (alignTitle) rich = mapOf("titleBg" to textStyleOf { align = "right" })
        }
    }

/** The two-slice box of a `pie-label-*` page, placed in the left or right half. */
private fun SeriesBuilder.labelPie(
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
private fun SeriesBuilder.walkSymbol(
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
private fun SeriesBuilder.repeatedSymbol(index: Int, size: NumberOrStringOrNumbers) {
    pictorialBar {
        name = "pictorialBar"
        symbol = "circle"
        symbolSize = size
        data = singleSlot(index, 2, 29000)
    }
}

/** One clipped symbol of `pictorialBar-clip`. */
private fun SeriesBuilder.clippedSymbol(
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
private fun SeriesBuilder.clipBackground(
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
private fun SeriesBuilder.mediaPies(
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
private fun weatherAxisLabel(): AxisLabelOption =
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
private fun weatherMarkPoint(): MarkPointOption =
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

public val docJsonExamples: List<JsonExample> =
    listOf(
        doc.example("doc-example/getting-started") {
            title { text = "ECharts Getting Started Example" }
            tooltip { }
            legend { entries("sales") }
            xAxis { categories("Shirts", "Cardigans", "Chiffons", "Pants", "Heels", "Socks") }
            yAxis { }
            series {
                bar {
                    name = "sales"
                    values(5, 20, 36, 10, 10, 20)
                }
            }
        },
        doc.example("doc-example/tutorial-styling-step0") {
            tutorialStyling(0)
        },
        doc.example("doc-example/tutorial-styling-step1") {
            tutorialStyling(1)
        },
        doc.example("doc-example/tutorial-styling-step2") {
            tutorialStyling(2)
        },
        doc.example("doc-example/tutorial-styling-step3") {
            tutorialStyling(3)
        },
        doc.example("doc-example/tutorial-styling-step4") {
            tutorialStyling(4)
        },
        doc.example("doc-example/tutorial-styling-step5") {
            tutorialStyling(5)
        },
        doc.example("doc-example/aria-pie") {
            aria { enabled = true }
            title {
                text = "某站点用户访问来源"
                left = str("center")
            }
            series {
                pie {
                    name = "访问来源"
                    data =
                        namedValues(
                            "直接访问" to 335,
                            "邮件营销" to 310,
                            "联盟广告" to 234,
                            "视频广告" to 135,
                            "搜索引擎" to 1548,
                        )
                }
            }
        },
        doc.example("doc-example/aria-decal") {
            xAxis {
                type = AxisType.Value
                max = num(1)
                show = false
            }
            yAxis {
                type = AxisType.Value
                show = false
            }
            aria {
                enabled = true
                decal =
                    AriaDecalOption().apply {
                        show = true
                        decals =
                            listOf(
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(1, 0),
                                    dashArrayY = jsonInts(2, 5),
                                    symbolSize = 1.0,
                                    rotation = kotlin.math.PI / 6,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    symbol = "circle",
                                    dashArrayX = jsonIntArrays(listOf(8, 8), listOf(0, 8, 8, 0)),
                                    dashArrayY = jsonInts(6, 0),
                                    symbolSize = 0.8,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(1, 0),
                                    dashArrayY = jsonInts(4, 3),
                                    rotation = -kotlin.math.PI / 4,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonIntArrays(listOf(6, 6), listOf(0, 6, 6, 0)),
                                    dashArrayY = jsonInts(6, 0),
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonIntArrays(listOf(1, 0), listOf(1, 6)),
                                    dashArrayY = jsonInts(1, 0, 6, 0),
                                    rotation = kotlin.math.PI / 4,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    symbol = "triangle",
                                    dashArrayX = jsonIntArrays(listOf(9, 9), listOf(0, 9, 9, 0)),
                                    dashArrayY = jsonInts(7, 2),
                                    symbolSize = 0.75,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(1, 0),
                                    dashArrayY = JsonPrimitive(10),
                                    rotation = 0.0,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(10, 10),
                                    dashArrayY = jsonInts(1, 0),
                                    rotation = 0.0,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonInts(10, 5, 20, 5),
                                    dashArrayY = JsonPrimitive(10),
                                    rotation = 0.0,
                                ),
                                decalOf(
                                    color = DECAL_COLOR,
                                    dashArrayX = jsonIntArrays(listOf(10, 5, 20, 5), listOf(20, 5)),
                                    dashArrayY = jsonInts(10, 5),
                                    rotation = 0.0,
                                ),
                                decalOf(
                                    symbol = "rect",
                                    dashArrayX = jsonIntArrays(listOf(8, 8), listOf(0, 8, 8, 0)),
                                    dashArrayY = jsonInts(8, 0),
                                    symbolSize = 0.8,
                                    rotation = kotlin.math.PI / 4,
                                    color = "black",
                                ),
                            )
                    }
            }
            series {
                pie {
                    radius = NumberOrStringOrNumbers.of(str("25%"), str("80%"))
                    label { show = false }
                    data = List(DECAL_COUNT) { index -> namedValue("decal ${index + 1}", 1) }
                }
            }
            color = colors("#ddd")
        },
        doc.example("doc-example/aria-decal-newspaper") {
            series {
                bar {
                    name = "Apples"
                    values(108, 26, 39, 24)
                    itemStyle {
                        borderColor = "black".asColor()
                        borderWidth = 2.0
                    }
                }
                bar {
                    name = "Oranges"
                    values(23, 40, 60, 70)
                    itemStyle {
                        borderColor = "black".asColor()
                        borderWidth = 2.0
                    }
                }
                bar {
                    name = "Bananas"
                    values(129, 40, 40, 50)
                    itemStyle {
                        borderColor = "black".asColor()
                        borderWidth = 2.0
                    }
                }
                bar {
                    name = "Pears"
                    values(40, 60, 50, 89)
                    itemStyle {
                        borderColor = "black".asColor()
                        borderWidth = 2.0
                    }
                }
            }
            xAxis {
                categories("Q1", "Q2", "Q3", "Q4")
                axisLine { lineStyle = LineStyleOption().apply { color = "black".asColor() } }
                axisTick { lineStyle = LineStyleOption().apply { color = "black".asColor() } }
                axisLabel { color = "black".asColor() }
            }
            yAxis {
                axisLine { lineStyle = LineStyleOption().apply { color = "black".asColor() } }
                axisTick { lineStyle = LineStyleOption().apply { color = "black".asColor() } }
                axisLabel { color = "black".asColor() }
                splitLine {
                    lineStyle =
                        LineStyleOption().apply {
                            color = "black".asColor()
                            type = "dashed"
                        }
                }
            }
            aria {
                decal =
                    AriaDecalOption().apply {
                        show = true
                        decals =
                            listOf(
                                decalOf(
                                    dashArrayX = JsonPrimitive(0),
                                    dashArrayY = JsonPrimitive(0),
                                    color = "black",
                                ),
                                decalOf(
                                    dashArrayX = JsonPrimitive(0),
                                    dashArrayY = JsonPrimitive(0),
                                    color = "black",
                                ),
                                decalOf(
                                    dashArrayX = jsonInts(1, 0),
                                    dashArrayY = jsonInts(2, 5),
                                    symbolSize = 1.0,
                                    rotation = kotlin.math.PI / 6,
                                    color = "black",
                                ),
                                decalOf(
                                    symbol = "circle",
                                    dashArrayX = jsonIntArrays(listOf(8, 8), listOf(0, 8, 8, 0)),
                                    dashArrayY = jsonInts(6, 0),
                                    symbolSize = 0.8,
                                    color = "black",
                                ),
                            )
                    }
            }
            color = colors("white", "black", "white", "white")
        },
        doc.example("doc-example/aria-decal-simple") {
            decalAxes()
            series {
                bar { values(120, 200, 150, 80, 70, 110, 130) }
                bar { values(20, 40, 90, 40, 30, 70, 120) }
                bar { values(140, 230, 120, 50, 30, 150, 120) }
            }
            aria {
                enabled = true
                decal = AriaDecalOption().apply { show = true }
            }
        },
        doc.example("doc-example/title-block") {
            series {
                scatter {
                    data =
                        listOf(
                            textBlock(
                                0,
                                "{tc|Center Title}{titleBg|}\n  Content text xxxxxxxx  xxxxxxxx  \n{hr|}\n" +
                                    "  xxxxx  xxxxxxxx  xxxxxxxxx  ",
                                true,
                            ),
                            textBlock(
                                1,
                                "{titleBg|Left Title}\n  Content text xxxxxxxx  xxxxxxxx  \n{hr|}\n" +
                                    "  xxxxx  xxxxxxxx  xxxxxxxxx  ",
                                false,
                            ),
                            textBlock(
                                2,
                                "{titleBg|Right Title}\n  Content text xxxxxxxx  xxxxxxxx  \n{hr|}\n" +
                                    "  xxxxx  xxxxxxxx  xxxxxxxxx  ",
                                true,
                            ),
                        )
                    symbolSize = NumberOrStringOrNumbers.of(1)
                    label {
                        show = true
                        backgroundColor = "#ddd".asColor()
                        borderColor = "#555".asColor()
                        borderWidth = 1.0
                        borderRadius = NumberOrStringOrNumbers.of(5)
                        color = "#000".asColor()
                        fontSize = 14.0
                        rich = titleBlockRich()
                    }
                }
            }
            hiddenTextAxes(minY = 0, maxY = 2, inverseY = true)
        },
        doc.example("doc-example/axis-label-align-min-max") {
            xAxis {
                axisLabel {
                    formatter = "label of {value}"
                    showMinLabel = true
                    showMaxLabel = true
                }
            }
            yAxis { }
            series {
                line { data = AXIS_LABEL_POINTS }
            }
            dataZoom {
                type = DataZoomType.Slider
                start = 20.0
                end = 42.0
            }
        },
        doc.example("doc-example/line-stack-tiled") {
            title {
                text = "某楼盘销售情况"
                subtext = "纯属虚构"
            }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { entries("意向", "预购", "成交") }
            toolbox {
                show = true
                feature =
                    ToolboxFeatureOption().apply {
                        magicType =
                            ToolboxMagicTypeFeature().apply {
                                show = true
                                type = listOf(SeriesType("stack"), SeriesType("tiled"))
                            }
                        saveAsImage = ToolboxSaveAsImageFeature().apply { show = true }
                    }
            }
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories("周一", "周二", "周三", "周四", "周五", "周六", "周日")
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    name = "成交"
                    smooth = BooleanOrNumber.of(true)
                    values(10, 12, 21, 54, 260, 830, 710)
                }
                line {
                    name = "预购"
                    smooth = BooleanOrNumber.of(true)
                    values(30, 182, 434, 791, 390, 30, 10)
                }
                line {
                    name = "意向"
                    smooth = BooleanOrNumber.of(true)
                    values(1320, 1132, 601, 234, 120, 90, 20)
                }
            }
        },
        doc.example("doc-example/dataset-encode-simple0") {
            dataset {
                source =
                    datasetRows(
                        listOf(textValue("score"), textValue("amount"), textValue("product")),
                        listOf(valueOf(89.3), valueOf(58212), textValue("Matcha Latte")),
                        listOf(valueOf(57.1), valueOf(78254), textValue("Milk Tea")),
                        listOf(valueOf(74.4), valueOf(41032), textValue("Cheese Cocoa")),
                        listOf(valueOf(50.1), valueOf(12755), textValue("Cheese Brownie")),
                        listOf(valueOf(89.7), valueOf(20145), textValue("Matcha Cocoa")),
                        listOf(valueOf(68.1), valueOf(79146), textValue("Tea")),
                        listOf(valueOf(19.6), valueOf(91852), textValue("Orange Juice")),
                        listOf(valueOf(10.6), valueOf(101852), textValue("Lemon Juice")),
                        listOf(valueOf(32.7), valueOf(20112), textValue("Walnut Brownie")),
                    )
            }
            grid { containLabel = true }
            xAxis { }
            yAxis { type = AxisType.Category }
            series {
                bar {
                    encode =
                        buildJsonObject {
                            put("x", "amount")
                            put("y", "product")
                        }
                }
            }
        },
        doc.example("doc-example/data-transform-multiple-sort-bar") {
            dataset {
                dimensions =
                    listOf(
                        JsonPrimitive("name"),
                        JsonPrimitive("age"),
                        JsonPrimitive("profession"),
                        JsonPrimitive("score"),
                        JsonPrimitive("date"),
                    )
                source =
                    datasetRows(
                        listOf(
                            textValue(" Hannah Krause "),
                            valueOf(41),
                            textValue("Engineer"),
                            valueOf(314),
                            textValue("2011-02-12"),
                        ),
                        listOf(
                            textValue("Zhao Qian "),
                            valueOf(20),
                            textValue("Teacher"),
                            valueOf(351),
                            textValue("2011-03-01"),
                        ),
                        listOf(
                            textValue(" Jasmin Krause "),
                            valueOf(52),
                            textValue("Musician"),
                            valueOf(287),
                            textValue("2011-02-14"),
                        ),
                        listOf(
                            textValue("Li Lei"),
                            valueOf(37),
                            textValue("Teacher"),
                            valueOf(219),
                            textValue("2011-02-18"),
                        ),
                        listOf(
                            textValue(" Karle Neumann "),
                            valueOf(25),
                            textValue("Engineer"),
                            valueOf(253),
                            textValue("2011-04-02"),
                        ),
                        listOf(
                            textValue(" Adrian Groß"),
                            valueOf(19),
                            textValue("Teacher"),
                            DataValue.Null,
                            textValue("2011-01-16"),
                        ),
                        listOf(
                            textValue("Mia Neumann"),
                            valueOf(71),
                            textValue("Engineer"),
                            valueOf(165),
                            textValue("2011-03-19"),
                        ),
                        listOf(
                            textValue(" Böhm Fuchs"),
                            valueOf(36),
                            textValue("Musician"),
                            valueOf(318),
                            textValue("2011-02-24"),
                        ),
                        listOf(
                            textValue("Han Meimei "),
                            valueOf(67),
                            textValue("Engineer"),
                            valueOf(366),
                            textValue("2011-03-12"),
                        ),
                    )
            }
            dataset {
                transform =
                    buildJsonObject {
                        put("type", "sort")
                        put(
                            "config",
                            buildJsonArray {
                                add(
                                    buildJsonObject {
                                        put("dimension", "profession")
                                        put("order", "desc")
                                    },
                                )
                                add(
                                    buildJsonObject {
                                        put("dimension", "score")
                                        put("order", "desc")
                                    },
                                )
                            },
                        )
                    }
            }
            xAxis {
                type = AxisType.Category
                axisLabel {
                    interval = num(0)
                    rotate = 30.0
                }
            }
            yAxis { }
            series {
                bar {
                    label {
                        show = true
                        rotate = 90.0
                        position = "insideBottom"
                        align = "left"
                        verticalAlign = "middle"
                    }
                    encode =
                        buildJsonObject {
                            put("x", "name")
                            put("y", "score")
                            put("label", buildJsonArray { add(JsonPrimitive("profession")) })
                        }
                    datasetIndex = 1
                }
            }
        },
        doc.example("doc-example/label-position") {
            series {
                scatter {
                    symbolSize = NumberOrStringOrNumbers.of(360)
                    symbol = "roundRect"
                    data = points(1 to 1)
                    label {
                        position = "top"
                        align = "center"
                        verticalAlign = "middle"
                        distance = 5.0
                        show = true
                        formatter = "Label Text"
                        width = num(200)
                        height = num(60)
                        backgroundColor = "rgba(0, 255, 255, 0.3)".asColor()
                        borderColor = "#555".asColor()
                        borderWidth = 2.0
                        borderRadius = NumberOrStringOrNumbers.of(5)
                        padding = NumberOrStringOrNumbers.of(10)
                        fontSize = 18.0
                        shadowBlur = 3.0
                        shadowColor = "#888".asColor()
                        shadowOffsetX = 0.0
                        shadowOffsetY = 3.0
                        textBorderColor = "#000".asColor()
                        textBorderWidth = 3.0
                        color = "#fff".asColor()
                    }
                }
            }
            xAxis { max = num(2) }
            yAxis { max = num(2) }
        },
        doc.example("doc-example/pie-label-margin") {
            title { text = "Pie label margin" }
            title {
                subtext = "margin: \"25%\""
                left = str("25%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "margin: 10"
                left = str("75%")
                top = str("75%")
                textAlign = "center"
            }
            val slices = pieLabelSlices(longNames = false)
            series {
                labelPie(slices, num(0), str("50%"), align = "edge", margin = str("25%"))
                labelPie(slices, str("50%"), num(0), align = "edge", margin = num(10))
            }
        },
        doc.example("doc-example/pie-label-distanceToLabelLine") {
            title { text = "Pie label margin" }
            title {
                subtext = "distanceToLabelLine: 5"
                left = str("25%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "distanceToLabelLine: 20"
                left = str("75%")
                top = str("75%")
                textAlign = "center"
            }
            val slices = pieLabelSlices(longNames = true)
            series {
                labelPie(slices, num(0), str("50%"))
                labelPie(slices, str("50%"), num(0))
            }
        },
        doc.example("doc-example/pie-label-bleedMargin") {
            title { text = "Pie label margin" }
            title {
                subtext = "bleedMargin: 50"
                left = str("25%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "bleedMargin: 10"
                left = str("75%")
                top = str("75%")
                textAlign = "center"
            }
            val slices = pieLabelSlices(longNames = true)
            series {
                labelPie(slices, num(0), str("50%"), bleedMargin = 50.0)
                labelPie(slices, str("50%"), num(0), bleedMargin = 10.0)
            }
        },
        doc.example("doc-example/polar-anticlockwise") {
            polar { }
            angleAxis {
                min = num(0)
                max = num(360)
                interval = 30.0
            }
            radiusAxis {
                min = num(0)
                max = num(10)
                interval = 2.0
            }
            series { }
        },
        doc.example("doc-example/polar-start-angle") {
            polar { }
            angleAxis {
                min = num(0)
                max = num(360)
                interval = 30.0
                startAngle = 45.0
            }
            radiusAxis {
                min = num(0)
                max = num(10)
                interval = 2.0
            }
            series { }
        },
        doc.example("doc-example/polar-end-angle") {
            tooltip { }
            angleAxis {
                type = AxisType.Category
                endAngle = -180.0
                categories("S1", "S2", "S3")
            }
            radiusAxis { }
            polar { }
            series {
                bar {
                    polarIndex = 0
                    values(1, 2, 3)
                    coordinateSystem = "polar"
                }
            }
        },
        doc.example("doc-example/radar") {
            radar {
                indicator =
                    listOf(
                        RadarIndicator().apply { name = "指标一" },
                        RadarIndicator().apply { name = "指标二" },
                        RadarIndicator().apply { name = "指标三" },
                        RadarIndicator().apply { name = "指标四" },
                        RadarIndicator().apply { name = "指标五" },
                    )
                center = listOf(str("50%"), str("50%"))
                radius = NumberOrStringOrNumbers.of("80%")
                startAngle = 90.0
                splitNumber = 4
                shape = "circle"
                axisName =
                    LabelOption().apply {
                        formatter = "【{value}】"
                        color = "#72ACD1".asColor()
                    }
                axisTick =
                    AxisTickOption().apply {
                        show = true
                        lineStyle =
                            LineStyleOption().apply {
                                color = "rgba(255, 255, 255, 0.8)".asColor()
                            }
                    }
                axisLabel =
                    AxisLabelOption().apply {
                        show = true
                        color = "white".asColor()
                    }
                axisLine =
                    AxisLineOption().apply {
                        lineStyle =
                            LineStyleOption().apply {
                                color = "rgba(255, 255, 255, 0.4)".asColor()
                            }
                    }
                splitLine =
                    SplitLineOption().apply {
                        lineStyle =
                            LineStyleOption().apply {
                                color = "rgba(255, 255, 255, 0.4)".asColor()
                            }
                    }
            }
        },
        doc.example("doc-example/sunburst-simple") {
            series {
                sunburst {
                    data =
                        nodes(
                            nodeItem("A", 10, nodeItem("Aa", 3), nodeItem("Ab", 5)),
                            nodeItem("B", null, nodeItem("Ba", 4), nodeItem("Bb", 2)),
                            nodeItem("C", 3),
                        )
                }
            }
        },
        doc.example("doc-example/sunburst-color") {
            series {
                sunburst {
                    data =
                        nodes(
                            sunburstNode(
                                "A",
                                10,
                                null,
                                sunburstNode("Aa", 3, color = "red"),
                                sunburstNode("Ab", 5),
                            ),
                            sunburstNode(
                                "B",
                                null,
                                "red",
                                sunburstNode("Ba", 4),
                                sunburstNode("Bb", 2),
                            ),
                            sunburstNode("C", 3),
                        )
                    itemStyle { color = "#aaa".asColor() }
                    levels =
                        listOf(
                            buildJsonObject { },
                            buildJsonObject {
                                put(
                                    "itemStyle",
                                    buildJsonObject { put("color", "blue") },
                                )
                            },
                        )
                }
            }
        },
        doc.example("doc-example/sunburst-label-align") {
            series {
                sunburst {
                    radius = NumberOrStringOrNumbers.of(str("20%"), str("90%"))
                    emphasis { focus = "none" }
                    data =
                        nodes(
                            *Array(8) {
                                alignedSunburstNode(
                                    name = "left",
                                    align = "left",
                                    children =
                                        listOf(
                                            alignedSunburstNode(name = "right", align = "right", value = 1),
                                        ),
                                )
                            },
                        )
                    label { fontSize = 14.0 }
                }
            }
        },
        doc.example("doc-example/pictorialBar-position") {
            color = colors("#bb0004", "#FFD48A")
            legend { entries("pictorial element", "reference bar") }
            xAxis {
                categories(
                    "symbolPosition: \"start\"\nsymbolOffset: [0, 0]",
                    "symbolPosition: \"end\"\nsymbolOffset: [0, 0]",
                    "symbolPosition: \"center\"\nsymbolOffset: [0, 0]",
                    "symbolPosition: \"end\"\nsymbolOffset: ['-100%', -120]",
                )
                axisTick { show = false }
                axisLabel { interval = num(0) }
            }
            yAxis {
                max = num(80)
                splitLine { show = false }
            }
            series {
                walkSymbol(0, "start")
                walkSymbol(1, "end")
                walkSymbol(2, "center")
                walkSymbol(3, "end", listOf(num(-80), str("-100%")))
                bar {
                    name = "reference bar"
                    barGap = "-100%"
                    values(60, 60, 60, 60)
                    itemStyle { color = "#FFD48A".asColor() }
                }
            }
        },
        doc.example("doc-example/pictorialBar-repeat") {
            legend { entries("pictorialBar", "bar") }
            xAxis { categories("symbolRepeat: true", "symbolRepeat: false") }
            yAxis { splitLine { show = false } }
            animationEasing = "elasticOut"
            series {
                pictorialBar {
                    name = "pictorialBar"
                    symbol = "circle"
                    symbolRepeat = BooleanOrNumberOrString.of(true)
                    symbolSize = NumberOrStringOrNumbers.of(num(50), num(50))
                    data = singleSlot(0, 2, 29000)
                }
                repeatedSymbol(1, NumberOrStringOrNumbers.of(num(50), str("100%")))
            }
        },
        doc.example("doc-example/pictorialBar-clip") {
            color = colors("#bb0004", "#ccc")
            xAxis {
                categories(
                    "symbolClip: true\nsymbolRepeat: false",
                    "symbolClip: true\nsymbolRepeat: true",
                    "symbolClip: false\nsymbolRepeat: true",
                )
                axisTick { show = false }
                axisLabel { interval = num(0) }
            }
            yAxis {
                max = num(80)
                splitLine { show = false }
            }
            series {
                clippedSymbol(0, clipped = true, repeat = null, size = null)
                clippedSymbol(
                    1,
                    clipped = true,
                    repeat = true,
                    size = NumberOrStringOrNumbers.of(str("50%"), str("80%")),
                )
                clippedSymbol(
                    2,
                    clipped = false,
                    repeat = true,
                    size = NumberOrStringOrNumbers.of(str("50%"), str("80%")),
                )
                clipBackground(0, null)
                clipBackground(1, NumberOrStringOrNumbers.of(str("50%"), str("80%")))
                clipBackground(2, NumberOrStringOrNumbers.of(str("50%"), str("80%")))
            }
        },
        doc.example("doc-example/scatter-tutorial-dataZoom-1") {
            xAxis { type = AxisType.Value }
            yAxis { type = AxisType.Value }
            dataZoom {
                type = DataZoomType.Slider
                start = 1.0
                end = 35.0
            }
            series {
                scatter {
                    name = "scatter"
                    itemStyle { opacity = 0.8 }
                    data = SCATTER_TUTORIAL
                }
            }
        },
        doc.example("doc-example/scatter-tutorial-dataZoom-2") {
            xAxis { type = AxisType.Value }
            yAxis { type = AxisType.Value }
            dataZoom {
                type = DataZoomType.Slider
                start = 1.0
                end = 35.0
            }
            dataZoom {
                type = DataZoomType.Inside
                start = 1.0
                end = 35.0
            }
            series {
                scatter {
                    name = "scatter"
                    itemStyle { opacity = 0.8 }
                    data = SCATTER_TUTORIAL
                }
            }
        },
        doc.example("doc-example/scatter-tutorial-dataZoom-3") {
            xAxis { type = AxisType.Value }
            yAxis { type = AxisType.Value }
            dataZoom {
                type = DataZoomType.Slider
                xAxisIndex = listOf(0)
                start = 10.0
                end = 60.0
            }
            dataZoom {
                type = DataZoomType.Inside
                xAxisIndex = listOf(0)
                start = 10.0
                end = 60.0
            }
            dataZoom {
                type = DataZoomType.Slider
                yAxisIndex = listOf(0)
                start = 30.0
                end = 80.0
            }
            dataZoom {
                type = DataZoomType.Inside
                yAxisIndex = listOf(0)
                start = 30.0
                end = 80.0
            }
            series {
                scatter {
                    name = "scatter"
                    itemStyle { opacity = 0.8 }
                    data = SCATTER_TUTORIAL
                }
            }
        },
        doc.example("doc-example/scatter-dataZoom-all") {
            animation = false
            legend { entries("scatter", "scatter2", "scatter3") }
            tooltip { }
            xAxis {
                type = AxisType.Value
                min = str("dataMin")
                max = str("dataMax")
                splitLine { show = true }
            }
            yAxis {
                type = AxisType.Value
                min = str("dataMin")
                max = str("dataMax")
                splitLine { show = true }
            }
            dataZoom {
                type = DataZoomType.Slider
                show = true
                xAxisIndex = listOf(0)
                start = 1.0
                end = 35.0
            }
            dataZoom {
                type = DataZoomType.Slider
                show = true
                yAxisIndex = listOf(0)
                left = str("93%")
                start = 29.0
                end = 36.0
            }
            dataZoom {
                type = DataZoomType.Inside
                xAxisIndex = listOf(0)
                start = 1.0
                end = 35.0
            }
            dataZoom {
                type = DataZoomType.Inside
                yAxisIndex = listOf(0)
                start = 29.0
                end = 36.0
            }
            series {
                scatter {
                    name = "scatter"
                    itemStyle { opacity = 0.8 }
                    data = scatterCloud(15.0, 20261024L)
                }
                scatter {
                    name = "scatter2"
                    itemStyle { opacity = 0.8 }
                    data = scatterCloud(10.0, 20261025L)
                }
                scatter {
                    name = "scatter3"
                    itemStyle { opacity = 0.8 }
                    data = scatterCloud(15.0, 20261026L)
                }
            }
        },
        doc.example("doc-example/scatter-visualMap-categories") {
            color = colors("#dd4444", "#fec42c", "#80F1BE")
            legend {
                top = num(10)
                entries("北京", "上海", "广州")
            }
            grid {
                left = str("10%")
                right = num(200)
                top = str("15%")
                bottom = str("10%")
            }
            tooltip {
                padding = NumberOrStringOrNumbers.of(10)
                borderColor = "#777".asColor()
                borderWidth = 1.0
            }
            xAxis {
                type = AxisType.Value
                name = "日期"
                nameGap = 16.0
                nameTextStyle { fontSize = 14.0 }
                max = num(31)
                splitLine { show = false }
                axisLabel { formatter = "{value}" }
            }
            yAxis {
                type = AxisType.Value
                name = "AQI指数"
                nameLocation = "end"
                nameGap = 20.0
                nameTextStyle { fontSize = 20.0 }
                splitLine { show = false }
            }
            series {
                scatter {
                    name = "北京"
                    data = AQI_BJ
                    itemStyle {
                        opacity = 0.8
                        shadowBlur = 10.0
                        shadowOffsetX = 0.0
                        shadowOffsetY = 0.0
                        shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                    }
                }
                scatter {
                    name = "上海"
                    data = AQI_SH
                    itemStyle {
                        opacity = 0.8
                        shadowBlur = 10.0
                        shadowOffsetX = 0.0
                        shadowOffsetY = 0.0
                        shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                    }
                }
                scatter {
                    name = "广州"
                    data = AQI_GZ
                    itemStyle {
                        opacity = 0.8
                        shadowBlur = 10.0
                        shadowOffsetX = 0.0
                        shadowOffsetY = 0.0
                        shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                    }
                }
            }
        },
        doc.example("doc-example/parallel-all") {
            color = colors("#c23531", "#91c7ae", "#dd8668")
            legend {
                top = num(10)
                itemGap = 20.0
                entries("北京", "上海", "广州")
            }
            parallelAxis {
                name = "日期"
                inverse = true
                max = num(31)
                nameLocation = "start"
            }
            parallelAxis { name = "AQI" }
            parallelAxis { name = "PM2.5" }
            parallelAxis { name = "PM10" }
            parallelAxis { name = " CO" }
            parallelAxis { name = "NO2" }
            parallelAxis { name = "SO2" }
            parallelAxis {
                name = "等级"
                type = AxisType.Category
                categories("优", "良", "轻度污染", "中度污染", "重度污染", "严重污染")
            }
            parallel {
                left = str("5%")
                right = str("13%")
                bottom = str("10%")
                top = str("20%")
                parallelAxisDefault =
                    AxisOption().apply {
                        type = AxisType.Value
                        name = "AQI指数"
                        nameLocation = "end"
                        nameGap = 20.0
                        nameTextStyle { fontSize = 12.0 }
                    }
            }
            series {
                parallel {
                    name = "北京"
                    lineStyle {
                        width = 1.0
                        opacity = 0.5
                    }
                    data = AQI_BJ
                }
                parallel {
                    name = "上海"
                    lineStyle {
                        width = 1.0
                        opacity = 0.5
                    }
                    data = AQI_SH
                }
                parallel {
                    name = "广州"
                    lineStyle {
                        width = 1.0
                        opacity = 0.5
                    }
                    data = AQI_GZ
                }
            }
        },
        doc.example("doc-example/pie-media") {
            baseOption =
                echartsOption {
                    title {
                        text = "南丁格尔玫瑰图"
                        subtext = "纯属虚构"
                        left = str("center")
                    }
                    tooltip {
                        trigger = TooltipTrigger.Item
                        formatter = "{a} <br/>{b} : {c} ({d}%)"
                    }
                    legend { entries("rose1", "rose2", "rose3", "rose4", "rose5", "rose6", "rose7", "rose8") }
                    toolbox {
                        show = true
                        feature =
                            ToolboxFeatureOption().apply {
                                dataView = ToolboxDataViewFeature().apply { readOnly = false }
                                magicType =
                                    ToolboxMagicTypeFeature().apply {
                                        type = listOf(SeriesType.Pie, SeriesType.Funnel)
                                    }
                                restore = ToolboxFeature().apply { show = true }
                                saveAsImage = ToolboxSaveAsImageFeature().apply { show = true }
                            }
                    }
                    series {
                        pie {
                            name = "半径模式"
                            roseType = BooleanOrString.of("radius")
                            label { show = false }
                            labelLine { show = false }
                            emphasis { label { show = true } }
                            data =
                                namedValues(
                                    "rose1" to 10,
                                    "rose2" to 5,
                                    "rose3" to 15,
                                    "rose4" to 25,
                                    "rose5" to 20,
                                    "rose6" to 35,
                                    "rose7" to 30,
                                    "rose8" to 40,
                                )
                        }
                        pie {
                            name = "面积模式"
                            roseType = BooleanOrString.of("area")
                            data =
                                namedValues(
                                    "rose1" to 10,
                                    "rose2" to 5,
                                    "rose3" to 15,
                                    "rose4" to 25,
                                    "rose5" to 20,
                                    "rose6" to 35,
                                    "rose7" to 30,
                                    "rose8" to 40,
                                )
                        }
                    }
                }
            media {
                option =
                    echartsOption {
                        legend {
                            right = str("center")
                            bottom = num(0)
                            orient = Orient.Horizontal
                        }
                        series {
                            mediaPies(
                                NumberOrStringOrNumbers.of(num(20), str("50%")),
                                NumberOrStringOrNumbers.of(num(30), str("50%")),
                                listOf(str("25%"), str("50%")),
                                listOf(str("75%"), str("50%")),
                            )
                        }
                    }
            }
            media {
                query = buildJsonObject { put("minAspectRatio", 1) }
                option =
                    echartsOption {
                        legend {
                            right = str("center")
                            bottom = num(0)
                            orient = Orient.Horizontal
                        }
                        series {
                            mediaPies(
                                NumberOrStringOrNumbers.of(num(20), str("50%")),
                                NumberOrStringOrNumbers.of(num(30), str("50%")),
                                listOf(str("25%"), str("50%")),
                                listOf(str("75%"), str("50%")),
                            )
                        }
                    }
            }
            media {
                query = buildJsonObject { put("maxAspectRatio", 1) }
                option =
                    echartsOption {
                        legend {
                            right = str("center")
                            bottom = num(0)
                            orient = Orient.Horizontal
                        }
                        series {
                            mediaPies(
                                NumberOrStringOrNumbers.of(num(20), str("50%")),
                                NumberOrStringOrNumbers.of(num(30), str("50%")),
                                listOf(str("50%"), str("30%")),
                                listOf(str("50%"), str("70%")),
                            )
                        }
                    }
            }
            media {
                query = buildJsonObject { put("maxWidth", 500) }
                option =
                    echartsOption {
                        legend {
                            right = num(10)
                            top = str("15%")
                            orient = Orient.Vertical
                        }
                        series {
                            mediaPies(
                                NumberOrStringOrNumbers.of(num(20), str("50%")),
                                NumberOrStringOrNumbers.of(num(30), str("50%")),
                                listOf(str("50%"), str("30%")),
                                listOf(str("50%"), str("75%")),
                            )
                        }
                    }
            }
        },
        doc.example("doc-example/text-options") {
            series {
                scatter {
                    symbolSize = NumberOrStringOrNumbers.of(1)
                    data =
                        listOf(
                            dataItem {
                                value = point(0, 0)
                                label {
                                    show = true
                                    formatter =
                                        "Plain text\n" +
                                            "{textBorder|textBorderColor + textBorderWidth}\n" +
                                            "{textShadow|textShadowColor + textShadowBlur + textShadowOffsetX + textShadowOffsetY}\n" +
                                            "{bg|backgroundColor + borderRadius + padding}\n" +
                                            "{border|borderColor + borderWidth + borderRadius + padding}\n" +
                                            "{shadow|shadowColor + shadowBlur + shadowOffsetX + shadowOffsetY}"
                                    backgroundColor = "#eee".asColor()
                                    borderColor = "#333".asColor()
                                    borderWidth = 2.0
                                    borderRadius = NumberOrStringOrNumbers.of(5)
                                    padding = NumberOrStringOrNumbers.of(10)
                                    color = "#000".asColor()
                                    fontSize = 14.0
                                    shadowBlur = 3.0
                                    shadowColor = "#888".asColor()
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 3.0
                                    lineHeight = 30.0
                                    rich =
                                        mapOf(
                                            "textBorder" to
                                                textStyleOf {
                                                    fontSize = 20.0
                                                    textBorderColor = "#000".asColor()
                                                    textBorderWidth = 3.0
                                                    color = "#fff".asColor()
                                                },
                                            "textShadow" to
                                                textStyleOf {
                                                    fontSize = 16.0
                                                    textShadowBlur = 5.0
                                                    textShadowColor = "#000".asColor()
                                                    textShadowOffsetX = 3.0
                                                    textShadowOffsetY = 3.0
                                                    color = "#fff".asColor()
                                                },
                                            "bg" to
                                                textStyleOf {
                                                    backgroundColor = "#339911".asColor()
                                                    color = "#fff".asColor()
                                                    borderRadius = NumberOrStringOrNumbers.of(15)
                                                    padding = NumberOrStringOrNumbers.of(5)
                                                },
                                            "border" to
                                                textStyleOf {
                                                    color = "#000".asColor()
                                                    borderColor = "#449911".asColor()
                                                    borderWidth = 1.0
                                                    borderRadius = NumberOrStringOrNumbers.of(3)
                                                    padding = NumberOrStringOrNumbers.of(5)
                                                },
                                            "shadow" to
                                                textStyleOf {
                                                    backgroundColor = "#992233".asColor()
                                                    padding = NumberOrStringOrNumbers.of(5)
                                                    color = "#fff".asColor()
                                                    shadowBlur = 5.0
                                                    shadowColor = "#336699".asColor()
                                                    shadowOffsetX = 6.0
                                                    shadowOffsetY = 6.0
                                                },
                                        )
                                }
                            },
                        )
                }
            }
            hiddenTextAxes()
        },
        doc.example("doc-example/text-fregment-align") {
            series {
                scatter {
                    data = points(0 to 0)
                    symbolSize = NumberOrStringOrNumbers.of(1)
                    label {
                        show = true
                        formatter =
                            "{tc|height is specified as 30}{box|}\n" +
                                "{lh|Biggest lineHeight} {vt|verticalAlign \"top\"} {vb|verticalAlign \"bottom\"} " +
                                "{vm|verticalAlign \"middle\"}\n" +
                                "{l1|align \"left\"}{l2|align \"left\"}{c1|align \"center\"}{c2|align \"center\"}" +
                                "{r2|align \"right\"}{r1|align \"right\"}\n" +
                                "{tc|height is specified as 30}{box|}"
                        backgroundColor = "#ddd".asColor()
                        borderColor = "#555".asColor()
                        borderWidth = 1.0
                        borderRadius = NumberOrStringOrNumbers.of(5)
                        padding = NumberOrStringOrNumbers.of(num(0), num(20), num(0), num(20))
                        color = "#000".asColor()
                        fontSize = 14.0
                        rich =
                            mapOf(
                                "box" to
                                    textStyleOf {
                                        backgroundColor = "#000".asColor()
                                        height = num(30)
                                        padding = NumberOrStringOrNumbers.of(num(0), num(20), num(0), num(20))
                                        align = "right"
                                        width = str("100%")
                                        color = "#eee".asColor()
                                    },
                                "tc" to
                                    textStyleOf {
                                        align = "center"
                                        color = "#eee".asColor()
                                    },
                                "lh" to
                                    textStyleOf {
                                        fontSize = 30.0
                                        color = "rgb(199,86,83)".asColor()
                                        borderWidth = 1.0
                                        borderColor = "rgb(199,86,83)".asColor()
                                    },
                                "vt" to
                                    textStyleOf {
                                        verticalAlign = "top"
                                        backgroundColor = "#991122".asColor()
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(num(1), num(2), num(1), num(2))
                                    },
                                "vb" to
                                    textStyleOf {
                                        verticalAlign = "bottom"
                                        backgroundColor = "#991122".asColor()
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(num(1), num(2), num(1), num(2))
                                    },
                                "vm" to
                                    textStyleOf {
                                        verticalAlign = "middle"
                                        backgroundColor = "#991122".asColor()
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(num(1), num(2), num(1), num(2))
                                    },
                                "l1" to
                                    textStyleOf {
                                        backgroundColor = "#7066FF".asColor()
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "l2" to
                                    textStyleOf {
                                        backgroundColor = "#339911".asColor()
                                        color = "#fff".asColor()
                                        borderRadius = NumberOrStringOrNumbers.of(15)
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "r1" to
                                    textStyleOf {
                                        backgroundColor = "#7066FF".asColor()
                                        align = "right"
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "r2" to
                                    textStyleOf {
                                        backgroundColor = "#339911".asColor()
                                        align = "right"
                                        color = "#fff".asColor()
                                        borderRadius = NumberOrStringOrNumbers.of(15)
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "c1" to
                                    textStyleOf {
                                        backgroundColor = "#7066FF".asColor()
                                        align = "center"
                                        color = "#fff".asColor()
                                        padding = NumberOrStringOrNumbers.of(5)
                                        width = num(100)
                                    },
                                "c2" to
                                    textStyleOf {
                                        backgroundColor = "#339911".asColor()
                                        align = "center"
                                        color = "#fff".asColor()
                                        borderRadius = NumberOrStringOrNumbers.of(15)
                                        padding = NumberOrStringOrNumbers.of(5)
                                        width = num(100)
                                    },
                            )
                    }
                }
            }
            hiddenTextAxes()
        },
        doc.example("bar-rich-text") {
            title { text = "Weather Statistics" }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = AxisPointerOption().apply { type = "shadow" }
            }
            legend { entries("City Alpha", "City Beta", "City Gamma") }
            grid { left = num(100) }
            toolbox {
                show = true
                feature =
                    ToolboxFeatureOption().apply {
                        saveAsImage = ToolboxSaveAsImageFeature().apply { show = true }
                    }
            }
            xAxis {
                type = AxisType.Value
                name = "Days"
                axisLabel { formatter = "{value}" }
            }
            yAxis {
                type = AxisType.Category
                inverse = true
                categories("Sunny", "Cloudy", "Showers")
                axisLabel = weatherAxisLabel()
            }
            series {
                bar {
                    name = "City Alpha"
                    values(165, 170, 30)
                    label { show = true }
                    markPoint = weatherMarkPoint()
                }
                bar {
                    name = "City Beta"
                    values(150, 105, 110)
                    label { show = true }
                }
                bar {
                    name = "City Gamma"
                    values(220, 82, 63)
                    label { show = true }
                }
            }
        },
        doc.example("polar-endAngle") {
            tooltip { }
            angleAxis {
                type = AxisType.Category
                polarIndex = 0
                startAngle = 90.0
                endAngle = 0.0
                categories("S1", "S2", "S3")
            }
            angleAxis {
                type = AxisType.Category
                polarIndex = 1
                startAngle = -90.0
                endAngle = -180.0
                categories("T1", "T2", "T3")
            }
            radiusAxis { polarIndex = 0 }
            radiusAxis { polarIndex = 1 }
            polar { }
            polar { }
            series {
                bar {
                    polarIndex = 0
                    values(1, 2, 3)
                    coordinateSystem = "polar"
                }
                bar {
                    polarIndex = 1
                    values(1, 2, 3)
                    coordinateSystem = "polar"
                }
            }
        },
    )
