package demo.json.examples.doc

import demo.json.JsonCategory
import demo.json.JsonExample
import io.github.shijianjs.kmpechartsjson.*
import demo.json.*

private val docExampleLineStackTiledJsonCategory = JsonCategory("json/examples/doc/DocExampleLineStackTiledJson.kt")

val docExampleLineStackTiledJsonExample: JsonExample =
    docExampleLineStackTiledJsonCategory.example("doc-example/line-stack-tiled") {
            title {
                text = "某楼盘销售情况"
                subtext = "纯属虚构"
            }
            tooltip { trigger = TooltipTrigger.Axis }
            legend { entries("意向", "预购", "成交") }
            toolbox {
                show = true
                feature =
                    ToolboxFeatureOption().apply {
                        magicType =
                            ToolboxMagicTypeFeature().apply {
                                show = true
                                type = listOf(SeriesType("stack"), SeriesType("tiled"))
                            }
                        saveAsImage = ToolboxSaveAsImageFeature().apply { show = true }
                    }
            }
            xAxis {
                type = AxisType.Category
                boundaryGap = BoundaryGap.of(false)
                categories("周一", "周二", "周三", "周四", "周五", "周六", "周日")
            }
            yAxis { type = AxisType.Value }
            series {
                line {
                    name = "成交"
                    smooth = BooleanOrNumber.of(true)
                    values(10, 12, 21, 54, 260, 830, 710)
                }
                line {
                    name = "预购"
                    smooth = BooleanOrNumber.of(true)
                    values(30, 182, 434, 791, 390, 30, 10)
                }
                line {
                    name = "意向"
                    smooth = BooleanOrNumber.of(true)
                    values(1320, 1132, 601, 234, 120, 90, 20)
                }
            }
        }
