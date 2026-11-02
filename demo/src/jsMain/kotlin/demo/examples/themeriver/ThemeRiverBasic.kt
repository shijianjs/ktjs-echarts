package demo.examples.themeriver

import demo.CatalogEntry
import demo.Groups
import echarts.AxisPointerTypes
import echarts.AxisTypes
import echarts.TooltipTriggers
import echarts.options.AxisPointerLabelOption
import echarts.options.AxisPointerOption
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.SingleAxisOption
import echarts.options.SplitLineOption
import echarts.options.SplitLineStyleOption
import echarts.options.StringOrDouble
import echarts.options.TooltipOption
import echarts.options.one
import echarts.options.themeRiverSeries
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRLineTypes
import demo.examples.Category
import demo.examples.shared.legendNames

private val themeRiverBasicCategory = Category(Groups.THEME_RIVER, "examples/themeriver/ThemeRiverBasic.kt")

val themeRiverBasicEntry: CatalogEntry =
    themeRiverBasicCategory.chart("themeRiver-basic", "主题河流图", "ThemeRiver") {
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    type = AxisPointerTypes.`line`
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("rgba(0,0,0,0.2)")
                                            width = 1.0
                                            type = ZRLineTypes.solid
                                        }
                                }
                        },
                    )
                legend = one(unsafeJso<LegendOption> { data = legendNames("DQ", "TY", "SS", "QG", "SY", "DD"); top = StringOrDouble(15.0) })
                singleAxis =
                    one(
                        unsafeJso<SingleAxisOption> {
                            top = StringOrDouble(50.0)
                            bottom = StringOrDouble(50.0)
                            type = AxisTypes.`time`
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    animation = echarts.options.BooleanOrString(true)
                                    label = unsafeJso<AxisPointerLabelOption> { show = true }
                                }
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    show = true
                                    lineStyle = unsafeJso<SplitLineStyleOption> { type = ZRLineTypes.dashed; opacity = 0.2 }
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        themeRiverSeries {
                            emphasis = riverEmphasis()
                            typedData = basicRiverData()
                        },
                    )
            }
        }
