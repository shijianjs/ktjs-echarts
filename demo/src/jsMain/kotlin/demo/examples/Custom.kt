package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.DataZoomTypes
import echarts.ECharts
import echarts.FilterModes
import echarts.TooltipTriggers
import echarts.VisualProps
import echarts.options.AxisMinMax
import echarts.options.BarGridLayoutOptionForCustomSeries
import echarts.options.BooleanOrString
import echarts.options.CoordinateSystemDataCoord
import echarts.options.CustomBaseElementOption
import echarts.options.CustomElementOption
import echarts.options.CustomGroupOption
import echarts.options.CustomPathOption
import echarts.options.DataZoomOption
import echarts.options.DimensionDefinitionLoose
import echarts.options.DimensionLoose
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.LegendOption
import echarts.options.OptionDataItem
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.OptionEncode
import echarts.options.OptionEncodeValue
import echarts.options.ScatterDataItem
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.TooltipFormatter
import echarts.options.TooltipFormatterCallback
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.customSeries
import echarts.options.numbers
import echarts.options.one
import echarts.options.scatterSeries
import echarts.options.strings
import js.objects.unsafeJso
import kotlin.js.js
import zrender.RectLike
import zrender.ZRColor
import zrender.ZRStyleProps

/*
 * Custom series examples ported from `public/examples/ts/custom-*.ts` of
 * apache/echarts-examples.
 *
 * A custom series draws its data with `renderItem`, which receives the data item and a small API
 * (`value` / `coord` / `size` / `layout` / `style` / `visual`) and returns an element tree. Every
 * example below returns `CustomPathOption` / `CustomGroupOption` values built with the typed
 * element options of the wrapper.
 *
 * `Math.random()` of the originals is replaced by a fixed sequence (see [sequence]).
 *
 * Not ported:
 *
 * - `custom-ohlc` and `custom-wind` read their data from the example server (`$.get` / `$.getJSON`);
 * - `custom-gauge` and `custom-polar-heatmap` need the polar centre `params.coordSys.cx` / `cy`,
 *   which the wrapper does not model yet;
 * - `custom-spiral-race` / `custom-gantt-flight` / `custom-calendar-icon` / `custom-hexbin` are
 *   long animation or icon demos left for a later batch.
 */

private val custom = Category(Groups.CUSTOM, "examples/Custom.kt")

/** A reproducible replacement for `Math.random()`; see the calendar examples. */
private fun sequence(seed: Long): () -> Double {
    var state = seed
    return {
        state = state * 6364136223846793005L + 1442695040888963407L and Long.MAX_VALUE
        (state % 100000L).toDouble() / 100000.0
    }
}

/** The dimension index [index] of a data item, as `api.value` / `encode` take it. */
private fun dim(index: Int): DimensionLoose = DimensionLoose(index.toDouble())

/** A data coordinate of a cartesian coordinate system. */
private fun coord(vararg values: OptionDataValue?): CoordinateSystemDataCoord =
    CoordinateSystemDataCoord(arrayOf(*values))

/** A data extent handed to `api.size`. */
private fun extent(vararg values: OptionDataValue?): OptionDataValueOrArray =
    OptionDataValueOrArray(arrayOf(*values))

/** A `line` element of a custom element tree. */
private fun lineElement(style: ZRStyleProps, block: zrender.ZRShapeProps.() -> Unit): CustomPathOption =
    unsafeJso {
        type = "line"
        this.style = style
        shape = unsafeJso(block)
    }

/** A `group` element that holds [elements]. */
private fun groupElement(vararg elements: CustomBaseElementOption): CustomGroupOption =
    unsafeJso {
        type = "group"
        children = Array<CustomElementOption>(elements.size) { elements[it] }
    }

/** `encode: { x: <x>, y: <y> }`. */
private fun xyEncode(x: OptionEncodeValue, y: OptionEncodeValue): OptionEncode =
    unsafeJso {
        this.x = x
        this.y = y
    }

