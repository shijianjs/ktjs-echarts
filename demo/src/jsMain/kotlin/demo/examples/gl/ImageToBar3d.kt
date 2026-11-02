package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import web.dom.document
import web.html.HtmlTagName
import demo.examples.Category
import demo.examples.shared.every

private val imageToBar3dCategory = Category(Groups.GL, "examples/gl/ImageToBar3d.kt")

val imageToBar3dEntry: CatalogEntry =
    imageToBar3dCategory.interactive(
            "image-to-bar3d",
            "图像转三维柱状图",
            "Image to Bar3D",
            "把一张肖像的亮度反过来变成柱高：深色头发立得最高，柱子的颜色就是它所在像素的颜色。",
            "The luminance of a portrait is inverted into the bar height, so the dark hair stands " +
                "tallest, and every bar keeps the color of the pixel it stands for.",
        ) { chart ->
            val picture = document.createElement(HtmlTagName.img)
            picture.listen(LOADGlImageBars) { chart.setOption(imageToBar3DOption(picture)) }
            picture.src = "data/img/gl/portrait.jpg"
        }
