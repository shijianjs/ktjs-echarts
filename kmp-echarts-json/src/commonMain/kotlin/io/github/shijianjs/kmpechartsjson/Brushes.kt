package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/** Box selection ("brush") settings. */
@Serializable
public class BrushOption {
    /** Tools shown in the toolbox, for example `["rect", "clear"]`. */
    public var toolbox: List<String>? = null

    /** Series linked to the selection; `"all"` links every series. */
    public var brushLink: JsonElement? = null

    /** Series the selection applies to. */
    public var seriesIndex: List<Int>? = null

    /** Geo components the selection applies to. */
    public var geoIndex: List<Int>? = null

    /** X axes the selection applies to. */
    public var xAxisIndex: List<Int>? = null

    /** Y axes the selection applies to. */
    public var yAxisIndex: List<Int>? = null

    /** Initial selection shape: `"rect"`, `"polygon"`, `"lineX"`, `"lineY"` or `"none"`. */
    public var brushType: String? = null

    /** Selection accumulation: `"single"` or `"multiple"`. */
    public var brushMode: String? = null

    /** Whether the selected area can be moved and resized. */
    public var transformable: Boolean? = null

    /** Selected area style. */
    public var brushStyle: ItemStyleOption? = null

    /** Throttle type of the selection event: `"debounce"` or `"fixRate"`. */
    public var throttleType: String? = null

    /** Throttle delay in milliseconds. */
    public var throttleDelay: Double? = null

    /** Whether clicking the chart clears the selection. */
    public var removeOnClick: Boolean? = null

    /** Style of elements inside the selection. */
    public var inBrush: EmphasisOption? = null

    /** Style of elements outside the selection. */
    public var outOfBrush: EmphasisOption? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null
}
