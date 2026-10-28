package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor

/*
 * The AQI page of the parallel coordinate family, ported from
 * `public/examples/ts/parallel-aqi.ts`.
 *
 * The upstream example reads its three tables from the example server (`$.get` on
 * `data/asset/data/aqi-beijing.json` and friends); the same numbers are inlined here as Kotlin
 * arrays, one per city, with the grade column as text. Everything else — the dark background, the
 * visual map over PM2.5, the eight axes and the shared axis defaults — is the upstream option.
 */

private val parallelAqi = Category(Groups.PARALLEL, "examples/ParallelAqi.kt")

/** The first day reported by every table, the `date` dimension; the axis is inverted. */
private const val AQI_LAST_DAY = 31.0

/** Grades of the `等级` axis, in the order of the upstream category axis. */
private val AQI_GRADES = arrayOf("优", "良", "轻度污染", "中度污染", "重度污染", "严重污染")

/** Beijing, `[date, AQI, PM2.5, PM10, CO, NO2, SO2]` plus the grade. */
private val AQI_BEIJING =
    arrayOf(
        doubleArrayOf(1.0, 55.0, 9.0, 56.0, 0.46, 18.0, 6.0),
        doubleArrayOf(2.0, 25.0, 11.0, 21.0, 0.65, 34.0, 9.0),
        doubleArrayOf(3.0, 56.0, 7.0, 63.0, 0.3, 14.0, 5.0),
        doubleArrayOf(4.0, 33.0, 7.0, 29.0, 0.33, 16.0, 6.0),
        doubleArrayOf(5.0, 42.0, 24.0, 44.0, 0.76, 40.0, 16.0),
        doubleArrayOf(6.0, 82.0, 58.0, 90.0, 1.77, 68.0, 33.0),
        doubleArrayOf(7.0, 74.0, 49.0, 77.0, 1.46, 48.0, 27.0),
        doubleArrayOf(8.0, 78.0, 55.0, 80.0, 1.29, 59.0, 29.0),
        doubleArrayOf(9.0, 267.0, 216.0, 280.0, 4.8, 108.0, 64.0),
        doubleArrayOf(10.0, 185.0, 127.0, 216.0, 2.52, 61.0, 27.0),
        doubleArrayOf(11.0, 39.0, 19.0, 38.0, 0.57, 31.0, 15.0),
        doubleArrayOf(12.0, 41.0, 11.0, 40.0, 0.43, 21.0, 7.0),
        doubleArrayOf(13.0, 64.0, 38.0, 74.0, 1.04, 46.0, 22.0),
        doubleArrayOf(14.0, 108.0, 79.0, 120.0, 1.7, 75.0, 41.0),
        doubleArrayOf(15.0, 108.0, 63.0, 116.0, 1.48, 44.0, 26.0),
        doubleArrayOf(16.0, 33.0, 6.0, 29.0, 0.34, 13.0, 5.0),
        doubleArrayOf(17.0, 94.0, 66.0, 110.0, 1.54, 62.0, 31.0),
        doubleArrayOf(18.0, 186.0, 142.0, 192.0, 3.88, 93.0, 79.0),
        doubleArrayOf(19.0, 57.0, 31.0, 54.0, 0.96, 32.0, 14.0),
        doubleArrayOf(20.0, 22.0, 8.0, 17.0, 0.48, 23.0, 10.0),
        doubleArrayOf(21.0, 39.0, 15.0, 36.0, 0.61, 29.0, 13.0),
        doubleArrayOf(22.0, 94.0, 69.0, 114.0, 2.08, 73.0, 39.0),
        doubleArrayOf(23.0, 99.0, 73.0, 110.0, 2.43, 76.0, 48.0),
        doubleArrayOf(24.0, 31.0, 12.0, 30.0, 0.5, 32.0, 16.0),
        doubleArrayOf(25.0, 42.0, 27.0, 43.0, 1.0, 53.0, 22.0),
        doubleArrayOf(26.0, 154.0, 117.0, 157.0, 3.05, 92.0, 58.0),
        doubleArrayOf(27.0, 234.0, 185.0, 230.0, 4.09, 123.0, 69.0),
        doubleArrayOf(28.0, 160.0, 120.0, 186.0, 2.77, 91.0, 50.0),
        doubleArrayOf(29.0, 134.0, 96.0, 165.0, 2.76, 83.0, 41.0),
        doubleArrayOf(30.0, 52.0, 24.0, 60.0, 1.03, 50.0, 21.0),
        doubleArrayOf(31.0, 46.0, 5.0, 49.0, 0.28, 10.0, 6.0),
    )

