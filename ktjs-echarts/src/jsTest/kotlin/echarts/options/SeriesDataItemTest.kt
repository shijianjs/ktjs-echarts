package echarts.options

import echarts.AnimationTypes
import echarts.ClipWindows
import echarts.ColorMappingBys
import echarts.EChartsInstance
import echarts.FocusNodeAdjacencies
import echarts.MarkerRelativeTos
import echarts.MarkerStatisticTypes
import echarts.GraphLayouts
import echarts.MoveOverlaps
import echarts.NodeAligns
import echarts.NodeClickBehaviors
import echarts.Orients
import echarts.SeriesTypes
import echarts.SortOrders
import echarts.TreeLayouts
import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.jsTypeOf
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import zrender.ZRColor
import zrender.ZRFontWeight
import zrender.ZRShapeProps

/**
 * Tests for the data item types of the series options.
 *
 * Every data item type must stay assignable to the member that upstream declares it on, and the
 * values must survive on the JavaScript object. These options are plain JavaScript objects, so the
 * assertions read the values back through `unsafeCast` the same way a JavaScript caller would.
 */
class SeriesDataItemTest {
    @Test
    fun markPointDataCarriesTheStatisticAndThePosition() {
        val mark = unsafeJso<MarkPointOption> {
            data = arrayOf(
                unsafeJso<MarkPointDataItemOption> {
                    name = "max"
                    type = MarkerStatisticTypes.max
                    valueIndex = 1.0
                    coord = arrayOf(
                        ScaleDataValueOrMarkerStatisticType(ScaleDataValue("Mon")),
                        ScaleDataValueOrMarkerStatisticType(MarkerStatisticTypes.max),
                    )
                    symbolSize = SymbolSize(24.0)
                },
            )
        }

        val item = mark.data!![0]
        assertEquals("max", item.name)
        assertEquals("max", item.type!!.unsafeCast<String>())
        assertEquals(1.0, item.valueIndex)
        assertEquals(2, item.coord?.size)
        assertEquals("Mon", item.coord!![0].unsafeCast<String>())
        assertEquals(24.0, item.symbolSize!!.unsafeCast<Double>())
    }

    @Test
    fun markLineDataAcceptsASingleDefinitionOrAPair() {
        val mark = unsafeJso<MarkLineOption> {
            data = arrayOf(
                MarkLineDataItem(
                    unsafeJso<MarkLine1DDataItemOption> {
                        yAxis = StringOrDouble("average")
                        type = MarkerStatisticTypes.average
                        label = unsafeJso<MarkLineLabelOption> { formatter = LabelFormatter("avg") }
                    },
                ),
                MarkLineDataItem(
                    arrayOf(
                        unsafeJso<MarkLine2DDataItemDimOption> {
                            coord = arrayOf(
                                ScaleDataValueOrMarkerStatisticType(ScaleDataValue(0.0)),
                                ScaleDataValueOrMarkerStatisticType(ScaleDataValue(0.0)),
                            )
                        },
                        unsafeJso<MarkLine2DDataItemDimOption> {
                            coord = arrayOf(
                                ScaleDataValueOrMarkerStatisticType(ScaleDataValue(1.0)),
                                ScaleDataValueOrMarkerStatisticType(ScaleDataValue(1.0)),
                            )
                        },
                    ),
                ),
            )
        }

        assertEquals(2, mark.data?.size)
        val single = mark.data!![0].unsafeCast<MarkLine1DDataItemOption>()
        assertEquals(MarkerStatisticTypes.average, single.type)
        assertEquals("average", single.yAxis!!.unsafeCast<String>())

        val pair = mark.data!![1].unsafeCast<Array<MarkLine2DDataItemDimOption>>()
        assertEquals(2, pair.size)
        assertEquals(0.0, pair[0].coord!![0].unsafeCast<Double>())
    }

