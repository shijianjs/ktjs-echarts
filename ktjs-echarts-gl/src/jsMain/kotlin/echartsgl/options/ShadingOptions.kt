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
}

/**
 * Shading of the 3D graphics of a series or component.
 *
 * `"color"` draws the flat colors only, `"lambert"` adds classic lighting, and `"realistic"` uses
 * physically based rendering driven by [realisticMaterial] and the environment of the scene.
 *
 * The interface is mixed into the GL series and into the geographic components, so the same
 * material options apply wherever a surface is shaded.
 */
external interface ShadingOption : GlMaterialSetOption {
    /** Shading model: `"color"`, `"lambert"` or `"realistic"`. */
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
