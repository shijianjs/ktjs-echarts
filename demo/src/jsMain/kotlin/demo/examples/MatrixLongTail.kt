package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.round
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
import zrender.ZRFontWeights
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * The matrix examples that lay their cells out at run time and are too big or too interactive for
 * `Matrix.kt`: `matrix-periodic-table` places 120 cells with a custom `renderItem` and annotates the
 * blocks with `graphic` captions positioned through `convertToPixel`, and `matrix-mbti` draws a 16
 * by 16 compatibility matrix with decal patterns and swaps to the grouped summary on click.
 *
 * `matrix-stock` (a sparkline table of prices, volume, MACD, order book and depth, with titles and
 * grids laid out on the matrix) lives in `MatrixStock.kt`.
 */

private val matrixLongTail = Category(Groups.MATRIX, "examples/MatrixLongTail.kt")

/** One cell of `matrix-periodic-table`. */
private fun PeriodicElement.dataItem(): OptionDataItem =
    OptionDataItem(
        arrayOf(
            OptionDataValue(group),
            OptionDataValue(period),
            number?.let { OptionDataValue(it) },
            OptionDataValue(symbol),
            OptionDataValue(color),
        ),
    )

/**
 * Label of the periodic table: the atomic number above the symbol, and only the symbol for the two
 * placeholder cells (their atomic number is `null`).
 *
 * The original also sets `label.textStyle`, which ECharts 6 does not read: the label falls back to
 * the global `textStyle` like any other label, so that block is not ported.
 */
private fun periodicLabel(): LabelOption =
    unsafeJso {
        show = true
        formatter =
            LabelFormatter { params ->
                val number = textAt(params.value, 2)
                if (number == "null" || number == "undefined") {
                    "{small|${textAt(params.value, 3)}}"
                } else {
                    "${numberAt(params.value, 2).toInt()}\n${textAt(params.value, 3)}"
                }
            }
        rich =
            recordOf(
                "small" to
                    unsafeJso<TextStyleOption> {
                        fontSize = StringOrDouble(12.0)
                        color = "#777"
                    },
            ).unsafeCast<Record<String, TextStyleOption>>()
    }

/** Draws one element cell as a rectangle inset by two pixels inside the matrix cell it occupies. */
private fun periodicRenderItem(): CustomSeriesRenderItem =
    { _, api ->
        val group = api.value(DimensionLoose(0.0))
        val period = api.value(DimensionLoose(1.0))
        val rect = api.layout(CoordinateSystemDataCoord(arrayOf(group, period))).rect
        val left = rect?.x
        val top = rect?.y
        val width = rect?.width
        val height = rect?.height
        // `api.value` returns `NaN` for the `null` atomic number of the two placeholder cells, which
        // is exactly what the original tests with `isNaN`; those cells lose the stroke and half opacity.
        val isElement = !numeric(api.value(DimensionLoose(2.0))).isNaN()
        if (left == null || top == null || width == null || height == null) {
            null
        } else {
            val margin = 2.0
            unsafeJso<CustomPathOption> {
                type = "rect"
                shape =
                    unsafeJso<ZRShapeProps> {
                        x = left + margin
                        y = top + margin
                        this.width = width - margin * 2
                        this.height = height - margin * 2
                    }
                style =
                    api.style(
                        unsafeJso<ZRStyleProps> {
                            fill = ZRColor(textOf(api.value(DimensionLoose(4.0))))
                            stroke = ZRColor("#aaa")
                            lineWidth = if (isElement) 1.0 else 0.0
                            opacity = if (isElement) 1.0 else 0.5
                        },
                    )
            }
        }
    }

/**
 * The option of `matrix-periodic-table`.
 *
 * The original fixes the matrix at 900 pixels and centres it; the demo page is narrower than that on
 * most screens, so the width follows the chart and keeps 80 pixels of the canvas for the captions
 * that sit left of the first column.
 */
