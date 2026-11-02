package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val bumpChartJsonCategory = JsonCategory("json/examples/line/BumpChartJson.kt")

val bumpChartJsonExample: JsonExample =
    // ------------------------------------------------------------ long tail
        bumpChartJsonCategory.example("bump-chart") {
            val rankings = bumpRankings()
            title { text = "Bump Chart (Ranking)" }
            tooltip { trigger = TooltipTrigger.Item }
            grid {
                left = num(30)
                right = num(110)
                bottom = num(30)
                containLabel = true
            }
            saveAsImageToolbox()
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                splitLine { show = true }
                axisLabel {
                    margin = 30.0
                    fontSize = 16.0
                }
                categories(*BUMP_YEARS)
            }
            yAxis {
                type = AxisType.Value
                inverse = true
                interval = 1.0
                min = num(1)
                max = num(BUMP_NAMES.size)
                axisLabel {
                    margin = 30.0
                    fontSize = 16.0
                    formatter = "#{value}"
                }
            }
            series {
                BUMP_NAMES.forEachIndexed { index, food ->
                    line {
                        name = food
                        symbolSize = NumberOrStringOrNumbers.of(20)
                        smooth = BooleanOrNumber.of(true)
                        emphasis = focusSeries()
                        endLabel {
                            show = true
                            formatter = "{a}"
                            distance = 20.0
                        }
                        lineStyle { width = 4.0 }
                        data = rankings[index].map { rank -> valueOf(rank) }
                    }
                }
            }
        }
