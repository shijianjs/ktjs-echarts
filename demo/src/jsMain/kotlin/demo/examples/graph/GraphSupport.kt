package demo.examples.graph

import io.github.shijianjs.ktjs.echarts.options.GraphDataItem
import io.github.shijianjs.ktjs.echarts.options.GraphEdgeItemOption
import io.github.shijianjs.ktjs.echarts.options.GraphNodeItemOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
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
