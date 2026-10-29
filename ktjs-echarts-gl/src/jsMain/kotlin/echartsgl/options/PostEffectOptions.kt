package echartsgl.options

import echartsgl.BooleanOrAuto
import echartsgl.QualityType
import echartsgl.TextureSource
import kotlin.js.JsName

/**
 * Post-processing effects of a 3D component.
 *
 * The effects need a render pass per frame, so they are disabled by default. When [enable] turns
 * them on, WebGL's multisample anti-aliasing stops working; use [TemporalSuperSamplingOption]
 * rather than FXAA to keep text and lines sharp.
 */
external interface PostEffectOption {
    /** Whether post-processing is enabled. */
    var enable: Boolean?

    /** Bloom, the glow around bright areas. */
    var bloom: BloomOption?

    /** Depth of field; its alias `"DOF"` takes precedence when both are set. */
    var depthOfField: DepthOfFieldOption?

    /** Alias of [depthOfField]; upstream reads it before [depthOfField]. */
    @JsName("DOF")
    var dof: DepthOfFieldOption?

    /** Screen space ambient occlusion; alias [ssao] takes precedence when both are set. */
    var screenSpaceAmbientOcclusion: ScreenSpaceAmbientOcclusionOption?

    /** Alias of [screenSpaceAmbientOcclusion]; upstream reads it first. */
    @JsName("SSAO")
    var ssao: ScreenSpaceAmbientOcclusionOption?

    /** Screen space reflection; alias [ssr] takes precedence when both are set. */
    var screenSpaceReflection: ScreenSpaceReflectionOption?

    /** Alias of [screenSpaceReflection]; upstream reads it first. */
    @JsName("SSR")
    var ssr: ScreenSpaceReflectionOption?

    /** Color correction, exposure and the lookup texture. */
    var colorCorrection: ColorCorrectionOption?

    /** Edge outline highlighting. */
    var edge: EdgeOption?

    /** Fast approximate anti-aliasing. */
    @JsName("FXAA")
    var fxaa: FxaaOption?
}

/**
 * Bloom of a [PostEffectOption].
 */
external interface BloomOption {
    /** Whether the bloom is enabled. */
    var enable: Boolean?

    /** Intensity of the glow. */
    var intensity: Double?
}

/**
 * Depth of field of a [PostEffectOption]; also known as `"DOF"`.
 */
external interface DepthOfFieldOption {
    /** Whether the depth of field is enabled. */
    var enable: Boolean?

    /** Distance of the focused area from the camera. */
    var focalDistance: Double?

    /** Depth of the fully sharp band around [focalDistance]. */
    var focalRange: Double?

    /** Aperture of the simulated lens; a smaller value gives a shallower depth of field. */
    var fstop: Double?

    /** Blur radius outside the focused area. */
    var blurRadius: Double?

    /** Sampling quality, which trades speed for smoothness. */
    var quality: QualityType?
}

/**
 * Screen space ambient occlusion of a [PostEffectOption]; also known as `"SSAO"`.
 */
external interface ScreenSpaceAmbientOcclusionOption {
    /** Whether the occlusion is enabled. */
    var enable: Boolean?

    /** Sampling radius; a larger radius looks more natural but needs a higher [quality]. */
    var radius: Double?

    /** Sampling quality. */
    var quality: QualityType?

    /** Strength of the darkening. */
    var intensity: Double?
}

/**
 * Screen space reflection of a [PostEffectOption]; also known as `"SSR"`.
 */
external interface ScreenSpaceReflectionOption {
    /** Whether the reflection is enabled. */
    var enable: Boolean?

    /** Sampling quality. */
    var quality: QualityType?

    /** Roughness above which a surface stops reflecting. */
    var maxRoughness: Double?

    /** Whether the physically based reflection model is used. */
    var physical: Boolean?
}

/**
 * Color correction of a [PostEffectOption].
 */
external interface ColorCorrectionOption {
    /** Whether the correction is enabled. */
    var enable: Boolean?

    /** Lookup texture that maps the scene colors to the corrected ones. */
    var lookupTexture: TextureSource?

    /** Exposure of the image. */
    var exposure: Double?

    /** Brightness of the image. */
    var brightness: Double?

    /** Contrast of the image. */
    var contrast: Double?

    /** Saturation of the image. */
    var saturation: Double?
}

/**
 * Edge outline of a [PostEffectOption].
 *
 * The effect is read by upstream but is not part of its option documentation.
 */
external interface EdgeOption {
    /** Whether the outline is enabled. */
    var enable: Boolean?

    /** Color of the outline, as `[r, g, b, a]` channels. */
    var color: Array<Double>?
}

/**
 * Fast approximate anti-aliasing of a [PostEffectOption]; also known as `"FXAA"`.
 */
external interface FxaaOption {
    /** Whether the anti-aliasing is enabled. */
    var enable: Boolean?
}

/**
 * Temporal supersampling of a 3D component.
 *
 * Once the picture is still, it is sampled several times per pixel and averaged, which
 * anti-aliases the scene and progressively improves depth of field, SSAO and shadows.
 */
external interface TemporalSuperSamplingOption {
    /** Whether supersampling is enabled; the default is `"auto"`, i.e. follow [PostEffectOption.enable]. */
    var enable: BooleanOrAuto?
}
