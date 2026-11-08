package demo.examples.scatter

import io.github.shijianjs.ktjs.echarts.AxisPointerTypes
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.Orients
import io.github.shijianjs.ktjs.echarts.TooltipTriggers
import io.github.shijianjs.ktjs.echarts.options.AxisPointerOption
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ScatterDataItem
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.SymbolSize
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.VisualMapOption
import io.github.shijianjs.ktjs.echarts.options.VisualOptionUnit
import io.github.shijianjs.ktjs.echarts.options.XAxisOption
import io.github.shijianjs.ktjs.echarts.options.YAxisOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.scatterSeries
import js.objects.unsafeJso
import demo.examples.gauge.center
import demo.examples.matrix.dimension
import demo.examples.shared.doubleRow
import demo.examples.shared.row

/** One `[area, price]` row of the `house-price-area2.json` table. */
internal fun priceItem(pair: Array<Double>): ScatterDataItem? =
    ScatterDataItem(doubleRow(pair[0], pair[1]))

/** The `[area, price]` rows as scatter items. */
internal fun priceItems(rows: Array<Array<Double>>): Array<ScatterDataItem?> =
    Array(rows.size) { index -> priceItem(rows[index]) }

/** The option of the page; [items] is the whole table. */
internal fun streamOption(items: Array<ScatterDataItem?>): EChartsOption =
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
