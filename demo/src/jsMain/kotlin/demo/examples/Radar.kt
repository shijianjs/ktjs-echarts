package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.LegendTypes
import echarts.RadarShapes
import echarts.TooltipTriggers
import echarts.options.AreaStyleOption
import echarts.options.AxisLineOption
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.EmphasisOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatter
import echarts.options.LabelFormatterCallback
import echarts.options.LabelOption
import echarts.options.LegendOption
import echarts.options.LineStyleOption
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.RadarIndicator
import echarts.options.RadarOption
import echarts.options.RadarSeriesDataItem
import echarts.options.RadarSeriesDataItemOption
import echarts.options.SelectionModes
import echarts.options.SeriesOption
import echarts.options.SplitAreaOption
import echarts.options.SplitAreaStyleOption
import echarts.options.SplitLineOption
import echarts.options.SplitLineStyleOption
import echarts.options.StringOrDouble
import echarts.options.SymbolShape
import echarts.options.SymbolSize
import echarts.options.StringOrStringArray
import echarts.options.TextStyleOption
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.ZRColorOrArray
import echarts.options.dataItems
import echarts.options.one
import echarts.options.radarSeries
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import zrender.ZRLineTypes

/*
 * Radar examples ported from `public/examples/ts/radar*.ts` of apache/echarts-examples.
 */

private val radar = Category(Groups.RADAR, "examples/Radar.kt")

/** One axis of a radar coordinate system. */
private fun indicator(name: String, max: Double? = null): RadarIndicator =
    unsafeJso {
        this.name = name
        this.max = max
    }

/** An unnamed data row: one value per indicator. */
private fun rowItem(vararg values: Double): RadarSeriesDataItem =
    RadarSeriesDataItem(Array(values.size) { index -> OptionDataValue(values[index]) })

/** A named data item of a radar series. */
private fun namedRadar(
    name: String,
    values: Array<OptionDataValue?>,
    block: RadarSeriesDataItemOption.() -> Unit = {},
): RadarSeriesDataItem =
    RadarSeriesDataItem(
        unsafeJso<RadarSeriesDataItemOption> {
            this.name = OptionId(name)
            typedValue = values
            block()
        },
    )

/** The AQI tables of `radar-aqi`; each row is `[AQI, PM2.5, PM10, CO, NO2, SO2, day]`. */
private val beijingTable =
    listOf(
        doubleArrayOf(55.0, 9.0, 56.0, 0.46, 18.0, 6.0, 1.0),
        doubleArrayOf(25.0, 11.0, 21.0, 0.65, 34.0, 9.0, 2.0),
        doubleArrayOf(56.0, 7.0, 63.0, 0.3, 14.0, 5.0, 3.0),
        doubleArrayOf(33.0, 7.0, 29.0, 0.33, 16.0, 6.0, 4.0),
        doubleArrayOf(42.0, 24.0, 44.0, 0.76, 40.0, 16.0, 5.0),
        doubleArrayOf(82.0, 58.0, 90.0, 1.77, 68.0, 33.0, 6.0),
        doubleArrayOf(74.0, 49.0, 77.0, 1.46, 48.0, 27.0, 7.0),
        doubleArrayOf(78.0, 55.0, 80.0, 1.29, 59.0, 29.0, 8.0),
        doubleArrayOf(267.0, 216.0, 280.0, 4.8, 108.0, 64.0, 9.0),
        doubleArrayOf(185.0, 127.0, 216.0, 2.52, 61.0, 27.0, 10.0),
        doubleArrayOf(39.0, 19.0, 38.0, 0.57, 31.0, 15.0, 11.0),
        doubleArrayOf(41.0, 11.0, 40.0, 0.43, 21.0, 7.0, 12.0),
        doubleArrayOf(64.0, 38.0, 74.0, 1.04, 46.0, 22.0, 13.0),
        doubleArrayOf(108.0, 79.0, 120.0, 1.7, 75.0, 41.0, 14.0),
        doubleArrayOf(108.0, 63.0, 116.0, 1.48, 44.0, 26.0, 15.0),
        doubleArrayOf(33.0, 6.0, 29.0, 0.34, 13.0, 5.0, 16.0),
        doubleArrayOf(94.0, 66.0, 110.0, 1.54, 62.0, 31.0, 17.0),
        doubleArrayOf(186.0, 142.0, 192.0, 3.88, 93.0, 79.0, 18.0),
        doubleArrayOf(57.0, 31.0, 54.0, 0.96, 32.0, 14.0, 19.0),
        doubleArrayOf(22.0, 8.0, 17.0, 0.48, 23.0, 10.0, 20.0),
        doubleArrayOf(39.0, 15.0, 36.0, 0.61, 29.0, 13.0, 21.0),
        doubleArrayOf(94.0, 69.0, 114.0, 2.08, 73.0, 39.0, 22.0),
        doubleArrayOf(99.0, 73.0, 110.0, 2.43, 76.0, 48.0, 23.0),
        doubleArrayOf(31.0, 12.0, 30.0, 0.5, 32.0, 16.0, 24.0),
        doubleArrayOf(42.0, 27.0, 43.0, 1.0, 53.0, 22.0, 25.0),
        doubleArrayOf(154.0, 117.0, 157.0, 3.05, 92.0, 58.0, 26.0),
        doubleArrayOf(234.0, 185.0, 230.0, 4.09, 123.0, 69.0, 27.0),
        doubleArrayOf(160.0, 120.0, 186.0, 2.77, 91.0, 50.0, 28.0),
        doubleArrayOf(134.0, 96.0, 165.0, 2.76, 83.0, 41.0, 29.0),
        doubleArrayOf(52.0, 24.0, 60.0, 1.03, 50.0, 21.0, 30.0),
        doubleArrayOf(46.0, 5.0, 49.0, 0.28, 10.0, 6.0, 31.0),
    )

