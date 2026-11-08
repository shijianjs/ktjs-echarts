package io.github.shijianjs.ktjs.echarts.json

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.json.JsonPrimitive

/** Field-level serialization coverage for [Datasets]. */
class DatasetsFieldsTest {

    /** Every declared field is written and serialized under its own key. */
    @Test
    fun datasetOptionFields() {
        val value = DatasetOption()
        value.id = "s"
        value.source = datasetRows(dataValues(1.0))
        value.dimensions = listOf(JsonPrimitive("x"))
        value.seriesLayoutBy = "s"
        value.sourceHeader = true
        value.fromDatasetIndex = 1
        value.fromDatasetId = "s"
        value.fromTransformResult = 1
        value.transform = JsonPrimitive("x")
        assertEquals(
            setOf(
                "id",
                "source",
                "dimensions",
                "seriesLayoutBy",
                "sourceHeader",
                "fromDatasetIndex",
                "fromDatasetId",
                "fromTransformResult",
                "transform",
            ),
            value.encodeWith(DatasetOption.serializer()).keys,
        )
    }
}
