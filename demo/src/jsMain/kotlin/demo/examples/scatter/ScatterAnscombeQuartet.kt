package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center

private val scatterAnscombeQuartetCategory = Category(Groups.SCATTER, "examples/scatter/ScatterAnscombeQuartet.kt")

val scatterAnscombeQuartetEntry: CatalogEntry =
    scatterAnscombeQuartetCategory.chart(
            "scatter-anscombe-quartet",
            "安斯库姆四重奏",
            "Anscomb's quartet",
            "四组数据各自的散点形状完全不同，却共享同一条回归线 `y = 0.5 * x + 3`；" +
                "四个网格加四条 markLine 就够，不需要任何统计包。",
            "Four point clouds that look nothing alike yet share one regression line, " +
                "`y = 0.5 * x + 3`; four grids and four mark lines, no statistics package needed.",
        ) {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Anscombe's quartet"
                            left = StringOrDouble("center")
                            top = StringOrDouble(0.0)
                        },
                    )
                grid = anscombeGrids()
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            formatter =
                                TooltipFormatter(
                                    fun(_: TopLevelFormatterParams): TooltipContent = TooltipContent("Group {a}: ({c})"),
                                )
                        },
                    )
                xAxis =
                    Array(4) { index ->
                        unsafeJso<XAxisOption> {
                            gridIndex = index.toDouble()
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(20.0)
                        }
                    }
                yAxis =
                    Array(4) { index ->
                        unsafeJso<YAxisOption> {
                            gridIndex = index.toDouble()
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(15.0)
                        }
                    }
                series =
                    Array<SeriesOption>(4) { index ->
                        scatterSeries {
                            name = ANSCOMBE_NAMES[index]
                            xAxisIndex = index.toDouble()
                            yAxisIndex = index.toDouble()
                            typedData = ANSCOMBE_GROUPS[index]
                            markLine = quartetMarkLine()
                        }
                    }
            }
        }
