package demo.examples.dataset

import io.github.shijianjs.ktjs.echarts.options.DataTransformOption
import io.github.shijianjs.ktjs.echarts.options.DatasetOption
import io.github.shijianjs.ktjs.echarts.options.DatasetSource
import io.github.shijianjs.ktjs.echarts.options.DimensionLoose
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.OptionEncode
import io.github.shijianjs.ktjs.echarts.options.OptionEncodeValue
import io.github.shijianjs.ktjs.echarts.options.PieSeriesOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.datasetArrayRows
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.gauge.center
import demo.examples.matrix.dimension
import demo.examples.shared.every
import demo.examples.shared.recordOf
import demo.examples.shared.row
import demo.examples.sunburst.radius

/** `legend: {}`, `tooltip: {}` — a component with every option left at its default. */
internal fun emptyLegend(): Array<LegendOption> = one(unsafeJso())

/** `tooltip: {}`. */
internal fun emptyTooltip(): Array<TooltipOption> = one(unsafeJso())

/**
 * Rows for `dataset.source`.
 *
 * `datasetArrayRows` follows the upstream element type and therefore takes non-nullable elements,
 * while the shared `row` helper produces `Array<OptionDataValue?>`; both describe the same
 * JavaScript array, so the rows are handed over with an explicit cast.
 */
/** The `source` of a hand-built dataset; see the cast above. */
internal fun tableSource(rows: Array<Array<OptionDataValue?>>): DatasetSource =
    datasetArrayRows(rows.unsafeCast<Array<Array<OptionDataValue>>>())

/** `{ source: <rows> }`, the dataset of the examples that only provide data. */
internal fun rowsDataset(rows: Array<Array<OptionDataValue?>>): DatasetOption = unsafeJso { source = tableSource(rows) }

/** A dataset built from [block], e.g. to add dimensions next to the source. */
internal fun datasetOf(block: DatasetOption.() -> Unit): DatasetOption = unsafeJso(block)

/** `encode: { itemName: 'product', value: <year> }` of the pies. */
internal fun pieEncode(year: String): OptionEncode =
    unsafeJso {
        itemName = OptionEncodeValue(DimensionLoose("product"))
        value = OptionEncodeValue(DimensionLoose(year))
    }

/** `encode: { x: <dimension>, y: <dimension> }`. */
internal fun xyEncode(x: String, y: String): OptionEncode =
    unsafeJso {
        this.x = OptionEncodeValue(DimensionLoose(x))
        this.y = OptionEncodeValue(DimensionLoose(y))
    }

/** `{ name: value, ... }` row of `dataset-simple1`. */
internal fun objectRow(vararg entries: Pair<String, Any>): Record<String, OptionDataValue> {
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
internal fun transform(type: String, vararg config: Pair<String, JsAny>): DataTransformOption =
    unsafeJso {
        this.type = type
        this.config = recordOf(*config)
    }

/** One pie of `dataset-default`; a `null` [year] keeps the default encode. */
internal fun yearPie(year: String?, centerX: String, centerY: String): PieSeriesOption =
    pieSeries {
        radius = StringOrDoubleOrArray("20%")
        center = arrayOf(StringOrDouble(centerX), StringOrDouble(centerY))
        if (year != null) encode = pieEncode(year)
    }

/** A bar of the second grid of `dataset-series-layout-by`. */
internal fun gridBar(): SeriesOption = barSeries { xAxisIndex = 1.0; yAxisIndex = 1.0 }

/** `{ dimension: 'Year', value: <year> }` of `data-transform-multiple-pie`. */
internal fun yearFilter(year: Double): DataTransformOption =
    unsafeJso {
        type = "filter"
        config = recordOf("dimension" to ("Year" as JsAny), "value" to (year as JsAny))
    }

/** One pie of `data-transform-multiple-pie`, reading its own dataset. */
internal fun stackedYearPie(x: String, y: String, datasetIndex: Double): PieSeriesOption =
    pieSeries {
        radius = StringOrDoubleOrArray(50.0)
        center = arrayOf(StringOrDouble(x), StringOrDouble(y))
        this.datasetIndex = datasetIndex
    }

/** The `option` of one media unit of `data-transform-multiple-pie`: only the centers change. */
internal fun mediaCenters(first: String, second: String, third: String): EChartsOption =
    unsafeJso {
        series =
            arrayOf<SeriesOption>(
                pieSeries { center = arrayOf(StringOrDouble(first), StringOrDouble("50%")) },
                pieSeries { center = arrayOf(StringOrDouble(second), StringOrDouble("50%")) },
                pieSeries { center = arrayOf(StringOrDouble(third), StringOrDouble("50%")) },
            )
    }
