package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val areaStackGradientCategory = Category(Groups.LINE, "examples/line/AreaStackGradient.kt")

val areaStackGradientEntry: CatalogEntry =
    areaStackGradientCategory.chart("area-stack-gradient", "渐变堆叠面积图", "Gradient Stacked Area Chart") {
            unsafeJso<EChartsOption> {
                color =
                    arrayOf(
                        ZRColor("#80FFA5"),
                        ZRColor("#00DDFF"),
                        ZRColor("#37A2FF"),
                        ZRColor("#FF0087"),
                        ZRColor("#FFBF00"),
                    )
                title = one(unsafeJso<TitleOption> { text = "Gradient Stacked Area Chart" })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    type = AxisPointerTypes.cross
                                    label = unsafeJso<AxisPointerLabelOption> { backgroundColor = ZRColor("#6a7985") }
                                }
                        },
                    )
                legend = one(unsafeJso<LegendOption> { data = legendNames(*STACK_BANDS.map { it.name }.toTypedArray()) })
                toolbox = toolbox(saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series = STACK_BANDS.map { stackBand(it) }.toTypedArray()
            }
        }
