package demo.ui

import io.github.shijianjs.ktjs.dom.webcomponent.CustomElementAdapter
import io.github.shijianjs.ktjs.dom.webcomponent.CustomElementReferenceAdapter
import io.github.shijianjs.ktjs.echarts.ECharts
import io.github.shijianjs.ktjs.echarts.EChartsInstance
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.js
import web.dom.document
import web.events.Event
import web.events.EventType
import web.events.addEventListener
import web.events.removeEventListener
import web.mutation.MutationObserver
import web.mutation.MutationObserverInit
import web.window.window

/** Dark theme registered by ECharts itself; used when the document runs in dark mode. */
private const val DARK_THEME = "dark"

/** Height the chart takes when an example does not ask for its own. */
private const val DEFAULT_HEIGHT = "clamp(360px, 62vh, 680px)"

/** `resize` as a typed event. */
private val RESIZE: EventType<Event> = EventType("resize")

/**
 * `<demo-chart>`: one ECharts instance, mounted when the element enters the document and disposed when
 * it leaves.
 *
 * That lifecycle is why this is an element rather than a plain `<div>`: a chart cannot measure itself
 * while it is detached, and the router swaps the whole content column on every route change — an
 * instance kept alive after its page left the document keeps its canvas, its timers and its animation
 * loop. Connecting and disconnecting is exactly the moment to mount and dispose, and the browser calls
 * it for us.
 *
 * The chart re-initialises when the document's colour scheme changes: ECharts picks its theme at init
 * time, so a chart created in light mode would keep `#333` text on a dark background forever. ECharts
 * ships the `dark` theme itself; the element only has to pass its name and rebuild the instance.
 */
class ChartElement : CustomElementAdapter() {
    /** Explicit height; `null` keeps [DEFAULT_HEIGHT]. */
    var height: String? = null

    /** Builds the option once the element is in the document; set by [chart] / [interactiveChart]. */
    internal var setup: ((EChartsInstance, ChartHolder) -> Unit)? = null

    private var instance: EChartsInstance? = null

    private var holder: ChartHolder? = null

    private var themeObserver: MutationObserver? = null

    /** Window resize handler, kept in a field so it can be unregistered again. */
    private val onWindowResize: (Event) -> Unit = {
        instance?.resize()
        holder?.resize()
    }

    override fun connectedCallback() {
        // A custom element is `display: inline` by default, which gives the container no box for
        // ECharts to measure. Both declarations are about this element's own box, so they stay here.
        style.display = "block"
        style.width = "100%"
        style.height = height ?: DEFAULT_HEIGHT
        style.minHeight = "240px"
        mount()
        watchTheme()
        window.addEventListener(RESIZE, onWindowResize)
    }

    override fun disconnectedCallback() {
        window.removeEventListener(RESIZE, onWindowResize)
        themeObserver?.disconnect()
        themeObserver = null
        unmount()
    }

    /** Runs a new setup against a fresh instance; used when a page switches the chart's input. */
    fun reload(setup: (EChartsInstance, ChartHolder) -> Unit) {
        this.setup = setup
        unmount()
        mount()
    }

    private fun mount() {
        if (instance != null) return
        val setup = setup ?: return
        val chart = ECharts.init(this, if (prefersDark()) DARK_THEME else null, null)
        instance = chart
        holder = ChartHolder().also { setup(chart, it) }
    }

    private fun unmount() {
        holder?.release()
        holder = null
        instance?.dispose()
        instance = null
    }

    /**
     * Re-initialises the chart when the document's `theme` attribute changes.
     *
     * The attribute is what Lumo's `light-dark()` colours follow and what the frame's theme menu
     * writes, so it is the one signal a chart can watch without knowing anything about the shell.
     */
    private fun watchTheme() {
        if (themeObserver != null) return
        val observer = MutationObserver { _, _ ->
            unmount()
            mount()
        }
        observer.observe(
            document.documentElement,
            unsafeJso<MutationObserverInit> {
                attributes = true
                attributeFilter = arrayOf("theme")
            },
        )
        themeObserver = observer
    }

    companion object : CustomElementReferenceAdapter<ChartElement>(ChartElement::class.js, "demo-chart")
}

/**
 * Chart theme of the moment.
 *
 * `theme="dark"` is explicit dark, `theme="light-dark"` follows the browser, and no attribute means
 * light — the same three states the shell's theme menu writes. `prefers-color-scheme` has no typed
 * counterpart in kotlin-browser, so that single query stays one JavaScript expression.
 */
private fun prefersDark(): Boolean =
    when (document.documentElement.getAttribute("theme")) {
        "dark" -> true
        "light-dark" -> js("window.matchMedia('(prefers-color-scheme: dark)').matches") as Boolean
        else -> false
    }

/**
 * Handles one example owns while its chart is mounted: timers, resize callbacks and cleanup.
 *
 * The element creates one per mount cycle and drops it on unmount, so an example that animates with an
 * interval or listens on `window` cannot outlive the page it belongs to.
 */

/** Window timers have no typed counterpart in kotlin-browser; each call is one JavaScript expression. */
internal fun startInterval(handler: () -> Unit, delayMillis: Int): Int =
    js("(fn, ms) => window.setInterval(fn, ms)")(handler, delayMillis) as Int

internal fun startTimeout(handler: () -> Unit, delayMillis: Int): Int =
    js("(fn, ms) => window.setTimeout(fn, ms)")(handler, delayMillis) as Int

internal fun cancelInterval(handle: Int) {
    js("(h) => window.clearInterval(h)")(handle)
}

internal fun cancelTimeout(handle: Int) {
    js("(h) => window.clearTimeout(h)")(handle)
}

/** Feeds the chart the option [option] builds. */
fun optionInput(option: () -> EChartsOption): (EChartsInstance, ChartHolder) -> Unit =
    { chart, _ -> chart.setOption(option()) }

/** Feeds the chart the already-serialized option, i.e. the same object a server would return. */
fun jsonInput(json: String): (EChartsInstance, ChartHolder) -> Unit =
    { chart, _ -> chart.setOption(parseOption(json)) }

/** A chart that applies the option [option] builds. */
fun chart(option: () -> EChartsOption, height: String? = null): ChartElement =
    ChartElement().apply {
        this.height = height
        setup = optionInput(option)
    }

/** A chart that drives the instance itself: animation, timeline, user interaction, ... */
fun interactiveChart(
    height: String? = null,
    setup: (EChartsInstance, ChartHolder) -> Unit,
): ChartElement =
    ChartElement().apply {
        this.height = height
        this.setup = setup
    }

/** A chart fed a serialized option. */
fun jsonChart(json: String, height: String? = null): ChartElement =
    interactiveChart(height, jsonInput(json))

/** Parses a serialized option into the plain object `setOption` accepts. */
private fun parseOption(json: String): JsAny = js("(text) => JSON.parse(text)")(json) as JsAny
