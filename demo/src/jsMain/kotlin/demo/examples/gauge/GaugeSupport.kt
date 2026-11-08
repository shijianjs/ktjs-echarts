package demo.examples.gauge

import io.github.shijianjs.ktjs.echarts.options.GaugeDataItem
import io.github.shijianjs.ktjs.echarts.options.GaugeDataItemOption
import io.github.shijianjs.ktjs.echarts.options.GaugeDetailOption
import io.github.shijianjs.ktjs.echarts.options.GaugeTitleOption
import io.github.shijianjs.ktjs.echarts.options.LabelFormatterCallback
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.OptionId
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatter
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.js

/** A gauge data item, optionally with its own title and detail blocks (`gauge-ring`). */
internal fun gaugeData(
    name: String?,
    value: Double,
    title: GaugeTitleOption? = null,
    detail: GaugeDetailOption? = null,
): GaugeDataItem =
    GaugeDataItem(
        unsafeJso<GaugeDataItemOption> {
            if (name != null) this.name = OptionId(name)
            this.value = OptionDataValue(value)
            this.title = title
            this.detail = detail
        },
    )

/** `offsetCenter` of a title or detail block. */
internal fun center(vararg values: String): Array<StringOrDouble> = Array(values.size) { StringOrDouble(values[it]) }

/** The `{a} <br/>{b} : {c}%` tooltip of the basic gauges. */
internal val gaugeTooltip =
    one(unsafeJso<TooltipOption> { formatter = TooltipFormatter("{a} <br/>{b} : {c}%") })

/** `Math.round(value * 100) + ''`, the detail formatter of `gauge-grade`. */
internal val roundedPercent =
    js("(value) => Math.round(value * 100) + ''") as LabelFormatterCallback

/** The grade letters of `gauge-grade`: only the four split values have a label. */
internal val gradeLabels =
    js(
        "(value) => value === 0.875 ? 'Grade A' : value === 0.625 ? 'Grade B' : " +
            "value === 0.375 ? 'Grade C' : value === 0.125 ? 'Grade D' : ''",
    ) as LabelFormatterCallback
