package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.dataset.row
import demo.json.points

/** The 24 hours of the day, the category axis of both punch cards. */
internal val PUNCH_HOURS =
    arrayOf(
        "12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a",
        "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p",
    )

/** The seven weekdays, Saturday first, the other category axis of both punch cards. */
internal val PUNCH_DAYS =
    arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/** The 168 commit counts of the punch cards: `rows[day][hour]`. */
internal val PUNCH_VALUES: List<List<Double>> =
    listOf(
        listOf(
            5.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0,
            4.0, 1.0, 1.0, 3.0, 4.0, 6.0, 4.0, 4.0, 3.0, 3.0, 2.0, 5.0,
        ),
        listOf(
            7.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 5.0, 2.0,
            2.0, 6.0, 9.0, 11.0, 6.0, 7.0, 8.0, 12.0, 5.0, 5.0, 7.0, 2.0,
        ),
        listOf(
            1.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 3.0, 2.0,
            1.0, 9.0, 8.0, 10.0, 6.0, 5.0, 5.0, 5.0, 7.0, 4.0, 2.0, 4.0,
        ),
        listOf(
            7.0, 3.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 5.0, 4.0,
            7.0, 14.0, 13.0, 12.0, 9.0, 5.0, 5.0, 10.0, 6.0, 4.0, 4.0, 1.0,
        ),
        listOf(
            1.0, 3.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 2.0, 4.0, 4.0,
            2.0, 4.0, 4.0, 14.0, 12.0, 1.0, 8.0, 5.0, 3.0, 7.0, 3.0, 0.0,
        ),
        listOf(
            2.0, 1.0, 0.0, 3.0, 0.0, 0.0, 0.0, 0.0, 2.0, 0.0, 4.0, 1.0,
            5.0, 10.0, 5.0, 7.0, 11.0, 6.0, 0.0, 5.0, 3.0, 4.0, 2.0, 0.0,
        ),
        listOf(
            1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0,
            2.0, 1.0, 3.0, 4.0, 0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 2.0, 6.0,
        ),
    )

/** The 22 measurements of `scatter-simple`. */
internal val ANSCOMBE_POINTS: Array<DoubleArray> =
    arrayOf(
        doubleArrayOf(10.0, 8.04),
        doubleArrayOf(8.07, 6.95),
        doubleArrayOf(13.0, 7.58),
        doubleArrayOf(9.05, 8.81),
        doubleArrayOf(11.0, 8.33),
        doubleArrayOf(14.0, 7.66),
        doubleArrayOf(13.4, 6.81),
        doubleArrayOf(10.0, 6.33),
        doubleArrayOf(14.0, 8.96),
        doubleArrayOf(12.5, 6.82),
        doubleArrayOf(9.15, 7.2),
        doubleArrayOf(11.5, 7.2),
        doubleArrayOf(3.03, 4.23),
        doubleArrayOf(12.2, 7.83),
        doubleArrayOf(2.02, 4.47),
        doubleArrayOf(1.05, 3.33),
        doubleArrayOf(4.05, 4.96),
        doubleArrayOf(6.03, 7.24),
        doubleArrayOf(12.0, 6.26),
        doubleArrayOf(12.0, 8.84),
        doubleArrayOf(7.08, 5.82),
        doubleArrayOf(5.02, 5.68),
    )

/** The 1990 rows of `bubble-gradient`: `[income, life expectancy, population, year]`. */
internal val EXPECTANCY_1990: Array<DoubleArray> =
    arrayOf(
        doubleArrayOf(28604.0, 77.0, 17096869.0, 1990.0),
        doubleArrayOf(31163.0, 77.4, 27662440.0, 1990.0),
        doubleArrayOf(1516.0, 68.0, 1154605773.0, 1990.0),
        doubleArrayOf(13670.0, 74.7, 10582082.0, 1990.0),
        doubleArrayOf(28599.0, 75.0, 4986705.0, 1990.0),
        doubleArrayOf(29476.0, 77.1, 56943299.0, 1990.0),
        doubleArrayOf(31476.0, 75.4, 78958237.0, 1990.0),
        doubleArrayOf(28666.0, 78.1, 254830.0, 1990.0),
        doubleArrayOf(1777.0, 57.7, 870601776.0, 1990.0),
        doubleArrayOf(29550.0, 79.1, 122249285.0, 1990.0),
        doubleArrayOf(2076.0, 67.9, 20194354.0, 1990.0),
        doubleArrayOf(12087.0, 72.0, 42972254.0, 1990.0),
        doubleArrayOf(24021.0, 75.4, 3397534.0, 1990.0),
        doubleArrayOf(43296.0, 76.8, 4240375.0, 1990.0),
        doubleArrayOf(10088.0, 70.8, 38195258.0, 1990.0),
        doubleArrayOf(19349.0, 69.6, 147568552.0, 1990.0),
        doubleArrayOf(10670.0, 67.3, 53994605.0, 1990.0),
        doubleArrayOf(26424.0, 75.7, 57110117.0, 1990.0),
        doubleArrayOf(37062.0, 75.4, 252847810.0, 1990.0),
    )

