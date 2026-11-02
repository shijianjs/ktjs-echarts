package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.legendNames

private val barPolarStackCategory = Category(Groups.BAR, "examples/bar/BarPolarStack.kt")

val barPolarStackEntry: CatalogEntry =
    barPolarStackCategory.chart("bar-polar-stack", "极坐标系下的堆叠柱状图", "Stacked Bar Chart on Polar") {
            unsafeJso<EChartsOption> {
                angleAxis = one(unsafeJso<AngleAxisOption> {})
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            type = AxisTypes.category
                            data = strings("Mon", "Tue", "Wed", "Thu")
                            z = 10.0
                        },
                    )
                polar = one(unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        polarStackSeries("A", numbers(1.0, 2.0, 3.0, 4.0)),
                        polarStackSeries("B", numbers(2.0, 4.0, 6.0, 8.0)),
                        polarStackSeries("C", numbers(1.0, 2.0, 3.0, 4.0)),
                    )
                legend = one(unsafeJso<LegendOption> { show = true; data = legendNames("A", "B", "C") })
            }
        }
