package demo.examples.line

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.gradientColorStop
import io.github.shijianjs.ktjs.zrender.linearGradient
import js.objects.unsafeJso
import demo.examples.custom.coord
import demo.examples.dataset.filteredSince1950
import demo.examples.lines.coordinate
import demo.examples.shared.Lcg
import demo.examples.shared.every
import demo.examples.shared.row
import demo.examples.shared.textAt
import demo.examples.themeriver.days

/** One AQI band of the visual map; `lte` is null for the last, open-ended band. */
internal fun aqiPiece(gt: Double, lte: Double?, color: String): VisualPiece =
    unsafeJso {
        this.gt = gt
        if (lte != null) this.lte = lte
        this.color = StringOrStringArray(color)
    }

/** A horizontal threshold line at [value]. */
internal fun aqiMark(value: Double): MarkLineDataItem =
    MarkLineDataItem(unsafeJso<MarkLine1DDataItemOption> { yAxis = StringOrDouble(value) })

/** The `[r, theta]` spiral of `line-polar`. */
internal fun polarSpiral(): Array<LineDataItem?> {
    val data = Array(101) { index ->
        val theta = index / 100.0 * 360
        val r = 5 * (1 + kotlin.math.sin(theta / 180 * kotlin.math.PI))
        doubleArrayOf(r, theta)
    }
    return points(*data)
}

/** The `[r, i]` curve of `line-polar2`. */
internal fun polarRose(): Array<LineDataItem?> {
    val data = Array(361) { index ->
        val t = index / 180.0 * kotlin.math.PI
        doubleArrayOf(kotlin.math.sin(2 * t) * kotlin.math.cos(2 * t), index.toDouble())
    }
    return points(*data)
}

/** The product of three sines of `line-function`, sampled every 0.1. */
internal fun functionPlot(): Array<LineDataItem?> {
    val data =
        (0..4000).map { step ->
            val x = -200 + step * 0.1
            val scaled = x / 10
            val y =
                kotlin.math.sin(scaled) *
                    kotlin.math.cos(scaled * 2 + 1) *
                    kotlin.math.sin(scaled * 3 + 2) * 50
            doubleArrayOf(x, y)
        }
    return points(*data.toTypedArray())
}

/** Nine days of a random walk starting right after 2016-10-03, the data of `line-tooltip-touch`. */
internal fun touchWalk(seed: Int, reset: Double): Array<LineDataItem?> {
    val random = Lcg(seed)
    var value = random.next() * reset
    return Array(9) { index ->
        value = kotlin.math.round((random.next() - 0.5) * 20 + value)
        if (value <= 0) value = random.next() * reset
        // Upstream joins the date parts without padding, so no leading zeros here.
        LineDataItem(row("2016-10-${index + 4}", value))
    }
}

/** One smoothed, stacked area of `line-tooltip-touch`. */
internal fun touchLine(
    name: String,
    color: String,
    from: String,
    to: String,
    data: Array<LineDataItem?>,
): LineSeriesOption =
    lineSeries {
        this.name = name
        smooth = BooleanOrDouble(true)
        symbol = SymbolShape("circle")
        symbolSize = SymbolSize(5.0)
        sampling = "average"
        stack = "a"
        itemStyle = unsafeJso<ItemStyleOption> { this.color = ZRColor(color) }
        areaStyle =
            unsafeJso<AreaStyleOption> {
                this.color = linearGradient(0.0, 0.0, 0.0, 1.0, arrayOf(gradientColorStop(0.0, from), gradientColorStop(1.0, to)))
            }
        typedData = data
    }

/** The twelve mark-line label positions of `line-markline`, in the upstream order. */
internal val MARK_LINE_LABEL_POSITIONS =
    arrayOf(
        "start",
        "middle",
        "end",
        "insideStart",
        "insideStartTop",
        "insideStartBottom",
        "insideMiddle",
        "insideMiddleTop",
        "insideMiddleBottom",
        "insideEnd",
        "insideEndTop",
        "insideEndBottom",
    )

/** `[x, y]` in the coordinate system of the series, as a mark line end accepts it. */
internal fun markerCoord(
    x: Double,
    y: Double,
): Array<ScaleDataValueOrMarkerStatisticType> =
    arrayOf(x, y).map { ScaleDataValueOrMarkerStatisticType(ScaleDataValue(it)) }.toTypedArray()

