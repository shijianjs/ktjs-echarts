package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.ui.listen
import web.dom.document
import web.html.HtmlTagName
import demo.examples.Category

private val bar3dVoxelizeImageCategory = Category(Groups.GL, "examples/gl/Bar3dVoxelizeImage.kt")

val bar3dVoxelizeImageEntry: CatalogEntry =
    bar3dVoxelizeImageCategory.interactive(
            "bar3d-voxelize-image",
            "图像体素化",
            "Voxelize image",
            "一枚硬币的照片被压到八十行像素，变成一片金属质感的柱阵，亮处的颜色溢出成高光。",
            "A coin photo is squeezed to eighty rows of pixels and rebuilt as a metal bar field whose " +
                "bright pixels overflow into highlights.",
        ) { chart ->
            val picture = document.createElement(HtmlTagName.img)
            picture.listen(LOADGlImageBars) { chart.setOption(voxelizeOption(picture)) }
            picture.src = "data/img/gl/bitcoin.png"
        }
