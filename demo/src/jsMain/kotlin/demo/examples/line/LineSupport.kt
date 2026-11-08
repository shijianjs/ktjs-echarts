package demo.examples.line

import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.AreaStyleOption
import io.github.shijianjs.ktjs.echarts.options.BooleanOrStringArray
import io.github.shijianjs.ktjs.echarts.EmphasisFocuses
import io.github.shijianjs.ktjs.echarts.options.EmphasisOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.lineSeries
import io.github.shijianjs.ktjs.echarts.options.numbers
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.strings
import js.objects.unsafeJso
import demo.examples.matrix.names

internal val week = arrayOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

internal val axisTooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })

internal val seriesFocus = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }

internal fun categoryAxis(vararg names: String, boundaryGap: Boolean? = null): Array<XAxisOption> =
    one(
        unsafeJso<XAxisOption> {
            type = AxisTypes.category
            if (boundaryGap != null) this.boundaryGap = BooleanOrStringArray(boundaryGap)
            data = strings(*names)
        },
    )

internal val valueAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })

/** One series of the stacked line and area examples. */
internal fun stackedSeries(
    name: String,
    values: DoubleArray,
    area: Boolean,
    label: Boolean = false,
): SeriesOption =
    lineSeries {
        this.name = name
        stack = "Total"
        if (area) areaStyle = unsafeJso<AreaStyleOption> {}
        emphasis = seriesFocus
        if (label) this.label = unsafeJso<LabelOption> { show = true; position = LabelPosition("top") }
        typedData = numbers(*values)
    }