private val guangzhouTable =
    listOf(
        doubleArrayOf(26.0, 37.0, 27.0, 1.163, 27.0, 13.0, 1.0),
        doubleArrayOf(85.0, 62.0, 71.0, 1.195, 60.0, 8.0, 2.0),
        doubleArrayOf(78.0, 38.0, 74.0, 1.363, 37.0, 7.0, 3.0),
        doubleArrayOf(21.0, 21.0, 36.0, 0.634, 40.0, 9.0, 4.0),
        doubleArrayOf(41.0, 42.0, 46.0, 0.915, 81.0, 13.0, 5.0),
        doubleArrayOf(56.0, 52.0, 69.0, 1.067, 92.0, 16.0, 6.0),
        doubleArrayOf(64.0, 30.0, 28.0, 0.924, 51.0, 2.0, 7.0),
        doubleArrayOf(55.0, 48.0, 74.0, 1.236, 75.0, 26.0, 8.0),
        doubleArrayOf(76.0, 85.0, 113.0, 1.237, 114.0, 27.0, 9.0),
        doubleArrayOf(91.0, 81.0, 104.0, 1.041, 56.0, 40.0, 10.0),
        doubleArrayOf(84.0, 39.0, 60.0, 0.964, 25.0, 11.0, 11.0),
        doubleArrayOf(64.0, 51.0, 101.0, 0.862, 58.0, 23.0, 12.0),
        doubleArrayOf(70.0, 69.0, 120.0, 1.198, 65.0, 36.0, 13.0),
        doubleArrayOf(77.0, 105.0, 178.0, 2.549, 64.0, 16.0, 14.0),
        doubleArrayOf(109.0, 68.0, 87.0, 0.996, 74.0, 29.0, 15.0),
        doubleArrayOf(73.0, 68.0, 97.0, 0.905, 51.0, 34.0, 16.0),
        doubleArrayOf(54.0, 27.0, 47.0, 0.592, 53.0, 12.0, 17.0),
        doubleArrayOf(51.0, 61.0, 97.0, 0.811, 65.0, 19.0, 18.0),
        doubleArrayOf(91.0, 71.0, 121.0, 1.374, 43.0, 18.0, 19.0),
        doubleArrayOf(73.0, 102.0, 182.0, 2.787, 44.0, 19.0, 20.0),
        doubleArrayOf(73.0, 50.0, 76.0, 0.717, 31.0, 20.0, 21.0),
        doubleArrayOf(84.0, 94.0, 140.0, 2.238, 68.0, 18.0, 22.0),
        doubleArrayOf(93.0, 77.0, 104.0, 1.165, 53.0, 7.0, 23.0),
        doubleArrayOf(99.0, 130.0, 227.0, 3.97, 55.0, 15.0, 24.0),
        doubleArrayOf(146.0, 84.0, 139.0, 1.094, 40.0, 17.0, 25.0),
        doubleArrayOf(113.0, 108.0, 137.0, 1.481, 48.0, 15.0, 26.0),
        doubleArrayOf(81.0, 48.0, 62.0, 1.619, 26.0, 3.0, 27.0),
        doubleArrayOf(56.0, 48.0, 68.0, 1.336, 37.0, 9.0, 28.0),
        doubleArrayOf(82.0, 92.0, 174.0, 3.29, 0.0, 13.0, 29.0),
        doubleArrayOf(106.0, 116.0, 188.0, 3.628, 101.0, 16.0, 30.0),
        doubleArrayOf(118.0, 50.0, 0.0, 1.383, 76.0, 11.0, 31.0),
    )

