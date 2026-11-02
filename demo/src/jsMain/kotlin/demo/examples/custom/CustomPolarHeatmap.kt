package demo.examples.custom

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
import demo.examples.Category
import demo.examples.matrix.dimension
import demo.examples.shared.legendNames
import demo.examples.shared.numberArray

private val customPolarHeatmapCategory = Category(Groups.CUSTOM, "examples/custom/CustomPolarHeatmap.kt")

val customPolarHeatmapEntry: CatalogEntry =
    customPolarHeatmapCategory.chart(
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
        }
