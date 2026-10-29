package echarts

import echarts.options.AxisBreakIdentifier
import echarts.options.DimensionLoose
import echarts.options.EChartsOption
import echarts.options.ModelFinderIndexQuery
import echarts.options.OptionDataItem
import echarts.options.StringOrDouble
import echarts.options.StringOrStringArray
import js.array.ArrayLike
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.JsName
import kotlin.js.unsafeCast
import web.cssom.FontStyle
import zrender.RendererType
import zrender.ZRColor
import zrender.ZRElementEvent
import zrender.ZRFontWeight

/**
 * Options of [ECharts.init].
 */
external interface EChartsInitOpts {
    /** Locale name or a locale object registered with [ECharts.registerLocale]. */
    var locale: StringOrLocaleOption?

    /** Renderer used by the chart. */
    var renderer: RendererType?

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
 * Text used by the built-in components in a specific language.
 *
 * Pass an instance to [ECharts.init] through [EChartsInitOpts.locale] or register it by name with
 * [ECharts.registerLocale]. Every section is optional: missing entries are filled from the
 * default locale.
 */
external interface LocaleOption {
    /** Names of months and weekdays. */
    var time: LocaleTimeOption?

    /** Legend texts. */
    var legend: LocaleLegendOption?

    /** Toolbox texts. */
    var toolbox: LocaleToolboxOption?

    /** Series type names used by the toolbox and by `aria` descriptions. */
    var series: LocaleSeriesOption?

    /** Accessibility texts. */
    var aria: LocaleAriaOption?
}

/** Month and weekday names. */
external interface LocaleTimeOption {
    /** Full month names, January first. */
    var month: Array<String>?

    /** Abbreviated month names, January first. */
    var monthAbbr: Array<String>?

    /** Full weekday names, Sunday first. */
    var dayOfWeek: Array<String>?

    /** Abbreviated weekday names, Sunday first. */
    var dayOfWeekAbbr: Array<String>?
}

/** Legend texts. */
external interface LocaleLegendOption {
    /** Texts of the legend selector. */
    var selector: LocaleLegendSelectorOption?
}

/** Texts of the legend selector buttons. */
external interface LocaleLegendSelectorOption {
    /** "Select all" button text. */
    var all: String?

    /** "Invert selection" button text. */
    var inverse: String?
}

/** Toolbox texts. */
external interface LocaleToolboxOption {
    /** Brush feature texts. */
    var brush: LocaleToolboxBrushOption?

    /** Data view feature texts. */
    var dataView: LocaleToolboxDataViewOption?

    /** Data zoom feature texts. */
    var dataZoom: LocaleToolboxDataZoomOption?

    /** Magic type feature texts. */
    var magicType: LocaleToolboxMagicTypeOption?

    /** Restore feature texts. */
    var restore: LocaleToolboxRestoreOption?

    /** Save-as-image feature texts. */
    var saveAsImage: LocaleToolboxSaveAsImageOption?
}

/** Brush feature texts. */
external interface LocaleToolboxBrushOption {
    /** Titles of the brush modes. */
    var title: LocaleToolboxBrushTitleOption?
}

/** Titles of the brush modes. */
external interface LocaleToolboxBrushTitleOption {
    /** Rectangular selection. */
    var rect: String?

    /** Lasso selection. */
    var polygon: String?

    /** Horizontal selection. */
    var lineX: String?

    /** Vertical selection. */
    var lineY: String?

    /** Keep selections. */
    var keep: String?

    /** Clear selections. */
    var clear: String?
}

/** Data view feature texts. */
external interface LocaleToolboxDataViewOption {
    /** Title of the feature. */
    var title: String?

    /** Labels of the data view dialog: title, close and refresh. */
    var lang: Array<String>?
}

/** Data zoom feature texts. */
external interface LocaleToolboxDataZoomOption {
    /** Titles of the zoom and reset buttons. */
    var title: LocaleToolboxDataZoomTitleOption?
}

/** Titles of the data zoom buttons. */
external interface LocaleToolboxDataZoomTitleOption {
    /** Zoom button title. */
    var zoom: String?

    /** Reset button title. */
    var back: String?
}

/** Magic type feature texts. */
external interface LocaleToolboxMagicTypeOption {
    /** Titles of the target series types. */
    var title: LocaleToolboxMagicTypeTitleOption?
}

/** Titles of the magic type buttons. */
external interface LocaleToolboxMagicTypeTitleOption {
    /** Line chart. */
    var line: String?

    /** Bar chart. */
    var bar: String?

    /** Stacked chart. */
    var stack: String?

    /** Tiled chart. */
    var tiled: String?
}

/** Restore feature texts. */
external interface LocaleToolboxRestoreOption {
    /** Title of the feature. */
    var title: String?
}

/** Save-as-image feature texts. */
external interface LocaleToolboxSaveAsImageOption {
    /** Title of the feature. */
    var title: String?

    /** Labels of the save dialog: tooltip and message. */
    var lang: Array<String>?
}

/** Series texts. */
external interface LocaleSeriesOption {
    /** Display names of the series types. */
    var typeNames: LocaleSeriesTypeNamesOption?
}

/** Display names of the series types. */
external interface LocaleSeriesTypeNamesOption {
    /** Pie series. */
    var pie: String?

    /** Bar series. */
    var bar: String?

    /** Line series. */
    var line: String?

    /** Scatter series. */
    var scatter: String?

    /** Effect scatter series. */
    var effectScatter: String?

    /** Radar series. */
    var radar: String?

    /** Tree series. */
    var tree: String?

    /** Treemap series. */
    var treemap: String?

    /** Boxplot series. */
    var boxplot: String?

    /** Candlestick series. */
    var candlestick: String?

    /** K-line series. */
    var k: String?

    /** Heatmap series. */
    var heatmap: String?

    /** Map series. */
    var map: String?

    /** Parallel series. */
    var parallel: String?

    /** Lines series. */
    var lines: String?

    /** Graph series. */
    var graph: String?

    /** Sankey series. */
    var sankey: String?

    /** Funnel series. */
    var funnel: String?

    /** Gauge series. */
    var gauge: String?

    /** Pictorial bar series. */
    var pictorialBar: String?

    /** Theme river series. */
    var themeRiver: String?

    /** Sunburst series. */
    var sunburst: String?

    /** Custom series. */
    var custom: String?

    /** Chart series. */
    var chart: String?
}

/** Accessibility texts. */
external interface LocaleAriaOption {
    /** General description texts. */
    var general: LocaleAriaGeneralOption?

    /** Description texts of series data. */
    var series: LocaleAriaSeriesOption?

    /** Description texts of data values. */
    var data: LocaleAriaDataOption?
}

/** General accessibility texts. */
external interface LocaleAriaGeneralOption {
    /** Template used when the chart has a title. */
    var withTitle: String?

    /** Template used when the chart has no title. */
    var withoutTitle: String?
}

/** Accessibility texts of series data. */
external interface LocaleAriaSeriesOption {
    /** Template used for a single series. */
    var single: LocaleAriaSeriesSingleOption?

    /** Template used for several series. */
    var multiple: LocaleAriaSeriesMultipleOption?
}

/** Accessibility texts of a single series. */
external interface LocaleAriaSeriesSingleOption {
    /** Prefix before the series description. */
    var prefix: String?

    /** Template used when the series has a name. */
    var withName: String?

    /** Template used when the series has no name. */
    var withoutName: String?
}

/** Accessibility texts of several series. */
external interface LocaleAriaSeriesMultipleOption {
    /** Prefix before the series description. */
    var prefix: String?

    /** Template used when the series has a name. */
    var withName: String?

    /** Template used when the series has no name. */
    var withoutName: String?

    /** Separators between the series. */
    var separator: LocaleAriaSeparatorOption?
}

/** Separators between accessibility descriptions. */
external interface LocaleAriaSeparatorOption {
    /** Separator between entries. */
    var middle: String?

    /** Separator at the end. */
    var end: String?
}

/** Accessibility texts of data values. */
external interface LocaleAriaDataOption {
    /** Template used when every data item is described. */
    var allData: String?

    /** Template used when only some data items are described. */
    var partialData: String?

    /** Template used when the data item has a name. */
    var withName: String?

    /** Template used when the data item has no name. */
    var withoutName: String?

    /** Separators between the data descriptions. */
    var separator: LocaleAriaSeparatorOption?
}

/**
 * `string | LocaleOption`, accepted by [EChartsInitOpts.locale] and [ECharts.registerLocale].
 */
external interface StringOrLocaleOption

/** Wraps the name of a registered locale. */
fun StringOrLocaleOption(value: String): StringOrLocaleOption =
    value.unsafeCast<StringOrLocaleOption>()

/** Wraps an inline locale object. */
fun StringOrLocaleOption(value: LocaleOption): StringOrLocaleOption =
    value.unsafeCast<StringOrLocaleOption>()

/**
 * Options of [EChartsInstance.setOption].
 */
external interface SetOptionOpts {
    /** Whether to replace the current option instead of merging into it. */
    var notMerge: Boolean?

    /** Whether the update is deferred until the next frame. */
    var lazyUpdate: Boolean?

    /** Whether to suppress events triggered by the update. */
    var silent: Boolean?

    /** Component types whose current option is replaced instead of merged. */
    var replaceMerge: StringOrStringArray?

    /** Transition animation between the old and the new option. */
    var transition: SetOptionTransition?
}

/**
 * Options of [EChartsInstance.setTheme].
 */
external interface SetThemeOpts {
    /** Whether to suppress events triggered by the repaint. */
    var silent: Boolean?
}

/**
 * Options of [EChartsInstance.resize].
 */
external interface ResizeOpts {
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
external interface LoadingOpts {
    /** Text shown under the spinner. */
    var text: String?

    /** Color of the spinner. */
    var color: ZRColor?

    /** Color of the text. */
    var textColor: ZRColor?

    /** Color of the mask. */
    var maskColor: ZRColor?

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
    var fontWeight: ZRFontWeight?

    /** Font style of the text, a CSS `font-style` keyword. */
    var fontStyle: FontStyle?

    /** Font family of the text. */
    var fontFamily: String?
}

/**
 * Options of [EChartsInstance.renderToCanvas].
 */
external interface RenderToCanvasOpts {
    /** Background color of the rendered canvas. */
    var backgroundColor: ZRColor?

    /** Pixel ratio of the rendered canvas. */
    var pixelRatio: Double?
}

/**
 * Options of [EChartsInstance.renderToSVGString].
 */
external interface RenderToSVGStringOpts {
    /** Whether to use `viewBox` instead of fixed `width`/`height`. */
    var useViewBox: Boolean?
}

/**
 * Options of [EChartsInstance.getDataURL].
 */
external interface GetDataURLOpts {
    /** Image format of the data URL. */
    var type: ImageType?

    /** Pixel ratio of the image. */
    var pixelRatio: Double?

    /** Background color of the image. */
    var backgroundColor: ZRColor?

    /** Components excluded from the image. */
    var excludeComponents: Array<String>?
}

/**
 * Options of [EChartsInstance.getConnectedDataURL].
 */
external interface GetConnectedDataURLOpts : GetDataURLOpts {
    /** Background color of the connected charts. */
    var connectedBackgroundColor: ZRColor?
}

/**
 * Options of [EChartsInstance.dispatchAction].
 */
external interface DispatchActionOpts {
    /** Whether to suppress the events triggered by the action. */
    var silent: Boolean?

    /** Whether to flush the pending update immediately. */
    var flush: Boolean?
}

/**
 * Parameters of [EChartsInstance.appendData].
 */
external interface AppendDataParams {
    /** Index of the target series. */
    var seriesIndex: Double?

    /** Data appended to the series, one item per entry. */
    var data: Array<OptionDataItem?>?

    /**
     * Flat view of [data] for the large-data series.
     *
     * Upstream declares the appended data as `any`; a `Float32Array` / `Float64Array` of consecutive
     * rows is what the large-data examples append chunk by chunk, and the series has to set
     * `dimensions` for it. Same JavaScript property as [data], so assign exactly one of them.
     */
    @JsName("data")
    var typedFlatData: ArrayLike<JsAny>?
}

/**
 * Browser flags detected by zrender.
 */
external interface BrowserEnv {
    /** Whether the browser is Firefox. */
    val firefox: Boolean

    /** Whether the browser is Internet Explorer. */
    val ie: Boolean

    /** Whether the browser is the legacy Edge. */
    val edge: Boolean

    /** Whether the browser is the Chromium-based Edge. */
    val newEdge: Boolean

    /** Whether the code runs in the WeChat browser. */
    val weChat: Boolean

    /** Browser version. */
    val version: StringOrDouble
}

/**
 * Chart behavior flags of the current runtime.
 */
external interface Env {
    /** Browser information. */
    val browser: BrowserEnv

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
external interface Dependencies {
    /** Version of the bundled zrender. */
    val zrender: String
}

/**
 * Well-known stage priorities used by the registration functions.
 */
external interface Priority {
    /** Priorities of processor stages. */
    val PROCESSOR: PriorityProcessor

    /** Priorities of visual stages. */
    val VISUAL: PriorityVisual
}

/**
 * Priorities of processor stages.
 */
external interface PriorityProcessor {
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
external interface PriorityVisual {
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
external interface ModelFinder

/** Wraps a main type name such as `"series"`, `"xAxis"` or `"geo"`. */
fun ModelFinder(name: String): ModelFinder = name.unsafeCast<ModelFinder>()

/** Wraps a query object. */
fun ModelFinder(query: ModelFinderObject): ModelFinder = query.unsafeCast<ModelFinder>()

/**
 * Query object accepted by [ModelFinder].
 */
external interface ModelFinderObject {
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
external interface EventQuery

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

    /** Native zrender element event that produced the ECharts event. */
    val event: ZRElementEvent?
}

/**
 * Arbitrary event data published by a custom action.
 *
 * Upstream declares an index signature (`[key: string]: any`); read extra fields through a cast.
 */
external interface ECEventData

/**
 * Result of [ActionRefineEvent]: the content of the event published to the user.
 */
external interface ActionRefineEventResult {
    /** Fields published on the event. */
    var eventContent: ECEventData
}

/**
 * Payload of an action dispatched with [EChartsInstance.dispatchAction] or handled by a custom
 * action registered with [ECharts.registerAction].
 */
external interface ECActionEvent {
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
    val batch: Array<ECEventData>?
}

/**
 * Payload of the `rendered` event.
 */
external interface RenderedEventParam {
    /** Milliseconds elapsed since the last update. */
    val elapsedTime: Double
}

/**
 * One entry of [SelectChangedEvent.selected].
 */
external interface SelectedInfo {
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
external interface SelectChangedEvent {
    /** Always `"selectchanged"`. */
    val type: String

    /** Whether the event was caused by a click. */
    val isFromClick: Boolean

    /** Action that produced the change; see [SelectChangedFromActions]. */
    val fromAction: SelectChangedFromAction

    /** Payload of the action that produced the change. */
    val fromActionPayload: Payload

    /** Selected items per series. */
    val selected: Array<SelectedInfo>
}

/**
 * One break of an [AxisBreakChangedEvent].
 */
external interface AxisBreakChangedEventBreak : AxisBreakIdentifier {
    /** Whether the break is expanded after the change. */
    val isExpanded: Boolean

    /** State of the break before the change. */
    val old: AxisBreakChangedEventOldState

    /** Index of the x axis the break belongs to. */
    val xAxisIndex: ModelFinderIndexQuery?

    /** Index of the y axis the break belongs to. */
    val yAxisIndex: ModelFinderIndexQuery?

    /** Index of the single axis the break belongs to. */
    val singleAxisIndex: ModelFinderIndexQuery?
}

/**
 * State of a break before an [AxisBreakChangedEvent].
 */
external interface AxisBreakChangedEventOldState {
    /** Whether the break was expanded. */
    val isExpanded: Boolean
}

/**
 * Payload of the `axisbreakchanged` event.
 */
external interface AxisBreakChangedEvent {
    /** Always `"axisbreakchanged"`. */
    val type: String

    /** Action that produced the change; see [AxisBreakActions]. */
    val fromAction: AxisBreakAction

    /** Payload of the action that produced the change. */
    val fromActionPayload: Payload

    /** Breaks affected by the change. */
    val breaks: Array<AxisBreakChangedEventBreak>
}

/**
 * Payload of an action dispatched with [EChartsInstance.dispatchAction] or received by an action
 * handler.
 */
external interface Payload {
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
external interface PayloadAnimationPart {
    /** Duration in milliseconds. */
    var duration: Double?

    /** Easing name. */
    var easing: String?

    /** Delay in milliseconds. */
    var delay: Double?
}

/**
 * Payload of the `highlight` action.
 *
 * Upstream narrows `type` to `"highlight"` and adds `notBlur`; the wrapper keeps the shared
 * [Payload] members, so a plain [Payload] and this type are interchangeable.
 */
external interface HighlightPayload : Payload

/**
 * Payload of the `downplay` action.
 *
 * See [HighlightPayload] for why the upstream `type` narrowing has no Kotlin counterpart.
 */
external interface DownplayPayload : Payload

/**
 * Payload of the `selectchanged` event.
 *
 * Upstream keeps this type for backward compatibility and points at [SelectChangedEvent].
 */
external interface SelectChangedPayload : Payload {
    /** Whether the change was caused by a click. */
    var isFromClick: Boolean?

    /** Action that produced the change: `"select"`, `"unselect"` or `"toggleSelected"`. */
    var fromAction: SelectChangedFromAction?

    /** Payload of the action that produced the change. */
    var fromActionPayload: Payload?

    /** Selected items per series. */
    var selected: Array<SelectedInfo>?
}

/**
 * Fields shared by the axis break action payloads: the axis to address and the breaks to apply.
 *
 * `xAxisIndex` / `yAxisIndex` come from [Payload] and are typed as a single index; the upstream
 * `ModelFinderIndexQuery` form (including `"all"`) is available as [typedXAxisIndex] and
 * [typedYAxisIndex], which map back to the same JavaScript property.
 */
external interface BaseAxisBreakPayload : Payload {
    /**
     * Index of the x axis, including `"all"` and index arrays.
     *
     * `@JsName("xAxisIndex")`: same JavaScript property as [Payload.xAxisIndex], see
     * `TYPE-MAPPING.md` §12.
     */
    @JsName("xAxisIndex")
    var typedXAxisIndex: ModelFinderIndexQuery?

    /**
     * Index of the y axis, including `"all"` and index arrays.
     *
     * `@JsName("yAxisIndex")`: same JavaScript property as [Payload.yAxisIndex].
     */
    @JsName("yAxisIndex")
    var typedYAxisIndex: ModelFinderIndexQuery?

    /** Id of the x axis, takes precedence over the index. */
    var xAxisId: String?

    /** Name of the x axis, takes precedence over the index. */
    var xAxisName: String?

    /** Id of the y axis, takes precedence over the index. */
    var yAxisId: String?

    /** Name of the y axis, takes precedence over the index. */
    var yAxisName: String?

    /** Index of the single axis. */
    var singleAxisIndex: ModelFinderIndexQuery?

    /** Id of the single axis, takes precedence over the index. */
    var singleAxisId: String?

    /** Name of the single axis, takes precedence over the index. */
    var singleAxisName: String?

    /** Breaks the action applies to; required by the upstream action. */
    var breaks: Array<AxisBreakIdentifier>
}

/**
 * Payload of the `expandAxisBreak` action.
 */
external interface ExpandAxisBreakPayload : BaseAxisBreakPayload

/**
 * Payload of the `collapseAxisBreak` action.
 */
external interface CollapseAxisBreakPayload : BaseAxisBreakPayload

/**
 * Payload of the `toggleAxisBreak` action.
 */
external interface ToggleAxisBreakPayload : BaseAxisBreakPayload

/**
 * Description of a custom action registered with [ECharts.registerAction].
 *
 * Create one with `ActionInfo("myAction")` and set the optional fields.
 */
external interface ActionInfo {
    /** Action type passed to `dispatchAction`. */
    var type: String

    /** Event name emitted after the action; defaults to [type]. */
    var event: String?

    /** Update method used to refresh the chart after the action; see [ECUpdateMethodNames]. */
    var update: String?

    /** Handler invoked with the action payload. */
    var action: ActionHandler?

    /** Callback that builds the public event content. */
    var refineEvent: ActionRefineEvent?

    /** Whether to publish the non-refined event as well. */
    var publishNonRefinedEvent: Boolean?

    /** Whether the action payload is used as a component query. */
    var componentQuery: Boolean?
}

/**
 * Builds an [ActionInfo] with the given action type.
 */
fun ActionInfo(type: String): ActionInfo = unsafeJso { this.type = type }

/**
 * Registration API handed to an [EChartsExtensionInstaller].
 *
 * The installer receives the registry object that ECharts uses to add charts, components,
 * renderers and features; the wrapper keeps it opaque because upstream does not export a public
 * type for it.
 */
external interface EChartsInstallRegisters

/**
 * Installs a chart, component, renderer or feature into ECharts.
 *
 * The installers exported by `echarts/charts`, `echarts/components`, `echarts/renderers` and
 * `echarts/features` already have this type; pass them to [ECharts.use].
 */
typealias EChartsExtensionInstaller = (EChartsInstallRegisters) -> Unit

/**
 * Class (constructor) produced by the `extend*` helpers of `echarts/core`.
 *
 * Upstream types them as `ComponentModelConstructor`, `SeriesModelConstructor`, ...; the wrapper
 * keeps the class opaque because Kotlin cannot express `new ()`. A JavaScript class is a function,
 * so ECharts also accepts the value as an [EChartsExtensionInstaller].
 */
external interface EChartsClassConstructor

/**
 * Object form of an extension, mirroring upstream `EChartsExtension`.
 */
external interface EChartsExtension {
    /** Installs the extension. */
    val install: EChartsExtensionInstaller
}

/**
 * Finder of the series that a `setOption` transition applies to.
 */
external interface TransitionSeriesFinder {
    /** Indices of the series. */
    var seriesIndex: ModelFinderIndexQuery?

    /** Id of the series. */
    var seriesId: String?

    /** Dimension used to match the data items. */
    var dimension: DimensionLoose
}

/**
 * One side of a transition rule: a single finder or an array of finders.
 */
external interface TransitionSeriesFinderList

/** Wraps a single finder. */
fun TransitionSeriesFinderList(value: TransitionSeriesFinder): TransitionSeriesFinderList =
    value.unsafeCast<TransitionSeriesFinderList>()

/** Wraps several finders. */
fun TransitionSeriesFinderList(values: Array<TransitionSeriesFinder>): TransitionSeriesFinderList =
    values.unsafeCast<TransitionSeriesFinderList>()

/**
 * One transition rule of [SetOptionOpts.transition].
 */
external interface SetOptionTransitionItem {
    /** Series the transition starts from. */
    var from: TransitionSeriesFinderList?

    /** Series the transition ends at. */
    var to: TransitionSeriesFinderList?
}

/**
 * Transition animation between the old and the new option: one rule or an array of rules.
 */
external interface SetOptionTransition

/** Wraps a single rule. */
fun SetOptionTransition(item: SetOptionTransitionItem): SetOptionTransition =
    item.unsafeCast<SetOptionTransition>()

/** Wraps several rules. */
fun SetOptionTransition(items: Array<SetOptionTransitionItem>): SetOptionTransition =
    items.unsafeCast<SetOptionTransition>()

/**
 * Options object accepted by [ECharts.init] when a theme object is given instead of a theme name.
 */
typealias ThemeOption = EChartsOption
