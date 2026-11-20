package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.lines.FlightsData
import demo.examples.shared.every

internal class Lines3dFlightsGl {
    companion object {
        private val CATEGORY = Category(Groups.GL, "examples/gl/Lines3dFlightsGl.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive(
                    "lines3d-flights-gl",
                    "航线 - 俯视",
                    "Flights GL",
                    "从正上方俯瞰几乎压平的黑色世界地图，所有航线同时飞行，拖尾以恒定速度前进。",
                    "A nearly flat black world map seen from straight above, with every route flying at once " +
                        "and the trails advancing at a constant speed.",
                ) { chart ->
                    registerWorldMap().then {
                        loadJson("data/gl/flights.json").then { json ->
                            chart.setOption(
                                flightsGlOption(flightRoutes(json.unsafeCast<FlightsData>())),
                            )
                        }
                    }
                }
    }
}
