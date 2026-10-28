package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.round
import zrender.ZRColor

/*
 * The two Shanghai-index pages, ported from `public/examples/ts/candlestick-sh.ts` and
 * `candlestick-sh-2015.ts`.
 *
 * Both draw the daily candles of the Shanghai Composite under a bundle of four moving averages. The
 * first page covers the first half of 2013 and marks the extremes of the four price dimensions; the
 * second one covers the whole of 2015 and keeps the two empty rows the upstream table starts with:
 * that table is written newest first and reversed, so the two `['', '', ...]` rows become two leading
 * zero candles, which is also why the y axis of the official thumbnail starts at zero.
 *
 * The tables are transcribed as [OhlcDay] rows and the averages are computed at page build time, the
 * same way as the upstream `splitData` / `calculateMA` helpers: the first `window - 1` points of a
 * line are `'-'`, the empty value ECharts skips.
 */

private val candlestickLongTail = Category(Groups.CANDLESTICK, "examples/CandlestickLongTail.kt")

/** Color of a rising candle, the upstream `upColor` of `candlestick-sh`. */
private const val UP_COLOR = "#ec0000"

/** Border color of a rising candle; see [UP_COLOR]. */
private const val UP_BORDER_COLOR = "#8A0000"

/** Color of a falling candle; see [UP_COLOR]. */
private const val DOWN_COLOR = "#00da3c"

/** Border color of a falling candle; see [UP_COLOR]. */
private const val DOWN_BORDER_COLOR = "#008F28"

/** Window lengths of the four moving-average lines of both pages. */
private val MA_WINDOWS = intArrayOf(5, 10, 20, 30)

/** Color of the axes, the legend and the data shadow of `candlestick-sh-2015`. */
private const val AXIS_2015_COLOR = "#8392A5"

/** Color of the crosshair of `candlestick-sh-2015`. */
private const val CROSS_2015_COLOR = "#376df4"

/** Handle icon of the slider of `candlestick-sh-2015`, copied verbatim from upstream. */
private const val HANDLE_2015_ICON =
    "path://M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z"

/** One row of an upstream `[date, open, close, lowest, highest]` table. */
private class OhlcDay(
    val date: String,
    val open: Double,
    val close: Double,
    val lowest: Double,
    val highest: Double,
)

