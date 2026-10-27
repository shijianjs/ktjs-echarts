package echarts

import echarts.options.BarGridLayoutOptionForCustomSeries
import echarts.options.BarGridLayoutResultForCustomSeries
import echarts.options.CoordinateSystemDataCoord
import echarts.options.CoordinateSystemDataLayout
import echarts.options.CustomRootElementOption
import echarts.options.CustomSeriesFontOption
import echarts.options.DimensionDefinitionLoose
import echarts.options.DimensionLoose
import echarts.options.DoubleOrDoubleArray
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.StringOrDouble
import js.objects.Record
import kotlin.js.JsAny
import web.html.HTMLCanvasElement
import web.html.HTMLImageElement
import zrender.RectLike
import zrender.ZRElement
import zrender.ZRStyleProps
import zrender.ZRenderType

/**
 * The `echarts.dataTool` namespace. Empty in the npm build; the tools live in
 * `echarts/extension/dataTool`.
 */
external interface DataToolNamespace

/**
 * Result of `PlatformAPI.measureText`.
 */
external interface PlatformTextSize {
    /** Width of the measured text. */
    val width: Double
}

/**
 * Platform overrides accepted by `setPlatformAPI`.
 */
external interface PlatformAPI {
    /** Creates a canvas element. */
    fun createCanvas(): HTMLCanvasElement?

    /** Measures a text block. */
    fun measureText(text: String, font: String? = definedExternally): PlatformTextSize?

    /** Loads an image. */
    fun loadImage(src: String, onload: () -> Unit, onerror: () -> Unit): HTMLImageElement?

    /** Returns the current time in milliseconds. */
    fun getTime(): Double?
}

/**
 * Source exposed to an external data transform.
 */
external interface ExternalSource {
    /** Format of the source data. */
    val sourceFormat: String

    /** Returns the raw data. */
    fun getRawData(): JsAny

    /** Returns one raw data item. */
    fun getRawDataItem(dataIndex: Double): JsAny

    /** Returns a copy of the raw data. */
    fun cloneRawData(): JsAny

    /** Returns the definition of one dimension. */
    fun getDimensionInfo(dim: DimensionLoose): ExternalDimensionDefinition?

    /** Returns the definitions of every dimension. */
    fun cloneAllDimensionInfo(): Array<ExternalDimensionDefinition>

    /** Number of data items. */
    fun count(): Double

    /** Returns the value at [dataIndex] and [dimIndex]. */
    fun retrieveValue(dataIndex: Double, dimIndex: Double): OptionDataValue?

    /** Returns the value at [dimIndex] of one data item. */
    fun retrieveValueFromItem(dataItem: JsAny, dimIndex: Double): OptionDataValue?

    /** Converts a raw value with the given dimension definition. */
    fun convertValue(rawVal: JsAny, dimInfo: ExternalDimensionDefinition): JsAny?

    /** Returns the index of one dimension. */
    fun getDimensionIndex(dim: String): Double
}

/**
 * One dimension definition exposed to an external data transform.
 */
external interface ExternalDimensionDefinition {
    /** Index of the dimension. */
    val index: Double

    /** Name of the dimension. */
    val name: String?

    /** Display name of the dimension. */
    val displayName: String?

    /** Type of the dimension. */
    val type: String?
}

/**
 * Parameter of an external data transform.
 */
external interface ExternalTransformParam {
    /** Upstream source. */
    val upstream: ExternalSource

    /** Upstream sources, when the transform has several inputs. */
    val upstreamList: Array<ExternalSource>

    /** Transform configuration. */
    val config: JsAny?
}

/**
 * Result item of an external data transform.
 */
external interface ExternalTransformResultItem {
    /** Transformed data. */
    var data: JsAny

    /** Dimension definitions of the transformed data. */
    var dimensions: Array<echarts.options.DimensionDefinitionLoose>?
}

/**
 * External data transform registered with `registerTransform`.
 */
external interface ExternalTransform {
    /** Transform type used by `dataset.transform`. */
    var type: String

    /** Whether the transform is built into ECharts. */
    var isBuiltIn: Boolean?

    /** Runs the transform. */
    var transform: ((param: ExternalTransformParam) -> Array<ExternalTransformResultItem>)?
}

