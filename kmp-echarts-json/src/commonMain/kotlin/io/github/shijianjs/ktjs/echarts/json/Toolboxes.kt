package io.github.shijianjs.ktjs.echarts.json

import kotlinx.serialization.Serializable

/** Toolbox with the built-in features enabled. */
@Serializable
public class ToolboxOption {
    /** Component id used by cross references. */
    public var id: String? = null

    /** Whether the toolbox is shown. */
    public var show: Boolean? = null

    /** Layout direction. */
    public var orient: Orient? = null

    /** Size of one tool icon. */
    public var itemSize: Double? = null

    /** Gap between tools. */
    public var itemGap: Double? = null

    /** Whether tool titles are shown on hover. */
    public var showTitle: Boolean? = null

    /** Icon style of the tools. */
    public var iconStyle: ItemStyleOption? = null

    /** Emphasis state of the tools. */
    public var emphasis: EmphasisOption? = null

    /** z-order; larger values draw on top. */
    public var z: Double? = null

    /** Distance to the left edge. */
    public var left: NumberOrString? = null

    /** Distance to the top edge. */
    public var top: NumberOrString? = null

    /** Distance to the right edge. */
    public var right: NumberOrString? = null

    /** Distance to the bottom edge. */
    public var bottom: NumberOrString? = null

    /** Enabled features. */
    public var feature: ToolboxFeatureOption? = null
}

/** The built-in toolbox features. Set a feature to `null` to hide it. */
@Serializable
public class ToolboxFeatureOption {
    /** Save the chart as an image. */
    public var saveAsImage: ToolboxSaveAsImageFeature? = null

    /** Restore the option to its initial state. */
    public var restore: ToolboxFeature? = null

    /** Show the option data in a panel. */
    public var dataView: ToolboxDataViewFeature? = null

    /** Enable a data zoom. */
    public var dataZoom: ToolboxFeature? = null

    /** Switch between chart types. */
    public var magicType: ToolboxMagicTypeFeature? = null

    /** Toggle box selection. */
    public var brush: ToolboxFeature? = null
}

/** Shared configuration of a toolbox feature. */
@Serializable
public open class ToolboxFeature {
    /** Whether the feature is shown. */
    public var show: Boolean? = null

    /** Title shown on hover. */
    public var title: String? = null

    /** Icon path or image URL. */
    public var icon: String? = null

    /** Icon style. */
    public var iconStyle: ItemStyleOption? = null

    /** Emphasis state of the icon. */
    public var emphasis: EmphasisOption? = null

    /** Pixel ratio of the downloaded image. */
    public var pixelRatio: Double? = null
}

/** `saveAsImage` feature, which downloads the chart as an image. */
@Serializable
public class ToolboxSaveAsImageFeature : ToolboxFeature() {
    /** Image type: `"png"` or `"jpeg"`. */
    public var type: String? = null

    /** File name without extension. */
    public var name: String? = null

    /** Background color of the image. */
    public var backgroundColor: EChartsColor? = null

    /** Whether the chart background is included. */
    public var connectedBackgroundColor: EChartsColor? = null

    /** Components excluded from the image, for example `["toolbox"]`. */
    public var excludeComponents: List<String>? = null
}

/** `dataView` feature, which shows the current option data in an editable panel. */
@Serializable
public class ToolboxDataViewFeature : ToolboxFeature() {
    /** Whether the panel is read-only. */
    public var readOnly: Boolean? = null

    /** Background color of the panel. */
    public var backgroundColor: EChartsColor? = null

    /** Text color of the panel. */
    public var textColor: EChartsColor? = null

    /** Button color of the panel. */
    public var buttonColor: EChartsColor? = null

    /** Button text color of the panel. */
    public var buttonTextColor: EChartsColor? = null
}

/** `magicType` feature, which switches between chart types. */
@Serializable
public class ToolboxMagicTypeFeature : ToolboxFeature() {
    /** Chart types offered by the switch, for example `["line", "bar"]`. */
    public var type: List<SeriesType>? = null
}
