package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import js.objects.unsafeJso as jso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import zrender.ZRColor
import zrender.ZRLineTypes

/*
 * The scatter pages of the long tail: the two label-alignment variants, the effect scatter pair and
 * the Anscombe quartet.
 *
 * `scatter-label-align-right` reads `myChart.getWidth()` inside its `labelLayout` callback, so it is
 * an `interactive` example like the upstream page; the callback is wrapped in the wrapper's
 * [LabelLayout] factory because upstream passes the function directly as `labelLayout`.
 *
 * `scatter-label-align-top` and `scatter-label-align-right` share the same life-expectancy table,
 * which is why both live in this file.
 */

private val scatterLongTail = Category(Groups.SCATTER, "examples/ScatterLongTail.kt")

/**
 * One row of the life-expectancy tables: `[人均 GDP, 期望寿命, 人口, 国家, 年份]`.
 *
 * The fourth column is a country name, so the rows are two-element tuples plus a text — the wrapper
 * types a scatter item as an array of values, and `row(...)` builds exactly that.
 */
private fun lifeRow(
    income: Double,
    life: Double,
    population: Double,
    country: String,
    year: Int,
): ScatterDataItem? =
    ScatterDataItem(row(income, life, population, country, year))

/** The 1990 rows of both label-alignment pages. */
private val LIFE_ROWS_1990 =
    arrayOf(
        lifeRow(28604.0, 77.0, 17096869.0, "Australia", 1990),
        lifeRow(31163.0, 77.4, 27662440.0, "Canada", 1990),
        lifeRow(1516.0, 68.0, 1154605773.0, "China", 1990),
        lifeRow(13670.0, 74.7, 10582082.0, "Cuba", 1990),
        lifeRow(28599.0, 75.0, 4986705.0, "Finland", 1990),
        lifeRow(29476.0, 77.1, 56943299.0, "France", 1990),
        lifeRow(31476.0, 75.4, 78958237.0, "Germany", 1990),
        lifeRow(28666.0, 78.1, 254830.0, "Iceland", 1990),
        lifeRow(1777.0, 57.7, 870601776.0, "India", 1990),
        lifeRow(29550.0, 79.1, 122249285.0, "Japan", 1990),
        lifeRow(2076.0, 67.9, 20194354.0, "North Korea", 1990),
        lifeRow(12087.0, 72.0, 42972254.0, "South Korea", 1990),
        lifeRow(24021.0, 75.4, 3397534.0, "New Zealand", 1990),
        lifeRow(43296.0, 76.8, 4240375.0, "Norway", 1990),
        lifeRow(10088.0, 70.8, 38195258.0, "Poland", 1990),
        lifeRow(19349.0, 69.6, 147568552.0, "Russia", 1990),
        lifeRow(10670.0, 67.3, 53994605.0, "Turkey", 1990),
        lifeRow(26424.0, 75.7, 57110117.0, "United Kingdom", 1990),
        lifeRow(37062.0, 75.4, 252847810.0, "United States", 1990),
    )

/** The 2015 rows of the same table; only `scatter-label-align-top` draws them upstream. */
private val LIFE_ROWS_2015 =
    arrayOf(
        lifeRow(44056.0, 81.8, 23968973.0, "Australia", 2015),
        lifeRow(43294.0, 81.7, 35939927.0, "Canada", 2015),
        lifeRow(13334.0, 76.9, 1376048943.0, "China", 2015),
        lifeRow(21291.0, 78.5, 11389562.0, "Cuba", 2015),
        lifeRow(38923.0, 80.8, 5503457.0, "Finland", 2015),
        lifeRow(37599.0, 81.9, 64395345.0, "France", 2015),
        lifeRow(44053.0, 81.1, 80688545.0, "Germany", 2015),
        lifeRow(42182.0, 82.8, 329425.0, "Iceland", 2015),
        lifeRow(5903.0, 66.8, 1311050527.0, "India", 2015),
        lifeRow(36162.0, 83.5, 126573481.0, "Japan", 2015),
        lifeRow(1390.0, 71.4, 25155317.0, "North Korea", 2015),
        lifeRow(34644.0, 80.7, 50293439.0, "South Korea", 2015),
        lifeRow(34186.0, 80.6, 4528526.0, "New Zealand", 2015),
        lifeRow(64304.0, 81.6, 5210967.0, "Norway", 2015),
        lifeRow(24787.0, 77.3, 38611794.0, "Poland", 2015),
        lifeRow(23038.0, 73.13, 143456918.0, "Russia", 2015),
        lifeRow(19360.0, 76.5, 78665830.0, "Turkey", 2015),
        lifeRow(38225.0, 81.4, 64715810.0, "United Kingdom", 2015),
        lifeRow(53354.0, 79.1, 321773631.0, "United States", 2015),
    )

