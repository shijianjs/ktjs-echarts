package demo.examples.parallel

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.matrix.dimension
import demo.examples.shared.every
import demo.examples.shared.legendNames
import demo.examples.themeriver.days

private val parallelAqiCategory = Category(Groups.PARALLEL, "examples/parallel/ParallelAqi.kt")

val parallelAqiEntry: CatalogEntry =
    parallelAqiCategory.chart(
            "parallel-aqi",
            "AQI 分布（平行坐标）",
            "Parallel Aqi",
            "北京、上海、广州三地的 31 天空气质量，八个维度里第七维是分级的「等级」类目轴；" +
                "visualMap 按 PM2.5 给每条折线上色。",
            "Thirty-one days of air quality in Beijing, Shanghai and Guangzhou; the eighth dimension " +
                "is a category axis of grades and a visual map colours every polyline by PM2.5.",
        ) {
            unsafeJso<EChartsOption> {
                backgroundColor = ZRColor("#333")
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            bottom = StringOrDouble(30.0)
                            data = legendNames("Beijing", "Shanghai", "Guangzhou")
                            itemGap = 20.0
                            textStyle =
                                unsafeJso<LabelOption> {
                                    color = "#fff"
                                    fontSize = StringOrDouble(14.0)
                                }
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            padding = DoubleOrDoubleArray(10.0)
                            backgroundColor = ZRColor("#222")
                            borderColor = ZRColor("#777")
                            borderWidth = 1.0
                        },
                    )
                parallelAxis =
                    arrayOf(
                        aqiAxis(0, "日期") {
                            inverse = true
                            max = AxisMinMax(AQI_LAST_DAY)
                            nameLocation = AxisNameLocations.start
                        },
                        aqiAxis(1, "AQI"),
                        aqiAxis(2, "PM2.5"),
                        aqiAxis(3, "PM10"),
                        aqiAxis(4, " CO"),
                        aqiAxis(5, "NO2"),
                        aqiAxis(6, "SO2"),
                        aqiAxis(7, "等级") {
                            type = AxisTypes.category
                            data = strings(*AQI_GRADES)
                        },
                    )
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            show = true
                            min = 0.0
                            max = 150.0
                            dimension = 2.0
                            inRange =
                                unsafeJso<VisualOptionUnit> {
                                    color =
                                        StringOrStringArray(
                                            arrayOf("#50a3ba", "#eac736", "#d94e5d"),
                                        )
                                }
                        },
                    )
                parallel =
                    one(
                        unsafeJso<ParallelOption> {
                            left = StringOrDouble("5%")
                            right = StringOrDouble("18%")
                            bottom = StringOrDouble(100.0)
                            parallelAxisDefault = aqiAxisDefaults()
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        parallelSeries {
                            name = "Beijing"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = aqiItems(AQI_BEIJING, AQI_BEIJING_GRADES)
                        },
                        parallelSeries {
                            name = "Shanghai"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = aqiItems(AQI_SHANGHAI, AQI_SHANGHAI_GRADES)
                        },
                        parallelSeries {
                            name = "Guangzhou"
                            lineStyle = unsafeJso<LineStyleOption> { width = 1.0; opacity = 0.5 }
                            typedData = aqiItems(AQI_GUANGZHOU, AQI_GUANGZHOU_GRADES)
                        },
                    )
            }
        }
