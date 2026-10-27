package echarts.options

import echarts.MarkerStatisticType
import echarts.SortOrder
import js.objects.Record
import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.LinearGradientObject
import zrender.PatternObject
import zrender.RadialGradientObject
import zrender.RectLike
import zrender.ZRColor

/**
 * A single data value: `string | number | Date | null | undefined`.
 *
 * Null and undefined are represented by a Kotlin `null`.
 */
external interface OptionDataValue

/** Wraps a string value. */
fun OptionDataValue(value: String): OptionDataValue = value.unsafeCast<OptionDataValue>()

/** Wraps a numeric value. */
fun OptionDataValue(value: Double): OptionDataValue = value.unsafeCast<OptionDataValue>()

/** Wraps a date value. */
fun OptionDataValue(value: Date): OptionDataValue = value.unsafeCast<OptionDataValue>()

/**
 * `OptionDataValue | OptionDataValue[]`, used by formatter callbacks that may receive a single
 * value or one value per dimension.
 */
external interface OptionDataValueOrArray

/** Wraps a single value. */
fun OptionDataValueOrArray(value: OptionDataValue): OptionDataValueOrArray =
    value.unsafeCast<OptionDataValueOrArray>()

/** Wraps a string value. */
fun OptionDataValueOrArray(value: String): OptionDataValueOrArray =
    value.unsafeCast<OptionDataValueOrArray>()

/** Wraps a numeric value. */
fun OptionDataValueOrArray(value: Double): OptionDataValueOrArray =
    value.unsafeCast<OptionDataValueOrArray>()

/** Wraps a date value. */
fun OptionDataValueOrArray(value: Date): OptionDataValueOrArray =
    value.unsafeCast<OptionDataValueOrArray>()

/** Wraps an array of values. */
fun OptionDataValueOrArray(values: Array<OptionDataValue?>): OptionDataValueOrArray =
    values.unsafeCast<OptionDataValueOrArray>()

/**
 * Content returned by a tooltip formatter: an HTML string, an element or an array of elements.
 */
external interface TooltipContent

/** Wraps an HTML string. */
fun TooltipContent(value: String): TooltipContent = value.unsafeCast<TooltipContent>()

/** Wraps an element. */
fun TooltipContent(value: web.html.HTMLElement): TooltipContent = value.unsafeCast<TooltipContent>()

/** Wraps several elements. */
fun TooltipContent(values: Array<web.html.HTMLElement>): TooltipContent =
    values.unsafeCast<TooltipContent>()

/**
 * Tooltip marker returned by `echarts.format.getTooltipMarker`: an HTML string or, in rich-text
 * mode, a [RichTextTooltipMarker].
 */
external interface TooltipMarker

/** Wraps the HTML markup form. */
fun TooltipMarker(value: String): TooltipMarker = value.unsafeCast<TooltipMarker>()

/** Wraps the rich-text form. */
fun TooltipMarker(value: RichTextTooltipMarker): TooltipMarker = value.unsafeCast<TooltipMarker>()

/**
 * Rich-text tooltip marker.
 */
external interface RichTextTooltipMarker {
    /** Render mode of the marker, `"html"` or `"richText"`. */
    var renderMode: String

    /** Markup or text content of the marker. */
    var content: String

    /** Style of the marker. */
    var style: Record<String, JsAny>
}

/**
 * `string | { [key: string]: string }`, used by toolbox titles and icons.
 */
external interface StringOrDictionary

/** Wraps a plain string. */
fun StringOrDictionary(value: String): StringOrDictionary = value.unsafeCast<StringOrDictionary>()

/** Wraps a dictionary keyed by feature name. */
fun StringOrDictionary(values: Record<String, String>): StringOrDictionary =
    values.unsafeCast<StringOrDictionary>()

/**
 * `string | HTMLElement`, used by callbacks that may return text or a node.
 */
external interface StringOrHTMLElement

/** Wraps a string. */
fun StringOrHTMLElement(value: String): StringOrHTMLElement = value.unsafeCast<StringOrHTMLElement>()

/** Wraps an element. */
fun StringOrHTMLElement(value: web.html.HTMLElement): StringOrHTMLElement =
    value.unsafeCast<StringOrHTMLElement>()

/**
 * A series data item: `OptionDataValue | Dictionary<OptionDataValue> | OptionDataValue[] |
 * OptionDataItemObject`.
 */
external interface OptionDataItem

/** Wraps a single value. */
fun OptionDataItem(value: OptionDataValue): OptionDataItem = value.unsafeCast<OptionDataItem>()

/** Wraps a row of values, for example an `[x, y, ...]` tuple. */
fun OptionDataItem(values: Array<OptionDataValue?>): OptionDataItem = values.unsafeCast<OptionDataItem>()

/** Wraps a data item object. */
fun OptionDataItem(value: OptionDataItemObject): OptionDataItem = value.unsafeCast<OptionDataItem>()

/** Wraps a dictionary of named values. */
fun OptionDataItem(values: Record<String, OptionDataValue>): OptionDataItem = values.unsafeCast<OptionDataItem>()

/** Wraps a category axis data item. */
fun OptionDataItem(value: CategoryAxisDataItem): OptionDataItem = value.unsafeCast<OptionDataItem>()

/**
 * Object form of a data item.
 *
 * Concrete series extend this interface with their own styling and layout fields.
 */
