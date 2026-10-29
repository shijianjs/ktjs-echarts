package echarts.options

import echarts.Orient
import echarts.RoamTrigger
import echarts.NodeAlign
import echarts.GraphLayout
import echarts.GraphInitLayout
import echarts.FocusNodeAdjacency
import echarts.ChordCoordinateSystem
import echarts.SankeyCoordinateSystem
import kotlin.js.JsName
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * Series that draw relations between items: graph, chord and sankey.
 */

/**
 * Configuration of the force layout of a graph series.
 */
external interface GraphForceOption {
    /** Initial layout of the nodes. */
    var initLayout: GraphInitLayout?

    /** Repulsion between the nodes, one value per category or a single value. */
    var repulsion: DoubleOrDoubleArray?

    /** Gravity towards the center. */
    var gravity: Double?

    /** Friction of the movement. */
    var friction: Double?

    /** Ideal edge length, one value per category or a single value. */
    var edgeLength: DoubleOrDoubleArray?

    /** Whether the layout animates. */
    var layoutAnimation: Boolean?
}

/**
 * Configuration of the circular layout of a graph series.
 */
external interface GraphCircularOption {
    /** Whether the labels are rotated along the node positions. */
    var rotateLabel: Boolean?
}

/**
 * Line style of a graph edge; the upstream interface only adds the curvature, which
 * [LineStyleOption] already carries.
 */
typealias GraphEdgeLineStyleOption = LineStyleOption

/**
 * Style of the nodes of a graph series.
 */
external interface GraphNodeStateOption {
    /** Item style. */
    var itemStyle: ItemStyleOption?

    /** Label style. */
    var label: LabelOption?
}

/**
 * One node of a graph series.
 */
external interface GraphNodeItemOption : GraphNodeStateOption, SymbolOptionMixin {
    /** Node id. */
    var id: String?

    /** Node name, referenced by the `source` and `target` of the edges. */
    var name: String?

    /** Value of the node: a single value or one value per dimension. */
    var value: OptionDataValueOrArray?

    /** Fixed x position. */
    var x: Double?

    /** Fixed y position. */
    var y: Double?

    /** Whether the node keeps its position during a force layout. */
    var fixed: Boolean?

    /** Index or name of the category the node belongs to. */
    var category: StringOrDouble?

    /** Whether the node can be dragged. */
    var draggable: Boolean?

    /** Cursor shown when hovering the node. */
    var cursor: String?
}

/**
 * Style of the edges of a graph series.
 */
external interface GraphEdgeStateOption {
    /** Line style. */
    var lineStyle: GraphEdgeLineStyleOption?

    /** Label style. */
    var label: LabelOption?
}

/**
 * One edge of a graph series.
 *
 * Upstream declares `GraphEdgeItemObject<OptionDataValueNumeric>`, so [value] is the generic data
 * item value.
 */
external interface GraphEdgeItemOption : GraphEdgeStateOption, OptionDataItemObject {
    /** Name or index of the source node. */
    var source: StringOrDouble?

    /** Name or index of the target node. */
    var target: StringOrDouble?

    /** Symbol of both line ends. */
    var symbol: StringOrStringArray?

    /** Size of the symbols at both line ends. */
    var symbolSize: DoubleOrDoubleArray?

    /** Whether the edge is ignored by a force layout. */
    var ignoreForceLayout: Boolean?
}

/**
 * One category of the nodes of a graph series.
 */
external interface GraphCategoryItemOption : GraphNodeStateOption, SymbolOptionMixin {
    /** Name of the category, shown in the legend. */
    var name: String?
}

/**
 * Graph series: a node-link diagram.
 */