    @Test
    fun markAreaDataAcceptsASingleDefinitionOrAPair() {
        val mark = unsafeJso<MarkAreaOption> {
            data = arrayOf(
                MarkAreaDataItem(unsafeJso<MarkArea1DDataItemOption> { yAxis = 10.0; name = "band" }),
                MarkAreaDataItem(
                    arrayOf(
                        unsafeJso<MarkArea2DDataItemDimOption> { xAxis = ScaleDataValue("Mon") },
                        unsafeJso<MarkArea2DDataItemDimOption> { relativeTo = MarkerRelativeTos.coordinate },
                    ),
                ),
            )
        }

        assertEquals(2, mark.data?.size)
        val single = mark.data!![0].unsafeCast<MarkArea1DDataItemOption>()
        assertEquals("band", single.name)
        assertEquals(10.0, single.yAxis)

        val pair = mark.data!![1].unsafeCast<Array<MarkArea2DDataItemDimOption>>()
        assertEquals("Mon", pair[0].xAxis!!.unsafeCast<String>())
        assertEquals("coordinate", pair[1].relativeTo!!.unsafeCast<String>())
    }

    @Test
    fun gaugeColorStopIsATwoElementJavaScriptArray() {
        val stop = gaugeColorStop(0.75, "#91cc75")
        val values = stop.unsafeCast<Array<Any>>()

        assertEquals("object", jsTypeOf(stop))
        assertEquals(2, values.size)
        assertEquals(0.75, values[0].unsafeCast<Double>())
        assertEquals("#91cc75", values[1].unsafeCast<String>())

        val axisLine = unsafeJso<GaugeAxisLineOption> {
            roundCap = true
            lineStyle = unsafeJso<GaugeAxisLineStyleOption> {
                width = 14.0
                color = arrayOf(gaugeColorStop(0.3, "#5470c6"), gaugeColorStop(1.0, "#91cc75"))
            }
        }
        assertEquals(2, axisLine.lineStyle!!.color?.size)
        assertEquals(14.0, axisLine.lineStyle!!.width)
        assertEquals(0.3, axisLine.lineStyle!!.color!![0].unsafeCast<Array<Any>>()[0].unsafeCast<Double>())
    }

    @Test
    fun mapSeriesRegionsReuseTheRegionOption() {
        val series = mapSeries {
            map = "china"
            regions = arrayOf(
                unsafeJso<RegionOption> { name = "Guangdong"; selected = true; silent = false },
            )
        }

        assertEquals("Guangdong", series.regions!![0].name)
        assertTrue(series.regions!![0].selected!!)
    }

    @Test
    fun graphSeriesCarriesTypedCategoriesNodesAndEdges() {
        val series = graphSeries {
            layout = GraphLayouts.force
            categories = arrayOf(
                unsafeJso<GraphCategoryItemOption> { name = "core" },
                unsafeJso<GraphCategoryItemOption> { name = "shell" },
            )
            nodes = arrayOf(
                unsafeJso<GraphNodeItemOption> {
                    name = "a"
                    category = StringOrDouble(0.0)
                    value = OptionDataValueOrArray(1.0)
                },
                unsafeJso<GraphNodeItemOption> { name = "b"; category = StringOrDouble("shell") },
            )
            links = arrayOf(
                unsafeJso<GraphEdgeItemOption> {
                    source = StringOrDouble("a")
                    target = StringOrDouble("b")
                    lineStyle = unsafeJso<GraphEdgeLineStyleOption> { curveness = 0.2 }
                },
            )
            edges = links
        }

        assertEquals(2, series.categories?.size)
        assertEquals(2, series.nodes?.size)
        assertEquals("shell", series.nodes!![1].category!!.unsafeCast<String>())
        assertEquals(0.2, series.links!![0].lineStyle!!.curveness)
        assertEquals(1, series.edges?.size)
    }

