package demo.examples.doc

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.round
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ElementTextConfig
import zrender.PatternObject
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import demo.examples.gauge.center
import demo.examples.shared.doubleRow
import demo.examples.shared.fixed
import demo.examples.shared.numberAt
import demo.examples.shared.pieItem
import demo.examples.shared.row
import demo.examples.sunburst.radius

/** `value` rounded to three decimals, the precision of the `random(max)` helper upstream. */
internal fun round3(value: Double): Double = round(value * 1000.0) / 1000.0

/** The walking figure of `pictorialBar-position`. */
internal const val WALK_SYMBOL =
    "path://M29.902,23.275c1.86,0,3.368-1.506,3.368-3.365c0-1.859-1.508-3.365-3.368-3.365 " +
        "c-1.857,0-3.365,1.506-3.365,3.365C26.537,21.769,28.045,23.275,29.902,23.275z " +
        "M36.867,30.74c-1.666-0.467-3.799-1.6-4.732-4.199 c-0.932-2.6-3.131-2.998-4.797-2.998s-7.098,3.894-7.098,3.894c-1.133,1.001-2.1,6.502-0.967,6.769 " +
        "c1.133,0.269,1.266-1.533,1.934-3.599c0.666-2.065,3.797-3.466,3.797-3.466s0.201,2.467-0.398,3.866 " +
        "c-0.599,1.399-1.133,2.866-1.467,6.198s-1.6,3.665-3.799,6.266c-2.199,2.598-0.6,3.797,0.398,3.664 " +
        "c1.002-0.133,5.865-5.598,6.398-6.998c0.533-1.397,0.668-3.732,0.668-3.732s0,0,2.199,1.867c2.199,1.865,2.332,4.6,2.998,7.73 " +
        "s2.332,0.934,2.332-0.467c0-1.401,0.269-5.465-1-7.064c-1.265-1.6-3.73-3.465-3.73-5.265s1.199-3.732,1.199-3.732 " +
        "c0.332,1.667,3.335,3.065,5.599,3.399C38.668,33.206,38.533,31.207,36.867,30.74z"

/** The rocket of `pictorialBar-symbolSize`. */
internal const val ROCKET_SYMBOL =
    "path://M-244.396,44.399c0,0,0.47-2.931-2.427-6.512c2.819-8.221,3.21-15.709,3.21-15.709s5.795,1.383,5.795,7.325C-237.818,39.679-244.396,44.399-244.396,44.399z " +
        "M-260.371,40.827c0,0-3.881-12.946-3.881-18.319c0-2.416,0.262-4.566,0.669-6.517h17.684c0.411,1.952,0.675,4.104,0.675,6.519c0,5.291-3.87,18.317-3.87,18.317H-260.371z " +
        "M-254.745,18.951c-1.99,0-3.603,1.676-3.603,3.744c0,2.068,1.612,3.744,3.603,3.744c1.988,0,3.602-1.676,3.602-3.744S-252.757,18.951-254.745,18.951z " +
        "M-255.521,2.228v-5.098h1.402v4.969c1.603,1.213,5.941,5.069,7.901,12.5h-17.05C-261.373,7.373-257.245,3.558-255.521,2.228zM-265.07,44.399c0,0-6.577-4.721-6.577-14.896c0-5.942,5.794-7.325,5.794-7.325s0.393,7.488,3.211,15.708C-265.539,41.469-265.07,44.399-265.07,44.399z " +
        "M-252.36,45.15l-1.176-1.22L-254.789,48l-1.487-4.069l-1.019,2.116l-1.488-3.826h8.067L-252.36,45.15z"

/** The star of `pictorialBar-symbolBoundingDataArray`. */
internal const val STAR_SYMBOL =
    "path://m15.5,19c-0.082,0 -0.164,-0.02 -0.239,-0.061l-5.261,-2.869l-5.261,2.869c-0.168,0.092 -0.373,0.079 -0.529,-0.032s-0.235,-0.301 -0.203,-0.49l0.958,-5.746l-3.818,-3.818c-0.132,-0.132 -0.18,-0.328 -0.123,-0.506s0.209,-0.31 0.394,-0.341l5.749,-0.958l2.386,-4.772c0.085,-0.169 0.258,-0.276 0.447,-0.276s0.363,0.107 0.447,0.276l2.386,4.772l5.749,0.958c0.185,0.031 0.337,0.162 0.394,0.341s0.01,0.374 -0.123,0.506l-3.818,3.818l0.958,5.746c0.031,0.189 -0.048,0.379 -0.203,0.49c-0.086,0.061 -0.188,0.093 -0.29,0.093z"

