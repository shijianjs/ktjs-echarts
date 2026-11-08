package demo.examples.themeriver

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.AxisMinMax
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.SingleAxisOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.themeRiverSeries
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
