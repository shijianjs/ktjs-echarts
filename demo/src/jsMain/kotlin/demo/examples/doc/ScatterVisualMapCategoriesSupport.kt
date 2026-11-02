package demo.examples.doc

import echarts.*
import echarts.options.*
import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor
import demo.examples.chord.column
import demo.examples.matrix.dimension
import demo.examples.matrix.names
import demo.examples.matrix.separator
import demo.examples.scatter.SCATTER_AQI_BEIJING
import demo.examples.shared.every
import demo.examples.shared.row
import demo.examples.shared.textAt

/** The three series names, in the order of the legend. */
internal val CATEGORIES_CITIES = arrayOf("北京", "上海", "广州")

/** The colour of every series, in the order of [CATEGORIES_CITIES]. */
internal val CATEGORIES_COLORS = arrayOf("#dd4444", "#fec42c", "#80F1BE")

/** The grade column of a row, which is the dimension the visual map reads. */
internal const val GRADE_COLUMN = 7

/** The CO column, and the Beijing row whose CO the two upstream tables disagree about. */
internal const val CO_COLUMN = 4

internal const val MISSING_CO_ROW = 8

/** The columns the tooltip prints, in the order of the upstream `schema`. */
internal val SCHEMA_TEXTS =
    arrayOf("日", "AQI指数", "PM2.5", "PM10", "一氧化碳（CO）", "二氧化氮（NO₂）", "二氧化硫（SO₂）")

/** The grades, worst first, in the order upstream hands them to `categories`. */
internal val GRADE_CATEGORIES = arrayOf("严重污染", "重度污染", "中度污染", "轻度污染", "良", "优")

/** Beijing with the `-` of this example: a copy, so the table of `scatter-aqi-color` stays as it is. */
internal val CATEGORIES_BEIJING: Array<Array<OptionDataValue?>> =
    Array(SCATTER_AQI_BEIJING.size) { index ->
        SCATTER_AQI_BEIJING[index].copyOf().also { values ->
            if (index == MISSING_CO_ROW) {
                values[CO_COLUMN] = OptionDataValue("-")
            }
        }
    }

/** Rows of one city as scatter items; upstream hands the raw 8-element rows to the series. */
internal fun categoriesData(rows: Array<Array<OptionDataValue?>>): Array<ScatterDataItem?> =
    Array(rows.size) { index -> ScatterDataItem(rows[index]) }

/** The tooltip of the page: the header names the city, the day and that day's grade. */
internal fun categoriesTooltip(): TooltipFormatter =
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
internal fun categoriesVisualMap(): VisualMapOption =
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
internal fun categoriesItemStyle(): ItemStyleOption =
    unsafeJso {
        opacity = 0.8
        shadowBlur = 10.0
        shadowOffsetX = 0.0
        shadowOffsetY = 0.0
        shadowColor = ZRColor("rgba(0, 0, 0, 0.5)")
    }

/** One scatter series of the page. */
internal fun categoriesSeries(
    name: String,
    rows: Array<Array<OptionDataValue?>>,
): ScatterSeriesOption =
    scatterSeries {
        this.name = name
        itemStyle = categoriesItemStyle()
        typedData = categoriesData(rows)
    }