/** The 2015 rows of `bubble-gradient`, same columns as [EXPECTANCY_1990]. */
internal val EXPECTANCY_2015: Array<DoubleArray> =
    arrayOf(
        doubleArrayOf(44056.0, 81.8, 23968973.0, 2015.0),
        doubleArrayOf(43294.0, 81.7, 35939927.0, 2015.0),
        doubleArrayOf(13334.0, 76.9, 1376048943.0, 2015.0),
        doubleArrayOf(21291.0, 78.5, 11389562.0, 2015.0),
        doubleArrayOf(38923.0, 80.8, 5503457.0, 2015.0),
        doubleArrayOf(37599.0, 81.9, 64395345.0, 2015.0),
        doubleArrayOf(44053.0, 81.1, 80688545.0, 2015.0),
        doubleArrayOf(42182.0, 82.8, 329425.0, 2015.0),
        doubleArrayOf(5903.0, 66.8, 1311050527.0, 2015.0),
        doubleArrayOf(36162.0, 83.5, 126573481.0, 2015.0),
        doubleArrayOf(1390.0, 71.4, 25155317.0, 2015.0),
        doubleArrayOf(34644.0, 80.7, 50293439.0, 2015.0),
        doubleArrayOf(34186.0, 80.6, 4528526.0, 2015.0),
        doubleArrayOf(64304.0, 81.6, 5210967.0, 2015.0),
        doubleArrayOf(24787.0, 77.3, 38611794.0, 2015.0),
        doubleArrayOf(23038.0, 73.13, 143456918.0, 2015.0),
        doubleArrayOf(19360.0, 76.5, 78665830.0, 2015.0),
        doubleArrayOf(38225.0, 81.4, 64715810.0, 2015.0),
        doubleArrayOf(53354.0, 79.1, 321773631.0, 2015.0),
    )

/** One row of the life-expectancy table of `scatter-label-align-top`. */
internal class CountryRow(
    val income: Double,
    val life: Double,
    val population: Double,
    val country: String,
    val year: Int,
)

/** The 19 countries of `scatter-label-align-top` in 1990. */
internal val COUNTRY_ROWS_1990: Array<CountryRow> =
    arrayOf(
        CountryRow(28604.0, 77.0, 17096869.0, "Australia", 1990),
        CountryRow(31163.0, 77.4, 27662440.0, "Canada", 1990),
        CountryRow(1516.0, 68.0, 1154605773.0, "China", 1990),
        CountryRow(13670.0, 74.7, 10582082.0, "Cuba", 1990),
        CountryRow(28599.0, 75.0, 4986705.0, "Finland", 1990),
        CountryRow(29476.0, 77.1, 56943299.0, "France", 1990),
        CountryRow(31476.0, 75.4, 78958237.0, "Germany", 1990),
        CountryRow(28666.0, 78.1, 254830.0, "Iceland", 1990),
        CountryRow(1777.0, 57.7, 870601776.0, "India", 1990),
        CountryRow(29550.0, 79.1, 122249285.0, "Japan", 1990),
        CountryRow(2076.0, 67.9, 20194354.0, "North Korea", 1990),
        CountryRow(12087.0, 72.0, 42972254.0, "South Korea", 1990),
        CountryRow(24021.0, 75.4, 3397534.0, "New Zealand", 1990),
        CountryRow(43296.0, 76.8, 4240375.0, "Norway", 1990),
        CountryRow(10088.0, 70.8, 38195258.0, "Poland", 1990),
        CountryRow(19349.0, 69.6, 147568552.0, "Russia", 1990),
        CountryRow(10670.0, 67.3, 53994605.0, "Turkey", 1990),
        CountryRow(26424.0, 75.7, 57110117.0, "United Kingdom", 1990),
        CountryRow(37062.0, 75.4, 252847810.0, "United States", 1990),
    )

