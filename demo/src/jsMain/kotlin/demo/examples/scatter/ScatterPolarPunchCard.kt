package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.AngleAxisOption
import io.github.shijianjs.ktjs.echarts.options.AnimationDelay
import io.github.shijianjs.ktjs.echarts.options.AxisLabelOption
import io.github.shijianjs.ktjs.echarts.options.AxisLineOption
import io.github.shijianjs.ktjs.echarts.options.BooleanOrString
import io.github.shijianjs.ktjs.echarts.options.BooleanOrStringArray
import io.github.shijianjs.ktjs.echarts.options.CallbackDataParams
import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.PolarOption
import io.github.shijianjs.ktjs.echarts.options.RadiusAxisOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.TooltipContent
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatter
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.scatterSeries
import io.github.shijianjs.ktjs.echarts.options.strings
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
