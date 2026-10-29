package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.AxisTypes
import echarts.options.*
import echartsgl.GlColor
import echartsgl.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * The `scatter3D` pages of the `gl/` directory of apache/echarts-examples.
 *
 * They live next to `Gl.kt` because all three read a data file; the bar, line and surface pages
 * build their option statically.
 *
 * `scatter3d` upstream sets `theme: dark` and paints the axes, the axis pointer and the visual map
 * labels white for it. The gallery runs on its own light/dark switch, which ECharts applies as a
 * theme, so those colours are left out here: white text on the light background would be invisible.
 */

private val glScatter = Category(Groups.GL, "examples/GlScatter.kt")

/** Column indices of `nutrients.json` used by `scatter3d`. */
private const val PROTEIN = 2
private const val SODIUM = 4
private const val FIBER = 5
private const val VITAMIN_C = 6

/** `encode: { x, y, z, tooltip: [0..4] }` of a 3D series of the dataset pages. */
private fun encode3(x: String, y: String, z: String): OptionEncode =
    unsafeJso {
        this.x = OptionEncodeValue(DimensionLoose(x))
        this.y = OptionEncodeValue(DimensionLoose(y))
        this.z = OptionEncodeValue(DimensionLoose(z))
        tooltip = OptionEncodeValue(dimensionScratch())
    }

/** `encode: { x, y, tooltip: [0..4] }` of the 2D scatters next to the 3D one. */
private fun encode2(x: String, y: String): OptionEncode =
    unsafeJso {
        this.x = OptionEncodeValue(DimensionLoose(x))
        this.y = OptionEncodeValue(DimensionLoose(y))
        tooltip = OptionEncodeValue(dimensionScratch())
    }

/** The `[0, 1, 2, 3, 4]` tooltip columns every dataset page of the family shares. */
private fun dimensionScratch(): Array<DimensionLoose> =
    Array(5) { index -> DimensionLoose(index.toDouble()) }

/** The `dataset` block of the `life-expectancy-table.json` pages. */
private fun lifeExpectancyDataset(json: JsAny): DatasetOption =
    unsafeJso {
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
    }

/**
 * One row of `nutrients.json` as a `scatter3D` item.
 *
 * The six values are the three coordinates followed by the two dimensions the `visualMap`
 * components read: the fiber content drives the colour, vitamin C the symbol size.
 */
private fun nutrientItem(row: JsAny?, index: Int): Scatter3DDataItem =
    Scatter3DDataItem(
        doubleRow(
            numberAt(row, PROTEIN),
            numberAt(row, FIBER),
            numberAt(row, SODIUM),
            numberAt(row, FIBER),
            numberAt(row, VITAMIN_C),
            index.toDouble(),
        ),
    )

/**
 * One grid of the 2x2 scatter matrix, as wide as [width] and pinned to [bottom] or [top].
 */
private fun matrixGrid(left: String, width: String, bottom: String? = null, top: String? = null): GridOption =
    unsafeJso {
        this.left = StringOrDouble(left)
        this.width = StringOrDouble(width)
        if (bottom != null) this.bottom = StringOrDouble(bottom)
        if (top != null) this.top = StringOrDouble(top)
    }

/** x axis of one panel of the scatter matrix. */
private fun panelXAxis(index: Double, name: String, category: Boolean = false): XAxisOption =
    unsafeJso {
        type = if (category) AxisTypes.category else AxisTypes.value
        gridIndex = index
        this.name = name
        if (category) boundaryGap = BooleanOrStringArray(false)
        axisLabel =
            unsafeJso<AxisLabelOption> {
                rotate = 50.0
                interval = AxisInterval(0.0)
            }
    }

/** y axis of one panel of the scatter matrix. */
private fun panelYAxis(index: Double, name: String): YAxisOption =
    unsafeJso {
        type = AxisTypes.value
        gridIndex = index
        this.name = name
    }

