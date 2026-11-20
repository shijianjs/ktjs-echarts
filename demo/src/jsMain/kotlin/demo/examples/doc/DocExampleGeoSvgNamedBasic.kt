package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category

internal class DocExampleGeoSvgNamedBasic {
    companion object {
        private val CATEGORY = Category(Groups.DOC, "examples/doc/DocExampleGeoSvgNamedBasic.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart("doc-example/geo-svg-named-basic", "SVG 地图的命名区域", "Geo SVG named regions") {
                    registerSvgMap("simple_svg", NAMED_SVG)
                    unsafeJso<EChartsOption> {
                        geo = one(unsafeJso<GeoOption> { map = "simple_svg" })
                    }
                }
    }
}
