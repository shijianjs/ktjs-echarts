package demo.examples.sunburst

import demo.CatalogEntry
import demo.Groups
import echarts.VisualMapTypes
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.one
import echarts.options.sunburstSeries
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.Category

private val sunburstVisualMapCategory = Category(Groups.SUNBURST, "examples/sunburst/SunburstVisualMap.kt")

val sunburstVisualMapEntry: CatalogEntry =
    sunburstVisualMapCategory.chart("sunburst-visualMap", "旭日图使用视觉编码", "Sunburst VisualMap") {
            unsafeJso<EChartsOption> {
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.continuous
                            min = 0.0
                            max = 10.0
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color =
                                        StringOrStringArray(
                                            arrayOf("#2F93C8", "#AEC48F", "#FFDB5C", "#F98862"),
                                        )
                                }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = visualMapData()
                            radius = radius(StringOrDouble(0.0), StringOrDouble("90%"))
                            typedLabel = radialLabel()
                        },
                    )
            }
        }
