package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor
import zrender.ZRStyleProps

/*
 * Candlestick examples ported from `public/examples/ts/candlestick-*.ts` of apache/echarts-examples.
 *
 * `candlestick-sh` and `candlestick-sh-2015` carry 88 and 246 rows of inline data, so they live in
 * `CandlestickLongTail.kt` with the rest of the transcribed tables.
 *
 * `candlestick-touch` only *targets* touch screens: it turns the tooltip trigger off and lets the
 * axis-pointer handles do the seeking, and both are drawn by ECharts itself, so the page has no
 * pointer logic of its own and lives here as a static chart.
 */

private val candlestick = Category(Groups.CANDLESTICK, "examples/Candlestick.kt")

/** One `[open, close, lowest, highest]` row. */
private fun candle(open: Double, close: Double, lowest: Double, highest: Double): CandlestickDataItem =
    CandlestickDataItem(row(open, close, lowest, highest))

val candlestickEntries: List<CatalogEntry> =
    listOf(
        candlestick.chart("candlestick-simple", "基础 K 线图", "Basic Candlestick") {
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings("2017-10-24", "2017-10-25", "2017-10-26", "2017-10-27")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        candlestickSeries {
                            typedData =
                                dataItems(
                                    candle(20.0, 34.0, 10.0, 38.0),
                                    candle(40.0, 35.0, 30.0, 50.0),
                                    candle(31.0, 38.0, 33.0, 44.0),
                                    candle(38.0, 15.0, 5.0, 42.0),
                                )
                        },
                    )
            }
        },
        candlestick.chart(
            "candlestick-touch",
            "触屏上的坐标轴指示器",
            "Axis Pointer Link and Touch",
            summaryZh = "上下两个联动网格画日 K 与成交量；tooltip 由拖动的坐标轴指示器触发，横轴指示器联动两个网格。",
            summaryEn = "Daily candles over a volume grid; the tooltip follows the dragged axis pointer, and the x pointers of both grids stay linked.",
        ) {
            candlestickTouchOption()
        },
    )

// ---------------------------------------------------------------------------------------------
// candlestick-touch
// ---------------------------------------------------------------------------------------------

/** Candle colors of `candlestick-touch`, copied from the upstream `colorList`. */
private val CANDLESTICK_TOUCH_COLORS =
    arrayOf(
        "#c23531", "#2f4554", "#61a0a8", "#d48265", "#91c7ae", "#749f83", "#ca8622", "#bda29a",
        "#6e7074", "#546570", "#c4ccd3",
    )

/** Font of the moving-average hints drawn by the graphic component. */
private const val CANDLESTICK_TOUCH_FONT = "bold 12px Sans-serif"

/** Slider handle of `candlestick-touch`, copied verbatim from upstream. */
private const val CANDLESTICK_TOUCH_HANDLE_ICON =
    "path://M10.7,11.9H9.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4h1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z"

/**
 * Closing-price averages of the last [window] days, the upstream `calculateMA`.
 *
 * The first `window - 1` points are `"-"`, the value ECharts skips; the averages are rounded to two
 * decimals into strings, exactly like the upstream `toFixed(2)`.
 */
private fun touchMovingAverage(window: Int): Array<LineDataItem?> =
    Array(CANDLESTICK_TOUCH_DATA.size) { index ->
        if (index < window) {
            LineDataItem(OptionDataValue("-"))
        } else {
            var sum = 0.0
            for (offset in 0 until window) {
                sum += CANDLESTICK_TOUCH_DATA[index - offset][1]
            }
            LineDataItem(OptionDataValue(fixed(sum / window, 2)))
        }
    }

/** The volume bar of the lower grid of `candlestick-touch`. */
private fun touchVolumeSeries(): BarSeriesOption =
    barSeries {
        name = "Volume"
        xAxisIndex = 1.0
        yAxisIndex = 1.0
        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#7fbe9e") }
        emphasis =
            unsafeJso<EmphasisOption> {
                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#140") }
            }
        typedData = numbers(*CANDLESTICK_TOUCH_VOLUMES)
    }

