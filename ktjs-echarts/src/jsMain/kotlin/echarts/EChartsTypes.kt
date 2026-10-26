package echarts

import echarts.options.EChartsOption
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import kotlin.js.JsAny
import kotlin.js.unsafeCast

/**
 * Options of [ECharts.init].
 */
external interface EChartsInitOpts : JsAny {
    /** Locale name or a locale object registered with [ECharts.registerLocale]. */
    var locale: JsAny?

    /** Renderer: `"canvas"` or `"svg"`. */
    var renderer: String?

    /** Device pixel ratio used by the renderer. */
    var devicePixelRatio: Double?

    /** Whether dirty-rectangle rendering is enabled. */
    var useDirtyRect: Boolean?

    /** Whether a coarse pointer is used for touch devices. */
    var useCoarsePointer: Boolean?

    /** Size of the touch pointer in pixels. */
    var pointerSize: Double?

    /** Whether the instance renders on the server without a DOM element. */
    var ssr: Boolean?

    /** Explicit chart width; `"auto"` uses the container width. */
    var width: StringOrDouble?

    /** Explicit chart height; `"auto"` uses the container height. */
    var height: StringOrDouble?
}

/**
 * Options of [EChartsInstance.setOption].
 */
external interface SetOptionOpts : JsAny {
    /** Whether to replace the current option instead of merging into it. */
    var notMerge: Boolean?

    /** Whether the update is deferred until the next frame. */
    var lazyUpdate: Boolean?

    /** Whether to suppress events triggered by the update. */
    var silent: Boolean?

    /** Component types whose current option is replaced instead of merged. */
    var replaceMerge: StringOrStringArray?

    /** Transition animation between the old and the new option. */
    var transition: JsAny?
}

/**
 * Options of [EChartsInstance.setTheme].
 */
external interface SetThemeOpts : JsAny {
    /** Whether to suppress events triggered by the repaint. */
    var silent: Boolean?
}

/**
 * Options of [EChartsInstance.resize].
 */
external interface ResizeOpts : JsAny {
    /** New width; `"auto"` keeps the container width. */
    var width: StringOrDouble?

    /** New height; `"auto"` keeps the container height. */
    var height: StringOrDouble?

    /** Animation of the resize. */
    var animation: echarts.options.StateAnimationOption?

    /** Whether to suppress events triggered by the resize. */
    var silent: Boolean?
}

/**
 * Options of [EChartsInstance.showLoading].
 */
external interface LoadingOpts : JsAny {
    /** Text shown under the spinner. */
    var text: String?

    /** Text color. */
    var color: JsAny?

    /** Color of the text. */
    var textColor: JsAny?

    /** Color of the mask. */
    var maskColor: JsAny?

    /** z-level of the loading effect. */
    var zlevel: Double?

    /** Font size of the text. */
    var fontSize: Double?

    /** Whether to show the spinner. */
    var showSpinner: Boolean?

    /** Radius of the spinner. */
    var spinnerRadius: Double?

    /** Width of the spinner line. */
    var lineWidth: Double?

    /** Font weight of the text. */
    var fontWeight: JsAny?

    /** Font style of the text. */
    var fontStyle: String?

    /** Font family of the text. */
    var fontFamily: String?
}

/**
 * Options of [EChartsInstance.renderToCanvas].
 */
external interface RenderToCanvasOpts : JsAny {
    /** Background color of the rendered canvas. */
    var backgroundColor: JsAny?

    /** Pixel ratio of the rendered canvas. */
    var pixelRatio: Double?
}

/**
 * Options of [EChartsInstance.renderToSVGString].
 */
external interface RenderToSVGStringOpts : JsAny {
    /** Whether to use `viewBox` instead of fixed `width`/`height`. */
    var useViewBox: Boolean?
}

/**
 * Options of [EChartsInstance.getDataURL].
 */