private fun periodicTableOption(matrixWidth: Double): EChartsOption =
    unsafeJso {
        this.matrix =
            matrixOption {
                x =
                    dimension {
                        data = names(*(1..19).map { it.toString() }.toTypedArray())
                        label = unsafeJso<LabelOption> { show = false }
                        itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 0.0 }
                        dividerLineStyle = unsafeJso<LineStyleOption> { width = 0.0 }
                    }
                y =
                    dimension {
                        data = names(*(1..10).map { it.toString() }.toTypedArray())
                        label = unsafeJso<LabelOption> { show = false }
                        itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 0.0 }
                        dividerLineStyle = unsafeJso<LineStyleOption> { width = 0.0 }
                    }
                left = StringOrDouble("center")
                this.width = StringOrDouble(matrixWidth)
                backgroundStyle = unsafeJso<ItemStyleOption> { borderWidth = 0.0 }
                body = unsafeJso<MatrixBodyOption> { itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 0.0 } }
            }
        series =
            arrayOf<SeriesOption>(
                customSeries {
                    coordinateSystem = "matrix"
                    typedData = Array<OptionDataItem?>(periodicElements.size) { periodicElements[it].dataItem() }
                    label = periodicLabel()
                    renderItem = periodicRenderItem()
                },
            )
    }

/** One block caption of the periodic table, placed relative to a matrix cell. */
private class PeriodicCaption(
    val group: String,
    val period: String,
    val text: String,
    val dx: Double = 0.0,
    val dy: Double = 0.0,
)

private val periodicCaptions =
    arrayOf(
        PeriodicCaption("2", "9", "Lanthanides", dx = 20.0),
        PeriodicCaption("2", "10", "Actinides", dx = 20.0),
        PeriodicCaption("1", "1", "Nonmetals", dx = -70.0),
        PeriodicCaption("1", "2", "Metals", dx = -70.0),
        PeriodicCaption("19", "1", "Noble gases", dy = -40.0),
        PeriodicCaption("9", "3", "Transition metals\n(somtimes excl. group 12)", dx = -25.0),
        PeriodicCaption("1", "8", "s-block\n(incl. He)", dx = 20.0, dy = -3.0),
        PeriodicCaption("3", "8", "f-block", dy = -10.0),
        PeriodicCaption("9", "8", "d-block", dx = -25.0, dy = -10.0),
        PeriodicCaption("17", "8", "p-block (excl. He)", dx = -25.0, dy = -10.0),
        PeriodicCaption("16", "1", "Some elements near\nthe dashed staircase are\nsometimes called metalloids"),
    )

/**
 * Captions of the blocks, anchored to the pixel centre of their cells.
 *
 * `convertToPixel` returns the pair `[x, y]`; the wrapper leaves the value union opaque, so the pair is
 * cast to an array here.
 */
private fun periodicCaptionsOption(chart: EChartsInstance): EChartsOption {
    val finder = ModelFinder(unsafeJso<ModelFinderObject> { matrixIndex = 0.0 })
    val elements =
        Array<GraphicElement>(periodicCaptions.size) { index ->
            val caption = periodicCaptions[index]
            val center =
                chart
                    .convertToPixel(
                        finder,
                        OptionDataValueOrArray(
                            arrayOf(OptionDataValue(caption.group), OptionDataValue(caption.period)),
                        ),
                    ).unsafeCast<Array<Double>>()
            unsafeJso<GraphicElement> {
                type = "text"
                x = StringOrDouble(center[0] + caption.dx)
                y = StringOrDouble(center[1] + caption.dy)
                style =
                    unsafeJso<ZRStyleProps> {
                        text = caption.text
                        fill = ZRColor("#333")
                        font = "italic bold 14px sans-serif"
                        align = TextAlign.center
                        verticalAlign = VerticalAlign.middle
                    }
            }
        }
    return unsafeJso {
        graphic =
            arrayOf(
                GraphicComponentLooseOption(
                    unsafeJso<GraphicComponentOption> {
                        this.elements = elements
                    },
                ),
            )
    }
}