/**
 * Creator of a coordinate system registered with `registerCoordinateSystem`.
 */
external interface CoordinateSystemCreator {
    /** Creates the coordinate system instances. */
    var create: (GlobalModel, ExtensionAPI) -> Array<CoordinateSystemMaster>

    /** Dimensions of the coordinate system. */
    var dimensions: Array<String>?

    /** Returns the dimension definitions of the coordinate system. */
    var getDimensionsInfo: (() -> Array<echarts.options.DimensionDefinitionLoose>)?
}

/**
 * A coordinate system instance created by [CoordinateSystemCreator].
 */
external interface CoordinateSystemMaster {
    /** Dimensions of the coordinate system. */
    val dimensions: Array<String>

    /** Model of the component that owns the coordinate system. */
    val model: ComponentModel?

    /** Updates the coordinate system. */
    fun update(ecModel: GlobalModel, api: ExtensionAPI)

    /** Whether [point] is inside the coordinate system. */
    fun containPoint(point: Array<Double>): Boolean

    /** Returns the axes of the coordinate system. */
    fun getAxes(): Array<Axis>

    /** Returns the rectangle of the coordinate system. */
    fun getRect(): RectLike

    /** Converts a data coordinate to a pixel point. */
    fun convertToPixel(ecModel: GlobalModel, finder: ModelFinder, value: JsAny, opt: JsAny? = definedExternally): JsAny

    /** Converts a pixel point to a data coordinate. */
    fun convertFromPixel(ecModel: GlobalModel, finder: ModelFinder, pixelValue: JsAny, opt: JsAny? = definedExternally): JsAny

    /** Converts a data coordinate to a layout. */
    fun convertToLayout(ecModel: GlobalModel, finder: ModelFinder, value: JsAny, opt: JsAny? = definedExternally): JsAny
}

/**
 * Stage handler registered with `registerLayout` or `registerVisual`.
 */
external interface StageHandler {
    /** Whether the task runs for every series. */
    var createOnAllSeries: Boolean?

    /** Series type the task applies to. */
    var seriesType: String?

    /** Returns the series the task applies to. */
    var getTargetSeries: ((GlobalModel, ExtensionAPI) -> JsAny)?

    /** Whether filtered series are also processed. */
    var performRawSeries: Boolean?

    /** Plans the task. */
    var plan: ((SeriesModel, GlobalModel, ExtensionAPI, Payload?) -> JsAny)?

    /** Runs the task once for the whole chart. */
    var overallReset: ((GlobalModel, ExtensionAPI, Payload?) -> Unit)?

    /** Runs the task for one series. */
    var reset: ((SeriesModel, GlobalModel, ExtensionAPI, Payload?) -> JsAny)?

    /** Whether the overall task must run when the pipeline progresses. */
    var dirtyOnOverallProgress: Boolean?
}

/**
 * Callback form of a stage handler: an overall reset function.
 */
typealias StageHandlerOverallReset = (GlobalModel, ExtensionAPI, Payload?) -> Unit

/**
 * Creator of a loading effect registered with `registerLoading`.
 */
typealias LoadingEffectCreator = (api: ExtensionAPI, cfg: JsAny) -> ZRElement

/**
 * Parameters passed to a custom series `renderItem` callback.
 */
external interface CustomSeriesRenderItemParams {
    /** Context of the render item. */
    val context: Record<String, JsAny>?

    /** Index of the data item. */
    val dataIndex: Double

    /** Id of the series. */
    val seriesId: String

    /** Name of the series. */
    val seriesName: String

    /** Index of the series. */
    val seriesIndex: Double

    /** Coordinate system of the series. */
    val coordSys: CustomSeriesRenderItemCoordSys

    /** Indices of the data dimensions used by each encoding role. */
    val encode: Record<String, Array<Double>>?

    /** Index of the data item inside the current data. */
    val dataIndexInside: Double

    /** Number of data items. */
    val dataInsideLength: Double

    /** Payload of the data item. */
    val itemPayload: Record<String, JsAny>?

    /** Type of the action that triggered the render. */
    val actionType: String?
}

/**
 * Coordinate system description passed to a custom series `renderItem` callback.
 *
 * The upstream type only declares [type]; the members of the coordinate systems that are laid out
 * as a rectangle (`cartesian2d`, `matrix`, `calendar`, ...) are documented in the handbook and are
 * available at run time, so they are modeled here as optional members.
 */
