package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.options.AnimationDuration
import echarts.CustomSeriesRenderItem
import echarts.options.BooleanOrUniversalTransitionOption
import echarts.options.CustomElementOption
import echarts.options.CustomGroupOption
import echarts.options.CustomPathOption
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.OptionDataItemObject
import echarts.options.OptionDataItem
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.PieDataItem
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.dataItems
import echarts.options.one
import echarts.options.pieSeries
import echarts.options.customSeries
import js.objects.unsafeJso
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.round
import kotlin.math.sin
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * `pie-parliament-transition` ported from `public/examples/ts/pie-parliament-transition.ts`.
 *
 * Two options share the series id `distribution`: a pie and a custom series that lays the same
 * slices out as seats of a parliament. `universalTransition` morphs one into the other, and a timer
 * alternates between them every two seconds; the timer belongs to the page, so the chart holder
 * drops it when the visitor leaves.
 */

private val customTransition = Category(Groups.CUSTOM, "examples/CustomTransition.kt")

/** One slice of the parliament: the name and the seat count. */
private class ParliamentSlice(val name: String, val value: Double)

/** The six slices of the example. */
private val PARLIAMENT_SLICES =
    listOf(
        ParliamentSlice("A", 800.0),
        ParliamentSlice("B", 635.0),
        ParliamentSlice("C", 580.0),
        ParliamentSlice("D", 484.0),
        ParliamentSlice("E", 300.0),
        ParliamentSlice("F", 200.0),
    )

/** The upstream `defaultPalette`, indexed by slice. */
private val PARLIAMENT_PALETTE =
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
private const val PARLIAMENT_INNER_PERCENT = 30.0
private const val PARLIAMENT_OUTER_PERCENT = 80.0

/** Milliseconds between the two options. */
private const val PARLIAMENT_SWITCH_MILLIS = 2000

/** The start angle of every slice: `-PI / 2`, the twelve o'clock position. */
private val PARLIAMENT_START_ANGLE = -PI / 2

/** The cumulative angles of the slices, one per boundary; the last is the full circle. */
private fun parliamentAngles(slices: List<ParliamentSlice>): DoubleArray {
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
private fun parliamentLayout(
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
private fun parliamentRenderItem(angles: DoubleArray): CustomSeriesRenderItem =
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
private fun parliamentPieData(slices: List<ParliamentSlice>): Array<PieDataItem?> =
    dataItems(*Array(slices.size) { index -> pieItem(slices[index].name, slices[index].value) })

/** The pie half of the pair. */
private fun parliamentPieOption(slices: List<ParliamentSlice>): EChartsOption =
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
private fun parliamentSeatsOption(slices: List<ParliamentSlice>): EChartsOption {
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

val customTransitionEntries: List<CatalogEntry> =
    listOf(
        customTransition.interactive(
            "pie-parliament-transition",
            "自定义议会图与饼图过渡动画",
            "Transition of Parliament and Pie Chart",
            "同一个 `id` 的饼图与自定义系列每两秒互相切换，`universalTransition` 把扇形拆成议会的" +
                "席位圆点；定时器由 ChartHolder 托管，离开页面即清理。",
            "A pie and a custom series with the same `id` alternate every two seconds; " +
                "`universalTransition` splits the sectors into parliament seats, and the page's " +
                "ChartHolder owns the timer so it stops when the visitor leaves.",
        ) { chart, holder ->
            val pie = parliamentPieOption(PARLIAMENT_SLICES)
            val parliament = parliamentSeatsOption(PARLIAMENT_SLICES)
            var current = pie
            chart.setOption(current)
            holder.setInterval({
                current = if (current === pie) parliament else pie
                chart.setOption(current)
            }, PARLIAMENT_SWITCH_MILLIS)
        },
    )
