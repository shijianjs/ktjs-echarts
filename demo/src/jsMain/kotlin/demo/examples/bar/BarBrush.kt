package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.shared.Lcg

private val barBrushCategory = Category(Groups.BAR, "examples/bar/BarBrush.kt")

val barBrushEntry: CatalogEntry =
    barBrushCategory.interactive(
            "bar-brush",
            "柱状图框选",
            "Brush Select on Column Chart",
            summaryZh = "工具栏里切到矩形、多边形或轴向框选后，右下角的标题会列出被选中的原始数据下标。",
            summaryEn = "Brush with the toolbar; the title in the lower right lists the selected raw data indices.",
        ) { chart ->
            val random = Lcg(20120418)
            val classes = Array(10) { "Class$it" }
            val first = DoubleArray(10) { roundToCents(random.next() * 2) }
            val second = DoubleArray(10) { roundToCents(random.next() * 5) }
            val third = DoubleArray(10) { roundToCents(random.next() + 0.3) }
            val fourth = DoubleArray(10) { roundToCents(random.next()) }
            chart.setOption(barBrushOption(classes, first, second, third, fourth))
            chart.on(EChartsEvents.BrushSelected) { event ->
                chart.setOption(brushSelectionTitle(event.unsafeCast<BrushSelectedEvent>()))
            }
        }
