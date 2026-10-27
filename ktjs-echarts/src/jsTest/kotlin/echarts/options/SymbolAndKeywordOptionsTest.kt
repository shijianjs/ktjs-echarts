package echarts.options

import echarts.AxisPositions
import echarts.FunnelAligns
import echarts.FunnelSorts
import echarts.GraphInitLayouts
import echarts.GraphLayouts
import echarts.PictorialBarSymbolPositions
import echarts.PictorialBarSymbolRepeatDirections
import echarts.RadarShapes
import echarts.SeriesLayoutBys
import echarts.TooltipTriggerOns
import echarts.TreeEdgeShapes
import echarts.TreeLayouts
import echarts.createChart
import echarts.destroyChart
import js.objects.Record
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.jsTypeOf
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Tests for the series options that were remodelled after the upstream `.d.ts`: the symbol members
 * (which now use the `SymbolShape` / `SymbolSize` / `SymbolRotate` / `SymbolOffset` unions through
 * `SymbolOptionMixin`) and the keyword fields that became string-literal unions.
 */
class SymbolAndKeywordOptionsTest {
    @Test
    fun symbolMembersAcceptBothLiteralsAndCallbacks() {
        val series = lineSeries {
            symbol = SymbolShape("circle")
            symbolSize = SymbolSize(arrayOf(10.0, 12.0))
            symbolRotate = SymbolRotate(45.0)
            symbolOffset = SymbolOffset(arrayOf(StringOrDouble("10%"), StringOrDouble(4.0)))
        }

        assertEquals("circle", series.symbol!!.unsafeCast<String>())
        assertEquals(2, series.symbolSize!!.unsafeCast<Array<Double>>().size)
        assertEquals(45.0, series.symbolRotate!!.unsafeCast<Double>())
        assertEquals(2, series.symbolOffset!!.unsafeCast<Array<StringOrDouble>>().size)

        val callbacks = scatterSeries {
            symbol = SymbolShape { value, params -> "${value.unsafeCast<String>()}-${params.dataIndex}" }
            symbolSize = SymbolSize { _, _ -> DoubleOrDoubleArray(8.0) }
            symbolRotate = SymbolRotate { _, _ -> 90.0 }
            symbolOffset = SymbolOffset { _, _ -> StringOrDoubleOrArray(2.0) }
        }

        assertEquals("function", jsTypeOf(callbacks.symbol))
        assertEquals("function", jsTypeOf(callbacks.symbolSize))
        assertEquals("function", jsTypeOf(callbacks.symbolRotate))
        assertEquals("function", jsTypeOf(callbacks.symbolOffset))
    }

    @Test
    fun symbolMembersAreSharedWithTheItemTypes() {
        val node = unsafeJso<GraphNodeItemOption> {
            name = "a"
            symbol = SymbolShape("rect")
            symbolSize = SymbolSize(16.0)
            symbolRotate = SymbolRotate(30.0)
            symbolOffset = SymbolOffset(2.0)
        }

        assertEquals("rect", node.symbol!!.unsafeCast<String>())
        assertEquals(16.0, node.symbolSize!!.unsafeCast<Double>())
        assertEquals(30.0, node.symbolRotate!!.unsafeCast<Double>())
        assertEquals(2.0, node.symbolOffset!!.unsafeCast<Double>())
    }

    @Test
    fun markOptionsSplitTheSharedAndThePerMarkMembers() {
        val markPoint = unsafeJso<MarkPointOption> {
            silent = true
            precision = 2.0
            animation = false
            symbol = SymbolShape("pin")
            itemStyle = unsafeJso<ItemStyleOption> { color = zrender.ZRColor("#d9534f") }
            data = arrayOf(unsafeJso<MarkPointDataItemOption> { name = "max"; type = echarts.MarkerStatisticTypes.max })
        }
        assertTrue(markPoint.silent!!)
        assertFalse(markPoint.animation!!)
        assertEquals("pin", markPoint.symbol!!.unsafeCast<String>())
        assertEquals(1, markPoint.data?.size)
        assertNotNull(markPoint.itemStyle)

        val markLine = unsafeJso<MarkLineOption> {
            symbol = StringOrStringArray(arrayOf("circle", "rect"))
            symbolSize = DoubleOrDoubleArray(arrayOf(6.0, 6.0))
            label = unsafeJso<LabelOption> { show = false }
            data = arrayOf()
        }
        assertEquals(2, markLine.symbol!!.unsafeCast<Array<String>>().size)
        assertEquals(2, markLine.symbolSize!!.unsafeCast<Array<Double>>().size)
        assertFalse(markLine.label!!.show!!)

        // A mark area has no symbol members upstream.
        val markArea = unsafeJso<MarkAreaOption> { silent = true }
        assertTrue(markArea.silent!!)
    }