external interface GetDataURLOpts : JsAny {
    /** Image type: `"png"`, `"jpeg"` or `"svg"`. */
    var type: String?

    /** Pixel ratio of the image. */
    var pixelRatio: Double?

    /** Background color of the image. */
    var backgroundColor: JsAny?

    /** Components excluded from the image. */
    var excludeComponents: Array<String>?
}

/**
 * Options of [EChartsInstance.getConnectedDataURL].
 */
external interface GetConnectedDataURLOpts : GetDataURLOpts {
    /** Background color of the connected charts. */
    var connectedBackgroundColor: JsAny?
}

/**
 * Options of [EChartsInstance.dispatchAction].
 */
external interface DispatchActionOpts : JsAny {
    /** Whether to suppress the events triggered by the action. */
    var silent: Boolean?

    /** Whether to flush the pending update immediately. */
    var flush: Boolean?
}

/**
 * Parameters of [EChartsInstance.appendData].
 */
external interface AppendDataParams : JsAny {
    /** Index of the target series. */
    var seriesIndex: Double?

    /** Data appended to the series. */
    var data: Array<JsAny>?
}

/**
 * Chart behavior flags of the current runtime.
 */
external interface Env : JsAny {
    /** Browser information; `false` outside a browser. */
    val browser: JsAny

    /** Whether the code runs in Node.js. */
    val node: Boolean

    /** Whether the code runs in a WeChat mini program. */
    val wxa: Boolean

    /** Whether the code runs in a Web Worker. */
    val worker: Boolean

    /** Whether the current renderer supports SVG. */
    val svgSupported: Boolean

    /** Whether touch events are supported. */
    val touchEventsSupported: Boolean

    /** Whether pointer events are supported. */
    val pointerEventsSupported: Boolean

    /** Whether the DOM is available. */
    val domSupported: Boolean

    /** Whether CSS transforms are supported. */
    val transformSupported: Boolean

    /** Whether 3D transforms are supported. */
    val transform3dSupported: Boolean

    /** Whether a global `window` exists. */
    val hasGlobalWindow: Boolean
}

/**
 * Runtime dependency versions reported by ECharts.
 */
external interface Dependencies : JsAny {
    /** Version of the bundled zrender. */
    val zrender: String
}

/**
 * Well-known stage priorities used by the registration functions.
 */
external interface Priority : JsAny {
    /** Priorities of processor stages. */
    val PROCESSOR: PriorityProcessor

    /** Priorities of visual stages. */
    val VISUAL: PriorityVisual
}

/**
 * Priorities of processor stages.
 */
external interface PriorityProcessor : JsAny {
    /** Priority of the series filter stage. */
    val SERIES_FILTER: Double

    /** Priority of the axis statistics stage. */
    val AXIS_STATISTICS: Double

    /** Priority of the filter stage. */
    val FILTER: Double

    /** Priority of the statistic stage. */
    val STATISTIC: Double

    /** Priority of the statistics stage. */
    val STATISTICS: Double
}

/**
 * Priorities of visual stages.
 */
external interface PriorityVisual : JsAny {
    /** Priority of the layout stage. */
    val LAYOUT: Double

    /** Priority of the progressive layout stage. */
    val PROGRESSIVE_LAYOUT: Double

    /** Priority of the global visual stage. */
    val GLOBAL: Double

    /** Priority of the chart visual stage. */
    val CHART: Double

    /** Priority of the post-chart layout stage. */
    val POST_CHART_LAYOUT: Double

    /** Priority of the component visual stage. */
    val COMPONENT: Double

    /** Priority of the brush visual stage. */
    val BRUSH: Double

    /** Priority of the chart item visual stage. */
    val CHART_ITEM: Double

    /** Priority of the accessibility stage. */
    val ARIA: Double

    /** Priority of the decal stage. */
    val DECAL: Double
}

/**
 * Identifier of a series or component, used by `convertToPixel`, `getVisual`, ...
 *
 * Create one with `ModelFinder("series")` or `ModelFinder { seriesIndex = 0 }`.
 */
