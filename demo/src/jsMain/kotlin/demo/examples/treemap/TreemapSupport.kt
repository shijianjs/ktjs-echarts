package demo.examples.treemap

import echarts.options.OptionDataValue
import echarts.options.OptionId
import echarts.options.TreemapSeriesNodeItemOption
import js.objects.unsafeJso

/** A node of a treemap, with optional children. */
internal fun treemapNode(
    name: String,
    value: Double,
    vararg children: TreemapSeriesNodeItemOption,
): TreemapSeriesNodeItemOption =
    unsafeJso {
        this.name = OptionId(name)
        this.value = OptionDataValue(value)
        if (children.isNotEmpty()) this.children = Array(children.size) { children[it] }
    }
