package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.shared.axisLabel

private val datasetEncode1Category = Category(Groups.DATASET, "examples/dataset/DatasetEncode1.kt")

val datasetEncode1Entry: CatalogEntry =
    datasetEncode1Category.interactive(
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
        }