/* --------------------------------------------------------------------------------------------
 * matrix-mbti
 * ------------------------------------------------------------------------------------------ */

private val MBTI_TYPES =
    arrayOf(
        "ENFJ", "ENFP", "ENTJ", "ENTP",
        "ESFJ", "ESFP", "ESTJ", "ESTP",
        "INFJ", "INFP", "INTJ", "INTP",
        "ISFJ", "ISFP", "ISTJ", "ISTP",
    )

private val MBTI_GROUPS = arrayOf("NF", "NT", "SJ", "SP")

private val mbtiGroupMembers =
    mapOf(
        "NF" to arrayOf("INFJ", "INFP", "ENFJ", "ENFP"),
        "NT" to arrayOf("INTJ", "INTP", "ENTJ", "ENTP"),
        "SJ" to arrayOf("ISFJ", "ISTJ", "ESFJ", "ESTJ"),
        "SP" to arrayOf("ISFP", "ISTP", "ESFP", "ESTP"),
    )

/** The base, lighter and darker tone of one MBTI group. */
private class MbtiShade(val base: String, val lighter: String, val darker: String)

private val mbtiShades =
    mapOf(
        "NF" to MbtiShade("#2D9A69", "#10CA77", "#0FB369"),
        "NT" to MbtiShade("#7D568F", "#9253AF", "#854AA0"),
        "SJ" to MbtiShade("#3A8DAB", "#26A9D9", "#2298C3"),
        "SP" to MbtiShade("#E0A433", "#F4AC24", "#F2A30D"),
    )

private const val MBTI_FONT = "Ubuntu Condensed, sans-serif"

private fun mbtiShadeOf(mbti: String): MbtiShade =
    when {
        mbti.contains("NF") -> mbtiShades.getValue("NF")
        mbti.contains("NT") -> mbtiShades.getValue("NT")
        mbti.contains('S') && mbti.contains('J') -> mbtiShades.getValue("SJ")
        else -> mbtiShades.getValue("SP")
    }

/** Color of a type or group; `lightness` is negative for the lighter tone and positive for the darker one. */
private fun mbtiColor(mbti: String, lightness: Int = 0): String {
    val shade = mbtiShadeOf(mbti)
    return when {
        lightness < 0 -> shade.lighter
        lightness > 0 -> shade.darker
        else -> shade.base
    }
}

/** Group of a type: NF and NT first, then the S plus J / P pairs. */
private fun mbtiGroupOf(mbti: String): String =
    when {
        mbti.contains("NF") -> "NF"
        mbti.contains("NT") -> "NT"
        mbti[1] == 'S' && mbti[3] == 'J' -> "SJ"
        mbti[1] == 'S' && mbti[3] == 'P' -> "SP"
        else -> ""
    }

/**
 * The two-tone pattern of one cell.
 *
 * The original also writes `shape: 'circle'`, which is not a field of `DecalObject`: zrender reads
 * `symbol` and falls back to its `"rect"` default, so the circle never takes effect upstream either.
 */
private fun mbtiDecal(a: String, b: String, tile: Double): DecalObject =
    unsafeJso {
        symbolSize = 1.0
        color = mbtiColor(a, 1)
        backgroundColor = mbtiColor(b, 1)
        dashArrayX =
            DecalDashArrayX(
                arrayOf(
                    DoubleOrDoubleArray(arrayOf(tile, tile)),
                    DoubleOrDoubleArray(arrayOf(0.0, tile, tile, 0.0)),
                ),
            )
        dashArrayY = DoubleOrDoubleArray(arrayOf(tile, 0.0))
    }

