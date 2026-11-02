package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.shared.legendNames

private val docExamplePictorialBarRepeatCategory = Category(Groups.DOC, "examples/doc/DocExamplePictorialBarRepeat.kt")

val docExamplePictorialBarRepeatEntry: CatalogEntry =
    docExamplePictorialBarRepeatCategory.chart(
            "doc-example/pictorialBar-repeat",
            "象形柱图：重复填充",
            "Pictorial bar: repeated symbols",
        ) {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { data = legendNames("pictorialBar", "bar") })
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings("symbolRepeat: true", "symbolRepeat: false")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { splitLine = unsafeJso<SplitLineOption> { show = false } })
                animationEasing = AnimationEasings.elasticOut
                series =
                    arrayOf<SeriesOption>(
                        pictorialBarSeries {
                            symbol = "circle"
                            typedData =
                                dataItems(
                                    pictorialItem(29000.0) {
                                        symbolRepeat = BooleanOrDoubleOrString(true)
                                        symbolSize =
                                            StringOrDoubleOrArray(
                                                arrayOf(StringOrDouble(50.0), StringOrDouble(50.0)),
                                            )
                                    },
                                    pictorialItem(29000.0) {
                                        symbolSize =
                                            StringOrDoubleOrArray(
                                                arrayOf(StringOrDouble(50.0), StringOrDouble("100%")),
                                            )
                                    },
                                )
                        },
                    )
            }
        }
