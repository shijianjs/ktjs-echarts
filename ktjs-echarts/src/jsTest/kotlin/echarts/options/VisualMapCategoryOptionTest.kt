package echarts.options

import echarts.asText
import echarts.createChart
import echarts.destroyChart
import echarts.finder
import js.objects.recordOf
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Coverage of the category-indexed forms of a visual map: `categories` with `selected`, and the
 * dictionary form of the `inRange` / `outOfRange` visuals.
 *
 * Upstream declares those visuals as `{ [key in BuiltinVisualProperty]?: any }`, so the dictionary
 * only shows up in the implementation: `preprocessForSpecifiedCategory` in
 * `src/visual/VisualMapping.ts` hashes the categories and files a key that is not one of them at
 * index `-1`, which is why upstream's own examples use `''` as the fallback entry.
 */
class VisualMapCategoryOptionTest {
    @Test
    fun dictionaryVisualsMapCategories() {
        val (chart, container) = createChart()
        try {
            chart.setOption(
                unsafeJso<EChartsOption> {
                    xAxis = arrayOf(unsafeJso<XAxisOption> { })
                    yAxis = arrayOf(unsafeJso<YAxisOption> { })
                    series =
                        arrayOf<SeriesOption>(
                            scatterSeries {
                                typedData =
                                    arrayOf(
                                        gradedPoint(1.0, 10.0, "优"),
                                        gradedPoint(2.0, 20.0, "良"),
                                        gradedPoint(3.0, 30.0, "重度污染"),
                                    )
                            },
                        )
                    visualMap = arrayOf(categoryVisualMap())
                },
            )

            // "优" is selected and named by the dictionaries.
            assertEquals("diamond", chart.getVisual(finder { dataIndex = 0.0 }, "symbol").asText())
            assertEquals(30.0, chart.getVisual(finder { dataIndex = 0.0 }, "symbolSize").unsafeCast<Double>())
            // "良" is selected but only the fallback entry covers it.
            assertEquals("circle", chart.getVisual(finder { dataIndex = 1.0 }, "symbol").asText())
            // "重度污染" is deselected, so the outOfRange visuals apply to it.
            assertEquals("circle", chart.getVisual(finder { dataIndex = 2.0 }, "symbol").asText())
            assertEquals("#000", chart.getVisual(finder { dataIndex = 2.0 }, "color").asText())

            val visualMap = chart.getOption().visualMap!![0]
            assertEquals(
                listOf("严重污染", "重度污染", "中度污染", "轻度污染", "良", "优"),
                visualMap.categories.unsafeCast<Array<String>?>()?.toList(),
            )
            assertEquals(false, visualMap.selected?.get("重度污染"))
            assertEquals("diamond", visualMap.inRange?.categorySymbol?.get("优"))
            assertEquals("circle", visualMap.inRange?.categorySymbol?.get(""))
            assertEquals(50.0, visualMap.outOfRange?.categorySymbolSize?.get("优"))
            assertEquals("#fff", visualMap.inRange?.categoryColor?.get("优"))
        } finally {
            destroyChart(chart, container)
        }
    }

    /** The category visual map of the upstream `scatter-visualMap-categories` example. */
    private fun categoryVisualMap(): VisualMapOption =
        unsafeJso {
            right = StringOrDouble(0.0)
            dimension = 2.0
            categories = arrayOf("严重污染", "重度污染", "中度污染", "轻度污染", "良", "优")
            selected = recordOf("严重污染" to false, "重度污染" to false)
            inRange =
                unsafeJso<VisualOptionUnit> {
                    symbolSize = DoubleOrDoubleArray(30.0)
                    categorySymbol = recordOf("优" to "diamond", "" to "circle")
                    categoryColor = recordOf("优" to "#fff", "" to "#ccc")
                }
            outOfRange =
                unsafeJso<VisualOptionUnit> {
                    color = StringOrStringArray("#000")
                    categorySymbolSize = recordOf("优" to 50.0, "" to 30.0)
                    categorySymbol = recordOf("优" to "diamond", "" to "circle")
                }
        }
}

/** A scatter row of `[x, y, grade]`: the grade is the dimension the visual map reads. */
private fun gradedPoint(
    x: Double,
    y: Double,
    grade: String,
): ScatterDataItem? =
    ScatterDataItem(arrayOf<OptionDataValue?>(OptionDataValue(x), OptionDataValue(y), OptionDataValue(grade)))
