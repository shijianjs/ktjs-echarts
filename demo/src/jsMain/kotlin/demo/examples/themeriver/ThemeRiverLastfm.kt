package demo.examples.themeriver

import demo.CatalogEntry
import demo.Groups
import echarts.options.AxisMinMax
import echarts.options.EChartsOption
import echarts.options.LabelOption
import echarts.options.SeriesOption
import echarts.options.SingleAxisOption
import echarts.options.one
import echarts.options.themeRiverSeries
import js.objects.unsafeJso
import demo.examples.Category

private val themeRiverLastfmCategory = Category(Groups.THEME_RIVER, "examples/themeriver/ThemeRiverLastfm.kt")

val themeRiverLastfmEntry: CatalogEntry =
    themeRiverLastfmCategory.chart("themeRiver-lastfm", "Lastfm 主题河流图", "ThemeRiver Lastfm") {
            unsafeJso<EChartsOption> {
                singleAxis = one(unsafeJso<SingleAxisOption> { max = AxisMinMax("dataMax") })
                series =
                    arrayOf<SeriesOption>(
                        themeRiverSeries {
                            typedData = lastFmData()
                            label = unsafeJso<LabelOption> { show = false }
                        },
                    )
            }
        }
