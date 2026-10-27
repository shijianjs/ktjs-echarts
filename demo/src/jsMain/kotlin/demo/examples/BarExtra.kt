package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor

/*
 * Bar examples whose interesting part is the label layout rather than the data.
 *
 * `bar-label-rotation` ships a control panel on the official page that re-applies the label options
 * on every change; the chart itself is static, so the port keeps the initial values and drops the
 * panel (and with it the `dataView` / `mark` toolbox buttons, which need that panel to be useful).
 */

private val barExtra = Category(Groups.BAR, "examples/BarExtra.kt")

/** The rotated labels shared by the four series of `bar-label-rotation`. */
private fun rotatedLabels(): LabelOption = unsafeJso {
    show = true
    position = LabelPosition("insideBottom")
    distance = 15.0
    align = TextAlign.left
    verticalAlign = VerticalAlign.middle
    rotate = 90.0
    formatter = LabelFormatter("{c}  {name|{a}}")
    fontSize = StringOrDouble(16.0)
    rich = recordOf("name" to unsafeJso<TextStyleOption> {}).unsafeCast<Record<String, TextStyleOption>>()
}

val barExtraEntries: List<CatalogEntry> =
    listOf(
        barExtra.chart("bar-label-rotation", "柱状图标签旋转", "Bar Label Rotation") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                legend = one(unsafeJso<LegendOption> { data = legendNames("Forest", "Steppe", "Desert", "Wetland") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            data = strings("2012", "2013", "2014", "2015", "2016")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Forest"
                            barGap = "0"
                            label = rotatedLabels()
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(320.0, 332.0, 301.0, 334.0, 390.0)
                        },
                        barSeries {
                            name = "Steppe"
                            label = rotatedLabels()
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(220.0, 182.0, 191.0, 234.0, 290.0)
                        },
                        barSeries {
                            name = "Desert"
                            label = rotatedLabels()
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(150.0, 232.0, 201.0, 154.0, 190.0)
                        },
                        barSeries {
                            name = "Wetland"
                            label = rotatedLabels()
                            emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
                            typedData = numbers(98.0, 77.0, 101.0, 99.0, 40.0)
                        },
                    )
                color =
                    arrayOf(
                        ZRColor("#5470c6"),
                        ZRColor("#91cc75"),
                        ZRColor("#fac858"),
                        ZRColor("#ee6666"),
                    )
            }
        },
    )