/**
 * The bubble size of both pages: `Math.sqrt(data[2]) / 5e2`.
 *
 * Upstream writes `(data) => ...` where the callback receives the data item, not the parameters
 * object; the wrapper calls it with the item as the first argument for the same reason.
 */
private val lifeSymbolSize: SymbolSize =
    SymbolSize { value, _ -> DoubleOrDoubleArray(kotlin.math.sqrt(numberAt(value, 2)) / 500.0) }

/** The label of both pages: the country name, the fourth element of the row. */
private val countryLabel: LabelFormatterCallback = { params ->
    textAt(params.value, 3)
}

/** The label line shared by both pages: a short grey segment. */
private fun lifeLabelLine(): LabelLineOption =
    unsafeJso<LabelLineOption> {
        show = true
        length2 = 5.0
        lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#bbb") }
    }

/** `scatter-label-align-top`: the labels are moved to the top and overlap is resolved by `shiftX`. */
private fun labelTopOption(): EChartsOption =
    unsafeJso<EChartsOption> {
        xAxis = one(unsafeJso<XAxisOption> {})
        yAxis = one(unsafeJso<YAxisOption> { scale = true })
        series =
            arrayOf<SeriesOption>(
                scatterSeries {
                    name = "1990"
                    typedData = LIFE_ROWS_1990
                    symbolSize = lifeSymbolSize
                    emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.self }
                    labelLayout =
                        LabelLayout(
                            unsafeJso<LabelLayoutOption> {
                                y = StringOrDouble(20.0)
                                align = TextAlign.center
                                hideOverlap = true
                                moveOverlap = MoveOverlaps.shiftX
                            },
                        )
                    labelLine = lifeLabelLine()
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            formatter = LabelFormatter(countryLabel)
                            minMargin = 10.0
                            position = LabelPosition("top")
                        }
                },
            )
    }

/** `scatter-effect`: the two effect-scatter bubbles that sit on top of the plain cloud. */
private val EFFECT_BUBBLES: Array<ScatterDataItem?> =
    points(doubleArrayOf(172.7, 105.2), doubleArrayOf(153.4, 42.0))

