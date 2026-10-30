package demo.json.examples

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.namedValue
import demo.json.namedValues
import io.github.shijianjs.kmpechartsjson.*

/*
 * Pie examples carried over from `pie-*.ts` of apache/echarts-examples.
 *
 * The pages keep the official ids, so the Kotlin/JS port and the JSON port of the same chart show
 * up under one route. Image-heavy examples (`pie-pattern`, `pie-rich-text`) and the pages that call
 * the chart API on a timer (`pie-highlight`, `pie-labelLine-adjust`) stay Kotlin-only.
 */

private val pie = JsonCategory("json/examples/PieJson.kt")

/** The `{ name, value }` set shared by the doughnut family. */
private val ACCESS_FROM =
    namedValues(
        "Search Engine" to 1048,
        "Direct" to 735,
        "Email" to 580,
        "Union Ads" to 484,
        "Video Ads" to 300,
    )

/** Legend entries named after [names]. */
private fun legendOf(vararg names: String): List<String> = names.toList()

/** Eight rose values, labelled `rose 1` .. `rose 8`. */
private fun roseData(vararg values: Number): List<io.github.shijianjs.kmpechartsjson.DataValue> =
    namedValues(*Array(values.size) { index -> "rose ${index + 1}" to values[index] })

/** Label of the doughnut family: the hovered percentage in the middle of the ring. */
private fun centerEmphasis(): io.github.shijianjs.kmpechartsjson.EmphasisOption.() -> Unit = {
    label {
        show = true
        fontSize = 40.0
        fontWeight = str("bold")
    }
}

private val doughnutRadius = NumberOrStringOrNumbers.of(str("40%"), str("70%"))