/** The 88 rows of `candlestick-sh`, from 2013/1/24 to 2013/6/13. */
private val SHANGHAI_2013 =
    arrayOf(
        OhlcDay("2013/1/24", 2320.26, 2320.26, 2287.3, 2362.94),
        OhlcDay("2013/1/25", 2300.0, 2291.3, 2288.26, 2308.38),
        OhlcDay("2013/1/28", 2295.35, 2346.5, 2295.35, 2346.92),
        OhlcDay("2013/1/29", 2347.22, 2358.98, 2337.35, 2363.8),
        OhlcDay("2013/1/30", 2360.75, 2382.48, 2347.89, 2383.76),
        OhlcDay("2013/1/31", 2383.43, 2385.42, 2371.23, 2391.82),
        OhlcDay("2013/2/1", 2377.41, 2419.02, 2369.57, 2421.15),
        OhlcDay("2013/2/4", 2425.92, 2428.15, 2417.58, 2440.38),
        OhlcDay("2013/2/5", 2411.0, 2433.13, 2403.3, 2437.42),
        OhlcDay("2013/2/6", 2432.68, 2434.48, 2427.7, 2441.73),
        OhlcDay("2013/2/7", 2430.69, 2418.53, 2394.22, 2433.89),
        OhlcDay("2013/2/8", 2416.62, 2432.4, 2414.4, 2443.03),
        OhlcDay("2013/2/18", 2441.91, 2421.56, 2415.43, 2444.8),
        OhlcDay("2013/2/19", 2420.26, 2382.91, 2373.53, 2427.07),
        OhlcDay("2013/2/20", 2383.49, 2397.18, 2370.61, 2397.94),
        OhlcDay("2013/2/21", 2378.82, 2325.95, 2309.17, 2378.82),
        OhlcDay("2013/2/22", 2322.94, 2314.16, 2308.76, 2330.88),
        OhlcDay("2013/2/25", 2320.62, 2325.82, 2315.01, 2338.78),
        OhlcDay("2013/2/26", 2313.74, 2293.34, 2289.89, 2340.71),
        OhlcDay("2013/2/27", 2297.77, 2313.22, 2292.03, 2324.63),
        OhlcDay("2013/2/28", 2322.32, 2365.59, 2308.92, 2366.16),
        OhlcDay("2013/3/1", 2364.54, 2359.51, 2330.86, 2369.65),
        OhlcDay("2013/3/4", 2332.08, 2273.4, 2259.25, 2333.54),
        OhlcDay("2013/3/5", 2274.81, 2326.31, 2270.1, 2328.14),
        OhlcDay("2013/3/6", 2333.61, 2347.18, 2321.6, 2351.44),
        OhlcDay("2013/3/7", 2340.44, 2324.29, 2304.27, 2352.02),
        OhlcDay("2013/3/8", 2326.42, 2318.61, 2314.59, 2333.67),
        OhlcDay("2013/3/11", 2314.68, 2310.59, 2296.58, 2320.96),
        OhlcDay("2013/3/12", 2309.16, 2286.6, 2264.83, 2333.29),
        OhlcDay("2013/3/13", 2282.17, 2263.97, 2253.25, 2286.33),
        OhlcDay("2013/3/14", 2255.77, 2270.28, 2253.31, 2276.22),
        OhlcDay("2013/3/15", 2269.31, 2278.4, 2250.0, 2312.08),
        OhlcDay("2013/3/18", 2267.29, 2240.02, 2239.21, 2276.05),
        OhlcDay("2013/3/19", 2244.26, 2257.43, 2232.02, 2261.31),
        OhlcDay("2013/3/20", 2257.74, 2317.37, 2257.42, 2317.86),
        OhlcDay("2013/3/21", 2318.21, 2324.24, 2311.6, 2330.81),
        OhlcDay("2013/3/22", 2321.4, 2328.28, 2314.97, 2332.0),
        OhlcDay("2013/3/25", 2334.74, 2326.72, 2319.91, 2344.89),
        OhlcDay("2013/3/26", 2318.58, 2297.67, 2281.12, 2319.99),
        OhlcDay("2013/3/27", 2299.38, 2301.26, 2289.0, 2323.48),
        OhlcDay("2013/3/28", 2273.55, 2236.3, 2232.91, 2273.55),
        OhlcDay("2013/3/29", 2238.49, 2236.62, 2228.81, 2246.87),
        OhlcDay("2013/4/1", 2229.46, 2234.4, 2227.31, 2243.95),
        OhlcDay("2013/4/2", 2234.9, 2227.74, 2220.44, 2253.42),
        OhlcDay("2013/4/3", 2232.69, 2225.29, 2217.25, 2241.34),
        OhlcDay("2013/4/8", 2196.24, 2211.59, 2180.67, 2212.59),
        OhlcDay("2013/4/9", 2215.47, 2225.77, 2215.47, 2234.73),
        OhlcDay("2013/4/10", 2224.93, 2226.13, 2212.56, 2233.04),
        OhlcDay("2013/4/11", 2236.98, 2219.55, 2217.26, 2242.48),
        OhlcDay("2013/4/12", 2218.09, 2206.78, 2204.44, 2226.26),
        OhlcDay("2013/4/15", 2199.91, 2181.94, 2177.39, 2204.99),
        OhlcDay("2013/4/16", 2169.63, 2194.85, 2165.78, 2196.43),
        OhlcDay("2013/4/17", 2195.03, 2193.8, 2178.47, 2197.51),
        OhlcDay("2013/4/18", 2181.82, 2197.6, 2175.44, 2206.03),
        OhlcDay("2013/4/19", 2201.12, 2244.64, 2200.58, 2250.11),
        OhlcDay("2013/4/22", 2236.4, 2242.17, 2232.26, 2245.12),
        OhlcDay("2013/4/23", 2242.62, 2184.54, 2182.81, 2242.62),
        OhlcDay("2013/4/24", 2187.35, 2218.32, 2184.11, 2226.12),
        OhlcDay("2013/4/25", 2213.19, 2199.31, 2191.85, 2224.63),
        OhlcDay("2013/4/26", 2203.89, 2177.91, 2173.86, 2210.58),
        OhlcDay("2013/5/2", 2170.78, 2174.12, 2161.14, 2179.65),
        OhlcDay("2013/5/3", 2179.05, 2205.5, 2179.05, 2222.81),
        OhlcDay("2013/5/6", 2212.5, 2231.17, 2212.5, 2236.07),
        OhlcDay("2013/5/7", 2227.86, 2235.57, 2219.44, 2240.26),
        OhlcDay("2013/5/8", 2242.39, 2246.3, 2235.42, 2255.21),
        OhlcDay("2013/5/9", 2246.96, 2232.97, 2221.38, 2247.86),
        OhlcDay("2013/5/10", 2228.82, 2246.83, 2225.81, 2247.67),
        OhlcDay("2013/5/13", 2247.68, 2241.92, 2231.36, 2250.85),
        OhlcDay("2013/5/14", 2238.9, 2217.01, 2205.87, 2239.93),
        OhlcDay("2013/5/15", 2217.09, 2224.8, 2213.58, 2225.19),
        OhlcDay("2013/5/16", 2221.34, 2251.81, 2210.77, 2252.87),
        OhlcDay("2013/5/17", 2249.81, 2282.87, 2248.41, 2288.09),
        OhlcDay("2013/5/20", 2286.33, 2299.99, 2281.9, 2309.39),
        OhlcDay("2013/5/21", 2297.11, 2305.11, 2290.12, 2305.3),
        OhlcDay("2013/5/22", 2303.75, 2302.4, 2292.43, 2314.18),
        OhlcDay("2013/5/23", 2293.81, 2275.67, 2274.1, 2304.95),
        OhlcDay("2013/5/24", 2281.45, 2288.53, 2270.25, 2292.59),
        OhlcDay("2013/5/27", 2286.66, 2293.08, 2283.94, 2301.7),
        OhlcDay("2013/5/28", 2293.4, 2321.32, 2281.47, 2322.1),
        OhlcDay("2013/5/29", 2323.54, 2324.02, 2321.17, 2334.33),
        OhlcDay("2013/5/30", 2316.25, 2317.75, 2310.49, 2325.72),
        OhlcDay("2013/5/31", 2320.74, 2300.59, 2299.37, 2325.53),
        OhlcDay("2013/6/3", 2300.21, 2299.25, 2294.11, 2313.43),
        OhlcDay("2013/6/4", 2297.1, 2272.42, 2264.76, 2297.1),
        OhlcDay("2013/6/5", 2270.71, 2270.93, 2260.87, 2276.86),
        OhlcDay("2013/6/6", 2264.43, 2242.11, 2240.07, 2266.69),
        OhlcDay("2013/6/7", 2242.26, 2210.9, 2205.07, 2250.63),
        OhlcDay("2013/6/13", 2190.1, 2148.35, 2126.22, 2190.1),
    )

