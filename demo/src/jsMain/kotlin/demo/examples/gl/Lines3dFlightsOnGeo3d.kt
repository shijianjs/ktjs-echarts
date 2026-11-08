package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.Payload
import io.github.shijianjs.ktjs.echarts.gl.EChartsGlActions
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.events.Event
import web.events.removeEventListener
import web.window.window
import demo.examples.Category
import demo.examples.lines.FlightsData
import demo.examples.shared.every

private val lines3dFlightsOnGeo3dCategory = Category(Groups.GL, "examples/gl/Lines3dFlightsOnGeo3d.kt")

val lines3dFlightsOnGeo3dEntry: CatalogEntry =
    lines3dFlightsOnGeo3dCategory.interactive(
            "lines3d-flights-on-geo3d",
            "航空线 - Geo3D",
            "Flights on Geo3D",
            "扁平的世界地图上，每道拖尾沿航线飞行；按任意键暂停或继续动画。",
            "On the flat map a trail runs along every route; any key press pauses or resumes the animation.",
        ) { chart, holder ->
            registerWorldMap().then {
                loadJson("data/gl/flights.json").then { json ->
                    chart.setOption(flightsOnGeo3DOption(flightRoutes(json.unsafeCast<FlightsData>())))
                }
            }
            val toggleEffect: (Event) -> Unit = {
                chart.dispatchAction(
                    unsafeJso<Payload> {
                        type = EChartsGlActions.Lines3DToggleEffect
                        seriesIndex = 0.0
                    },
                )
            }
            window.listen(KEYDOWN, toggleEffect)
            holder.onUnmount { window.removeEventListener(KEYDOWN, toggleEffect) }
        }
