package demo.examples.matrix

import echarts.CustomSeriesRenderItem
import echarts.Orients
import echarts.VisualMapTypes
import echarts.options.CoordinateSystemDataCoord
import echarts.options.CustomGroupOption
import echarts.options.CustomTextOption
import echarts.options.OptionEncode
import echarts.options.OptionEncodeValue
import echarts.options.customSeries
import echarts.options.CustomPathOption
import echarts.options.CustomSeriesOption
import echarts.options.DimensionLoose
import echarts.options.GraphicElement
import echarts.options.HeatmapDataItem
import echarts.options.LabelFormatter
import echarts.options.MatrixDimensionCellLooseOption
import echarts.options.MatrixDimensionCellOption
import echarts.options.MatrixDimensionOption
import echarts.options.MatrixOption
import echarts.options.ScatterDataItem
import echarts.options.StringOrDouble
import echarts.options.VisualMapOption
import echarts.options.dataItems
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import demo.examples.chord.column
import demo.examples.custom.extent
import demo.examples.gauge.center
import demo.examples.shared.fixed
import demo.examples.shared.numberAt
import demo.examples.shared.row
import demo.examples.shared.valueText

/**
 * A reproducible replacement for `Math.random()`: a linear congruential sequence returning values
 * in `[0, 1)`.
 */
internal fun sequence(seed: Long): () -> Double {
    var state = seed
    return {
        state = (state * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
        (state % 100000L).toDouble() / 100000.0
    }
}

/** One `matrix` component. */
internal fun matrixOption(block: MatrixOption.() -> Unit): Array<MatrixOption> = one(unsafeJso(block))

/** One dimension of the matrix: a header row or a header column. */
internal fun dimension(block: MatrixDimensionOption.() -> Unit): MatrixDimensionOption = unsafeJso(block)

/** A header cell, optionally with nested cells. */
internal fun cell(value: String, vararg kids: MatrixDimensionCellOption): MatrixDimensionCellOption =
    unsafeJso {
        this.value = value
        if (kids.isNotEmpty()) children = arrayOf(*kids)
    }

/** Header cells that are plain names. */
internal fun names(vararg values: String): Array<MatrixDimensionCellLooseOption> =
    Array(values.size) { MatrixDimensionCellLooseOption(values[it]) }

/** A header cell that has children of its own. */
internal fun branch(value: String, vararg kids: MatrixDimensionCellOption): MatrixDimensionCellLooseOption =
    MatrixDimensionCellLooseOption(cell(value, *kids))

/** `[x, y, value]` points of a matrix heatmap. */
internal fun heatmapPoints(points: List<Triple<String, String, Double>>): Array<HeatmapDataItem?> =
    dataItems(*points.map { (x, y, value) -> HeatmapDataItem(row(x, y, value)) }.toTypedArray())

/** `[x, y, value]` points of a matrix scatter series. */
internal fun scatterPoints(points: List<Triple<String, String, Double>>): Array<ScatterDataItem?> =
    dataItems(*points.map { (x, y, value) -> ScatterDataItem(row(x, y, value)) }.toTypedArray())

/** A continuous visual map that colors the value of dimension 2. */
internal fun valueVisualMap(min: Double, max: Double, block: VisualMapOption.() -> Unit = {}): Array<VisualMapOption> =
    one(
        unsafeJso {
            type = VisualMapTypes.continuous
            this.min = min
            this.max = max
            dimension = 2.0
            calculable = true
            orient = Orients.horizontal
            top = StringOrDouble(5.0)
            left = StringOrDouble("center")
            block()
        },
    )

/** Label formatter of the correlation matrices: two decimals of the value of dimension 2. */
internal fun twoDecimals(): LabelFormatter =
    LabelFormatter { params -> fixed(numberAt(params.value, 2), 2) }

/** One `custom` series of the mini-bar example, placed on the matrix cell of `encode.x` / `encode.y`. */
internal fun miniBarSeries(xDim: Int, yDim: Int, valueDim: Int): CustomSeriesOption =
    customSeries {
        coordinateSystem = "matrix"
        encode =
            unsafeJso<OptionEncode> {
                x = OptionEncodeValue(DimensionLoose(xDim.toDouble()))
                y = OptionEncodeValue(DimensionLoose(yDim.toDouble()))
            }
        renderItem = miniBarRenderItem(xDim, yDim, valueDim)
    }

/** The value extent the mini bars are scaled to, as in the original example. */
internal val miniBarExtent = doubleArrayOf(0.0, 10000.0)

/** Draws one mini bar plus its value next to the bar, inside the cell the layout returns. */
internal fun miniBarRenderItem(xDim: Int, yDim: Int, valueDim: Int): CustomSeriesRenderItem =
    { _, api ->
        val xValue = api.value(DimensionLoose(xDim.toDouble()))
        val yValue = api.value(DimensionLoose(yDim.toDouble()))
        val labelValue = api.value(DimensionLoose(valueDim.toDouble())).unsafeCast<Double?>()
        val rect = api.layout(CoordinateSystemDataCoord(arrayOf(xValue, yValue))).rect
        if (rect == null || labelValue == null) {
            null
        } else {
            val rectY = rect.y!!
            val rectHeight = rect.height!!
            val rectWidth = rect.width!!
            val height = rectHeight * 0.2
            val barY = rectY + (rectHeight - height) / 4 * 3
            val barX = rect.x!! + rectWidth * 0.15
            val width = linearMap(labelValue, miniBarExtent, doubleArrayOf(0.0, rectWidth * 0.5))
            unsafeJso<CustomGroupOption> {
                type = "group"
                children =
                    arrayOf(
                        unsafeJso<CustomPathOption> {
                            type = "rect"
                            shape =
                                unsafeJso<ZRShapeProps> {
                                    x = barX
                                    y = barY
                                    this.width = width
                                    this.height = height
                                }
                            style = api.style(unsafeJso<ZRStyleProps> { fill = ZRColor("#0ca8df") })
                        },
                        unsafeJso<CustomTextOption> {
                            type = "text"
                            x = barX
                            y = rectY + rectHeight / 4 * 1.5
                            style =
                                unsafeJso<ZRStyleProps> {
                                    text = valueText(labelValue)
                                    fill = ZRColor("#333")
                                    align = web.cssom.TextAlign.left
                                    verticalAlign = web.cssom.VerticalAlign.middle
                                }
                        },
                    )
            }
        }
    }

/** Maps [value] from [domain] onto [range], the way the original `linearMap` helper does. */
internal fun linearMap(value: Double, domain: DoubleArray, range: DoubleArray): Double {
    val subDomain = domain[1] - domain[0]
    val subRange = range[1] - range[0]
    return when {
        subDomain == 0.0 -> if (subRange == 0.0) range[0] else (range[0] + range[1]) / 2
        value == domain[0] -> range[0]
        value == domain[1] -> range[1]
        else -> (value - domain[0]) / subDomain * subRange + range[0]
    }
}

/**
 * A `text` element of a `graphic` component, optionally rotated in degrees.
 *
 * The position goes through `left` / `top` rather than `x` / `y`: the latter is the raw zrender
 * prop, which takes a number only, so a percentage string would land the element at the origin.
 */
internal fun textElement(
    text: String,
    left: StringOrDouble,
    top: StringOrDouble,
    rotation: Double,
): echarts.options.GraphicElement =
    unsafeJso {
        type = "text"
        this.left = left
        this.top = top
        this.rotation = rotation
        style =
            unsafeJso {
                this.text = text
                fill = ZRColor("#333")
                font = "bold 24px serif"
                align = web.cssom.TextAlign.center
            }
    }
