package demo.examples.candlestick

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.chord.column
import demo.examples.matrix.dimension
import demo.examples.shared.LARGE_CANDLE_COUNT
import demo.examples.shared.axisLabel
import demo.examples.shared.largeCandleSource
import demo.examples.shared.thousands
import demo.examples.shared.toolbox

/** `candlestick-large` belongs to the candlestick family; the source file is shared with the two above. */
private val candlestickLargeCategory = Category(Groups.CANDLESTICK, "examples/candlestick/CandlestickLarge.kt")

val candlestickLargeEntry: CatalogEntry =
    candlestickLargeCategory.chart(
            "candlestick-large",
            "大数据量K线图",
            "Large Scale Candlestick",
            "二十万根分钟级 K 线走 dataset，成交量挂在第二个网格里用 large 模式画柱；" +
                "涨跌靠额外的 sign 列与隐藏的 visualMap 上色。",
            "Two hundred thousand minute candles driven by a `dataset`, with the volume in a second " +
                "grid drawn in large mode; an extra sign column and a hidden visual map paint the up " +
                "and down candles.",
        ) {
            val source = largeCandleSource()
            unsafeJso<EChartsOption> {
                dataset = one(unsafeJso<DatasetOption> { this.source = source })
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Data Amount: ${thousands(LARGE_CANDLE_COUNT)}"
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.line }
                        },
                    )
                toolbox =
                    one(
                        unsafeJso<ToolboxOption> {
                            feature =
                                unsafeJso<ToolboxFeature> {
                                    dataZoom =
                                        unsafeJso<ToolboxDataZoomFeatureOption> {
                                            yAxisIndex = ModelFinderIndexQuery("none")
                                        }
                                }
                        },
                    )
                grid =
                    arrayOf(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble("10%")
                            bottom = StringOrDouble(200.0)
                        },
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble("10%")
                            height = StringOrDouble(80.0)
                            bottom = StringOrDouble(80.0)
                        },
                    )
                xAxis =
                    arrayOf(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            boundaryGap = BooleanOrStringArray(false)
                            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                        },
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            gridIndex = 1.0
                            boundaryGap = BooleanOrStringArray(false)
                            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                        },
                    )
                yAxis =
                    arrayOf(
                        unsafeJso<YAxisOption> {
                            scale = true
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                        unsafeJso<YAxisOption> {
                            scale = true
                            gridIndex = 1.0
                            splitNumber = 2.0
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
                            start = 10.0
                            end = 100.0
                        },
                        unsafeJso<DataZoomOption> {
                            show = true
                            xAxisIndex = IndexOrIndexArray(arrayOf(0.0, 1.0))
                            type = DataZoomTypes.slider
                            bottom = StringOrDouble(10.0)
                            start = 10.0
                            end = 100.0
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = false
                            seriesIndex = IndexOrIndexArray(1.0)
                            dimension = 6.0
                            pieces =
                                arrayOf(
                                    unsafeJso<VisualPiece> {
                                        value = 1.0
                                        color = StringOrStringArray(UP_COLOR)
                                    },
                                    unsafeJso<VisualPiece> {
                                        value = -1.0
                                        color = StringOrStringArray(DOWN_COLOR)
                                    },
                                )
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        candlestickSeries {
                            itemStyle =
                                unsafeJso<CandlestickItemStyleOption> {
                                    color = ZRColor(UP_COLOR)
                                    color0 = ZRColor(DOWN_COLOR)
                                    borderColor = ZRColor(UP_BORDER_COLOR)
                                    borderColor0 = ZRColor(DOWN_BORDER_COLOR)
                                }
                            encode =
                                unsafeJso<OptionEncode> {
                                    x = OptionEncodeValue(DimensionLoose(0.0))
                                    y =
                                        OptionEncodeValue(
                                            arrayOf(
                                                DimensionLoose(1.0),
                                                DimensionLoose(4.0),
                                                DimensionLoose(3.0),
                                                DimensionLoose(2.0),
                                            ),
                                        )
                                }
                        },
                        barSeries {
                            name = "Volumn"
                            xAxisIndex = 1.0
                            yAxisIndex = 1.0
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#7fbe9e") }
                            large = true
                            encode =
                                unsafeJso<OptionEncode> {
                                    x = OptionEncodeValue(DimensionLoose(0.0))
                                    y = OptionEncodeValue(DimensionLoose(5.0))
                                }
                        },
                    )
            }
        }
