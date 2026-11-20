package demo.examples.matrix

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.examples.shared.row

/** One row of the order book: a price, its amount, and whether it is a bid. */
internal class OrderRow(val price: Double, val amount: Double, val isBid: Boolean)
