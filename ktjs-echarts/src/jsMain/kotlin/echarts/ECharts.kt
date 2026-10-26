package echarts

import echarts.options.DimensionDefinitionLoose
import kotlin.js.JsAny
import web.canvas.CanvasRenderingContext2D
import web.html.HTMLCanvasElement
import web.html.HTMLElement
import zrender.MatrixNamespace
import zrender.UtilNamespace
import zrender.VectorNamespace
import zrender.ZRElement
import zrender.ZRenderNamespace
import zrender.ZrUtilNamespace

/**
 * Named exports of the `echarts` npm package (the full bundle).
 *
 * The package is ESM-only, so the declarations live in a `@JsModule` object: every member is
 * compiled to a named import, which is what `echarts/index.js` provides.
 *
 * Use [EChartsCore] instead when the application imports the modular entry point and registers
 * charts and components manually for a smaller bundle.
 */
@JsModule("echarts")
external object ECharts {
    /**
     * ECharts version, e.g. `"6.1.0"`.
     */
    val version: String

    /**
     * Runtime environment flags (`browser`, `node`, `worker`, `svgSupported`, ...).
     */
    val env: Env

    /**
     * Runtime dependency versions, currently `{ zrender: "..." }`.
     */
    val dependencies: Dependencies

    /**
     * Well-known priorities for [registerProcessor], [registerLayout] and [registerVisual].
     */
    val PRIORITY: Priority

    /**
     * Creates a chart instance.
     *
     * @param dom container element; pass `null` only together with `opts.ssr = true`.
     * @param theme registered theme name.
     * @param opts renderer, locale, device pixel ratio and other initialization options.
     */
    fun init(
        dom: HTMLElement? = definedExternally,
        theme: String? = definedExternally,
        opts: EChartsInitOpts? = definedExternally,
    ): EChartsInstance

    /**
     * Creates a chart instance with an inline theme object instead of a registered theme name.
     */
    fun init(
        dom: HTMLElement?,
        theme: ThemeOption,
        opts: EChartsInitOpts? = definedExternally,
    ): EChartsInstance

    /**
     * Connects several charts that share [groupId] so that tooltip, dataZoom, legend and
     * highlight actions are synchronized. Returns the group id.
     */
    fun connect(groupId: String): String

    /**
     * Connects the given charts and returns the generated group id.
     */
    fun connect(charts: Array<EChartsInstance>): String

    /**
     * Disconnects a group.
     */
    fun disconnect(groupId: String)

    /**
     * Alias of [disconnect]; kept because the upstream package exports both names.
     */
    fun disConnect(groupId: String)

    /**
     * Disposes a chart instance.
     */
    fun dispose(chart: EChartsInstance)

    /**
     * Disposes the chart mounted on the given element, if any.
     */
    fun dispose(dom: HTMLElement)

    /**
     * Disposes the chart with the given id, if any.
     */
    fun dispose(id: String)

    /**
     * Returns the chart mounted on [dom], or `null` when the element has no chart.
     */
    fun getInstanceByDom(dom: HTMLElement): EChartsInstance?

    /**
     * Returns the chart with the given id, or `null` when it does not exist.
     */
    fun getInstanceById(id: String): EChartsInstance?

    /**
     * Registers a theme. A theme is an option object whose keys are the default values to apply.
     */
    fun registerTheme(name: String, theme: ThemeOption)

    /**
     * Registers a locale object, for example the object exported by `echarts/i18n/langEN`.
     */
    fun registerLocale(name: String, locale: LocaleOption)

    /**
     * Registers a map (GeoJSON or SVG).
     *
     * @param mapName name used by `series.map` / `geo.map`.
     * @param map GeoJSON object, `{ geoJSON, specialAreas }`, or `{ svg }`.
     * @param specialAreas optional area offsets for legacy GeoJSON maps.
     */
    fun registerMap(mapName: String, map: MapInput, specialAreas: GeoSpecialAreas? = definedExternally)

    /**
     * Returns the map registered under [mapName], or `null` when it does not exist.
     */
    fun getMap(mapName: String): JsAny?

    /**
     * Installs ECharts extensions, for example the installers exported by `echarts/charts`.
     */
    fun use(extension: EChartsExtensionInstaller)

    /**
     * Installs several ECharts extensions.
     */
    fun use(extensions: Array<EChartsExtensionInstaller>)

    /**
     * Registers a custom action.
     *
     * @param type action type passed to `dispatchAction`.
     * @param eventType event name emitted after the action.
     * @param action handler invoked with the action payload.
     */
    fun registerAction(type: String, eventType: String, action: (Payload) -> Unit)

    /**
     * Registers a custom action whose event name equals its type.
     */
    fun registerAction(type: String, action: (Payload) -> Unit)

    /**
     * Registers a custom action described by an [ActionInfo] object.
     */
    fun registerAction(actionInfo: ActionInfo, action: ((Payload) -> Unit)? = definedExternally)

    /**
     * Registers a coordinate system.
     */
    fun registerCoordinateSystem(type: String, creator: CoordinateSystemCreator)

    /**
     * Returns the dimensions of a registered coordinate system.
     */
    fun getCoordinateSystemDimensions(type: String): Array<DimensionDefinitionLoose>

    /**
     * Registers a custom series renderer.
     */
    fun registerCustomSeries(seriesType: String, renderItem: CustomSeriesRenderItem)

    /**
     * Registers a layout stage handler.
     */
    fun registerLayout(handler: StageHandlerOverallReset)

    /**
     * Registers a layout stage handler.
     */
    fun registerLayout(handler: StageHandler)

    /**
     * Registers a layout stage handler with an explicit [priority].
     */
    fun registerLayout(priority: Double, handler: StageHandlerOverallReset)

    /**
     * Registers a layout stage handler with an explicit [priority].
     */
    fun registerLayout(priority: Double, handler: StageHandler)

    /**
     * Registers a visual encoding stage handler.
     */
    fun registerVisual(handler: StageHandlerOverallReset)

    /**
     * Registers a visual encoding stage handler.
     */
    fun registerVisual(handler: StageHandler)

    /**
     * Registers a visual encoding stage handler with an explicit [priority].
     */
    fun registerVisual(priority: Double, handler: StageHandlerOverallReset)

    /**
     * Registers a visual encoding stage handler with an explicit [priority].
     */
    fun registerVisual(priority: Double, handler: StageHandler)

    /**
     * Registers a loading effect.
     */
    fun registerLoading(name: String, creator: LoadingEffectCreator)

    /**
     * Registers a callback that runs after a chart is initialized.
     */
    fun registerPostInit(callback: (EChartsInstance) -> Unit)

    /**
     * Registers a callback that runs after every update.
     */
    fun registerPostUpdate(callback: PostUpdater)

    /**
     * Registers an option preprocessor.
     */
    fun registerPreprocessor(preprocessor: OptionPreprocessor)

    /**
     * Registers a data processor.
     */
    fun registerProcessor(processor: StageHandlerOverallReset)

    /**
     * Registers a data processor.
     */
    fun registerProcessor(processor: StageHandler)

    /**
     * Registers a data processor with an explicit [priority].
     */
    fun registerProcessor(priority: Double, processor: StageHandlerOverallReset)

    /**
     * Registers a data processor with an explicit [priority].
     */
    fun registerProcessor(priority: Double, processor: StageHandler)

    /**
     * Registers an external data transform used by `dataset.transform`.
     */
    fun registerTransform(transform: ExternalTransform)

    /**
     * Registers a callback for an update lifecycle event (`afterinit`, `afterupdate`,
     * `series:beforeupdate`, ...).
     */
    fun registerUpdateLifecycle(name: String, callback: (JsAny) -> Unit)

    /**
     * Overrides the canvas factory used by zrender (SSR and Node.js environments).
     */
    fun setCanvasCreator(creator: () -> HTMLCanvasElement)

    /**
     * Overrides platform APIs such as `createCanvas` and `measureText` (SSR and Node.js).
     */
    fun setPlatformAPI(api: PlatformAPI)

    /**
     * Parses GeoJSON into ECharts regions.
     */
    fun parseGeoJSON(geoJson: GeoJSON, nameProperty: String): Array<GeoJSONRegion>

    /**
     * Alias of [parseGeoJSON]; kept because the upstream package exports both names.
     */
    fun parseGeoJson(geoJson: GeoJSON, nameProperty: String): Array<GeoJSONRegion>

    /**
     * Wraps [fn] so that it is called at most once per [delay] milliseconds.
     *
     * @param debounce when `true`, only the last call in the window is executed.
     */
    fun throttle(fn: JsAny, delay: Double? = definedExternally, debounce: Boolean? = definedExternally): JsAny

    /**
     * The `echarts.format` namespace (string and number formatting helpers).
     */
    val format: FormatNamespace

    /**
     * The `echarts.time` namespace (date parsing and formatting helpers).
     */
    val time: TimeNamespace

    /**
     * The `echarts.number` namespace (numeric helpers).
     */
    val number: NumberNamespace

    /**
     * The `echarts.helper` namespace (model and data helpers for extension authors).
     */
    val helper: HelperNamespace

    /**
     * The `echarts.graphic` namespace (shape and transform helpers).
     */
    val graphic: GraphicNamespace

    /**
     * The `echarts.color` namespace (color parsing and interpolation helpers).
     */
    val color: ColorNamespace

    /**
     * The `echarts.matrix` namespace (2D matrix helpers).
     */
    val matrix: MatrixNamespace

    /**
     * The `echarts.vector` namespace (2D vector helpers).
     */
    val vector: VectorNamespace

    /**
     * The `echarts.util` namespace (zrender utilities).
     */
    val util: UtilNamespace

    /**
     * The `echarts.zrUtil` namespace (zrender utilities with their original names).
     */
    val zrUtil: ZrUtilNamespace

    /**
     * The `echarts.zrender` namespace (the bundled zrender library).
     */
    val zrender: ZRenderNamespace

    /**
     * The `echarts.dataTool` namespace. Empty in the npm build; the tools live in
     * `echarts/extension/dataTool`.
     */
    val dataTool: DataToolNamespace

    /**
     * Draws a single displayable element on a canvas context.
     */
    fun innerDrawElementOnCanvas(context: CanvasRenderingContext2D?, element: ZRElement)
}

