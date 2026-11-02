package demo.examples.doc

import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import zrender.ElementTextConfig
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import demo.examples.bar.rightLabel
import demo.examples.dataset.transform
import demo.examples.gauge.center
import demo.examples.geo.region
import demo.examples.matrix.cell
import demo.examples.matrix.dimension
import demo.examples.matrix.names
import demo.examples.shared.axisLabel
import demo.examples.shared.every
import demo.examples.shared.iconBackground
import demo.examples.shared.pieItem
import demo.examples.shared.recordOf
import demo.examples.shared.richOf
import demo.examples.shared.row
import demo.examples.sunburst.radius

/** The five slices the pie tutorials share. */
internal fun visitSource(): Array<PieDataItem?> =
    dataItems(
        pieItem("视频广告", 235.0),
        pieItem("联盟广告", 274.0),
        pieItem("邮件营销", 310.0),
        pieItem("直接访问", 335.0),
        pieItem("搜索引擎", 400.0),
    )

/** A sunburst node with an optional colour and an optional list of children. */
internal fun docSunburstNode(
    name: String? = null,
    value: Double? = null,
    color: String? = null,
    label: SunburstLabelOption? = null,
    children: Array<SunburstSeriesNodeItemOption>? = null,
): SunburstSeriesNodeItemOption {
    val nodeName = name
    val nodeValue = value
    val fill = color
    val nodeLabel = label
    val nodeChildren = children
    return unsafeJso {
        if (nodeName != null) this.name = OptionId(nodeName)
        if (nodeValue != null) this.value = OptionDataValue(nodeValue)
        if (fill != null) itemStyle = unsafeJso { this.color = ZRColor(fill) }
        if (nodeLabel != null) this.label = nodeLabel
        if (nodeChildren != null) this.children = nodeChildren
    }
}

/** `label: { align: <align>, position: 'inside' }` of `sunburst-label-align`. */
internal fun insideLabel(align: TextAlign): SunburstLabelOption = unsafeJso {
    this.align = align
    position = LabelPosition("inside")
}

/** The hidden `-1..1` x axis the text examples sit on. */
internal fun hiddenXAxis(): Array<XAxisOption> =
    one(
        unsafeJso<XAxisOption> {
            axisLabel = unsafeJso<AxisLabelOption> { show = false }
            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
            splitLine = unsafeJso<SplitLineOption> { show = false }
            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
            min = AxisMinMax(-1.0)
            max = AxisMinMax(1.0)
        },
    )

/** The hidden `-1..1` y axis the text examples sit on. */
internal fun hiddenYAxis(): Array<YAxisOption> =
    one(
        unsafeJso<YAxisOption> {
            axisLabel = unsafeJso<AxisLabelOption> { show = false }
            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
            splitLine = unsafeJso<SplitLineOption> { show = false }
            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
            min = AxisMinMax(-1.0)
            max = AxisMinMax(1.0)
        },
    )

/** An icon cell of the `titleBlockRich` table; the block is empty, so it needs an explicit width. */
internal fun titleBlockIcon(name: String): TextStyleOption =
    unsafeJso {
        height = 30.0
        align = TextAlign.left
        backgroundColor = ZRColor(iconBackground(name))
        width = StringOrDouble(30.0)
    }

/** The `rich` table of `doc-example/title-block`, shared by the three blocks. */
internal fun titleBlockRich(): Record<String, TextStyleOption> =
    richOf(
        "titleBg" to
            unsafeJso<TextStyleOption> {
                backgroundColor = ZRColor("#000")
                height = 30.0
                borderRadius = DoubleOrDoubleArray(arrayOf(5.0, 5.0, 0.0, 0.0))
                padding = DoubleOrDoubleArray(arrayOf(0.0, 10.0, 0.0, 10.0))
                width = StringOrDouble("100%")
                color = "#eee"
            },
        "tc" to
            unsafeJso<TextStyleOption> {
                align = TextAlign.center
                color = "#eee"
            },
        "hr" to
            unsafeJso<TextStyleOption> {
                borderColor = "#777"
                width = StringOrDouble("100%")
                borderWidth = 0.5
                height = 0.0
            },
        "sunny" to titleBlockIcon("Sunny"),
        "cloudy" to titleBlockIcon("Cloudy"),
        "showers" to titleBlockIcon("Showers"),
    )

