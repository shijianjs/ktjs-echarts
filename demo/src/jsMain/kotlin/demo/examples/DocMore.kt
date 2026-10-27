package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.round
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ElementTextConfig
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * The second batch of the `doc-example/` pages of `public/examples/ts`.
 *
 * The first batch lives in `DocExamples.kt`; this file keeps the pages that were still missing: the
 * pictorial bar tutorials, the three dataZoom scatter pages, the text and label pages, the two
 * animation pages and the sunburst / treemap introductions.
 *
 * Ported as in the original:
 *
 * - the descriptions of `pictorialBar-symbolSize` are placed through `convertToPixel` and re-placed on
 *   the `updated` event; the relayout uses a silent `setOption`, so the handler cannot retrigger
 *   itself (ECharts does not emit `updated` for a silent update);
 * - the `path://` symbols of the pictorial bar pages are copied verbatim.
 *
 * Changed on purpose:
 *
 * - `Math.random()` (zooming scatter, the custom-series transition and the value animation) is
 *   replaced by [Lcg]: the values still change on every tick, but the sequence is reproducible;
 * - the ECharts 4 `itemStyle.normal` wrappers are dropped (ECharts 6 has no `normal` state) and the
 *   same fields are set directly; `axisLabel.textStyle` of
 *   `pictorialBar-symbolBoundingDataArray` is flattened onto the label, where ECharts 6 reads text
 *   styles from;
 * - `label-position` is driven by a configuration panel on the official site; the demo has no panel,
 *   so the page shows the panel's defaults (`top` / `center` / `middle` / `5`);
 * - the tooltip of `treemap-borderColor` is plain text (`{b}: {c} KB`) instead of the HTML one of the
 *   original, per the UI rules of this repository.
 */

private val docMore = Category(Groups.DOC, "examples/DocMore.kt")

/** `value` rounded to three decimals, the precision of the `random(max)` helper upstream. */
private fun round3(value: Double): Double = round(value * 1000.0) / 1000.0

/** The walking figure of `pictorialBar-position`. */
private const val WALK_SYMBOL =
    "path://M29.902,23.275c1.86,0,3.368-1.506,3.368-3.365c0-1.859-1.508-3.365-3.368-3.365 " +
        "c-1.857,0-3.365,1.506-3.365,3.365C26.537,21.769,28.045,23.275,29.902,23.275z " +
        "M36.867,30.74c-1.666-0.467-3.799-1.6-4.732-4.199 c-0.932-2.6-3.131-2.998-4.797-2.998s-7.098,3.894-7.098,3.894c-1.133,1.001-2.1,6.502-0.967,6.769 " +
        "c1.133,0.269,1.266-1.533,1.934-3.599c0.666-2.065,3.797-3.466,3.797-3.466s0.201,2.467-0.398,3.866 " +
        "c-0.599,1.399-1.133,2.866-1.467,6.198s-1.6,3.665-3.799,6.266c-2.199,2.598-0.6,3.797,0.398,3.664 " +
        "c1.002-0.133,5.865-5.598,6.398-6.998c0.533-1.397,0.668-3.732,0.668-3.732s0,0,2.199,1.867c2.199,1.865,2.332,4.6,2.998,7.73 " +
        "s2.332,0.934,2.332-0.467c0-1.401,0.269-5.465-1-7.064c-1.265-1.6-3.73-3.465-3.73-5.265s1.199-3.732,1.199-3.732 " +
        "c0.332,1.667,3.335,3.065,5.599,3.399C38.668,33.206,38.533,31.207,36.867,30.74z"

/** The rocket of `pictorialBar-symbolSize`. */
private const val ROCKET_SYMBOL =
    "path://M-244.396,44.399c0,0,0.47-2.931-2.427-6.512c2.819-8.221,3.21-15.709,3.21-15.709s5.795,1.383,5.795,7.325C-237.818,39.679-244.396,44.399-244.396,44.399z " +
        "M-260.371,40.827c0,0-3.881-12.946-3.881-18.319c0-2.416,0.262-4.566,0.669-6.517h17.684c0.411,1.952,0.675,4.104,0.675,6.519c0,5.291-3.87,18.317-3.87,18.317H-260.371z " +
        "M-254.745,18.951c-1.99,0-3.603,1.676-3.603,3.744c0,2.068,1.612,3.744,3.603,3.744c1.988,0,3.602-1.676,3.602-3.744S-252.757,18.951-254.745,18.951z " +
        "M-255.521,2.228v-5.098h1.402v4.969c1.603,1.213,5.941,5.069,7.901,12.5h-17.05C-261.373,7.373-257.245,3.558-255.521,2.228zM-265.07,44.399c0,0-6.577-4.721-6.577-14.896c0-5.942,5.794-7.325,5.794-7.325s0.393,7.488,3.211,15.708C-265.539,41.469-265.07,44.399-265.07,44.399z " +
        "M-252.36,45.15l-1.176-1.22L-254.789,48l-1.487-4.069l-1.019,2.116l-1.488-3.826h8.067L-252.36,45.15z"