    @Test
    fun treeSeriesLeavesAndLabelLayoutAreTyped() {
        val series = treeSeries {
            layout = TreeLayouts.orthogonal
            leaves = unsafeJso<TreeSeriesLeavesOption> {
                label = unsafeJso<LabelOption> { position = LabelPosition("right") }
            }
            labelLayout = LabelLayout(
                unsafeJso<LabelLayoutOption> {
                    hideOverlap = true
                    moveOverlap = MoveOverlaps.shiftY
                },
            )
            typedData = items(
                unsafeJso<TreeSeriesNodeItemOption> {
                    name = OptionId("root")
                    value = OptionDataValue(1.0)
                    children = arrayOf(unsafeJso<TreeSeriesNodeItemOption> { name = OptionId("leaf") })
                },
            )
        }

        assertEquals("right", series.leaves!!.label!!.position!!.unsafeCast<String>())
        val layout = series.labelLayout!!.unsafeCast<LabelLayoutOption>()
        assertTrue(layout.hideOverlap!!)
        assertEquals("shiftY", layout.moveOverlap!!.unsafeCast<String>())
        assertEquals(1, series.data?.size)
        val root = series.data!![0].unsafeCast<TreeSeriesNodeItemOption>()
        assertEquals("root", root.name!!.unsafeCast<String>())
        assertEquals(1, root.children?.size)
    }

    @Test
    fun treeLabelLayoutAcceptsACallback() {
        val series = treeSeries {
            labelLayout = LabelLayout { params -> unsafeJso<LabelLayoutOption> { dx = params.seriesIndex } }
        }
        assertEquals("function", jsTypeOf(series.labelLayout))
    }

    @Test
    fun treemapSeriesCarriesBreadcrumbLevelsAndNodes() {
        val series = treemapSeries {
            sort = BooleanOrSortOrder(SortOrders.desc)
            clipWindow = ClipWindows.origin
            nodeClick = NodeClickBehaviors.zoomToNode
            colorMappingBy = ColorMappingBys.`value`
            colorSaturation = DoubleArrayOrNone(arrayOf(0.35, 0.5))
            breadcrumb = unsafeJso<TreemapBreadcrumbOption> {
                show = false
                height = StringOrDouble(22.0)
                itemStyle = unsafeJso<BreadcrumbItemStyleOption> {
                    color = ZRColor("#ffffff")
                    textStyle = unsafeJso<LabelOption> { color = "#333333" }
                }
                emphasis = unsafeJso<BreadcrumbEmphasisOption> {
                    disabled = true
                    itemStyle = unsafeJso<BreadcrumbItemStyleOption> { color = ZRColor("#eeeeee") }
                }
            }
            levels = arrayOf(
                unsafeJso<TreemapSeriesLevelOption> {
                    color = ColorArrayOrNone("#ffffff")
                    decal = DecalArrayOrNone("none")
                    itemStyle = unsafeJso<TreemapSeriesItemStyleOption> {
                        gapWidth = 2.0
                        borderColorSaturation = 0.6
                    }
                },
            )
            typedData = items(
                unsafeJso<TreemapSeriesNodeItemOption> {
                    name = OptionId("root")
                    value = OptionDataValue(100.0)
                    color = ColorArrayOrNone(arrayOf(ZRColor("#111111"), ZRColor("#222222")))
                    children = arrayOf(unsafeJso<TreemapSeriesNodeItemOption> { name = OptionId("child") })
                },
            )
        }

        assertEquals("desc", series.sort!!.unsafeCast<String>())
        assertEquals("origin", series.clipWindow!!.unsafeCast<String>())
        assertFalse(series.breadcrumb!!.show!!)
        assertEquals(22.0, series.breadcrumb!!.height!!.unsafeCast<Double>())
        assertTrue(series.breadcrumb!!.emphasis!!.disabled!!)
        assertEquals("value", series.colorMappingBy!!.unsafeCast<String>())
        assertEquals(2, series.colorSaturation!!.unsafeCast<Array<Double>>().size)
        assertEquals(1, series.levels?.size)
        assertEquals(2.0, series.levels!![0].itemStyle!!.gapWidth)
        assertEquals("#ffffff", series.levels!![0].color!!.unsafeCast<String>())
        val root = series.data!![0].unsafeCast<TreemapSeriesNodeItemOption>()
        assertEquals(2, root.color!!.unsafeCast<Array<ZRColor>>().size)
        assertEquals(1, root.children?.size)
    }

