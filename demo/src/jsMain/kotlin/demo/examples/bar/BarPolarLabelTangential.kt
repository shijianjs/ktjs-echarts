package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.sunburst.radius

private val barPolarLabelTangentialCategory = Category(Groups.BAR, "examples/bar/BarPolarLabelTangential.kt")

val barPolarLabelTangentialEntry: CatalogEntry =
    barPolarLabelTangentialCategory.chart(
            "bar-polar-label-tangential",
            "极坐标柱状图标签（半径轴为类别轴）",
            "Tangential Polar Bar Label Position",
        ) {
            unsafeJso<EChartsOption> {
                title = one(unsafeJso<TitleOption> { text = "Tangential Polar Bar Label Position (middle)" })
                polar =
                    one(
                        unsafeJso<PolarOption> {
                            radius = StringOrDoubleOrArray(arrayOf(StringOrDouble(30.0), StringOrDouble("80%")))
                        },
                    )
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            max = AxisMinMax(4.0)
                            startAngle = 75.0
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            type = AxisTypes.category
                            data = polarLabelCategories()
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
            }
        }