/**
 * Named exports of the modular `echarts/core` entry point.
 *
 * Use this object together with the installers from [EChartsCharts], [EChartsComponents],
 * [EChartsRenderers] and [EChartsFeatures] to keep the bundle small:
 *
 * ```kotlin
 * ECharts.use(arrayOf(EChartsCharts.LineChart, EChartsComponents.GridComponent, EChartsRenderers.CanvasRenderer))
 * val chart = EChartsCore.init(dom)
 * ```
 *
 * The API surface is identical to [ECharts]; see that object for the documentation of each member.
 */
@JsModule("echarts/core")
external object EChartsCore {
    /** ECharts version. */
    val version: String

    /** Runtime environment flags. */
    val env: Env

    /** Runtime dependency versions. */
    val dependencies: Dependencies

    /** Well-known stage priorities. */
    val PRIORITY: Priority

    /** Creates a chart instance. See [ECharts.init]. */
    fun init(
        dom: HTMLElement? = definedExternally,
        theme: String? = definedExternally,
        opts: EChartsInitOpts? = definedExternally,
    ): EChartsInstance

    /** Creates a chart instance with an inline theme object. See [ECharts.init]. */
    fun init(dom: HTMLElement?, theme: ThemeOption, opts: EChartsInitOpts? = definedExternally): EChartsInstance

    /** Connects charts that share a group id. See [ECharts.connect]. */
    fun connect(groupId: String): String

    /** Connects the given charts. See [ECharts.connect]. */
    fun connect(charts: Array<EChartsInstance>): String

    /** Disconnects a group. See [ECharts.disconnect]. */
    fun disconnect(groupId: String)

    /** Alias of [disconnect]. */
    fun disConnect(groupId: String)

    /** Disposes a chart instance. See [ECharts.dispose]. */
    fun dispose(chart: EChartsInstance)

    /** Disposes the chart mounted on an element. See [ECharts.dispose]. */
    fun dispose(dom: HTMLElement)

    /** Disposes the chart with the given id. See [ECharts.dispose]. */
    fun dispose(id: String)

    /** Returns the chart mounted on [dom]. See [ECharts.getInstanceByDom]. */
    fun getInstanceByDom(dom: HTMLElement): EChartsInstance?

    /** Returns the chart with the given id. See [ECharts.getInstanceById]. */
    fun getInstanceById(id: String): EChartsInstance?

    /** Registers a theme. See [ECharts.registerTheme]. */
    fun registerTheme(name: String, theme: ThemeOption)

    /** Registers a locale object. See [ECharts.registerLocale]. */
    fun registerLocale(name: String, locale: LocaleOption)

    /** Registers a map. See [ECharts.registerMap]. */
    fun registerMap(mapName: String, map: MapInput, specialAreas: GeoSpecialAreas? = definedExternally)

    /** Returns a registered map. See [ECharts.getMap]. */
    fun getMap(mapName: String): JsAny?

    /** Installs ECharts extensions. See [ECharts.use]. */
    fun use(extension: EChartsExtensionInstaller)

    /** Installs several ECharts extensions. See [ECharts.use]. */
    fun use(extensions: Array<EChartsExtensionInstaller>)

    /** Registers a custom action. See [ECharts.registerAction]. */
    fun registerAction(type: String, eventType: String, action: (Payload) -> Unit)

    /** Registers a custom action. See [ECharts.registerAction]. */
    fun registerAction(type: String, action: (Payload) -> Unit)

    /** Registers a custom action described by an [ActionInfo]. See [ECharts.registerAction]. */
    fun registerAction(actionInfo: ActionInfo, action: ((Payload) -> Unit)? = definedExternally)

    /** Registers a coordinate system. See [ECharts.registerCoordinateSystem]. */
    fun registerCoordinateSystem(type: String, creator: CoordinateSystemCreator)

    /** Returns the dimensions of a coordinate system. See [ECharts.getCoordinateSystemDimensions]. */
    fun getCoordinateSystemDimensions(type: String): Array<DimensionDefinitionLoose>

    /** Registers a custom series renderer. See [ECharts.registerCustomSeries]. */
    fun registerCustomSeries(seriesType: String, renderItem: CustomSeriesRenderItem)

    /** Registers a layout stage handler. See [ECharts.registerLayout]. */
    fun registerLayout(handler: StageHandlerOverallReset)

    /** Registers a layout stage handler. See [ECharts.registerLayout]. */
    fun registerLayout(priority: Double, handler: (JsAny) -> Unit)

    /** Registers a visual encoding stage handler. See [ECharts.registerVisual]. */
    fun registerVisual(handler: StageHandlerOverallReset)

    /** Registers a visual encoding stage handler. See [ECharts.registerVisual]. */
    fun registerVisual(priority: Double, handler: (JsAny) -> Unit)

    /** Registers a loading effect. See [ECharts.registerLoading]. */
    fun registerLoading(name: String, creator: LoadingEffectCreator)

    /** Registers a post-init callback. See [ECharts.registerPostInit]. */
    fun registerPostInit(callback: (EChartsInstance) -> Unit)

    /** Registers a post-update callback. See [ECharts.registerPostUpdate]. */
    fun registerPostUpdate(callback: PostUpdater)

    /** Registers an option preprocessor. See [ECharts.registerPreprocessor]. */
    fun registerPreprocessor(preprocessor: OptionPreprocessor)

    /** Registers a data processor. See [ECharts.registerProcessor]. */
    fun registerProcessor(processor: (JsAny) -> Unit)

    /** Registers a data processor. See [ECharts.registerProcessor]. */
    fun registerProcessor(priority: Double, processor: (JsAny) -> Unit)

    /** Registers a data transform. See [ECharts.registerTransform]. */
    fun registerTransform(transform: ExternalTransform)

    /** Registers an update lifecycle callback. See [ECharts.registerUpdateLifecycle]. */
    fun registerUpdateLifecycle(name: String, callback: (JsAny) -> Unit)

    /** Overrides the canvas factory. See [ECharts.setCanvasCreator]. */
    fun setCanvasCreator(creator: () -> HTMLCanvasElement)

    /** Overrides platform APIs. See [ECharts.setPlatformAPI]. */
    fun setPlatformAPI(api: PlatformAPI)

    /** Parses GeoJSON. See [ECharts.parseGeoJSON]. */
    fun parseGeoJSON(geoJson: GeoJSON, nameProperty: String): Array<GeoJSONRegion>

    /** Alias of [parseGeoJSON]. See [ECharts.parseGeoJson]. */
    fun parseGeoJson(geoJson: GeoJSON, nameProperty: String): Array<GeoJSONRegion>

    /** Throttles a function. See [ECharts.throttle]. */
    fun throttle(fn: JsAny, delay: Double? = definedExternally, debounce: Boolean? = definedExternally): JsAny

    /** The `format` namespace. See [ECharts.format]. */
    val format: FormatNamespace

    /** The `time` namespace. See [ECharts.time]. */
    val time: TimeNamespace

    /** The `number` namespace. See [ECharts.number]. */
    val number: NumberNamespace

    /** The `helper` namespace. See [ECharts.helper]. */
    val helper: HelperNamespace

    /** The `graphic` namespace. See [ECharts.graphic]. */
    val graphic: GraphicNamespace

    /** The `color` namespace. See [ECharts.color]. */
    val color: ColorNamespace

    /** The `matrix` namespace. See [ECharts.matrix]. */
    val matrix: MatrixNamespace

    /** The `vector` namespace. See [ECharts.vector]. */
    val vector: VectorNamespace

    /** The `util` namespace. See [ECharts.util]. */
    val util: UtilNamespace

    /** The `zrUtil` namespace. See [ECharts.zrUtil]. */
    val zrUtil: ZrUtilNamespace

    /** The `zrender` namespace. See [ECharts.zrender]. */
    val zrender: ZRenderNamespace

    /** The `dataTool` namespace. See [ECharts.dataTool]. */
    val dataTool: DataToolNamespace

    /** Draws a single element on a canvas context. See [ECharts.innerDrawElementOnCanvas]. */
    fun innerDrawElementOnCanvas(context: CanvasRenderingContext2D?, element: ZRElement)
}

