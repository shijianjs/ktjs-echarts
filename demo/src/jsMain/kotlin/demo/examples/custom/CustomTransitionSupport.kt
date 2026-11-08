package demo.examples.custom

import io.github.shijianjs.ktjs.echarts.options.AnimationDuration
import io.github.shijianjs.ktjs.echarts.CustomSeriesRenderItem
import io.github.shijianjs.ktjs.echarts.options.BooleanOrUniversalTransitionOption
import io.github.shijianjs.ktjs.echarts.options.CustomElementOption
import io.github.shijianjs.ktjs.echarts.options.CustomGroupOption
import io.github.shijianjs.ktjs.echarts.options.CustomPathOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataItemObject
import io.github.shijianjs.ktjs.echarts.options.OptionDataItem
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.OptionId
import io.github.shijianjs.ktjs.echarts.options.PieDataItem
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.pieSeries
import io.github.shijianjs.ktjs.echarts.options.customSeries
import js.objects.unsafeJso
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.round
import kotlin.math.sin
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRShapeProps
import io.github.shijianjs.ktjs.zrender.ZRStyleProps
import demo.examples.shared.every
import demo.examples.shared.percent
import demo.examples.shared.pieItem
import demo.examples.shared.row
import demo.examples.sunburst.radius

/** One slice of the parliament: the name and the seat count. */
internal class ParliamentSlice(val name: String, val value: Double)

/** The six slices of the example. */
internal val PARLIAMENT_SLICES =
    listOf(
        ParliamentSlice("A", 800.0),
        ParliamentSlice("B", 635.0),
        ParliamentSlice("C", 580.0),
        ParliamentSlice("D", 484.0),
        ParliamentSlice("E", 300.0),
        ParliamentSlice("F", 200.0),
    )

/** The upstream `defaultPalette`, indexed by slice. */
internal val PARLIAMENT_PALETTE =
    arrayOf(
        "#5070dd",
        "#b6d634",
        "#505372",
        "#ff994d",
        "#0ca8df",
        "#ffd10a",
        "#fb628b",
        "#785db0",
        "#3fbe95",
    )

/** Inner and outer radius of the ring, in percent of the shorter side, as upstream. */
internal const val PARLIAMENT_INNER_PERCENT = 30.0

internal const val PARLIAMENT_OUTER_PERCENT = 80.0

/** Milliseconds between the two options. */
internal const val PARLIAMENT_SWITCH_MILLIS = 2000

/** The start angle of every slice: `-PI / 2`, the twelve o'clock position. */
internal val PARLIAMENT_START_ANGLE = -PI / 2

/** The cumulative angles of the slices, one per boundary; the last is the full circle. */
internal fun parliamentAngles(slices: List<ParliamentSlice>): DoubleArray {
    val total = slices.sumOf { it.value }
    val angles = DoubleArray(slices.size + 1)
    var current = PARLIAMENT_START_ANGLE
    for (index in slices.indices) {
        angles[index] = current
        current += slices[index].value / total * PI * 2
    }
    angles[slices.size] = PARLIAMENT_START_ANGLE + PI * 2
    return angles
}

/**
 * Seats of one slice: rings from [innerRadius] to [outerRadius], each ring packed with seats of
 * [seatSize] and shifted so that the ring starts at a multiple of the seat width.
 */
internal fun parliamentLayout(
    startAngle: Double,
    endAngle: Double,
    totalAngle: Double,
    innerRadius: Double,
    outerRadius: Double,
    seatSize: Double,
): List<Pair<Double, Double>> {
    val rowCount = ceil((outerRadius - innerRadius) / seatSize).toInt()
    val points = ArrayList<Pair<Double, Double>>()
    var radius = innerRadius
    for (row in 0 until rowCount) {
        val ringSeats = round(totalAngle * radius / seatSize)
        val adjustedSize = totalAngle * radius / ringSeats
        var k = floor(startAngle * radius / adjustedSize) * adjustedSize
        val end = floor(endAngle * radius / adjustedSize) * adjustedSize - 1e-6
        while (k < end) {
            val angle = k / radius
            points += cos(angle) * radius to sin(angle) * radius
            k += adjustedSize
        }
        radius += seatSize
    }
    return points
}