/** One day of the AQI table: `[day, AQI, PM2.5, PM10, CO, NO2, SO2]` plus its grade. */
internal class AqiDay(val values: DoubleArray, val grade: String)

/** Builds one [AqiDay]. */
internal fun aqiDay(
    dayValue: Double,
    aqiValue: Double,
    pm25: Double,
    pm10: Double,
    co: Double,
    no2: Double,
    so2: Double,
    grade: String,
): AqiDay = AqiDay(doubleArrayOf(dayValue, aqiValue, pm25, pm10, co, no2, so2), grade)

/** Every other day of the Beijing table of `scatter-aqi-color`. */
internal val AQI_BEIJING: Array<AqiDay> =
    arrayOf(
        aqiDay(1.0, 55.0, 9.0, 56.0, 0.46, 18.0, 6.0, "良"),
        aqiDay(3.0, 56.0, 7.0, 63.0, 0.3, 14.0, 5.0, "良"),
        aqiDay(5.0, 42.0, 24.0, 44.0, 0.76, 40.0, 16.0, "优"),
        aqiDay(7.0, 74.0, 49.0, 77.0, 1.46, 48.0, 27.0, "良"),
        aqiDay(9.0, 267.0, 216.0, 280.0, 4.8, 108.0, 64.0, "重度污染"),
        aqiDay(11.0, 39.0, 19.0, 38.0, 0.57, 31.0, 15.0, "优"),
        aqiDay(13.0, 64.0, 38.0, 74.0, 1.04, 46.0, 22.0, "良"),
        aqiDay(15.0, 108.0, 63.0, 116.0, 1.48, 44.0, 26.0, "轻度污染"),
        aqiDay(17.0, 94.0, 66.0, 110.0, 1.54, 62.0, 31.0, "良"),
        aqiDay(19.0, 57.0, 31.0, 54.0, 0.96, 32.0, 14.0, "良"),
        aqiDay(21.0, 39.0, 15.0, 36.0, 0.61, 29.0, 13.0, "优"),
        aqiDay(23.0, 99.0, 73.0, 110.0, 2.43, 76.0, 48.0, "良"),
        aqiDay(25.0, 42.0, 27.0, 43.0, 1.0, 53.0, 22.0, "优"),
        aqiDay(27.0, 234.0, 185.0, 230.0, 4.09, 123.0, 69.0, "重度污染"),
        aqiDay(29.0, 134.0, 96.0, 165.0, 2.76, 83.0, 41.0, "轻度污染"),
        aqiDay(31.0, 46.0, 5.0, 49.0, 0.28, 10.0, 6.0, "优"),
    )

/** Every other day of the Shanghai table of `scatter-aqi-color`. */
internal val AQI_SHANGHAI: Array<AqiDay> =
    arrayOf(
        aqiDay(1.0, 91.0, 45.0, 125.0, 0.82, 34.0, 23.0, "良"),
        aqiDay(3.0, 83.0, 60.0, 84.0, 1.09, 73.0, 27.0, "良"),
        aqiDay(5.0, 106.0, 77.0, 114.0, 1.07, 55.0, 51.0, "轻度污染"),
        aqiDay(7.0, 106.0, 77.0, 114.0, 1.07, 55.0, 51.0, "轻度污染"),
        aqiDay(9.0, 53.0, 33.0, 47.0, 0.64, 50.0, 17.0, "良"),
        aqiDay(11.0, 117.0, 81.0, 124.0, 1.03, 45.0, 24.0, "轻度污染"),
        aqiDay(13.0, 95.0, 69.0, 130.0, 1.28, 74.0, 50.0, "良"),
        aqiDay(15.0, 108.0, 80.0, 121.0, 1.3, 85.0, 37.0, "轻度污染"),
        aqiDay(17.0, 79.0, 43.0, 107.0, 1.05, 59.0, 37.0, "良"),
        aqiDay(19.0, 97.0, 71.0, 113.0, 1.17, 88.0, 31.0, "良"),
        aqiDay(21.0, 87.0, 63.0, 101.0, 0.9, 56.0, 41.0, "良"),
        aqiDay(23.0, 87.0, 62.0, 100.0, 1.0, 72.0, 28.0, "良"),
        aqiDay(25.0, 65.0, 45.0, 51.0, 0.74, 39.0, 17.0, "良"),
        aqiDay(27.0, 39.0, 24.0, 39.0, 0.59, 50.0, 19.0, "优"),
        aqiDay(29.0, 188.0, 143.0, 197.0, 1.66, 99.0, 51.0, "中度污染"),
        aqiDay(31.0, 187.0, 143.0, 201.0, 1.39, 89.0, 53.0, "中度污染"),
    )

