package demo.examples

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
import zrender.ZRLineTypes

/*
 * The small examples of the official documentation, ported from
 * the `doc-example/` folder of `public/examples/ts` (the `doc-example/` ids of `chart-list-data.json`).
 *
 * The manifest keeps these out of the chart families: they illustrate one option field each, so the
 * pages are intentionally short. Their ids keep the `doc-example/` prefix of the official site so
 * the "official example" link of a page points at the same page.
 *
 * The second batch of the folder lives in `DocMore.kt`: the pictorial bar tutorials, the dataZoom
 * scatter pages, the text / label pages, the animation pages and the sunburst / treemap
 * introductions.
 *
 * Still not ported: the pages that need a second renderer (`canvas-vs-svg`), an asynchronous
 * `setOption` (`tutorial-async`, `tutorial-loading`, `tutorial-dynamic-data`), the timeline and
 * media components, the map / `visualMap` combinations, and the pages built on official-site image
 * assets.
 *
 * The stored tutorials (`tutorial-styling-step*`) still use the ECharts 4 shape with a `normal`
 * state inside `itemStyle` / `label` / `labelLine`; ECharts 6 dropped that state, so the ports set
 * the same fields directly and note it per example.
 */

private val docExample = Category(Groups.DOC, "examples/DocExamples.kt")

/** The five slices the pie tutorials share. */
private fun visitSource(): Array<PieDataItem?> =
    dataItems(
        pieItem("视频广告", 235.0),
        pieItem("联盟广告", 274.0),
        pieItem("邮件营销", 310.0),
        pieItem("直接访问", 335.0),
        pieItem("搜索引擎", 400.0),
    )

