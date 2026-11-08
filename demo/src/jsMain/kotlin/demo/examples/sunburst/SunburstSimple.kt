package demo.examples.sunburst

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.sunburstSeries
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val sunburstSimpleCategory = Category(Groups.SUNBURST, "examples/sunburst/SunburstSimple.kt")

val sunburstSimpleEntry: CatalogEntry =
    sunburstSimpleCategory.chart("sunburst-simple", "基础旭日图", "Basic Sunburst") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = familyData()
                            radius = radius(StringOrDouble(0.0), StringOrDouble("90%"))
                            typedLabel = radialLabel()
                        },
                    )
            }
        }
