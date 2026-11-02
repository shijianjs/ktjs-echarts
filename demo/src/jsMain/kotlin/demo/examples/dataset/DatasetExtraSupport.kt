package demo.examples.dataset

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.JsName
import kotlin.js.unsafeCast
import demo.examples.chord.column
import demo.examples.gauge.center
import demo.examples.matrix.dimension
import demo.examples.shared.recordOf
import demo.examples.shared.row
import demo.examples.shared.textOf
import demo.examples.sunburst.radius

/** `encode: { x, y, tooltip: [0..4] }` of one scatter of the matrix. */
internal fun matrixEncode(x: String, y: String): OptionEncode =
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
internal fun matrixGrid(
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

/** One condition of the `and` list of a `filter` transform. */
internal external interface FilterCondition {
    /** Dimension the condition applies to. */
    var dimension: String

    /** Keep the rows whose value is greater than or equal to this one. */
    var gte: Double?

    /** Keep the rows whose value equals this one; upstream spells the key `=` in the option. */
    @JsName("=")
    var equals: String?
}

/**
 * The `filter` dataset of `data-transform-filter` and `line-race`: the rows of [country] from 1950 on.
 *
 * The `and` list is an array of condition objects; `transform.config` is typed as a plain record
 * upstream, so the array is handed over with a cast, exactly like `data-transform-multiple-pie`
 * passes its single condition.
 */
internal fun filteredSince1950(id: String, country: String): DatasetOption =
    unsafeJso {
        this.id = id
        fromDatasetId = "dataset_raw"
        transform =
            arrayOf(
                unsafeJso<DataTransformOption> {
                    type = "filter"
                    config =
                        recordOf(
                            "and" to
                                arrayOf<FilterCondition>(
                                    unsafeJso {
                                        dimension = "Year"
                                        gte = 1950.0
                                    },
                                    unsafeJso {
                                        dimension = "Country"
                                        equals = country
                                    },
                                ).unsafeCast<JsAny>(),
                        )
                },
            )
    }

/** One line of `data-transform-filter`, reading the derived dataset [datasetId]. */
internal fun incomeLine(datasetId: String): LineSeriesOption =
    lineSeries {
        this.datasetId = datasetId
        showSymbol = false
        encode =
            unsafeJso<OptionEncode> {
                x = OptionEncodeValue(DimensionLoose("Year"))
                y = OptionEncodeValue(DimensionLoose("Income"))
                itemName = OptionEncodeValue(DimensionLoose("Year"))
                tooltip = OptionEncodeValue(DimensionLoose("Income"))
            }
    }

/**
 * The `updateAxisPointer` payload of `dataset-link`.
 *
 * Upstream reads `event.axesInfo[0].value`, and types that event as `any` — the axis pointer component
 * publishes `axesInfo` but the npm declarations do not name it, so the value is read in JavaScript and
 * the "no axis under the pointer" case comes back as `-1`.
 */
internal fun linkedDimension(event: ECElementEvent): Double =
    js("(event) => { const info = event && event.axesInfo && event.axesInfo[0]; return info ? Number(info.value) + 1 : -1; }")(
        event,
    ) as Double

/** The shared table: one row per product, one column per year. */
internal val LINKED_PRODUCTS =
    arrayOf(
        row("product", "2012", "2013", "2014", "2015", "2016", "2017"),
        row("Milk Tea", 56.5, 82.1, 88.7, 70.1, 53.4, 85.1),
        row("Matcha Latte", 51.1, 51.4, 55.1, 53.3, 73.8, 68.7),
        row("Cheese Cocoa", 40.1, 62.2, 69.5, 36.4, 45.2, 32.5),
        row("Walnut Brownie", 25.2, 37.1, 41.2, 18, 33.9, 49.1),
    )

internal fun linkedDatasetOption(): EChartsOption =
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
internal fun linkedPieDimensionOption(dimension: Double): EChartsOption =
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
