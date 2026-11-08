package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.legendNames

private val barLabelRotationCategory = Category(Groups.BAR, "examples/bar/BarLabelRotation.kt")

val barLabelRotationEntry: CatalogEntry =
    barLabelRotationCategory.chart("bar-label-rotation", "柱状图标签旋转", "Bar Label Rotation") {
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
                            barGap = StringOrDouble("0")
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
        }
