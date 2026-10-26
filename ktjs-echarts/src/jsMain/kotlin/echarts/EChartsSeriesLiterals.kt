package echarts

import kotlin.js.unsafeCast

/*
 * String-literal unions that belong to the data series: markers, hierarchies, relations and label
 * layout. The component-level literals live in `EChartsLiterals.kt`.
 *
 * Same convention as `EChartsLiterals.kt`: Kotlin cannot assign a JavaScript string to a member of
 * an external interface without a compiler plugin, so the values live in a plain object next to the
 * type, the way kotlin-wrappers declares `web.html.HtmlTagName`. The constants compile to the bare
 * string, so they are interchangeable with the literals they replace:
 *
 * ```kotlin
 * data = arrayOf(MarkLineDataItem(unsafeJso { yAxis = "average"; type = MarkerStatisticTypes.average }))
 * ```
 */

/**
 * Statistical method used by mark points, mark lines and mark areas to place the marker.
 */
sealed external interface MarkerStatisticType

/** Wraps a value that comes from JavaScript. Prefer the constants in [MarkerStatisticTypes]. */
inline fun MarkerStatisticType(value: String): MarkerStatisticType = value.unsafeCast<MarkerStatisticType>()

/**
 * The documented values of [MarkerStatisticType].
 */
object MarkerStatisticTypes {
    /** Mean of the data. */
    inline val average: MarkerStatisticType get() = MarkerStatisticType("average")

    /** Minimum of the data. */
    inline val min: MarkerStatisticType get() = MarkerStatisticType("min")

    /** Maximum of the data. */
    inline val max: MarkerStatisticType get() = MarkerStatisticType("max")

    /** Median of the data. */
    inline val median: MarkerStatisticType get() = MarkerStatisticType("median")

}

/**
 * What the `x` and `y` of a marker position are relative to.
 */
sealed external interface MarkerRelativeTo

/** Wraps a value that comes from JavaScript. Prefer the constants in [MarkerRelativeTos]. */
inline fun MarkerRelativeTo(value: String): MarkerRelativeTo = value.unsafeCast<MarkerRelativeTo>()

/**
 * The documented values of [MarkerRelativeTo].
 */
object MarkerRelativeTos {
    /** Position relative to the container of the chart. */
    inline val container: MarkerRelativeTo get() = MarkerRelativeTo("container")

    /** Position relative to the coordinate system. */
    inline val coordinate: MarkerRelativeTo get() = MarkerRelativeTo("coordinate")

}

/**
 * Sort order of a treemap or sunburst hierarchy.
 */
sealed external interface SortOrder

/** Wraps a value that comes from JavaScript. Prefer the constants in [SortOrders]. */
inline fun SortOrder(value: String): SortOrder = value.unsafeCast<SortOrder>()

/**
 * The documented values of [SortOrder].
 */
object SortOrders {
    /** Ascending: smaller values are laid out first. */
    inline val asc: SortOrder get() = SortOrder("asc")

    /** Descending: larger values are laid out first. */
    inline val desc: SortOrder get() = SortOrder("desc")

}

/**
 * What the color of a treemap node is mapped by.
 */
sealed external interface ColorMappingBy

/** Wraps a value that comes from JavaScript. Prefer the constants in [ColorMappingBys]. */
inline fun ColorMappingBy(value: String): ColorMappingBy = value.unsafeCast<ColorMappingBy>()

/**
 * The documented values of [ColorMappingBy].
 */
object ColorMappingBys {
    /** Map the color to the value of the node. */
    inline val `value`: ColorMappingBy get() = ColorMappingBy("value")

    /** Map the color to the index of the node. */
    inline val index: ColorMappingBy get() = ColorMappingBy("index")

    /** Map the color to the id of the node. */
    inline val id: ColorMappingBy get() = ColorMappingBy("id")

}

/**
 * Region of a treemap that the zoomed window is clipped to.
 */
sealed external interface ClipWindow

/** Wraps a value that comes from JavaScript. Prefer the constants in [ClipWindows]. */
inline fun ClipWindow(value: String): ClipWindow = value.unsafeCast<ClipWindow>()

/**
 * The documented values of [ClipWindow].
 */
object ClipWindows {
    /** Clip to the current root node. */
    inline val origin: ClipWindow get() = ClipWindow("origin")

    /** Display every node of the treemap. */
    inline val fullscreen: ClipWindow get() = ClipWindow("fullscreen")

}

/**
 * Behaviour of a treemap or sunburst node when it is clicked.
 *
 * The upstream type is `false | "zoomToNode" | "link"` for treemap and
 * `false | "rootToNode" | "link"` for sunburst; [NodeClickBehavior] covers both sets, and each
 * option only accepts the values documented for that series.
 */
