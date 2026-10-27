package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.TooltipTriggers
import echarts.options.ChordSeriesOption
import echarts.options.EChartsOption
import echarts.EmphasisFocuses
import echarts.options.ItemStyleOption
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.ChordDataItem
import echarts.options.ChordNodeItemOption
import echarts.options.chordSeries
import echarts.options.dataItems
import echarts.options.one
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRFontWeights

/*
 * Chord examples ported from `public/examples/ts/chord*.ts` of apache/echarts-examples.
 */

private val chord = Category(Groups.CHORD, "examples/Chord.kt")

private val chordLabels = arrayOf("A", "B", "C", "D", "E", "F", "G")

/** The nodes `A` .. `F`. */
private fun chordNodes(vararg names: String): Array<ChordDataItem?> =
    dataItems(*Array(names.size) { index -> ChordDataItem(unsafeJso<ChordNodeItemOption> { name = names[index] }) })

/** One edge of a chord series. */
private fun link(source: String, target: String, value: Double): echarts.options.ChordEdgeItemOption =
    unsafeJso {
        this.source = StringOrDouble(source)
        this.target = StringOrDouble(target)
        this.value = echarts.options.OptionDataValue(value)
    }

private val chordTooltip = one(unsafeJso<TooltipOption> {})

private val chordLegend = one(unsafeJso<echarts.options.LegendOption> {})

val chordEntries: List<CatalogEntry> =
    listOf(
        chord.chart("chord-simple", "基础和弦图", "Basic Chord") {
            unsafeJso<EChartsOption> {
                tooltip = chordTooltip
                legend = chordLegend
                series =
                    arrayOf<SeriesOption>(
                        chordSeries {
                            clockwise = false
                            label = unsafeJso<LabelOption> { show = true }
                            lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("target") }
                            typedData = chordNodes("A", "B", "C", "D")
                            links =
                                arrayOf(
                                    link("A", "B", 40.0),
                                    link("A", "C", 20.0),
                                    link("B", "D", 20.0),
                                )
                        },
                    )
            }
        },
        chord.chart("chord-minAngle", "和弦图 minAngle", "Chord minAngle") {
            unsafeJso<EChartsOption> {
                tooltip = chordTooltip
                legend = chordLegend
                series =
                    arrayOf<SeriesOption>(
                        chordSeries {
                            label = unsafeJso<LabelOption> { show = true }
                            minAngle = 30.0
                            typedData = chordNodes("A", "B", "C", "D", "E", "F")
                            links =
                                arrayOf(
                                    link("A", "B", 40.0),
                                    link("B", "C", 20.0),
                                    link("E", "A", 5.0),
                                )
                        },
                    )
            }
        },
        chord.chart("chord-lineStyle-color", "和弦图边的颜色", "Chord lineStyle.color") {
            unsafeJso<EChartsOption> {
                tooltip = chordTooltip
                legend = chordLegend
                title =
                    arrayOf(
                        unsafeJso<TitleOption> {
                            text = "lineStyle.color"
                            textStyle = unsafeJso<echarts.options.TextStyleOption> { fontSize = StringOrDouble(24.0) }
                        },
                        columnTitle(0, "source"),
                        columnTitle(1, "target"),
                        columnTitle(2, "gradient"),
                    )
                series =
                    arrayOf<SeriesOption>(
                        column(0, "source", chordLabels),
                        column(1, "target", chordLabels),
                        column(2, "gradient", chordLabels),
                    )
            }
        },
        chord.chart("chord-style", "和弦图样式", "Chord Style") {
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
        },
    )

/** One of the three columns of `chord-lineStyle-color`. */
private fun column(index: Int, color: String, nodes: Array<String>): ChordSeriesOption =
    chordSeries {
        label = unsafeJso<LabelOption> { show = true }
        center = arrayOf(StringOrDouble("${(index * 2 + 1) / 6.0 * 100}%"), StringOrDouble("50%"))
        radius = StringOrDoubleOrArray(arrayOf(StringOrDouble("28%"), StringOrDouble("32%")))
        lineStyle = unsafeJso<LineStyleOption> { this.color = ZRColor(color) }
        typedData = chordNodes(*nodes)
        links =
            arrayOf(
                link("A", "B", 30.0),
                link("A", "C", 20.0),
                link("B", "D", 10.0),
                link("C", "A", 15.0),
                link("D", "A", 25.0),
            )
    }

/** Caption above one of the three columns. */
private fun columnTitle(index: Int, text: String): TitleOption =
    unsafeJso {
        this.text = text
        left = StringOrDouble("${(index * 2 + 1) / 6.0 * 100}%")
        top = StringOrDouble("25%")
        textAlign = "center"
        padding = echarts.options.DoubleOrDoubleArray(0.0)
    }
