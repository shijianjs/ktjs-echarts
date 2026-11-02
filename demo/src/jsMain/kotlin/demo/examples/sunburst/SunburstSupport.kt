package demo.examples.sunburst

import echarts.EmphasisFocuses
import echarts.options.EmphasisOption
import echarts.options.LabelRotates
import echarts.options.StringOrDouble
import echarts.options.StringOrDoubleOrArray
import echarts.options.SunburstLabelOption
import js.objects.unsafeJso
import kotlin.js.js
import demo.examples.funnel.labels

/** `label: { rotate: 'radial' }` of the examples that only rotate the labels. */
internal fun radialLabel(): SunburstLabelOption = unsafeJso { typedRotate = LabelRotates.radial }

/** `emphasis: { focus: 'ancestor' }`, the emphasis state of the data-driven variants. */
internal fun ancestorEmphasis(): EmphasisOption = unsafeJso { focus = EmphasisFocuses.ancestor }

/** Radius of a sunburst as an `[inner, outer]` pair. */
internal fun radius(inner: StringOrDouble, outer: StringOrDouble): StringOrDoubleOrArray =
    StringOrDoubleOrArray(arrayOf(inner, outer))
