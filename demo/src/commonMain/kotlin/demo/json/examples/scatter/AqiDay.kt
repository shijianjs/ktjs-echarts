package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*

/** One day of the AQI table: `[day, AQI, PM2.5, PM10, CO, NO2, SO2]` plus its grade. */
internal class AqiDay(val values: DoubleArray, val grade: String)
