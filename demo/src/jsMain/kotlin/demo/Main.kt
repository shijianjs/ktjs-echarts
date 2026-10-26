package demo

import echarts.ECharts
import echarts.EChartsEvents
import echarts.EChartsInstance
import echarts.ECElementEvent
import echarts.options.EChartsOption
import echarts.options.GridOption
import echarts.options.LegendOption
import echarts.options.SeriesOption
import echarts.options.TitleOption
import echarts.options.TooltipOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.BooleanOrDouble
import echarts.options.StringOrDouble
import echarts.options.barSeries
import echarts.options.echartsOption
import echarts.options.lineSeries
import echarts.options.numbers
import echarts.options.one
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.cssom.ClassName
import web.dom.ElementId
import web.dom.document
import web.events.EventHandler
import web.html.ButtonType
import web.html.HTMLButtonElement
import web.html.HTMLDivElement
import web.html.HTMLElement
import web.html.HtmlTagName
import web.html.button
import web.window.window

private const val PROJECT_NAME = "ktjs-echarts"

/**
 * Entry point of the demo page.
 *
 * The page builds its DOM once with `document.createElement` and then only mutates the nodes it
 * owns: the chart option is updated through `setOption` with the data that changed, and the status
 * line is written with `textContent`. Nothing is re-rendered from a template string.
 */
fun main() {
    val months = mutableListOf("Jan", "Feb", "Mar", "Apr", "May", "Jun")
    val revenue = mutableListOf(42.0, 58.0, 51.0, 74.0, 68.0, 91.0)
    val orders = mutableListOf(28.0, 33.0, 30.0, 45.0, 41.0, 52.0)

    val page = document.createElement(HtmlTagName.div)
    page.className = ClassName("page")

    val heading = document.createElement(HtmlTagName.h1)
    heading.textContent = "$PROJECT_NAME demo"

    val lead = document.createElement(HtmlTagName.p)
    lead.className = ClassName("lead")
    lead.textContent =
        "A chart built entirely with the typed Kotlin/JS wrapper: options, series factories and " +
            "events are regular Kotlin declarations."

    val toolbar = document.createElement(HtmlTagName.div)
    toolbar.className = ClassName("toolbar")

    val randomizeButton = createButton("randomize", "Randomize data")
    val appendButton = createButton("append", "Add month")
    val status = document.createElement(HtmlTagName.span)
    status.className = ClassName("status")
    status.id = ElementId("status")
    status.ariaLive = "polite"
    status.textContent = "Click a chart item to inspect it."

    toolbar.append(randomizeButton, appendButton, status)

    val chartElement = document.createElement(HtmlTagName.div)
    chartElement.className = ClassName("chart")
    chartElement.id = ElementId("chart")
    chartElement.role = "img"
    chartElement.ariaLabel = "Revenue and orders per month"

    page.append(heading, lead, toolbar, chartElement)
    document.body.append(page)

    val chart = ECharts.init(chartElement, null, null)
    chart.setOption(buildOption(months, revenue, orders))

    chart.on(EChartsEvents.Click) { event: ECElementEvent ->
        setStatus(status, "Clicked ${event.seriesName}: ${event.name} = ${event.value}")
    }

    // A chart cannot observe its container, so the page forwards resize events to it.
    window.onresize = EventHandler { chart.resize() }

    randomizeButton.onclick = EventHandler {
        val seed = (months.size * 17 + revenue.size * 31).toDouble()
        for (index in revenue.indices) {
            revenue[index] = 40.0 + (seed + index * 13.0) % 60.0
            orders[index] = 20.0 + (seed + index * 7.0) % 40.0
        }
        chart.setOption(echartsOption<EChartsOption> { series = seriesOptions(revenue, orders) })
        setStatus(status, "Randomized ${revenue.size} months of data.")
    }

    appendButton.onclick = EventHandler {
        months += "M${months.size + 1}"
        revenue += 45.0 + (months.size * 11.0) % 55.0
        orders += 24.0 + (months.size * 5.0) % 35.0
        chart.setOption(
            echartsOption<EChartsOption> {
                xAxis = one(echartsOption<XAxisOption> { type = "category"; data = toJsArray(months) })
                series = seriesOptions(revenue, orders)
            },
        )
        setStatus(status, "Now showing ${months.size} months.")
    }
}

private fun createButton(id: String, label: String): HTMLButtonElement {
    val button = document.createElement(HtmlTagName.button)
    button.className = ClassName("action")
    button.id = ElementId(id)
    button.type = ButtonType.button
    button.textContent = label
    return button
}

private fun setStatus(element: HTMLElement, text: String) {
    element.textContent = text
}

private fun buildOption(
    months: List<String>,
    revenue: List<Double>,
    orders: List<Double>,
): EChartsOption = echartsOption {
    title = one(echartsOption<TitleOption> {
        text = "Monthly revenue"
        subtext = "Revenue and orders, in thousands"
    })
    tooltip = echartsOption<TooltipOption> { trigger = "axis" }
    legend = one(echartsOption<LegendOption> { top = StringOrDouble(6.0) })
    grid = one(echartsOption<GridOption> {
        top = StringOrDouble(96.0)
        left = StringOrDouble(48.0)
        right = StringOrDouble(24.0)
        bottom = StringOrDouble(36.0)
        containLabel = true
    })
    xAxis = one(echartsOption<XAxisOption> {
        type = "category"
        data = toJsArray(months)
    })
    yAxis = one(echartsOption<YAxisOption> { type = "value" })
    series = seriesOptions(revenue, orders)
}

private fun seriesOptions(revenue: List<Double>, orders: List<Double>): Array<SeriesOption> = arrayOf(
    lineSeries {
        name = "Revenue"
        smooth = BooleanOrDouble(true)
        data = numbers(*revenue.toDoubleArray())
    },
    barSeries {
        name = "Orders"
        data = numbers(*orders.toDoubleArray())
    },
)

private fun toJsArray(values: List<String>): Array<JsAny> =
    values.map { value -> value.unsafeCast<JsAny>() }.toTypedArray()
