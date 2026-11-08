package demo.examples.custom

import io.github.shijianjs.ktjs.echarts.AnimationEasings
import io.github.shijianjs.ktjs.echarts.AxisTypes
import io.github.shijianjs.ktjs.echarts.CustomSeriesRenderItemAPI
import io.github.shijianjs.ktjs.echarts.CustomSeriesRenderItemParams
import io.github.shijianjs.ktjs.echarts.CustomSeriesRenderItemReturn
import io.github.shijianjs.ktjs.echarts.options.AnimationDuration
import io.github.shijianjs.ktjs.echarts.options.AngleAxisOption
import io.github.shijianjs.ktjs.echarts.options.AxisMinMax
import io.github.shijianjs.ktjs.echarts.options.CoordinateSystemDataCoord
import io.github.shijianjs.ktjs.echarts.options.CustomClipPathOption
import io.github.shijianjs.ktjs.echarts.options.CustomElementShapeOption
import io.github.shijianjs.ktjs.echarts.options.CustomElementStyleOption
import io.github.shijianjs.ktjs.echarts.options.CustomGroupOption
import io.github.shijianjs.ktjs.echarts.options.CustomImageOption
import io.github.shijianjs.ktjs.echarts.options.CustomPathOption
import io.github.shijianjs.ktjs.echarts.options.CustomTextOption
import io.github.shijianjs.ktjs.echarts.options.DatasetOption
import io.github.shijianjs.ktjs.echarts.options.DimensionLoose
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.PolarOption
import io.github.shijianjs.ktjs.echarts.options.RadiusAxisOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.StringOrStringArray
import io.github.shijianjs.ktjs.echarts.options.TooltipOption
import io.github.shijianjs.ktjs.echarts.options.TransitionDuringAPI
import io.github.shijianjs.ktjs.echarts.options.customSeries
import io.github.shijianjs.ktjs.echarts.options.datasetArrayRows
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import io.github.shijianjs.ktjs.zrender.StringOrImageLike
import io.github.shijianjs.ktjs.zrender.ZRColor
import io.github.shijianjs.ktjs.zrender.ZRFontWeight
import io.github.shijianjs.ktjs.zrender.ZRShapeProps
import io.github.shijianjs.ktjs.zrender.ZRStyleProps
import demo.examples.gauge.center
import demo.examples.lines.coordinate
import demo.examples.scatter.point
import demo.examples.shared.recordOf
import demo.examples.sunburst.radius

/** Panel picture of the dial, `ROOT_PATH + '/data/asset/img/custom-gauge-panel.png'` upstream. */
internal const val PANEL_IMAGE = "data/img/custom-gauge-panel.png"

/** Value the dial reaches at the end of the scale. */
internal const val VAL_ON_RADIAN_MAX = 200.0

internal const val OUTER_RADIUS = 200.0

internal const val INNER_RADIUS = 170.0

/** Radius where the pointer starts, so its base stays under the hub. */
internal const val POINTER_INNER_RADIUS = 40.0

/** Radius of the white hub drawn over the pointer. */
internal const val INSIDE_PANEL_RADIUS = 140.0

/** Distance of a polar point from the centre of the dial, in pixels. */
internal fun polarPoint(cx: Double, cy: Double, radius: Double, radian: Double): Array<Double> =
    arrayOf(
        kotlin.math.cos(radian) * radius + cx,
        -kotlin.math.sin(radian) * radius + cy,
    )

/** The three corners of the pointer: two on the rim and one at its base. */
internal fun pointerPoints(cx: Double, cy: Double, polarEndRadian: Double): Array<Array<Double>> =
    arrayOf(
        polarPoint(cx, cy, OUTER_RADIUS, polarEndRadian),
        polarPoint(cx, cy, OUTER_RADIUS, polarEndRadian + kotlin.math.PI * 0.03),
        polarPoint(cx, cy, POINTER_INNER_RADIUS, polarEndRadian),
    )

/** Percentage shown in the hub; upstream formats `value / 200 * 100` with `toFixed(0)`. */
internal fun gaugeText(valOnRadian: Double): String =
    "${kotlin.math.round(valOnRadian / VAL_ON_RADIAN_MAX * 100).toInt()}%"

/** Reads one of the transitioned angles back out of the extra data during an animation frame. */
internal fun extraRadian(during: TransitionDuringAPI, key: String): Double =
    during.getExtra(key)?.unsafeCast<Double>() ?: 0.0

/**
 * `renderItem` of `custom-gauge`: the clipped panel picture, the pointer, the hub and the value.
 */
