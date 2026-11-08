package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel
import demo.examples.shared.fixed

private val docExamplePictorialBarClipCategory = Category(Groups.DOC, "examples/doc/DocExamplePictorialBarClip.kt")

val docExamplePictorialBarClipEntry: CatalogEntry =
    docExamplePictorialBarClipCategory.chart(
            "doc-example/pictorialBar-clip",
            "象形柱图：裁剪",
            "Pictorial bar: symbolClip",
        ) {
            fun symbolHalfSize(): StringOrDoubleOrArray =
                StringOrDoubleOrArray(arrayOf(StringOrDouble("50%"), StringOrDouble("80%")))
            unsafeJso<EChartsOption> {
                color = arrayOf(ZRColor("#bb0004"), ZRColor("#ccc"))
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data =
                                strings(
                                    "symbolClip: true\nsymbolRepeat: false",
                                    "symbolClip: true\nsymbolRepeat: true",
                                    "symbolClip: false\nsymbolRepeat: true",
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
                        // The series that holds the real value; it sits above the background.
                        pictorialBarSeries {
                            name = "realValue"
                            symbol = PAW_SYMBOL
                            z = 10.0
                            symbolBoundingData = DoubleOrDoubleArray(80.0)
                            typedData =
                                dataItems(
                                    pictorialItem(40.0) { symbolClip = true },
                                    pictorialItem(40.0) {
                                        symbolSize = symbolHalfSize()
                                        symbolClip = true
                                        symbolRepeat = BooleanOrDoubleOrString(true)
                                    },
                                    pictorialItem(40.0) {
                                        symbolSize = symbolHalfSize()
                                        symbolClip = false
                                        symbolRepeat = BooleanOrDoubleOrString(true)
                                    },
                                )
                        },
                        // The background series repeats the same symbols in grey.
                        pictorialBarSeries {
                            name = "background"
                            symbol = PAW_SYMBOL
                            symbolBoundingData = DoubleOrDoubleArray(80.0)
                            // The original nests these fields under `label.normal.textStyle`.
                            label =
                                unsafeJso<LabelOption> {
                                    show = true
                                    position = LabelPosition("outside")
                                    formatter = LabelFormatter("value: {c}")
                                    color = "#bb0004"
                                }
                            typedData =
                                dataItems(
                                    // Zero disables the initial animation of the background but
                                    // keeps the animation of later updates.
                                    pictorialItem(40.0) { animationDuration = AnimationDuration(0.0) },
                                    pictorialItem(40.0) {
                                        symbolSize = symbolHalfSize()
                                        symbolRepeat = BooleanOrDoubleOrString("fixed")
                                        animationDuration = AnimationDuration(0.0)
                                    },
                                    pictorialItem(40.0) {
                                        symbolSize = symbolHalfSize()
                                        symbolRepeat = BooleanOrDoubleOrString("fixed")
                                    },
                                )
                        },
                    )
            }
        }
