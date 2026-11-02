package demo.json.examples.line

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val areaSimpleJsonCategory = JsonCategory("json/examples/line/AreaSimpleJson.kt")

val areaSimpleJsonExample: JsonExample =
    areaSimpleJsonCategory.example("area-simple") {
            title {
                left = str("center")
                text = "Large Area Chart"
            }
            // The original pins the tooltip at 10% of the height through a `position` callback.
            tooltip { trigger = TooltipTrigger.Axis }
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        restore = ToolboxFeature()
                        saveAsImage = ToolboxSaveAsImageFeature()
                    }
            }
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                data =
                    consecutiveDays(AREA_SIMPLE_YEAR, AREA_SIMPLE_MONTH, AREA_SIMPLE_DAY, AREA_POINT_COUNT) {
                            year,
                            month,
                            day,
                        ->
                        "$year/$month/$day"
                    }
            }
            yAxis {
                type = AxisType.Value
                boundaryGap = BoundaryGap.of(num(0), str("100%"))
            }
            dataZoom {
                type = DataZoomType.Inside
                start = 0.0
                end = 10.0
            }
            dataZoom {
                start = 0.0
                end = 10.0
            }
            series {
                line {
                    name = "Fake Data"
                    symbol = "none"
                    sampling = "lttb"
                    itemStyle { color = "rgb(255, 70, 131)".asColor() }
                    areaStyle {
                        color = verticalGradient("rgb(255, 158, 68)", "rgb(255, 70, 131)")
                    }
                    data = areaSimpleValues()
                }
            }
        }
