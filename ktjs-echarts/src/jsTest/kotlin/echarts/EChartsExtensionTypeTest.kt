package echarts

import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Covers the registration and extension API spread across `EChartsExtensionTypes.kt` and
 * `EChartsExtension.kt`: the external transform flag, the coordinate-system creator callbacks, the
 * whole `StageHandler` callback set, and the read/write model properties on `ComponentModel`,
 * `SeriesModel`, `ChartView` and `SeriesData`. Every field is set through [unsafeJso] and read back
 * so the typed member keeps mapping to the exact JavaScript key; callback fields are asserted
 * non-null rather than invoked.
 */
class EChartsExtensionTypeTest {

    @Test
    fun externalTransformAndCoordinateSystemCreatorRoundTrip() {
        val transform = unsafeJso<ExternalTransform> {
            isBuiltIn = true
        }
        assertEquals(true, transform.isBuiltIn)

        val creator = unsafeJso<CoordinateSystemCreator> {
            create = { _, _ -> emptyArray() }
            getDimensionsInfo = { emptyArray() }
        }
        assertNotNull(creator.create)
        assertNotNull(creator.getDimensionsInfo)
    }

    @Test
    fun stageHandlerFieldsRoundTrip() {
        val handler = unsafeJso<StageHandler> {
            createOnAllSeries = true
            seriesType = "series-line"
            performRawSeries = true
            dirtyOnOverallProgress = true
            getTargetSeries = { _, _ -> "none".unsafeCast<JsAny>() }
            plan = { _, _, _, _ -> "none".unsafeCast<JsAny>() }
            overallReset = { _, _, _ -> }
        }
        assertEquals(true, handler.createOnAllSeries)
        assertEquals("series-line", handler.seriesType)
        assertEquals(true, handler.performRawSeries)
        assertEquals(true, handler.dirtyOnOverallProgress)
        assertNotNull(handler.getTargetSeries)
        assertNotNull(handler.plan)
        assertNotNull(handler.overallReset)
    }

    @Test
    fun modelAndDataWriteablePropertiesRoundTrip() {
        val component = unsafeJso<ComponentModel> { preventAutoZ = true }
        assertEquals(true, component.preventAutoZ)

        val series = unsafeJso<SeriesModel> { defaultSymbol = "circle" }
        assertEquals("circle", series.defaultSymbol)

        val view = unsafeJso<ChartView> { ignoreLabelLineUpdate = true }
        assertEquals(true, view.ignoreLabelLineUpdate)

        val data = unsafeJso<SeriesData> {
            dataType = "node"
            hasItemOption = true
        }
        assertEquals("node", data.dataType)
        assertEquals(true, data.hasItemOption)
    }
}