private val shanghaiTable =
    listOf(
        doubleArrayOf(91.0, 45.0, 125.0, 0.82, 34.0, 23.0, 1.0),
        doubleArrayOf(65.0, 27.0, 78.0, 0.86, 45.0, 29.0, 2.0),
        doubleArrayOf(83.0, 60.0, 84.0, 1.09, 73.0, 27.0, 3.0),
        doubleArrayOf(109.0, 81.0, 121.0, 1.28, 68.0, 51.0, 4.0),
        doubleArrayOf(106.0, 77.0, 114.0, 1.07, 55.0, 51.0, 5.0),
        doubleArrayOf(109.0, 81.0, 121.0, 1.28, 68.0, 51.0, 6.0),
        doubleArrayOf(106.0, 77.0, 114.0, 1.07, 55.0, 51.0, 7.0),
        doubleArrayOf(89.0, 65.0, 78.0, 0.86, 51.0, 26.0, 8.0),
        doubleArrayOf(53.0, 33.0, 47.0, 0.64, 50.0, 17.0, 9.0),
        doubleArrayOf(80.0, 55.0, 80.0, 1.01, 75.0, 24.0, 10.0),
        doubleArrayOf(117.0, 81.0, 124.0, 1.03, 45.0, 24.0, 11.0),
        doubleArrayOf(99.0, 71.0, 142.0, 1.1, 62.0, 42.0, 12.0),
        doubleArrayOf(95.0, 69.0, 130.0, 1.28, 74.0, 50.0, 13.0),
        doubleArrayOf(116.0, 87.0, 131.0, 1.47, 84.0, 40.0, 14.0),
        doubleArrayOf(108.0, 80.0, 121.0, 1.3, 85.0, 37.0, 15.0),
        doubleArrayOf(134.0, 83.0, 167.0, 1.16, 57.0, 43.0, 16.0),
        doubleArrayOf(79.0, 43.0, 107.0, 1.05, 59.0, 37.0, 17.0),
        doubleArrayOf(71.0, 46.0, 89.0, 0.86, 64.0, 25.0, 18.0),
        doubleArrayOf(97.0, 71.0, 113.0, 1.17, 88.0, 31.0, 19.0),
        doubleArrayOf(84.0, 57.0, 91.0, 0.85, 55.0, 31.0, 20.0),
        doubleArrayOf(87.0, 63.0, 101.0, 0.9, 56.0, 41.0, 21.0),
        doubleArrayOf(104.0, 77.0, 119.0, 1.09, 73.0, 48.0, 22.0),
        doubleArrayOf(87.0, 62.0, 100.0, 1.0, 72.0, 28.0, 23.0),
        doubleArrayOf(168.0, 128.0, 172.0, 1.49, 97.0, 56.0, 24.0),
        doubleArrayOf(65.0, 45.0, 51.0, 0.74, 39.0, 17.0, 25.0),
        doubleArrayOf(39.0, 24.0, 38.0, 0.61, 47.0, 17.0, 26.0),
        doubleArrayOf(39.0, 24.0, 39.0, 0.59, 50.0, 19.0, 27.0),
        doubleArrayOf(93.0, 68.0, 96.0, 1.05, 79.0, 29.0, 28.0),
        doubleArrayOf(188.0, 143.0, 197.0, 1.66, 99.0, 51.0, 29.0),
        doubleArrayOf(174.0, 131.0, 174.0, 1.55, 108.0, 50.0, 30.0),
        doubleArrayOf(187.0, 143.0, 201.0, 1.39, 89.0, 53.0, 31.0),
    )

