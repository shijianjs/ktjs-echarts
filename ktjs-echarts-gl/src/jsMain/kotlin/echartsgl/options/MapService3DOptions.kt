package echartsgl.options

import echartsgl.DoubleOrAuto
import kotlin.js.JsAny

/*
 * Map services that replace the `geo3D` component with a tiled map.
 *
 * The map itself is a third-party SDK (`mapbox-gl-js` for `mapbox3D`, `maptalks` for `maptalks3D`)
 * that has to be loaded by the page before the component can be used and that holds its own access
 * token; neither is part of the option.
 */

/**
 * Members shared by [Mapbox3DOption] and [Maptalks3DOption].
 *
 * The camera is described by the same members the map services use: [center], [zoom], [pitch] and
 * [bearing].
 */
external interface MapService3DOption {
    /**
     * Whether the map ignores mouse picking.
     *
     * Upstream reads it from the component model but does not document it.
     */
    var silent: Boolean?

    /** z-level of the WebGL layer; it must not be shared with a canvas component. */
    var zlevel: Double?

    /** Center of the map, `[longitude, latitude]`. */
    var center: Array<Double>?

    /** Zoom level of the map. */
    var zoom: Double?

    /** Pitch of the camera in degrees; `0` looks straight down. */
    var pitch: Double?

    /** Bearing of the camera in degrees, i.e. the direction the map is rotated into. */
    var bearing: Double?

    /** Scale applied to the altitude of the series drawn on the map. */
    var altitudeScale: Double?

    /**
     * Height of the scene box above the map; `"auto"` derives it from [altitudeScale] and the data.
     */
    var boxHeight: DoubleOrAuto?

    /** Light of the scene. */
    var light: LightOption?

    /** Post-processing effects of the scene. */
    var postEffect: PostEffectOption?

    /** Temporal supersampling of the scene. */
    var temporalSuperSampling: TemporalSuperSamplingOption?
}

/**
 * `mapbox3D` component: a Mapbox GL map with the 3D series drawn on top of it.
 *
 * The page has to load `mapbox-gl-js` and set its access token (`mapboxgl.accessToken`) before the
 * component is used. `bar3D`, `line3D`, `scatter3D`, `lines3D`, `polygons3D` and `map3D` series can
 * be drawn on it by setting their `coordinateSystem` to `"mapbox3D"`.
 *
 * The `shading` option of the component documentation is not read by upstream, so it is not
 * modelled here.
 */
external interface Mapbox3DOption : MapService3DOption {
    /**
     * Style of the map: a Mapbox style URL such as `"mapbox://styles/mapbox/dark-v9"`, or a style
     * object in the Mapbox style specification.
     *
     * Upstream passes it to `mapbox.setStyle`, so it is either a string or an object.
     */
    var style: JsAny?
}

/**
 * `maptalks3D` component: an maptalks map with the 3D series drawn on top of it.
 *
 * The page has to load `maptalks` and its tile layer before the component is used. The series are
 * attached by setting their `coordinateSystem` to `"maptalks3D"`.
 */
external interface Maptalks3DOption : MapService3DOption {
    /** URL template of the raster tiles, in the `{s}` / `{z}` / `{x}` / `{y}` form of the services. */
    var urlTemplate: String?

    /** Attribution line shown at the corner of the map. */
    var attribution: String?
}
