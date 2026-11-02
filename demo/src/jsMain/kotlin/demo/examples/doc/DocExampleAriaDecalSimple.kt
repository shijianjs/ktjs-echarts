package demo.examples.doc

import demo.CatalogEntry
import echarts.*
import echarts.options.*
import demo.Groups
import js.objects.unsafeJso
import demo.examples.Category

private val docExampleAriaDecalSimpleCategory = Category(Groups.DOC, "examples/doc/DocExampleAriaDecalSimple.kt")

val docExampleAriaDecalSimpleEntry: CatalogEntry =
    docExampleAriaDecalSimpleCategory.chart("doc-example/aria-decal-simple", "无障碍花纹（简单）", "Aria decal (simple)") {
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series =
                    arrayOf<SeriesOption>(
                        barSeries { typedData = numbers(120.0, 200.0, 150.0, 80.0, 70.0, 110.0, 130.0) },
                        barSeries { typedData = numbers(20.0, 40.0, 90.0, 40.0, 30.0, 70.0, 120.0) },
                        barSeries { typedData = numbers(140.0, 230.0, 120.0, 50.0, 30.0, 150.0, 120.0) },
                    )
                aria =
                    unsafeJso<AriaOption> {
                        enabled = true
                        decal = unsafeJso<AriaDecalOption> { show = true }
                    }
            }
        }
