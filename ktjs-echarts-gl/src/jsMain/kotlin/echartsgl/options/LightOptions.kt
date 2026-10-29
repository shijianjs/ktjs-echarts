package echartsgl.options

import echartsgl.QualityType
import zrender.ZRColor

/**
 * Light of a 3D component.
 *
 * Only the components and series that use a shading other than `"color"` are lit, so the option is
 * ignored while `shading` is `"color"`.
 */
external interface LightOption {
    /** Main light source; in a `globe` it is the sun. */
    var main: MainLightOption?

    /** Ambient light, which lifts the parts the main light does not reach. */
    var ambient: AmbientLightOption?

    /** Environment light baked into a cubemap texture. */
    var ambientCubemap: AmbientCubemapLightOption?
}

/**
 * Main light source of a [LightOption].
 */
external interface MainLightOption {
    /** Color of the light. */
    var color: ZRColor?

    /** Intensity of the light. */
    var intensity: Double?

    /** Whether the light casts shadows; it costs an extra render pass per frame. */
    var shadow: Boolean?

    /** Quality of the shadow map: `"low"`, `"medium"`, `"high"` or `"ultra"`. */
    var shadowQuality: QualityType?

    /** Vertical direction of the light in degrees. */
    var alpha: Double?

    /** Horizontal direction of the light in degrees. */
    var beta: Double?

    /**
     * Time of the sun; a `globe` only, where the light is the sun and its position follows the
     * time of day. The system time is used when it is not set.
     */
    var time: GlobeSunTime?
}

/**
 * Ambient light of a [LightOption].
 */
external interface AmbientLightOption {
    /** Color of the ambient light. */
    var color: ZRColor?

    /** Intensity of the ambient light. */
    var intensity: Double?
}

/**
 * Environment light of a [LightOption], sampled from a panoramic cubemap.
 *
 * The texture can be an `.hdr` panorama or a regular image; its diffuse and specular contributions
 * are tuned separately.
 */
external interface AmbientCubemapLightOption {
    /** URL of the panorama. */
    var texture: String?

    /** Exposure applied while the HDR texture is decoded. */
    var exposure: Double?

    /** Intensity of the diffuse contribution. */
    var diffuseIntensity: Double?

    /** Intensity of the specular contribution; `realistic` shading only. */
    var specularIntensity: Double?
}
