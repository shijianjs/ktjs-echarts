package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor

/*
 * `matrix-grid-layout` from `public/examples/ts/matrix-grid-layout.js` of apache/echarts-examples.
 *
 * Five sections — page title, header, sidebar, main content and footer — are placed on a matrix
 * that only exists to define columns and rows; the charts inside a section are ordinary grids that
 * are pinned to the same cell through `grid.coord`. Two media units declare a narrow (one column)
 * and a wide (four columns) arrangement, so resizing the canvas re-lays the page out like CSS grid.
 *
 * Upstream composes the option with helper functions (`assembleIntoEChartsOption`) and ships a
 * floating control panel that toggles the matrix grid lines. The panel is part of the example site,
 * not of the chart API, so the port keeps the option and drops the panel; the grid lines stay off.
 *
 * The matrix dimensions are declared with `length` instead of `data: Array(n).fill(null)`: the
 * upstream matrix builds exactly those empty cells from `length`, and the port then never has to
 * hand null entries to a `data` array. `length` only applies while `data` is absent, which is why
 * the base `matrix.x` / `matrix.y` below carry `show` alone.
 */

private val matrixGridLayout = Category(Groups.MATRIX, "examples/MatrixGridLayout.kt")

/** One section of the page: it always owns a title, and every section except the page title owns a grid. */
private class LayoutSection(val id: String, val hasGrid: Boolean) {
    /** Id of the title of the section. */
    val titleId: String get() = "${id}_title"

    /** Id shared by the grid, both axes and the series of the section. */
    val gridId: String get() = id.removePrefix("section_")
}

/** The sections, in the order the upstream page declares them. */
private val LAYOUT_SECTIONS =
    arrayOf(
        LayoutSection("section_title_1", hasGrid = false),
        LayoutSection("section_header_1", hasGrid = true),
        LayoutSection("section_sidebar_1", hasGrid = true),
        LayoutSection("section_main_content_area_1", hasGrid = true),
        LayoutSection("section_footer_1", hasGrid = true),
    )

/** Caption of a section title. */
private class SectionTitle(val text: String, val top: Double, val fontSize: Double? = null)

/** The captions of [LAYOUT_SECTIONS]; only the page title keeps the default font size. */
private val SECTION_TITLES =
    mapOf(
        "section_title_1" to SectionTitle("Resize the Canvas to Check the Responsiveness", 10.0),
        "section_header_1" to SectionTitle("Header Section", 5.0, 14.0),
        "section_sidebar_1" to SectionTitle("Sidebar Section", 15.0, 14.0),
        "section_main_content_area_1" to SectionTitle("Main Content Area", 15.0, 14.0),
        "section_footer_1" to SectionTitle("Footer Section", 15.0, 14.0),
    )

/** Inset of one section grid inside its matrix cell, in pixels. */
private class SectionGrid(
    val top: Double,
    val bottom: Double,
    val left: Double,
    val right: Double,
)

/** The grids of the four chart sections, as the upstream page insets them. */
private val SECTION_GRIDS =
    mapOf(
        "section_header_1" to SectionGrid(top = 30.0, bottom = 10.0, left = 10.0, right = 10.0),
        "section_sidebar_1" to SectionGrid(top = 50.0, bottom = 30.0, left = 40.0, right = 30.0),
        "section_main_content_area_1" to SectionGrid(top = 50.0, bottom = 10.0, left = 10.0, right = 10.0),
        "section_footer_1" to SectionGrid(top = 50.0, bottom = 10.0, left = 20.0, right = 20.0),
    )

/**
 * One media unit: the number of columns and the cell of every section.
 *
 * A coordinate is one dimension of a `coord` option: a single `0`-like value, or a `[min, max]`
 * range of cells.
 */
private class LayoutMedia(
    val maxWidth: Double?,
    val columns: Double,
    val coords: Map<String, Array<DoubleArray>>,
)

/** The narrow arrangement: one column, every section stacked. */
private val NARROW_LAYOUT =
    LayoutMedia(
        maxWidth = 500.0,
        columns = 1.0,
        coords =
            mapOf(
                "section_title_1" to arrayOf(doubleArrayOf(0.0), doubleArrayOf(0.0)),
                "section_header_1" to arrayOf(doubleArrayOf(0.0), doubleArrayOf(1.0, 2.0)),
                "section_sidebar_1" to arrayOf(doubleArrayOf(0.0), doubleArrayOf(3.0, 4.0)),
                "section_main_content_area_1" to arrayOf(doubleArrayOf(0.0), doubleArrayOf(5.0, 7.0)),
                "section_footer_1" to arrayOf(doubleArrayOf(0.0), doubleArrayOf(8.0, 9.0)),
            ),
    )

