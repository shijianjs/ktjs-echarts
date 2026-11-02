package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import echarts.DataZoomTypes
import echarts.options.CustomBaseElementOption
import echarts.options.DataZoomOption
import echarts.options.DimensionLoose
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.ItemStyleOption
import echarts.options.LegendOption
import echarts.options.OptionDataItem
import echarts.options.OptionEncodeValue
import echarts.options.ScatterDataItem
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.customSeries
import echarts.options.one
import echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.row

private val customErrorScatterCategory = Category(Groups.CUSTOM, "examples/custom/CustomErrorScatter.kt")

val customErrorScatterEntry: CatalogEntry =
    customErrorScatterCategory.chart("custom-error-scatter", "散点图误差范围", "Error Scatter on Cartesian") {
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
        }
