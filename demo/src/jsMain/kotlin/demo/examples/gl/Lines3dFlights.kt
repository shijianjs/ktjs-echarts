package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.Payload
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.gl.EChartsGlActions
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.events.Event
import web.events.removeEventListener
import web.window.window
import demo.examples.Category
import demo.examples.lines.FlightsData
import demo.examples.shared.every

private val lines3dFlightsCategory = Category(Groups.GL, "examples/gl/Lines3dFlights.kt")

val lines3dFlightsEntry: CatalogEntry =
    lines3dFlightsCategory.interactive(
            "lines3d-flights",
            "航线",
            "Flights",
            "一万七千条航线按航空公司拆成十一条系列，图例一次只显示一家；每道拖尾沿航线飞行，按任意键暂停。",
            "Seventeen thousand routes split into one series per airline, the legend showing a single " +
                "airline at a time; a trail runs along every route and any key press pauses them.",
        ) { chart, holder ->
            loadJson("data/gl/flights.json").then { json ->
                val flights = json.unsafeCast<FlightsData>()
                val airlines = airlineSeries(flights)
                chart.setOption(flightsByAirlineOption(airlines, flights))
                val toggleEffect: (Event) -> Unit = {
                    airlines.series.indices.forEach { index ->
                        chart.dispatchAction(
                            unsafeJso<Payload> {
                                type = EChartsGlActions.Lines3DToggleEffect
                                seriesIndex = index.toDouble()
                            },
                        )
                    }
                }
                window.listen(KEYDOWN, toggleEffect)
                holder.onUnmount { window.removeEventListener(KEYDOWN, toggleEffect) }
            }
        }
