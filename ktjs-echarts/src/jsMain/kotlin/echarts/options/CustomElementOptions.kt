package echarts.options

import echarts.AnimationEasing
import echarts.BlurScope
import js.objects.Record
import kotlin.js.JsAny
import kotlin.js.JsName
import kotlin.js.unsafeCast
import zrender.ElementTextConfig
import zrender.TransformProp
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/*
 * Element tree returned by `CustomSeriesOption.renderItem`.
 *
 * Upstream builds these types from zrender's `PathProps` / `TextProps` / `ImageProps` / `GroupProps`
 * plus the generic helpers `TransitionOptionMixin<T>` and `ElementKeyframeAnimationOption<Props>`.
 * Kotlin cannot express those generic instantiations, so the shared members are collected in
 * [CustomElementProps] and the shapes / styles reuse [ZRShapeProps] and [ZRStyleProps].
 */

/**
 * Element properties carried by a keyframe or by a transition start / end state.
 *
 * Upstream `T` in `TransitionOptionMixin<T>` and `AnimationKeyframe<T>`: the part shared by
 * zrender's `GroupProps`, `PathProps`, `TextProps` and `ImageProps`.
 */
external interface CustomElementProps {
    /** Horizontal position. */
    var x: Double?

    /** Vertical position. */
    var y: Double?

    /** Horizontal scale. */
    var scaleX: Double?

    /** Vertical scale. */
    var scaleY: Double?

    /** Rotation in radians. */
    var rotation: Double?

    /** Horizontal origin of the transform. */
    var originX: Double?

    /** Vertical origin of the transform. */
    var originY: Double?

    /** Horizontal anchor of the transform. */
    var anchorX: Double?

    /** Vertical anchor of the transform. */
    var anchorY: Double?

    /** Horizontal skew. */
    var skewX: Double?

    /** Vertical skew. */
    var skewY: Double?

    /** Style of the element. */
    var style: ZRStyleProps?

    /** Shape of a path element. */
    var shape: ZRShapeProps?
}

/**
 * One keyframe of [ElementKeyframeAnimationOption].
 *
 * Upstream `AnimationKeyframe<T>`, i.e. [CustomElementProps] plus [easing] and [percent].
 */
external interface AnimationKeyframe : CustomElementProps {
    /** Easing of the segment that ends at this keyframe. */
    var easing: AnimationEasing?

    /** Position of the keyframe in the animation, `0..100`. */
    var percent: Double?
}

/**
 * Timing of one element animation.
 *
 * Upstream `AnimationOption` of zrender (`AnimationOption$1` in the ECharts declarations).
 */
external interface ElementAnimationOption {
    /** Duration in milliseconds. */
    var duration: Double?

    /** Easing of the animation. */
    var easing: AnimationEasing?

    /** Delay in milliseconds. */
    var delay: Double?
}

/**
 * Keyframe animation of a custom element.
 *
 * Upstream `ElementKeyframeAnimationOption<Props>`; Kotlin cannot carry the `Props` parameter, so
 * the keyframes use [CustomElementProps].
 */
external interface ElementKeyframeAnimationOption : ElementAnimationOption {
    /** Whether the animation loops. */
    var loop: Boolean?

    /** Keyframes of the animation. */
    var keyframes: Array<AnimationKeyframe>?
}

/**
 * `ElementKeyframeAnimationOption | ElementKeyframeAnimationOption[]`.
 */
external interface ElementKeyframeAnimationOptionOrArray

/** Wraps a single animation. */
fun ElementKeyframeAnimationOptionOrArray(
    value: ElementKeyframeAnimationOption,
): ElementKeyframeAnimationOptionOrArray = value.unsafeCast<ElementKeyframeAnimationOptionOrArray>()

/** Wraps a list of animations, one per property group. */
fun ElementKeyframeAnimationOptionOrArray(
    values: Array<ElementKeyframeAnimationOption>,
): ElementKeyframeAnimationOptionOrArray = values.unsafeCast<ElementKeyframeAnimationOptionOrArray>()

/**
 * Transition configuration shared by the custom element options.
 *
 * Upstream `TransitionOptionMixin<T>` mixed into groups, paths, images and text.
 */
external interface TransitionOptionMixin {
    /**
     * Properties that are transitioned, one name or a list of names; `"all"` transitions
     * everything.
     */
    var transition: StringOrStringArray?

    /** State the element enters from. */
    var enterFrom: CustomElementProps?

    /** State the element leaves to. */
    var leaveTo: CustomElementProps?

    /** Animation used when the element enters. */
    var enterAnimation: ElementAnimationOption?

    /** Animation used when the element is updated. */
    var updateAnimation: ElementAnimationOption?

    /** Animation used when the element leaves. */
    var leaveAnimation: ElementAnimationOption?
}

