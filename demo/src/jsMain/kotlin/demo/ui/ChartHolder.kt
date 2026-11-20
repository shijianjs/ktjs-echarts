package demo.ui

import web.window.window

class ChartHolder internal constructor() {
    private val intervalHandles = mutableListOf<Int>()

    private val timeoutHandles = mutableListOf<Int>()

    private val resizeHandlers = mutableListOf<() -> Unit>()

    private val unmountHandlers = mutableListOf<() -> Unit>()

    /** Registers an interval timer owned by this chart; cleared when the chart is disposed. */
    fun setInterval(handler: () -> Unit, delayMillis: Int): Int =
        startInterval(handler, delayMillis).also { intervalHandles.add(it) }

    /** Registers a one-shot timer owned by this chart; cleared if it has not fired on disposal. */
    fun setTimeout(handler: () -> Unit, delayMillis: Int): Int =
        startTimeout(handler, delayMillis).also { timeoutHandles.add(it) }

    /**
     * Registers a handler that runs after the container was resized.
     *
     * Examples that place pixel-sized overlays need the new size; the handlers belong to this mount
     * cycle, so [release] drops them.
     */
    fun onResize(handler: () -> Unit) {
        resizeHandlers.add(handler)
    }

    /**
     * Registers a handler that releases a resource the example owns, such as a listener on `window`.
     *
     * Runs before the instance is disposed.
     */
    fun onUnmount(handler: () -> Unit) {
        unmountHandlers.add(handler)
    }

    /** Forwards a resize to the handlers registered through [onResize]. */
    internal fun resize() {
        resizeHandlers.forEach { it() }
    }

    /** Clears timers, drops handlers and runs the unmount callbacks. */
    internal fun release() {
        intervalHandles.forEach(::cancelInterval)
        intervalHandles.clear()
        timeoutHandles.forEach(::cancelTimeout)
        timeoutHandles.clear()
        resizeHandlers.clear()
        unmountHandlers.forEach { it() }
        unmountHandlers.clear()
    }
}