/**
 * One text block of `doc-example/title-block`.
 *
 * The per-item label only overrides the formatter and, for the center and right titles, the `titleBg`
 * alignment; the rest of the table comes from the series-level `rich`.
 */
internal fun titleBlockItem(y: Double, formatter: String, alignTitle: Boolean): ScatterDataItem =
    ScatterDataItem(
        unsafeJso<ScatterDataItemOption> {
            typedValue = OptionDataValueOrArray(arrayOf(OptionDataValue(0.0), OptionDataValue(y)))
            label =
                unsafeJso<LabelOption> {
                    this.formatter = LabelFormatter(formatter)
                    if (alignTitle) {
                        rich = richOf("titleBg" to unsafeJso<TextStyleOption> { align = TextAlign.right })
                    }
                }
        },
    )

/** The seven slices of the `pie-label-*` pages; the `distanceToLabelLine` / `bleedMargin` pages use longer names. */
internal fun pieLabelSlices(longNames: Boolean): Array<PieDataItem?> {
    val names =
        if (longNames) {
            arrayOf("Apple Numbers", "Strawberry Numbers", "Banana Numbers", "Orange Numbers", "Pear Numbers", "Pineapple Numbers", "Grape Numbers")
        } else {
            arrayOf("Apples", "Strawberries", "Bananas", "Oranges", "Pears", "Pineapples", "Grapes")
        }
    val values = doubleArrayOf(70.0, 68.0, 48.0, 40.0, 32.0, 27.0, 18.0)
    return Array<PieDataItem?>(names.size) { index -> pieItem(names[index], values[index]) }
}

/** The title block above the two pies of a `pie-label-*` page. */
internal fun pieLabelTitles(leftCaption: String, rightCaption: String): Array<TitleOption> =
    arrayOf(
        unsafeJso<TitleOption> { text = "Pie label margin" },
        unsafeJso<TitleOption> {
            subtext = leftCaption
            left = StringOrDouble("25%")
            top = StringOrDouble("75%")
            textAlign = "center"
        },
        unsafeJso<TitleOption> {
            subtext = rightCaption
            left = StringOrDouble("75%")
            top = StringOrDouble("75%")
            textAlign = "center"
        },
    )

/** The two half-width pies of a `pie-label-*` page, each with its own label options. */
internal fun pieLabelPair(
    slices: Array<PieDataItem?>,
    leftLabel: PieLabelOption,
    rightLabel: PieLabelOption,
): Array<SeriesOption> =
    arrayOf(
        pieSeries {
            radius = StringOrDoubleOrArray("25%")
            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
            animation = false
            typedLabel = leftLabel
            left = StringOrDouble(0.0)
            right = StringOrDouble("50%")
            top = StringOrDouble(0.0)
            bottom = StringOrDouble(0.0)
            typedData = slices
        },
        pieSeries {
            radius = StringOrDoubleOrArray("25%")
            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
            animation = false
            typedLabel = rightLabel
            left = StringOrDouble("50%")
            right = StringOrDouble(0.0)
            top = StringOrDouble(0.0)
            bottom = StringOrDouble(0.0)
            typedData = slices
        },
    )

/** One descriptor of the `sort` transform of `doc-example/data-transform-multiple-sort-bar`. */
internal external interface SortDescriptor {
    var dimension: String
    var order: String
}

/**
 * Config of the `sort` transform: an array of descriptors.
 *
 * Upstream types `transform.config` as `unknown`; the wrapper types it as a record, so the array is
 * cast to the record type. ECharts only looks at the resulting JavaScript value.
 */
internal fun sortDescriptors(vararg pairs: Pair<String, String>): Record<String, JsAny> =
    Array(pairs.size) { index ->
        unsafeJso<SortDescriptor> {
            dimension = pairs[index].first
            order = pairs[index].second
        }
    }.unsafeCast<Record<String, JsAny>>()

/**
 * Source rows of `doc-example/data-transform-multiple-sort-bar`.
 *
 * One score is `null`, which [datasetArrayRows] cannot express, so the rows are cast to
 * [DatasetSource]; ECharts reads the plain JavaScript arrays the same way.
 */
