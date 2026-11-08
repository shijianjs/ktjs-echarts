package demo.examples.themeriver

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.AxisPointerTypes
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.AxisPointerLabelOption
import io.github.shijianjs.ktjs.echarts.options.AxisPointerOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.LineStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SingleAxisOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineOption
import io.github.shijianjs.ktjs.echarts.options.SplitLineStyleOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.themeRiverSeries
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRLineTypes
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
                                    animation = io.github.shijianjs.ktjs.echarts.options.BooleanOrString(true)
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