/** The plain cloud of `scatter-effect`. */
private val EFFECT_CLOUD: Array<ScatterDataItem?> =
    doubleArrayOf(
        161.2, 51.6, 167.5, 59.0, 159.5, 49.2, 157.0, 63.0, 155.8, 53.6,
        170.0, 59.0, 159.1, 47.6, 166.0, 69.8, 176.2, 66.8, 160.2, 75.2,
        172.5, 55.2, 170.9, 54.2, 172.9, 62.5, 153.4, 42.0, 160.0, 50.0,
        147.2, 49.8, 168.2, 49.2, 175.0, 73.2, 157.0, 47.8, 167.6, 68.8,
        159.5, 50.6, 175.0, 82.5, 166.8, 57.2, 176.5, 87.8, 170.2, 72.8,
        174.0, 54.5, 173.0, 59.8, 179.9, 67.3, 170.5, 67.8, 160.0, 47.0,
        154.4, 46.2, 162.0, 55.0, 176.5, 83.0, 160.0, 54.4, 152.0, 45.8,
        162.1, 53.6, 170.0, 73.2, 160.2, 52.1, 161.3, 67.9, 166.4, 56.6,
        168.9, 62.3, 163.8, 58.5, 167.6, 54.5, 160.0, 50.2, 161.3, 60.3,
        167.6, 58.3, 165.1, 56.2, 160.0, 50.2, 170.0, 72.9, 157.5, 59.8,
        167.6, 61.0, 160.7, 69.1, 163.2, 55.9, 152.4, 46.5, 157.5, 54.3,
        168.3, 54.8, 180.3, 60.7, 165.5, 60.0, 165.0, 62.0, 164.5, 60.3,
        156.0, 52.7, 160.0, 74.3, 163.0, 62.0, 165.7, 73.1, 161.0, 80.0,
        162.0, 54.7, 166.0, 53.2, 174.0, 75.7, 172.7, 61.1, 167.6, 55.7,
        151.1, 48.7, 164.5, 52.3, 163.5, 50.0, 152.0, 59.3, 169.0, 62.5,
        164.0, 55.7, 161.2, 54.8, 155.0, 45.9, 170.0, 70.6, 176.2, 67.2,
        170.0, 69.4, 162.5, 58.2, 170.3, 64.8, 164.1, 71.6, 169.5, 52.8,
        163.2, 59.8, 154.5, 49.0, 159.8, 50.0, 173.2, 69.2, 170.0, 55.9,
        161.4, 63.4, 169.0, 58.2, 166.2, 58.6, 159.4, 45.7, 162.5, 52.2,
        159.0, 48.6, 162.8, 57.8, 159.0, 55.6, 179.8, 66.8, 162.9, 59.4,
        161.0, 53.6, 151.1, 73.2, 168.2, 53.4, 168.9, 69.0, 173.2, 58.4,
        171.8, 56.2, 178.0, 70.6, 164.3, 59.8, 163.0, 72.0, 168.5, 65.2,
        166.8, 56.6, 172.7, 105.2, 163.5, 51.8, 169.4, 63.4, 167.8, 59.0,
        159.5, 47.6, 167.6, 63.0, 161.2, 55.2, 160.0, 45.0, 163.2, 54.0,
        162.2, 50.2, 161.3, 60.2, 149.5, 44.8, 157.5, 58.8, 163.2, 56.4,
        172.7, 62.0, 155.0, 49.2, 156.5, 67.2, 164.0, 53.8, 160.9, 54.4,
        162.8, 58.0, 167.0, 59.8, 160.0, 54.8, 160.0, 43.2, 168.9, 60.5,
        158.2, 46.4, 156.0, 64.4, 160.0, 48.8, 167.1, 62.2, 158.0, 55.5,
        167.6, 57.8, 156.0, 54.6, 162.1, 59.2, 173.4, 52.7, 159.8, 53.2,
        170.5, 64.5, 159.2, 51.8, 157.5, 56.0, 161.3, 63.6, 162.6, 63.2,
        160.0, 59.5, 168.9, 56.8, 165.1, 64.1, 162.6, 50.0, 165.1, 72.3,
        166.4, 55.0, 160.0, 55.9, 152.4, 60.4, 170.2, 69.1, 162.6, 84.5,
        170.2, 55.9, 158.8, 55.5, 172.7, 69.5, 167.6, 76.4, 162.6, 61.4,
        167.6, 65.9, 156.2, 58.6, 175.2, 66.8, 172.1, 56.6, 162.6, 58.6,
        160.0, 55.9, 165.1, 59.1, 182.9, 81.8, 166.4, 70.7, 165.1, 56.8,
        177.8, 60.0, 165.1, 58.2, 175.3, 72.7, 154.9, 54.1, 158.8, 49.1,
        172.7, 75.9, 168.9, 55.0, 161.3, 57.3, 167.6, 55.0, 165.1, 65.5,
        175.3, 65.5, 157.5, 48.6, 163.8, 58.6, 167.6, 63.6, 165.1, 55.2,
        165.1, 62.7, 168.9, 56.6, 162.6, 53.9, 164.5, 63.2, 176.5, 73.6,
        168.9, 62.0, 175.3, 63.6, 159.4, 53.2, 160.0, 53.4, 170.2, 55.0,
        162.6, 70.5, 167.6, 54.5, 162.6, 54.5, 160.7, 55.9, 160.0, 59.0,
        157.5, 63.6, 162.6, 54.5, 152.4, 47.3, 170.2, 67.7, 165.1, 80.9,
        172.7, 70.5, 165.1, 60.9, 170.2, 63.6, 170.2, 54.5, 170.2, 59.1,
        161.3, 70.5, 167.6, 52.7, 167.6, 62.7, 165.1, 86.3, 162.6, 66.4,
        152.4, 67.3, 168.9, 63.0, 170.2, 73.6, 175.2, 62.3, 175.2, 57.7,
        160.0, 55.4, 165.1, 104.1, 174.0, 55.5, 170.2, 77.3, 160.0, 80.5,
        167.6, 64.5, 167.6, 72.3, 167.6, 61.4, 154.9, 58.2, 162.6, 81.8,
        175.3, 63.6, 171.4, 53.4, 157.5, 54.5, 165.1, 53.6, 160.0, 60.0,
        174.0, 73.6, 162.6, 61.4, 174.0, 55.5, 162.6, 63.6, 161.3, 60.9,
        156.2, 60.0, 149.9, 46.8, 169.5, 57.3, 160.0, 64.1, 175.3, 63.6,
        169.5, 67.3, 160.0, 75.5, 172.7, 68.2, 162.6, 61.4, 157.5, 76.8,
        176.5, 71.8, 164.4, 55.5, 160.7, 48.6, 174.0, 66.4, 163.8, 67.3,
    ).let { flat -> Array(flat.size / 2) { index -> ScatterDataItem(doubleRow(flat[index * 2], flat[index * 2 + 1])) } }