external interface OptionDataItemObject {
    /** Data item id, used by `setOption` merges and actions. */
    var id: OptionId?

    /** Data item name, shown in labels and tooltips. */
    var name: OptionName?

    /** Group id, used by the universal transition. */
    var groupId: OptionId?

    /** Child group id, used by the universal transition. */
    var childGroupId: OptionId?

    /** Value of the item. */
    var value: OptionDataValue?

    /** Whether the item is selected. */
    var selected: Boolean?
}

/**
 * State members that most data items override: the emphasis, blur and select variants of the item.
 *
 * Upstream declares them per series through `StatesOptionMixin`; the wrapper uses the same
 * [EmphasisOption] / [BlurOption] / [SelectOption] types as the series level does, which is wider
 * than the per-series narrowing but keeps one declaration for every data item.
 */
external interface DataItemStateOption {
    /** Emphasis (hover) state of the item. */
    var emphasis: EmphasisOption?

    /** Blur (non-focused) state of the item. */
    var blur: BlurOption?

    /** Select state of the item. */
    var select: SelectOption?
}

/**
 * `string | number`, used by component and data item ids.
 */
external interface OptionId

/** Wraps a string id. */
fun OptionId(value: String): OptionId = value.unsafeCast<OptionId>()

/** Wraps a numeric id. */
fun OptionId(value: Double): OptionId = value.unsafeCast<OptionId>()

/** `string | number`, used by component and data item names. */
typealias OptionName = OptionId

/**
 * `boolean | "auto" | number`, describing whether the first row of a dataset contains the
 * dimension names.
 */
external interface OptionSourceHeader

/** Wraps a boolean. */
fun OptionSourceHeader(value: Boolean): OptionSourceHeader = value.unsafeCast<OptionSourceHeader>()

/** Wraps `"auto"` or another string value. */
fun OptionSourceHeader(value: String): OptionSourceHeader = value.unsafeCast<OptionSourceHeader>()

/** Wraps a row index. */
fun OptionSourceHeader(value: Double): OptionSourceHeader = value.unsafeCast<OptionSourceHeader>()

/**
 * Explicit definition of a data dimension.
 */
external interface DimensionDefinition {
    /** Dimension type: `"number"`, `"ordinal"`, `"time"` or `"float"`. */
    var type: String?

    /** Dimension name. */
    var name: String?

    /** Name used by tooltips and axis labels. */
    var displayName: String?
}

/**
 * A dimension definition, either a name or a [DimensionDefinition].
 */
external interface DimensionDefinitionLoose

/** Wraps a dimension name. */
fun DimensionDefinitionLoose(value: String): DimensionDefinitionLoose =
    value.unsafeCast<DimensionDefinitionLoose>()

/** Wraps an explicit definition. */
fun DimensionDefinitionLoose(value: DimensionDefinition): DimensionDefinitionLoose =
    value.unsafeCast<DimensionDefinitionLoose>()

/**
 * A dimension reference: a name or an index.
 */
external interface DimensionLoose

/** Wraps a dimension name. */
fun DimensionLoose(value: String): DimensionLoose = value.unsafeCast<DimensionLoose>()

/** Wraps a dimension index. */
fun DimensionLoose(value: Double): DimensionLoose = value.unsafeCast<DimensionLoose>()

/**
 * Value of one `encode` entry: a dimension or an array of dimensions.
 */
external interface OptionEncodeValue

/** Wraps one dimension. */
fun OptionEncodeValue(value: DimensionLoose): OptionEncodeValue = value.unsafeCast<OptionEncodeValue>()

/** Wraps several dimensions. */
fun OptionEncodeValue(values: Array<DimensionLoose>): OptionEncodeValue =
    values.unsafeCast<OptionEncodeValue>()

/**
 * Mapping between data dimensions and the roles used by a series.
 */
external interface OptionEncode {
    /** Dimensions used by the tooltip. */
    var tooltip: OptionEncodeValue?

    /** Dimensions used by the label. */
    var label: OptionEncodeValue?

    /** Dimension used as the item name. */
    var itemName: OptionEncodeValue?

    /** Dimension used as the item id. */
    var itemId: OptionEncodeValue?

    /** Dimension used as the series name. */
    var seriesName: OptionEncodeValue?

    /** Dimension used as the item group id. */
    var itemGroupId: OptionEncodeValue?

    /** Dimension used as the item child group id. */
    var childGroupdId: OptionEncodeValue?

    /**
     * Dimension used as the x coordinate, or as the category of a category axis.
     *
     * Upstream declares this together with the other coordinate dimensions through the index
     * signature `[coordDim: string]`; the documented ones live here because Kotlin has no index
     * signature on an external interface.
     */
    var x: OptionEncodeValue?

    /** Dimension used as the y coordinate. */
    var y: OptionEncodeValue?

    /** Dimension used as the value of the item, e.g. by pie and map series. */
    var value: OptionEncodeValue?

    /** Dimension used as the radius, e.g. by polar series. */
    var radius: OptionEncodeValue?

    /** Dimension used as the angle, e.g. by polar series. */
    var angle: OptionEncodeValue?
}

/**
 * Dataset source data.
 *
 * The upstream type is a union of several shapes. Because overloads that only differ in their
 * element type cannot coexist in Kotlin/JS, the non-array branches use dedicated helper functions
 * instead of `DatasetSource(...)`:
 *
 * ```kotlin
 * source = datasetArrayRows(arrayOf(row))
 * source = datasetObjectRows(arrayOf(objectRow))
 * ```
 */
external interface DatasetSource

