package echartsgl.options

import echarts.options.StringOrDouble
import echartsgl.DoubleOrAuto
import echartsgl.GlColor
import echartsgl.GlEnvironment

/**
 * 3D geographic coordinate system, drawn from a GeoJSON registered with `ECharts.registerMap`.
 *
 * The map is the same one the 2D [geo][echarts.options.GeoOption] component uses. Every polygon
 * of the GeoJSON is extruded into a box; the space above the regions is where the series with
 * `coordinateSystem: "geo3D"` are drawn.
 *
 * The component is installed by [Geo3DComponent][echartsgl.Geo3DComponent] and has to be used
 * together with a registered map, otherwise the coordinate system cannot be created.
 */
external interface Geo3DOption : ShadingOption {
    /** Whether the component is shown; hiding it also hides the series drawn in it. */
    var show: Boolean?

    /**
     * Whether the regions ignore mouse picking.
     *
     * Upstream reads it from the component model to decide picking, but does not document it.
     */
    var silent: Boolean?

    /** z-level of the WebGL layer; it must not be shared with a canvas component. */
    var zlevel: Double?

    /** Distance between the component and the left edge of the canvas. */
    var left: StringOrDouble?

    /** Distance between the component and the top edge of the canvas. */
    var top: StringOrDouble?

    /** Width of the component inside the canvas. */
    var width: StringOrDouble?

    /** Height of the component inside the canvas. */
    var height: StringOrDouble?

    /** Name of a map registered with `ECharts.registerMap`. */
    var map: String?

    /** Width of the map box in the 3D scene. */
    var boxWidth: Double?

    /** Height of the map box, which contains both the regions and the series above them. */
    var boxHeight: Double?

    /** Depth of the map box; `"auto"` keeps the aspect ratio of the GeoJSON. */
    var boxDepth: DoubleOrAuto?

    /**
     * Scale applied to the ratio between the width and the height of the map; the default is
     * `0.75`.
     *
     * Upstream reads it from the option but does not document it.
     */
    var aspectScale: Double?

    /** Height of an area; `boxHeight - regionHeight` is left for the series drawn above it. */
    var regionHeight: Double?

    /** Environment map of the scene: `"auto"`, a color or a panoramic texture. */
    var environment: GlEnvironment?

    /** Plane the map stands on, which makes the scene look like a model on a table. */
    var groundPlane: Geo3DGroundPlaneOption?

    /** Label of a region. */
    var label: GlLabelOption?

    /** Default style of the regions. */
    var itemStyle: GlItemStyleOption?

    /** Style of a region while the mouse hovers over it. */
    var emphasis: Geo3DEmphasisOption?

    /** Per-region overrides, matched to the GeoJSON by name. */
    var regions: Array<Geo3DRegionOption>?

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
}

/**
 * Ground plane of a [Geo3DOption].
 *
 * The plane has its own materials; when they are omitted the material of the component is used.
 */
external interface Geo3DGroundPlaneOption : GlMaterialSetOption {
    /** Whether the ground is shown. */
    var show: Boolean?

    /** Color of the ground. */
    var color: GlColor?
}

/**
 * Emphasis (hover) state of the regions of a [Geo3DOption].
 */
external interface Geo3DEmphasisOption {
    /** Label style in the emphasis state. */
    var label: GlLabelOption?

    /** Style of the region in the emphasis state. */
    var itemStyle: GlItemStyleOption?
}

/**
 * Overrides of one region of a [Geo3DOption].
 *
 * The region is matched to the GeoJSON feature by [name]; the other members have the same meaning
 * as their component-level counterparts.
 */
external interface Geo3DRegionOption {
    /** Name of the region, as written in `properties.name` of the GeoJSON feature. */
    var name: String?

    /**
     * Height of the region; the component-level `regionHeight` is used when it is not set.
     *
     * The option documentation calls this member `regionHeight`, but upstream reads `height`
     * (`Geo3DBuilder`).
     */
    var height: Double?

    /** Style of the region. */
    var itemStyle: GlItemStyleOption?

    /** Label of the region. */
    var label: GlLabelOption?

    /** Style of the region while the mouse hovers over it. */
    var emphasis: Geo3DEmphasisOption?
}
