package demo.examples.chord

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.SeriesOption
import echarts.options.chordSeries
import js.objects.unsafeJso
import demo.examples.Category

private val chordMinAngleCategory = Category(Groups.CHORD, "examples/chord/ChordMinAngle.kt")

val chordMinAngleEntry: CatalogEntry =
    chordMinAngleCategory.chart("chord-minAngle", "和弦图 minAngle", "Chord minAngle") {
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
        }
