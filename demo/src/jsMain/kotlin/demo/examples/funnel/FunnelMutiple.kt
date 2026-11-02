package demo.examples.funnel

import demo.CatalogEntry
import demo.Groups
import echarts.Orients
import echarts.options.EChartsOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.TitleOption
import echarts.options.one
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.legendNames
import demo.examples.shared.toolbox

private val funnelMutipleCategory = Category(Groups.FUNNEL, "examples/funnel/FunnelMutiple.kt")

val funnelMutipleEntry: CatalogEntry =
    funnelMutipleCategory.chart("funnel-mutiple", "多漏斗图", "Multiple Funnels") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "Funnel"
                            left = StringOrDouble("left")
                            top = StringOrDouble("bottom")
                        },
                    )
                tooltip = tooltipPercent
                toolbox =
                    toolbox(
                        orient = Orients.vertical,
                        top = StringOrDouble("center"),
                        dataView = true,
                        restore = true,
                        saveAsImage = true,
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            orient = Orients.vertical
                            left = StringOrDouble("left")
                            data = legendNames(*labels.toTypedArray())
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        quadrantFunnel("Funnel", "5%", "50%", labelled = false),
                        quadrantFunnel("Pyramid", "5%", "5%", labelled = false),
                        quadrantFunnel("Funnel", "55%", "5%", labelled = true),
                        quadrantFunnel("Pyramid", "55%", "50%", labelled = true),
                    )
            }
        }