/** The daily candles of `candlestick-touch`. */
private fun touchCandleSeries(): CandlestickSeriesOption =
    candlestickSeries {
        name = "日K"
        typedData =
            dataItems(
                *Array(CANDLESTICK_TOUCH_DATA.size) { index ->
                    val day = CANDLESTICK_TOUCH_DATA[index]
                    candle(day[0], day[1], day[2], day[3])
                },
            )
        typedItemStyle =
            unsafeJso<CandlestickItemStyleOption> {
                color = ZRColor("#ef232a")
                color0 = ZRColor("#14b143")
                borderColor = ZRColor("#ef232a")
                borderColor0 = ZRColor("#14b143")
            }
        emphasis =
            unsafeJso<EmphasisOption> {
                itemStyle =
                    unsafeJso<CandlestickItemStyleOption> {
                        color = ZRColor("black")
                        color0 = ZRColor("#444")
                        borderColor = ZRColor("black")
                        borderColor0 = ZRColor("#444")
                    }
            }
    }

/** One moving-average line of `candlestick-touch`. */
private fun touchMovingAverageSeries(name: String, window: Int): LineSeriesOption =
    lineSeries {
        this.name = name
        showSymbol = false
        smooth = BooleanOrDouble(true)
        typedData = touchMovingAverage(window)
        lineStyle = unsafeJso<LineStyleOption> { width = 1.0 }
    }

/** The date axis of the candle grid; labels are formatted as `MM-dd`. */
private fun touchCandleAxis(): XAxisOption =
    unsafeJso {
        type = AxisTypes.category
        data = strings(*CANDLESTICK_TOUCH_DATES)
        boundaryGap = BooleanOrStringArray(false)
        axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#777") } }
        axisLabel =
            unsafeJso<AxisLabelOption> {
                // A category axis hands the formatter the category string itself, not a params object.
                formatter = LabelFormatter { params -> ECharts.format.formatTime("MM-dd", params.unsafeCast<JsAny>()) }
            }
        min = AxisMinMax("dataMin")
        max = AxisMinMax("dataMax")
        axisPointer = unsafeJso<AxisPointerOption> { show = BooleanOrString(true) }
    }

/** The date axis of the volume grid; its shadow handle is what the page is named after. */
private fun touchVolumeAxisX(): XAxisOption =
    unsafeJso {
        type = AxisTypes.category
        gridIndex = 1.0
        data = strings(*CANDLESTICK_TOUCH_DATES)
        boundaryGap = BooleanOrStringArray(false)
        splitLine = unsafeJso<SplitLineOption> { show = false }
        axisLabel = unsafeJso<AxisLabelOption> { show = false }
        axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
        axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#777") } }
        min = AxisMinMax("dataMin")
        max = AxisMinMax("dataMax")
        axisPointer =
            unsafeJso<AxisPointerOption> {
                type = AxisPointerTypes.shadow
                label = unsafeJso<AxisPointerLabelOption> { show = false }
                triggerTooltip = true
                handle =
                    unsafeJso<AxisPointerHandleOption> {
                        show = true
                        margin = 30.0
                        color = "#B80C00"
                    }
            }
    }

/** The price axis of the candle grid; the labels sit inside the grid. */
private fun touchPriceAxis(): YAxisOption =
    unsafeJso {
        scale = true
        splitNumber = 2.0
        axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#777") } }
        splitLine = unsafeJso<SplitLineOption> { show = true }
        axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
        axisLabel =
            unsafeJso<AxisLabelOption> {
                inside = true
                formatter = LabelFormatter("{value}\n")
            }
    }

/** The volume axis of the lower grid; it carries no labels of its own. */
private fun touchVolumeAxisY(): YAxisOption =
    unsafeJso {
        scale = true
        gridIndex = 1.0
        splitNumber = 2.0
        axisLabel = unsafeJso<AxisLabelOption> { show = false }
        axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
        axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
        splitLine = unsafeJso<SplitLineOption> { show = false }
    }

/**
 * The MA5 / MA10 / MA20 hints the graphic component draws under the legend.
 *
 * Upstream gives each text element an id and a style but no text, so the port keeps them as
 * placeholders with the same ids.
 */
