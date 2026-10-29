package echartsgl.options

import echarts.options.BoxLayoutOptionMixin
import echarts.options.DoubleOrDoubleArray
import echarts.options.OptionDataItemObject
import echarts.options.OptionDataValueOrArray
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.SymbolOptionMixin
import echartsgl.GlColor
import echartsgl.GraphGLFocusNodeAdjacencyOn
import kotlin.js.JsName

/*
 * WebGL force-directed graph.
 *
 * The layout is a force simulation started once the series is rendered; it writes the positions of
 * the nodes back into the option, which is why `getOption` returns the settled `data` of a
 * `graphGL` series with an `x` and a `y` on every node.
 */

/**
 * Configuration of the ForceAtlas2 layout of a [GraphGLSeriesOption].
 *
 * The layout runs on the GPU by default and falls back to a web worker on the CPU; the members below
 * configure the simulation itself in both cases.
 *
 * Upstream declares `initLayout` and the documented `stopThreshold` in its default option, but the
 * layout never reads either of them, so they are not modelled here.
 */
external interface GraphGLForceAtlas2Option {
    /** Whether the layout runs on the GPU; the CPU implementation is a fallback. */
    var GPU: Boolean?

    /** Iterations of one update; a larger value trades drawing time for layout speed. */
    var steps: Double?

    /**
     * Number of iterations after which the layout stops.
     *
     * Upstream reads it from the model but does not document it.
     */
    var maxSteps: Double?

    /** Whether the repulsion of a node takes its degree into account. */
    var repulsionByDegree: Boolean?

    /** Whether to use the lin-log mode, which makes clusters more compact. */
    var linLogMode: Boolean?

    /**
     * Whether the gravity behaves like a strong gravity.
     *
     * Upstream declares it in its default option but does not document it.
     */
    var strongGravityMode: Boolean?

    /** Gravity towards [gravityCenter]. */
    var gravity: Double?

    /** Center the gravity pulls towards; the middle of the initial positions by default. */
    var gravityCenter: Array<Double>?

    /** Scale of the layout; a larger value repels the nodes more. */
    var scaling: Double?

    /** Influence of the edge weight on the layout; the factor is exponential. */
    var edgeWeightInfluence: Double?

    /** Range the weights of the edges are mapped into, or one value for all of them. */
    var edgeWeight: DoubleOrDoubleArray?

    /** Range the weights of the nodes are mapped into, or one value for all of them. */
    var nodeWeight: DoubleOrDoubleArray?

    /** Whether overlapping nodes are pushed apart. */
    var preventOverlap: Boolean?

    /**
     * Whether to use the Barnes-Hut approximation.
     *
     * Upstream computes the default from the number of nodes (on above 1000) and does not document
     * that the option can be set; it still takes a user value.
     */
    var barnesHutOptimize: Boolean?

    /**
     * Accuracy of the Barnes-Hut approximation.
     *
     * Upstream declares it in its layout defaults but does not document it.
     */
    var barnesHutTheta: Double?

    /**
     * Jitter tolerance of the layout.
     *
     * Upstream declares it in its layout defaults but does not document it. The misspelling of
     * "tolerance" is the name of the upstream option.
     */
    var jitterTolerence: Double?

    /**
     * Whether the layout discourages hubs.
     *
     * Upstream declares it in its layout defaults but does not document it.
     */
    var dissuadeHubs: Boolean?
}

/**
 * Style of an edge of a [GraphGLSeriesOption].
 */
external interface GraphGLLineStyleOption {
    /** Color of the edge. See [GlColor][echartsgl.GlColor] for the HDR array form. */
    var color: GlColor?

    /** Width of the edge in pixels. */
    var width: Double?

    /** Opacity, `0..1`. */
    var opacity: Double?
}

/**
 * Emphasis (hover) state of a node, an edge or a whole [GraphGLSeriesOption].
 */
external interface GraphGLEmphasisOption {
    /** Label style in the emphasis state. */
    var label: GlLabelOption?

    /** Style of a node in the emphasis state. */
    var itemStyle: GlItemStyleOption?

    /** Style of an edge in the emphasis state. */
    var lineStyle: GraphGLLineStyleOption?
}

/**
 * One node of a [GraphGLSeriesOption].
 */
