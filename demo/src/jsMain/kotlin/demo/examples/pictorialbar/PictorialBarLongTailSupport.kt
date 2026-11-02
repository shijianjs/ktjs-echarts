package demo.examples.pictorialbar

import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor
import demo.examples.Category
import demo.examples.matrix.branch
import demo.examples.matrix.names
import demo.examples.shared.axisLabel
import demo.examples.shared.every
import demo.examples.shared.fixed
import demo.examples.shared.legendNames
import demo.examples.shared.numeric
import demo.examples.shared.valueText

/** The delay before `pictorialBar-bar-transition` switches to the next option, upstream 2500 ms. */
internal const val TRANSITION_MILLIS = 2500

/** Category names of the vehicle pages, in the order of the axis. */
internal val VEHICLE_NAMES = arrayOf("reindeer", "ship", "plane", "train", "car")

/** The `path://` silhouette of each entry of [VEHICLE_NAMES]. */
internal val VEHICLE_PATHS = arrayOf(REINDEER_PATH, SHIP_PATH, PLANE_PATH, TRAIN_PATH, CAR_PATH)

/** The 2015 values of `pictorialBar-vehicle` and `-bar-transition`. */
internal val VEHICLE_2015 = doubleArrayOf(157.0, 21.0, 66.0, 78.0, 123.0)

/** The 2016 values of the same two pages. */
internal val VEHICLE_2016 = doubleArrayOf(184.0, 29.0, 73.0, 91.0, 95.0)

/** The shared label of the vehicle bars: right of the bar, offset ten pixels to the right. */
internal fun vehicleLabel(): LabelOption =
    unsafeJso {
        show = true
        position = LabelPosition("right")
        offset = arrayOf(10.0, 0.0)
        fontSize = StringOrDouble(16.0)
    }

/** One vehicle bar: the value plus its silhouette. */
internal fun vehicleItem(value: Double, symbol: String): PictorialBarDataItem =
    PictorialBarDataItem(
        unsafeJso<PictorialBarDataItemOption> {
            this.value = OptionDataValue(value)
            this.symbol = symbol
        },
    )

/** The bars of [values]; [symbol] overrides the silhouette of every bar, as the diamond step does. */
internal fun vehicleItems(values: DoubleArray, symbol: String? = null): Array<PictorialBarDataItem?> =
    dataItems(
        *Array(values.size) { index ->
            vehicleItem(values[index], symbol ?: VEHICLE_PATHS[index])
        },
    )

/** Axes, legend and title shared by the vehicle page and the three transition options. */
internal fun vehicleOption(series: Array<SeriesOption>, durationUpdate: Double? = null): EChartsOption =
    unsafeJso {
        title = one(unsafeJso<TitleOption> { text = "Vehicles in X City" })
        legend = one(unsafeJso<LegendOption> { data = legendNames("2015", "2016") })
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.axis
                    axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.shadow }
                },
            )
        grid =
            one(
                unsafeJso<GridOption> {
                    containLabel = true
                    left = StringOrDouble(20.0)
                },
            )
        yAxis =
            one(
                unsafeJso<YAxisOption> {
                    data = strings(*VEHICLE_NAMES)
                    inverse = true
                    axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                    axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                    axisLabel =
                        unsafeJso<AxisLabelOption> {
                            margin = 30.0
                            fontSize = StringOrDouble(14.0)
                        }
                    axisPointer =
                        unsafeJso<AxisPointerOption> {
                            label = unsafeJso<AxisPointerLabelOption> {
                                show = true
                                margin = 30.0
                            }
                        }
                },
            )
        xAxis =
            one(
                unsafeJso<XAxisOption> {
                    splitLine = unsafeJso<SplitLineOption> { show = false }
                    axisLabel = unsafeJso<AxisLabelOption> { show = false }
                    axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                    axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                },
            )
        this.series = series
        if (durationUpdate != null) animationDurationUpdate = AnimationDuration(durationUpdate)
    }