private fun touchAverageHints(): GraphicElement =
    unsafeJso {
        type = "group"
        left = StringOrDouble("center")
        top = StringOrDouble(70.0)
        width = 300.0
        bounding = GraphicBoundings.raw
        children =
            arrayOf(
                unsafeJso<GraphicElement> {
                    id = "MA5"
                    type = "text"
                    style = unsafeJso<ZRStyleProps> { fill = ZRColor(CANDLESTICK_TOUCH_COLORS[1]); font = CANDLESTICK_TOUCH_FONT }
                    left = StringOrDouble(0.0)
                },
                unsafeJso<GraphicElement> {
                    id = "MA10"
                    type = "text"
                    style = unsafeJso<ZRStyleProps> { fill = ZRColor(CANDLESTICK_TOUCH_COLORS[2]); font = CANDLESTICK_TOUCH_FONT }
                    left = StringOrDouble("center")
                },
                unsafeJso<GraphicElement> {
                    id = "MA20"
                    type = "text"
                    style = unsafeJso<ZRStyleProps> { fill = ZRColor(CANDLESTICK_TOUCH_COLORS[3]); font = CANDLESTICK_TOUCH_FONT }
                    right = StringOrDouble(0.0)
                },
            )
    }

/** The option of `candlestick-touch`. */
private fun candlestickTouchOption(): EChartsOption =
    unsafeJso {
        animation = false
        color = Array(CANDLESTICK_TOUCH_COLORS.size) { index -> ZRColor(CANDLESTICK_TOUCH_COLORS[index]) }
        title =
            one(
                unsafeJso<TitleOption> {
                    left = StringOrDouble("center")
                    text = "Candlestick on Mobile"
                },
            )
        legend =
            one(
                unsafeJso<LegendOption> {
                    top = StringOrDouble(30.0)
                    data = legendNames("日K", "MA5", "MA10", "MA20", "MA30")
                },
            )
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    triggerOn = TooltipTriggerOns.none
                    transitionDuration = 0.0
                    confine = true
                    borderRadius = 4.0
                    borderWidth = 1.0
                    borderColor = ZRColor("#333")
                    backgroundColor = ZRColor("rgba(255,255,255,0.9)")
                    textStyle =
                        unsafeJso<TextStyleOption> {
                            fontSize = StringOrDouble(12.0)
                            color = "#333"
                        }
                    // The box sits under the legend and snaps to the left or right half of the view.
                    position =
                        TooltipPosition { point, _, _, _, size ->
                            val box = unsafeJso<TooltipBoxLayoutOption> { top = StringOrDouble(60.0) }
                            if (point[0] < size.viewSize[0] / 2) {
                                box.left = StringOrDouble(5.0)
                            } else {
                                box.right = StringOrDouble(5.0)
                            }
                            TooltipPosition(box)
                        }
                },
            )
        axisPointer =
            one(
                unsafeJso<AxisPointerOption> {
                    link =
                        arrayOf(
                            unsafeJso<AxisPointerLink> {
                                xAxisIndex = ModelFinderIndexQuery(arrayOf(0.0, 1.0))
                            },
                        )
                },
            )
        dataZoom =
            arrayOf(
                unsafeJso<DataZoomOption> {
                    type = DataZoomTypes.slider
                    xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
                    realtime = false
                    start = 20.0
                    end = 70.0
                    top = StringOrDouble(65.0)
                    height = StringOrDouble(20.0)
                    handleIcon = CANDLESTICK_TOUCH_HANDLE_ICON
                    handleSize = StringOrDouble("120%")
                },
                unsafeJso<DataZoomOption> {
                    type = DataZoomTypes.inside
                    xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
                    start = 40.0
                    end = 70.0
                    top = StringOrDouble(30.0)
                    height = StringOrDouble(20.0)
                },
            )
        xAxis = arrayOf(touchCandleAxis(), touchVolumeAxisX())
        yAxis = arrayOf(touchPriceAxis(), touchVolumeAxisY())
        grid =
            arrayOf(
                unsafeJso<GridOption> {
                    left = StringOrDouble(20.0)
                    right = StringOrDouble(20.0)
                    top = StringOrDouble(110.0)
                    height = StringOrDouble(120.0)
                },
                unsafeJso<GridOption> {
                    left = StringOrDouble(20.0)
                    right = StringOrDouble(20.0)
                    height = StringOrDouble(40.0)
                    top = StringOrDouble(260.0)
                },
            )
        graphic = arrayOf(GraphicComponentLooseOption(touchAverageHints()))
        series =
            arrayOf<SeriesOption>(
                touchVolumeSeries(),
                touchCandleSeries(),
                touchMovingAverageSeries("MA5", 5),
                touchMovingAverageSeries("MA10", 10),
                touchMovingAverageSeries("MA20", 20),
            )
    }

