@file:JsModule("echarts/core")

package echarts

import kotlin.js.JsAny
import zrender.ZRElement
import zrender.ZRGroup
import zrender.ZRenderType

/**
 * Base class of the models that wrap an option fragment.
 *
 * Extension authors receive instances of this class (or its subclasses) in custom series,
 * component and layout code. The declarations here cover the members that extension code
 * normally uses; see the upstream TypeScript declaration of `Model` for the full surface.
 */
external open class Model {
    /** Option fragment owned by this model. */
    val option: JsAny

    /** Parent model, or `null`/`undefined` for a root model. */
    val parentModel: Model?

    /** Global model that owns this model. */
    val ecModel: GlobalModel?

    /** Creates a model for the given option. */
    constructor(
        option: JsAny? = definedExternally,
        parentModel: Model? = definedExternally,
        ecModel: GlobalModel? = definedExternally,
    )

    /** Initializes this model with an option. */
    fun init(
        option: JsAny,
        parentModel: Model? = definedExternally,
        ecModel: GlobalModel? = definedExternally,
    )

    /** Merges the input option into this model. */
    fun mergeOption(option: JsAny, ecModel: GlobalModel? = definedExternally)

    /** Reads the value at [path]; paths are keys or arrays of keys. */
    fun get(path: JsAny, ignoreParent: Boolean? = definedExternally): JsAny

    /** Reads the value of the property named [path]. */
    fun get(path: String, ignoreParent: Boolean? = definedExternally): JsAny

    /** Reads the value at the given key path. */
    fun get(path: Array<String>, ignoreParent: Boolean? = definedExternally): JsAny

    /** Reads the value of [key] without walking up to the parent model. */
    fun getShallow(key: String, ignoreParent: Boolean? = definedExternally): JsAny

    /** Returns the sub-model at [path]. */
    fun getModel(path: JsAny, parentModel: Model? = definedExternally): Model

    /** Returns the sub-model named [path]. */
    fun getModel(path: String, parentModel: Model? = definedExternally): Model

    /** Whether the model has an option. */
    fun isEmpty(): Boolean

    /** Restores the data-related state after a re-render. */
    fun restoreData()

    /** Returns a copy of this model. */
    fun clone(): Model

    /** Normalizes a path string or array into an array of keys. */
    fun parsePath(path: JsAny): Array<String>

    /** Whether animations are enabled for this model. */
    fun isAnimationEnabled(): Boolean
}

/**
 * Model of a component such as `grid`, `tooltip` or `xAxis`.
 */
external open class ComponentModel : Model {
    /** Full component type, e.g. `"xAxis"` or `"series.line"`. */
    val type: String

    /** Component id. */
    val id: String

    /** Component name. */
    val name: String

    /** Main component type, e.g. `"series"`. */
    val mainType: String

    /** Component sub type, e.g. `"line"`. */
    val subType: String

    /** Index of the component inside its main type. */
    val componentIndex: Int

    /** Unique id of this model instance. */
    val uid: String

    /** Whether the framework must not assign `z`, `zlevel` and `z2` automatically. */
    var preventAutoZ: Boolean

    /** Returns the default option of the component. */
    fun getDefaultOption(): JsAny

    /** Returns the components of [mainType] that this component refers to. */
    fun getReferringComponents(mainType: String, opt: JsAny): JsAny

    /** Returns the box layout parameters (`left`, `top`, `width`, ...) of the component. */
    fun getBoxLayoutParams(): JsAny

    /** Returns the key used to group components into a progressive `zlevel`. */
    fun getZLevelKey(): String

    /** Sets the `zlevel` used by the component. */
    fun setZLevel(zlevel: Double)
}

/**
 * Model of a series.
 */
