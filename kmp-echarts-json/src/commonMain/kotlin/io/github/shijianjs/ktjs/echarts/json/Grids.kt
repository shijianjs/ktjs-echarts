package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.Serializable

/** A cartesian grid that hosts the `xAxis` and `yAxis` of a chart. */
@Serializable
public class GridOption {
    /** Component id used by `gridIndex` cross references. */
    public var id: String? = null

    /** Whether the grid is shown. */
    public var show: Boolean? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Distance to the left edge. */
    public var left: NumberOrString? = null

    /** Distance to the top edge. */
    public var top: NumberOrString? = null

    /** Distance to the right edge. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge. */
    public var bottom: NumberOrString? = null

    /** Grid width. */
    public var width: NumberOrString? = null

    /** Grid height. */
    public var height: NumberOrString? = null

    /** Whether the grid is expanded to contain axis labels. */
    public var containLabel: Boolean? = null

    /** Background color. */
    public var backgroundColor: EChartsColor? = null

    /** Border color. */
    public var borderColor: EChartsColor? = null

    /** Border width. */
    public var borderWidth: Double? = null

    /** Shadow blur radius. */
    public var shadowBlur: Double? = null

    /** Shadow color. */
    public var shadowColor: EChartsColor? = null

    /** Horizontal shadow offset. */
    public var shadowOffsetX: Double? = null

    /** Vertical shadow offset. */
    public var shadowOffsetY: Double? = null

    /** Tooltip limited to this grid; overrides the top-level tooltip for its contents. */
    public var tooltip: TooltipOption? = null
}

/** A polar coordinate system hosting `angleAxis` and `radiusAxis`. */
@Serializable
public class PolarOption {
    /** Component id used by `polarIndex` cross references. */
    public var id: String? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Center of the circle as `[x, y]`, absolute or percentage values. */
    public var center: List<NumberOrString>? = null

    /** Radius as a number, a percentage string or `[inner, outer]`. */
    public var radius: NumberOrStringOrNumbers? = null
}

/** A parallel coordinate system used by `series.type = "parallel"`. */
@Serializable
public class ParallelOption {
    /** Component id used by `parallelIndex` cross references. */
    public var id: String? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Distance to the left edge. */
    public var left: NumberOrString? = null

    /** Distance to the top edge. */
    public var top: NumberOrString? = null

    /** Distance to the right edge. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge. */
    public var bottom: NumberOrString? = null

    /** Layout width. */
    public var width: NumberOrString? = null

    /** Layout height. */
    public var height: NumberOrString? = null

    /** Defaults applied to every `parallelAxis` of this coordinate system. */
    public var parallelAxisDefault: AxisOption? = null
}
