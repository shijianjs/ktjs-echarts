package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.toolbox

private val lineAqiCategory = Category(Groups.LINE, "examples/line/LineAqi.kt")

val lineAqiEntry: CatalogEntry =
    lineAqiCategory.interactive("line-aqi", "北京 AQI 可视化", "Beijing AQI") { chart ->
            loadJson("data/aqi-beijing.json").then { json ->
                val rows = json.unsafeCast<Array<Array<JsAny>>>()
                val dates = Array(rows.size) { rows[it][0].unsafeCast<String>() }
                val values = DoubleArray(rows.size) { rows[it][1].unsafeCast<Double>() }
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Beijing AQI"
                                    left = StringOrDouble("1%")
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                        grid =
                            one(
                                unsafeJso<GridOption> {
                                    left = StringOrDouble("5%")
                                    right = StringOrDouble("15%")
                                    bottom = StringOrDouble("10%")
                                },
                            )
                        xAxis = one(unsafeJso<XAxisOption> { data = strings(*dates) })
                        yAxis = one(unsafeJso<YAxisOption> {})
                        toolbox = toolbox(saveAsImage = true)
                        dataZoom =
                            arrayOf(
                                unsafeJso<DataZoomOption> { startValue = DataZoomValue("2014-06-01") },
                                unsafeJso<DataZoomOption> { type = DataZoomTypes.inside },
                            )
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    top = StringOrDouble(50.0)
                                    right = StringOrDouble(10.0)
                                    pieces =
                                        arrayOf(
                                            aqiPiece(0.0, 50.0, "#93CE07"),
                                            aqiPiece(50.0, 100.0, "#FBDB0F"),
                                            aqiPiece(100.0, 150.0, "#FC7D02"),
                                            aqiPiece(150.0, 200.0, "#FD0100"),
                                            aqiPiece(200.0, 300.0, "#AA069F"),
                                            aqiPiece(300.0, null, "#AC3B2A"),
                                        )
                                    outOfRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray("#999") }
                                },
                            )
                        series =
                            one(
                                lineSeries {
                                    name = "Beijing AQI"
                                    typedData = numbers(*values)
                                    markLine =
                                        unsafeJso<MarkLineOption> {
                                            silent = true
                                            lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#333") }
                                            data =
                                                arrayOf(
                                                    aqiMark(50.0),
                                                    aqiMark(100.0),
                                                    aqiMark(150.0),
                                                    aqiMark(200.0),
                                                    aqiMark(300.0),
                                                )
                                        }
                                },
                            )
                    },
                )
                null
            }
        }