internal fun sortBarSource(): DatasetSource =
    arrayOf(
        arrayOf<Any?>(" Hannah Krause ", 41.0, "Engineer", 314.0, "2011-02-12"),
        arrayOf<Any?>("Zhao Qian ", 20.0, "Teacher", 351.0, "2011-03-01"),
        arrayOf<Any?>(" Jasmin Krause ", 52.0, "Musician", 287.0, "2011-02-14"),
        arrayOf<Any?>("Li Lei", 37.0, "Teacher", 219.0, "2011-02-18"),
        arrayOf<Any?>(" Karle Neumann ", 25.0, "Engineer", 253.0, "2011-04-02"),
        arrayOf<Any?>(" Adrian Groß", 19.0, "Teacher", null, "2011-01-16"),
        arrayOf<Any?>("Mia Neumann", 71.0, "Engineer", 165.0, "2011-03-19"),
        arrayOf<Any?>(" Böhm Fuchs", 36.0, "Musician", 318.0, "2011-02-24"),
        arrayOf<Any?>("Han Meimei ", 67.0, "Engineer", 366.0, "2011-03-12"),
    ).unsafeCast<DatasetSource>()

/** One row of the `doc-example/parallel-all` tables: `[date, AQI, PM2.5, PM10, CO, NO2, SO2, grade]`. */
internal fun aqiRow(
    day: Double,
    aqi: Double,
    pm25: Double,
    pm10: Double,
    co: Double,
    no2: Double,
    so2: Double,
    grade: String,
): ParallelSeriesDataItem = ParallelSeriesDataItem(row(day, aqi, pm25, pm10, co, no2, so2, grade))

/** The daily AQI table of 北京 in `doc-example/parallel-all`. */
internal val parallelBeijing: List<ParallelSeriesDataItem> =
    listOf<ParallelSeriesDataItem>(
        aqiRow(1.0, 55.0, 9.0, 56.0, 0.46, 18.0, 6.0, "良"),
        aqiRow(2.0, 25.0, 11.0, 21.0, 0.65, 34.0, 9.0, "优"),
        aqiRow(3.0, 56.0, 7.0, 63.0, 0.3, 14.0, 5.0, "良"),
        aqiRow(4.0, 33.0, 7.0, 29.0, 0.33, 16.0, 6.0, "优"),
        aqiRow(5.0, 42.0, 24.0, 44.0, 0.76, 40.0, 16.0, "优"),
        aqiRow(6.0, 82.0, 58.0, 90.0, 1.77, 68.0, 33.0, "良"),
        aqiRow(7.0, 74.0, 49.0, 77.0, 1.46, 48.0, 27.0, "良"),
        aqiRow(8.0, 78.0, 55.0, 80.0, 1.29, 59.0, 29.0, "良"),
        aqiRow(9.0, 267.0, 216.0, 280.0, 4.8, 108.0, 64.0, "重度污染"),
        aqiRow(10.0, 185.0, 127.0, 216.0, 2.52, 61.0, 27.0, "中度污染"),
        aqiRow(11.0, 39.0, 19.0, 38.0, 0.57, 31.0, 15.0, "优"),
        aqiRow(12.0, 41.0, 11.0, 40.0, 0.43, 21.0, 7.0, "优"),
        aqiRow(13.0, 64.0, 38.0, 74.0, 1.04, 46.0, 22.0, "良"),
        aqiRow(14.0, 108.0, 79.0, 120.0, 1.7, 75.0, 41.0, "轻度污染"),
        aqiRow(15.0, 108.0, 63.0, 116.0, 1.48, 44.0, 26.0, "轻度污染"),
        aqiRow(16.0, 33.0, 6.0, 29.0, 0.34, 13.0, 5.0, "优"),
        aqiRow(17.0, 94.0, 66.0, 110.0, 1.54, 62.0, 31.0, "良"),
        aqiRow(18.0, 186.0, 142.0, 192.0, 3.88, 93.0, 79.0, "中度污染"),
        aqiRow(19.0, 57.0, 31.0, 54.0, 0.96, 32.0, 14.0, "良"),
        aqiRow(20.0, 22.0, 8.0, 17.0, 0.48, 23.0, 10.0, "优"),
        aqiRow(21.0, 39.0, 15.0, 36.0, 0.61, 29.0, 13.0, "优"),
        aqiRow(22.0, 94.0, 69.0, 114.0, 2.08, 73.0, 39.0, "良"),
        aqiRow(23.0, 99.0, 73.0, 110.0, 2.43, 76.0, 48.0, "良"),
        aqiRow(24.0, 31.0, 12.0, 30.0, 0.5, 32.0, 16.0, "优"),
        aqiRow(25.0, 42.0, 27.0, 43.0, 1.0, 53.0, 22.0, "优"),
        aqiRow(26.0, 154.0, 117.0, 157.0, 3.05, 92.0, 58.0, "中度污染"),
        aqiRow(27.0, 234.0, 185.0, 230.0, 4.09, 123.0, 69.0, "重度污染"),
        aqiRow(28.0, 160.0, 120.0, 186.0, 2.77, 91.0, 50.0, "中度污染"),
        aqiRow(29.0, 134.0, 96.0, 165.0, 2.76, 83.0, 41.0, "轻度污染"),
        aqiRow(30.0, 52.0, 24.0, 60.0, 1.03, 50.0, 21.0, "良"),
        aqiRow(31.0, 46.0, 5.0, 49.0, 0.28, 10.0, 6.0, "优"),
    )

