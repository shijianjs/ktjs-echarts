package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.Record
import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.sqrt
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
import zrender.ZRLineTypes
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * Custom series examples that do not fit the plain `renderItem` shape of `Custom.kt`: they read the
 * polar / calendar fields of `params.coordSys` or draw sectors instead of rectangles.
 *
 * Dropped on purpose: `custom-gauge` needs the `ROOT_PATH` panel images, and `custom-gantt-flight`
 * animates over a hand-built Gantt table. `custom-hexbin` lives in `CustomHexbin.kt`.
 *
 * `custom-spiral-race` drives itself with `ChartHolder.setTimeout`, so the timers stop when the
 * page is left.
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
        customExtra.interactive("custom-profile", "性能分析图", "Profile") { chart ->
            val categories = arrayOf("categoryA", "categoryB", "categoryC")
            val types =
                arrayOf(
                    Pair("JS Heap", "#7b9ce1"),
                    Pair("Documents", "#bd6d6c"),
                    Pair("Nodes", "#75d874"),
                    Pair("Listeners", "#e0bc78"),
                    Pair("GPU Memory", "#dc77dc"),
                    Pair("GPU", "#72b362"),
                )
            val start0 = 100000.0
            val datas = mutableListOf<OptionDataValue>()
            var base = start0
            for (cat in 0 until 3) {
                for (i in 0 until 10) {
                    val t = types[(cat * 3 + i) % types.size]
                    val duration = ((cat * 10 + i * 7) % 100 * 100 + 50).toDouble()
                    val start = base
                    val end = base + duration
                    datas.add(
                        arrayOf<OptionDataValue>(
                            OptionDataValue(cat.toDouble()),
                            OptionDataValue(start),
                            OptionDataValue(end),
                            OptionDataValue(duration),
                        ).unsafeCast<OptionDataValue>(),
                    )
                    base = end + ((cat * 5 + i * 3) % 20 * 100 + 50).toDouble()
                }
            }
            chart.setOption(
                unsafeJso<EChartsOption> {
                    tooltip =
                        one(
                            unsafeJso<TooltipOption> {
                                formatter =
                                    TooltipFormatter(
                                        js("(param) => { return param.marker + param.value[3] + ' ms'; }")
                                            .unsafeCast<TooltipFormatterCallback>(),
                                    )
                            },
                        )
                    title = one(unsafeJso<TitleOption> { text = "Profile"; left = StringOrDouble("center") })
                    dataZoom =
                        arrayOf<DataZoomOption>(
                            unsafeJso<DataZoomOption> {
                                type = DataZoomTypes.slider
                                filterMode = FilterModes.weakFilter
                                showDataShadow = BooleanOrString(false)
                            },
                            unsafeJso<DataZoomOption> {
                                type = DataZoomTypes.inside
                                filterMode = FilterModes.weakFilter
                            },
                        )
                    grid = one(unsafeJso<GridOption> { height = StringOrDouble(300.0) })
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                min = start0.unsafeCast<AxisMinMax>()
                                scale = true
                            },
                        )
                    yAxis = one(unsafeJso<YAxisOption> { data = strings(*categories) })
                    series =
                        arrayOf<SeriesOption>(
                            customSeries {
                                renderItem = { _, api ->
                                    val categoryIndex = api.value(DimensionLoose(0.0))
                                    val start =
                                        api.coord(
                                            CoordinateSystemDataCoord(
                                                arrayOf(api.value(DimensionLoose(1.0)), categoryIndex),
                                            ),
                                        )
                                    val end =
                                        api.coord(
                                            CoordinateSystemDataCoord(
                                                arrayOf(api.value(DimensionLoose(2.0)), categoryIndex),
                                            ),
                                        )
                                    val h = 10.0
                                    unsafeJso<CustomPathOption> {
                                        type = "rect"
                                        shape =
                                            unsafeJso {
                                                x = start[0]
                                                y = start[1] - h / 2
                                                width = end[0] - start[0]
                                                height = h
                                            }
                                        style = api.style()
                                    }
                                }
                                itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.8 }
                                encode =
                                    unsafeJso {
                                        this.x =
                                            OptionEncodeValue(
                                                arrayOf<DimensionLoose>(DimensionLoose(1.0), DimensionLoose(2.0)),
                                            )
                                        this.y = OptionEncodeValue(DimensionLoose(0.0))
                                    }
                                typedData = datas.toTypedArray().unsafeCast<Array<OptionDataItem?>>()
                            },
                        )
                },
            )
            null
        },
        customExtra.interactive("custom-spiral-race", "自定义螺旋线竞速", "Spiral Race") { chart, holder ->
            // Upstream replays a pre-recorded table: every step widens the three spirals.
            var currentDataIndex = 0
            chart.setOption(
                unsafeJso<EChartsOption> {
                    animationDuration = AnimationDuration(spiralAnimationDuration)
                    animationDurationUpdate = AnimationDuration(spiralAnimationDurationUpdate)
                    animationEasingUpdate = AnimationEasings.linear
                    dataset = one(spiralDataset(currentDataIndex))
                    tooltip = one(unsafeJso<TooltipOption> {})
                    angleAxis =
                        one(
                            unsafeJso<AngleAxisOption> {
                                type = AxisTypes.value
                                splitArea = unsafeJso<SplitAreaOption> { show = true }
                                axisLabel =
                                    unsafeJso<AxisLabelOption> {
                                        formatter =
                                            LabelFormatter(
                                                js("(val) => ['Aries','Taurus','Gemini','Cancer','Leo','Virgo','Libra','Scorpius','Sagittarius','Capricornus','Aquarius','Pisces'][val]")
                                                    as LabelFormatterCallback,
                                            )
                                        color = "rgba(0,0,0,0.2)"
                                    }
                                axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(0,0,0,0.2)") } }
                                min = AxisMinMax(0.0)
                                max = AxisMinMax(valOnRoundRadian.toDouble())
                            },
                        )
                    radiusAxis =
                        one(
                            unsafeJso<RadiusAxisOption> {
                                type = AxisTypes.value
                                interval = AxisInterval(1.0)
                                splitLine = unsafeJso<SplitLineOption> { show = false }
                                axisLabel =
                                    unsafeJso<AxisLabelOption> {
                                        color = "rgba(0,0,0,0.6)"
                                        formatter =
                                            LabelFormatter(
                                                js("(val) => val === 1 ? 'A' : val === 2 ? 'B' : val === 3 ? 'C' : ''") as LabelFormatterCallback,
                                            )
                                    }
                                axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                                axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("rgba(0,0,0,0.2)") } }
                                min = AxisMinMax(0.0)
                                max = AxisMinMax(getMaxRadius(currentDataIndex))
                            },
                        )
                    polar = one(unsafeJso<PolarOption> {})
                    series =
                        arrayOf<SeriesOption>(
                            customSeries {
                                coordinateSystem = "polar"
                                renderItem = { params, api ->
                                    val dataIndex = params.dataIndex.toInt()
                                    val children = mutableListOf<CustomElementOption>()
                                    val valOnStartRadius = api.value(DimensionLoose(0.0)).unsafeCast<Double>()
                                    val valOnEndRadian = api.value(DimensionLoose(1.0)).unsafeCast<Double>()
                                    val coords =
                                        api.coord(
                                            CoordinateSystemDataCoord(
                                                arrayOf(OptionDataValue(valOnStartRadius), OptionDataValue(valOnEndRadian)),
                                            ),
                                        )
                                    val startRadius = coords[2]
                                    val endRadian = coords[3]
                                    val widthRadius =
                                        api.coord(
                                            CoordinateSystemDataCoord(
                                                arrayOf(OptionDataValue(barWidthValue), OptionDataValue(0.0)),
                                            ),
                                        )[2]
                                    val cx = params.coordSys.cx ?: 0.0
                                    val cy = params.coordSys.cy ?: 0.0
                                    val points = makeShapePoints(cx, cy, widthRadius, startRadius, endRadian)
                                    children.add(
                                        unsafeJso<CustomPathOption> {
                                            type = "polygon"
                                            shape = unsafeJso<ZRShapeProps> { this.points = points }
                                            style = unsafeJso<ZRStyleProps> { fill = spiralColors[dataIndex].fill }
                                        },
                                    )
                                    val point = makeLabelPosition(cx, cy, widthRadius, startRadius, endRadian)
                                    children.add(
                                        unsafeJso<CustomTextOption> {
                                            type = "text"
                                            x = point[0]
                                            y = point[1]
                                            z2 = 50.0
                                            style =
                                                unsafeJso<SpiralTextStyle> {
                                                    text = makeText(endRadian)
                                                    fill = spiralColors[dataIndex].text
                                                    stroke = ZRColor("#fff")
                                                    lineWidth = 3.0
                                                    fontSize = StringOrDouble(16.0)
                                                    align = TextAlign.center
                                                    verticalAlign = VerticalAlign.middle
                                                    rich =
                                                        recordOf(
                                                            "round" to unsafeJso<ZRStyleProps> { fontSize = StringOrDouble(24.0) },
                                                            "percent" to unsafeJso<ZRStyleProps> { fontSize = StringOrDouble(18.0) },
                                                        )
                                                }
                                        },
                                    )
                                    unsafeJso<CustomGroupOption> { type = "group"; this.children = children.toTypedArray() }
                                }
                            },
                        )
                },
            )
            fun next() {
                currentDataIndex++
                if (currentDataIndex >= datasourceList.size) return
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        dataset = one(spiralDataset(currentDataIndex))
                        radiusAxis = one(unsafeJso<RadiusAxisOption> { max = AxisMinMax(getMaxRadius(currentDataIndex)) })
                    },
                )
                if (currentDataIndex < datasourceList.size - 1) {
                    holder.setTimeout(::next, spiralAnimationDurationUpdate.toInt())
                }
            }
            holder.setTimeout(::next, 1000)
            null
        },
    )