/** The star of `pictorialBar-symbolBoundingDataArray`. */
private const val STAR_SYMBOL =
    "path://m15.5,19c-0.082,0 -0.164,-0.02 -0.239,-0.061l-5.261,-2.869l-5.261,2.869c-0.168,0.092 -0.373,0.079 -0.529,-0.032s-0.235,-0.301 -0.203,-0.49l0.958,-5.746l-3.818,-3.818c-0.132,-0.132 -0.18,-0.328 -0.123,-0.506s0.209,-0.31 0.394,-0.341l5.749,-0.958l2.386,-4.772c0.085,-0.169 0.258,-0.276 0.447,-0.276s0.363,0.107 0.447,0.276l2.386,4.772l5.749,0.958c0.185,0.031 0.337,0.162 0.394,0.341s0.01,0.374 -0.123,0.506l-3.818,3.818l0.958,5.746c0.031,0.189 -0.048,0.379 -0.203,0.49c-0.086,0.061 -0.188,0.093 -0.29,0.093z"

/** The paw of `pictorialBar-clip`. */
private const val PAW_SYMBOL =
    "path://M36.7,102.84c-1.17,2.54-2.99,4.98-3.39,7.63c-1.51,9.89-3.31,19.58-1.93,29.95 c0.95,7.15-2.91,14.82-3.57,22.35c-0.64,7.36-0.2,14.86,0.35,22.25c0.12,1.68,2.66,3.17,4.67,5.4c-0.6,0.82-1.5,2.22-2.58,3.48 c-0.96,1.12-1.96,2.35-3.21,3.04c-1.71,0.95-3.71,2.03-5.51,1.9c-1.18-0.08-3.04-2.13-3.16-3.43c-0.44-4.72,0-9.52-0.41-14.25 c-0.94-10.88-2.32-21.72-3.24-32.61c-0.49-5.84-1.63-12.01-0.35-17.54c3.39-14.56,2.8-28.84,0.36-43.4 c-2.71-16.16-1.06-32.4,0.54-48.59c0.91-9.22,4.62-17.36,8.53-25.57c1.32-2.77,1.88-6.84,0.87-9.62C21.89-3.77,18.09-11,14.7-18.38 c-0.56,0.1-1.13,0.21-1.69,0.31C10.17-11.52,6.29-5.2,4.71,1.65C2.05,13.21-4.42,22.3-11.43,31.28c-1.32,1.69-2.51,3.5-3.98,5.04 c-4.85,5.08-3.25,10.98-2.32,16.82c0.25,1.53,0.52,3.06,0.77,4.59c-0.53,0.22-1.07,0.43-1.6,0.65c-1.07-2.09-2.14-4.19-3.28-6.44 c-6.39,2.91-2.67,9.6-5.23,15.16c-1.61-3.31-2.77-5.68-3.93-8.06c0-0.33,0-0.67,0-1c6.96-16.08,14.63-31.9,20.68-48.31 C-5.24-4.07-2.03-18.55,2-32.73c0.36-1.27,0.75-2.53,0.98-3.82c1.36-7.75,4.19-10.23,11.88-10.38c1.76-0.04,3.52-0.21,5.76-0.35 c-0.55-3.95-1.21-7.3-1.45-10.68c-0.61-8.67,0.77-16.69,7.39-23.19c2.18-2.14,4.27-4.82,5.25-7.65c2.39-6.88,11.66-9,16.94-8.12 c5.92,0.99,12.15,7.93,12.16,14.12c0.01,9.89-5.19,17.26-12.24,23.68c-2.17,1.97-5.35,4.77-5.17,6.94c0.31,3.78,4.15,5.66,8.08,6.04 c1.82,0.18,3.7,0.37,5.49,0.1c5.62-0.85,8.8,2.17,10.85,6.73C73.38-27.19,78.46-14.9,84.2-2.91c1.52,3.17,4.52,5.91,7.41,8.09 c7.64,5.77,15.57,11.16,23.45,16.61c2.28,1.58,4.64,3.23,7.21,4.14c5.18,1.84,8.09,5.63,9.82,10.46c0.45,1.24,0.19,3.71-0.6,4.18 c-1.06,0.63-3.15,0.27-4.44-0.38c-7.05-3.54-12.84-8.88-19.14-13.5c-3.5-2.57-7.9-4-12.03-5.6c-9.44-3.66-17.73-8.42-22.5-18.09 c-2.43-4.94-6.09-9.27-9.69-14.61c-1.2,10.98-4.46,20.65,1.14,31.19c6.62,12.47,5.89,26.25,1.21,39.49 c-2.52,7.11-6.5,13.74-8.67,20.94c-1.91,6.33-2.2,13.15-3.23,19.75c-0.72,4.63-0.84,9.48-2.36,13.84 c-2.49,7.16-6.67,13.83-5.84,21.82c0.42,4.02,1.29,7.99,2.1,12.8c-3.74-0.49-7.47-0.4-10.67-1.66c-1.33-0.53-2.43-4.11-2.07-6.01 c1.86-9.94,3.89-19.69,0.07-29.74C34.55,108.63,36.19,105.52,36.7,102.84c1.25-8.45,2.51-16.89,3.71-24.9 c-0.83-0.58-0.85-0.59-0.87-0.61c-0.03,0.16-0.07,0.32-0.09,0.48C38.53,86.15,37.62,94.5,36.7,102.84z"

