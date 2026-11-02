package demo.examples.line

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.math.floor
import zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.ConfidenceBandItem
import demo.examples.shared.axisLabel
import demo.examples.shared.numberAt
import demo.examples.shared.percent
import demo.examples.shared.textOf

private val confidenceBandCategory = Category(Groups.LINE, "examples/line/ConfidenceBand.kt")

val confidenceBandEntry: CatalogEntry =
    confidenceBandCategory.interactive("confidence-band", "置信带", "Confidence Band") { chart ->
            loadJson("data/confidence-band.json").then { json ->
                val table = json.unsafeCast<Array<ConfidenceBandItem>>()
                // The original shifts the whole table up so that the lowest bound sits at zero.
                val base = -floor(table.fold(Double.POSITIVE_INFINITY) { min, item -> minOf(min, item.l) })
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Confidence Band"
                                    subtext = "Example in MetricsGraphics.js"
                                    left = StringOrDouble("center")
                                },
                            )
                        tooltip =
                            one(
                                unsafeJso<TooltipOption> {
                                    trigger = TooltipTriggers.axis
                                    axisPointer =
                                        unsafeJso<AxisPointerOption> {
                                            type = AxisPointerTypes.cross
                                            animation = BooleanOrString(false)
                                            label =
                                                unsafeJso<AxisPointerLabelOption> {
                                                    backgroundColor = ZRColor("#ccc")
                                                    borderColor = "#aaa"
                                                    borderWidth = 1.0
                                                    shadowBlur = 0.0
                                                    shadowOffsetX = 0.0
                                                    shadowOffsetY = 0.0
                                                    color = "#222"
                                                }
                                        }
                                    formatter =
                                        TooltipFormatter(
                                            { params ->
                                                val items = params.unsafeCast<Array<CallbackDataParams>>()
                                                val last = items[2]
                                                TooltipContent(
                                                    textOf(last.name) + "<br />" +
                                                        percent(numberAt(last.value, 0) - base),
                                                )
                                            },
                                        )
                                },
                            )
                        grid =
                            one(
                                unsafeJso<GridOption> {
                                    left = StringOrDouble("3%")
                                    right = StringOrDouble("4%")
                                    bottom = StringOrDouble("3%")
                                    containLabel = true
                                },
                            )
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.category
                                    data = strings(*Array(table.size) { table[it].date })
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            formatter =
                                                LabelFormatter(
                                                    js(
                                                        "(value, index) => index === 0 ? value : " +
                                                            "(new Date(value).getMonth() + 1) + '-' + " +
                                                            "(new Date(value).getDate())",
                                                    ) as LabelFormatterCallback,
                                                )
                                        }
                                    boundaryGap = BooleanOrStringArray(false)
                                },
                            )
                        yAxis =
                            one(
                                unsafeJso<YAxisOption> {
                                    axisLabel =
                                        unsafeJso<AxisLabelOption> {
                                            formatter =
                                                LabelFormatter(
                                                    js("(offset) => function (value) {\n" +
                                                        "    return (value - offset) * 100 + '%';\n" +
                                                        "}")(base) as LabelFormatterCallback,
                                                )
                                        }
                                    axisPointer =
                                        unsafeJso<AxisPointerOption> {
                                            label =
                                                unsafeJso<AxisPointerLabelOption> {
                                                    formatter =
                                                        LabelFormatter(
                                                            js("(offset) => function (params) {\n" +
                                                                "    return ((params.value - offset) * 100).toFixed(1) + '%';\n" +
                                                                "}")(base) as LabelFormatterCallback,
                                                        )
                                                }
                                        }
                                    splitNumber = 3.0
                                },
                            )
                        series =
                            arrayOf<SeriesOption>(
                                lineSeries {
                                    name = "L"
                                    typedData = numbers(*DoubleArray(table.size) { table[it].l + base })
                                    lineStyle = unsafeJso<LineStyleOption> { opacity = 0.0 }
                                    stack = "confidence-band"
                                    symbol = SymbolShape("none")
                                },
                                lineSeries {
                                    name = "U"
                                    typedData = numbers(*DoubleArray(table.size) { table[it].u - table[it].l })
                                    lineStyle = unsafeJso<LineStyleOption> { opacity = 0.0 }
                                    areaStyle = unsafeJso<AreaStyleOption> { color = ZRColor("#ccc") }
                                    stack = "confidence-band"
                                    symbol = SymbolShape("none")
                                },
                                lineSeries {
                                    typedData = numbers(*DoubleArray(table.size) { table[it].value + base })
                                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#333") }
                                    showSymbol = false
                                },
                            )
                    },
                )
                null
            }
        }
