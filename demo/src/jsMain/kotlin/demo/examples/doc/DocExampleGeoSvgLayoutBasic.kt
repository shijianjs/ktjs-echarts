package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import demo.examples.Category

private val docExampleGeoSvgLayoutBasicCategory = Category(Groups.DOC, "examples/doc/DocExampleGeoSvgLayoutBasic.kt")

val docExampleGeoSvgLayoutBasicEntry: CatalogEntry =
    docExampleGeoSvgLayoutBasicCategory.chart("doc-example/geo-svg-layout-basic", "SVG 地图的布局", "Geo SVG layout") {
            svgLayoutCells.groupBy { it.map }.forEach { (name, cells) -> registerSvgMap(name, cells.first().svg) }
            svgLayoutOption()
        }