/** One header cell of the matrix: a group with its four types nested under it. */
private fun mbtiGroupCell(group: String, groupFont: Double, itemFont: Double): MatrixDimensionCellLooseOption =
    MatrixDimensionCellLooseOption(
        unsafeJso<MatrixDimensionCellOption> {
            value = group
            label =
                unsafeJso<LabelOption> {
                    color = mbtiColor(group)
                    fontSize = StringOrDouble(groupFont)
                    fontWeight = ZRFontWeights.bolder
                    padding = DoubleOrDoubleArray(0.0)
                }
            children =
                Array(mbtiGroupMembers.getValue(group).size) { index ->
                    val type = mbtiGroupMembers.getValue(group)[index]
                    unsafeJso<MatrixDimensionCellOption> {
                        value = type
                        label =
                            unsafeJso<LabelOption> {
                                color = mbtiColor(group)
                                fontSize = StringOrDouble(itemFont)
                                fontWeight = ZRFontWeights.bold
                            }
                    }
                }
        },
    )

private fun mbtiPercent(value: Double): String = "${round(value * 100).toInt()}%"

/** The 256 type pairs with their own cell pattern; the scatter series draws the percentages on top. */
private fun mbtiDetailSeries(): Array<SeriesOption> {
    val rated = HashMap<String, Double>(mbtiPairs.size * 2)
    for (pair in mbtiPairs) rated["${pair.a}-${pair.b}"] = pair.value
    val cells = ArrayList<HeatmapDataItem?>(MBTI_TYPES.size * MBTI_TYPES.size)
    val labels = ArrayList<ScatterDataItem?>(MBTI_TYPES.size * MBTI_TYPES.size)
    for (a in MBTI_TYPES) {
        for (b in MBTI_TYPES) {
            val value = rated["$a-$b"] ?: rated["$b-$a"] ?: 0.0
            cells +=
                HeatmapDataItem(
                    unsafeJso<HeatmapDataItemOption> {
                        typedValue = arrayOf(OptionDataValue(a), OptionDataValue(b), OptionDataValue(value))
                        itemStyle =
                            unsafeJso<ItemStyleOption> {
                                decal = DecalSetting(mbtiDecal(a, b, 1.0))
                                borderColor = ZRColor(mbtiColor(b))
                                borderWidth = 0.0
                            }
                    },
                )
            labels +=
                ScatterDataItem(
                    unsafeJso<ScatterDataItemOption> {
                        typedValue =
                            OptionDataValueOrArray(
                                arrayOf(OptionDataValue(a), OptionDataValue(b), OptionDataValue(value)),
                            )
                        label =
                            unsafeJso<LabelOption> {
                                color = if (value < 0.2) mbtiColor(b) else "#fff"
                                opacity = if (value < 0.15) 0.6 else 1.0
                            }
                    },
                )
        }
    }
    return arrayOf(
        heatmapSeries {
            id = "detail-heatmap"
            coordinateSystem = "matrix"
            typedData = cells.toTypedArray()
            label = unsafeJso<LabelOption> { show = false }
            emphasis = unsafeJso<EmphasisOption> { itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 5.0 } }
        },
        scatterSeries {
            id = "detail-scatter"
            coordinateSystem = "matrix"
            symbolSize = SymbolSize(0.0)
            typedData = labels.toTypedArray()
            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#fff") }
            label =
                unsafeJso<LabelOption> {
                    show = true
                    formatter = LabelFormatter { params -> mbtiPercent(numberAt(params.value, 2)) }
                    fontWeight = ZRFontWeights.bold
                    color = "inherit"
                }
            silent = true
        },
    )
}

private fun mbtiMedian(values: List<Double>): Double {
    val sorted = values.sorted()
    val mid = sorted.size / 2
    return if (sorted.size % 2 == 0) (sorted[mid - 1] + sorted[mid]) / 2 else sorted[mid]
}

