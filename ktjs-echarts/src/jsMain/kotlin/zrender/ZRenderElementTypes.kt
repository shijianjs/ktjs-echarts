package zrender

import echarts.options.DoubleOrDoubleArray
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import kotlin.js.JsAny
import web.canvas.CanvasLineCap
import web.canvas.CanvasLineJoin
import web.cssom.FontStyle
import web.cssom.TextAlign
import web.cssom.VerticalAlign
import web.events.Event

/**
 * A `[x, y]` point, used by bounding-rectangle intersection results.
 */
external interface ZRPointLike {
    /** X coordinate. */
    var x: Double

    /** Y coordinate. */
    var y: Double
}

/**
 * Options of [ZRBoundingRect.intersect].
 */
external interface BoundingRectIntersectOpt {
    /** Direction used to compute the minimum translation vector. */
    var direction: Double?

    /** Whether the translation vector is computed in both directions. */
    var bidirectional: Boolean?

    /** Distance under which two rectangles are considered touching. */
    var touchThreshold: Double?

    /** Receives the intersection rectangle. */
    var outIntersectRect: RectLike?

    /** Whether negative sizes are clamped. */
    var clamp: Boolean?
}

/**
 * Axis-aligned rectangle returned by the zrender geometry helpers.
 *
 * Upstream class `BoundingRect`. The static members are not exposed by this wrapper.
 */
external interface ZRBoundingRect : RectLike {
    /** Expands this rectangle so that it contains [other]. */
    fun union(other: ZRBoundingRect)

    /** Applies a 2D affine matrix to this rectangle. */
    fun applyTransform(m: Array<Double>)

    /** Returns the matrix that maps this rectangle onto [b]. */
    fun calculateTransform(b: RectLike): Array<Double>

    /** Whether this rectangle intersects [b]. */
    fun intersect(
        b: RectLike,
        mtv: ZRPointLike? = definedExternally,
        opt: BoundingRectIntersectOpt? = definedExternally,
    ): Boolean

    /** Whether `(x, y)` is inside this rectangle. */
    fun contain(x: Double, y: Double): Boolean

    /** Returns a copy of this rectangle. */
    fun clone(): ZRBoundingRect

    /** Copies the values of [other] into this rectangle. */
    fun copy(other: RectLike)

    /** Returns a plain object with the same values. */
    fun plain(): RectLike

    /** Whether every value is finite. */
    fun isFinite(): Boolean

    /** Whether the size is zero. */
    fun isZero(): Boolean
}

/**
 * Rectangle returned by `echarts.helper.getLayoutRect`.
 *
 * Upstream `LayoutRect extends BoundingRect` and adds the computed margins.
 */
external interface LayoutRect : ZRBoundingRect {
    /** Margins applied around the rectangle. */
    var margin: Array<Double>
}

/**
 * Properties of a graphic shape.
 *
 * Upstream declares `PathProps['shape']` as `Dictionary<any>`; the fields below cover the shapes
 * built into zrender (`circle`, `rect`, `sector`, `polygon`, `polyline`, `line`, `arc`,
 * `bezierCurve`, `ring`, `ellipse`, `compoundPath` and `path`). Additional keys can be added
 * through a JavaScript cast when a custom shape needs them.
 */
external interface ZRShapeProps {
    /** Left edge of a `"rect"` or a path layout. */
    var x: Double?

    /** Top edge of a `"rect"` or a path layout. */
    var y: Double?

    /** Width of a `"rect"` or a path layout. */
    var width: Double?

    /** Height of a `"rect"` or a path layout. */
    var height: Double?

    /** Corner radius of a `"rect"`. */
    var r: Double?

    /** Start radius of a `"sector"`, `"arc"` or `"ring"`. */
    var r0: Double?

    /** Horizontal center of a `"circle"`, `"sector"`, `"arc"`, `"ring"` or `"ellipse"`. */
    var cx: Double?

    /** Vertical center of a `"circle"`, `"sector"`, `"arc"`, `"ring"` or `"ellipse"`. */
    var cy: Double?

    /** Horizontal radius of an `"ellipse"`. */
    var rx: Double?

    /** Vertical radius of an `"ellipse"`. */
    var ry: Double?

    /** Start angle of a `"sector"`, `"arc"` or `"ring"`, in radians. */
    var startAngle: Double?

    /** End angle of a `"sector"`, `"arc"` or `"ring"`, in radians. */
    var endAngle: Double?

    /** Whether a sector is drawn clockwise. */
    var clockwise: Boolean?

    /** Start point of a `"line"`. */
    var x1: Double?

    /** Start point of a `"line"`. */
    var y1: Double?

    /** End point of a `"line"`, or the first control point of a `"bezierCurve"`. */
    var x2: Double?

    /** End point of a `"line"`, or the first control point of a `"bezierCurve"`. */
    var y2: Double?

    /** Second control point of a `"bezierCurve"`. */
    var x3: Double?

    /** Second control point of a `"bezierCurve"`. */
    var y3: Double?

    /** End point of a `"bezierCurve"`. */
    var x4: Double?

    /** End point of a `"bezierCurve"`. */
    var y4: Double?

    /** How much of a `"line"` is drawn, `0..1`. */
    var percent: Double?

    /** Points of a `"polygon"` or `"polyline"`, `[[x, y], ...]`. */
    var points: Array<Array<Double>>?

    /** Paths of a `"compoundPath"`. */
    var paths: Array<ZRShapeProps>?

    /** SVG path data of a `"path"`. */
    var pathData: String?

    /** SVG path data of a `"path"`; alias of [pathData]. */
    var d: String?

    /** Layout of the path data inside the element: `"center"` or `"cover"`. */
    var layout: String?
}

