package demo.examples.gauge

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.toolbox

private val gaugeCarCategory = Category(Groups.GAUGE, "examples/gauge/GaugeCar.kt")

val gaugeCarEntry: CatalogEntry =
    gaugeCarCategory.chart("gauge-car", "汽车仪表盘", "Gauge Car", height = "40rem") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#000")
                tooltip =
                    one(
                        unsafeJso<TooltipOption> { formatter = TooltipFormatter("{a} <br/>{b} : {c}%") },
                    )
                toolbox = toolbox(restore = true, saveAsImage = true)
                series =
                    arrayOf<SeriesOption>(
                        carSpeedDial(),
                        carPositionDial(),
                        carRevDial(),
                        carSpeedReadout(),
                        carTripHeader(),
                        carFuelDial(),
                        carGearDial(),
                    )
            }
        }