/** The 246 rows of `candlestick-sh-2015`, the last two of them empty. */
private val SHANGHAI_2015 =
    arrayOf(
        OhlcDay("", 0.0, 0.0, 0.0, 0.0),
        OhlcDay("", 0.0, 0.0, 0.0, 0.0),
        OhlcDay("2015/1/5", 3258.63, 3350.52, 3253.88, 3369.28),
        OhlcDay("2015/1/6", 3330.8, 3351.45, 3303.18, 3394.22),
        OhlcDay("2015/1/7", 3326.65, 3373.95, 3312.21, 3374.9),
        OhlcDay("2015/1/8", 3371.96, 3293.46, 3285.09, 3381.57),
        OhlcDay("2015/1/9", 3276.97, 3285.41, 3267.51, 3404.83),
        OhlcDay("2015/1/12", 3258.21, 3229.32, 3191.58, 3275.19),
        OhlcDay("2015/1/13", 3223.54, 3235.3, 3214.41, 3259.39),
        OhlcDay("2015/1/14", 3242.34, 3222.44, 3193.98, 3268.48),
        OhlcDay("2015/1/15", 3224.07, 3336.46, 3207.54, 3337.08),
        OhlcDay("2015/1/16", 3343.6, 3376.5, 3340.49, 3400.32),
        OhlcDay("2015/1/19", 3189.73, 3116.35, 3095.07, 3262.21),
        OhlcDay("2015/1/20", 3114.56, 3173.05, 3100.48, 3190.25),
        OhlcDay("2015/1/21", 3189.08, 3323.61, 3178.34, 3337.0),
        OhlcDay("2015/1/22", 3327.32, 3343.34, 3293.98, 3352.38),
        OhlcDay("2015/1/23", 3357.1, 3351.76, 3328.29, 3406.79),
        OhlcDay("2015/1/26", 3347.26, 3383.18, 3321.31, 3384.8),
        OhlcDay("2015/1/27", 3389.85, 3352.96, 3290.22, 3390.22),
        OhlcDay("2015/1/28", 3325.72, 3305.74, 3294.66, 3354.8),
        OhlcDay("2015/1/29", 3259.0, 3262.3, 3234.24, 3286.79),
        OhlcDay("2015/1/30", 3273.75, 3210.36, 3210.31, 3288.5),
        OhlcDay("2015/2/2", 3148.14, 3128.3, 3122.57, 3175.13),
        OhlcDay("2015/2/3", 3156.09, 3204.91, 3129.73, 3207.94),
        OhlcDay("2015/2/4", 3212.82, 3174.13, 3171.14, 3238.98),
        OhlcDay("2015/2/5", 3251.21, 3136.53, 3135.82, 3251.21),
        OhlcDay("2015/2/6", 3120.09, 3075.91, 3052.94, 3129.54),
        OhlcDay("2015/2/9", 3063.51, 3095.12, 3049.11, 3119.03),
        OhlcDay("2015/2/10", 3090.49, 3141.59, 3084.25, 3142.1),
        OhlcDay("2015/2/11", 3145.76, 3157.7, 3139.05, 3166.42),
        OhlcDay("2015/2/12", 3157.96, 3173.42, 3134.24, 3181.77),
        OhlcDay("2015/2/13", 3186.81, 3203.83, 3182.79, 3237.16),
        OhlcDay("2015/2/16", 3206.14, 3222.36, 3195.88, 3228.85),
        OhlcDay("2015/2/17", 3230.88, 3246.91, 3230.77, 3255.73),
        OhlcDay("2015/2/25", 3256.48, 3228.84, 3215.55, 3257.22),
        OhlcDay("2015/2/26", 3222.15, 3298.36, 3202.19, 3300.62),
        OhlcDay("2015/2/27", 3296.83, 3310.3, 3291.01, 3324.55),
        OhlcDay("2015/3/2", 3332.72, 3336.28, 3298.67, 3336.76),
        OhlcDay("2015/3/3", 3317.7, 3263.05, 3260.43, 3317.7),
        OhlcDay("2015/3/4", 3264.18, 3279.53, 3250.48, 3286.59),
        OhlcDay("2015/3/5", 3264.08, 3248.48, 3221.67, 3266.64),
        OhlcDay("2015/3/6", 3248.04, 3241.19, 3234.53, 3266.93),
        OhlcDay("2015/3/9", 3224.31, 3302.41, 3198.37, 3307.7),
        OhlcDay("2015/3/10", 3289.08, 3286.07, 3277.09, 3309.92),
        OhlcDay("2015/3/11", 3289.59, 3290.9, 3278.47, 3325.05),
        OhlcDay("2015/3/12", 3314.81, 3349.32, 3300.49, 3360.05),
        OhlcDay("2015/3/13", 3359.49, 3372.91, 3352.15, 3391.25),
        OhlcDay("2015/3/16", 3391.16, 3449.3, 3377.09, 3449.3),
        OhlcDay("2015/3/17", 3469.6, 3502.85, 3459.69, 3504.12),
        OhlcDay("2015/3/18", 3510.5, 3577.3, 3503.85, 3577.66),
        OhlcDay("2015/3/19", 3576.02, 3582.27, 3546.84, 3600.68),
        OhlcDay("2015/3/20", 3587.08, 3617.32, 3569.38, 3632.34),
        OhlcDay("2015/3/23", 3640.1, 3687.73, 3635.49, 3688.25),
        OhlcDay("2015/3/24", 3692.57, 3691.41, 3600.7, 3715.87),
        OhlcDay("2015/3/25", 3680.95, 3660.73, 3634.56, 3693.15),
        OhlcDay("2015/3/26", 3641.94, 3682.09, 3615.01, 3707.32),
        OhlcDay("2015/3/27", 3686.13, 3691.1, 3656.83, 3710.48),
        OhlcDay("2015/3/30", 3710.61, 3786.57, 3710.61, 3795.94),
        OhlcDay("2015/3/31", 3822.99, 3747.9, 3737.04, 3835.57),
        OhlcDay("2015/4/1", 3748.34, 3810.29, 3742.21, 3817.08),
        OhlcDay("2015/4/2", 3827.69, 3825.78, 3775.89, 3835.45),
        OhlcDay("2015/4/3", 3803.38, 3863.93, 3792.21, 3864.41),
        OhlcDay("2015/4/7", 3899.42, 3961.38, 3891.73, 3961.67),
        OhlcDay("2015/4/8", 3976.53, 3994.81, 3903.65, 4000.22),
        OhlcDay("2015/4/9", 4006.13, 3957.53, 3900.03, 4016.4),
        OhlcDay("2015/4/10", 3947.49, 4034.31, 3929.32, 4040.35),
        OhlcDay("2015/4/13", 4072.72, 4121.71, 4057.29, 4128.07),
        OhlcDay("2015/4/14", 4125.78, 4135.56, 4091.26, 4168.35),
        OhlcDay("2015/4/15", 4135.65, 4084.16, 4069.01, 4175.49),
        OhlcDay("2015/4/16", 4055.92, 4194.82, 4031.24, 4195.31),
        OhlcDay("2015/4/17", 4254.72, 4287.3, 4238.91, 4317.22),
        OhlcDay("2015/4/20", 4301.35, 4217.08, 4190.68, 4356.0),
        OhlcDay("2015/4/21", 4212.19, 4293.62, 4188.57, 4294.38),
        OhlcDay("2015/4/22", 4304.6, 4398.49, 4297.95, 4400.19),
        OhlcDay("2015/4/23", 4414.48, 4414.51, 4358.84, 4444.41),
        OhlcDay("2015/4/24", 4355.95, 4393.69, 4318.12, 4416.38),
        OhlcDay("2015/4/27", 4441.93, 4527.4, 4441.93, 4529.73),
        OhlcDay("2015/4/28", 4527.63, 4476.21, 4432.9, 4572.39),
        OhlcDay("2015/4/29", 4446.12, 4476.62, 4398.64, 4499.94),
        OhlcDay("2015/4/30", 4483.01, 4441.65, 4441.05, 4507.34),
        OhlcDay("2015/5/4", 4441.34, 4480.46, 4387.43, 4487.57),
        OhlcDay("2015/5/5", 4479.85, 4298.71, 4282.24, 4488.87),
        OhlcDay("2015/5/6", 4311.64, 4229.27, 4187.37, 4376.35),
        OhlcDay("2015/5/7", 4197.9, 4112.21, 4108.01, 4213.76),
        OhlcDay("2015/5/8", 4152.98, 4205.92, 4099.04, 4206.86),
        OhlcDay("2015/5/11", 4231.27, 4333.58, 4187.82, 4334.88),
        OhlcDay("2015/5/12", 4342.37, 4401.22, 4317.98, 4402.31),
        OhlcDay("2015/5/13", 4402.38, 4375.76, 4342.48, 4415.63),
        OhlcDay("2015/5/14", 4372.82, 4378.31, 4329.04, 4397.75),
        OhlcDay("2015/5/15", 4366.82, 4308.69, 4278.55, 4366.82),
        OhlcDay("2015/5/18", 4277.9, 4283.49, 4260.51, 4324.83),
        OhlcDay("2015/5/19", 4285.78, 4417.55, 4285.78, 4418.4),
        OhlcDay("2015/5/20", 4434.98, 4446.29, 4432.28, 4520.54),
        OhlcDay("2015/5/21", 4456.44, 4529.42, 4438.26, 4530.48),
        OhlcDay("2015/5/22", 4584.98, 4657.6, 4562.99, 4658.27),
        OhlcDay("2015/5/25", 4660.08, 4813.8, 4656.83, 4814.67),
        OhlcDay("2015/5/26", 4854.85, 4910.9, 4779.08, 4911.69),
        OhlcDay("2015/5/27", 4932.85, 4941.71, 4857.06, 4958.16),
        OhlcDay("2015/5/28", 4943.74, 4620.27, 4614.24, 4986.5),
        OhlcDay("2015/5/29", 4603.47, 4611.74, 4431.56, 4698.19),
        OhlcDay("2015/6/1", 4633.1, 4828.74, 4615.23, 4829.5),
        OhlcDay("2015/6/2", 4844.7, 4910.53, 4797.55, 4911.57),
        OhlcDay("2015/6/3", 4924.38, 4909.98, 4822.44, 4942.06),
        OhlcDay("2015/6/4", 4912.95, 4947.1, 4647.41, 4947.96),
        OhlcDay("2015/6/5", 5016.09, 5023.1, 4898.07, 5051.63),
        OhlcDay("2015/6/8", 5045.69, 5131.88, 4997.48, 5146.95),
        OhlcDay("2015/6/9", 5145.98, 5113.53, 5042.96, 5147.45),
        OhlcDay("2015/6/10", 5049.2, 5106.04, 5001.49, 5164.16),
        OhlcDay("2015/6/11", 5101.44, 5121.59, 5050.76, 5122.46),
        OhlcDay("2015/6/12", 5143.34, 5166.35, 5103.4, 5178.19),
        OhlcDay("2015/6/15", 5174.42, 5062.99, 5048.74, 5176.79),
        OhlcDay("2015/6/16", 5004.41, 4887.43, 4842.1, 5029.68),
        OhlcDay("2015/6/17", 4890.55, 4967.9, 4767.22, 4983.66),
        OhlcDay("2015/6/18", 4942.52, 4785.36, 4780.87, 4966.77),
        OhlcDay("2015/6/19", 4689.93, 4478.36, 4476.5, 4744.08),
        OhlcDay("2015/6/23", 4471.61, 4576.49, 4264.77, 4577.94),
        OhlcDay("2015/6/24", 4604.58, 4690.15, 4552.13, 4691.77),
        OhlcDay("2015/6/25", 4711.76, 4527.78, 4483.55, 4720.7),
        OhlcDay("2015/6/26", 4399.93, 4192.87, 4139.53, 4456.9),
        OhlcDay("2015/6/29", 4289.77, 4053.03, 3875.05, 4297.47),
        OhlcDay("2015/6/30", 4006.75, 4277.22, 3847.88, 4279.97),
        OhlcDay("2015/7/1", 4214.15, 4053.7, 4043.37, 4317.05),
        OhlcDay("2015/7/2", 4058.62, 3912.77, 3795.25, 4080.39),
        OhlcDay("2015/7/3", 3793.71, 3686.92, 3629.56, 3927.13),
        OhlcDay("2015/7/6", 3975.21, 3775.91, 3653.04, 3975.21),
        OhlcDay("2015/7/7", 3654.78, 3727.12, 3585.4, 3750.57),
        OhlcDay("2015/7/8", 3467.4, 3507.19, 3421.53, 3599.25),
        OhlcDay("2015/7/9", 3432.45, 3709.33, 3373.54, 3748.48),
        OhlcDay("2015/7/10", 3707.46, 3877.8, 3677.43, 3959.22),
        OhlcDay("2015/7/13", 3918.99, 3970.39, 3858.64, 4030.19),
        OhlcDay("2015/7/14", 3958.37, 3924.49, 3855.56, 4035.43),
        OhlcDay("2015/7/15", 3874.97, 3805.7, 3741.25, 3914.27),
        OhlcDay("2015/7/16", 3758.5, 3823.18, 3688.44, 3877.51),
        OhlcDay("2015/7/17", 3831.42, 3957.35, 3814.15, 3994.48),
        OhlcDay("2015/7/20", 3948.42, 3992.11, 3927.12, 4021.33),
        OhlcDay("2015/7/21", 3939.9, 4017.67, 3912.8, 4041.82),
        OhlcDay("2015/7/22", 3996.43, 4026.04, 3960.86, 4042.34),
        OhlcDay("2015/7/23", 4022.27, 4123.92, 4019.04, 4132.61),
        OhlcDay("2015/7/24", 4124.75, 4070.91, 4044.83, 4184.45),
        OhlcDay("2015/7/27", 3985.57, 3725.56, 3720.44, 4051.16),
        OhlcDay("2015/7/28", 3573.14, 3663.0, 3537.36, 3762.53),
        OhlcDay("2015/7/29", 3689.82, 3789.17, 3612.06, 3792.07),
        OhlcDay("2015/7/30", 3773.79, 3705.77, 3685.96, 3844.37),
        OhlcDay("2015/7/31", 3655.67, 3663.73, 3620.17, 3729.51),
        OhlcDay("2015/8/3", 3614.99, 3622.91, 3549.5, 3648.94),
        OhlcDay("2015/8/4", 3621.85, 3756.54, 3601.29, 3757.03),
        OhlcDay("2015/8/5", 3745.65, 3694.57, 3676.39, 3782.35),
        OhlcDay("2015/8/6", 3625.5, 3661.54, 3614.74, 3710.57),
        OhlcDay("2015/8/7", 3692.61, 3744.2, 3686.3, 3756.74),
        OhlcDay("2015/8/10", 3786.03, 3928.42, 3775.85, 3943.62),
        OhlcDay("2015/8/11", 3928.81, 3927.91, 3891.18, 3970.34),
        OhlcDay("2015/8/12", 3881.23, 3886.32, 3871.14, 3937.77),
        OhlcDay("2015/8/13", 3869.91, 3954.56, 3838.16, 3955.79),
        OhlcDay("2015/8/14", 3976.41, 3965.33, 3939.83, 4000.68),
        OhlcDay("2015/8/17", 3947.84, 3993.67, 3907.4, 3994.54),
        OhlcDay("2015/8/18", 3999.13, 3748.16, 3743.39, 4006.34),
        OhlcDay("2015/8/19", 3646.8, 3794.11, 3558.38, 3811.43),
        OhlcDay("2015/8/20", 3754.57, 3664.29, 3663.61, 3788.01),
        OhlcDay("2015/8/21", 3609.96, 3507.74, 3490.54, 3652.84),
        OhlcDay("2015/8/24", 3373.48, 3209.91, 3191.88, 3388.36),
        OhlcDay("2015/8/25", 3004.13, 2964.97, 2947.94, 3123.03),
        OhlcDay("2015/8/26", 2980.79, 2927.29, 2850.71, 3092.04),
        OhlcDay("2015/8/27", 2978.03, 3083.59, 2906.49, 3085.42),
        OhlcDay("2015/8/28", 3125.26, 3232.35, 3102.95, 3235.84),
        OhlcDay("2015/8/31", 3203.56, 3205.99, 3109.16, 3207.86),
        OhlcDay("2015/9/1", 3157.83, 3166.62, 3053.74, 3180.33),
        OhlcDay("2015/9/2", 3027.68, 3160.17, 3019.09, 3194.48),
        OhlcDay("2015/9/7", 3149.38, 3080.42, 3066.3, 3217.58),
        OhlcDay("2015/9/8", 3054.44, 3170.45, 3011.12, 3174.71),
        OhlcDay("2015/9/9", 3182.55, 3243.09, 3165.7, 3256.74),
        OhlcDay("2015/9/10", 3190.55, 3197.89, 3178.9, 3243.28),
        OhlcDay("2015/9/11", 3189.48, 3200.23, 3163.45, 3223.76),
        OhlcDay("2015/9/14", 3221.17, 3114.8, 3049.23, 3229.48),
        OhlcDay("2015/9/15", 3043.8, 3005.17, 2983.92, 3081.7),
        OhlcDay("2015/9/16", 2998.04, 3152.26, 2983.54, 3182.93),
        OhlcDay("2015/9/17", 3131.98, 3086.06, 3085.31, 3204.7),
        OhlcDay("2015/9/18", 3100.28, 3097.92, 3070.34, 3122.05),
        OhlcDay("2015/9/21", 3072.09, 3156.54, 3060.86, 3159.88),
        OhlcDay("2015/9/22", 3161.32, 3185.62, 3152.48, 3213.48),
        OhlcDay("2015/9/23", 3137.72, 3115.89, 3104.74, 3164.04),
        OhlcDay("2015/9/24", 3126.49, 3142.69, 3109.69, 3151.16),
        OhlcDay("2015/9/25", 3130.85, 3092.35, 3063.0, 3149.95),
        OhlcDay("2015/9/28", 3085.57, 3100.76, 3042.31, 3103.07),
        OhlcDay("2015/9/29", 3055.22, 3038.14, 3021.16, 3068.3),
        OhlcDay("2015/9/30", 3052.84, 3052.78, 3039.74, 3073.3),
        OhlcDay("2015/10/8", 3156.07, 3143.36, 3133.13, 3172.28),
        OhlcDay("2015/10/9", 3146.64, 3183.15, 3137.79, 3192.72),
        OhlcDay("2015/10/12", 3193.54, 3287.66, 3188.41, 3318.71),
        OhlcDay("2015/10/13", 3262.16, 3293.23, 3253.25, 3298.63),
        OhlcDay("2015/10/14", 3280.02, 3262.44, 3256.25, 3307.32),
        OhlcDay("2015/10/15", 3255.03, 3338.07, 3254.39, 3338.3),
        OhlcDay("2015/10/16", 3358.3, 3391.35, 3334.85, 3393.02),
        OhlcDay("2015/10/19", 3401.63, 3386.7, 3355.57, 3423.4),
        OhlcDay("2015/10/20", 3377.55, 3425.33, 3357.86, 3425.52),
        OhlcDay("2015/10/21", 3428.56, 3320.68, 3265.44, 3447.26),
        OhlcDay("2015/10/22", 3292.29, 3368.74, 3282.99, 3373.78),
        OhlcDay("2015/10/23", 3377.55, 3412.43, 3360.22, 3422.02),
        OhlcDay("2015/10/26", 3448.65, 3429.58, 3402.0, 3457.52),
        OhlcDay("2015/10/27", 3409.14, 3434.34, 3332.62, 3441.57),
        OhlcDay("2015/10/28", 3417.01, 3375.2, 3367.23, 3439.76),
        OhlcDay("2015/10/29", 3387.77, 3387.32, 3362.51, 3411.71),
        OhlcDay("2015/10/30", 3380.28, 3382.56, 3346.59, 3417.2),
        OhlcDay("2015/11/2", 3337.58, 3325.08, 3322.31, 3391.06),
        OhlcDay("2015/11/3", 3330.32, 3316.7, 3302.18, 3346.27),
        OhlcDay("2015/11/4", 3325.62, 3459.64, 3325.62, 3459.65),
        OhlcDay("2015/11/5", 3459.22, 3522.82, 3455.53, 3585.66),
        OhlcDay("2015/11/6", 3514.44, 3590.03, 3508.83, 3596.38),
        OhlcDay("2015/11/9", 3588.5, 3646.88, 3588.5, 3673.76),
        OhlcDay("2015/11/10", 3617.4, 3640.49, 3607.89, 3669.53),
        OhlcDay("2015/11/11", 3635.0, 3650.25, 3605.62, 3654.88),
        OhlcDay("2015/11/12", 3656.82, 3632.9, 3603.23, 3659.31),
        OhlcDay("2015/11/13", 3600.76, 3580.84, 3564.81, 3632.56),
        OhlcDay("2015/11/16", 3522.46, 3606.96, 3519.42, 3607.61),
        OhlcDay("2015/11/17", 3629.98, 3604.8, 3598.07, 3678.27),
        OhlcDay("2015/11/18", 3605.06, 3568.47, 3558.7, 3617.07),
        OhlcDay("2015/11/19", 3573.78, 3617.06, 3561.04, 3618.21),
        OhlcDay("2015/11/20", 3620.79, 3630.5, 3607.92, 3640.53),
        OhlcDay("2015/11/23", 3630.87, 3610.31, 3598.87, 3654.75),
        OhlcDay("2015/11/24", 3602.89, 3616.11, 3563.1, 3616.48),
        OhlcDay("2015/11/25", 3614.07, 3647.93, 3607.52, 3648.37),
        OhlcDay("2015/11/26", 3659.57, 3635.55, 3629.86, 3668.38),
        OhlcDay("2015/11/27", 3616.54, 3436.3, 3412.43, 3621.9),
        OhlcDay("2015/11/30", 3433.85, 3445.4, 3327.81, 3470.37),
        OhlcDay("2015/12/1", 3442.44, 3456.31, 3417.54, 3483.41),
        OhlcDay("2015/12/2", 3450.28, 3536.91, 3427.66, 3538.85),
        OhlcDay("2015/12/3", 3525.73, 3584.82, 3517.23, 3591.73),
        OhlcDay("2015/12/4", 3558.15, 3524.99, 3510.41, 3568.97),
        OhlcDay("2015/12/7", 3529.81, 3536.93, 3506.62, 3543.95),
        OhlcDay("2015/12/8", 3518.65, 3470.07, 3466.79, 3518.65),
        OhlcDay("2015/12/9", 3462.58, 3472.44, 3454.88, 3495.7),
        OhlcDay("2015/12/10", 3469.81, 3455.5, 3446.27, 3503.65),
        OhlcDay("2015/12/11", 3441.6, 3434.58, 3410.92, 3455.55),
        OhlcDay("2015/12/14", 3403.51, 3520.67, 3399.28, 3521.78),
        OhlcDay("2015/12/15", 3518.13, 3510.35, 3496.85, 3529.96),
        OhlcDay("2015/12/16", 3522.09, 3516.19, 3506.29, 3538.69),
        OhlcDay("2015/12/17", 3533.63, 3580.0, 3533.63, 3583.41),
        OhlcDay("2015/12/18", 3574.94, 3578.96, 3568.16, 3614.7),
        OhlcDay("2015/12/21", 3568.58, 3642.47, 3565.75, 3651.06),
        OhlcDay("2015/12/22", 3645.99, 3651.77, 3616.87, 3652.63),
        OhlcDay("2015/12/23", 3653.28, 3636.09, 3633.03, 3684.57),
        OhlcDay("2015/12/24", 3631.31, 3612.49, 3572.28, 3640.22),
        OhlcDay("2015/12/25", 3614.05, 3627.91, 3601.74, 3635.26),
        OhlcDay("2015/12/28", 3635.77, 3533.78, 3533.78, 3641.59),
        OhlcDay("2015/12/29", 3528.4, 3563.74, 3515.52, 3564.17),
        OhlcDay("2015/12/30", 3566.73, 3572.88, 3538.11, 3573.68),
        OhlcDay("2015/12/31", 3570.47, 3539.18, 3538.35, 3580.6),
    )

