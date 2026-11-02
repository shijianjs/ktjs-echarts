package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import demo.examples.Category
import demo.examples.sunburst.radius

private val barStackBorderRadiusCategory = Category(Groups.BAR, "examples/bar/BarStackBorderRadius.kt")

val barStackBorderRadiusEntry: CatalogEntry =
    barStackBorderRadiusCategory.chart(
            "bar-stack-borderRadius",
            "带圆角的堆积柱状图",
            "Stacked Bar with borderRadius",
            "两条堆叠序列里留着 `-` 空位，只有真正封住栈顶的那根柱子带圆角，圆角写在每个数据项的 itemStyle 上。",
            "Two stack groups with `-` holes: only the bar that really closes a stack gets its corner " +
                "radius, which is written on the item style of that single data item.",
        ) {
            unsafeJso<EChartsOption> {
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.category
                            data = strings(*AD_DAYS)
                        },
                    )
                yAxis = one(unsafeJso<YAxisOption> { type = AxisTypes.value })
                series = Array<SeriesOption>(RADIUS_NAMES.size) { index -> radiusSeries(index) }
            }
        }
