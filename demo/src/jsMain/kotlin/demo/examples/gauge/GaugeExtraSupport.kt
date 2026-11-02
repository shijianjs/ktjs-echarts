package demo.examples.gauge

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.shared.every

/** `{ value: <value> }`, the single data item every gauge here uses. */
internal fun gaugeValue(value: Double): GaugeDataItem =
    GaugeDataItem(unsafeJso<GaugeDataItemOption> { this.value = OptionDataValue(value) })
