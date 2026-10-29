package echartsgl

import echarts.SeriesType
import kotlin.js.unsafeCast
import web.html.HTMLCanvasElement
import web.html.HTMLImageElement
import zrender.ZRColor

/*
 * String-literal unions of `echarts-gl`.
 *
 * `echarts-gl` ships no TypeScript declarations, so these types are derived from the option
 * documentation (`option-gl` in apache/echarts-doc) and from the values the source accepts. The
 * constants compile to the bare string, so they are interchangeable with the literals they replace.
 */

/**
 * Coordinate system a GL series is drawn in.
 */
sealed external interface CoordinateSystem3D

/** Wraps a value that comes from JavaScript. Prefer the constants in [CoordinateSystems3D]. */
inline fun CoordinateSystem3D(value: String): CoordinateSystem3D = value.unsafeCast<CoordinateSystem3D>()

/**
 * The values of [CoordinateSystem3D].
 */
object CoordinateSystems3D {
    /** 3D cartesian coordinate system created by [Grid3DComponent]. */
    inline val cartesian3D: CoordinateSystem3D get() = CoordinateSystem3D("cartesian3D")

    /** 3D geographic coordinate system created by [Geo3DComponent]. */
    inline val geo3D: CoordinateSystem3D get() = CoordinateSystem3D("geo3D")

    /** Globe created by [GlobeComponent]. */
    inline val globe: CoordinateSystem3D get() = CoordinateSystem3D("globe")

    /** Mapbox GL coordinate system created by [Mapbox3DComponent]. */
    inline val mapbox3D: CoordinateSystem3D get() = CoordinateSystem3D("mapbox3D")

    /** maptalks coordinate system created by [Maptalks3DComponent]. */
    inline val maptalks3D: CoordinateSystem3D get() = CoordinateSystem3D("maptalks3D")
}

/**
 * Shading mode of 3D graphics.
 */
sealed external interface ShadingType

/** Wraps a value that comes from JavaScript. Prefer the constants in [ShadingTypes]. */
inline fun ShadingType(value: String): ShadingType = value.unsafeCast<ShadingType>()

/**
 * The values of [ShadingType].
 */
object ShadingTypes {
    /** Only the color is drawn; light does not affect the surface. */
    inline val color: ShadingType get() = ShadingType("color")

    /** Classic lambert shading, based on the light direction. */
    inline val lambert: ShadingType get() = ShadingType("lambert")

    /** Physically based rendering, driven by the `*Material` options and the environment. */
    inline val realistic: ShadingType get() = ShadingType("realistic")

    /** Hatching shading, drawn with the `hatchingMaterial` option. */
    inline val hatching: ShadingType get() = ShadingType("hatching")
}

/**
 * Projection of the camera.
 */
sealed external interface ProjectionType

/** Wraps a value that comes from JavaScript. Prefer the constants in [ProjectionTypes]. */
inline fun ProjectionType(value: String): ProjectionType = value.unsafeCast<ProjectionType>()

/**
 * The values of [ProjectionType].
 */
object ProjectionTypes {
    /** Perspective projection, the default. */
    inline val perspective: ProjectionType get() = ProjectionType("perspective")

    /** Orthographic projection; it is sized by `viewControl.orthographicSize`. */
    inline val orthographic: ProjectionType get() = ProjectionType("orthographic")
}

/**
 * Quality preset of shadows, SSAO and depth of field.
 */
sealed external interface QualityType

/** Wraps a value that comes from JavaScript. Prefer the constants in [QualityTypes]. */
inline fun QualityType(value: String): QualityType = value.unsafeCast<QualityType>()

/**
 * The values of [QualityType].
 */
object QualityTypes {
    /** Low quality. */
    inline val low: QualityType get() = QualityType("low")

    /** Medium quality. */
    inline val medium: QualityType get() = QualityType("medium")

    /** High quality. */
    inline val high: QualityType get() = QualityType("high")

    /** Ultra quality. */
    inline val ultra: QualityType get() = QualityType("ultra")
}

/**
 * Direction of `viewControl.autoRotate`.
 */
sealed external interface AutoRotateDirection

/** Wraps a value that comes from JavaScript. Prefer the constants in [AutoRotateDirections]. */
inline fun AutoRotateDirection(value: String): AutoRotateDirection = value.unsafeCast<AutoRotateDirection>()

/**
 * The values of [AutoRotateDirection].
 */
object AutoRotateDirections {
    /** Clockwise, seen from above. The default. */
    inline val cw: AutoRotateDirection get() = AutoRotateDirection("cw")

    /** Counterclockwise, seen from above. */
    inline val ccw: AutoRotateDirection get() = AutoRotateDirection("ccw")
}

/**
 * Mouse button that drives a view control gesture.
 */
sealed external interface MouseButton

/** Wraps a value that comes from JavaScript. Prefer the constants in [MouseButtons]. */
inline fun MouseButton(value: String): MouseButton = value.unsafeCast<MouseButton>()

/**
 * The values of [MouseButton].
 */
object MouseButtons {
    /** Left mouse button. */
    inline val left: MouseButton get() = MouseButton("left")

    /** Middle mouse button. */
    inline val middle: MouseButton get() = MouseButton("middle")