/** Every other day of the Guangzhou table of `scatter-aqi-color`. */
internal val AQI_GUANGZHOU: Array<AqiDay> =
    arrayOf(
        aqiDay(1.0, 26.0, 37.0, 27.0, 1.163, 27.0, 13.0, "优"),
        aqiDay(3.0, 78.0, 38.0, 74.0, 1.363, 37.0, 7.0, "良"),
        aqiDay(5.0, 41.0, 42.0, 46.0, 0.915, 81.0, 13.0, "优"),
        aqiDay(7.0, 64.0, 30.0, 28.0, 0.924, 51.0, 2.0, "良"),
        aqiDay(9.0, 76.0, 85.0, 113.0, 1.237, 114.0, 27.0, "良"),
        aqiDay(11.0, 84.0, 39.0, 60.0, 0.964, 25.0, 11.0, "良"),
        aqiDay(13.0, 70.0, 69.0, 120.0, 1.198, 65.0, 36.0, "良"),
        aqiDay(15.0, 109.0, 68.0, 87.0, 0.996, 74.0, 29.0, "轻度污染"),
        aqiDay(17.0, 54.0, 27.0, 47.0, 0.592, 53.0, 12.0, "良"),
        aqiDay(19.0, 91.0, 71.0, 121.0, 1.374, 43.0, 18.0, "良"),
        aqiDay(21.0, 73.0, 50.0, 76.0, 0.717, 31.0, 20.0, "良"),
        aqiDay(23.0, 93.0, 77.0, 104.0, 1.165, 53.0, 7.0, "良"),
        aqiDay(25.0, 146.0, 84.0, 139.0, 1.094, 40.0, 17.0, "轻度污染"),
        aqiDay(27.0, 81.0, 48.0, 62.0, 1.619, 26.0, 3.0, "良"),
        aqiDay(29.0, 82.0, 92.0, 174.0, 3.29, 0.0, 13.0, "良"),
        aqiDay(31.0, 118.0, 50.0, 0.0, 1.383, 76.0, 11.0, "轻度污染"),
    )

/** Thirty-one sampled rows of the female table of `scatter-weight`. */
internal val FEMALE_WEIGHT: Array<DoubleArray> =
    arrayOf(
        doubleArrayOf(161.2, 51.6),
        doubleArrayOf(160.2, 75.2),
        doubleArrayOf(172.9, 62.5),
        doubleArrayOf(175.0, 73.2),
        doubleArrayOf(176.5, 87.8),
        doubleArrayOf(160.0, 47.0),
        doubleArrayOf(160.0, 54.4),
        doubleArrayOf(161.3, 67.9),
        doubleArrayOf(160.0, 50.2),
        doubleArrayOf(167.6, 61.0),
        doubleArrayOf(180.3, 60.7),
        doubleArrayOf(160.0, 74.3),
        doubleArrayOf(174.0, 75.7),
        doubleArrayOf(152.0, 59.3),
        doubleArrayOf(170.0, 69.4),
        doubleArrayOf(159.8, 50.0),
        doubleArrayOf(159.4, 45.7),
        doubleArrayOf(179.8, 66.8),
        doubleArrayOf(151.1, 73.2),
        doubleArrayOf(178.0, 70.6),
        doubleArrayOf(172.7, 105.2),
        doubleArrayOf(160.0, 45.0),
        doubleArrayOf(172.7, 62.0),
        doubleArrayOf(160.0, 43.2),
        doubleArrayOf(167.1, 62.2),
        doubleArrayOf(159.2, 51.8),
        doubleArrayOf(162.6, 84.5),
        doubleArrayOf(175.3, 72.7),
        doubleArrayOf(165.1, 80.9),
        doubleArrayOf(165.1, 104.1),
        doubleArrayOf(162.6, 81.8),
    )

