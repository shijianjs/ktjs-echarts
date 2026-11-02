package demo.examples.bar

import demo.CatalogEntry
import demo.Groups
import echarts.*
import echarts.options.*
import js.objects.unsafeJso
import kotlin.js.unsafeCast
import demo.examples.Category
import demo.examples.shared.legendNames
import demo.examples.shared.row
import demo.examples.shared.textOf

private val barPolarRealEstateCategory = Category(Groups.BAR, "examples/bar/BarPolarRealEstate.kt")

val barPolarRealEstateEntry: CatalogEntry =
    barPolarRealEstateCategory.chart("bar-polar-real-estate", "极坐标系下的房租区间图", "Bar Chart on Polar") {
            unsafeJso<EChartsOption> {
                title =
                    one(
                        unsafeJso<TitleOption> {
                            text = "How expensive is it to rent an apartment in China?"
                            subtext = "Data from https://www.numbeo.com"
                        },
                    )
                legend =
                    one(
                        unsafeJso<LegendOption> {
                            show = true
                            top = StringOrDouble("bottom")
                            data = legendNames("Range", "Average")
                        },
                    )
                grid = one(unsafeJso<GridOption> { top = StringOrDouble(100.0) })
                angleAxis =
                    one(
                        unsafeJso<AngleAxisOption> {
                            type = AxisTypes.category
                            data = strings(*realEstateCities)
                        },
                    )
                tooltip =
                    one(
                        unsafeJso<TooltipOption> {
                            show = true
                            formatter =
                                TooltipFormatter { params ->
                                    val index = params.unsafeCast<CallbackDataParams>().dataIndex
                                    val row = realEstateRows[index]
                                    TooltipContent(
                                        realEstateCities[index] +
                                            "<br>Lowest：" + textOf(row[0]) +
                                            "<br>Highest：" + textOf(row[1]) +
                                            "<br>Average：" + textOf(row[2]),
                                    )
                                }
                        },
                    )
                radiusAxis = one(unsafeJso<RadiusAxisOption> {})
                polar = one(unsafeJso<PolarOption> {})
                series =
                    arrayOf<SeriesOption>(
                        realEstateSeries(null, { it[0] }, transparent = true, stack = "Min Max"),
                        realEstateSeries("Range", { it[1] - it[0] }, transparent = false, stack = "Min Max"),
                        realEstateSeries(
                            null,
                            { it[2] - REAL_ESTATE_BAR_HEIGHT },
                            transparent = true,
                            stack = "Average",
                            z = 10.0,
                        ),
                        realEstateSeries(
                            "Average",
                            { REAL_ESTATE_BAR_HEIGHT * 2 },
                            transparent = false,
                            stack = "Average",
                            barGap = "-100%",
                            z = 10.0,
                        ),
                    )
            }
        }