internal fun gaugeRenderItem(
    params: CustomSeriesRenderItemParams,
    api: CustomSeriesRenderItemAPI,
): CustomSeriesRenderItemReturn {
    val cx = params.coordSys.cx ?: 0.0
    val cy = params.coordSys.cy ?: 0.0
    val valOnRadian = api.value(DimensionLoose(1.0)).unsafeCast<Double?>() ?: 0.0
    // A polar coordinate system appends the radius and the angle in radians to the point.
    val radius = api.value(DimensionLoose(0.0)).unsafeCast<Double?>() ?: 0.0
    val coords = api.coord(CoordinateSystemDataCoord(arrayOf<OptionDataValue?>(OptionDataValue(radius), OptionDataValue(valOnRadian))))
    val polarEndRadian = coords[3]
    val imageStyle =
        unsafeJso<CustomElementStyleOption> {
            image = StringOrImageLike(PANEL_IMAGE)
            x = cx - OUTER_RADIUS
            y = cy - OUTER_RADIUS
            width = StringOrDouble(OUTER_RADIUS * 2)
            height = StringOrDouble(OUTER_RADIUS * 2)
        }

    return unsafeJso<CustomGroupOption> {
        type = "group"
        children =
            arrayOf(
                unsafeJso<CustomImageOption> {
                    type = "image"
                    style = imageStyle
                    clipPath =
                        CustomClipPathOption(
                            unsafeJso<CustomPathOption> {
                                type = "sector"
                                shape =
                                    unsafeJso<CustomElementShapeOption> {
                                        this.cx = cx
                                        this.cy = cy
                                        r = OUTER_RADIUS
                                        r0 = INNER_RADIUS
                                        startAngle = 0.0
                                        endAngle = -polarEndRadian
                                        transition = StringOrStringArray("endAngle")
                                        enterFrom = unsafeJso<ZRShapeProps> { endAngle = 0.0 }
                                    }
                            },
                        )
                },
                unsafeJso<CustomImageOption> {
                    type = "image"
                    style = imageStyle
                    clipPath =
                        CustomClipPathOption(
                            unsafeJso<CustomPathOption> {
                                type = "polygon"
                                shape =
                                    unsafeJso<CustomElementShapeOption> {
                                        points = pointerPoints(cx, cy, polarEndRadian)
                                    }
                                extra =
                                    recordOf(
                                        "polarEndRadian" to OptionDataValue(polarEndRadian),
                                        "transition" to StringOrStringArray("polarEndRadian"),
                                        "enterFrom" to recordOf("polarEndRadian" to OptionDataValue(0.0)),
                                    )
                                typedDuring = { during ->
                                    during.setShape(
                                        "points",
                                        pointerPoints(cx, cy, extraRadian(during, "polarEndRadian")).unsafeCast<JsAny>(),
                                    )
                                }
                            },
                        )
                },
                unsafeJso<CustomPathOption> {
                    type = "circle"
                    shape =
                        unsafeJso<ZRShapeProps> {
                            this.cx = cx
                            this.cy = cy
                            r = INSIDE_PANEL_RADIUS
                        }
                    style =
                        unsafeJso<ZRStyleProps> {
                            fill = ZRColor("#fff")
                            shadowBlur = 25.0
                            shadowOffsetX = 0.0
                            shadowOffsetY = 0.0
                            shadowColor = "rgba(76,107,167,0.4)"
                        }
                },
                unsafeJso<CustomTextOption> {
                    type = "text"
                    extra =
                        recordOf(
                            "valOnRadian" to OptionDataValue(valOnRadian),
                            "transition" to StringOrStringArray("valOnRadian"),
                            "enterFrom" to recordOf("valOnRadian" to OptionDataValue(0.0)),
                        )
                    style =
                        unsafeJso<CustomElementStyleOption> {
                            text = gaugeText(valOnRadian)
                            fontSize = StringOrDouble(50.0)
                            fontWeight = ZRFontWeight(700.0)
                            x = cx
                            y = cy
                            fill = ZRColor("rgb(0,50,190)")
                            align = TextAlign.center
                            verticalAlign = VerticalAlign.middle
                            enterFrom = unsafeJso<ZRStyleProps> { opacity = 0.0 }
                        }
                    typedDuring = { during ->
                        during.setStyle("text", StringOrStringArray(gaugeText(extraRadian(during, "valOnRadian"))))
                    }
                },
            )
    }
}

/** Option of `custom-gauge`; the value itself lives in the dataset so the interval can replace it. */
internal fun gaugeOption(valOnRadian: Double): EChartsOption =
    unsafeJso {
        animationEasing = AnimationEasings.quarticInOut
        animationDuration = AnimationDuration(1000.0)
        animationDurationUpdate = AnimationDuration(1000.0)
        animationEasingUpdate = AnimationEasings.quarticInOut
        dataset =
            arrayOf(
                unsafeJso<DatasetOption> {
                    source = datasetArrayRows(arrayOf(arrayOf(OptionDataValue(1.0), OptionDataValue(valOnRadian))))
                },
            )
        tooltip = one(unsafeJso<TooltipOption> {})
        angleAxis =
            one(
                unsafeJso<AngleAxisOption> {
                    type = AxisTypes.value
                    startAngle = 0.0
                    show = false
                    min = AxisMinMax(0.0)
                    max = AxisMinMax(VAL_ON_RADIAN_MAX)
                },
            )
        radiusAxis =
            one(
                unsafeJso<RadiusAxisOption> {
                    type = AxisTypes.value
                    show = false
                },
            )
        polar = one(unsafeJso<PolarOption> {})
        series =
            arrayOf<SeriesOption>(
                customSeries {
                    coordinateSystem = "polar"
                    renderItem = { params, api -> gaugeRenderItem(params, api) }
                },
            )
    }

/** The same option without the animation settings, merged into the chart by the interval. */
internal fun gaugeUpdate(valOnRadian: Double): EChartsOption =
    unsafeJso {
        dataset =
            arrayOf(
                unsafeJso<DatasetOption> {
                    source = datasetArrayRows(arrayOf(arrayOf(OptionDataValue(1.0), OptionDataValue(valOnRadian))))
                },
            )
    }
