package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.AxisPointerTypes
import echarts.AxisTypes
import echarts.Orients
import echarts.TooltipTriggers
import echarts.options.AxisPointerOption
import echarts.options.EChartsOption
import echarts.options.ScatterDataItem
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import echarts.options.SymbolSize
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.VisualMapOption
import echarts.options.VisualOptionUnit
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.one
import echarts.options.scatterSeries
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * `scatter-stream-visual` of the official example gallery.
 *
 * The page draws sixteen thousand `[area, price]` points with a continuous visual map: the whole
 * table arrives as one JSON array and the scatter series colours every point from the second
 * dimension.
 */

private val scatterStreamVisual = Category(Groups.SCATTER, "examples/ScatterStreamVisual.kt")

/** One `[area, price]` row of the `house-price-area2.json` table. */
private fun priceItem(pair: Array<Double>): ScatterDataItem? =
    ScatterDataItem(doubleRow(pair[0], pair[1]))

/** The `[area, price]` rows as scatter items. */
private fun priceItems(rows: Array<Array<Double>>): Array<ScatterDataItem?> =
    Array(rows.size) { index -> priceItem(rows[index]) }

/** The option of the page; [items] is the whole table. */
private fun streamOption(items: Array<ScatterDataItem?>): EChartsOption =
    unsafeJso {
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "Dispersion of house price based on the area"
                    left = StringOrDouble("center")
                    top = StringOrDouble(0.0)
                },
            )
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    min = 15202.0
                    max = 159980.0
                    dimension = 1.0
                    orient = Orients.vertical
                    right = StringOrDouble(10.0)
                    top = StringOrDouble("center")
                    text = arrayOf("HIGH", "LOW")
                    calculable = true
                    inRange =
                        unsafeJso<VisualOptionUnit> {
                            color = StringOrStringArray(arrayOf("#f2c31a", "#24b7f2"))
                        }
                },
            )
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    trigger = TooltipTriggers.item
                    axisPointer = unsafeJso<AxisPointerOption> { type = AxisPointerTypes.cross }
                },
            )
        xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value })
        yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
        series =
            one<SeriesOption>(
                scatterSeries {
                    name = "price-area"
                    symbolSize = SymbolSize(5.0)
                    typedData = items
                },
            )
    }

val scatterStreamVisualEntries: List<CatalogEntry> =
    listOf(
        scatterStreamVisual.interactive(
            "scatter-stream-visual",
            "流式渲染与视觉映射",
            "Visual interaction with stream",
            "一万六千套房的面积与总价铺成散点，连续的 visualMap 按总价把点从金色渐变到蓝色。",
            "Sixteen thousand houses plotted by area against price, with a continuous visual map " +
                "shading every point from gold to blue by its price.",
        ) { chart ->
            loadJson("data/house-price-area2.json").then { json ->
                chart.setOption(streamOption(priceItems(json.unsafeCast<Array<Array<Double>>>())))
            }
        },
    )