/** The daily AQI table of 广州 in `doc-example/parallel-all`. */
internal val parallelGuangzhou: List<ParallelSeriesDataItem> =
    listOf<ParallelSeriesDataItem>(
        aqiRow(1.0, 26.0, 37.0, 27.0, 1.163, 27.0, 13.0, "优"),
        aqiRow(2.0, 85.0, 62.0, 71.0, 1.195, 60.0, 8.0, "良"),
        aqiRow(3.0, 78.0, 38.0, 74.0, 1.363, 37.0, 7.0, "良"),
        aqiRow(4.0, 21.0, 21.0, 36.0, 0.634, 40.0, 9.0, "优"),
        aqiRow(5.0, 41.0, 42.0, 46.0, 0.915, 81.0, 13.0, "优"),
        aqiRow(6.0, 56.0, 52.0, 69.0, 1.067, 92.0, 16.0, "良"),
        aqiRow(7.0, 64.0, 30.0, 28.0, 0.924, 51.0, 2.0, "良"),
        aqiRow(8.0, 55.0, 48.0, 74.0, 1.236, 75.0, 26.0, "良"),
        aqiRow(9.0, 76.0, 85.0, 113.0, 1.237, 114.0, 27.0, "良"),
        aqiRow(10.0, 91.0, 81.0, 104.0, 1.041, 56.0, 40.0, "良"),
        aqiRow(11.0, 84.0, 39.0, 60.0, 0.964, 25.0, 11.0, "良"),
        aqiRow(12.0, 64.0, 51.0, 101.0, 0.862, 58.0, 23.0, "良"),
        aqiRow(13.0, 70.0, 69.0, 120.0, 1.198, 65.0, 36.0, "良"),
        aqiRow(14.0, 77.0, 105.0, 178.0, 2.549, 64.0, 16.0, "良"),
        aqiRow(15.0, 109.0, 68.0, 87.0, 0.996, 74.0, 29.0, "轻度污染"),
        aqiRow(16.0, 73.0, 68.0, 97.0, 0.905, 51.0, 34.0, "良"),
        aqiRow(17.0, 54.0, 27.0, 47.0, 0.592, 53.0, 12.0, "良"),
        aqiRow(18.0, 51.0, 61.0, 97.0, 0.811, 65.0, 19.0, "良"),
        aqiRow(19.0, 91.0, 71.0, 121.0, 1.374, 43.0, 18.0, "良"),
        aqiRow(20.0, 73.0, 102.0, 182.0, 2.787, 44.0, 19.0, "良"),
        aqiRow(21.0, 73.0, 50.0, 76.0, 0.717, 31.0, 20.0, "良"),
        aqiRow(22.0, 84.0, 94.0, 140.0, 2.238, 68.0, 18.0, "良"),
        aqiRow(23.0, 93.0, 77.0, 104.0, 1.165, 53.0, 7.0, "良"),
        aqiRow(24.0, 99.0, 130.0, 227.0, 3.97, 55.0, 15.0, "良"),
        aqiRow(25.0, 146.0, 84.0, 139.0, 1.094, 40.0, 17.0, "轻度污染"),
        aqiRow(26.0, 113.0, 108.0, 137.0, 1.481, 48.0, 15.0, "轻度污染"),
        aqiRow(27.0, 81.0, 48.0, 62.0, 1.619, 26.0, 3.0, "良"),
        aqiRow(28.0, 56.0, 48.0, 68.0, 1.336, 37.0, 9.0, "良"),
        aqiRow(29.0, 82.0, 92.0, 174.0, 3.29, 0.0, 13.0, "良"),
        aqiRow(30.0, 106.0, 116.0, 188.0, 3.628, 101.0, 16.0, "轻度污染"),
        aqiRow(31.0, 118.0, 50.0, 0.0, 1.383, 76.0, 11.0, "轻度污染"),
    )

