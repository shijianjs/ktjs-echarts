package zrender

import kotlin.js.JsAny
import web.html.HTMLCanvasElement

/**
 * The `echarts.matrix` namespace: 2D affine matrix helpers.
 *
 * Matrices are arrays of six numbers in the same order as 2D canvas transforms.
 */
external interface MatrixNamespace {
    /** Creates an identity matrix. */
    fun create(): Array<Double>

    /** Resets [out] to the identity matrix. */
    fun identity(out: Array<Double>): Array<Double>

    /** Copies [m] into [out]. */
    fun copy(out: Array<Double>, m: Array<Double>): Array<Double>

    /** Multiplies [m1] and [m2] into [out]. */
    fun mul(out: Array<Double>, m1: Array<Double>, m2: Array<Double>): Array<Double>

    /** Translates [a] by [v] into [out]. */
    fun translate(out: Array<Double>, a: Array<Double>, v: Array<Double>): Array<Double>

    /** Rotates [a] by [rad] radians around [pivot] into [out]. */
    fun rotate(out: Array<Double>, a: Array<Double>, rad: Double, pivot: Array<Double>? = definedExternally): Array<Double>

    /** Scales [a] by [v] into [out]. */
    fun scale(out: Array<Double>, a: Array<Double>, v: Array<Double>): Array<Double>

    /** Inverts [a] into [out], or returns `null` when [a] is not invertible. */
    fun invert(out: Array<Double>, a: Array<Double>): Array<Double>?

    /** Returns a copy of [a]. */
    fun clone(a: Array<Double>): Array<Double>
}

/**
 * The `echarts.vector` namespace: 2D vector helpers.
 */
external interface VectorNamespace {
    /** Creates a vector. */
    fun create(x: Double? = definedExternally, y: Double? = definedExternally): Array<Double>

    /** Copies [v] into [out]. */
    fun copy(out: Array<Double>, v: Array<Double>): Array<Double>

    /** Returns a copy of [v]. */
    fun clone(v: Array<Double>): Array<Double>

    /** Sets the components of [out]. */
    fun set(out: Array<Double>, a: Double, b: Double): Array<Double>

    /** Adds [v1] and [v2] into [out]. */
    fun add(out: Array<Double>, v1: Array<Double>, v2: Array<Double>): Array<Double>

    /** Adds [v2] scaled by [a] to [v1] into [out]. */
    fun scaleAndAdd(out: Array<Double>, v1: Array<Double>, v2: Array<Double>, a: Double): Array<Double>

    /** Subtracts [v2] from [v1] into [out]. */
    fun sub(out: Array<Double>, v1: Array<Double>, v2: Array<Double>): Array<Double>

    /** Returns the length of [v]. */
    fun len(v: Array<Double>): Double

    /** Returns the squared length of [v]. */
    fun lenSquare(v: Array<Double>): Double

    /** Multiplies [v1] and [v2] component-wise into [out]. */
    fun mul(out: Array<Double>, v1: Array<Double>, v2: Array<Double>): Array<Double>

    /** Divides [v1] by [v2] component-wise into [out]. */
    fun div(out: Array<Double>, v1: Array<Double>, v2: Array<Double>): Array<Double>

    /** Returns the dot product of [v1] and [v2]. */
    fun dot(v1: Array<Double>, v2: Array<Double>): Double

    /** Scales [v] by [s] into [out]. */
    fun scale(out: Array<Double>, v: Array<Double>, s: Double): Array<Double>

    /** Normalizes [v] into [out]. */
    fun normalize(out: Array<Double>, v: Array<Double>): Array<Double>

    /** Returns the distance between [v1] and [v2]. */
    fun distance(v1: Array<Double>, v2: Array<Double>): Double

    /** Returns the squared distance between [v1] and [v2]. */
    fun distanceSquare(v1: Array<Double>, v2: Array<Double>): Double

    /** Negates [v] into [out]. */
    fun negate(out: Array<Double>, v: Array<Double>): Array<Double>

    /** Interpolates between [v1] and [v2] by [t] into [out]. */
    fun lerp(out: Array<Double>, v1: Array<Double>, v2: Array<Double>, t: Double): Array<Double>

    /** Applies the transform [m] to [v] into [out]. */
    fun applyTransform(out: Array<Double>, v: Array<Double>, m: Array<Double>): Array<Double>

    /** Stores the component-wise minimum of [v1] and [v2] in [out]. */
    fun min(out: Array<Double>, v1: Array<Double>, v2: Array<Double>): Array<Double>

    /** Stores the component-wise maximum of [v1] and [v2] in [out]. */
    fun max(out: Array<Double>, v1: Array<Double>, v2: Array<Double>): Array<Double>
}

/**
 * The `echarts.util` namespace: a subset of the zrender utilities.
 */