/** Text style of the spiral labels; [ZRStyleProps] does not declare `rich` yet. */
private external interface SpiralTextStyle : ZRStyleProps {
    var rich: Record<String, ZRStyleProps>?
}

/** Content of the `dataset` of step [index]: `[radius value, radian value]` pairs. */
private fun spiralDataset(index: Int): DatasetOption =
    unsafeJso {
        source =
            datasetArrayRows(
                Array(datasourceList[index].size) { row ->
                    Array(datasourceList[index][row].size) { column -> OptionDataValue(datasourceList[index][row][column]) }
                },
            )
    }
private external interface SpiralColor {
    var fill: ZRColor
    var text: ZRColor
}

private val spiralColors =
    arrayOf(
        unsafeJso<SpiralColor> { fill = ZRColor("#5470c6"); text = ZRColor("#2747a5") },
        unsafeJso<SpiralColor> { fill = ZRColor("#91cc75"); text = ZRColor("#447f27") },
        unsafeJso<SpiralColor> { fill = ZRColor("#fac858"); text = ZRColor("#a0761c") },
    )

private val radianLabels =
    arrayOf(
        "Aries", "Taurus", "Gemini", "Cancer", "Leo", "Virgo", "Libra", "Scorpius",
        "Sagittarius", "Capricornus", "Aquarius", "Pisces",
    )