/** The 23 mark lines of `line-markline`: a horizontal line and a diagonal one per label position. */
internal fun markLinePositions(): Array<MarkLineDataItem?> {
    val data = mutableListOf<MarkLineDataItem>()
    MARK_LINE_LABEL_POSITIONS.forEachIndexed { index, position ->
        // Every third line drops one step, so the twelve lines form four rows of three.
        data +=
            MarkLineDataItem(
                unsafeJso<MarkLine1DDataItemOption> {
                    name = position
                    yAxis = StringOrDouble(1.8 - 0.2 * kotlin.math.floor(index / 3.0))
                    label =
                        unsafeJso<MarkLineLabelOption> {
                            formatter = LabelFormatter("{b}")
                            this.position = LabelPosition(position)
                        }
                },
            )
        if (position != "middle") {
            // `insideMiddle` and `middle` land on the same place, so the diagonal shares one label.
            val label = if (position == "insideMiddle") "insideMiddle / middle" else position
            data +=
                MarkLineDataItem(
                    arrayOf(
                        unsafeJso<MarkLine2DDataItemDimOption> {
                            name = "start: $position"
                            coord = markerCoord(0.0, 0.3)
                            this.label =
                                unsafeJso<MarkLineLabelOption> {
                                    formatter = LabelFormatter(label)
                                    this.position = LabelPosition(position)
                                }
                        },
                        unsafeJso<MarkLine2DDataItemDimOption> {
                            name = "end: $position"
                            coord = markerCoord(3.0, 1.0)
                        },
                    ),
                )
        }
    }
    return data.toTypedArray()
}

/** One time-of-day band of `line-sections`; `gt`/`lte` are omitted at the open ends. */
internal fun sectionPiece(
    gt: Double?,
    lte: Double?,
    color: String,
): VisualPiece =
    unsafeJso {
        if (gt != null) this.gt = gt
        if (lte != null) this.lte = lte
        this.color = StringOrStringArray(color)
    }

/** A shaded `[from, to]` band of `line-sections`; the label sits on the first edge. */
internal fun peakArea(
    name: String,
    from: String,
    to: String,
): MarkAreaDataItem =
    MarkAreaDataItem(
        arrayOf(
            unsafeJso<MarkArea2DDataItemDimOption> {
                this.name = name
                xAxis = ScaleDataValue(from)
            },
            unsafeJso<MarkArea2DDataItemDimOption> { xAxis = ScaleDataValue(to) },
        ),
    )

// ---------------------------------------------------------------------------------------------
// line-race
// ---------------------------------------------------------------------------------------------

/** The countries of `line-race`, copied from the upstream `countries`. */
internal val LINE_RACE_COUNTRIES =
    arrayOf("Finland", "France", "Germany", "Iceland", "Norway", "Poland", "Russia", "United Kingdom")

/** One racing line, reading its own filtered dataset. */
internal fun lineRaceSeries(country: String): LineSeriesOption =
    lineSeries {
        datasetId = "dataset_$country"
        name = country
        showSymbol = false
        endLabel =
            unsafeJso<LabelOption> {
                show = true
                formatter = LabelFormatter { params -> textAt(params.value, 3) + ": " + textAt(params.value, 0) }
            }
        labelLayout = LabelLayout(unsafeJso<LabelLayoutOption> { moveOverlap = MoveOverlaps.shiftY })
        emphasis = unsafeJso<EmphasisOption> { focus = EmphasisFocuses.series }
        encode =
            unsafeJso<OptionEncode> {
                x = OptionEncodeValue(DimensionLoose("Year"))
                y = OptionEncodeValue(DimensionLoose("Income"))
                label =
                    OptionEncodeValue(
                        arrayOf<DimensionLoose>(DimensionLoose("Country"), DimensionLoose("Income")),
                    )
                itemName = OptionEncodeValue(DimensionLoose("Year"))
                tooltip = OptionEncodeValue(DimensionLoose("Income"))
            }
    }

/** The option of `line-race`; [raw] is the shared life-expectancy table. */
internal fun lineRaceOption(raw: DatasetSource): EChartsOption =
    unsafeJso {
        animationDuration = AnimationDuration(10000.0)
        dataset =
            arrayOf(
                unsafeJso<DatasetOption> {
                    id = "dataset_raw"
                    source = raw
                },
                *Array(LINE_RACE_COUNTRIES.size) { index ->
                    filteredSince1950("dataset_" + LINE_RACE_COUNTRIES[index], LINE_RACE_COUNTRIES[index])
                },
            )
        title = one(unsafeJso<TitleOption> { text = "Income of Germany and France since 1950" })
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    order = TooltipOrders.valueDesc
                    trigger = TooltipTriggers.axis
                },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    type = AxisTypes.category
                    nameLocation = AxisNameLocations.middle
                },
            )
        yAxis = one(unsafeJso<YAxisOption> { name = "Income" })
        grid = one(unsafeJso<GridOption> { right = StringOrDouble(140.0) })
        series =
            Array<SeriesOption>(LINE_RACE_COUNTRIES.size) { index -> lineRaceSeries(LINE_RACE_COUNTRIES[index]) }
    }
