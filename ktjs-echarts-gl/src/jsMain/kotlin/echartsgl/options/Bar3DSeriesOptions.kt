package echartsgl.options

import echarts.options.LabelFormatter
import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValue
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOption
import echarts.options.TextStyleOption
import echartsgl.CoordinateSystem3D
import echartsgl.GlBlendMode
import echartsgl.GlColor
import echartsgl.GridPlaneType
import kotlin.js.JsName
import kotlin.js.unsafeCast

/*
 * 3D bar series.
 *
 * `echarts-gl` ships no TypeScript declarations, so the option and data item interfaces are written
 * from the `option-gl` documentation and from the `defaultOption` of `Bar3DSeries.js`. Where the
 * GL option differs from the 2D series options in the core module (labels carry a nested
 * `textStyle`, item colors may be HDR arrays) the GL types are declared here rather than reused.
 */

/**
 * Label of a GL series or data item.
 *
 * Unlike the 2D [echarts.options.LabelOption] it has no `position`; the label is always drawn next
 * to its graphic and its text style is nested in [textStyle] instead of being flattened.
 */
external interface GlLabelOption {
    /** Whether the label is shown. */
    var show: Boolean?

    /** Distance to the graphic, in pixels in screen space and in scene units for surfaces. */
    var distance: Double?

    /** Formatter: a template string such as `"{b}: {c}"` or a callback. */
    var formatter: LabelFormatter?

    /** Text style of the label. */
    var textStyle: TextStyleOption?
}

/**
 * Style of a GL graphic, such as a bar or a symbol.
 */
external interface GlItemStyleOption {
    /** Fill color. See [GlColor] for the HDR array form. */
    var color: GlColor?

    /** Opacity, `0..1`. */
    var opacity: Double?
}

/**
 * Emphasis (hover) state of a GL series or data item.
 */
external interface GlEmphasisOption {
    /** Whether the emphasis state is disabled. */
    var disabled: Boolean?

    /** Label style in the emphasis state. */
    var label: GlLabelOption?

    /** Style of the graphic in the emphasis state. */
    var itemStyle: GlItemStyleOption?
}

/**
 * One data item of a [Bar3DSeriesOption].
 *
 * The upstream form of an item is an array: `[x, y, z]` in a `cartesian3D` coordinate system,
 * `[lng, lat, value]` in `geo3D` and `globe`. Extra values in the array can be mapped by a
 * `visualMap` component.
 */
external interface Bar3DDataItemOption : OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the inherited [value] stays the generic single value.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Style of the bar of this item. */
    var itemStyle: GlItemStyleOption?

    /** Label of this item. */
    var label: GlLabelOption?

    /** Emphasis state of this item. */
    var emphasis: GlEmphasisOption?
}

/**
 * One entry of [Bar3DSeriesOption.typedData].
 *
 * Upstream `OptionDataValue[] | Bar3DDataItemOption`; the single-value form is accepted as well,
 * although a bar needs all three coordinates.
 */
external interface Bar3DDataItem

/** Wraps a single value. */
fun Bar3DDataItem(value: OptionDataValue): Bar3DDataItem = value.unsafeCast<Bar3DDataItem>()

/** Wraps a row of values, for example an `[x, y, z]` triple. */
fun Bar3DDataItem(values: Array<OptionDataValue?>): Bar3DDataItem = values.unsafeCast<Bar3DDataItem>()

/** Wraps a data item object. */
fun Bar3DDataItem(value: Bar3DDataItemOption): Bar3DDataItem = value.unsafeCast<Bar3DDataItem>()

/**
 * 3D bar series, `series.type = "bar3D"`.
 *
 * Drawn in a [Grid3DOption], or on a `geo3D` / `globe` component; a bar is sized by its value, so
 * the `visualMap` component can map data to height and color.
 */
external interface Bar3DSeriesOption : SeriesOption, ShadingOption {
    /** Coordinate system: `"cartesian3D"`, `"geo3D"` or `"globe"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [LineSeriesOption.typedCoordinateSystem][echarts.options.LineSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem3D?

    /** Index of the [Grid3DOption] the series is drawn in. */
    var grid3DIndex: Double?

    /** Index of the `geo3D` component the series is drawn in. */
    var geo3DIndex: Double?

    /** Index of the `globe` component the series is drawn in. */
    var globeIndex: Double?

    /** Size of the bevel on the bar edges, `0..1`; `0` leaves sharp edges. */
    var bevelSize: Double?

    /** Smoothness of the bevel; a larger value gives a rounder edge. */
    var bevelSmoothness: Double?

    /** Stack group name; bars of the same group stack along the value dimension. */
    var stack: String?

    /** Minimum height of a bar. */
    var minHeight: Double?

    /** Plane of the grid a bar stands on: `"xy"`, `"yz"` or `"xz"`; `cartesian3D` only. */
    var onGridPlane: GridPlaneType?

    /** Blend mode of the bars: `"source-over"` or `"lighter"`. */
    @JsName("blendMode")
    var typedBlendMode: GlBlendMode?

    /** Style of the bars. */
    @JsName("itemStyle")
    var typedItemStyle: GlItemStyleOption?

    /** Label of the bars. */
    @JsName("label")
    var typedLabel: GlLabelOption?

    /** Emphasis state of the bars. */
    @JsName("emphasis")
    var typedEmphasis: GlEmphasisOption?

    /** Typed view of [SeriesOption.data] with the item type of a 3D bar series. */
    @JsName("data")
    var typedData: Array<Bar3DDataItem?>?
}