/** Beijing's grades, one per row of [AQI_BEIJING]. */
private val AQI_BEIJING_GRADES = arrayOf("良", "优", "良", "优", "优", "良", "良", "良", "重度污染", "中度污染", "优", "优", "良", "轻度污染", "轻度污染", "优", "良", "中度污染", "良", "优", "优", "良", "良", "优", "优", "中度污染", "重度污染", "中度污染", "轻度污染", "良", "优")

/** Guangzhou, same columns as [AQI_BEIJING]. */
private val AQI_GUANGZHOU =
    arrayOf(
        doubleArrayOf(1.0, 26.0, 37.0, 27.0, 1.163, 27.0, 13.0),
        doubleArrayOf(2.0, 85.0, 62.0, 71.0, 1.195, 60.0, 8.0),
        doubleArrayOf(3.0, 78.0, 38.0, 74.0, 1.363, 37.0, 7.0),
        doubleArrayOf(4.0, 21.0, 21.0, 36.0, 0.634, 40.0, 9.0),
        doubleArrayOf(5.0, 41.0, 42.0, 46.0, 0.915, 81.0, 13.0),
        doubleArrayOf(6.0, 56.0, 52.0, 69.0, 1.067, 92.0, 16.0),
        doubleArrayOf(7.0, 64.0, 30.0, 28.0, 0.924, 51.0, 2.0),
        doubleArrayOf(8.0, 55.0, 48.0, 74.0, 1.236, 75.0, 26.0),
        doubleArrayOf(9.0, 76.0, 85.0, 113.0, 1.237, 114.0, 27.0),
        doubleArrayOf(10.0, 91.0, 81.0, 104.0, 1.041, 56.0, 40.0),
        doubleArrayOf(11.0, 84.0, 39.0, 60.0, 0.964, 25.0, 11.0),
        doubleArrayOf(12.0, 64.0, 51.0, 101.0, 0.862, 58.0, 23.0),
        doubleArrayOf(13.0, 70.0, 69.0, 120.0, 1.198, 65.0, 36.0),
        doubleArrayOf(14.0, 77.0, 105.0, 178.0, 2.549, 64.0, 16.0),
        doubleArrayOf(15.0, 109.0, 68.0, 87.0, 0.996, 74.0, 29.0),
        doubleArrayOf(16.0, 73.0, 68.0, 97.0, 0.905, 51.0, 34.0),
        doubleArrayOf(17.0, 54.0, 27.0, 47.0, 0.592, 53.0, 12.0),
        doubleArrayOf(18.0, 51.0, 61.0, 97.0, 0.811, 65.0, 19.0),
        doubleArrayOf(19.0, 91.0, 71.0, 121.0, 1.374, 43.0, 18.0),
        doubleArrayOf(20.0, 73.0, 102.0, 182.0, 2.787, 44.0, 19.0),
        doubleArrayOf(21.0, 73.0, 50.0, 76.0, 0.717, 31.0, 20.0),
        doubleArrayOf(22.0, 84.0, 94.0, 140.0, 2.238, 68.0, 18.0),
        doubleArrayOf(23.0, 93.0, 77.0, 104.0, 1.165, 53.0, 7.0),
        doubleArrayOf(24.0, 99.0, 130.0, 227.0, 3.97, 55.0, 15.0),
        doubleArrayOf(25.0, 146.0, 84.0, 139.0, 1.094, 40.0, 17.0),
        doubleArrayOf(26.0, 113.0, 108.0, 137.0, 1.481, 48.0, 15.0),
        doubleArrayOf(27.0, 81.0, 48.0, 62.0, 1.619, 26.0, 3.0),
        doubleArrayOf(28.0, 56.0, 48.0, 68.0, 1.336, 37.0, 9.0),
        doubleArrayOf(29.0, 82.0, 92.0, 174.0, 3.29, 0.0, 13.0),
        doubleArrayOf(30.0, 106.0, 116.0, 188.0, 3.628, 101.0, 16.0),
        doubleArrayOf(31.0, 118.0, 50.0, 0.0, 1.383, 76.0, 11.0),
    )

