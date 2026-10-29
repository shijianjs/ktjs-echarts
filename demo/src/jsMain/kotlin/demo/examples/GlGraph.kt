package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.options.DoubleOrDoubleArray
import echarts.options.EChartsOption
import echarts.options.OptionDataValueOrArray
import echarts.options.OptionId
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolSize
import echartsgl.GlColor
import echartsgl.GraphGLFocusNodeAdjacencyOns
import echartsgl.options.GraphGLCategoryOption
import echartsgl.options.GraphGLEmphasisOption
import echartsgl.options.GraphGLForceAtlas2Option
import echartsgl.options.GraphGLLineStyleOption
import echartsgl.options.GraphGLLinkOption
import echartsgl.options.GraphGLNodeOption
import echartsgl.options.GlItemStyleOption
import echartsgl.options.GlLabelOption
import echartsgl.options.graphGLSeries
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.math.sqrt
import zrender.ZRColor

/*
 * The three `graphGL` pages of the `gl/` directory of apache/echarts-examples.
 *
 * `graphGL` lays a node-link graph out with ForceAtlas2 on the GPU, so all three pages are networks
 * of thousands of nodes that would be far too slow on the canvas renderer: a 50x50 mesh, a 23k-node
 * internet graph and the 10k-package NPM dependency graph.
 *
 * Four differences against the originals:
 *
 * - `graphgl-gpu-layout` paints its own black background: upstream prints a white mesh and relies on
 *   `theme: dark` for the backdrop, while the gallery applies its own light theme.
 * - the initial node positions come from `Lcg` over a fixed box instead of `Math.random()` and
 *   `window.innerWidth` / `innerHeight`; the layout overwrites them anyway, and a fixed box keeps
 *   the pages reproducible.
 * - `graphgl-large-internet` pairs the two endpoints of each row of its edge table. Upstream reads
 *   the table as a flat `[source, target, source, target, ...]` list, but the shipped file is an
 *   array of `[source, target]` pairs, so its edges never reach the layout.
 * - `graphgl-npm-dep` is ported without `modularity`. That option comes from the separate
 *   `echarts-graph-modularity` demo plugin, which the site loads from a CDN and this project does not
 *   ship; without it the option would be ignored, and every node keeps the first colour of the
 *   palette, so the ForceAtlas2 configuration of the page is kept as it is. Its `stopThreshold` is
 *   left out too, because `echarts-gl` declares but never reads it.
 */

private val glGraph = Category(Groups.GL, "examples/GlGraph.kt")

/** Side of the mesh of `graphgl-gpu-layout`. */
private const val MESH_SIZE = 50

/** The palette both network pages give their node categories. */
private val NETWORK_COLORS =
    arrayOf(
        "rgb(203,239,15)",
        "rgb(73,15,239)",
        "rgb(239,231,15)",
        "rgb(15,217,239)",
        "rgb(30,15,239)",
        "rgb(15,174,239)",
        "rgb(116,239,15)",
        "rgb(239,15,58)",
        "rgb(15,239,174)",
        "rgb(239,102,15)",
        "rgb(239,15,15)",
        "rgb(15,44,239)",
        "rgb(239,145,15)",
        "rgb(30,239,15)",
        "rgb(239,188,15)",
        "rgb(159,239,15)",
        "rgb(159,15,239)",
        "rgb(15,239,44)",
        "rgb(15,239,87)",
        "rgb(15,239,217)",
        "rgb(203,15,239)",
        "rgb(239,15,188)",
        "rgb(239,15,102)",
        "rgb(239,58,15)",
        "rgb(239,15,145)",
        "rgb(116,15,239)",
        "rgb(15,131,239)",
        "rgb(73,239,15)",
        "rgb(15,239,131)",
        "rgb(15,87,239)",
        "rgb(239,15,231)",
    )

/** One node of a network page, scattered over a fixed box before the layout starts. */
private fun scatteredNode(random: Lcg, width: Double, height: Double): GraphGLNodeOption =
    unsafeJso {
        x = random.next() * width
        y = random.next() * height
        typedValue = OptionDataValueOrArray(1.0)
    }

/** A node of the mesh: the index is its position in the `MESH_SIZE` square. */
private fun meshNodes(random: Lcg): Array<GraphGLNodeOption?> =
    Array(MESH_SIZE * MESH_SIZE) { scatteredNode(random, 1280.0, 720.0) }

/** One edge of the mesh; the nodes are indexed row by row. */
private fun meshEdge(from: Int, to: Int): GraphGLLinkOption =
    unsafeJso {
        source = StringOrDouble(from.toDouble())
        target = StringOrDouble(to.toDouble())
        typedValue = OptionDataValueOrArray(1.0)
    }

