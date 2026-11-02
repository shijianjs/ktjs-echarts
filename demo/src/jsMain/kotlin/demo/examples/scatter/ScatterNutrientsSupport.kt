package demo.examples.scatter

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import kotlin.js.unsafeCast
import kotlin.math.round
import zrender.RectLike
import zrender.ZRColor
import zrender.ZRLineTypes
import demo.examples.chord.column
import demo.examples.matrix.cell
import demo.examples.matrix.dimension
import demo.examples.matrix.names
import demo.examples.shared.axisLabel
import demo.examples.shared.every
import demo.examples.shared.numberAt
import demo.examples.shared.numeric
import demo.examples.shared.row
import demo.examples.shared.textAt

/** Column indices of `nutrients.json`, in the order of the upstream schema. */
internal const val NUTRIENT_NAME = 0

internal const val NUTRIENT_GROUP = 1

internal const val NUTRIENT_PROTEIN = 2

internal const val NUTRIENT_CALCIUM = 3

internal const val NUTRIENT_FIBER = 5

internal const val NUTRIENT_POTASSIUM = 7

internal const val NUTRIENT_CARBOHYDRATE = 8

internal const val NUTRIENT_ID = 16

/** The rows the two scatter pages keep: upstream takes `slice(0, 1000)` of the table. */
internal const val NUTRIENT_SCATTER_ROWS = 1000

/** One normalized table of `nutrients.json`: the group names, their colours and the rows. */
internal class NutrientTable(
    val groups: Array<String>,
    val colors: Array<String>,
    val rows: Array<Array<JsAny>>,
)

/** `parseFloat(value) || 0` of the upstream normalization; an empty cell becomes zero. */
internal fun parseNutrient(value: JsAny?): Double = js("(value) => parseFloat(value) || 0")(value) as Double

/** Passes a Kotlin number through a `JsAny` parameter such as the hue of `modifyHSL`. */
internal fun jsNumber(value: Double): JsAny = js("(value) => value")(value) as JsAny

/**
 * The upstream `normalizeData`: numeric columns become numbers, and every distinct group name gets
 * the next hue of the shared `#5A94DF` ramp.
 */
internal fun normalizeNutrients(json: JsAny, limit: Int): NutrientTable {
    val raw = json.unsafeCast<Array<Array<JsAny>>>()
    val names = LinkedHashSet<String>()
    raw.forEach { row -> names.add(textAt(row, NUTRIENT_GROUP)) }
    val groups = names.toTypedArray()
    val hStep = round(300.0 / (groups.size - 1))
    val colors = Array(groups.size) { index -> ECharts.color.modifyHSL("#5A94DF", jsNumber(hStep * index)) }
    val rows =
        Array(minOf(limit, raw.size)) { index ->
            val source = raw[index]
            Array(source.size) { column ->
                when (column) {
                    NUTRIENT_NAME, NUTRIENT_GROUP, NUTRIENT_ID -> source[column]
                    else -> jsNumber(parseNutrient(source[column]))
                }
            }
        }
    return NutrientTable(groups, colors, rows)
}

/** The piecewise visual map every nutrient page shares: one colour per group. */
internal fun nutrientVisualMap(table: NutrientTable, dimension: Double): VisualMapOption =
    unsafeJso {
        show = false
        this.dimension = dimension
        categories = strings<OptionDataItem>(*table.groups).unsafeCast<JsAny>()
        inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(table.colors) }
        outOfRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(arrayOf("#ccc")) }
        top = StringOrDouble(20.0)
        textStyle = unsafeJso<LabelOption> { color = "#fff" }
        realtime = false
    }

/** `[protein, calcium, group, index]` of one row, the tuple `scatter-nutrients` draws. */
internal fun nutrientPoint(row: Array<JsAny>, index: Int): ScatterDataItem =
    ScatterDataItem(
        row(
            numberAt(row, NUTRIENT_PROTEIN),
            numberAt(row, NUTRIENT_CALCIUM),
            textAt(row, NUTRIENT_GROUP),
            index.toDouble(),
        ),
    )

