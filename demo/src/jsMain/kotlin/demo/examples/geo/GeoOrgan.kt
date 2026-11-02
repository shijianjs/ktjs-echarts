package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category

private val geoOrganCategory = Category(Groups.GEO, "examples/geo/GeoOrgan.kt")

val geoOrganEntry: CatalogEntry =
    geoOrganCategory.interactive("geo-organ", "内脏数据（SVG）", "Organ Data") { chart ->
            loadText("data/Veins_Medical_Diagram_clip_art.svg").then { svg ->
                ECharts.registerMap(
                    "organ_diagram",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        geo =
                            one(
                                unsafeJso<GeoOption> {
                                    left = StringOrDouble(10.0)
                                    right = StringOrDouble("50%")
                                    map = "organ_diagram"
                                    selectedMode = SelectionMode("multiple")
                                    emphasis =
                                        unsafeJso<EmphasisOption> {
                                            focus = "self".unsafeCast<EmphasisFocus>()
                                            itemStyle = unsafeJso<ItemStyleOption> {}
                                            label =
                                                unsafeJso<LabelOption> {
                                                    position = LabelPosition("bottom")
                                                    distance = 0.0
                                                    textBorderColor = "#fff"
                                                    textBorderWidth = 2.0
                                                }
                                        }
                                },
                            )
                        grid =
                            one(
                                unsafeJso<GridOption> {
                                    left = StringOrDouble("60%")
                                    top = StringOrDouble("20%")
                                    bottom = StringOrDouble("20%")
                                },
                            )
                        xAxis = one(unsafeJso<XAxisOption> {})
                        yAxis =
                            one(
                                unsafeJso<YAxisOption> {
                                    data =
                                        strings(
                                            "heart",
                                            "large-intestine",
                                            "small-intestine",
                                            "spleen",
                                            "kidney",
                                            "lung",
                                            "liver",
                                        )
                                },
                            )
                        series =
                            one(
                                barSeries {
                                    typedData =
                                        numbers(
                                            121.0, 321.0, 141.0, 52.0, 198.0, 289.0, 139.0,
                                        )
                                },
                            )
                    },
                )
                null
            }
        }
