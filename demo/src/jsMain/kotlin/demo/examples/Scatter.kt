package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AngleAxisOption
import echarts.options.AnimationDelay
import echarts.options.AxisLabelOption
import echarts.options.AxisLineOption
import echarts.options.AxisMinMax
import echarts.options.BooleanOrString
import echarts.options.BooleanOrStringArray
import echarts.options.CallbackDataParams
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.GridOption
import echarts.options.ItemStyleOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.OptionDataItem
import echarts.options.PolarOption
import echarts.options.RadiusAxisOption
import echarts.options.ScatterDataItem
import echarts.options.SeriesOption
import echarts.options.SplitLineOption
import echarts.options.SplitLineStyleOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echarts.options.TitleOption
import echarts.options.TooltipContent
import echarts.options.TooltipFormatter
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.dataItems
import echarts.options.one
import echarts.options.points
import echarts.options.scatterSeries
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import zrender.ZRLineTypes
import zrender.gradientColorStop
import zrender.radialGradient

/*
 * Scatter examples ported from `public/examples/ts/scatter-*.ts` and `bubble-gradient.ts` of
 * apache/echarts-examples.
 *
 * `scatter-jitter` reads the chart width, so it runs as an interactive example; the regression and
 * clustering originals are not ported in this pass (see `demo/AGENTS.md`).
 */

private val scatter = Category(Groups.SCATTER, "examples/Scatter.kt")

/** A `[x, y]` point row. */
private fun point(x: Double, y: Double): ScatterDataItem = ScatterDataItem(row(x, y))

/** The 22 measurements of `scatter-simple`. */
private val anscombe =
    listOf(
        doubleArrayOf(10.0, 8.04),
        doubleArrayOf(8.07, 6.95),
        doubleArrayOf(13.0, 7.58),
        doubleArrayOf(9.05, 8.81),
        doubleArrayOf(11.0, 8.33),
        doubleArrayOf(14.0, 7.66),
        doubleArrayOf(13.4, 6.81),
        doubleArrayOf(10.0, 6.33),
        doubleArrayOf(14.0, 8.96),
        doubleArrayOf(12.5, 6.82),
        doubleArrayOf(9.15, 7.2),
        doubleArrayOf(11.5, 7.2),
        doubleArrayOf(3.03, 4.23),
        doubleArrayOf(12.2, 7.83),
        doubleArrayOf(2.02, 4.47),
        doubleArrayOf(1.05, 3.33),
        doubleArrayOf(4.05, 4.96),
        doubleArrayOf(6.03, 7.24),
        doubleArrayOf(12.0, 6.26),
        doubleArrayOf(12.0, 8.84),
        doubleArrayOf(7.08, 5.82),
        doubleArrayOf(5.02, 5.68),
    )

/** The life expectancy / GDP table of `bubble-gradient`, one row per year. */
private val expectancy1990 =
    listOf(
        doubleArrayOf(28604.0, 77.0, 17096869.0, 1990.0),
        doubleArrayOf(31163.0, 77.4, 27662440.0, 1990.0),
        doubleArrayOf(1516.0, 68.0, 1154605773.0, 1990.0),
        doubleArrayOf(13670.0, 74.7, 10582082.0, 1990.0),
        doubleArrayOf(28599.0, 75.0, 4986705.0, 1990.0),
        doubleArrayOf(29476.0, 77.1, 56943299.0, 1990.0),
        doubleArrayOf(31476.0, 75.4, 78958237.0, 1990.0),
        doubleArrayOf(28666.0, 78.1, 254830.0, 1990.0),
        doubleArrayOf(1777.0, 57.7, 870601776.0, 1990.0),
        doubleArrayOf(29550.0, 79.1, 122249285.0, 1990.0),
        doubleArrayOf(2076.0, 67.9, 20194354.0, 1990.0),
        doubleArrayOf(12087.0, 72.0, 42972254.0, 1990.0),
        doubleArrayOf(24021.0, 75.4, 3397534.0, 1990.0),
        doubleArrayOf(43296.0, 76.8, 4240375.0, 1990.0),
        doubleArrayOf(10088.0, 70.8, 38195258.0, 1990.0),
        doubleArrayOf(19349.0, 69.6, 147568552.0, 1990.0),
        doubleArrayOf(10670.0, 67.3, 53994605.0, 1990.0),
        doubleArrayOf(26424.0, 75.7, 57110117.0, 1990.0),
        doubleArrayOf(37062.0, 75.4, 252847810.0, 1990.0),
    )

private val expectancy2015 =
    listOf(
        doubleArrayOf(44056.0, 81.8, 23968973.0, 2015.0),
        doubleArrayOf(43294.0, 81.7, 35939927.0, 2015.0),
        doubleArrayOf(13334.0, 76.9, 1376048943.0, 2015.0),
        doubleArrayOf(21291.0, 78.5, 11389562.0, 2015.0),
        doubleArrayOf(38923.0, 80.8, 5503457.0, 2015.0),
        doubleArrayOf(37599.0, 81.9, 64395345.0, 2015.0),
        doubleArrayOf(44053.0, 81.1, 80688545.0, 2015.0),
        doubleArrayOf(42182.0, 82.8, 329425.0, 2015.0),
        doubleArrayOf(5903.0, 66.8, 1311050527.0, 2015.0),
        doubleArrayOf(36162.0, 83.5, 126573481.0, 2015.0),
        doubleArrayOf(1390.0, 71.4, 25155317.0, 2015.0),
        doubleArrayOf(34644.0, 80.7, 50293439.0, 2015.0),
        doubleArrayOf(34186.0, 80.6, 4528526.0, 2015.0),
        doubleArrayOf(64304.0, 81.6, 5210967.0, 2015.0),
        doubleArrayOf(24787.0, 77.3, 38611794.0, 2015.0),
        doubleArrayOf(23038.0, 73.13, 143456918.0, 2015.0),
        doubleArrayOf(19360.0, 76.5, 78665830.0, 2015.0),
        doubleArrayOf(38225.0, 81.4, 64715810.0, 2015.0),
        doubleArrayOf(53354.0, 79.1, 321773631.0, 2015.0),
    )

