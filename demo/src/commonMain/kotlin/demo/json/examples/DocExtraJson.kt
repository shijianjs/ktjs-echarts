package demo.json.examples

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*

/*
 * Two `doc-example/` pages that `DocJson.kt` does not carry: the rich-text label of
 * `text-block-fragment` and the array form of `symbolBoundingData` of
 * `pictorialBar-symbolBoundingDataArray`.
 *
 * They live in their own file because both exercise model fields that no other page writes: the
 * `rich` map of `LabelOption`, and `NumberOrNumbers` / `BooleanOrNumberOrString` on a pictorial bar.
 *
 * The pictorial page keeps the three series of the original. ECharts 6 reads `axisLabel` text styles
 * from the label itself, so the `textStyle` wrapper of the upstream `axisLabel` is flattened, the
 * same way the Kotlin/JS port does. `symbolRepeat: 'fixed'` is representable here through
 * `BooleanOrNumberOrString`, unlike the boolean/number-only shape the Kotlin/JS port once assumed.
 */

private val docExtra = JsonCategory("json/examples/DocExtraJson.kt")

/** The star of `pictorialBar-symbolBoundingDataArray`. */
private const val STAR_SYMBOL =
    "path://m15.5,19c-0.082,0 -0.164,-0.02 -0.239,-0.061l-5.261,-2.869l-5.261,2.869c-0.168,0.092 -0.373,0.079 -0.529,-0.032s-0.235,-0.301 -0.203,-0.49l0.958,-5.746l-3.818,-3.818c-0.132,-0.132 -0.18,-0.328 -0.123,-0.506s0.209,-0.31 0.394,-0.341l5.749,-0.958l2.386,-4.772c0.085,-0.169 0.258,-0.276 0.447,-0.276s0.363,0.107 0.447,0.276l2.386,4.772l5.749,0.958c0.185,0.031 0.337,0.162 0.394,0.341s0.01,0.374 -0.123,0.506l-3.818,3.818l0.958,5.746c0.031,0.189 -0.048,0.379 -0.203,0.49c-0.086,0.061 -0.188,0.093 -0.29,0.093z"

/** Hides every part of an axis; the text pages draw on invisible `-1..1` axes. */
private fun AxisOption.hidden() {
    axisLabel { show = false }
    axisLine { show = false }
    splitLine { show = false }
    axisTick { show = false }
}

/** One background or filled star series of the pictorial page. */
private fun SeriesBuilder.starSeries(name: String, values: List<DataValue>, block: SeriesOption.() -> Unit) {
    pictorialBar {
        this.name = name
        symbol = STAR_SYMBOL
        symbolSize = NumberOrStringOrNumbers.of(str("80%"), str("80%"))
        data = values
        block()
    }
}

public val docExtraJsonExamples: List<JsonExample> =
    listOf(
        docExtra.example("doc-example/text-block-fragment") {
            xAxis {
                hidden()
                min = num(-1)
                max = num(1)
            }
            yAxis {
                hidden()
                min = num(-1)
                max = num(1)
            }
            series {
                scatter {
                    symbolSize = NumberOrStringOrNumbers.of(1)
                    data = listOf(point(0, 0))
                    label {
                        show = true
                        formatter =
                            "The whole box is a {term|Text Block}, with\n" +
                                "red border and grey background.\n" +
                                "{fragment1|A Text Fragment} {fragment2|Another Text Fragment}\n" +
                                "Text fragments can be customized."
                        backgroundColor = "#eee".asColor()
                        borderColor = "rgb(199,86,83)".asColor()
                        borderWidth = 2.0
                        borderRadius = NumberOrStringOrNumbers.of(5)
                        padding = NumberOrStringOrNumbers.of(10)
                        color = "#000".asColor()
                        fontSize = 14.0
                        shadowBlur = 3.0
                        shadowColor = "#888".asColor()
                        shadowOffsetX = 0.0
                        shadowOffsetY = 3.0
                        lineHeight = 30.0
                        rich =
                            mapOf(
                                "term" to
                                    textStyleOf {
                                        fontSize = 18.0
                                        color = "rgb(199,86,83)".asColor()
                                    },
                                "fragment1" to
                                    textStyleOf {
                                        backgroundColor = "#000".asColor()
                                        color = "yellow".asColor()
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                                "fragment2" to
                                    textStyleOf {
                                        backgroundColor = "#339911".asColor()
                                        color = "#fff".asColor()
                                        borderRadius = NumberOrStringOrNumbers.of(15)
                                        padding = NumberOrStringOrNumbers.of(5)
                                    },
                            )
                    }
                }
            }
        },
        docExtra.example("doc-example/pictorialBar-symbolBoundingDataArray") {
            val categories = listOf("DTA", "Fin", "Tool", "VTG", "CAAT", "UYL", "TC")
            val values = listOf(-12, -60, 34, 23, -21, 0, -25).map { valueOf(it) }
            tooltip {
                trigger = TooltipTrigger.Axis
                axisPointer = AxisPointerOption().apply { type = "shadow" }
            }
            grid { left = num(120) }
            xAxis {
                splitLine { show = false }
                axisLine { show = false }
                axisTick { show = false }
                axisLabel { show = false }
            }
            yAxis {
                categories(*categories.toTypedArray())
                axisTick { show = false }
                axisLine { lineStyle = LineStyleOption().apply { color = "#ccc".asColor() } }
                axisLabel {
                    margin = 40.0
                    color = "#999".asColor()
                    fontSize = 16.0
                }
                splitLine { show = true }
                position = "top"
            }
            series {
                starSeries("bg", values) {
                    tooltip = TooltipOption().apply { trigger = TooltipTrigger.None }
                    itemStyle { color = "#ddd".asColor() }
                    silent = true
                    symbolRepeat = BooleanOrNumberOrString.of("fixed")
                    symbolClip = false
                    symbolBoundingData = NumberOrNumbers.of(40)
                    z = -1.0
                }
                starSeries("bg", values) {
                    tooltip = TooltipOption().apply { trigger = TooltipTrigger.None }
                    itemStyle { color = "#ddd".asColor() }
                    silent = true
                    symbolRepeat = BooleanOrNumberOrString.of("fixed")
                    symbolClip = false
                    symbolBoundingData = NumberOrNumbers.of(-60)
                    z = -1.0
                }
                starSeries("data", values) {
                    symbolRepeat = BooleanOrNumberOrString.of(true)
                    symbolClip = true
                    symbolBoundingData = NumberOrNumbers.of(-60, 40)
                }
            }
        },
    )

