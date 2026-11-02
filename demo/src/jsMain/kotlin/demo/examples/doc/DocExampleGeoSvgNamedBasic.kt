package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category

private val docExampleGeoSvgNamedBasicCategory = Category(Groups.DOC, "examples/doc/DocExampleGeoSvgNamedBasic.kt")

val docExampleGeoSvgNamedBasicEntry: CatalogEntry =
    docExampleGeoSvgNamedBasicCategory.chart("doc-example/geo-svg-named-basic", "SVG 地图的命名区域", "Geo SVG named regions") {
            registerSvgMap("simple_svg", NAMED_SVG)
            unsafeJso<EChartsOption> {
                geo = one(unsafeJso<GeoOption> { map = "simple_svg" })
            }
        }