/** The category labels of [days]. */
private fun dateLabels(days: Array<OhlcDay>): Array<OptionDataItem?> =
    strings(*Array(days.size) { index -> days[index].date })

/** The `[open, close, lowest, highest]` rows of [days]. */
private fun candleRows(days: Array<OhlcDay>): Array<CandlestickDataItem?> =
    dataItems(
        *Array(days.size) { index ->
            val day = days[index]
            CandlestickDataItem(row(day.open, day.close, day.lowest, day.highest))
        },
    )

/** Upstream `calculateMA`: the mean of the last [window] closes, `'-'` until the window is full. */
private fun movingAverage(days: Array<OhlcDay>, window: Int): Array<LineDataItem?> =
    Array(days.size) { index ->
        if (index < window) {
            LineDataItem(OptionDataValue("-"))
        } else {
            var sum = 0.0
            for (offset in 0 until window) {
                sum += days[index - offset].close
            }
            LineDataItem(OptionDataValue(sum / window))
        }
    }

/** The four moving-average series of [days]; [showSymbol] stays null on `candlestick-sh`. */
private fun movingAverageSeries(
    days: Array<OhlcDay>,
    showSymbol: Boolean?,
    style: LineStyleOption.() -> Unit,
): Array<SeriesOption> =
    Array(MA_WINDOWS.size) { index ->
        lineSeries {
            name = "MA${MA_WINDOWS[index]}"
            typedData = movingAverage(days, MA_WINDOWS[index])
            smooth = BooleanOrDouble(true)
            this.showSymbol = showSymbol
            lineStyle = unsafeJso(style)
        }
    }

