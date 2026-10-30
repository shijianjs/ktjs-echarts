package demo.json.examples

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*

/*
 * Dataset and data-transform examples carried over from `dataset-*.ts` and `data-transform-*.ts` of
 * apache/echarts-examples, mirroring the Kotlin/JS port in `examples/Dataset.kt`.
 *
 * Only the seven static pages are ported. The rest of the family stays Kotlin/JS only: `dataset-encode1`
 * and `data-transform-filter` read the life expectancy table from the example server, `dataset-link`
 * reacts to the `updateAxisPointer` event, and `data-transform-aggregate` registers the
 * `ecSimpleTransform` plugin from a CDN script.
 *
 * No page of this family uses a callback-valued option, so nothing had to be dropped for the JSON
 * model. `legend` / `tooltip` without settings are written as empty components, the same object a
 * `legend: {}` / `tooltip: {}` literal produces. The `sort` / `filter` pipeline entries are raw JSON
 * because `DatasetOption.transform` is deliberately untyped: the built-in transforms configure
 * themselves with per-transform keys that ECharts does not type either. The two `media` branches of
 * `data-transform-multiple-pie` are kept, since the model has a `media` component.
 */

private val tables = JsonCategory("json/examples/DatasetJson.kt")

/** One dataset row; numbers and names keep their JSON type. */
private fun row(vararg cells: DataValue): List<DataValue> = cells.toList()

/** A row whose first cell is a label and whose remaining cells are numbers. */
private fun namedRow(label: String, vararg values: Number): List<DataValue> =
    listOf(textValue(label)) + values.map { valueOf(it) }

/** `dimensions: ['product', '2015', ...]`; the model keeps the entries as raw JSON. */
private fun dimensionsOf(vararg names: String): List<JsonElement> = names.map { JsonPrimitive(it) }

/** `encode: { x: 'amount', y: 'product' }`, or `itemName` / `value` for the pies. */
private fun encodeOf(vararg entries: Pair<String, String>): JsonObject =
    buildJsonObject { entries.forEach { (channel, dimension) -> put(channel, JsonPrimitive(dimension)) } }

/**
 * `transform: [ <transform> ]`.
 *
 * ECharts also accepts a bare object; the upstream examples write a one-element list, and so does the
 * Kotlin/JS port.
 */
private fun transforms(vararg entries: JsonElement): JsonElement = JsonArray(entries.toList())

/** `{ type: 'sort', config: { dimension: 'score', order: 'desc' } }` of `data-transform-sort-bar`. */
private val SORT_BY_SCORE: JsonElement =
    buildJsonObject {
        put("type", JsonPrimitive("sort"))
        put(
            "config",
            buildJsonObject {
                put("dimension", JsonPrimitive("score"))
                put("order", JsonPrimitive("desc"))
            },
        )
    }

/** `{ type: 'filter', config: { dimension: 'Year', value: <year> } }` of the pie page. */
private fun yearFilter(year: Number): JsonElement =
    buildJsonObject {
        put("type", JsonPrimitive("filter"))
        put(
            "config",
            buildJsonObject {
                put("dimension", JsonPrimitive("Year"))
                put("value", JsonPrimitive(year))
            },
        )
    }

/** One `{ product, 2015, 2016, 2017 }` row of `dataset-simple1`; all four columns are named. */
private fun objectRow(
    product: String,
    y2015: Number,
    y2016: Number,
    y2017: Number,
): Map<String, DataValue> =
    linkedMapOf(
        "product" to textValue(product),
        "2015" to valueOf(y2015),
        "2016" to valueOf(y2016),
        "2017" to valueOf(y2017),
    )

/** One pie of `dataset-default`; a `null` [datasetYear] keeps the default encode. */
private fun SeriesBuilder.yearPie(datasetYear: String?, centerX: String, centerY: String) {
    pie {
        radius = NumberOrStringOrNumbers.of("20%")
        center = listOf(str(centerX), str(centerY))
        if (datasetYear != null) {
            encode = encodeOf("itemName" to "product", "value" to datasetYear)
        }
    }
}

/** One pie of `data-transform-multiple-pie`, reading its own transformed dataset. */
private fun SeriesBuilder.stackedYearPie(x: String, y: String, datasetIndex: Int) {
    pie {
        radius = NumberOrStringOrNumbers.of(50)
        center = listOf(str(x), str(y))
        this.datasetIndex = datasetIndex
    }
}

/** One media branch of `data-transform-multiple-pie`: only the pie centers change. */
private fun pieCenters(first: String, second: String, third: String): EChartsOption =
    echartsOption {
        series {
            pie { center = listOf(str(first), str("50%")) }
            pie { center = listOf(str(second), str("50%")) }
            pie { center = listOf(str(third), str("50%")) }
        }
    }

