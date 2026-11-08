package demo.examples.graph

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.matrix.names
import demo.examples.shared.every
import demo.examples.shared.fixed
import demo.examples.shared.legendNames
import demo.examples.shared.numeric

/** Shape of `les-miserables.json`: character nodes with a fixed position, their links and categories. */
internal external interface LesMiserablesNode {
    val id: String
    val name: String
    val symbolSize: Double
    val x: Double
    val y: Double
    val value: Double
    val category: Double
}

/** One link of `les-miserables.json`, referencing nodes by id. */
internal external interface LesMiserablesLink {
    val source: String
    val target: String
}

/** One node category of `les-miserables.json`; only the name is read. */
internal external interface LesMiserablesCategory {
    val name: String
}

/** The whole `les-miserables.json` table. */
internal external interface LesMiserablesData {
    val nodes: Array<LesMiserablesNode>
    val links: Array<LesMiserablesLink>
    val categories: Array<LesMiserablesCategory>
}

/**
 * The nodes of `les-miserables.json` as graph data items.
 *
 * [size] overrides the `symbolSize` of every node (`graph-force` draws all of them at 5), and
 * [labelAbove] shows the label only for the nodes whose own size is above it, the `node.label.show`
 * loop of the upstream callback.
 */
internal fun lesNodes(
    table: LesMiserablesData,
    size: Double? = null,
    labelAbove: Double? = null,
): Array<GraphDataItem?> =
    Array(table.nodes.size) { index ->
        val node = table.nodes[index]
        GraphDataItem(
            unsafeJso<GraphNodeItemOption> {
                id = node.id
                name = node.name
                x = node.x
                y = node.y
                value = OptionDataValueOrArray(node.value)
                category = StringOrDouble(node.category)
                symbolSize = SymbolSize(size ?: node.symbolSize)
                if (labelAbove != null) {
                    label = unsafeJso<LabelOption> { show = node.symbolSize > labelAbove }
                }
            },
        )
    }

/** The links of `les-miserables.json`; the ends are node ids. */
internal fun lesLinks(table: LesMiserablesData): Array<GraphEdgeItemOption> =
    Array(table.links.size) { index ->
        val link = table.links[index]
        unsafeJso<GraphEdgeItemOption> {
            source = StringOrDouble(link.source)
            target = StringOrDouble(link.target)
        }
    }

/** The categories of `les-miserables.json`, the legend of the four pages. */
internal fun lesCategories(table: LesMiserablesData): Array<GraphCategoryItemOption> =
    Array(table.categories.size) { index ->
        unsafeJso<GraphCategoryItemOption> { name = table.categories[index].name }
    }

/** Legend names of the four `les-miserables` pages. */
internal fun lesLegend(table: LesMiserablesData): Array<LegendOption> =
    one(unsafeJso<LegendOption> { data = legendNames(*Array(table.categories.size) { table.categories[it].name }) })

/** The shared series of the `les-miserables` pages; [block] sets the layout of the page. */
internal fun lesSeries(
    table: LesMiserablesData,
    size: Double? = null,
    labelAbove: Double? = null,
    block: GraphSeriesOption.() -> Unit,
): GraphSeriesOption =
    graphSeries {
        name = "Les Miserables"
        typedData = lesNodes(table, size, labelAbove)
        links = lesLinks(table)
        categories = lesCategories(table)
        roam = BooleanOrString(true)
        label =
            unsafeJso<LabelOption> {
                position = LabelPosition("right")
                formatter = LabelFormatter("{b}")
            }
        lineStyle =
            unsafeJso<LineStyleOption> {
                color = ZRColor("source")
                curveness = 0.3
            }
        block()
    }

/** Shape of `npmdepgraph.min10.json`: placed package nodes plus edges that reference them by id. */
internal external interface NpmGraphNode {
    val x: Double
    val y: Double
    val id: String
    val label: String
    val size: Double
    val color: String
}

/** One edge of `npmdepgraph.min10.json`. */
internal external interface NpmGraphEdge {
    val sourceID: String
    val targetID: String
}

/** The whole `npmdepgraph.min10.json` table. */
internal external interface NpmGraph {
    val nodes: Array<NpmGraphNode>
    val edges: Array<NpmGraphEdge>
}

/** The package nodes of `npmdepgraph.min10.json`, each one with its upstream colour. */
internal fun npmNodes(table: NpmGraph): Array<GraphDataItem?> =
    Array(table.nodes.size) { index ->
        val node = table.nodes[index]
        GraphDataItem(
            unsafeJso<GraphNodeItemOption> {
                x = node.x
                y = node.y
                id = node.id
                name = node.label
                symbolSize = SymbolSize(node.size)
                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor(node.color) }
            },
        )
    }

/** The edges of `npmdepgraph.min10.json`, the two ends are package ids. */
internal fun npmEdges(table: NpmGraph): Array<GraphEdgeItemOption> =
    Array(table.edges.size) { index ->
        val edge = table.edges[index]
        unsafeJso<GraphEdgeItemOption> {
            source = StringOrDouble(edge.sourceID)
            target = StringOrDouble(edge.targetID)
        }
    }

/** Shape of `webkit-dep.json`: nodes without an id and links that reference the node index. */
internal external interface WebkitNode {
    val name: String
    val value: Double
    val category: Double
}

/** One link of `webkit-dep.json`; the ends are node indexes. */
internal external interface WebkitLink {
    val source: Double
    val target: Double
}

/** The whole `webkit-dep.json` table. */
internal external interface WebkitDepData {
    val nodes: Array<WebkitNode>
    val links: Array<WebkitLink>
}

/** The five categories of `webkit-dep.json`, in the order the upstream legend lists them. */
internal val WEBKIT_CATEGORIES = arrayOf("HTMLElement", "WebGL", "SVG", "CSS", "Other")

/** The nodes of `webkit-dep.json`; upstream assigns the index as the id, which the links rely on. */
internal fun webkitNodes(table: WebkitDepData): Array<GraphDataItem?> =
    Array(table.nodes.size) { index ->
        val node = table.nodes[index]
        GraphDataItem(
            unsafeJso<GraphNodeItemOption> {
                id = "$index"
                name = node.name
                value = OptionDataValueOrArray(node.value)
                category = StringOrDouble(node.category)
            },
        )
    }

/** The links of `webkit-dep.json`; the numeric ends are node indexes. */
internal fun webkitEdges(table: WebkitDepData): Array<GraphEdgeItemOption> =
    Array(table.links.size) { index ->
        val link = table.links[index]
        unsafeJso<GraphEdgeItemOption> {
            source = StringOrDouble(link.source)
            target = StringOrDouble(link.target)
        }
    }