external interface ModelFinder : JsAny

/** Wraps a main type name such as `"series"`, `"xAxis"` or `"geo"`. */
fun ModelFinder(name: String): ModelFinder = name.unsafeCast<ModelFinder>()

/** Wraps a query object. */
fun ModelFinder(query: ModelFinderObject): ModelFinder = query.unsafeCast<ModelFinder>()

/**
 * Query object accepted by [ModelFinder].
 */
external interface ModelFinderObject : JsAny {
    /** Index of the series. */
    var seriesIndex: Double?

    /** Id of the series. */
    var seriesId: String?

    /** Name of the series. */
    var seriesName: String?

    /** Index of the geo component. */
    var geoIndex: Double?

    /** Id of the geo component. */
    var geoId: String?

    /** Name of the geo component. */
    var geoName: String?

    /** Index of the bmap component. */
    var bmapIndex: Double?

    /** Id of the bmap component. */
    var bmapId: String?

    /** Name of the bmap component. */
    var bmapName: String?

    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Id of the x axis. */
    var xAxisId: String?

    /** Name of the x axis. */
    var xAxisName: String?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Id of the y axis. */
    var yAxisId: String?

    /** Name of the y axis. */
    var yAxisName: String?

    /** Index of the grid. */
    var gridIndex: Double?

    /** Id of the grid. */
    var gridId: String?

    /** Name of the grid. */
    var gridName: String?

    /** Index of the matrix coordinate system. */
    var matrixIndex: Double?

    /** Id of the matrix coordinate system. */
    var matrixId: String?

    /** Name of the matrix coordinate system. */
    var matrixName: String?

    /** Index of the data item inside the series. */
    var dataIndex: Double?

    /** Index of the data item inside the current transform. */
    var dataIndexInside: Double?
}

/**
 * Query used to filter event handlers registered with [EChartsInstance.on].
 *
 * Create one with `EventQuery("series")` or `EventQuery { seriesIndex = 0 }`.
 */
external interface EventQuery : JsAny

/** Wraps a main type name. */
fun EventQuery(name: String): EventQuery = name.unsafeCast<EventQuery>()

/** Wraps a query object. */
fun EventQuery(query: ModelFinderObject): EventQuery = query.unsafeCast<EventQuery>()

/**
 * Event payload of element events such as `click`, `mouseover` or `legendselectchanged`.
 */
external interface ECElementEvent : echarts.options.CallbackDataParams {
    /** Event name, e.g. `"click"`. */
    val type: String

    /** Native DOM event that produced the ECharts event. */
    val event: JsAny

    /** Extra event data, when the event provides one. */
    val detail: JsAny
}

/**
 * Payload of an action dispatched with [EChartsInstance.dispatchAction] or handled by a custom
 * action registered with [ECharts.registerAction].
 */
external interface ECActionEvent : JsAny {
    /** Action type. */
    val type: String

    /** Main type of the target component, e.g. `"series"`. */
    val componentType: String?

    /** Index of the target component. */
    val componentIndex: Double?

    /** Index of the target series. */
    val seriesIndex: Double?

    /** Whether the action is not propagated to connected charts. */
    val escapeConnect: Boolean?

    /** Payloads of a batch action. */
    val batch: Array<JsAny>?
}

/**
 * Payload of the `rendered` event.
 */
external interface RenderedEventParam : JsAny {
    /** Milliseconds elapsed since the last update. */
    val elapsedTime: Double
}

/**
 * One entry of [SelectChangedEvent.selected].
 */
external interface SelectedInfo : JsAny {
    /** Index of the series. */
    val seriesIndex: Double

    /** Data type of the selected items. */
    val dataType: String

    /** Indices of the selected data items. */
    val dataIndex: Array<Double>
}

/**
 * Payload of the `selectchanged` event.
 */