/** One pictorial bar item: the value plus the symbol options of that bar. */
private fun pictorialItem(
    value: Double,
    block: PictorialBarDataItemOption.() -> Unit = {},
): PictorialBarDataItem {
    val item = unsafeJso<PictorialBarDataItemOption> { this.value = OptionDataValue(value) }
    item.block()
    return PictorialBarDataItem(item)
}

/** A sunburst node with an optional value and an optional list of children. */
private fun sunburstNode(
    name: String,
    value: Double? = null,
    children: Array<SunburstSeriesNodeItemOption>? = null,
): SunburstSeriesNodeItemOption =
    unsafeJso {
        this.name = OptionId(name)
        if (value != null) this.value = OptionDataValue(value)
        if (children != null) this.children = children
    }

/** The ten points of the `scatter-tutorial-dataZoom-*` pages. */
private val tutorialScatterValues =
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

/** One scatter series of the tutorial pages; the symbol size comes from the third value. */
private fun tutorialScatter(name: String): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        // The original nests `opacity` under `itemStyle.normal`; ECharts 6 dropped that state.
        itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
        symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) * 40.0) }
        typedData = points(*tutorialScatterValues)
    }

/** A scatter series of `scatter-dataZoom-all`; the symbol size comes from the third value. */
private fun zoomScatter(name: String, data: Array<DoubleArray>): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
        symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) * 40.0) }
        typedData = points(*data)
    }

/** One zoom of `scatter-dataZoom-all`: either on the x axis or on the y axis. */
private fun zoomWindow(
    type: DataZoomType,
    start: Double,
    end: Double,
    xAxisIndex: IndexOrIndexArray? = null,
    yAxisIndex: IndexOrIndexArray? = null,
    left: StringOrDouble? = null,
): DataZoomOption =
    unsafeJso {
        this.type = type
        show = true
        if (xAxisIndex != null) this.xAxisIndex = xAxisIndex
        if (yAxisIndex != null) this.yAxisIndex = yAxisIndex
        this.start = start
        this.end = end
        if (left != null) this.left = left
    }

/** The four descriptions of `pictorialBar-symbolSize`, one per bar. */
private val symbolSizeDescriptions =
    arrayOf(
        arrayOf(
            "symbolSize: [\"50%\", \"100%\"]",
            "That is:",
            "50% of reference bar width",
            "100% of reference bar height.",
        ),
        arrayOf(
            "symbolSize: [\"50%\", 120]",
            "That is:",
            "50% of reference bar width,",
            "fixed height 120.",
        ),
        arrayOf("symbolSize: 50", "equivalent to [50, 50]"),
        arrayOf(
            "symbolSize: \"70%\"",
            "equivalent to [\"70%\", \"70%\"]",
            "",
            "When symbolRepeat used, both",
            "width and height percentage are",
            "calculated based on the width of",
            "reference bar.",
        ),
    )

/**
 * Draws the four descriptions of `pictorialBar-symbolSize` under the bars.
 *
 * `setOption` runs with `silent = true`: the handler is bound to `updated`, and a silent update does
 * not emit that event, so the relayout cannot trigger itself.
 */
private fun placeSymbolSizeDescriptions(chart: EChartsInstance) {
    val graphic =
        Array(symbolSizeDescriptions.size) { index ->
            val pixel =
                chart
                    .convertToPixel(
                        ModelFinder("grid"),
                        OptionDataValueOrArray(
                            arrayOf(OptionDataValue(index.toDouble()), OptionDataValue(70.0)),
                        ),
                    ).unsafeCast<Array<Double>>()
            GraphicComponentLooseOption(
                unsafeJso<GraphicElement> {
                    type = "group"
                    position = pixel
                    children =
                        arrayOf(
                            unsafeJso<GraphicElement> {
                                type = "text"
                                left = StringOrDouble("center")
                                top = StringOrDouble("middle")
                                z = 101.0
                                style =
                                    unsafeJso<ZRStyleProps> {
                                        text = symbolSizeDescriptions[index].joinToString("\n")
                                        fill = ZRColor("#bb0004")
                                    }
                            },
                        )
                },
            )
        }
    chart.setOption(
        unsafeJso<EChartsOption> { this.graphic = graphic },
        unsafeJso<SetOptionOpts> { silent = true },
    )
}

/** The stacked colour tags of `text-fregment-align`. */
private fun alignTag(align: TextAlign?, rounded: Boolean, width: Double? = null): TextStyleOption =
    unsafeJso {
        backgroundColor = ZRColor(if (rounded) "#339911" else "#7066FF")
        color = "#fff"
        if (rounded) borderRadius = DoubleOrDoubleArray(15.0)
        padding = DoubleOrDoubleArray(5.0)
        if (align != null) this.align = align
        if (width != null) this.width = StringOrDouble(width)
    }

