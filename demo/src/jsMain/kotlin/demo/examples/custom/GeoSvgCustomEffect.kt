package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import demo.examples.Category

private val geoSvgCustomEffectCategory = Category(Groups.CUSTOM, "examples/custom/GeoSvgCustomEffect.kt")

val geoSvgCustomEffectEntry: CatalogEntry =
    geoSvgCustomEffectCategory.interactive("geo-svg-custom-effect", "自定义特效（SVG）", "SVG Custom Effect") { chart ->
            loadText("data/Map_of_Iceland.svg").then { svg ->
                ECharts.registerMap(
                    "iceland_svg",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    tooltip = unsafeJso<TooltipOption> { show = true }
                                    map = "iceland_svg"
                                    roam = BooleanOrString(true)
                                },
                            )
                        series =
                            one(
                                customSeries {
                                    coordinateSystem = "geo"
                                    geoIndex = 0.0
                                    zlevel = 1.0
                                    typedData =
                                        arrayOf(
                                            arrayOf(OptionDataValue(488.2358421078053), OptionDataValue(459.70913833075736), OptionDataValue(100.0)),
                                            arrayOf(OptionDataValue(770.3415644319939), OptionDataValue(757.9672194986475), OptionDataValue(30.0)),
                                            arrayOf(OptionDataValue(1180.0329284196291), OptionDataValue(743.6141808346214), OptionDataValue(80.0)),
                                            arrayOf(OptionDataValue(894.03790632245), OptionDataValue(1188.1985153835008), OptionDataValue(61.0)),
                                            arrayOf(OptionDataValue(1372.98925630313), OptionDataValue(477.3839988649537), OptionDataValue(70.0)),
                                            arrayOf(OptionDataValue(1378.62251255796), OptionDataValue(935.6708486282843), OptionDataValue(81.0)),
                                        ).unsafeCast<Array<OptionDataItem?>>()
                                    renderItem = { params, api ->
                                        val coord =
                                            api.coord(
                                                CoordinateSystemDataCoord(
                                                    arrayOf(
                                                        api.value(DimensionLoose(0.0)),
                                                        api.value(DimensionLoose(1.0)),
                                                    ),
                                                ),
                                            )
                                        val circles =
                                            Array(5) { index ->
                                                unsafeJso<CustomPathOption> {
                                                    type = "circle"
                                                    shape = unsafeJso<ZRShapeProps> { cx = 0.0; cy = 0.0; r = 30.0 }
                                                    style =
                                                        unsafeJso<ZRStyleProps> {
                                                            stroke = ZRColor("red")
                                                            fill = ZRColor("none")
                                                            lineWidth = 2.0
                                                        }
                                                    keyframeAnimation =
                                                        ElementKeyframeAnimationOptionOrArray(rippleAnimation(index))
                                                }
                                            }
                                        val pin =
                                            unsafeJso<CustomPathOption> {
                                                type = "path"
                                                shape =
                                                    unsafeJso<ZRShapeProps> {
                                                        d = PIN_PATH
                                                        x = -10.0
                                                        y = -35.0
                                                        width = 20.0
                                                        height = 40.0
                                                    }
                                                style = unsafeJso<ZRStyleProps> { fill = ZRColor("red") }
                                                keyframeAnimation =
                                                    ElementKeyframeAnimationOptionOrArray(jumpAnimation())
                                            }
                                        unsafeJso<CustomGroupOption> {
                                            type = "group"
                                            x = coord[0]
                                            y = coord[1]
                                            children =
                                                Array(circles.size + 1) { index ->
                                                    if (index < circles.size) circles[index] else pin
                                                }
                                        }
                                    }
                                },
                            )
                    },
                )
                null
            }
        }
