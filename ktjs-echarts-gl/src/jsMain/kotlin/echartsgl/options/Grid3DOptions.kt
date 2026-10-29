package echartsgl.options

import echarts.options.StringOrDouble
import echartsgl.GlEnvironment

/**
 * 3D cartesian coordinate system.
 *
 * It needs [xAxis3D][EChartsGlOption.xAxis3D], [yAxis3D][EChartsGlOption.yAxis3D] and
 * [zAxis3D][EChartsGlOption.zAxis3D] to be set together with it, and accepts the 3D lines, bar,
 * scatter and surface series.
 *
 * The [axisLine], [axisTick], [axisLabel], [splitLine], [splitArea] and [axisPointer] settings here
 * are the defaults of every axis; the same setting on an axis overrides them for that axis only.
 */
external interface Grid3DOption {
    /** Whether the component is shown. */
    var show: Boolean?

    /** z-level of the WebGL layer; it must not be shared with a canvas component. */
    var zlevel: Double?

    /** Distance between the grid and the left edge of the canvas. */
    var left: StringOrDouble?

    /** Distance between the grid and the top edge of the canvas. */
    var top: StringOrDouble?

    /** Width of the grid inside the canvas. */
    var width: StringOrDouble?

    /** Height of the grid inside the canvas. */
    var height: StringOrDouble?

    /** Environment map: `"auto"`, a color or a panoramic texture. */
    var environment: GlEnvironment?

    /** Width of the grid box in the 3D scene. */
    var boxWidth: Double?

    /** Height of the grid box in the 3D scene. */
    var boxHeight: Double?

    /** Depth of the grid box in the 3D scene. */
    var boxDepth: Double?

    /** Default axis pointer of the three axes. */
    var axisPointer: Axis3DPointerOption?

    /** Default axis line of the three axes. */
    var axisLine: Axis3DLineOption?

    /** Default axis ticks of the three axes. */
    var axisTick: Axis3DTickOption?

    /** Default axis labels of the three axes. */
    var axisLabel: Axis3DLabelOption?

    /** Default split lines of the three axes. */
    var splitLine: Axis3DSplitLineOption?

    /** Default split areas of the three axes. */
    var splitArea: Axis3DSplitAreaOption?

    /** Light of the scene and of every series drawn in it. */
    var light: LightOption?

    /** Post-processing effects of the scene. */
    var postEffect: PostEffectOption?

    /** Temporal supersampling of the scene. */
    var temporalSuperSampling: TemporalSuperSamplingOption?

    /** Camera control. */
    var viewControl: ViewControlOption?
}