/** The `dimensions` of the error scatter example. */
private val errorDimensions =
    arrayOf(
        DimensionDefinitionLoose("name"),
        DimensionDefinitionLoose("Price"),
        DimensionDefinitionLoose("Prime cost"),
        DimensionDefinitionLoose("Prime cost min"),
        DimensionDefinitionLoose("Prime cost max"),
        DimensionDefinitionLoose("Price min"),
        DimensionDefinitionLoose("Price max"),
    )

val customEntries: List<CatalogEntry> =
    listOf(
        custom.chart("custom-profit", "利润分布直方图", "Profit") {
            val colors = listOf("#4f81bd", "#c0504d", "#9bbb59", "#604a7b", "#948a54", "#e46c0b")
            val bars =
                listOf(
                    row(10.0, 16.0, 3.0, "A"),
                    row(16.0, 18.0, 15.0, "B"),
                    row(18.0, 26.0, 12.0, "C"),
                    row(26.0, 32.0, 22.0, "D"),
                    row(32.0, 56.0, 7.0, "E"),
                    row(56.0, 62.0, 17.0, "F"),
                )
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Profit"; left = StringOrDouble("center") })
                tooltip = one(unsafeJso<TooltipOption> {})
                xAxis = one(unsafeJso<XAxisOption> { scale = true })
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        customSeries {
                            typedData =
                                Array<OptionDataItem?>(bars.size) { index ->
                                    styledItem(bars[index], colors[index])
                                }
                            label = unsafeJso<LabelOption> { show = true; position = LabelPosition("top") }
                            dimensions =
                                arrayOf(
                                    DimensionDefinitionLoose("from"),
                                    DimensionDefinitionLoose("to"),
                                    DimensionDefinitionLoose("profit"),
                                )
                            encode =
                                unsafeJso {
                                    this.x = OptionEncodeValue(arrayOf(dim(0), dim(1)))
                                    this.y = OptionEncodeValue(dim(2))
                                    tooltip = OptionEncodeValue(arrayOf(dim(0), dim(1), dim(2)))
                                    itemName = OptionEncodeValue(dim(3))
                                }
                            renderItem = { _, api ->
                                val from = api.value(dim(0))
                                val to = api.value(dim(1))
                                val profit = api.value(dim(2))
                                val start = api.coord(coord(from, profit))
                                val size =
                                    numberArray(
                                        api.size(
                                            extent(
                                                OptionDataValue(numeric(to) - numeric(from)),
                                                profit,
                                            ),
                                        ),
                                    )
                                unsafeJso<CustomPathOption> {
                                    type = "rect"
                                    shape =
                                        unsafeJso {
                                            x = start[0]
                                            y = start[1]
                                            width = size[0]
                                            height = size[1]
                                        }
                                    style = api.style()
                                }
                            }
                        },
                    )
            }
        },
        custom.chart("custom-cartesian-polygon", "自定义多边形图", "Custom Cartesian Polygon") {
            val random = sequence(20260611L)
            val points =
                List(7) {
                    doubleRow(kotlin.math.round(random() * 100), kotlin.math.round(random() * 400))
                }
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend = one(unsafeJso<LegendOption> { data = legendNames("bar", "error") })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            filterMode = FilterModes.none
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            filterMode = FilterModes.none
                        },
                    )
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        customSeries {
                            clip = true
                            typedData = Array<OptionDataItem?>(points.size) { OptionDataItem(points[it]) }
                            // The original draws the polygon only once, guarded by a flag it keeps in
                            // `params.context`; drawing it for the first data item has the same effect
                            // and does not mutate the shared render context.
                            renderItem = { params, api ->
                                if (params.dataIndex != 0.0) {
                                    null
                                } else {
                                    val color = textOf(api.visual(VisualProps.color))
                                    unsafeJso<CustomPathOption> {
                                        type = "polygon"
                                        shape =
                                            unsafeJso {
                                                this.points =
                                                    Array(points.size) { index ->
                                                        api.coord(
                                                            coord(
                                                                OptionDataValue(numeric(points[index][0])),
                                                                OptionDataValue(numeric(points[index][1])),
                                                            ),
                                                        )
                                                    }
                                            }
                                        style = api.style(unsafeJso<ZRStyleProps> { fill = ZRColor(color) })
                                    }
                                }
                            }
                        },
                    )
            }
        },
        custom.chart("custom-error-bar", "误差棒图", "Error Bar Chart") {
            val random = sequence(20260612L)
            val categories = List(100) { "category$it" }
            val barValues = mutableListOf<Double>()
            val errorValues =
                List(100) { index ->
                    val value = random() * 1000
                    barValues += kotlin.math.round(value * 100) / 100
                    row(
                        index.toDouble(),
                        kotlin.math.round(kotlin.math.max(0.0, value - random() * 100)),
                        kotlin.math.round(value + random() * 80),
                    )
                }
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                title = one(unsafeJso<TitleOption> { text = "Error bar chart" })
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("bar", "error")
                            top = StringOrDouble(20.0)
                            right = StringOrDouble(30.0)
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            start = 50.0
                            end = 70.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            start = 50.0
                            end = 70.0
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data =
                                strings(*categories.toTypedArray())
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            name = "bar"
                            typedData =
                                numbers(*barValues.toDoubleArray())
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#77bef7") }
                        },
                        customSeries {
                            name = "error"
                            itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 1.5 }
                            encode = xyEncode(OptionEncodeValue(dim(0)), OptionEncodeValue(arrayOf(dim(1), dim(2))))
                            typedData = Array<OptionDataItem?>(errorValues.size) { OptionDataItem(errorValues[it]) }
                            z = 100.0
                            renderItem = { _, api ->
                                val x = api.value(dim(0))
                                val high = api.coord(coord(x, api.value(dim(1))))
                                val low = api.coord(coord(x, api.value(dim(2))))
                                val halfWidth = numberArray(api.size(extent(OptionDataValue(1.0), OptionDataValue(0.0))))[0] * 0.1
                                val style = api.style()
                                groupElement(
                                    lineElement(style) {
                                        x1 = high[0] - halfWidth
                                        y1 = high[1]
                                        x2 = high[0] + halfWidth
                                        y2 = high[1]
                                    },
                                    lineElement(style) {
                                        x1 = high[0]
                                        y1 = high[1]
                                        x2 = low[0]
                                        y2 = low[1]
                                    },
                                    lineElement(style) {
                                        x1 = low[0] - halfWidth
                                        y1 = low[1]
                                        x2 = low[0] + halfWidth
                                        y2 = low[1]
                                    },
                                )
                            }
                        },
                    )
            }
        },
        custom.chart("custom-error-scatter", "散点图误差范围", "Error Scatter on Cartesian") {
            // Prime cost and price of the ACME collection, with the min/max of both.
            val rows =
                listOf(
                    row("Blouse \"Blue Viola\"", 101.88, 99.75, 76.75, 116.75, 69.88, 119.88),
                    row("Dress \"Daisy\"", 155.8, 144.03, 126.03, 156.03, 129.8, 188.8),
                    row("Trousers \"Cutesy Classic\"", 203.25, 173.56, 151.56, 187.56, 183.25, 249.25),
                    row("Dress \"Morning Dew\"", 256.0, 120.5, 98.5, 136.5, 236.0, 279.0),
                    row("Turtleneck \"Dark Chocolate\"", 408.89, 294.75, 276.75, 316.75, 385.89, 427.89),
                    row("Jumper \"Early Spring\"", 427.36, 430.24, 407.24, 452.24, 399.36, 461.36),
                    row("Breeches \"Summer Mood\"", 356.0, 135.5, 123.5, 151.5, 333.0, 387.0),
                    row("Dress \"Mauve Chamomile\"", 406.0, 95.5, 73.5, 111.5, 366.0, 429.0),
                    row("Dress \"Flying Tits\"", 527.36, 503.24, 488.24, 525.24, 485.36, 551.36),
                    row("Dress \"Singing Nightingales\"", 587.36, 543.24, 518.24, 555.24, 559.36, 624.36),
                    row("Sundress \"Cloudy weather\"", 603.36, 407.24, 392.24, 419.24, 581.36, 627.36),
                    row("Sundress \"East motives\"", 633.36, 477.24, 445.24, 487.24, 594.36, 652.36),
                    row("Sweater \"Cold morning\"", 517.36, 437.24, 416.24, 454.24, 488.36, 565.36),
                    row("Trousers \"Lavender Fields\"", 443.36, 387.24, 370.24, 413.24, 412.36, 484.36),
                    row("Jumper \"Coffee with Milk\"", 543.36, 307.24, 288.24, 317.24, 509.36, 574.36),
                    row("Blouse \"Blooming Cactus\"", 790.36, 277.24, 254.24, 295.24, 764.36, 818.36),
                    row("Sweater \"Fluffy Comfort\"", 790.34, 678.34, 660.34, 690.34, 762.34, 824.34),
                )
            val tooltipDimensions = OptionEncodeValue(arrayOf(dim(2), dim(1), dim(3), dim(4), dim(5), dim(6)))
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("bar", "error")
                            top = StringOrDouble(15.0)
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.slider },
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside },
                    )
                grid = one(unsafeJso<GridOption> { top = StringOrDouble(60.0) })
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        scatterSeries {
                            name = "error"
                            dimensions = errorDimensions
                            encode =
                                unsafeJso {
                                    this.x = OptionEncodeValue(dim(2))
                                    this.y = OptionEncodeValue(dim(1))
                                    tooltip = tooltipDimensions
                                    itemName = OptionEncodeValue(dim(0))
                                }
                            typedData = Array<ScatterDataItem?>(rows.size) { ScatterDataItem(rows[it]) }
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#77bef7") }
                        },
                        customSeries {
                            name = "error"
                            dimensions = errorDimensions
                            encode =
                                unsafeJso {
                                    this.x = OptionEncodeValue(arrayOf(dim(2), dim(3), dim(4)))
                                    this.y = OptionEncodeValue(arrayOf(dim(1), dim(5), dim(6)))
                                    tooltip = tooltipDimensions
                                    itemName = OptionEncodeValue(dim(0))
                                }
                            typedData = Array<OptionDataItem?>(rows.size) { OptionDataItem(rows[it]) }
                            z = 100.0
                            renderItem = { params, api ->
                                val encode = params.encode
                                val elements = mutableListOf<CustomBaseElementOption>()
                                for (baseDim in 0..1) {
                                    val otherDim = 1 - baseDim
                                    val baseName = if (baseDim == 0) "x" else "y"
                                    val otherName = if (otherDim == 0) "x" else "y"
                                    val baseIndex = encode?.get(baseName)?.get(0) ?: 0.0
                                    val otherIndex = encode?.get(otherName) ?: arrayOf(0.0)
                                    val baseValue = api.value(DimensionLoose(baseIndex))
                                    val high =
                                        api.coord(
                                            if (baseDim == 0) {
                                                coord(baseValue, api.value(DimensionLoose(otherIndex[1])))
                                            } else {
                                                coord(api.value(DimensionLoose(otherIndex[1])), baseValue)
                                            },
                                        )
                                    val low =
                                        api.coord(
                                            if (baseDim == 0) {
                                                coord(baseValue, api.value(DimensionLoose(otherIndex[2])))
                                            } else {
                                                coord(api.value(DimensionLoose(otherIndex[2])), baseValue)
                                            },
                                        )
                                    val style = api.style()
                                    val halfWidth = 5.0
                                    elements +=
                                        lineElement(style) {
                                            if (baseDim == 0) {
                                                x1 = high[0] - halfWidth
                                                y1 = high[1]
                                                x2 = high[0] + halfWidth
                                                y2 = high[1]
                                            } else {
                                                x1 = high[0]
                                                y1 = high[1] - halfWidth
                                                x2 = high[0]
                                                y2 = high[1] + halfWidth
                                            }
                                        }
                                    elements +=
                                        lineElement(style) {
                                            x1 = high[0]
                                            y1 = high[1]
                                            x2 = low[0]
                                            y2 = low[1]
                                        }
                                    elements +=
                                        lineElement(style) {
                                            if (baseDim == 0) {
                                                x1 = low[0] - halfWidth
                                                y1 = low[1]
                                                x2 = low[0] + halfWidth
                                                y2 = low[1]
                                            } else {
                                                x1 = low[0]
                                                y1 = low[1] - halfWidth
                                                x2 = low[0]
                                                y2 = low[1] + halfWidth
                                            }
                                        }
                                }
                                groupElement(*elements.toTypedArray())
                            }
                        },
                    )
            }
        },
        custom.chart("custom-profile", "性能分析图", "Profile") {
            // Ten intervals per category, walking a timeline; the durations are fixed, not random.
            val random = sequence(20260613L)
            val categories = listOf("categoryA", "categoryB", "categoryC")
            val types =
                listOf(
                    "JS Heap" to "#7b9ce1",
                    "Documents" to "#bd6d6c",
                    "Nodes" to "#75d874",
                    "Listeners" to "#e0bc78",
                    "GPU Memory" to "#dc77dc",
                    "GPU" to "#72b362",
                )
            val startTime = 1_600_000_000_000.0
            val items = mutableListOf<OptionDataItem?>()
            categories.forEachIndexed { categoryIndex, _ ->
                var baseTime = startTime
                repeat(10) {
                    val type = types[kotlin.math.round(random() * (types.size - 1)).toInt()]
                    val duration = kotlin.math.round(random() * 10000)
                    val end = baseTime + duration
                    items +=
                        styledItem(
                            row(
                                categoryIndex.toDouble(),
                                baseTime,
                                end,
                                duration,
                            ),
                            type.second,
                        )
                    baseTime = end + kotlin.math.round(random() * 2000)
                }
            }
            unsafeJso<EChartsOption> {
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            formatter =
                                TooltipFormatter(
                                    js(
                                        "(p) => p.marker + p.name + ': ' + p.value[3] + ' ms'",
                                    ) as TooltipFormatterCallback,
                                )
                        },
                    )
                title = one(unsafeJso<TitleOption> { text = "Profile"; left = StringOrDouble("center") })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            filterMode = FilterModes.weakFilter
                            showDataShadow = BooleanOrString(false)
                            top = StringOrDouble(400.0)
                            labelFormatter = LabelFormatter("")
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            filterMode = FilterModes.weakFilter
                        },
                    )
                grid = one(unsafeJso<GridOption> { height = StringOrDouble(300.0) })
                xAxis = one(unsafeJso<XAxisOption> { min = AxisMinMax(startTime); scale = true })
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            data = strings(*categories.toTypedArray())
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        customSeries {
                            itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
                            encode = xyEncode(OptionEncodeValue(arrayOf(dim(1), dim(2))), OptionEncodeValue(dim(0)))
                            typedData = items.toTypedArray()
                            renderItem = { params, api ->
                                val category = api.value(dim(0))
                                val start = api.coord(coord(api.value(dim(1)), category))
                                val end = api.coord(coord(api.value(dim(2)), category))
                                val barHeight = numberArray(api.size(extent(OptionDataValue(0.0), OptionDataValue(1.0))))[1] * 0.6
                                val shape: RectLike =
                                    unsafeJso {
                                        x = start[0]
                                        y = start[1] - barHeight / 2
                                        width = end[0] - start[0]
                                        height = barHeight
                                    }
                                // Clip the bar to the grid, as the original does with
                                // `echarts.graphic.clipRectByRect`.
                                val coordSys = params.coordSys
                                val rect =
                                    ECharts.graphic.clipRectByRect(
                                        shape,
                                        unsafeJso<RectLike> {
                                            x = 0.0
                                            y = 0.0
                                            width = coordSys.width ?: 0.0
                                            height = coordSys.height ?: 0.0
                                        },
                                    )
                                if (rect == null) {
                                    null
                                } else {
                                    unsafeJso<CustomPathOption> {
                                        type = "rect"
                                        this.shape =
                                            unsafeJso {
                                                x = rect.x
                                                y = rect.y
                                                width = rect.width
                                                height = rect.height
                                            }
                                        style = api.style()
                                    }
                                }
                            }
                        },
                    )
            }
        },
        custom.chart("custom-bar-trend", "柱状图趋势", "Custom Bar Trend") {
            val random = sequence(20260614L)
            val yearCount = 7
            val categoryCount = 30
            val categories = List(categoryCount) { "category$it" }
            val years = List(yearCount) { (2010 + it).toString() }
            val legendItems = listOf("trend") + years
            val seriesValues = List(yearCount) { mutableListOf<Double>() }
            val customRows =
                List(categoryCount) { index ->
                    val first = kotlin.math.round(random() * 1000 * 100) / 100
                    val values = mutableListOf(first)
                    seriesValues[0] += first
                    for (j in 1 until yearCount) {
                        val previous = seriesValues[j - 1][index]
                        val value = kotlin.math.round(kotlin.math.max(0.0, previous + (random() - 0.5) * 200) * 100) / 100
                        seriesValues[j] += value
                        values += value
                    }
                    mutableListOf<OptionDataValue?>(OptionDataValue(index.toDouble())).also { list ->
                        values.forEach { list += OptionDataValue(it) }
                    }
                }
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                this.legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames(*legendItems.toTypedArray())
                            top = StringOrDouble(20.0)
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.slider
                            start = 50.0
                            end = 70.0
                        },
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            start = 50.0
                            end = 70.0
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings(*categories.toTypedArray())
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        customSeries {
                            name = "trend"
                            itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 2.0 }
                            encode =
                                xyEncode(
                                    OptionEncodeValue(dim(0)),
                                    OptionEncodeValue(Array(yearCount) { dim(it + 1) }),
                                )
                            typedData = Array<OptionDataItem?>(customRows.size) { OptionDataItem(customRows[it].toTypedArray()) }
                            z = 100.0
                            renderItem = { params, api ->
                                val x = api.value(dim(0))
                                val indices = api.currentSeriesIndices()
                                val layout =
                                    api.barLayout(
                                        unsafeJso<BarGridLayoutOptionForCustomSeries> {
                                            barGap = StringOrDouble("30%")
                                            barCategoryGap = StringOrDouble("20%")
                                            count = indices.size.toDouble() - 1
                                        },
                                    )
                                val points = mutableListOf<Array<Double>>()
                                indices.forEachIndexed { index, seriesIndex ->
                                    if (seriesIndex != params.seriesIndex) {
                                        val point = api.coord(coord(x, api.value(DimensionLoose(seriesIndex))))
                                        point[0] += layout?.get(index - 1)?.offsetCenter ?: 0.0
                                        point[1] -= 20
                                        points += point
                                    }
                                }
                                unsafeJso<CustomPathOption> {
                                    type = "polyline"
                                    shape =
                                        unsafeJso {
                                            this.points = points.toTypedArray()
                                        }
                                    style = api.style(unsafeJso<ZRStyleProps> { stroke = ZRColor(textOf(api.visual(VisualProps.color))); fill = ZRColor("none") })
                                }
                            }
                        },
                        *seriesValues
                            .mapIndexed { index, values ->
                                barSeries {
                                    animation = false
                                    name = years[index]
                                    itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.5 }
                                    typedData = numbers(*values.toDoubleArray())
                                } as SeriesOption
                            }
                            .toTypedArray(),
                    )
            }
        },
    )
