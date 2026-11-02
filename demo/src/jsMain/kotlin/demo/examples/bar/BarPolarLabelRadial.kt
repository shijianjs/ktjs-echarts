package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.sunburst.radius

private val barPolarLabelRadialCategory = Category(Groups.BAR, "examples/bar/BarPolarLabelRadial.kt")

val barPolarLabelRadialEntry: CatalogEntry =
    barPolarLabelRadialCategory.chart(
            "bar-polar-label-radial",
            "极坐标柱状图标签（角度轴为类别轴）",
            "Radial Polar Bar Label Position",
        ) {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Radial Polar Bar Label Position (middle)" })
                polar =
                    one(
                        unsafeJso<PolarOption> {
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(30.0), StringOrDouble("80%")))
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> { max = AxisMinMax(4.0) })
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            data = polarLabelCategories()
                            startAngle = 75.0
                        },
                    )
                tooltip = one(unsafeJso<TooltipOption> {})
                series =
                    one(
                        barSeries {
                            coordinateSystem = "polar"
                            label = polarBarLabel()
                            typedData = polarLabelValues()
                        },
                    )
                animation = false
            }
        }
