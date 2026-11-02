package demo.examples.chord

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.chordSeries
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRFontWeights
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.sunburst.radius

private val chordStyleCategory = Category(Groups.CHORD, "examples/chord/ChordStyle.kt")

val chordStyleEntry: CatalogEntry =
    chordStyleCategory.chart("chord-style", "和弦图样式", "Chord Style") {
            unsafeJso<EChartsOption> {
                tooltip = chordTooltip
                legend = chordLegend
                series =
                    arrayOf<SeriesOption>(
                        chordSeries {
                            padAngle = 1.0
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("48%"))
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble("70%"), StringOrDouble("80%")))
                            typedData = chordNodes("A", "B", "C", "D", "E", "F", "G")
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderRadius =
                                        StringOrDoubleOrArray(arrayOf(StringOrDouble(0.0), StringOrDouble(15.0)))
                                    borderWidth = 2.0
                                    borderColor = ZRColor("#fff")
                                }
                            lineStyle = unsafeJso<LineStyleOption> { opacity = 0.3; color = ZRColor("gradient") }
                            emphasis = unsafeJso<echarts.options.EmphasisOption> { focus = EmphasisFocuses.self }
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    position = LabelPosition("inside")
                                    color = "#fff"
                                    fontWeight = ZRFontWeights.bold
                                }
                            links =
                                arrayOf(
                                    link("A", "B", 14.0),
                                    link("A", "C", 8.0),
                                    link("B", "C", 20.0),
                                    link("B", "E", 15.0),
                                    link("C", "B", 8.0),
                                    link("C", "E", 3.0),
                                    link("D", "A", 12.0),
                                    link("D", "B", 3.0),
                                    link("E", "A", 15.0),
                                    link("E", "C", 5.0),
                                    link("F", "C", 5.0),
                                    link("G", "A", 6.0),
                                    link("G", "B", 8.0),
                                    link("G", "D", 4.0),
                                )
                        },
                    )
            }
        }