    @Test
    fun sunburstSeriesCarriesSortLevelsAndNodes() {
        val comparator: (SortParam, SortParam) -> Double = { a, b -> a.getValue() - b.getValue() }
        val series = sunburstSeries {
            sort = SunburstSort(comparator)
            animationType = AnimationTypes.expansion
            nodeClick = NodeClickBehaviors.rootToNode
            levels = arrayOf(
                unsafeJso<SunburstSeriesLevelOption> {
                    radius = arrayOf(StringOrDouble(0.0), StringOrDouble("40%"))
                    r0 = StringOrDouble(0.0)
                    label = unsafeJso<SunburstLabelOption> { minAngle = 15.0 }
                    highlight = unsafeJso<SunburstHighlightOption> {
                        itemStyle = unsafeJso<SunburstItemStyleOption> { opacity = 0.8 }
                    }
                },
            )
            typedData = items(
                unsafeJso<SunburstSeriesNodeItemOption> {
                    name = OptionId("root")
                    value = OptionDataValue(1.0)
                    children = arrayOf(
                        unsafeJso<SunburstSeriesNodeItemOption> { name = OptionId("a"); value = OptionDataValue(1.0) },
                    )
                },
            )
        }

        assertEquals("function", jsTypeOf(series.sort))
        assertEquals("expansion", series.animationType!!.unsafeCast<String>())
        assertEquals("rootToNode", series.nodeClick!!.unsafeCast<String>())
        assertEquals("40%", series.levels!![0].radius!![1].unsafeCast<String>())
        assertEquals(15.0, series.levels!![0].label!!.minAngle)
        assertEquals(0.8, series.levels!![0].highlight!!.itemStyle!!.opacity)
        assertEquals(1, series.data?.size)
    }

    @Test
    fun sunburstSortAcceptsAFixedOrder() {
        val series = sunburstSeries { sort = SunburstSort(SortOrders.asc) }
        assertEquals("asc", series.sort!!.unsafeCast<String>())
    }

    @Test
    fun sankeySeriesCarriesLevelsAndEdges() {
        val series = sankeySeries {
            nodeAlign = NodeAligns.left
            orient = Orients.horizontal
            color = arrayOf(ZRColor("#5470c6"), ZRColor("#91cc75"))
            levels = arrayOf(
                unsafeJso<SankeyLevelOption> {
                    depth = 0.0
                    itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 1.0 }
                },
            )
            nodes = arrayOf(
                unsafeJso<SankeyNodeItemOption> {
                    name = OptionId("a")
                    depth = 0.0
                    focusNodeAdjacency = FocusNodeAdjacencies.inEdges
                },
            )
            links = arrayOf(
                unsafeJso<SankeyEdgeItemOption> {
                    source = StringOrDouble("a")
                    target = StringOrDouble("b")
                    value = OptionDataValue(5.0)
                    edgeLabel = unsafeJso<LabelOption> { show = true }
                    lineStyle = unsafeJso<SankeyEdgeStyleOption> { curveness = 0.5 }
                },
            )
            edges = links
        }