/** The vertical and horizontal edges between the neighbours of the mesh. */
private fun meshEdges(): Array<GraphGLLinkOption?> {
    val edges = mutableListOf<GraphGLLinkOption>()
    for (column in 0 until MESH_SIZE) {
        for (row in 0 until MESH_SIZE) {
            val index = column + row * MESH_SIZE
            if (column < MESH_SIZE - 1) edges += meshEdge(index, index + 1)
            if (row < MESH_SIZE - 1) edges += meshEdge(index, index + MESH_SIZE)
        }
    }
    return edges.toTypedArray()
}

/** The option of `graphgl-gpu-layout`: a white mesh that the layout opens up into a lattice. */
private fun gpuLayoutOption(): EChartsOption =
    unsafeJso {
        backgroundColor = ZRColor("#000")
        series =
            arrayOf<SeriesOption>(
                graphGLSeries {
                    nodes = meshNodes(Lcg(20251029))
                    edges = meshEdges()
                    typedItemStyle = unsafeJso<GlItemStyleOption> { color = GlColor(ZRColor("rgba(255,255,255,0.8)")) }
                    typedLineStyle =
                        unsafeJso<GraphGLLineStyleOption> {
                            color = GlColor(ZRColor("rgba(255,255,255,0.8)"))
                            width = 3.0
                        }
                    forceAtlas2 =
                        unsafeJso<GraphGLForceAtlas2Option> {
                            steps = 5.0
                            jitterTolerence = 10.0
                            edgeWeightInfluence = 4.0
                        }
                },
            )
    }

/** Shape of `internet.graph.json`: `nodes` are `[x, y, symbolSize, category]` rows. */
private external interface InternetGraph {
    /** One row per node. */
    var nodes: Array<Array<Double>>

    /** One `[source, target]` row per edge. */
    var edges: Array<Array<Double>>
}

/** The node categories of [graph], in ascending order of their numeric name. */
private fun internetCategories(graph: InternetGraph): Array<GraphGLCategoryOption?> {
    val names = graph.nodes.map { node -> node[3] }.distinct().sorted()
    return names.map { name ->
        unsafeJso<GraphGLCategoryOption> { this.name = name.toInt().toString() }
    }.toTypedArray()
}

/** The nodes of [graph], scattered over a fixed box and grouped by their fourth column. */
private fun internetNodes(graph: InternetGraph): Array<GraphGLNodeOption?> {
    val random = Lcg(20251030)
    return Array(graph.nodes.size) { index ->
        val node = graph.nodes[index]
        unsafeJso<GraphGLNodeOption> {
            x = random.next() * 1280.0
            y = random.next() * 720.0
            symbolSize = SymbolSize(node[2])
            category = StringOrDouble(node[3])
            typedValue = OptionDataValueOrArray(1.0)
        }
    }
}

/** The edges of [graph], one per `[source, target]` row. */
private fun internetEdges(graph: InternetGraph): Array<GraphGLLinkOption?> =
    Array(graph.edges.size) { index ->
        val edge = graph.edges[index]
        unsafeJso<GraphGLLinkOption> {
            source = StringOrDouble(edge[0])
            target = StringOrDouble(edge[1])
            typedValue = OptionDataValueOrArray(2.0)
        }
    }

/** The option of `graphgl-large-internet`. */
private fun largeInternetOption(graph: InternetGraph): EChartsOption =
    unsafeJso {
        backgroundColor = ZRColor("#000")
        color = NETWORK_COLORS.map { ZRColor(it) }.toTypedArray()
        series =
            arrayOf<SeriesOption>(
                graphGLSeries {
                    nodes = internetNodes(graph)
                    edges = internetEdges(graph)
                    categories = internetCategories(graph)
                    typedLineStyle = unsafeJso<GraphGLLineStyleOption> { color = GlColor(ZRColor("rgba(255,255,255,0.2)")) }
                    typedItemStyle = unsafeJso<GlItemStyleOption> { opacity = 1.0 }
                    forceAtlas2 =
                        unsafeJso<GraphGLForceAtlas2Option> {
                            steps = 1.0
                            jitterTolerence = 10.0
                            edgeWeight = DoubleOrDoubleArray(arrayOf(0.2, 1.0))
                            gravity = 0.0
                            edgeWeightInfluence = 1.0
                            scaling = 0.2
                        }
                },
            )
    }

/** Shape of `npmdep.json`: names, flat `[source, target, ...]` edges and the dependent counts. */
private external interface NpmDependencyJson {
    /** Package names, the index of each is its node index. */
    var nodes: Array<String>

    /** Flat endpoint list: `[source, target, source, target, ...]`. */
    var edges: Array<Double>

    /** Number of dependents of the package at the same index. */
    var dependentsCount: Array<Double>
}