/** The trading calendar of `candlestick-touch`, the upstream `dates`. */
private val CANDLESTICK_TOUCH_DATES =
    arrayOf(
        "2016-03-29", "2016-03-30", "2016-03-31", "2016-04-01",
        "2016-04-04", "2016-04-05", "2016-04-06", "2016-04-07",
        "2016-04-08", "2016-04-11", "2016-04-12", "2016-04-13",
        "2016-04-14", "2016-04-15", "2016-04-18", "2016-04-19",
        "2016-04-20", "2016-04-21", "2016-04-22", "2016-04-25",
        "2016-04-26", "2016-04-27", "2016-04-28", "2016-04-29",
        "2016-05-02", "2016-05-03", "2016-05-04", "2016-05-05",
        "2016-05-06", "2016-05-09", "2016-05-10", "2016-05-11",
        "2016-05-12", "2016-05-13", "2016-05-16", "2016-05-17",
        "2016-05-18", "2016-05-19", "2016-05-20", "2016-05-23",
        "2016-05-24", "2016-05-25", "2016-05-26", "2016-05-27",
        "2016-05-31", "2016-06-01", "2016-06-02", "2016-06-03",
        "2016-06-06", "2016-06-07", "2016-06-08", "2016-06-09",
        "2016-06-10", "2016-06-13", "2016-06-14", "2016-06-15",
        "2016-06-16", "2016-06-17", "2016-06-20", "2016-06-21",
        "2016-06-22",
    )