external open class SeriesModel : ComponentModel {
    /** Index of the series inside the option. */
    val seriesIndex: Int

    /** Coordinate system the series is attached to. */
    val coordinateSystem: JsAny

    /** Whether the series uses a symbol. */
    val hasSymbolVisual: Boolean

    /** Default symbol name or path. */
    var defaultSymbol: String

    /** Icon shown in the legend. */
    var legendIcon: String

    /** Initializes the data of the series. */
    fun getInitialData(option: JsAny, ecModel: GlobalModel): SeriesData

    /** Returns the data of the series. */
    fun getData(dataType: String? = definedExternally): SeriesData

    /** Returns every data list of the series (`main`, `node`, `edge`, ...). */
    fun getAllData(): Array<SeriesData>

    /** Replaces the data list of the series. */
    fun setData(data: SeriesData)

    /** Returns the raw (unprocessed) data of the series. */
    fun getRawData(): SeriesData

    /** Returns the base axis of the coordinate system. */
    fun getBaseAxis(): Axis

    /** Returns the indices of the data items nearest to [value] on [axisDim]. */
    fun indicesOfNearest(axisDim: String, dim: String, value: Double, maxDistance: Double? = definedExternally): Array<Int>

    /** Returns the data indices selected by [select]. */
    fun getSelectedDataIndices(): Array<Int>

    /** Whether the data item is selected. */
    fun isSelected(dataIndex: Int, dataType: String? = definedExternally): Boolean

    /** Selects data items. */
    fun select(innerDataIndices: Array<Int>, dataType: String? = definedExternally)

    /** Unselects data items. */
    fun unselect(innerDataIndices: Array<Int>, dataType: String? = definedExternally)

    /** Toggles the selection of data items. */
    fun toggleSelect(innerDataIndices: Array<Int>, dataType: String? = definedExternally)

    /** Appends raw data items to the series. */
    fun appendData(params: JsAny)
}

/**
 * View that renders a series.
 */
external open class ChartView {
    /** Series type rendered by this view. */
    var type: String

    /** Root group of the rendered elements. */
    val group: ZRGroup

    /** Unique id of this view instance. */
    val uid: String

    /** Whether the label line update is left to the chart itself (used by pie and funnel). */
    var ignoreLabelLineUpdate: Boolean

    /** Initializes the view. */
    fun init(ecModel: GlobalModel, api: ExtensionAPI)

    /** Renders the series. */
    fun render(seriesModel: SeriesModel, ecModel: GlobalModel, api: ExtensionAPI, payload: Payload)

    /** Highlights a series or a data item. */
    fun highlight(seriesModel: SeriesModel, ecModel: GlobalModel, api: ExtensionAPI, payload: Payload)

    /** Downplays a series or a data item. */
    fun downplay(seriesModel: SeriesModel, ecModel: GlobalModel, api: ExtensionAPI, payload: Payload)

    /** Removes the rendered elements of the series. */
    fun remove(ecModel: GlobalModel, api: ExtensionAPI)

    /** Disposes this view. */
    fun dispose(ecModel: GlobalModel, api: ExtensionAPI)

    /** Updates the view after the data changed. */
    fun updateView(seriesModel: SeriesModel, ecModel: GlobalModel, api: ExtensionAPI, payload: Payload)

    /** Updates the visual encoding of the view. */
    fun updateVisual(seriesModel: SeriesModel, ecModel: GlobalModel, api: ExtensionAPI, payload: Payload)

    /** Traverses the elements rendered in this update. */
    fun eachRendered(cb: (element: ZRElement) -> Boolean?)
}

/**
 * View that renders a component.
 */
external open class ComponentView {
    /** Root group of the rendered elements. */
    val group: ZRGroup

    /** Unique id of this view instance. */
    val uid: String

    /** Initializes the view. */
    fun init(ecModel: GlobalModel, api: ExtensionAPI)

    /** Renders the component. */
    fun render(model: ComponentModel, ecModel: GlobalModel, api: ExtensionAPI, payload: Payload)

    /** Disposes this view. */
    fun dispose(ecModel: GlobalModel, api: ExtensionAPI)

    /** Updates the view after the option changed. */
    fun updateView(model: ComponentModel, ecModel: GlobalModel, api: ExtensionAPI, payload: Payload)

    /** Updates the layout of the view. */
    fun updateLayout(model: ComponentModel, ecModel: GlobalModel, api: ExtensionAPI, payload: Payload)

    /** Updates the visual encoding of the view. */
    fun updateVisual(model: ComponentModel, ecModel: GlobalModel, api: ExtensionAPI, payload: Payload)

    /** Traverses the elements rendered in this update. */
    fun eachRendered(cb: (element: ZRElement) -> Boolean?)
}