private fun expectancyRows(table: List<DoubleArray>): Array<ScatterDataItem?> =
    dataItems(*Array(table.size) { index -> ScatterDataItem(doubleRow(*table[index])) })

/** `Math.sqrt(data[2]) / 5e2`: the bubble radius of `bubble-gradient`. */
private val bubbleSymbolSize =
    SymbolSize(js("(value) => Math.sqrt(value[2]) / 5e2") as (OptionDataItem, CallbackDataParams) -> DoubleOrDoubleArray)

private fun bubbleSeries(name: String, table: List<DoubleArray>, inner: String, outer: String): SeriesOption =
    scatterSeries {
        this.name = name
        typedData = expectancyRows(table)
        symbolSize = bubbleSymbolSize
        emphasis =
            unsafeJso<EmphasisOption> {
                focus = EmphasisFocuses.series
                label =
                    unsafeJso {
                        show = true
                        // The original reads the country name out of the data row.
                        formatter =
                            echarts.options.LabelFormatter(
                                js("(params) => params.data[3]") as echarts.options.LabelFormatterCallback,
                            )
                        position = echarts.options.LabelPosition("top")
                    }
            }
        itemStyle =
            unsafeJso<ItemStyleOption> {
                shadowBlur = 10.0
                shadowColor = ZRColor(if (name == "1990") "rgba(120, 36, 50, 0.5)" else "rgba(25, 100, 150, 0.5)")
                shadowOffsetY = 5.0
                color =
                    radialGradient(
                        0.4,
                        0.3,
                        1.0,
                        arrayOf(gradientColorStop(0.0, inner), gradientColorStop(1.0, outer)),
                    )
            }
    }

val scatterEntries: List<CatalogEntry> =
    listOf(
        scatter.chart("scatter-simple", "基础散点图", "Basic Scatter Chart") {
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(20.0)
                            typedData = dataItems(*Array(anscombe.size) { point(anscombe[it][0], anscombe[it][1]) })
                        },
                    )
            }
        },
        scatter.chart("bubble-gradient", "气泡图", "Bubble Chart") {
            unsafeJso<EChartsOption> {
                backgroundColor =
                    radialGradient(
                        0.3,
                        0.3,
                        0.8,
                        arrayOf(gradientColorStop(0.0, "#f7f8fa"), gradientColorStop(1.0, "#cdd0d5")),
                    )
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Life Expectancy and GDP by Country"
                            left = StringOrDouble("5%")
                            top = StringOrDouble("3%")
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            right = StringOrDouble("10%")
                            top = StringOrDouble("3%")
                            data = legendNames("1990", "2015")
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble("8%"); top = StringOrDouble("10%") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            splitLine = unsafeJso { lineStyle = unsafeJso<SplitLineStyleOption> { type = ZRLineTypes.dashed } }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            splitLine = unsafeJso { lineStyle = unsafeJso<SplitLineStyleOption> { type = ZRLineTypes.dashed } }
                            scale = true
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        bubbleSeries("1990", expectancy1990, "rgb(251, 118, 123)", "rgb(204, 46, 72)"),
                        bubbleSeries("2015", expectancy2015, "rgb(129, 227, 238)", "rgb(25, 183, 207)"),
                    )
            }
        },
        scatter.interactive("scatter-jitter", "带抖动的散点图", "Scatter with Jittering") { chart ->
            // The original derives the jitter amplitude from the chart width, so the option is built
            // on mount; a fixed seed keeps the cloud reproducible.
            var seed = 48151623L
            fun nextRandom(): Double {
                seed = (seed * 6364136223846793005L + 1442695040888963407L) and Long.MAX_VALUE
                return (seed % 100000L).toDouble() / 100000.0
            }
            val cloud =
                dataItems(
                    *buildList {
                        for (day in 0 until 7) {
                            for (index in 0 until 1000) {
                                val y = kotlin.math.tan(index.toDouble()) / 2.0 + 7.0
                                add(ScatterDataItem(row(day.toDouble(), y, nextRandom())))
                            }
                        }
                    }.toTypedArray(),
                )
            val gridLeft = 80.0
            val gridRight = 50.0
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "Scatter with Jittering" })
                    grid = one(unsafeJso<GridOption> { left = StringOrDouble(gridLeft); right = StringOrDouble(gridRight) })
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.category
                                jitter = (chart.getWidth() - gridLeft - gridRight) / 7.0 * 0.8
                                data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                max = AxisMinMax(10.0)
                                min = AxisMinMax(0.0)
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            scatterSeries {
                                name = "Sleeping Hours"
                                typedData = cloud
                                colorBy = "data"
                                itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.4 }
                            },
                        )
                },
            )
        },
        scatter.chart("scatter-polar-punchCard", "GitHub 打卡气泡图（极坐标）", "Punch Card of Github") {
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
        },
    )

/**
 * The punch card of [punchCardValues] as `[dayIndex, hourIndex, commits]` points, which is the shape
 * `scatter-polar-punchCard` plots on the polar coordinate system.
 */
private fun punchCardPoints(): Array<ScatterDataItem?> =
    points(
        *punchCardValues
            .flatMapIndexed { day, row ->
                row.mapIndexed { hour, value -> doubleArrayOf(day.toDouble(), hour.toDouble(), value.toDouble()) }
            }
            .toTypedArray(),
    )