/** The three vertical-alignment tags of `text-fregment-align`. */
private fun verticalTag(align: VerticalAlign): TextStyleOption =
    unsafeJso {
        verticalAlign = align
        backgroundColor = ZRColor("#991122")
        color = "#fff"
        padding = DoubleOrDoubleArray(arrayOf(1.0, 2.0, 1.0, 2.0))
    }

/** One rectangle of `custom-transition-simple`, with or without a transition. */
private fun transitionRect(
    x: Double,
    y: Double,
    width: Double,
    height: Double,
    radius: Double,
    fill: String,
    lineWidth: Double,
    text: String,
    transition: StringOrStringArray,
): CustomPathOption =
    unsafeJso {
        type = "rect"
        this.x = x
        this.y = y
        shape =
            unsafeJso<ZRShapeProps> {
                this.x = -width / 2.0
                this.y = -height / 2.0
                this.width = width
                this.height = height
                r = radius
            }
        style =
            unsafeJso<ZRStyleProps> {
                this.fill = ZRColor(fill)
                stroke = ZRColor("#000")
                this.lineWidth = lineWidth
            }
        textContent =
            CustomTextContentOption(
                unsafeJso<CustomTextOption> {
                    style =
                        unsafeJso<ZRStyleProps> {
                            this.text = text
                            fontSize = StringOrDouble(20.0)
                        }
                },
            )
        textConfig = unsafeJso<ElementTextConfig> { position = StringOrDoubleOrArray("bottom") }
        silent = true
        this.transition = transition
    }

/** One background / filled star series of `pictorialBar-symbolBoundingDataArray`. */
private fun starSeries(
    name: String,
    values: DoubleArray,
    block: PictorialBarSeriesOption.() -> Unit,
): PictorialBarSeriesOption =
    pictorialBarSeries {
        this.name = name
        symbol = STAR_SYMBOL
        symbolSize = StringOrDoubleOrArray(arrayOf(StringOrDouble("80%"), StringOrDouble("80%")))
        typedData = numbers(*values)
    }.apply(block)

