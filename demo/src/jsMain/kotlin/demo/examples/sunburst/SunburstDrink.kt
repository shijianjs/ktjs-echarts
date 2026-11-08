package demo.examples.sunburst

import demo.CatalogEntry
import demo.Groups
import io.github.shijianjs.ktjs.echarts.options.DoubleOrDoubleArray
import io.github.shijianjs.ktjs.echarts.options.EChartsOption
import io.github.shijianjs.ktjs.echarts.options.ItemStyleOption
import io.github.shijianjs.ktjs.echarts.options.LabelPosition
import io.github.shijianjs.ktjs.echarts.options.LabelRotates
import io.github.shijianjs.ktjs.echarts.options.SeriesOption
import io.github.shijianjs.ktjs.echarts.options.StringOrDouble
import io.github.shijianjs.ktjs.echarts.options.SunburstSeriesLevelOption
import io.github.shijianjs.ktjs.echarts.options.TextStyleOption
import io.github.shijianjs.ktjs.echarts.options.TitleOption
import io.github.shijianjs.ktjs.echarts.options.one
import io.github.shijianjs.ktjs.echarts.options.sunburstSeries
import js.objects.unsafeJso
import kotlin.js.js
import web.cssom.TextAlign
import demo.examples.Category
import demo.examples.gauge.center

private val sunburstDrinkCategory = Category(Groups.SUNBURST, "examples/sunburst/SunburstDrink.kt")

val sunburstDrinkEntry: CatalogEntry =
    sunburstDrinkCategory.chart("sunburst-drink", "饮品风味分类", "Drink Flavors") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "WORLD COFFEE RESEARCH SENSORY LEXICON"
                            subtext = "Source: https://worldcoffeeresearch.org/work/sensory-lexicon/"
                            sublink = "https://worldcoffeeresearch.org/work/sensory-lexicon/"
                            textStyle =
                                unsafeJso<TextStyleOption> {
                                    fontSize = StringOrDouble(14.0)
                                    align = TextAlign.center
                                }
                            subtextStyle = unsafeJso<TextStyleOption> { align = TextAlign.center }
                        },
                    )
                series =
                    arrayOf<SeriesOption>(
                        sunburstSeries {
                            typedData = drinkData()
                            radius = radius(StringOrDouble(0.0), StringOrDouble("95%"))
                            emphasis = ancestorEmphasis()
                            levels =
                                arrayOf(
                                    unsafeJso(),
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble("15%")
                                        r = StringOrDouble("35%")
                                        itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 2.0 }
                                        label = unsafeJso { typedRotate = LabelRotates.tangential }
                                    },
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble("35%")
                                        r = StringOrDouble("70%")
                                        label = unsafeJso { align = TextAlign.right }
                                    },
                                    unsafeJso<SunburstSeriesLevelOption> {
                                        r0 = StringOrDouble("70%")
                                        r = StringOrDouble("72%")
                                        label =
                                            unsafeJso {
                                                position = LabelPosition("outside")
                                                padding = DoubleOrDoubleArray(3.0)
                                                silent = false
                                            }
                                        itemStyle = unsafeJso<ItemStyleOption> { borderWidth = 3.0 }
                                    },
                                )
                        },
                    )
            }
        }
