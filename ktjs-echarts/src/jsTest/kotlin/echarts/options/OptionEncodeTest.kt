package echarts.options

import echarts.AxisTypes
import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.js
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tests for [OptionEncode], the mapping between a series' data dimensions and the roles it uses.
 *
 * Upstream declares the named roles in `src/util/types.ts`; the wrapper repeats them on the
 * interface because Kotlin external interfaces cannot carry an index signature. The property names
 * are the JavaScript ones ECharts reads, so a typo silently disables a role instead of failing to
 * compile — these tests read the names back through raw JavaScript.
 */
class OptionEncodeTest {
    /** Reads `encode[key]` through JavaScript, bypassing the Kotlin declaration being tested. */
    private fun rawName(encode: OptionEncode, key: String): String? =
        js("(encode, key) => encode[key]")(encode, key) as String?

    @Test
    fun namedRolesKeepTheirUpstreamPropertyNames() {
        val encode =
            unsafeJso<OptionEncode> {
                x = OptionEncodeValue(DimensionLoose("x"))
                y = OptionEncodeValue(DimensionLoose("y"))
                itemGroupId = OptionEncodeValue(DimensionLoose("groupId"))
                itemChildGroupId = OptionEncodeValue(DimensionLoose("childGroupId"))
            }

        assertEquals("x", rawName(encode, "x"))
        assertEquals("y", rawName(encode, "y"))
        assertEquals("groupId", rawName(encode, "itemGroupId"))
        assertEquals("childGroupId", rawName(encode, "itemChildGroupId"))
    }

    @Test
    fun rolesAcceptSeveralDimensions() {
        val encode =
            unsafeJso<OptionEncode> {
                itemGroupId = OptionEncodeValue(arrayOf(DimensionLoose("groupId"), DimensionLoose(3.0)))
            }

        val group = js("(encode) => encode.itemGroupId")(encode) as Array<*>
        assertEquals(2, group.size)
        assertEquals("groupId", group[0] as String)
        assertEquals(3.0, group[1] as Double)
    }

    @Test
    fun aChartKeepsTheGroupDimensions() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
                    yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                    series =
                        one(
                            barSeries {
                                dimensions =
                                    arrayOf(
                                        DimensionDefinitionLoose("x"),
                                        DimensionDefinitionLoose("y"),
                                        DimensionDefinitionLoose("groupId"),
                                        DimensionDefinitionLoose("childGroupId"),
                                    )
                                encode =
                                    unsafeJso {
                                        x = OptionEncodeValue(DimensionLoose("x"))
                                        y = OptionEncodeValue(DimensionLoose("y"))
                                        itemGroupId = OptionEncodeValue(DimensionLoose("groupId"))
                                        itemChildGroupId = OptionEncodeValue(DimensionLoose("childGroupId"))
                                    }
                                typedData =
                                    dataItems(
                                        BarDataItem(
                                            arrayOf<OptionDataValue?>(
                                                OptionDataValue("a"),
                                                OptionDataValue(1.0),
                                                OptionDataValue("level"),
                                                OptionDataValue("next"),
                                            ),
                                        ),
                                    )
                            },
                        )
                },
            )

            val stored = assertNotNull(chart.getOption().series?.first()?.encode)
            assertNotNull(stored.itemGroupId, "the group dimension survives a setOption round trip")
            assertNotNull(stored.itemChildGroupId, "the child group dimension survives a setOption round trip")
        } finally {
            destroyChart(chart, container)
        }
    }
}