public val datasetJsonExamples: List<JsonExample> =
    listOf(
        tables.example("dataset-simple0") {
            legend { }
            tooltip { }
            dataset {
                rows(
                    row(textValue("product"), textValue("2015"), textValue("2016"), textValue("2017")),
                    namedRow("Matcha Latte", 43.3, 85.8, 93.7),
                    namedRow("Milk Tea", 83.1, 73.4, 55.1),
                    namedRow("Cheese Cocoa", 86.4, 65.2, 82.5),
                    namedRow("Walnut Brownie", 72.4, 53.9, 39.1),
                )
            }
            xAxis { type = AxisType.Category }
            yAxis { }
            // Each bar series is mapped to a column of `dataset.source` by default.
            series {
                bar { }
                bar { }
                bar { }
            }
        },
        tables.example("dataset-simple1") {
            legend { }
            tooltip { }
            dataset {
                dimensions = dimensionsOf("product", "2015", "2016", "2017")
                source =
                    DatasetSource.ObjectRows(
                        listOf(
                            objectRow("Matcha Latte", 43.3, 85.8, 93.7),
                            objectRow("Milk Tea", 83.1, 73.4, 55.1),
                            objectRow("Cheese Cocoa", 86.4, 65.2, 82.5),
                            objectRow("Walnut Brownie", 72.4, 53.9, 39.1),
                        ),
                    )
            }
            xAxis { type = AxisType.Category }
            yAxis { }
            series {
                bar { }
                bar { }
                bar { }
            }
        },
        tables.example("dataset-default") {
            legend { }
            tooltip { }
            dataset {
                rows(
                    row(
                        textValue("product"),
                        textValue("2012"),
                        textValue("2013"),
                        textValue("2014"),
                        textValue("2015"),
                        textValue("2016"),
                        textValue("2017"),
                    ),
                    namedRow("Milk Tea", 86.5, 92.1, 85.7, 83.1, 73.4, 55.1),
                    namedRow("Matcha Latte", 41.1, 30.4, 65.1, 53.3, 83.8, 98.7),
                    namedRow("Cheese Cocoa", 24.1, 67.2, 79.5, 86.4, 65.2, 82.5),
                    namedRow("Walnut Brownie", 55.2, 67.1, 69.2, 72.4, 53.9, 39.1),
                )
            }
            series {
                // No encode: the first column after `product` is used.
                yearPie(null, "25%", "30%")
                yearPie("2013", "75%", "30%")
                yearPie("2014", "25%", "75%")
                yearPie("2015", "75%", "75%")
            }
        },
        tables.example("dataset-encode0") {
            dataset {
                rows(
                    row(textValue("score"), textValue("amount"), textValue("product")),
                    row(valueOf(89.3), valueOf(58212), textValue("Matcha Latte")),
                    row(valueOf(57.1), valueOf(78254), textValue("Milk Tea")),
                    row(valueOf(74.4), valueOf(41032), textValue("Cheese Cocoa")),
                    row(valueOf(50.1), valueOf(12755), textValue("Cheese Brownie")),
                    row(valueOf(89.7), valueOf(20145), textValue("Matcha Cocoa")),
                    row(valueOf(68.1), valueOf(79146), textValue("Tea")),
                    row(valueOf(19.6), valueOf(91852), textValue("Orange Juice")),
                    row(valueOf(10.6), valueOf(101852), textValue("Lemon Juice")),
                    row(valueOf(32.7), valueOf(20112), textValue("Walnut Brownie")),
                )
            }
            grid { containLabel = true }
            xAxis { name = "amount" }
            yAxis { type = AxisType.Category }
            visualMap {
                orient = Orient.Horizontal
                left = str("center")
                min = 10.0
                max = 100.0
                text = listOf("High Score", "Low Score")
                // The score column drives the color.
                dimension = num(0)
                inRange { color = colors("#65B581", "#FFCE34", "#FD665F") }
            }
            series {
                bar { encode = encodeOf("x" to "amount", "y" to "product") }
            }
        },
        tables.example("dataset-series-layout-by") {
            legend { }
            tooltip { }
            dataset {
                rows(
                    row(
                        textValue("product"),
                        textValue("2012"),
                        textValue("2013"),
                        textValue("2014"),
                        textValue("2015"),
                    ),
                    namedRow("Matcha Latte", 41.1, 30.4, 65.1, 53.3),
                    namedRow("Milk Tea", 86.5, 92.1, 85.7, 83.1),
                    namedRow("Cheese Cocoa", 24.1, 67.2, 79.5, 86.4),
                )
            }
            xAxis {
                type = AxisType.Category
                gridIndex = 0
            }
            xAxis {
                type = AxisType.Category
                gridIndex = 1
            }
            yAxis { gridIndex = 0 }
            yAxis { gridIndex = 1 }
            grid { bottom = str("55%") }
            grid { top = str("55%") }
            series {
                // The first three series are laid out by row, in the first grid.
                bar { seriesLayoutBy = "row" }
                bar { seriesLayoutBy = "row" }
                bar { seriesLayoutBy = "row" }
                // The others are laid out by column, in the second grid.
                bar {
                    xAxisIndex = 1
                    yAxisIndex = 1
                }
                bar {
                    xAxisIndex = 1
                    yAxisIndex = 1
                }
                bar {
                    xAxisIndex = 1
                    yAxisIndex = 1
                }
                bar {
                    xAxisIndex = 1
                    yAxisIndex = 1
                }
            }
        },
        tables.example("data-transform-sort-bar") {
            dataset {
                dimensions = dimensionsOf("name", "age", "profession", "score", "date")
                rows(
                    // The trailing space of `Jasmin Krause ` is in the upstream table.
                    row(
                        textValue("Hannah Krause"),
                        valueOf(41),
                        textValue("Engineer"),
                        valueOf(314),
                        textValue("2011-02-12"),
                    ),
                    row(
                        textValue("Zhao Qian"),
                        valueOf(20),
                        textValue("Teacher"),
                        valueOf(351),
                        textValue("2011-03-01"),
                    ),
                    row(
                        textValue("Jasmin Krause "),
                        valueOf(52),
                        textValue("Musician"),
                        valueOf(287),
                        textValue("2011-02-14"),
                    ),
                    row(
                        textValue("Li Lei"),
                        valueOf(37),
                        textValue("Teacher"),
                        valueOf(219),
                        textValue("2011-02-18"),
                    ),
                    row(
                        textValue("Karle Neumann"),
                        valueOf(25),
                        textValue("Engineer"),
                        valueOf(253),
                        textValue("2011-04-02"),
                    ),
                    // `-` is the score of the row the sort transform pushes to the end.
                    row(
                        textValue("Adrian Groß"),
                        valueOf(19),
                        textValue("Teacher"),
                        textValue("-"),
                        textValue("2011-01-16"),
                    ),
                    row(
                        textValue("Mia Neumann"),
                        valueOf(71),
                        textValue("Engineer"),
                        valueOf(165),
                        textValue("2011-03-19"),
                    ),
                    row(
                        textValue("Böhm Fuchs"),
                        valueOf(36),
                        textValue("Musician"),
                        valueOf(318),
                        textValue("2011-02-24"),
                    ),
                    row(
                        textValue("Han Meimei"),
                        valueOf(67),
                        textValue("Engineer"),
                        valueOf(366),
                        textValue("2011-03-12"),
                    ),
                )
            }
            dataset { transform = transforms(SORT_BY_SCORE) }
            xAxis {
                type = AxisType.Category
                axisLabel {
                    interval = num(0)
                    rotate = 30.0
                }
            }
            yAxis { }
            series {
                bar {
                    encode = encodeOf("x" to "name", "y" to "score")
                    datasetIndex = 1
                }
            }
        },
        tables.example("data-transform-multiple-pie") {
            dataset {
                rows(
                    row(
                        textValue("Product"),
                        textValue("Sales"),
                        textValue("Price"),
                        textValue("Year"),
                    ),
                    namedRow("Cake", 123, 32, 2011),
                    namedRow("Cereal", 231, 14, 2011),
                    namedRow("Tofu", 235, 5, 2011),
                    namedRow("Dumpling", 341, 25, 2011),
                    namedRow("Biscuit", 122, 29, 2011),
                    namedRow("Cake", 143, 30, 2012),
                    namedRow("Cereal", 201, 19, 2012),
                    namedRow("Tofu", 255, 7, 2012),
                    namedRow("Dumpling", 241, 27, 2012),
                    namedRow("Biscuit", 102, 34, 2012),
                    namedRow("Cake", 153, 28, 2013),
                    namedRow("Cereal", 181, 21, 2013),
                    namedRow("Tofu", 395, 4, 2013),
                    namedRow("Dumpling", 281, 31, 2013),
                    namedRow("Biscuit", 92, 39, 2013),
                    namedRow("Cake", 223, 29, 2014),
                    namedRow("Cereal", 211, 17, 2014),
                    namedRow("Tofu", 345, 3, 2014),
                    namedRow("Dumpling", 211, 35, 2014),
                    namedRow("Biscuit", 72, 24, 2014),
                )
            }
            dataset { transform = transforms(yearFilter(2011)) }
            dataset { transform = transforms(yearFilter(2012)) }
            dataset { transform = transforms(yearFilter(2013)) }
            series {
                stackedYearPie("50%", "25%", 1)
                stackedYearPie("50%", "50%", 2)
                stackedYearPie("50%", "75%", 3)
            }
            // Only for the responsive layout: the pies sit in a row on a wide canvas.
            media {
                query = buildJsonObject { put("minAspectRatio", JsonPrimitive(1)) }
                option = pieCenters("25%", "50%", "75%")
            }
            media { option = pieCenters("50%", "25%", "50%") }
        },
    )