/** The rounded value of a mark point, the upstream `Math.round(param.value)`. */
private fun roundedLabel(): LabelOption =
    unsafeJso {
        formatter = LabelFormatter({ params -> valueText(round(numeric(params.value))) })
    }

/** The `coord` of a mark point as its tooltip prints it, the upstream `param.data.coord || ''`. */
private fun coordOf(params: CallbackDataParams): String =
    params.data.unsafeCast<MarkPointDataItemOption>().coord?.joinToString(",") { textOf(it) }.orEmpty()

/** The four mark points of `candlestick-sh`: one explicit coordinate and three statistics. */
private fun shanghaiMarkPoint(): MarkPointOption =
    unsafeJso {
        label = roundedLabel()
        data =
            arrayOf(
                unsafeJso<MarkPointDataItemOption> {
                    name = "Mark"
                    coord =
                        arrayOf(
                            ScaleDataValueOrMarkerStatisticType(ScaleDataValue("2013/5/31")),
                            ScaleDataValueOrMarkerStatisticType(ScaleDataValue(2300.0)),
                        )
                    value = StringOrDouble(2300.0)
                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("rgb(41,60,85)") }
                },
                unsafeJso<MarkPointDataItemOption> {
                    name = "highest value"
                    type = MarkerStatisticTypes.max
                    valueDim = "highest"
                },
                unsafeJso<MarkPointDataItemOption> {
                    name = "lowest value"
                    type = MarkerStatisticTypes.min
                    valueDim = "lowest"
                },
                unsafeJso<MarkPointDataItemOption> {
                    name = "average value on close"
                    type = MarkerStatisticTypes.average
                    valueDim = "close"
                },
            )
        tooltip =
            unsafeJso {
                formatter =
                    TooltipFormatter({ params ->
                        val point = params.unsafeCast<CallbackDataParams>()
                        TooltipContent(point.name + "<br>" + coordOf(point))
                    })
            }
    }