/** The `scatter-nutrients` option; all colours come from the two visual maps. */
internal fun nutrientScatterOption(table: NutrientTable): EChartsOption =
    unsafeJso {
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    name = "protein"
                    splitLine = unsafeJso<SplitLineOption> { show = false }
                },
            )
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    name = "calcium"
                    splitLine = unsafeJso<SplitLineOption> { show = false }
                },
            )
        visualMap =
            arrayOf(
                nutrientVisualMap(table, dimension = 2.0),
                unsafeJso {
                    show = false
                    dimension = 3.0
                    max = 100.0
                    inRange = unsafeJso<VisualOptionUnit> { colorLightness = DoubleOrDoubleArray(arrayOf(0.15, 0.6)) }
                },
            )
        series =
            one<SeriesOption>(
                scatterSeries {
                    zlevel = 1.0
                    name = "nutrients"
                    typedData = Array(table.rows.size) { index -> nutrientPoint(table.rows[index], index) }
                    animationThreshold = 5000.0
                    progressiveThreshold = 5000.0
                },
            )
        animationEasingUpdate = AnimationEasings.cubicInOut
        animationDurationUpdate = AnimationDuration(2000.0)
    }

/** The four panes of the matrix; the axis colour of each pane side comes from the upstream table. */
internal val NUTRIENT_MATRIX_COLORS =
    mapOf(
        "xAxisLeft" to "#2A8339",
        "xAxisRight" to "#367DA6",
        "yAxisTop" to "#A68B36",
        "yAxisBottom" to "#BD5692",
    )

/** The four axis fields of the matrix page, in legend order: left/top, left/bottom, right/top, right/bottom. */
internal val NUTRIENT_MATRIX_PANES =
    arrayOf(
        "xAxisLeft-yAxisTop",
        "xAxisLeft-yAxisBottom",
        "xAxisRight-yAxisTop",
        "xAxisRight-yAxisBottom",
    )

/** The schema field drawn on each side of every pane. */
internal val NUTRIENT_MATRIX_FIELDS =
    mapOf(
        "xAxisLeft-yAxisTop" to ("carbohydrate" to "calcium"),
        "xAxisLeft-yAxisBottom" to ("carbohydrate" to "fiber"),
        "xAxisRight-yAxisTop" to ("potassium" to "calcium"),
        "xAxisRight-yAxisBottom" to ("potassium" to "fiber"),
    )

/** One x axis of the matrix, named after its schema field and coloured after its pane. */
internal fun nutrientMatrixXAxis(pane: String): XAxisOption {
    val color = NUTRIENT_MATRIX_COLORS.getValue(if (pane.startsWith("xAxisLeft")) "xAxisLeft" else "xAxisRight")
    return unsafeJso {
        id = pane
        name = NUTRIENT_MATRIX_FIELDS.getValue(pane).first
        nameLocation = AxisNameLocations.middle
        nameGap = 30.0
        gridId = pane
        splitLine = unsafeJso<SplitLineOption> { show = false }
        axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { this.color = ZRColor(color) } }
        axisLabel = unsafeJso<AxisLabelOption> { this.color = color }
        axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { this.color = ZRColor(color) } }
    }
}

/** One y axis of the matrix; the pane selects both its colour and the `calcium` / `fiber` field. */
internal fun nutrientMatrixYAxis(pane: String): YAxisOption {
    val top = pane.substringAfter('-') == "yAxisTop"
    val color = NUTRIENT_MATRIX_COLORS.getValue(if (top) "yAxisTop" else "yAxisBottom")
    val name = NUTRIENT_MATRIX_FIELDS.getValue(pane).second
    return unsafeJso {
        id = pane
        this.name = name
        nameLocation = AxisNameLocations.end
        nameGap = 10.0
        gridId = pane
        splitLine = unsafeJso<SplitLineOption> { show = false }
        axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { this.color = ZRColor(color) } }
        axisLabel = unsafeJso<AxisLabelOption> { this.color = color }
        axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { this.color = ZRColor(color) } }
    }
}

/** One pane of the 2x2 matrix; [left] and [top] pin it to its corner. */
internal fun nutrientMatrixGrid(pane: String): GridOption {
    val left = pane.startsWith("xAxisLeft")
    val top = pane.substringAfter('-') == "yAxisTop"
    return unsafeJso {
        id = pane
        if (left) this.left = StringOrDouble(80.0) else this.right = StringOrDouble(50.0)
        if (top) this.top = StringOrDouble(50.0) else this.bottom = StringOrDouble(80.0)
        width = StringOrDouble("35%")
        height = StringOrDouble("35%")
    }
}

