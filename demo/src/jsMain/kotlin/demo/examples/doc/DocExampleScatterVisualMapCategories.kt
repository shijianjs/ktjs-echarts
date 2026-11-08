package demo.examples.doc

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.Category
import demo.examples.scatter.SCATTER_AQI_GUANGZHOU
import demo.examples.scatter.SCATTER_AQI_SHANGHAI
import demo.examples.shared.axisLabel
import demo.examples.shared.every
import demo.examples.shared.legendNames

private val docExampleScatterVisualMapCategoriesCategory = Category(Groups.DOC, "examples/doc/DocExampleScatterVisualMapCategories.kt")

val docExampleScatterVisualMapCategoriesEntry: CatalogEntry =
    docExampleScatterVisualMapCategoriesCategory.chart(
            "doc-example/scatter-visualMap-categories",
            "散点图与视觉映射（类目）",
            "Scatter with visualMap (categories)",
            "三个城市 31 天的空气质量按等级映射形状与大小：`categories` 给出等级顺序，" +
                "`inRange.symbol` 用字典把「优」换成菱形、空串 `''` 是其余等级的默认值，" +
                "`selected` 一开始就关掉「严重污染」与「重度污染」，它们因此取 `outOfRange` 的黑色大号。",
            "Air quality of three cities, encoded by the grade of each day: `categories` orders the " +
                "grades, the `inRange.symbol` dictionary turns 优 into a diamond with the empty key as " +
                "the default for every other grade, and `selected` starts with the two worst grades " +
                "switched off, which drops them to the black, oversized `outOfRange` visuals.",
        ) {
            unsafeJso<EChartsOption> {
                color = Array(CATEGORIES_COLORS.size) { ZRColor(CATEGORIES_COLORS[it]) }
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            top = StringOrDouble(10.0)
                            data = legendNames(*CATEGORIES_CITIES)
                        },
                    )
                grid =
                    one(
                        unsafeJso<GridOption> {
                            left = StringOrDouble("10%")
                            right = StringOrDouble(200.0)
                            top = StringOrDouble("15%")
                            bottom = StringOrDouble("10%")
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            padding = DoubleOrDoubleArray(10.0)
                            borderColor = ZRColor("#777")
                            borderWidth = 1.0
                            formatter = categoriesTooltip()
                        },
                    )
                xAxis =
                    one(
                        unsafeJso<XAxisOption> {
                            type = AxisTypes.value
                            name = "日期"
                            nameGap = 16.0
                            nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(14.0) }
                            max = AxisMinMax(31.0)
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                            axisLabel = unsafeJso<AxisLabelOption> { formatter = LabelFormatter("{value}") }
                        },
                    )
                yAxis =
                    one(
                        unsafeJso<YAxisOption> {
                            type = AxisTypes.value
                            name = "AQI指数"
                            nameLocation = AxisNameLocations.end
                            nameGap = 20.0
                            nameTextStyle = unsafeJso<LabelOption> { fontSize = StringOrDouble(20.0) }
                            splitLine = unsafeJso<SplitLineOption> { show = false }
                        },
                    )
                visualMap = one(categoriesVisualMap())
                series =
                    arrayOf<SeriesOption>(
                        categoriesSeries(CATEGORIES_CITIES[0], CATEGORIES_BEIJING),
                        categoriesSeries(CATEGORIES_CITIES[1], SCATTER_AQI_SHANGHAI),
                        categoriesSeries(CATEGORIES_CITIES[2], SCATTER_AQI_GUANGZHOU),
                    )
            }
        }