private val valOnRoundRadian = radianLabels.size
private val radianStep = kotlin.math.PI / 45
private val barWidthValue = 0.4
private val valOnRadiusStep = 4.0
private val startRadian = kotlin.math.PI / 2
private const val spiralAnimationDuration = 5000.0
private const val spiralAnimationDurationUpdate = 7000.0

private val datasourceList: Array<Array<Array<Double>>> =
    arrayOf(
        arrayOf(arrayOf(1.0, 3.0), arrayOf(2.0, 6.0), arrayOf(3.0, 9.0)),
        arrayOf(arrayOf(1.0, 12.0), arrayOf(2.0, 16.0), arrayOf(3.0, 14.0)),
        arrayOf(arrayOf(1.0, 17.0), arrayOf(2.0, 22.0), arrayOf(3.0, 19.0)),
        arrayOf(arrayOf(1.0, 19.0), arrayOf(2.0, 33.0), arrayOf(3.0, 24.0)),
        arrayOf(arrayOf(1.0, 24.0), arrayOf(2.0, 42.0), arrayOf(3.0, 29.0)),
        arrayOf(arrayOf(1.0, 27.0), arrayOf(2.0, 47.0), arrayOf(3.0, 41.0)),
        arrayOf(arrayOf(1.0, 36.0), arrayOf(2.0, 52.0), arrayOf(3.0, 52.0)),
        arrayOf(arrayOf(1.0, 46.0), arrayOf(2.0, 59.0), arrayOf(3.0, 63.0)),
        arrayOf(arrayOf(1.0, 60.0), arrayOf(2.0, 63.0), arrayOf(3.0, 69.0)),
    )

