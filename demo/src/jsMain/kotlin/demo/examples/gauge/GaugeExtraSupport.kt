package demo.examples.gauge

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.shared.every

/** `{ value: <value> }`, the single data item every gauge here uses. */
internal fun gaugeValue(value: Double): GaugeDataItem =
    GaugeDataItem(unsafeJso<GaugeDataItemOption> { this.value = OptionDataValue(value) })