/** One group of the Anscombe quartet; every group plots eleven `[x, y]` points. */
private fun anscombeGroup(vararg pairs: DoubleArray): Array<ScatterDataItem?> = points(*pairs)

/** The four groups of `scatter-anscombe-quartet`. */
private val ANSCOMBE_GROUPS =
    arrayOf(
        anscombeGroup(
            doubleArrayOf(10.0, 8.04), doubleArrayOf(8.0, 6.95), doubleArrayOf(13.0, 7.58),
            doubleArrayOf(9.0, 8.81), doubleArrayOf(11.0, 8.33), doubleArrayOf(14.0, 9.96),
            doubleArrayOf(6.0, 7.24), doubleArrayOf(4.0, 4.26), doubleArrayOf(12.0, 10.84),
            doubleArrayOf(7.0, 4.82), doubleArrayOf(5.0, 5.68),
        ),
        anscombeGroup(
            doubleArrayOf(10.0, 9.14), doubleArrayOf(8.0, 8.14), doubleArrayOf(13.0, 8.74),
            doubleArrayOf(9.0, 8.77), doubleArrayOf(11.0, 9.26), doubleArrayOf(14.0, 8.1),
            doubleArrayOf(6.0, 6.13), doubleArrayOf(4.0, 3.1), doubleArrayOf(12.0, 9.13),
            doubleArrayOf(7.0, 7.26), doubleArrayOf(5.0, 4.74),
        ),
        anscombeGroup(
            doubleArrayOf(10.0, 7.46), doubleArrayOf(8.0, 6.77), doubleArrayOf(13.0, 12.74),
            doubleArrayOf(9.0, 7.11), doubleArrayOf(11.0, 7.81), doubleArrayOf(14.0, 8.84),
            doubleArrayOf(6.0, 6.08), doubleArrayOf(4.0, 5.39), doubleArrayOf(12.0, 8.15),
            doubleArrayOf(7.0, 6.42), doubleArrayOf(5.0, 5.73),
        ),
        anscombeGroup(
            doubleArrayOf(8.0, 6.58), doubleArrayOf(8.0, 5.76), doubleArrayOf(8.0, 7.71),
            doubleArrayOf(8.0, 8.84), doubleArrayOf(8.0, 8.47), doubleArrayOf(8.0, 7.04),
            doubleArrayOf(8.0, 5.25), doubleArrayOf(19.0, 12.5), doubleArrayOf(8.0, 5.56),
            doubleArrayOf(8.0, 7.91), doubleArrayOf(8.0, 6.89),
        ),
    )

/** The roman numerals of the four groups. */
private val ANSCOMBE_NAMES = arrayOf("I", "II", "III", "IV")

/** The grid cells of `scatter-anscombe-quartet`, in the order of [ANSCOMBE_NAMES]. */
private fun anscombeGrids(): Array<GridOption> =
    arrayOf(
        "7%" to "7%",
        null to "7%",
        "7%" to null,
        null to null,
    ).mapIndexed { index, (left, top) ->
        unsafeJso<GridOption> {
            if (left != null) {
                this.left = StringOrDouble(left)
                width = StringOrDouble("38%")
                if (top != null) height = StringOrDouble("38%")
            } else {
                right = StringOrDouble("7%")
                width = StringOrDouble("38%")
                if (top != null) height = StringOrDouble("38%")
            }
            if (top != null) {
                this.top = StringOrDouble(top)
            } else {
                bottom = StringOrDouble("7%")
                height = StringOrDouble("38%")
            }
            require(index < 4)
        }
    }.toTypedArray()

