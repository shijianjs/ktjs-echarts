package demo.examples.radar

import echarts.options.AreaStyleOption
import echarts.options.ItemStyleOption
import echarts.options.LabelFormatterCallback
import echarts.options.LineStyleOption
import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.RadarIndicator
import echarts.options.RadarSeriesDataItem
import echarts.options.RadarSeriesDataItemOption
import echarts.options.SeriesOption
import echarts.options.SymbolShape
import echarts.options.dataItems
import echarts.options.one
import echarts.options.radarSeries
import js.objects.unsafeJso
import kotlin.js.js
import zrender.ZRColor
import demo.examples.lines.coordinate
import demo.examples.shared.row

/** One axis of a radar coordinate system. */
internal fun indicator(name: String, max: Double? = null): RadarIndicator =
    unsafeJso {
        this.name = name
        this.max = max
    }

/** An unnamed data row: one value per indicator. */
internal fun rowItem(vararg values: Double): RadarSeriesDataItem =
    RadarSeriesDataItem(Array(values.size) { index -> OptionDataValue(values[index]) })

/** A named data item of a radar series. */
internal fun namedRadar(
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
internal val beijingTable =
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

internal val guangzhouTable =
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

internal val shanghaiTable =
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

internal fun aqiRows(table: List<DoubleArray>): Array<RadarSeriesDataItem?> =
    dataItems(*Array(table.size) { index -> rowItem(*table[index]) })

/** `radar-custom` prints the raw value of the data item; the gauge-style callbacks upstream differ. */
internal val rawValueFormatter = js("(params) => String(params.value)") as LabelFormatterCallback

/** One of the three city series of `radar-aqi`. */
internal fun aqiSeries(
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
