package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import io.github.shijianjs.ktjs.echarts.options.*
import io.github.shijianjs.ktjs.echarts.gl.options.*
import web.dom.document
import web.html.HtmlTagName
import demo.examples.Category

private val bar3dMythCategory = Category(Groups.GL, "examples/gl/Bar3dMyth.kt")

val bar3dMythEntry: CatalogEntry =
    bar3dMythCategory.interactive(
            "bar3d-myth",
            "Bar3D - 星云",
            "Bar3D - Myth",
            "把一张星云照片的每个像素变成一根细柱：高度取亮度并把视野压扁，柱子保留像素颜色，亮处像在发光。",
            "Every pixel of a nebula photograph becomes a thin bar: the luminance sets the height in a " +
                "squashed box, and the bars keep the colour of their pixel, so the bright parts glow.",
        ) { chart ->
            val picture = document.createElement(HtmlTagName.img)
            picture.listen(LOAD) { chart.setOption(mythOption(picture)) }
            picture.src = "data/img/gl/sample.jpg"
        }
