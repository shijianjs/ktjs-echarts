package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.legendNames

private val lineGraphicCategory = Category(Groups.LINE, "examples/line/LineGraphic.kt")

val lineGraphicEntry: CatalogEntry =
    lineGraphicCategory.chart(
            "line-graphic",
            "自定义图形组件",
            "Custom Graphic Component",
            "折线之外用 graphic 画两块浮层：一块 45° 旋转的标题横幅，一个带阴影和自动换行的说明框。",
            "A line chart with two graphic overlays: a banner rotated by 45 degrees and a shadowed text " +
                "box that wraps inside its own width.",
        ) {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { data = legendNames("Altitude (km) vs Temperature (°C)") })
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            formatter = TooltipFormatter("Temperature : <br/>{b}km : {c}°C")
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("3%")
                            right = StringOrDouble("4%")
                            bottom = StringOrDouble("3%")
                            containLabel = true
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} °C") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} km") }
                            boundaryGap = BooleanOrStringArray(true)
                            data = strings("0", "10", "20", "30", "40", "50", "60", "70", "80")
                        },
                    )
                graphic = arrayOf(GraphicComponentLooseOption(graphicBanner()), GraphicComponentLooseOption(graphicExplanation()))
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "高度(km)与气温(°C)变化关系"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(15.0, -50.0, -56.5, -46.5, -22.1, -2.5, -27.7, -55.7, -76.5)
                        },
                    )
            }
        }