/** Wraps the original shape: an array of values, value arrays or data item objects. */
fun datasetOriginal(values: Array<OptionDataItem>): DatasetSource =
    values.unsafeCast<DatasetSource>()

/** Wraps rows in which every row is an object keyed by dimension name. */
fun datasetObjectRows(rows: Array<Record<String, OptionDataValue>>): DatasetSource =
    rows.unsafeCast<DatasetSource>()

/** Wraps rows in which every row is an array of values. */
fun datasetArrayRows(rows: Array<Array<OptionDataValue>>): DatasetSource =
    rows.unsafeCast<DatasetSource>()

/** Wraps columns keyed by dimension name. */
fun datasetKeyedColumns(columns: Record<String, Array<OptionDataValue>>): DatasetSource =
    columns.unsafeCast<DatasetSource>()

/** Wraps a typed array of numbers. Kotlin `DoubleArray` maps to a JavaScript `Float64Array`. */
fun datasetTypedArray(values: DoubleArray): DatasetSource = values.unsafeCast<DatasetSource>()

/** @see DatasetSource */
typealias OptionSourceData = DatasetSource

/**
 * An external data transform used by `dataset.transform`.
 */
external interface DataTransformOption {
    /** Transform type, e.g. `"filter"` or `"sort"`. */
    var type: String?

    /** Transform-specific configuration. */
    var config: Record<String, JsAny>?

    /** Whether to print debug information. */
    var print: Boolean?
}

/** A pipeline of transforms. */
typealias PipedDataTransformOption = Array<DataTransformOption>

/**
 * Limit of the roam zoom level.
 */
external interface ScaleLimitOption {
    /** Minimum zoom. */
    var min: Double?

    /** Maximum zoom. */
    var max: Double?
}

/**
 * Shape of a symbol: a built-in name such as `"circle"`, a `"path://..."` / `"image://..."` value,
 * or a callback that computes it from the data item.
 *
 * Upstream `string | SymbolCallback<T>`.
 */
external interface SymbolShape

/** Wraps a built-in shape name or a path. */
fun SymbolShape(value: String): SymbolShape = value.unsafeCast<SymbolShape>()

/** Wraps a callback that computes the shape from the data item. */
fun SymbolShape(callback: (value: OptionDataItem, params: CallbackDataParams) -> String): SymbolShape =
    callback.unsafeCast<SymbolShape>()

/**
 * Offset of a symbol: `string | number | (string | number)[] | callback`.
 */
external interface SymbolOffset

/** Wraps a percentage-like string. */
fun SymbolOffset(value: String): SymbolOffset = value.unsafeCast<SymbolOffset>()

/** Wraps an absolute number. */
fun SymbolOffset(value: Double): SymbolOffset = value.unsafeCast<SymbolOffset>()

/** Wraps an `[x, y]` pair. */
fun SymbolOffset(values: Array<StringOrDouble>): SymbolOffset = values.unsafeCast<SymbolOffset>()

/** Wraps a callback that computes the offset from the data value. */
fun SymbolOffset(callback: (value: OptionDataItem, params: CallbackDataParams) -> StringOrDoubleOrArray): SymbolOffset =
    callback.unsafeCast<SymbolOffset>()

/**
 * Size of a symbol: `number | number[] | callback`.
 */
external interface SymbolSize

/** Wraps a single number. */
fun SymbolSize(value: Double): SymbolSize = value.unsafeCast<SymbolSize>()

/** Wraps a `[width, height]` pair. */
fun SymbolSize(values: Array<Double>): SymbolSize = values.unsafeCast<SymbolSize>()

/** Wraps a callback that computes the size from the data value. */
fun SymbolSize(callback: (value: OptionDataItem, params: CallbackDataParams) -> DoubleOrDoubleArray): SymbolSize =
    callback.unsafeCast<SymbolSize>()

/**
 * Rotation of a symbol in degrees: `number | callback`.
 */
external interface SymbolRotate

/** Wraps a rotation in degrees. */
fun SymbolRotate(value: Double): SymbolRotate = value.unsafeCast<SymbolRotate>()

/** Wraps a callback that computes the rotation from the data value. */
fun SymbolRotate(callback: (value: OptionDataItem, params: CallbackDataParams) -> Double): SymbolRotate =
    callback.unsafeCast<SymbolRotate>()

/**
 * `number | string | (number | string)[]`, used by radii and similar sizes.
 */
external interface StringOrDoubleOrArray

/** Wraps a string. */
fun StringOrDoubleOrArray(value: String): StringOrDoubleOrArray =
    value.unsafeCast<StringOrDoubleOrArray>()

/** Wraps a number. */
fun StringOrDoubleOrArray(value: Double): StringOrDoubleOrArray =
    value.unsafeCast<StringOrDoubleOrArray>()

/** Wraps an array of numbers and strings. */
fun StringOrDoubleOrArray(values: Array<StringOrDouble>): StringOrDoubleOrArray =
    values.unsafeCast<StringOrDoubleOrArray>()

/**
 * Position of a label: a built-in position name or an `[x, y]` pair.
 */
external interface LabelPosition

/** Wraps a built-in position such as `"top"`, `"inside"` or `"insideRight"`. */
fun LabelPosition(value: String): LabelPosition = value.unsafeCast<LabelPosition>()

/** Wraps an `[x, y]` position. */
fun LabelPosition(values: Array<StringOrDouble>): LabelPosition = values.unsafeCast<LabelPosition>()

