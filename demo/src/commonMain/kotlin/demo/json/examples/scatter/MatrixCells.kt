package demo.json.examples.scatter

import demo.json.*
import io.github.shijianjs.ktjs.echarts.json.*

/** The generated grids, axes and series of the lower triangle of the matrix. */
internal class MatrixCells(
    val grids: List<GridOption>,
    val xAxes: List<AxisOption>,
    val yAxes: List<AxisOption>,
    val series: List<SeriesOption>,
)
