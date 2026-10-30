package io.github.shijianjs.kmpechartsjson

import kotlin.test.Test
import kotlin.test.assertEquals

/** Field-level serialization coverage for [Geos]. */
class GeosFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun geoOptionFields() {
        val value = GeoOption()
        value.id = "s"
        value.map = "s"
        value.roam = BooleanOrString.of(true)
        value.center = listOf(num(1.0))
        value.zoom = 1.0
        value.scaleLimit = ScaleLimit()
        value.nameMap = mapOf("k" to "s")
        value.selectedMode = BooleanOrString.of(true)
        value.label = LabelOption()
        value.itemStyle = ItemStyleOption()
        value.emphasis = EmphasisOption()
        value.select = EmphasisOption()
        value.blur = EmphasisOption()
        value.regions = listOf(GeoRegion())
        value.silent = true
        value.aspectScale = 1.0
        value.boundingCoords = listOf(listOf(1.0))
        value.layoutCenter = listOf(num(1.0))
        value.layoutSize = num(1.0)
        value.z = 1.0
        value.zlevel = 1.0
        value.left = num(1.0)
        value.top = num(1.0)
        value.right = num(1.0)
        value.bottom = num(1.0)
        assertEquals(
            setOf(
                "id",
                "map",
                "roam",
                "center",
                "zoom",
                "scaleLimit",
                "nameMap",
                "selectedMode",
                "label",
                "itemStyle",
                "emphasis",
                "select",
                "blur",
                "regions",
                "silent",
                "aspectScale",
                "boundingCoords",
                "layoutCenter",
                "layoutSize",
                "z",
                "zlevel",
                "left",
                "top",
                "right",
                "bottom",
            ),
            value.encodeWith(GeoOption.serializer()).keys,
        )
    }

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun geoRegionFields() {
        val value = GeoRegion()
        value.name = "s"
        value.selected = true
        value.itemStyle = ItemStyleOption()
        value.label = LabelOption()
        value.emphasis = EmphasisOption()
        value.select = EmphasisOption()
        value.blur = EmphasisOption()
        assertEquals(
            setOf(
                "name",
                "selected",
                "itemStyle",
                "label",
                "emphasis",
                "select",
                "blur",
            ),
            value.encodeWith(GeoRegion.serializer()).keys,
        )
    }
}
