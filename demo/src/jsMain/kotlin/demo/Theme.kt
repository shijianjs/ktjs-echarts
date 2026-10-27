package demo

import kotlin.js.js
import web.dom.document

/**
 * Light/dark switching.
 *
 * The switch is the `data-theme="dark"` attribute on `<html>`: every colour of the page comes from a
 * custom property declared for both themes in `demo.css`, so flipping the attribute re-colours the
 * whole site. ECharts has its own dark theme, so a switch also rebuilds the current page and
 * re-creates the chart instances with the matching theme.
 *
 * The choice is remembered in `localStorage`; `index.html` applies it before first paint so a stored
 * dark preference does not flash the light theme.
 */
object Theme {
    private const val STORAGE_KEY = "ktjs-echarts-theme"
    private const val DARK = "dark"

    /** Whether the dark theme is active. */
    val isDark: Boolean
        get() = document.documentElement.getAttribute("data-theme") == DARK

    /** Re-applies the theme stored by a previous visit. Called once by `main`. */
    fun applyStored() {
        if (storedValue() == DARK) apply(true) else apply(false)
    }

    /** Applies [dark] to the document and remembers the choice. */
    fun apply(dark: Boolean) {
        val root = document.documentElement
        if (dark) {
            root.setAttribute("data-theme", DARK)
        } else {
            root.removeAttribute("data-theme")
        }
        store(if (dark) DARK else "light")
    }

    // `localStorage` has no wrapper in kotlin-browser 2026.10.1, so the two calls go through `js`.
    private fun storedValue(): String? =
        js("() => { try { return localStorage.getItem('$STORAGE_KEY') } catch (e) { return null } }")() as String?

    private fun store(value: String) {
        js("(value) => { try { localStorage.setItem('$STORAGE_KEY', value) } catch (e) { /* storage disabled */ } }")(value)
    }
}