/**
 * Parameters of a tooltip formatter or position callback: the single item branch, or the array
 * branch used by an `"axis"` trigger.
 *
 * Upstream `TopLevelFormatterParams`. `TooltipPositionCallbackParams` is declared with the same
 * shape, so it is modelled as [TopLevelFormatterParams] instead of a second opaque type.
 */
external interface TopLevelFormatterParams

/** Wraps the single-item branch. */
fun TopLevelFormatterParams(value: CallbackDataParams): TopLevelFormatterParams =
    value.unsafeCast<TopLevelFormatterParams>()

/** Wraps the `"axis"` trigger branch. */
fun TopLevelFormatterParams(values: Array<CallbackDataParams>): TopLevelFormatterParams =
    values.unsafeCast<TopLevelFormatterParams>()

/** Upstream `TooltipPositionCallbackParams`, structurally identical to [TopLevelFormatterParams]. */
typealias TooltipPositionCallbackParams = TopLevelFormatterParams

/**
 * Signature of a tooltip `formatter` callback.
 *
 * Upstream `TooltipFormatterCallback<TopLevelFormatterParams>` also passes the async ticket and
 * accepts an async result callback; the wrapper keeps the synchronous shape.
 */
typealias TooltipFormatterCallback = (params: TopLevelFormatterParams) -> TooltipContent

/**
 * Position of a tooltip: an array, a built-in position, a callback or a box layout.
 */
external interface TooltipPosition

/** Wraps a built-in position such as `"inside"`, `"top"` or `"bottom"`. */
fun TooltipPosition(value: String): TooltipPosition = value.unsafeCast<TooltipPosition>()

/** Wraps an `[x, y]` position. */
fun TooltipPosition(values: Array<StringOrDouble>): TooltipPosition = values.unsafeCast<TooltipPosition>()

/** Wraps a box layout. */
fun TooltipPosition(value: TooltipBoxLayoutOption): TooltipPosition = value.unsafeCast<TooltipPosition>()

/**
 * Signature of a tooltip `position` callback: receives the pointer `[x, y]`, the formatter
 * parameters (a single object or an array, like [TooltipFormatterCallback]), the tooltip element,
 * the rect of the hovered elements and the content/view sizes.
 */
typealias TooltipPositionCallback = (
    point: Array<Double>,
    params: TopLevelFormatterParams,
    el: JsAny?,
    rect: RectLike?,
    size: TooltipSize,
) -> TooltipPosition

/** Wraps a callback that computes the position from the pointer and content rect. */
fun TooltipPosition(callback: TooltipPositionCallback): TooltipPosition = callback.unsafeCast<TooltipPosition>()

/**
 * `boolean | TooltipOption`, used by [TimelineDataItemOption.tooltip].
 *
 * Upstream declares the member as `boolean`, but the timeline view resolves the item's option model,
 * so a tooltip object works and the official examples pass one.
 */
external interface BooleanOrTooltipOption

/** Wraps the flag form; `false` hides the tooltip of that point. */
fun BooleanOrTooltipOption(value: Boolean): BooleanOrTooltipOption = value.unsafeCast<BooleanOrTooltipOption>()

/** Wraps a tooltip option object for one component or data item. */
fun BooleanOrTooltipOption(value: TooltipOption): BooleanOrTooltipOption = value.unsafeCast<BooleanOrTooltipOption>()

/**
 * Tooltip position expressed as distances to the container edges.
 */
external interface TooltipBoxLayoutOption {
    /** Distance to the top edge. */
    var top: StringOrDouble?

    /** Distance to the left edge. */
    var left: StringOrDouble?

    /** Distance to the right edge. */
    var right: StringOrDouble?

    /** Distance to the bottom edge. */
    var bottom: StringOrDouble?
}

/**
 * Position and size of a component or series inside its container.
 *
 * Upstream `BoxLayoutOptionMixin`; a component that mixes it in can be positioned either by
 * distances to the edges or by an explicit size, or both.
 */
external interface BoxLayoutOptionMixin {
    /** Distance to the top edge, in pixels or as a percentage of the container. */
    var top: StringOrDouble?

    /** Distance to the right edge, in pixels or as a percentage of the container. */
    var right: StringOrDouble?

    /** Distance to the bottom edge, in pixels or as a percentage of the container. */
    var bottom: StringOrDouble?

    /** Distance to the left edge, in pixels or as a percentage of the container. */
    var left: StringOrDouble?

    /** Width, in pixels or as a percentage of the container. */
    var width: StringOrDouble?

    /** Height, in pixels or as a percentage of the container. */
    var height: StringOrDouble?
}

/**
 * Series drawn on a cartesian coordinate system.
 *
 * Upstream `SeriesOnCartesianOptionMixin`. `*Id` takes precedence over the matching `*Index`.
 */
external interface SeriesOnCartesianOptionMixin {
    /** Index of the x axis. */
    var xAxisIndex: Double?

    /** Index of the y axis. */
    var yAxisIndex: Double?

    /** Id of the x axis. */
    var xAxisId: String?

    /** Id of the y axis. */
    var yAxisId: String?
}

/**
 * Series drawn on a polar coordinate system.
 *
 * Upstream `SeriesOnPolarOptionMixin`.
 */
external interface SeriesOnPolarOptionMixin {
    /** Index of the polar coordinate system. */
    var polarIndex: Double?

    /** Id of the polar coordinate system. */
    var polarId: String?
}

/**
 * Series drawn on a single axis.
 *
 * Upstream `SeriesOnSingleOptionMixin`.
 */