/**
 * Transform and extra-data access passed to the `during` callback of every custom element.
 *
 * Upstream `TransitionBaseDuringAPI`.
 */
external interface TransitionBaseDuringAPI {
    /** Writes a transform property. */
    fun setTransform(key: TransformProp, value: Double): TransitionBaseDuringAPI

    /** Reads a transform property. */
    fun getTransform(key: TransformProp): Double

    /** Writes one entry of the extra data. */
    fun setExtra(key: String, value: JsAny): TransitionBaseDuringAPI

    /** Reads one entry of the extra data. */
    fun getExtra(key: String): JsAny?
}

/**
 * Access passed to the `during` callback of a displayable custom element.
 *
 * Upstream `TransitionDuringAPI`, which adds style and shape access to [TransitionBaseDuringAPI].
 */
external interface TransitionDuringAPI : TransitionBaseDuringAPI {
    /** Writes a shape property. */
    fun setShape(key: String, value: JsAny): TransitionDuringAPI

    /** Reads a shape property. */
    fun getShape(key: String): JsAny?

    /** Writes a style property. */
    fun setStyle(key: String, value: JsAny): TransitionDuringAPI

    /** Reads a style property. */
    fun getStyle(key: String): JsAny?
}

/**
 * Focus behaviour of the root element returned by `renderItem`: `"none"`, `"self"`, `"series"`
 * or a list of data indices.
 *
 * Upstream `CustomRootElementOption['focus']`.
 */
sealed external interface CustomElementFocus

/** Wraps a focus keyword. Prefer the constants in [CustomElementFocuses]. */
fun CustomElementFocus(value: String): CustomElementFocus = value.unsafeCast<CustomElementFocus>()

/** Wraps a list of data indices. */
fun CustomElementFocus(values: Array<Double>): CustomElementFocus =
    values.unsafeCast<CustomElementFocus>()

/**
 * The documented keyword values of [CustomElementFocus].
 */
object CustomElementFocuses {
    /** No focus effect. */
    inline val none: CustomElementFocus get() = CustomElementFocus("none")

    /** Focus this element only. */
    inline val self: CustomElementFocus get() = CustomElementFocus("self")

    /** Focus the whole series. */
    inline val series: CustomElementFocus get() = CustomElementFocus("series")
}

/**
 * `false | "byName" | "byIndex"`, the `$mergeChildren` member of a group element.
 */
sealed external interface CustomMergeChildrenOption

/** Wraps a boolean; only `false` is meaningful. */
fun CustomMergeChildrenOption(value: Boolean): CustomMergeChildrenOption =
    value.unsafeCast<CustomMergeChildrenOption>()

/** Wraps a merge mode keyword. */
fun CustomMergeChildrenOption(value: String): CustomMergeChildrenOption =
    value.unsafeCast<CustomMergeChildrenOption>()

/**
 * The documented values of [CustomMergeChildrenOption].
 */
object CustomMergeChildrenOptions {
    /** Merge the children by their name. */
    inline val byName: CustomMergeChildrenOption get() = CustomMergeChildrenOption("byName")

    /** Merge the children by their index. */
    inline val byIndex: CustomMergeChildrenOption get() = CustomMergeChildrenOption("byIndex")

    /** Replace the children instead of merging them. */
    inline val none: CustomMergeChildrenOption get() = CustomMergeChildrenOption(false)
}

/**
 * `CustomTextOption | false`, the `textContent` member of a custom element.
 */
external interface CustomTextContentOption

/** Wraps a text element. */
fun CustomTextContentOption(value: CustomTextOption): CustomTextContentOption =
    value.unsafeCast<CustomTextContentOption>()

/** Wraps `false`, which disables the text. */
fun CustomTextContentOption(value: Boolean): CustomTextContentOption =
    value.unsafeCast<CustomTextContentOption>()

/**
 * `CustomPathOption | false`, the `clipPath` member of a custom element.
 */
external interface CustomClipPathOption

/** Wraps a path element. */
fun CustomClipPathOption(value: CustomPathOption): CustomClipPathOption =
    value.unsafeCast<CustomClipPathOption>()

/** Wraps `false`, which disables the clip path. */
fun CustomClipPathOption(value: Boolean): CustomClipPathOption =
    value.unsafeCast<CustomClipPathOption>()

