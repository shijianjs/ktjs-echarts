package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.sqrt
import zrender.ZRColor
import zrender.ZRLineTypes
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * Custom series examples that do not fit the plain `renderItem` shape of `Custom.kt`: they read the
 * polar / calendar fields of `params.coordSys` or draw sectors instead of rectangles.
 *
 * Dropped on purpose: `custom-gauge` needs `ROOT_PATH` panel images, `custom-hexbin` fetches its
 * data with `$.getJSON`, `custom-spiral-race` and `custom-gantt-flight` animate with `setInterval`.
 */

/** Shape of `winds.json`: a grid of `[vx, vy]` vectors. */
private external interface WindData {
    val nx: Double
    val ny: Double
    val data: Array<Array<Double>>
}

/** Fisher-Yates shuffle with a fixed seed, so the page is reproducible. */
private fun shuffleFixed(values: Array<DoubleArray>, seed: Int) {
    val random = kotlin.random.Random(seed)
    for (index in values.indices.reversed()) {
        val swap = random.nextInt(index + 1)
        val temporary = values[index]
        values[index] = values[swap]
        values[swap] = temporary
    }
}
/** The location pin path used by the SVG custom-effect example. */
private const val PIN_PATH =
    "M16 0c-5.523 0-10 4.477-10 10 0 10 10 22 10 22s10-12 10-22c0-5.523-4.477-10-10-10zM16 16c-3.314 0-6-2.686-6-6s2.686-6 6-6 6 2.686 6 6-2.686 6-6 6z"

/** Ripple animation of a location marker; `index` staggers the start across the five rings. */
private fun rippleAnimation(index: Int): ElementKeyframeAnimationOption {
    val delay = (-index.toDouble() / 4) * 4000
    return unsafeJso {
        duration = 4000.0
        loop = true
        this.delay = delay
        keyframes = arrayOf(
            unsafeJso<AnimationKeyframe> {
                percent = 0.0
                scaleX = 0.0
                scaleY = 0.0
                style = unsafeJso<ZRStyleProps> { opacity = 1.0 }
            },
            unsafeJso<AnimationKeyframe> {
                percent = 1.0
                scaleX = 1.0
                scaleY = 0.4
                style = unsafeJso<ZRStyleProps> { opacity = 0.0 }
            },
        )
    }
}

/** Bouncing animation of the pin itself. */
private fun jumpAnimation(): ElementKeyframeAnimationOption {
    return unsafeJso {
        duration = 1000.0
        loop = true
        this.delay = kotlin.random.Random.nextDouble() * 1000
        keyframes = arrayOf(
            unsafeJso<AnimationKeyframe> { y = -10.0; percent = 0.5; easing = "cubicOut".unsafeCast<AnimationEasing>() },
            unsafeJso<AnimationKeyframe> { y = 0.0; percent = 1.0; easing = "bounceOut".unsafeCast<AnimationEasing>() },
        )
    }
}
private val customExtra = Category(Groups.CUSTOM, "examples/CustomExtra.kt")

/** Hours of the day, the angle axis of `custom-polar-heatmap`. */
private val punchCardHours =
    arrayOf("12a", "1a", "2a", "3a", "4a", "5a", "6a", "7a", "8a", "9a", "10a", "11a", "12p", "1p", "2p", "3p", "4p", "5p", "6p", "7p", "8p", "9p", "10p", "11p")

/** Days of the week, the radius axis of `custom-polar-heatmap`. */
private val punchCardDays = arrayOf("Saturday", "Friday", "Thursday", "Wednesday", "Tuesday", "Monday", "Sunday")

/**
 * `rawData` of the original punch card, one row per day and one column per hour.
 *
 * The example expands it into `[dayIndex, hourIndex, value]` triples.
 */
