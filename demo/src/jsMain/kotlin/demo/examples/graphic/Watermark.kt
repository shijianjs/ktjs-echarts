package demo.examples.graphic

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.axisLabel
import demo.examples.sunburst.radius

private val watermarkCategory = Category(Groups.GRAPHIC, "examples/graphic/Watermark.kt")

val watermarkEntry: CatalogEntry =
    watermarkCategory.chart(
            "watermark",
            "水印 - ECharts 下载统计",
            "Watermark - ECharts Download",
            "用 canvas 画一块 ECHARTS 斜向水印，再把它当成 backgroundColor 的重复 pattern；" +
                "两个柱状图块与两个饼图共用同一份下载统计。",
            "The ECHARTS watermark is drawn into a canvas tile and used as a repeating background " +
                "pattern; two stacked bar blocks and two pies share the same download statistics.",
        ) {
            val downloads = totalOf(DOWNLOAD_COUNTS).toInt()
            val themes = totalOf(THEME_COUNTS).toInt()
            unsafeJso<EChartsOption> {
                backgroundColor = watermarkBackground()
                tooltip = one(unsafeJso<TooltipOption> {})
                title =
                    arrayOf(
                        unsafeJso<TitleOption> {
                            text = "在线构建"
                            subtext = "总计 ${BUILDER_ALL.toInt()}"
                            left = StringOrDouble("25%")
                            textAlign = "center"
                        },
                        unsafeJso<TitleOption> {
                            text = "各版本下载"
                            subtext = "总计 $downloads"
                            left = StringOrDouble("75%")
                            textAlign = "center"
                        },
                        unsafeJso<TitleOption> {
                            text = "主题下载"
                            subtext = "总计 $themes"
                            left = StringOrDouble("75%")
                            top = StringOrDouble("50%")
                            textAlign = "center"
                        },
                    )
                grid =
                    arrayOf(
                        unsafeJso<GridOption> {
                            top = StringOrDouble(50.0)
                            width = StringOrDouble("50%")
                            bottom = StringOrDouble("45%")
                            left = StringOrDouble(10.0)
                            containLabel = true
                        },
                        unsafeJso<GridOption> {
                            top = StringOrDouble("55%")
                            width = StringOrDouble("50%")
                            bottom = StringOrDouble(0.0)
                            left = StringOrDouble(10.0)
                            containLabel = true
                        },
                    )
                xAxis =
                    arrayOf(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            max = AxisMinMax(BUILDER_ALL)
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            max = AxisMinMax(BUILDER_ALL)
                            gridIndex = 1.0
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            data = strings(*namesOf(BUILDER_CHARTS))
                            axisLabel = rotatedLabels()
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.category
                            gridIndex = 1.0
                            data = strings(*namesOf(BUILDER_COMPONENTS))
                            axisLabel = rotatedLabels()
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                series =
                    builderBars(BUILDER_CHARTS, "chart", null) +
                        builderBars(BUILDER_COMPONENTS, "component", 1.0) +
                        arrayOf<SeriesOption>(
                            pieSeries {
                                radius =
                                    StringOrDoubleOrArray(
                                        arrayOf(StringOrDouble(0.0), StringOrDouble("30%")),
                                    )
                                center = arrayOf(StringOrDouble("75%"), StringOrDouble("25%"))
                                typedData = countItems(DOWNLOAD_COUNTS)
                            },
                            pieSeries {
                                radius =
                                    StringOrDoubleOrArray(
                                        arrayOf(StringOrDouble(0.0), StringOrDouble("30%")),
                                    )
                                center = arrayOf(StringOrDouble("75%"), StringOrDouble("75%"))
                                typedData = countItems(THEME_COUNTS)
                            },
                        )
            }
        }