/**
 * Chart installers exported by `echarts/charts`.
 *
 * Pass them to [EChartsCore.use] when the application uses the modular entry point.
 */
@JsModule("echarts/charts")
external object EChartsCharts {
    /** Installs the line chart. */
    val LineChart: EChartsExtensionInstaller

    /** Installs the bar chart. */
    val BarChart: EChartsExtensionInstaller

    /** Installs the pie chart. */
    val PieChart: EChartsExtensionInstaller

    /** Installs the scatter chart. */
    val ScatterChart: EChartsExtensionInstaller

    /** Installs the effect scatter chart. */
    val EffectScatterChart: EChartsExtensionInstaller

    /** Installs the radar chart. */
    val RadarChart: EChartsExtensionInstaller

    /** Installs the map chart. */
    val MapChart: EChartsExtensionInstaller

    /** Installs the tree chart. */
    val TreeChart: EChartsExtensionInstaller

    /** Installs the treemap chart. */
    val TreemapChart: EChartsExtensionInstaller

    /** Installs the sunburst chart. */
    val SunburstChart: EChartsExtensionInstaller

    /** Installs the graph chart. */
    val GraphChart: EChartsExtensionInstaller

    /** Installs the chord chart. */
    val ChordChart: EChartsExtensionInstaller

    /** Installs the funnel chart. */
    val FunnelChart: EChartsExtensionInstaller

    /** Installs the gauge chart. */
    val GaugeChart: EChartsExtensionInstaller

    /** Installs the pictorial bar chart. */
    val PictorialBarChart: EChartsExtensionInstaller

    /** Installs the theme river chart. */
    val ThemeRiverChart: EChartsExtensionInstaller

    /** Installs the custom chart. */
    val CustomChart: EChartsExtensionInstaller

    /** Installs the boxplot chart. */
    val BoxplotChart: EChartsExtensionInstaller

    /** Installs the candlestick chart. */
    val CandlestickChart: EChartsExtensionInstaller

    /** Installs the heatmap chart. */
    val HeatmapChart: EChartsExtensionInstaller

    /** Installs the parallel chart. */
    val ParallelChart: EChartsExtensionInstaller

    /** Installs the lines chart. */
    val LinesChart: EChartsExtensionInstaller

    /** Installs the sankey chart. */
    val SankeyChart: EChartsExtensionInstaller
}

