package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category

private val geoSvgMapCategory = Category(Groups.GEO, "examples/geo/GeoSvgMap.kt")

val geoSvgMapEntry: CatalogEntry =
    geoSvgMapCategory.interactive("geo-svg-map", "地图（SVG）", "GEO SVG Map") { chart ->
            loadText("data/Sicily_prehellenic_topographic_map.svg").then { svg ->
                ECharts.registerMap(
                    "sicily",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(sicilyOption())
                chart.onSelectChanged { event: SelectChangedEvent ->
                    if (event.selected.isEmpty()) {
                        chart.dispatchAction(unsafeJso<Payload> { type = "hideTip" })
                        chart.dispatchAction(unsafeJso<Payload> { type = "geoSelect"; geoIndex = 0.0 })
                    } else {
                        val buttonIndex = event.selected[0].dataIndex[0].toInt()
                        val name = SICILY_ROUTE_NAMES[buttonIndex]
                        chart.dispatchAction(
                            unsafeJso<Payload> {
                                type = "geoSelect"
                                geoIndex = 0.0
                                this.name = name
                            },
                        )
                        chart.dispatchAction(
                            unsafeJso<Payload> {
                                type = "showTip"
                                geoIndex = 0.0
                                this.name = name
                            },
                        )
                    }
                }
                null
            }
        }
