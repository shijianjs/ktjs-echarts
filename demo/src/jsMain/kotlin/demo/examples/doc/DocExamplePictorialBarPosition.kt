package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.shared.axisLabel
import demo.examples.shared.legendNames

private val docExamplePictorialBarPositionCategory = Category(Groups.DOC, "examples/doc/DocExamplePictorialBarPosition.kt")

val docExamplePictorialBarPositionEntry: CatalogEntry =
    docExamplePictorialBarPositionCategory.chart(
            "doc-example/pictorialBar-position",
            "象形柱图：符号位置",
            "Pictorial bar: symbol position",
        ) {
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
                            data =
                                strings(
                                    "symbolPosition: \"start\"\nsymbolOffset: [0, 0]",
                                    "symbolPosition: \"end\"\nsymbolOffset: [0, 0]",
                                    "symbolPosition: \"center\"\nsymbolOffset: [0, 0]",
                                    "symbolPosition: \"end\"\nsymbolOffset: ['-100%', -120]",
                                )
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
                series =
                    arrayOf<SeriesOption>(
                        pictorialBarSeries {
                            name = "pictorial element"
                            symbol = WALK_SYMBOL
                            symbolSize =
                                StringOrDoubleOrArray(
                                    arrayOf(StringOrDouble(50.0), StringOrDouble(120.0)),
                                )
                            z = 10.0
                            typedData =
                                dataItems(
                                    pictorialItem(60.0) { symbolPosition = PictorialBarSymbolPositions.start },
                                    pictorialItem(60.0) { symbolPosition = PictorialBarSymbolPositions.end },
                                    pictorialItem(60.0) { symbolPosition = PictorialBarSymbolPositions.center },
                                    pictorialItem(60.0) {
                                        symbolPosition = PictorialBarSymbolPositions.end
                                        symbolOffset =
                                            StringOrDoubleOrArray(
                                                arrayOf(StringOrDouble(-80.0), StringOrDouble("-100%")),
                                            )
                                    },
                                )
                        },
                        barSeries {
                            name = "reference bar"
                            barGap = StringOrDouble("-100%")
                            typedData = numbers(60.0, 60.0, 60.0, 60.0)
                        },
                    )
            }
        }