public val pieJsonExamples: List<JsonExample> =
    listOf(
        pie.example("pie-simple") {
            title {
                text = "Referer of a Website"
                subtext = "Fake Data"
                left = str("center")
            }
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                orient = Orient.Vertical
                left = str("left")
            }
            series {
                pie {
                    name = "Access From"
                    radius = NumberOrStringOrNumbers.of("50%")
                    data = ACCESS_FROM
                    emphasis {
                        itemStyle {
                            shadowBlur = 10.0
                            shadowOffsetX = 0.0
                            shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                        }
                    }
                }
            }
        },
        pie.example("pie-doughnut") {
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                top = str("5%")
                left = str("center")
            }
            series {
                pie {
                    name = "Access From"
                    radius = doughnutRadius
                    avoidLabelOverlap = false
                    label {
                        show = false
                        position = "center"
                    }
                    emphasis(centerEmphasis())
                    labelLine { show = false }
                    data = ACCESS_FROM
                }
            }
        },
        pie.example("pie-half-donut") {
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                top = str("5%")
                left = str("center")
            }
            series {
                pie {
                    name = "Access From"
                    radius = doughnutRadius
                    center = listOf(str("50%"), str("70%"))
                    startAngle = 180.0
                    endAngle = 360.0
                    data = ACCESS_FROM
                }
            }
        },
        pie.example("pie-borderRadius") {
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                top = str("5%")
                left = str("center")
            }
            series {
                pie {
                    name = "Access From"
                    radius = doughnutRadius
                    avoidLabelOverlap = false
                    itemStyle {
                        borderRadius = NumberOrStringOrNumbers.of(10)
                        borderColor = "#fff".asColor()
                        borderWidth = 2.0
                    }
                    label {
                        show = false
                        position = "center"
                    }
                    emphasis(centerEmphasis())
                    labelLine { show = false }
                    data = ACCESS_FROM
                }
            }
        },
        pie.example("pie-padAngle") {
            tooltip { trigger = TooltipTrigger.Item }
            legend {
                top = str("5%")
                left = str("center")
            }
            series {
                pie {
                    name = "Access From"
                    radius = doughnutRadius
                    avoidLabelOverlap = false
                    padAngle = 5.0
                    itemStyle { borderRadius = NumberOrStringOrNumbers.of(10) }
                    label {
                        show = false
                        position = "center"
                    }
                    emphasis(centerEmphasis())
                    labelLine { show = false }
                    data = ACCESS_FROM
                }
            }
        },
        pie.example("pie-roseType") {
            title {
                text = "Nightingale Chart"
                subtext = "Fake Data"
                left = str("center")
            }
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c} ({d}%)"
            }
            legend {
                entries("rose1", "rose2", "rose3", "rose4", "rose5", "rose6", "rose7", "rose8")
            }
            series {
                pie {
                    name = "Radius Mode"
                    radius = NumberOrStringOrNumbers.of(num(20), num(140))
                    center = listOf(str("25%"), str("50%"))
                    roseType = io.github.shijianjs.kmpechartsjson.BooleanOrString.of("radius")
                    itemStyle { borderRadius = NumberOrStringOrNumbers.of(5) }
                    label { show = false }
                    emphasis { label { show = true } }
                    data = roseData(40, 33, 28, 22, 20, 15, 12, 10)
                }
                pie {
                    name = "Area Mode"
                    radius = NumberOrStringOrNumbers.of(num(20), num(140))
                    center = listOf(str("75%"), str("50%"))
                    roseType = io.github.shijianjs.kmpechartsjson.BooleanOrString.of("area")
                    itemStyle { borderRadius = NumberOrStringOrNumbers.of(5) }
                    data = roseData(30, 28, 26, 24, 22, 20, 18, 16)
                }
            }
        },
        pie.example("pie-roseType-simple") {
            legend { top = str("bottom") }
            series {
                pie {
                    name = "Nightingale Chart"
                    radius = NumberOrStringOrNumbers.of(num(50), num(250))
                    center = listOf(str("50%"), str("50%"))
                    roseType = io.github.shijianjs.kmpechartsjson.BooleanOrString.of("area")
                    itemStyle { borderRadius = NumberOrStringOrNumbers.of(8) }
                    data = roseData(40, 38, 32, 30, 28, 26, 22, 18)
                }
            }
        },
        pie.example("pie-alignTo") {
            val alignData =
                namedValues(
                    "Apples" to 70,
                    "Strawberries" to 68,
                    "Bananas" to 48,
                    "Oranges" to 40,
                    "Pears" to 32,
                    "Pineapples" to 27,
                    "Grapes" to 18,
                )
            title {
                text = "Pie label alignTo"
                left = str("center")
            }
            title {
                subtext = "alignTo: \"none\" (default)"
                left = str("16.67%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "alignTo: \"labelLine\""
                left = str("50%")
                top = str("75%")
                textAlign = "center"
            }
            title {
                subtext = "alignTo: \"edge\""
                left = str("83.33%")
                top = str("75%")
                textAlign = "center"
            }
            series {
                pie {
                    radius = NumberOrStringOrNumbers.of("25%")
                    center = listOf(str("50%"), str("50%"))
                    data = alignData
                    label {
                        position = "outer"
                        alignTo = "none"
                        bleedMargin = 5.0
                    }
                    left = num(0)
                    right = str("66.6667%")
                    top = num(0)
                    bottom = num(0)
                }
                pie {
                    radius = NumberOrStringOrNumbers.of("25%")
                    center = listOf(str("50%"), str("50%"))
                    data = alignData
                    label {
                        position = "outer"
                        alignTo = "labelLine"
                        bleedMargin = 5.0
                    }
                    left = str("33.3333%")
                    right = str("33.3333%")
                    top = num(0)
                    bottom = num(0)
                }
                pie {
                    radius = NumberOrStringOrNumbers.of("25%")
                    center = listOf(str("50%"), str("50%"))
                    data = alignData
                    label {
                        position = "outer"
                        alignTo = "edge"
                        margin = num(20)
                    }
                    left = str("66.6667%")
                    right = num(0)
                    top = num(0)
                    bottom = num(0)
                }
            }
        },
        pie.example("pie-nest") {
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c} ({d}%)"
            }
            legend {
                entries(
                    "Direct",
                    "Marketing",
                    "Search Engine",
                    "Email",
                    "Union Ads",
                    "Video Ads",
                    "Baidu",
                    "Google",
                    "Bing",
                    "Others",
                )
            }
            series {
                pie {
                    name = "Access From"
                    selectedMode = io.github.shijianjs.kmpechartsjson.BooleanOrString.of("single")
                    radius = NumberOrStringOrNumbers.of(num(0), str("30%"))
                    data =
                        namedValues(
                            "Search Engine" to 1548,
                            "Direct" to 775,
                            "Marketing" to 679,
                        )
                    label {
                        position = "inner"
                        fontSize = 14.0
                    }
                    labelLine { show = false }
                }
                pie {
                    name = "Access From"
                    radius = NumberOrStringOrNumbers.of(str("45%"), str("60%"))
                    data =
                        namedValues(
                            "Baidu" to 1048,
                            "Direct" to 335,
                            "Email" to 310,
                            "Google" to 251,
                            "Union Ads" to 234,
                            "Bing" to 147,
                            "Video Ads" to 135,
                            "Others" to 102,
                        )
                    labelLine { length = 30.0 }
                    label {
                        formatter = "{a|{a}}{abg|}\n{hr|}\n  {b|{b}：}{c}  {per|{d}%}  "
                        backgroundColor = "#F6F8FC".asColor()
                        borderColor = "#8C8D8E".asColor()
                        borderWidth = 1.0
                        borderRadius = NumberOrStringOrNumbers.of(4)
                        rich =
                            mapOf(
                                "a" to
                                    textStyleOf {
                                        color = "#6E7079".asColor()
                                        lineHeight = 22.0
                                        align = "center"
                                    },
                                "hr" to
                                    textStyleOf {
                                        borderColor = "#8C8D8E".asColor()
                                        width = str("100%")
                                        borderWidth = 1.0
                                        height = num(0)
                                    },
                                "b" to
                                    textStyleOf {
                                        color = "#4C5058".asColor()
                                        fontSize = 14.0
                                        fontWeight = str("bold")
                                        lineHeight = 33.0
                                    },
                                "per" to
                                    textStyleOf {
                                        color = "#fff".asColor()
                                        backgroundColor = "#4C5058".asColor()
                                        padding = NumberOrStringOrNumbers.of(num(3), num(4))
                                        borderRadius = NumberOrStringOrNumbers.of(4)
                                    },
                            )
                    }
                }
            }
        },
        pie.example("pie-custom") {
            backgroundColor = "#2c343c".asColor()
            title {
                text = "Customized Pie"
                left = str("center")
                top = num(20)
                textStyle { color = "#ccc".asColor() }
            }
            tooltip { trigger = TooltipTrigger.Item }
            visualMap {
                show = false
                min = 80.0
                max = 600.0
                inRange { colorLightness = listOf(0.0, 1.0) }
            }
            series {
                pie {
                    name = "Access From"
                    radius = NumberOrStringOrNumbers.of("55%")
                    center = listOf(str("50%"), str("50%"))
                    data =
                        namedValues(
                            "Video Ads" to 235,
                            "Union Ads" to 274,
                            "Email" to 310,
                            "Direct" to 335,
                            "Search Engine" to 400,
                        )
                    roseType = io.github.shijianjs.kmpechartsjson.BooleanOrString.of("radius")
                    label { color = "rgba(255, 255, 255, 0.3)".asColor() }
                    labelLine {
                        lineStyle { color = "rgba(255, 255, 255, 0.3)".asColor() }
                        smooth = NumberOrStringOrNumbers.of(0.2)
                        length = 10.0
                        length2 = num(20)
                    }
                    itemStyle {
                        color = "#c23531".asColor()
                        shadowBlur = 200.0
                        shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                    }
                    animationEasing = "elasticOut"
                    animationDelay = 100.0
                }
            }
        },
        pie.example("pie-legend") {
            val surnames = surnames(50)
            title {
                text = "同名数量统计"
                subtext = "纯属虚构"
                left = str("center")
            }
            tooltip {
                trigger = TooltipTrigger.Item
                formatter = "{a} <br/>{b} : {c} ({d}%)"
            }
            legend {
                type = LegendType.Scroll
                orient = Orient.Vertical
                right = num(10)
                top = num(20)
                bottom = num(20)
                entries(*surnames.map { it.first }.toTypedArray())
            }
            series {
                pie {
                    name = "姓名"
                    radius = NumberOrStringOrNumbers.of("55%")
                    center = listOf(str("40%"), str("50%"))
                    data = surnames.map { (name, value) -> demo.json.namedValue(name, value) }
                    emphasis {
                        itemStyle {
                            shadowBlur = 10.0
                            shadowOffsetX = 0.0
                            shadowColor = "rgba(0, 0, 0, 0.5)".asColor()
                        }
                    }
                }
            }
        },
    )

