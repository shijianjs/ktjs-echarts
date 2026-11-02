package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val lineMarkerJsonCategory = JsonCategory("json/examples/line/LineMarkerJson.kt")

val lineMarkerJsonExample: JsonExample =
    lineMarkerJsonCategory.example("line-marker") {
            title { text = "Temperature Change in the Coming Week" }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { }
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        dataView = ToolboxDataViewFeature().apply { readOnly = false }
                        magicType = ToolboxMagicTypeFeature().apply { type = listOf(SeriesType.Line, SeriesType.Bar) }
                        restore = ToolboxFeature()
                        saveAsImage = ToolboxSaveAsImageFeature()
                    }
            }
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories(*WEEK)
            }
            yAxis {
                type = AxisType.Value
                axisLabel { formatter = "{value} °C" }
            }
            series {
                line {
                    name = "Highest"
                    values(10, 11, 13, 11, 12, 12, 9)
                    markPoint {
                        data =
                            listOf(
                                MarkPointData().apply {
                                    type = "max"
                                    name = "Max"
                                },
                                MarkPointData().apply {
                                    type = "min"
                                    name = "Min"
                                },
                            )
                    }
                    markLine { data = listOf(averageMarkLine()) }
                }
                line {
                    name = "Lowest"
                    values(1, -2, 2, 5, 3, 2, 0)
                    markPoint {
                        data =
                            listOf(
                                MarkPointData().apply {
                                    name = "周最低"
                                    value = valueOf(-2)
                                    xAxis = num(1)
                                    yAxis = num(-1.5)
                                },
                            )
                    }
                    markLine {
                        data =
                            listOf(
                                averageMarkLine(),
                                MarkLineDataItem.of(
                                    MarkLineData().apply {
                                        symbol = "none"
                                        x = str("90%")
                                        yAxis = str("max")
                                    },
                                    MarkLineData().apply {
                                        symbol = "circle"
                                        type = "max"
                                        name = "最高点"
                                        label =
                                            LabelOption().apply {
                                                position = "start"
                                                formatter = "Max"
                                            }
                                    },
                                ),
                            )
                    }
                }
            }
        }