/** One cell of the grouped summary: both directions share the label and the pattern of the pair. */
private fun mbtiGroupSeries(fontSize: Double): Array<SeriesOption> {
    val pooled = LinkedHashMap<String, MutableList<Double>>()
    for (pair in mbtiPairs) {
        val groupA = mbtiGroupOf(pair.a)
        val groupB = mbtiGroupOf(pair.b)
        val key = if (groupA > groupB) "$groupA-$groupB" else "$groupB-$groupA"
        pooled.getOrPut(key) { mutableListOf() } += pair.value
    }
    val cells = ArrayList<HeatmapDataItem?>(pooled.size * 2)
    for ((key, values) in pooled) {
        val groups = key.split("-")
        val a = groups[0]
        val b = groups[1]
        val value = mbtiMedian(values)
        val label =
            unsafeJso<LabelOption> {
                color = if (value < 0.2) mbtiColor(b) else "#fff"
                this.fontSize = StringOrDouble(fontSize)
                fontWeight = ZRFontWeights.bold
                opacity = if (value < 0.15) 0.6 else 1.0
                formatter = LabelFormatter { params -> mbtiPercent(numberAt(params.value, 2)) }
            }
        val itemStyle =
            unsafeJso<ItemStyleOption> {
                decal = DecalSetting(mbtiDecal(a, b, 3.0))
                borderColor = ZRColor(mbtiColor(b))
                borderWidth = 0.0
            }
        cells +=
            HeatmapDataItem(
                unsafeJso<HeatmapDataItemOption> {
                    typedValue = arrayOf(OptionDataValue(a), OptionDataValue(b), OptionDataValue(value))
                    this.label = label
                    this.itemStyle = itemStyle
                },
            )
        if (a != b) {
            cells +=
                HeatmapDataItem(
                    unsafeJso<HeatmapDataItemOption> {
                        typedValue = arrayOf(OptionDataValue(b), OptionDataValue(a), OptionDataValue(value))
                        this.label = label
                        this.itemStyle = itemStyle
                    },
                )
        }
    }
    return arrayOf(
        heatmapSeries {
            id = "summary-heatmap"
            coordinateSystem = "matrix"
            typedData = cells.toTypedArray()
            label = unsafeJso<LabelOption> { show = true }
        },
    )
}

private fun mbtiMatrix(canvasWidth: Double, canvasHeight: Double, groupFont: Double, itemFont: Double): Array<MatrixOption> {
    val size = round(kotlin.math.min(canvasWidth, canvasHeight) * 0.9)
    return one(
        unsafeJso<MatrixOption> {
            x =
                dimension {
                    data = Array(MBTI_GROUPS.size) { mbtiGroupCell(MBTI_GROUPS[it], groupFont, itemFont) }
                    itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("transparent"); borderWidth = 0.0 }
                    dividerLineStyle = unsafeJso<LineStyleOption> { width = 0.0 }
                    label = unsafeJso<LabelOption> { fontFamily = MBTI_FONT }
                    levels =
                        arrayOf<MatrixDimensionLevelOption?>(
                            unsafeJso { levelSize = StringOrDouble(25.0) },
                            unsafeJso { levelSize = StringOrDouble(30.0) },
                        )
                }
            y =
                dimension {
                    data = Array(MBTI_GROUPS.size) { mbtiGroupCell(MBTI_GROUPS[it], groupFont, itemFont) }
                    itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("transparent"); borderWidth = 0.0 }
                    dividerLineStyle = unsafeJso<LineStyleOption> { width = 0.0 }
                    label = unsafeJso<LabelOption> { fontFamily = MBTI_FONT }
                }
            body =
                unsafeJso<MatrixBodyOption> {
                    itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 0.0 }
                    label = unsafeJso<LabelOption> { fontFamily = MBTI_FONT }
                }
            this.width = StringOrDouble(size)
            this.height = StringOrDouble(size)
            left = StringOrDouble(round((canvasWidth - size) / 2))
            top = StringOrDouble(50.0)
            backgroundStyle =
                unsafeJso<ItemStyleOption> {
                    color = ZRColor("transparent")
                    borderColor = ZRColor("transparent")
                    borderWidth = 0.0
                }
        },
    )
}

/**
 * The option of `matrix-mbti`, in either the per-type detail or the per-group summary form.
 *
 * The original reads `window.innerWidth` / `innerHeight`; the demo drives the same numbers from the
 * chart size so the page does not depend on the viewport around it.
 */
