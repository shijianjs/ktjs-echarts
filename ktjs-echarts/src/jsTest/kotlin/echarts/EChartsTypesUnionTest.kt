package echarts

import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame

/**
 * Covers the opaque union factories and the `ActionInfo` / `ActionRefineEventResult` members of
 * `EChartsTypes.kt`. Each union factory is a no-op cast, so wrapping and unwrapping with
 * [unsafeCast] proves the Kotlin type keeps mapping to the exact JavaScript value; the
 * `ActionInfo` booleans and the refined-event content are set through [unsafeJso] and read back.
 */
class EChartsTypesUnionTest {

    @Test
    fun eventQueryFactoriesWrapNameAndFinder() {
        val byName = EventQuery("datazoom")
        assertEquals("datazoom", byName.unsafeCast<String>())

        val finder = unsafeJso<ModelFinderObject> { seriesIndex = 0.0 }
        assertSame(finder, EventQuery(finder).unsafeCast<ModelFinderObject>())
    }

    @Test
    fun transitionFinderAndSetOptionTransitionUnionsWrapTheirShapes() {
        val finder = unsafeJso<TransitionSeriesFinder> { seriesId = "s0" }
        assertSame(finder, TransitionSeriesFinderList(finder).unsafeCast<TransitionSeriesFinder>())

        val finderList = arrayOf(finder)
        assertEquals(1, TransitionSeriesFinderList(finderList).unsafeCast<Array<TransitionSeriesFinder>>().size)

        val item = unsafeJso<SetOptionTransitionItem> { }
        assertSame(item, SetOptionTransition(item).unsafeCast<SetOptionTransitionItem>())

        val itemList = arrayOf(item)
        assertEquals(1, SetOptionTransition(itemList).unsafeCast<Array<SetOptionTransitionItem>>().size)
    }

    @Test
    fun actionInfoAndRefineEventResultFieldsRoundTrip() {
        val info = ActionInfo("wrapper-test-action")
        info.publishNonRefinedEvent = true
        info.componentQuery = false
        assertEquals("wrapper-test-action", info.type)
        assertEquals(true, info.publishNonRefinedEvent)
        assertEquals(false, info.componentQuery)
        assertNull(info.refineEvent)

        val result = unsafeJso<ActionRefineEventResult> {
            eventContent = unsafeJso<ECEventData> { }
        }
        assertNotNull(result.eventContent)
    }
}
