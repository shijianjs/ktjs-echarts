package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.lineSeries
import echarts.options.one
import echarts.options.points
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.lines.coordinate

private val lineInCartesianCoordinateSystemCategory = Category(Groups.LINE, "examples/line/LineInCartesianCoordinateSystem.kt")

val lineInCartesianCoordinateSystemEntry: CatalogEntry =
    lineInCartesianCoordinateSystemCategory.chart("line-in-cartesian-coordinate-system", "双数值轴折线图", "Line Chart in Cartesian Coordinate System") {
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> {})
                yAxis = one(unsafeJso<YAxisOption> {})
                series =
                    arrayOf<SeriesOption>(
                        lineSeries { typedData = points(doubleArrayOf(10.0, 40.0), doubleArrayOf(50.0, 100.0), doubleArrayOf(40.0, 20.0)) },
                    )
            }
        }
