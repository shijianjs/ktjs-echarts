package demo.examples.chord

import echarts.options.ChordSeriesOption
import echarts.options.LabelOption
import echarts.options.LineStyleOption
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
import demo.examples.gauge.center
import demo.examples.matrix.names
import demo.examples.sunburst.radius

internal val chordLabels = arrayOf("A", "B", "C", "D", "E", "F", "G")

/** The nodes `A` .. `F`. */
internal fun chordNodes(vararg names: String): Array<ChordDataItem?> =
    dataItems(*Array(names.size) { index -> ChordDataItem(unsafeJso<ChordNodeItemOption> { name = names[index] }) })

/** One edge of a chord series. */
internal fun link(source: String, target: String, value: Double): echarts.options.ChordEdgeItemOption =
    unsafeJso {
        this.source = StringOrDouble(source)
        this.target = StringOrDouble(target)
        this.value = echarts.options.OptionDataValue(value)
    }

internal val chordTooltip = one(unsafeJso<TooltipOption> {})

internal val chordLegend = one(unsafeJso<echarts.options.LegendOption> {})

/** One of the three columns of `chord-lineStyle-color`. */
internal fun column(index: Int, color: String, nodes: Array<String>): ChordSeriesOption =
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
internal fun columnTitle(index: Int, text: String): TitleOption =
    unsafeJso {
        this.text = text
        left = StringOrDouble("${(index * 2 + 1) / 6.0 * 100}%")
        top = StringOrDouble("25%")
        textAlign = "center"
        padding = echarts.options.DoubleOrDoubleArray(0.0)
    }