external interface SeriesOnSingleOptionMixin {
    /** Index of the single axis. */
    var singleAxisIndex: Double?

    /** Id of the single axis. */
    var singleAxisId: String?
}

/**
 * Series drawn on a geo coordinate system.
 *
 * Upstream `SeriesOnGeoOptionMixin`.
 */
external interface SeriesOnGeoOptionMixin {
    /** Index of the geo coordinate system. */
    var geoIndex: Double?

    /** Id of the geo coordinate system. */
    var geoId: String?
}

/**
 * Series drawn on a radar coordinate system.
 *
 * Upstream `SeriesOnRadarOptionMixin`.
 */
external interface SeriesOnRadarOptionMixin {
    /** Index of the radar coordinate system. */
    var radarIndex: Double?

    /** Id of the radar coordinate system. */
    var radarId: String?
}

/**
 * Component placed on a calendar coordinate system.
 *
 * Upstream `ComponentOnCalendarOptionMixin`.
 */
external interface ComponentOnCalendarOptionMixin {
    /** Index of the calendar coordinate system. */
    var calendarIndex: Double?

    /** Id of the calendar coordinate system. */
    var calendarId: String?
}

/**
 * Component placed on a matrix coordinate system.
 *
 * Upstream `ComponentOnMatrixOptionMixin`.
 */
external interface ComponentOnMatrixOptionMixin {
    /** Index of the matrix coordinate system. */
    var matrixIndex: Double?

    /** Id of the matrix coordinate system. */
    var matrixId: String?
}

/**
 * Sizes passed to a tooltip position callback.
 */
external interface TooltipSize {
    /** Size of the tooltip content, `[width, height]`. */
    var contentSize: Array<Double>

    /** Size of the chart view, `[width, height]`. */
    var viewSize: Array<Double>
}

/**
 * `boolean | number | string`, used by options that accept a flag, a number or a named mode.
 */
external interface BooleanOrDoubleOrString

/** Wraps a boolean. */
fun BooleanOrDoubleOrString(value: Boolean): BooleanOrDoubleOrString =
    value.unsafeCast<BooleanOrDoubleOrString>()

/** Wraps a number. */
fun BooleanOrDoubleOrString(value: Double): BooleanOrDoubleOrString =
    value.unsafeCast<BooleanOrDoubleOrString>()

/** Wraps a string. */
fun BooleanOrDoubleOrString(value: String): BooleanOrDoubleOrString =
    value.unsafeCast<BooleanOrDoubleOrString>()

/**
 * `boolean | (string | number)[]`, used by `boundaryGap`.
 */
external interface BooleanOrStringArray

/** Wraps a boolean. */
fun BooleanOrStringArray(value: Boolean): BooleanOrStringArray =
    value.unsafeCast<BooleanOrStringArray>()

/** Wraps an array of numbers and strings. */
fun BooleanOrStringArray(values: Array<StringOrDouble>): BooleanOrStringArray =
    values.unsafeCast<BooleanOrStringArray>()

/**
 * Minimum or maximum of an axis: a number, a keyword such as `"dataMin"`, or a callback.
 */
external interface AxisMinMax

/** Wraps a number. */
fun AxisMinMax(value: Double): AxisMinMax = value.unsafeCast<AxisMinMax>()

/** Wraps a keyword such as `"dataMin"` or `"dataMax"`. */
fun AxisMinMax(value: String): AxisMinMax = value.unsafeCast<AxisMinMax>()

/** Wraps a callback that receives the data extent. */
fun AxisMinMax(callback: (extent: AxisExtent) -> Double): AxisMinMax =
    callback.unsafeCast<AxisMinMax>()

/**
 * Extent passed to an [AxisMinMax] callback.
 */
external interface AxisExtent {
    /** Minimum value of the data. */
    var min: Double

    /** Maximum value of the data. */
    var max: Double
}

/**
 * Interval between axis ticks or labels: a number, `"auto"`, or a callback.
 */
external interface AxisInterval

/** Wraps a number. */
fun AxisInterval(value: Double): AxisInterval = value.unsafeCast<AxisInterval>()

/** Wraps `"auto"` or another keyword. */
fun AxisInterval(value: String): AxisInterval = value.unsafeCast<AxisInterval>()

/** Wraps a callback that decides whether the tick at [index] is shown. */
fun AxisInterval(callback: (index: Double, value: String) -> Boolean): AxisInterval =
    callback.unsafeCast<AxisInterval>()

/**
 * One or more axis indices controlled by a component.
 */
external interface IndexOrIndexArray

/** Wraps a single index. */
fun IndexOrIndexArray(value: Double): IndexOrIndexArray = value.unsafeCast<IndexOrIndexArray>()

/** Wraps several indices. */
fun IndexOrIndexArray(values: Array<Double>): IndexOrIndexArray = values.unsafeCast<IndexOrIndexArray>()

/** Wraps the keywords `"all"` and `"none"`, used by `dataZoom` to reach every axis. */
fun IndexOrIndexArray(value: String): IndexOrIndexArray = value.unsafeCast<IndexOrIndexArray>()

/**
 * Query for the index of one or more components: a number, an array, `"all"`, `"none"` or `false`.
 */
external interface ModelFinderIndexQuery

/** Wraps a single index. */
fun ModelFinderIndexQuery(value: Double): ModelFinderIndexQuery = value.unsafeCast<ModelFinderIndexQuery>()

/** Wraps several indices. */
fun ModelFinderIndexQuery(values: Array<Double>): ModelFinderIndexQuery =
    values.unsafeCast<ModelFinderIndexQuery>()