/**
 * Data list of a series or of a component that owns data.
 *
 * Upstream exports this class as `List`; it is named `SeriesData` here so that it does not
 * shadow [kotlin.collections.List].
 */
@JsName("List")
external open class SeriesData {
    /** Name of every dimension in this list. */
    val dimensions: Array<String>

    /** Model that owns this data list. */
    val hostModel: Model

    /** Data type, e.g. `"main"`, `"node"` or `"edge"`. */
    var dataType: String?

    /** Whether the data items are given as objects with per-item options. */
    var hasItemOption: Boolean

    /** Creates a data list from dimension definitions and its host model. */
    constructor(dimensionsInput: JsAny, hostModel: Model)

    /** Creates a data list from a list of dimension names and its host model. */
    constructor(dimensions: Array<String>, hostModel: Model)

    /** Returns the concrete dimension name of [dim]. */
    fun getDimension(dim: String): String

    /** Returns the concrete dimension name at [dim]. */
    fun getDimension(dim: Int): String

    /** Returns the index of [dim], or `-1` when it does not exist. */
    fun getDimensionIndex(dim: String): Int

    /** Returns the index of the dimension at [dim], or `-1` when it does not exist. */
    fun getDimensionIndex(dim: Int): Int

    /** Returns the type and calculation info of [dim]. */
    fun getDimensionInfo(dim: String): JsAny

    /** Returns the type and calculation info of the dimension at [dim]. */
    fun getDimensionInfo(dim: Int): JsAny

    /** Returns the concrete dimension names used by the coordinate system. */
    fun getDimensionsOnCoord(): Array<String>

    /** Maps a coordinate dimension to a data dimension. */
    fun mapDimension(coordDim: String, idx: Int? = definedExternally): String

    /** Maps a coordinate dimension to all of its data dimensions. */
    fun mapDimensionsAll(coordDim: String): Array<String>

    /** Returns the underlying data store. */
    fun getStore(): JsAny

    /** (Re)initializes the data list. */
    fun initData(
        data: JsAny,
        nameList: Array<String>? = definedExternally,
        dimValueGetter: ((dataItem: JsAny, dimIndex: Int, dataIndex: Int, dimName: String, data: JsAny) -> JsAny)? = definedExternally,
    )

    /** Appends raw data items. */
    fun appendData(data: Array<JsAny>)

    /** Appends values, one row per data item. */
    fun appendValues(values: Array<Array<JsAny>>, names: Array<String>? = definedExternally)

    /** Returns the approximate extent of [dim]. */
    fun getApproximateExtent(dim: String, filter: JsAny? = definedExternally): Array<Double>

    /** Sets the approximate extent of [dim]. */
    fun setApproximateExtent(extent: Array<Double>, dim: String)

    /** Returns a calculation info value. */
    fun getCalculationInfo(key: String): JsAny

    /** Sets a calculation info value. */
    fun setCalculationInfo(key: String, value: JsAny)

    /** Returns the name of the data item at [idx]. */
    fun getName(idx: Int): String

    /** Returns the id of the data item at [idx]. */
    fun getId(idx: Int): String

    /** Number of data items in this list. */
    fun count(): Int

    /** Returns the value of [dim] at [idx]. */
    fun get(dim: String, idx: Int): JsAny

    /** Returns the value of [dim] at the given raw index. */
    fun getByRawIndex(dim: String, rawIdx: Int): JsAny

    /** Returns the indices of the data items of this list. */
    fun getIndices(): JsAny

    /** Returns the extent of [dim]. */
    fun getDataExtent(dim: String): Array<Double>

    /** Returns the sum of [dim]. */
    fun getSum(dim: String): Double

    /** Returns the median of [dim]. */
    fun getMedian(dim: String): Double

    /** Returns the values of all dimensions at [idx]. */
    fun getValues(idx: Int): Array<JsAny>

    /** Returns the values of [dimensions] at [idx]. */
    fun getValues(dimensions: Array<String>, idx: Int): Array<JsAny>

    /** Whether the data item at [idx] has a value. */
    fun hasValue(idx: Int): Boolean

    /** Returns the index of the data item named [name], or `-1`. */
    fun indexOfName(name: String): Int

    /** Returns the raw index of the data item at [idx]. */
    fun getRawIndex(idx: Int): Int

    /** Returns the index of the given raw index, or `-1`. */
    fun indexOfRawIndex(rawIndex: Int): Int

    /** Returns the index of the first data item whose [dim] equals [value]. */
    fun rawIndexOf(dim: String, value: Double): Int

    /** Traverses the indices of the data items. */
    fun each(cb: (idx: Int) -> Unit)

    /** Traverses one dimension; the callback receives the value and the index. */
    fun each(dim: String, cb: (value: JsAny, idx: Int) -> Unit)

    /** Keeps only the data items accepted by the callback. */
    fun filterSelf(cb: (idx: Int) -> Boolean): SeriesData

    /** Keeps only the data items accepted by the callback; [dim] selects the value passed to it. */
    fun filterSelf(dim: String, cb: (value: JsAny, idx: Int) -> Boolean): SeriesData

    /** Keeps the data items whose values are inside [range]. */
    fun selectRange(range: JsAny): SeriesData

    /** Downsamples the list with a custom sampler. */
    fun downSample(
        dimension: String,
        rate: Double,
        sampleValue: (frameValues: Array<JsAny>) -> Double,
        sampleIndex: (frameValues: Array<JsAny>, value: Double) -> Int,
    ): SeriesData

    /** Downsamples the list by keeping the minimum and maximum of each frame. */
    fun minmaxDownSample(valueDimension: String, rate: Double): SeriesData

    /** Downsamples the list with the Largest-Triangle-Three-Buckets algorithm. */
    fun lttbDownSample(valueDimension: String, rate: Double): SeriesData

    /** Returns the raw data item at [idx]. */
    fun getRawDataItem(idx: Int): JsAny

    /** Returns the per-item model at [idx]. */
    fun getItemModel(idx: Int): Model

    /** Returns the diff between this list and [otherList]. */
    fun diff(otherList: SeriesData): JsAny

    /** Returns a visual value of the whole list. */
    fun getVisual(key: String): JsAny

    /** Sets a visual value of the whole list. */
    fun setVisual(key: String, value: JsAny)

    /** Returns a visual value of the data item at [idx]. */
    fun getItemVisual(idx: Int, key: String): JsAny

    /** Sets a visual value of the data item at [idx]. */
    fun setItemVisual(idx: Int, key: String, value: JsAny)

    /** Clears every visual value. */
    fun clearAllVisual()

    /** Sets a layout value of the whole list. */
    fun setLayout(key: String, value: JsAny)

    /** Returns a layout value of the whole list. */
    fun getLayout(key: String): JsAny

    /** Returns the layout of the data item at [idx]. */
    fun getItemLayout(idx: Int): JsAny

    /** Sets the layout of the data item at [idx]. */
    fun setItemLayout(idx: Int, layout: JsAny, merge: Boolean? = definedExternally)

    /** Clears the per-item layouts. */
    fun clearItemLayouts()

    /** Sets the graphic element of the data item at [idx]. */
    fun setItemGraphicEl(idx: Int, el: ZRElement)

    /** Returns the graphic element of the data item at [idx]. */
    fun getItemGraphicEl(idx: Int): ZRElement?

    /** Returns a shallow copy of this list. */
    fun cloneShallow(list: SeriesData? = definedExternally): SeriesData
}