external interface SelectChangedEvent : JsAny {
    /** Always `"selectchanged"`. */
    val type: String

    /** Whether the event was caused by a click. */
    val isFromClick: Boolean

    /** Action that produced the change. */
    val fromAction: String

    /** Payload of the action that produced the change. */
    val fromActionPayload: Payload

    /** Selected items per series. */
    val selected: Array<SelectedInfo>
}

/**
 * One break of an [AxisBreakChangedEvent].
 */
external interface AxisBreakChangedEventBreak : JsAny {
    /** Whether the break is expanded. */
    val isExpanded: Boolean

    /** State before the change. */
    val old: JsAny
}

/**
 * Payload of the `axisbreakchanged` event.
 */
external interface AxisBreakChangedEvent : JsAny {
    /** Always `"axisbreakchanged"`. */
    val type: String

    /** Action that produced the change: `"expandAxisBreak"`, `"collapseAxisBreak"` or `"toggleAxisBreak"`. */
    val fromAction: String

    /** Payload of the action that produced the change. */
    val fromActionPayload: Payload

    /** Breaks affected by the change. */
    val breaks: Array<AxisBreakChangedEventBreak>
}

/**
 * Payload of an action dispatched with [EChartsInstance.dispatchAction] or received by an action
 * handler.
 */
external interface Payload : JsAny {
    /** Action type, e.g. `"highlight"`, `"showTip"` or `"select"`. */
    var type: String?

    /** Whether the action is not propagated to connected charts. */
    var escapeConnect: Boolean?

    /** Payloads of a batch action. */
    var batch: Array<Payload>?

    /** Series ids excluded from the action. */
    var excludeSeriesId: StringOrStringArray?

    /** Animation of the action. */
    var animation: PayloadAnimationPart?

    /** Index of the target series. */
    var seriesIndex: Double?

    /** Id of the target series. */
    var seriesId: String?

    /** Name of the target series. */
    var seriesName: String?

    /** Index of the target data item. */
    var dataIndex: Double?

    /** Index of the target data item inside the current transform. */
    var dataIndexInside: Double?

    /** Index of the target geo component. */
    var geoIndex: Double?

    /** Index of the target x axis. */
    var xAxisIndex: Double?

    /** Index of the target y axis. */
    var yAxisIndex: Double?

    /** Name of the target data item. */
    var name: String?

    /** Value of the target data item. */
    var value: JsAny?

    /** Whether the highlight action does not blur other elements. */
    var notBlur: Boolean?

    /** Whether the action is silent. */
    var silent: Boolean?
}

/**
 * Animation part of an action payload.
 */
external interface PayloadAnimationPart : JsAny {
    /** Duration in milliseconds. */
    var duration: Double?

    /** Easing name. */
    var easing: String?

    /** Delay in milliseconds. */
    var delay: Double?
}

/**
 * Description of a custom action registered with [ECharts.registerAction].
 *
 * Create one with `ActionInfo("myAction")` and set the optional fields.
 */
external interface ActionInfo : JsAny {
    /** Action type passed to `dispatchAction`. */
    var type: String?

    /** Event name emitted after the action; defaults to [type]. */
    var event: String?

    /** Update method used to refresh the chart after the action. */
    var update: String?

    /** Handler invoked with the action payload. */
    var action: JsAny?

    /** Callback that builds the public event content. */
    var refineEvent: JsAny?

    /** Whether to publish the non-refined event as well. */
    var publishNonRefinedEvent: Boolean?

    /** Whether the action payload is used as a component query. */
    var componentQuery: Boolean?
}

/**
 * Builds an [ActionInfo] with the given action type.
 */
fun ActionInfo(type: String): ActionInfo = echarts.options.echartsOption { this.type = type }

/**
 * Options object accepted by [EChartsInstance.setOption] when the typed model is not used.
 */
typealias RawOption = JsAny

/**
 * Options object accepted by [ECharts.init] when a theme object is given instead of a theme name.
 */
typealias ThemeOption = EChartsOption
