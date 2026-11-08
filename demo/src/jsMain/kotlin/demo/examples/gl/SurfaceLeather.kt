package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.options.*
import web.dom.document
import web.html.HtmlTagName
import demo.examples.Category

private val surfaceLeatherCategory = Category(Groups.GL, "examples/gl/SurfaceLeather.kt")

val surfaceLeatherEntry: CatalogEntry =
    surfaceLeatherCategory.interactive(
            "surface-leather",
            "皮革材质",
            "Leather Material",
            "皮革贴图压出凹凸的球体：高度图改变顶点位置，粗糙度与法线贴图补上细纹。",
            "A leather ball: a height map displaces the vertices and the roughness and normal maps " +
                "add the grain.",
        ) { chart ->
            val heightMap = document.createElement(HtmlTagName.img)
            heightMap.listen(LOADGlSurfaceMaterial) { chart.setOption(leatherOption(leatherScale(heightMap))) }
            heightMap.src = "data/img/gl/leather/leather_height.jpg"
        }
