package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.lines.FlightsData

internal class Lines3dAirlineOnGlobe {
    companion object {
        private val CATEGORY = Category(Groups.GL, "examples/gl/Lines3dAirlineOnGlobe.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.interactive(
                    "lines3d-airline-on-globe",
                    "航空线 - 地球",
                    "Airline on Globe",
                    "一万七千条航线贴着黑底地球，用加色混合叠成一层淡蓝色薄雾。",
                    "Seventeen thousand flight routes cling to a dark globe, additive blending turning them " +
                        "into a faint blue haze.",
                ) { chart ->
                    loadJson("data/gl/flights.json").then { json ->
                        chart.setOption(airlineOnGlobeOption(flightRoutes(json.unsafeCast<FlightsData>())))
                    }
                }
    }
}
