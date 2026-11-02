package demo.examples.tree

import echarts.options.LabelOption
import echarts.options.LabelPosition
import echarts.options.TreeSeriesLeavesOption
import echarts.options.TreeSeriesNodeItemOption
import js.objects.unsafeJso
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import demo.examples.shared.every

/** Collapses every second child of [root], the preparation `tree-basic` and `tree-orient-*` do. */
internal fun collapseEverySecondChild(root: TreeSeriesNodeItemOption) {
    root.children?.forEachIndexed { index, child ->
        if (index % 2 == 0) child.collapsed = true
    }
}

/** Label of the non-leaf nodes: to the left of the node, right aligned. */
internal fun branchLabel(): LabelOption =
    unsafeJso {
        position = LabelPosition("left")
        verticalAlign = VerticalAlign.middle
        align = TextAlign.right
    }

/** Label of the leaves: to the right of the node, left aligned. */
internal fun leafLabel(): LabelOption =
    unsafeJso {
        position = LabelPosition("right")
        verticalAlign = VerticalAlign.middle
        align = TextAlign.left
    }

/** Leaves of a left-to-right tree: the mirror image of [branchLabel]. */
internal fun leaves(): TreeSeriesLeavesOption = unsafeJso { label = leafLabel() }
