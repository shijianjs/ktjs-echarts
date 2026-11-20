package demo.examples.gl

import demo.CatalogEntry
import demo.Groups
import demo.examples.Category

internal class GraphglGpuLayout {
    companion object {
        private val CATEGORY = Category(Groups.GL, "examples/gl/GraphglGpuLayout.kt")

        val ENTRY: CatalogEntry =
            CATEGORY.chart(
                    "graphgl-gpu-layout",
                    "GraphGL - GPU 布局",
                    "GraphGL - GPU Layout",
                    "50×50 的网格节点与相邻边，ForceAtlas2 在 GPU 上把整张网格撑成规整的点阵。",
                    "A 50x50 mesh of nodes and their neighbour edges, opened up into a regular lattice by " +
                        "the ForceAtlas2 layout running on the GPU.",
                ) {
                    gpuLayoutOption()
                }
    }
}
