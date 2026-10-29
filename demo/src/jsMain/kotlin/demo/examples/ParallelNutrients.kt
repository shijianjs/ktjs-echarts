package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * `parallel-nutrients` from `public/examples/ts/parallel-nutrients.ts` of apache/echarts-examples.
 *
 * Every one of the 7637 rows of `data/nutrients.json` becomes a polyline across fifteen axes; the
 * piecewise visual map colours the lines by food group, in the group order the normalization
 * produces. The table and the normalization are shared with the two nutrient scatters
 * (`ScatterNutrients.kt`).
 */

private val parallelNutrients = Category(Groups.PARALLEL, "examples/ParallelNutrients.kt")

/** The fifteen dimensions of the page, in the order of the upstream `parallelAxis` list. */
private val PARALLEL_NUTRIENT_AXES =
    arrayOf(
        NUTRIENT_ID to "id",
        NUTRIENT_PROTEIN to "protein",
        4 to "sodium",
        NUTRIENT_CALCIUM to "calcium",
        NUTRIENT_FIBER to "fiber",
        6 to "vitaminc",
        NUTRIENT_POTASSIUM to "potassium",
        NUTRIENT_CARBOHYDRATE to "carbohydrate",
        9 to "sugars",
        10 to "fat",
        11 to "water",
        12 to "calories",
        13 to "saturated",
        14 to "monounsat",
        15 to "polyunsat",
    )

/** One row as a parallel item: the name and the group stay strings, the other 15 cells are numbers. */
private fun parallelNutrientItem(row: Array<JsAny>): ParallelSeriesDataItem =
    ParallelSeriesDataItem(
        Array(row.size) { column ->
            if (column == NUTRIENT_NAME || column == NUTRIENT_GROUP) {
                OptionDataValue(textAt(row, column))
            } else {
                OptionDataValue(numberAt(row, column))
            }
        },
    )

/** One axis of the page: its dimension, its name, and `scale` on the id axis only. */
private fun parallelNutrientAxis(dim: Int, name: String): ParallelAxisOption =
    unsafeJso {
        this.dim = IndexOrIndexArray(dim.toDouble())
        this.name = name
        if (dim == NUTRIENT_ID) scale = true
        nameLocation = AxisNameLocations.end
    }

/** The shared axis defaults of the page. */
private fun parallelNutrientAxisDefaults(): ParallelAxisOption =
    unsafeJso {
        type = AxisTypes.value
        name = "nutrients"
        nameLocation = AxisNameLocations.end
        nameGap = 20.0
        nameTextStyle =
            unsafeJso<LabelOption> {
                color = "#fff"
                fontSize = StringOrDouble(14.0)
            }
        axisLine = unsafeJso<AxisLineOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#aaa") } }
        axisTick = unsafeJso<AxisTickOption> { lineStyle = unsafeJso<LineStyleOption> { color = ZRColor("#777") } }
        splitLine = unsafeJso<SplitLineOption> { show = false }
        axisLabel = unsafeJso<AxisLabelOption> { color = "#fff" }
        realtime = false
    }

/** The option of `parallel-nutrients`; [table] is the whole normalized nutrient file. */
private fun parallelNutrientsOption(table: NutrientTable): EChartsOption =
    unsafeJso<EChartsOption> {
        backgroundColor = ZRColor("#333")
        tooltip =
            one(
                unsafeJso<TooltipOption> {
                    padding = DoubleOrDoubleArray(10.0)
                    backgroundColor = ZRColor("#222")
                    borderColor = ZRColor("#777")
                    borderWidth = 1.0
                },
            )
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "Groups"
                    top = StringOrDouble(0.0)
                    left = StringOrDouble(0.0)
                    textStyle = unsafeJso<TextStyleOption> { color = "#fff" }
                },
            )
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    show = true
                    type = VisualMapTypes.piecewise
                    categories = strings<OptionDataItem>(*table.groups).unsafeCast<JsAny>()
                    dimension = NUTRIENT_GROUP.toDouble()
                    inRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(table.colors) }
                    outOfRange = unsafeJso<VisualOptionUnit> { color = StringOrStringArray(arrayOf("#ccc")) }
                    top = StringOrDouble(20.0)
                    textStyle = unsafeJso<LabelOption> { color = "#fff" }
                    realtime = false
                },
            )
        parallelAxis =
            Array(PARALLEL_NUTRIENT_AXES.size) { index ->
                val (dim, name) = PARALLEL_NUTRIENT_AXES[index]
                parallelNutrientAxis(dim, name)
            }
        parallel =
            one(
                unsafeJso<ParallelOption> {
                    left = StringOrDouble(280.0)
                    top = StringOrDouble(20.0)
                    width = StringOrDouble(400.0)
                    layout = Orients.vertical
                    parallelAxisDefault = parallelNutrientAxisDefaults()
                },
            )
        animation = false
        series =
            one<SeriesOption>(
                parallelSeries {
                    name = "nutrients"
                    lineStyle =
                        unsafeJso<LineStyleOption> {
                            width = 0.5
                            opacity = 0.05
                        }
                    inactiveOpacity = 0.0
                    activeOpacity = 0.01
                    progressive = 500.0
                    smooth = BooleanOrDouble(true)
                    typedData = Array(table.rows.size) { index -> parallelNutrientItem(table.rows[index]) }
                },
            )
    }

val parallelNutrientsEntries: List<CatalogEntry> =
    listOf(
        parallelNutrients.interactive(
            "parallel-nutrients",
            "营养结构（平行坐标）",
            "Parallel Nutrients",
            "整张营养表铺在十五条平行轴上，每条折线是一种食品；右侧的 visualMap 按 25 个食品类别着色，" +
                "鼠标悬停会把其它折线淡出。",
            "The whole nutrient table laid across fifteen parallel axes, one polyline per food; the " +
                "piecewise visual map colours the lines by food group and hovering fades the rest.",
        ) { chart ->
            loadJson("data/nutrients.json").then { json ->
                chart.setOption(parallelNutrientsOption(normalizeNutrients(json, Int.MAX_VALUE)))
                null
            }
        },
    )
