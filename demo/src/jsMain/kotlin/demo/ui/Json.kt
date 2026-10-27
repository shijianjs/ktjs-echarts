package demo.ui

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