/** Guangzhou's grades, one per row of [AQI_GUANGZHOU]. */
private val AQI_GUANGZHOU_GRADES = arrayOf("优", "良", "良", "优", "优", "良", "良", "良", "良", "良", "良", "良", "良", "良", "轻度污染", "良", "良", "良", "良", "良", "良", "良", "良", "良", "轻度污染", "轻度污染", "良", "良", "良", "轻度污染", "轻度污染")

/** Shanghai, same columns as [AQI_BEIJING]. */
private val AQI_SHANGHAI =
    arrayOf(
        doubleArrayOf(1.0, 91.0, 45.0, 125.0, 0.82, 34.0, 23.0),
        doubleArrayOf(2.0, 65.0, 27.0, 78.0, 0.86, 45.0, 29.0),
        doubleArrayOf(3.0, 83.0, 60.0, 84.0, 1.09, 73.0, 27.0),
        doubleArrayOf(4.0, 109.0, 81.0, 121.0, 1.28, 68.0, 51.0),
        doubleArrayOf(5.0, 106.0, 77.0, 114.0, 1.07, 55.0, 51.0),
        doubleArrayOf(6.0, 109.0, 81.0, 121.0, 1.28, 68.0, 51.0),
        doubleArrayOf(7.0, 106.0, 77.0, 114.0, 1.07, 55.0, 51.0),
        doubleArrayOf(8.0, 89.0, 65.0, 78.0, 0.86, 51.0, 26.0),
        doubleArrayOf(9.0, 53.0, 33.0, 47.0, 0.64, 50.0, 17.0),
        doubleArrayOf(10.0, 80.0, 55.0, 80.0, 1.01, 75.0, 24.0),
        doubleArrayOf(11.0, 117.0, 81.0, 124.0, 1.03, 45.0, 24.0),
        doubleArrayOf(12.0, 99.0, 71.0, 142.0, 1.1, 62.0, 42.0),
        doubleArrayOf(13.0, 95.0, 69.0, 130.0, 1.28, 74.0, 50.0),
        doubleArrayOf(14.0, 116.0, 87.0, 131.0, 1.47, 84.0, 40.0),
        doubleArrayOf(15.0, 108.0, 80.0, 121.0, 1.3, 85.0, 37.0),
        doubleArrayOf(16.0, 134.0, 83.0, 167.0, 1.16, 57.0, 43.0),
        doubleArrayOf(17.0, 79.0, 43.0, 107.0, 1.05, 59.0, 37.0),
        doubleArrayOf(18.0, 71.0, 46.0, 89.0, 0.86, 64.0, 25.0),
        doubleArrayOf(19.0, 97.0, 71.0, 113.0, 1.17, 88.0, 31.0),
        doubleArrayOf(20.0, 84.0, 57.0, 91.0, 0.85, 55.0, 31.0),
        doubleArrayOf(21.0, 87.0, 63.0, 101.0, 0.9, 56.0, 41.0),
        doubleArrayOf(22.0, 104.0, 77.0, 119.0, 1.09, 73.0, 48.0),
        doubleArrayOf(23.0, 87.0, 62.0, 100.0, 1.0, 72.0, 28.0),
        doubleArrayOf(24.0, 168.0, 128.0, 172.0, 1.49, 97.0, 56.0),
        doubleArrayOf(25.0, 65.0, 45.0, 51.0, 0.74, 39.0, 17.0),
        doubleArrayOf(26.0, 39.0, 24.0, 38.0, 0.61, 47.0, 17.0),
        doubleArrayOf(27.0, 39.0, 24.0, 39.0, 0.59, 50.0, 19.0),
        doubleArrayOf(28.0, 93.0, 68.0, 96.0, 1.05, 79.0, 29.0),
        doubleArrayOf(29.0, 188.0, 143.0, 197.0, 1.66, 99.0, 51.0),
        doubleArrayOf(30.0, 174.0, 131.0, 174.0, 1.55, 108.0, 50.0),
        doubleArrayOf(31.0, 187.0, 143.0, 201.0, 1.39, 89.0, 53.0),
    )

/** Shanghai's grades, one per row of [AQI_SHANGHAI]. */
private val AQI_SHANGHAI_GRADES = arrayOf("良", "良", "良", "轻度污染", "轻度污染", "轻度污染", "轻度污染", "良", "良", "良", "轻度污染", "良", "良", "轻度污染", "轻度污染", "轻度污染", "良", "良", "良", "良", "良", "轻度污染", "良", "中度污染", "良", "优", "优", "良", "中度污染", "中度污染", "中度污染")