/** The four sliders of the matrix: two horizontal under the panes, two vertical next to them. */
internal fun nutrientMatrixDataZoom(): Array<DataZoomOption> =
    arrayOf(
        unsafeJso {
            type = DataZoomTypes.slider
            filterMode = FilterModes.empty
            realtime = false
            width = StringOrDouble("35%")
            height = StringOrDouble(20.0)
            left = StringOrDouble(80.0)
            bottom = StringOrDouble(10.0)
            xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
        },
        unsafeJso {
            type = DataZoomTypes.slider
            filterMode = FilterModes.empty
            realtime = false
            width = StringOrDouble("35%")
            height = StringOrDouble(20.0)
            right = StringOrDouble(50.0)
            bottom = StringOrDouble(10.0)
            xAxisIndex = IndexOrIndexArray(arrayOf(2.0, 3.0))
        },
        unsafeJso {
            type = DataZoomTypes.slider
            filterMode = FilterModes.empty
            realtime = false
            orient = Orients.vertical
            width = StringOrDouble(20.0)
            height = StringOrDouble("35%")
            left = StringOrDouble(10.0)
            top = StringOrDouble(50.0)
            yAxisIndex = IndexOrIndexArray(arrayOf(0.0, 2.0))
        },
        unsafeJso {
            type = DataZoomTypes.slider
            filterMode = FilterModes.empty
            realtime = false
            orient = Orients.vertical
            width = StringOrDouble(20.0)
            height = StringOrDouble("35%")
            left = StringOrDouble(10.0)
            bottom = StringOrDouble(80.0)
            yAxisIndex = IndexOrIndexArray(arrayOf(1.0, 3.0))
        },
    )

/** `item.axisDim` of an axis-pointer tooltip item; the wrapper types do not model this field. */
internal fun axisDimOf(item: CallbackDataParams): String = js("(item) => item.axisDim")(item) as String

/**
 * The crosshair tooltip of the matrix: every pane reports its point, and the points that share a
 * name are folded into one block per cursor dimension.
 */
internal fun nutrientMatrixTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val items = params.unsafeCast<Array<CallbackDataParams>>()
            val byName = LinkedHashMap<String, MutableMap<String, Double>>()
            val onX = LinkedHashMap<String, MutableMap<String, Double>>()
            val onY = LinkedHashMap<String, MutableMap<String, Double>>()
            val onXY = LinkedHashMap<String, MutableMap<String, Double>>()
            items.forEach { item ->
                val data = item.data
                val name = textAt(data, 3)
                val fields = byName.getOrPut(name) { LinkedHashMap() }
                fields[textAt(data, 4)] = numberAt(data, 0)
                fields[textAt(data, 5)] = numberAt(data, 1)
                when (axisDimOf(item)) {
                    "x" -> onX[name] = fields
                    "y" -> onY[name] = fields
                    else -> onXY[name] = fields
                }
            }
            byName.forEach { (name, fields) ->
                if (onX.containsKey(name) && onY.containsKey(name)) {
                    onXY[name] = fields
                    onX.remove(name)
                    onY.remove(name)
                }
            }
            val result = StringBuilder()
            listOf(onXY to "CROSS", onX to "V LINE", onY to "H LINE").forEach { (points, label) ->
                if (points.isEmpty()) return@forEach
                result.append("<div style=\"margin: 10px 0\"><div style=\"font-size: 16px; color: #aaa\">POINTS ON ")
                result.append(label).append("</div>")
                points.forEach { (name, fields) ->
                    result.append("<div style=\"margin: 10px 0\">").append(name).append("<br/>")
                    result.append(
                        fields.entries.joinToString("<br/>") { (field, value) ->
                            "<span style=\"color:${NUTRIENT_MATRIX_COLORS.getValue(field)}\">$field</span>: $value"
                        },
                    )
                    result.append("</div>")
                }
                result.append("</div>")
            }
            TooltipContent(result.toString())
        },
    )

