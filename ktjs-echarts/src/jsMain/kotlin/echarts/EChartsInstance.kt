package echarts

import echarts.options.EChartsOption
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.html.HTMLCanvasElement
import web.html.HTMLElement

/**
 * Chart instance returned by [ECharts.init].
 *
 * The methods mirror the upstream `EChartsType` class. Options are typed as [EChartsOption]; use
 * [setOptionRaw] for option fragments that are not modelled by the wrapper.
 */
external interface EChartsInstance {
    /**
     * Unique chart id assigned by ECharts.
     */
    val id: String

    /**
     * Group id used by [ECharts.connect] / [ECharts.disconnect]. Assigning a value joins the chart
     * to that group.
     */
    var group: String

    /**
     * Returns the container element.
     */
    fun getDom(): HTMLElement

    /**
     * Returns [id].
     */
    fun getId(): String

    /**
     * Returns the underlying zrender instance, or `null` in server-side rendering mode.
     *
     * The `zrender` package is not a dependency of this wrapper, so the instance is untyped; use
     * [ECharts.zrender] for its runtime API.
     */
    fun getZr(): JsAny?

    /**
     * Whether the chart runs in server-side rendering mode (`init(null, ..., ssr = true)`).
     */
    fun isSSR(): Boolean

    /**
     * Merges an option into the chart.
     */
    fun setOption(option: EChartsOption, opts: SetOptionOpts? = definedExternally)

    /**
     * Merges an option into the chart using the legacy positional arguments.
     *
     * @param notMerge whether to replace the current option instead of merging.
     */
    fun setOption(option: EChartsOption, notMerge: Boolean)

    /**
     * Merges an option into the chart using the legacy positional arguments.
     *
     * @param notMerge whether to replace the current option instead of merging.
     * @param lazyUpdate whether to defer the update to the next frame.
     */
    fun setOption(option: EChartsOption, notMerge: Boolean, lazyUpdate: Boolean)

    /**
     * Merges an option that is not covered by the typed [EChartsOption] model.
     */
    fun setOption(option: JsAny, opts: SetOptionOpts? = definedExternally)

    /**
     * Applies a theme by name and repaints the chart.
     */
    fun setTheme(theme: String, opts: SetThemeOpts? = definedExternally)

    /**
     * Applies an inline theme object and repaints the chart.
     */
    fun setTheme(theme: ThemeOption, opts: SetThemeOpts? = definedExternally)

    /**
     * Returns the normalized option currently held by the chart. The result is the full option
     * including defaults, so it is intentionally untyped.
     */
    fun getOption(): JsAny

    /**
     * Returns the chart width in CSS pixels.
     */
    fun getWidth(): Double

    /**
     * Returns the chart height in CSS pixels.
     */
    fun getHeight(): Double

    /**
     * Returns the device pixel ratio used by the renderer.
     */
    fun getDevicePixelRatio(): Double

    /**
     * Returns the canvas that contains the chart.
     *
     * @deprecated upstream in favour of [renderToCanvas].
     */
    @Deprecated("Use renderToCanvas instead.", ReplaceWith("renderToCanvas()"))
    fun getRenderedCanvas(opts: JsAny? = definedExternally): HTMLCanvasElement

    /**
     * Renders the chart to a new canvas.
     */
    fun renderToCanvas(opts: RenderToCanvasOpts? = definedExternally): HTMLCanvasElement

    /**
     * Renders the chart to an SVG string. Requires the SVG renderer.
     */
    fun renderToSVGString(opts: RenderToSVGStringOpts? = definedExternally): String

    /**
     * Returns an SVG data URL. Requires the SVG renderer.
     */
    fun getSvgDataURL(): String

    /**
     * Returns a data URL for the chart image.
     */
    fun getDataURL(opts: GetDataURLOpts? = definedExternally): String

    /**
     * Returns a data URL for a group of connected charts.
     */
    fun getConnectedDataURL(opts: GetConnectedDataURLOpts? = definedExternally): String

    /**
     * Converts a logical coordinate to a pixel coordinate.
     *
     * @param finder `ModelFinder("series")`, or a query object such as `{ seriesIndex: 0 }`.
     */
    fun convertToPixel(finder: ModelFinder, value: JsAny): JsAny

    /**
     * Converts a logical coordinate to a layout coordinate.
     */
    fun convertToLayout(finder: ModelFinder, value: JsAny, opt: JsAny? = definedExternally): JsAny

    /**
     * Converts a pixel coordinate to a logical coordinate.
     */
    fun convertFromPixel(finder: ModelFinder, value: JsAny): JsAny

    /**
     * Whether the given pixel point is inside the finder's coordinate system or component.
     */
    fun containPixel(finder: ModelFinder, value: Array<Double>): Boolean

    /**
     * Returns a visual value (`"color"`, `"symbol"`, `"symbolSize"`, ...) of a series or data item.
     */
    fun getVisual(finder: ModelFinder, visualType: String): JsAny

    /**
     * Whether the chart has been disposed.
     */
    fun isDisposed(): Boolean

    /**
     * Removes all components and series without disposing the instance.
     */
    fun clear()

    /**
     * Disposes the instance and releases its resources.
     */
    fun dispose()

    /**
     * Resizes the chart.
     */
    fun resize(opts: ResizeOpts? = definedExternally)

    /**
     * Shows a loading effect.
     *
     * @param name registered loading effect name; the default effect when `null`.
     * @param cfg effect options.
     */
    fun showLoading(name: String?, cfg: LoadingOpts? = definedExternally)

    /**
     * Shows the default loading effect with the given options.
     */
    fun showLoading(cfg: LoadingOpts)

    /**
     * Hides the loading effect.
     */
    fun hideLoading()

    /**
     * Builds an action payload from a dispatched event.
     */
    fun makeActionFromEvent(event: ECActionEvent): Payload

    /**
     * Dispatches an action.
     */
    fun dispatchAction(payload: Payload, opt: DispatchActionOpts? = definedExternally)

    /**
     * Dispatches an action.
     *
     * @param silent whether to suppress the events triggered by the action.
     */
    fun dispatchAction(payload: Payload, silent: Boolean)

    /**
     * Re-runs the label layout.
     */
    fun updateLabelLayout()

    /**
     * Appends data to a series that supports streaming (`lines`, `graph`, ...).
     */
    fun appendData(params: AppendDataParams)

    /**
     * Registers an event handler.
     *
     * @param eventName event name, see [EChartsEvents].
     * @param handler receives the packed element event.
     */
    fun on(eventName: String, handler: (ECElementEvent) -> Unit): EChartsInstance

    /**
     * Registers an event handler filtered by a query object such as `{ seriesIndex: 0 }`.
     */
    fun on(eventName: String, query: EventQuery, handler: (ECElementEvent) -> Unit): EChartsInstance

    /**
     * Removes every handler of an event.
     */
    fun off(eventName: String): EChartsInstance

    /**
     * Removes one handler of an event.
     */
    fun off(eventName: String, handler: (ECElementEvent) -> Unit): EChartsInstance
}

