package demo.examples.candlestick

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.JsAny
import demo.examples.Category

private val candlestickTouchCategory = Category(Groups.CANDLESTICK, "examples/candlestick/CandlestickTouch.kt")

val candlestickTouchEntry: CatalogEntry =
    candlestickTouchCategory.chart(
            "candlestick-touch",
            "触屏上的坐标轴指示器",
            "Axis Pointer Link and Touch",
            summaryZh = "上下两个联动网格画日 K 与成交量；tooltip 由拖动的坐标轴指示器触发，横轴指示器联动两个网格。",
            summaryEn = "Daily candles over a volume grid; the tooltip follows the dragged axis pointer, and the x pointers of both grids stay linked.",
        ) {
            candlestickTouchOption()
        }
