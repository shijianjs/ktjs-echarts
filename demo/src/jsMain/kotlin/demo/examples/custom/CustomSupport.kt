package demo.examples.custom

import io.github.shijianjs.ktjs.echarts.AxisPointerTypes
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.BrushToolboxIconTypes
import io.github.shijianjs.ktjs.echarts.CustomSeriesRenderItemAPI
import io.github.shijianjs.ktjs.echarts.CustomSeriesRenderItemReturn
import io.github.shijianjs.ktjs.echarts.DataZoomTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.VisualProps
import io.github.shijianjs.ktjs.echarts.options.AxisLineOption
import io.github.shijianjs.ktjs.echarts.options.AxisMinMax
import io.github.shijianjs.ktjs.echarts.options.AxisPointerLink
import io.github.shijianjs.ktjs.echarts.options.AxisPointerOption
import io.github.shijianjs.ktjs.echarts.options.BooleanOrString
import io.github.shijianjs.ktjs.echarts.options.BooleanOrStringArray
import io.github.shijianjs.ktjs.echarts.options.CoordinateSystemDataCoord
import io.github.shijianjs.ktjs.echarts.options.CustomBaseElementOption
import io.github.shijianjs.ktjs.echarts.options.CustomElementOption
import io.github.shijianjs.ktjs.echarts.options.CustomGroupOption
import io.github.shijianjs.ktjs.echarts.options.CustomPathOption
import io.github.shijianjs.ktjs.echarts.options.DataZoomOption
import io.github.shijianjs.ktjs.echarts.options.DimensionDefinitionLoose
import io.github.shijianjs.ktjs.echarts.options.DimensionLoose
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.GridOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.ModelFinderIndexQuery
import io.github.shijianjs.ktjs.echarts.options.OptionDataItem
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.OptionDataValueOrArray
import io.github.shijianjs.ktjs.echarts.options.OptionEncode
import io.github.shijianjs.ktjs.echarts.options.OptionEncodeValue
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SplitAreaOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.ToolboxBrushFeatureOption
import io.github.shijianjs.ktjs.echarts.options.ToolboxDataZoomFeatureOption
import io.github.shijianjs.ktjs.echarts.options.ToolboxFeature
import io.github.shijianjs.ktjs.echarts.options.ToolboxOption
import io.github.shijianjs.ktjs.echarts.options.TooltipBoxLayoutOption
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.TooltipPosition
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.customSeries
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRStyleProps
import demo.examples.chord.column
import demo.examples.funnel.labels
import demo.examples.gauge.center
import demo.examples.lines.coordinate
import demo.examples.matrix.dimension
import demo.examples.scatter.point
import demo.examples.shared.legendNames
import demo.examples.shared.numberArray
import demo.examples.shared.numeric
import demo.examples.shared.row
import demo.examples.shared.textOf
import demo.examples.shared.toolbox

/** A reproducible replacement for `Math.random()`; see the calendar examples. */
internal fun sequence(seed: Long): () -> Double {
    var state = seed
    return {
        state = state * 6364136223846793005L + 1442695040888963407L and Long.MAX_VALUE
        (state % 100000L).toDouble() / 100000.0
    }
}

/** The dimension index [index] of a data item, as `api.value` / `encode` take it. */
internal fun dim(index: Int): DimensionLoose = DimensionLoose(index.toDouble())

/** A data coordinate of a cartesian coordinate system. */
internal fun coord(vararg values: OptionDataValue?): CoordinateSystemDataCoord =
    CoordinateSystemDataCoord(arrayOf(*values))

/** A data extent handed to `api.size`. */
internal fun extent(vararg values: OptionDataValue?): OptionDataValueOrArray =
    OptionDataValueOrArray(arrayOf(*values))

/** A `line` element of a custom element tree. */
internal fun lineElement(style: ZRStyleProps, block: io.github.shijianjs.ktjs.zrender.ZRShapeProps.() -> Unit): CustomPathOption =
    unsafeJso {
        type = "line"
        this.style = style
        shape = unsafeJso(block)
    }

/** A `group` element that holds [elements]. */
internal fun groupElement(vararg elements: CustomBaseElementOption): CustomGroupOption =
    unsafeJso {
        type = "group"
        children = Array<CustomElementOption>(elements.size) { elements[it] }
    }

/** `encode: { x: <x>, y: <y> }`. */
internal fun xyEncode(x: OptionEncodeValue, y: OptionEncodeValue): OptionEncode =
    unsafeJso {
        this.x = x
        this.y = y
    }

/** The `dimensions` of the error scatter example. */
internal val errorDimensions =
    arrayOf(
        DimensionDefinitionLoose("name"),
        DimensionDefinitionLoose("Price"),
        DimensionDefinitionLoose("Prime cost"),
        DimensionDefinitionLoose("Prime cost min"),
        DimensionDefinitionLoose("Prime cost max"),
        DimensionDefinitionLoose("Price min"),
        DimensionDefinitionLoose("Price max"),
    )

/**
 * Columns of one row of `stock-DJI.json`: date, open, close, lowest, highest, volume.
 */
internal const val OHLC_DATE = 0

internal const val OHLC_OPEN = 1

internal const val OHLC_CLOSE = 2

internal const val OHLC_LOWEST = 3

internal const val OHLC_HIGHEST = 4

/** The two arrays `splitData()` of `custom-ohlc` produces out of the quote table. */
internal class OhlcSplit(
    val dates: Array<OptionDataItem?>,
    val values: Array<OptionDataItem?>,
)

/**
 * Splits `stock-DJI.json` into the category labels and the `[index, open, close, lowest, highest]`
 * rows; the original overwrites the date column with the index in place, the copy here keeps both.
 */
