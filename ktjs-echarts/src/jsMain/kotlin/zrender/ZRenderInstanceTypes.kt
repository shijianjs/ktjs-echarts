package zrender

import echarts.options.BooleanOrString
import echarts.options.StringOrDouble
import kotlin.js.JsAny
import web.html.HTMLElement

/**
 * A zrender instance, returned by `EChartsInstance.getZr`.
 */
external interface ZRenderType {
    /** Container element, `null` in server-side rendering mode. */
    val dom: HTMLElement?

    /** Numeric id of the instance. */
    val id: Double

    /** Adds an element to the root group. */
    fun add(element: ZRElement)

    /** Removes an element from the root group. */
    fun remove(element: ZRElement)

    /** Configures the `zLevel`-th canvas layer. */
    fun configLayer(zLevel: Double, config: ZRLayerConfig)

    /** Repaints the instance on the next frame. */
    fun refresh()

    /** Repaints the instance immediately. */
    fun refreshImmediately(noAnimationUpdate: Boolean? = definedExternally)

    /** Flushes the pending animation frame. */
    fun flush()

    /** Resizes the instance. */
    fun resize(opt: ZRenderResizeOpt? = definedExternally)

    /** Returns the width of the instance. */
    fun getWidth(): Double?

    /** Returns the height of the instance. */
    fun getHeight(): Double?

    /** Sets the background color. */
    fun setBackgroundColor(color: ZRColor)

    /** Returns the background color. */
    fun getBackgroundColor(): ZRColor

    /** Sets the cursor style of the container. */
    fun setCursorStyle(cursorStyle: String)

    /** Registers an event handler. */
    fun on(eventName: String, handler: (ZRElementEvent) -> Unit)

    /** Removes an event handler. */
    fun off(eventName: String, handler: (ZRElementEvent) -> Unit)

    /** Triggers an event. */
    fun trigger(eventName: String, event: JsAny? = definedExternally)

    /** Removes every element. */
    fun clear()

    /** Disposes the instance. */
    fun dispose()
}

/**
 * Options of [ZRenderType.configLayer].
 */
external interface ZRLayerConfig {
    /** Colour the layer is cleared with before each frame. */
    var clearColor: ZRColor?

    /** Whether the content of the previous frame is kept, producing a motion-blur trail. */
    var motionBlur: Boolean?

    /** Opacity of the previous frame when [motionBlur] is on. */
    var lastFrameAlpha: Double?
}

/**
 * Options of [ZRenderType.resize].
 */
external interface ZRenderResizeOpt {
    /** New width in pixels or as a CSS size string. */
    var width: StringOrDouble?

    /** New height in pixels or as a CSS size string. */
    var height: StringOrDouble?
}

/**
 * The `echarts.zrender` namespace: the bundled zrender library.
 */
external interface ZRenderNamespace {
    /** Version of the bundled zrender. */
    val version: String

    /** Creates a zrender instance. */
    fun init(dom: HTMLElement? = definedExternally, opts: ZRenderInitOpt? = definedExternally): ZRenderType

    /** Disposes an instance. */
    fun dispose(zr: ZRenderType)

    /** Disposes every instance. */
    fun disposeAll()

    /** Returns the instance with the given id. */
    fun getInstance(id: Double): ZRenderType

    /** Registers a painter. */
    fun registerPainter(name: String, painter: JsAny)

    /** Returns the SSR data of an element. */
    fun getElementSSRData(element: ZRElement): JsAny

    /** Registers an SSR data getter. */
    fun registerSSRDataGetter(getter: (ZRElement) -> JsAny)
}

/**
 * Options of `ZRenderNamespace.init`.
 */
external interface ZRenderInitOpt {
    /** Renderer used by the instance. */
    var renderer: RendererType?

    /** Device pixel ratio. */
    var devicePixelRatio: Double?

    /** Width of the instance. */
    var width: StringOrDouble?

    /** Height of the instance. */
    var height: StringOrDouble?

    /** Whether dirty-rectangle rendering is enabled. */
    var useDirtyRect: Boolean?

    /** Whether a coarse pointer is used. */
    var useCoarsePointer: BooleanOrString?

    /** Size of the touch pointer. */
    var pointerSize: Double?

    /** Whether the instance renders on the server. */
    var ssr: Boolean?
}