/**
 * Component installers exported by `echarts/components`.
 *
 * Pass them to [EChartsCore.use] when the application uses the modular entry point.
 */
@JsModule("echarts/components")
external object EChartsComponents {
    /** Installs the aria component. */
    val AriaComponent: EChartsExtensionInstaller

    /** Installs the axis pointer component. */
    val AxisPointerComponent: EChartsExtensionInstaller

    /** Installs the brush component. */
    val BrushComponent: EChartsExtensionInstaller

    /** Installs the calendar component. */
    val CalendarComponent: EChartsExtensionInstaller

    /** Installs both data zoom variants. */
    val DataZoomComponent: EChartsExtensionInstaller

    /** Installs the inside data zoom component. */
    val DataZoomInsideComponent: EChartsExtensionInstaller

    /** Installs the slider data zoom component. */
    val DataZoomSliderComponent: EChartsExtensionInstaller

    /** Installs the dataset component. */
    val DatasetComponent: EChartsExtensionInstaller

    /** Installs the geo component. */
    val GeoComponent: EChartsExtensionInstaller

    /** Installs the graphic component. */
    val GraphicComponent: EChartsExtensionInstaller

    /** Installs the grid component. */
    val GridComponent: EChartsExtensionInstaller

    /** Installs the simple grid component. */
    val GridSimpleComponent: EChartsExtensionInstaller

    /** Installs both legend variants. */
    val LegendComponent: EChartsExtensionInstaller

    /** Installs the plain legend component. */
    val LegendPlainComponent: EChartsExtensionInstaller

    /** Installs the scrollable legend component. */
    val LegendScrollComponent: EChartsExtensionInstaller

    /** Installs the mark area component. */
    val MarkAreaComponent: EChartsExtensionInstaller

    /** Installs the mark line component. */
    val MarkLineComponent: EChartsExtensionInstaller

    /** Installs the mark point component. */
    val MarkPointComponent: EChartsExtensionInstaller

    /** Installs the matrix component. */
    val MatrixComponent: EChartsExtensionInstaller

    /** Installs the parallel coordinate component. */
    val ParallelComponent: EChartsExtensionInstaller

    /** Installs the polar coordinate component. */
    val PolarComponent: EChartsExtensionInstaller

    /** Installs the radar coordinate component. */
    val RadarComponent: EChartsExtensionInstaller

    /** Installs the single axis component. */
    val SingleAxisComponent: EChartsExtensionInstaller

    /** Installs the thumbnail component. */
    val ThumbnailComponent: EChartsExtensionInstaller

    /** Installs the timeline component. */
    val TimelineComponent: EChartsExtensionInstaller

    /** Installs the title component. */
    val TitleComponent: EChartsExtensionInstaller

    /** Installs the toolbox component. */
    val ToolboxComponent: EChartsExtensionInstaller

    /** Installs the tooltip component. */
    val TooltipComponent: EChartsExtensionInstaller

    /** Installs the transform component. */
    val TransformComponent: EChartsExtensionInstaller

    /** Installs both visual map variants. */
    val VisualMapComponent: EChartsExtensionInstaller

    /** Installs the continuous visual map component. */
    val VisualMapContinuousComponent: EChartsExtensionInstaller

    /** Installs the piecewise visual map component. */
    val VisualMapPiecewiseComponent: EChartsExtensionInstaller
}

/**
 * Renderer installers exported by `echarts/renderers`.
 *
 * Pass one of them to [EChartsCore.use] and select it with [EChartsInitOpts.renderer].
 */
@JsModule("echarts/renderers")
external object EChartsRenderers {
    /** Installs the canvas renderer. */
    val CanvasRenderer: EChartsExtensionInstaller

    /** Installs the SVG renderer. */
    val SVGRenderer: EChartsExtensionInstaller
}

/**
 * Feature installers exported by `echarts/features`.
 */
@JsModule("echarts/features")
external object EChartsFeatures {
    /** Installs the axis break feature. */
    val AxisBreak: EChartsExtensionInstaller

    /** Installs the label layout feature. */
    val LabelLayout: EChartsExtensionInstaller

    /** Installs the legacy grid `containLabel` behaviour. */
    val LegacyGridContainLabel: EChartsExtensionInstaller

    /** Installs the scatter jitter feature. */
    val ScatterJitter: EChartsExtensionInstaller

    /** Installs the universal transition feature. */
    val UniversalTransition: EChartsExtensionInstaller
}
