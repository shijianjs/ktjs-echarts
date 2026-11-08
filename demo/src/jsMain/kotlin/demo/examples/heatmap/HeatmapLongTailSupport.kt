package demo.examples.heatmap

import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.HeatmapDataItem
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.VisualOptionUnit
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.heatmapSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.chord.column
import demo.examples.matrix.names
import demo.examples.shared.PerlinNoise
import demo.examples.shared.row

/** The eleven colors both pages map the field through. */
internal val FIELD_COLORS =
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
internal const val FIELD_COLUMNS = 201

/** Rows the axis names: `0..99`, while the sampling below runs one row further. */
internal const val FIELD_ROWS = 100

/** Both loops of upstream sample `i <= 200` and `j <= 100`. */
internal const val FIELD_LAST_COLUMN = 200

internal const val FIELD_LAST_ROW = 100

/** Seed of the noise field; upstream passes `Math.random()` here. */
internal const val NOISE_SEED = 0.42

/** The perlin field of both pages, sampled once and reused. */
internal val gaussianField: Array<HeatmapDataItem?> by lazy {
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

internal fun fieldColors(): VisualOptionUnit =
    unsafeJso<VisualOptionUnit> { color = StringOrStringArray(FIELD_COLORS) }

internal fun fieldAxes(): Pair<XAxisOption, YAxisOption> =
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
internal fun fieldSeries(): Array<SeriesOption> =
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
