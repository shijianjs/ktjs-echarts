package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

/**
 * `graphic` component: free-floating shapes and text.
 *
 * Element geometry lives in [GraphicElement.shape] and [GraphicElement.style] as JSON objects,
 * because the accepted keys depend on the element type (a `rect` has `width`/`height`, a `text`
 * has `text`, a `path` has `pathData`).
 */
@Serializable
public class GraphicOption {
    /** Component id used by cross references. */
    public var id: String? = null

    /** Top-level elements. */
    public var elements: List<GraphicElement>? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Canvas layer; components on a higher level are drawn in a separate canvas. */
    public var zlevel: Double? = null

    /** Distance to the left edge. */
    public var left: NumberOrString? = null

    /** Distance to the top edge. */
    public var top: NumberOrString? = null

    /** Distance to the right edge. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge. */
    public var bottom: NumberOrString? = null
}

/** One graphic element: a shape, an image, a text or a group. */
@Serializable
public class GraphicElement {
    /** Element type: `"group"`, `"image"`, `"text"`, `"rect"`, `"circle"`, `"ring"`, `"polygon"`, `"polyline"`, `"line"`, `"bezierCurve"`, `"arc"`, `"sector"` or `"path"`. */
    public var type: String? = null

    /** Element id used by cross references. */
    public var id: String? = null

    /** Element name. */
    public var name: String? = null

    /** Distance to the left edge. */
    public var left: NumberOrString? = null

    /** Distance to the top edge. */
    public var top: NumberOrString? = null

    /** Distance to the right edge. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge. */
    public var bottom: NumberOrString? = null

    /** z-order within the group. */
    public var z: Double? = null

    /** Whether the element ignores mouse events. */
    public var silent: Boolean? = null

    /** Whether the element is hidden. */
    public var invisible: Boolean? = null

    /** Geometry of a shape; accepted keys depend on [type]. */
    public var shape: JsonObject? = null

    /** Style of the element; accepted keys depend on [type]. */
    public var style: JsonObject? = null

    /** Text of a `"text"` element. */
    public var textContent: String? = null

    /** Children of a `"group"` element. */
    public var children: List<GraphicElement>? = null
}