        assertEquals("left", series.nodeAlign!!.unsafeCast<String>())
        assertEquals(1, series.levels?.size)
        assertEquals(0.0, series.levels!![0].depth)
        assertEquals(1.0, series.levels!![0].itemStyle!!.borderWidth)
        assertEquals("inEdges", series.nodes!![0].focusNodeAdjacency!!.unsafeCast<String>())
        assertEquals(0.5, series.links!![0].lineStyle!!.curveness)
        assertEquals(1, series.edges?.size)
        assertEquals(2, series.color?.size)
    }

    @Test
    fun chordSeriesCarriesTypedNodesAndEdges() {
        val series = chordSeries {
            nodeAlign = NodeAligns.justify
            nodes = arrayOf(
                unsafeJso<ChordNodeItemOption> { id = "n0"; name = "a"; value = OptionDataValueOrArray(3.0) },
            )
            links = arrayOf(
                unsafeJso<ChordEdgeItemOption> {
                    source = StringOrDouble("a")
                    target = StringOrDouble("b")
                    lineStyle = unsafeJso<ChordEdgeLineStyleOption> { curveness = 0.3 }
                },
            )
            edges = links
        }

        assertEquals("justify", series.nodeAlign!!.unsafeCast<String>())
        assertEquals("n0", series.nodes!![0].id)
        assertEquals(0.3, series.links!![0].lineStyle!!.curveness)
        assertEquals(1, series.edges?.size)
    }

    @Test
    fun customSeriesRenderItemIsATypedFunction() {
        val series = customSeries {
            renderItem = { _, api ->
                val value = api.value(DimensionLoose(0.0))!!
                unsafeJso<CustomPathOption> {
                    type = "rect"
                    shape = unsafeJso<ZRShapeProps> {
                        width = api.getWidth()
                        height = value.unsafeCast<Double>()
                    }
                }
            }
        }

        assertEquals("function", jsTypeOf(series.renderItem))
        assertEquals(SeriesTypes.custom, series.type)
    }

    @Test
    fun customSeriesRenderItemSupportTypesAcceptPlainObjects() {
        val option = unsafeJso<BarGridLayoutOptionForCustomSeries> {
            count = 3.0
            barWidth = StringOrDouble("20%")
            barGap = StringOrDouble("30%")
        }
        val item = unsafeJso<BarGridLayoutResultItem> {
            bandWidth = 100.0
            offset = 0.0
            width = 20.0
            offsetCenter = 10.0
        }
        val font = unsafeJso<CustomSeriesFontOption> {
            fontStyle = web.cssom.FontStyle.italic
            fontWeight = ZRFontWeight(700.0)
            fontSize = StringOrDouble(12.0)
        }

        assertEquals(3.0, option.count)
        assertEquals("20%", option.barWidth!!.unsafeCast<String>())
        assertEquals(10.0, item.offsetCenter)
        assertEquals("italic", font.fontStyle.unsafeCast<String>())
    }

    @Test
    fun labelLayoutIsSharedWithTheSeries() {
        val series = lineSeries {
            labelLayout = LabelLayout(
                unsafeJso<LabelLayoutOption> {
                    draggable = true
                    dy = 4.0
                    width = 40.0
                    labelLinePoints = arrayOf(arrayOf(0.0, 0.0), arrayOf(1.0, 1.0))
                },
            )
        }
        val layout = series.labelLayout!!.unsafeCast<LabelLayoutOption>()
        assertTrue(layout.draggable!!)
        assertEquals(4.0, layout.dy)
        assertEquals(2, layout.labelLinePoints?.size)
    }
}

/**
 * End-to-end check that a real chart accepts an option built from the new item types and keeps the
 * series it was given.
 */