/** Thirty-one sampled rows of the male table of `scatter-weight`. */
internal val MALE_WEIGHT: Array<DoubleArray> =
    arrayOf(
        doubleArrayOf(174.0, 65.6),
        doubleArrayOf(175.3, 71.8),
        doubleArrayOf(193.5, 80.7),
        doubleArrayOf(187.2, 78.8),
        doubleArrayOf(184.5, 78.4),
        doubleArrayOf(192.0, 90.0),
        doubleArrayOf(192.7, 93.8),
        doubleArrayOf(176.0, 78.8),
        doubleArrayOf(178.0, 89.6),
        doubleArrayOf(164.5, 63.2),
        doubleArrayOf(189.2, 84.1),
        doubleArrayOf(170.0, 59.5),
        doubleArrayOf(186.7, 87.8),
        doubleArrayOf(177.2, 94.1),
        doubleArrayOf(164.1, 55.2),
        doubleArrayOf(174.0, 86.8),
        doubleArrayOf(192.0, 101.6),
        doubleArrayOf(181.1, 66.0),
        doubleArrayOf(197.1, 90.9),
        doubleArrayOf(180.1, 93.0),
        doubleArrayOf(184.4, 68.0),
        doubleArrayOf(192.0, 102.3),
        doubleArrayOf(167.4, 53.9),
        doubleArrayOf(177.8, 116.4),
        doubleArrayOf(190.5, 108.6),
        doubleArrayOf(172.7, 95.9),
        doubleArrayOf(188.0, 90.5),
        doubleArrayOf(170.8, 93.2),
        doubleArrayOf(186.7, 101.4),
        doubleArrayOf(177.8, 102.5),
        doubleArrayOf(190.5, 98.2),
    )

/** The four groups of `scatter-anscombe-quartet`. */
internal val ANSCOMBE_GROUPS: Array<Array<DoubleArray>> =
    arrayOf(
        arrayOf(
            doubleArrayOf(10.0, 8.04),
            doubleArrayOf(8.0, 6.95),
            doubleArrayOf(13.0, 7.58),
            doubleArrayOf(9.0, 8.81),
            doubleArrayOf(11.0, 8.33),
            doubleArrayOf(14.0, 9.96),
            doubleArrayOf(6.0, 7.24),
            doubleArrayOf(4.0, 4.26),
            doubleArrayOf(12.0, 10.84),
            doubleArrayOf(7.0, 4.82),
            doubleArrayOf(5.0, 5.68),
        ),
        arrayOf(
            doubleArrayOf(10.0, 9.14),
            doubleArrayOf(8.0, 8.14),
            doubleArrayOf(13.0, 8.74),
            doubleArrayOf(9.0, 8.77),
            doubleArrayOf(11.0, 9.26),
            doubleArrayOf(14.0, 8.1),
            doubleArrayOf(6.0, 6.13),
            doubleArrayOf(4.0, 3.1),
            doubleArrayOf(12.0, 9.13),
            doubleArrayOf(7.0, 7.26),
            doubleArrayOf(5.0, 4.74),
        ),
        arrayOf(
            doubleArrayOf(10.0, 7.46),
            doubleArrayOf(8.0, 6.77),
            doubleArrayOf(13.0, 12.74),
            doubleArrayOf(9.0, 7.11),
            doubleArrayOf(11.0, 7.81),
            doubleArrayOf(14.0, 8.84),
            doubleArrayOf(6.0, 6.08),
            doubleArrayOf(4.0, 5.39),
            doubleArrayOf(12.0, 8.15),
            doubleArrayOf(7.0, 6.42),
            doubleArrayOf(5.0, 5.73),
        ),
        arrayOf(
            doubleArrayOf(8.0, 6.58),
            doubleArrayOf(8.0, 5.76),
            doubleArrayOf(8.0, 7.71),
            doubleArrayOf(8.0, 8.84),
            doubleArrayOf(8.0, 8.47),
            doubleArrayOf(8.0, 7.04),
            doubleArrayOf(8.0, 5.25),
            doubleArrayOf(19.0, 12.5),
            doubleArrayOf(8.0, 5.56),
            doubleArrayOf(8.0, 7.91),
            doubleArrayOf(8.0, 6.89),
        ),
    )

