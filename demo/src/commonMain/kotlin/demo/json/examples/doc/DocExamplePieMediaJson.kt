package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import demo.json.namedValues
import io.github.shijianjs.kmpechartsjson.*
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import demo.json.examples.shared.mediaPies
import demo.json.*

private val docExamplePieMediaJsonCategory = JsonCategory("json/examples/doc/DocExamplePieMediaJson.kt")

val docExamplePieMediaJsonExample: JsonExample =
    docExamplePieMediaJsonCategory.example("doc-example/pie-media") {
            baseOption =
                echartsOption {
                    title {
                        text = "南丁格尔玫瑰图"
                        subtext = "纯属虚构"
                        left = str("center")
                    }
                    tooltip {
                        trigger = TooltipTrigger.Item
                        formatter = "{a} <br/>{b} : {c} ({d}%)"
                    }
                    legend { entries("rose1", "rose2", "rose3", "rose4", "rose5", "rose6", "rose7", "rose8") }
                    toolbox {
                        show = true
                        feature =
                            ToolboxFeatureOption().apply {
                                dataView = ToolboxDataViewFeature().apply { readOnly = false }
                                magicType =
                                    ToolboxMagicTypeFeature().apply {
                                        type = listOf(SeriesType.Pie, SeriesType.Funnel)
                                    }
                                restore = ToolboxFeature().apply { show = true }
                                saveAsImage = ToolboxSaveAsImageFeature().apply { show = true }
                            }
                    }
                    series {
                        pie {
                            name = "半径模式"
                            roseType = BooleanOrString.of("radius")
                            label { show = false }
                            labelLine { show = false }
                            emphasis { label { show = true } }
                            data =
                                namedValues(
                                    "rose1" to 10,
                                    "rose2" to 5,
                                    "rose3" to 15,
                                    "rose4" to 25,
                                    "rose5" to 20,
                                    "rose6" to 35,
                                    "rose7" to 30,
                                    "rose8" to 40,
                                )
                        }
                        pie {
                            name = "面积模式"
                            roseType = BooleanOrString.of("area")
                            data =
                                namedValues(
                                    "rose1" to 10,
                                    "rose2" to 5,
                                    "rose3" to 15,
                                    "rose4" to 25,
                                    "rose5" to 20,
                                    "rose6" to 35,
                                    "rose7" to 30,
                                    "rose8" to 40,
                                )
                        }
                    }
                }
            media {
                option =
                    echartsOption {
                        legend {
                            right = str("center")
                            bottom = num(0)
                            orient = Orient.Horizontal
                        }
                        series {
                            mediaPies(
                                NumberOrStringOrNumbers.of(num(20), str("50%")),
                                NumberOrStringOrNumbers.of(num(30), str("50%")),
                                listOf(str("25%"), str("50%")),
                                listOf(str("75%"), str("50%")),
                            )
                        }
                    }
            }
            media {
                query = buildJsonObject { put("minAspectRatio", 1) }
                option =
                    echartsOption {
                        legend {
                            right = str("center")
                            bottom = num(0)
                            orient = Orient.Horizontal
                        }
                        series {
                            mediaPies(
                                NumberOrStringOrNumbers.of(num(20), str("50%")),
                                NumberOrStringOrNumbers.of(num(30), str("50%")),
                                listOf(str("25%"), str("50%")),
                                listOf(str("75%"), str("50%")),
                            )
                        }
                    }
            }
            media {
                query = buildJsonObject { put("maxAspectRatio", 1) }
                option =
                    echartsOption {
                        legend {
                            right = str("center")
                            bottom = num(0)
                            orient = Orient.Horizontal
                        }
                        series {
                            mediaPies(
                                NumberOrStringOrNumbers.of(num(20), str("50%")),
                                NumberOrStringOrNumbers.of(num(30), str("50%")),
                                listOf(str("50%"), str("30%")),
                                listOf(str("50%"), str("70%")),
                            )
                        }
                    }
            }
            media {
                query = buildJsonObject { put("maxWidth", 500) }
                option =
                    echartsOption {
                        legend {
                            right = num(10)
                            top = str("15%")
                            orient = Orient.Vertical
                        }
                        series {
                            mediaPies(
                                NumberOrStringOrNumbers.of(num(20), str("50%")),
                                NumberOrStringOrNumbers.of(num(30), str("50%")),
                                listOf(str("50%"), str("30%")),
                                listOf(str("50%"), str("75%")),
                            )
                        }
                    }
            }
        }