val glScatterEntries: List<CatalogEntry> =
    listOf(
        glScatter.interactive(
            "scatter3d",
            "三维散点图",
            "Scatter3D",
            "食品营养成分表：蛋白质、纤维、钠三轴定位，纤维含量决定颜色、维生素 C 决定点的大小，两个滑杆可调阈值。",
            "A nutrient table drawn in three dimensions: fiber drives the colour and vitamin C the " +
                "symbol size, both adjustable with the two sliders.",
        ) { chart ->
            loadJson("data/nutrients.json").then { json ->
                val rows = json.unsafeCast<Array<JsAny?>>()
                val colorMax = rows.maxOf { numberAt(it, FIBER) } / 2
                val symbolSizeMax = rows.maxOf { numberAt(it, VITAMIN_C) } / 2
                chart.setOption(
                    unsafeJso<EChartsGlOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        visualMap =
                            arrayOf(
                                unsafeJso<VisualMapOption> {
                                    top = StringOrDouble(10.0)
                                    calculable = true
                                    dimension = 3.0
                                    max = colorMax
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            color =
                                                StringOrStringArray(
                                                    arrayOf(
                                                        "#1710c0",
                                                        "#0b9df0",
                                                        "#00fea8",
                                                        "#00ff0d",
                                                        "#f5f811",
                                                        "#f09a09",
                                                        "#fe0300",
                                                    ),
                                                )
                                        }
                                },
                                unsafeJso<VisualMapOption> {
                                    bottom = StringOrDouble(10.0)
                                    calculable = true
                                    dimension = 4.0
                                    max = symbolSizeMax
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            symbolSize = DoubleOrDoubleArray(arrayOf(10.0, 40.0))
                                        }
                                },
                            )
                        xAxis3D =
                            one(
                                unsafeJso<Axis3DOption> {
                                    name = "protein"
                                    type = AxisTypes.value
                                },
                            )
                        yAxis3D =
                            one(
                                unsafeJso<Axis3DOption> {
                                    name = "fiber"
                                    type = AxisTypes.value
                                },
                            )
                        zAxis3D =
                            one(
                                unsafeJso<Axis3DOption> {
                                    name = "sodium"
                                    type = AxisTypes.value
                                },
                            )
                        grid3D = one(unsafeJso<Grid3DOption> {})
                        series =
                            one(
                                scatter3DSeries {
                                    symbolSize = SymbolSize(12.0)
                                    typedItemStyle =
                                        unsafeJso<GlItemStyleOption> {
                                            borderWidth = 1.0
                                            borderColor = GlColor(ZRColor("rgba(255,255,255,0.8)"))
                                        }
                                    typedEmphasis =
                                        unsafeJso<GlEmphasisOption> {
                                            itemStyle = unsafeJso<GlItemStyleOption> { color = GlColor(ZRColor("#fff")) }
                                        }
                                    dimensions =
                                        arrayOf(
                                            DimensionDefinitionLoose("protein"),
                                            DimensionDefinitionLoose("fiber"),
                                            DimensionDefinitionLoose("sodium"),
                                            DimensionDefinitionLoose("fiber"),
                                            DimensionDefinitionLoose("vitaminc"),
                                            DimensionDefinitionLoose("index"),
                                        )
                                    typedData = Array(rows.size) { index -> nutrientItem(rows[index], index) }
                                },
                            )
                    },
                )
                null
            }
        },
        glScatter.interactive(
            "scatter3d-scatter",
            "三维散点图和散点矩阵结合使用",
            "3D Scatter with Scatter Matrix",
            "左边一张三维散点图，右边四张二维散点图，同样是收入、预期寿命、人口三列的不同组合。",
            "A 3D scatter on the left and four 2D scatters on the right, each showing another pair of " +
                "the income, life expectancy and population columns.",
        ) { chart ->
            loadJson("data/life-expectancy-table.json").then { json ->
                val size = "57%"
                chart.setOption(
                    unsafeJso<EChartsGlOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        grid3D = one(unsafeJso<Grid3DOption> { width = StringOrDouble("50%") })
                        xAxis3D = one(unsafeJso<Axis3DOption> {})
                        yAxis3D = one(unsafeJso<Axis3DOption> {})
                        zAxis3D = one(unsafeJso<Axis3DOption> {})
                        grid =
                            arrayOf(
                                matrixGrid(left = "50%", width = "20%", bottom = size),
                                matrixGrid(left = "75%", width = "20%", bottom = size),
                                matrixGrid(left = "50%", width = "20%", top = size),
                                matrixGrid(left = "75%", width = "20%", top = size),
                            )
                        xAxis =
                            arrayOf(
                                panelXAxis(0.0, "Income"),
                                panelXAxis(1.0, "Country", category = true),
                                panelXAxis(2.0, "Income"),
                                panelXAxis(3.0, "Life Expectancy"),
                            )
                        yAxis =
                            arrayOf(
                                panelYAxis(0.0, "Life Expectancy"),
                                panelYAxis(1.0, "Income"),
                                panelYAxis(2.0, "Population"),
                                panelYAxis(3.0, "Population"),
                            )
                        dataset = one(lifeExpectancyDataset(json))
                        series =
                            arrayOf<SeriesOption>(
                                scatter3DSeries {
                                    symbolSize = SymbolSize(3.0)
                                    encode = encode3("Population", "Life Expectancy", "Income")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 0.0
                                    yAxisIndex = 0.0
                                    encode = encode2("Income", "Life Expectancy")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 1.0
                                    yAxisIndex = 1.0
                                    encode = encode2("Country", "Income")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 2.0
                                    yAxisIndex = 2.0
                                    encode = encode2("Income", "Population")
                                },
                                scatterSeries {
                                    symbolSize = SymbolSize(2.5)
                                    xAxisIndex = 3.0
                                    yAxisIndex = 3.0
                                    encode = encode2("Life Expectancy", "Population")
                                },
                            )
                    },
                )
                null
            }
        },
        glScatter.interactive(
            "scatter3D-dataset",
            "使用 dataset 为三维散点图设置数据",
            "3D Scatter with Dataset",
            "国家、预期寿命、收入三列直接当作三个坐标轴，人口与年份只出现在 tooltip 里。",
            "Country, life expectancy and income feed the three axes; population and year stay in " +
                "the tooltip.",
        ) { chart ->
            loadJson("data/life-expectancy-table.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsGlOption> {
                        grid3D = one(unsafeJso<Grid3DOption> {})
                        xAxis3D = one(unsafeJso<Axis3DOption> { type = AxisTypes.category })
                        yAxis3D = one(unsafeJso<Axis3DOption> {})
                        zAxis3D = one(unsafeJso<Axis3DOption> {})
                        dataset = one(lifeExpectancyDataset(json))
                        series =
                            one(
                                scatter3DSeries {
                                    symbolSize = SymbolSize(2.5)
                                    encode = encode3("Country", "Life Expectancy", "Income")
                                },
                            )
                    },
                )
                null
            }
        },
    )
