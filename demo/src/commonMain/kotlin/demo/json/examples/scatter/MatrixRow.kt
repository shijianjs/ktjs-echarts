package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*
import demo.json.examples.dataset.row

/** One row of the matrix: its city, its six numbers and its grade. */
internal class MatrixRow(val city: String, val values: DoubleArray, val grade: String)
