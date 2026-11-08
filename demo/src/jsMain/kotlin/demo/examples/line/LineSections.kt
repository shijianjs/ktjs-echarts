package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.matrix.dimension
import demo.examples.shared.axisLabel
import demo.examples.shared.toolbox

private val lineSectionsCategory = Category(Groups.LINE, "examples/line/LineSections.kt")

val lineSectionsEntry: CatalogEntry =
    lineSectionsCategory.chart("line-sections", "一天用电量分布", "Distribution of Electricity") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Distribution of Electricity"
                            subtext = "Fake Data"
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                        },
                    )
                toolbox = toolbox(saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data =
                                strings(
                                    "00:00", "01:15", "02:30", "03:45", "05:00", "06:15", "07:30", "08:45",
                                    "10:00", "11:15", "12:30", "13:45", "15:00", "16:15", "17:30", "18:45",
                                    "20:00", "21:15", "22:30", "23:45",
                                )
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value} W") }
                            axisPointer = unsafeJso<AxisPointerOption> { snap = true }
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            dimension = 0.0
                            pieces =
                                arrayOf(
                                    sectionPiece(null, 6.0, "green"),
                                    sectionPiece(6.0, 8.0, "red"),
                                    sectionPiece(8.0, 14.0, "green"),
                                    sectionPiece(14.0, 17.0, "red"),
                                    sectionPiece(17.0, null, "green"),
                                )
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "Electricity"
                            smooth = BooleanOrDouble(true)
                            typedData =
                                numbers(
                                    300.0, 280.0, 250.0, 260.0, 270.0, 300.0, 550.0, 500.0, 400.0, 390.0,
                                    380.0, 390.0, 400.0, 500.0, 600.0, 750.0, 800.0, 700.0, 600.0, 400.0,
                                )
                            markArea =
                                unsafeJso<MarkAreaOption> {
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> { color = ZRColor("rgba(255, 173, 177, 0.4)") }
                                    data =
                                        arrayOf(
                                            peakArea("Morning Peak", "07:30", "10:00"),
                                            peakArea("Evening Peak", "17:30", "21:15"),
                                        )
                                }
                        },
                    )
            }
        }
