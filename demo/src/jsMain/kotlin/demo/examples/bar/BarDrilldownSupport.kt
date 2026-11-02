package demo.examples.bar

import echarts.options.AnimationDuration
import echarts.AxisTypes
import echarts.options.BarDataItem
import echarts.options.BarSeriesOption
import echarts.options.BooleanOrUniversalTransitionOption
import echarts.options.DimensionDefinitionLoose
import echarts.options.DimensionLoose
import echarts.options.EChartsOption
import echarts.options.GraphicComponentLooseOption
import echarts.options.GraphicElement
import echarts.options.OptionDataValue
import echarts.options.OptionEncode
import echarts.options.OptionEncodeValue
import echarts.options.SeriesOption
import echarts.options.StringOrDouble
import echarts.options.UniversalTransitionOption
import echarts.options.XAxisOption
import echarts.options.YAxisOption
import echarts.options.barSeries
import echarts.options.dataItems
import echarts.options.one
import js.objects.unsafeJso
import zrender.ZRColor
import zrender.ZRStyleProps
import demo.examples.matrix.names
import demo.examples.shared.every
import demo.examples.shared.row

/** One bar of a level: its name, value and the level its group drills into (`null` at the bottom). */
internal class DrillRow(val name: String, val value: Double, val child: String?)

/** One level of the drilldown, identified by the `groupId` shared by its rows. */
internal class DrillLevel(val id: String, val rows: List<DrillRow>) {
    /** The data row at [index] as `[name, value, groupId, childGroupId?]`. */
    fun columns(index: Int): Array<OptionDataValue?> {
        val item = rows[index]
        return if (item.child == null) {
            row(item.name, item.value, id)
        } else {
            row(item.name, item.value, id, item.child)
        }
    }
}

/** Every level of `bar-multi-drilldown`, in the upstream `allLevelData` order. */
internal val DRILL_LEVELS =
    listOf(
        DrillLevel(
            "things",
            listOf(
                DrillRow("Animals", 3.0, "animals"),
                DrillRow("Fruits", 3.0, "fruits"),
                DrillRow("Cars", 2.0, "cars"),
            ),
        ),
        DrillLevel(
            "animals",
            listOf(
                DrillRow("Dogs", 3.0, "dogs"),
                DrillRow("Cats", 4.0, "cats"),
                DrillRow("Birds", 3.0, "birds"),
            ),
        ),
        DrillLevel(
            "fruits",
            listOf(
                DrillRow("Pomes", 3.0, "pomes"),
                DrillRow("Berries", 4.0, "berries"),
                DrillRow("Citrus", 9.0, "citrus"),
            ),
        ),
        DrillLevel(
            "cars",
            listOf(
                DrillRow("SUV", 5.0, "suv"),
                DrillRow("Sports", 3.0, "sports"),
            ),
        ),
        DrillLevel(
            "dogs",
            listOf(
                DrillRow("Corgi", 5.0, null),
                DrillRow("Bulldog", 6.0, null),
                DrillRow("Shiba Inu", 7.0, null),
            ),
        ),
        DrillLevel(
            "cats",
            listOf(
                DrillRow("American Shorthair", 2.0, null),
                DrillRow("British Shorthair", 9.0, null),
                DrillRow("Bengal", 2.0, null),
                DrillRow("Birman", 2.0, null),
            ),
        ),
        DrillLevel(
            "birds",
            listOf(
                DrillRow("Goose", 1.0, null),
                DrillRow("Owl", 2.0, null),
                DrillRow("Eagle", 8.0, null),
            ),
        ),
        DrillLevel(
            "pomes",
            listOf(
                DrillRow("Apple", 9.0, null),
                DrillRow("Pear", 2.0, null),
                DrillRow("Kiwi", 1.0, null),
            ),
        ),
        DrillLevel(
            "berries",
            listOf(
                DrillRow("Blackberries", 7.0, null),
                DrillRow("Cranberries", 2.0, null),
                DrillRow("Strawberries", 9.0, null),
                DrillRow("Grapes", 4.0, null),
            ),
        ),
        DrillLevel(
            "citrus",
            listOf(
                DrillRow("Oranges", 3.0, null),
                DrillRow("Grapefruits", 7.0, null),
                DrillRow("Tangerines", 8.0, null),
                DrillRow("Lemons", 7.0, null),
                DrillRow("Limes", 3.0, null),
                DrillRow("Kumquats", 2.0, null),
                DrillRow("Citrons", 3.0, null),
                DrillRow("Tengelows", 3.0, null),
                DrillRow("Uglifruit", 1.0, null),
            ),
        ),
        DrillLevel(
            "suv",
            listOf(
                DrillRow("Mazda CX-30", 7.0, null),
                DrillRow("BMW X2", 7.0, null),
                DrillRow("Ford Bronco Sport", 2.0, null),
                DrillRow("Toyota RAV4", 9.0, null),
                DrillRow("Porsche Macan", 4.0, null),
            ),
        ),
        DrillLevel(
            "sports",
            listOf(
                DrillRow("Porsche 718 Cayman", 2.0, null),
                DrillRow("Porsche 911 Turbo", 2.0, null),
                DrillRow("Ferrari F8", 4.0, null),
            ),
        ),
    )

/** The level with [id]; every `childGroupId` of [DRILL_LEVELS] names one. */
internal fun drillLevel(id: String): DrillLevel = DRILL_LEVELS.first { it.id == id }

/** The `Back` caption of `bar-multi-drilldown`; the root level shows it too, as upstream does. */
internal fun drillBackElement(onBack: () -> Unit): GraphicComponentLooseOption =
    GraphicComponentLooseOption(
        unsafeJso<GraphicElement> {
            type = "text"
            left = StringOrDouble(50.0)
            top = StringOrDouble(20.0)
            style =
                unsafeJso<ZRStyleProps> {
                    text = "Back"
                    fontSize = StringOrDouble(18.0)
                    fill = ZRColor("grey")
                }
            onclick = { onBack() }
        },
    )

/** The bar series of one level: four data columns matched by `dimensions` and `encode`. */
internal fun drillBars(level: DrillLevel): BarSeriesOption =
    barSeries {
        dimensions =
            arrayOf(
                DimensionDefinitionLoose("x"),
                DimensionDefinitionLoose("y"),
                DimensionDefinitionLoose("groupId"),
                DimensionDefinitionLoose("childGroupId"),
            )
        encode =
            unsafeJso<OptionEncode> {
                x = OptionEncodeValue(DimensionLoose("x"))
                y = OptionEncodeValue(DimensionLoose("y"))
                itemGroupId = OptionEncodeValue(DimensionLoose("groupId"))
                itemChildGroupId = OptionEncodeValue(DimensionLoose("childGroupId"))
            }
        typedData =
            dataItems(*Array(level.rows.size) { index -> BarDataItem(level.columns(index)) })
        universalTransition =
            BooleanOrUniversalTransitionOption(
                unsafeJso<UniversalTransitionOption> {
                    enabled = true
                    divideShape = "clone"
                },
            )
    }

/** The option of one level of `bar-multi-drilldown`. */
internal fun drillOption(level: DrillLevel, onBack: () -> Unit): EChartsOption =
    unsafeJso {
        xAxis = one(unsafeJso<XAxisOption> { type = AxisTypes.category })
        yAxis = one(unsafeJso<YAxisOption> { minInterval = 1.0 })
        animationDurationUpdate = AnimationDuration(500.0)
        series = arrayOf<SeriesOption>(drillBars(level))
        graphic = arrayOf(drillBackElement(onBack))
    }
