package demo.examples.dataset

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import demo.examples.Category

private val dataTransformFilterCategory = Category(Groups.DATASET, "examples/dataset/DataTransformFilter.kt")

val dataTransformFilterEntry: CatalogEntry =
    dataTransformFilterCategory.interactive(
            "data-transform-filter",
            "数据过滤",
            "Data Transform Filter",
            summaryZh = "两个 filter 变换各自从原始数据集里筛出 1950 年之后的德国、法国收入，两条折线直接读派生数据集。",
            summaryEn = "Two filter transforms pick the German and French income since 1950 out of the raw table; each line reads its own derived dataset.",
        ) { chart ->
            loadJson("data/life-expectancy-table.json").then { json ->
                chart.setOption(
                    unsafeJso<EChartsOption> {
                        dataset =
                            arrayOf(
                                unsafeJso<DatasetOption> {
                                    id = "dataset_raw"
                                    source = json.unsafeCast<DatasetSource>()
                                },
                                filteredSince1950("dataset_since_1950_of_germany", "Germany"),
                                filteredSince1950("dataset_since_1950_of_france", "France"),
                            )
                        title = one(unsafeJso<TitleOption> { text = "Income of Germany and France since 1950" })
                        tooltip = one(unsafeJso<TooltipOption> { trigger = TooltipTriggers.axis })
                        xAxis =
                            one(
                                unsafeJso<XAxisOption> {
                                    type = AxisTypes.category
                                    nameLocation = AxisNameLocations.middle
                                },
                            )
                        yAxis = one(unsafeJso<YAxisOption> { name = "Income" })
                        series =
                            arrayOf<SeriesOption>(
                                incomeLine("dataset_since_1950_of_germany"),
                                incomeLine("dataset_since_1950_of_france"),
                            )
                    },
                )
                null
            }
        }