/** One repeated-symbol bar of the vehicle page. */
internal fun vehicleSeries(
    name: String,
    values: DoubleArray,
    barGap: StringOrDouble?,
    barCategoryGap: StringOrDouble?,
): SeriesOption =
    pictorialBarSeries {
        this.name = name
        label = vehicleLabel()
        symbolRepeat = BooleanOrDoubleOrString(true)
        symbolSize = StringOrDoubleOrArray(arrayOf(StringOrDouble("80%"), StringOrDouble("60%")))
        this.barGap = barGap
        this.barCategoryGap = barCategoryGap
        typedData = vehicleItems(values)
    }

/** `universalTransition` of the transition page: the items morph one after another. */
internal fun vehicleTransition(): BooleanOrUniversalTransitionOption =
    BooleanOrUniversalTransitionOption(
        unsafeJso<UniversalTransitionOption> {
            enabled = true
            delay = { index, count -> (index / count) * 1000.0 }
        },
    )

/**
 * One series of the transition page.
 *
 * Upstream builds every option with the same object literal and only swaps `type`; a bar series
 * simply ignores the pictorial members, so this port keeps the branch explicit instead.
 */
internal fun transitionSeries(
    id: String,
    name: String,
    values: DoubleArray,
    bar: Boolean,
    symbol: String?,
    barGap: StringOrDouble?,
    barCategoryGap: StringOrDouble?,
): SeriesOption =
    if (bar) {
        barSeries {
            this.id = id
            this.name = name
            label = vehicleLabel()
            this.barGap = barGap
            this.barCategoryGap = barCategoryGap
            typedData = numbers(*values)
            universalTransition = vehicleTransition()
        }
    } else {
        pictorialBarSeries {
            this.id = id
            this.name = name
            label = vehicleLabel()
            symbolRepeat = BooleanOrDoubleOrString(true)
            symbolSize = StringOrDoubleOrArray(arrayOf(StringOrDouble("80%"), StringOrDouble("60%")))
            this.barGap = barGap
            this.barCategoryGap = barCategoryGap
            typedData = vehicleItems(values, symbol)
            universalTransition = vehicleTransition()
        }
    }

/** One of the three options of `pictorialBar-bar-transition`. */
internal fun transitionOption(bar: Boolean, symbol: String? = null): EChartsOption =
    vehicleOption(
        arrayOf(
            transitionSeries("bar1", "2015", VEHICLE_2015, bar, symbol, null, StringOrDouble("40%")),
            transitionSeries("bar2", "2016", VEHICLE_2016, bar, symbol, StringOrDouble("10%"), null),
        ),
        durationUpdate = 500.0,
    )

/** The upper bound of the water-content bars: a full body is 150 units tall. */
internal const val BODY_MAX = 150.0

/** The five slots of `pictorialBar-body-fill`. */
internal val BODY_NAMES = arrayOf("a", "b", "c", "d", "e")

/** The silhouette of each slot of [BODY_NAMES]. */
internal val BODY_PATHS =
    arrayOf(BODY_0_PATH, BODY_1_PATH, BODY_2_PATH, BODY_3_PATH, BODY_4_PATH)

/** The water content of `typeA`. */
internal val BODY_A = doubleArrayOf(123.0, 34.0, 101.0, 89.0, 72.0)

/** The water content of `typeB`. */
internal val BODY_B = doubleArrayOf(12.0, 44.0, 131.0, 33.0, 142.0)

/** The shared label of the water-content bars: the percentage above the head. */
internal fun bodyLabel(): LabelOption =
    unsafeJso {
        show = true
        position = LabelPosition("top")
        offset = arrayOf(0.0, -20.0)
        formatter =
            LabelFormatter { params ->
                fixed((numeric(params.value) / BODY_MAX) * 100.0, 0) + "%"
            }
        fontSize = StringOrDouble(18.0)
        fontFamily = "Arial"
    }

