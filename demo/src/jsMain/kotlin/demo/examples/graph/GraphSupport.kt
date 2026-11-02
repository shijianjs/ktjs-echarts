package demo.examples.graph

import echarts.options.GraphDataItem
import echarts.options.GraphEdgeItemOption
import echarts.options.GraphNodeItemOption
import echarts.options.StringOrDouble
import js.objects.unsafeJso

/** One node with an explicit position. */
internal fun node(name: String, x: Double, y: Double): GraphDataItem =
    GraphDataItem(
        unsafeJso<GraphNodeItemOption> {
            this.name = name
            this.x = x
            this.y = y
        },
    )

/** A node of the force layout, identified by id. */
internal fun idNode(id: Int): GraphDataItem =
    GraphDataItem(unsafeJso<GraphNodeItemOption> { this.id = "$id" })

/** One link between two nodes. */
internal fun link(source: String, target: String, block: GraphEdgeItemOption.() -> Unit = {}): GraphEdgeItemOption =
    unsafeJso {
        this.source = StringOrDouble(source)
        this.target = StringOrDouble(target)
        block()
    }
