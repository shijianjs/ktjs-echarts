package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.Orients
import echarts.SeriesLayoutBys
import echarts.options.AxisInterval
import echarts.options.AxisLabelOption
import echarts.options.DataTransformOption
import echarts.options.DatasetOption
import echarts.options.DatasetSource
import echarts.options.DimensionDefinitionLoose
import echarts.options.DimensionLoose
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.LegendOption
import echarts.options.MediaQueryOption
import echarts.options.MediaUnit
import echarts.options.OptionDataValue
import echarts.options.OptionEncode
import echarts.options.OptionEncodeValue
import echarts.options.PieSeriesOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.StringOrStringArray
import echarts.options.TooltipOption
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.datasetArrayRows
import echarts.options.datasetObjectRows
import echarts.options.one
import echarts.options.pieSeries
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast

/*
 * Dataset examples ported from `public/examples/ts/dataset-*.ts` and
 * `public/examples/ts/data-transform-*.ts` of apache/echarts-examples.
 *
 * Not ported:
 *
 * - `dataset-encode1` and `data-transform-filter` read the life expectancy table from the example
 *   server;
 * - `dataset-link` needs the payload of the `updateAxisPointer` event (`axesInfo`), which the
 *   wrapper does not model yet;
 * - `data-transform-aggregate` registers the `ecSimpleTransform` plugin from a CDN script.
 */

private val dataset = Category(Groups.DATASET, "examples/Dataset.kt")

/** `legend: {}`, `tooltip: {}` — a component with every option left at its default. */
private fun emptyLegend(): Array<LegendOption> = one(unsafeJso())

/** `tooltip: {}`. */
private fun emptyTooltip(): Array<TooltipOption> = one(unsafeJso())

/**
 * Rows for `dataset.source`.
 *
 * `datasetArrayRows` follows the upstream element type and therefore takes non-nullable elements,
 * while the shared `row` helper produces `Array<OptionDataValue?>`; both describe the same
 * JavaScript array, so the rows are handed over with an explicit cast.
 */
/** The `source` of a hand-built dataset; see the cast above. */
private fun tableSource(rows: Array<Array<OptionDataValue?>>): DatasetSource =
    datasetArrayRows(rows.unsafeCast<Array<Array<OptionDataValue>>>())

/** `{ source: <rows> }`, the dataset of the examples that only provide data. */
private fun rowsDataset(rows: Array<Array<OptionDataValue?>>): DatasetOption = unsafeJso { source = tableSource(rows) }

/** A dataset built from [block], e.g. to add dimensions next to the source. */
private fun datasetOf(block: DatasetOption.() -> Unit): DatasetOption = unsafeJso(block)

/** `encode: { itemName: 'product', value: <year> }` of the pies. */
private fun pieEncode(year: String): OptionEncode =
    unsafeJso {
        itemName = OptionEncodeValue(DimensionLoose("product"))
        value = OptionEncodeValue(DimensionLoose(year))
    }

/** `encode: { x: <dimension>, y: <dimension> }`. */
private fun xyEncode(x: String, y: String): OptionEncode =
    unsafeJso {
        this.x = OptionEncodeValue(DimensionLoose(x))
        this.y = OptionEncodeValue(DimensionLoose(y))
    }

/** `{ name: value, ... }` row of `dataset-simple1`. */
private fun objectRow(vararg entries: Pair<String, Any>): Record<String, OptionDataValue> {
    val keys = Array(entries.size) { entries[it].first }
    val values =
        Array<OptionDataValue>(entries.size) { index ->
            when (val value = entries[index].second) {
                is Number -> OptionDataValue(value.toDouble())
                is String -> OptionDataValue(value)
                else -> throw IllegalArgumentException("unsupported row value: $value")
            }
        }
    return js(
        "(keys, values) => { const o = {}; for (let i = 0; i < keys.length; i++) { o[keys[i]] = values[i]; } return o; }",
    )(keys, values) as Record<String, OptionDataValue>
}

/** The `sort` / `filter` transform of `data-transform-*`. */
private fun transform(type: String, vararg config: Pair<String, JsAny>): DataTransformOption =
    unsafeJso {
        this.type = type
        this.config = recordOf(*config)
    }