/**
 * The rows of [table] with [grades] appended as the eighth dimension.
 *
 * Upstream writes the grade straight into the array, which makes the tuple a mixed
 * `[number, ..., string]`; the wrapper types a parallel item as a tuple of values, so the text is
 * wrapped in [OptionDataValue] like every other member.
 */
private fun aqiItems(
    table: Array<DoubleArray>,
    grades: Array<String>,
): Array<ParallelSeriesDataItem?> =
    dataItems(
        *Array(table.size) { index ->
            val cells = table[index]
            ParallelSeriesDataItem(
                row(
                    cells[0],
                    cells[1],
                    cells[2],
                    cells[3],
                    cells[4],
                    cells[5],
                    cells[6],
                    grades[index],
                ),
            )
        },
    )

/** One axis of the AQI page: its dimension, its name and the keywords the original sets. */
private fun aqiAxis(dim: Int, name: String, block: ParallelAxisOption.() -> Unit = {}): ParallelAxisOption =
    unsafeJso<ParallelAxisOption> {
        this.dim = IndexOrIndexArray(dim.toDouble())
        this.name = name
        block()
    }

/** The shared axis defaults of the page, applied to all eight axes. */
private fun aqiAxisDefaults(): ParallelAxisOption =
    unsafeJso<ParallelAxisOption> {
        type = AxisTypes.value
        name = "AQI指数"
        nameLocation = AxisNameLocations.end
        nameGap = 20.0
        nameTextStyle =
            unsafeJso<LabelOption> {
                color = "#fff"
                fontSize = StringOrDouble(12.0)
            }
        axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#aaa") } }
        axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#777") } }
        splitLine = unsafeJso<SplitLineOption> { show = false }
        axisLabel = unsafeJso<AxisLabelOption> { color = "#fff" }
    }

val parallelAqiEntries: List<CatalogEntry> =
    listOf(
        parallelAqi.chart(
            "parallel-aqi",
            "AQI 分布（平行坐标）",
            "Parallel Aqi",
            "北京、上海、广州三地的 31 天空气质量，八个维度里第七维是分级的「等级」类目轴；" +
                "visualMap 按 PM2.5 给每条折线上色。",
            "Thirty-one days of air quality in Beijing, Shanghai and Guangzhou; the eighth dimension " +
                "is a category axis of grades and a visual map colours every polyline by PM2.5.",
        ) {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#333")
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            bottom = StringOrDouble(30.0)
                            data = legendNames("Beijing", "Shanghai", "Guangzhou")
                            itemGap = 20.0
                            textStyle =
                                unsafeJso<LabelOption> {
                                    color = "#fff"
                                    fontSize = StringOrDouble(14.0)
                                }
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            padding = DoubleOrDoubleArray(10.0)
                            backgroundColor = ZRColor("#222")
                            borderColor = ZRColor("#777")
                            borderWidth = 1.0
                        },
                    )
                parallelAxis =
                    arrayOf(
                        aqiAxis(0, "日期") {
                            inverse = true
                            max = AxisMinMax(AQI_LAST_DAY)
                            nameLocation = AxisNameLocations.start
                        },
                        aqiAxis(1, "AQI"),
                        aqiAxis(2, "PM2.5"),
                        aqiAxis(3, "PM10"),
                        aqiAxis(4, " CO"),
                        aqiAxis(5, "NO2"),
                        aqiAxis(6, "SO2"),
                        aqiAxis(7, "等级") {
                            type = AxisTypes.category
                            data = strings(*AQI_GRADES)
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = true
                            min = 0.0
                            max = 150.0
                            dimension = 2.0
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color =
                                        StringOrStringArray(
                                            arrayOf("#50a3ba", "#eac736", "#d94e5d"),
                                        )
                                }
                        },
                    )
                parallel =
                    one(
                        unsafeJso<ParallelOption> {
                            left = StringOrDouble("5%")
                            right = StringOrDouble("18%")
                            bottom = StringOrDouble(100.0)
                            parallelAxisDefault = aqiAxisDefaults()
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        parallelSeries {
                            name = "Beijing"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = aqiItems(AQI_BEIJING, AQI_BEIJING_GRADES)
                        },
                        parallelSeries {
                            name = "Shanghai"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = aqiItems(AQI_SHANGHAI, AQI_SHANGHAI_GRADES)
                        },
                        parallelSeries {
                            name = "Guangzhou"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = aqiItems(AQI_GUANGZHOU, AQI_GUANGZHOU_GRADES)
                        },
                    )
            }
        },
    )