/** One end of the lowest-to-highest line of `candlestick-sh`: a circle and no label. */
private fun lineEnd(name: String?, type: MarkerStatisticType, valueDim: String): MarkLine2DDataItemDimOption =
    unsafeJso {
        this.name = name
        this.type = type
        this.valueDim = valueDim
        symbol = SymbolShape("circle")
        symbolSize = SymbolSize(10.0)
        label = unsafeJso<MarkLineLabelOption> { show = false }
        emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<MarkLineLabelOption> { show = false } }
    }

/** The three mark lines of `candlestick-sh`: the low-to-high pair and the two closing lines. */
private fun shanghaiMarkLine(): MarkLineOption =
    unsafeJso {
        symbol = StringOrStringArray(arrayOf("none", "none"))
        data =
            arrayOf(
                MarkLineDataItem(
                    arrayOf(
                        lineEnd("from lowest to highest", MarkerStatisticTypes.min, "lowest"),
                        lineEnd(null, MarkerStatisticTypes.max, "highest"),
                    ),
                ),
                MarkLineDataItem(
                    unsafeJso<MarkLine1DDataItemOption> {
                        name = "min line on close"
                        type = MarkerStatisticTypes.min
                        valueDim = "close"
                    },
                ),
                MarkLineDataItem(
                    unsafeJso<MarkLine1DDataItemOption> {
                        name = "max line on close"
                        type = MarkerStatisticTypes.max
                        valueDim = "close"
                    },
                ),
            )
    }