/** The daily AQI table of 上海 in `doc-example/parallel-all`. */
internal val parallelShanghai: List<ParallelSeriesDataItem> =
    listOf<ParallelSeriesDataItem>(
        aqiRow(1.0, 91.0, 45.0, 125.0, 0.82, 34.0, 23.0, "良"),
        aqiRow(2.0, 65.0, 27.0, 78.0, 0.86, 45.0, 29.0, "良"),
        aqiRow(3.0, 83.0, 60.0, 84.0, 1.09, 73.0, 27.0, "良"),
        aqiRow(4.0, 109.0, 81.0, 121.0, 1.28, 68.0, 51.0, "轻度污染"),
        aqiRow(5.0, 106.0, 77.0, 114.0, 1.07, 55.0, 51.0, "轻度污染"),
        aqiRow(6.0, 109.0, 81.0, 121.0, 1.28, 68.0, 51.0, "轻度污染"),
        aqiRow(7.0, 106.0, 77.0, 114.0, 1.07, 55.0, 51.0, "轻度污染"),
        aqiRow(8.0, 89.0, 65.0, 78.0, 0.86, 51.0, 26.0, "良"),
        aqiRow(9.0, 53.0, 33.0, 47.0, 0.64, 50.0, 17.0, "良"),
        aqiRow(10.0, 80.0, 55.0, 80.0, 1.01, 75.0, 24.0, "良"),
        aqiRow(11.0, 117.0, 81.0, 124.0, 1.03, 45.0, 24.0, "轻度污染"),
        aqiRow(12.0, 99.0, 71.0, 142.0, 1.1, 62.0, 42.0, "良"),
        aqiRow(13.0, 95.0, 69.0, 130.0, 1.28, 74.0, 50.0, "良"),
        aqiRow(14.0, 116.0, 87.0, 131.0, 1.47, 84.0, 40.0, "轻度污染"),
        aqiRow(15.0, 108.0, 80.0, 121.0, 1.3, 85.0, 37.0, "轻度污染"),
        aqiRow(16.0, 134.0, 83.0, 167.0, 1.16, 57.0, 43.0, "轻度污染"),
        aqiRow(17.0, 79.0, 43.0, 107.0, 1.05, 59.0, 37.0, "良"),
        aqiRow(18.0, 71.0, 46.0, 89.0, 0.86, 64.0, 25.0, "良"),
        aqiRow(19.0, 97.0, 71.0, 113.0, 1.17, 88.0, 31.0, "良"),
        aqiRow(20.0, 84.0, 57.0, 91.0, 0.85, 55.0, 31.0, "良"),
        aqiRow(21.0, 87.0, 63.0, 101.0, 0.9, 56.0, 41.0, "良"),
        aqiRow(22.0, 104.0, 77.0, 119.0, 1.09, 73.0, 48.0, "轻度污染"),
        aqiRow(23.0, 87.0, 62.0, 100.0, 1.0, 72.0, 28.0, "良"),
        aqiRow(24.0, 168.0, 128.0, 172.0, 1.49, 97.0, 56.0, "中度污染"),
        aqiRow(25.0, 65.0, 45.0, 51.0, 0.74, 39.0, 17.0, "良"),
        aqiRow(26.0, 39.0, 24.0, 38.0, 0.61, 47.0, 17.0, "优"),
        aqiRow(27.0, 39.0, 24.0, 39.0, 0.59, 50.0, 19.0, "优"),
        aqiRow(28.0, 93.0, 68.0, 96.0, 1.05, 79.0, 29.0, "良"),
        aqiRow(29.0, 188.0, 143.0, 197.0, 1.66, 99.0, 51.0, "中度污染"),
        aqiRow(30.0, 174.0, 131.0, 174.0, 1.55, 108.0, 50.0, "中度污染"),
        aqiRow(31.0, 187.0, 143.0, 201.0, 1.39, 89.0, 53.0, "中度污染"),
    )

