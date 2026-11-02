package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.funnel.labels
import demo.examples.gauge.center
import demo.examples.geo.region

private val docExampleGeoSvgLabelBasicCategory = Category(Groups.DOC, "examples/doc/DocExampleGeoSvgLabelBasic.kt")

val docExampleGeoSvgLabelBasicEntry: CatalogEntry =
    docExampleGeoSvgLabelBasicCategory.chart("doc-example/geo-svg-label-basic", "SVG 地图的区域标签", "Geo SVG region labels") {
            registerSvgMap("simple_svg", LABELLED_SVG)
            unsafeJso<EChartsOption> {
                geo =
                    one(
                        unsafeJso<GeoOption> {
                            map = "simple_svg"
                            regions =
                                arrayOf(
                                    svgRegion(
                                        "name_a",
                                        "This two rects are wrapped in a <g name=\"...\">",
                                        "So a single label is displayed in the center",
                                        "of the bounding rect of the <g>",
                                    ),
                                    svgRegion(
                                        "name_b",
                                        "This two rects are named",
                                        "with the same name.",
                                        "So each of them has a label.",
                                    ),
                                )
                        },
                    )
            }
        }
