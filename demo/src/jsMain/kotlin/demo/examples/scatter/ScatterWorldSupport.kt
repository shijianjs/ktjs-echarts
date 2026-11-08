package demo.examples.scatter

import io.github.shijianjs.ktjs.echarts.*
import io.github.shijianjs.ktjs.echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import io.github.shijianjs.ktjs.zrender.ZRColor
import demo.examples.gauge.center
import demo.examples.gl.WorldCountry
import demo.examples.gl.worldPopulationCountries
import demo.examples.matrix.dimension
import demo.examples.shared.numeric

/** `1347565324` -> `"1,347,565,324"`. */
internal fun grouped(value: Double): String = value.toLong().toString().reversed().chunked(3).joinToString(",").reversed()

/**
 * `seriesName` / country / population.
 *
 * Upstream splits `params.value + ''` on the dot of the coordinates and ends up printing
 * `.undefined`; the third dimension is formatted directly instead.
 */
internal fun populationTooltip(): TooltipOption =
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

internal fun countryItem(country: WorldCountry): ScatterDataItem =
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

internal fun worldPopulationOption(): EChartsOption {
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
