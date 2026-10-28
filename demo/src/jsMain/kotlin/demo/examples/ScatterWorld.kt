package demo.examples

import demo.CatalogEntry
import demo.Groups
import demo.ui.loadJson
import echarts.ECharts
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import zrender.ZRColor

/*
 * The `scatter-world-population` page, ported from `public/examples/ts/scatter-world-population.js`.
 *
 * The country table (centroid + 2011 population + the colour of the upstream palette) lives in
 * `WorldPopulationData.kt`; the base map is the same `world.json` the lines pages register.
 */

private val scatterWorld = Category(Groups.SCATTER, "examples/ScatterWorld.kt")

/** `1347565324` -> `"1,347,565,324"`. */
private fun grouped(value: Double): String = value.toLong().toString().reversed().chunked(3).joinToString(",").reversed()

/**
 * `seriesName` / country / population.
 *
 * Upstream splits `params.value + ''` on the dot of the coordinates and ends up printing
 * `.undefined`; the third dimension is formatted directly instead.
 */
private fun populationTooltip(): TooltipOption =
    unsafeJso {
        trigger = TooltipTriggers.item
        formatter =
            TooltipFormatter { raw ->
                val params = raw.unsafeCast<CallbackDataParams>()
                val value = params.value.unsafeCast<Array<OptionDataValue>>()
                TooltipContent(
                    params.seriesName + "<br/>" + params.name + " : " + grouped(numeric(value[2])),
                )
            }
    }

private fun countryItem(country: WorldCountry): ScatterDataItem =
    ScatterDataItem(
        unsafeJso<ScatterDataItemOption> {
            name = OptionId(country.name)
            typedValue =
                OptionDataValueOrArray(
                    arrayOf(
                        OptionDataValue(country.longitude),
                        OptionDataValue(country.latitude),
                        OptionDataValue(country.value),
                    ),
                )
            emphasis =
                unsafeJso<EmphasisOption> {
                    label = unsafeJso<LabelOption> { show = true; position = LabelPosition("right") }
                }
            itemStyle = unsafeJso<ItemStyleOption> { color = ZRColor(country.color) }
        },
    )

private fun worldPopulationOption(): EChartsOption {
    val max = worldPopulationCountries.maxOf { it.value }
    return unsafeJso {
        backgroundColor = ZRColor("#404a59")
        title =
            one(
                unsafeJso<TitleOption> {
                    text = "World Population (2011)"
                    subtext = "From Gapminder"
                    left = StringOrDouble("center")
                    top = StringOrDouble("top")
                    textStyle = unsafeJso<TextStyleOption> { color = "#fff" }
                },
            )
        tooltip = one(populationTooltip())
        visualMap =
            one(
                unsafeJso<VisualMapOption> {
                    show = false
                    min = 0.0
                    this.max = max
                    inRange = unsafeJso<VisualOptionUnit> { symbolSize = DoubleOrDoubleArray(arrayOf(6.0, 60.0)) }
                },
            )
        geo =
            one(
                unsafeJso<GeoOption> {
                    map = "world"
                    roam = BooleanOrString(true)
                    emphasis =
                        unsafeJso<EmphasisOption> {
                            label = unsafeJso<LabelOption> { show = false }
                            itemStyle = unsafeJso<ItemStyleOption> { areaColor = ZRColor("#2a333d") }
                        }
                    itemStyle =
                        unsafeJso<ItemStyleOption> {
                            areaColor = ZRColor("#323c48")
                            borderColor = ZRColor("#111")
                        }
                },
            )
        series =
            arrayOf<SeriesOption>(
                scatterSeries {
                    coordinateSystem = "geo"
                    typedData =
                        dataItems(
                            *Array(worldPopulationCountries.size) { countryItem(worldPopulationCountries[it]) },
                        )
                },
            )
    }
}

val scatterWorldEntries: List<CatalogEntry> =
    listOf(
        scatterWorld.interactive(
            "scatter-world-population",
            "世界人口（2011）",
            "World Population (2011)",
            "169 个国家按人口数量映射气泡直径，落在可缩放平移的世界地图上。",
            "169 countries as bubbles sized by their 2011 population, on a roamable world map.",
        ) { chart ->
            loadJson("data/world.json").then { world ->
                ECharts.registerMap(
                    "world",
                    MapInput(unsafeJso<GeoJSONMapInput> { geoJSON = world.unsafeCast<GeoJSONSourceInput>() }),
                )
                chart.setOption(worldPopulationOption())
                null
            }
        },
    )