/** One pane series of the matrix: `[x, y, group, name, schemaX, schemaY, index]` per row. */
internal fun nutrientMatrixSeries(pane: String, table: NutrientTable): ScatterSeriesOption {
    val (xName, yName) = NUTRIENT_MATRIX_FIELDS.getValue(pane)
    val xField = NUTRIENT_FIELDS.getValue(xName)
    val yField = NUTRIENT_FIELDS.getValue(yName)
    return scatterSeries {
        zlevel = 1.0
        name = "nutrients"
        xAxisId = pane
        yAxisId = pane
        symbolSize = SymbolSize(8.0)
        emphasis = unsafeJso<EmphasisOption> { itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#fff") } }
        typedData =
            Array(table.rows.size) { index ->
                val row = table.rows[index]
                ScatterDataItem(
                    row(
                        numberAt(row, xField),
                        numberAt(row, yField),
                        textAt(row, NUTRIENT_GROUP),
                        textAt(row, NUTRIENT_NAME),
                        xName,
                        yName,
                        index.toDouble(),
                    ),
                )
            }
    }
}

/** Schema name to column index, for the four fields the matrix panes draw. */
internal val NUTRIENT_FIELDS =
    mapOf(
        "protein" to NUTRIENT_PROTEIN,
        "calcium" to NUTRIENT_CALCIUM,
        "fiber" to NUTRIENT_FIBER,
        "potassium" to NUTRIENT_POTASSIUM,
        "carbohydrate" to NUTRIENT_CARBOHYDRATE,
    )

/** The option of `scatter-nutrients-matrix`: four linked panes over the same rows. */
internal fun nutrientMatrixOption(table: NutrientTable): EChartsOption =
    unsafeJso {
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.none
                    padding = DoubleOrDoubleArray(arrayOf(10.0, 20.0, 10.0, 20.0))
                    backgroundColor = ZRColor("rgba(0,0,0,0.7)")
                    transitionDuration = 0.0
                    extraCssText = "width: 300px; white-space: normal"
                    textStyle =
                        unsafeJso<LabelOption> {
                            color = "#fff"
                            fontSize = StringOrDouble(12.0)
                        }
                    formatter = nutrientMatrixTooltip()
                    position =
                        TooltipPosition(
                            fun(
                                point: Array<Double>,
                                _: TopLevelFormatterParams,
                                _: JsAny?,
                                _: RectLike?,
                                size: TooltipSize,
                            ): TooltipPosition =
                                TooltipPosition(
                                    unsafeJso<TooltipBoxLayoutOption> {
                                        if (point[0] < size.viewSize[0] / 2) {
                                            left = StringOrDouble(60.0)
                                        } else {
                                            right = StringOrDouble(60.0)
                                        }
                                        if (point[1] < size.viewSize[1] / 2) {
                                            top = StringOrDouble(20.0)
                                        } else {
                                            bottom = StringOrDouble(20.0)
                                        }
                                    },
                                ),
                        )
                },
            )
        axisPointer =
            one(
                unsafeJso<AxisPointerOption> {
                    show = BooleanOrString(true)
                    snap = true
                    lineStyle = unsafeJso<LineStyleOption> { type = ZRLineTypes.dashed }
                    label =
                        unsafeJso<AxisPointerLabelOption> {
                            show = true
                            margin = 6.0
                            backgroundColor = ZRColor("#556")
                            color = "#fff"
                        }
                    link =
                        arrayOf(
                            unsafeJso<AxisPointerLink> { xAxisId = arrayOf("xAxisLeft-yAxisTop", "xAxisLeft-yAxisBottom") },
                            unsafeJso<AxisPointerLink> { xAxisId = arrayOf("xAxisRight-yAxisTop", "xAxisRight-yAxisBottom") },
                            unsafeJso<AxisPointerLink> { yAxisId = arrayOf("xAxisLeft-yAxisTop", "xAxisRight-yAxisTop") },
                            unsafeJso<AxisPointerLink> { yAxisId = arrayOf("xAxisLeft-yAxisBottom", "xAxisRight-yAxisBottom") },
                        )
                },
            )
        xAxis = Array(NUTRIENT_MATRIX_PANES.size) { index -> nutrientMatrixXAxis(NUTRIENT_MATRIX_PANES[index]) }
        yAxis = Array(NUTRIENT_MATRIX_PANES.size) { index -> nutrientMatrixYAxis(NUTRIENT_MATRIX_PANES[index]) }
        grid = Array(NUTRIENT_MATRIX_PANES.size) { index -> nutrientMatrixGrid(NUTRIENT_MATRIX_PANES[index]) }
        dataZoom = nutrientMatrixDataZoom()
        visualMap = one(nutrientVisualMap(table, dimension = 2.0))
        series = Array<SeriesOption>(NUTRIENT_MATRIX_PANES.size) { index -> nutrientMatrixSeries(NUTRIENT_MATRIX_PANES[index], table) }
        animationThreshold = 5000.0
        progressiveThreshold = 5000.0
        animationEasingUpdate = AnimationEasings.cubicInOut
        animationDurationUpdate = AnimationDuration(2000.0)
    }
