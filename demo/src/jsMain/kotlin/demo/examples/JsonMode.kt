package demo.examples

import kotlin.js.JsAny
import kotlin.js.js

/** Parses a serialized option into the plain object `setOption` accepts. */
internal fun parseJsonOption(json: String): JsAny = js("(text) => JSON.parse(text)")(json)