/** Wraps `"all"`, `"none"` or another keyword. */
fun ModelFinderIndexQuery(value: String): ModelFinderIndexQuery =
    value.unsafeCast<ModelFinderIndexQuery>()

/** Wraps a boolean. */
fun ModelFinderIndexQuery(value: Boolean): ModelFinderIndexQuery =
    value.unsafeCast<ModelFinderIndexQuery>()

/**
 * Query for the id of one or more components: an [OptionId], an array of ids, or nothing.
 *
 * Upstream `ModelFinderIdQuery`.
 */
external interface ModelFinderIdQuery

/** Wraps a single id. */
fun ModelFinderIdQuery(value: OptionId): ModelFinderIdQuery = value.unsafeCast<ModelFinderIdQuery>()

/** Wraps several ids. */
fun ModelFinderIdQuery(values: Array<OptionId>): ModelFinderIdQuery = values.unsafeCast<ModelFinderIdQuery>()

/**
 * A data zoom bound: a percentage number, a value name, a number or a date.
 */
external interface DataZoomValue

/** Wraps a percentage number. */
fun DataZoomValue(value: Double): DataZoomValue = value.unsafeCast<DataZoomValue>()

/** Wraps a category name or a date string. */
fun DataZoomValue(value: String): DataZoomValue = value.unsafeCast<DataZoomValue>()

/** Wraps a date. */
fun DataZoomValue(value: Date): DataZoomValue = value.unsafeCast<DataZoomValue>()

/**
 * Visual style applied by a visual map.
 *
 * Upstream declares every numeric field as `number | number[]` and `color` as
 * `string | string[] | Dictionary<string>`; the array form maps one value per piece. The dictionary
 * form of `color` (used together with `categories`) is not modelled — cast it at the call site.
 */
external interface VisualOptionUnit {
    /** Symbol shape. */
    var symbol: String?

    /** Symbol size. */
    var symbolSize: DoubleOrDoubleArray?

    /** Color. */
    var color: StringOrStringArray?

    /** Alpha of the color, `0..1`. */
    var colorAlpha: DoubleOrDoubleArray?

    /** Opacity, `0..1`. */
    var opacity: DoubleOrDoubleArray?

    /** Lightness of the color, `0..1`. */
    var colorLightness: DoubleOrDoubleArray?

    /** Saturation of the color, `0..1`. */
    var colorSaturation: DoubleOrDoubleArray?

    /** Hue of the color, `0..360`. */
    var colorHue: DoubleOrDoubleArray?

    /** Decal pattern. */
    var decal: DecalObject?

    /** z-order offset. */
    var liftZ: Double?
}

/** @see VisualOptionUnit */
typealias VisualOptionPiecewise = VisualOptionUnit

/**
 * One piece of a piecewise visual map.
 */
external interface VisualPiece : VisualOptionUnit {
    /** Lower bound (inclusive). */
    var min: Double?

    /** Upper bound (inclusive). */
    var max: Double?

    /** Exclusive lower bound. */
    var gt: Double?

    /** Exclusive upper bound. */
    var lt: Double?

    /** Inclusive lower bound. */
    var gte: Double?

    /** Inclusive upper bound. */
    var lte: Double?

    /** Exact value. */
    var value: Double?

    /** Text shown for this piece. */
    var label: String?
}

/**
 * Configuration of the `inRange` / `outOfRange` controllers of a visual map.
 */
external interface VisualMapControllerOption {
    /** Style applied inside the selected range. */
    var inRange: VisualOptionUnit?

    /** Style applied outside the selected range. */
    var outOfRange: VisualOptionUnit?
}

/**
 * One target of a visual map.
 */
external interface VisualMapSeriesTarget {
    /** Index of the series. */
    var seriesIndex: Double?

    /** Id of the series. */
    var seriesId: OptionId?

    /** Dimension of the series. */
    var dimension: Double
}

/**
 * Universal transition animation between series types.
 */
external interface UniversalTransitionOption {
    /** Whether the transition is enabled. */
    var enabled: Boolean?

    /** Delay of one data item, given its index and the item count. */
    var delay: ((index: Double, count: Double) -> Double)?

    /** How shapes are divided: `"split"` or `"clone"`. */
    var divideShape: String?

    /**
     * Key used to match data items between the old and the new series:
     * `"id"`, `"name"`, a dimension name or a callback.
     */
    var seriesKey: StringOrStringArray?
}

/**
 * `boolean | UniversalTransitionOption`.
 */
external interface BooleanOrUniversalTransitionOption

/** Wraps a boolean. */
fun BooleanOrUniversalTransitionOption(value: Boolean): BooleanOrUniversalTransitionOption =
    value.unsafeCast<BooleanOrUniversalTransitionOption>()

/** Wraps a configuration object. */
fun BooleanOrUniversalTransitionOption(
    value: UniversalTransitionOption,
): BooleanOrUniversalTransitionOption = value.unsafeCast<BooleanOrUniversalTransitionOption>()

/**
 * Decal pattern configuration accepted by item and area styles.
 */
