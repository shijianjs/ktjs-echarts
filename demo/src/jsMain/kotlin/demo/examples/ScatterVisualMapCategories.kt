package demo.examples

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * `doc-example/scatter-visualMap-categories` from
 * `public/examples/ts/doc-example/scatter-visualMap-categories.js` of apache/echarts-examples.
 *
 * The three tables are the ones of `scatter-aqi-color`, kept in `ScatterAqiData.kt`, with the one
 * cell the two upstream files disagree on: this file leaves the CO of Beijing's ninth day as the
 * string `-`, where `scatter-aqi-color.ts` gives it `4.8`. Only that cell is replaced below.
 *
 * This is the reference page of `visualMap.categories`: the grade of a day (dimension 7) picks a
 * symbol and a size by name, the empty key `''` of the dictionary is the default for every grade it
 * does not name, and `selected` starts with the two worst grades switched off, which drops them to
 * the `outOfRange` visuals.
 *
 * The upstream `itemStyle` still uses the ECharts 4 `normal` state, which ECharts 6 dropped, so the
 * port sets the same fields directly.
 */

private val scatterVisualMapCategories = Category(Groups.DOC, "examples/ScatterVisualMapCategories.kt")

/** The three series names, in the order of the legend. */
private val CATEGORIES_CITIES = arrayOf("北京", "上海", "广州")

/** The colour of every series, in the order of [CATEGORIES_CITIES]. */
private val CATEGORIES_COLORS = arrayOf("#dd4444", "#fec42c", "#80F1BE")

/** The grade column of a row, which is the dimension the visual map reads. */
private const val GRADE_COLUMN = 7

/** The CO column, and the Beijing row whose CO the two upstream tables disagree about. */
private const val CO_COLUMN = 4

private const val MISSING_CO_ROW = 8

/** The columns the tooltip prints, in the order of the upstream `schema`. */
private val SCHEMA_TEXTS =
    arrayOf("日", "AQI指数", "PM2.5", "PM10", "一氧化碳（CO）", "二氧化氮（NO₂）", "二氧化硫（SO₂）")

/** The grades, worst first, in the order upstream hands them to `categories`. */
private val GRADE_CATEGORIES = arrayOf("严重污染", "重度污染", "中度污染", "轻度污染", "良", "优")

/** Beijing with the `-` of this example: a copy, so the table of `scatter-aqi-color` stays as it is. */
private val CATEGORIES_BEIJING: Array<Array<OptionDataValue?>> =
    Array(SCATTER_AQI_BEIJING.size) { index ->
        SCATTER_AQI_BEIJING[index].copyOf().also { values ->
            if (index == MISSING_CO_ROW) {
                values[CO_COLUMN] = OptionDataValue("-")
            }
        }
    }

/** Rows of one city as scatter items; upstream hands the raw 8-element rows to the series. */
private fun categoriesData(rows: Array<Array<OptionDataValue?>>): Array<ScatterDataItem?> =
    Array(rows.size) { index -> ScatterDataItem(rows[index]) }

/** The tooltip of the page: the header names the city, the day and that day's grade. */
private fun categoriesTooltip(): TooltipFormatter =
    TooltipFormatter(
        { params ->
            val item = params.unsafeCast<CallbackDataParams>()
            val value = item.value
            val details =
                (1..6).joinToString(separator = "") { column ->
                    SCHEMA_TEXTS[column] + "：" + textAt(value, column) + "<br>"
                }
            TooltipContent(
                "<div style=\"border-bottom: 1px solid #ccc;font-size: 18px;padding-bottom: 7px;" +
                    "margin-bottom: 7px\">" +
                    item.seriesName + " " + textAt(value, 0) + "日：" + textAt(value, GRADE_COLUMN) +
                    "</div>" + details,
            )
        },
    )

/** The one visual map of the page, mapping the grade column by category name. */
private fun categoriesVisualMap(): VisualMapOption =
    unsafeJso {
        // Upstream clears the default `left` with null so that only `right` positions the component.
        left = null
        right = StringOrDouble(0.0)
        dimension = GRADE_COLUMN.toDouble()
        categories = GRADE_CATEGORIES
        selected = recordOf("严重污染" to false, "重度污染" to false)
        inRange =
            unsafeJso<VisualOptionUnit> {
                symbolSize = DoubleOrDoubleArray(30.0)
                categorySymbol = recordOf("优" to "diamond", "" to "circle")
            }
        outOfRange =
            unsafeJso<VisualOptionUnit> {
                color = StringOrStringArray("#000")
                categorySymbolSize = recordOf("优" to 50.0, "" to 30.0)
                categorySymbol = recordOf("优" to "diamond", "" to "circle")
            }
    }

/** The bubble style all three series share: soft edges and a drop shadow. */
private fun categoriesItemStyle(): ItemStyleOption =
    unsafeJso {
        opacity = 0.8
        shadowBlur = 10.0
        shadowOffsetX = 0.0
        shadowOffsetY = 0.0
        shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
    }

/** One scatter series of the page. */
private fun categoriesSeries(
    name: String,
    rows: Array<Array<OptionDataValue?>>,
): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        itemStyle = categoriesItemStyle()
        typedData = categoriesData(rows)
    }

val scatterVisualMapCategoriesEntries: List<CatalogEntry> =
    listOf(
        scatterVisualMapCategories.chart(
            "doc-example/scatter-visualMap-categories",
            "散点图与视觉映射（类目）",
            "Scatter with visualMap (categories)",
            "三个城市 31 天的空气质量按等级映射形状与大小：`categories` 给出等级顺序，" +
                "`inRange.symbol` 用字典把「优」换成菱形、空串 `''` 是其余等级的默认值，" +
                "`selected` 一开始就关掉「严重污染」与「重度污染」，它们因此取 `outOfRange` 的黑色大号。",
            "Air quality of three cities, encoded by the grade of each day: `categories` orders the " +
                "grades, the `inRange.symbol` dictionary turns 优 into a diamond with the empty key as " +
                "the default for every other grade, and `selected` starts with the two worst grades " +
                "switched off, which drops them to the black, oversized `outOfRange` visuals.",
        ) {
            unsafeJso<EChartsOption> {
                color = Array(CATEGORIES_COLORS.size) { ZRColor(CATEGORIES_COLORS[it]) }
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            top = StringOrDouble(10.0)
                            data = legendNames(*CATEGORIES_CITIES)
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble(200.0)
                            top = StringOrDouble("15%")
                            bottom = StringOrDouble("10%")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            padding = DoubleOrDoubleArray(10.0)
                            borderColor = ZRColor("#777")
                            borderWidth = 1.0
                            formatter = categoriesTooltip()
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            name = "日期"
                            nameGap = 16.0
                            nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(14.0) }
                            max = AxisMinMax(31.0)
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value}") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "AQI指数"
                            nameLocation = AxisNameLocations.end
                            nameGap = 20.0
                            nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(20.0) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                visualMap = one(categoriesVisualMap())
                series =
                    arrayOf<SeriesOption>(
                        categoriesSeries(CATEGORIES_CITIES[0], CATEGORIES_BEIJING),
                        categoriesSeries(CATEGORIES_CITIES[1], SCATTER_AQI_SHANGHAI),
                        categoriesSeries(CATEGORIES_CITIES[2], SCATTER_AQI_GUANGZHOU),
                    )
            }
        },
    )
