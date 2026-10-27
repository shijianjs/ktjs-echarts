package echarts.options

import echarts.TreeLayout
import echarts.TreeEdgeShape
import echarts.TreeOrient
import echarts.SortOrder
import echarts.NodeClickBehavior
import echarts.EmphasisFocus
import echarts.ColorMappingBy
import echarts.ClipWindow
import echarts.BlurScope
import echarts.AnimationType
import echarts.AnimationEasing
import kotlin.js.JsName
import kotlin.js.unsafeCast

/*
 * Series that lay out a hierarchy of data items: tree, treemap and sunburst.
 */

/**
 * Style of one node of a tree series.
 */
external interface TreeSeriesStateOption {
    /** Item style. */
    var itemStyle: ItemStyleOption?

    /** Line style of the edge between the node and its parent. */
    var lineStyle: LineStyleOption?

    /** Label style. */
    var label: LabelOption?
}

/**
 * One node of a tree series.
 *
 * Upstream `TreeSeriesNodeItemOption` extends `OptionDataItemObject`, so a node can be put into an
 * `Array<OptionDataItem?>` with [items] and assigned to `SeriesOption.data`.
 */
external interface TreeSeriesNodeItemOption : TreeSeriesStateOption, OptionDataItemObject {
    /** Child nodes. */
    var children: Array<TreeSeriesNodeItemOption>?

    /** Whether the children of the node are collapsed initially. */
    var collapsed: Boolean?

    /** Link opened when the node is clicked. */
    var link: String?

    /** Target of [link], e.g. `"_blank"`. */
    var target: String?
}

/**
 * Configuration of the leaves of a tree series.
 */
external interface TreeSeriesLeavesOption : TreeSeriesStateOption

/**
 * Tree series: a hierarchy laid out as a node-link diagram.
 */
