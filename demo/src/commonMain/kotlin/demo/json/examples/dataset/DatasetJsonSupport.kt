package demo.json.examples.dataset

import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.*

/** One dataset row; numbers and names keep their JSON type. */
internal fun row(vararg cells: DataValue): List<DataValue> = cells.toList()

/** A row whose first cell is a label and whose remaining cells are numbers. */
internal fun namedRow(label: String, vararg values: Number): List<DataValue> =
    listOf(textValue(label)) + values.map { valueOf(it) }

/** `dimensions: ['product', '2015', ...]`; the model keeps the entries as raw JSON. */
internal fun dimensionsOf(vararg names: String): List<JsonElement> = names.map { JsonPrimitive(it) }

/** `encode: { x: 'amount', y: 'product' }`, or `itemName` / `value` for the pies. */
internal fun encodeOf(vararg entries: Pair<String, String>): JsonObject =
    buildJsonObject { entries.forEach { (channel, dimension) -> put(channel, JsonPrimitive(dimension)) } }

/**
 * `transform: [ <transform> ]`.
 *
 * ECharts also accepts a bare object; the upstream examples write a one-element list, and so does the
 * Kotlin/JS port.
 */
internal fun transforms(vararg entries: JsonElement): JsonElement = JsonArray(entries.toList())

/** `{ type: 'sort', config: { dimension: 'score', order: 'desc' } }` of `data-transform-sort-bar`. */
internal val SORT_BY_SCORE: JsonElement =
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
internal fun yearFilter(year: Number): JsonElement =
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
internal fun objectRow(
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
internal fun SeriesBuilder.yearPie(datasetYear: String?, centerX: String, centerY: String) {
    pie {
        radius = NumberOrStringOrNumbers.of("20%")
        center = listOf(str(centerX), str(centerY))
        if (datasetYear != null) {
            encode = encodeOf("itemName" to "product", "value" to datasetYear)
        }
    }
}

/** One pie of `data-transform-multiple-pie`, reading its own transformed dataset. */
internal fun SeriesBuilder.stackedYearPie(x: String, y: String, datasetIndex: Int) {
    pie {
        radius = NumberOrStringOrNumbers.of(50)
        center = listOf(str(x), str(y))
        this.datasetIndex = datasetIndex
    }
}

/** One media branch of `data-transform-multiple-pie`: only the pie centers change. */
internal fun pieCenters(first: String, second: String, third: String): EChartsOption =
    echartsOption {
        series {
            pie { center = listOf(str(first), str("50%")) }
            pie { center = listOf(str(second), str("50%")) }
            pie { center = listOf(str(third), str("50%")) }
        }
    }