/** The default arrangement: a title band, a sidebar and a content column. */
private val WIDE_LAYOUT =
    LayoutMedia(
        maxWidth = null,
        columns = 4.0,
        coords =
            mapOf(
                "section_title_1" to arrayOf(doubleArrayOf(0.0, 3.0), doubleArrayOf(0.0)),
                "section_header_1" to arrayOf(doubleArrayOf(0.0, 3.0), doubleArrayOf(1.0, 2.0)),
                "section_sidebar_1" to arrayOf(doubleArrayOf(0.0), doubleArrayOf(3.0, 9.0)),
                "section_main_content_area_1" to arrayOf(doubleArrayOf(1.0, 3.0), doubleArrayOf(3.0, 7.0)),
                "section_footer_1" to arrayOf(doubleArrayOf(1.0, 3.0), doubleArrayOf(8.0, 9.0)),
            ),
    )

/** The rows of ten days of the page, `[date, value]`, or `[value, date]` when [inverseXY]. */
private fun layoutRows(
    seed: Int,
    dayCount: Int,
    inverseXY: Boolean,
): Array<Array<OptionDataValue?>> {
    val random = kotlin.random.Random(seed)
    // 2025-05-05, a Monday, the start of both walks.
    val dayStart = 1746403200000.0
    val sevenDays = 604800000.0
    var lastValue = kotlin.math.round(random.nextDouble() * 300).toInt()
    var turnCount: Int? = null
    var sign = -1
    return Array(dayCount) { index ->
        if (turnCount == null || index >= turnCount!!) {
            turnCount =
                index + kotlin.math.round((dayCount / 4.0) * ((random.nextDouble() - 0.5) * 0.1)).toInt()
            sign = -sign
        }
        val deltaMagnitude = 50.0
        val delta =
            kotlin.math.round(
                random.nextDouble() * deltaMagnitude - deltaMagnitude / 2 + (sign * deltaMagnitude) / 3,
            ).toInt()
        lastValue = kotlin.math.max(0, lastValue + delta)
        val date = OptionDataValue(ECharts.time.format(dayStart + index * sevenDays, "{yyyy}-{MM}-{dd}", false))
        val value = OptionDataValue(lastValue.toDouble())
        if (inverseXY) arrayOf(value, date) else arrayOf(date, value)
    }
}

/** A coordinate of [LayoutMedia]: one value or a `[min, max]` range per dimension. */
private fun layoutCoord(ranges: Array<DoubleArray>): CoordinateSystemDataCoord =
    CoordinateSystemDataCoordRanges(
        Array(ranges.size) { dim -> Array(ranges[dim].size) { index -> OptionDataValue(ranges[dim][index]) } },
    )

/** The titles of all sections, laid out on the matrix. */
private fun layoutTitles(): Array<TitleOption> =
    Array(LAYOUT_SECTIONS.size) { index ->
        val section = LAYOUT_SECTIONS[index]
        val title = SECTION_TITLES.getValue(section.id)
        unsafeJso {
            id = section.titleId
            coordinateSystem = "matrix"
            text = title.text
            left = StringOrDouble("center")
            top = StringOrDouble(title.top)
            if (title.fontSize != null) {
                textStyle = unsafeJso<TextStyleOption> { fontSize = StringOrDouble(title.fontSize) }
            }
        }
    }

/** The four section grids, every one pinned to a matrix cell by the media units. */
private fun layoutGrids(): Array<GridOption> =
    LAYOUT_SECTIONS.filter { it.hasGrid }.map { section ->
        val grid = SECTION_GRIDS.getValue(section.id)
        unsafeJso<GridOption> {
            id = section.gridId
            coordinateSystem = "matrix"
            tooltip = unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis }
            top = StringOrDouble(grid.top)
            bottom = StringOrDouble(grid.bottom)
            left = StringOrDouble(grid.left)
            right = StringOrDouble(grid.right)
            outerBounds =
                unsafeJso<BoxLayoutOption> {
                    top = StringOrDouble(30.0)
                    left = StringOrDouble(20.0)
                    bottom = StringOrDouble(20.0)
                    right = StringOrDouble(20.0)
                }
        }
    }.toTypedArray()

/** The x axis of one section, as the upstream page configures it. */
private fun layoutXAxis(sectionId: String, gridId: String): XAxisOption =
    when (sectionId) {
        "section_header_1", "section_main_content_area_1", "section_footer_1" ->
            unsafeJso {
                type = AxisTypes.time
                id = gridId
                this.gridId = gridId
            }
        else ->
            unsafeJso {
                id = gridId
                this.gridId = gridId
                splitLine = unsafeJso<SplitLineOption> { show = false }
                axisLabel = unsafeJso<AxisLabelOption> { hideOverlap = true }
            }
    }

/** The y axis of one section. */
private fun layoutYAxis(sectionId: String, gridId: String): YAxisOption =
    when (sectionId) {
        "section_header_1", "section_footer_1" ->
            unsafeJso {
                id = gridId
                this.gridId = gridId
                splitNumber = 2.0
                splitLine = unsafeJso<SplitLineOption> { show = false }
            }
        "section_sidebar_1" ->
            unsafeJso {
                type = AxisTypes.time
                id = gridId
                this.gridId = gridId
                axisLabel = unsafeJso<AxisLabelOption> { hideOverlap = true }
            }
        else ->
            unsafeJso {
                id = gridId
                this.gridId = gridId
            }
    }

