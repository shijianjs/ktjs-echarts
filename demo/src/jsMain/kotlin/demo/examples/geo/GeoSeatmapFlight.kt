package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category

private val geoSeatmapFlightCategory = Category(Groups.GEO, "examples/geo/GeoSeatmapFlight.kt")

val geoSeatmapFlightEntry: CatalogEntry =
    geoSeatmapFlightCategory.interactive("geo-seatmap-flight", "航班选座（SVG）", "Flight Seatmap") { chart ->
            loadText("data/flight-seats.svg").then { svg ->
                ECharts.registerMap(
                    "flight-seats",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "flight-seats"
                                    roam = BooleanOrString(true)
                                    selectedMode = SelectionMode("multiple")
                                    layoutCenter = arrayOf(StringOrDouble("50%"), StringOrDouble("50%"))
                                    layoutSize = StringOrDouble("95%")
                                    tooltip = unsafeJso<TooltipOption> { show = true }
                                    itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#fff") }
                                    regions = takenRegions()
                                },
                            )
                    },
                )
                null
            }
        }