/**
 * Axis of a coordinate system (cartesian, polar, single axis, ...).
 */
external open class Axis {
    /** Axis type: `"category"`, `"value"`, `"time"` or `"log"`. */
    var type: String

    /** Name of the dimension the axis works on. */
    val dim: String

    /** Scale of the axis. */
    var scale: JsAny

    /** Model of the axis. */
    var model: ComponentModel

    /** Whether the coordinate lies inside the axis extent. */
    fun contain(coord: Double): Boolean

    /** Whether the data value lies inside the axis extent. */
    fun containData(data: JsAny): Boolean

    /** Whether the data value lies inside the axis extent. */
    fun containData(data: Double): Boolean

    /** Returns the coordinate extent. */
    fun getExtent(): Array<Double>

    /** Sets the coordinate extent. */
    fun setExtent(start: Double, end: Double)

    /** Converts a data value to a coordinate. */
    fun dataToCoord(data: JsAny, clamp: Boolean? = definedExternally): Double

    /** Converts a numeric data value to a coordinate. */
    fun dataToCoord(data: Double, clamp: Boolean? = definedExternally): Double

    /** Converts a coordinate to a data value. */
    fun coordToData(coord: Double, clamp: Boolean? = definedExternally): Double

    /** Converts a pixel point to a data value. */
    fun pointToData(point: Array<Double>, clamp: Boolean? = definedExternally): Double

    /** Returns the tick coordinates, honoring `onBand`. */
    fun getTicksCoords(opt: JsAny? = definedExternally): Array<JsAny>

    /** Returns the minor tick coordinates, grouped per major tick. */
    fun getMinorTicksCoords(): Array<Array<JsAny>>

    /** Returns the labels of the axis. */
    fun getViewLabels(ctx: JsAny? = definedExternally): Array<JsAny>

    /** Returns the label model of the axis. */
    fun getLabelModel(): Model

    /** Returns the tick model of the axis. */
    fun getTickModel(): Model

    /** Returns the band width of the axis. */
    fun getBandWidth(): Double

    /** Returns the rotation of the axis, in degrees. */
    fun getRotate(): Double

    /** Returns the computed interval of the category ticks and labels. */
    fun calculateCategoryInterval(ctx: JsAny? = definedExternally): Int
}