/** A walk of [days] days as line items. */
private fun layoutLineItems(seed: Int, days: Int): Array<LineDataItem?> =
    dataItems(*layoutRows(seed, days, false).map { LineDataItem(it) }.toTypedArray())

/** A walk of [days] days as bar items. */
private fun layoutBarItems(seed: Int, days: Int, inverseXY: Boolean): Array<BarDataItem?> =
    dataItems(*layoutRows(seed, days, inverseXY).map { BarDataItem(it) }.toTypedArray())

/** The single series of every section: a line for the two long walks, a bar for the ten-day ones. */
private fun layoutSeries(): Array<SeriesOption> =
    arrayOf(
        lineSeries {
            id = "header_1"
            xAxisId = "header_1"
            yAxisId = "header_1"
            symbol = SymbolShape("none")
            typedData = layoutLineItems(seed = 11, days = 100)
        },
        barSeries {
            id = "sidebar_1"
            xAxisId = "sidebar_1"
            yAxisId = "sidebar_1"
            typedData = layoutBarItems(seed = 23, days = 10, inverseXY = true)
        },
        lineSeries {
            id = "main_content_area_1"
            xAxisId = "main_content_area_1"
            yAxisId = "main_content_area_1"
            symbol = SymbolShape("none")
            typedData = layoutLineItems(seed = 37, days = 100)
        },
        barSeries {
            id = "footer_1"
            xAxisId = "footer_1"
            yAxisId = "footer_1"
            typedData = layoutBarItems(seed = 41, days = 10, inverseXY = false)
        },
    )

/** The option a media unit applies: the matrix dimensions plus the cell of every section. */
private fun layoutMediaOption(media: LayoutMedia): EChartsOption =
    unsafeJso {
        matrix =
            arrayOf(
                unsafeJso<MatrixOption> {
                    x = dimension { length = media.columns }
                    y = dimension { length = 10.0 }
                },
            )
        title =
            Array(LAYOUT_SECTIONS.size) { index ->
                val section = LAYOUT_SECTIONS[index]
                unsafeJso<TitleOption> {
                    id = section.titleId
                    coord = layoutCoord(media.coords.getValue(section.id))
                }
            }
        grid =
            LAYOUT_SECTIONS.filter { it.hasGrid }.map { section ->
                unsafeJso<GridOption> {
                    id = section.gridId
                    coord = layoutCoord(media.coords.getValue(section.id))
                }
            }.toTypedArray()
    }

/** The whole page: the sections, their grids and the two arrangements. */
private fun layoutOption(): EChartsOption =
    unsafeJso {
        matrix =
            arrayOf(
                unsafeJso<MatrixOption> {
                    x = dimension { show = false }
                    y = dimension { show = false }
                    body =
                        unsafeJso<MatrixBodyOption> {
                            itemStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("none") }
                        }
                    backgroundStyle = unsafeJso<ItemStyleOption> { borderColor = ZRColor("none") }
                    top = StringOrDouble(0.0)
                    bottom = StringOrDouble(0.0)
                    left = StringOrDouble(0.0)
                    right = StringOrDouble(0.0)
                },
            )
        tooltip = one(unsafeJso<TooltipOption> {})
        title = layoutTitles()
        grid = layoutGrids()
        xAxis = LAYOUT_SECTIONS.filter { it.hasGrid }.map { layoutXAxis(it.id, it.gridId) }.toTypedArray()
        yAxis = LAYOUT_SECTIONS.filter { it.hasGrid }.map { layoutYAxis(it.id, it.gridId) }.toTypedArray()
        series = layoutSeries()
        media =
            arrayOf(
                unsafeJso<MediaUnit> {
                    query = unsafeJso<MediaQueryOption> { maxWidth = NARROW_LAYOUT.maxWidth }
                    option = layoutMediaOption(NARROW_LAYOUT)
                },
                unsafeJso<MediaUnit> {
                    option = layoutMediaOption(WIDE_LAYOUT)
                },
            )
    }

val matrixGridLayoutEntries: List<CatalogEntry> =
    listOf(
        matrixGridLayout.chart(
            "matrix-grid-layout",
            "矩阵中响应式网格布局",
            "Responsive grid layout based on matrix",
            "矩阵只负责定义行列，五个区块各自的网格通过 `grid.coord` 落在指定单元格上；" +
                "两个 `media` 单元分别描述窄屏（单列堆叠）与宽屏（标题带 + 侧栏 + 内容列），" +
                "拖动改变画布宽度即可看到重排。",
            "The matrix only defines columns and rows; every section grid is pinned to its cells by " +
                "`grid.coord`, and two `media` units describe the narrow (one stacked column) and the " +
                "wide (title band, sidebar, content column) arrangements.",
        ) {
            layoutOption()
        },
    )
