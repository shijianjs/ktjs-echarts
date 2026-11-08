package demo.examples.geo

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadText
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.gauge.center

private val geoBeefCutsCategory = Category(Groups.GEO, "examples/geo/GeoBeefCuts.kt")

val geoBeefCutsEntry: CatalogEntry =
    geoBeefCutsCategory.interactive("geo-beef-cuts", "庖丁解牛", "GEO Beef Cuts") { chart ->
            loadText("data/Beef_cuts_France.svg").then { svg ->
                ECharts.registerMap(
                    "Beef_cuts_France",
                    MapInput(unsafeJso<SVGMapInput> { this.svg = GeoSVGSourceInput(svg) }),
                )
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        tooltip = one(unsafeJso<TooltipOption> {})
                        visualMap =
                            one(
                                unsafeJso<VisualMapOption> {
                                    left = StringOrDouble("center")
                                    bottom = StringOrDouble("10%")
                                    min = 5.0
                                    max = 100.0
                                    orient = Orient("horizontal")
                                    text = arrayOf("", "Price")
                                    realtime = true
                                    calculable = true
                                    inRange =
                                        unsafeJso<VisualOptionUnit> {
                                            color = StringOrStringArray(arrayOf("#dbac00", "#db6e00", "#cf0000"))
                                        }
                                },
                            )
                        series =
                            one(
                                mapSeries {
                                    name = "French Beef Cuts"
                                    map = "Beef_cuts_France"
                                    roam = BooleanOrString(true)
                                    emphasis = unsafeJso<EmphasisOption> { label = unsafeJso<LabelOption> { show = false } }
                                    selectedMode = SelectionMode(false)
                                    typedData =
                                        dataItems(
                                            beefCut("Queue", 15.0),
                                            beefCut("Langue", 35.0),
                                            beefCut("Plat de joue", 15.0),
                                            beefCut("Gros bout de poitrine", 25.0),
                                            beefCut("Jumeau à pot-au-feu", 45.0),
                                            beefCut("Onglet", 85.0),
                                            beefCut("Plat de tranche", 25.0),
                                            beefCut("Araignée", 15.0),
                                            beefCut("Gîte à la noix", 55.0),
                                            beefCut("Bavette d'aloyau", 25.0),
                                            beefCut("Tende de tranche", 65.0),
                                            beefCut("Rond de gîte", 45.0),
                                            beefCut("Bavettede de flanchet", 85.0),
                                            beefCut("Flanchet", 35.0),
                                            beefCut("Hampe", 75.0),
                                            beefCut("Plat de côtes", 65.0),
                                            beefCut("Tendron Milieu de poitrine", 65.0),
                                            beefCut("Macreuse à pot-au-feu", 85.0),
                                            beefCut("Rumsteck", 75.0),
                                            beefCut("Faux-filet", 65.0),
                                            beefCut("Côtes Entrecôtes", 55.0),
                                            beefCut("Basses côtes", 45.0),
                                            beefCut("Collier", 85.0),
                                            beefCut("Jumeau à biftek", 15.0),
                                            beefCut("Paleron", 65.0),
                                            beefCut("Macreuse à bifteck", 45.0),
                                            beefCut("Gîte", 85.0),
                                            beefCut("Aiguillette baronne", 65.0),
                                            beefCut("Filet", 95.0),
                                        )
                                },
                            )
                    },
                )
                null
                }
            }