/**
 * The global model that owns every component and series of a chart instance.
 */
external interface GlobalModel {
    /** Returns the component of [mainType] at [index]. */
    fun getComponent(mainType: String, index: Int): ComponentModel?

    /** Returns the component of [mainType] with the given id. */
    fun getComponent(mainType: String, id: String): ComponentModel?

    /** Returns the series at [index]. */
    fun getSeriesByIndex(index: Int): SeriesModel?

    /** Returns the first series named [name]. */
    fun getSeriesByName(name: String): SeriesModel?

    /** Runs [cb] for every series. */
    fun eachSeries(cb: (series: SeriesModel) -> Unit)

    /** Runs [cb] for every component of [mainType]. */
    fun eachComponent(mainType: String, cb: (component: ComponentModel) -> Unit)
}

/**
 * API object passed to views and layout/visual handlers.
 */
external interface ExtensionAPI {
    /** Width of the chart, in pixels. */
    fun getWidth(): Double

    /** Height of the chart, in pixels. */
    fun getHeight(): Double

    /** Returns the zrender instance. */
    fun getZr(): ZRenderType

    /** Returns the DOM element of the chart. */
    fun getDom(): web.html.HTMLElement?

    /** Returns the global model of the chart. */
    fun getModel(): GlobalModel

    /** Converts a data value to a pixel point with the given coordinate system. */
    fun convertToPixel(finder: JsAny, value: JsAny): JsAny

    /** Converts a pixel point to a data value with the given coordinate system. */
    fun convertFromPixel(finder: JsAny, value: JsAny): JsAny

    /** Dispatches an action. */
    fun dispatchAction(payload: Payload)
}

/** Creates a component model class from a prototype object. */
external fun extendComponentModel(proto: JsAny): EChartsClassConstructor

/** Creates a component view class from a prototype object. */
external fun extendComponentView(proto: JsAny): EChartsClassConstructor

/** Creates a series model class from a prototype object. */
external fun extendSeriesModel(proto: JsAny): EChartsClassConstructor

/** Creates a chart view class from a prototype object. */
external fun extendChartView(proto: JsAny): EChartsClassConstructor
