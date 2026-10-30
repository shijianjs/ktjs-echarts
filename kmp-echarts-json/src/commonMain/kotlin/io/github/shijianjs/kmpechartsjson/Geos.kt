package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.Serializable

/** A geographic coordinate system backed by a registered map. */
@Serializable
public class GeoOption {
    /** Component id used by `geoIndex` cross references. */
    public var id: String? = null

    /** Name of the map registered with `echarts.registerMap`. */
    public var map: String? = null

    /** Pan and zoom behavior: `true`, `false`, `"scale"` or `"move"`. */
    public var roam: BooleanOrString? = null

    /** Center of the map as `[longitude, latitude]` or percentages. */
    public var center: List<NumberOrString>? = null

    /** Initial zoom factor. */
    public var zoom: Double? = null

    /** Minimum and maximum zoom. */
    public var scaleLimit: ScaleLimit? = null

    /** Map from source map names to display names. */
    public var nameMap: Map<String, String>? = null

    /** Selection mode: `true`, `false`, `"single"` or `"multiple"`. */
    public var selectedMode: BooleanOrString? = null

    /** Region label. */
    public var label: LabelOption? = null

    /** Region style. */
    public var itemStyle: ItemStyleOption? = null

    /** Emphasis state. */
    public var emphasis: EmphasisOption? = null

    /** Selected state. */
    public var select: EmphasisOption? = null

    /** Blur state, used together with `emphasis.focus`. */
    public var blur: EmphasisOption? = null

    /** Per-region overrides. */
    public var regions: List<GeoRegion>? = null

    /** Whether the map ignores mouse events. */
    public var silent: Boolean? = null

    /** Aspect ratio applied to the map; defaults to Mercator's `0.75`. */
    public var aspectScale: Double? = null

    /** Bounding box as `[[west, south], [east, north]]`. */
    public var boundingCoords: List<List<Double>>? = null

    /** Layout center as `[x, y]` percentages, used instead of [left]/[top]. */
    public var layoutCenter: List<NumberOrString>? = null

    /** Layout size used together with [layoutCenter]. */
    public var layoutSize: NumberOrString? = null

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
}

/** Overrides for one region of a [GeoOption] or map series. */
@Serializable
public class GeoRegion {
    /** Region name as it appears in the map data. */
    public var name: String? = null

    /** Whether the region starts selected. */
    public var selected: Boolean? = null

    /** Region style. */
    public var itemStyle: ItemStyleOption? = null

    /** Region label. */
    public var label: LabelOption? = null

    /** Emphasis state of the region. */
    public var emphasis: EmphasisOption? = null

    /** Selected state of the region. */
    public var select: EmphasisOption? = null

    /** Blur state of the region. */
    public var blur: EmphasisOption? = null
}
