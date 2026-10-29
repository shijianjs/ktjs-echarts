package echartsgl.options

import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echartsgl.CoordinateSystem3D
import echartsgl.DoubleOrAuto
import echartsgl.GlEnvironment
import kotlin.js.JsName

/**
 * One region of a [Map3DSeriesOption].
 *
 * The regions of the map are taken from the GeoJSON; the items of [Map3DSeriesOption.typedData]
 * override the data, the height and the style of the regions with the same name.
 */
external interface Map3DDataItemOption : OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the value is what a `visualMap` component maps to the color.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Height the region is extruded to; `regionHeight` of the series by default. */
    var height: Double?

    /** Style of the region. */
    var itemStyle: GlItemStyleOption?

    /** Label of the region. */
    var label: GlLabelOption?

    /** Emphasis state of the region. */
    var emphasis: Geo3DEmphasisOption?
}

/**
 * 3D map series, `series.type = "map3D"`.
 *
 * The 3D counterpart of the 2D [map][echarts.options.MapSeriesOption]: every region of a registered
 * GeoJSON is extruded, and the data can give a region its own height and color. On its own
 * [coordinateSystem] the series is a `geo3D` component with the map inside it; with `"mapbox3D"` or
 * `"maptalks3D"` it draws the regions of that map service instead, and the `geo3D` and scene
 * members of this option are ignored.
 */
external interface Map3DSeriesOption : SeriesOption, ShadingOption {
    /** Coordinate system: `"geo3D"`, `"mapbox3D"` or `"maptalks3D"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [Bar3DSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: CoordinateSystem3D?

    /** Index of the `geo3D` component the series is drawn in; its own map when not set. */
    var geo3DIndex: Double?

    /** Index of the `mapbox3D` component the series is drawn on. */
    var mapbox3DIndex: Double?

    /** Index of the `maptalks3D` component the series is drawn on. */
    var maptalks3DIndex: Double?

    /** Whether the series is shown. */
    var show: Boolean?

    /** Name of a map registered with `ECharts.registerMap`; `geo3D` coordinate system only. */
    var map: String?

    /** Distance between the map and the left edge of the canvas. */
    var left: StringOrDouble?

    /** Distance between the map and the top edge of the canvas. */
    var top: StringOrDouble?

    /** Width of the map inside the canvas. */
    var width: StringOrDouble?

    /** Height of the map inside the canvas. */
    var height: StringOrDouble?

    /** Width of the map box in the 3D scene. */
    var boxWidth: Double?

    /** Height of the map box, which contains both the regions and the series above them. */
    var boxHeight: Double?

    /** Depth of the map box; `"auto"` keeps the aspect ratio of the GeoJSON. */
    var boxDepth: DoubleOrAuto?

    /** Height of the regions; `boxHeight - regionHeight` is left for the series above them. */
    var regionHeight: Double?

    /**
     * Scale applied to the ratio between the width and the height of the map; the default is
     * `0.75`.
     *
     * Upstream reads it from the option but does not document it.
     */
    var aspectScale: Double?

    /** Environment map of the scene: `"auto"`, a color or a panoramic texture. */
    var environment: GlEnvironment?

    /** Plane the map stands on, which makes the scene look like a model on a table. */
    var groundPlane: Geo3DGroundPlaneOption?

    /** Label of a region. */
    @JsName("label")
    var typedLabel: GlLabelOption?

    /** Default style of the regions. */
    @JsName("itemStyle")
    var typedItemStyle: GlItemStyleOption?

    /** Style of a region while the mouse hovers over it. */
    @JsName("emphasis")
    var typedEmphasis: Geo3DEmphasisOption?

    /** Light of the scene. */
    var light: LightOption?

    /** Post-processing effects of the scene. */
    var postEffect: PostEffectOption?

    /** Temporal supersampling of the scene. */
    var temporalSuperSampling: TemporalSuperSamplingOption?

    /** Camera control. */
    var viewControl: ViewControlOption?

    /** Wireframe debug overlay. */
    var debug: GlDebugOption?

    /** Typed view of [SeriesOption.data] with the item type of a map region. */
    @JsName("data")
    var typedData: Array<Map3DDataItemOption?>?
}