external interface DecalObject {
    /** Symbol of the pattern. */
    var symbol: StringOrStringArray?

    /** Size of one tile. */
    var symbolSize: Double?

    /** Whether to keep the symbol aspect ratio. */
    var symbolKeepAspect: Boolean?

    /** Color of the pattern. */
    var color: String?

    /** Background color of the pattern. */
    var backgroundColor: String?

    /** Horizontal dash spacing. */
    var dashArrayX: DecalDashArrayX?

    /** Vertical dash spacing. */
    var dashArrayY: DoubleOrDoubleArray?

    /**
     * Rotation of the pattern, in radians.
     *
     * Upstream hands the value to `zrender`'s `Pattern.rotation`, which the SVG renderer divides by
     * `PI` to get degrees; the official decal examples therefore pass multiples of `Math.PI`.
     */
    var rotation: Double?

    /** Maximum tile width. */
    var maxTileWidth: Double?

    /** Maximum tile height. */
    var maxTileHeight: Double?
}

/**
 * Horizontal dash spacing of a decal: a number or an array of numbers / number arrays.
 */
external interface DecalDashArrayX

/** Wraps a single spacing. */
fun DecalDashArrayX(value: Double): DecalDashArrayX = value.unsafeCast<DecalDashArrayX>()

/** Wraps an array of spacings, where each entry may itself be an array. */
fun DecalDashArrayX(values: Array<DoubleOrDoubleArray>): DecalDashArrayX =
    values.unsafeCast<DecalDashArrayX>()

/**
 * Setting of an item decal: a [DecalObject] or the string `"none"`.
 */
external interface DecalSetting

/** Wraps a decal configuration. */
fun DecalSetting(value: DecalObject): DecalSetting = value.unsafeCast<DecalSetting>()

/** Wraps `"none"`. */
fun DecalSetting(value: String): DecalSetting = value.unsafeCast<DecalSetting>()

/**
 * One decal or a list of decals, used by `aria.decal.decals`.
 */
external interface DecalList

/** Wraps a single decal. */
fun DecalList(value: DecalObject): DecalList = value.unsafeCast<DecalList>()

/** Wraps a list of decals. */
fun DecalList(values: Array<DecalObject>): DecalList = values.unsafeCast<DecalList>()

/**
 * `boolean | "single" | "multiple" | "series"`, used by the selection modes of a series or of a
 * legend.
 */
external interface SelectionMode

/** Wraps a boolean. */
fun SelectionMode(value: Boolean): SelectionMode = value.unsafeCast<SelectionMode>()

/** Wraps a mode name. */
fun SelectionMode(value: String): SelectionMode = value.unsafeCast<SelectionMode>()

/**
 * The documented values of [SelectionMode].
 */
object SelectionModes {
    /** One item is selected at a time. */
    inline val single: SelectionMode get() = SelectionMode("single")

    /** Several items are selected at a time. */
    inline val multiple: SelectionMode get() = SelectionMode("multiple")

    /** Selecting an item selects its whole series, used by the series option. */
    inline val series: SelectionMode get() = SelectionMode("series")
}

/**
 * `Dictionary<boolean> | "all"`, used by `SeriesOption.selectedMap`.
 */
external interface SelectedMap

/** Wraps the map of the selected data items, keyed by name or index. */
fun SelectedMap(value: Record<String, Boolean>): SelectedMap = value.unsafeCast<SelectedMap>()

/** Wraps `"all"`. */
fun SelectedMap(value: String): SelectedMap = value.unsafeCast<SelectedMap>()

/**
 * A date-like data value: `Date | string | number`.
 */
external interface OptionDataValueDate

/** Wraps a date. */
fun OptionDataValueDate(value: Date): OptionDataValueDate = value.unsafeCast<OptionDataValueDate>()

/** Wraps a date string. */
fun OptionDataValueDate(value: String): OptionDataValueDate = value.unsafeCast<OptionDataValueDate>()

/** Wraps a timestamp. */
fun OptionDataValueDate(value: Double): OptionDataValueDate = value.unsafeCast<OptionDataValueDate>()

/**
 * Layout returned by `convertToLayout`.
 */
external interface CoordinateSystemDataLayout {
    /** Layout rectangle. */
    var rect: RectLike?

    /** Rectangle of the content inside [rect]. */
    var contentRect: RectLike?

    /** Range of the matrix locators, when the coordinate system is a matrix. */
    var matrixXYLocatorRange: Array<Array<Double>>?
}

/**
 * A data coordinate handed to a coordinate system: one value, one value per dimension, or one
 * value-or-range per dimension.
 *
 * Upstream `CoordinateSystemDataCoord`. Used by `renderItem`'s `api.coord` / `api.layout` and by
 * the `coord` field of components placed on another coordinate system (a grid on a matrix, ...).
 *
 * ```kotlin
 * api.coord(CoordinateSystemDataCoord(arrayOf(OptionDataValue(1.0), OptionDataValue(2.0))))
 * ```
 */
external interface CoordinateSystemDataCoord

/** Wraps a single value. */
fun CoordinateSystemDataCoord(value: OptionDataValue): CoordinateSystemDataCoord =
    value.unsafeCast<CoordinateSystemDataCoord>()

/** Wraps one value per dimension. */
fun CoordinateSystemDataCoord(values: Array<OptionDataValue?>): CoordinateSystemDataCoord =
    values.unsafeCast<CoordinateSystemDataCoord>()

/**
 * Wraps one value or one `[min, max]` range per dimension.
 *
 * Upstream is `(value | value[])[]`; the nested array form cannot share the name of the flat form
 * in Kotlin/JS, so it is expressed as a separate factory.
 */
fun CoordinateSystemDataCoordRanges(values: Array<Array<OptionDataValue?>>): CoordinateSystemDataCoord =
    values.unsafeCast<CoordinateSystemDataCoord>()

