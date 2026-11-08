package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.Serializable

/** A radar coordinate system used by `series.type = "radar"`. */
@Serializable
public class RadarOption {
    /** Component id used by `radarIndex` cross references. */
    public var id: String? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Center of the radar as `[x, y]`, absolute or percentage values. */
    public var center: List<NumberOrString>? = null

    /** Radius as a number, a percentage string or `[inner, outer]`. */
    public var radius: NumberOrStringOrNumbers? = null

    /** Angle of the first indicator, in degrees. */
    public var startAngle: Double? = null

    /** Shape of the radar outline: `"polygon"` or `"circle"`. */
    public var shape: String? = null

    /** Indicator name style. */
    public var axisName: LabelOption? = null

    /** Gap between an indicator name and the outline. */
    public var nameGap: Double? = null

    /** Number of segments between the center and the outline. */
    public var splitNumber: Int? = null

    /** Whether each indicator scales to its own data range. */
    public var scale: Boolean? = null

    /** Axis line. */
    public var axisLine: AxisLineOption? = null

    /** Axis tick marks. */
    public var axisTick: AxisTickOption? = null

    /** Indicator labels. */
    public var axisLabel: AxisLabelOption? = null

    /** Concentric split lines. */
    public var splitLine: SplitLineOption? = null

    /** Alternating bands between split lines. */
    public var splitArea: SplitAreaOption? = null

    /** Indicators, one per axis. */
    public var indicator: List<RadarIndicator>? = null

    /** Whether clicking the radar triggers a `click` event. */
    public var triggerEvent: Boolean? = null

    /** Whether the radar ignores mouse events. */
    public var silent: Boolean? = null
}

/** One indicator (axis) of a [RadarOption]. */
@Serializable
public class RadarIndicator {
    /** Indicator name. */
    public var name: String? = null

    /** Maximum value; defaults to the data maximum. */
    public var max: Double? = null

    /** Minimum value. */
    public var min: Double? = null

    /** Name color. */
    public var color: EChartsColor? = null
}
