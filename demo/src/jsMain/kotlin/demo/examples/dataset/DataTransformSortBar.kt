package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import echarts.AxisTypes
import echarts.options.AxisInterval
import echarts.options.AxisLabelOption
import echarts.options.DimensionDefinitionLoose
import echarts.options.EChartsOption
import echarts.options.SeriesOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.one
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.matrix.dimension
import demo.examples.shared.axisLabel
import demo.examples.shared.row

private val dataTransformSortBarCategory = Category(Groups.DATASET, "examples/dataset/DataTransformSortBar.kt")

val dataTransformSortBarEntry: CatalogEntry =
    dataTransformSortBarCategory.chart("data-transform-sort-bar", "柱状图排序", "Sort Data in Bar Chart") {
            unsafeJso<EChartsOption> {
                dataset =
                    arrayOf(
                        datasetOf {
                            dimensions =
                                arrayOf(
                                    DimensionDefinitionLoose("name"),
                                    DimensionDefinitionLoose("age"),
                                    DimensionDefinitionLoose("profession"),
                                    DimensionDefinitionLoose("score"),
                                    DimensionDefinitionLoose("date"),
                                )
                            source =
                                tableSource(
                                    arrayOf(
                                        row("Hannah Krause", 41, "Engineer", 314, "2011-02-12"),
                                        row("Zhao Qian", 20, "Teacher", 351, "2011-03-01"),
                                        row("Jasmin Krause ", 52, "Musician", 287, "2011-02-14"),
                                        row("Li Lei", 37, "Teacher", 219, "2011-02-18"),
                                        row("Karle Neumann", 25, "Engineer", 253, "2011-04-02"),
                                        row("Adrian Groß", 19, "Teacher", "-", "2011-01-16"),
                                        row("Mia Neumann", 71, "Engineer", 165, "2011-03-19"),
                                        row("Böhm Fuchs", 36, "Musician", 318, "2011-02-24"),
                                        row("Han Meimei", 67, "Engineer", 366, "2011-03-12"),
                                    ),
                                )
                        },
                        datasetOf {
                            transform =
                                arrayOf(transform("sort", "dimension" to ("score" as JsAny), "order" to ("desc" as JsAny)))
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            axisLabel = unsafeJso<AxisLabelOption> { interval = AxisInterval(0.0); rotate = 30.0 }
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> {})
                series = arrayOf<SeriesOption>(barSeries { encode = xyEncode("name", "score"); datasetIndex = 1.0 })
            }
        }