external interface TreeSeriesOption :
    SeriesOption,
    SymbolOptionMixin,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin
{
    /** Layout of the tree. */
    var layout: TreeLayout?

    /** Orientation of an orthogonal tree. */
    var orient: TreeOrient?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the tree. */
    var width: StringOrDouble?

    /** Height of the tree. */
    var height: StringOrDouble?

    /** Depth of the tree that is expanded initially. */
    var initialTreeDepth: Double?

    /** Style of the leaves; overrides the style they inherit from their parent. */
    var leaves: TreeSeriesLeavesOption?

    /** Whether to expand or collapse a node on click. */
    var expandAndCollapse: Boolean?

    /** Duration of the layout animation, or a callback per element. */
    override var animationDuration: AnimationDuration?

    /** Easing of the layout animation. */
    override var animationEasing: AnimationEasing?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Center of the tree. */
    var center: Array<StringOrDouble>?

    /** Initial zoom level. */
    var zoom: Double?

    /** Zoom limits. */
    var scaleLimit: ScaleLimitOption?

    /** Shape of the edges. */
    var edgeShape: TreeEdgeShape?

    /** Position of the fork in a `"polyline"` edge. */
    var edgeForkPosition: StringOrDouble?

    /**
     * Typed view of [SeriesOption.data] with the item type of a tree series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<TreeSeriesNodeItemOption?>?
}

/**
 * Label of a treemap node; the upstream interface only narrows `formatter`, which [LabelOption]
 * already accepts.
 */
typealias TreemapSeriesLabelOption = LabelOption

/**
 * Item style of a treemap node: adds the treemap-specific visual settings.
 */
external interface TreemapSeriesItemStyleOption : ItemStyleOption {
    /** Alpha of the color, or one value per level; `-1` keeps the color. */
    var colorAlpha: Double?

    /** Saturation of the color, or one value per level; `-1` keeps the color. */
    var colorSaturation: Double?

    /** Saturation of the border color. */
    var borderColorSaturation: Double?

    /** Gap between the border and the fill, as a ratio. */
    var gapWidth: Double?
}

/**
 * Visual mapping of a treemap, shared by the series, the levels and the nodes.
 */
external interface TreemapSeriesVisualOption {
    /** Dimension used for the color mapping: an index or a name. */
    var visualDimension: StringOrDouble?

    /** What the color is mapped by. */
    var colorMappingBy: ColorMappingBy?

    /** Minimum value of the color mapping. */
    var visualMin: Double?

    /** Maximum value of the color mapping. */
    var visualMax: Double?

    /** Alpha per level, or `"none"` to keep the alpha of the item. */
    var colorAlpha: DoubleArrayOrNone?

    /** Saturation per level, or `"none"` to keep the saturation of the item. */
    var colorSaturation: DoubleArrayOrNone?

    /** Minimum area for a node to be visible. */
    var visibleMin: Double?

    /** Minimum area for the children of a node to be visible. */
    var childrenVisibleMin: Double?
}

/**
 * Style of a treemap node or level.
 */
external interface TreemapStateOption {
    /** Item style. */
    var itemStyle: TreemapSeriesItemStyleOption?

    /** Label style. */
    var label: TreemapSeriesLabelOption?

    /** Label style of the parent nodes. */
    var upperLabel: TreemapSeriesLabelOption?
}

/**
 * Per-level configuration of a treemap.
 */
external interface TreemapSeriesLevelOption : TreemapSeriesVisualOption, TreemapStateOption {
    /** Color palette of the level, or `"none"` to keep the color of the item. */
    var color: ColorArrayOrNone?

    /** Decal of the level, or `"none"` to keep the decal of the item. */
    var decal: DecalArrayOrNone?
}

/**
 * One node of a treemap.
 *
 * Upstream does not declare `OptionDataItemObject` as a base, but the members match; the Kotlin
 * interface extends it so that a node can be put into an `Array<OptionDataItem?>` with [items] and
 * assigned to `SeriesOption.data`.
 *
 * [value] is inherited from [OptionDataItemObject], i.e. it is the generic data value rather than
 * the upstream `number | number[]`.
 */
external interface TreemapSeriesNodeItemOption : TreemapSeriesVisualOption, TreemapStateOption, OptionDataItemObject {
    /** Child nodes. */
    var children: Array<TreemapSeriesNodeItemOption>?

    /** Color palette of the node, or `"none"` to keep the color of the parent. */
    var color: ColorArrayOrNone?

    /** Decal of the node, or `"none"` to keep the decal of the parent. */
    var decal: DecalArrayOrNone?

    /** Cursor shown when hovering the node. */
    var cursor: String?
}

/**
 * Item style of a breadcrumb, which adds a text style.
 */
external interface BreadcrumbItemStyleOption : ItemStyleOption {
    /** Style of the breadcrumb text. */
    var textStyle: LabelOption?
}

/**
 * State style of the breadcrumb.
 */
external interface BreadcrumbEmphasisOption {
    /** Whether the emphasis state is disabled. */
    var disabled: Boolean?

    /** What the emphasis state focuses on. */
    var focus: EmphasisFocus?

    /** Scope that is blurred while [focus] is active. */
    var blurScope: BlurScope?

    /** Item style in the emphasis state. */
    var itemStyle: BreadcrumbItemStyleOption?
}

/**
 * Breadcrumb navigation of a treemap.
 */
external interface TreemapBreadcrumbOption : BoxLayoutOption {
    /** Whether the breadcrumb is shown. */
    var show: Boolean?

    /** Width of an empty breadcrumb item. */
    var emptyItemWidth: Double?

    /** Item style of the breadcrumb. */
    var itemStyle: BreadcrumbItemStyleOption?

    /** Style of the breadcrumb in the emphasis state. */
    var emphasis: BreadcrumbEmphasisOption?
}

/**
 * Treemap series: a hierarchy laid out as nested rectangles.
 */
external interface TreemapSeriesOption :
    SeriesOption,
    TreemapSeriesVisualOption,
    ComponentOnCalendarOptionMixin,
    ComponentOnMatrixOptionMixin
{
    /** Whether the nodes are sorted by value: `true`/`false` or an explicit order. */
    var sort: BooleanOrSortOrder?

    /** Region the zoomed window is clipped to. */
    var clipWindow: ClipWindow?

    /** Ratio between the width and the height of the tiles. */
    var squareRatio: Double?

    /** Depth of the tree that can be drilled down. */
    var leafDepth: Double?

    /** Icon of the drill-down button. */
    var drillDownIcon: String?

    /** Behaviour when a leaf node is clicked. */
    var nodeClick: NodeClickBehavior?

    /** Ratio of the zoom after a drill-down. */
    var zoomToNodeRatio: Double?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Gap between nodes in pixels. */
    var nodeGap: Double?

    /** Width of the nodes. */
    var nodeWidth: Double?

    /** Distance to the left side. */
    var left: StringOrDouble?

    /** Distance to the top side. */
    var top: StringOrDouble?

    /** Distance to the right side. */
    var right: StringOrDouble?

    /** Distance to the bottom side. */
    var bottom: StringOrDouble?

    /** Width of the treemap. */
    var width: StringOrDouble?

    /** Height of the treemap. */
    var height: StringOrDouble?

    /** Breadcrumb navigation. */
    var breadcrumb: TreemapBreadcrumbOption?

    /** Style of the labels of parent nodes. */
    var upperLabel: TreemapSeriesLabelOption?

    /** Per-level configuration; the last entry applies to the deeper levels. */
    var levels: Array<TreemapSeriesLevelOption>?

    /**
     * Typed view of [SeriesOption.data] with the item type of a treemap series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<TreemapSeriesNodeItemOption?>?
}

/**
 * Item style of a sunburst node; the upstream interface only narrows `borderRadius`, which
 * [ItemStyleOption] already accepts.
 */
typealias SunburstItemStyleOption = ItemStyleOption

/**
 * Label of a sunburst node: adds the minimum angle below which the label is hidden.
 */
external interface SunburstLabelOption : LabelOption {
    /** Minimum angle of a sector for its label to be shown, in degrees. */
    var minAngle: Double?

    /**
     * Typed view of [LabelOption.rotate]; upstream allows the `"radial"` / `"tangential"` keywords
     * as well as a number of degrees.
     */
    @JsName("rotate")
    var typedRotate: LabelRotate?
}

/**
 * Style of one node or level of a sunburst.
 */
external interface SunburstStateOption {
    /** Item style. */
    var itemStyle: SunburstItemStyleOption?

    /** Label style. */
    var label: SunburstLabelOption?
}

/**
 * One node of a sunburst.
 */
external interface SunburstSeriesNodeItemOption : SunburstStateOption, OptionDataItemObject {
    /** Behaviour when the node is clicked. */
    var nodeClick: NodeClickBehavior?

    /** Link opened when the node is clicked. */
    var link: String?

    /** Target of [link], e.g. `"_blank"`. */
    var target: String?

    /** Child nodes. */
    var children: Array<SunburstSeriesNodeItemOption>?

    /** Whether the children of the node are collapsed initially. */
    var collapsed: Boolean?

    /** Cursor shown when hovering the node. */
    var cursor: String?
}

/**
 * Style of the nodes highlighted by the hovered sector.
 */
external interface SunburstHighlightOption {
    /** Item style of the highlighted sectors. */
    var itemStyle: SunburstItemStyleOption?

    /** Label of the highlighted sectors. */
    var label: SunburstLabelOption?
}

/**
 * Per-level configuration of a sunburst.
 */
external interface SunburstSeriesLevelOption : SunburstStateOption {
    /** Radius of the level, `[inner, outer]`. */
    var radius: Array<StringOrDouble>?

    /** Outer radius of the level; deprecated in favour of [radius]. */
    var r: StringOrDouble?

    /** Inner radius of the level; deprecated in favour of [radius]. */
    var r0: StringOrDouble?

    /** Style of the sectors highlighted while the level is hovered. */
    var highlight: SunburstHighlightOption?
}

/**
 * One sector passed to a [SunburstSort] callback.
 */
external interface SortParam {
    /** Index of the data item. */
    val dataIndex: Double

    /** Depth of the node. */
    val depth: Double

    /** Height of the subtree below the node. */
    val height: Double

    /** Returns the value used to sort the node. */
    fun getValue(): Double
}

/**
 * `sort` of a sunburst: a fixed order or a comparison function over [SortParam].
 */
external interface SunburstSort

/** Wraps a sort order. */
fun SunburstSort(value: SortOrder): SunburstSort = value.unsafeCast<SunburstSort>()

/** Wraps a comparison function. */
fun SunburstSort(compare: (a: SortParam, b: SortParam) -> Double): SunburstSort =
    compare.unsafeCast<SunburstSort>()

/**
 * Sunburst series: a hierarchy laid out as concentric rings.
 */
external interface SunburstSeriesOption : SeriesOption, ComponentOnCalendarOptionMixin, ComponentOnMatrixOptionMixin {
    /** Center of the sunburst. */
    var center: Array<StringOrDouble>?

    /** Radius of the sunburst, or `[inner, outer]`. */
    var radius: StringOrDoubleOrArray?

    /** Sort order of the sectors; `null` keeps the order of the data. */
    var sort: SunburstSort?

    /** Start angle in degrees. */
    var startAngle: Double?

    /** End angle in degrees. */
    var endAngle: Double?

    /** Minimum angle of a sector in degrees. */
    var minAngle: Double?

    /** Minimum angle for the label to be shown, in degrees. */
    var minShowLabelAngle: Double?

    /** Whether the sectors are drawn clockwise. */
    var clockwise: Boolean?

    /** Whether sectors of zero value are still shown. */
    var stillShowZeroSum: Boolean?

    /** Behaviour when a node is clicked. */
    var nodeClick: NodeClickBehavior?

    /** Whether to render the label of sectors whose value is zero. */
    var renderLabelForZeroData: Boolean?

    /** Whether panning and zooming are enabled. */
    var roam: BooleanOrString?

    /** Gap between nodes in pixels. */
    var nodeGap: Double?

    /** Width of the nodes. */
    var nodeWidth: Double?

    /** Per-level configuration; the last entry applies to the deeper levels. */
    var levels: Array<SunburstSeriesLevelOption>?

    /** Precision of the percentage values. */
    var percentPrecision: Double?

    /** Kind of animation used when the root of the sunburst changes. */
    var animationType: AnimationType?

    /** Typed view of [SeriesOption.label] with the sunburst-specific members. */
    @JsName("label")
    var typedLabel: SunburstLabelOption?

    /**
     * Typed view of [SeriesOption.data] with the item type of a sunburst series.
     *
     * Both properties read and write the same JavaScript property; see `TYPE-MAPPING.md` §12.
     */
    @JsName("data")
    var typedData: Array<SunburstSeriesNodeItemOption?>?
}