/** Registers an inline SVG as the map [name]; every `geo-svg-*` page does this before its option. */
internal fun registerSvgMap(name: String, svg: String) {
    ECharts.registerMap(name, MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }))
}

/** One labelled region of `doc-example/geo-svg-label-basic`. */
internal fun svgRegion(name: String, vararg lines: String): RegionOption =
    unsafeJso {
        this.name = name
        label =
            unsafeJso<LabelOption> {
                formatter = LabelFormatter(lines.joinToString("\n"))
                backgroundColor = ZRColor("rgba(0,0,0,0.2)")
                padding = DoubleOrDoubleArray(5.0)
            }
    }

/** The two-rectangle map of `doc-example/geo-svg-named-basic`: one named path, one unnamed. */
internal const val NAMED_SVG =
    """<?xml version="1.0" encoding="utf-8"?>
<svg xmlns="http://www.w3.org/2000/svg" version="1.2" fill-rule="evenodd" xml:space="preserve">
    <path name="named_rect" d="M 0,0 L 0,100 100,100 100,0 Z" fill="#765" />
    <path d="M 150,0 L 150,100 250,100 250,0 Z" fill="#567" />
</svg>
"""

/** The map of `doc-example/geo-svg-label-basic`: a named `<g>` and two paths that share a name. */
internal const val LABELLED_SVG =
    """<?xml version="1.0" encoding="utf-8"?>
<svg xmlns="http://www.w3.org/2000/svg" version="1.2" fill-rule="evenodd" xml:space="preserve"
        viewBox="-180 0 610 300">
    <g name="name_a">
        <path d="M 0,0 L 0,100 100,100 100,0 Z" fill="#765" />
        <path d="M 150,0 L 150,100 250,100 250,0 Z" fill="#765" />
    </g>
    <path name="name_b" d="M -80,200 L -80,300 20,300 20,200 Z" fill="#567" />
    <path name="name_b" d="M 230,200 L 230,300 330,300 330,200 Z" fill="#567" />
</svg>
"""

/** One `geo` of `doc-example/geo-svg-layout-basic`: the map, whether it pins its coordinates, and the caption. */
internal class SvgLayoutCell(val map: String, val svg: String, val pinned: Boolean, val caption: List<String>)

/** Caption lines the pinned cells of `doc-example/geo-svg-layout-basic` add. */
internal val PINNED_CAPTION = listOf("geo.boundingCoords: ", "    [[-50, -50], [150, 150]]")

internal val svgLayoutCells =
    listOf(
        SvgLayoutCell(
            "svg_noWidthHeight_noViewBox",
            """<?xml version="1.0" encoding="utf-8"?>
<svg xmlns="http://www.w3.org/2000/svg" version="1.2" fill-rule="evenodd" xml:space="preserve">
    <circle cx="0" cy="0" r="100" fill="#765" />
</svg>
""",
            pinned = false,
            caption = listOf("<svg>", "<circle cx=\"0\" cy=\"0\" r=\"100\"/>", "</svg>"),
        ),
        SvgLayoutCell(
            "svg_noWidthHeight_hasViewBox",
            """<?xml version="1.0" encoding="utf-8"?>
<svg xmlns="http://www.w3.org/2000/svg" version="1.2" fill-rule="evenodd" xml:space="preserve"
    viewBox="0 0 200 200"
>
    <circle cx="0" cy="0" r="100" fill="#765" />
</svg>
""",
            pinned = false,
            caption = listOf("<svg viewBox=\"0 0 200 200\">", "<circle cx=\"0\" cy=\"0\" r=\"100\"/>", "</svg>"),
        ),
        SvgLayoutCell(
            "svg_hasWidthHeight_hasViewBox",
            """<?xml version="1.0" encoding="utf-8"?>
<svg xmlns="http://www.w3.org/2000/svg" version="1.2" fill-rule="evenodd" xml:space="preserve"
    width="100"
    height="200"
    viewBox="0 0 200 200"
>
    <circle cx="0" cy="0" r="100" fill="#765" />
</svg>
""",
            pinned = false,
            caption =
                listOf(
                    "<svg viewBox=\"0 0 200 200\"",
                    "     width=\"100\" height=\"200\">",
                    "<circle cx=\"0\" cy=\"0\" r=\"100\"/>",
                    "</svg>",
                ),
        ),
    ).let { cells ->
        // The second row repeats the three maps with `boundingCoords` pinned to the same box.
        cells + cells.map { cell -> SvgLayoutCell(cell.map, cell.svg, true, cell.caption + PINNED_CAPTION) }
    }