/**
 * `ZRStyleProps | false`, the style of an element state; `false` disables the state style.
 */
external interface CustomStyleOption

/** Wraps a style. */
fun CustomStyleOption(value: ZRStyleProps): CustomStyleOption =
    value.unsafeCast<CustomStyleOption>()

/** Wraps `false`, which disables the style. */
fun CustomStyleOption(value: Boolean): CustomStyleOption =
    value.unsafeCast<CustomStyleOption>()

/**
 * Style of one interaction state (`emphasis`, `blur` or `select`) of a custom element.
 *
 * Upstream `CustomDisplayableOptionOnState`.
 */
external interface CustomDisplayableOptionOnState {
    /** Horizontal position. */
    var x: Double?

    /** Vertical position. */
    var y: Double?

    /** Horizontal scale. */
    var scaleX: Double?

    /** Vertical scale. */
    var scaleY: Double?

    /** Rotation in radians. */
    var rotation: Double?

    /** Horizontal origin of the transform. */
    var originX: Double?

    /** Vertical origin of the transform. */
    var originY: Double?

    /** Horizontal anchor of the transform. */
    var anchorX: Double?

    /** Vertical anchor of the transform. */
    var anchorY: Double?

    /** Horizontal skew. */
    var skewX: Double?

    /** Vertical skew. */
    var skewY: Double?

    /** Placement of the text attached to the element. */
    var textConfig: ElementTextConfig?

    /** z-index of the element inside its z-level. */
    var z2: Double?

    /** Style of the state, or `false` to disable it. */
    var style: CustomStyleOption?
}

/**
 * Base of every custom element option.
 *
 * Upstream `CustomBaseElementOption`: the members that groups, paths, images and text share.
 * [type] is the element type, e.g. `"group"`, `"rect"` or `"text"`; each subtype documents the
 * value it requires.
 */
external interface CustomBaseElementOption {
    /** Element type, e.g. `"group"`, `"text"`, `"rect"`, `"circle"` or `"image"`. */
    var type: String

    /** Element id. */
    var id: String?

    /** Element name. */
    var name: String?

    /** Extra information attached to the element. */
    var info: Record<String, JsAny>?

    /** Text attached to the element, or `false` to disable it. */
    var textContent: CustomTextContentOption?

    /** Clip path of the element, or `false` to disable it. */
    var clipPath: CustomClipPathOption?

    /** Whether the tooltip is disabled for this element. */
    var tooltipDisabled: Boolean?

    /**
     * Extra data of the element.
     *
     * Upstream is `Dictionary<unknown> & TransitionOptionMixin`: the transition members can be set
     * through the same object, but Kotlin cannot express a record with fixed members.
     */
    var extra: Record<String, JsAny>?

    /** Callback invoked on every animation frame with the base transition API. */
    var during: ((TransitionBaseDuringAPI) -> Unit)?

    /** Animation used when the element enters. */
    var enterAnimation: ElementAnimationOption?

    /** Animation used when the element is updated. */
    var updateAnimation: ElementAnimationOption?

    /** Animation used when the element leaves. */
    var leaveAnimation: ElementAnimationOption?

    /** Horizontal position. */
    var x: Double?

    /** Vertical position. */
    var y: Double?

    /** Horizontal scale. */
    var scaleX: Double?

    /** Vertical scale. */
    var scaleY: Double?

    /** Rotation in radians. */
    var rotation: Double?

    /** Horizontal origin of the transform. */
    var originX: Double?

    /** Vertical origin of the transform. */
    var originY: Double?

    /** Horizontal anchor of the transform. */
    var anchorX: Double?

    /** Vertical anchor of the transform. */
    var anchorY: Double?

    /** Horizontal skew. */
    var skewX: Double?

    /** Vertical skew. */
    var skewY: Double?

    /** Whether the element ignores mouse events. */
    var silent: Boolean?

    /** Whether the element is ignored by the layout. */
    var ignore: Boolean?

    /** Placement of the text attached to the element. */
    var textConfig: ElementTextConfig?

    /**
     * Focus behaviour of the root element.
     *
     * Upstream only declares this on `CustomRootElementOption`, which intersects the element
     * union with these fields; it is modelled on the base so that every element type can carry it.
     */
    var focus: CustomElementFocus?

    /** Scope of the blur effect of the root element. */
    var blurScope: BlurScope?

    /** Whether the emphasis state of the root element is disabled. */
    var emphasisDisabled: Boolean?
}

/**
 * A displayable custom element: a path, an image or a text.
 *
 * Upstream `CustomDisplayableOption`.
 */
