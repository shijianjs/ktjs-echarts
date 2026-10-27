package echarts.options

import echarts.ECharts
import echarts.GraphicActions
import echarts.createChart
import echarts.destroyChart
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertSame
import kotlin.test.assertTrue
import zrender.RectLike
import zrender.ZRColor
import zrender.ZRShapeProps
import zrender.ZRStyleProps

/**
 * Tests for the matrix coordinate system, the graphic component union and the data-coordinate
 * helper types they share.
 *
 * Upstream introduced the matrix coordinate system in ECharts 6; the option types live in
 * `shared.d.ts` (`MatrixOption`, `MatrixDimensionCellLooseOption`, `GraphicComponentLooseOption`).
 */
class MatrixAndGraphicOptionTest {
    private fun cell(value: String, vararg children: MatrixDimensionCellOption): MatrixDimensionCellOption =
        unsafeJso {
            this.value = value
            this.size = StringOrDouble(20.0)
            this.z2 = 2.0
            if (children.isNotEmpty()) this.children = arrayOf(*children)
        }

    @Test
    fun matrixDimensionCellLooseOptionWrapsBothBranches() {
        val branch = cell("A", cell("A1"), cell("A2"))
        val wrapped = MatrixDimensionCellLooseOption(branch)
        assertSame(branch, wrapped.unsafeCast<MatrixDimensionCellOption>())
        assertEquals("A", branch.value)
        assertEquals(2, branch.children!!.size)
        assertEquals("A1", branch.children!![0].value)
        assertEquals(20.0, branch.size!!.unsafeCast<Double>())
        assertEquals(2.0, branch.z2)

        val leaf = MatrixDimensionCellLooseOption("B")
        assertEquals("B", leaf.unsafeCast<String>())
    }

    @Test
    fun dimensionKeepsLevelSizeAndCells() {
        val dimension = unsafeJso<MatrixDimensionOption> {
            levelSize = StringOrDouble(40.0)
            length = 200.0
            levels = arrayOf<MatrixDimensionLevelOption?>(unsafeJso { levelSize = StringOrDouble("50%") }, null)
            data = arrayOf(MatrixDimensionCellLooseOption("X1"), MatrixDimensionCellLooseOption(cell("X2")))
            label = unsafeJso<LabelOption> { fontSize = StringOrDouble(12.0) }
        }

        assertEquals(40.0, dimension.levelSize!!.unsafeCast<Double>())
        assertEquals(200.0, dimension.length)
        assertEquals(2, dimension.levels!!.size)
        assertNull(dimension.levels!![1])
        assertEquals("50%", dimension.levels!![0]!!.levelSize!!.unsafeCast<String>())
        assertEquals(2, dimension.data!!.size)
        assertEquals(12.0, dimension.label!!.fontSize!!.unsafeCast<Double>())
    }

    @Test
    fun bodyAndCornerCellsCarryCoordinates() {
        val cellOption = unsafeJso<MatrixBodyCornerCellOption> {
            value = "Break"
            coord =
                arrayOf(
                    MatrixCoordRangeOption(OptionDataValue(0.0)),
                    MatrixCoordRangeOption(arrayOf(OptionDataValue(1.0), OptionDataValue(2.0))),
                )
            coordClamp = true
            mergeCells = true
            label = unsafeJso<LabelOption> { color = "#999" }
        }

        assertEquals("Break", cellOption.value)
        assertTrue(cellOption.coordClamp!!)
        assertTrue(cellOption.mergeCells!!)
        assertEquals(2, cellOption.coord!!.size)
        assertEquals(0.0, cellOption.coord!![0]!!.unsafeCast<Double>())
        assertEquals(2, cellOption.coord!![1]!!.unsafeCast<Array<Double>>().size)
        assertEquals("#999", cellOption.label!!.color)
    }

