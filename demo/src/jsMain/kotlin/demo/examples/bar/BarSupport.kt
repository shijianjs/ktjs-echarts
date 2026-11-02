package demo.examples.bar

import echarts.AxisPointerTypes
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AxisPointerOption
import echarts.options.BarDataItem
import echarts.options.BarDataItemOption
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.OptionDataValue
import echarts.options.SeriesOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import kotlin.math.cos
import kotlin.math.sin
import zrender.ZRColor
import demo.examples.matrix.names
import demo.examples.shared.every

internal val week = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

internal val shadowPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }

internal fun categoryAxis(vararg names: String): Array<XAxisOption> =
    one(unsafeJso<XAxisOption> { type = AxisTypes.category; data = strings(*names) })

internal val valueAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })

internal val axisTooltip =
    one(
        unsafeJso<TooltipOption> {
            trigger = TooltipTriggers.axis
            axisPointer = shadowPointer
        },
    )

/** `focus: 'series'` on every series of the stacked examples. */
internal val seriesFocus = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }

/** A bar with a single value overridden. */
internal fun colouredBar(value: Double, colour: String): BarDataItem =
    BarDataItem(
        unsafeJso<BarDataItemOption> {
            this.value = OptionDataValue(value)
            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor(colour) }
        },
    )

/** `bar-animation-delay` generates 100 categories from a sine and a cosine. */
internal fun delayedData(cosine: Boolean): DoubleArray =
    DoubleArray(100) { index ->
        val angle = index / 5.0
        val wave = if (cosine) cos(angle) else sin(angle)
        (wave * (angle - 10.0) + index / 6.0) * 5.0
    }

/** One series of `bar-y-category-stack`. */
internal fun stackedBar(name: String, vararg values: Double): SeriesOption =
    barSeries {
        this.name = name
        stack = "total"
        label = unsafeJso<LabelOption> { show = true }
        emphasis = seriesFocus
        typedData = numbers(*values)
    }

/** The `{ position: 'right' }` label shared by the negative half of `bar-negative2`. */
internal fun rightLabel(): LabelOption = unsafeJso { position = LabelPosition("right") }
