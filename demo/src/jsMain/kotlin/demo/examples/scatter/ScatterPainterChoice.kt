package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel

private val scatterPainterChoiceCategory = Category(Groups.SCATTER, "examples/scatter/ScatterPainterChoice.kt")

val scatterPainterChoiceEntry: CatalogEntry =
    scatterPainterChoiceCategory.interactive("scatter-painter-choice", "历代绘画大师的色彩运用", "Master Painter Color Choices Throughout History") { chart ->
            loadJson("data/masterPainterColorChoice.json").then { json ->
                val painters = json.unsafeCast<Array<PainterColorData>>()[0]
                val data: Array<ScatterDataItem?> = painterPoints(painters)
                val colorOf = ZRColor(painterColors(painters.marker.color))
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Master Painter Color Choices Throughout History"
                                    subtext = "Data From Plot.ly"
                                    left = StringOrDouble("right")
                                },
                            )
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.value
                                    splitLine = unsafeJso<SplitLineOption> { show = false }
                                    scale = true
                                    splitNumber = 5.0
                                    max = AxisMinMax("dataMax")
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            formatter =
                                                LabelFormatter(
                                                    js("(value) => value + 's'") as LabelFormatterCallback,
                                                )
                                        }
                                },
                            )
                        yAxis =
                            one(
                                unsafeJso<YAxisOption> {
                                    type = AxisTypes.value
                                    min = AxisMinMax(0.0)
                                    max = AxisMinMax(360.0)
                                    interval = AxisInterval(60.0)
                                    name = "Hue"
                                    splitLine = unsafeJso<SplitLineOption> { show = false }
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                scatterSeries {
                                    name = "scatter"
                                    symbolSize =
                                        SymbolSize { _, params ->
                                            val index = params.dataIndex
                                            DoubleOrDoubleArray(painters.marker.size[index] / painters.marker.sizeref)
                                        }
                                    itemStyle = unsafeJso<ItemStyleOption> { color = colorOf }
                                    typedData = data
                                },
                            )
                    },
                )
                null
            }
        }
