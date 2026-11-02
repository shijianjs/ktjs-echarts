package demo.examples.scatter

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.themeriver.days

private val scatterAqiColorCategory = Category(Groups.SCATTER, "examples/scatter/ScatterAqiColor.kt")

val scatterAqiColorEntry: CatalogEntry =
    scatterAqiColorCategory.chart(
            "scatter-aqi-color",
            "AQI 气泡图",
            "Scatter Aqi Color",
            "北京、上海、广州三地各 31 天的空气质量；点的大小按 PM2.5 映射，明暗按二氧化硫，" +
                "两个 visualMap 都带可拖动的控制器。",
            "Thirty-one days of air quality in three cities: PM2.5 sets the bubble size and sulphur " +
                "dioxide the lightness, both with draggable visual-map controllers.",
        ) {
            unsafeJso<EChartsOption> {
                color = Array(SCATTER_AQI_COLORS.size) { ZRColor(SCATTER_AQI_COLORS[it]) }
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            top = StringOrDouble(10.0)
                            data = legendNames(*SCATTER_AQI_CITIES)
                            textStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0) }
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble(150.0)
                            top = StringOrDouble("18%")
                            bottom = StringOrDouble("10%")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            backgroundColor = ZRColor("rgba(255,255,255,0.7)")
                            formatter = aqiTooltip()
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            name = "日期"
                            nameGap = 16.0
                            nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(16.0) }
                            max = AxisMinMax(31.0)
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                yAxis = one(aqiValueAxis("AQI指数", end = true))
                visualMap = arrayOf(aqiVisualMap(pm25 = true), aqiVisualMap(pm25 = false))
                series =
                    arrayOf<SeriesOption>(
                        aqiScatterSeries("北京", SCATTER_AQI_BEIJING),
                        aqiScatterSeries("上海", SCATTER_AQI_SHANGHAI),
                        aqiScatterSeries("广州", SCATTER_AQI_GUANGZHOU),
                    )
            }
        }
