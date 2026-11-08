package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category

private val polarEndAngleCategory = Category(Groups.BAR, "examples/bar/PolarEndAngle.kt")

val polarEndAngleEntry: CatalogEntry =
    polarEndAngleCategory.chart("polar-endAngle", "极坐标系 endAngle", "Polar endAngle") {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                angleAxis =
                    arrayOf(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            polarIndex = 0.0
                            startAngle = 90.0
                            endAngle = 0.0
                            data = strings("S1", "S2", "S3")
                        },
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            polarIndex = 1.0
                            startAngle = -90.0
                            endAngle = -180.0
                            data = strings("T1", "T2", "T3")
                        },
                    )
                radiusAxis =
                    arrayOf(
                        unsafeJso<RadiusAxisOption> { polarIndex = 0.0 },
                        unsafeJso<RadiusAxisOption> { polarIndex = 1.0 },
                    )
                polar = arrayOf(unsafeJso<PolarOption> {}, unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        barSeries {
                            polarIndex = 0.0
                            coordinateSystem = "polar"
                            typedData = numbers(1.0, 2.0, 3.0)
                        },
                        barSeries {
                            polarIndex = 1.0
                            coordinateSystem = "polar"
                            typedData = numbers(1.0, 2.0, 3.0)
                        },
                    )
            }
        }
