package demo.json.examples.bar

import demo.json.*
import io.github.shijianjs.kmpechartsjson.*
import demo.json.JsonCategory
import demo.json.JsonExample

private val barLargeJsonCategory = JsonCategory("json/examples/bar/BarLargeJson.kt")

val barLargeJsonExample: JsonExample =
    barLargeJsonCategory.example("bar-large") {
            val count = 300
            val seriesValues = largeBarValues(count)
            title {
                text = "$count Data"
                left = num(10)
            }
            toolbox {
                feature =
                    ToolboxFeatureOption().apply {
                        dataZoom = ToolboxFeature()
                        saveAsImage = ToolboxSaveAsImageFeature().apply { pixelRatio = 2.0 }
                    }
            }
            shadowAxisTooltip()
            grid { bottom = num(90) }
            dataZoom { type = DataZoomType.Inside }
            dataZoom { type = DataZoomType.Slider }
            xAxis {
                silent = false
                data = largeBarCategories(count)
                splitLine { show = false }
                splitArea { show = false }
            }
            yAxis { splitArea { show = false } }
            series {
                bar {
                    data = seriesValues.map { value -> valueOf(value) }
                    large = true
                }
            }
        }
