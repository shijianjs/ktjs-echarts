package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val geoSvgTrafficCategory = Category(Groups.GEO, "examples/geo/GeoSvgTraffic.kt")

val geoSvgTrafficEntry: CatalogEntry =
    geoSvgTrafficCategory.interactive("geo-svg-traffic", "交通（SVG）", "SVG Traffic") { chart ->
            loadText("data/ksia-ext-plan-min.svg").then { svg ->
                ECharts.registerMap(
                    "ksia-ext-plan",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "ksia-ext-plan"
                                    roam = BooleanOrString(true)
                                    layoutCenter = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                                    layoutSize = StringOrDouble("100%")
                                },
                            )
                        series =
                            one(
                                linesSeries {
                                    name = "Route"
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    polyline = true
                                    lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#c46e54"); width = 0.0 }
                                    effect =
                                        unsafeJso<LinesEffectOption> {
                                            show = true
                                            period = 8.0
                                            color = ZRColor("#a10000")
                                            trailLength = 0.0
                                            symbolSize = 12.0
                                            symbol =
                                                "path://M87.1667 3.8333L80.5.5h-60l-6.6667 3.3333L.5 70.5v130l10 10h80l10 -10v-130zM15.5 190.5l15 -20h40l15 20zm75 -65l-15 5v35l15 15zm-80 0l15 5v35l-15 15zm65 0l15 -5v-40l-15 20zm-50 0l-15 -5v-40l15 20zm 65,-55 -15,25 c -15,-5 -35,-5 -50,0 l -15,-25 c 25,-15 55,-15 80,0 z"
                                        }
                                    z = 100.0
                                    typedData =
                                        dataItems(
                                            trafficRoute("#a10000", 100.0, TRAFFIC_A),
                                            trafficRoute("#00067d", 80.0, TRAFFIC_B),
                                            trafficRoute("#997405", 60.0, TRAFFIC_C),
                                        )
                                },
                            )
                    },
                )
                null
            }
        }
