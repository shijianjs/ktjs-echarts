package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.gradientColorStop
import io.github.shijianjs.ktjs.zrender.linearGradient
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.scatter.point
import demo.examples.shared.areaSimpleDates
import demo.examples.shared.areaSimpleValues
import demo.examples.shared.toolbox

/** The two generated-data pages live in this file too, but they belong to other families. */
private val areaSimpleCategory = Category(Groups.LINE, "examples/line/AreaSimple.kt")

val areaSimpleEntry: CatalogEntry =
    areaSimpleCategory.chart(
            "area-simple",
            "大数据量面积图",
            "Large scale area chart",
            "两万个点的随机游走铺成渐变的面积图，只显示窗口里的十分之一，`sampling: 'lttb'` " +
                "负责抽稀；tooltip 固定在图表顶部 10% 处。",
            "Twenty thousand points of a random walk under a gradient area; the data zoom shows the " +
                "first tenth and `sampling: 'lttb'` thins the rest, with the tooltip pinned to 10% of " +
                "the height.",
        ) {
            val dates = areaSimpleDates()
            val values = areaSimpleValues()
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            position =
                                TooltipPosition(
                                    fun(
                                        point: Array<Double>,
                                        _: TopLevelFormatterParams,
                                        _: JsAny?,
                                        _: io.github.shijianjs.ktjs.zrender.RectLike?,
                                        _: TooltipSize,
                                    ): TooltipPosition =
                                        TooltipPosition(
                                            arrayOf(StringOrDouble(point[0]), StringOrDouble("10%")),
                                        ),
                                )
                        },
                    )
                title =
                    one(
                        unsafeJso<TitleOption> {
                            left = StringOrDouble("center")
                            text = "Large Area Chart"
                        },
                    )
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            feature =
                                unsafeJso<ToolboxFeature> {
                                    dataZoom =
                                        unsafeJso<ToolboxDataZoomFeatureOption> {
                                            yAxisIndex = ModelFinderIndexQuery("none")
                                        }
                                    restore = unsafeJso<ToolboxFeatureOption> {}
                                    saveAsImage = unsafeJso<SaveAsImageOption> {}
                                }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings(*dates)
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            boundaryGap =
                                BooleanOrStringArray(
                                    arrayOf(StringOrDouble(0.0), StringOrDouble("100%")),
                                )
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            start = 0.0
                            end = 10.0
                        },
                        unsafeJso<DataZoomOption> {
                            start = 0.0
                            end = 10.0
                        },
                    )
                series =
                    one(
                        lineSeries {
                            name = "Fake Data"
                            symbol = SymbolShape("none")
                            sampling = "lttb"
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("rgb(255, 70, 131)") }
                            areaStyle =
                                unsafeJso<AreaStyleOption> {
                                    color =
                                        linearGradient(
                                            0.0,
                                            0.0,
                                            0.0,
                                            1.0,
                                            arrayOf(
                                                gradientColorStop(0.0, "rgb(255, 158, 68)"),
                                                gradientColorStop(1.0, "rgb(255, 70, 131)"),
                                            ),
                                        )
                                }
                            typedData = numbers(*values)
                        },
                    )
            }
        }
