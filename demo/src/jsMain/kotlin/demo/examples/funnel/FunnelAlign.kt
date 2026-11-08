package demo.examples.funnel

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.FunnelAligns
import io.github.shijianjs.ktjs.echarts.Orients
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.LegendOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val funnelAlignCategory = Category(Groups.FUNNEL, "examples/funnel/FunnelAlign.kt")

val funnelAlignEntry: CatalogEntry =
    funnelAlignCategory.chart("funnel-align", "漏斗图(对比)", "Funnel Compare") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Funnel Compare"
                            subtext = "Fake Data"
                            left = StringOrDouble("left")
                            top = StringOrDouble("bottom")
                        },
                    )
                tooltip = tooltipPercent
                toolbox = toolbox(orient = Orients.vertical, top = StringOrDouble("center"), dataView = true, restore = true, saveAsImage = true)
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            orient = Orients.vertical
                            left = StringOrDouble("left")
                            data = legendNames("Prod A", "Prod B", "Prod C", "Prod D", "Prod E")
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        compareFunnel("Funnel", "5%", "50%", FunnelAligns.right, ascending = false),
                        compareFunnel("Pyramid", "5%", "5%", FunnelAligns.right, ascending = true),
                        compareFunnel("Funnel", "55%", "5%", FunnelAligns.left, ascending = false),
                        compareFunnel("Pyramid", "55%", "50%", FunnelAligns.left, ascending = true),
                    )
            }
        }
