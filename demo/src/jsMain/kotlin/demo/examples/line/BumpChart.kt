package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.toolbox

private val bumpChartCategory = Category(Groups.LINE, "examples/line/BumpChart.kt")

val bumpChartEntry: CatalogEntry =
    bumpChartCategory.chart(
            "bump-chart",
            "凹凸图",
            "Bump Chart (Ranking)",
            "九种食物在六年里的名次曲线：名次作 y 轴并倒序，每条线末端用 endLabel 写出自己的名字。",
            "The ranking lines of nine foods over six years: the y axis is the rank, inverted, and each " +
                "line writes its own name at its end with `endLabel`.",
        ) {
            val rankings = bumpRankings()
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Bump Chart (Ranking)" })
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble(30.0)
                            right = StringOrDouble(110.0)
                            bottom = StringOrDouble(30.0)
                            containLabel = true
                        },
                    )
                toolbox = toolbox(saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            splitLine = unsafeJso<SplitLineOption> { show = true }
                            axisLabel = unsafeJso<AxisLabelOption> { margin = 30.0; fontSize = StringOrDouble(16.0) }
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings(*BUMP_YEARS)
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    margin = 30.0
                                    fontSize = StringOrDouble(16.0)
                                    formatter = LabelFormatter("#{value}")
                                }
                            inverse = true
                            interval = AxisInterval(1.0)
                            min = AxisMinMax(1.0)
                            max = AxisMinMax(BUMP_NAMES.size.toDouble())
                        },
                    )
                series =
                    Array<SeriesOption>(BUMP_NAMES.size) { index ->
                        bumpSeries(BUMP_NAMES[index], rankings[index])
                    }
            }
        }
