package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import demo.examples.Category
import demo.examples.shared.mixTimelineYears

private val docExampleMixTimelineAllCategory = Category(Groups.DOC, "examples/doc/DocExampleMixTimelineAll.kt")

val docExampleMixTimelineAllEntry: CatalogEntry =
    docExampleMixTimelineAllCategory.chart(
            "doc-example/mix-timeline-all",
            "时间轴：全国宏观经济指标",
            "Timeline of the national macro-economic indicators",
        ) {
            unsafeJso {
                baseOption = mixTimelineBaseOption()
                options = Array(mixTimelineYears.size) { index -> mixTimelineYearOption(index) }
            }
        }