    @Test
    fun matrixOptionAcceptsDimensionsAndLayout() {
        val matrix = unsafeJso<MatrixOption> {
            x = unsafeJso<MatrixDimensionOption> { data = arrayOf(MatrixDimensionCellLooseOption("A")) }
            y = unsafeJso<MatrixDimensionOption> { data = arrayOf(MatrixDimensionCellLooseOption("U")) }
            left = StringOrDouble("center")
            width = StringOrDouble(600.0)
            triggerEvent = false
        }

        assertEquals(1, matrix.x!!.data!!.size)
        assertEquals(1, matrix.y!!.data!!.size)
        assertEquals("center", matrix.left!!.unsafeCast<String>())
        assertEquals(600.0, matrix.width!!.unsafeCast<Double>())
        assertEquals(false, matrix.triggerEvent)
    }

    @Test
    fun coordinateSystemDataCoordWrapsFlatAndRangeForms() {
        val flat = CoordinateSystemDataCoord(arrayOf(OptionDataValue(1.0), OptionDataValue("A")))
        assertEquals(2, flat.unsafeCast<Array<Any>>().size)

        val single = CoordinateSystemDataCoord(OptionDataValue(3.0))
        assertEquals(3.0, single.unsafeCast<Double>())

        val ranges =
            CoordinateSystemDataCoordRanges(
                arrayOf(
                    arrayOf(OptionDataValue(1.0), OptionDataValue(5.0)),
                    arrayOf(OptionDataValue(2.0), OptionDataValue(8.0)),
                ),
            )
        assertEquals(2, ranges.unsafeCast<Array<Array<Double>>>().size)
        assertEquals(5.0, ranges.unsafeCast<Array<Array<Double>>>()[0][1])
    }

    @Test
    fun gridCanBeLaidOutOnAnotherCoordinateSystem() {
        val grid = unsafeJso<GridOption> {
            coordinateSystem = "matrix"
            coord = CoordinateSystemDataCoord(arrayOf(OptionDataValue("A"), OptionDataValue("U")))
            top = StringOrDouble(10.0)
        }

        assertEquals("matrix", grid.coordinateSystem)
        assertEquals(2, grid.coord!!.unsafeCast<Array<Any>>().size)
    }

    @Test
    fun graphicOptionAcceptsComponentsAndBareElements() {
        val text =
            unsafeJso<GraphicElement> {
                type = "text"
                x = StringOrDouble("45%")
                y = StringOrDouble(40.0)
                style = unsafeJso<ZRStyleProps> { this.text = "caption"; fill = ZRColor("#333") }
            }
        val component =
            unsafeJso<GraphicComponentOption> {
                id = "caption"
                elements = arrayOf(text)
                animation = false
                action = GraphicActions.merge
            }

        val wrappedComponent = GraphicComponentLooseOption(component)
        assertSame(component, wrappedComponent.unsafeCast<GraphicComponentOption>())
        val wrappedElement = GraphicComponentLooseOption(text)
        assertSame(text, wrappedElement.unsafeCast<GraphicElement>())

        assertEquals(1, component.elements!!.size)
        assertEquals("45%", component.elements!![0].x!!.unsafeCast<String>())
        assertEquals(false, component.animation)
        assertEquals("merge", component.unsafeCast<GraphicActionProbe>().action)
    }

    @Test
    fun clipRectByRectClipsAndReportsMisses() {
        val target = unsafeJso<RectLike> { x = -5.0; y = 2.0; width = 20.0; height = 5.0 }
        val rect = unsafeJso<RectLike> { x = 0.0; y = 0.0; width = 10.0; height = 10.0 }

        val clipped = ECharts.graphic.clipRectByRect(target, rect)
        assertNotNull(clipped)
        assertEquals(0.0, clipped.x)
        assertEquals(10.0, clipped.width)

        val outside = unsafeJso<RectLike> { x = 100.0; y = 100.0; width = 1.0; height = 1.0 }
        assertNull(ECharts.graphic.clipRectByRect(outside, rect))
    }

