package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.legendNames

private val docExamplePictorialBarSymbolSizeCategory = Category(Groups.DOC, "examples/doc/DocExamplePictorialBarSymbolSize.kt")

val docExamplePictorialBarSymbolSizeEntry: CatalogEntry =
    docExamplePictorialBarSymbolSizeCategory.interactive(
            "doc-example/pictorialBar-symbolSize",
            "象形柱图：符号尺寸",
            "Pictorial bar: symbol size",
        ) { chart, holder ->
            chart.setOption(
                unsafeJso<EChartsOption> {
                    color = arrayOf(ZRColor("#bb0004"), ZRColor("#FFD48A"))
                    legend =
                        one(
                            unsafeJso<LegendOption> {
                                data = legendNames("pictorial element", "reference bar")
                            },
                        )
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                data = strings("a", "b", "c", "d")
                                axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                                axisLabel = unsafeJso<AxisLabelOption> { interval = AxisInterval(0.0) }
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                max = AxisMinMax(80.0)
                                splitLine = unsafeJso<SplitLineOption> { show = false }
                            },
                        )
                    grid =
                        one(
                            unsafeJso<GridOption> {
                                left = StringOrDouble(40.0)
                                right = StringOrDouble(10.0)
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            pictorialBarSeries {
                                name = "pictorial element"
                                symbol = ROCKET_SYMBOL
                                z = 10.0
                                typedData =
                                    dataItems(
                                        pictorialItem(60.0) {
                                            symbolSize =
                                                StringOrDoubleOrArray(
                                                    arrayOf(StringOrDouble("50%"), StringOrDouble("100%")),
                                                )
                                        },
                                        pictorialItem(60.0) {
                                            symbolSize =
                                                StringOrDoubleOrArray(
                                                    arrayOf(StringOrDouble("50%"), StringOrDouble(120.0)),
                                                )
                                        },
                                        pictorialItem(60.0) { symbolSize = StringOrDoubleOrArray(50.0) },
                                        pictorialItem(60.0) {
                                            symbolRepeat = BooleanOrDoubleOrString(true)
                                            symbolSize = StringOrDoubleOrArray("70%")
                                        },
                                    )
                            },
                            barSeries {
                                name = "reference bar"
                                barGap = StringOrDouble("-100%")
                                typedData = numbers(60.0, 60.0, 60.0, 60.0)
                            },
                        )
                },
            )
            holder.setTimeout({ placeSymbolSizeDescriptions(chart) }, 100)
            chart.on("updated") { _ -> placeSymbolSizeDescriptions(chart) }
        }
