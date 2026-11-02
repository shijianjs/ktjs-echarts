package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import echarts.options.*
import echartsgl.options.*
import web.dom.document
import web.html.HtmlTagName
import demo.examples.Category

private val imageSurfaceSushuangCategory = Category(Groups.GL, "examples/gl/ImageSurfaceSushuang.kt")

val imageSurfaceSushuangEntry: CatalogEntry =
    imageSurfaceSushuangCategory.interactive(
            "image-surface-sushuang",
            "Image Surface Sushuang",
            "Image Surface Sushuang",
            "把一张照片的每个像素当作曲面顶点：亮度决定高度，像素本身的颜色决定曲面颜色。",
            "Every pixel of a photograph becomes a surface vertex: its luminance is the height and " +
                "its colour is the colour of the surface.",
        ) { chart ->
            val picture = document.createElement(HtmlTagName.img)
            picture.listen(LOAD) { chart.setOption(sushuangOption(picture)) }
            picture.src = "data/img/gl/sushuang.jpg"
        }