    @Test
    fun pictorialBarKeywordFieldsAreLiterals() {
        val series = pictorialBarSeries {
            symbolSize = StringOrDoubleOrArray(arrayOf(StringOrDouble("10%"), StringOrDouble(20.0)))
            symbolOffset = StringOrDoubleOrArray(0.0)
            symbolRotate = 15.0
            symbolPosition = PictorialBarSymbolPositions.end
            symbolRepeatDirection = PictorialBarSymbolRepeatDirections.start
            symbolBoundingData = DoubleOrDoubleArray(arrayOf(10.0, 20.0))
            symbolMargin = StringOrDoubleOrArray("10%")
        }

        assertEquals(2, series.symbolSize!!.unsafeCast<Array<StringOrDouble>>().size)
        assertEquals("end", series.symbolPosition!!.unsafeCast<String>())
        assertEquals("start", series.symbolRepeatDirection!!.unsafeCast<String>())
        assertEquals(2, series.symbolBoundingData!!.unsafeCast<Array<Double>>().size)
    }

    @Test
    fun funnelRadarAndLayoutFieldsAreLiterals() {
        val funnel = funnelSeries {
            sort = FunnelSorts.ascending
            funnelAlign = FunnelAligns.center
        }
        assertEquals("ascending", funnel.sort!!.unsafeCast<String>())
        assertEquals("center", funnel.funnelAlign!!.unsafeCast<String>())

        val radar = unsafeJso<RadarOption> { shape = RadarShapes.circle }
        assertEquals("circle", radar.shape!!.unsafeCast<String>())

        val graph = graphSeries {
            layout = GraphLayouts.force
            force = unsafeJso<GraphForceOption> { initLayout = GraphInitLayouts.circular }
        }
        assertEquals("force", graph.layout!!.unsafeCast<String>())
        assertEquals("circular", graph.force!!.initLayout!!.unsafeCast<String>())

        val tree = treeSeries {
            layout = TreeLayouts.radial
            edgeShape = TreeEdgeShapes.curve
            edgeForkPosition = StringOrDouble(0.5)
        }
        assertEquals("radial", tree.layout!!.unsafeCast<String>())
        assertEquals("curve", tree.edgeShape!!.unsafeCast<String>())
        assertEquals(0.5, tree.edgeForkPosition!!.unsafeCast<Double>())

        assertEquals(SeriesLayoutBys.column, lineSeries { seriesLayoutBy = SeriesLayoutBys.column }.seriesLayoutBy)
        assertEquals(SeriesLayoutBys.row, barSeries { seriesLayoutBy = SeriesLayoutBys.row }.seriesLayoutBy)
    }

    @Test
    fun componentKeywordFieldsAreLiterals() {
        val option = unsafeJso<EChartsOption> {
            tooltip = one(unsafeJso<TooltipOption> { triggerOn = TooltipTriggerOns.mouseMoveClickWheel })
            xAxis = one(
                unsafeJso<XAxisOption> {
                    position = AxisPositions.bottom
                    axisLine = unsafeJso<AxisLineOption> { symbol = StringOrStringArray(arrayOf("none", "arrow")) }
                },
            )
            legend = one(
                unsafeJso<LegendOption> {
                    symbolRotate = StringOrDouble("inherit")
                    selectedMode = SelectionModes.multiple
                },
            )
        }

        assertEquals("mousemove|click|mousewheel", option.tooltip!![0].triggerOn!!.unsafeCast<String>())
        assertEquals("bottom", option.xAxis!![0].position!!.unsafeCast<String>())
        assertEquals(2, option.xAxis!![0].axisLine!!.symbol!!.unsafeCast<Array<String>>().size)
        assertEquals("inherit", option.legend!![0].symbolRotate!!.unsafeCast<String>())
        assertEquals("multiple", option.legend!![0].selectedMode!!.unsafeCast<String>())
    }

