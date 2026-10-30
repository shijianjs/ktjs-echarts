package demo

import kotlin.js.js

/** Which code form the "KMP JSON model" pages display above their chart. */
enum class SourceMode(val label: String, internal val stored: String) {
    Kotlin("Kotlin", "kotlin"),
    Json("JSON", "json"),
}

/**
 * Code-source switch of the top bar.
 *
 * Language and theme change how a page is built, and so does this switch: it selects whether the
 * model pages show the Kotlin that builds the option or the JSON that model serializes to. The shell
 * therefore drops the cached pages and rebuilds the current route, exactly like a language switch.
 *
 * The choice is remembered in `localStorage`, so a reload keeps the last view.
 */
object SourceModeState {
    private const val STORAGE_KEY = "ktjs-echarts-source-mode"

    private val listeners = mutableListOf<(SourceMode) -> Unit>()

    private var active: SourceMode = stored() ?: SourceMode.Kotlin

    /** The code form pages currently show. */
    val current: SourceMode
        get() = active

    /** Switches the code form, remembers the choice and notifies the shell. */
    fun set(mode: SourceMode) {
        if (mode == active) return
        active = mode
        store(mode.stored)
        listeners.toList().forEach { it(mode) }
    }

    /** Registers a listener invoked after [set] changed the mode. */
    fun onChange(listener: (SourceMode) -> Unit) {
        listeners += listener
    }

    // `localStorage` has no wrapper in kotlin-browser, so the two calls go through `js`.
    private fun stored(): SourceMode? {
        val value =
            js("() => { try { return localStorage.getItem('$STORAGE_KEY') } catch (e) { return null } }")() as String?
        return SourceMode.entries.firstOrNull { it.stored == value }
    }

    private fun store(value: String) {
        js("(value) => { try { localStorage.setItem('$STORAGE_KEY', value) } catch (e) { /* storage disabled */ } }")(value)
    }
}
