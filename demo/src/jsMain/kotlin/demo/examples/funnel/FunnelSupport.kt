package demo.examples.funnel

import io.github.shijianjs.ktjs.echarts.FunnelSorts
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.FunnelDataItem
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.TooltipFormatter
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.dataItems
import io.github.shijianjs.ktjs.echarts.options.funnelSeries
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import demo.examples.shared.funnelItem
import demo.examples.shared.toolbox

internal val labels = listOf("Show", "Click", "Visit", "Inquiry", "Order")

internal val tooltipPercent =
    one(
        unsafeJso<TooltipOption> {
            trigger = TooltipTriggers.item
            formatter = TooltipFormatter("{a} <br/>{b} : {c}%")
        },
    )

internal val funnelToolbox = toolbox(dataView = true, restore = true, saveAsImage = true)

/** The five stages, in the order the originals declare them. */
internal fun stages(vararg values: Double): Array<FunnelDataItem?> =
    dataItems(*Array(values.size) { index -> funnelItem(labels[index], values[index]) })

/** One quadrant of `funnel-align`. */
internal fun compareFunnel(
    name: String,
    left: String,
    top: String,
    align: io.github.shijianjs.ktjs.echarts.FunnelAlign,
    ascending: Boolean,
): SeriesOption =
    funnelSeries {
        this.name = name
        width = StringOrDouble("40%")
        height = StringOrDouble("45%")
        this.left = StringOrDouble(left)
        this.top = StringOrDouble(top)
        funnelAlign = align
        if (ascending) sort = FunnelSorts.ascending
        typedData =
            dataItems(
                funnelItem("Prod C", 60.0),
                funnelItem("Prod D", 30.0),
                funnelItem("Prod E", 10.0),
                funnelItem("Prod B", 80.0),
                funnelItem("Prod A", 100.0),
            )
    }

/** One quadrant of `funnel-mutiple`; the right-hand ones put the label outside. */
internal fun quadrantFunnel(name: String, left: String, top: String, labelled: Boolean): SeriesOption =
    funnelSeries {
        this.name = name
        width = StringOrDouble("40%")
        height = StringOrDouble("45%")
        this.left = StringOrDouble(left)
        this.top = StringOrDouble(top)
        if (name == "Pyramid") sort = FunnelSorts.ascending
        if (labelled) label = unsafeJso<LabelOption> { position = LabelPosition("left") }
        typedData = stages(60.0, 30.0, 10.0, 80.0, 100.0)
    }
