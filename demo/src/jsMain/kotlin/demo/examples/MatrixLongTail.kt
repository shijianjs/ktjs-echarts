package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * The matrix examples that lay their cells out at run time and are too big or too interactive for
 * `Matrix.kt`: `matrix-periodic-table` places 120 cells with a custom `renderItem` and annotates the
 * blocks with `graphic` captions positioned through `convertToPixel`.
 *
 * `matrix-mbti` (decals plus a click that swaps the grouped series) and `matrix-stock` (a sparkline
 * table with `dataZoom` and a detail view) are still pending.
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

val matrixLongTailEntries: List<CatalogEntry> =
    listOf(
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