/** The paw of `pictorialBar-clip`. */
internal const val PAW_SYMBOL =
    "path://M36.7,102.84c-1.17,2.54-2.99,4.98-3.39,7.63c-1.51,9.89-3.31,19.58-1.93,29.95 c0.95,7.15-2.91,14.82-3.57,22.35c-0.64,7.36-0.2,14.86,0.35,22.25c0.12,1.68,2.66,3.17,4.67,5.4c-0.6,0.82-1.5,2.22-2.58,3.48 c-0.96,1.12-1.96,2.35-3.21,3.04c-1.71,0.95-3.71,2.03-5.51,1.9c-1.18-0.08-3.04-2.13-3.16-3.43c-0.44-4.72,0-9.52-0.41-14.25 c-0.94-10.88-2.32-21.72-3.24-32.61c-0.49-5.84-1.63-12.01-0.35-17.54c3.39-14.56,2.8-28.84,0.36-43.4 c-2.71-16.16-1.06-32.4,0.54-48.59c0.91-9.22,4.62-17.36,8.53-25.57c1.32-2.77,1.88-6.84,0.87-9.62C21.89-3.77,18.09-11,14.7-18.38 c-0.56,0.1-1.13,0.21-1.69,0.31C10.17-11.52,6.29-5.2,4.71,1.65C2.05,13.21-4.42,22.3-11.43,31.28c-1.32,1.69-2.51,3.5-3.98,5.04 c-4.85,5.08-3.25,10.98-2.32,16.82c0.25,1.53,0.52,3.06,0.77,4.59c-0.53,0.22-1.07,0.43-1.6,0.65c-1.07-2.09-2.14-4.19-3.28-6.44 c-6.39,2.91-2.67,9.6-5.23,15.16c-1.61-3.31-2.77-5.68-3.93-8.06c0-0.33,0-0.67,0-1c6.96-16.08,14.63-31.9,20.68-48.31 C-5.24-4.07-2.03-18.55,2-32.73c0.36-1.27,0.75-2.53,0.98-3.82c1.36-7.75,4.19-10.23,11.88-10.38c1.76-0.04,3.52-0.21,5.76-0.35 c-0.55-3.95-1.21-7.3-1.45-10.68c-0.61-8.67,0.77-16.69,7.39-23.19c2.18-2.14,4.27-4.82,5.25-7.65c2.39-6.88,11.66-9,16.94-8.12 c5.92,0.99,12.15,7.93,12.16,14.12c0.01,9.89-5.19,17.26-12.24,23.68c-2.17,1.97-5.35,4.77-5.17,6.94c0.31,3.78,4.15,5.66,8.08,6.04 c1.82,0.18,3.7,0.37,5.49,0.1c5.62-0.85,8.8,2.17,10.85,6.73C73.38-27.19,78.46-14.9,84.2-2.91c1.52,3.17,4.52,5.91,7.41,8.09 c7.64,5.77,15.57,11.16,23.45,16.61c2.28,1.58,4.64,3.23,7.21,4.14c5.18,1.84,8.09,5.63,9.82,10.46c0.45,1.24,0.19,3.71-0.6,4.18 c-1.06,0.63-3.15,0.27-4.44-0.38c-7.05-3.54-12.84-8.88-19.14-13.5c-3.5-2.57-7.9-4-12.03-5.6c-9.44-3.66-17.73-8.42-22.5-18.09 c-2.43-4.94-6.09-9.27-9.69-14.61c-1.2,10.98-4.46,20.65,1.14,31.19c6.62,12.47,5.89,26.25,1.21,39.49 c-2.52,7.11-6.5,13.74-8.67,20.94c-1.91,6.33-2.2,13.15-3.23,19.75c-0.72,4.63-0.84,9.48-2.36,13.84 c-2.49,7.16-6.67,13.83-5.84,21.82c0.42,4.02,1.29,7.99,2.1,12.8c-3.74-0.49-7.47-0.4-10.67-1.66c-1.33-0.53-2.43-4.11-2.07-6.01 c1.86-9.94,3.89-19.69,0.07-29.74C34.55,108.63,36.19,105.52,36.7,102.84c1.25-8.45,2.51-16.89,3.71-24.9 c-0.83-0.58-0.85-0.59-0.87-0.61c-0.03,0.16-0.07,0.32-0.09,0.48C38.53,86.15,37.62,94.5,36.7,102.84z"

