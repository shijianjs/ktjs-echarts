package echartsgl.options

import echarts.options.StringOrDouble
import echartsgl.GlEnvironment
import echartsgl.GlobeBlendTarget
import echartsgl.GlobeLayerType
import echartsgl.QualityType
import echartsgl.ShadingType
import echartsgl.TextureSource
import kotlin.js.Date
import kotlin.js.unsafeCast
import zrender.ZRColor

/**
 * Globe component: an earth on which the series with `coordinateSystem: "globe"` are drawn.
 *
 * The surface is a sphere textured with [baseTexture]; the band between [globeRadius] and
 * [globeOuterRadius] is used by the 3D bars, lines and symbols, the same way a `geo3D` uses the
 * space above its regions.
 *
 * The component is installed by [GlobeComponent][echartsgl.GlobeComponent].
 */
external interface GlobeOption : ShadingOption {
    /** Whether the globe is shown. */
    var show: Boolean?

    /** z-level of the WebGL layer; it must not be shared with a canvas component. */
    var zlevel: Double?

    /** Distance between the globe and the left edge of the canvas. */
    var left: StringOrDouble?

    /** Distance between the globe and the top edge of the canvas. */
    var top: StringOrDouble?

    /** Width of the globe inside the canvas. */
    var width: StringOrDouble?

    /** Height of the globe inside the canvas. */
    var height: StringOrDouble?

    /** Environment map: `"auto"`, a color or a panoramic texture. */
    var environment: GlEnvironment?

    /** Base color of the sphere, used where [baseTexture] does not cover it. */
    var baseColor: ZRColor?

    /** Texture of the surface; it can also be an ECharts instance drawn onto the sphere. */
    var baseTexture: TextureSource?

    /** Grayscale height texture used for bump mapping. */
    var heightTexture: TextureSource?

    /** Texture that moves the vertices along their normals; defaults to [heightTexture]. */
    var displacementTexture: TextureSource?

    /** Strength of the vertex displacement; `0` disables it. */
    var displacementScale: Double?

    /** Quality of the vertex displacement: `"low"`, `"medium"`, `"high"` or `"ultra"`. */
    var displacementQuality: QualityType?

    /** Radius of the earth. */
    var globeRadius: Double?

    /** Radius of the atmosphere-free space the series are drawn in. */
    var globeOuterRadius: Double?

    /** Light of the scene; [MainLightOption.time] sets the position of the sun. */
    var light: LightOption?

    /** Glow around the edge of the earth. */
    var atmosphere: GlobeAtmosphereOption?

    /** Post-processing effects of the scene. */
    var postEffect: PostEffectOption?

    /** Temporal supersampling of the scene. */
    var temporalSuperSampling: TemporalSuperSamplingOption?

    /** Camera control. */
    var viewControl: ViewControlOption?

    /** Extra textures drawn over or into the surface, such as clouds and night lights. */
    var layers: Array<GlobeLayerOption>?

    /** Wireframe debug overlay. */
    var debug: GlDebugOption?
}

/**
 * Glow around the edge of a [GlobeOption].
 *
 * The glow is a separate pass over the sphere silhouette; it is off by default.
 */
external interface GlobeAtmosphereOption {
    /** Whether the glow is drawn. */
    var show: Boolean?

    /** Distance of the glow from the surface, in scene units. */
    var offset: Double?

    /** Color of the glow. */
    var color: ZRColor?

    /** Falloff of the outer glow; a larger power makes it thinner. */
    var glowPower: Double?

    /** Falloff of the inner glow; a larger power makes it thinner. */
    var innerGlowPower: Double?
}

/**
 * One extra texture of a [GlobeOption].
 *
 * An `"overlay"` layer floats above the surface, a `"blend"` layer is mixed into the material of
 * the globe itself. Every layer is rebuilt from the option on each update.
 */
external interface GlobeLayerOption {
    /** Whether the layer is drawn. */
    var show: Boolean?

    /** Kind of the layer: `"overlay"` or `"blend"`. */
    var type: GlobeLayerType?

    /** Name of the layer; documented as the update key, but never read from the option. */
    var name: String?

    /** Channel a `"blend"` layer is mixed into: `"albedo"` or `"emission"`. */
    var blendTo: GlobeBlendTarget?

    /** Strength of the blend. */
    var intensity: Double?

    /** Shading of an `"overlay"` layer. */
    var shading: ShadingType?

    /** Distance of an `"overlay"` layer from the surface, in scene units. */
    var distance: Double?

    /** Texture of the layer. */
    var texture: TextureSource?
}

/**
 * Time of the sun of a [GlobeOption], read from `light.main.time`.
 *
 * Upstream passes it to `echarts.number.parseDate`, so a [Date], a millisecond timestamp or a date
 * string all work. The system time is used when it is not set.
 */
external interface GlobeSunTime

/** Wraps a point in time. */
fun GlobeSunTime(date: Date): GlobeSunTime = date.unsafeCast<GlobeSunTime>()

/** Wraps a millisecond timestamp. */
fun GlobeSunTime(timestamp: Double): GlobeSunTime = timestamp.unsafeCast<GlobeSunTime>()

/** Wraps a date string understood by `echarts.number.parseDate`. */
fun GlobeSunTime(value: String): GlobeSunTime = value.unsafeCast<GlobeSunTime>()