/** The min and max lines both water-content series draw behind their bars. */
internal fun bodyMarkLine(): MarkLineOption =
    unsafeJso {
        symbol = StringOrStringArray("none")
        lineStyle = unsafeJso<LineStyleOption> { opacity = 0.3 }
        data =
            arrayOf(
                MarkLineDataItem(
                    unsafeJso<MarkLine1DDataItemOption> {
                        type = MarkerStatisticTypes.max
                        label = unsafeJso<MarkLineLabelOption> { formatter = LabelFormatter("max: {c}") }
                    },
                ),
                MarkLineDataItem(
                    unsafeJso<MarkLine1DDataItemOption> {
                        type = MarkerStatisticTypes.min
                        label = unsafeJso<MarkLineLabelOption> { formatter = LabelFormatter("min: {c}") }
                    },
                ),
            )
    }

/** One clipped body: the value cuts the silhouette at the matching height. */
internal fun bodySeries(name: String, values: DoubleArray): SeriesOption =
    pictorialBarSeries {
        this.name = name
        symbolClip = true
        symbolBoundingData = DoubleOrDoubleArray(BODY_MAX)
        label = bodyLabel()
        markLine = bodyMarkLine()
        z = 10.0
        typedData =
            dataItems(
                *Array(BODY_PATHS.size) { index -> vehicleItem(values[index], BODY_PATHS[index]) },
            )
    }

/** The grey silhouette behind the two types, drawn at the full [BODY_MAX] height. */
internal fun fullBodySeries(): SeriesOption =
    pictorialBarSeries {
        name = "full"
        symbolBoundingData = DoubleOrDoubleArray(BODY_MAX)
        animationDuration = AnimationDuration(0.0)
        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ccc") }
        typedData =
            dataItems(
                *Array(BODY_PATHS.size) { index -> vehicleItem(1.0, BODY_PATHS[index]) },
            )
    }

/** The hill silhouette of `pictorialBar-velocity`, copied verbatim from upstream. */
internal const val VELOCITY_HILL = "path://M0,10 L10,10 C5.5,10 5.5,5 5,0 C4.5,5 4.5,10 0,10 z"

/** The categories of `pictorialBar-velocity`, from the fastest to the slowest. */
internal val VELOCITY_NAMES = arrayOf("驯鹿", "火箭", "飞机", "高铁", "轮船", "汽车", "跑步", "步行")

/** The values of `pictorialBar-velocity`. */
internal val VELOCITY_VALUES = doubleArrayOf(123.0, 60.0, 25.0, 18.0, 12.0, 9.0, 2.0, 1.0)

/** The silhouette of each entry of [VELOCITY_NAMES]. */
internal val VELOCITY_PATHS =
    arrayOf(REINDEER_PATH, ROCKET_PATH, PLANE_PATH, TRAIN_PATH, SHIP_PATH, CAR_PATH, RUN_PATH, WALK_PATH)

/** The `symbolSize` of each entry of [VELOCITY_NAMES]. */
internal val VELOCITY_SIZES =
    arrayOf(
        doubleArrayOf(60.0, 60.0),
        doubleArrayOf(50.0, 60.0),
        doubleArrayOf(65.0, 35.0),
        doubleArrayOf(50.0, 30.0),
        doubleArrayOf(50.0, 35.0),
        doubleArrayOf(40.0, 30.0),
        doubleArrayOf(40.0, 50.0),
        doubleArrayOf(40.0, 50.0),
    )

/** One glyph of the velocity page: a silhouette sized per category. */
internal fun velocityGlyph(index: Int): PictorialBarDataItem =
    PictorialBarDataItem(
        unsafeJso<PictorialBarDataItemOption> {
            value = OptionDataValue(VELOCITY_VALUES[index])
            symbol = VELOCITY_PATHS[index]
            symbolSize =
                StringOrDoubleOrArray(
                    arrayOf(
                        StringOrDouble(VELOCITY_SIZES[index][0]),
                        StringOrDouble(VELOCITY_SIZES[index][1]),
                    ),
                )
        },
    )

/** The tooltip of `pictorialBar-velocity`: the category and its value. */
internal fun velocityTooltip(): TooltipOption =
    unsafeJso {
        trigger = TooltipTriggers.axis
        axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.none }
        formatter =
            TooltipFormatter { params ->
                val items = params.unsafeCast<Array<CallbackDataParams>>()
                TooltipContent(items[0].name + ": " + valueText(items[0].value))
            }
    }