/** One pictorial bar item: the value plus the symbol options of that bar. */
internal fun pictorialItem(
    value: Double,
    block: PictorialBarDataItemOption.() -> Unit = {},
): PictorialBarDataItem {
    val item = unsafeJso<PictorialBarDataItemOption> { this.value = OptionDataValue(value) }
    item.block()
    return PictorialBarDataItem(item)
}

/** A sunburst node with an optional value and an optional list of children. */
internal fun sunburstNode(
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
internal val tutorialScatterValues =
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
internal fun tutorialScatter(name: String): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        // The original nests `opacity` under `itemStyle.normal`; ECharts 6 dropped that state.
        itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
        symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) * 40.0) }
        typedData = points(*tutorialScatterValues)
    }

/** A scatter series of `scatter-dataZoom-all`; the symbol size comes from the third value. */
internal fun zoomScatter(name: String, data: Array<DoubleArray>): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
        symbolSize = SymbolSize { value, _ -> DoubleOrDoubleArray(numberAt(value, 2) * 40.0) }
        typedData = points(*data)
    }

/** One zoom of `scatter-dataZoom-all`: either on the x axis or on the y axis. */
internal fun zoomWindow(
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
internal val symbolSizeDescriptions =
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
internal fun placeSymbolSizeDescriptions(chart: EChartsInstance) {
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
internal fun alignTag(align: TextAlign?, rounded: Boolean, width: Double? = null): TextStyleOption =
    unsafeJso {
        backgroundColor = ZRColor(if (rounded) "#339911" else "#7066FF")
        color = "#fff"
        if (rounded) borderRadius = DoubleOrDoubleArray(15.0)
        padding = DoubleOrDoubleArray(5.0)
        if (align != null) this.align = align
        if (width != null) this.width = StringOrDouble(width)
    }

/** The three vertical-alignment tags of `text-fregment-align`. */
internal fun verticalTag(align: VerticalAlign): TextStyleOption =
    unsafeJso {
        verticalAlign = align
        backgroundColor = ZRColor("#991122")
        color = "#fff"
        padding = DoubleOrDoubleArray(arrayOf(1.0, 2.0, 1.0, 2.0))
    }

/** One rectangle of `custom-transition-simple`, with or without a transition. */
internal fun transitionRect(
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
internal fun starSeries(
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

/** The eight rose slices of `pie-media`, named after the legend. */
internal val roseLegendNames = Array(8) { index -> "rose${index + 1}" }

/** The values of the two roses of `pie-media`; both series use the same table. */
internal val roseValues = doubleArrayOf(10.0, 5.0, 15.0, 25.0, 20.0, 35.0, 30.0, 40.0)

/**
 * One rose series of `pie-media`; the base option only sets the name and the rose mode, the radius
 * and the centre come from the matched `media` unit.
 *
 * The original also sets the ECharts 4 `label.normal` / `lableLine.normal` blocks (the second one is
 * a typo ECharts never read) and the legacy `calculable` flag; ECharts 6 has none of them.
 */
internal fun roseSeries(name: String, roseType: RoseType): SeriesOption =
    pieSeries {
        this.name = name
        this.roseType = roseType
        typedData = dataItems(*Array(roseValues.size) { index -> pieItem("rose${index + 1}", roseValues[index]) })
    }

/**
 * One `media` unit of `pie-media`: the legend box and the two rose centres.
 *
 * [centers] holds one `"x/y"` pair per series, which is expanded into the two percentages upstream
 * writes as an array.
 */
internal fun roseMedia(
    query: MediaQueryOption?,
    legendRight: StringOrDouble,
    legendTop: StringOrDouble?,
    legendBottom: StringOrDouble?,
    orient: Orient,
    vararg centers: String,
): MediaUnit =
    unsafeJso {
        this.query = query
        option =
            unsafeJso {
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            right = legendRight
                            if (legendTop != null) this.top = legendTop
                            if (legendBottom != null) this.bottom = legendBottom
                            this.orient = orient
                        },
                    )
                series =
                    Array<SeriesOption>(centers.size) { index ->
                        val parts = centers[index].split('/')
                        pieSeries {
                            radius =
                                StringOrDoubleOrArray(
                                    arrayOf(
                                        StringOrDouble(20.0 + 10.0 * index),
                                        StringOrDouble("50%"),
                                    ),
                                )
                            center =
                                arrayOf(
                                    StringOrDouble(parts[0]),
                                    StringOrDouble(parts[1]),
                                )
                        }
                    }
            }
    }

/** The fill colour of the `aria-decal` patterns. */
internal const val DECAL_COLOR = "rgba(0, 0, 0, 0.8)"

/** Number of swatches in one row of `aria-decal`. */
internal const val ARIA_DECAL_COLUMNS = 6

/** The swirl of the last decal of `aria-decal`, copied verbatim. */
internal const val DECAL_SWIRL_PATH =
    "path://M136.71078,90.7149424 C135.038663,90.084817 133.319214,89.8204828 131.637995,89.8987366 " +
        "C131.93792,88.5693311 132.022117,87.2371959 131.901965,85.9319035 C131.475516,81.1970909 128.414369,76.8944942 123.681104,75.0882862 " +
        "C118.812669,73.2488659 113.532803,74.556888 110.072058,78.0014222 C111.44425,78.0132512 112.836467,78.2716709 114.184082,78.7876003 " +
        "C120.385569,81.1311211 123.498145,88.0593162 121.156545,94.2582038 C120.289538,96.5421242 118.80038,98.4052027 116.960324,99.7405227 " +
        "C113.796773,102.011704 109.591905,102.714169 105.683776,101.228711 C99.4827445,98.8915594 96.3669821,91.9606345 98.7113134,85.7581072 " +
        "C99.0176102,84.9537305 99.4044636,84.2007647 99.8609505,83.4869258 L99.8545788,83.4869258 " +
        "C99.8545788,83.4869258 99.8545788,83.4869258 99.8545788,83.4932953 C101.850742,80.3754255 103.561544,76.9968612 104.927364,73.3990041 " +
        "C115.528966,45.4164295 101.427934,14.127175 73.4261234,3.52605368 C45.4215819,-7.07142797 14.1333382,7.02472855 3.52900521,35.0104878 " +
        "C0.629425582,42.6470623 -0.42099504,50.523403 0.149272308,58.1686218 C0.16110547,58.4206719 0.185226914,58.6786366 0.209348358,58.9366014 " +
        "C2.47266801,84.9182432 19.2184117,108.595036 45.2413538,118.442737 C71.7121355,128.45559 100.448968,121.413198 119.352898,102.762395 " +
        "C122.585626,99.6263266 127.465895,98.4657129 131.950208,100.160455 C137.34704,102.203699 140.39954,107.712861 139.559386,113.191995 " +
        "C141.579216,111.769777 143.215378,109.750646 144.163851,107.256987 C146.676122,100.628613 143.33553,93.2204304 136.71078,90.7149424 ZZ"

/** Horizontal dash spacing of a decal from a flat list of numbers. */
internal fun dashX(vararg values: Double): DecalDashArrayX =
    DecalDashArrayX(Array(values.size) { index -> DoubleOrDoubleArray(values[index]) })

/** Horizontal dash spacing of a decal from the tiles of a repeated pattern. */
internal fun dashXTiles(vararg tiles: DoubleArray): DecalDashArrayX =
    DecalDashArrayX(Array(tiles.size) { index -> DoubleOrDoubleArray(tiles[index].toTypedArray()) })

/** Vertical dash spacing of a decal; one value keeps the number form upstream uses. */
internal fun dashY(vararg values: Double): DoubleOrDoubleArray =
    if (values.size == 1) DoubleOrDoubleArray(values[0]) else DoubleOrDoubleArray(values.toTypedArray())

/** One decal of the `aria-decal` gallery; unset members stay out of the option object. */
internal fun decal(
    color: String? = DECAL_COLOR,
    symbol: String? = null,
    symbolSize: Double? = null,
    dashArrayX: DecalDashArrayX? = null,
    dashArrayY: DoubleOrDoubleArray? = null,
    rotation: Double? = null,
): DecalObject =
    unsafeJso {
        if (color != null) this.color = color
        if (symbol != null) this.symbol = StringOrStringArray(symbol)
        if (symbolSize != null) this.symbolSize = symbolSize
        if (dashArrayX != null) this.dashArrayX = dashArrayX
        if (dashArrayY != null) this.dashArrayY = dashArrayY
        if (rotation != null) this.rotation = rotation
    }

/**
 * The twelve decal patterns of `doc-example/aria-decal`.
 *
 * The rotations are multiples of `PI`, which is what zrender reads; the original ends with a
 * `base64` image decal that this site does not ship, so the swirl `path://` is the last entry.
 */
internal fun ariaDecals(): Array<DecalObject> =
    arrayOf(
        decal(
            dashArrayX = dashX(1.0, 0.0),
            dashArrayY = dashY(2.0, 5.0),
            symbolSize = 1.0,
            rotation = kotlin.math.PI / 6,
        ),
        decal(
            symbol = "circle",
            dashArrayX = dashXTiles(doubleArrayOf(8.0, 8.0), doubleArrayOf(0.0, 8.0, 8.0, 0.0)),
            dashArrayY = dashY(6.0, 0.0),
            symbolSize = 0.8,
        ),
        decal(dashArrayX = dashX(1.0, 0.0), dashArrayY = dashY(4.0, 3.0), rotation = -kotlin.math.PI / 4),
        decal(dashArrayX = dashXTiles(doubleArrayOf(6.0, 6.0), doubleArrayOf(0.0, 6.0, 6.0, 0.0)), dashArrayY = dashY(6.0, 0.0)),
        decal(
            dashArrayX = dashXTiles(doubleArrayOf(1.0, 0.0), doubleArrayOf(1.0, 6.0)),
            dashArrayY = dashY(1.0, 0.0, 6.0, 0.0),
            rotation = kotlin.math.PI / 4,
        ),
        decal(
            symbol = "triangle",
            dashArrayX = dashXTiles(doubleArrayOf(9.0, 9.0), doubleArrayOf(0.0, 9.0, 9.0, 0.0)),
            dashArrayY = dashY(7.0, 2.0),
            symbolSize = 0.75,
        ),
        decal(dashArrayX = dashX(1.0, 0.0), dashArrayY = dashY(10.0), rotation = 0.0),
        decal(dashArrayX = dashX(10.0, 10.0), dashArrayY = dashY(1.0, 0.0), rotation = 0.0),
        decal(dashArrayX = dashX(10.0, 5.0, 20.0, 5.0), dashArrayY = dashY(10.0), rotation = 0.0),
        decal(
            dashArrayX =
                dashXTiles(
                    doubleArrayOf(10.0, 5.0, 20.0, 5.0),
                    doubleArrayOf(20.0, 5.0),
                ),
            dashArrayY = dashY(10.0, 5.0),
            rotation = 0.0,
        ),
        decal(
            color = "black",
            symbol = "rect",
            dashArrayX = dashXTiles(doubleArrayOf(8.0, 8.0), doubleArrayOf(0.0, 8.0, 8.0, 0.0)),
            dashArrayY = dashY(8.0, 0.0),
            symbolSize = 0.8,
            rotation = kotlin.math.PI / 4,
        ),
        decal(
            symbol = DECAL_SWIRL_PATH,
            dashArrayX = dashXTiles(doubleArrayOf(30.0, 30.0), doubleArrayOf(0.0, 30.0, 30.0, 0.0)),
            dashArrayY = dashY(15.0, 0.0),
        ),
    )

/**
 * One swatch series of `aria-decal`: a filled rectangle painted with the color and the decal that
 * the accessibility module assigned to the item, laid out on a [ARIA_DECAL_COLUMNS]-wide grid.
 */
internal fun decalSwatch(index: Int): SeriesOption =
    customSeries {
        typedData = arrayOf(OptionDataItem(doubleRow(index.toDouble())))
        renderItem = { _, api ->
            val value = api.value(DimensionLoose(0.0)).unsafeCast<Double>()
            unsafeJso<CustomGroupOption> {
                type = "group"
                x = (value % ARIA_DECAL_COLUMNS) * 120 + 50
                y = kotlin.math.floor(value / ARIA_DECAL_COLUMNS) * 120 + 50
                children =
                    arrayOf(
                        unsafeJso<CustomPathOption> {
                            type = "rect"
                            shape =
                                unsafeJso<ZRShapeProps> {
                                    x = 0.0
                                    y = 0.0
                                    width = 100.0
                                    height = 100.0
                                }
                            style =
                                unsafeJso<ZRStyleProps> {
                                    fill = api.visual(VisualProps.color).unsafeCast<ZRColor>()
                                    decal = api.visual(VisualProps.decal).unsafeCast<PatternObject>()
                                }
                        },
                    )
            }
        }
    }
