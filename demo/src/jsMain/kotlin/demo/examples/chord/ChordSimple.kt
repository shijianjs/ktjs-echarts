package demo.examples.chord

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.LineStyleOption
import echarts.options.SeriesOption
import echarts.options.chordSeries
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category

private val chordSimpleCategory = Category(Groups.CHORD, "examples/chord/ChordSimple.kt")

val chordSimpleEntry: CatalogEntry =
    chordSimpleCategory.chart("chord-simple", "基础和弦图", "Basic Chord") {
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
        }
