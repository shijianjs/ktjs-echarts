package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast

/*
 * Dataset examples beyond the first page of the family.
 *
 * `dataset-encode1` (the official "Encode and Matrix" page) puts four scatters on four grids and
 * encodes a different pair of columns on each of them. The table is
 * `demo/src/jsMain/resources/data/life-expectancy-table.json` (see its `SOURCE.md`).
 *
 * The `thumbnail` component of the original is not ported: it only adds a mini map of the viewport.
 *
 * `dataset-link` keeps its table inline, the way upstream does, and drives the pie from the
 * `updateAxisPointer` event instead of the upstream `setTimeout` wrapper — the chart is already mounted
 * when the option is set here, so the extra tick has nothing to wait for.
 */

private val datasetExtra = Category(Groups.DATASET, "examples/DatasetExtra.kt")

/** `encode: { x, y, tooltip: [0..4] }` of one scatter of the matrix. */
private fun matrixEncode(x: String, y: String): OptionEncode =
    unsafeJso {
        this.x = OptionEncodeValue(DimensionLoose(x))
        this.y = OptionEncodeValue(DimensionLoose(y))
        tooltip =
            OptionEncodeValue(
                arrayOf<DimensionLoose>(
                    DimensionLoose(0.0),
                    DimensionLoose(1.0),
                    DimensionLoose(2.0),
                    DimensionLoose(3.0),
                    DimensionLoose(4.0),
                ),
            )
    }

/** One grid of the 2x2 matrix, inset by [size] on the given corners. */
private fun matrixGrid(
    left: String? = null,
    right: String? = null,
    top: String? = null,
    bottom: String? = null,
): GridOption = unsafeJso {
    if (left != null) this.left = StringOrDouble(left)
    if (right != null) this.right = StringOrDouble(right)
    if (top != null) this.top = StringOrDouble(top)
    if (bottom != null) this.bottom = StringOrDouble(bottom)
}

val datasetExtraEntries: List<CatalogEntry> =
    listOf(
        datasetExtra.interactive(
            "dataset-encode1",
            "指定数据到坐标轴的映射",
            "Encode and Matrix",
        ) { chart ->
            loadJson("data/life-expectancy-table.json").then { json ->
                val size = "57%"
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        legend = one(unsafeJso<LegendOption> {})
                        tooltip = one(unsafeJso<TooltipOption> {})
                        grid =
                            arrayOf(
                                matrixGrid(right = size, bottom = size),
                                matrixGrid(left = size, bottom = size),
                                matrixGrid(right = size, top = size),
                                matrixGrid(left = size, top = size),
                            )
                        xAxis =
                            arrayOf(
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.value
                                    gridIndex = 0.0
                                    name = "Income"
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            rotate = 50.0
                                            interval = AxisInterval(0.0)
                                        }
                                },
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.category
                                    gridIndex = 1.0
                                    name = "Country"
                                    boundaryGap = BooleanOrStringArray(false)
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            rotate = 50.0
                                            interval = AxisInterval(0.0)
                                        }
                                },
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.value
                                    gridIndex = 2.0
                                    name = "Income"
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            rotate = 50.0
                                            interval = AxisInterval(0.0)
                                        }
                                },
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.value
                                    gridIndex = 3.0
                                    name = "Life Expectancy"
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            rotate = 50.0
                                            interval = AxisInterval(0.0)
                                        }
                                },
                            )
                        yAxis =
                            arrayOf(
                                unsafeJso<YAxisOption> {
                                    type = AxisTypes.value
                                    gridIndex = 0.0
                                    name = "Life Expectancy"
                                },
                                unsafeJso<YAxisOption> {
                                    type = AxisTypes.value
                                    gridIndex = 1.0
                                    name = "Income"
                                },
                                unsafeJso<YAxisOption> {
                                    type = AxisTypes.value
                                    gridIndex = 2.0
                                    name = "Population"
                                },
                                unsafeJso<YAxisOption> {
                                    type = AxisTypes.value
                                    gridIndex = 3.0
                                    name = "Population"
                                },
                            )
                        dataset =
                            one(
                                unsafeJso<DatasetOption> {
                                    dimensions =
                                        arrayOf(
                                            DimensionDefinitionLoose("Income"),
                                            DimensionDefinitionLoose("Life Expectancy"),
                                            DimensionDefinitionLoose("Population"),
                                            DimensionDefinitionLoose("Country"),
                                            DimensionDefinitionLoose(
                                                unsafeJso<DimensionDefinition> {
                                                    name = "Year"
                                                    type = "ordinal"
                                                },
                                            ),
                                        )
                                    source = json.unsafeCast<DatasetSource>()
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 0.0
                                    yAxisIndex = 0.0
                                    encode = matrixEncode("Income", "Life Expectancy")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 1.0
                                    yAxisIndex = 1.0
                                    encode = matrixEncode("Country", "Income")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 2.0
                                    yAxisIndex = 2.0
                                    encode = matrixEncode("Income", "Population")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 3.0
                                    yAxisIndex = 3.0
                                    encode = matrixEncode("Life Expectancy", "Population")
                                },
                            )
                    },
                )
                null
            }
        },
        datasetExtra.interactive(
            "dataset-link",
            "联动和共享数据集",
            "Share Dataset",
            summaryZh = "四条折线与一张饼图共用同一个 dataset；axisPointer 扫到哪一年，饼图就换成那一年的维度。",
            summaryEn = "Four lines and one pie share a single dataset; the axis pointer swaps the encoded dimension of the pie.",
        ) { chart ->
            chart.setOption(linkedDatasetOption())
            chart.on("updateAxisPointer") { event ->
                val dimension = linkedDimension(event)
                if (dimension >= 0) {
                    chart.setOption(linkedPieDimensionOption(dimension))
                }
            }
        },
    )

