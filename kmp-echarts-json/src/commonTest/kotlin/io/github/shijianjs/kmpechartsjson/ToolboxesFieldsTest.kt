package io.github.shijianjs.kmpechartsjson

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [Toolboxes]. */
class ToolboxesFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun toolboxOptionFields() {
        val value = ToolboxOption()
        value.id = "s"
        value.show = true
        value.orient = Orient.Horizontal
        value.itemSize = 1.0
        value.itemGap = 1.0
        value.showTitle = true
        value.iconStyle = ItemStyleOption()
        value.emphasis = EmphasisOption()
        value.z = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        value.feature = ToolboxFeatureOption()
        assertEquals(
            setOf(
                "id",
                "show",
                "orient",
                "itemSize",
                "itemGap",
                "showTitle",
                "iconStyle",
                "emphasis",
                "z",
                "left",
                "top",
                "right",
                "bottom",
                "feature",
            ),
            value.encodeWith(ToolboxOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun toolboxFeatureOptionFields() {
        val value = ToolboxFeatureOption()
        value.saveAsImage = ToolboxSaveAsImageFeature()
        value.restore = ToolboxFeature()
        value.dataView = ToolboxDataViewFeature()
        value.dataZoom = ToolboxFeature()
        value.magicType = ToolboxMagicTypeFeature()
        value.brush = ToolboxFeature()
        assertEquals(
            setOf(
                "saveAsImage",
                "restore",
                "dataView",
                "dataZoom",
                "magicType",
                "brush",
            ),
            value.encodeWith(ToolboxFeatureOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun toolboxFeatureFields() {
        val value = ToolboxFeature()
        value.show = true
        value.title = "s"
        value.icon = "s"
        value.iconStyle = ItemStyleOption()
        value.emphasis = EmphasisOption()
        value.pixelRatio = 1.0
        assertEquals(
            setOf(
                "show",
                "title",
                "icon",
                "iconStyle",
                "emphasis",
                "pixelRatio",
            ),
            value.encodeWith(ToolboxFeature.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun toolboxSaveAsImageFeatureFields() {
        val value = ToolboxSaveAsImageFeature()
        value.type = "s"
        value.name = "s"
        value.backgroundColor = "red".asColor()
        value.connectedBackgroundColor = "red".asColor()
        value.excludeComponents = listOf("s")
        assertEquals(
            setOf(
                "type",
                "name",
                "backgroundColor",
                "connectedBackgroundColor",
                "excludeComponents",
            ),
            value.encodeWith(ToolboxSaveAsImageFeature.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun toolboxDataViewFeatureFields() {
        val value = ToolboxDataViewFeature()
        value.readOnly = true
        value.backgroundColor = "red".asColor()
        value.textColor = "red".asColor()
        value.buttonColor = "red".asColor()
        value.buttonTextColor = "red".asColor()
        assertEquals(
            setOf(
                "readOnly",
                "backgroundColor",
                "textColor",
                "buttonColor",
                "buttonTextColor",
            ),
            value.encodeWith(ToolboxDataViewFeature.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun toolboxMagicTypeFeatureFields() {
        val value = ToolboxMagicTypeFeature()
        value.type = listOf(SeriesType.Line)
        assertEquals(
            setOf(
                "type",
            ),
            value.encodeWith(ToolboxMagicTypeFeature.serializer()).keys,
        )
    }
}