/** The npm palette, which has one colour fewer than [NETWORK_COLORS]. */
private val NPM_COLORS = NETWORK_COLORS.copyOfRange(0, 30)

/** The nodes of the dependency graph; only the packages with more than 5000 dependents label. */
private fun npmNodes(json: NpmDependencyJson): Array<GraphGLNodeOption?> =
    Array(json.nodes.size) { index ->
        val dependents = json.dependentsCount[index]
        unsafeJso<GraphGLNodeOption> {
            name = OptionId(json.nodes[index])
            typedValue = OptionDataValueOrArray(dependents)
            emphasis = unsafeJso<GraphGLEmphasisOption> { label = unsafeJso<GlLabelOption> { show = true } }
            if (dependents > 5000.0) {
                label = unsafeJso<GlLabelOption> { show = true }
            }
        }
    }

/** The edges of the dependency graph, paired from the flat endpoint list. */
private fun npmEdges(json: NpmDependencyJson): Array<GraphGLLinkOption?> =
    Array(json.edges.size / 2) { index ->
        unsafeJso<GraphGLLinkOption> {
            source = StringOrDouble(json.edges[index * 2])
            target = StringOrDouble(json.edges[index * 2 + 1])
        }
    }

/** The option of `graphgl-npm-dep`; the node count is the square root of the dependents. */
private fun npmDependencyOption(json: NpmDependencyJson): EChartsOption =
    unsafeJso {
        backgroundColor = ZRColor("#000")
        color = NPM_COLORS.map { ZRColor(it) }.toTypedArray()
        series =
            arrayOf<SeriesOption>(
                graphGLSeries {
                    nodes = npmNodes(json)
                    edges = npmEdges(json)
                    typedLineStyle = unsafeJso<GraphGLLineStyleOption> { color = GlColor(ZRColor("rgba(255,255,255,1)")); opacity = 0.05 }
                    typedItemStyle = unsafeJso<GlItemStyleOption> { opacity = 1.0 }
                    focusNodeAdjacency = false
                    focusNodeAdjacencyOn = GraphGLFocusNodeAdjacencyOns.click
                    symbolSize =
                        SymbolSize { value, _ ->
                            DoubleOrDoubleArray(sqrt(numeric(value) / 10))
                        }
                    typedLabel = unsafeJso<GlLabelOption> { color = "#fff" }
                    typedEmphasis =
                        unsafeJso<GraphGLEmphasisOption> {
                            label = unsafeJso<GlLabelOption> { show = false }
                            lineStyle = unsafeJso<GraphGLLineStyleOption> { opacity = 0.5; width = 4.0 }
                        }
                    forceAtlas2 =
                        unsafeJso<GraphGLForceAtlas2Option> {
                            steps = 5.0
                            jitterTolerence = 10.0
                            edgeWeight = DoubleOrDoubleArray(arrayOf(0.2, 1.0))
                            gravity = 5.0
                            edgeWeightInfluence = 0.0
                        }
                },
            )
    }

val glGraphEntries: List<CatalogEntry> =
    listOf(
        glGraph.chart(
            "graphgl-gpu-layout",
            "GraphGL - GPU 布局",
            "GraphGL - GPU Layout",
            "50×50 的网格节点与相邻边，ForceAtlas2 在 GPU 上把整张网格撑成规整的点阵。",
            "A 50x50 mesh of nodes and their neighbour edges, opened up into a regular lattice by " +
                "the ForceAtlas2 layout running on the GPU.",
        ) {
            gpuLayoutOption()
        },
        glGraph.interactive(
            "graphgl-large-internet",
            "GraphGL - 大规模互联网图谱",
            "GraphGL - Large Internet",
            "两万三千个节点、四万八千条边的互联网拓扑，按第四列的 31 个类别着色，一次布局只跑一步。",
            "An internet topology of twenty-three thousand nodes and forty-eight thousand edges, " +
                "coloured by its thirty-one categories with a single layout step per frame.",
        ) { chart ->
            loadJson("data/gl/internet.graph.json").then { json ->
                chart.setOption(largeInternetOption(json.unsafeCast<InternetGraph>()))
            }
        },
        glGraph.interactive(
            "graphgl-npm-dep",
            "NPM 依赖关系图",
            "NPM Dependencies with graphGL",
            "一万个 npm 包、两万七千条依赖边：节点大小按依赖者数量的平方根，依赖者超过 5000 的包直接显示名字。",
            "Ten thousand npm packages and twenty-seven thousand dependency edges: a node's size is " +
                "the square root of its dependent count, and packages with more than 5000 dependents " +
                "show their name.",
        ) { chart ->
            loadJson("data/gl/npmdep.json").then { json ->
                chart.setOption(npmDependencyOption(json.unsafeCast<NpmDependencyJson>()))
            }
        },
    )
