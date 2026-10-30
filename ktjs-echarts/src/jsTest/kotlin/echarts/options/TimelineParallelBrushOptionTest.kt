package echarts.options

import echarts.TimelineControlPositions
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import zrender.ZRColor

/**
 * Tests for the `timeline`, `parallel` / `parallelAxis` and `brush` structural options.
 *
 * These cover the parts of `EChartsOption.kt` and `ComponentOptions.kt` that go beyond layout and
 * styling: the play controls and playback knobs of [TimelineOption], the expandable-axis window of
 * [ParallelOption] and the selection styling of [BrushOption]. Each case builds the option through
 * [unsafeJso] and reads every field back, so the typed property keeps mapping to the exact JavaScript
 * key and the union wrappers accept their forms.
 */
class TimelineParallelBrushOptionTest {
    @Test
    fun timelinePlaybackAndControlStyleFieldsRoundTrip() {
        val controlStyle = unsafeJso<TimelineControlStyle> {
            showPlayBtn = true
            showPrevBtn = false
            showNextBtn = true
            playIcon = "M0,0L1,1"
            stopIcon = "M0,0H1"
            prevIcon = "M1,0L0,1"
            nextIcon = "M0,0L1,1"
            playBtnSize = StringOrDouble(20.0)
            stopBtnSize = StringOrDouble(18.0)
            nextBtnSize = StringOrDouble(16.0)
            prevBtnSize = StringOrDouble(14.0)
            position = TimelineControlPositions.right
        }
        val timeline = unsafeJso<TimelineOption> {
            rewind = true
            playbackSpeed = 2.0
            replaceMerge = StringOrStringArray("series")
            this.controlStyle = controlStyle
        }

        assertEquals(true, timeline.rewind)
        assertEquals(2.0, timeline.playbackSpeed)
        assertEquals("series", timeline.replaceMerge!!.unsafeCast<String>())
        assertTrue(timeline.controlStyle!!.showPlayBtn!!)
        assertEquals(false, timeline.controlStyle!!.showPrevBtn)
        assertTrue(timeline.controlStyle!!.showNextBtn!!)
        assertEquals("M0,0L1,1", timeline.controlStyle!!.playIcon)
        assertEquals("M0,0H1", timeline.controlStyle!!.stopIcon)
        assertEquals("M1,0L0,1", timeline.controlStyle!!.prevIcon)
        assertEquals("M0,0L1,1", timeline.controlStyle!!.nextIcon)
        assertEquals(20.0, timeline.controlStyle!!.playBtnSize!!.unsafeCast<Double>())
        assertEquals(18.0, timeline.controlStyle!!.stopBtnSize!!.unsafeCast<Double>())
        assertEquals(16.0, timeline.controlStyle!!.nextBtnSize!!.unsafeCast<Double>())
        assertEquals(14.0, timeline.controlStyle!!.prevBtnSize!!.unsafeCast<Double>())
        assertEquals("right", timeline.controlStyle!!.position!!.unsafeCast<String>())
    }

    @Test
    fun parallelExpandWindowAndAxisDefaultRoundTrip() {
        val parallelAxisDefault = unsafeJso<ParallelAxisOption> {
            parallelIndex = 0.0
            dim = IndexOrIndexArray(arrayOf(0.0, 1.0))
            areaSelectStyle = unsafeJso<AreaSelectStyleOption> {
                width = 20.0
                color = ZRColor("rgba(0,0,255,0.2)")
            }
        }
        val parallel = unsafeJso<ParallelOption> {
            axisExpandable = true
            axisExpandCenter = 2.0
            axisExpandCount = 5.0
            axisExpandWidth = 80.0
            axisExpandRate = 40.0
            axisExpandDebounce = 200.0
            axisExpandSlideTriggerArea = arrayOf(-0.15, 0.5, 0.2)
            axisExpandWindow = arrayOf(0.0, 3.0)
            this.parallelAxisDefault = parallelAxisDefault
        }

        assertTrue(parallel.axisExpandable!!)
        assertEquals(2.0, parallel.axisExpandCenter)
        assertEquals(5.0, parallel.axisExpandCount)
        assertEquals(80.0, parallel.axisExpandWidth)
        assertEquals(40.0, parallel.axisExpandRate)
        assertEquals(200.0, parallel.axisExpandDebounce)
        assertEquals(3, assertNotNull(parallel.axisExpandSlideTriggerArea).size)
        assertEquals(2, assertNotNull(parallel.axisExpandWindow).size)

        val stored = assertNotNull(parallel.parallelAxisDefault)
        assertEquals(0.0, stored.parallelIndex)
        assertEquals(2, stored.dim!!.unsafeCast<Array<Double>>().size)
        assertEquals(20.0, assertNotNull(stored.areaSelectStyle).width)
    }

    @Test
    fun brushSelectionFieldsRoundTrip() {
        val brush = unsafeJso<BrushOption> {
            transformable = false
            throttleDelay = 300.0
            removeOnClick = true
            inBrush = unsafeJso<VisualOptionUnit> { opacity = DoubleOrDoubleArray(1.0) }
            outOfBrush = unsafeJso<VisualOptionUnit> { opacity = DoubleOrDoubleArray(0.2) }
            brushLink = ModelFinderIndexQuery("all")
        }

        assertEquals(false, brush.transformable)
        assertEquals(300.0, brush.throttleDelay)
        assertTrue(brush.removeOnClick!!)
        assertEquals(1.0, assertNotNull(brush.inBrush).opacity!!.unsafeCast<Double>())
        assertEquals(0.2, assertNotNull(brush.outOfBrush).opacity!!.unsafeCast<Double>())
        assertEquals("all", brush.brushLink!!.unsafeCast<String>())
    }
}
