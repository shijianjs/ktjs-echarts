package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.math.ceil
import kotlin.math.sqrt
import demo.examples.Category
import demo.examples.gauge.center

private val lineEasingCategory = Category(Groups.LINE, "examples/line/LineEasing.kt")

val lineEasingEntry: CatalogEntry =
    lineEasingCategory.chart("line-easing", "缓动函数可视化", "Line Easing Visualizing", height = "44rem") {
            val columns = ceil(sqrt(EASINGS.size.toDouble())).toInt()
            val rows = ceil(EASINGS.size.toDouble() / columns).toInt()
            val cells = EASINGS.mapIndexed { index, easing -> easingCell(easing, index, columns, rows) }
            unsafeJso<EChartsOption> {
                title =
                    cells.map { it.title }.toTypedArray() +
                        unsafeJso<TitleOption> {
                            text = "Different Easing Functions"
                            top = StringOrDouble("bottom")
                            left = StringOrDouble("center")
                        }
                grid = cells.map { it.grid }.toTypedArray()
                xAxis = cells.map { it.xAxis }.toTypedArray()
                yAxis = cells.map { it.yAxis }.toTypedArray()
                series = cells.map { it.series }.toTypedArray()
            }
        }
