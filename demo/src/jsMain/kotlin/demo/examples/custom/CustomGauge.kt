package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.Lcg

private val customGaugeCategory = Category(Groups.CUSTOM, "examples/custom/CustomGauge.kt")

val customGaugeEntry: CatalogEntry =
    customGaugeCategory.interactive("custom-gauge", "自定义仪表", "Custom Gauge") { chart, holder ->
            chart.setOption(gaugeOption(156.0))
            val random = Lcg(20160418)
            holder.setInterval(
                {
                    chart.setOption(gaugeUpdate(kotlin.math.round(random.next() * VAL_ON_RADIAN_MAX)))
                },
                3000,
            )
        }