external interface UtilNamespace {
    /** Maps [arr] with [cb]. */
    fun <T, R> map(arr: Array<T>, cb: (value: T, index: Double, arr: Array<T>) -> R): Array<R>

    /** Runs [cb] for every entry of [arr]. */
    fun <T> each(arr: Array<T>, cb: (value: T, index: Double, arr: Array<T>) -> Unit)

    /** Returns the index of [value] in [arr], or `-1`. */
    fun <T> indexOf(arr: Array<T>, value: T): Double

    /** Inherits [baseClazz] into [clazz]. */
    fun inherits(clazz: Function<*>, baseClazz: Function<*>)

    /** Reduces [arr] with [cb]. */
    fun <T, S> reduce(arr: Array<T>, cb: (previous: S, current: T, index: Double, arr: Array<T>) -> S, memo: S? = definedExternally): S

    /** Filters [arr] with [cb]. */
    fun <T> filter(arr: Array<T>, cb: (value: T, index: Double, arr: Array<T>) -> Boolean): Array<T>

    /** Binds [func] to [ctx]. */
    fun bind(func: Function<*>, ctx: JsAny): Function<*>

    /** Curries [func] with the given arguments. */
    fun curry(func: Function<*>, vararg args: JsAny): Function<*>

    /** Whether [value] is an array. */
    fun isArray(value: JsAny?): Boolean

    /** Whether [value] is a string. */
    fun isString(value: JsAny?): Boolean

    /** Whether [value] is an object. */
    fun isObject(value: JsAny?): Boolean

    /** Whether [value] is a function. */
    fun isFunction(value: JsAny?): Boolean

    /** Copies the properties of [source] into [target]. */
    fun extend(target: JsAny, source: JsAny): JsAny

    /** Fills the missing properties of [target] from [source]. */
    fun defaults(target: JsAny, source: JsAny, overlay: Boolean? = definedExternally): JsAny

    /** Returns a deep copy of [source]. */
    fun <T : JsAny> clone(source: T): T

    /** Merges [source] into [target]. */
    fun merge(target: JsAny, source: JsAny, overwrite: Boolean? = definedExternally): JsAny
}

/**
 * Hash map returned by [ZrUtilNamespace.createHashMap].
 *
 * It is a lighter alternative to `Map` used throughout zrender. Upstream also allows numeric
 * keys; this wrapper exposes the string-key form, which is what the public API documents.
 */
external interface ZRHashMap<T> {
    /** Whether [key] is present in the map. */
    fun hasKey(key: String): Boolean

    /** Returns the value stored under [key], or `null`/`undefined` when it is absent. */
    fun get(key: String): T?

    /** Stores [value] under [key] and returns it. */
    fun set(key: String, value: T): T

    /** Runs [cb] for every entry of the map. */
    fun each(cb: (value: T, key: String) -> Unit, context: JsAny? = definedExternally)

    /** Returns every key of the map. */
    fun keys(): Array<String>

    /** Removes [key] from the map. */
    fun removeKey(key: String)
}

/**
 * The `echarts.zrUtil` namespace: the full zrender utility collection.
 */
external interface ZrUtilNamespace {
    /** Returns a unique id. */
    fun guid(): Double

    /** Logs an error to the console. */
    fun logError(vararg args: JsAny?)

    /** Returns a deep copy of [source]. */
    fun <T : JsAny> clone(source: T): T

    /** Merges [source] into [target]. */
    fun merge(target: JsAny, source: JsAny, overwrite: Boolean? = definedExternally): JsAny

    /** Extends [target] with [source]. */
    fun extend(target: JsAny, source: JsAny): JsAny

    /** Fills the missing properties of [target] from [source]. */
    fun defaults(target: JsAny, source: JsAny, overlay: Boolean? = definedExternally): JsAny

    /** Creates a canvas element. */
    fun createCanvas(): HTMLCanvasElement

    /** Returns the index of [value] in [arr], or `-1`. */
    fun <T> indexOf(arr: Array<T>, value: T): Double

    /** Maps [arr] with [cb]. */
    fun <T, R> map(arr: Array<T>, cb: (value: T, index: Double, arr: Array<T>) -> R): Array<R>

    /** Runs [cb] for every entry of [arr]. */
    fun <T> each(arr: Array<T>, cb: (value: T, index: Double, arr: Array<T>) -> Unit)

    /** Filters [arr] with [cb]. */
    fun <T> filter(arr: Array<T>, cb: (value: T, index: Double, arr: Array<T>) -> Boolean): Array<T>

    /** Finds the first entry of [arr] accepted by [cb]. */
    fun <T> find(arr: Array<T>, cb: (value: T, index: Double, arr: Array<T>) -> Boolean): T?

    /** Returns the keys of [obj]. */
    fun keys(obj: JsAny): Array<String>

    /** Creates a hash map from an existing hash map, a plain object or an array of keys. */
    fun <T> createHashMap(obj: JsAny? = definedExternally): ZRHashMap<T>

    /** Whether [value] is an array. */
    fun isArray(value: JsAny?): Boolean

    /** Whether [value] is a function. */
    fun isFunction(value: JsAny?): Boolean

    /** Whether [value] is a string. */
    fun isString(value: JsAny?): Boolean

    /** Whether [value] is a number. */
    fun isNumber(value: JsAny?): Boolean

    /** Whether [value] is an object. */
    fun isObject(value: JsAny?): Boolean

    /** Normalizes a padding shorthand. */
    fun normalizeCssArray(value: Array<Double>): Array<Double>

    /** Normalizes a padding shorthand. */
    fun normalizeCssArray(value: Double): Array<Double>

    /** Throws when [condition] is falsy. */
    fun assert(condition: Boolean, message: String? = definedExternally)

    /** Removes the surrounding whitespace of [str]. */
    fun trim(str: String): String

    /** Returns a function that calls [func] at most once per [delay] milliseconds. */
    fun <T> throttle(func: (T) -> Unit, delay: Double, debounce: Boolean? = definedExternally): (T) -> Unit
}