sealed external interface NodeClickBehavior

/** Wraps a value that comes from JavaScript. Prefer the constants in [NodeClickBehaviors]. */
inline fun NodeClickBehavior(value: String): NodeClickBehavior = value.unsafeCast<NodeClickBehavior>()

/** Wraps `false`: clicking a node does nothing. */
fun NodeClickBehavior(value: Boolean): NodeClickBehavior = value.unsafeCast<NodeClickBehavior>()

/**
 * The documented values of [NodeClickBehavior].
 */
object NodeClickBehaviors {
    /** Clicking does nothing. */
    inline val none: NodeClickBehavior get() = NodeClickBehavior(false)

    /** Zoom the clicked node to the root of the treemap. */
    inline val zoomToNode: NodeClickBehavior get() = NodeClickBehavior("zoomToNode")

    /** Reset the sunburst to the clicked node. */
    inline val rootToNode: NodeClickBehavior get() = NodeClickBehavior("rootToNode")

    /** Open the link configured on the data item of the node. */
    inline val link: NodeClickBehavior get() = NodeClickBehavior("link")

}

/**
 * Kind of animation used when the root of a sunburst changes.
 */
sealed external interface AnimationType

/** Wraps a value that comes from JavaScript. Prefer the constants in [AnimationTypes]. */
inline fun AnimationType(value: String): AnimationType = value.unsafeCast<AnimationType>()

/**
 * The documented values of [AnimationType].
 */
object AnimationTypes {
    /** The sectors expand or shrink in place. */
    inline val expansion: AnimationType get() = AnimationType("expansion")

    /** The whole chart scales between the old and the new root. */
    inline val scale: AnimationType get() = AnimationType("scale")

}

/**
 * Alignment of the nodes of a sankey or chord diagram.
 */
sealed external interface NodeAlign

/** Wraps a value that comes from JavaScript. Prefer the constants in [NodeAligns]. */
inline fun NodeAlign(value: String): NodeAlign = value.unsafeCast<NodeAlign>()

/**
 * The documented values of [NodeAlign].
 */
object NodeAligns {
    /** Align both ends of the diagram. */
    inline val justify: NodeAlign get() = NodeAlign("justify")

    /** Align the nodes to the left (or the top) edge. */
    inline val left: NodeAlign get() = NodeAlign("left")

    /** Align the nodes to the right (or the bottom) edge. */
    inline val right: NodeAlign get() = NodeAlign("right")

}

/**
 * How the overlapped labels of a series are moved apart.
 */
sealed external interface MoveOverlap

/** Wraps a value that comes from JavaScript. Prefer the constants in [MoveOverlaps]. */
inline fun MoveOverlap(value: String): MoveOverlap = value.unsafeCast<MoveOverlap>()

/**
 * The documented values of [MoveOverlap].
 */
object MoveOverlaps {
    /** Shift the labels along the x axis, keeping their order. */
    inline val shiftX: MoveOverlap get() = MoveOverlap("shiftX")

    /** Shift the labels along the y axis, keeping their order. */
    inline val shiftY: MoveOverlap get() = MoveOverlap("shiftY")

    /** Shuffle the labels along the x axis. */
    inline val shuffleX: MoveOverlap get() = MoveOverlap("shuffleX")

    /** Shuffle the labels along the y axis. */
    inline val shuffleY: MoveOverlap get() = MoveOverlap("shuffleY")

}

/**
 * How `focusNodeAdjacency` highlights the elements around the hovered one.
 *
 * The upstream type is `boolean | "inEdges" | "outEdges" | "allEdges"`; prefer the constants in
 * [FocusNodeAdjacencies] and wrap `true` / `false` with the boolean factory.
 */
sealed external interface FocusNodeAdjacency

/** Wraps a boolean. */
fun FocusNodeAdjacency(value: Boolean): FocusNodeAdjacency = value.unsafeCast<FocusNodeAdjacency>()

/** Wraps a value that comes from JavaScript. Prefer the constants in [FocusNodeAdjacencies]. */
inline fun FocusNodeAdjacency(value: String): FocusNodeAdjacency =
    value.unsafeCast<FocusNodeAdjacency>()

/**
 * The documented values of [FocusNodeAdjacency].
 */
object FocusNodeAdjacencies {
    /** Highlight the edges that point at the hovered node. */
    inline val inEdges: FocusNodeAdjacency get() = FocusNodeAdjacency("inEdges")

    /** Highlight the edges that leave the hovered node. */
    inline val outEdges: FocusNodeAdjacency get() = FocusNodeAdjacency("outEdges")

    /** Highlight both the incoming and the outgoing edges of the hovered node. */
    inline val allEdges: FocusNodeAdjacency get() = FocusNodeAdjacency("allEdges")

}
