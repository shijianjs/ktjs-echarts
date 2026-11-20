package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*

/** `centered text that sits above one cell of the `line-easing` mesh. */
internal class EasingCell(
    /** Left edge of the cell, in percent. */
    val leftPercent: Double,
    /** Top edge of the cell, in percent. */
    val topPercent: Double,
    /** Width and height of the cell, in percent. */
    val sizePercent: Double,
)
