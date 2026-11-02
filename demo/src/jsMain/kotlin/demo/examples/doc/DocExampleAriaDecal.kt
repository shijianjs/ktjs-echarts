package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import zrender.ZRColor
import demo.examples.Category

private val docExampleAriaDecalCategory = Category(Groups.DOC, "examples/doc/DocExampleAriaDecal.kt")

val docExampleAriaDecalEntry: CatalogEntry =
    docExampleAriaDecalCategory.chart(
            "doc-example/aria-decal",
            "无障碍图案（decal）一览",
            "Accessibility decal patterns",
        ) {
            val decalGallery = ariaDecals()
            unsafeJso<EChartsOption> {
                xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.value; max = AxisMinMax(1.0); show = false })
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value; show = false })
                color = arrayOf(ZRColor("#ddd"))
                aria =
                    unsafeJso<AriaOption> {
                        enabled = true
                        decal =
                            unsafeJso<AriaDecalOption> {
                                show = true
                                decals = DecalList(decalGallery)
                            }
                    }
                series = Array<SeriesOption>(decalGallery.size) { index -> decalSwatch(index) }
            }
        }
