package echarts.options

import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.jsTypeOf
import kotlin.js.unsafeCast
import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for the accessibility decal declarations: the `aria.decals` list, the [DecalObject] pattern
 * fields and the per-item [DecalSetting].
 *
 * Upstream builds a `zrender` pattern from a [DecalObject] and passes `rotation` to it unchanged,
 * which the renderer later divides by `PI`, so the radian form is pinned here along with the union
 * branches a caller has to wrap explicitly.
 */
class AriaDecalOptionTest {
    private fun decal(): DecalObject =
        unsafeJso {
            symbol = StringOrStringArray(arrayOf("rect", "circle"))
            symbolSize = 0.5
            symbolKeepAspect = true
            color = "#999966"
            backgroundColor = "#eeeeee"
            dashArrayX = DecalDashArrayX(
                arrayOf(
                    DoubleOrDoubleArray(arrayOf(5.0, 5.0)),
                    DoubleOrDoubleArray(1.0),
                ),
            )
            dashArrayY = DoubleOrDoubleArray(arrayOf(4.0, 6.0))
            rotation = -PI / 4
            maxTileWidth = 40.0
            maxTileHeight = 40.0
        }

    @Test
    fun decalObjectKeepsItsPatternFields() {
        val pattern = decal()

        assertEquals(2, pattern.symbol!!.unsafeCast<Array<String>>().size)
        assertEquals(0.5, pattern.symbolSize)
        assertTrue(pattern.symbolKeepAspect!!)
        assertEquals("#999966", pattern.color)
        assertEquals("#eeeeee", pattern.backgroundColor)
        assertEquals(2, pattern.dashArrayX!!.unsafeCast<Array<JsAny>>().size)
        assertEquals(5.0, pattern.dashArrayX!!.unsafeCast<Array<Array<Double>>>()[0][0])
        assertEquals(1.0, pattern.dashArrayX!!.unsafeCast<Array<DoubleOrDoubleArray>>()[1].unsafeCast<Double>())
        assertEquals(6.0, pattern.dashArrayY!!.unsafeCast<Array<Double>>()[1])
        assertEquals(-PI / 4, pattern.rotation)
        assertEquals(40.0, pattern.maxTileWidth)
        assertEquals(40.0, pattern.maxTileHeight)
    }

    @Test
    fun decalListWrapsAPatternAndAPatternList() {
        val single = DecalList(decal())
        assertEquals("object", jsTypeOf(single))
        assertEquals("#999966", single.unsafeCast<DecalObject>().color)

        val many = DecalList(arrayOf(decal(), decal()))
        assertEquals("object", jsTypeOf(many))
        assertEquals(2, many.unsafeCast<Array<DecalObject>>().size)
    }

    @Test
    fun itemStyleDecalAcceptsAPatternOrNone() {
        val pattern = DecalSetting(decal())
        assertEquals("object", jsTypeOf(pattern))
        assertEquals("#999966", pattern.unsafeCast<DecalObject>().color)
        assertEquals("none", DecalSetting("none").unsafeCast<String>())

        val style = unsafeJso<ItemStyleOption> { decal = pattern }
        assertEquals(-PI / 4, style.decal!!.unsafeCast<DecalObject>().rotation)

        val disabled = unsafeJso<AreaStyleOption> { decal = DecalSetting("none") }
        assertEquals("none", disabled.decal!!.unsafeCast<String>())
    }

    @Test
    fun ariaDecalIsAcceptedByAChart() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    aria =
                        unsafeJso<AriaOption> {
                            enabled = true
                            label = unsafeJso<AriaLabelOption> { description = "A decalled pie" }
                            decal =
                                unsafeJso<AriaDecalOption> {
                                    show = true
                                    decals = DecalList(arrayOf(decal(), decal()))
                                }
                        }
                    series = one(pieSeries { typedData = numbers(3.0, 5.0) })
                },
            )

            // Upstream keeps `aria` as the single object it declares, unlike the components whose
            // stored option `getOption` normalises into an array.
            val stored = assertNotNull(chart.getOption().aria)
            assertTrue(stored.enabled!!)
            assertEquals("A decalled pie", stored.label!!.description)
            assertEquals(true, stored.decal!!.show)
            val decals = assertNotNull(stored.decal!!.decals).unsafeCast<Array<DecalObject>>()
            assertEquals(2, decals.size)
            assertEquals(-PI / 4, decals[0].rotation)
        } finally {
            destroyChart(chart, container)
        }
    }
}