class SeriesDataItemChartTest {
    @Test
    fun chartAcceptsTheHierarchyRelationAndGaugeOptions() {
        val (chart: EChartsInstance, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    series = arrayOf<SeriesOption>(
                        treemapSeries {
                            sort = BooleanOrSortOrder(SortOrders.desc)
                            colorMappingBy = ColorMappingBys.`value`
                            levels = arrayOf(
                                unsafeJso<TreemapSeriesLevelOption> {
                                    itemStyle = unsafeJso<TreemapSeriesItemStyleOption> { gapWidth = 2.0 }
                                },
                            )
                            breadcrumb = unsafeJso<TreemapBreadcrumbOption> { show = false }
                            typedData = items(
                                unsafeJso<TreemapSeriesNodeItemOption> {
                                    name = OptionId("root")
                                    value = OptionDataValue(10.0)
                                    children = arrayOf(
                                        unsafeJso<TreemapSeriesNodeItemOption> {
                                            name = OptionId("leaf")
                                            value = OptionDataValue(10.0)
                                        },
                                    )
                                },
                            )
                        },
                        sunburstSeries {
                            sort = SunburstSort(SortOrders.asc)
                            animationType = AnimationTypes.scale
                            typedData = items(
                                unsafeJso<SunburstSeriesNodeItemOption> {
                                    name = OptionId("root")
                                    value = OptionDataValue(10.0)
                                    children = arrayOf(
                                        unsafeJso<SunburstSeriesNodeItemOption> {
                                            name = OptionId("leaf")
                                            value = OptionDataValue(10.0)
                                        },
                                    )
                                },
                            )
                        },
                        treeSeries {
                            leaves = unsafeJso<TreeSeriesLeavesOption> {
                                label = unsafeJso<LabelOption> { show = true }
                            }
                            typedData = items(
                                unsafeJso<TreeSeriesNodeItemOption> {
                                    name = OptionId("root")
                                    value = OptionDataValue(1.0)
                                },
                            )
                        },
                        gaugeSeries {
                            axisLine = unsafeJso<GaugeAxisLineOption> {
                                lineStyle = unsafeJso<GaugeAxisLineStyleOption> {
                                    width = 14.0
                                    color = arrayOf(
                                        gaugeColorStop(0.3, "#5470c6"),
                                        gaugeColorStop(1.0, "#91cc75"),
                                    )
                                }
                            }
                        },
                        graphSeries {
                            layout = GraphLayouts.circular
                            categories = arrayOf(unsafeJso<GraphCategoryItemOption> { name = "core" })
                            nodes = arrayOf(
                                unsafeJso<GraphNodeItemOption> { name = "a"; category = StringOrDouble(0.0) },
                            )
                            links = arrayOf(
                                unsafeJso<GraphEdgeItemOption> {
                                    source = StringOrDouble("a")
                                    target = StringOrDouble("a")
                                },
                            )
                        },
                        sankeySeries {
                            nodeAlign = NodeAligns.left
                            levels = arrayOf(unsafeJso<SankeyLevelOption> { depth = 0.0 })
                            nodes = arrayOf(
                                unsafeJso<SankeyNodeItemOption> { name = OptionId("a"); depth = 0.0 },
                                unsafeJso<SankeyNodeItemOption> { name = OptionId("b"); depth = 1.0 },
                            )
                            links = arrayOf(
                                unsafeJso<SankeyEdgeItemOption> {
                                    source = StringOrDouble("a")
                                    target = StringOrDouble("b")
                                    value = OptionDataValue(4.0)
                                },
                            )
                        },
                        chordSeries {
                            nodeAlign = NodeAligns.justify
                            nodes = arrayOf(
                                unsafeJso<ChordNodeItemOption> { name = "a" },
                                unsafeJso<ChordNodeItemOption> { name = "b" },
                            )
                            links = arrayOf(
                                unsafeJso<ChordEdgeItemOption> {
                                    source = StringOrDouble("a")
                                    target = StringOrDouble("b")
                                    value = OptionDataValue(2.0)
                                },
                            )
                        },
                    )
                },
            )

            val stored = chart.getOption().unsafeCast<EChartsOption>()
            val types = stored.series!!.map { it.type!!.unsafeCast<String>() }
            assertEquals(
                listOf("treemap", "sunburst", "tree", "gauge", "graph", "sankey", "chord"),
                types,
            )
            assertTrue(chart.getWidth() > 0)
        } finally {
            destroyChart(chart, container)
        }
    }
}
