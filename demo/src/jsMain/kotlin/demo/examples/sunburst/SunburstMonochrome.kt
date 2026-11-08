package demo.examples.sunburst

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.sunburstSeries
import js.objects.unsafeJso
import kotlin.js.js
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val sunburstMonochromeCategory = Category(Groups.SUNBURST, "examples/sunburst/SunburstMonochrome.kt")

val sunburstMonochromeEntry: CatalogEntry =
    sunburstMonochromeCategory.chart("sunburst-monochrome", "单色旭日图", "Monochrome Sunburst") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = monochromeData()
                            radius = radius(StringOrDouble("15%"), StringOrDouble("80%"))
                            emphasis = ancestorEmphasis()
                            typedLabel = radialLabel()
                            // `levels: []` upstream: the levels are derived from the data only.
                            levels = emptyArray()
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#ddd"); borderWidth = 2.0 }
                        },
                    )
            }
        }
