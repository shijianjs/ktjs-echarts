package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/** Kind of a `visualMap` component. */
@Serializable
public enum class VisualMapType {
    /** Continuous gradient mapping. */
    @SerialName("continuous")
    Continuous,

    /** Discrete piecewise mapping. */
    @SerialName("piecewise")
    Piecewise,
}

/**
 * `visualMap` component, mapping data values to colors and symbols.
 *
 * [Continuous] or [VisualMapType.Piecewise] select which properties apply; the rest are ignored by
 * ECharts.
 */
@Serializable
public class VisualMapOption {
    /** Mapping kind. */
    public var type: VisualMapType? = null

    /** Whether the component is shown. */
    public var show: Boolean? = null

    /** Component id used by cross references. */
    public var id: String? = null

    /** Indices of the series this component applies to. */
    public var seriesIndex: List<Int>? = null

    /** Minimum value of the data range. */
    public var min: Double? = null

    /** Maximum value of the data range. */
    public var max: Double? = null

    /** Whether the minimum value itself is excluded. */
    public var minOpen: Boolean? = null

    /** Whether the maximum value itself is excluded. */
    public var maxOpen: Boolean? = null

    /** Number of pieces of a continuous component. */
    public var splitNumber: Int? = null

    /** Explicit pieces of a piecewise component. */
    public var pieces: List<VisualMapPiece>? = null

    /** Layout direction. */
    public var orient: Orient? = null

    /** Width of one color block. */
    public var itemWidth: Double? = null

    /** Height of one color block. */
    public var itemHeight: Double? = null

    /** Alignment of the component: `"auto"`, `"left"` or `"right"`. */
    public var align: String? = null

    /** `[low, high]` texts shown at both ends of a continuous component. */
    public var text: List<String>? = null

    /** Gap between the component and [text]. */
    public var textGap: Double? = null

    /** Number of decimals of displayed values. */
    public var precision: Int? = null

    /** Symbol used by pieces that map to a symbol. */
    public var itemSymbol: String? = null

    /** Data dimension the mapping reads; a name or an index. */
    public var dimension: NumberOrString? = null

    /** Visual properties of values inside the range. */
    public var inRange: VisualMapRange? = null

    /** Visual properties of values outside the range. */
    public var outOfRange: VisualMapRange? = null

    /** In-range and out-of-range visuals of the draggable controller; a render callback is not JSON. */
    public var controller: JsonElement? = null

    /** Whether the range is continuously adjustable. */
    public var calculable: Boolean? = null

    /** Whether the chart updates while dragging. */
    public var realtime: Boolean? = null

    /** Initial selection of pieces: a number, indices or `"all"`. */
    public var selectedMode: BooleanOrString? = null

    /** Whether hovering a data item highlights the mapped value. */
    public var hoverLink: Boolean? = null

    /** Selected range of a continuous component. */
    public var range: List<Double>? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Distance to the left edge. */
    public var left: NumberOrString? = null

    /** Distance to the top edge. */
    public var top: NumberOrString? = null

    /** Distance to the right edge. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge. */
    public var bottom: NumberOrString? = null

    /** Label text style. */
    public var textStyle: TextStyleOption? = null

    /** Item style of the color blocks. */
    public var itemStyle: ItemStyleOption? = null

    /** Emphasis state. */
    public var emphasis: EmphasisOption? = null
}

/**
 * One piece of a piecewise visual map.
 *
 * A piece is defined either by [min]/[max] or by one of the exclusive bounds [lt], [lte], [gt],
 * [gte]; [value] matches one exact value.
 */
@Serializable
public class VisualMapPiece {
    /** Lower bound, inclusive. */
    public var min: Double? = null

    /** Upper bound, inclusive. */
    public var max: Double? = null

    /** Exclusive upper bound. */
    public var lt: Double? = null

    /** Inclusive upper bound. */
    public var lte: Double? = null

    /** Exclusive lower bound. */
    public var gt: Double? = null

    /** Inclusive lower bound. */
    public var gte: Double? = null

    /** Exact value matched by this piece. */
    public var value: Double? = null

    /** Label of the piece; defaults to the bound description. */
    public var label: String? = null

    /** Color applied to values in the piece. */
    public var color: EChartsColor? = null

    /** Symbol size applied to values in the piece. */
    public var symbolSize: NumberOrStringOrNumbers? = null

    /** Opacity applied to values in the piece. */
    public var opacity: Double? = null
}

/** Visual properties a visual map applies to the matching values. */
@Serializable
public class VisualMapRange {
    /** Color ramp; one color at each stop of the data range. */
    public var color: List<EChartsColor>? = null

    /** Alpha ramp, one value per color. */
    public var colorAlpha: List<Double>? = null

    /** Opacity ramp. */
    public var opacity: List<Double>? = null

    /** Symbol size ramp. */
    public var symbolSize: List<Double>? = null

    /** Lightness ramp. */
    public var colorLightness: List<Double>? = null

    /** Saturation ramp. */
    public var colorSaturation: List<Double>? = null

    /** Hue ramp. */
    public var colorHue: List<Double>? = null

    /** Symbol applied to values in the range. */
    public var symbol: String? = null
}
