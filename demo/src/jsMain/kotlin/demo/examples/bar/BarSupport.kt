package demo.examples.bar

import io.github.shijianjs.ktjs.echarts.AxisPointerTypes
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.AxisPointerOption
import io.github.shijianjs.ktjs.echarts.options.BarDataItem
import io.github.shijianjs.ktjs.echarts.options.BarDataItemOption
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.barSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import kotlin.math.cos
import kotlin.math.sin
import io.github.shijianjs.ktjs.zrender.ZRColor
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
