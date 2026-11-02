package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category

private val docExampleSunburstSimpleCategory = Category(Groups.DOC, "examples/doc/DocExampleSunburstSimple.kt")

val docExampleSunburstSimpleEntry: CatalogEntry =
    docExampleSunburstSimpleCategory.chart("doc-example/sunburst-simple", "旭日图基础", "Sunburst basics") {
            unsafeJso<EChartsOption> {
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData =
                                arrayOf<SunburstSeriesNodeItemOption?>(
                                    sunburstNode(
                                        "A",
                                        10.0,
                                        arrayOf(sunburstNode("Aa", 3.0), sunburstNode("Ab", 5.0)),
                                    ),
                                    sunburstNode(
                                        "B",
                                        children =
                                            arrayOf(sunburstNode("Ba", 4.0), sunburstNode("Bb", 2.0)),
                                    ),
                                    sunburstNode("C", 3.0),
                                )
                        },
                    )
            }
        }