internal fun splitOhlc(rows: Array<Array<JsAny>>): OhlcSplit =
    OhlcSplit(
        dates = Array<OptionDataItem?>(rows.size) { index -> OptionDataItem(OptionDataValue(textOf(rows[index][OHLC_DATE]))) },
        values =
            Array<OptionDataItem?>(rows.size) { index ->
                val row = rows[index]
                OptionDataItem(
                    arrayOf<OptionDataValue?>(
                        OptionDataValue(index.toDouble()),
                        OptionDataValue(numeric(row[OHLC_OPEN])),
                        OptionDataValue(numeric(row[OHLC_CLOSE])),
                        OptionDataValue(numeric(row[OHLC_LOWEST])),
                        OptionDataValue(numeric(row[OHLC_HIGHEST])),
                    ),
                )
            },
    )

/** `renderItem` of `custom-ohlc`: the high-low line plus the open and close ticks. */
internal fun ohlcRenderItem(api: CustomSeriesRenderItemAPI): CustomSeriesRenderItemReturn {
    val xValue = api.value(dim(0)) ?: return null
    fun point(column: Int): Array<Double> = api.coord(coord(xValue, api.value(dim(column))))
    val open = point(OHLC_OPEN)
    val close = point(OHLC_CLOSE)
    val low = point(OHLC_LOWEST)
    val high = point(OHLC_HIGHEST)
    val halfWidth = numberArray(api.size(extent(OptionDataValue(1.0), OptionDataValue(0.0))))[0] * 0.35
    val style = api.style(unsafeJso<ZRStyleProps> { stroke = ZRColor(textOf(api.visual(VisualProps.color))) })
    return groupElement(
        lineElement(style) {
            x1 = low[0]
            y1 = low[1]
            x2 = high[0]
            y2 = high[1]
        },
        lineElement(style) {
            x1 = open[0]
            y1 = open[1]
            x2 = open[0] - halfWidth
            y2 = open[1]
        },
        lineElement(style) {
            x1 = close[0]
            y1 = close[1]
            x2 = close[0] + halfWidth
            y2 = close[1]
        },
    )
}

/** Option of `custom-ohlc`, built once `stock-DJI.json` has loaded. */
internal fun ohlcOption(split: OhlcSplit): EChartsOption =
    unsafeJso {
        animation = false
        legend =
            one(
                unsafeJso<LegendOption> {
                    bottom = StringOrDouble(10.0)
                    left = StringOrDouble("center")
                    data = legendNames("Dow-Jones index")
                },
            )
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.axis
                    axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                    position =
                        TooltipPosition(
                            { point, _, _, _, size ->
                                val box =
                                    unsafeJso<TooltipBoxLayoutOption> {
                                        top = StringOrDouble(10.0)
                                        if (point[0] < size.viewSize[0] / 2.0) {
                                            right = StringOrDouble(30.0)
                                        } else {
                                            left = StringOrDouble(30.0)
                                        }
                                    }
                                TooltipPosition(box)
                            },
                        )
                },
            )
        axisPointer =
            one(
                unsafeJso<AxisPointerOption> {
                    link = one(unsafeJso<AxisPointerLink> { xAxisIndex = ModelFinderIndexQuery("all") })
                },
            )
        toolbox =
            one(
                unsafeJso<ToolboxOption> {
                    feature =
                        unsafeJso<ToolboxFeature> {
                            dataZoom =
                                unsafeJso<ToolboxDataZoomFeatureOption> {
                                    yAxisIndex = ModelFinderIndexQuery(false)
                                }
                            brush =
                                unsafeJso<ToolboxBrushFeatureOption> {
                                    type = arrayOf(BrushToolboxIconTypes.lineX, BrushToolboxIconTypes.clear)
                                }
                        }
                },
            )
        grid = one(unsafeJso<GridOption> { left = StringOrDouble("10%"); right = StringOrDouble("8%"); bottom = StringOrDouble(150.0) })
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    data = split.dates
                    boundaryGap = BooleanOrStringArray(false)
                    axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                    splitLine = unsafeJso<SplitLineOption> { show = false }
                    min = AxisMinMax("dataMin")
                    max = AxisMinMax("dataMax")
                    axisPointer = unsafeJso<AxisPointerOption> { z = 100.0 }
                },
            )
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    scale = true
                    splitArea = unsafeJso<SplitAreaOption> { show = true }
                },
            )
        dataZoom =
            arrayOf(
                unsafeJso<DataZoomOption> {
                    type = DataZoomTypes.inside
                    start = 98.0
                    end = 100.0
                    minValueSpan = 10.0
                },
                unsafeJso<DataZoomOption> {
                    show = true
                    type = DataZoomTypes.slider
                    bottom = StringOrDouble(60.0)
                    start = 98.0
                    end = 100.0
                    minValueSpan = 10.0
                },
            )
        series =
            arrayOf<SeriesOption>(
                customSeries {
                    name = "Dow-Jones index"
                    dimensions =
                        arrayOf(
                            DimensionDefinitionLoose("-"),
                            DimensionDefinitionLoose("open"),
                            DimensionDefinitionLoose("close"),
                            DimensionDefinitionLoose("lowest"),
                            DimensionDefinitionLoose("highest"),
                        )
                    encode =
                        unsafeJso<OptionEncode> {
                            x = OptionEncodeValue(dim(0))
                            y = OptionEncodeValue(arrayOf(dim(1), dim(2), dim(3), dim(4)))
                            tooltip = OptionEncodeValue(arrayOf(dim(1), dim(2), dim(3), dim(4)))
                        }
                    typedData = split.values
                    renderItem = { _, api -> ohlcRenderItem(api) }
                },
            )
    }
