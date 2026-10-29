package demo.ui

import js.buffer.ArrayBuffer
import js.typedarrays.Float32Array
import js.typedarrays.Int32Array
import kotlin.js.JsAny
import kotlin.js.Promise
import kotlin.js.js

/**
 * Loads a JSON document from the static files next to the site (`data/<name>.json`).
 *
 * The datasets that the official examples fetch with `$.get` live in
 * `demo/src/jsMain/resources/data/` (see the `SOURCE.md` there), so a page that needs one mounts its
 * chart first and applies the option once the promise settles; the alternative — inlining hundreds
 * of kilobytes into Kotlin — would make the ports unreadable.
 *
 * `fetch` is written as a plain JavaScript function because kotlin-browser does not model it; the
 * result is `JsAny` because JSON has no static type, so each caller narrows it to the option it is
 * about to build.
 */
fun loadJson(path: String): Promise<JsAny> =
    js("function (path) { return fetch(path).then(function (response) { return response.json(); }); }")(path)
        as Promise<JsAny>

/** Loads a text document (an SVG map, for instance) from the static files. */
fun loadText(path: String): Promise<String> =
    js("function (path) { return fetch(path).then(function (response) { return response.text(); }); }")(path)
        as Promise<String>

/**
 * Loads a binary file of 32-bit floats, e.g. the `[x0, y0, x1, y1, ...]` point table of a large
 * scatter page.
 *
 * The typed array is passed to ECharts as it is: passing it as `data` is what makes large-data
 * pages cheap, because no per-point JavaScript object is created.
 */
fun loadFloat32(path: String): Promise<Float32Array<ArrayBuffer>> =
    js(
        "function (path) { return fetch(path).then(function (response) { return response.arrayBuffer(); })" +
            ".then(function (buffer) { return new Float32Array(buffer); }); }",
    )(path) as Promise<Float32Array<ArrayBuffer>>

/** Loads a binary file of 32-bit integers, e.g. the coordinate pairs of the bulk GPS page. */
fun loadInt32(path: String): Promise<Int32Array<ArrayBuffer>> =
    js(
        "function (path) { return fetch(path).then(function (response) { return response.arrayBuffer(); })" +
            ".then(function (buffer) { return new Int32Array(buffer); }); }",
    )(path) as Promise<Int32Array<ArrayBuffer>>
