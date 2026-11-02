package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import echarts.options.*
import kotlin.js.Date
import kotlin.js.js
import demo.examples.Category

private val gaugeClockCategory = Category(Groups.GAUGE, "examples/gauge/GaugeClock.kt")

val gaugeClockEntry: CatalogEntry =
    gaugeClockCategory.interactive("gauge-clock", "时钟仪表盘", "Clock", height = "40rem") { chart, holder ->
            chart.setOption(clockOption(0.0, 0.0, 0.0, false))
            holder.setInterval({
                if (chart.isDisposed()) return@setInterval
                val now = Date()
                val second = now.getSeconds().toDouble()
                val minute = now.getMinutes().toDouble() + second / 60.0
                val hour = (now.getHours() % 12).toDouble() + minute / 60.0
                chart.setOption(clockOption(hour, minute, second, true))
            }, 1000)
        }
