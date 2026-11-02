package demo.examples.graphic

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import demo.examples.Category
import demo.examples.matrix.names
import demo.examples.shared.textOf

private val barDrilldownCategory = Category(Groups.GRAPHIC, "examples/graphic/BarDrilldown.kt")

val barDrilldownEntry: CatalogEntry =
    barDrilldownCategory.interactive(
            "bar-drilldown",
            "柱状图下钻动画",
            "Bar Chart Drilldown Animation",
            "点击柱体把同一组的细分数据换进来，`universalTransition` 让柱子在两级之间变形；" +
                "下钻后左上角出现一个可点的 Back。",
            "Clicking a bar swaps in the breakdown of its group; `universalTransition` morphs the " +
                "bars between the two levels and a clickable Back caption appears on top.",
        ) { chart ->
            chart.setOption(drilldownTopOption())
            chart.on(EChartsEvents.Click) { params ->
                val groupId = textOf(js("(event) => (event.data && event.data.groupId) || ''")(params) as JsAny)
                val level = DRILLDOWN_LEVELS.firstOrNull { it.groupId == groupId }
                if (level != null) {
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            xAxis = one(unsafeJso<XAxisOption> { data = strings(*level.names) })
                            series = arrayOf<SeriesOption>(drilldownSeries(level.groupId, level.values))
                            graphic = arrayOf(backElement { chart.setOption(drilldownTopOption()) })
                        },
                    )
                }
                null
            }
        }
