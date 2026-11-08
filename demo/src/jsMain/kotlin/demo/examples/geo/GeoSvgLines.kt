package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRLineTypes
import demo.examples.Category
import demo.examples.gauge.center

private val geoSvgLinesCategory = Category(Groups.GEO, "examples/geo/GeoSvgLines.kt")

val geoSvgLinesEntry: CatalogEntry =
    geoSvgLinesCategory.interactive("geo-svg-lines", "路径图（SVG 地图）", "GEO SVG Lines") { chart ->
            loadText("data/MacOdrum-LV5-floorplan-web.svg").then { svg ->
                ECharts.registerMap(
                    "MacOdrum-LV5-floorplan-web",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        title =
                            one(
                                unsafeJso<TitleOption> {
                                    text = "Visit Route"
                                    left = StringOrDouble("center")
                                    bottom = StringOrDouble(10.0)
                                },
                            )
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "MacOdrum-LV5-floorplan-web"
                                    roam = BooleanOrString(true)
                                    emphasis =
                                        unsafeJso<EmphasisOption> {
                                            itemStyle = unsafeJso<ItemStyleOption> {}
                                            label = unsafeJso<LabelOption> { show = false }
                                        }
                                },
                            )
                        series =
                            one(
                                linesSeries {
                                    name = "Route"
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = false } }
                                    polyline = true
                                    lineStyle =
                                        unsafeJso<LineStyleOption> {
                                            color = ZRColor("#c46e54")
                                            width = 5.0
                                            opacity = 1.0
                                            type = ZRLineTypes.dotted
                                        }
                                    typedData =
                                        dataItems(
                                            unsafeJso<LinesDataItemOption> {
                                                coords =
                                                    arrayOf(
                                                        arrayOf(110.6189462165178, 456.64349563895087),
                                                        arrayOf(124.10988522879458, 450.8570048730469),
                                                        arrayOf(123.9272226116071, 389.9520693708147),
                                                        arrayOf(61.58708083147317, 386.87942320312504),
                                                        arrayOf(61.58708083147317, 72.8954315876116),
                                                        arrayOf(258.29514854771196, 72.8954315876116),
                                                        arrayOf(260.75457021484374, 336.8559607533482),
                                                        arrayOf(280.5277985253906, 410.2406672084263),
                                                        arrayOf(275.948185765904, 528.0254369698661),
                                                        arrayOf(111.06907909458701, 552.795792593471),
                                                        arrayOf(118.87138231445309, 701.365737015904),
                                                        arrayOf(221.36468155133926, 758.7870354617745),
                                                        arrayOf(307.86195445452006, 742.164737297712),
                                                        arrayOf(366.8489324762834, 560.9895157073103),
                                                        arrayOf(492.8750778390066, 560.9895157073103),
                                                        arrayOf(492.8750778390066, 827.9639780566406),
                                                        arrayOf(294.9255269587053, 827.9639780566406),
                                                        arrayOf(282.79803391043527, 868.2476088113839),
                                                    )
                                            },
                                        )
                                },
                            )
                    },
                )
                null
            }
        }
