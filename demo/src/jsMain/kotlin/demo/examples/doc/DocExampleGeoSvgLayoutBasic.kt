package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import demo.examples.Category

internal class DocExampleGeoSvgLayoutBasic {
    companion object {
        private val CATEGORY = Category(Groups.DOC, "examples/doc/DocExampleGeoSvgLayoutBasic.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart("doc-example/geo-svg-layout-basic", "SVG 地图的布局", "Geo SVG layout") {
                    svgLayoutCells.groupBy { it.map }.forEach { (name, cells) -> registerSvgMap(name, cells.first().svg) }
                    svgLayoutOption()
                }
    }
}