/** The names of the four quartets. */
internal val ANSCOMBE_NAMES = arrayOf("I", "II", "III", "IV")

/** Six numeric dimensions of `scatter-matrix`; the seventh value is the city. */
internal const val MATRIX_DIMS = 6

/** Left margin of the matrix, in percent. */
internal const val MATRIX_BASE_LEFT = 5.0

/** Top margin of the matrix, in percent. */
internal const val MATRIX_BASE_TOP = 10.0

/** Gap between two cells of the matrix, in percent. */
internal const val MATRIX_CELL_GAP = 2.0

/** Distance between two cells of the matrix, in percent. */
internal val MATRIX_STEP = (100.0 - MATRIX_BASE_LEFT - MATRIX_CELL_GAP) / MATRIX_DIMS

/** Width of one matrix cell. */
internal val MATRIX_CELL_WIDTH = "${MATRIX_STEP - MATRIX_CELL_GAP}%"

/** Height of one matrix cell. */
internal val MATRIX_CELL_HEIGHT = "${(100.0 - MATRIX_BASE_TOP - MATRIX_CELL_GAP) / MATRIX_DIMS - MATRIX_CELL_GAP}%"

/** The six axis names of the matrix and of its parallel block. */
internal val MATRIX_AXES = arrayOf("AQI", "PM 2.5", "PM 10", "CO", "NO₂", "SO₂")

/** The grade axis of the parallel block. */
internal val MATRIX_GRADES = arrayOf("优", "良", "轻度", "中度", "重度", "严重")

/** One row of the matrix: its city, its six numbers and its grade. */
internal class MatrixRow(val city: String, val values: DoubleArray, val grade: String)

/** Nine sampled rows, three per city, of the AQI table behind `scatter-matrix`. */
internal val MATRIX_ROWS: Array<MatrixRow> =
    arrayOf(
        MatrixRow("北京", doubleArrayOf(267.0, 216.0, 280.0, 4.8, 108.0, 64.0), "重度"),
        MatrixRow("北京", doubleArrayOf(33.0, 6.0, 29.0, 0.34, 13.0, 5.0), "优"),
        MatrixRow("北京", doubleArrayOf(234.0, 185.0, 230.0, 4.09, 123.0, 69.0), "重度"),
        MatrixRow("广州", doubleArrayOf(76.0, 85.0, 113.0, 1.237, 114.0, 27.0), "良"),
        MatrixRow("广州", doubleArrayOf(73.0, 68.0, 97.0, 0.905, 51.0, 34.0), "良"),
        MatrixRow("广州", doubleArrayOf(81.0, 48.0, 62.0, 1.619, 26.0, 3.0), "良"),
        MatrixRow("上海", doubleArrayOf(53.0, 33.0, 47.0, 0.64, 50.0, 17.0), "良"),
        MatrixRow("上海", doubleArrayOf(134.0, 83.0, 167.0, 1.16, 57.0, 43.0), "轻度"),
        MatrixRow("上海", doubleArrayOf(39.0, 24.0, 39.0, 0.59, 50.0, 19.0), "优"),
    )

/** Points of `scatter-large`; the original plots two million. */
internal const val LARGE_SCATTER_POINTS = 60

/** `focus: 'self'` of the labelled bubbles. */
internal fun focusSelf(): EmphasisOption = EmphasisOption().apply { focus = "self" }

/** `focus: 'series'` of the weighted bubbles. */
internal fun focusSeries(): EmphasisOption = EmphasisOption().apply { focus = "series" }

/** A dashed split line, shared by the two axes of `bubble-gradient`. */
internal fun dashedSplitLine(): SplitLineOption =
    SplitLineOption().apply { lineStyle = LineStyleOption().apply { type = "dashed" } }

/** The `[x, y]` points of `scatter-simple`. */
internal fun anscombePoints(): List<DataValue> = ANSCOMBE_POINTS.map { point(it[0], it[1]) }