external interface CustomSeriesRenderItemCoordSys {
    /** Type of the coordinate system. */
    val type: String

    /** X coordinate of the layout rectangle of a cartesian coordinate system. */
    val x: Double?

    /** Y coordinate of the layout rectangle of a cartesian coordinate system. */
    val y: Double?

    /** Width of the layout rectangle of a cartesian coordinate system. */
    val width: Double?

    /** Height of the layout rectangle of a cartesian coordinate system. */
    val height: Double?
}

/**
 * Helper API passed to a custom series `renderItem` callback.
 */
external interface CustomSeriesRenderItemAPI {
    /** Width of the chart. */
    fun getWidth(): Double

    /** Height of the chart. */
    fun getHeight(): Double

    /** Returns the zrender instance. */
    fun getZr(): ZRenderType

    /** Returns the device pixel ratio. */
    fun getDevicePixelRatio(): Double

    /** Converts a data coordinate to a pixel coordinate. */
    fun coord(data: CoordinateSystemDataCoord, opt: JsAny? = definedExternally): Array<Double>

    /** Returns the size of a data extent in pixels, e.g. the size of one category band. */
    fun size(dataSize: OptionDataValueOrArray, dataItem: OptionDataValueOrArray? = definedExternally): DoubleOrDoubleArray

    /** Returns the layout of a data coordinate. */
    fun layout(data: CoordinateSystemDataCoord, opt: JsAny? = definedExternally): CoordinateSystemDataLayout

    /** Returns the value of [dim]. */
    fun value(dim: DimensionLoose, dataIndexInside: Double? = definedExternally): OptionDataValue?

    /** Returns the raw ordinal value of [dim]. */
    fun ordinalRawValue(dim: DimensionLoose, dataIndexInside: Double? = definedExternally): StringOrDouble?

    /** Returns the style of the data item. */
    fun style(userProps: ZRStyleProps? = definedExternally, dataIndexInside: Double? = definedExternally): ZRStyleProps

    /** Returns the emphasis style of the data item. */
    fun styleEmphasis(userProps: ZRStyleProps? = definedExternally, dataIndexInside: Double? = definedExternally): ZRStyleProps

    /** Returns a visual value of the data item. */
    fun visual(visualType: VisualProp, dataIndexInside: Double? = definedExternally): JsAny?

    /** Computes the bar layout of the data item. */
    fun barLayout(opt: BarGridLayoutOptionForCustomSeries): BarGridLayoutResultForCustomSeries?

    /** Returns the indices of the current series. */
    fun currentSeriesIndices(): Array<Double>

    /** Builds a CSS font string. */
    fun font(opt: CustomSeriesFontOption): String
}

/**
 * Return value of a custom series `renderItem` callback: a custom element tree, `null` or `undefined`.
 */
typealias CustomSeriesRenderItemReturn = CustomRootElementOption?

/**
 * Callback that renders one data item of a custom series.
 */
typealias CustomSeriesRenderItem = (params: CustomSeriesRenderItemParams, api: CustomSeriesRenderItemAPI) -> CustomSeriesRenderItemReturn

/**
 * Callback that preprocesses an option before it is normalized.
 */
typealias OptionPreprocessor = (option: echarts.options.EChartsOption, isTheme: Boolean) -> Unit

/**
 * Handler of a custom action registered with `registerAction`.
 *
 * Returning event data publishes it; return `null` to publish nothing.
 */
typealias ActionHandler = (payload: Payload, ecModel: GlobalModel, api: ExtensionAPI) -> ECEventData?

/**
 * Callback that builds the public event content of a custom action.
 */
typealias ActionRefineEvent = (
    actionResultBatch: Array<ECEventData>,
    payload: Payload,
    ecModel: GlobalModel,
    api: ExtensionAPI,
) -> ActionRefineEventResult

/**
 * Callback invoked after a chart is initialized.
 */
typealias PostIniter = (chart: EChartsInstance) -> Unit

/**
 * Callback invoked after every update.
 */
typealias PostUpdater = (ecModel: GlobalModel, api: ExtensionAPI) -> Unit
