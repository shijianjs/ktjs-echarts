package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast

/*
 * Dataset examples that need the life-expectancy table of the official site.
 *
 * `dataset-encode1` (the official "Encode and Matrix" page) puts four scatters on four grids and
 * encodes a different pair of columns on each of them. The table is
 * `demo/src/jsMain/resources/data/life-expectancy-table.json` (see its `SOURCE.md`).
 *
 * The `thumbnail` component of the original is not ported: it only adds a mini map of the viewport.
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
    )
