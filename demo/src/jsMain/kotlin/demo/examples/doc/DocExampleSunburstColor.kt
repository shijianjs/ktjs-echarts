package demo.examples.doc

import demo.CatalogEntry
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category

private val docExampleSunburstColorCategory = Category(Groups.DOC, "examples/doc/DocExampleSunburstColor.kt")

val docExampleSunburstColorEntry: CatalogEntry =
    docExampleSunburstColorCategory.chart("doc-example/sunburst-color", "旭日图配色", "Sunburst colour") {
            val a =
                docSunburstNode(
                    name = "A",
                    value = 10.0,
                    children =
                        arrayOf(
                            docSunburstNode(name = "Aa", value = 3.0, color = "red"),
                            docSunburstNode(name = "Ab", value = 5.0),
                        ),
                )
            val b =
                docSunburstNode(
                    name = "B",
                    color = "red",
                    children = arrayOf(docSunburstNode(name = "Ba", value = 4.0), docSunburstNode(name = "Bb", value = 2.0)),
                )
            unsafeJso<EChartsOption> {
                series =
                    one(
                        sunburstSeries {
                            typedData = dataItems(a, b, docSunburstNode(name = "C", value = 3.0))
                            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("#aaa") }
                            levels =
                                arrayOf(
                                    unsafeJso<SunburstSeriesLevelOption> {},
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor("blue") }
                                    },
                                )
                        },
                    )
            }
        }
