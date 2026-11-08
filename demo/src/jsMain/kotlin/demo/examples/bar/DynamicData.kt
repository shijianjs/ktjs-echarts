package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.JsAny
import io.github.shijianjs.ktjs.zrender.ZRColor
import kotlin.math.round
import demo.examples.Category
import demo.examples.shared.DYNAMIC_WINDOW
import demo.examples.shared.Lcg
import demo.examples.shared.clockLabel
import demo.examples.shared.round1
import demo.examples.shared.toolbox

private val dynamicDataCategory = Category(Groups.BAR, "examples/bar/DynamicData.kt")

val dynamicDataEntry: CatalogEntry =
    dynamicDataCategory.interactive("dynamic-data", "动态数据", "Dynamic Data") { chart, holder ->
            val random = Lcg(20261028)
            val now = Date()
            val categories =
                Array(DYNAMIC_WINDOW) { index ->
                    clockLabel(Date(now.getTime() - (DYNAMIC_WINDOW - 1 - index) * 2000.0))
                }
            val categories2 = Array(DYNAMIC_WINDOW) { index -> (index + 1).toString() }
            val prices = DoubleArray(DYNAMIC_WINDOW) { round(random.next() * 1000) }
            val orders = DoubleArray(DYNAMIC_WINDOW) { round1(random.next() * 10 + 5) }
            var counter = 11

            chart.setOption(
                unsafeJso<EChartsOption> {
                    title = one(unsafeJso<TitleOption> { text = "Dynamic Data" })
                    tooltip =
                        one(
                            unsafeJso<TooltipOption> {
                                trigger = TooltipTriggers.axis
                                axisPointer =
                                    unsafeJso<AxisPointerOption> {
                                        type = AxisPointerTypes.cross
                                        label =
                                            unsafeJso<AxisPointerLabelOption> {
                                                backgroundColor = ZRColor("#283b56")
                                            }
                                    }
                            },
                        )
                    legend = one(unsafeJso<LegendOption> {})
                    toolbox =
                        one(
                            unsafeJso<ToolboxOption> {
                                show = true
                                feature =
                                    unsafeJso<ToolboxFeature> {
                                        dataView = unsafeJso<DataViewOption> { readOnly = false }
                                        restore = unsafeJso<ToolboxFeatureOption> {}
                                        saveAsImage = unsafeJso<SaveAsImageOption> {}
                                    }
                            },
                        )
                    dataZoom = one(unsafeJso<DataZoomOption> { show = false; start = 0.0; end = 100.0 })
                    xAxis =
                        arrayOf(
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.category
                                boundaryGap = BooleanOrStringArray(true)
                                data = strings(*categories)
                            },
                            unsafeJso<XAxisOption> {
                                type = AxisTypes.category
                                boundaryGap = BooleanOrStringArray(true)
                                data = strings(*categories2)
                            },
                        )
                    yAxis =
                        arrayOf(
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                scale = true
                                name = "Price"
                                max = AxisMinMax(30.0)
                                min = AxisMinMax(0.0)
                                boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.2), StringOrDouble(0.2)))
                            },
                            unsafeJso<YAxisOption> {
                                type = AxisTypes.value
                                scale = true
                                name = "Order"
                                max = AxisMinMax(1200.0)
                                min = AxisMinMax(0.0)
                                boundaryGap = BooleanOrStringArray(arrayOf(StringOrDouble(0.2), StringOrDouble(0.2)))
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            barSeries {
                                name = "Dynamic Bar"
                                xAxisIndex = 1.0
                                yAxisIndex = 1.0
                                typedData = numbers(*prices)
                            },
                            lineSeries {
                                name = "Dynamic Line"
                                typedData = numbers(*orders)
                            },
                        )
                },
            )
            holder.setInterval({
                if (!chart.isDisposed()) {
                    categories.copyInto(categories, 0, 1)
                    categories[DYNAMIC_WINDOW - 1] = clockLabel(Date())
                    categories2.copyInto(categories2, 0, 1)
                    categories2[DYNAMIC_WINDOW - 1] = (counter++).toString()
                    prices.copyInto(prices, 0, 1)
                    prices[DYNAMIC_WINDOW - 1] = round(random.next() * 1000)
                    orders.copyInto(orders, 0, 1)
                    orders[DYNAMIC_WINDOW - 1] = round1(random.next() * 10 + 5)
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            xAxis =
                                arrayOf(
                                    unsafeJso<XAxisOption> { data = strings(*categories) },
                                    unsafeJso<XAxisOption> { data = strings(*categories2) },
                                )
                            series =
                                arrayOf<SeriesOption>(
                                    barSeries { typedData = numbers(*prices) },
                                    lineSeries { typedData = numbers(*orders) },
                                )
                        },
                    )
                }
            }, 2100)
        }