    /** Right mouse button; the browser context menu is suppressed while it is used. */
    inline val right: MouseButton get() = MouseButton("right")
}

/**
 * Blend mode of a GL series.
 */
sealed external interface GlBlendMode

/** Wraps a value that comes from JavaScript. Prefer the constants in [GlBlendModes]. */
inline fun GlBlendMode(value: String): GlBlendMode = value.unsafeCast<GlBlendMode>()

/**
 * The values of [GlBlendMode].
 */
object GlBlendModes {
    /** Alpha blending; the default. */
    inline val sourceOver: GlBlendMode get() = GlBlendMode("source-over")

    /** Additive blending, which highlights overlapping graphics. */
    inline val lighter: GlBlendMode get() = GlBlendMode("lighter")
}

/**
 * Plane of the base grid a 3D bar stands on.
 */
sealed external interface GridPlaneType

/** Wraps a value that comes from JavaScript. Prefer the constants in [GridPlaneTypes]. */
inline fun GridPlaneType(value: String): GridPlaneType = value.unsafeCast<GridPlaneType>()

/**
 * The values of [GridPlaneType].
 */
object GridPlaneTypes {
    /** Bars stand on the x-y plane and grow along z. The default. */
    inline val xy: GridPlaneType get() = GridPlaneType("xy")

    /** Bars stand on the y-z plane and grow along x. */
    inline val yz: GridPlaneType get() = GridPlaneType("yz")

    /** Bars stand on the x-z plane and grow along y. */
    inline val xz: GridPlaneType get() = GridPlaneType("xz")
}

/**
 * A toggle that also accepts `"auto"`, such as `temporalSuperSampling.enable`.
 */
external interface BooleanOrAuto

/** Wraps a boolean. */
fun BooleanOrAuto(value: Boolean): BooleanOrAuto = value.unsafeCast<BooleanOrAuto>()

/** Wraps the keyword `"auto"`. */
fun BooleanOrAuto(value: String): BooleanOrAuto = value.unsafeCast<BooleanOrAuto>()

/** The keyword accepted by [BooleanOrAuto]. */
object BooleanOrAutos {
    /** Decide from the surrounding options, e.g. enable supersampling with `postEffect`. */
    inline val auto: BooleanOrAuto get() = BooleanOrAuto("auto")
}

/**
 * Texture of a material: a URL, an image element or a canvas element.
 */
external interface TextureSource

/** Wraps a texture URL. */
fun TextureSource(url: String): TextureSource = url.unsafeCast<TextureSource>()

/** Wraps an already loaded image element. */
fun TextureSource(image: HTMLImageElement): TextureSource = image.unsafeCast<TextureSource>()

/** Wraps a canvas element used as the texture. */
fun TextureSource(canvas: HTMLCanvasElement): TextureSource = canvas.unsafeCast<TextureSource>()

/**
 * A material channel that is either a constant or a texture, such as `roughness` / `metalness`.
 */
external interface NumberOrTexture

/** Wraps a constant value. */
fun NumberOrTexture(value: Double): NumberOrTexture = value.unsafeCast<NumberOrTexture>()

/** Wraps a texture URL. */
fun NumberOrTexture(url: String): NumberOrTexture = url.unsafeCast<NumberOrTexture>()

/** Wraps an already loaded image element. */
fun NumberOrTexture(image: HTMLImageElement): NumberOrTexture = image.unsafeCast<NumberOrTexture>()

/** Wraps a canvas element used as the texture. */
fun NumberOrTexture(canvas: HTMLCanvasElement): NumberOrTexture = canvas.unsafeCast<NumberOrTexture>()

/**
 * Environment map of a 3D component: `"auto"`, a color (solid or gradient) or a panoramic texture
 * URL.
 */
external interface GlEnvironment

/** Wraps a color or a texture URL. */
fun GlEnvironment(value: String): GlEnvironment = value.unsafeCast<GlEnvironment>()

/** Wraps a gradient object. */
fun GlEnvironment(value: ZRColor): GlEnvironment = value.unsafeCast<GlEnvironment>()

/** The keyword accepted by [GlEnvironment]. */
object GlEnvironments {
    /** Use `light.ambientCubemap.texture` when it is set, otherwise draw no environment. */
    inline val auto: GlEnvironment get() = GlEnvironment("auto")
}

/**
 * Color of a GL graphic: a regular color, an RGB(A) array whose channels may exceed `1` for HDR
 * values, or a callback.
 *
 * Use [GlColor] with a [CallbackDataParams] callback through `ZRColor` to compute the color per
 * data item.
 */
external interface GlColor

/** Wraps a color string, gradient or pattern. */
fun GlColor(value: ZRColor): GlColor = value.unsafeCast<GlColor>()

/** Wraps an `[r, g, b]` or `[r, g, b, a]` array; channels above `1` are HDR. */
fun GlColor(rgba: Array<Double>): GlColor = rgba.unsafeCast<GlColor>()

/**
 * Series types added by `echarts-gl`.
 *
 * The values are usable wherever a [SeriesType][echarts.SeriesType] is expected; the part before
 * the colon mirrors the upstream `series.<type>` name.
 */
object EChartsGlSeriesTypes {
    /** 3D bar, installed by [Bar3DChart]. */
    inline val bar3D: SeriesType get() = SeriesType("bar3D")
}
