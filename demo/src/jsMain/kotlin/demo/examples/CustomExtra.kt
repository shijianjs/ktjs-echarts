package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor
import zrender.ZRLineTypes
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * Custom series examples that do not fit the plain `renderItem` shape of `Custom.kt`: they read the
 * polar / calendar fields of `params.coordSys` or draw sectors instead of rectangles.
 *
 * Dropped on purpose: `custom-gauge` needs `ROOT_PATH` panel images, `custom-hexbin` fetches its
 * data with `$.getJSON`, `custom-spiral-race` and `custom-gantt-flight` animate with `setInterval`.
 */

private val customExtra = Category(Groups.CUSTOM, "examples/CustomExtra.kt")

/** Hours of the day, the angle axis of `custom-polar-heatmap`. */
private val punchCardHours =
    arrayOf("12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a", "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p")

/** Days of the week, the radius axis of `custom-polar-heatmap`. */
private val punchCardDays = arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/**
 * `rawData` of the original punch card, one row per day and one column per hour.
 *
 * The example expands it into `[dayIndex, hourIndex, value]` triples.
 */
private val punchCardValues =
    arrayOf(
        intArrayOf(5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 1, 1, 3, 4, 6, 4, 4, 3, 3, 2, 5),
        intArrayOf(7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 2, 2, 6, 9, 11, 6, 7, 8, 12, 5, 5, 7, 2),
        intArrayOf(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 1, 9, 8, 10, 6, 5, 5, 5, 7, 4, 2, 4),
        intArrayOf(7, 3, 0, 0, 0, 0, 0, 0, 1, 0, 5, 4, 7, 14, 13, 12, 9, 5, 5, 10, 6, 4, 4, 1),
        intArrayOf(1, 3, 0, 0, 0, 1, 0, 0, 0, 2, 4, 4, 2, 4, 4, 14, 12, 1, 8, 5, 3, 7, 3, 0),
        intArrayOf(2, 1, 0, 3, 0, 0, 0, 0, 2, 0, 4, 1, 5, 10, 5, 7, 11, 6, 0, 5, 3, 4, 2, 0),
        intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 1, 3, 4, 0, 0, 0, 0, 1, 2, 2, 6),
    )

/** The `[day, hour, value]` triples of `custom-polar-heatmap`. */
private fun punchCardData(): Array<OptionDataItem?> =
    dataItems(
        *punchCardValues
            .flatMapIndexed { day, row ->
                row.mapIndexed { hour, value ->
                    OptionDataItem(
                        arrayOf<OptionDataValue?>(
                            OptionDataValue(day.toDouble()),
                            OptionDataValue(hour.toDouble()),
                            OptionDataValue(value.toDouble()),
                        ),
                    )
                }
            }
            .toTypedArray(),
    )

val customExtraEntries: List<CatalogEntry> =
    listOf(
        customExtra.chart(
            "custom-polar-heatmap",
            "极坐标热力图（自定义系列）",
            "Polar Heatmap",
        ) {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { data = legendNames("Punch Card") })
                polar = one(unsafeJso<PolarOption> {})
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.continuous
                            min = 0.0
                            max = 14.0
                            top = StringOrDouble("middle")
                            dimension = 2.0
                            calculable = true
                        },
                    )
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            data = strings(*punchCardHours)
                            boundaryGap = BooleanOrStringArray(false)
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    show = true
                                    lineStyle =
                                        unsafeJso<SplitLineStyleOption> {
                                            color = ZRColor("#ddd")
                                            type = ZRLineTypes.dashed
                                        }
                                }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            type = AxisTypes.category
                            data = strings(*punchCardDays)
                            z = 100.0
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        customSeries {
                            name = "Punch Card"
                            coordinateSystem = "polar"
                            typedData = punchCardData()
                            renderItem = { params, api ->
                                val values =
                                    arrayOf<OptionDataValue?>(
                                        api.value(DimensionLoose(0.0)),
                                        api.value(DimensionLoose(1.0)),
                                    )
                                val coord = api.coord(CoordinateSystemDataCoord(values))
                                val size =
                                    numberArray(
                                        api.size(
                                            OptionDataValueOrArray(
                                                arrayOf(OptionDataValue(1.0), OptionDataValue(1.0)),
                                            ),
                                            OptionDataValueOrArray(values),
                                        ),
                                    )
                                unsafeJso<CustomPathOption> {
                                    type = "sector"
                                    shape =
                                        unsafeJso<ZRShapeProps> {
                                            cx = params.coordSys.cx
                                            cy = params.coordSys.cy
                                            r0 = coord[2] - size[0] / 2
                                            r = coord[2] + size[0] / 2
                                            startAngle = -(coord[3] + size[1] / 2)
                                            endAngle = -(coord[3] - size[1] / 2)
                                        }
                                    style =
                                        api.style(
                                            unsafeJso<ZRStyleProps> {
                                                fill = api.visual(VisualProps.color).unsafeCast<ZRColor>()
                                            },
                                        )
                                }
                            }
                        },
                    )
            }
        },
    )
