package demo.examples

import echarts.options.FunnelDataItem
import echarts.options.FunnelDataItemOption
import echarts.options.GaugeDataItem
import echarts.options.GaugeDataItemOption
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.PieDataItem
import echarts.options.PieDataItemOption
import echarts.options.RadarSeriesDataItem
import echarts.options.RadarSeriesDataItemOption
import echarts.options.DataViewOption
import echarts.options.DatasetSource
import echarts.options.LegendDataItemOption
import echarts.options.MagicTypeOption
import echarts.options.SaveAsImageOption
import echarts.options.StringOrDouble
import echarts.options.ToolboxFeature
import echarts.options.ToolboxFeatureOption
import echarts.options.ToolboxOption
import echarts.options.datasetArrayRows
import echarts.Orient
import echarts.options.one
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js

/*
 * Element builders shared by the example files.
 *
 * The wrapper types a data item name as `string | number` and a value as `string | number | Date`,
 * and maps both unions to a factory-based interface; a bare Kotlin literal therefore does not
 * compile. These helpers keep the examples readable without weakening the types.
 */

/** A row of numbers, the element type of the tuple unions that hold only numbers. */
internal fun doubleRow(vararg values: Double): Array<OptionDataValue?> =
    Array(values.size) { index -> OptionDataValue(values[index]) }

/** A row of values, the element type of the tuple unions (`heatmap`, `parallel`, `themeRiver`, ...). */
internal fun row(vararg values: Any): Array<OptionDataValue?> =
    Array(values.size) { index ->
        when (val value = values[index]) {
            is Number -> OptionDataValue(value.toDouble())
            is String -> OptionDataValue(value)
            else -> throw IllegalArgumentException("unsupported row value: $value")
        }
    }

/** Rows of numbers as a dataset source (`dataset.source` of the boxplot examples). */
internal fun numberRows(rows: List<DoubleArray>): DatasetSource =
    datasetArrayRows(Array(rows.size) { row -> Array(rows[row].size) { column -> OptionDataValue(rows[row][column]) } })

/**
 * A dictionary for the option fields typed as `Record<String, V>`.
 *
 * Kotlin has no typed object-literal syntax for an index signature, so the keys and the values are
 * handed to JavaScript as two parallel arrays and folded into a plain object there.
 */
internal fun recordOf(vararg pairs: Pair<String, JsAny>): Record<String, JsAny> =
    js(
        "(keys, values) => { const o = {}; for (let i = 0; i < keys.length; i++) { o[keys[i]] = values[i]; } return o; }",
    )(
        Array(pairs.size) { pairs[it].first },
        Array(pairs.size) { pairs[it].second },
    ) as Record<String, JsAny>

/**
 * Runs [action] every [millis] milliseconds.
 *
 * The animated examples guard the body with `chart.isDisposed()`: leaving the page disposes the
 * chart instance while the timer keeps running.
 */
internal fun every(millis: Int, action: () -> Unit) {
    js("(fn, ms) => { setInterval(fn, ms) }")(action, millis)
}

/** A pie item: `{ name, value }`. */
internal fun pieItem(name: String, value: Double): PieDataItem =
    PieDataItem(
        unsafeJso<PieDataItemOption> {
            this.name = OptionId(name)
            this.value = OptionDataValue(value)
        },
    )

/** A funnel item: `{ name, value }`. */
internal fun funnelItem(name: String, value: Double): FunnelDataItem =
    FunnelDataItem(
        unsafeJso<FunnelDataItemOption> {
            this.name = OptionId(name)
            this.value = OptionDataValue(value)
        },
    )

/** A gauge item: `{ name, value }`. */
internal fun gaugeItem(name: String, value: Double): GaugeDataItem =
    GaugeDataItem(
        unsafeJso<GaugeDataItemOption> {
            this.name = OptionId(name)
            this.value = OptionDataValue(value)
        },
    )

/** A radar item: one value per indicator of the radar coordinate system. */
internal fun radarItem(name: String, vararg values: Double): RadarSeriesDataItem =
    RadarSeriesDataItem(
        unsafeJso<RadarSeriesDataItemOption> {
            this.name = OptionId(name)
            typedValue = Array(values.size) { index -> OptionDataValue(values[index]) }
        },
    )

/**
 * Renders a data value for a label or tooltip formatter.
 *
 * The value is a `string | number | Date` union at runtime, so it is stringified in JavaScript; a
 * list value (radar, custom series) is joined the same way ECharts does it.
 */
internal fun valueText(value: Any?): String = js("(value) => value == null ? '' : String(value)")(value) as String

/**
 * Legend entries named after [names]; use for the `data` field of a legend.
 */
internal fun legendNames(vararg names: String): Array<LegendDataItemOption> =
    Array(names.size) { index -> unsafeJso<LegendDataItemOption> { name = names[index] } }

/**
 * A toolbox with the features the official examples use.
 *
 * `toolbox.feature` is an open dictionary upstream; the wrapper models the six features ECharts 6
 * actually registers, so the dead `mark` key of some original examples has no counterpart here.
 */
internal fun toolbox(
    dataView: Boolean = false,
    restore: Boolean = false,
    saveAsImage: Boolean = false,
    brush: Boolean = false,
    magicType: Boolean = false,
    dataZoom: Boolean = false,
    orient: Orient? = null,
    top: StringOrDouble? = null,
    right: StringOrDouble? = null,
): Array<ToolboxOption> =
    one(
        unsafeJso<ToolboxOption> {
            this.orient = orient
            this.top = top
            this.right = right
            feature =
                unsafeJso<ToolboxFeature> {
                    if (dataView) this.dataView = unsafeJso<DataViewOption> { readOnly = false }
                    if (restore) this.restore = unsafeJso<ToolboxFeatureOption> {}
                    if (saveAsImage) this.saveAsImage = unsafeJso<SaveAsImageOption> {}
                    if (brush) this.brush = unsafeJso<ToolboxFeatureOption> {}
                    if (magicType) this.magicType = unsafeJso<MagicTypeOption> {}
                    if (dataZoom) this.dataZoom = unsafeJso<ToolboxFeatureOption> {}
                }
        },
    )