private fun aqiRows(table: List<DoubleArray>): Array<RadarSeriesDataItem?> =
    dataItems(*Array(table.size) { index -> rowItem(*table[index]) })

/** `radar-custom` prints the raw value of the data item; the gauge-style callbacks upstream differ. */
private val rawValueFormatter = js("(params) => String(params.value)") as LabelFormatterCallback

val radarEntries: List<CatalogEntry> =
    listOf(
        radar.chart("radar", "基础雷达图", "Basic Radar Chart") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Basic Radar Chart" })
                legend = one(unsafeJso<LegendOption> { data = legendNames("Allocated Budget", "Actual Spending") })
                radar =
                    one(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Sales", 6500.0),
                                    indicator("Administration", 16000.0),
                                    indicator("Information Technology", 30000.0),
                                    indicator("Customer Support", 38000.0),
                                    indicator("Development", 52000.0),
                                    indicator("Marketing", 25000.0),
                                )
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        radarSeries {
                            name = "Budget vs spending"
                            typedData =
                                dataItems(
                                    radarItem("Allocated Budget", 4200.0, 3000.0, 20000.0, 35000.0, 50000.0, 18000.0),
                                    radarItem("Actual Spending", 5000.0, 14000.0, 28000.0, 26000.0, 42000.0, 21000.0),
                                )
                        },
                    )
            }
        },
        radar.chart("radar-aqi", "AQI - 雷达图", "AQI - Radar Chart") {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#161627")
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "AQI - Radar"
                            left = StringOrDouble("center")
                            textStyle = unsafeJso<TextStyleOption> { color = "#eee" }
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            bottom = StringOrDouble(5.0)
                            data = legendNames("Beijing", "Shanghai", "Guangzhou")
                            itemGap = 20.0
                            textStyle = unsafeJso<LabelOption> { color = "#fff"; fontSize = StringOrDouble(14.0) }
                            selectedMode = SelectionModes.single
                        },
                    )
                radar =
                    one(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("AQI", 300.0),
                                    indicator("PM2.5", 250.0),
                                    indicator("PM10", 300.0),
                                    indicator("CO", 5.0),
                                    indicator("NO2", 200.0),
                                    indicator("SO2", 100.0),
                                )
                            shape = RadarShapes.circle
                            splitNumber = 5.0
                            axisName = unsafeJso<LabelOption> { color = "rgb(238, 197, 102)" }
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    lineStyle =
                                        unsafeJso<SplitLineStyleOption> {
                                            typedColor =
                                                ZRColorOrArray(
                                                    arrayOf(
                                                        ZRColor("rgba(238, 197, 102, 1)"),
                                                        ZRColor("rgba(238, 197, 102, 0.8)"),
                                                        ZRColor("rgba(238, 197, 102, 0.6)"),
                                                        ZRColor("rgba(238, 197, 102, 0.4)"),
                                                        ZRColor("rgba(238, 197, 102, 0.2)"),
                                                        ZRColor("rgba(238, 197, 102, 0.1)"),
                                                    ),
                                                )
                                        }
                                }
                            splitArea = unsafeJso<SplitAreaOption> { show = false }
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(238, 197, 102, 0.5)") }
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        aqiSeries("Beijing", "#F9713C", 0.1, aqiRows(beijingTable)),
                        aqiSeries("Shanghai", "#B3E4A1", 0.05, aqiRows(shanghaiTable)),
                        aqiSeries("Guangzhou", "rgb(238, 197, 102)", 0.05, aqiRows(guangzhouTable)),
                    )
            }
        },
        radar.chart("radar-custom", "自定义雷达图", "Customized Radar Chart") {
            unsafeJso<EChartsOption> {
                color = arrayOf(ZRColor("#67F9D8"), ZRColor("#FFE434"), ZRColor("#56A3F1"), ZRColor("#FF917C"))
                title = one(unsafeJso<TitleOption> { text = "Customized Radar Chart" })
                legend = one(unsafeJso<LegendOption> {})
                radar =
                    arrayOf(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Indicator1"),
                                    indicator("Indicator2"),
                                    indicator("Indicator3"),
                                    indicator("Indicator4"),
                                    indicator("Indicator5"),
                                )
                            center = arrayOf(StringOrDouble("25%"), StringOrDouble("50%"))
                            radius = StringOrDouble(120.0)
                            startAngle = 90.0
                            splitNumber = 4.0
                            shape = RadarShapes.circle
                            axisName =
                                unsafeJso<LabelOption> {
                                    formatter = LabelFormatter("【{value}】")
                                    color = "#428BD4"
                                }
                            splitArea =
                                unsafeJso<SplitAreaOption> {
                                    areaStyle =
                                        unsafeJso<SplitAreaStyleOption> {
                                            typedColor =
                                                arrayOf(
                                                    ZRColor("#77EADF"),
                                                    ZRColor("#26C3BE"),
                                                    ZRColor("#64AFE9"),
                                                    ZRColor("#428BD4"),
                                                )
                                            shadowColor = ZRColor("rgba(0, 0, 0, 0.2)")
                                            shadowBlur = 10.0
                                        }
                                }
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(211, 253, 250, 0.8)") }
                                }
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    lineStyle = unsafeJso<SplitLineStyleOption> { color = ZRColor("rgba(211, 253, 250, 0.8)") }
                                }
                        },
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Indicator1", 150.0),
                                    indicator("Indicator2", 150.0),
                                    indicator("Indicator3", 150.0),
                                    indicator("Indicator4", 120.0),
                                    indicator("Indicator5", 108.0),
                                    indicator("Indicator6", 72.0),
                                )
                            center = arrayOf(StringOrDouble("75%"), StringOrDouble("50%"))
                            radius = StringOrDouble(120.0)
                            axisName =
                                unsafeJso<LabelOption> {
                                    color = "#fff"
                                    backgroundColor = ZRColor("#666")
                                    borderRadius = DoubleOrDoubleArray(3.0)
                                    padding = DoubleOrDoubleArray(arrayOf(3.0, 5.0))
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        radarSeries {
                            emphasis = unsafeJso<EmphasisOption> { lineStyle = unsafeJso<LineStyleOption> { width = 4.0 } }
                            typedData =
                                dataItems(
                                    namedRadar("Data A", row(100.0, 8.0, 0.4, -80.0, 2000.0)),
                                    namedRadar("Data B", row(60.0, 5.0, 0.3, -100.0, 1500.0)) {
                                        areaStyle = unsafeJso<AreaStyleOption> { color = ZRColor("rgba(255, 228, 52, 0.6)") }
                                    },
                                )
                        },
                        radarSeries {
                            radarIndex = 1.0
                            typedData =
                                dataItems(
                                    namedRadar("Data C", row(120.0, 118.0, 130.0, 100.0, 99.0, 70.0)) {
                                        symbol = SymbolShape("rect")
                                        symbolSize = SymbolSize(12.0)
                                        lineStyle = unsafeJso<LineStyleOption> { type = ZRLineTypes.dashed }
                                        label =
                                            unsafeJso<LabelOption> {
                                                show = true
                                                formatter = LabelFormatter(rawValueFormatter)
                                            }
                                    },
                                    namedRadar("Data D", row(100.0, 93.0, 50.0, 90.0, 70.0, 60.0)) {
                                        areaStyle =
                                            unsafeJso<AreaStyleOption> {
                                                color =
                                                    zrender.radialGradient(
                                                        0.1,
                                                        0.6,
                                                        1.0,
                                                        arrayOf(
                                                            zrender.gradientColorStop(0.0, "rgba(255, 145, 124, 0.1)"),
                                                            zrender.gradientColorStop(1.0, "rgba(255, 145, 124, 0.9)"),
                                                        ),
                                                    )
                                            }
                                    },
                                )
                        },
                    )
            }
        },
        radar.chart("radar2", "浏览器占比变化", "Proportion of Browsers") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Proportion of Browsers"
                            subtext = "Fake Data"
                            top = StringOrDouble(10.0)
                            left = StringOrDouble(10.0)
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.item })
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            type = LegendTypes.scroll
                            bottom = StringOrDouble(10.0)
                            data = legendNames(*Array(28) { index -> "${index + 2001}" })
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            top = StringOrDouble("middle")
                            right = StringOrDouble(10.0)
                            inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(arrayOf("red", "yellow")) }
                            calculable = true
                        },
                    )
                radar =
                    one(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("IE8-", 400.0),
                                    indicator("IE9+", 400.0),
                                    indicator("Safari", 400.0),
                                    indicator("Firefox", 400.0),
                                    indicator("Chrome", 400.0),
                                )
                        },
                    )
                series =
                    Array<SeriesOption>(28) { index ->
                        val year = index + 1
                        radarSeries {
                            symbol = SymbolShape("none")
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0 }
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    areaStyle = unsafeJso<AreaStyleOption> { color = ZRColor("rgba(0,250,0,0.3)") }
                                }
                            typedData =
                                dataItems(
                                    radarItem(
                                        "${year + 2000}",
                                        (40 - year) * 10.0,
                                        (38 - year) * 4.0 + 60.0,
                                        year * 5.0 + 10.0,
                                        year * 9.0,
                                        year * year / 2.0,
                                    ),
                                )
                        }
                    }
            }
        },
        radar.chart("radar-multiple", "多雷达图", "Multiple Radar") {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Multiple Radar" })
                tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            left = StringOrDouble("center")
                            data =
                                legendNames(
                                    "A Software",
                                    "A Phone",
                                    "Another Phone",
                                    "Precipitation",
                                    "Evaporation",
                                )
                        },
                    )
                radar =
                    arrayOf(
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Brand", 100.0),
                                    indicator("Content", 100.0),
                                    indicator("Usability", 100.0),
                                    indicator("Function", 100.0),
                                )
                            center = arrayOf(StringOrDouble("25%"), StringOrDouble("40%"))
                            radius = StringOrDouble(80.0)
                        },
                        unsafeJso<RadarOption> {
                            indicator =
                                arrayOf(
                                    indicator("Look", 100.0),
                                    indicator("Photo", 100.0),
                                    indicator("System", 100.0),
                                    indicator("Performance", 100.0),
                                    indicator("Screen", 100.0),
                                )
                            radius = StringOrDouble(80.0)
                            center = arrayOf(StringOrDouble("50%"), StringOrDouble("60%"))
                        },
                        unsafeJso<RadarOption> {
                            indicator = Array(12) { index -> indicator("${index + 1}月", 100.0) }
                            center = arrayOf(StringOrDouble("75%"), StringOrDouble("40%"))
                            radius = StringOrDouble(80.0)
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        radarSeries {
                            tooltip = unsafeJso<TooltipOption> { trigger = TooltipTriggers.item }
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData = dataItems(radarItem("A Software", 60.0, 73.0, 85.0, 40.0))
                        },
                        radarSeries {
                            radarIndex = 1.0
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData =
                                dataItems(
                                    radarItem("A Phone", 85.0, 90.0, 90.0, 95.0, 95.0),
                                    radarItem("Another Phone", 95.0, 80.0, 95.0, 90.0, 93.0),
                                )
                        },
                        radarSeries {
                            radarIndex = 2.0
                            areaStyle = unsafeJso<AreaStyleOption> {}
                            typedData =
                                dataItems(
                                    radarItem(
                                        "Precipitation",
                                        2.6,
                                        5.9,
                                        9.0,
                                        26.4,
                                        28.7,
                                        70.7,
                                        75.6,
                                        82.2,
                                        48.7,
                                        18.8,
                                        6.0,
                                        2.3,
                                    ),
                                    radarItem("Evaporation", 2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 35.6, 62.2, 32.6, 20.0, 6.4, 3.3),
                                )
                        },
                    )
            }
        },
    )

/** One of the three city series of `radar-aqi`. */
private fun aqiSeries(
    name: String,
    color: String,
    areaOpacity: Double,
    data: Array<RadarSeriesDataItem?>,
): SeriesOption =
    radarSeries {
        this.name = name
        symbol = SymbolShape("none")
        lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
        itemStyle = unsafeJso<ItemStyleOption> { this.color = ZRColor(color) }
        areaStyle = unsafeJso<AreaStyleOption> { opacity = areaOpacity }
        typedData = data
    }
