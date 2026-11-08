package demo.examples.sunburst

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrDoubleOrArray
import io.github.shijianjs.ktjs.echarts.options.sunburstSeries
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val sunburstBorderRadiusCategory = Category(Groups.SUNBURST, "examples/sunburst/SunburstBorderRadius.kt")

val sunburstBorderRadiusEntry: CatalogEntry =
    sunburstBorderRadiusCategory.chart("sunburst-borderRadius", "圆角旭日图", "Sunburst with Rounded Corner") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = familyData()
                            radius = radius(StringOrDouble(60.0), StringOrDouble("90%"))
                            itemStyle =
                                unsafeJso<ItemStyleOption> {
                                    borderRadius = StringOrDoubleOrArray(7.0)
                                    borderWidth = 2.0
                                }
                            typedLabel = unsafeJso { show = false }
                        },
                    )
            }
        }