private fun getSpiralValueOnRadius(valOnStartRadius: Double, valOnEndAngle: Double): Double =
    valOnStartRadius + valOnRadiusStep * (valOnEndAngle / valOnRoundRadian)

private fun getSpiralRadius(startRadius: Double, endRadian: Double, radiusStep: Double): Double =
    startRadius + radiusStep * ((startRadian - endRadian) / (kotlin.math.PI * 2))

private fun getRadiusStepByWidth(widthRadius: Double): Double = (widthRadius / barWidthValue) * valOnRadiusStep

private fun convertToPolarPoint(radius: Double, radian: Double, cx: Double, cy: Double): Array<Double> =
    arrayOf(kotlin.math.cos(radian) * radius + cx, -kotlin.math.sin(radian) * radius + cy)

private fun makeShapePoints(
    cx: Double,
    cy: Double,
    widthRadius: Double,
    startRadius: Double,
    endRadian: Double,
): Array<Array<Double>> {
    val points = mutableListOf<Array<Double>>()
    val radiusStep = getRadiusStepByWidth(widthRadius)
    var iRadian = startRadian
    val end1 = endRadian - radianStep
    while (iRadian > end1) {
        if (iRadian < endRadian) iRadian = endRadian
        val iRadius = getSpiralRadius(startRadius - widthRadius, iRadian, radiusStep)
        points.add(convertToPolarPoint(iRadius, iRadian, cx, cy))
        iRadian -= radianStep
    }
    iRadian = endRadian
    val end2 = startRadian + radianStep
    while (iRadian < end2) {
        if (iRadian > startRadian) iRadian = startRadian
        val iRadius = getSpiralRadius(startRadius + widthRadius, iRadian, radiusStep)
        points.add(convertToPolarPoint(iRadius, iRadian, cx, cy))
        iRadian += radianStep
    }
    return points.toTypedArray()
}

private fun makeLabelPosition(
    cx: Double,
    cy: Double,
    widthRadius: Double,
    startRadius: Double,
    endRadian: Double,
): Array<Double> {
    val radiusStep = getRadiusStepByWidth(widthRadius)
    val iRadius = getSpiralRadius(startRadius, endRadian, radiusStep)
    return convertToPolarPoint(iRadius, endRadian - 10 / iRadius, cx, cy)
}

private fun getMaxRadius(idx: Int): Double {
    var radius = 0.0
    for (item in datasourceList[idx]) {
        radius = kotlin.math.max(radius, getSpiralValueOnRadius(item[0], item[1]))
    }
    return kotlin.math.ceil(radius * 1.2)
}

private fun makeText(endRadian: Double): String {
    val radian = startRadian - endRadian
    val pi2 = kotlin.math.PI * 2
    val round = kotlin.math.floor(radian / pi2)
    val p = ((radian / pi2) % 1) * 100
    val percent = (kotlin.math.round(p * 10) / 10.0).toString() + "%"
    return "Round {round|" + round.toInt() + "}\n{percent|" + percent + "}"
}