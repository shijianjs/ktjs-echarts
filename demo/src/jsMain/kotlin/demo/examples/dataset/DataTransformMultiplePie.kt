package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.MediaQueryOption
import io.github.shijianjs.ktjs.echarts.options.MediaUnit
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.row

private val dataTransformMultiplePieCategory = Category(Groups.DATASET, "examples/dataset/DataTransformMultiplePie.kt")

val dataTransformMultiplePieEntry: CatalogEntry =
    dataTransformMultiplePieCategory.chart("data-transform-multiple-pie", "分割数据到数个饼图", "Partition Data to Pies") {
            unsafeJso<EChartsOption> {
                dataset =
                    arrayOf(
                        rowsDataset(
                            arrayOf(
                                row("Product", "Sales", "Price", "Year"),
                                row("Cake", 123, 32, 2011),
                                row("Cereal", 231, 14, 2011),
                                row("Tofu", 235, 5, 2011),
                                row("Dumpling", 341, 25, 2011),
                                row("Biscuit", 122, 29, 2011),
                                row("Cake", 143, 30, 2012),
                                row("Cereal", 201, 19, 2012),
                                row("Tofu", 255, 7, 2012),
                                row("Dumpling", 241, 27, 2012),
                                row("Biscuit", 102, 34, 2012),
                                row("Cake", 153, 28, 2013),
                                row("Cereal", 181, 21, 2013),
                                row("Tofu", 395, 4, 2013),
                                row("Dumpling", 281, 31, 2013),
                                row("Biscuit", 92, 39, 2013),
                                row("Cake", 223, 29, 2014),
                                row("Cereal", 211, 17, 2014),
                                row("Tofu", 345, 3, 2014),
                                row("Dumpling", 211, 35, 2014),
                                row("Biscuit", 72, 24, 2014),
                            ),
                        ),
                        datasetOf { transform = arrayOf(yearFilter(2011.0)) },
                        datasetOf { transform = arrayOf(yearFilter(2012.0)) },
                        datasetOf { transform = arrayOf(yearFilter(2013.0)) },
                    )
                series =
                    arrayOf<SeriesOption>(
                        stackedYearPie("50%", "25%", 1.0),
                        stackedYearPie("50%", "50%", 2.0),
                        stackedYearPie("50%", "75%", 3.0),
                    )
                // Only for the responsive layout: the pies sit in a row on a wide canvas.
                media =
                    arrayOf(
                        unsafeJso<MediaUnit> {
                            query = unsafeJso<MediaQueryOption> { minAspectRatio = 1.0 }
                            option = mediaCenters("25%", "50%", "75%")
                        },
                        unsafeJso<MediaUnit> { option = mediaCenters("50%", "25%", "50%") },
                    )
            }
        }