/**
 * Merges an option that is not covered by the typed [EChartsOption] model.
 */
fun EChartsInstance.setOptionRaw(option: JsAny, opts: SetOptionOpts? = null) {
    setOption(option, opts)
}

/**
 * Shows the default loading effect.
 */
fun EChartsInstance.showLoading() {
    showLoading(null, null)
}

/**
 * Shows a named loading effect without options.
 */
fun EChartsInstance.showLoading(name: String) {
    showLoading(name, null)
}

/**
 * Registers a handler for the `selectchanged` event.
 */
fun EChartsInstance.onSelectChanged(handler: (SelectChangedEvent) -> Unit): EChartsInstance =
    on("selectchanged") { event: ECElementEvent -> handler(event.unsafeCast<SelectChangedEvent>()) }

/**
 * Registers a handler for the `axisbreakchanged` event.
 */
fun EChartsInstance.onAxisBreakChanged(handler: (AxisBreakChangedEvent) -> Unit): EChartsInstance =
    on("axisbreakchanged") { event: ECElementEvent -> handler(event.unsafeCast<AxisBreakChangedEvent>()) }

/**
 * Registers a handler for the `finished` event.
 */
fun EChartsInstance.onFinished(handler: () -> Unit): EChartsInstance =
    on("finished") { handler() }

/**
 * Registers a handler for the `rendered` event.
 */
fun EChartsInstance.onRendered(handler: (RenderedEventParam) -> Unit): EChartsInstance =
    on("rendered") { event: ECElementEvent -> handler(event.unsafeCast<RenderedEventParam>()) }