/** One `[open, close, lowest, highest]` row per day, the first four columns of the upstream `data`. */
private val CANDLESTICK_TOUCH_DATA =
    arrayOf(
        doubleArrayOf(17512.58, 17633.11, 17434.27, 17642.81),
        doubleArrayOf(17652.36, 17716.66, 17652.36, 17790.11),
        doubleArrayOf(17716.05, 17685.09, 17669.72, 17755.7),
        doubleArrayOf(17661.74, 17792.75, 17568.02, 17811.48),
        doubleArrayOf(17799.39, 17737.0, 17710.67, 17806.38),
        doubleArrayOf(17718.03, 17603.32, 17579.56, 17718.03),
        doubleArrayOf(17605.45, 17716.05, 17542.54, 17723.55),
        doubleArrayOf(17687.28, 17541.96, 17484.23, 17687.28),
        doubleArrayOf(17555.39, 17576.96, 17528.16, 17694.51),
        doubleArrayOf(17586.48, 17556.41, 17555.9, 17731.63),
        doubleArrayOf(17571.34, 17721.25, 17553.57, 17744.43),
        doubleArrayOf(17741.66, 17908.28, 17741.66, 17918.35),
        doubleArrayOf(17912.25, 17926.43, 17885.44, 17962.14),
        doubleArrayOf(17925.95, 17897.46, 17867.41, 17937.65),
        doubleArrayOf(17890.2, 18004.16, 17848.22, 18009.53),
        doubleArrayOf(18012.1, 18053.6, 17984.43, 18103.46),
        doubleArrayOf(18059.49, 18096.27, 18031.21, 18167.63),
        doubleArrayOf(18092.84, 17982.52, 17963.89, 18107.29),
        doubleArrayOf(17985.05, 18003.75, 17909.89, 18026.85),
        doubleArrayOf(17990.94, 17977.24, 17855.55, 17990.94),
        doubleArrayOf(17987.38, 17990.32, 17934.17, 18043.77),
        doubleArrayOf(17996.14, 18041.55, 17920.26, 18084.66),
        doubleArrayOf(18023.88, 17830.76, 17796.55, 18035.73),
        doubleArrayOf(17813.09, 17773.64, 17651.98, 17814.83),
        doubleArrayOf(17783.78, 17891.16, 17773.71, 17912.35),
        doubleArrayOf(17870.75, 17750.91, 17670.88, 17870.75),
        doubleArrayOf(17735.02, 17651.26, 17609.01, 17738.06),
        doubleArrayOf(17664.48, 17660.71, 17615.82, 17736.11),
        doubleArrayOf(17650.3, 17740.63, 17580.38, 17744.54),
        doubleArrayOf(17743.85, 17705.91, 17668.38, 17783.16),
        doubleArrayOf(17726.66, 17928.35, 17726.66, 17934.61),
        doubleArrayOf(17919.03, 17711.12, 17711.05, 17919.03),
        doubleArrayOf(17711.12, 17720.5, 17625.38, 17798.19),
        doubleArrayOf(17711.12, 17535.32, 17512.48, 17734.74),
        doubleArrayOf(17531.76, 17710.71, 17531.76, 17755.8),
        doubleArrayOf(17701.46, 17529.98, 17469.92, 17701.46),
        doubleArrayOf(17501.28, 17526.62, 17418.21, 17636.22),
        doubleArrayOf(17514.16, 17435.4, 17331.07, 17514.16),
        doubleArrayOf(17437.32, 17500.94, 17437.32, 17571.75),
        doubleArrayOf(17507.04, 17492.93, 17480.05, 17550.7),
        doubleArrayOf(17525.19, 17706.05, 17525.19, 17742.59),
        doubleArrayOf(17735.09, 17851.51, 17735.09, 17891.71),
        doubleArrayOf(17859.52, 17828.29, 17803.82, 17888.66),
        doubleArrayOf(17826.85, 17873.22, 17824.73, 17873.22),
        doubleArrayOf(17891.5, 17787.2, 17724.03, 17899.24),
        doubleArrayOf(17754.55, 17789.67, 17664.79, 17809.18),
        doubleArrayOf(17789.05, 17838.56, 17703.55, 17838.56),
        doubleArrayOf(17799.8, 17807.06, 17689.68, 17833.17),
        doubleArrayOf(17825.69, 17920.33, 17822.81, 17949.68),
        doubleArrayOf(17936.22, 17938.28, 17936.22, 18003.23),
        doubleArrayOf(17931.91, 18005.05, 17931.91, 18016.0),
        doubleArrayOf(17969.98, 17985.19, 17915.88, 18005.22),
        doubleArrayOf(17938.82, 17865.34, 17812.34, 17938.82),
        doubleArrayOf(17830.5, 17732.48, 17731.35, 17893.28),
        doubleArrayOf(17710.77, 17674.82, 17595.79, 17733.92),
        doubleArrayOf(17703.65, 17640.17, 17629.01, 17762.96),
        doubleArrayOf(17602.23, 17733.1, 17471.29, 17754.91),
        doubleArrayOf(17733.44, 17675.16, 17602.78, 17733.44),
        doubleArrayOf(17736.87, 17804.87, 17736.87, 17946.36),
        doubleArrayOf(17827.33, 17829.73, 17799.8, 17877.84),
        doubleArrayOf(17832.67, 17780.83, 17770.36, 17920.16),
    )

/** Volume of each day, the upstream `volumes` (also the fifth column of `data`). */
private val CANDLESTICK_TOUCH_VOLUMES =
    doubleArrayOf(
        8616.0, 7933.0, 1026.0, 10489.0, 8523.0, 11523.0,
        9941.0, 9012.0, 7999.0, 1071.0, 8102.0, 9171.0,
        8451.0, 11816.0, 8939.0, 8982.0, 10021.0, 10272.0,
        13412.0, 8377.0, 9257.0, 10909.0, 10092.0, 13667.0,
        801.0, 9706.0, 9502.0, 8153.0, 8002.0, 8559.0,
        7579.0, 8739.0, 8856.0, 8664.0, 8844.0, 10326.0,
        7912.0, 9553.0, 11199.0, 8779.0, 8648.0, 7918.0,
        6894.0, 7319.0, 14739.0, 7853.0, 7556.0, 8227.0,
        7187.0, 7875.0, 7126.0, 6969.0, 9054.0, 10169.0,
        9374.0, 9413.0, 9195.0, 24868.0, 9938.0, 8513.0,
        8944.0,
    )