/** The shared regression line of the quartet: `y = 0.5 * x + 3`, from `(0, 3)` to `(20, 13)`. */
private fun quartetMarkLine(): MarkLineOption =
    unsafeJso<MarkLineOption> {
        animation = false
        label =
            unsafeJso<MarkLineLabelOption> {
                formatter = LabelFormatter({ "y = 0.5 * x + 3" })
                align = TextAlign.right
            }
        lineStyle = unsafeJso<LineStyleOption> { type = ZRLineTypes.solid }
        tooltip =
            unsafeJso<TooltipOption> {
                formatter =
                    TooltipFormatter(
                        fun(_: TopLevelFormatterParams): TooltipContent = TooltipContent("y = 0.5 * x + 3"),
                    )
            }
        data =
            arrayOf<MarkLineDataItem?>(
                MarkLineDataItem(
                    arrayOf<MarkLine2DDataItemDimOption>(
                        unsafeJso<MarkLine2DDataItemDimOption> {
                            coord =
                                arrayOf(
                                    ScaleDataValueOrMarkerStatisticType(ScaleDataValue(0.0)),
                                    ScaleDataValueOrMarkerStatisticType(ScaleDataValue(3.0)),
                                )
                            symbol = SymbolShape("none")
                        },
                        unsafeJso<MarkLine2DDataItemDimOption> {
                            coord =
                                arrayOf(
                                    ScaleDataValueOrMarkerStatisticType(ScaleDataValue(20.0)),
                                    ScaleDataValueOrMarkerStatisticType(ScaleDataValue(13.0)),
                                )
                            symbol = SymbolShape("none")
                        },
                    ),
                ),
            )
    }

val scatterLongTailEntries: List<CatalogEntry> =
    listOf(
        scatterLongTail.chart(
            "scatter-label-align-top",
            "散点图标签顶部对齐",
            "Align Label on the Top",
            "人均 GDP 与期望寿命的气泡散点，标签是国名，`labelLayout` 把它们统一挪到气泡上方并横向错开。",
            "Bubbles of income against life expectancy labelled with the country name; `labelLayout` " +
                "lifts every label to the top of its bubble and shifts them apart horizontally.",
        ) {
            labelTopOption()
        },
        scatterLongTail.interactive(
            "scatter-label-align-right",
            "散点图标签右侧对齐",
            "Align Label on the Right",
            "同一份数据，标签排到图表右侧的一列上：`labelLayout` 是回调，位置取自 `chart.getWidth()`。",
            "The same data with every label parked in a column on the right; `labelLayout` is a " +
                "callback that reads the chart width.",
        ) { chart ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { splitLine = unsafeJso<SplitLineOption> { show = false } })
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                splitLine = unsafeJso<SplitLineOption> { show = false }
                                scale = true
                            },
                        )
                    grid =
                        one(
                            unsafeJso<GridOption> {
                                left = StringOrDouble(40.0)
                                right = StringOrDouble(130.0)
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            scatterSeries {
                                name = "1990"
                                typedData = LIFE_ROWS_1990
                                symbolSize = lifeSymbolSize
                                emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.self }
                                labelLayout =
                                    LabelLayout(
                                        { _: LabelLayoutCallbackParams ->
                                            unsafeJso<LabelLayoutOption> {
                                                x = StringOrDouble(chart.getWidth() - 100)
                                                moveOverlap = MoveOverlaps.shiftY
                                            }
                                        } as LabelLayoutCallback,
                                    )
                                labelLine = lifeLabelLine()
                                label =
                                    unsafeJso<LabelOption> {
                                        show = true
                                        formatter = LabelFormatter(countryLabel)
                                        position = LabelPosition("right")
                                        minMargin = 2.0
                                    }
                            },
                        )
                },
            )
        },
        scatterLongTail.chart("scatter-effect", "涟漪特效散点图", "Effect Scatter Chart") {
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> { scale = true })
                yAxis = one(unsafeJso<YAxisOption> { scale = true })
                series =
                    arrayOf<SeriesOption>(
                        effectScatterSeries {
                            symbolSize = SymbolSize(20.0)
                            typedData = EFFECT_BUBBLES.unsafeCast<Array<EffectScatterDataItem?>>()
                        },
                        scatterSeries { typedData = EFFECT_CLOUD },
                    )
            }
        },
        scatterLongTail.chart(
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
        },
    )