/** The candlestick series of `candlestick-sh`, with its mark point and mark line. */
private fun shanghaiCandle(): CandlestickSeriesOption =
    candlestickSeries {
        name = "日K"
        typedData = candleRows(SHANGHAI_2013)
        typedItemStyle =
            unsafeJso<CandlestickItemStyleOption> {
                color = ZRColor(UP_COLOR)
                color0 = ZRColor(DOWN_COLOR)
                borderColor = ZRColor(UP_BORDER_COLOR)
                borderColor0 = ZRColor(DOWN_BORDER_COLOR)
            }
        markPoint = shanghaiMarkPoint()
        markLine = shanghaiMarkLine()
    }

val candlestickLongTailEntries: List<CatalogEntry> =
    listOf(
        candlestickLongTail.chart(
            "candlestick-sh",
            "上证指数",
            "ShangHai Index",
            "2013 年上半年的上证指数日 K，叠了四条均线；标记点写出四个价格维度的极值，" +
                "标记线把最低价与最高价连起来，另有两条收盘价的极值线。",
            "The Shanghai Composite in the first half of 2013 with four moving averages: the mark " +
                "points label the extremes of all four price dimensions, one mark line joins the " +
                "lowest low to the highest high, and two more trace the closing extremes.",
        ) {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "上证指数"
                            left = StringOrDouble(0.0)
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("日K", "MA5", "MA10", "MA20", "MA30")
                            top = StringOrDouble(20.0)
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble("10%")
                            bottom = StringOrDouble("15%")
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = dateLabels(SHANGHAI_2013)
                            boundaryGap = BooleanOrStringArray(false)
                            axisLine = unsafeJso<AxisLineOption> { onZero = BooleanOrString(false) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            min = AxisMinMax("dataMin")
                            max = AxisMinMax("dataMax")
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            scale = true
                            splitArea = unsafeJso<SplitAreaOption> { show = true }
                        },
                    )
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            type = DataZoomTypes.inside
                            start = 50.0
                            end = 100.0
                        },
                        unsafeJso<DataZoomOption> {
                            show = true
                            type = DataZoomTypes.slider
                            top = StringOrDouble("90%")
                            start = 50.0
                            end = 100.0
                        },
                    )
                series =
                    arrayOf<SeriesOption>(shanghaiCandle()) +
                        movingAverageSeries(SHANGHAI_2013, showSymbol = null) { opacity = 0.5 }
            }
        },
        candlestickLongTail.chart(
            "candlestick-sh-2015",
            "2015 年上证指数",
            "ShangHai Index, 2015",
            "2015 年全年的上证指数日 K：上游表倒序存放，并留下两行空记录，所以图上开头是两根零值蜡烛。" +
                "滑块用自定义把手图标与数据阴影，缩放到 50% 可以看到 6 月的高点。",
            "The Shanghai Composite for the whole of 2015. The upstream table is stored newest first " +
                "and keeps two empty rows, so the chart opens with two zero candles; the slider uses " +
                "a custom handle icon and a data shadow.",
        ) {
            unsafeJso<EChartsOption> {
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            data = legendNames("日K", "MA5", "MA10", "MA20", "MA30")
                            inactiveColor = "#777"
                            top = StringOrDouble(20.0)
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            trigger = TooltipTriggers.axis
                            axisPointer =
                                unsafeJso<AxisPointerOption> {
                                    animation = BooleanOrString(false)
                                    type = AxisPointerTypes.cross
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor(CROSS_2015_COLOR)
                                            width = 2.0
                                            opacity = 1.0
                                        }
                                }
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = dateLabels(SHANGHAI_2015)
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(AXIS_2015_COLOR) }
                                }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            scale = true
                            axisLine =
                                unsafeJso<AxisLineOption> {
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor(AXIS_2015_COLOR) }
                                }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                grid = one(unsafeJso<GridOption> { bottom = StringOrDouble(80.0) })
                dataZoom =
                    arrayOf(
                        unsafeJso<DataZoomOption> {
                            textStyle = unsafeJso<TextStyleOption> { color = AXIS_2015_COLOR }
                            handleIcon = HANDLE_2015_ICON
                            dataBackground =
                                unsafeJso<DataZoomDataBackgroundOption> {
                                    areaStyle = unsafeJso<AreaStyleOption> { color = ZRColor(AXIS_2015_COLOR) }
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            opacity = 0.8
                                            color = ZRColor(AXIS_2015_COLOR)
                                        }
                                }
                            brushSelect = true
                        },
                        unsafeJso<DataZoomOption> { type = DataZoomTypes.inside },
                    )
                series =
                    arrayOf<SeriesOption>(
                        candlestickSeries {
                            name = "Day"
                            typedData = candleRows(SHANGHAI_2015)
                            typedItemStyle =
                                unsafeJso<CandlestickItemStyleOption> {
                                    color = ZRColor("#FD1050")
                                    color0 = ZRColor("#0CF49B")
                                    borderColor = ZRColor("#FD1050")
                                    borderColor0 = ZRColor("#0CF49B")
                                }
                        },
                    ) + movingAverageSeries(SHANGHAI_2015, showSymbol = false) { width = 1.0 }
            }
        },
    )