/**
 * A visual value returned by `getVisual`: text, a number, a number array, a pattern or a gradient.
 */
external interface VisualValue

/** Wraps a text value. */
fun VisualValue(value: String): VisualValue = value.unsafeCast<VisualValue>()

/** Wraps a number. */
fun VisualValue(value: Double): VisualValue = value.unsafeCast<VisualValue>()

/** Wraps an array of numbers. */
fun VisualValue(values: Array<Double>): VisualValue = values.unsafeCast<VisualValue>()

/** Wraps a pattern. */
fun VisualValue(value: PatternObject): VisualValue = value.unsafeCast<VisualValue>()

/** Wraps a linear gradient. */
fun VisualValue(value: LinearGradientObject): VisualValue = value.unsafeCast<VisualValue>()

/** Wraps a radial gradient. */
fun VisualValue(value: RadialGradientObject): VisualValue = value.unsafeCast<VisualValue>()

/**
 * Layout box of a component, expressed as distances and sizes.
 */
external interface BoxLayoutOption {
    /** Width; a number or a percentage string. */
    var width: StringOrDouble?

    /** Height; a number or a percentage string. */
    var height: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Distance to the left side. */
    var left: StringOrDouble?
}

/**
 * A value that can be placed on a scale: `number | string | Date`.
 *
 * Used by the axis-related members of marker position options.
 */
external interface ScaleDataValue

/** Wraps a number. */
fun ScaleDataValue(value: Double): ScaleDataValue = value.unsafeCast<ScaleDataValue>()

/** Wraps a category name or a date string. */
fun ScaleDataValue(value: String): ScaleDataValue = value.unsafeCast<ScaleDataValue>()

/** Wraps a date. */
fun ScaleDataValue(value: Date): ScaleDataValue = value.unsafeCast<ScaleDataValue>()

/**
 * `ScaleDataValue | MarkerStatisticType`, used by `MarkerPositionOption.coord`.
 */
external interface ScaleDataValueOrMarkerStatisticType

/** Wraps an explicit value. */
fun ScaleDataValueOrMarkerStatisticType(value: ScaleDataValue): ScaleDataValueOrMarkerStatisticType =
    value.unsafeCast<ScaleDataValueOrMarkerStatisticType>()

/** Wraps a statistic such as `"average"`. */
fun ScaleDataValueOrMarkerStatisticType(value: MarkerStatisticType): ScaleDataValueOrMarkerStatisticType =
    value.unsafeCast<ScaleDataValueOrMarkerStatisticType>()

/**
 * `number | '-'`, used by data values that may be explicitly empty.
 */
external interface OptionDataValueNumeric

/** Wraps a number. */
fun OptionDataValueNumeric(value: Double): OptionDataValueNumeric =
    value.unsafeCast<OptionDataValueNumeric>()

/** Wraps `"-"`, which marks the value as empty. */
fun OptionDataValueNumeric(value: String): OptionDataValueNumeric =
    value.unsafeCast<OptionDataValueNumeric>()

/**
 * `number[] | "none"`, used by the visual options of a treemap level.
 */
external interface DoubleArrayOrNone

/** Wraps an array of numbers. */
fun DoubleArrayOrNone(values: Array<Double>): DoubleArrayOrNone = values.unsafeCast<DoubleArrayOrNone>()

/** Wraps `"none"`. */
fun DoubleArrayOrNone(value: String): DoubleArrayOrNone = value.unsafeCast<DoubleArrayOrNone>()

/**
 * `ColorString[] | "none"`, used by the color of a treemap level or node.
 */
external interface ColorArrayOrNone

/** Wraps an array of colors. */
fun ColorArrayOrNone(values: Array<ZRColor>): ColorArrayOrNone = values.unsafeCast<ColorArrayOrNone>()

/** Wraps `"none"`. */
fun ColorArrayOrNone(value: String): ColorArrayOrNone = value.unsafeCast<ColorArrayOrNone>()

/**
 * `ZRColor | ZRColor[]`: a single colour, or a palette cycled by the element index.
 *
 * Used where upstream widens a style type's colour parameter, for example
 * `SplitLineOption.lineStyle: LineStyleOption<ZRColor | ZRColor[]>`.
 */
external interface ZRColorOrArray

/** Wraps a single colour. */
fun ZRColorOrArray(value: ZRColor): ZRColorOrArray = value.unsafeCast<ZRColorOrArray>()

/** Wraps a palette of colours. */
fun ZRColorOrArray(values: Array<ZRColor>): ZRColorOrArray = values.unsafeCast<ZRColorOrArray>()

/**
 * `DecalObject[] | "none"`, used by the decal of a treemap level or node.
 */
external interface DecalArrayOrNone

/** Wraps an array of decals. */
fun DecalArrayOrNone(values: Array<DecalObject>): DecalArrayOrNone =
    values.unsafeCast<DecalArrayOrNone>()

/** Wraps `"none"`. */
fun DecalArrayOrNone(value: String): DecalArrayOrNone = value.unsafeCast<DecalArrayOrNone>()

/**
 * `boolean | SortOrder`, used by the `sort` option of a treemap.
 */
external interface BooleanOrSortOrder

/** Wraps a boolean. */
fun BooleanOrSortOrder(value: Boolean): BooleanOrSortOrder = value.unsafeCast<BooleanOrSortOrder>()

/** Wraps a sort order. */
fun BooleanOrSortOrder(value: SortOrder): BooleanOrSortOrder = value.unsafeCast<BooleanOrSortOrder>()