/**
 * Placement of the text attached to an element.
 *
 * Upstream `ElementTextConfig` in zrender.
 */
external interface ElementTextConfig {
    /**
     * Position of the text relative to the element: a keyword such as `"inside"` or
     * `"insideTop"`, or a pair of offsets.
     */
    var position: StringOrDoubleOrArray?

    /** Rotation of the text, in radians. */
    var rotation: Double?

    /** Rectangle the text is laid out in. */
    var layoutRect: RectLike?

    /** Offset added to [position], `[x, y]`. */
    var offset: Array<Double>?

    /** Origin of the rotation: `"center"` or `[x, y]`. */
    var origin: StringOrDoubleOrArray?

    /** Distance between the text and the element. */
    var distance: Double?

    /** Whether the position is computed in the local coordinate system. */
    var local: Boolean?

    /** Fill used when the text is inside the element. */
    var insideFill: String?

    /** Stroke used when the text is inside the element. */
    var insideStroke: String?

    /** Fill used when the text is outside the element. */
    var outsideFill: String?

    /** Stroke used when the text is outside the element. */
    var outsideStroke: String?

    /** Whether the text is placed inside the element. */
    var inside: Boolean?

    /** Whether the overflow area is computed automatically. */
    var autoOverflowArea: Boolean?
}

/**
 * Style of a graphic element.
 *
 * Upstream declares this as `PathStyleProps` for paths and `TextStyleProps` for text; the two have
 * a large common subset, which is modelled here.
 */
external interface ZRStyleProps {
    /** Text content of a `"text"` element. */
    var text: String?

    /** Horizontal offset of the text box inside an element. */
    var x: Double?

    /** Vertical offset of the text box inside an element. */
    var y: Double?

    /** Image source of an `"image"` element. */
    var image: StringOrImageLike?

    /** Left edge of the image source rectangle, for cropping. */
    var sx: Double?

    /** Top edge of the image source rectangle, for cropping. */
    var sy: Double?

    /** Width of the image source rectangle, for cropping. */
    var sWidth: Double?

    /** Height of the image source rectangle, for cropping. */
    var sHeight: Double?

    /** Fill color. */
    var fill: ZRColor?

    /** Stroke color. */
    var stroke: ZRColor?

    /** Whether the stroke width is independent of the element scale. */
    var strokeNoScale: Boolean?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Opacity of the fill, `0..1`. */
    var fillOpacity: Double?

    /** Opacity of the stroke, `0..1`. */
    var strokeOpacity: Double?

    /** Width of the line. */
    var lineWidth: Double?

    /** Dash pattern of the line. */
    var lineDash: ZRLineType?

    /** Offset of the dash pattern. */
    var lineDashOffset: Double?

    /** Cap style of the line. */
    var lineCap: CanvasLineCap?

    /** Join style of the line. */
    var lineJoin: CanvasLineJoin?

    /** Miter limit of the line. */
    var miterLimit: Double?

    /** Percentage of the stroke that is drawn, `0..1`. */
    var strokePercent: Double?

    /** Whether the stroke is drawn below the fill. */
    var strokeFirst: Boolean?

    /** Decal pattern applied to the element. */
    var decal: PatternObject?

    /** CSS font shorthand. */
    var font: String?

    /** Font style, a CSS `font-style` keyword. */
    var fontStyle: FontStyle?

    /** Font weight. */
    var fontWeight: ZRFontWeight?

    /** Font family. */
    var fontFamily: String?

    /** Font size in pixels or as a CSS size string. */
    var fontSize: StringOrDouble?

    /** Horizontal alignment of the text. */
    var align: TextAlign?

    /** Vertical alignment of the text. */
    var verticalAlign: VerticalAlign?

    /** Height of one text line. */
    var lineHeight: Double?

    /** Width of the text block. */
    var width: StringOrDouble?

    /** Height of the text block. */
    var height: StringOrDouble?

    /** CSS tag of the text. */
    var tag: String?

    /** Text overflow behaviour. */
    var overflow: TextOverflow?

    /** Overflow behaviour of a single line. */
    var lineOverflow: String?

    /** Ellipsis shown when the text is truncated. */
    var ellipsis: String?

    /** Minimum number of characters kept when the text is truncated. */
    var truncateMinChar: Double?

    /** Color of the text shadow. */
    var textShadowColor: String?

    /** Blur radius of the text shadow. */
    var textShadowBlur: Double?

    /** Horizontal offset of the text shadow. */
    var textShadowOffsetX: Double?

    /** Vertical offset of the text shadow. */
    var textShadowOffsetY: Double?

    /** Background color behind the text. */
    var backgroundColor: ZRColor?

    /** Padding of the text. */
    var padding: StringOrDoubleOrArray?

    /** Margin around the text. */
    var margin: DoubleOrDoubleArray?

    /** Border color of the text background. */
    var borderColor: ZRColor?

    /** Width of the text background border. */
    var borderWidth: Double?

    /** Border radius of the text background. */
    var borderRadius: StringOrDoubleOrArray?

    /** Color of the shadow. */
    var shadowColor: String?

    /** Blur radius of the shadow. */
    var shadowBlur: Double?

    /** Horizontal offset of the shadow. */
    var shadowOffsetX: Double?

    /** Vertical offset of the shadow. */
    var shadowOffsetY: Double?
}