    @Test
    fun selectionModeAndSelectedMapAreSharedWithTheSeries() {
        val record: Record<String, Boolean> = unsafeJso<JsAny> { }.unsafeCast<Record<String, Boolean>>()
        val series = pieSeries {
            selectedMode = SelectionModes.series
            selectedMap = SelectedMap(record)
        }

        assertEquals("series", series.selectedMode!!.unsafeCast<String>())
        assertEquals("object", jsTypeOf(series.selectedMap))
        assertEquals("all", SelectedMap("all").unsafeCast<String>())
        assertEquals("single", SelectionModes.single.unsafeCast<String>())
        assertEquals("multiple", SelectionModes.multiple.unsafeCast<String>())
    }

    @Test
    fun gaugeDataItemCarriesThePerItemConfiguration() {
        val series = gaugeSeries {
            title = unsafeJso<GaugeTitleOption> {
                offsetCenter = arrayOf(StringOrDouble(0.0), StringOrDouble("-30%"))
            }
            detail = unsafeJso<GaugeDetailOption> {
                offsetCenter = arrayOf(StringOrDouble(0.0), StringOrDouble("40%"))
            }
            data = items(
                unsafeJso<GaugeDataItemOption> {
                    name = OptionId("cpu")
                    value = OptionDataValue(72.0)
                    pointer = unsafeJso<GaugePointerOption> { show = false }
                    progress = unsafeJso<GaugeProgressOption> { show = true }
                },
            )
        }

        assertEquals(2, series.title!!.offsetCenter?.size)
        assertEquals("40%", series.detail!!.offsetCenter!![1].unsafeCast<String>())
        val item = series.data!![0].unsafeCast<GaugeDataItemOption>()
        assertEquals("cpu", item.name!!.unsafeCast<String>())
        assertFalse(item.pointer!!.show!!)
        assertTrue(item.progress!!.show!!)
    }

    @Test
    fun themeRiverDataItemIsAThreeElementTuple() {
        val item = themeRiverDataItem(
            OptionDataValue("2026-01-01"),
            OptionDataValue(10.0),
            OptionDataValue("band"),
        )
        val values = item.unsafeCast<Array<Any>>()

        assertEquals(3, values.size)
        assertEquals("2026-01-01", values[0].unsafeCast<String>())
        assertEquals(10.0, values[1].unsafeCast<Double>())
        assertEquals("band", values[2].unsafeCast<String>())

        val series = themeRiverSeries {
            boundaryGap = arrayOf(StringOrDouble(0.0), StringOrDouble(0.0))
            data = arrayOf(OptionDataItem(item))
        }
        assertEquals(1, series.data?.size)
        assertEquals(2, series.boundaryGap?.size)
    }
}

/**
 * End-to-end check that the keyword and symbol values survive a real chart.
 */
class SymbolAndKeywordChartTest {
    @Test
    fun chartKeepsTheSymbolAndKeywordValues() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    tooltip = one(unsafeJso<TooltipOption> { trigger = echarts.TooltipTriggers.axis })
                    xAxis = one(
                        unsafeJso<XAxisOption> {
                            type = echarts.AxisTypes.category
                            position = AxisPositions.bottom
                            data = strings("a", "b")
                        },
                    )
                    yAxis = one(unsafeJso<YAxisOption> { type = echarts.AxisTypes.value })
                    series = arrayOf<SeriesOption>(
                        lineSeries {
                            data = numbers(1.0, 2.0)
                            symbol = SymbolShape("rect")
                            symbolSize = SymbolSize(20.0)
                            symbolRotate = SymbolRotate(30.0)
                        },
                        pictorialBarSeries {
                            data = numbers(1.0, 2.0)
                            symbolPosition = PictorialBarSymbolPositions.end
                        },
                    )
                },
            )

            val stored = chart.getOption().unsafeCast<EChartsOption>()
            val line = stored.series!![0].unsafeCast<LineSeriesOption>()
            assertEquals("rect", line.symbol!!.unsafeCast<String>())
            assertEquals(20.0, line.symbolSize!!.unsafeCast<Double>())
            assertEquals(30.0, line.symbolRotate!!.unsafeCast<Double>())
            assertEquals("bottom", stored.xAxis!![0].position!!.unsafeCast<String>())

            val pictorial = stored.series!![1].unsafeCast<PictorialBarSeriesOption>()
            assertEquals("end", pictorial.symbolPosition!!.unsafeCast<String>())
        } finally {
            destroyChart(chart, container)
        }
    }
}
