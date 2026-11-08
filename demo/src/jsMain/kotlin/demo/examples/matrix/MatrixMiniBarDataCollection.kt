package demo.examples.matrix

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.DatasetOption
import io.github.shijianjs.ktjs.echarts.options.OptionDataValue
import io.github.shijianjs.ktjs.echarts.options.datasetArrayRows
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LabelOption
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.one
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRFontWeights
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.shared.row

private val matrixMiniBarDataCollectionCategory = Category(Groups.MATRIX, "examples/matrix/MatrixMiniBarDataCollection.kt")

val matrixMiniBarDataCollectionEntry: CatalogEntry =
    matrixMiniBarDataCollectionCategory.chart(
            "matrix-mini-bar-data-collection",
            "矩阵坐标系表头数据自动收集（以微型条形图为例）",
            "Matrix Header Data Collection (Mini Bar)",
        ) {
            unsafeJso<EChartsOption> {
                dataset =
                    one(
                        unsafeJso<DatasetOption> {
                            source =
                                datasetArrayRows(
                                    arrayOf(
                                        row("2021-02-01", "amount", 1212.0, "file", 2321.0, "Q", 1412.0),
                                        row("2021-02-02", "amount", 7181.0, "file", 2114.0, "Q", 1402.0),
                                        row("2021-02-03", "amount", 2763.0, "file", 4212.0, "Q", 8172.0),
                                        row("2021-02-04", "amount", 6122.0, "file", 2942.0, "Q", 6121.0),
                                        row("2021-02-05", "amount", 4221.0, "file", 3411.0, "Q", 1987.0),
                                        row("2021-02-06", "amount", 7221.0, "file", 5121.0, "Q", 1303.0),
                                        row("2021-02-07", "amount", 5121.0, "file", 4121.0, "Q", 1819.0),
                                        row("2021-02-08", "amount", 6121.0, "file", 3121.0, "Q", 2303.0),
                                        row("2021-02-09", "amount", 7121.0, "file", 2121.0, "Q", 3303.0),
                                        row("2021-02-10", "amount", 8121.0, "file", 1121.0, "Q", 4303.0),
                                    ).unsafeCast<Array<Array<OptionDataValue>>>(),
                                )
                        },
                    )
                this.matrix =
                    matrixOption {
                        // `x` / `y` data are collected from the dataset dimensions named by each
                        // series' `encode`; only the layout of the header is set here.
                        x =
                            dimension {
                                levelSize = StringOrDouble(50.0)
                                itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#f0f8ff") }
                                label = unsafeJso<LabelOption> { fontWeight = ZRFontWeights.bold }
                            }
                    }
                series =
                    arrayOf<SeriesOption>(
                        miniBarSeries(1, 0, 2),
                        miniBarSeries(3, 0, 4),
                        miniBarSeries(5, 0, 6),
                    )
            }
        }
