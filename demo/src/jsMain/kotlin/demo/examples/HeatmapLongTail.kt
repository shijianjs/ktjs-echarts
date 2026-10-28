package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.VisualMapTypes
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.GridOption
import echarts.options.HeatmapDataItem
import echarts.options.ItemStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.TooltipOption
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.dataItems
import echarts.options.heatmapSeries
import echarts.options.numbers
import echarts.options.one
import js.objects.unsafeJso
import zrender.ZRColor

/*
 * The large-data heatmaps of the official gallery.
 *
 * Upstream samples a perlin field over 201 x 101 cells, which is the point of these pages: the series
 * switches to progressive rendering and no animation, so 20K cells stay interactive. The field is
 * computed once per page view and shared by the two pages, and the noise is seeded with a fixed value
 * because upstream calls `Math.random()`, which would repaint a different canvas on every visit.
 *
 * Kept as upstream wrote it: the y axis lists the categories `0..99` while the loop samples `j` up to
 * and including `100`, so the last row has no category and never shows.
 */

private val heatmapLongTail = Category(Groups.HEATMAP, "examples/HeatmapLongTail.kt")

/** The eleven colors both pages map the field through. */
private val FIELD_COLORS =
    arrayOf(
        "#313695",
        "#4575b4",
        "#74add1",
        "#abd9e9",
        "#e0f3f8",
        "#ffffbf",
        "#fee090",
        "#fdae61",
        "#f46d43",
        "#d73027",
        "#a50026",
    )

/** Columns of the sampled field: `i` runs from `0` to `200`. */
private const val FIELD_COLUMNS = 201

/** Rows the axis names: `0..99`, while the sampling below runs one row further. */
private const val FIELD_ROWS = 100

/** Both loops of upstream sample `i <= 200` and `j <= 100`. */
private const val FIELD_LAST_COLUMN = 200
private const val FIELD_LAST_ROW = 100

/** Seed of the noise field; upstream passes `Math.random()` here. */
private const val NOISE_SEED = 0.42

/** The perlin field of both pages, sampled once and reused. */
private val gaussianField: Array<HeatmapDataItem?> by lazy {
    val noise = PerlinNoise()
    noise.seed(NOISE_SEED)
    val points = ArrayList<HeatmapDataItem>(FIELD_COLUMNS * FIELD_ROWS)
    for (column in 0..FIELD_LAST_COLUMN) {
        for (sampleRow in 0..FIELD_LAST_ROW) {
            points.add(
                HeatmapDataItem(
                    row(
                        column.toDouble(),
                        sampleRow.toDouble(),
                        noise.perlin2(column / 40.0, sampleRow / 20.0) + 0.5,
                    ),
                ),
            )
        }
    }
    dataItems(*points.toTypedArray())
}

private fun fieldColors(): VisualOptionUnit =
    unsafeJso<VisualOptionUnit> { color = StringOrStringArray(FIELD_COLORS) }

private fun fieldAxes(): Pair<XAxisOption, YAxisOption> =
    Pair(
        unsafeJso<XAxisOption> {
            type = AxisTypes.category
            data = numbers(*DoubleArray(FIELD_COLUMNS) { it.toDouble() })
        },
        unsafeJso<YAxisOption> {
            type = AxisTypes.category
            data = numbers(*DoubleArray(FIELD_ROWS) { it.toDouble() })
        },
    )

/** The series both pages share; only the color mapping differs. */
private fun fieldSeries(): Array<SeriesOption> =
    arrayOf<SeriesOption>(
        heatmapSeries {
            name = "Gaussian"
            typedData = gaussianField
            progressive = 1000.0
            animation = false
            emphasis =
                unsafeJso<EmphasisOption> {
                    itemStyle =
                        unsafeJso<ItemStyleOption> {
                            borderColor = ZRColor("#333")
                            borderWidth = 1.0
                        }
                }
        },
    )

val heatmapLongTailEntries: List<CatalogEntry> =
    listOf(
        heatmapLongTail.chart(
            "heatmap-large",
            "热力图 - 2w 数据",
            "Heatmap - 20K data",
        ) {
            val (fieldXAxis, fieldYAxis) = fieldAxes()
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis = one(fieldXAxis)
                yAxis = one(fieldYAxis)
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            min = 0.0
                            max = 1.0
                            calculable = true
                            realtime = false
                            inRange = fieldColors()
                        },
                    )
                series = fieldSeries()
            }
        },
        heatmapLongTail.chart(
            "heatmap-large-piecewise",
            "热力图 - 颜色的离散映射",
            "Heatmap - Discrete Mapping of Color",
        ) {
            val (fieldXAxis, fieldYAxis) = fieldAxes()
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                grid =
                    one(
                        unsafeJso<GridOption> {
                            right = StringOrDouble(140.0)
                            left = StringOrDouble(40.0)
                        },
                    )
                xAxis = one(fieldXAxis)
                yAxis = one(fieldYAxis)
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.piecewise
                            min = 0.0
                            max = 1.0
                            left = StringOrDouble("right")
                            top = StringOrDouble("center")
                            calculable = true
                            realtime = false
                            splitNumber = 8.0
                            inRange = fieldColors()
                        },
                    )
                series = fieldSeries()
            }
        },
    )
