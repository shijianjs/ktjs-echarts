package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category

internal class DocExamplePolarStartAngle {
    companion object {
        private val CATEGORY = Category(Groups.DOC, "examples/doc/DocExamplePolarStartAngle.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart("doc-example/polar-start-angle", "极坐标起始角度", "Polar start angle") {
                    unsafeJso<EChartsOption> {
                        polar = one(unsafeJso<PolarOption> {})
                        angleAxis =
                            one(
                                unsafeJso<AngleAxisOption> {
                                    min = AxisMinMax(0.0)
                                    max = AxisMinMax(360.0)
                                    interval = AxisInterval(30.0)
                                    startAngle = 45.0
                                },
                            )
                        radiusAxis =
                            one(
                                unsafeJso<RadiusAxisOption> {
                                    min = AxisMinMax(0.0)
                                    max = AxisMinMax(10.0)
                                    interval = AxisInterval(2.0)
                                },
                            )
                        series = emptyArray()
                    }
                }
    }
}