external interface GraphGLNodeOption : SymbolOptionMixin, OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the value is the weight of the node in the layout.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Initial x position; the layout starts from it and overwrites it. */
    var x: Double?

    /** Initial y position; the layout starts from it and overwrites it. */
    var y: Double?

    /** Index or name of the category the node belongs to. */
    var category: StringOrDouble?

    /** Style of the node. */
    var itemStyle: GlItemStyleOption?

    /** Label of the node. */
    var label: GlLabelOption?

    /** Emphasis state of the node. */
    var emphasis: GraphGLEmphasisOption?
}

/**
 * One edge of a [GraphGLSeriesOption].
 */
external interface GraphGLLinkOption : OptionDataItemObject {
    /**
     * Typed view of [OptionDataItemObject.value].
     *
     * Upstream `OptionDataValue[]`; the value is the weight of the edge in the layout.
     */
    @JsName("value")
    var typedValue: OptionDataValueOrArray?

    /** Name or index of the source node. */
    var source: StringOrDouble?

    /** Name or index of the target node. */
    var target: StringOrDouble?

    /** Style of the edge. */
    var lineStyle: GraphGLLineStyleOption?

    /** Emphasis state of the edge. */
    var emphasis: GraphGLEmphasisOption?
}

/**
 * One category of the nodes of a [GraphGLSeriesOption].
 *
 * A category is the parent of the models of the nodes that belong to it, so its style, symbol and
 * label are inherited by those nodes and it is the entry the legend uses.
 */
external interface GraphGLCategoryOption : SymbolOptionMixin {
    /** Name of the category, shown in the legend. */
    var name: String?

    /** Style of the nodes of the category. */
    var itemStyle: GlItemStyleOption?

    /** Label of the nodes of the category. */
    var label: GlLabelOption?

    /** Emphasis state of the nodes of the category. */
    var emphasis: GraphGLEmphasisOption?
}

/**
 * WebGL force-directed graph series, `series.type = "graphGL"`.
 *
 * A node-link diagram laid out with the ForceAtlas2 algorithm and drawn on the GPU, which makes
 * networks of tens of thousands of nodes practical. Nodes come from either [typedData] or [nodes],
 * edges from either [links] or [edges]; [categories] group the nodes for the legend and for
 * inherited styles.
 *
 * Upstream declares `roam` and `center` in its default option, but the view only reads [zoom] and
 * [offset], so neither is modelled here.
 */
external interface GraphGLSeriesOption :
    SeriesOption,
    BoxLayoutOptionMixin,
    SymbolOptionMixin
{
    /** Layout of the graph; `"forceAtlas2"` is the only supported value. */
    var layout: String?

    /** Configuration of the ForceAtlas2 layout. */
    var forceAtlas2: GraphGLForceAtlas2Option?

    /** Whether the neighbours of the hovered or clicked node are highlighted. */
    var focusNodeAdjacency: Boolean?

    /** Pointer event that focuses the neighbours of a node. */
    var focusNodeAdjacencyOn: GraphGLFocusNodeAdjacencyOn?

    /** Zoom level of the view. */
    var zoom: Double?

    /**
     * Pan offset of the view in pixels, `[x, y]`.
     *
     * Upstream reads the option from the model and writes it back on roaming, but does not document
     * it.
     */
    var offset: Array<Double>?

    /** Style of the nodes. */
    @JsName("itemStyle")
    var typedItemStyle: GlItemStyleOption?

    /** Style of the edges. */
    @JsName("lineStyle")
    var typedLineStyle: GraphGLLineStyleOption?

    /** Label of the nodes. */
    @JsName("label")
    var typedLabel: GlLabelOption?

    /** Emphasis state of the nodes and edges. */
    @JsName("emphasis")
    var typedEmphasis: GraphGLEmphasisOption?

    /** Categories of the nodes, referenced by [GraphGLNodeOption.category]. */
    var categories: Array<GraphGLCategoryOption?>?

    /** Nodes of the graph; an alias of [SeriesOption.data]. */
    var nodes: Array<GraphGLNodeOption?>?

    /** Edges of the graph. */
    var links: Array<GraphGLLinkOption?>?

    /** Edges of the graph; an alias of [links]. */
    var edges: Array<GraphGLLinkOption?>?

    /** Typed view of [SeriesOption.data] with the item type of a graph node. */
    @JsName("data")
    var typedData: Array<GraphGLNodeOption?>?
}