/** The four-value bubbles of `bubble-gradient`, sized by the population they carry. */
internal fun expectancyItems(table: Array<DoubleArray>): List<DataValue> =
    table.map { row ->
        dataItem {
            value = arrayValue(valueOf(row[0]), valueOf(row[1]), valueOf(row[2]), valueOf(row[3]))
            symbolSize = NumberOrStringOrNumbers.of(kotlin.math.sqrt(row[2]) / 500.0)
        }
    }

/** The labelled bubbles of `scatter-label-align-top`; the label reads the item name. */
internal fun countryItems(rows: Array<CountryRow>): List<DataValue> =
    rows.map { row ->
        dataItem {
            name = row.country
            value =
                arrayValue(
                    valueOf(row.income),
                    valueOf(row.life),
                    valueOf(row.population),
                    textValue(row.country),
                    valueOf(row.year),
                )
            symbolSize = NumberOrStringOrNumbers.of(kotlin.math.sqrt(row.population) / 500.0)
        }
    }

/** The eight-value rows of `scatter-aqi-color`. */
internal fun aqiItems(days: Array<AqiDay>): List<DataValue> =
    days.map { day ->
        val values = day.values
        arrayValue(
            valueOf(values[0]),
            valueOf(values[1]),
            valueOf(values[2]),
            valueOf(values[3]),
            valueOf(values[4]),
            valueOf(values[5]),
            valueOf(values[6]),
            textValue(day.grade),
        )
    }

/**
 * The `[day, hour, commits]` rows of the two punch cards, sized by the commit count.
 *
 * The polar page plots `[day, hour]` and the cartesian one `[hour, day]`, matching the upstream
 * column order of each page.
 */
internal fun punchCardItems(polar: Boolean): List<DataValue> =
    PUNCH_VALUES.flatMapIndexed { day, row ->
        row.mapIndexed { hour, commits ->
            val first = if (polar) day.toDouble() else hour.toDouble()
            val second = if (polar) hour.toDouble() else day.toDouble()
            dataItem {
                value = arrayValue(valueOf(first), valueOf(second), valueOf(commits))
                symbolSize = NumberOrStringOrNumbers.of(commits * 2.0)
            }
        }
    }

/** Fixed random source of `scatter-large`, standing in for its `Math.random()` calls. */
internal class ScatterRandom(seed: Int) {
    private var state = seed

    /** Next value in `0..1`. */
    fun next(): Double {
        state = state * 1664525 + 1013904223
        return (state ushr 1).toDouble() / 2147483648.0
    }
}

/** One sine cloud of `scatter-large`, `[x, sin(x) - 0.1 * x * random + offset / 10]`. */
internal fun largeCloud(random: ScatterRandom, offset: Double): List<DataValue> =
    List(LARGE_SCATTER_POINTS) {
        val x = random.next() * 10
        val y = kotlin.math.sin(x) - x * 0.1 * random.next() + offset / 10
        point(x, y)
    }

/** The dashed data-range box of one `scatter-weight` series. */
internal fun dataRangeArea(areaName: String): MarkAreaOption =
    MarkAreaOption().apply {
        silent = true
        itemStyle =
            ItemStyleOption().apply {
                color = "transparent".asColor()
                borderWidth = 1.0
                borderType = "dashed"
            }
        data =
            listOf(
                MarkAreaDataItem.of(
                    MarkAreaData().apply {
                        name = areaName
                        coord = listOf(str("min"), str("min"))
                    },
                    MarkAreaData().apply { coord = listOf(str("max"), str("max")) },
                ),
            )
    }

/** The highest and the lowest person of one `scatter-weight` series. */
internal fun weightExtremes(): MarkPointOption =
    MarkPointOption().apply {
        data =
            listOf(
                MarkPointData().apply {
                    type = "max"
                    name = "Max"
                },
                MarkPointData().apply {
                    type = "min"
                    name = "Min"
                },
            )
    }

/** The average line plus the vertical split of one `scatter-weight` series. */
internal fun weightAverageLine(averageName: String, splitHeight: Double): MarkLineOption =
    MarkLineOption().apply {
        lineStyle = LineStyleOption().apply { type = "solid" }
        data =
            listOf(
                MarkLineDataItem.of(
                    MarkLineData().apply {
                        type = "average"
                        name = averageName
                    },
                ),
                MarkLineDataItem.of(MarkLineData().apply { xAxis = num(splitHeight) }),
            )
    }