/**
 * Event object passed to element event handlers on zrender elements.
 */
external interface ZRElementEvent {
    /** Event name, e.g. `"click"` or `"mousemove"`. */
    val type: String

    /** Native DOM event that produced this event. */
    val event: Event?

    /** Element that received the event. */
    val target: ZRElement

    /** Topmost element under the pointer. */
    val topTarget: ZRElement

    /** Whether the event stops propagating to the parent elements. */
    var cancelBubble: Boolean

    /** Horizontal position inside the element. */
    val offsetX: Double

    /** Vertical position inside the element. */
    val offsetY: Double

    /** Horizontal position of a pinch gesture. */
    val pinchX: Double

    /** Vertical position of a pinch gesture. */
    val pinchY: Double

    /** Scale of a pinch gesture. */
    val pinchScale: Double

    /** Rotation of a gesture event. */
    val wheelDelta: Double

    /** Whether the event was produced by a touch device. */
    val zrByTouch: Boolean

    /** Mouse button or touch identifier. */
    val which: Double

    /** Stops the event from bubbling to the parent elements. */
    fun stop()
}

/**
 * A zrender element: a displayable shape, text, image or group.
 *
 * This covers the members that custom series, custom elements and event handlers normally use;
 * see the upstream `Element` declaration for the full surface.
 */
external interface ZRElement {
    /** Unique id of the element. */
    var id: String

    /** Name of the element. */
    var name: String

    /** Element type, e.g. `"circle"`, `"text"` or `"group"`. */
    val type: String

    /** Horizontal position. */
    var x: Double

    /** Vertical position. */
    var y: Double

    /** Horizontal scale. */
    var scaleX: Double

    /** Vertical scale. */
    var scaleY: Double

    /** Rotation in radians. */
    var rotation: Double

    /** Horizontal origin of the transform. */
    var originX: Double

    /** Vertical origin of the transform. */
    var originY: Double

    /** z-index of the element. */
    var z: Double

    /** z-index of the element inside its z-level. */
    var z2: Double

    /** z-level of the element. */
    var zlevel: Double

    /** Whether the element ignores mouse events. */
    var silent: Boolean

    /** Whether the element is invisible. */
    var invisible: Boolean

    /** Whether the element is ignored by the layout. */
    var ignore: Boolean

    /** Placement of the text attached to the element. */
    var textConfig: ElementTextConfig?

    /** Cursor shown on hover. */
    var cursor: String

    /** Whether the element can be dragged. */
    var draggable: Boolean

    /** Style of the element. */
    var style: ZRStyleProps

    /** Shape of the element. */
    var shape: ZRShapeProps

    /** Parent group, or `null` for a root element. */
    val parent: ZRGroup?

    /** Reads an element property. */
    fun attr(key: String): JsAny

    /** Writes an element property. */
    fun attr(key: String, value: JsAny?)

    /** Registers an event handler. */
    fun on(eventName: String, handler: (ZRElementEvent) -> Unit)

    /** Removes an event handler. */
    fun off(eventName: String, handler: (ZRElementEvent) -> Unit)

    /** Removes the element from its parent. */
    fun remove()

    /** Returns the bounding rectangle of the element. */
    fun getBoundingRect(): ZRBoundingRect
}

/**
 * A group of zrender elements.
 */
external interface ZRGroup : ZRElement {
    /** Child elements of the group. */
    val children: Array<ZRElement>

    /** Adds a child element. */
    fun add(element: ZRElement)

    /** Removes a child element. */
    fun remove(element: ZRElement)

    /** Removes every child element. */
    fun removeAll()

    /** Runs [cb] for every child element. */
    fun eachChild(cb: (element: ZRElement) -> Unit)
}