val docMoreEntries: List<CatalogEntry> =
    listOf(
        docMore.chart(
            "doc-example/pictorialBar-repeat",
            "象形柱图：重复填充",
            "Pictorial bar: repeated symbols",
        ) {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { data = legendNames("pictorialBar", "bar") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings("symbolRepeat: true", "symbolRepeat: false")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { splitLine = unsafeJso<SplitLineOption> { show = false } })
                animationEasing = AnimationEasings.elasticOut
                series =
                    arrayOf<SeriesOption>(
                        pictorialBarSeries {
                            symbol = "circle"
                            typedData =
                                dataItems(
                                    pictorialItem(29000.0) {
                                        symbolRepeat = BooleanOrDoubleOrString(true)
                                        symbolSize =
                                            StringOrDoubleOrArray(
                                                arrayOf(StringOrDouble(50.0), StringOrDouble(50.0)),
                                            )
                                    },
                                    pictorialItem(29000.0) {
                                        symbolSize =
                                            StringOrDoubleOrArray(
                                                arrayOf(StringOrDouble(50.0), StringOrDouble("100%")),
                                            )
                                    },
                                )
                        },
                    )
            }
        },
        docMore.chart(
            "doc-example/pictorialBar-position",
            "象形柱图：符号位置",
            "Pictorial bar: symbol position",
        ) {
            unsafeJso<EChartsOption> {
                color = arrayOf(ZRColor("#bb0004"), ZRColor("#FFD48A"))
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("pictorial element", "reference bar")
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data =
                                strings(
                                    "symbolPosition: \"start\"\nsymbolOffset: [0, 0]",
                                    "symbolPosition: \"end\"\nsymbolOffset: [0, 0]",
                                    "symbolPosition: \"center\"\nsymbolOffset: [0, 0]",
                                    "symbolPosition: \"end\"\nsymbolOffset: ['-100%', -120]",
                                )
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { interval = AxisInterval(0.0) }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            max = AxisMinMax(80.0)
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        pictorialBarSeries {
                            name = "pictorial element"
                            symbol = WALK_SYMBOL
                            symbolSize =
                                StringOrDoubleOrArray(
                                    arrayOf(StringOrDouble(50.0), StringOrDouble(120.0)),
                                )
                            z = 10.0
                            typedData =
                                dataItems(
                                    pictorialItem(60.0) { symbolPosition = PictorialBarSymbolPositions.start },
                                    pictorialItem(60.0) { symbolPosition = PictorialBarSymbolPositions.end },
                                    pictorialItem(60.0) { symbolPosition = PictorialBarSymbolPositions.center },
                                    pictorialItem(60.0) {
                                        symbolPosition = PictorialBarSymbolPositions.end
                                        symbolOffset =
                                            StringOrDoubleOrArray(
                                                arrayOf(StringOrDouble(-80.0), StringOrDouble("-100%")),
                                            )
                                    },
                                )
                        },
                        barSeries {
                            name = "reference bar"
                            barGap = StringOrDouble("-100%")
                            typedData = numbers(60.0, 60.0, 60.0, 60.0)
                        },
                    )
            }
        },
        docMore.interactive(
            "doc-example/pictorialBar-symbolSize",
            "象形柱图：符号尺寸",
            "Pictorial bar: symbol size",
        ) { chart, holder ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    color = arrayOf(ZRColor("#bb0004"), ZRColor("#FFD48A"))
                    legend =
                        one(
                            unsafeJso<LegendOption> {
                                data = legendNames("pictorial element", "reference bar")
                            },
                        )
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                data = strings("a", "b", "c", "d")
                                axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                                axisLabel = unsafeJso<AxisLabelOption> { interval = AxisInterval(0.0) }
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                max = AxisMinMax(80.0)
                                splitLine = unsafeJso<SplitLineOption> { show = false }
                            },
                        )
                    grid =
                        one(
                            unsafeJso<GridOption> {
                                left = StringOrDouble(40.0)
                                right = StringOrDouble(10.0)
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            pictorialBarSeries {
                                name = "pictorial element"
                                symbol = ROCKET_SYMBOL
                                z = 10.0
                                typedData =
                                    dataItems(
                                        pictorialItem(60.0) {
                                            symbolSize =
                                                StringOrDoubleOrArray(
                                                    arrayOf(StringOrDouble("50%"), StringOrDouble("100%")),
                                                )
                                        },
                                        pictorialItem(60.0) {
                                            symbolSize =
                                                StringOrDoubleOrArray(
                                                    arrayOf(StringOrDouble("50%"), StringOrDouble(120.0)),
                                                )
                                        },
                                        pictorialItem(60.0) { symbolSize = StringOrDoubleOrArray(50.0) },
                                        pictorialItem(60.0) {
                                            symbolRepeat = BooleanOrDoubleOrString(true)
                                            symbolSize = StringOrDoubleOrArray("70%")
                                        },
                                    )
                            },
                            barSeries {
                                name = "reference bar"
                                barGap = StringOrDouble("-100%")
                                typedData = numbers(60.0, 60.0, 60.0, 60.0)
                            },
                        )
                },
            )
            holder.setTimeout({ placeSymbolSizeDescriptions(chart) }, 100)
            chart.on("updated") { _ -> placeSymbolSizeDescriptions(chart) }
        },
        docMore.chart(
            "doc-example/pictorialBar-symbolBoundingDataArray",
            "象形柱图：基准值数组",
            "Pictorial bar: symbolBoundingData array",
        ) {
            val categories = arrayOf("DTA", "Fin", "Tool", "VTG", "CAAT", "UYL", "TC")
            val values = doubleArrayOf(-12.0, -60.0, 34.0, 23.0, -21.0, 0.0, -25.0)
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                        },
                    )
                grid = one(unsafeJso<GridOption> { left = StringOrDouble(120.0) })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            data = strings(*categories)
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#ccc") }
                                }
                            // The original nests `color` / `fontSize` under `axisLabel.textStyle`;
                            // ECharts 6 reads text styles from the label itself.
                            axisLabel =
                                unsafeJso<AxisLabelOption> {
                                    margin = 40.0
                                    color = "#999"
                                    fontSize = StringOrDouble(16.0)
                                }
                            splitLine = unsafeJso<SplitLineOption> { show = true }
                            position = AxisPositions.top
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        starSeries("bg", values) {
                            tooltip = unsafeJso<TooltipOption> { trigger = TooltipTriggers.none }
                            // The original uses `itemStyle.normal.color`.
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddd") }
                            silent = true
                            symbolRepeat = BooleanOrDoubleOrString("fixed")
                            symbolClip = false
                            symbolBoundingData = DoubleOrDoubleArray(40.0)
                            z = -1.0
                        },
                        starSeries("bg", values) {
                            tooltip = unsafeJso<TooltipOption> { trigger = TooltipTriggers.none }
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddd") }
                            silent = true
                            symbolRepeat = BooleanOrDoubleOrString("fixed")
                            symbolClip = false
                            symbolBoundingData = DoubleOrDoubleArray(-60.0)
                            z = -1.0
                        },
                        starSeries("data", values) {
                            symbolRepeat = BooleanOrDoubleOrString(true)
                            symbolClip = true
                            symbolBoundingData = DoubleOrDoubleArray(arrayOf(-60.0, 40.0))
                        },
                    )
            }
        },
        docMore.chart(
            "doc-example/pictorialBar-clip",
            "象形柱图：裁剪",
            "Pictorial bar: symbolClip",
        ) {
            fun symbolHalfSize(): StringOrDoubleOrArray =
                StringOrDoubleOrArray(arrayOf(StringOrDouble("50%"), StringOrDouble("80%")))
            unsafeJso<EChartsOption> {
                color = arrayOf(ZRColor("#bb0004"), ZRColor("#ccc"))
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data =
                                strings(
                                    "symbolClip: true\nsymbolRepeat: false",
                                    "symbolClip: true\nsymbolRepeat: true",
                                    "symbolClip: false\nsymbolRepeat: true",
                                )
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { interval = AxisInterval(0.0) }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            max = AxisMinMax(80.0)
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        // The series that holds the real value; it sits above the background.
                        pictorialBarSeries {
                            name = "realValue"
                            symbol = PAW_SYMBOL
                            z = 10.0
                            symbolBoundingData = DoubleOrDoubleArray(80.0)
                            typedData =
                                dataItems(
                                    pictorialItem(40.0) { symbolClip = true },
                                    pictorialItem(40.0) {
                                        symbolSize = symbolHalfSize()
                                        symbolClip = true
                                        symbolRepeat = BooleanOrDoubleOrString(true)
                                    },
                                    pictorialItem(40.0) {
                                        symbolSize = symbolHalfSize()
                                        symbolClip = false
                                        symbolRepeat = BooleanOrDoubleOrString(true)
                                    },
                                )
                        },
                        // The background series repeats the same symbols in grey.
                        pictorialBarSeries {
                            name = "background"
                            symbol = PAW_SYMBOL
                            symbolBoundingData = DoubleOrDoubleArray(80.0)
                            // The original nests these fields under `label.normal.textStyle`.
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    position = LabelPosition("outside")
                                    formatter = LabelFormatter("value: {c}")
                                    color = "#bb0004"
                                }
                            typedData =
                                dataItems(
                                    // Zero disables the initial animation of the background but
                                    // keeps the animation of later updates.
                                    pictorialItem(40.0) { animationDuration = AnimationDuration(0.0) },
                                    pictorialItem(40.0) {
                                        symbolSize = symbolHalfSize()
                                        symbolRepeat = BooleanOrDoubleOrString("fixed")
                                        animationDuration = AnimationDuration(0.0)
                                    },
                                    pictorialItem(40.0) {
                                        symbolSize = symbolHalfSize()
                                        symbolRepeat = BooleanOrDoubleOrString("fixed")
                                    },
                                )
                        },
                    )
            }
        },
        docMore.chart(
            "doc-example/scatter-tutorial-dataZoom-2",
            "散点图与缩放（二）",
            "Scatter with dataZoom (2)",
        ) {
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
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            start = 1.0
                            end = 35.0
                        },
                    )
                series = arrayOf<SeriesOption>(tutorialScatter("scatter"))
            }
        },
        docMore.chart(
            "doc-example/scatter-tutorial-dataZoom-3",
            "散点图与缩放（三）",
            "Scatter with dataZoom (3)",
        ) {
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            xAxisIndex = IndexOrIndexArray(0.0)
                            start = 10.0
                            end = 60.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            xAxisIndex = IndexOrIndexArray(0.0)
                            start = 10.0
                            end = 60.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            yAxisIndex = IndexOrIndexArray(0.0)
                            start = 30.0
                            end = 80.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            yAxisIndex = IndexOrIndexArray(0.0)
                            start = 30.0
                            end = 80.0
                        },
                    )
                series = arrayOf<SeriesOption>(tutorialScatter("scatter"))
            }
        },
        docMore.chart(
            "doc-example/scatter-dataZoom-all",
            "散点图：双轴缩放",
            "Scatter with dataZoom on both axes",
        ) {
            val random = Lcg(20261028)
            fun data(xMax: Double): Array<DoubleArray> =
                Array(500) {
                    doubleArrayOf(
                        round3(random.next() * xMax),
                        round3(random.next() * 10.0),
                        round3(random.next()),
                    )
                }
            val data1 = data(15.0)
            val data2 = data(10.0)
            val data3 = data(15.0)
            unsafeJso<EChartsOption> {
                animation = false
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("scatter", "scatter2", "scatter3")
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                            splitLine = unsafeJso<SplitLineOption> { show = true }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                            splitLine = unsafeJso<SplitLineOption> { show = true }
                        },
                    )
                dataZoom =
                    arrayOf(
                        zoomWindow(
                            DataZoomTypes.slider,
                            1.0,
                            35.0,
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0)),
                        ),
                        zoomWindow(
                            DataZoomTypes.slider,
                            29.0,
                            36.0,
                            yAxisIndex = IndexOrIndexArray(arrayOf(0.0)),
                            left = StringOrDouble("93%"),
                        ),
                        zoomWindow(
                            DataZoomTypes.inside,
                            1.0,
                            35.0,
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0)),
                        ),
                        zoomWindow(
                            DataZoomTypes.inside,
                            29.0,
                            36.0,
                            yAxisIndex = IndexOrIndexArray(arrayOf(0.0)),
                        ),
                    )
                series =
                    arrayOf<SeriesOption>(
                        zoomScatter("scatter", data1),
                        zoomScatter("scatter2", data2),
                        zoomScatter("scatter3", data3),
                    )
            }
        },
        docMore.chart("doc-example/sunburst-simple", "旭日图基础", "Sunburst basics") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData =
                                arrayOf<SunburstSeriesNodeItemOption?>(
                                    sunburstNode(
                                        "A",
                                        10.0,
                                        arrayOf(sunburstNode("Aa", 3.0), sunburstNode("Ab", 5.0)),
                                    ),
                                    sunburstNode(
                                        "B",
                                        children =
                                            arrayOf(sunburstNode("Ba", 4.0), sunburstNode("Bb", 2.0)),
                                    ),
                                    sunburstNode("C", 3.0),
                                )
                        },
                    )
            }
        },
        docMore.chart(
            "doc-example/text-fregment-align",
            "文本片段的对齐",
            "Text fragment alignment",
        ) {
            unsafeJso<EChartsOption> {
                xAxis = hiddenXAxis()
                yAxis = hiddenYAxis()
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(1.0)
                            typedData =
                                dataItems(
                                    ScatterDataItem(
                                        arrayOf<OptionDataValue?>(
                                            OptionDataValue(0.0),
                                            OptionDataValue(0.0),
                                        ),
                                    ),
                                )
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    formatter =
                                        LabelFormatter(
                                            listOf(
                                                "{tc|height is specified as 30}{box|}",
                                                "{lh|Biggest lineHeight} {vt|verticalAlign \"top\"} " +
                                                    "{vb|verticalAlign \"bottom\"} {vm|verticalAlign \"middle\"}",
                                                "{l1|align \"left\"}{l2|align \"left\"}{c1|align \"center\"}" +
                                                    "{c2|align \"center\"}{r2|align \"right\"}{r1|align \"right\"}",
                                                "{tc|height is specified as 30}{box|}",
                                            ).joinToString("\n"),
                                        )
                                    backgroundColor = ZRColor("#ddd")
                                    borderColor = "#555"
                                    borderWidth = 1.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    padding = DoubleOrDoubleArray(arrayOf(0.0, 20.0, 0.0, 20.0))
                                    color = "#000"
                                    fontSize = StringOrDouble(14.0)
                                    rich =
                                        recordOf(
                                            "box" to
                                                unsafeJso<TextStyleOption> {
                                                    backgroundColor = ZRColor("#000")
                                                    height = 30.0
                                                    padding =
                                                        DoubleOrDoubleArray(arrayOf(0.0, 20.0, 0.0, 20.0))
                                                    align = TextAlign.right
                                                    width = StringOrDouble("100%")
                                                    color = "#eee"
                                                },
                                            "tc" to
                                                unsafeJso<TextStyleOption> {
                                                    align = TextAlign.center
                                                    color = "#eee"
                                                },
                                            "lh" to
                                                unsafeJso<TextStyleOption> {
                                                    fontSize = StringOrDouble(30.0)
                                                    color = "rgb(199,86,83)"
                                                    borderWidth = 1.0
                                                    borderColor = "rgb(199,86,83)"
                                                },
                                            "vt" to verticalTag(VerticalAlign.top),
                                            "vb" to verticalTag(VerticalAlign.bottom),
                                            "vm" to verticalTag(VerticalAlign.middle),
                                            "l1" to alignTag(null, rounded = false),
                                            "l2" to alignTag(null, rounded = true),
                                            "r1" to alignTag(TextAlign.right, rounded = false),
                                            "r2" to alignTag(TextAlign.right, rounded = true),
                                            "c1" to alignTag(TextAlign.center, rounded = false, width = 100.0),
                                            "c2" to alignTag(TextAlign.center, rounded = true, width = 100.0),
                                        ).unsafeCast<Record<String, TextStyleOption>>()
                                }
                        },
                    )
            }
        },
        docMore.chart("doc-example/label-position", "标签位置", "Label position") {
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> { max = AxisMinMax(2.0) })
                yAxis = one(unsafeJso<YAxisOption> { max = AxisMinMax(2.0) })
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            symbolSize = SymbolSize(360.0)
                            symbol = SymbolShape("roundRect")
                            typedData =
                                dataItems(
                                    ScatterDataItem(
                                        arrayOf<OptionDataValue?>(
                                            OptionDataValue(1.0),
                                            OptionDataValue(1.0),
                                        ),
                                    ),
                                )
                            // The official page drives these four values from a configuration
                            // panel; the demo has no panel and shows its defaults.
                            label =
                                unsafeJso<LabelOption> {
                                    position = LabelPosition("top")
                                    align = TextAlign.center
                                    verticalAlign = VerticalAlign.middle
                                    distance = 5.0
                                    show = true
                                    formatter = LabelFormatter("Label Text")
                                    width = StringOrDouble(200.0)
                                    height = 60.0
                                    backgroundColor = ZRColor("rgba(0, 255, 255, 0.3)")
                                    borderColor = "#555"
                                    borderWidth = 2.0
                                    borderRadius = DoubleOrDoubleArray(5.0)
                                    padding = DoubleOrDoubleArray(10.0)
                                    fontSize = StringOrDouble(18.0)
                                    shadowBlur = 3.0
                                    shadowColor = ZRColor("#888")
                                    shadowOffsetX = 0.0
                                    shadowOffsetY = 3.0
                                    textBorderColor = "#000"
                                    textBorderWidth = 3.0
                                    color = "#fff"
                                }
                        },
                    )
            }
        },
        docMore.interactive(
            "doc-example/custom-transition-simple",
            "自定义系列的过渡动画",
            "Custom series transition",
        ) { chart, holder ->
            val random = Lcg(20261028)
            fun item(): Array<DoubleArray> = arrayOf(doubleArrayOf(random.next(), random.next()))

            chart.setOption(
                unsafeJso<EChartsOption> {
                    animationDurationUpdate = AnimationDuration(1500.0)
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                min = AxisMinMax(0.0)
                                max = AxisMinMax(1.0)
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                min = AxisMinMax(0.0)
                                max = AxisMinMax(1.0)
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            customSeries {
                                renderItem = { params, api ->
                                    val x = api.value(DimensionLoose(0.0)).unsafeCast<Double>()
                                    val y = api.value(DimensionLoose(1.0)).unsafeCast<Double>()
                                    val pos =
                                        api.coord(
                                            CoordinateSystemDataCoord(
                                                arrayOf(OptionDataValue(x), OptionDataValue(y)),
                                            ),
                                        )
                                    val width = 50.0 + random.next() * 150.0
                                    val height = 50.0 + random.next() * 150.0
                                    val radius = random.next() * 40.0
                                    val fill = ECharts.color.random()
                                    val lineWidth = 2.0 + round(random.next() * 20.0)
                                    val alignWidth = params.coordSys.width ?: 0.0
                                    unsafeJso<CustomGroupOption> {
                                        type = "group"
                                        children =
                                            arrayOf(
                                                transitionRect(
                                                    pos[0] / 2.0,
                                                    pos[1],
                                                    width,
                                                    height,
                                                    radius,
                                                    fill,
                                                    lineWidth,
                                                    "Has transition",
                                                    StringOrStringArray(
                                                        arrayOf("shape", "style", "x", "y"),
                                                    ),
                                                ),
                                                transitionRect(
                                                    pos[0] / 2.0 + alignWidth / 2.0,
                                                    pos[1],
                                                    width,
                                                    height,
                                                    radius,
                                                    fill,
                                                    lineWidth,
                                                    "No transition",
                                                    StringOrStringArray(emptyArray()),
                                                ),
                                            )
                                    }
                                }
                                typedData = points(*item())
                            },
                        )
                },
            )
            holder.setInterval(
                {
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    customSeries { typedData = points(*item()) },
                                )
                        },
                    )
                },
                2000,
            )
        },
        docMore.interactive(
            "doc-example/value-animation-simple",
            "标签数值动画",
            "Value animation",
        ) { chart, holder ->
            val random = Lcg(20261028)
            val yMax = 1000.0
            val updateDuration = 3000
            fun nextValue(): Double = round(random.next() * yMax)

            fun bar(name: String, animate: Boolean, value: Double): BarSeriesOption =
                barSeries {
                    this.name = name
                    typedData = numbers(value)
                    label =
                        unsafeJso<LabelOption> {
                            show = true
                            valueAnimation = animate
                            position = LabelPosition("top")
                            fontSize = StringOrDouble(40.0)
                        }
                    barWidth = StringOrDouble(20.0)
                    animationDurationUpdate = AnimationDuration(updateDuration * 0.7)
                    animationEasingUpdate = AnimationEasings.linear
                }

            val initial = nextValue()
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                data = strings("a")
                                axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            },
                        )
                    yAxis = one(unsafeJso<YAxisOption> { max = AxisMinMax(yMax) })
                    legend = one(unsafeJso<LegendOption> { bottom = StringOrDouble(10.0) })
                    series =
                        arrayOf<SeriesOption>(
                            bar("valueAnimation: true", true, initial).apply {
                                barGap = StringOrDouble("1000%")
                                barCategoryGap = StringOrDouble(200.0)
                            },
                            bar("valueAnimation: false", false, initial),
                        )
                },
            )
            holder.setInterval(
                {
                    val value = nextValue()
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    barSeries { typedData = numbers(value) },
                                    barSeries { typedData = numbers(value) },
                                )
                        },
                    )
                },
                updateDuration,
            )
        },
        docMore.interactive(
            "doc-example/treemap-borderColor",
            "矩形树图的边框颜色",
            "Treemap border colour",
        ) { chart ->
            loadJson("data/disk.tree.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Disk Usage"
                                    left = StringOrDouble("center")
                                },
                            )
                        // The original formats the folder path as HTML; the UI rules of this
                        // repository keep markup out of the sources, so the tooltip is plain text.
                        tooltip = one(unsafeJso<TooltipOption> { formatter = TooltipFormatter("{b}: {c} KB") })
                        series =
                            arrayOf<SeriesOption>(
                                treemapSeries {
                                    name = "Disk Usage"
                                    visibleMin = 300.0
                                    label =
                                        unsafeJso<LabelOption> {
                                            show = true
                                            formatter = LabelFormatter("{b}")
                                        }
                                    itemStyle =
                                        unsafeJso<TreemapSeriesItemStyleOption> {
                                            borderColor = ZRColor("#fff")
                                        }
                                    levels = diskLevels()
                                    typedData = json.unsafeCast<Array<TreemapSeriesNodeItemOption?>>()
                                },
                            )
                    },
                )
                null
            }
        },
    )
