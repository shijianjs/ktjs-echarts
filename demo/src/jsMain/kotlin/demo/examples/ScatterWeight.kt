package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import kotlin.js.unsafeCast
import zrender.ZRColor
import zrender.ZRLineTypes

/*
 * The height/weight distribution page, ported from `public/examples/ts/scatter-weight.ts`.
 *
 * The two tables (260 female and 247 male rows) live in `ScatterWeightData.kt`, generated from the
 * upstream source. Each series also carries its own data-range rectangle, the min/max points and an
 * average line, so a click on the brush toolbar can zoom into one of them.
 */

private val scatterWeight = Category(Groups.SCATTER, "examples/ScatterWeight.kt")

/** The series of the page, one per table. */
private fun weightSeries(
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
private fun dataRangeArea(name: String): MarkAreaOption =
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
private fun extremes(): MarkPointOption =
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
private fun averageLine(averageName: String, splitHeight: Double): MarkLineOption =
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
private fun isTuple(value: JsAny?): Boolean = js("(value) => Array.isArray(value)")(value) as Boolean

/** The tooltip of the page: a tuple prints as `cm` + `kg`, a mark line as its single value. */
private fun weightTooltip(): TooltipOption =
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
private fun weightToolbox(): Array<ToolboxOption> =
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
private fun AxisOption.styleAsValueAxis(suffix: String) {
    type = AxisTypes.value
    scale = true
    axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} $suffix") }
    splitLine = unsafeJso<SplitLineOption> { show = false }
}

/** The horizontal axis, in centimetres. */
private fun weightXAxis(): XAxisOption = unsafeJso { styleAsValueAxis("cm") }

/** The vertical axis, in kilograms. */
private fun weightYAxis(): YAxisOption = unsafeJso { styleAsValueAxis("kg") }

val scatterWeightEntries: List<CatalogEntry> =
    listOf(
        scatterWeight.chart(
            "scatter-weight",
            "男性女性身高体重分布",
            "Distribution of Height and Weight",
            "260 位女性与 247 位男性的身高体重散点，每个系列叠着自己的取值范围虚线框、" +
                "最大值/最小值标记和平均值线，右上角工具箱可切换缩放与框选。",
            "The height and weight of 260 women and 247 men; each series carries its own dashed " +
                "data-range box, its extreme points and an average line, and the toolbox on the " +
                "right switches zooming and brushing.",
        ) {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Male and female height and weight distribution"
                            subtext = "Data from: Heinz 2003"
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("3%")
                            right = StringOrDouble("7%")
                            bottom = StringOrDouble("7%")
                            containLabel = true
                        },
                    )
                tooltip = one(weightTooltip())
                toolbox = weightToolbox()
                brush = one(unsafeJso<BrushOption> {})
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("Female", "Male")
                            left = StringOrDouble("center")
                            bottom = StringOrDouble(10.0)
                        },
                    )
                xAxis = one(weightXAxis())
                yAxis = one(weightYAxis())
                series =
                    arrayOf(
                        weightSeries("Female", femaleHeightWeight, "AVG", 160.0),
                        weightSeries("Male", maleHeightWeight, "Average", 170.0),
                    )
            }
        },
    )