/**
 * The `updateAxisPointer` payload of `dataset-link`.
 *
 * Upstream reads `event.axesInfo[0].value`, and types that event as `any` — the axis pointer component
 * publishes `axesInfo` but the npm declarations do not name it, so the value is read in JavaScript and
 * the "no axis under the pointer" case comes back as `-1`.
 */
private fun linkedDimension(event: ECElementEvent): Double =
    js("(event) => { const info = event && event.axesInfo && event.axesInfo[0]; return info ? Number(info.value) + 1 : -1; }")(
        event,
    ) as Double

/** The shared table: one row per product, one column per year. */
private val LINKED_PRODUCTS =
    arrayOf(
        row("product", "2012", "2013", "2014", "2015", "2016", "2017"),
        row("Milk Tea", 56.5, 82.1, 88.7, 70.1, 53.4, 85.1),
        row("Matcha Latte", 51.1, 51.4, 55.1, 53.3, 73.8, 68.7),
        row("Cheese Cocoa", 40.1, 62.2, 69.5, 36.4, 45.2, 32.5),
        row("Walnut Brownie", 25.2, 37.1, 41.2, 18, 33.9, 49.1),
    )

private fun linkedDatasetOption(): EChartsOption =
    unsafeJso<EChartsOption> {
        legend = one(unsafeJso<LegendOption> {})
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.axis
                    showContent = false
                },
            )
        dataset =
            one(
                unsafeJso<DatasetOption> {
                    source = datasetArrayRows(LINKED_PRODUCTS.unsafeCast<Array<Array<OptionDataValue>>>())
                },
            )
        xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
        yAxis = one(unsafeJso<YAxisOption> { gridIndex = 0.0 })
        grid = one(unsafeJso<GridOption> { top = StringOrDouble("55%") })
        series =
            arrayOf<SeriesOption>(
                *Array(4) {
                    lineSeries {
                        smooth = BooleanOrDouble(true)
                        seriesLayoutBy = SeriesLayoutBys.row
                        emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                    }
                },
                pieSeries {
                    id = "pie"
                    radius = StringOrDoubleOrArray("30%")
                    center = arrayOf(StringOrDouble("50%"), StringOrDouble("25%"))
                    emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.self }
                    label = unsafeJso<LabelOption> { formatter = LabelFormatter("{b}: {@2012} ({d}%)") }
                    encode =
                        unsafeJso<OptionEncode> {
                            itemName = OptionEncodeValue(DimensionLoose("product"))
                            value = OptionEncodeValue(DimensionLoose("2012"))
                            tooltip = OptionEncodeValue(DimensionLoose("2012"))
                        }
                },
            )
    }

/**
 * The merge option that points the pie at the year under the axis pointer.
 *
 * Upstream writes the `series` field as a single object; the wrapper models it as the array form, which
 * ECharts merges by `id` exactly the same way.
 */
private fun linkedPieDimensionOption(dimension: Double): EChartsOption =
    unsafeJso<EChartsOption> {
        series =
            one<SeriesOption>(
                unsafeJso<PieSeriesOption> {
                    id = "pie"
                    label =
                        unsafeJso<LabelOption> {
                            formatter = LabelFormatter("{b}: {@[${textOf(dimension)}]} ({d}%)")
                        }
                    encode =
                        unsafeJso<OptionEncode> {
                            value = OptionEncodeValue(DimensionLoose(dimension))
                            tooltip = OptionEncodeValue(DimensionLoose(dimension))
                        }
                },
            )
    }