external interface CustomDisplayableOption : CustomBaseElementOption {
    /** z-level of the element. */
    var zlevel: Double?

    /** z-index of the element. */
    var z: Double?

    /** z-index of the element inside its z-level. */
    var z2: Double?

    /** Whether the element is invisible. */
    var invisible: Boolean?

    /** Style of the element. */
    var style: ZRStyleProps?

    /** Callback invoked on every animation frame with the displayable transition API. */
    @JsName("during")
    var typedDuring: ((TransitionDuringAPI) -> Unit)?

    /**
     * Emphasis style of the element.
     *
     * Upstream `styleEmphasis`, kept as an alias for backwards compatibility.
     */
    var styleEmphasis: CustomStyleOption?

    /** Style of the emphasis state. */
    var emphasis: CustomDisplayableOptionOnState?

    /** Style of the blur state. */
    var blur: CustomDisplayableOptionOnState?

    /** Style of the select state. */
    var select: CustomDisplayableOptionOnState?
}

/**
 * Any element of a custom element tree.
 *
 * Upstream `CustomElementOption`, a union of [CustomPathOption], [CustomImageOption],
 * [CustomTextOption], [CustomCompoundPathOption] and [CustomGroupOption]. All of them extend
 * [CustomBaseElementOption], which is therefore used as the union type.
 */
typealias CustomElementOption = CustomBaseElementOption

/**
 * Element returned by `renderItem`.
 *
 * Upstream `CustomRootElementOption`: [CustomElementOption] intersected with the focus fields,
 * which [CustomBaseElementOption] also carries.
 */
typealias CustomRootElementOption = CustomBaseElementOption

/**
 * A group element: `type` must be `"group"`.
 *
 * Upstream `CustomGroupOption`.
 */
external interface CustomGroupOption : CustomBaseElementOption, TransitionOptionMixin {
    /** Width of the group. */
    var width: Double?

    /** Height of the group. */
    var height: Double?

    /** Whether the children are diffed by name instead of by index. */
    var diffChildrenByName: Boolean?

    /** Child elements; required. */
    var children: Array<CustomElementOption>

    /**
     * How the children are merged when the option is updated: `false`, `"byName"` or `"byIndex"`.
     *
     * Upstream property is `$mergeChildren`.
     */
    @JsName("\$mergeChildren")
    var mergeChildren: CustomMergeChildrenOption?

    /** Keyframe animation, one per property group. */
    var keyframeAnimation: ElementKeyframeAnimationOptionOrArray?
}

/**
 * A path element: `type` is one of the built-in shapes (`"circle"`, `"rect"`, `"sector"`,
 * `"polygon"`, `"polyline"`, `"line"`, `"arc"`, `"bezierCurve"`, `"ring"`, `"ellipse"`,
 * `"compoundPath"`) or `"path"` for SVG path data.
 *
 * Upstream `CustomPathOption`, a union of one interface per shape. The shape members are all part
 * of [ZRShapeProps], so a single interface represents them.
 */
external interface CustomPathOption : CustomDisplayableOption, TransitionOptionMixin {
    /** Whether consecutive paths with the same style are batched. */
    var autoBatch: Boolean?

    /** Whether the shape morphs when the element type changes. */
    var morph: Boolean?

    /** Shape of the path. */
    var shape: ZRShapeProps?

    /** Keyframe animation, one per property group. */
    var keyframeAnimation: ElementKeyframeAnimationOptionOrArray?
}

/**
 * An image element: `type` must be `"image"`.
 *
 * Upstream `CustomImageOption`.
 */
external interface CustomImageOption : CustomDisplayableOption, TransitionOptionMixin {
    /** Keyframe animation, one per property group. */
    var keyframeAnimation: ElementKeyframeAnimationOptionOrArray?
}

/**
 * A text element: `type` must be `"text"`.
 *
 * Upstream `CustomTextOption`.
 */
external interface CustomTextOption : CustomDisplayableOption, TransitionOptionMixin {
    /** Keyframe animation, one per property group. */
    var keyframeAnimation: ElementKeyframeAnimationOptionOrArray?
}

/**
 * A compound path element: `type` must be `"compoundPath"`.
 *
 * Upstream `CustomCompoundPathOption`.
 */
external interface CustomCompoundPathOption : CustomDisplayableOption, TransitionOptionMixin {
    /** Shape of the compound path. */
    var shape: ZRShapeProps?

    /** Keyframe animation, one per property group. */
    var keyframeAnimation: ElementKeyframeAnimationOptionOrArray?
}
