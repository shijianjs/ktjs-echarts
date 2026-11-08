package demo.examples.pictorialbar

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.axisLabel

private val pictorialBarVelocityCategory = Category(Groups.PICTORIAL_BAR, "examples/pictorialbar/PictorialBarVelocity.kt")

val pictorialBarVelocityEntry: CatalogEntry =
    pictorialBarVelocityCategory.chart(
            "pictorialBar-velocity",
            "驯鹿的速度",
            "Velocity of Christmas Reindeers",
            "透明的小山形状作为背景柱，柱顶再用 `symbolPosition: end` 与 `symbolOffset` 挂上" +
                "速度相称的剪影，从驯鹿一路排到步行。",
            "A translucent hill is the background bar; `symbolPosition: end` and `symbolOffset` hang " +
                "a silhouette of the matching speed on top of each one, from reindeer down to walking.",
        ) {
            unsafeJso<EChartsOption> {
                color = arrayOf(ZRColor("#e54035"))
                tooltip = one(velocityTooltip())
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            data = strings(*VELOCITY_NAMES)
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { color = "#e54035" }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisTick = unsafeJso<AxisTickOption> { show = BooleanOrString(false) }
                            axisLine = unsafeJso<AxisLineOption> { show = BooleanOrString(false) }
                            axisLabel = unsafeJso<AxisLabelOption> { show = false }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        pictorialBarSeries {
                            name = "hill"
                            barCategoryGap = StringOrDouble("-130%")
                            symbol = VELOCITY_HILL
                            itemStyle = unsafeJso<ItemStyleOption> { opacity = 0.5 }
                            emphasis =
                                unsafeJso<EmphasisOption> {
                                    itemStyle = unsafeJso<ItemStyleOption> { opacity = 1.0 }
                                }
                            typedData = numbers(*VELOCITY_VALUES)
                            z = 10.0
                        },
                        pictorialBarSeries {
                            name = "glyph"
                            barGap = StringOrDouble("-100%")
                            symbolPosition = PictorialBarSymbolPositions.end
                            symbolSize = StringOrDoubleOrArray(50.0)
                            symbolOffset =
                                StringOrDoubleOrArray(
                                    arrayOf(StringOrDouble(0.0), StringOrDouble("-120%")),
                                )
                            typedData =
                                dataItems(
                                    *Array(VELOCITY_NAMES.size) { index -> velocityGlyph(index) },
                                )
                        },
                    )
            }
        }