external interface GraphSeriesOption :
    SeriesOption,
    SymbolOptionMixin,
    BoxLayoutOptionMixin,
    SeriesOnCartesianOptionMixin,
    SeriesOnPolarOptionMixin,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin,
    SeriesOnGeoOptionMixin,
    SeriesOnSingleOptionMixin
{
    /** Layout of the nodes. */
    var layout: GraphLayout?

    /** Whether the layout animates. */
    var layoutAnimation: Boolean?

    /** Configuration of the force layout. */
    var force: GraphForceOption?

    /** Configuration of the circular layout. */
    var circular: GraphCircularOption?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Area that triggers roaming. */
    var roamTrigger: RoamTrigger?

    /** Center of the graph. */
    var center: Array<StringOrDouble>?

    /** Initial zoom level. */
    var zoom: Double?

    /** Zoom limits. */
    var scaleLimit: ScaleLimitOption?

    /** Whether the nodes can be dragged. */
    var draggable: Boolean?

    /** Symbol of the edges, one for both ends or one per end. */
    var edgeSymbol: StringOrStringArray?

    /** Symbol size of the edges. */
    var edgeSymbolSize: DoubleOrDoubleArray?

    /** Label of the edges. */
    var edgeLabel: LabelOption?

    /** Symbol size scale ratio on roaming. */
    var nodeScaleRatio: Double?

    /** Categories of the nodes, referenced by [GraphNodeItemOption.category]. */
    var categories: Array<GraphCategoryItemOption>?

    /** Nodes of the graph. */
    var nodes: Array<GraphNodeItemOption>?

    /** Edges of the graph, with the options of every edge. */
    var edges: Array<GraphEdgeItemOption>?

    /** Edges of the graph; alias of [edges]. */
    var links: Array<GraphEdgeItemOption>?

    /** Whether to highlight the adjacent nodes on hover. */
    var focusNodeAdjacency: Boolean?

    /** Whether the edges are curved automatically; `[0, 1]` per edge when an array. */
    var autoCurveness: BooleanOrDouble?

    /**
     * Typed view of [SeriesOption.data] with the item type of a graph series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<GraphDataItem?>?
}

/**
 * One entry of `GraphSeriesOption.typedData`.
 *
 * Upstream `GraphNodeItemOption | GraphDataValue`.
 */
external interface GraphDataItem

/** Wraps a single value. */
fun GraphDataItem(value: OptionDataValue): GraphDataItem = value.unsafeCast<GraphDataItem>()

/** Wraps a row of values. */
fun GraphDataItem(values: Array<OptionDataValue?>): GraphDataItem = values.unsafeCast<GraphDataItem>()

/** Wraps a node object. */
fun GraphDataItem(value: GraphNodeItemOption): GraphDataItem = value.unsafeCast<GraphDataItem>()

/**
 * Line style of a chord edge; the upstream interface only adds the curvature, which
 * [LineStyleOption] already carries.
 */
typealias ChordEdgeLineStyleOption = LineStyleOption

/**
 * Item style of a chord node; the upstream interface only narrows `borderRadius`, which
 * [ItemStyleOption] already accepts.
 */
typealias ChordItemStyleOption = ItemStyleOption

/**
 * Label of a chord node; the upstream interface only narrows `position` and adds `silent`, which
 * [LabelOption] already accepts.
 */
typealias ChordNodeLabelOption = LabelOption

/**
 * Style of the nodes of a chord diagram.
 */
external interface ChordNodeStateOption {
    /** Item style. */
    var itemStyle: ChordItemStyleOption?

    /** Label style. */
    var label: ChordNodeLabelOption?
}

/**
 * One node of a chord diagram.
 */
external interface ChordNodeItemOption : ChordNodeStateOption {
    /** Node id. */
    var id: String?

    /** Node name, referenced by the `source` and `target` of the edges. */
    var name: String?

    /** Value of the node: a single value or one value per dimension. */
    var value: OptionDataValueOrArray?
}

/**
 * Style of the edges of a chord diagram.
 */
external interface ChordEdgeStateOption {
    /** Line style. */
    var lineStyle: ChordEdgeLineStyleOption?

    /** Label style. */
    var label: LabelOption?
}

/**
 * One edge of a chord diagram.
 *
 * Upstream declares `GraphEdgeItemObject<OptionDataValueNumeric>`, so [value] is the generic data
 * item value.
 */
external interface ChordEdgeItemOption : ChordEdgeStateOption, OptionDataItemObject {
    /** Name or index of the source node. */
    var source: StringOrDouble?

    /** Name or index of the target node. */
    var target: StringOrDouble?
}

/**
 * Chord series introduced in ECharts 6.
 */
external interface ChordSeriesOption :
    SeriesOption,
    SymbolOptionMixin,
    BoxLayoutOptionMixin,
    SeriesOnCartesianOptionMixin,
    SeriesOnPolarOptionMixin,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin,
    SeriesOnGeoOptionMixin,
    SeriesOnSingleOptionMixin
{
    /** Coordinate system, always `"none"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [LineSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: ChordCoordinateSystem?

    /** Width of the nodes. */
    var nodeWidth: Double?

    /** Gap between nodes in pixels. */
    var nodeGap: Double?

    /** Alignment of the nodes. */
    var nodeAlign: NodeAlign?

    /** Number of layout iterations. */
    var layoutIterations: Double?

    /** Orientation of the diagram. */
    var orient: Orient?

    /** Whether the nodes can be dragged. */
    var draggable: Boolean?

    /** Center of the chord diagram. */
    var center: Array<StringOrDouble>?

    /** Radius of the chord diagram, or `[inner, outer]`. */
    var radius: StringOrDoubleOrArray?

    /** Start angle in degrees. */
    var startAngle: Double?

    /** End angle in degrees, or `"auto"`. */
    var endAngle: StringOrDouble?

    /** Minimum angle of a sector in degrees. */
    var minAngle: Double?

    /** Padding angle between sectors, in degrees. */
    var padAngle: Double?

    /** Whether the sectors are drawn clockwise. */
    var clockwise: Boolean?

    /** Label of the edges. */
    var edgeLabel: LabelOption?

    /** Nodes of the chord diagram. */
    var nodes: Array<ChordNodeItemOption>?

    /** Edges of the chord diagram, with the options of every edge. */
    var edges: Array<ChordEdgeItemOption>?

    /** Edges of the chord diagram; alias of [edges]. */
    var links: Array<ChordEdgeItemOption>?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Area that triggers roaming. */
    var roamTrigger: RoamTrigger?

    /**
     * Typed view of [SeriesOption.data] with the item type of a chord diagram.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<ChordDataItem?>?
}

/**
 * One entry of `ChordSeriesOption.typedData`.
 *
 * Upstream `ChordNodeItemOption | ChordDataValue`.
 */
external interface ChordDataItem

/** Wraps a single value. */
fun ChordDataItem(value: OptionDataValue): ChordDataItem = value.unsafeCast<ChordDataItem>()

/** Wraps a row of values. */
fun ChordDataItem(values: Array<OptionDataValue?>): ChordDataItem = values.unsafeCast<ChordDataItem>()

/** Wraps a node object. */
fun ChordDataItem(value: ChordNodeItemOption): ChordDataItem = value.unsafeCast<ChordDataItem>()

/**
 * Color of a sankey edge.
 *
 * Upstream declares `ZRColor | "gradient" | "source" | "target"`; Kotlin cannot widen the
 * inherited `LineStyleOption.color`, so the edge line style redeclares [color] with this type.
 */
external interface SankeyEdgeColor

/** A plain zrender color. */
fun SankeyEdgeColor(value: ZRColor): SankeyEdgeColor = value.unsafeCast<SankeyEdgeColor>()

/** One of the keywords `"gradient"`, `"source"`, `"target"`. */
fun SankeyEdgeColor(value: String): SankeyEdgeColor = value.unsafeCast<SankeyEdgeColor>()

/**
 * Line style of a sankey edge: the curvature comes from [LineStyleOption], the color is a
 * [SankeyEdgeColor] because an edge can follow the color of its source or target node.
 */
external interface SankeyEdgeStyleOption : LineStyleOption {
    /**
     * Color of this edge, or the end whose color it takes.
     *
     * Typed view of the inherited [LineStyleOption.color]; both write the same JavaScript property.
     */
    @JsName("color")
    var typedColor: SankeyEdgeColor?
}

/**
 * Style of the nodes of a sankey diagram.
 */
external interface SankeyNodeStateOption {
    /** Label style. */
    var label: LabelOption?

    /** Item style. */
    var itemStyle: ItemStyleOption?
}

/**
 * Style of the edges of a sankey diagram.
 */
external interface SankeyEdgeStateOption {
    /** Line style. */
    var lineStyle: SankeyEdgeStyleOption?
}

/**
 * One node of a sankey diagram.
 */
external interface SankeyNodeItemOption : SankeyNodeStateOption, OptionDataItemObject {
    /** Horizontal position inside the layer, `0..1`. */
    var localX: Double?

    /** Vertical position inside the layer, `0..1`. */
    var localY: Double?

    /** Layer the node is placed on. */
    var depth: Double?

    /** Whether the node can be dragged. */
    var draggable: Boolean?

    /** Whether to highlight the nodes connected to this one. */
    var focusNodeAdjacency: FocusNodeAdjacency?
}

/**
 * One edge of a sankey diagram.
 */
external interface SankeyEdgeItemOption : SankeyEdgeStateOption, OptionDataItemObject {
    /** Name or index of the source node. */
    var source: StringOrDouble?

    /** Name or index of the target node. */
    var target: StringOrDouble?

    /** Label of the edge. */
    var edgeLabel: LabelOption?

    /** Whether to highlight the edges connected to this one. */
    var focusNodeAdjacency: FocusNodeAdjacency?
}

/**
 * Per-level configuration of a sankey diagram.
 */
external interface SankeyLevelOption : SankeyNodeStateOption, SankeyEdgeStateOption {
    /** Layer this configuration applies to; required upstream. */
    var depth: Double
}

/**
 * Sankey diagram.
 */
external interface SankeySeriesOption :
    SeriesOption,
    BoxLayoutOptionMixin,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin
{
    /** Coordinate system, always `"view"`. */
    override var coordinateSystem: String?

    /** Typed view of [coordinateSystem]; see [LineSeriesOption.typedCoordinateSystem]. */
    @JsName("coordinateSystem")
    var typedCoordinateSystem: SankeyCoordinateSystem?

    /** Width of the nodes. */
    var nodeWidth: Double?

    /** Gap between nodes in pixels. */
    var nodeGap: Double?

    /** Alignment of the nodes. */
    var nodeAlign: NodeAlign?

    /** Number of layout iterations. */
    var layoutIterations: Double?

    /** Orientation of the diagram. */
    var orient: Orient?

    /** Whether the nodes can be dragged. */
    var draggable: Boolean?

    /** Color palette of the nodes; the edges derive their color from it. */
    var color: Array<ZRColor>?

    /** Label of the edges. */
    var edgeLabel: LabelOption?

    /** Nodes of the diagram. */
    var nodes: Array<SankeyNodeItemOption>?

    /** Edges of the diagram, with the options of every edge. */
    var edges: Array<SankeyEdgeItemOption>?

    /** Edges of the diagram; alias of [edges]. */
    var links: Array<SankeyEdgeItemOption>?

    /** Per-level configuration, keyed by [SankeyLevelOption.depth]. */
    var levels: Array<SankeyLevelOption>?

    /** Whether to highlight the adjacent nodes on hover. */
    var focusNodeAdjacency: FocusNodeAdjacency?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Area that triggers roaming. */
    var roamTrigger: RoamTrigger?

    /**
     * Typed view of [SeriesOption.data] with the item type of a sankey diagram.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<SankeyNodeItemOption?>?
}