    @Test
    fun matrixChartRendersHeatmapCells() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    matrix =
                        one(
                            unsafeJso<MatrixOption> {
                                x =
                                    unsafeJso<MatrixDimensionOption> {
                                        data =
                                            arrayOf(
                                                MatrixDimensionCellLooseOption(cell("A", cell("A1"), cell("A2"))),
                                            )
                                    }
                                y = unsafeJso<MatrixDimensionOption> { data = arrayOf(MatrixDimensionCellLooseOption("U")) }
                                top = StringOrDouble(20.0)
                                bottom = StringOrDouble(20.0)
                            },
                        )
                    visualMap = one(unsafeJso<VisualMapOption> { min = 0.0; max = 100.0; dimension = 2.0 })
                    series =
                        one(
                            heatmapSeries {
                                coordinateSystem = "matrix"
                                typedData =
                                    dataItems(
                                        HeatmapDataItem(
                                            arrayOf(
                                                OptionDataValue("A1"),
                                                OptionDataValue("U"),
                                                OptionDataValue(10.0),
                                            ),
                                        ),
                                        HeatmapDataItem(
                                            arrayOf(
                                                OptionDataValue("A2"),
                                                OptionDataValue("U"),
                                                OptionDataValue(90.0),
                                            ),
                                        ),
                                    )
                                label = unsafeJso<LabelOption> { show = true }
                            },
                        )
                },
            )

            val option = chart.getOption()
            assertEquals(1, option.matrix!!.size)
            assertEquals(1, option.matrix!![0].x!!.data!!.size)
            assertEquals(2, option.series!![0].unsafeCast<HeatmapSeriesOption>().typedData!!.size)
            assertTrue(chart.getWidth() > 0)
            assertTrue(chart.getHeight() > 0)
        } finally {
            destroyChart(chart, container)
        }
    }

    @Test
    fun renderItemCanLayoutCellsOnAMatrix() {
        val (chart, container) = createChart()
        var layoutRect: RectLike? = null
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    matrix =
                        one(
                            unsafeJso<MatrixOption> {
                                x = unsafeJso<MatrixDimensionOption> { data = arrayOf(MatrixDimensionCellLooseOption("A")) }
                                y = unsafeJso<MatrixDimensionOption> { data = arrayOf(MatrixDimensionCellLooseOption("U")) }
                                top = StringOrDouble(20.0)
                                bottom = StringOrDouble(20.0)
                            },
                        )
                    series =
                        one(
                            customSeries {
                                coordinateSystem = "matrix"
                                typedData =
                                    dataItems(
                                        OptionDataItem(
                                            arrayOf(
                                                OptionDataValue("A"),
                                                OptionDataValue("U"),
                                                OptionDataValue(1.0),
                                            ),
                                        ),
                                    )
                                renderItem = { _, api ->
                                    layoutRect =
                                        api.layout(
                                            CoordinateSystemDataCoord(
                                                arrayOf(
                                                    api.value(DimensionLoose(0.0)),
                                                    api.value(DimensionLoose(1.0)),
                                                ),
                                            ),
                                        ).rect
                                    unsafeJso<CustomPathOption> {
                                        type = "rect"
                                        shape =
                                            unsafeJso<ZRShapeProps> {
                                                x = layoutRect!!.x
                                                y = layoutRect!!.y
                                                width = layoutRect!!.width
                                                height = layoutRect!!.height
                                            }
                                        style = api.style()
                                    }
                                }
                            },
                        )
                },
            )
        } finally {
            destroyChart(chart, container)
        }

        assertTrue(layoutRect!!.width > 0.0)
        assertTrue(layoutRect!!.height > 0.0)
    }
}

/** Reads the `$action` property of a graphic component, which is not a Kotlin identifier. */
private external interface GraphicActionProbe {
    @kotlin.js.JsName("\$action")
    var action: String?
}
