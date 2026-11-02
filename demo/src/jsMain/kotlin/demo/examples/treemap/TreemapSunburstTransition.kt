package demo.examples.treemap

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor
import demo.examples.Category
import demo.examples.sunburst.radius

private val treemapSunburstTransitionCategory = Category(Groups.TREEMAP, "examples/treemap/TreemapSunburstTransition.kt")

val treemapSunburstTransitionEntry: CatalogEntry =
    treemapSunburstTransitionCategory.interactive(
            "treemap-sunburst-transition",
            "矩形树图和旭日图的动画过渡",
            "Transition between Treemap and Sunburst",
            setup = { chart, holder ->
                loadJson("data/echarts-package-size.json").then { json ->
                    val node = json.unsafeCast<PackageSizeTree>()
                    val treemap =
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    treemapSeries {
                                        id = "echarts-package-size"
                                        animationDurationUpdate = AnimationDuration(1000.0)
                                        roam = BooleanOrString(false)
                                        universalTransition = BooleanOrUniversalTransitionOption(true)
                                        label = unsafeJso<LabelOption> { show = true }
                                        breadcrumb = unsafeJso<TreemapBreadcrumbOption> { show = false }
                                        typedData = node.children
                                    },
                                )
                        }
                    val sunburst =
                        unsafeJso<EChartsOption> {
                            series =
                                arrayOf<SeriesOption>(
                                    sunburstSeries {
                                        id = "echarts-package-size"
                                        radius =
                                            StringOrDoubleOrArray(
                                                arrayOf(StringOrDouble("20%"), StringOrDouble("90%")),
                                            )
                                        animationDurationUpdate = AnimationDuration(1000.0)
                                        universalTransition = BooleanOrUniversalTransitionOption(true)
                                        itemStyle =
                                            unsafeJso<ItemStyleOption> {
                                                borderWidth = 1.0
                                                borderColor = ZRColor("rgba(255,255,255,.5)")
                                            }
                                        label = unsafeJso<LabelOption> { show = false }
                                        typedData = node.children.unsafeCast<Array<SunburstSeriesNodeItemOption?>>()
                                    },
                                )
                        }
                    var current = treemap
                    chart.setOption(current)
                    holder.setInterval(
                        {
                            current = if (current === treemap) sunburst else treemap
                            chart.setOption(current)
                        },
                        3000,
                    )
                    null
                }
            },
        )