/**
 * The option of `doc-example/geo-svg-layout-basic`: six squares, three maps each drawn twice — once
 * with the size the SVG itself declares and once pinned to `boundingCoords` — each with a red-headed
 * caption drawn by a `graphic` rect.
 */
internal fun svgLayoutOption(): EChartsOption {
    val width = 100.0
    val height = 100.0
    val xStart = 40.0
    var left = xStart
    var top = 40.0
    val geos = mutableListOf<GeoOption>()
    val graphics = mutableListOf<GraphicComponentLooseOption>()
    svgLayoutCells.forEachIndexed { index, cell ->
        geos +=
            unsafeJso<GeoOption> {
                map = cell.map
                this.left = StringOrDouble(left)
                this.top = StringOrDouble(top)
                this.width = StringOrDouble(width)
                this.height = StringOrDouble(height)
                if (cell.pinned) boundingCoords = arrayOf(arrayOf(-50.0, -50.0), arrayOf(150.0, 150.0))
            }
        graphics +=
            GraphicComponentLooseOption(
                unsafeJso<GraphicElement> {
                    type = "rect"
                    shape =
                        unsafeJso<ZRShapeProps> {
                            x = left
                            y = top
                            this.width = width
                            this.height = height
                        }
                    style =
                        unsafeJso<ZRStyleProps> {
                            // A graphic rect is filled black unless `fill` is cleared explicitly.
                            fill = null
                            stroke = ZRColor("#000")
                            lineWidth = 2.0
                        }
                    textContent =
                        unsafeJso<GraphicElement> {
                            type = "text"
                            style =
                                unsafeJso<SvgLayoutTextStyle> {
                                    text = (listOf("{head|geo $index:}") + cell.caption).joinToString("\n")
                                    fontFamily = "Arial"
                                    fontSize = StringOrDouble(12.0)
                                    align = TextAlign.left
                                    rich = svgLayoutRich
                                }
                        }
                    textConfig =
                        unsafeJso<ElementTextConfig> {
                            position =
                                StringOrDoubleOrArray(
                                    arrayOf(StringOrDouble(0.0), StringOrDouble(height + 10)),
                                )
                        }
                },
            )
        left += width + 120
        if ((index + 1) % 3 == 0) {
            left = xStart
            top += height + 120
        }
    }
    return unsafeJso {
        geo = geos.toTypedArray()
        graphic = graphics.toTypedArray()
    }
}

/** Text style of the graphic captions; [ZRStyleProps] does not declare `rich` yet. */
internal external interface SvgLayoutTextStyle : ZRStyleProps {
    var rich: Record<String, ZRStyleProps>?
}

/** The `{head|…}` style of the captions: red, one 20px line high. */
internal val svgLayoutRich: Record<String, ZRStyleProps> =
    recordOf("head" to unsafeJso<ZRStyleProps> { fill = ZRColor("red"); lineHeight = 20.0 })
        .unsafeCast<Record<String, ZRStyleProps>>()

/** One node of `doc-example/matrix-graph-box`, placed by the local coordinates of the cell box. */
internal fun matrixNode(name: String, x: Double, y: Double, value: Double): GraphDataItem =
    GraphDataItem(
        unsafeJso<GraphNodeItemOption> {
            this.name = name
            this.x = x
            this.y = y
            this.value = OptionDataValueOrArray(arrayOf(OptionDataValue(value)))
        },
    )

/** One directed link of `doc-example/matrix-graph-box`, by index of the node it joins. */
internal fun graphEdge(source: Double, target: Double): GraphEdgeItemOption =
    unsafeJso<GraphEdgeItemOption> {
        this.source = StringOrDouble(source)
        this.target = StringOrDouble(target)
    }
