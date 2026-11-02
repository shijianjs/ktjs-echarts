package demo.examples.custom

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps
import demo.examples.Category
import demo.examples.gauge.center
import demo.examples.scatter.point
import demo.examples.shared.axisLabel
import demo.examples.shared.every
import demo.examples.shared.percent

private val customSpiralRaceCategory = Category(Groups.CUSTOM, "examples/custom/CustomSpiralRace.kt")

val customSpiralRaceEntry: CatalogEntry =
    customSpiralRaceCategory.interactive("custom-spiral-race", "自定义螺旋线竞速", "Spiral Race") { chart, holder ->
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
        }