/** A sunburst node with an optional colour and an optional list of children. */
private fun docSunburstNode(
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
private fun insideLabel(align: TextAlign): SunburstLabelOption = unsafeJso {
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

/** The seven slices of the `pie-label-*` pages; the `distanceToLabelLine` / `bleedMargin` pages use longer names. */
private fun pieLabelSlices(longNames: Boolean): Array<PieDataItem?> {
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
private fun pieLabelTitles(leftCaption: String, rightCaption: String): Array<TitleOption> =
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
private fun pieLabelPair(
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
private external interface SortDescriptor {
    var dimension: String
    var order: String
}

/**
 * Config of the `sort` transform: an array of descriptors.
 *
 * Upstream types `transform.config` as `unknown`; the wrapper types it as a record, so the array is
 * cast to the record type. ECharts only looks at the resulting JavaScript value.
 */
private fun sortDescriptors(vararg pairs: Pair<String, String>): Record<String, JsAny> =
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
private fun sortBarSource(): DatasetSource =
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
private fun aqiRow(
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
private val parallelBeijing: List<ParallelSeriesDataItem> =
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
private val parallelGuangzhou: List<ParallelSeriesDataItem> =
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
private val parallelShanghai: List<ParallelSeriesDataItem> =
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

val docExampleEntries: List<CatalogEntry> =
    listOf(
        docExample.chart("doc-example/polar-anticlockwise", "逆时针排列的极坐标", "Polar anti-clockwise") {
            unsafeJso<EChartsOption> {
                polar = one(unsafeJso<PolarOption> {})
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(360.0)
                            interval = AxisInterval(30.0)
                            clockwise = false
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(10.0)
                            interval = AxisInterval(2.0)
                        },
                    )
                series = emptyArray()
            }
        },
        docExample.chart("doc-example/polar-start-angle", "极坐标起始角度", "Polar start angle") {
            unsafeJso<EChartsOption> {
                polar = one(unsafeJso<PolarOption> {})
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(360.0)
                            interval = AxisInterval(30.0)
                            startAngle = 45.0
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            min = AxisMinMax(0.0)
                            max = AxisMinMax(10.0)
                            interval = AxisInterval(2.0)
                        },
                    )
                series = emptyArray()
            }
        },
        docExample.chart("doc-example/polar-end-angle", "极坐标结束角度", "Polar end angle") {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            endAngle = -180.0
                            data = strings("S1", "S2", "S3")
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> {})
                polar = one(unsafeJso<PolarOption> {})
                series =
                    one(
                        barSeries {
                            coordinateSystem = "polar"
                            polarIndex = 0.0
                            typedData = numbers(1.0, 2.0, 3.0)
                        },
                    )
            }
        },
        docExample.chart("doc-example/getting-started", "快速上手示例", "Getting started") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "ECharts Getting Started Example" })
                tooltip = one(unsafeJso<TooltipOption> {})
                legend = one(unsafeJso<LegendOption> { data = legendNames("sales") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings("Shirts", "Cardigans", "Chiffons", "Pants", "Heels", "Socks")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    one(
                        barSeries {
                            name = "sales"
                            typedData = numbers(5.0, 20.0, 36.0, 10.0, 10.0, 20.0)
                        },
                    )
            }
        },
        docExample.chart("doc-example/aria-pie", "无障碍饼图", "Aria pie") {
            unsafeJso<EChartsOption> {
                aria = unsafeJso<AriaOption> { enabled = true }
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "某站点用户访问来源"
                            left = StringOrDouble("center")
                        },
                    )
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            typedData =
                                dataItems(
                                    pieItem("直接访问", 335.0),
                                    pieItem("邮件营销", 310.0),
                                    pieItem("联盟广告", 234.0),
                                    pieItem("视频广告", 135.0),
                                    pieItem("搜索引擎", 1548.0),
                                )
                        },
                    )
            }
        },
        docExample.chart("doc-example/sunburst-color", "旭日图配色", "Sunburst colour") {
            val a =
                docSunburstNode(
                    name = "A",
                    value = 10.0,
                    children =
                        arrayOf(
                            docSunburstNode(name = "Aa", value = 3.0, color = "red"),
                            docSunburstNode(name = "Ab", value = 5.0),
                        ),
                )
            val b =
                docSunburstNode(
                    name = "B",
                    color = "red",
                    children = arrayOf(docSunburstNode(name = "Ba", value = 4.0), docSunburstNode(name = "Bb", value = 2.0)),
                )
            unsafeJso<EChartsOption> {
                series =
                    one(
                        sunburstSeries {
                            typedData = dataItems(a, b, docSunburstNode(name = "C", value = 3.0))
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#aaa") }
                            levels =
                                arrayOf(
                                    unsafeJso<SunburstSeriesLevelOption> {},
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("blue") }
                                    },
                                )
                        },
                    )
            }
        },
        docExample.chart("doc-example/sunburst-label-align", "旭日图标签对齐", "Sunburst label align") {
            val data =
                Array(8) {
                    docSunburstNode(
                        name = "left",
                        label = insideLabel(TextAlign.left),
                        children = arrayOf(docSunburstNode(name = "right", value = 1.0, label = insideLabel(TextAlign.right))),
                    )
                }
            unsafeJso<EChartsOption> {
                series =
                    one(
                        sunburstSeries {
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble("20%"), StringOrDouble("90%")))
                            // `sort: null` keeps the order of the data, which is what the original
                            // asks for; ECharts types it as a nullable sort order.
                            sort = null
                            emphasis = unsafeJso { focus = EmphasisFocuses.none }
                            typedData = dataItems(*data)
                            label = unsafeJso<SunburstLabelOption> { fontSize = StringOrDouble(14.0) }
                        },
                    )
            }
        },
        docExample.chart("doc-example/line-stack-tiled", "折线图堆叠与平铺", "Stacked and tiled lines") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "某楼盘销售情况"
                            subtext = "纯属虚构"
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend = one(unsafeJso<LegendOption> { data = legendNames("意向", "预购", "成交") })
                toolbox = toolbox(magicType = true, saveAsImage = true)
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            data = strings("周一", "周二", "周三", "周四", "周五", "周六", "周日")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        lineSeries {
                            name = "成交"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(10.0, 12.0, 21.0, 54.0, 260.0, 830.0, 710.0)
                        },
                        lineSeries {
                            name = "预购"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(30.0, 182.0, 434.0, 791.0, 390.0, 30.0, 10.0)
                        },
                        lineSeries {
                            name = "意向"
                            smooth = BooleanOrDouble(true)
                            typedData = numbers(1320.0, 1132.0, 601.0, 234.0, 120.0, 90.0, 20.0)
                        },
                    )
            }
        },
        docExample.chart("doc-example/axis-label-align-min-max", "轴首尾标签对齐", "Axis min/max label alignment") {
            // The original fills the series with `Math.random()`; a fixed sequence keeps the page
            // reproducible, the point of the example is the alignment of the two end labels.
            val values = DoubleArray(200) { index -> (index * 37 % 100).toDouble() }
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    formatter = LabelFormatter("label of {value}")
                                    showMinLabel = true
                                    showMaxLabel = true
                                    alignMinLabel = TextAlign.left
                                    alignMaxLabel = TextAlign.right
                                }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    one(
                        lineSeries {
                            typedData = numbers(*values)
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            start = 20.0
                            end = 42.0
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step0", "教程：朴素饼图", "Tutorial: plain pie") {
            unsafeJso<EChartsOption> {
                series = one(pieSeries { name = "访问来源"; radius = StringOrDoubleOrArray("55%"); typedData = visitSource() })
            }
        },
        docExample.chart("doc-example/tutorial-styling-step1", "教程：南丁格尔玫瑰图", "Tutorial: rose pie") {
            unsafeJso<EChartsOption> {
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step2", "教程：阴影", "Tutorial: shadow") {
            unsafeJso<EChartsOption> {
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            // The original writes `itemStyle.normal`; ECharts 6 has no `normal`
                            // state any more, so the shadow is set on the style itself.
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step3", "教程：暗色背景", "Tutorial: dark background") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            label = unsafeJso<LabelOption> { color = "rgba(255, 255, 255, 0.3)" }
                            labelLine = unsafeJso<PieLabelLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.3)") } }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step4", "教程：统一颜色", "Tutorial: single colour") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            label = unsafeJso<LabelOption> { color = "rgba(255, 255, 255, 0.3)" }
                            labelLine = unsafeJso<PieLabelLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.3)") } }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#c23531")
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/tutorial-styling-step5", "教程：按亮度着色", "Tutorial: lightness by value") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#2c343c")
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            min = 80.0
                            max = 600.0
                            inRange =
                                unsafeJso {
                                    colorLightness = DoubleOrDoubleArray(arrayOf(0.0, 1.0))
                                }
                        },
                    )
                series =
                    one(
                        pieSeries {
                            name = "访问来源"
                            radius = StringOrDoubleOrArray("55%")
                            roseType = RoseTypes.area
                            typedData = visitSource()
                            label = unsafeJso<LabelOption> { color = "rgba(255, 255, 255, 0.3)" }
                            labelLine = unsafeJso<PieLabelLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.3)") } }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    color = ZRColor("#c23531")
                                    shadowBlur = 200.0
                                    shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/aria-decal-simple", "无障碍花纹（简单）", "Aria decal (simple)") {
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        barSeries { typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0) },
                        barSeries { typedData = numbers(20.0, 40.0, 90.0, 40.0, 30.0, 70.0, 120.0) },
                        barSeries { typedData = numbers(140.0, 230.0, 120.0, 50.0, 30.0, 150.0, 120.0) },
                    )
                aria =
                    unsafeJso<AriaOption> {
                        enabled = true
                        decal = unsafeJso<AriaDecalOption> { show = true }
                    }
            }
        },
        docExample.chart("doc-example/aria-decal-newspaper", "无障碍花纹（报纸风格）", "Aria decal (newspaper)") {
            val blackBorder = unsafeJso<ItemStyleOption> {
                borderColor = ZRColor("black")
                borderWidth = 2.0
            }
            unsafeJso<EChartsOption> {
                color =
                    arrayOf(
                        ZRColor("white"),
                        ZRColor("black"),
                        ZRColor("white"),
                        ZRColor("white"),
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings("Q1", "Q2", "Q3", "Q4")
                            axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisLabel = unsafeJso<AxisLabelOption> { color = "black" }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("black") } }
                            axisLabel = unsafeJso<AxisLabelOption> { color = "black" }
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    lineStyle =
                                        unsafeJso<SplitLineStyleOption> {
                                            color = ZRColor("black")
                                            type = ZRLineTypes.dashed
                                        }
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "Apples"
                            typedData = numbers(108.0, 26.0, 39.0, 24.0)
                            itemStyle = blackBorder
                        },
                        barSeries {
                            name = "Oranges"
                            typedData = numbers(23.0, 40.0, 60.0, 70.0)
                            itemStyle = blackBorder
                        },
                        barSeries {
                            name = "Bananas"
                            typedData = numbers(129.0, 40.0, 40.0, 50.0)
                            itemStyle = blackBorder
                        },
                        barSeries {
                            name = "Pears"
                            typedData = numbers(40.0, 60.0, 50.0, 89.0)
                            itemStyle = blackBorder
                        },
                    )
                aria =
                    unsafeJso<AriaOption> {
                        decal =
                            unsafeJso<AriaDecalOption> {
                                show = true
                                decals =
                                    DecalList(
                                        arrayOf(
                                            unsafeJso<DecalObject> {
                                                dashArrayX = DecalDashArrayX(0.0)
                                                dashArrayY = DoubleOrDoubleArray(0.0)
                                                color = "black"
                                            },
                                            unsafeJso<DecalObject> {
                                                dashArrayX = DecalDashArrayX(0.0)
                                                dashArrayY = DoubleOrDoubleArray(0.0)
                                                color = "black"
                                            },
                                            unsafeJso<DecalObject> {
                                                dashArrayX =
                                                    DecalDashArrayX(
                                                        arrayOf(DoubleOrDoubleArray(1.0), DoubleOrDoubleArray(0.0)),
                                                    )
                                                dashArrayY = DoubleOrDoubleArray(arrayOf(2.0, 5.0))
                                                symbolSize = 1.0
                                                rotation = kotlin.math.PI / 6
                                                color = "black"
                                            },
                                            unsafeJso<DecalObject> {
                                                symbol = StringOrStringArray("circle")
                                                dashArrayX =
                                                    DecalDashArrayX(
                                                        arrayOf(
                                                            DoubleOrDoubleArray(arrayOf(8.0, 8.0)),
                                                            DoubleOrDoubleArray(arrayOf(0.0, 8.0, 8.0, 0.0)),
                                                        ),
                                                    )
                                                dashArrayY = DoubleOrDoubleArray(arrayOf(6.0, 0.0))
                                                symbolSize = 0.8
                                                color = "black"
                                            },
                                        ),
                                    )
                            }
                    }
            }
        },
        docExample.chart("doc-example/text-block-fragment", "文本块与文本片段", "Text Block and Fragment") {
            unsafeJso<EChartsOption> {
                xAxis = hiddenXAxis()
                yAxis = hiddenYAxis()
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(1.0)
                            typedData =
                                dataItems(
                                    ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(0.0), OptionDataValue(0.0))),
                                )
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    formatter =
                                        LabelFormatter(
                                            "The whole box is a {term|Text Block}, with\n" +
                                                "red border and grey background.\n" +
                                                "{fragment1|A Text Fragment} {fragment2|Another Text Fragment}\n" +
                                                "Text fragments can be customized.",
                                        )
                                    backgroundColor = ZRColor("#eee")
                                    borderColor = "rgb(199,86,83)"
                                    borderWidth = 2.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    padding = DoubleOrDoubleArray(10.0)
                                    color = "#000"
                                    fontSize = StringOrDouble(14.0)
                                    shadowBlur = 3.0
                                    shadowColor = ZRColor("#888")
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 3.0
                                    lineHeight = 30.0
                                    rich =
                                        recordOf(
                                            "term" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(18.0)
                                                    color = "rgb(199,86,83)"
                                                },
                                            "fragment1" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#000")
                                                    color = "yellow"
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                            "fragment2" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#339911")
                                                    color = "#fff"
                                                    borderRadius = DoubleOrDoubleArray(15.0)
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                        ).unsafeCast<Record<String, TextStyleOption>>()
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/text-options", "文本样式选项", "Text Options") {
            unsafeJso<EChartsOption> {
                xAxis = hiddenXAxis()
                yAxis = hiddenYAxis()
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(1.0)
                            typedData =
                                dataItems(
                                    ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(0.0), OptionDataValue(0.0))),
                                )
                            // The original puts the label on the single data item; a series-level
                            // label renders the same text for one point.
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    formatter =
                                        LabelFormatter(
                                            "Plain text\n" +
                                                "{textBorder|textBorderColor + textBorderWidth}\n" +
                                                "{textShadow|textShadowColor + textShadowBlur + textShadowOffsetX + textShadowOffsetY}\n" +
                                                "{bg|backgroundColor + borderRadius + padding}\n" +
                                                "{border|borderColor + borderWidth + borderRadius + padding}\n" +
                                                "{shadow|shadowColor + shadowBlur + shadowOffsetX + shadowOffsetY}",
                                        )
                                    backgroundColor = ZRColor("#eee")
                                    borderColor = "#333"
                                    borderWidth = 2.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    padding = DoubleOrDoubleArray(10.0)
                                    color = "#000"
                                    fontSize = StringOrDouble(14.0)
                                    shadowBlur = 3.0
                                    shadowColor = ZRColor("#888")
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 3.0
                                    lineHeight = 30.0
                                    rich =
                                        recordOf(
                                            "textBorder" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(20.0)
                                                    textBorderColor = "#000"
                                                    textBorderWidth = 3.0
                                                    color = "#fff"
                                                },
                                            "textShadow" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(16.0)
                                                    textShadowBlur = 5.0
                                                    textShadowColor = "#000"
                                                    textShadowOffsetX = 3.0
                                                    textShadowOffsetY = 3.0
                                                    color = "#fff"
                                                },
                                            "bg" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#339911")
                                                    color = "#fff"
                                                    borderRadius = DoubleOrDoubleArray(15.0)
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                            "border" to
                                                unsafeJso<TextStyleOption> {
                                                    color = "#000"
                                                    borderColor = "#449911"
                                                    borderWidth = 1.0
                                                    borderRadius = DoubleOrDoubleArray(3.0)
                                                    padding = DoubleOrDoubleArray(5.0)
                                                },
                                            "shadow" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#992233")
                                                    padding = DoubleOrDoubleArray(5.0)
                                                    color = "#fff"
                                                    shadowBlur = 5.0
                                                    shadowColor = ZRColor("#336699")
                                                    shadowOffsetX = 6.0
                                                    shadowOffsetY = 6.0
                                                },
                                        ).unsafeCast<Record<String, TextStyleOption>>()
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/dataset-encode-simple0", "数据集编码（简单）", "Dataset encode (simple)") {
            unsafeJso<EChartsOption> {
                dataset =
                    one(
                        unsafeJso<DatasetOption> {
                            source =
                                datasetArrayRows(
                                    arrayOf(
                                        row("score", "amount", "product"),
                                        row(89.3, 58212.0, "Matcha Latte"),
                                        row(57.1, 78254.0, "Milk Tea"),
                                        row(74.4, 41032.0, "Cheese Cocoa"),
                                        row(50.1, 12755.0, "Cheese Brownie"),
                                        row(89.7, 20145.0, "Matcha Cocoa"),
                                        row(68.1, 79146.0, "Tea"),
                                        row(19.6, 91852.0, "Orange Juice"),
                                        row(10.6, 101852.0, "Lemon Juice"),
                                        row(32.7, 20112.0, "Walnut Brownie"),
                                    ).unsafeCast<Array<Array<OptionDataValue>>>(),
                                )
                        },
                    )
                grid = one(unsafeJso<GridOption> { containLabel = true })
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.category })
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            // The dataset columns drive both axes; nothing is encoded on the data.
                            encode =
                                unsafeJso<OptionEncode> {
                                    this.x = OptionEncodeValue(DimensionLoose("amount"))
                                    this.y = OptionEncodeValue(DimensionLoose("product"))
                                }
                        },
                    )
            }
        },
        docExample.chart("doc-example/scatter-tutorial-dataZoom-1", "散点图与缩放（一）", "Scatter with dataZoom (1)") {
            val values =
                arrayOf(
                    doubleArrayOf(14.616, 7.241, 0.896),
                    doubleArrayOf(3.958, 5.701, 0.955),
                    doubleArrayOf(2.768, 8.971, 0.669),
                    doubleArrayOf(9.051, 9.710, 0.171),
                    doubleArrayOf(14.046, 4.182, 0.536),
                    doubleArrayOf(12.295, 1.429, 0.962),
                    doubleArrayOf(4.417, 8.167, 0.113),
                    doubleArrayOf(0.492, 4.771, 0.785),
                    doubleArrayOf(7.632, 2.605, 0.645),
                    doubleArrayOf(14.242, 5.042, 0.368),
                )
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            start = 1.0
                            end = 35.0
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            name = "scatter"
                            itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
                            symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) * 40) }
                            typedData = points(*values)
                        },
                    )
            }
        },
        docExample.chart("doc-example/pie-label-margin", "饼图标签外边距", "Pie label margin") {
            unsafeJso<EChartsOption> {
                title = pieLabelTitles("margin: \"25%\"", "margin: 10")
                series =
                    pieLabelPair(
                        pieLabelSlices(false),
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.edge
                            margin = StringOrDouble("25%")
                        },
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.edge
                            margin = StringOrDouble(10.0)
                        },
                    )
            }
        },
        docExample.chart("doc-example/pie-label-distanceToLabelLine", "饼图标签与引导线距离", "Pie label distance to label line") {
            unsafeJso<EChartsOption> {
                title = pieLabelTitles("distanceToLabelLine: 5", "distanceToLabelLine: 20")
                series =
                    pieLabelPair(
                        pieLabelSlices(true),
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.labelLine
                            distanceToLabelLine = 5.0
                        },
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.labelLine
                            distanceToLabelLine = 20.0
                        },
                    )
            }
        },
        docExample.chart("doc-example/pie-label-bleedMargin", "饼图标签出血边距", "Pie label bleed margin") {
            unsafeJso<EChartsOption> {
                title = pieLabelTitles("bleedMargin: 50", "bleedMargin: 10")
                series =
                    pieLabelPair(
                        pieLabelSlices(true),
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.labelLine
                            bleedMargin = 50.0
                        },
                        unsafeJso<PieLabelOption> {
                            position = LabelPosition("outer")
                            alignTo = PieLabelAlignTos.labelLine
                            bleedMargin = 10.0
                        },
                    )
            }
        },
        docExample.chart("doc-example/radar", "雷达图的坐标轴样式", "Radar") {
            unsafeJso<EChartsOption> {
                radar =
                    one(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    unsafeJso<RadarIndicator> { name = "指标一" },
                                    unsafeJso<RadarIndicator> { name = "指标二" },
                                    unsafeJso<RadarIndicator> { name = "指标三" },
                                    unsafeJso<RadarIndicator> { name = "指标四" },
                                    unsafeJso<RadarIndicator> { name = "指标五" },
                                )
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                            radius = StringOrDouble("80%")
                            startAngle = 90.0
                            splitNumber = 4.0
                            shape = RadarShapes.circle
                            axisName =
                                unsafeJso<LabelOption> {
                                    formatter = LabelFormatter("【{value}】")
                                    color = "#72ACD1"
                                }
                            splitArea =
                                unsafeJso<SplitAreaOption> {
                                    areaStyle =
                                        unsafeJso<SplitAreaStyleOption> {
                                            typedColor =
                                                arrayOf(
                                                    ZRColor("#B8D3E4"),
                                                    ZRColor("#96C5E3"),
                                                    ZRColor("#7DB5DA"),
                                                    ZRColor("#72ACD1"),
                                                )
                                        }
                                }
                            axisTick =
                                unsafeJso<AxisTickOption> {
                                    show = BooleanOrString(true)
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.8)") }
                                }
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    show = true
                                    color = "white"
                                }
                            axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.4)") } }
                            splitLine = unsafeJso<SplitLineOption> { lineStyle = unsafeJso<SplitLineStyleOption> { color = ZRColor("rgba(255, 255, 255, 0.4)") } }
                        },
                    )
            }
        },
        docExample.interactive("doc-example/scatter-jitter-avoidOverlap", "散点抖动（避免重叠）", "Scatter with Jittering") { chart ->
            // The jitter is expressed in pixels, so it depends on the width of the plot area.
            val plotWidth = chart.getWidth() - 80.0 - 50.0
            // The original randomizes the third value; a fixed sequence keeps the page reproducible.
            val values =
                Array(7 * 30) { index ->
                    val day = index / 30
                    val position = index % 30
                    doubleArrayOf(day.toDouble(), kotlin.math.tan(position.toDouble()) / 2 + 7, (index % 100).toDouble() / 100.0)
                }
            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "Scatter with Jittering" })
                    grid =
                        one(
                            unsafeJso<GridOption> {
                                left = StringOrDouble(80.0)
                                right = StringOrDouble(50.0)
                            },
                        )
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.category
                                jitter = plotWidth / 7 * 0.8
                                jitterOverlap = false
                                data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                max = AxisMinMax(10.0)
                                min = AxisMinMax(0.0)
                            },
                        )
                    series =
                        one(
                            scatterSeries {
                                name = "Sleeping Hours"
                                colorBy = "data"
                                typedData = points(*values)
                            },
                        )
                },
            )
        },
        docExample.chart("doc-example/parallel-all", "平行坐标（多指标）", "Parallel (all)") {
            unsafeJso<EChartsOption> {
                color = arrayOf(ZRColor("#c23531"), ZRColor("#91c7ae"), ZRColor("#dd8668"))
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            top = StringOrDouble(10.0)
                            data = legendNames("北京", "上海", "广州")
                            itemGap = 20.0
                        },
                    )
                parallelAxis =
                    arrayOf(
                        unsafeJso<ParallelAxisOption> {
                            dim = IndexOrIndexArray(0.0)
                            name = "日期"
                            inverse = true
                            max = AxisMinMax(31.0)
                            nameLocation = AxisNameLocations.start
                        },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(1.0); name = "AQI" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(2.0); name = "PM2.5" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(3.0); name = "PM10" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(4.0); name = " CO" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(5.0); name = "NO2" },
                        unsafeJso<ParallelAxisOption> { dim = IndexOrIndexArray(6.0); name = "SO2" },
                        unsafeJso<ParallelAxisOption> {
                            dim = IndexOrIndexArray(7.0)
                            name = "等级"
                            type = AxisTypes.category
                            data = strings("优", "良", "轻度污染", "中度污染", "重度污染", "严重污染")
                        },
                    )
                parallel =
                    one(
                        unsafeJso<ParallelOption> {
                            left = StringOrDouble("5%")
                            right = StringOrDouble("13%")
                            bottom = StringOrDouble("10%")
                            top = StringOrDouble("20%")
                            parallelAxisDefault =
                                unsafeJso<ParallelAxisOption> {
                                    type = AxisTypes.value
                                    name = "AQI指数"
                                    nameLocation = AxisNameLocations.end
                                    nameGap = 20.0
                                    nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(12.0) }
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        parallelSeries {
                            name = "北京"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = dataItems(*parallelBeijing.toTypedArray())
                        },
                        parallelSeries {
                            name = "上海"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = dataItems(*parallelShanghai.toTypedArray())
                        },
                        parallelSeries {
                            name = "广州"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = dataItems(*parallelGuangzhou.toTypedArray())
                        },
                    )
            }
        },
        docExample.chart("doc-example/data-transform-multiple-sort-bar", "数据转换：多重排序", "Data transform: multiple sort") {
            unsafeJso<EChartsOption> {
                dataset =
                    arrayOf(
                        unsafeJso<DatasetOption> {
                            dimensions =
                                arrayOf(
                                    DimensionDefinitionLoose("name"),
                                    DimensionDefinitionLoose("age"),
                                    DimensionDefinitionLoose("profession"),
                                    DimensionDefinitionLoose("score"),
                                    DimensionDefinitionLoose("date"),
                                )
                            source = sortBarSource()
                        },
                        unsafeJso<DatasetOption> {
                            transform =
                                arrayOf(
                                    unsafeJso<DataTransformOption> {
                                        type = "sort"
                                        config = sortDescriptors("profession" to "desc", "score" to "desc")
                                    },
                                )
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    interval = AxisInterval(0.0)
                                    rotate = 30.0
                                }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    one(
                        barSeries {
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    rotate = 90.0
                                    position = LabelPosition("insideBottom")
                                    align = TextAlign.left
                                    verticalAlign = VerticalAlign.middle
                                }
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    // The original colours the bars by the profession column.
                                    color =
                                        js(
                                            "(params) => {" +
                                                " const colors = { Engineer: '#5470c6', Teacher: '#91cc75', Musician: '#fac858' };" +
                                                " return colors[params.data[2]];" +
                                                " }",
                                        )
                                            .unsafeCast<ZRColor>()
                                }
                            encode =
                                unsafeJso<OptionEncode> {
                                    x = OptionEncodeValue(DimensionLoose("name"))
                                    y = OptionEncodeValue(DimensionLoose("score"))
                                    label = OptionEncodeValue(arrayOf(DimensionLoose("profession")))
                                }
                            datasetIndex = 1.0
                        },
                    )
            }
        },
    )
