package demo.examples.chord

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import js.objects.unsafeJso
import demo.examples.Category

private val chordLineStyleColorCategory = Category(Groups.CHORD, "examples/chord/ChordLineStyleColor.kt")

val chordLineStyleColorEntry: CatalogEntry =
    chordLineStyleColorCategory.chart("chord-lineStyle-color", "和弦图边的颜色", "Chord lineStyle.color") {
            unsafeJso<EChartsOption> {
                tooltip = chordTooltip
                legend = chordLegend
                title =
                    arrayOf(
                        unsafeJso<TitleOption> {
                            text = "lineStyle.color"
                            textStyle = unsafeJso<io.github.shijianjs.ktjs.echarts.options.TextStyleOption> { fontSize = StringOrDouble(24.0) }
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
        }