/** `TextStyleOption` alias: the rich styles above are plain text styles. */
/** The surname pool of `pie-legend`, copied from the upstream `nameList`. */
private val SURNAME_POOL =
    arrayOf(
        "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨",
        "朱", "秦", "尤", "许", "何", "吕", "施", "张", "孔", "曹", "严", "华", "金", "魏", "陶", "姜",
        "戚", "谢", "邹", "喻", "柏", "水", "窦", "章", "云", "苏", "潘", "葛", "奚", "范", "彭", "郎",
        "鲁", "韦", "昌", "马", "苗", "凤", "花", "方", "俞", "任", "袁", "柳", "酆", "鲍", "史", "唐",
        "费", "廉", "岑", "薛", "雷", "贺", "倪", "汤", "滕", "殷", "罗", "毕", "郝", "邬", "安", "常",
        "乐", "于", "时", "傅", "皮", "卞", "齐", "康", "伍", "余", "元", "卜", "顾", "孟", "平", "黄",
        "和", "穆", "萧", "尹", "姚", "邵", "湛", "汪", "祁", "毛", "禹", "狄", "米", "贝", "明", "臧",
        "计", "伏", "成", "戴", "谈", "宋", "茅", "庞", "熊", "纪", "舒", "屈", "项", "祝", "董", "梁",
        "杜", "阮", "蓝", "闵", "席", "季", "麻", "强", "贾", "路", "娄", "危",
    )

/**
 * The generated surnames of `pie-legend`.
 *
 * Upstream draws names and values with `Math.random()`; a fixed linear congruential sequence keeps
 * the page reproducible while producing the same shape of data.
 */
private fun surnames(count: Int): List<Pair<String, Double>> {
    var state = 20261028L
    fun next(): Double {
        state = state * 1103515245L + 12345L
        return ((state ushr 16) and 0x7FFF).toDouble() / 0x8000
    }
    return List(count) {
        val word =
            buildString {
                repeat((next() * 2 + 1).toInt()) { append(SURNAME_POOL[(next() * SURNAME_POOL.size).toInt()]) }
            }
        val name = if (next() > 0.65) "$word·${SURNAME_POOL[(next() * SURNAME_POOL.size).toInt()]}" else word
        name to (next() * 100000).toLong().toDouble()
    }
}
