package echartsgl.options

import echarts.options.LabelOption
import echartsgl.GlColor

/*
 * Options shared by the `echarts-gl` series.
 *
 * `echarts-gl` ships no TypeScript declarations; these interfaces are written from the `option-gl`
 * documentation and the `defaultOption` of the series models.
 */

/**
 * Label of a GL series or data item.
 *
 * `echarts-gl` 2.x draws its labels with the label API of ECharts 6, so the text properties
 * (`color`, `fontSize`, `borderWidth`, ...) sit directly on the label, exactly like the 2D
 * [LabelOption]. It also keeps the `distance` and `position` members of that API.
 */
typealias GlLabelOption = LabelOption

/**
 * Style of a GL graphic, such as a bar, a symbol or a surface patch.
 */
external interface GlItemStyleOption {
    /** Fill color. See [GlColor][echartsgl.GlColor] for the HDR array form. */
    var color: GlColor?

    /** Opacity, `0..1`. */
    var opacity: Double?

    /** Border color of a scatter symbol. */
    var borderColor: GlColor?

    /** Border width of a scatter symbol. */
    var borderWidth: Double?
}

/**
 * Emphasis (hover) state of a GL series or data item.
 */
external interface GlEmphasisOption {
    /** Whether the emphasis state is disabled. */
    var disabled: Boolean?

    /** Label style in the emphasis state. */
    var label: GlLabelOption?

    /** Style of the graphic in the emphasis state. */
    var itemStyle: GlItemStyleOption?
}
