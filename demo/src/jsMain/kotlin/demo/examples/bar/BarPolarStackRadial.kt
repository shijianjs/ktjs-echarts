package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.legendNames

private val barPolarStackRadialCategory = Category(Groups.BAR, "examples/bar/BarPolarStackRadial.kt")

val barPolarStackRadialEntry: CatalogEntry =
    barPolarStackRadialCategory.chart(
            "bar-polar-stack-radial",
            "极坐标系下的堆叠柱状图（径向）",
            "Stacked Bar Chart on Polar (Radial)",
        ) {
            unsafeJso<EChartsOption> {
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> {})
                polar = one(unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        polarStackSeries("A", numbers(1.0, 2.0, 3.0, 4.0, 3.0, 5.0, 1.0)),
                        polarStackSeries("B", numbers(2.0, 4.0, 6.0, 1.0, 3.0, 2.0, 1.0)),
                        polarStackSeries("C", numbers(1.0, 2.0, 3.0, 4.0, 1.0, 2.0, 5.0)),
                    )
                legend = one(unsafeJso<LegendOption> { show = true; data = legendNames("A", "B", "C") })
            }
        }
