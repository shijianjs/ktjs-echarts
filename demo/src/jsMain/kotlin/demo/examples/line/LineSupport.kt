package demo.examples.line

import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AreaStyleOption
import echarts.options.BooleanOrStringArray
import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.SeriesOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.strings
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