private val punchCardValues =
    arrayOf(
        intArrayOf(5, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 4, 1, 1, 3, 4, 6, 4, 4, 3, 3, 2, 5),
        intArrayOf(7, 0, 0, 0, 0, 0, 0, 0, 0, 0, 5, 2, 2, 6, 9, 11, 6, 7, 8, 12, 5, 5, 7, 2),
        intArrayOf(1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 1, 9, 8, 10, 6, 5, 5, 5, 7, 4, 2, 4),
        intArrayOf(7, 3, 0, 0, 0, 0, 0, 0, 1, 0, 5, 4, 7, 14, 13, 12, 9, 5, 5, 10, 6, 4, 4, 1),
        intArrayOf(1, 3, 0, 0, 0, 1, 0, 0, 0, 2, 4, 4, 2, 4, 4, 14, 12, 1, 8, 5, 3, 7, 3, 0),
        intArrayOf(2, 1, 0, 3, 0, 0, 0, 0, 2, 0, 4, 1, 5, 10, 5, 7, 11, 6, 0, 5, 3, 4, 2, 0),
        intArrayOf(1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 2, 1, 3, 4, 0, 0, 0, 0, 1, 2, 2, 6),
    )

/** The `[day, hour, value]` triples of `custom-polar-heatmap`. */
private fun punchCardData(): Array<OptionDataItem?> =
    dataItems(
        *punchCardValues
            .flatMapIndexed { day, row ->
                row.mapIndexed { hour, value ->
                    OptionDataItem(
                        arrayOf<OptionDataValue?>(
                            OptionDataValue(day.toDouble()),
                            OptionDataValue(hour.toDouble()),
                            OptionDataValue(value.toDouble()),
                        ),
                    )
                }
            }
            .toTypedArray(),
    )

