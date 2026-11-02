package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.AngleAxisOption
import echarts.options.AnimationDelay
import echarts.options.AxisLabelOption
import echarts.options.AxisLineOption
import echarts.options.BooleanOrString
import echarts.options.BooleanOrStringArray
import echarts.options.CallbackDataParams
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.PolarOption
import echarts.options.RadiusAxisOption
import echarts.options.SplitLineOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.TitleOption
import echarts.options.TooltipContent
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.scatterSeries
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category
import demo.examples.custom.punchCardDays
import demo.examples.custom.punchCardHours
import demo.examples.heatmap.punchCard
import demo.examples.shared.axisLabel
import demo.examples.shared.legendNames
import demo.examples.shared.numberAt
import demo.examples.shared.textOf

private val scatterPolarPunchCardCategory = Category(Groups.SCATTER, "examples/scatter/ScatterPolarPunchCard.kt")

val scatterPolarPunchCardEntry: CatalogEntry =
    scatterPolarPunchCardCategory.chart("scatter-polar-punchCard", "GitHub 打卡气泡图（极坐标）", "Punch Card of Github") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Punch Card of Github" })
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("Punch Card")
                            left = StringOrDouble("right")
                        },
                    )
                polar = one(unsafeJso<PolarOption> {})
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            formatter =
                                TooltipFormatter { params ->
                                    val value = params.unsafeCast<CallbackDataParams>().value
                                    TooltipContent(
                                        "${textOf(numberAt(value, 2))} commits in " +
                                            punchCardHours[numberAt(value, 1).toInt()] +
                                            " of " + punchCardDays[numberAt(value, 0).toInt()],
                                    )
                                }
                        },
                    )
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            data = strings(*punchCardHours)
                            boundaryGap = BooleanOrStringArray(false)
                            splitLine = unsafeJso<SplitLineOption> { show = true }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            type = AxisTypes.category
                            data = strings(*punchCardDays)
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { rotate = 45.0 }
                        },
                    )
                series =
                    one(
                        scatterSeries {
                            name = "Punch Card"
                            coordinateSystem = "polar"
                            symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) * 2.0) }
                            animationDelay = AnimationDelay { index, _ -> index * 5.0 }
                            typedData = punchCardPoints()
                        },
                    )
            }
        }
