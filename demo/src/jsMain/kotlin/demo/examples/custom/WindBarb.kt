package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.options.one
import kotlin.js.unsafeCast
import demo.examples.Category

private val windBarbCategory = Category(Groups.CUSTOM, "examples/custom/WindBarb.kt")

val windBarbEntry: CatalogEntry =
    windBarbCategory.interactive(
            "wind-barb",
            "风向图",
            "Wind Barb",
            "同一根时间轴上叠四层：浪高是蓝色渐变面积，风速是虚线加每小时的转向箭头，" +
                "顶部是未来一周的天气图标与温度区间。",
            "Four layers on one time axis: wave height as a blue gradient area, wind speed as a " +
                "dotted line with one rotated arrow per hour, and the coming week's weather icons " +
                "with their temperature range on top.",
        ) { chart ->
            loadJson("data/wind-barb-hobart.json").then { json ->
                val table = json.unsafeCast<WindBarbJson>()
                chart.setOption(windBarbOption(readingRows(table), forecastRows(table)))
            }
        }