private fun mbtiOption(width: Double, height: Double, grouped: Boolean): EChartsOption {
    val groupFont = if (height > 700) 24.0 else 16.0
    val itemFont = if (height > 700) 13.0 else 11.0
    val valueFont = if (height > 700) 15.0 else 12.0
    return unsafeJso {
        backgroundColor = ZRColor("#F0F7F9")
        textStyle = unsafeJso<TextStyleOption> { fontFamily = MBTI_FONT }
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "MBTI Partner Compatibility"
                    subtext = "Data from: https://www.personalitydata.org/16-types/enfj-relationships#partner-matrix"
                    sublink = "https://www.personalitydata.org/16-types/enfj-relationships#partner-matrix"
                    left = StringOrDouble("center")
                    top = StringOrDouble(5.0)
                    textStyle =
                        unsafeJso<TextStyleOption> {
                            fontSize = StringOrDouble(20.0)
                            color = "#57576A"
                        }
                    itemGap = 5.0
                },
            )
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    formatter =
                        TooltipFormatter({ params ->
                            val point = params.unsafeCast<CallbackDataParams>()
                            val a = textAt(point.value, 0)
                            val b = textAt(point.value, 1)
                            val percent = mbtiPercent(numberAt(point.value, 2))
                            TooltipContent(
                                "<span style=\"color:${mbtiColor(b)};font-weight:bold\">$b</span> / " +
                                    "<span style=\"color:${mbtiColor(a)};font-weight:bold\">$a</span> : $percent",
                            )
                        })
                    borderColor = ZRColor("#eee")
                    padding = DoubleOrDoubleArray(arrayOf(2.0, 8.0))
                },
            )
        matrix = mbtiMatrix(width, height, groupFont, itemFont)
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    type = VisualMapTypes.continuous
                    min = 0.0
                    max = 1.0
                    dimension = 2.0
                    calculable = true
                    orient = Orients.horizontal
                    top = StringOrDouble(5.0)
                    left = StringOrDouble("center")
                    inRange = unsafeJso<VisualOptionUnit> { opacity = DoubleOrDoubleArray(arrayOf(0.0, 1.0)) }
                    seriesIndex = IndexOrIndexArray(arrayOf(0.0, 2.0))
                    show = false
                },
            )
        series = if (grouped) mbtiGroupSeries(valueFont) else mbtiDetailSeries()
        aria =
            unsafeJso<AriaOption> {
                enabled = true
                decal = unsafeJso<AriaDecalOption> { show = true }
            }
        animation = false
    }
}

val matrixLongTailEntries: List<CatalogEntry> =
    listOf(
        matrixLongTail.interactive(
            "matrix-mbti",
            "MBTI 伴侣相容性",
            "MBTI Partner Compatibility",
            summaryZh = "16×16 的伴侣相容矩阵，每个格子用双色 decal 图案区分两边的类型，点击画布可切换到四组的中位数汇总。",
            summaryEn = "A 16 by 16 partner matrix whose cells carry two-tone decals; click the chart to swap in the four-group median summary.",
        ) { chart ->
            var grouped = false
            chart.setOption(mbtiOption(chart.getWidth(), chart.getHeight(), grouped))
            chart.on(EChartsEvents.Click) {
                grouped = !grouped
                chart.setOption(mbtiOption(chart.getWidth(), chart.getHeight(), grouped), true)
            }
        },
        matrixLongTail.interactive(
            "matrix-periodic-table",
            "元素周期表",
            "Periodic Table",
            summaryZh = "19 组 × 10 周期的矩阵坐标上，custom 系列按 api.layout 铺出 118 个元素，三行类目说明用 graphic 文字标注。",
            summaryEn = "A custom series lays out 118 elements on a 19 × 10 matrix with api.layout; the block captions are graphic text elements.",
        ) { chart, holder ->
            chart.setOption(periodicTableOption(kotlin.math.max(480.0, chart.getWidth() - 160)))
            // The captions need the laid-out matrix, which only exists after the first render.
            holder.setTimeout({ chart.setOption(periodicCaptionsOption(chart)) }, 0)
        },
    )