/** The body of one `scatter-weight` series. */
internal fun SeriesOption.weightSeries(
    seriesName: String,
    rows: Array<DoubleArray>,
    averageName: String,
    splitHeight: Double,
) {
    name = seriesName
    emphasis = focusSeries()
    data = rows.map { point(it[0], it[1]) }
    markArea = dataRangeArea("$seriesName Data Range")
    markPoint = weightExtremes()
    markLine = weightAverageLine(averageName, splitHeight)
}

/** The colour of one city in `scatter-matrix`, the palette of its piecewise visual map. */
internal fun cityColor(city: String): String =
    when (city) {
        "北京" -> "#51689b"
        "上海" -> "#ce5c5c"
        else -> "#fbc357"
    }

/** One cell of the matrix: the rows projected onto dimensions [dimX] and [dimY]. */
internal fun matrixItems(dimX: Int, dimY: Int): List<DataValue> =
    MATRIX_ROWS.map { row ->
        dataItem {
            value =
                arrayValue(
                    valueOf(row.values[dimX]),
                    valueOf(row.values[dimY]),
                    textValue(row.city),
                )
            itemStyle = ItemStyleOption().apply { color = cityColor(row.city).asColor() }
        }
    }

/** The seven-value rows of the parallel block of `scatter-matrix`. */
internal fun parallelItems(): List<DataValue> =
    MATRIX_ROWS.map { row ->
        arrayValue(
            valueOf(row.values[0]),
            valueOf(row.values[1]),
            valueOf(row.values[2]),
            valueOf(row.values[3]),
            valueOf(row.values[4]),
            valueOf(row.values[5]),
            textValue(row.grade),
        )
    }

/** The generated grids, axes and series of the lower triangle of the matrix. */
internal class MatrixCells(
    val grids: List<GridOption>,
    val xAxes: List<AxisOption>,
    val yAxes: List<AxisOption>,
    val series: List<SeriesOption>,
)

/** Builds the matrix the way the original walks its table: one cell per dimension pair. */
internal fun matrixCells(): MatrixCells {
    val grids = mutableListOf<GridOption>()
    val xAxes = mutableListOf<AxisOption>()
    val yAxes = mutableListOf<AxisOption>()
    val series = mutableListOf<SeriesOption>()
    var index = 0
    for (rowIndex in 0 until MATRIX_DIMS) {
        for (columnIndex in 0 until MATRIX_DIMS) {
            if (columnIndex >= rowIndex) continue
            grids +=
                GridOption().apply {
                    left = str("${MATRIX_BASE_LEFT + rowIndex * MATRIX_STEP}%")
                    top = str("${MATRIX_BASE_TOP + columnIndex * MATRIX_STEP}%")
                    width = str(MATRIX_CELL_WIDTH)
                    height = str(MATRIX_CELL_HEIGHT)
                }
            xAxes +=
                AxisOption().apply {
                    type = AxisType.Value
                    splitNumber = 3
                    position = "top"
                    gridIndex = index
                    scale = true
                    axisLine = AxisLineOption().apply { show = columnIndex == 0; onZero = false }
                    axisTick = AxisTickOption().apply { show = columnIndex == 0; inside = true }
                    axisLabel = AxisLabelOption().apply { show = columnIndex == 0 }
                }
            yAxes +=
                AxisOption().apply {
                    type = AxisType.Value
                    splitNumber = 3
                    position = "right"
                    gridIndex = index
                    scale = true
                    axisLine = AxisLineOption().apply { show = rowIndex == MATRIX_DIMS - 1; onZero = false }
                    axisTick = AxisTickOption().apply { show = rowIndex == MATRIX_DIMS - 1; inside = true }
                    axisLabel = AxisLabelOption().apply { show = rowIndex == MATRIX_DIMS - 1 }
                }
            series +=
                SeriesOption(SeriesType.Scatter).apply {
                    symbolSize = NumberOrStringOrNumbers.of(4)
                    xAxisIndex = index
                    yAxisIndex = index
                    data = matrixItems(rowIndex, columnIndex)
                }
            index++
        }
    }
    return MatrixCells(grids, xAxes, yAxes, series)
}
