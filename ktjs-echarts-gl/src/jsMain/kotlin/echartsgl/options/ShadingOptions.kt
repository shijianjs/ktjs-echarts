package echartsgl.options

import echarts.options.DoubleOrDoubleArray
import echartsgl.NumberOrTexture
import echartsgl.ShadingType
import echartsgl.TextureSource

/**
 * One material per shading model; the model itself selects which of them is used.
 */
external interface GlMaterialSetOption {
    /** Material used while the shading is `"realistic"`. */
    var realisticMaterial: RealisticMaterialOption?

    /** Material used while the shading is `"lambert"`. */
    var lambertMaterial: LambertMaterialOption?

    /** Material used while the shading is `"color"`. */
    var colorMaterial: ColorMaterialOption?

    /** Material used while the shading is `"hatching"`. */
    var hatchingMaterial: HatchingMaterialOption?
}

/**
 * Shading of the 3D graphics of a series or component.
 *
 * `"color"` draws the flat colors only, `"lambert"` adds classic lighting, `"realistic"` uses
 * physically based rendering driven by [realisticMaterial] and the environment of the scene, and
 * `"hatching"` turns the lighting into hand-drawn hatch textures.
 *
 * The interface is mixed into the GL series and into the geographic components, so the same
 * material options apply wherever a surface is shaded.
 */
external interface ShadingOption : GlMaterialSetOption {
    /** Shading model: `"color"`, `"lambert"`, `"realistic"` or `"hatching"`. */
    var shading: ShadingType?
}

/**
 * Members shared by every GL material.
 */
external interface GlMaterialOption {
    /** Detail texture, stretched over the surface. */
    var detailTexture: TextureSource?

    /**
     * Number of times the material textures are tiled; the default stretches them once over the
     * surface. A pair sets the two axes separately. An axis tiled more than once needs a texture
     * whose width and height are powers of two.
     */
    var textureTiling: DoubleOrDoubleArray?

    /** Offset of the material textures; a pair offsets the two axes separately. */
    var textureOffset: DoubleOrDoubleArray?
}

/**
 * Physically based material of a [ShadingOption]; used while `shading` is `"realistic"`.
 */
external interface RealisticMaterialOption : GlMaterialOption {
    /** Roughness, `0` for a mirror and `1` for a fully diffuse surface, or a roughness map. */
    var roughness: NumberOrTexture?

    /** Metalness, `0` for a dielectric and `1` for a metal, or a metalness map. */
    var metalness: NumberOrTexture?

    /** Raises or lowers the overall roughness read from the roughness map. */
    var roughnessAdjust: Double?

    /** Raises or lowers the overall metalness read from the metalness map. */
    var metalnessAdjust: Double?

    /** Normal map that adds surface detail without extra geometry. */
    var normalTexture: TextureSource?
}

/**
 * Lambert material of a [ShadingOption]; used while `shading` is `"lambert"`.
 */
external interface LambertMaterialOption : GlMaterialOption

/**
 * Flat material of a [ShadingOption]; used while `shading` is `"color"`.
 */
external interface ColorMaterialOption : GlMaterialOption

/**
 * Hand-drawn material of a [ShadingOption]; used while `shading` is `"hatching"`.
 *
 * The lambert tone of the surface selects between six hatch textures, which makes the surface look
 * like it was drawn with a pen. Upstream also declares a `paperColor` in the default option, but
 * `graphicGL.setMaterialFromModel` never reads it, so it is not modelled here.
 */
external interface HatchingMaterialOption : GlMaterialOption {
    /**
     * Six hatch textures, from the darkest to the lightest.
     *
     * Upstream reads the option but does not document it, and reports an error when fewer than six
     * textures are given.
     */
    var hatchingTextures: Array<TextureSource?>?
}