val customExtraEntries: List<CatalogEntry> =
    listOf(
        customExtra.chart(
            "custom-polar-heatmap",
            "极坐标热力图（自定义系列）",
            "Polar Heatmap",
        ) {
            unsafeJso<EChartsOption> {
                legend = one(unsafeJso<LegendOption> { data = legendNames("Punch Card") })
                polar = one(unsafeJso<PolarOption> {})
                tooltip = one(unsafeJso<TooltipOption> {})
                visualMap =
                    one(
                        unsafeJso<VisualMapOption> {
                            type = VisualMapTypes.continuous
                            min = 0.0
                            max = 14.0
                            top = StringOrDouble("middle")
                            dimension = 2.0
                            calculable = true
                        },
                    )
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            data = strings(*punchCardHours)
                            boundaryGap = BooleanOrStringArray(false)
                            splitLine =
                                unsafeJso<SplitLineOption> {
                                    show = true
                                    lineStyle =
                                        unsafeJso<SplitLineStyleOption> {
                                            color = ZRColor("#ddd")
                                            type = ZRLineTypes.dashed
                                        }
                                }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                        },
                    )
                radiusAxis =
                    one(
                        unsafeJso<RadiusAxisOption> {
                            type = AxisTypes.category
                            data = strings(*punchCardDays)
                            z = 100.0
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        customSeries {
                            name = "Punch Card"
                            coordinateSystem = "polar"
                            typedData = punchCardData()
                            renderItem = { params, api ->
                                val values =
                                    arrayOf<OptionDataValue?>(
                                        api.value(DimensionLoose(0.0)),
                                        api.value(DimensionLoose(1.0)),
                                    )
                                val coord = api.coord(CoordinateSystemDataCoord(values))
                                val size =
                                    numberArray(
                                        api.size(
                                            OptionDataValueOrArray(
                                                arrayOf(OptionDataValue(1.0), OptionDataValue(1.0)),
                                            ),
                                            OptionDataValueOrArray(values),
                                        ),
                                    )
                                unsafeJso<CustomPathOption> {
                                    type = "sector"
                                    shape =
                                        unsafeJso<ZRShapeProps> {
                                            cx = params.coordSys.cx
                                            cy = params.coordSys.cy
                                            r0 = coord[2] - size[0] / 2
                                            r = coord[2] + size[0] / 2
                                            startAngle = -(coord[3] + size[1] / 2)
                                            endAngle = -(coord[3] - size[1] / 2)
                                        }
                                    style =
                                        api.style(
                                            unsafeJso<ZRStyleProps> {
                                                fill = api.visual(VisualProps.color).unsafeCast<ZRColor>()
                                            },
                                        )
                                }
                            }
                        },
                    )
            }
        },
        customExtra.interactive("custom-wind", "风场（自定义系列）", "Wind Vectors") { chart ->
            loadJson("data/world.json").then { world ->
                ECharts.registerMap(
                    "world",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = world.unsafeCast<GeoJSONSourceInput>() }),
                )
                loadJson("data/winds.json").then { json ->
                val wind = json.unsafeCast<WindData>()
                val nx = wind.nx.toInt()
                val ny = wind.ny.toInt()
                var maxMag = 0.0
                var minMag = Double.MAX_VALUE
                val data = Array(nx * ny) { index ->
                    val vx = wind.data[index][0]
                    val vy = wind.data[index][1]
                    val magnitude = sqrt(vx * vx + vy * vy)
                    maxMag = kotlin.math.max(magnitude, maxMag)
                    minMag = kotlin.math.min(magnitude, minMag)
                    val column = index % nx
                    val row = index / nx
                    doubleArrayOf(
                        column.toDouble() / nx * 360 - 180,
                        row.toDouble() / ny * 180 - 90,
                        vx,
                        vy,
                        magnitude,
                    )
                }
                shuffleFixed(data, 20260604)
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        backgroundColor = ZRColor("#333")
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    left = StringOrDouble("center")
                                    min = minMag
                                    max = maxMag
                                    dimension = 4.0
                                    calculable = true
                                    orient = Orient("horizontal")
                                    textStyle = unsafeJso<LabelOption> { color = "#fff" }
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            color =
                                                StringOrStringArray(
                                                    arrayOf(
                                                        "#313695", "#4575b4", "#74add1", "#abd9e9", "#e0f3f8",
                                                        "#ffffbf", "#fee090", "#fdae61", "#f46d43", "#d73027", "#a50026",
                                                    ),
                                                )
                                        }
                                },
                            )
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    map = "world"
                                    silent = true
                                    roam = BooleanOrString(true)
                                    itemStyle =
                                        unsafeJso<ItemStyleOption> {
                                            areaColor = ZRColor("#323c48")
                                            borderColor = ZRColor("#111")
                                        }
                                },
                            )
                        series =
                            one(
                                customSeries {
                                    coordinateSystem = "geo"
                                    progressive = 2000.0
                                    encode =
                                        unsafeJso<OptionEncode> {
                                            this.x = OptionEncodeValue(DimensionLoose(0.0))
                                            this.y = OptionEncodeValue(DimensionLoose(0.0))
                                        }
                                    typedData =
                                        dataItems(
                                            *data.map { row ->
                                                OptionDataItem(
                                                    Array(5) { dimension -> OptionDataValue(row[dimension]) },
                                                )
                                            }.toTypedArray(),
                                        )
                                    renderItem = { _, api ->
                                        val x = api.value(DimensionLoose(0.0)).unsafeCast<Double>()
                                        val y = api.value(DimensionLoose(1.0)).unsafeCast<Double>()
                                        val dx = api.value(DimensionLoose(2.0)).unsafeCast<Double>()
                                        val dy = api.value(DimensionLoose(3.0)).unsafeCast<Double>()
                                        val start =
                                            api.coord(
                                                CoordinateSystemDataCoord(
                                                    arrayOf<OptionDataValue?>(
                                                        OptionDataValue(kotlin.math.max(x - dx / 5, -180.0)),
                                                        OptionDataValue(kotlin.math.max(y - dy / 5, -90.0)),
                                                    ),
                                                ),
                                            )
                                        val end =
                                            api.coord(
                                                CoordinateSystemDataCoord(
                                                    arrayOf<OptionDataValue?>(
                                                        OptionDataValue(kotlin.math.min(x + dx / 5, 180.0)),
                                                        OptionDataValue(kotlin.math.min(y + dy / 5, 90.0)),
                                                    ),
                                                ),
                                            )
                                        unsafeJso<CustomPathOption> {
                                            type = "line"
                                            shape =
                                                unsafeJso<ZRShapeProps> {
                                                    x1 = start[0]
                                                    y1 = start[1]
                                                    x2 = end[0]
                                                    y2 = end[1]
                                                }
                                            style =
                                                unsafeJso<ZRStyleProps> {
                                                    lineWidth = 0.5
                                                    stroke = api.visual(VisualProps.color).unsafeCast<ZRColor>()
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
        },
        customExtra.interactive("geo-svg-custom-effect", "自定义特效（SVG）", "SVG Custom Effect") { chart ->
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
        },
    )