val datasetEntries: List<CatalogEntry> =
    listOf(
        dataset.chart("dataset-simple0", "最简单的数据集（dataset）", "Simple Example of Dataset") {
            unsafeJso<EChartsOption> {
                legend = emptyLegend()
                tooltip = emptyTooltip()
                dataset =
                    one(
                        rowsDataset(
                            arrayOf(
                                row("product", "2015", "2016", "2017"),
                                row("Matcha Latte", 43.3, 85.8, 93.7),
                                row("Milk Tea", 83.1, 73.4, 55.1),
                                row("Cheese Cocoa", 86.4, 65.2, 82.5),
                                row("Walnut Brownie", 72.4, 53.9, 39.1),
                            ),
                        ),
                    )
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                yAxis = one(unsafeJso<YAxisOption> {})
                // Each bar series is mapped to a column of `dataset.source` by default.
                series = arrayOf<SeriesOption>(barSeries {}, barSeries {}, barSeries {})
            }
        },
        dataset.chart("dataset-simple1", "对象数组的输入格式", "Dataset in Object Array") {
            unsafeJso<EChartsOption> {
                legend = emptyLegend()
                tooltip = emptyTooltip()
                dataset =
                    one(
                        datasetOf {
                            dimensions =
                                arrayOf(
                                    DimensionDefinitionLoose("product"),
                                    DimensionDefinitionLoose("2015"),
                                    DimensionDefinitionLoose("2016"),
                                    DimensionDefinitionLoose("2017"),
                                )
                            source =
                                datasetObjectRows(
                                    arrayOf(
                                        objectRow("product" to "Matcha Latte", "2015" to 43.3, "2016" to 85.8, "2017" to 93.7),
                                        objectRow("product" to "Milk Tea", "2015" to 83.1, "2016" to 73.4, "2017" to 55.1),
                                        objectRow("product" to "Cheese Cocoa", "2015" to 86.4, "2016" to 65.2, "2017" to 82.5),
                                        objectRow("product" to "Walnut Brownie", "2015" to 72.4, "2016" to 53.9, "2017" to 39.1),
                                    ),
                                )
                        },
                    )
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                yAxis = one(unsafeJso<YAxisOption> {})
                series = arrayOf<SeriesOption>(barSeries {}, barSeries {}, barSeries {})
            }
        },
        dataset.chart("dataset-default", "默认 encode 设置", "Default arrangement") {
            unsafeJso<EChartsOption> {
                legend = emptyLegend()
                tooltip = emptyTooltip()
                dataset =
                    one(
                        rowsDataset(
                            arrayOf(
                                row("product", "2012", "2013", "2014", "2015", "2016", "2017"),
                                row("Milk Tea", 86.5, 92.1, 85.7, 83.1, 73.4, 55.1),
                                row("Matcha Latte", 41.1, 30.4, 65.1, 53.3, 83.8, 98.7),
                                row("Cheese Cocoa", 24.1, 67.2, 79.5, 86.4, 65.2, 82.5),
                                row("Walnut Brownie", 55.2, 67.1, 69.2, 72.4, 53.9, 39.1),
                            ),
                        ),
                    )
                series =
                    arrayOf<SeriesOption>(
                        // No encode: the first column after `product` is used.
                        yearPie(null, "25%", "30%"),
                        yearPie("2013", "75%", "30%"),
                        yearPie("2014", "25%", "75%"),
                        yearPie("2015", "75%", "75%"),
                    )
            }
        },
        dataset.chart("dataset-encode0", "指定数据到坐标轴的映射", "Simple Encode") {
            unsafeJso<EChartsOption> {
                dataset =
                    one(
                        rowsDataset(
                            arrayOf(
                                row("score", "amount", "product"),
                                row(89.3, 58212, "Matcha Latte"),
                                row(57.1, 78254, "Milk Tea"),
                                row(74.4, 41032, "Cheese Cocoa"),
                                row(50.1, 12755, "Cheese Brownie"),
                                row(89.7, 20145, "Matcha Cocoa"),
                                row(68.1, 79146, "Tea"),
                                row(19.6, 91852, "Orange Juice"),
                                row(10.6, 101852, "Lemon Juice"),
                                row(32.7, 20112, "Walnut Brownie"),
                            ),
                        ),
                    )
                grid = one(unsafeJso<GridOption> { containLabel = true })
                xAxis = one(unsafeJso<XAxisOption> { name = "amount" })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.category })
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            orient = Orients.horizontal
                            left = StringOrDouble("center")
                            min = 10.0
                            max = 100.0
                            text = arrayOf("High Score", "Low Score")
                            // The score column drives the color.
                            dimension = 0.0
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color = StringOrStringArray(arrayOf("#65B581", "#FFCE34", "#FD665F"))
                                }
                        },
                    )
                series = arrayOf<SeriesOption>(barSeries { encode = xyEncode("amount", "product") })
            }
        },
        dataset.chart("dataset-series-layout-by", "系列按行和按列排布", "Series Layout By Column or Row") {
            unsafeJso<EChartsOption> {
                legend = emptyLegend()
                tooltip = emptyTooltip()
                dataset =
                    one(
                        rowsDataset(
                            arrayOf(
                                row("product", "2012", "2013", "2014", "2015"),
                                row("Matcha Latte", 41.1, 30.4, 65.1, 53.3),
                                row("Milk Tea", 86.5, 92.1, 85.7, 83.1),
                                row("Cheese Cocoa", 24.1, 67.2, 79.5, 86.4),
                            ),
                        ),
                    )
                xAxis =
                    arrayOf(
                        unsafeJso<XAxisOption> { type = AxisTypes.category; gridIndex = 0.0 },
                        unsafeJso<XAxisOption> { type = AxisTypes.category; gridIndex = 1.0 },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> { gridIndex = 0.0 },
                        unsafeJso<YAxisOption> { gridIndex = 1.0 },
                    )
                grid =
                    arrayOf(
                        unsafeJso<GridOption> { bottom = StringOrDouble("55%") },
                        unsafeJso<GridOption> { top = StringOrDouble("55%") },
                    )
                this.series =
                    arrayOf<SeriesOption>(
                        // The first three series are laid out by row, in the first grid.
                        barSeries { seriesLayoutBy = SeriesLayoutBys.row },
                        barSeries { seriesLayoutBy = SeriesLayoutBys.row },
                        barSeries { seriesLayoutBy = SeriesLayoutBys.row },
                        // The others are laid out by column, in the second grid.
                        gridBar(),
                        gridBar(),
                        gridBar(),
                        gridBar(),
                    )
            }
        },
        dataset.chart("data-transform-sort-bar", "柱状图排序", "Sort Data in Bar Chart") {
            unsafeJso<EChartsOption> {
                dataset =
                    arrayOf(
                        datasetOf {
                            dimensions =
                                arrayOf(
                                    DimensionDefinitionLoose("name"),
                                    DimensionDefinitionLoose("age"),
                                    DimensionDefinitionLoose("profession"),
                                    DimensionDefinitionLoose("score"),
                                    DimensionDefinitionLoose("date"),
                                )
                            source =
                                tableSource(
                                    arrayOf(
                                        row("Hannah Krause", 41, "Engineer", 314, "2011-02-12"),
                                        row("Zhao Qian", 20, "Teacher", 351, "2011-03-01"),
                                        row("Jasmin Krause ", 52, "Musician", 287, "2011-02-14"),
                                        row("Li Lei", 37, "Teacher", 219, "2011-02-18"),
                                        row("Karle Neumann", 25, "Engineer", 253, "2011-04-02"),
                                        row("Adrian Groß", 19, "Teacher", "-", "2011-01-16"),
                                        row("Mia Neumann", 71, "Engineer", 165, "2011-03-19"),
                                        row("Böhm Fuchs", 36, "Musician", 318, "2011-02-24"),
                                        row("Han Meimei", 67, "Engineer", 366, "2011-03-12"),
                                    ),
                                )
                        },
                        datasetOf {
                            transform =
                                arrayOf(transform("sort", "dimension" to ("score" as JsAny), "order" to ("desc" as JsAny)))
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            axisLabel = unsafeJso<AxisLabelOption> { interval = AxisInterval(0.0); rotate = 30.0 }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series = arrayOf<SeriesOption>(barSeries { encode = xyEncode("name", "score"); datasetIndex = 1.0 })
            }
        },
        dataset.chart("data-transform-multiple-pie", "分割数据到数个饼图", "Partition Data to Pies") {
            unsafeJso<EChartsOption> {
                dataset =
                    arrayOf(
                        rowsDataset(
                            arrayOf(
                                row("Product", "Sales", "Price", "Year"),
                                row("Cake", 123, 32, 2011),
                                row("Cereal", 231, 14, 2011),
                                row("Tofu", 235, 5, 2011),
                                row("Dumpling", 341, 25, 2011),
                                row("Biscuit", 122, 29, 2011),
                                row("Cake", 143, 30, 2012),
                                row("Cereal", 201, 19, 2012),
                                row("Tofu", 255, 7, 2012),
                                row("Dumpling", 241, 27, 2012),
                                row("Biscuit", 102, 34, 2012),
                                row("Cake", 153, 28, 2013),
                                row("Cereal", 181, 21, 2013),
                                row("Tofu", 395, 4, 2013),
                                row("Dumpling", 281, 31, 2013),
                                row("Biscuit", 92, 39, 2013),
                                row("Cake", 223, 29, 2014),
                                row("Cereal", 211, 17, 2014),
                                row("Tofu", 345, 3, 2014),
                                row("Dumpling", 211, 35, 2014),
                                row("Biscuit", 72, 24, 2014),
                            ),
                        ),
                        datasetOf { transform = arrayOf(yearFilter(2011.0)) },
                        datasetOf { transform = arrayOf(yearFilter(2012.0)) },
                        datasetOf { transform = arrayOf(yearFilter(2013.0)) },
                    )
                series =
                    arrayOf<SeriesOption>(
                        stackedYearPie("50%", "25%", 1.0),
                        stackedYearPie("50%", "50%", 2.0),
                        stackedYearPie("50%", "75%", 3.0),
                    )
                // Only for the responsive layout: the pies sit in a row on a wide canvas.
                media =
                    arrayOf(
                        unsafeJso<MediaUnit> {
                            query = unsafeJso<MediaQueryOption> { minAspectRatio = 1.0 }
                            option = mediaCenters("25%", "50%", "75%")
                        },
                        unsafeJso<MediaUnit> { option = mediaCenters("50%", "25%", "50%") },
                    )
            }
        },
    )

/** One pie of `dataset-default`; a `null` [year] keeps the default encode. */
private fun yearPie(year: String?, centerX: String, centerY: String): PieSeriesOption =
    pieSeries {
        radius = StringOrDoubleOrArray("20%")
        center = arrayOf(StringOrDouble(centerX), StringOrDouble(centerY))
        if (year != null) encode = pieEncode(year)
    }

/** A bar of the second grid of `dataset-series-layout-by`. */
private fun gridBar(): SeriesOption = barSeries { xAxisIndex = 1.0; yAxisIndex = 1.0 }

/** `{ dimension: 'Year', value: <year> }` of `data-transform-multiple-pie`. */
private fun yearFilter(year: Double): DataTransformOption =
    unsafeJso {
        type = "filter"
        config = recordOf("dimension" to ("Year" as JsAny), "value" to (year as JsAny))
    }

/** One pie of `data-transform-multiple-pie`, reading its own dataset. */
private fun stackedYearPie(x: String, y: String, datasetIndex: Double): PieSeriesOption =
    pieSeries {
        radius = StringOrDoubleOrArray(50.0)
        center = arrayOf(StringOrDouble(x), StringOrDouble(y))
        this.datasetIndex = datasetIndex
    }

/** The `option` of one media unit of `data-transform-multiple-pie`: only the centers change. */
private fun mediaCenters(first: String, second: String, third: String): EChartsOption =
    unsafeJso {
        series =
            arrayOf<SeriesOption>(
                pieSeries { center = arrayOf(StringOrDouble(first), StringOrDouble("50%")) },
                pieSeries { center = arrayOf(StringOrDouble(second), StringOrDouble("50%")) },
                pieSeries { center = arrayOf(StringOrDouble(third), StringOrDouble("50%")) },
            )
    }
