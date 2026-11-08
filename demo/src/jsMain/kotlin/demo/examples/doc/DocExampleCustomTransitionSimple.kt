package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.round
import demo.examples.Category
import demo.examples.custom.coord
import demo.examples.shared.Lcg
import demo.examples.sunburst.radius

private val docExampleCustomTransitionSimpleCategory = Category(Groups.DOC, "examples/doc/DocExampleCustomTransitionSimple.kt")

val docExampleCustomTransitionSimpleEntry: CatalogEntry =
    docExampleCustomTransitionSimpleCategory.interactive(
            "doc-example/custom-transition-simple",
            "自定义系列的过渡动画",
            "Custom series transition",
        ) { chart, holder ->
            val random = Lcg(20261028)
            fun item(): Array<DoubleArray> = arrayOf(doubleArrayOf(random.next(), random.next()))

            chart.setOption(
                unsafeJso<EChartsOption> {
                    animationDurationUpdate = AnimationDuration(1500.0)
                    xAxis =
                        one(
                            unsafeJso<XAxisOption> {
                                min = AxisMinMax(0.0)
                                max = AxisMinMax(1.0)
                            },
                        )
                    yAxis =
                        one(
                            unsafeJso<YAxisOption> {
                                min = AxisMinMax(0.0)
                                max = AxisMinMax(1.0)
                            },
                        )
                    series =
                        arrayOf<SeriesOption>(
                            customSeries {
                                renderItem = { params, api ->
                                    val x = api.value(DimensionLoose(0.0)).unsafeCast<Double>()
                                    val y = api.value(DimensionLoose(1.0)).unsafeCast<Double>()
                                    val pos =
                                        api.coord(
                                            CoordinateSystemDataCoord(
                                                arrayOf(OptionDataValue(x), OptionDataValue(y)),
                                            ),
                                        )
                                    val width = 50.0 + random.next() * 150.0
                                    val height = 50.0 + random.next() * 150.0
                                    val radius = random.next() * 40.0
                                    val fill = ECharts.color.random()
                                    val lineWidth = 2.0 + round(random.next() * 20.0)
                                    val alignWidth = params.coordSys.width ?: 0.0
                                    unsafeJso<CustomGroupOption> {
                                        type = "group"
                                        children =
                                            arrayOf(
                                                transitionRect(
                                                    pos[0] / 2.0,
                                                    pos[1],
                                                    width,
                                                    height,
                                                    radius,
                                                    fill,
                                                    lineWidth,
                                                    "Has transition",
                                                    StringOrStringArray(
                                                        arrayOf("shape", "style", "x", "y"),
                                                    ),
                                                ),
                                                transitionRect(
                                                    pos[0] / 2.0 + alignWidth / 2.0,
                                                    pos[1],
                                                    width,
                                                    height,
                                                    radius,
                                                    fill,
                                                    lineWidth,
                                                    "No transition",
                                                    StringOrStringArray(emptyArray()),
                                                ),
                                            )
                                    }
                                }
                                typedData = points(*item())
                            },
                        )
                },
            )
            holder.setInterval(
                {
                    chart.setOption(
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    customSeries { typedData = points(*item()) },
                                )
                        },
                    )
                },
                2000,
            )
        }