/**
 * The `renderItem` of the parliament: one circle per seat, grouped per slice.
 *
 * The radius and the seat size follow the shorter side of the chart, exactly like the original
 * `api.getWidth()` / `api.getHeight()` arithmetic.
 */
internal fun parliamentRenderItem(angles: DoubleArray): CustomSeriesRenderItem =
    { params, api ->
        val slice = params.dataIndex.toInt()
        val viewSize = min(api.getWidth(), api.getHeight())
        val innerRadius = PARLIAMENT_INNER_PERCENT / 100.0 * viewSize / 2
        val outerRadius = PARLIAMENT_OUTER_PERCENT / 100.0 * viewSize / 2
        val centerX = api.getWidth() * 0.5
        val centerY = api.getHeight() * 0.5
        val seatSize = viewSize / 50
        val points =
            parliamentLayout(
                angles[slice],
                angles[slice + 1],
                PI * 2,
                innerRadius,
                outerRadius,
                seatSize + 3,
            )
        val color = PARLIAMENT_PALETTE[slice % PARLIAMENT_PALETTE.size]

        unsafeJso<CustomGroupOption> {
            type = "group"
            children =
                Array<CustomElementOption>(points.size) { index ->
                    unsafeJso<CustomPathOption> {
                        type = "circle"
                        autoBatch = true
                        shape =
                            unsafeJso<ZRShapeProps> {
                                cx = centerX + points[index].first
                                cy = centerY + points[index].second
                                r = seatSize / 2
                            }
                        style = unsafeJso<ZRStyleProps> { fill = ZRColor(color) }
                    }
                }
        }
    }

/** The `{ name, value }` items of the pie half. */
internal fun parliamentPieData(slices: List<ParliamentSlice>): Array<PieDataItem?> =
    dataItems(*Array(slices.size) { index -> pieItem(slices[index].name, slices[index].value) })

/** The pie half of the pair. */
internal fun parliamentPieOption(slices: List<ParliamentSlice>): EChartsOption =
    unsafeJso {
        series =
            one(
                pieSeries {
                    id = "distribution"
                    radius =
                        StringOrDoubleOrArray(
                            arrayOf(StringOrDouble("$PARLIAMENT_INNER_PERCENT%"), StringOrDouble("$PARLIAMENT_OUTER_PERCENT%")),
                        )
                    label = unsafeJso<LabelOption> { show = false }
                    universalTransition = BooleanOrUniversalTransitionOption(true)
                    animationDurationUpdate = AnimationDuration(1000.0)
                    typedData = parliamentPieData(slices)
                },
            )
    }

/** The parliament half of the pair. */
internal fun parliamentSeatsOption(slices: List<ParliamentSlice>): EChartsOption {
    val angles = parliamentAngles(slices)
    val renderItem = parliamentRenderItem(angles)
    return unsafeJso {
        series =
            one(
                customSeries {
                    id = "distribution"
                    // Upstream sets `coordinateSystem: undefined`; leaving the default out has the
                    // same effect and keeps the option tree free of a dead key.
                    universalTransition = BooleanOrUniversalTransitionOption(true)
                    animationDurationUpdate = AnimationDuration(1000.0)
                    this.renderItem = renderItem
                    typedData =
                        dataItems(
                            *Array(slices.size) { index ->
                                OptionDataItem(
                                    unsafeJso<OptionDataItemObject> {
                                        name = OptionId(slices[index].name)
                                        value = OptionDataValue(slices[index].value)
                                    },
                                )
                            },
                        )
                },
            )
    }
}
