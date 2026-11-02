package demo.examples.scatter

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import kotlin.js.unsafeCast
import zrender.ZRColor
import zrender.ZRLineTypes
import demo.examples.shared.axisLabel
import demo.examples.shared.row
import demo.examples.shared.textAt
import demo.examples.shared.valueText

/** The series of the page, one per table. */
internal fun weightSeries(
    name: String,
    rows: Array<DoubleArray>,
    averageName: String,
    splitHeight: Double,
): SeriesOption =
    scatterSeries {
        this.name = name
        emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
        typedData =
            dataItems(
                *Array(rows.size) { index -> ScatterDataItem(row(rows[index][0], rows[index][1])) },
            )
        markArea = dataRangeArea(name + " Data Range")
        markPoint = extremes()
        markLine = averageLine(averageName, splitHeight)
    }

/** The dashed rectangle around all points of one series, the upstream `markArea`. */
internal fun dataRangeArea(name: String): MarkAreaOption =
    unsafeJso {
        silent = true
        itemStyle =
            unsafeJso<ItemStyleOption> {
                color = ZRColor("transparent")
                borderWidth = 1.0
                borderType = ZRLineTypes.dashed
            }
        data =
            arrayOf(
                MarkAreaDataItem(
                    arrayOf(
                        unsafeJso<MarkArea2DDataItemDimOption> {
                            this.name = name
                            xAxis = ScaleDataValue("min")
                            yAxis = ScaleDataValue("min")
                        },
                        unsafeJso<MarkArea2DDataItemDimOption> {
                            xAxis = ScaleDataValue("max")
                            yAxis = ScaleDataValue("max")
                        },
                    ),
                ),
            )
    }

/** The highest and the lowest person of a series. */
internal fun extremes(): MarkPointOption =
    unsafeJso {
        data =
            arrayOf(
                unsafeJso<MarkPointDataItemOption> {
                    type = MarkerStatisticTypes.max
                    name = "Max"
                },
                unsafeJso<MarkPointDataItemOption> {
                    type = MarkerStatisticTypes.min
                    name = "Min"
                },
            )
    }

/** The average line plus the vertical line at [splitHeight]. */
internal fun averageLine(averageName: String, splitHeight: Double): MarkLineOption =
    unsafeJso {
        lineStyle = unsafeJso<LineStyleOption> { type = ZRLineTypes.solid }
        data =
            arrayOf(
                MarkLineDataItem(
                    unsafeJso<MarkLine1DDataItemOption> {
                        type = MarkerStatisticTypes.average
                        name = averageName
                    },
                ),
                MarkLineDataItem(
                    unsafeJso<MarkLine1DDataItemOption> { xAxis = StringOrDouble(splitHeight) },
                ),
            )
    }

/** Whether a tooltip value is the `[height, weight]` tuple; the wrapper types it as a union. */
internal fun isTuple(value: JsAny?): Boolean = js("(value) => Array.isArray(value)")(value) as Boolean

/** The tooltip of the page: a tuple prints as `cm` + `kg`, a mark line as its single value. */
internal fun weightTooltip(): TooltipOption =
    unsafeJso {
        showDelay = 0.0
        formatter =
            TooltipFormatter { raw ->
                val params = raw.unsafeCast<CallbackDataParams>()
                val value = params.value
                val text =
                    if (isTuple(value)) {
                        params.seriesName + " :<br/>" + textAt(value, 0) + "cm " + textAt(value, 1) + "kg "
                    } else {
                        params.seriesName + " :<br/>" + params.name + " : " + valueText(value) + "kg "
                    }
                TooltipContent(text)
            }
        axisPointer =
            unsafeJso<AxisPointerOption> {
                show = BooleanOrString(true)
                type = AxisPointerTypes.cross
                lineStyle =
                    unsafeJso<LineStyleOption> {
                        type = ZRLineTypes.dashed
                        width = 1.0
                    }
            }
    }

/** The toolbar: the data-zoom switch and the three brush modes of the page. */
internal fun weightToolbox(): Array<ToolboxOption> =
    one(
        unsafeJso<ToolboxOption> {
            feature =
                unsafeJso<ToolboxFeature> {
                    dataZoom = unsafeJso<ToolboxDataZoomFeatureOption> {}
                    brush =
                        unsafeJso<ToolboxBrushFeatureOption> {
                            type =
                                arrayOf(
                                    BrushToolboxIconTypes.rect,
                                    BrushToolboxIconTypes.polygon,
                                    BrushToolboxIconTypes.clear,
                                )
                        }
                }
        },
    )

/** Style of the two value axes of the page, named after the unit [suffix]. */
internal fun AxisOption.styleAsValueAxis(suffix: String) {
    type = AxisTypes.value
    scale = true
    axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} $suffix") }
    splitLine = unsafeJso<SplitLineOption> { show = false }
}

/** The horizontal axis, in centimetres. */
internal fun weightXAxis(): XAxisOption = unsafeJso { styleAsValueAxis("cm") }

/** The vertical axis, in kilograms. */
internal fun weightYAxis(): YAxisOption = unsafeJso { styleAsValueAxis("kg") }
