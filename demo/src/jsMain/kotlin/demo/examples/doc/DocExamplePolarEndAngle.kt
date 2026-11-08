package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category

private val docExamplePolarEndAngleCategory = Category(Groups.DOC, "examples/doc/DocExamplePolarEndAngle.kt")

val docExamplePolarEndAngleEntry: CatalogEntry =
    docExamplePolarEndAngleCategory.chart("doc-example/polar-end-angle", "极坐标结束角度", "Polar end angle") {
            unsafeJso<EChartsOption> {
                tooltip = one(unsafeJso<TooltipOption> {})
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            endAngle = -180.0
                            data = strings("S1", "S2", "S3")
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> {})
                polar = one(unsafeJso<PolarOption> {})
                series =
                    one(
                        barSeries {
                            coordinateSystem = "polar"
                            polarIndex = 0.0
                            typedData = numbers(1.0, 2.0, 3.0)
                        },
                    )
            }
        }
