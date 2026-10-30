package io.github.shijianjs.kmpechartsjson

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

/** Accessibility settings. */
@Serializable
public class AriaOption {
    /** Whether the accessibility description is generated. */
    public var enabled: Boolean? = null

    /** Description generation settings. */
    public var label: AriaLabelOption? = null

    /** Decal pattern applied to series, which helps distinguish them without color. */
    public var decal: AriaDecalOption? = null
}

/** Text generation of the accessibility label. */
@Serializable
public class AriaLabelOption {
    /** Whether the description is generated. */
    public var enabled: Boolean? = null

    /** Default description of the whole chart. */
    public var description: String? = null

    /** Per-component description overrides. */
    public var general: JsonObject? = null
}

/** Decal pattern applied to all series by `aria`. */
@Serializable
public class AriaDecalOption {
    /** Whether decals are applied. */
    public var show: Boolean? = null

    /** Decals in order; they are assigned to series one after another. */
    public var decals: List<JsonElement>? = null
}
