package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.gauge.center

private val customProfileCategory = Category(Groups.CUSTOM, "examples/custom/CustomProfile.kt")

val customProfileEntry: CatalogEntry =
    customProfileCategory.interactive("custom-profile", "性能分析图", "Profile") { chart ->
            val categories = arrayOf("categoryA", "categoryB", "categoryC")
            val types =
                arrayOf(
                    Pair("JS Heap", "#7b9ce1"),
                    Pair("Documents", "#bd6d6c"),
                    Pair("Nodes", "#75d874"),
                    Pair("Listeners", "#e0bc78"),
                    Pair("GPU Memory", "#dc77dc"),
                    Pair("GPU", "#72b362"),
                )
            val start0 = 100000.0
            val datas = mutableListOf<OptionDataValue>()
            var base = start0
            for (cat in 0 until 3) {
                for (i in 0 until 10) {
                    val t = types[(cat * 3 + i) % types.size]
                    val duration = ((cat * 10 + i * 7) % 100 * 100 + 50).toDouble()
                    val start = base
                    val end = base + duration
                    datas.add(
                        arrayOf<OptionDataValue>(
                            OptionDataValue(cat.toDouble()),
                            OptionDataValue(start),
                            OptionDataValue(end),
                            OptionDataValue(duration),
                        ).unsafeCast<OptionDataValue>(),
                    )
                    base = end + ((cat * 5 + i * 3) % 20 * 100 + 50).toDouble()
                }
            }
            chart.setOption(
                unsafeJso<EChartsOption> {
                    tooltip =
                        one(
                            unsafeJso<TooltipOption> {
                                formatter =
                                    TooltipFormatter(
                                        js("(param) => { return param.marker + param.value[3] + ' ms'; }")
                                            .unsafeCast<TooltipFormatterCallback>(),
                                    )
                            },
                        )
                    title = one(unsafeJso<TitleOption> { text = "Profile"; left = StringOrDouble("center") })
                    dataZoom =
                        arrayOf<DataZoomOption>(
                            unsafeJso<DataZoomOption> {
                                type = DataZoomTypes.slider
                                filterMode = FilterModes.weakFilter
                                showDataShadow = BooleanOrString(false)
                            },
                            unsafeJso<DataZoomOption> {
                                type = DataZoomTypes.inside
                                filterMode = FilterModes.weakFilter
                            },
                        )
                    grid = one(unsafeJso<GridOption> { height = StringOrDouble(300.0) })
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                min = start0.unsafeCast<AxisMinMax>()
                                scale = true
                            },
                        )
                    yAxis = one(unsafeJso<YAxisOption> { data = strings(*categories) })
                    series =
                        arrayOf<SeriesOption>(
                            customSeries {
                                renderItem = { _, api ->
                                    val categoryIndex = api.value(DimensionLoose(0.0))
                                    val start =
                                        api.coord(
                                            CoordinateSystemDataCoord(
                                                arrayOf(api.value(DimensionLoose(1.0)), categoryIndex),
                                            ),
                                        )
                                    val end =
                                        api.coord(
                                            CoordinateSystemDataCoord(
                                                arrayOf(api.value(DimensionLoose(2.0)), categoryIndex),
                                            ),
                                        )
                                    val h = 10.0
                                    unsafeJso<CustomPathOption> {
                                        type = "rect"
                                        shape =
                                            unsafeJso {
                                                x = start[0]
                                                y = start[1] - h / 2
                                                width = end[0] - start[0]
                                                height = h
                                            }
                                        style = api.style()
                                    }
                                }
                                itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
                                encode =
                                    unsafeJso {
                                        this.x =
                                            OptionEncodeValue(
                                                arrayOf<DimensionLoose>(DimensionLoose(1.0), DimensionLoose(2.0)),
                                            )
                                        this.y = OptionEncodeValue(DimensionLoose(0.0))
                                    }
                                typedData = datas.toTypedArray().unsafeCast<Array<OptionDataItem?>>()
                            },
                        )
                },
            )
            null
        }
